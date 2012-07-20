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
import org.openmali.vecmath2.pools.Point3fPool;

/**
 * A simple 3-dimensional float-Point implementation.
 * 
 * Inspired by Kenji Hiranabe's Point3f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Point3f extends Tuple3f implements Externalizable, PointInterface< Tuple3f, Point3f >
{
    private static final long serialVersionUID = -6199186808152383088L;
    
    public static final Point3f ZERO = Point3f.newReadOnly( 0f, 0f, 0f );
    
    //private static final Point3fPool POOL = new Point3fPool( 128 );
    private static final ThreadLocal<Point3fPool> POOL = new ThreadLocal<Point3fPool>()
    {
        @Override
        protected Point3fPool initialValue()
        {
            return ( new Point3fPool( 128 ) );
        }
    };
    
    private Point3f readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    public final float distanceSquared( Point3f that )
    {
        float result = 0.0f;
        
        for ( int i = 0; i < getSize(); i++ )
        {
            final float d = this.getValue( i ) - that.getValue( i );
            result += d * d;
        }
        
        return ( result );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float distance( Point3f that )
    {
        return ( FastMath.sqrt( distanceSquared( that ) ) );
    }
    
    public final float distanceSquared( float p2x, float p2y, float p2z )
    {
        float result = 0.0f;
        
        float d = this.getX() - p2x;
        result += d * d;
        d = this.getY() - p2y;
        result += d * d;
        d = this.getZ() - p2z;
        result += d * d;
        
        return ( result );
    }
    
    public final float distance( float p2x, float p2y, float p2z )
    {
        return ( FastMath.sqrt( distanceSquared( p2x, p2y, p2z ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float distanceToOriginSquared()
    {
        return ( distance( 0f, 0f, 0f ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public float distanceToOrigin()
    {
        return ( FastMath.sqrt( distanceToOriginSquared() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point3f clone()
    {
        return ( new Point3f( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point3f asReadOnly()
    {
        return ( new Point3f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point3f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    protected Point3f( boolean readOnly, float x, float y, float z )
    {
        super( readOnly, x, y, z );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Point3f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param readOnly
     * @param tuple the Tuple3f to copy the values from
     */
    protected Point3f( boolean readOnly, Tuple3f tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY(), tuple.getZ() );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param readOnly
     */
    protected Point3f( boolean readOnly )
    {
        this( readOnly, 0.0f, 0.0f, 0.0f );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public Point3f( float x, float y, float z )
    {
        this( false, x, y, z );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public Point3f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param tuple the Tuple3f to copy the values from
     */
    public Point3f( Tuple3f tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Creates a new Point3f instance.
     */
    public Point3f()
    {
        this( false );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public static Point3f newReadOnly( float x, float y, float z )
    {
        return ( new Point3f( true, x, y, z ) );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Point3f newReadOnly( float[] values )
    {
        return ( new Point3f( true, values, null, true ) );
    }
    
    /**
     * Creates a new Point3f instance.
     * 
     * @param tuple the Tuple3f to copy the values from
     */
    public static Point3f newReadOnly( Tuple3f tuple )
    {
        return ( new Point3f( true, tuple ) );
    }
    
    /**
     * Creates a new Point3f instance.
     */
    public static Point3f newReadOnly()
    {
        return ( new Point3f( true ) );
    }
    
    /**
     * Allocates an Point3f instance from the pool.
     */
    public static Point3f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Point3f instance from the pool.
     */
    public static Point3f fromPool( float x, float y, float z )
    {
        return ( POOL.get().alloc( x, y, z ) );
    }
    
    /**
     * Allocates an Point3f instance from the pool.
     */
    public static Point3f fromPool( Tuple3f tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ() ) );
    }
    
    /**
     * Stores the given Point3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Point3f o )
    {
        POOL.get().free( o );
    }
}
