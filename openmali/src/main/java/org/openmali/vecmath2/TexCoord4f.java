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

import org.openmali.vecmath2.pools.TexCoord4fPool;

/**
 * A simple Texture-Coordinate implementation for 4 values.<br>
 * The order is (s, t, r, q).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class TexCoord4f extends TexCoordf< TexCoord4f > implements Externalizable
{
    private static final long serialVersionUID = -8625802351660888699L;
    
    //private static final TexCoord4fPool POOL = new TexCoord4fPool( 32 );
    private static final ThreadLocal<TexCoord4fPool> POOL = new ThreadLocal<TexCoord4fPool>()
    {
        @Override
        protected TexCoord4fPool initialValue()
        {
            return ( new TexCoord4fPool( 128 ) );
        }
    };
    
    /**
     * Sets all values of this texCoord to the specified ones.
     * 
     * @param s the s element to use
     * @param t the t element to use
     * @param p the P element to use
     * @param q the q element to use
     * 
     * @return itself
     */
    public final TexCoord4f set( float s, float t, float p, float q )
    {
        setS( s );
        setT( t );
        setP( p );
        setQ( q );
        
        return ( this );
    }
    
    /**
     * Sets the S (1st) texCoord component.
     * 
     * @param s
     * 
     * @return itself
     */
    public final TexCoord4f setS( float s )
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
    public final TexCoord4f s( float s )
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
    public final TexCoord4f setT( float t )
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
    public final TexCoord4f t( float t )
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
    public final TexCoord4f setP( float p )
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
    public final TexCoord4f p( float p )
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
     * Sets the Q (4th) texCoord component.
     * 
     * @param q
     * 
     * @return itself
     */
    public final TexCoord4f setQ( float q )
    {
        this.values[ roTrick + 3 ] = q;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the Q (4th) texCoord component.
     */
    public final float getQ()
    {
        return ( values[ 3 ] );
    }
    
    /**
     * Sets the Q (4th) texCoord component.
     * 
     * @param q
     * 
     * @return itself
     */
    public final TexCoord4f q( float q )
    {
        this.values[ roTrick + 3 ] = q;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * @return the Q (4th) texCoord component.
     */
    public final float q()
    {
        return ( values[ 3 ] );
    }
    
    /**
     * Adds v to this texCoord's S value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord4f addS( float v )
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
    public final TexCoord4f addT( float v )
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
    public final TexCoord4f addP( float v )
    {
        this.values[ roTrick + 2 ] += v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this texCoord's Q value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord4f addQ( float v )
    {
        this.values[ roTrick + 3 ] += v;
        
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
    public final TexCoord4f subS( float v )
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
    public final TexCoord4f subT( float v )
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
    public final TexCoord4f subP( float v )
    {
        this.values[ roTrick + 2 ] -= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this texCoord's Q value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord4f subQ( float v )
    {
        this.values[ roTrick + 3 ] -= v;
        
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
    public final TexCoord4f mulS( float v )
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
    public final TexCoord4f mulT( float v )
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
    public final TexCoord4f mulP( float v )
    {
        this.values[ roTrick + 2 ] *= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's Q value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord4f mulQ( float v )
    {
        this.values[ roTrick + 3 ] *= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's values with vs, vt, vp, vq.
     * 
     * @param vs
     * @param vt
     * @param vp
     * @param vq
     * 
     * @return itself
     */
    public final TexCoord4f mul( float vs, float vt, float vp, float vq )
    {
        this.values[ roTrick + 0 ] *= vs;
        this.values[ roTrick + 1 ] *= vt;
        this.values[ roTrick + 2 ] *= vp;
        this.values[ roTrick + 3 ] *= vq;
        
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
    public final TexCoord4f mul( float factor )
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
    public final TexCoord4f divS( float v )
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
    public final TexCoord4f divT( float v )
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
    public final TexCoord4f divP( float v )
    {
        this.values[ roTrick + 2 ] /= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's Q value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord4f divQ( float v )
    {
        this.values[ roTrick + 3 ] /= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's values by vs, vt, vp, vq.
     * 
     * @param vs
     * @param vt
     * @param vp
     * @param vq
     * 
     * @return itself
     */
    public final TexCoord4f div( float vs, float vt, float vp, float vq )
    {
        this.values[ roTrick + 0 ] /= vs;
        this.values[ roTrick + 1 ] /= vt;
        this.values[ roTrick + 2 ] /= vp;
        this.values[ roTrick + 3 ] /= vq;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param s
     * @param t
     * @param p
     * @param q
     * 
     * @return itself
     */
    public final TexCoord4f add( float s, float t, float p, float q )
    {
        this.values[ roTrick + 0 ] += s;
        this.values[ roTrick + 1 ] += t;
        this.values[ roTrick + 2 ] += p;
        this.values[ roTrick + 3 ] += q;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param s
     * @param t
     * @param p
     * @param q
     * 
     * @return itself
     */
    public final TexCoord4f sub( float s, float t, float p, float q )
    {
        this.values[ roTrick + 0 ] -= s;
        this.values[ roTrick + 1 ] -= t;
        this.values[ roTrick + 2 ] -= p;
        this.values[ roTrick + 3 ] -= q;
        
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
        return ( ( o != null ) && ( ( o instanceof TexCoord4f ) && equals( (TexCoord4f)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public TexCoord4f clone()
    {
        return ( new TexCoord4f( this ) );
    }
    
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param readOnly
     * @param s the S element to use
     * @param t the T element to use
     * @param p the P element to use
     * @param q the Q channel to use
     */
    protected TexCoord4f( boolean readOnly, float s, float t, float p, float q )
    {
        super( readOnly, new float[] { s, t, p, q } );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 4)
     */
    protected TexCoord4f( boolean readOnly, float[] values )
    {
        this( readOnly, values[ 0 ], values[ 1 ], values[ 2 ], values[ 3 ] );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param readOnly
     * @param texCoord the TexCoordf to copy the values from
     */
    protected TexCoord4f( boolean readOnly, TexCoord4f texCoord )
    {
        super( readOnly, newArray( texCoord.values, 4 ) );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param readOnly
     */
    protected TexCoord4f( boolean readOnly )
    {
        this( readOnly, 0f, 0f, 0f, 0f );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param s the S element to use
     * @param t the T element to use
     * @param p the P element to use
     * @param q the Q channel to use
     */
    public TexCoord4f( float s, float t, float p, float q )
    {
        this( false, s, t, p, q );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param values the values array (must be at least size 4)
     */
    public TexCoord4f( float[] values )
    {
        this( false, values );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public TexCoord4f( TexCoord4f texCoord )
    {
        this( false, texCoord );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     */
    public TexCoord4f()
    {
        this( false );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param s the S element to use
     * @param t the T element to use
     * @param p the P element to use
     * @param q the Q channel to use
     */
    public static final TexCoord4f newReadOnly( float s, float t, float p, float q )
    {
        return ( new TexCoord4f( true, s, t, p, q ) );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param values the values array (must be at least size 4)
     */
    public static final TexCoord4f newReadOnly( float[] values )
    {
        return ( new TexCoord4f( true, values ) );
    }
    
    /**
     * Creates a new TexCoord4f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public static final TexCoord4f newReadOnly( TexCoord4f texCoord )
    {
        return ( new TexCoord4f( true, texCoord ) );
    }
    
    /**
     * Allocates an TexCoord4f instance from the pool.
     */
    public static TexCoord4f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an TexCoord4f instance from the pool.
     * 
     * @param s the S element to use
     * @param t the T element to use
     * @param p the P element to use
     * @param q the Q channel to use
     */
    public static TexCoord4f fromPool( float s, float t, float p, float q )
    {
        return ( POOL.get().alloc( s, t, p, q ) );
    }
    
    /**
     * Stores the given TexCoord4f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( TexCoord4f o )
    {
        POOL.get().free( o );
    }
}
