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
 * This class simply overrides some methods in Matrix3f, that are faster there,
 * but are therefore not compatible with a sub-matrix.
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Arne Mueller
 */
class SubMatrix3f extends Matrix3f
{
    private static final long serialVersionUID = 7372605249739675868L;
    
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
        
        buffer.put( m01() );
        buffer.put( m11() );
        buffer.put( m21() );
        
        buffer.put( m02() );
        buffer.put( m12() );
        buffer.put( m22() );
        
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
        
        buffer.put( m01() );
        buffer.put( m11() );
        buffer.put( m21() );
        
        buffer.put( m02() );
        buffer.put( m12() );
        buffer.put( m22() );
        
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
        
        m01( buffer.get() );
        m11( buffer.get() );
        m21( buffer.get() );
        
        m02( buffer.get() );
        m12( buffer.get() );
        m22( buffer.get() );
        
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
        
        m01( buffer.get() );
        m11( buffer.get() );
        m21( buffer.get() );
        
        m02( buffer.get() );
        m12( buffer.get() );
        m22( buffer.get() );
        
        return ( buffer );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SubMatrix3f mul( float scalar )
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
    public SubMatrix3f invert()
    {
        float d = determinant();
        if ( d == 0.0f )
            return ( this );
        d = 1.0f / d;
        
        // alias-safe way.
        set( m11() * m22() - m12() * m21(), m02() * m21() - m01() * m22(), m01() * m12() - m02() * m11(),
             m12() * m20() - m10() * m22(), m00() * m22() - m02() * m20(), m02() * m10() - m00() * m12(),
             m10() * m21() - m11() * m20(), m01() * m20() - m00() * m21(), m00() * m11() - m01() * m10()
           );
        
        mul( d );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void interpolate( Matrix3f m1, Matrix3f m2, float alpha )
    {
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                this.set( r, c, m1.get( r, c ) + ( ( m2.get( r, c ) - m1.get( r, c ) ) * alpha ) );
            }
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    public SubMatrix3f( boolean readOnly, int dataBegin, int colSkip, float[] values )
    {
        super( readOnly, dataBegin, colSkip, values, null );
    }
}
