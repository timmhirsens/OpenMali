/**
 * Copyright (c) 2007-2009, OpenMaLi Project Group all rights reserved.
 * 
 * Portions based on the Sun's javax.vecmath interface, Copyright by Sun
 * Microsystems or Kenji Hiranabe's alternative GC-cheap implementation.
 * Many thanks to the developers.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of the 'OpenMaLi Project Group' nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 */
package org.openmali.decomposition;

import org.openmali.FastMath;
import org.openmali.vecmath2.MatrixMxNf;

/**
 * Cholesky Decomposition.
 * <p>
 * For a symmetric, positive definite matrix A, the Cholesky decomposition
 * is an lower triangular matrix L so that A = L * L'.
 * </p>
 * <p>
 * If the matrix is not symmetric or positive definite, the constructor
 * returns a partial decomposition and sets an internal flag that may
 * be queried by the isSPD() method.
 * </p>
 * 
 * @author <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 */
public class CholeskyDecomposition
{
    /**
     * Array for internal storage of decomposition.
     * @serial internal array storage.
     */
    private final MatrixMxNf L;
    
    /**
     * Row and column dimension (square matrix).
     * @serial matrix dimension.
     */
    private final int n;
    
    /**
     * Symmetric and positive definite flag.
     * @serial is symmetric and positive definite flag.
     */
    private boolean isSPD;
    
    /**
     * Cholesky algorithm for symmetric and positive definite matrix.
     * @param M Square, symmetric matrix.
     * return Structure to access L and isspd flag.
     */
    public CholeskyDecomposition( MatrixMxNf M )
    {
        // Initialize.
        this.n = M.getNumRows();
        this.L = new MatrixMxNf( n, n );
        this.isSPD = ( M.getNumCols() == n );
        
        // Main loop.
        for ( int j = 0; j < n; j++ )
        {
            float d = 0.0f;
            for ( int k = 0; k < j; k++ )
            {
                float s = 0.0f;
                for ( int i = 0; i < k; i++ )
                {
                    s += L.get( k, i ) * L.get( j, i );
                }
                
                s = ( M.get( j, k ) - s ) / L.get( k, k );
                L.set( j, k, s );
                d = d + s * s;
                isSPD = isSPD & ( M.get( k, j ) == M.get( j, k ) );
            }
            
            d = M.get( j, j ) - d;
            isSPD = isSPD & ( d > 0.0f );
            L.set( j, j, FastMath.sqrt( Math.max( d, 0.0f ) ) );
            
            for ( int k = j + 1; k < n; k++ )
            {
                L.set( j, k, 0f );
            }
        }
    }
    
    /* ------------------------
       Temporary, experimental code.
     * ------------------------ *\

       \** Right Triangular Cholesky Decomposition.
       <P>
       For a symmetric, positive definite matrix A, the Right Cholesky
       decomposition is an upper triangular matrix R so that A = R'*R.
       This constructor computes R with the Fortran inspired column oriented
       algorithm used in LINPACK and MATLAB.  In Java, we suspect a row oriented,
       lower triangular decomposition is faster.  We have temporarily included
       this constructor here until timing experiments confirm this suspicion.
       *\

       \** Array for internal storage of right triangular decomposition. **\
       private transient double[][] R;

       \** Cholesky algorithm for symmetric and positive definite matrix.
       @param  A           Square, symmetric matrix.
       @param  rightflag   Actual value ignored.
       @return             Structure to access R and isspd flag.
       *\

       public CholeskyDecomposition (Matrix Arg, int rightflag) {
          // Initialize.
          double[][] A = Arg.getArray();
          n = Arg.getColumnDimension();
          R = new double[n][n];
          isspd = (Arg.getColumnDimension() == n);
          // Main loop.
          for (int j = 0; j < n; j++) {
             double d = 0.0;
             for (int k = 0; k < j; k++) {
                double s = A[k][j];
                for (int i = 0; i < k; i++) {
                   s = s - R[i][k]*R[i][j];
                }
                R[k][j] = s = s/R[k][k];
                d = d + s*s;
                isspd = isspd & (A[k][j] == A[j][k]); 
             }
             d = A[j][j] - d;
             isspd = isspd & (d > 0.0);
             R[j][j] = Math.sqrt(Math.max(d,0.0));
             for (int k = j+1; k < n; k++) {
                R[k][j] = 0.0;
             }
          }
       }

       \** Return upper triangular factor.
       @return     R
       *\

       public Matrix getR () {
          return new Matrix(R,n,n);
       }
    
    \* ------------------------
       End of temporary code.
     * ------------------------ */
    
    /* ------------------------
       Public Methods
     * ------------------------ */
    
    /**
     * Is the matrix symmetric and positive definite?
     * @return true if A is symmetric and positive definite.
     */
    public final boolean isSPD()
    {
        return ( isSPD );
    }
    
    /**
     * Return triangular factor.
     * @return L
     */
    public MatrixMxNf getL()
    {
        return ( L );
    }
    
    /**
     * Solves A * X = B.
     * 
     * @param B A Matrix with as many rows as A and any number of columns.
     * @param result so that L*L'*X = B
     * 
     * @exception IllegalArgumentException Matrix row dimensions must agree.
     * @exception RuntimeException Matrix is not symmetric positive definite.
     */
    public final void solve( MatrixMxNf B, MatrixMxNf result )
    {
        if ( B.getNumRows() != n )
        {
            throw new IllegalArgumentException( "Matrix row dimensions must agree." );
        }
        if ( !isSPD )
        {
            throw new RuntimeException( "Matrix is not symmetric positive definite." );
        }
        
        final int nx = B.getNumCols();
        
        if ( ( result.getNumRows() != n ) || ( result.getNumCols() != nx ) )
        {
            throw new IllegalArgumentException( "Result Matrix does not match required dimensions (" + n + " x " + nx + ")." );
        }
        
        // Copy right hand side.
        result.set( B );
        
        // Solve L * Y = B;
        for ( int k = 0; k < n; k++ )
        {
            for ( int j = 0; j < nx; j++ )
            {
                for ( int i = 0; i < k; i++ )
                {
                    result.sub( k, j, result.get( i, j ) * L.get( k, i ) );
                }
                
                result.div( k, j, L.get( k, k ) );
            }
        }
        
        // Solve L' * X = Y;
        for ( int k = n - 1; k >= 0; k-- )
        {
            for ( int j = 0; j < nx; j++ )
            {
                for ( int i = k + 1; i < n; i++ )
                {
                    result.sub( k, j, result.get( i, j ) * L.get( i, k ) );
                }
                
                result.div( k, j, L.get( k, k ) );
            }
        }
    }
}
