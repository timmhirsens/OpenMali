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

import org.openmali.FastMath;
import org.openmali.vecmath2.pools.Vector2fPool;

/**
 * A 2 element vector that is represented by single precision floating point x, y
 * coordinates.
 * 
 * Inspired by Kenji Hiranabe's Vector2f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Vector2f extends Tuple2f implements Externalizable, VectorInterface< Tuple2f, Vector2f >
{
    private static final long serialVersionUID = -6082359302762117788L;
    
    public static final Vector2f ZERO = Vector2f.newReadOnly( 0f, 0f );
    
    public static final Vector2f POSITIVE_X_AXIS = Vector2f.newReadOnly( +1f, 0f );
    public static final Vector2f NEGATIVE_X_AXIS = Vector2f.newReadOnly( -1f, 0f );
    public static final Vector2f POSITIVE_Y_AXIS = Vector2f.newReadOnly( 0f, +1f );
    public static final Vector2f NEGATIVE_Y_AXIS = Vector2f.newReadOnly( 0f, -1f );
    
    //private static final Vector2fPool POOL = new Vector2fPool( 128 );
    private static final ThreadLocal<Vector2fPool> POOL = new ThreadLocal<Vector2fPool>()
    {
        @Override
        protected Vector2fPool initialValue()
        {
            return ( new Vector2fPool( 128 ) );
        }
    };
    
    private Vector2f readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    public final float lengthSquared()
    {
        return ( getX() * getX() + getY() * getY() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float length()
    {
        return ( FastMath.sqrt( lengthSquared() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector2f normalize()
    {
        final float l = length();
        
        // zero-div may occur.
        this.divX( l );
        this.divY( l );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector2f normalize( Vector2f vector )
    {
        set( vector );
        normalize();
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector2f cross( Vector2f v1, Vector2f v2 )
    {
        // store in tmp once for aliasing-safty
        // i.e. safe when a.cross(a, b)
        set( v1.getY() * v2.getX() - v1.getX() * v2.getY(),
             v1.getX() * v2.getY() - v1.getY() * v2.getX()
           );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float dot( Vector2f v2 )
    {
        return ( this.getX() * v2.getX() + this.getY() * v2.getY() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float angle( Vector2f v2 )
    {
        // more stable than acos
        return ( Math.abs( FastMath.atan2( this.getX() * v2.getY() - this.getY() * v2.getX(), dot( v2 ) ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2f clone()
    {
        return ( new Vector2f( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2f asReadOnly()
    {
        return ( new Vector2f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified xy coordinates.
     * 
     * @param readOnly
     * @param x the x coordinate
     * @param y the y coordinate
     */
    protected Vector2f( boolean readOnly, float x, float y )
    {
        super( readOnly, x, y );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified array.
     * 
     * @param readOnly
     * @param values the array of length 2 containing xy in order
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Vector2f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified Tuple2f.
     * 
     * @param readOnly
     * @param tuple the Tuple2f containing the initialization x y data
     */
    protected Vector2f( boolean readOnly, Tuple2f tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY() );
    }
    
    /**
     * Constructs and initializes a Vector2f to (0,0).
     * 
     * @param readOnly
     */
    protected Vector2f( boolean readOnly )
    {
        this( readOnly, 0f, 0f );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified xy coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Vector2f( float x, float y )
    {
        this( false, x, y );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified array.
     * 
     * @param values the array of length 2 containing xy in order
     */
    public Vector2f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified Tuple2f.
     * 
     * @param tuple the Tuple2f containing the initialization x y data
     */
    public Vector2f( Tuple2f tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Constructs and initializes a Vector2f to (0,0).
     */
    public Vector2f()
    {
        this( false );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified xy coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public static Vector2f newReadOnly( float x, float y )
    {
        return ( new Vector2f( true, x, y ) );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified array.
     * 
     * @param values the array of length 2 containing xy in order
     */
    public static Vector2f newReadOnly( float[] values )
    {
        return ( new Vector2f( true, values, null, true ) );
    }
    
    /**
     * Constructs and initializes a Vector2f from the specified Tuple2f.
     * 
     * @param tuple the Tuple2f containing the initialization x y data
     */
    public static Vector2f newReadOnly( Tuple2f tuple )
    {
        return ( new Vector2f( true, tuple ) );
    }
    
    /**
     * Constructs and initializes a Vector2f to (0,0).
     */
    public static Vector2f newReadOnly()
    {
        return ( new Vector2f( true ) );
    }
    
    /**
     * Allocates an Vector2f instance from the pool.
     */
    public static Vector2f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Vector2f instance from the pool.
     */
    public static Vector2f fromPool( float x, float y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Vector2f instance from the pool.
     */
    public static Vector2f fromPool( Tuple2f tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Vector2f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Vector2f o )
    {
        POOL.get().free( o );
    }
}
