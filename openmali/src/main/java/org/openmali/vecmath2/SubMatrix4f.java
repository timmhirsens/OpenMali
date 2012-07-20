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
package org.openmali.vecmath2;

/**
 * This class simply overrides some methods in Matrix4f, that are faster there,
 * but are therefore not compatible with a sub-matrix.
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Arne Mueller
 */
class SubMatrix4f extends Matrix4f
{
    private static final long serialVersionUID = -9193368404497123017L;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void set( Matrix3f mat )
    {
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        // This implementation is in 'no automatic size grow' policy.
        // When size mismatch, exception will be thrown from the below.
        this.values[ roTrick + dataBegin + 0 ] = mat.values[ 0 ];
        this.values[ roTrick + dataBegin + 1 ] = mat.values[ 1 ];
        this.values[ roTrick + dataBegin + 2 ] = mat.values[ 2 ];
        this.values[ roTrick + dataBegin + 3 ] = 0f;
        this.values[ roTrick + dataBegin + colSkip + 0 ] = mat.values[ 3 ];
        this.values[ roTrick + dataBegin + colSkip + 1 ] = mat.values[ 4 ];
        this.values[ roTrick + dataBegin + colSkip + 2 ] = mat.values[ 5 ];
        this.values[ roTrick + dataBegin + colSkip + 3 ] = 0f;
        this.values[ roTrick + dataBegin + 2 * colSkip + 0 ] = mat.values[ 6 ];
        this.values[ roTrick + dataBegin + 2 * colSkip + 1 ] = mat.values[ 7 ];
        this.values[ roTrick + dataBegin + 2 * colSkip + 2 ] = mat.values[ 8 ];
        this.values[ roTrick + dataBegin + 2 * colSkip + 3 ] = 0f;
        this.values[ roTrick + dataBegin + 3 * colSkip + 0 ] = 0f;
        this.values[ roTrick + dataBegin + 3 * colSkip + 1 ] = 0f;
        this.values[ roTrick + dataBegin + 3 * colSkip + 2 ] = 0f;
        this.values[ roTrick + dataBegin + 3 * colSkip + 3 ] = 1f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public java.nio.FloatBuffer writeToBuffer( java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.put( m00() );
        buffer.put( m10() );
        buffer.put( m20() );
        buffer.put( m30() );
        
        buffer.put( m01() );
        buffer.put( m11() );
        buffer.put( m21() );
        buffer.put( m31() );
        
        buffer.put( m02() );
        buffer.put( m12() );
        buffer.put( m22() );
        buffer.put( m32() );
        
        buffer.put( m03() );
        buffer.put( m13() );
        buffer.put( m23() );
        buffer.put( m33() );
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public java.nio.FloatBuffer writeToBuffer( java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        buffer.put( m00() );
        buffer.put( m10() );
        buffer.put( m20() );
        buffer.put( m30() );
        
        buffer.put( m01() );
        buffer.put( m11() );
        buffer.put( m21() );
        buffer.put( m31() );
        
        buffer.put( m02() );
        buffer.put( m12() );
        buffer.put( m22() );
        buffer.put( m32() );
        
        buffer.put( m03() );
        buffer.put( m13() );
        buffer.put( m23() );
        buffer.put( m33() );
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer )
    {
        m00( buffer.get() );
        m10( buffer.get() );
        m20( buffer.get() );
        m30( buffer.get() );
        
        m01( buffer.get() );
        m11( buffer.get() );
        m21( buffer.get() );
        m31( buffer.get() );
        
        m02( buffer.get() );
        m12( buffer.get() );
        m22( buffer.get() );
        m32( buffer.get() );
        
        m03( buffer.get() );
        m13( buffer.get() );
        m23( buffer.get() );
        m33( buffer.get() );
        
        return ( buffer );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        m00( buffer.get() );
        m10( buffer.get() );
        m20( buffer.get() );
        m30( buffer.get() );
        
        m01( buffer.get() );
        m11( buffer.get() );
        m21( buffer.get() );
        m31( buffer.get() );
        
        m02( buffer.get() );
        m12( buffer.get() );
        m22( buffer.get() );
        m32( buffer.get() );
        
        m03( buffer.get() );
        m13( buffer.get() );
        m23( buffer.get() );
        m33( buffer.get() );
        
        return ( buffer );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SubMatrix4f mul( float scalar )
    {
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                values[ roTrick + dataBegin + r * colSkip + c ] *= scalar;
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void interpolate( Matrix4f m1, Matrix4f m2, float alpha, boolean interpolateLastLine )
    {
        final int nr = interpolateLastLine ? getNumRows() : getNumRows() - 1;
        
        for ( int r = 0; r < nr; r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                this.set( r, c, m1.get( r, c ) + ( ( m2.get( r, c ) - m1.get( r, c ) ) * alpha ) );
            }
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SubMatrix4f invert()
    {
        float d = determinant();
        if ( d == 0.0f )
            return ( this );
        d = 1.0f / d;
        
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        // alias-safe way.
        // less *,+,- calculation than expanded expression.
        /*
        set( m11() * (m22() * m33() - m23() * m32()) + m12() * (m23() * m31() - m21() * m33()) + m13() * (m21() * m32() - m22() * m31()),
             m21() * (m02() * m33() - m03() * m32()) + m22() * (m03() * m31() - m01() * m33()) + m23() * (m01() * m32() - m02() * m31()),
             m31() * (m02() * m13() - m03() * m12()) + m32() * (m03() * m11() - m01() * m13()) + m33() * (m01() * m12() - m02() * m11()),
             m01() * (m13() * m22() - m12() * m23()) + m02() * (m11() * m23() - m13() * m21()) + m03() * (m12() * m21() - m11() * m22()),
             m12() * (m20() * m33() - m23() * m30()) + m13() * (m22() * m30() - m20() * m32()) + m10() * (m23() * m32() - m22() * m33()),
             m22() * (m00() * m33() - m03() * m30()) + m23() * (m02() * m30() - m00() * m32()) + m20() * (m03() * m32() - m02() * m33()),
             m32() * (m00() * m13() - m03() * m10()) + m33() * (m02() * m10() - m00() * m12()) + m30() * (m03() * m12() - m02() * m13()),
             m02() * (m13() * m20() - m10() * m23()) + m03() * (m10() * m22() - m12() * m20()) + m00() * (m12() * m23() - m13() * m22()),
             m13() * (m20() * m31() - m21() * m30()) + m10() * (m21() * m33() - m23() * m31()) + m11() * (m23() * m30() - m20() * m33()),
             m23() * (m00() * m31() - m01() * m30()) + m20() * (m01() * m33() - m03() * m31()) + m21() * (m03() * m30() - m00() * m33()),
             m33() * (m00() * m11() - m01() * m10()) + m30() * (m01() * m13() - m03() * m11()) + m31() * (m03() * m10() - m00() * m13()),
             m03() * (m11() * m20() - m10() * m21()) + m00() * (m13() * m21() - m11() * m23()) + m01() * (m10() * m23() - m13() * m20()),
             m10() * (m22() * m31() - m21() * m32()) + m11() * (m20() * m32() - m22() * m30()) + m12() * (m21() * m30() - m20() * m31()),
             m20() * (m02() * m31() - m01() * m32()) + m21() * (m00() * m32() - m02() * m30()) + m22() * (m01() * m30() - m00() * m31()),
             m30() * (m02() * m11() - m01() * m12()) + m31() * (m00() * m12() - m02() * m10()) + m32() * (m01() * m10() - m00() * m11()),
             m00() * (m11() * m22() - m12() * m21()) + m01() * (m12() * m20() - m10() * m22()) + m02() * (m10() * m21() - m11() * m20())
           );
        */
        set( get(1, 1) * (get(2, 2) * get(3, 3) - get(2, 3) * get(3, 2)) + get(1, 2) * (get(2, 3) * get(3, 1) - get(2, 1) * get(3, 3)) + get(1, 3) * (get(2, 1) * get(3, 2) - get(2, 2) * get(3, 1)),
             get(2, 1) * (get(0, 2) * get(3, 3) - get(0, 3) * get(3, 2)) + get(2, 2) * (get(0, 3) * get(3, 1) - get(0, 1) * get(3, 3)) + get(2, 3) * (get(0, 1) * get(3, 2) - get(0, 2) * get(3, 1)),
             get(3, 1) * (get(0, 2) * get(1, 3) - get(0, 3) * get(1, 2)) + get(3, 2) * (get(0, 3) * get(1, 1) - get(0, 1) * get(1, 3)) + get(3, 3) * (get(0, 1) * get(1, 2) - get(0, 2) * get(1, 1)),
             get(0, 1) * (get(1, 3) * get(2, 2) - get(1, 2) * get(2, 3)) + get(0, 2) * (get(1, 1) * get(2, 3) - get(1, 3) * get(2, 1)) + get(0, 3) * (get(1, 2) * get(2, 1) - get(1, 1) * get(2, 2)),
             get(1, 2) * (get(2, 0) * get(3, 3) - get(2, 3) * get(3, 0)) + get(1, 3) * (get(2, 2) * get(3, 0) - get(2, 0) * get(3, 2)) + get(1, 0) * (get(2, 3) * get(3, 2) - get(2, 2) * get(3, 3)),
             get(2, 2) * (get(0, 0) * get(3, 3) - get(0, 3) * get(3, 0)) + get(2, 3) * (get(0, 2) * get(3, 0) - get(0, 0) * get(3, 2)) + get(2, 0) * (get(0, 3) * get(3, 2) - get(0, 2) * get(3, 3)),
             get(3, 2) * (get(0, 0) * get(1, 3) - get(0, 3) * get(1, 0)) + get(3, 3) * (get(0, 2) * get(1, 0) - get(0, 0) * get(1, 2)) + get(3, 0) * (get(0, 3) * get(1, 2) - get(0, 2) * get(1, 3)),
             get(0, 2) * (get(1, 3) * get(2, 0) - get(1, 0) * get(2, 3)) + get(0, 3) * (get(1, 0) * get(2, 2) - get(1, 2) * get(2, 0)) + get(0, 0) * (get(1, 2) * get(2, 3) - get(1, 3) * get(2, 2)),
             get(1, 3) * (get(2, 0) * get(3, 1) - get(2, 1) * get(3, 0)) + get(1, 0) * (get(2, 1) * get(3, 3) - get(2, 3) * get(3, 1)) + get(1, 1) * (get(2, 3) * get(3, 0) - get(2, 0) * get(3, 3)),
             get(2, 3) * (get(0, 0) * get(3, 1) - get(0, 1) * get(3, 0)) + get(2, 0) * (get(0, 1) * get(3, 3) - get(0, 3) * get(3, 1)) + get(2, 1) * (get(0, 3) * get(3, 0) - get(0, 0) * get(3, 3)),
             get(3, 3) * (get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0)) + get(3, 0) * (get(0, 1) * get(1, 3) - get(0, 3) * get(1, 1)) + get(3, 1) * (get(0, 3) * get(1, 0) - get(0, 0) * get(1, 3)),
             get(0, 3) * (get(1, 1) * get(2, 0) - get(1, 0) * get(2, 1)) + get(0, 0) * (get(1, 3) * get(2, 1) - get(1, 1) * get(2, 3)) + get(0, 1) * (get(1, 0) * get(2, 3) - get(1, 3) * get(2, 0)),
             get(1, 0) * (get(2, 2) * get(3, 1) - get(2, 1) * get(3, 2)) + get(1, 1) * (get(2, 0) * get(3, 2) - get(2, 2) * get(3, 0)) + get(1, 2) * (get(2, 1) * get(3, 0) - get(2, 0) * get(3, 1)),
             get(2, 0) * (get(0, 2) * get(3, 1) - get(0, 1) * get(3, 2)) + get(2, 1) * (get(0, 0) * get(3, 2) - get(0, 2) * get(3, 0)) + get(2, 2) * (get(0, 1) * get(3, 0) - get(0, 0) * get(3, 1)),
             get(3, 0) * (get(0, 2) * get(1, 1) - get(0, 1) * get(1, 2)) + get(3, 1) * (get(0, 0) * get(1, 2) - get(0, 2) * get(1, 0)) + get(3, 2) * (get(0, 1) * get(1, 0) - get(0, 0) * get(1, 1)),
             get(0, 0) * (get(1, 1) * get(2, 2) - get(1, 2) * get(2, 1)) + get(0, 1) * (get(1, 2) * get(2, 0) - get(1, 0) * get(2, 2)) + get(0, 2) * (get(1, 0) * get(2, 1) - get(1, 1) * get(2, 0))
          );
        
        mul( d );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SubMatrix4f mul( Matrix4f mat1, Matrix4f mat2 )
    {
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        set( mat1.get(0, 0) * mat2.get(0, 0) + mat1.get(0, 1) * mat2.get(1, 0) + mat1.get(0, 2) * mat2.get(2, 0) + mat1.get(0, 3) * mat2.get(3, 0),
             mat1.get(0, 0) * mat2.get(0, 1) + mat1.get(0, 1) * mat2.get(1, 1) + mat1.get(0, 2) * mat2.get(2, 1) + mat1.get(0, 3) * mat2.get(3, 1),
             mat1.get(0, 0) * mat2.get(0, 2) + mat1.get(0, 1) * mat2.get(1, 2) + mat1.get(0, 2) * mat2.get(2, 2) + mat1.get(0, 3) * mat2.get(3, 2),
             mat1.get(0, 0) * mat2.get(0, 3) + mat1.get(0, 1) * mat2.get(1, 3) + mat1.get(0, 2) * mat2.get(2, 3) + mat1.get(0, 3) * mat2.get(3, 3),
             mat1.get(1, 0) * mat2.get(0, 0) + mat1.get(1, 1) * mat2.get(1, 0) + mat1.get(1, 2) * mat2.get(2, 0) + mat1.get(1, 3) * mat2.get(3, 0),
             mat1.get(1, 0) * mat2.get(0, 1) + mat1.get(1, 1) * mat2.get(1, 1) + mat1.get(1, 2) * mat2.get(2, 1) + mat1.get(1, 3) * mat2.get(3, 1),
             mat1.get(1, 0) * mat2.get(0, 2) + mat1.get(1, 1) * mat2.get(1, 2) + mat1.get(1, 2) * mat2.get(2, 2) + mat1.get(1, 3) * mat2.get(3, 2),
             mat1.get(1, 0) * mat2.get(0, 3) + mat1.get(1, 1) * mat2.get(1, 3) + mat1.get(1, 2) * mat2.get(2, 3) + mat1.get(1, 3) * mat2.get(3, 3),
             mat1.get(2, 0) * mat2.get(0, 0) + mat1.get(2, 1) * mat2.get(1, 0) + mat1.get(2, 2) * mat2.get(2, 0) + mat1.get(2, 3) * mat2.get(3, 0),
             mat1.get(2, 0) * mat2.get(0, 1) + mat1.get(2, 1) * mat2.get(1, 1) + mat1.get(2, 2) * mat2.get(2, 1) + mat1.get(2, 3) * mat2.get(3, 1),
             mat1.get(2, 0) * mat2.get(0, 2) + mat1.get(2, 1) * mat2.get(1, 2) + mat1.get(2, 2) * mat2.get(2, 2) + mat1.get(2, 3) * mat2.get(3, 2),
             mat1.get(2, 0) * mat2.get(0, 3) + mat1.get(2, 1) * mat2.get(1, 3) + mat1.get(2, 2) * mat2.get(2, 3) + mat1.get(2, 3) * mat2.get(3, 3),
             mat1.get(3, 0) * mat2.get(0, 0) + mat1.get(3, 1) * mat2.get(1, 0) + mat1.get(3, 2) * mat2.get(2, 0) + mat1.get(3, 3) * mat2.get(3, 0),
             mat1.get(3, 0) * mat2.get(0, 1) + mat1.get(3, 1) * mat2.get(1, 1) + mat1.get(3, 2) * mat2.get(2, 1) + mat1.get(3, 3) * mat2.get(3, 1),
             mat1.get(3, 0) * mat2.get(0, 2) + mat1.get(3, 1) * mat2.get(1, 2) + mat1.get(3, 2) * mat2.get(2, 2) + mat1.get(3, 3) * mat2.get(3, 2),
             mat1.get(3, 0) * mat2.get(0, 3) + mat1.get(3, 1) * mat2.get(1, 3) + mat1.get(3, 2) * mat2.get(2, 3) + mat1.get(3, 3) * mat2.get(3, 3)
           );
        
        return ( this );
    }
    
    public SubMatrix4f( boolean readOnly, int dataBegin, int colSkip, float[] values )
    {
        super( readOnly, dataBegin, colSkip, values, null );
    }
}
