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

import org.openmali.FastMathd;
import org.openmali.vecmath2.pools.Vector2dPool;

/**
 * A 2 element vector that is represented by single precision doubleing point x, y
 * coordinates.
 * 
 * Inspired by Kenji Hiranabe's Vector2d implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Vector2d extends Tuple2d implements Externalizable
{
    private static final long serialVersionUID = -6082359302762117788L;
    
    public static final Vector2d ZERO = Vector2d.newReadOnly( 0d, 0d );
    
    public static final Vector2d POSITIVE_X_AXIS = Vector2d.newReadOnly( +1d, 0d );
    public static final Vector2d NEGATIVE_X_AXIS = Vector2d.newReadOnly( -1d, 0d );
    public static final Vector2d POSITIVE_Y_AXIS = Vector2d.newReadOnly( 0d, +1d );
    public static final Vector2d NEGATIVE_Y_AXIS = Vector2d.newReadOnly( 0d, -1d );
    
    //private static final Vector2dPool POOL = new Vector2dPool( 128 );
    private static final ThreadLocal<Vector2dPool> POOL = new ThreadLocal<Vector2dPool>()
    {
        @Override
        protected Vector2dPool initialValue()
        {
            return ( new Vector2dPool( 128 ) );
        }
    };
    
    private Vector2d readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    public final double lengthSquared()
    {
        return ( getX() * getX() + getY() * getY() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double length()
    {
        return ( FastMathd.sqrt( lengthSquared() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector2d normalize()
    {
        final double l = length();
        
        // zero-div may occur.
        this.divX( l );
        this.divY( l );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector2d normalize( Vector2d vector )
    {
        set( vector );
        normalize();
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector2d cross( Vector2d v1, Vector2d v2 )
    {
        // store in tmp once for aliasing-safty
        // i.e. safe when a.cross(a, b)
        set( v1.getY() * v2.getX() - v1.getX() * v2.getY(), v1.getX() * v2.getY() - v1.getY() * v2.getX() );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double dot( Vector2d v2 )
    {
        return ( this.getX() * v2.getX() + this.getY() * v2.getY() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double angle( Vector2d v2 )
    {
        // more stable than acos
        return ( Math.abs( FastMathd.atan2( this.getX() * v2.getY() - this.getY() * v2.getX(), dot( v2 ) ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2d asReadOnly()
    {
        return ( new Vector2d( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2d getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified xy coordinates.
     * 
     * @param readOnly
     * @param x the x coordinate
     * @param y the y coordinate
     */
    protected Vector2d( boolean readOnly, double x, double y )
    {
        super( readOnly, x, y );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified array.
     * 
     * @param readOnly
     * @param values the array of length 2 containing xy in order
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Vector2d( boolean readOnly, double[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified Tuple2d.
     * 
     * @param readOnly
     * @param tuple the Tuple2d containing the initialization x y data
     */
    protected Vector2d( boolean readOnly, Tuple2d tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY() );
    }
    
    /**
     * Constructs and initializes a Vector2d to (0,0).
     * 
     * @param readOnly
     */
    protected Vector2d( boolean readOnly )
    {
        this( readOnly, 0d, 0d );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified xy coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Vector2d( double x, double y )
    {
        this( false, x, y );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified array.
     * 
     * @param values the array of length 2 containing xy in order
     */
    public Vector2d( double[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified Tuple2d.
     * 
     * @param tuple the Tuple2d containing the initialization x y data
     */
    public Vector2d( Tuple2d tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Constructs and initializes a Vector2d to (0,0).
     */
    public Vector2d()
    {
        this( false );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified xy coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public static Vector2d newReadOnly( double x, double y )
    {
        return ( new Vector2d( true, x, y ) );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified array.
     * 
     * @param values the array of length 2 containing xy in order
     */
    public static Vector2d newReadOnly( double[] values )
    {
        return ( new Vector2d( true, values, null, true ) );
    }
    
    /**
     * Constructs and initializes a Vector2d from the specified Tuple2d.
     * 
     * @param tuple the Tuple2d containing the initialization x y data
     */
    public static Vector2d newReadOnly( Tuple2d tuple )
    {
        return ( new Vector2d( true, tuple ) );
    }
    
    /**
     * Constructs and initializes a Vector2d to (0,0).
     */
    public static Vector2d newReadOnly()
    {
        return ( new Vector2d( true ) );
    }
    
    /**
     * Allocates an Vector2d instance from the pool.
     */
    public static Vector2d fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Vector2d instance from the pool.
     */
    public static Vector2d fromPool( double x, double y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Vector2d instance from the pool.
     */
    public static Vector2d fromPool( Tuple2d tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Vector2d instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Vector2d o )
    {
        POOL.get().free( o );
    }
}
