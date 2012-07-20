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
import org.openmali.vecmath2.pools.Point2fPool;

/**
 * A 2 element point that is represented by single precision floating point x,y
 * coordinates.
 * 
 * Inspired by Kenji Hiranabe's Point2f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Point2f extends Tuple2f implements Externalizable, PointInterface< Tuple2f, Point2f >
{
    private static final long serialVersionUID = -193058726611025895L;
    
    public static final Point2f ZERO = Point2f.newReadOnly( 0f, 0f );
    
    //private static final Point2fPool POOL = new Point2fPool( 128 );
    private static final ThreadLocal<Point2fPool> POOL = new ThreadLocal<Point2fPool>()
    {
        @Override
        protected Point2fPool initialValue()
        {
            return ( new Point2fPool( 128 ) );
        }
    };
    
    private Point2f readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    public final float distanceSquared( Point2f point2 )
    {
        final float dx = this.getX() - point2.getX();
        final float dy = this.getY() - point2.getY();
        
        return ( (dx * dx) + (dy * dy) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float distance( Point2f point2 )
    {
        return ( FastMath.sqrt( distanceSquared( point2 ) ) );
    }
    
    public final float distanceSquared( float p2x, float p2y )
    {
        float result = 0.0f;
        
        float d = this.getX() - p2x;
        result += d * d;
        d = this.getY() - p2y;
        result += d * d;
        
        return ( result );
    }
    
    public final float distance( float p2x, float p2y )
    {
        return ( FastMath.sqrt( distanceSquared( p2x, p2y ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float distanceToOriginSquared()
    {
        return ( distance( 0f, 0f ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public float distanceToOrigin()
    {
        return ( FastMath.sqrt( distanceToOriginSquared() ) );
    }
    
    /**
     * Computes the L-1 (Manhattan) distance between this point and point p1.
     * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
     * 
     * @param point2 the other point
     */
    public final float distanceL1( Point2f point2 )
    {
        return ( Math.abs( this.getX() - point2.getX() ) + Math.abs( this.getY() - point2.getY() ) );
    }
    
    /**
     * Computes the L-infinite distance between this point and point p1. The
     * L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2)].
     * 
     * @param point2 the other point
     */
    public final float distanceLinf( Point2f point2 )
    {
        return ( Math.max( Math.abs( this.getX() - point2.getY() ), Math.abs( this.getY() - point2.getY() ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2f clone()
    {
        return ( new Point2f( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2f asReadOnly()
    {
        return ( new Point2f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point2f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     */
    protected Point2f( boolean readOnly, float x, float y )
    {
        super( readOnly, x, y );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 2)
     * @param isDirty
     * @param copy
     */
    protected Point2f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param readOnly
     * @param tuple the Tuple2f to copy the values from
     */
    protected Point2f( boolean readOnly, Tuple2f tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY() );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param readOnly
     */
    protected Point2f( boolean readOnly )
    {
        this( readOnly, 0.0f, 0.0f );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public Point2f( float x, float y )
    {
        this( false, x, y );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public Point2f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param tuple the Tuple2f to copy the values from
     */
    public Point2f( Tuple2f tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Creates a new Point2f instance.
     */
    public Point2f()
    {
        this( false );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public static Point2f newReadOnly( float x, float y )
    {
        return ( new Point2f( true, x, y ) );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public static Point2f newReadOnly( float[] values )
    {
        return ( new Point2f( true, values, null, true ) );
    }
    
    /**
     * Creates a new Point2f instance.
     * 
     * @param tuple the Tuple2f to copy the values from
     */
    public static Point2f newReadOnly( Tuple2f tuple )
    {
        return ( new Point2f( true, tuple ) );
    }
    
    /**
     * Creates a new Point2f instance.
     */
    public static Point2f newReadOnly()
    {
        return ( new Point2f( true ) );
    }
    
    /**
     * Allocates an Point2f instance from the pool.
     */
    public static Point2f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Point2f instance from the pool.
     */
    public static Point2f fromPool( float x, float y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Point2f instance from the pool.
     */
    public static Point2f fromPool( Tuple2f tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Point2f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Point2f o )
    {
        POOL.get().free( o );
    }
}
