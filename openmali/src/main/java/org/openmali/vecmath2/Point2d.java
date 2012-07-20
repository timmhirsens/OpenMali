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
import org.openmali.vecmath2.pools.Point2dPool;

/**
 * A 2 element point that is represented by single precision doubleing point x,y
 * coordinates.
 * 
 * Inspired by Kenji Hiranabe's Point2d implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Point2d extends Tuple2d implements Externalizable
{
    private static final long serialVersionUID = -193058726611025895L;
    
    public static final Point2d ZERO = Point2d.newReadOnly( 0d, 0d );
    
    //private static final Point2dPool POOL = new Point2dPool( 128 );
    private static final ThreadLocal<Point2dPool> POOL = new ThreadLocal<Point2dPool>()
    {
        @Override
        protected Point2dPool initialValue()
        {
            return ( new Point2dPool( 128 ) );
        }
    };
    
    private Point2d readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    public final double distanceSquared( Point2d point2 )
    {
        final double dx = this.getX() - point2.getX();
        final double dy = this.getY() - point2.getY();
        
        return ( ( dx * dx ) + ( dy * dy ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double distance( Point2d point2 )
    {
        return ( FastMathd.sqrt( distanceSquared( point2 ) ) );
    }
    
    public final double distanceSquared( double p2x, double p2y )
    {
        double result = 0.0d;
        
        double d = this.getX() - p2x;
        result += d * d;
        d = this.getY() - p2y;
        result += d * d;
        
        return ( result );
    }
    
    public final double distance( double p2x, double p2y )
    {
        return ( FastMathd.sqrt( distanceSquared( p2x, p2y ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double distanceToOriginSquared()
    {
        return ( distance( 0d, 0d ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public double distanceToOrigin()
    {
        return ( FastMathd.sqrt( distanceToOriginSquared() ) );
    }
    
    /**
     * Computes the L-1 (Manhattan) distance between this point and point p1.
     * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
     * 
     * @param point2 the other point
     */
    public final double distanceL1( Point2d point2 )
    {
        return ( Math.abs( this.getX() - point2.getX() ) + Math.abs( this.getY() - point2.getY() ) );
    }
    
    /**
     * Computes the L-infinite distance between this point and point p1. The
     * L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2)].
     * 
     * @param point2 the other point
     */
    public final double distanceLinf( Point2d point2 )
    {
        return ( Math.max( Math.abs( this.getX() - point2.getY() ), Math.abs( this.getY() - point2.getY() ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2d asReadOnly()
    {
        return ( new Point2d( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2d getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     */
    protected Point2d( boolean readOnly, double x, double y )
    {
        super( readOnly, x, y );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 2)
     * @param isDirty
     * @param copy
     */
    protected Point2d( boolean readOnly, double[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param readOnly
     * @param tuple the Tuple2d to copy the values from
     */
    protected Point2d( boolean readOnly, Tuple2d tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY() );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param readOnly
     */
    protected Point2d( boolean readOnly )
    {
        this( readOnly, 0.0d, 0.0d );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public Point2d( double x, double y )
    {
        this( false, x, y );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public Point2d( double[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param tuple the Tuple2d to copy the values from
     */
    public Point2d( Tuple2d tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Creates a new Point2d instance.
     */
    public Point2d()
    {
        this( false );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public static Point2d newReadOnly( double x, double y )
    {
        return ( new Point2d( true, x, y ) );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public static Point2d newReadOnly( double[] values )
    {
        return ( new Point2d( true, values, null, true ) );
    }
    
    /**
     * Creates a new Point2d instance.
     * 
     * @param tuple the Tuple2d to copy the values from
     */
    public static Point2d newReadOnly( Tuple2d tuple )
    {
        return ( new Point2d( true, tuple ) );
    }
    
    /**
     * Creates a new Point2d instance.
     */
    public static Point2d newReadOnly()
    {
        return ( new Point2d( true ) );
    }
    
    /**
     * Allocates an Point2d instance from the pool.
     */
    public static Point2d fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Point2d instance from the pool.
     */
    public static Point2d fromPool( double x, double y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Point2d instance from the pool.
     */
    public static Point2d fromPool( Tuple2d tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Point2d instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Point2d o )
    {
        POOL.get().free( o );
    }
}
