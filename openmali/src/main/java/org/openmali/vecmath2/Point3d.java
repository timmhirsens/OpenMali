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
import org.openmali.vecmath2.pools.Point3dPool;

/**
 * A simple 3-dimensional double-Point implementation.
 * 
 * Inspired by Kenji Hiranabe's Point3d implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Point3d extends Tuple3d implements Externalizable
{
    public static final Point3d ZERO = Point3d.newReadOnly( 0d, 0d, 0d );
    
    //private static final Point3dPool POOL = new Point3dPool( 128 );
    private static final ThreadLocal<Point3dPool> POOL = new ThreadLocal<Point3dPool>()
    {
        @Override
        protected Point3dPool initialValue()
        {
            return ( new Point3dPool( 128 ) );
        }
    };
    
    private Point3d readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    public final double distanceSquared( Point3d that )
    {
        double result = 0.0d;
        
        for ( int i = 0; i < getSize(); i++ )
        {
            final double d = this.getValue( i ) - that.getValue( i );
            result += d * d;
        }
        
        return ( result );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double distance( Point3d that )
    {
        return ( FastMathd.sqrt( distanceSquared( that ) ) );
    }
    
    public final double distanceSquared( double p2x, double p2y, double p2z )
    {
        double result = 0.0d;
        
        double d = this.getX() - p2x;
        result += d * d;
        d = this.getY() - p2y;
        result += d * d;
        d = this.getZ() - p2z;
        result += d * d;
        
        return ( result );
    }
    
    public final double distance( double p2x, double p2y, double p2z )
    {
        return ( FastMathd.sqrt( distanceSquared( p2x, p2y, p2z ) ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double distanceToOriginSquared()
    {
        return ( distance( 0d, 0d, 0d ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public double distanceToOrigin()
    {
        return ( FastMathd.sqrt( distanceToOriginSquared() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point3d asReadOnly()
    {
        return ( new Point3d( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point3d getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    protected Point3d( boolean readOnly, double x, double y, double z )
    {
        super( readOnly, x, y, z );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Point3d( boolean readOnly, double[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param readOnly
     * @param tuple the Tuple3d to copy the values from
     */
    protected Point3d( boolean readOnly, Tuple3d tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY(), tuple.getZ() );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param readOnly
     */
    protected Point3d( boolean readOnly )
    {
        this( readOnly, 0.0d, 0.0d, 0.0d );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public Point3d( double x, double y, double z )
    {
        this( false, x, y, z );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public Point3d( double[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param tuple the Tuple3d to copy the values from
     */
    public Point3d( Tuple3d tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Creates a new Point3d instance.
     */
    public Point3d()
    {
        this( false );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public static Point3d newReadOnly( double x, double y, double z )
    {
        return ( new Point3d( true, x, y, z ) );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Point3d newReadOnly( double[] values )
    {
        return ( new Point3d( true, values, null, true ) );
    }
    
    /**
     * Creates a new Point3d instance.
     * 
     * @param tuple the Tuple3d to copy the values from
     */
    public static Point3d newReadOnly( Tuple3d tuple )
    {
        return ( new Point3d( true, tuple ) );
    }
    
    /**
     * Creates a new Point3d instance.
     */
    public static Point3d newReadOnly()
    {
        return ( new Point3d( true ) );
    }
    
    /**
     * Allocates an Point3d instance from the pool.
     */
    public static Point3d fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Point3d instance from the pool.
     */
    public static Point3d fromPool( double x, double y, double z )
    {
        return ( POOL.get().alloc( x, y, z ) );
    }
    
    /**
     * Allocates an Point3d instance from the pool.
     */
    public static Point3d fromPool( Tuple3d tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ() ) );
    }
    
    /**
     * Stores the given Point3d instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Point3d o )
    {
        POOL.get().free( o );
    }
}
