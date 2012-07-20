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

import org.openmali.vecmath2.pools.TexCoord1fPool;

/**
 * A simple Texture-Coordinate implementation for 1 value.<br>
 * The order is (s).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class TexCoord1f extends TexCoordf< TexCoord1f > implements Externalizable
{
    private static final long serialVersionUID = -8577168782997168074L;
    
    //private static final TexCoord1fPool POOL = new TexCoord1fPool( 32 );
    private static final ThreadLocal<TexCoord1fPool> POOL = new ThreadLocal<TexCoord1fPool>()
    {
        @Override
        protected TexCoord1fPool initialValue()
        {
            return ( new TexCoord1fPool( 128 ) );
        }
    };
    
    /**
     * Sets all values of this texCoord to the specified ones.
     * 
     * @param s the s element to use
     * 
     * @return itself
     */
    public final TexCoord1f set( float s )
    {
        setS( s );
        
        return ( this );
    }
    
    /**
     * Sets the S (1st) texCoord component.
     * 
     * @param s
     * 
     * @return itself
     */
    public final TexCoord1f setS( float s )
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
    public final TexCoord1f s( float s )
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
     * Adds v to this texCoord's S value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final TexCoord1f addS( float v )
    {
        this.values[ roTrick + 0 ] += v;
        
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
    public final TexCoord1f subS( float v )
    {
        this.values[ roTrick + 0 ] -= v;
        
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
    public final TexCoord1f mulS( float v )
    {
        this.values[ roTrick + 0 ] *= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this texCoord's values with vs.
     * 
     * @param vs
     * 
     * @return itself
     */
    public final TexCoord1f mul( float vs )
    {
        this.values[ roTrick + 0 ] *= vs;
        
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
    public final TexCoord1f divS( float v )
    {
        this.values[ roTrick + 0 ] /= v;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Divides this texCoord's values by vs.
     * 
     * @param vs
     * 
     * @return itself
     */
    public final TexCoord1f div( float vs )
    {
        this.values[ roTrick + 0 ] /= vs;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param s
     * 
     * @return itself
     */
    public final TexCoord1f add( float s )
    {
        this.values[ roTrick + 0 ] += s;
        
        this.isDirty = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param s
     * 
     * @return itself
     */
    public final TexCoord1f sub( float s )
    {
        this.values[ roTrick + 0 ] -= s;
        
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
        return ( ( o != null ) && ( ( o instanceof TexCoord1f ) && equals( (TexCoord1f)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public TexCoord1f clone()
    {
        return ( new TexCoord1f( this ) );
    }
    
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param readOnly
     * @param s the S element to use
     */
    protected TexCoord1f( boolean readOnly, float s )
    {
        super( readOnly, new float[] { s } );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 1)
     */
    protected TexCoord1f( boolean readOnly, float[] values )
    {
        this( readOnly, values[ 0 ] );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param readOnly
     * @param texCoord the TexCoordf to copy the values from
     */
    protected TexCoord1f( boolean readOnly, TexCoordf< ? > texCoord )
    {
        super( readOnly, newArray( texCoord.values, 1 ) );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param readOnly
     */
    protected TexCoord1f( boolean readOnly )
    {
        this( readOnly, 0f );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param s the S element to use
     */
    public TexCoord1f( float s )
    {
        this( false, s );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param values the values array (must be at least size 1)
     */
    public TexCoord1f( float[] values )
    {
        this( false, values );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public TexCoord1f( TexCoordf< ? > texCoord )
    {
        this( false, texCoord );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     */
    public TexCoord1f()
    {
        this( false );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param s the S element to use
     */
    public static final TexCoord1f newReadOnly( float s )
    {
        return ( new TexCoord1f( true, s ) );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param values the values array (must be at least size 1)
     */
    public static final TexCoord1f newReadOnly( float[] values )
    {
        return ( new TexCoord1f( true, values ) );
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param texCoord the TexCoordf to copy the values from
     */
    public static final TexCoord1f newReadOnly( TexCoordf< ? > texCoord )
    {
        return ( new TexCoord1f( true, texCoord ) );
    }
    
    /**
     * Allocates an TexCoord1f instance from the pool.
     */
    public static TexCoord1f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an TexCoord1f instance from the pool.
     */
    public static TexCoord1f fromPool( float s )
    {
        return ( POOL.get().alloc( s ) );
    }
    
    /**
     * Stores the given TexCoord1f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( TexCoord1f o )
    {
        POOL.get().free( o );
    }
}
