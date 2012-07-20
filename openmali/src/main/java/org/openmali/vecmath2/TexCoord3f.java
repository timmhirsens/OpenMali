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

import org.openmali.vecmath2.pools.TexCoord3fPool;

/**
 * A simple Texture-Coordinate implementation for 3 values.<br>
 * The order is (s, t, r).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class TexCoord3f extends TexCoordf< TexCoord3f > implements Externalizable
{
    private static final long serialVersionUID = -7616743403485094339L;
    
    //private static final TexCoord3fPool POOL = new TexCoord3fPool( 32 );
    private static final ThreadLocal<TexCoord3fPool> POOL = new ThreadLocal<TexCoord3fPool>()
    {
        @Override
        protected TexCoord3fPool initialValue()
        {
            return ( new TexCoord3fPool( 128 ) );
        }
    };
    
    /**
     * Sets all values of this texCoord to the specified ones.
     * 
     * @param s the s element to use
     * @param t the t element to use
     * @param r the p element to use
     * 
     * @return itself
     */
    public final TexCoord3f set( float s, float t, float r )
    {
        setS( s );
        setT( t );
        setP( r );
        
        return ( this );
    }
    
    /**
     * Sets the S (1st) texCoord component.
     * 
     * @param s
     * 
     * @return itself
     */
    public final TexCoord3f setS( float s )
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
    public final TexCoord3f s( float s )
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
    public final TexCoord3f setT( float t )
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
    public final TexCoord3f t( float t )
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
     * Sets the P (3rd) texCoord component.
     * 
     * @param p
     * 
     * @return itself
     */
    public final TexCoord3f setP( float p )
    {
        this.values[ roTrick + 2 ] = p;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the P (3rd) texCoord component.
     */
    public final float getP()
    {
        return ( values[ 2 ] );
    }
    
    /**
     * Sets the P (3rd) texCoord component.
     * 
     * @param p
     * 
     * @return itself
     */
    public final TexCoord3f p( float p )
    {
        this.values[ roTrick + 2 ] = p;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the P (3rd) texCoord component.
     */
    public final float p()
    {
        return ( values[ 2 ] );
    }
    
    /**
     * Adds v to this texCoord's S value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord3f addS( float v )
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
    public final TexCoord3f addT( float v )
    {
        this.values[ roTrick + 1 ] += v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this texCoord's P value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord3f addP( float v )
    {
        this.values[ roTrick + 2 ] += v;
        
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
    public final TexCoord3f subS( float v )
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
    public final TexCoord3f subT( float v )
    {
        this.values[ roTrick + 1 ] -= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this texCoord's P value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord3f subP( float v )
    {
        this.values[ roTrick + 2 ] -= v;
        
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
    public final TexCoord3f mulS( float v )
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
    public final TexCoord3f mulT( float v )
    {
        this.values[ roTrick + 1 ] *= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's P value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord3f mulP( float v )
    {
        this.values[ roTrick + 2 ] *= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's values with vs, vt, vr.
     * 
     * @param vs
     * @param vt
     * @param vp
     * 
     * @return itself
     */
    public final TexCoord3f mul( float vs, float vt, float vp )
    {
        this.values[ roTrick + 0 ] *= vs;
        this.values[ roTrick + 1 ] *= vt;
        this.values[ roTrick + 2 ] *= vp;
        
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
    public final TexCoord3f mul( float factor )
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
    public final TexCoord3f divS( float v )
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
    public final TexCoord3f divT( float v )
    {
        this.values[ roTrick + 1 ] /= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's P value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord3f divP( float v )
    {
        this.values[ roTrick + 2 ] /= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's values by vs, vt, vr.
     * 
     * @param vs
     * @param vt
     * @param vp
     * 
     * @return itself
     */
    public final TexCoord3f div( float vs, float vt, float vp )
    {
        this.values[ roTrick + 0 ] /= vs;
        this.values[ roTrick + 1 ] /= vt;
        this.values[ roTrick + 2 ] /= vp;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param s
     * @param t
     * @param p
     * 
     * @return itself
     */
    public final TexCoord3f add( float s, float t, float p )
    {
        this.values[ roTrick + 0 ] += s;
        this.values[ roTrick + 1 ] += t;
        this.values[ roTrick + 2 ] += p;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param s
     * @param t
     * @param p
     * 
     * @return itself
     */
    public final TexCoord3f sub( float s, float t, float p )
    {
        this.values[ roTrick + 0 ] -= s;
        this.values[ roTrick + 1 ] -= t;
        this.values[ roTrick + 2 ] -= p;
        
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
        return ( ( o != null ) && ( ( o instanceof TexCoord3f ) && equals( (TexCoord3f)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public TexCoord3f clone()
    {
        return ( new TexCoord3f( this ) );
    }
    
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param readOnly
     * @param s the S element to use
     * @param t the T element to use
     * @param p the P element to use
     */
    protected TexCoord3f( boolean readOnly, float s, float t, float p )
    {
        super( readOnly, new float[] { s, t, p } );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     */
    protected TexCoord3f( boolean readOnly, float[] values )
    {
        this( readOnly, values[ 0 ], values[ 1 ], values[ 2 ] );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param readOnly
     * @param texCoord the TexCoordf to copy the values from
     */
    protected TexCoord3f( boolean readOnly, TexCoordf< ? > texCoord )
    {
        super( readOnly, newArray( texCoord.values, 3 ) );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param readOnly
     */
    protected TexCoord3f( boolean readOnly )
    {
        this( readOnly, 0f, 0f, 0f );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param s the S element to use
     * @param t the T element to use
     * @param p the P element to use
     */
    public TexCoord3f( float s, float t, float p )
    {
        this( false, s, t, p );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public TexCoord3f( float[] values )
    {
        this( false, values );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public TexCoord3f( TexCoordf< ? > texCoord )
    {
        this( false, texCoord );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     */
    public TexCoord3f()
    {
        this( false );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param s the S element to use
     * @param t the T element to use
     * @param p the P element to use
     */
    public static final TexCoord3f newReadOnly( float s, float t, float p )
    {
        return ( new TexCoord3f( true, s, t, p ) );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static final TexCoord3f newReadOnly( float[] values )
    {
        return ( new TexCoord3f( true, values ) );
    }
    
    /**
     * Creates a new TexCoord3f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public static final TexCoord3f newReadOnly( TexCoordf< ? > texCoord )
    {
        return ( new TexCoord3f( true, texCoord ) );
    }
    
    /**
     * Allocates an TexCoord3f instance from the pool.
     */
    public static TexCoord3f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an TexCoord3f instance from the pool.
     * 
     * @param s
     * @param t
     * @param p
     */
    public static TexCoord3f fromPool( float s, float t, float p )
    {
        return ( POOL.get().alloc( s, t, p ) );
    }
    
    /**
     * Stores the given TexCoord3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( TexCoord3f o )
    {
        POOL.get().free( o );
    }
}
