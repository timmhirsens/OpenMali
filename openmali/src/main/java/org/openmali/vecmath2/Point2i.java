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

import org.openmali.vecmath2.pools.Point2iPool;

/**
 * A 2 element point that is represented by signed integer x,y coordinates.
 * 
 * Inspired by Kenji Hiranabe's Point2i implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Point2i extends Tuple2i implements Externalizable
{
    private static final long serialVersionUID = 887936353758486269L;
    
    public static final Point2i ZERO = Point2i.newReadOnly( 0, 0 );
    
    //private static final Point2iPool POOL = new Point2iPool( 128 );
    private static final ThreadLocal<Point2iPool> POOL = new ThreadLocal<Point2iPool>()
    {
        @Override
        protected Point2iPool initialValue()
        {
            return ( new Point2iPool( 128 ) );
        }
    };
    
    private Point2i readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2i clone()
    {
        return ( new Point2i( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2i asReadOnly()
    {
        return ( new Point2i( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2i getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified xyz coordinates.
     * 
     * @param readOnly
     * @param x the x coordinate
     * @param y the y coordinate
     */
    protected Point2i( boolean readOnly, int x, int y )
    {
        super( readOnly, x, y );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified array.
     * 
     * @param readOnly
     * @param values the array of length 3 containing xyz in order
     * @param isDirty
     * @param copy
     */
    protected Point2i( boolean readOnly, int[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified Point3i.
     * 
     * @param readOnly
     * @param tuple the Point3i containing the initialization x y z data
     */
    protected Point2i( boolean readOnly, Tuple2i tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY() );
    }
    
    /**
     * Constructs and initializes a Point3i to (0,0,0).
     * 
     * @param readOnly
     */
    protected Point2i( boolean readOnly )
    {
        this( readOnly, 0, 0 );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified xyz coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point2i( int x, int y )
    {
        this( false, x, y );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified array.
     * 
     * @param values the array of length 3 containing xyz in order
     */
    public Point2i( int[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified Point3i.
     * 
     * @param tuple the Point3i containing the initialization x y z data
     */
    public Point2i( Tuple2i tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Constructs and initializes a Point3i to (0,0,0).
     */
    public Point2i()
    {
        this( false );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified xyz coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public static Point2i newReadOnly( int x, int y )
    {
        return ( new Point2i( true, x, y ) );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified array.
     * 
     * @param values the array of length 3 containing xyz in order
     */
    public static Point2i newReadOnly( int[] values )
    {
        return ( new Point2i( true, values, null, true ) );
    }
    
    /**
     * Constructs and initializes a Point3i from the specified Point3i.
     * 
     * @param tuple the Point3i containing the initialization x y z data
     */
    public static Point2i newReadOnly( Tuple2i tuple )
    {
        return ( new Point2i( true, tuple ) );
    }
    
    /**
     * Constructs and initializes a Point3i to (0,0,0).
     */
    public static Point2i newReadOnly()
    {
        return ( new Point2i( true ) );
    }
    
    /**
     * Allocates an Point2i instance from the pool.
     */
    public static Point2i fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Point2i instance from the pool.
     */
    public static Point2i fromPool( int x, int y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Point2i instance from the pool.
     */
    public static Point2i fromPool( Tuple2i tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Point2i instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Point2i o )
    {
        POOL.get().free( o );
    }
}
