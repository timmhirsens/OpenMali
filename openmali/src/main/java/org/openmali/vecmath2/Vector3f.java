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
import org.openmali.vecmath2.pools.Vector3fPool;

/**
 * A simple 3-dimensional float-Vector implementation.
 * 
 * Inspired by Kenji Hiranabe's Vector3f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Vector3f extends Tuple3f implements Externalizable, VectorInterface< Tuple3f, Vector3f >
{
    private static final long serialVersionUID = -8110150970595936075L;
    
    public static final Vector3f ZERO = Vector3f.newReadOnly( 0f, 0f, 0f );
    
    public static final Vector3f POSITIVE_X_AXIS = Vector3f.newReadOnly( +1f, 0f, 0f );
    public static final Vector3f NEGATIVE_X_AXIS = Vector3f.newReadOnly( -1f, 0f, 0f );
    public static final Vector3f POSITIVE_Y_AXIS = Vector3f.newReadOnly( 0f, +1f, 0f );
    public static final Vector3f NEGATIVE_Y_AXIS = Vector3f.newReadOnly( 0f, -1f, 0f );
    public static final Vector3f POSITIVE_Z_AXIS = Vector3f.newReadOnly( 0f, 0f, +1f );
    public static final Vector3f NEGATIVE_Z_AXIS = Vector3f.newReadOnly( 0f, 0f, -1f );
    
    //private static final Vector3fPool POOL = new Vector3fPool( 128 );
    private static final ThreadLocal<Vector3fPool> POOL = new ThreadLocal<Vector3fPool>()
    {
        @Override
        protected Vector3fPool initialValue()
        {
            return ( new Vector3fPool( 128 ) );
        }
    };
    
    private Vector3f readOnlyInstance = null;
    
    /**
     * {@inheritDoc}
     */
    public final float lengthSquared()
    {
        float result = 0.0f;
        
        for ( int i = 0; i < getSize(); i++ )
        {
            result += this.getValue( i ) * this.getValue( i );
        }
        
        return ( result );
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
    public final Vector3f normalize()
    {
        final float l = length();
        
        // zero-div may occur.
        this.divX( l );
        this.divY( l );
        this.divZ( l );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector3f normalize( Vector3f vector )
    {
        set( vector );
        normalize();
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final Vector3f cross( Vector3f v1, Vector3f v2 )
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
    public final float dot( Vector3f v2 )
    {
        return ( this.getX() * v2.getX() + this.getY() * v2.getY() + this.getZ() * v2.getZ() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float angle( Vector3f v2 )
    {
        // return (double)Math.acos(dot(v1)/v1.length()/v.length());
        // Numerically, near 0 and PI are very bad condition for acos.
        // In 3-space, |atan2(sin,cos)| is much stable.
        
        /*
        final float xx = this.getX() * v2.getZ() - this.getZ() * v2.getY();
        final float yy = this.getZ() * v2.getX() - this.getX() * v2.getZ();
        final float zz = this.getX() * v2.getY() - this.getY() * v2.getX();
        final float cross = FastMath.sqrt( xx * xx + yy * yy + zz * zz );
        
        return ( Math.abs( FastMath.atan2( cross, dot( v2 ) ) ) );
        */
        float vDot = this.dot( v2 ) / ( this.length() * v2.length() );
        
        if ( vDot < -1.0f )
            vDot = -1.0f;
        if ( vDot >  1.0f )
            vDot =  1.0f;
        
        return ( FastMath.acos( vDot ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3f clone()
    {
        return ( new Vector3f( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3f asReadOnly()
    {
        return ( new Vector3f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    protected Vector3f( boolean readOnly, float x, float y, float z )
    {
        super( readOnly, x, y, z );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Vector3f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param readOnly
     * @param tuple the Tuple3f to copy the values from
     */
    protected Vector3f( boolean readOnly, Tuple3f tuple )
    {
        this( readOnly, tuple.getX(), tuple.getY(), tuple.getZ() );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param readOnly
     */
    protected Vector3f( boolean readOnly )
    {
        this( readOnly, 0.0f, 0.0f, 0.0f );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public Vector3f( float x, float y, float z )
    {
        this( false, x, y, z );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public Vector3f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param tuple the Tuple3f to copy the values from
     */
    public Vector3f( Tuple3f tuple )
    {
        this( false, tuple );
    }
    
    /**
     * Creates a new Vector3f instance.
     */
    public Vector3f()
    {
        this( false );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public static Vector3f newReadOnly( float x, float y, float z )
    {
        return ( new Vector3f( true, x, y, z ) );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Vector3f newReadOnly( float[] values )
    {
        return ( new Vector3f( true, values, null, true ) );
    }
    
    /**
     * Creates a new Vector3f instance.
     * 
     * @param tuple the Tuple3f to copy the values from
     */
    public static Vector3f newReadOnly( Tuple3f tuple )
    {
        return ( new Vector3f( true, tuple ) );
    }
    
    /**
     * Creates a new Vector3f instance.
     */
    public static Vector3f newReadOnly()
    {
        return ( new Vector3f( true ) );
    }
    
    /**
     * Allocates an Vector3f instance from the pool.
     */
    public static Vector3f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Vector3f instance from the pool.
     */
    public static Vector3f fromPool( float x, float y, float z )
    {
        return ( POOL.get().alloc( x, y, z ) );
    }
    
    /**
     * Allocates an Vector3f instance from the pool.
     */
    public static Vector3f fromPool( Tuple3f tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ() ) );
    }
    
    /**
     * Stores the given Vector3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Vector3f o )
    {
        POOL.get().free( o );
    }
}
