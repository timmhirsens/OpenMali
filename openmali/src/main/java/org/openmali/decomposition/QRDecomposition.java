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
 * QR Decomposition.
 * <p>
 * For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n
 * orthogonal matrix Q and an n-by-n upper triangular matrix R so that
 * A = Q*R.
 * </p>
 * <p>
 * The QR decompostion always exists, even if the matrix does not have
 * full rank, so the constructor will never fail.  The primary use of the
 * QR decomposition is in the least squares solution of nonsquare systems
 * of simultaneous linear equations.  This will fail if isFullRank()
 * returns false.
 * </p>
 * 
 * @author <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 */
public class QRDecomposition
{
    /**
     * Array for internal storage of decomposition.
     * 
     * @serial internal array storage.
     */
    private final MatrixMxNf QR;
    
    /**
     * Row and column dimensions.
     * 
     * @serial column dimension.
     * @serial row dimension.
    */
    private final int m, n;
    
    /** Array for internal storage of diagonal of R.
     *@serial diagonal of R.
     */
    private final float[] Rdiag;
    
    /**
     * QR Decomposition, computed by Householder reflections.
     * 
     * @param A Rectangular matrix
     */
    public QRDecomposition( MatrixMxNf A )
    {
        // Initialize.
        this.QR = new MatrixMxNf( A );
        this.m = A.getNumRows();
        this.n = A.getNumCols();
        this.Rdiag = new float[ n ];
        
        // Main loop.
        for ( int k = 0; k < n; k++ )
        {
            // Compute 2-norm of k-th column without under/overflow.
            float nrm = 0f;
            for ( int i = k; i < m; i++ )
            {
                nrm = FastMath.hypot( nrm, QR.get( i, k ) );
            }
            
            if ( nrm != 0f )
            {
                // Form k-th Householder vector.
                if ( QR.get( k, k ) < 0f )
                {
                    nrm = -nrm;
                }
                for ( int i = k; i < m; i++ )
                {
                    QR.div( i, k, nrm );
                }
                QR.add( k, k, 1.0f );
                
                // Apply transformation to remaining columns.
                for ( int j = k + 1; j < n; j++ )
                {
                    float s = 0.0f;
                    for ( int i = k; i < m; i++ )
                    {
                        s += QR.get( i, k ) * QR.get( i, j );
                    }
                    s = -s / QR.get( k, k );
                    for ( int i = k; i < m; i++ )
                    {
                        QR.add( i, j, s * QR.get( i, k ) );
                    }
                }
            }
            
            Rdiag[ k ] = -nrm;
        }
    }
    
    /**
     * Is the matrix full rank?
     * 
     * @return true if R, and hence A, has full rank.
     */
    public final boolean isFullRank()
    {
        for ( int j = 0; j < n; j++ )
        {
            if ( Rdiag[ j ] == 0f )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * Returns the Householder vectors.
     * 
     * @return Lower trapezoidal matrix whose columns define the reflections
     */
    public MatrixMxNf getH()
    {
        MatrixMxNf H = new MatrixMxNf( m, n );
        
        for ( int i = 0; i < m; i++ )
        {
            for ( int j = 0; j < n; j++ )
            {
                if ( i >= j )
                {
                    H.set( i, j, QR.get( i, j ) );
                }
                else
                {
                    H.set( i, j, 0f );
                }
            }
        }
        
        return ( H );
    }
    
    /**
     * @return the upper triangular factor.
     */
    public MatrixMxNf getR()
    {
        MatrixMxNf R = new MatrixMxNf( n, n );
        
        for ( int i = 0; i < n; i++ )
        {
            for ( int j = 0; j < n; j++ )
            {
                if ( i < j )
                {
                    R.set( i, j, QR.get( i, j ) );
                }
                else if ( i == j )
                {
                    R.set( i, j, Rdiag[ i ] );
                }
                else
                {
                    R.set( i, j, 0f );
                }
            }
        }
        
        return ( R );
    }
    
    /**
     * 
     * @return Generates and returns the (economy-sized) orthogonal factor.
     */
    public MatrixMxNf getQ()
    {
        MatrixMxNf Q = new MatrixMxNf( m, n );
        
        for ( int k = n - 1; k >= 0; k-- )
        {
            for ( int i = 0; i < m; i++ )
            {
                Q.set( i, k, 0f );
            }
            
            Q.set( k, k, 1.0f );
            
            for ( int j = k; j < n; j++ )
            {
                if ( QR.get( k, k ) != 0f )
                {
                    float s = 0.0f;
                    for ( int i = k; i < m; i++ )
                    {
                        s += QR.get( i, k ) * Q.get( i, j );
                    }
                    
                    s = -s / QR.get( k, k );
                    
                    for ( int i = k; i < m; i++ )
                    {
                        Q.add( i, j, s * QR.get( i, k ) );
                    }
                }
            }
        }
        
        return ( Q );
    }
    
    /**
     * Least squares solution of A * X = B.
     * 
     * @param B    A Matrix with as many rows as A and any number of columns.
     * 
     * @return     X that minimizes the two norm of Q*R*X-B.
     * 
     * @exception  IllegalArgumentException  Matrix row dimensions must agree.
     * @exception  RuntimeException  Matrix is rank deficient.
     */
    public final MatrixMxNf solve( MatrixMxNf B, MatrixMxNf result )
    {
        if ( B.getNumRows() != m )
        {
            throw new IllegalArgumentException( "Matrix row dimensions must agree." );
        }
        if ( !this.isFullRank() )
        {
            throw new RuntimeException( "Matrix is rank deficient." );
        }
        
        // Copy right hand side
        final int nx = B.getNumCols();
        final MatrixMxNf X = new MatrixMxNf( B );
        
        // Compute Y = transpose(Q) * B
        for ( int k = 0; k < n; k++ )
        {
            for ( int j = 0; j < nx; j++ )
            {
                float s = 0.0f;
                for ( int i = k; i < m; i++ )
                {
                    s += QR.get( i, k ) * X.get( i, j );
                }
                
                s = -s / QR.get( k, k );
                
                for ( int i = k; i < m; i++ )
                {
                    X.add( i, j, s * QR.get( i, k ) );
                }
            }
        }
        
        // Solve R * X = Y;
        for ( int k = n - 1; k >= 0; k-- )
        {
            for ( int j = 0; j < nx; j++ )
            {
                X.div( k, j, Rdiag[ k ] );
            }
            
            for ( int i = 0; i < k; i++ )
            {
                for ( int j = 0; j < nx; j++ )
                {
                    X.sub( i, j, X.get( k, j ) * QR.get( i, k ) );
                }
            }
        }
        
        //return ( new MatrixMxNf( X, n, nx ).getMatrix( 0, n - 1, 0, nx - 1 ) );
        
        MatrixMxNf result2 = X.getSharedSubMatrix( 0, 0, n - 1, nx - 1 );
        if ( result != null )
            result.set( result2 );
        
        return ( result2 );
    }
}
