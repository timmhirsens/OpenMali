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

import org.openmali.vecmath2.pools.Vector2iPool;

/**
 * A 2 element vector that is represented by signed integer x,y coordinates.
 * 
 * Inspired by Kenji Hiranabe's Vector2i implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Vector2i extends Tuple2i implements Externalizable
{
    private static final long serialVersionUID = -5646029784075385939L;
    
    public static final Vector2i ZERO = Vector2i.newReadOnly( 0, 0 );
    
    //private static final Vector2iPool POOL = new Vector2iPool( 128 );
    private static final ThreadLocal<Vector2iPool> POOL = new ThreadLocal<Vector2iPool>()
    {
        @Override
        protected Vector2iPool initialValue()
        {
            return ( new Vector2iPool( 128 ) );
        }
    };
    
    private Vector2i readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2i clone()
    {
        return ( new Vector2i( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2i asReadOnly()
    {
        return ( new Vector2i( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2i getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified xyz coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    protected Vector2i( boolean readOnly, int x, int y )
    {
        super( readOnly, x, y );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified array.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 2)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Vector2i( boolean readOnly, int[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified Point3i.
     * 
     * @param tuple the Point3i containing the initialization x y z data
     */
    protected Vector2i( boolean readOnly, Tuple2i tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY() );
    }
    
    /**
     * Constructs and initializes a Point3i to (0,0,0).
     */
    protected Vector2i( boolean readOnly )
    {
        this( readOnly, 0, 0 );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified xyz coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Vector2i( int x, int y )
    {
        this( false, x, y );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified array.
     * 
     * @param values the array of length 3 containing xyz in order
     */
    public Vector2i( int[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified Point3i.
     * 
     * @param tuple the Point3i containing the initialization x y z data
     */
    public Vector2i( Tuple2i tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Constructs and initializes a Point3i to (0,0,0).
     */
    public Vector2i()
    {
        this( false );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified xyz coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public static Vector2i newReadOnly( int x, int y )
    {
        return ( new Vector2i( true, x, y ) );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified array.
     * 
     * @param values the array of length 3 containing xyz in order
     */
    public static Vector2i newReadOnly( int[] values )
    {
        return ( new Vector2i( true, values, null, true ) );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified Point3i.
     * 
     * @param tuple the Point3i containing the initialization x y z data
     */
    public static Vector2i newReadOnly( Tuple2i tuple )
    {
        return ( new Vector2i( true, tuple ) );
    }
    
    /**
     * Constructs and initializes a Point3i to (0,0,0).
     */
    public static Vector2i newReadOnly()
    {
        return ( new Vector2i( true ) );
    }
    
    /**
     * Allocates an Vector2i instance from the pool.
     */
    public static Vector2i fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Vector2i instance from the pool.
     */
    public static Vector2i fromPool( int x, int y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Vector2i instance from the pool.
     */
    public static Vector2i fromPool( Tuple2i tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Vector2i instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Vector2i o )
    {
        POOL.get().free( o );
    }
}
