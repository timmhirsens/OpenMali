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

import org.openmali.vecmath2.MatrixMxNf;

/**
 * LU Decomposition.
 * <p>
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n
 * unit lower triangular matrix L, an n-by-n upper triangular matrix U,
 * and a permutation vector piv of length m so that A(piv,:) = L*U.
 * If m < n, then L is m-by-m and U is m-by-n.
 * </p>
 * <p>
 * The LU decompostion with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail.  The primary use of the
 * LU decomposition is in the solution of square systems of simultaneous
 * linear equations.  This will fail if isNonsingular() returns false.
 * </p>
 * 
 * @author <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 */
public class LUDecomposition
{
    /**
     * Array for internal storage of decomposition.
     * 
     * @serial internal array storage.
     */
    private final MatrixMxNf LU;
    
    /**
     * Row and column dimensions, and pivot sign.
     * 
     * @serial column dimension.
     * @serial row dimension.
     * @serial pivot sign.
     */
    private int m, n, pivsign;
    
    /**
     * Internal storage of pivot vector.
     * @serial pivot vector.
     */
    private final int[] piv;
    
    /**
     * LU Decomposition.
     * 
     * @param  A   Rectangular matrix
     */
    public LUDecomposition( MatrixMxNf A )
    {
        
        // Use a "left-looking", dot-product, Crout/Doolittle algorithm.
        
        this.LU = new MatrixMxNf( A );
        this.m = A.getNumRows();
        this.n = A.getNumCols();
        this.piv = new int[ m ];
        
        for ( int i = 0; i < m; i++ )
        {
            piv[ i ] = i;
        }
        
        this.pivsign = 1;
        float[] LUrowi = new float[ n ];;
        float[] LUcolj = new float[ m ];
        
        // Outer loop.
        for ( int j = 0; j < n; j++ )
        {
            // Make a copy of the j-th column to localize references.
            LU.getColumn( j, LUcolj );
            
            // Apply previous transformations.
            for ( int i = 0; i < m; i++ )
            {
                LU.getRow( i, LUrowi );
                
                // Most of the time is spent in the following dot product.
                
                final int kmax = Math.min( i, j );
                float s = 0.0f;
                for ( int k = 0; k < kmax; k++ )
                {
                    s += LUrowi[ k ] * LUcolj[ k ];
                }
                
                LUrowi[ j ] = LUcolj[ i ] -= s;
            }
            
            // Find pivot and exchange if necessary.
            
            int p = j;
            for ( int i = j + 1; i < m; i++ )
            {
                if ( Math.abs( LUcolj[ i ] ) > Math.abs( LUcolj[ p ] ) )
                {
                    p = i;
                }
            }
            
            if ( p != j )
            {
                for ( int k = 0; k < n; k++ )
                {
                    float t = LU.get( p, k );
                    LU.set( p, k, LU.get( j, k ) );
                    LU.set( j, k, t );
                }
                
                final int k = piv[ p ];
                piv[ p ] = piv[ j ];
                piv[ j ] = k;
                pivsign = -pivsign;
            }
            
            // Compute multipliers.
            
            if ( j < m & LU.get( j, j ) != 0f )
            {
                for ( int i = j + 1; i < m; i++ )
                {
                    LU.div( i, j, LU.get( j, j ) );
                }
            }
        }
    }
    
    /* ------------------------
       Temporary, experimental code.
       ------------------------ *\

       \** LU Decomposition, computed by Gaussian elimination.
       <P>
       This constructor computes L and U with the "daxpy"-based elimination
       algorithm used in LINPACK and MATLAB.  In Java, we suspect the dot-product,
       Crout algorithm will be faster.  We have temporarily included this
       constructor until timing experiments confirm this suspicion.
       <P>
       @param  A             Rectangular matrix
       @param  linpackflag   Use Gaussian elimination.  Actual value ignored.
       @return               Structure to access L, U and piv.
       *\

       public LUDecomposition (Matrix A, int linpackflag) {
          // Initialize.
          LU = A.getArrayCopy();
          m = A.getRowDimension();
          n = A.getColumnDimension();
          piv = new int[m];
          for (int i = 0; i < m; i++) {
             piv[i] = i;
          }
          pivsign = 1;
          // Main loop.
          for (int k = 0; k < n; k++) {
             // Find pivot.
             int p = k;
             for (int i = k+1; i < m; i++) {
                if (Math.abs(LU[i][k]) > Math.abs(LU[p][k])) {
                   p = i;
                }
             }
             // Exchange if necessary.
             if (p != k) {
                for (int j = 0; j < n; j++) {
                   double t = LU[p][j]; LU[p][j] = LU[k][j]; LU[k][j] = t;
                }
                int t = piv[p]; piv[p] = piv[k]; piv[k] = t;
                pivsign = -pivsign;
             }
             // Compute multipliers and eliminate k-th column.
             if (LU[k][k] != 0.0) {
                for (int i = k+1; i < m; i++) {
                   LU[i][k] /= LU[k][k];
                   for (int j = k+1; j < n; j++) {
                      LU[i][j] -= LU[i][k]*LU[k][j];
                   }
                }
             }
          }
       }

    \* ------------------------
       End of temporary code.
     * ------------------------ */

    /* ------------------------
       Public Methods
     * ------------------------ */

    /**
     * Is the matrix nonsingular?
     * 
     * @return true if U, and hence A, is nonsingular.
     */
    public final boolean isNonsingular()
    {
        for ( int j = 0; j < n; j++ )
        {
            if ( LU.get( j, j ) == 0f )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * @return lower triangular factor.
     */
    public MatrixMxNf getL()
    {
        MatrixMxNf L = new MatrixMxNf( m, n );
        
        for ( int i = 0; i < m; i++ )
        {
            for ( int j = 0; j < n; j++ )
            {
                if ( i > j )
                {
                    L.set( i, j, LU.get( i, j ) );
                }
                else if ( i == j )
                {
                    L.set( i, j, 1.0f );
                }
                else
                {
                    L.set( i, j, 0.0f );
                }
            }
        }
        
        return ( L );
    }
    
    /**
     * @return upper triangular factor.
     */
    public MatrixMxNf getU()
    {
        MatrixMxNf U = new MatrixMxNf( n, n );
        
        for ( int i = 0; i < n; i++ )
        {
            for ( int j = 0; j < n; j++ )
            {
                if ( i <= j )
                {
                    U.set( i, j, LU.get( i, j ) );
                }
                else
                {
                    U.set( i, j, 0.0f );
                }
            }
        }
        
        return ( U );
    }
    
    /**
     * @return pivot permutation vector
     */
    public final int[] getPivot()
    {
        final int[] p = new int[ m ];
        
        System.arraycopy( this.piv, 0, p, 0, m );
        
        return ( p );
    }
    
    /**
     * @return     determinant(A)
     * @exception  IllegalArgumentException  Matrix must be square
     */
    public final float det()
    {
        if ( m != n )
        {
            throw new IllegalArgumentException( "Matrix must be square." );
        }
        
        float d = (float)pivsign;
        for ( int j = 0; j < n; j++ )
        {
            d *= LU.get( j, j );
        }
        
        return ( d );
    }
    
    //public Matrix getMatrix (int[] r, int j0, int j1) {
    private static MatrixMxNf copySubMatrix( MatrixMxNf A, int[] rows, int c0, int c1 )
    {
        MatrixMxNf B = new MatrixMxNf( rows.length, c1 - c0 + 1 );
        
        try
        {
           for ( int i = 0; i < rows.length; i++ )
           {
               for ( int j = c0; j <= c1; j++ )
               {
                   B.set( i, j - c0, A.get( rows[ i ], j ) );
               }
           }
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            throw new ArrayIndexOutOfBoundsException( "Submatrix indices" );
        }
        
        return ( B );
    }
    
    /**
     * Solves A * X = B.
     * 
     * @param  B   A Matrix with as many rows as A and any number of columns.
     * 
     * @return     X so that L*U*X = B(piv,:)
     * 
     * @exception  IllegalArgumentException Matrix row dimensions must agree.
     * @exception  RuntimeException  Matrix is singular.
     */
    public final MatrixMxNf solve( MatrixMxNf B )
    {
        if ( B.getNumRows() != m )
        {
            throw new IllegalArgumentException( "Matrix row dimensions must agree." );
        }
        if ( !this.isNonsingular() )
        {
            throw new RuntimeException( "Matrix is singular." );
        }
        
        // Copy right hand side with pivoting
        final int nx = B.getNumCols();
        final MatrixMxNf X = copySubMatrix( B, piv, 0, nx - 1 );
        
        // Solve L * Y = B(piv, :)
        for ( int k = 0; k < n; k++ )
        {
            for ( int i = k + 1; i < n; i++ )
            {
                for ( int j = 0; j < nx; j++ )
                {
                    X.sub( i, j, X.get( k, j ) * LU.get( i, k ) );
                }
            }
        }
        // Solve U * X = Y;
        for ( int k = n - 1; k >= 0; k-- )
        {
            for ( int j = 0; j < nx; j++ )
            {
                X.div( k, j, LU.get( k, k ) );
            }
            for ( int i = 0; i < k; i++ )
            {
                for ( int j = 0; j < nx; j++ )
                {
                    X.sub( i, j, X.get( k, j ) * LU.get( i, k ) );
                }
            }
        }
        
        return ( X );
    }
}
