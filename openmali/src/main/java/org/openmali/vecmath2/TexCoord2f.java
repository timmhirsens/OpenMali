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

import java.io.Externalizable;

import org.openmali.vecmath2.pools.TexCoord2fPool;

/**
 * A simple Texture-Coordinate implementation for 2 values.<br>
 * The order is (s, t).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class TexCoord2f extends TexCoordf< TexCoord2f > implements Externalizable
{
    private static final long serialVersionUID = 4961153246436722156L;
    
    //private static final TexCoord2fPool POOL = new TexCoord2fPool( 32 );
    private static final ThreadLocal<TexCoord2fPool> POOL = new ThreadLocal<TexCoord2fPool>()
    {
        @Override
        protected TexCoord2fPool initialValue()
        {
            return ( new TexCoord2fPool( 128 ) );
        }
    };
    
    public static final TexCoord2f LOWER_LEFT = TexCoord2f.newReadOnly( 0f, 0f );
    public static final TexCoord2f LOWER_RIGHT = TexCoord2f.newReadOnly( 1f, 0f );
    public static final TexCoord2f UPPER_LEFT = TexCoord2f.newReadOnly( 0f, 1f );
    public static final TexCoord2f UPPER_RIGHT = TexCoord2f.newReadOnly( 1f, 1f );
    
    /**
     * Sets all values of this texCoord to the specified ones.
     * 
     * @param s the s element to use
     * @param t the t element to use
     * 
     * @return itself
     */
    public final TexCoord2f set( float s, float t )
    {
        setS( s );
        setT( t );
        
        return ( this );
    }
    
    /**
     * Sets the S (1st) texCoord component.
     * 
     * @param s
     * 
     * @return itself
     */
    public final TexCoord2f setS( float s )
    {
        this.values[ roTrick + 0 ] = s;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the S (1st) texCoord component.
     */
    public final float getS()
    {
        return ( values[ 0 ] );
    }
    
    /**
     * Sets the S (1st) texCoord component.
     * 
     * @param s
     * 
     * @return itself
     */
    public final TexCoord2f s( float s )
    {
        this.values[ roTrick + 0 ] = s;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the S (1st) texCoord component.
     */
    public final float s()
    {
        return ( values[ 0 ] );
    }
    
    /**
     * Sets the T (2nd) texCoord component.
     * 
     * @param t
     * 
     * @return itself
     */
    public final TexCoord2f setT( float t )
    {
        this.values[ roTrick + 1 ] = t;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the T (2nd) texCoord component.
     */
    public final float getT()
    {
        return ( values[ 1 ] );
    }
    
    /**
     * Sets the T (2nd) texCoord component.
     * 
     * @param t
     * 
     * @return itself
     */
    public final TexCoord2f t( float t )
    {
        this.values[ roTrick + 1 ] = t;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the T (2nd) texCoord component.
     */
    public final float t()
    {
        return ( values[ 1 ] );
    }
    
    /**
     * Adds v to this texCoord's S value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f addS( float v )
    {
        this.values[ roTrick + 0 ] += v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this texCoord's T value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f addT( float v )
    {
        this.values[ roTrick + 1 ] += v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this texCoord's S value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f subS( float v )
    {
        this.values[ roTrick + 0 ] -= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this texCoord's T value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f subT( float v )
    {
        this.values[ roTrick + 1 ] -= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's S value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f mulS( float v )
    {
        this.values[ roTrick + 0 ] *= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's T value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f mulT( float v )
    {
        this.values[ roTrick + 1 ] *= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's values with vs, vt.
     * 
     * @param vs
     * @param vt
     * 
     * @return itself
     */
    public final TexCoord2f mul( float vs, float vt )
    {
        this.values[ roTrick + 0 ] *= vs;
        this.values[ roTrick + 1 ] *= vt;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself.
     * 
     * @param factor the scalar value
     * 
     * @return itself
     */
    public final TexCoord2f mul( float factor )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] *= factor;
        }
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's S value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f divS( float v )
    {
        this.values[ roTrick + 0 ] /= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's T value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord2f divT( float v )
    {
        this.values[ roTrick + 1 ] /= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's values by vs, vt.
     * 
     * @param vs
     * @param vt
     * 
     * @return itself
     */
    public final TexCoord2f div( float vs, float vt )
    {
        this.values[ roTrick + 0 ] /= vs;
        this.values[ roTrick + 1 ] /= vt;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param s
     * @param t
     * 
     * @return itself
     */
    public final TexCoord2f add( float s, float t )
    {
        this.values[ roTrick + 0 ] += s;
        this.values[ roTrick + 1 ] += t;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param s
     * @param t
     * 
     * @return itself
     */
    public final TexCoord2f sub( float s, float t )
    {
        this.values[ roTrick + 0 ] -= s;
        this.values[ roTrick + 1 ] -= t;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Returns true if the Object t1 is of type Tuple3f and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Tuple3f.
     * 
     * @param o  the Object with which the comparison is made
     * @return  true or false
     */ 
    @Override
    public boolean equals( Object o )
    {
        return ( ( o != null ) && ( ( o instanceof TexCoord2f ) && equals( (TexCoord2f)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public TexCoord2f clone()
    {
        return ( new TexCoord2f( this ) );
    }
    
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param readOnly
     * @param s the S element to use
     * @param t the T element to use
     */
    protected TexCoord2f( boolean readOnly, float s, float t )
    {
        super( readOnly, new float[] { s, t } );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 2)
     */
    protected TexCoord2f( boolean readOnly, float[] values )
    {
        this( readOnly, values[ 0 ], values[ 1 ] );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param readOnly
     * @param texCoord the TexCoordf to copy the values from
     */
    protected TexCoord2f( boolean readOnly, TexCoordf< ? > texCoord )
    {
        super( readOnly, newArray( texCoord.values, 2 ) );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param readOnly
     */
    protected TexCoord2f( boolean readOnly )
    {
        this( readOnly, 0f, 0f );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param s the S element to use
     * @param t the T element to use
     */
    public TexCoord2f( float s, float t )
    {
        this( false, s, t );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public TexCoord2f( float[] values )
    {
        this( false, values );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public TexCoord2f( TexCoordf< ? > texCoord )
    {
        this( false, texCoord );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     */
    public TexCoord2f()
    {
        this( false );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param s the S element to use
     * @param t the T element to use
     */
    public static final TexCoord2f newReadOnly( float s, float t )
    {
        return ( new TexCoord2f( true, s, t ) );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public static final TexCoord2f newReadOnly( float[] values )
    {
        return ( new TexCoord2f( true, values ) );
    }
    
    /**
     * Creates a new TexCoord2f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public static final TexCoord2f newReadOnly( TexCoordf< ? > texCoord )
    {
        return ( new TexCoord2f( true, texCoord ) );
    }
    
    /**
     * Allocates an TexCoord2f instance from the pool.
     */
    public static TexCoord2f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an TexCoord2f instance from the pool.
     */
    public static TexCoord2f fromPool( float s, float t )
    {
        return ( POOL.get().alloc( s, t ) );
    }
    
    /**
     * Stores the given TexCoord2f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( TexCoord2f o )
    {
        POOL.get().free( o );
    }
}
