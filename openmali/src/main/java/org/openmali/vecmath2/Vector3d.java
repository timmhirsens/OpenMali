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
import org.openmali.vecmath2.pools.Vector3dPool;

/**
 * A simple 3-dimensional double-Vector implementation.
 * 
 * Inspired by Kenji Hiranabe's Vector3d implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Vector3d extends Tuple3d implements Externalizable
{
    private static final long serialVersionUID = -8110150970595936075L;
    
    public static final Vector3d ZERO = Vector3d.newReadOnly( 0d, 0d, 0d );
    
    public static final Vector3d POSITIVE_X_AXIS = Vector3d.newReadOnly( +1d, 0d, 0d );
    public static final Vector3d NEGATIVE_X_AXIS = Vector3d.newReadOnly( -1d, 0d, 0d );
    public static final Vector3d POSITIVE_Y_AXIS = Vector3d.newReadOnly( 0d, +1d, 0d );
    public static final Vector3d NEGATIVE_Y_AXIS = Vector3d.newReadOnly( 0d, -1d, 0d );
    public static final Vector3d POSITIVE_Z_AXIS = Vector3d.newReadOnly( 0d, 0d, +1d );
    public static final Vector3d NEGATIVE_Z_AXIS = Vector3d.newReadOnly( 0d, 0d, -1d );
    
    //private static final Vector3dPool POOL = new Vector3dPool( 128 );
    private static final ThreadLocal<Vector3dPool> POOL = new ThreadLocal<Vector3dPool>()
    {
        @Override
        protected Vector3dPool initialValue()
        {
            return ( new Vector3dPool( 128 ) );
        }
    };
    
    private Vector3d readOnlyInstance = null;
    
    
    /**
     * {@inheritDoc}
     */
    public final double lengthSquared()
    {
        double result = 0.0d;
        
        for ( int i = 0; i < getSize(); i++ )
        {
            result += this.getValue( i ) * this.getValue( i );
        }
        
        return ( result );
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
    public final Vector3d normalize()
    {
        final double l = length();
        
        // zero-div may occur.
        this.divX( l );
        this.divY( l );
        this.divZ( l );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector3d normalize( Vector3d vector )
    {
        set( vector );
        normalize();
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector3d cross( Vector3d v1, Vector3d v2 )
    {
        // store in tmp once for aliasing-safty
        // i.e. safe when a.cross(a, b)
        set( v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
             v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
             v1.getX() * v2.getY() - v1.getY() * v2.getX()
           );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double dot( Vector3d v2 )
    {
        return ( this.getX() * v2.getX() + this.getY() * v2.getY() + this.getZ() * v2.getZ() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double angle( Vector3d v2 )
    {
        // return (double)Math.acos(dot(v1)/v1.length()/v.length());
        // Numerically, near 0 and PI are very bad condition for acos.
        // In 3-space, |atan2(sin,cos)| is much stable.
        
        /*
        final double xx = this.getX() * v2.getZ() - this.getZ() * v2.getY();
        final double yy = this.getZ() * v2.getX() - this.getX() * v2.getZ();
        final double zz = this.getX() * v2.getY() - this.getY() * v2.getX();
        final double cross = FastMath.sqrt( xx * xx + yy * yy + zz * zz );
        
        return ( Math.abs( FastMath.atan2( cross, dot( v2 ) ) ) );
        */
        double vDot = this.dot( v2 ) / ( this.length() * v2.length() );
        
        if (vDot < -1.0d)
	    vDot = -1.0d;
        if ( vDot >  1.0d )
            vDot =  1.0d;
        
        return ( FastMathd.acos( vDot ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3d asReadOnly()
    {
        return ( new Vector3d( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3d getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    protected Vector3d( boolean readOnly, double x, double y, double z )
    {
        super( readOnly, x, y, z );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Vector3d( boolean readOnly, double[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param readOnly
     * @param tuple the Tuple3d to copy the values from
     */
    protected Vector3d( boolean readOnly, Tuple3d tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY(), tuple.getZ() );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param readOnly
     */
    protected Vector3d( boolean readOnly )
    {
        this( readOnly, 0.0d, 0.0d, 0.0d );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public Vector3d( double x, double y, double z )
    {
        this( false, x, y, z );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public Vector3d( double[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param tuple the Tuple3d to copy the values from
     */
    public Vector3d( Tuple3d tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Creates a new Vector3d instance.
     */
    public Vector3d()
    {
        this( false );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public static Vector3d newReadOnly( double x, double y, double z )
    {
        return ( new Vector3d( true, x, y, z ) );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Vector3d newReadOnly( double[] values )
    {
        return ( new Vector3d( true, values, null, true ) );
    }
    
    /**
     * Creates a new Vector3d instance.
     * 
     * @param tuple the Tuple3d to copy the values from
     */
    public static Vector3d newReadOnly( Tuple3d tuple )
    {
        return ( new Vector3d( true, tuple ) );
    }
    
    /**
     * Creates a new Vector3d instance.
     */
    public static Vector3d newReadOnly()
    {
        return ( new Vector3d( true ) );
    }
    
    /**
     * Allocates an Vector3d instance from the pool.
     */
    public static Vector3d fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Vector3d instance from the pool.
     */
    public static Vector3d fromPool( double x, double y, double z )
    {
        return ( POOL.get().alloc( x, y, z ) );
    }
    
    /**
     * Allocates an Vector3d instance from the pool.
     */
    public static Vector3d fromPool( Tuple3d tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ() ) );
    }
    
    /**
     * Stores the given Vector3d instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Vector3d o )
    {
        POOL.get().free( o );
    }
}
