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
 * Eigenvalues and eigenvectors of a real matrix. 
 * <p>
 * If A is symmetric, then A = V*D*V' where the eigenvalue matrix D is
 * diagonal and the eigenvector matrix V is orthogonal.
 * I.e. A = V.times(D.times(V.transpose())) and 
 * V.times(V.transpose()) equals the identity matrix.
 * </p>
 * <p>
 * If A is not symmetric, then the eigenvalue matrix D is block diagonal
 * with the real eigenvalues in 1-by-1 blocks and any complex eigenvalues,
 * lambda + i*mu, in 2-by-2 blocks, [lambda, mu; -mu, lambda].  The
 * columns of V represent the eigenvectors in the sense that A*V = V*D,
 * i.e. A.times(V) equals V.times(D).  The matrix V may be badly
 * conditioned, or even singular, so the validity of the equation
 * A = V*D*inverse(V) depends upon V.cond().
 * </p>
 * 
 * @author <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 */
public class EigenvalueDecomposition
{
    /**
     * Row and column dimension (square matrix).
     * 
     * @serial matrix dimension.
     */
    private final int n;
    
    /**
     * Symmetry flag.
     * 
     * @serial internal symmetry flag.
     */
    private final boolean isSymmetric;
    
    /**
     * Arrays for internal storage of eigenvalues.
     * 
     * @serial internal storage of eigenvalues.
     */
    private final float[] d, e;
    
    /**
     * Array for internal storage of eigenvectors.
     * 
     * @serial internal storage of eigenvectors.
     */
    private final MatrixMxNf V;
    
    /**
     * Array for internal storage of nonsymmetric Hessenberg form.
     * 
     * @serial internal storage of nonsymmetric Hessenberg form.
     */
    private final MatrixMxNf H;
    
    /**
     * Working storage for nonsymmetric algorithm.
     * 
     * @serial working storage for nonsymmetric algorithm.
     */
    private final float[] ort;
    
    /**
     * Symmetric Householder reduction to tridiagonal form.
     */
    private void tred2()
    {
        /*
         * This is derived from the Algol procedures tred2 by
         * Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
         * Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
         * Fortran subroutine in EISPACK.
         */
        
        for ( int j = 0; j < n; j++ )
        {
            d[ j ] = V.get( n - 1, j );
        }
        
        // Householder reduction to tridiagonal form.
        
        for ( int i = n - 1; i > 0; i-- )
        {
            // Scale to avoid under/overflow.
            
            float scale = 0.0f;
            float h = 0.0f;
            for ( int k = 0; k < i; k++ )
            {
                scale = scale + Math.abs( d[ k ] );
            }
            if ( scale == 0.0f )
            {
                e[ i ] = d[ i - 1 ];
                for ( int j = 0; j < i; j++ )
                {
                    d[ j ] = V.get( i - 1, j );
                    V.set( i, j, 0.0f );
                    V.set( j, i, 0.0f );
                }
            }
            else
            {
                // Generate Householder vector.
                
                for ( int k = 0; k < i; k++ )
                {
                    d[ k ] /= scale;
                    h += d[ k ] * d[ k ];
                }
                float f = d[ i - 1 ];
                float g = FastMath.sqrt( h );
                if ( f > 0 )
                {
                    g = -g;
                }
                e[ i ] = scale * g;
                h = h - f * g;
                d[ i - 1 ] = f - g;
                for ( int j = 0; j < i; j++ )
                {
                    e[ j ] = 0.0f;
                }
                
                // Apply similarity transformation to remaining columns.
                
                for ( int j = 0; j < i; j++ )
                {
                    f = d[ j ];
                    V.set( j, i, f );
                    g = e[ j ] + V.get( j, j ) * f;
                    for ( int k = j + 1; k <= i - 1; k++ )
                    {
                        g += V.get( k, j ) * d[ k ];
                        e[ k ] += V.get( k, j ) * f;
                    }
                    e[ j ] = g;
                }
                
                f = 0.0f;
                for ( int j = 0; j < i; j++ )
                {
                    e[ j ] /= h;
                    f += e[ j ] * d[ j ];
                }
                
                float hh = f / ( h + h );
                
                for ( int j = 0; j < i; j++ )
                {
                    e[ j ] -= hh * d[ j ];
                }
                
                for ( int j = 0; j < i; j++ )
                {
                    f = d[ j ];
                    g = e[ j ];
                    
                    for ( int k = j; k <= i - 1; k++ )
                    {
                        V.sub( k, j, f * e[ k ] + g * d[ k ] );
                    }
                    
                    d[ j ] = V.get( i - 1, j );
                    V.set( i, j, 0.0f );
                }
            }
            
            d[ i ] = h;
        }
        
        // Accumulate transformations.
        
        for ( int i = 0; i < n - 1; i++ )
        {
            V.set( n - 1, i, V.get( i, i ) );
            V.set( i, i, 1.0f );
            float h = d[ i + 1 ];
            
            if ( h != 0.0f )
            {
                for ( int k = 0; k <= i; k++ )
                {
                    d[ k ] = V.get( k, i + 1 ) / h;
                }
                
                for ( int j = 0; j <= i; j++ )
                {
                    float g = 0.0f;
                    for ( int k = 0; k <= i; k++ )
                    {
                        g += V.get( k, i + 1 ) * V.get( k, j );
                    }
                    
                    for ( int k = 0; k <= i; k++ )
                    {
                        V.sub( k, j, g * d[ k ] );
                    }
                }
            }
            
            for ( int k = 0; k <= i; k++ )
            {
                V.set( k, i + 1, 0.0f );
            }
        }
        
        for ( int j = 0; j < n; j++ )
        {
            d[ j ] = V.get( n - 1, j );
            V.set( n - 1, j, 0.0f );
        }
        
        V.set( n - 1, n - 1, 1.0f );
        e[ 0 ] = 0.0f;
    }
    
    /**
     * Symmetric tridiagonal QL algorithm.
     */
    private void tql2()
    {
        /*
         * This is derived from the Algol procedures tql2, by
         * Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
         * Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
         * Fortran subroutine in EISPACK.
         */
        
        for ( int i = 1; i < n; i++ )
        {
            e[ i - 1 ] = e[ i ];
        }
        e[ n - 1 ] = 0.0f;
        
        float f = 0.0f;
        float tst1 = 0.0f;
        float eps = FastMath.pow( 2.0f, -52.0f );
        
        for ( int l = 0; l < n; l++ )
        {
            // Find small subdiagonal element
            
            tst1 = Math.max( tst1, Math.abs( d[ l ] ) + Math.abs( e[ l ] ) );
            int m = l;
            while ( m < n )
            {
                if ( Math.abs( e[ m ] ) <= eps * tst1 )
                {
                    break;
                }
                m++;
            }
            
            // If m == l, d[l] is an eigenvalue, otherwise, iterate.
            
            if ( m > l )
            {
                int iter = 0;
                do
                {
                    iter = iter + 1; // (Could check iteration count here.)
                    
                    // Compute implicit shift
                    
                    float g = d[ l ];
                    float p = ( d[ l + 1 ] - g ) / ( 2.0f * e[ l ] );
                    float r = FastMath.hypot( p, 1.0f );
                    
                    if ( p < 0 )
                    {
                        r = -r;
                    }
                    
                    d[ l ] = e[ l ] / ( p + r );
                    d[ l + 1 ] = e[ l ] * ( p + r );
                    float dl1 = d[ l + 1 ];
                    float h = g - d[ l ];
                    for ( int i = l + 2; i < n; i++ )
                    {
                        d[ i ] -= h;
                    }
                    f = f + h;
                    
                    // Implicit QL transformation.
                    
                    p = d[ m ];
                    float c = 1.0f;
                    float c2 = c;
                    float c3 = c;
                    float el1 = e[ l + 1 ];
                    float s = 0.0f;
                    float s2 = 0.0f;
                    
                    for ( int i = m - 1; i >= l; i-- )
                    {
                        c3 = c2;
                        c2 = c;
                        s2 = s;
                        g = c * e[ i ];
                        h = c * p;
                        r = FastMath.hypot( p, e[ i ] );
                        e[ i + 1 ] = s * r;
                        s = e[ i ] / r;
                        c = p / r;
                        p = c * d[ i ] - s * g;
                        d[ i + 1 ] = h + s * ( c * g + s * d[ i ] );
                        
                        // Accumulate transformation.
                        
                        for ( int k = 0; k < n; k++ )
                        {
                            h = V.get( k, i + 1 );
                            V.set( k, i + 1, s * V.get( k, i ) + c * h );
                            V.set( k, i, c * V.get( k, i ) - s * h );
                        }
                    }
                    p = -s * s2 * c3 * el1 * e[ l ] / dl1;
                    e[ l ] = s * p;
                    d[ l ] = c * p;
                    
                    // Check for convergence.
                    
                }
                while ( Math.abs( e[ l ] ) > eps * tst1 );
            }
            d[ l ] = d[ l ] + f;
            e[ l ] = 0.0f;
        }
        
        // Sort eigenvalues and corresponding vectors.
        
        for ( int i = 0; i < n - 1; i++ )
        {
            int k = i;
            float p = d[ i ];
            for ( int j = i + 1; j < n; j++ )
            {
                if ( d[ j ] < p )
                {
                    k = j;
                    p = d[ j ];
                }
            }
            if ( k != i )
            {
                d[ k ] = d[ i ];
                d[ i ] = p;
                for ( int j = 0; j < n; j++ )
                {
                    p = V.get( j, i );
                    V.set( j, i, V.get( j, k ) );
                    V.set( j, k, p );
                }
            }
        }
    }
    
    /**
     * Nonsymmetric reduction to Hessenberg form.
     */
    private void orthes()
    {
        /*
         * This is derived from the Algol procedures orthes and ortran,
         * by Martin and Wilkinson, Handbook for Auto. Comp.,
         * Vol.ii-Linear Algebra, and the corresponding
         * Fortran subroutines in EISPACK.
         */
        
        final int low = 0;
        final int high = n - 1;
        
        for ( int m = low + 1; m <= high - 1; m++ )
        {
            // Scale column.
            
            float scale = 0.0f;
            for ( int i = m; i <= high; i++ )
            {
                scale = scale + Math.abs( H.get( i, m - 1 ) );
            }
            
            if ( scale != 0.0f )
            {
                // Compute Householder transformation.
                
                float h = 0.0f;
                for ( int i = high; i >= m; i-- )
                {
                    ort[ i ] = H.get( i, m - 1 ) / scale;
                    h += ort[ i ] * ort[ i ];
                }
                
                float g = FastMath.sqrt( h );
                if ( ort[ m ] > 0f )
                {
                    g = -g;
                }
                
                h = h - ort[ m ] * g;
                ort[ m ] = ort[ m ] - g;
                
                // Apply Householder similarity transformation
                // H = (I-u*u'/h)*H*(I-u*u')/h)
                
                for ( int j = m; j < n; j++ )
                {
                    float f = 0.0f;
                    for ( int i = high; i >= m; i-- )
                    {
                        f += ort[ i ] * H.get( i, j );
                    }
                    f = f / h;
                    for ( int i = m; i <= high; i++ )
                    {
                        H.sub( i, j, f * ort[ i ] );
                    }
                }
                
                for ( int i = 0; i <= high; i++ )
                {
                    float f = 0.0f;
                    for ( int j = high; j >= m; j-- )
                    {
                        f += ort[ j ] * H.get( i, j );
                    }
                    
                    f = f / h;
                    
                    for ( int j = m; j <= high; j++ )
                    {
                        H.sub( i, j, f * ort[ j ] );
                    }
                }
                
                ort[ m ] = scale * ort[ m ];
                H.set( m, m - 1, scale * g );
            }
        }
        
        // Accumulate transformations (Algol's ortran).
        
        for ( int i = 0; i < n; i++ )
        {
            for ( int j = 0; j < n; j++ )
            {
                V.set( i, j, ( i == j ? 1.0f : 0.0f ) );
            }
        }
        
        for ( int m = high - 1; m >= low + 1; m-- )
        {
            if ( H.get( m, m - 1 ) != 0.0f )
            {
                for ( int i = m + 1; i <= high; i++ )
                {
                    ort[ i ] = H.get( i, m - 1 );
                }
                for ( int j = m; j <= high; j++ )
                {
                    float g = 0.0f;
                    for ( int i = m; i <= high; i++ )
                    {
                        g += ort[ i ] * V.get( i, j );
                    }
                    
                    // Double division avoids possible underflow
                    g = ( g / ort[ m ] ) / H.get( m, m - 1 );
                    
                    for ( int i = m; i <= high; i++ )
                    {
                        V.add( i, j, g * ort[ i ] );
                    }
                }
            }
        }
    }
    
    // Complex scalar division.
    
    private transient float cdivr, cdivi;
    
    private void cdiv( float xr, float xi, float yr, float yi )
    {
        float r, d;
        if ( Math.abs( yr ) > Math.abs( yi ) )
        {
            r = yi / yr;
            d = yr + r * yi;
            cdivr = ( xr + r * xi ) / d;
            cdivi = ( xi - r * xr ) / d;
        }
        else
        {
            r = yr / yi;
            d = yi + r * yr;
            cdivr = ( r * xr + xi ) / d;
            cdivi = ( r * xi - xr ) / d;
        }
    }
    
    /**
     * Nonsymmetric reduction from Hessenberg to real Schur form.
     */
    private void hqr2()
    {
        /*
         * This is derived from the Algol procedure hqr2,
         * by Martin and Wilkinson, Handbook for Auto. Comp.,
         * Vol.ii-Linear Algebra, and the corresponding
         * Fortran subroutine in EISPACK.
         */
        
        // Initialize
        
        final int nn = this.n;
        int n = nn - 1;
        final int low = 0;
        final int high = nn - 1;
        float eps = FastMath.pow( 2.0f, -52.0f );
        float exshift = 0.0f;
        float p = 0, q = 0, r = 0, s = 0, z = 0, t, w, x, y;
        
        // Store roots isolated by balanc and compute matrix norm
        
        float norm = 0.0f;
        for ( int i = 0; i < nn; i++ )
        {
            if ( i < low | i > high )
            {
                d[ i ] = H.get( i, i );
                e[ i ] = 0.0f;
            }
            
            for ( int j = Math.max( i - 1, 0 ); j < nn; j++ )
            {
                norm = norm + Math.abs( H.get( i, j ) );
            }
        }
        
        // Outer loop over eigenvalue index
        
        int iter = 0;
        while ( n >= low )
        {
            // Look for single small sub-diagonal element
            
            int l = n;
            while ( l > low )
            {
                s = Math.abs( H.get( l - 1, l - 1 ) ) + Math.abs( H.get( l, l ) );
                if ( s == 0.0f )
                {
                    s = norm;
                }
                
                if ( Math.abs( H.get( l, l - 1 ) ) < eps * s )
                {
                    break;
                }
                
                l--;
            }
            
            // Check for convergence One root found
            
            if ( l == n )
            {
                H.set( n, n, H.get( n, n ) + exshift );
                d[ n ] = H.get( n, n );
                e[ n ] = 0.0f;
                n--;
                iter = 0;
                
                // Two roots found
            }
            else if ( l == n - 1 )
            {
                w = H.get( n, n - 1 ) * H.get( n - 1, n );
                p = ( H.get( n - 1, n - 1 ) - H.get( n, n ) ) / 2.0f;
                q = p * p + w;
                z = FastMath.sqrt( Math.abs( q ) );
                H.set( n, n, H.get( n, n ) + exshift );
                H.set( n - 1, n - 1, H.get( n - 1, n - 1 ) + exshift );
                x = H.get( n, n );
                
                // Real pair
                
                if ( q >= 0f )
                {
                    if ( p >= 0f )
                    {
                        z = p + z;
                    }
                    else
                    {
                        z = p - z;
                    }
                    d[ n - 1 ] = x + z;
                    d[ n ] = d[ n - 1 ];
                    if ( z != 0.0f )
                    {
                        d[ n ] = x - w / z;
                    }
                    e[ n - 1 ] = 0.0f;
                    e[ n ] = 0.0f;
                    x = H.get( n, n - 1 );
                    s = Math.abs( x ) + Math.abs( z );
                    p = x / s;
                    q = z / s;
                    r = FastMath.sqrt( p * p + q * q );
                    p = p / r;
                    q = q / r;
                    
                    // Row modification
                    
                    for ( int j = n - 1; j < nn; j++ )
                    {
                        z = H.get( n - 1, j );
                        H.set( n - 1, j, q * z + p * H.get( n, j ) );
                        H.set( n, j, q * H.get( n, j ) - p * z );
                    }
                    
                    // Column modification
                    
                    for ( int i = 0; i <= n; i++ )
                    {
                        z = H.get( i, n - 1 );
                        H.set( i, n - 1, q * z + p * H.get( i, n ) );
                        H.set( i, n, q * H.get( i, n ) - p * z );
                    }
                    
                    // Accumulate transformations
                    
                    for ( int i = low; i <= high; i++ )
                    {
                        z = V.get( i, n - 1 );
                        V.set( i, n - 1, q * z + p * V.get( i, n ) );
                        V.set( i, n, q * V.get( i, n ) - p * z );
                    }
                    
                    // Complex pair
                }
                else
                {
                    d[ n - 1 ] = x + p;
                    d[ n ] = x + p;
                    e[ n - 1 ] = z;
                    e[ n ] = -z;
                }
                n = n - 2;
                iter = 0;
                
                // No convergence yet
            }
            else
            {
                // Form shift
                
                x = H.get( n, n );
                y = 0.0f;
                w = 0.0f;
                if ( l < n )
                {
                    y = H.get( n - 1, n - 1 );
                    w = H.get( n, n - 1 ) * H.get( n - 1, n );
                }
                
                // Wilkinson's original ad hoc shift
                
                if ( iter == 10 )
                {
                    exshift += x;
                    for ( int i = low; i <= n; i++ )
                    {
                        H.sub( i, i, x );
                    }
                    s = Math.abs( H.get( n, n - 1 ) ) + Math.abs( H.get( n - 1, n - 2 ) );
                    x = y = 0.75f * s;
                    w = -0.4375f * s * s;
                }
                
                // MATLAB's new ad hoc shift
                
                if ( iter == 30 )
                {
                    s = ( y - x ) / 2.0f;
                    s = s * s + w;
                    if ( s > 0f )
                    {
                        s = FastMath.sqrt( s );
                        if ( y < x )
                        {
                            s = -s;
                        }
                        s = x - w / ( ( y - x ) / 2.0f + s );
                        for ( int i = low; i <= n; i++ )
                        {
                            H.sub( i, i, s );
                        }
                        exshift += s;
                        x = y = w = 0.964f;
                    }
                }
                
                iter = iter + 1; // (Could check iteration count here.)
                
                // Look for two consecutive small sub-diagonal elements
                
                int m = n - 2;
                while ( m >= l )
                {
                    z = H.get( m, m );
                    r = x - z;
                    s = y - z;
                    p = ( r * s - w ) / H.get( m + 1, m ) + H.get( m, m + 1 );
                    q = H.get( m + 1, m + 1 ) - z - r - s;
                    r = H.get( m + 2, m + 1 );
                    s = Math.abs( p ) + Math.abs( q ) + Math.abs( r );
                    p = p / s;
                    q = q / s;
                    r = r / s;
                    if ( m == l )
                    {
                        break;
                    }
                    if ( Math.abs( H.get( m, m - 1 ) ) * ( Math.abs( q ) + Math.abs( r ) ) < eps * ( Math.abs( p ) * ( Math.abs( H.get( m - 1, m - 1 ) ) + Math.abs( z ) + Math.abs( H.get( m + 1, m + 1 ) ) ) ) )
                    {
                        break;
                    }
                    
                    m--;
                }
                
                for ( int i = m + 2; i <= n; i++ )
                {
                    H.set( i, i - 2, 0.0f );
                    if ( i > m + 2 )
                    {
                        H.set( i, i - 3, 0.0f );
                    }
                }
                
                // Double QR step involving rows l:n and columns m:n
                
                for ( int k = m; k <= n - 1; k++ )
                {
                    final boolean notlast = ( k != n - 1 );
                    if ( k != m )
                    {
                        p = H.get( k, k - 1 );
                        q = H.get( k + 1, k - 1 );
                        r = ( notlast ? H.get( k + 2, k - 1 ) : 0.0f );
                        x = Math.abs( p ) + Math.abs( q ) + Math.abs( r );
                        if ( x != 0.0f )
                        {
                            p = p / x;
                            q = q / x;
                            r = r / x;
                        }
                    }
                    if ( x == 0.0f )
                    {
                        break;
                    }
                    s = FastMath.sqrt( p * p + q * q + r * r );
                    if ( p < 0f )
                    {
                        s = -s;
                    }
                    if ( s != 0f )
                    {
                        if ( k != m )
                        {
                            H.set( k, k - 1, -s * x );
                        }
                        else if ( l != m )
                        {
                            H.set( k, k - 1, -H.get( k, k - 1 ) );
                        }
                        p = p + s;
                        x = p / s;
                        y = q / s;
                        z = r / s;
                        q = q / p;
                        r = r / p;
                        
                        // Row modification
                        
                        for ( int j = k; j < nn; j++ )
                        {
                            p = H.get( k, j ) + q * H.get( k + 1, j );
                            if ( notlast )
                            {
                                p = p + r * H.get( k + 2, j );
                                H.set( k + 2, j, H.get( k + 2, j ) - p * z );
                            }
                            H.set( k, j, H.get( k, j ) - p * x );
                            H.set( k + 1, j, H.get( k + 1, j ) - p * y );
                        }
                        
                        // Column modification
                        
                        for ( int i = 0; i <= Math.min( n, k + 3 ); i++ )
                        {
                            p = x * H.get( i, k ) + y * H.get( i, k + 1 );
                            if ( notlast )
                            {
                                p = p + z * H.get( i, k + 2 );
                                H.set( i, k + 2, H.get( i, k + 2 ) - p * r );
                            }
                            H.set( i, k, H.get( i, k ) - p );
                            H.set( i, k + 1, H.get( i, k + 1 ) - p * q );
                        }
                        
                        // Accumulate transformations
                        
                        for ( int i = low; i <= high; i++ )
                        {
                            p = x * V.get( i, k ) + y * V.get( i, k + 1 );
                            if ( notlast )
                            {
                                p = p + z * V.get( i, k + 2 );
                                V.set( i, k + 2, V.get( i, k + 2 ) - p * r );
                            }
                            V.set( i, k, V.get( i, k ) - p );
                            V.set( i, k + 1, V.get( i, k + 1 ) - p * q );
                        }
                    } // (s != 0)
                } // k loop
            } // check convergence
        } // while (n >= low)
        
        // Backsubstitute to find vectors of upper triangular form
        
        if ( norm == 0.0f )
        {
            return;
        }
        
        for ( n = nn - 1; n >= 0; n-- )
        {
            p = d[ n ];
            q = e[ n ];
            
            // Real vector
            
            if ( q == 0f )
            {
                int l = n;
                H.set( n, n, 1.0f );
                
                for ( int i = n - 1; i >= 0; i-- )
                {
                    w = H.get( i, i ) - p;
                    r = 0.0f;
                    for ( int j = l; j <= n; j++ )
                    {
                        r = r + H.get( i, j ) * H.get( j, n );
                    }
                    
                    if ( e[ i ] < 0.0f )
                    {
                        z = w;
                        s = r;
                    }
                    else
                    {
                        l = i;
                        if ( e[ i ] == 0.0f )
                        {
                            if ( w != 0.0f )
                            {
                                H.set( i, n, -r / w );
                            }
                            else
                            {
                                H.set( i, n, -r / ( eps * norm ) );
                            }
                        }
                        else
                        {
                            // Solve real equations
                            
                            x = H.get( i, i + 1 );
                            y = H.get( i + 1, i );
                            q = ( d[ i ] - p ) * ( d[ i ] - p ) + e[ i ] * e[ i ];
                            t = ( x * s - z * r ) / q;
                            H.set( i, n, t );
                            
                            if ( Math.abs( x ) > Math.abs( z ) )
                            {
                                H.set( i + 1, n, ( -r - w * t ) / x );
                            }
                            else
                            {
                                H.set( i + 1, n, ( -s - y * t ) / z );
                            }
                        }
                        
                        // Overflow control
                        
                        t = Math.abs( H.get( i, n ) );
                        if ( ( eps * t ) * t > 1 )
                        {
                            for ( int j = i; j <= n; j++ )
                            {
                                H.set( j, n, H.get( j, n ) / t );
                            }
                        }
                    }
                }
                
                // Complex vector
                
            }
            else if ( q < 0f )
            {
                int l = n - 1;
                
                // Last vector component imaginary so matrix is triangular
                
                if ( Math.abs( H.get( n, n - 1 ) ) > Math.abs( H.get( n - 1, n ) ) )
                {
                    H.set( n - 1, n - 1, q / H.get( n, n - 1 ) );
                    H.set( n - 1, n, -( H.get( n, n ) - p ) / H.get( n, n - 1 ) );
                }
                else
                {
                    cdiv( 0.0f, -H.get( n - 1, n ), H.get( n - 1, n - 1 ) - p, q );
                    H.set( n - 1, n - 1, cdivr );
                    H.set( n - 1, n, cdivi );
                }
                H.set( n, n - 1, 0.0f );
                H.set( n, n, 1.0f );
                for ( int i = n - 2; i >= 0; i-- )
                {
                    float ra, sa, vr, vi;
                    ra = 0.0f;
                    sa = 0.0f;
                    for ( int j = l; j <= n; j++ )
                    {
                        ra = ra + H.get( i, j ) * H.get( j, n - 1 );
                        sa = sa + H.get( i, j ) * H.get( j, n );
                    }
                    w = H.get( i, i ) - p;
                    
                    if ( e[ i ] < 0.0f )
                    {
                        z = w;
                        r = ra;
                        s = sa;
                    }
                    else
                    {
                        l = i;
                        if ( e[ i ] == 0f )
                        {
                            cdiv( -ra, -sa, w, q );
                            H.set( i, n - 1, cdivr );
                            H.set( i, n, cdivi );
                        }
                        else
                        {
                            // Solve complex equations
                            
                            x = H.get( i, i + 1 );
                            y = H.get( i + 1, i );
                            vr = ( d[ i ] - p ) * ( d[ i ] - p ) + e[ i ] * e[ i ] - q * q;
                            vi = ( d[ i ] - p ) * 2.0f * q;
                            if ( vr == 0.0f & vi == 0.0f )
                            {
                                vr = eps * norm * ( Math.abs( w ) + Math.abs( q ) + Math.abs( x ) + Math.abs( y ) + Math.abs( z ) );
                            }
                            cdiv( x * r - z * ra + q * sa, x * s - z * sa - q * ra, vr, vi );
                            H.set( i, n - 1, cdivr );
                            H.set( i, n, cdivi );
                            if ( Math.abs( x ) > ( Math.abs( z ) + Math.abs( q ) ) )
                            {
                                H.set( i + 1, n - 1, ( -ra - w * H.get( i, n - 1 ) + q * H.get( i, n ) ) / x );
                                H.set( i + 1, n, ( -sa - w * H.get( i, n ) - q * H.get( i, n - 1 ) ) / x );
                            }
                            else
                            {
                                cdiv( -r - y * H.get( i, n - 1 ), -s - y * H.get( i, n ), z, q );
                                H.set( i + 1, n - 1, cdivr );
                                H.set( i + 1, n, cdivi );
                            }
                        }
                        
                        // Overflow control
                        
                        t = Math.max( Math.abs( H.get( i, n - 1 ) ), Math.abs( H.get( i, n ) ) );
                        if ( ( eps * t ) * t > 1 )
                        {
                            for ( int j = i; j <= n; j++ )
                            {
                                H.set( j, n - 1, H.get( j, n - 1 ) / t );
                                H.set( j, n, H.get( j, n ) / t );
                            }
                        }
                    }
                }
            }
        }
        
        // Vectors of isolated roots
        
        for ( int i = 0; i < nn; i++ )
        {
            if ( i < low | i > high )
            {
                for ( int j = i; j < nn; j++ )
                {
                    V.set( i, j, H.get( i, j ) );
                }
            }
        }
        
        // Back transformation to get eigenvectors of original matrix
        
        for ( int j = nn - 1; j >= low; j-- )
        {
            for ( int i = low; i <= high; i++ )
            {
                z = 0.0f;
                for ( int k = low; k <= Math.min( j, high ); k++ )
                {
                    z = z + V.get( i, k ) * H.get( k, j );
                }
                V.set( i, j, z );
            }
        }
    }
    
    /* ------------------------
       Constructor
     * ------------------------ */

    /**
     * Check for symmetry, then construct the eigenvalue decomposition
     * 
     * @param A    Square matrix
     */
    public EigenvalueDecomposition( MatrixMxNf A )
    {
        n = A.getNumCols();
        this.V = new MatrixMxNf( n, n );
        this.d = new float[ n ];
        this.e = new float[ n ];
        
        boolean isSym = true;
        for ( int j = 0; ( j < n ) & isSym; j++ )
        {
            for ( int i = 0; ( i < n ) & isSym; i++ )
            {
                isSym = ( A.get( i, j ) == A.get( j, i ) );
            }
        }
        
        this.isSymmetric = isSym;
        
        if ( isSymmetric )
        {
            for ( int i = 0; i < n; i++ )
            {
                for ( int j = 0; j < n; j++ )
                {
                    V.set( i, j, A.get( i, j ) );
                }
            }
            
            // Tridiagonalize.
            tred2();
            
            // Diagonalize.
            tql2();
            
            // TODO: Check, if this is allowed!
            this.H = null;
            this.ort = null;
        }
        else
        {
            this.H = new MatrixMxNf( n, n );
            this.ort = new float[ n ];
            
            for ( int j = 0; j < n; j++ )
            {
                for ( int i = 0; i < n; i++ )
                {
                    H.set( i, j, A.get( i, j ) );
                }
            }
            
            // Reduce to Hessenberg form.
            orthes();
            
            // Reduce Hessenberg to real Schur form.
            hqr2();
        }
    }
    
    /* ------------------------
       Public Methods
     * ------------------------ */

    /** 
     * @return the eigenvector matrix
     */
    public MatrixMxNf getV()
    {
        MatrixMxNf result = new MatrixMxNf( n, n );
        result.set( V );
        
        return ( result );
    }
    
    /**
     * Returns the real parts of the eigenvalues
     * @return     real(diag(D))
     */
    public float[] getRealEigenvalues()
    {
        return ( d );
    }
    
    /**
     * Return the imaginary parts of the eigenvalues
     * @return     imag(diag(D))
     */
    public float[] getImagEigenvalues()
    {
        return ( e );
    }
    
    /**
     * @return the block diagonal eigenvalue matrix
     */
    public MatrixMxNf getD()
    {
        MatrixMxNf D = new MatrixMxNf( n, n );
        
        for ( int i = 0; i < n; i++ )
        {
            for ( int j = 0; j < n; j++ )
            {
                D.set( i, j, 0.0f );
            }
            
            D.set( i, i, d[ i ] );
            
            if ( e[ i ] > 0f )
            {
                D.set( i, i + 1, e[ i ] );
            }
            else if ( e[ i ] < 0f )
            {
                D.set( i, i - 1, e[ i ] );
            }
        }
        
        return D;
    }
}
