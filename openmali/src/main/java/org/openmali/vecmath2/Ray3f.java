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
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openmali.vecmath2.pools.Ray3fPool;

/**
 * Simple three-dimensional ray implementation.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Ray3f implements Externalizable
{
    private static final long serialVersionUID = 5553385662401846014L;
    
    //private static final Ray3fPool POOL = new Ray3fPool( 32 );
    private static final ThreadLocal<Ray3fPool> POOL = new ThreadLocal<Ray3fPool>()
    {
        @Override
        protected Ray3fPool initialValue()
        {
            return ( new Ray3fPool( 128 ) );
        }
    };
    
    private final Point3f origin;
    private final Vector3f direction;
    
    /**
     * @return Is this ray a read-only one?
     */
    public final boolean isReadOnly()
    {
        return ( direction.isReadOnly() );
    }
    
    /**
     * Marks this ray non-dirty.
     * Any value-manipulation will mark it dirty again.
     * 
     * @return the old value
     */
    public final boolean setClean()
    {
        final boolean oldValue = isDirty();
        
        origin.setClean();
        direction.setClean();
        
        return ( oldValue );
    }
    
    /**
     * @return This ray's dirty-flag
     */
    public final boolean isDirty()
    {
        return ( origin.isDirty() || direction.isDirty() );
    }
    
    /**
     * Sets origin and direction to zero.
     */
    public final void setZero()
    {
        this.origin.setZero();
        this.direction.setZero();
    }
    
    /**
     * Sets the ray's origin.
     * 
     * @param x
     * @param y
     * @param z
     * 
     * @return itself
     */
    public final Ray3f setOrigin( float x, float y, float z )
    {
        this.origin.set( x, y, z );
        
        return ( this );
    }
    
    /**
     * Sets the ray's origin.
     * 
     * @param origin new origin
     * 
     * @return itself
     */
    public final Ray3f setOrigin( Tuple3f origin )
    {
        return ( setOrigin( origin.getX(), origin.getY(), origin.getZ() ) );
    }
    
    /**
     * @return the ray's origin
     */
    public final Point3f getOrigin()
    {
        return ( origin );
    }
    
    /**
     * Sets the ray's direction.
     * 
     * @param x
     * @param y
     * @param z
     * 
     * @return itself
     */
    public final Ray3f setDirection( float x, float y, float z )
    {
        this.direction.set( x, y, z );
        
        return ( this );
    }
    
    /**
     * Sets the ray's direction.
     * 
     * @param direction new direction
     * 
     * @return itself
     */
    public final Ray3f setDirection( Tuple3f direction )
    {
        return ( setDirection( direction.getX(), direction.getY(), direction.getZ() ) );
    }
    
    /**
     * @return the ray's direction
     */
    public final Vector3f getDirection()
    {
        return ( direction );
    }
    
    /**
     * @return the ray's length
     * <i>same as lengthSquared()</i>
     */
    public final float getLengthSquared()
    {
        return ( direction.lengthSquared() );
    }
    
    /**
     * @return the ray's length
     * <i>same as getLengthSquared()</i>
     */
    public final float lengthSquared()
    {
        return ( direction.lengthSquared() );
    }
    
    /**
     * @return the ray's length
     * <i>same as length()</i>
     */
    public final float getLength()
    {
        return ( direction.length() );
    }
    
    /**
     * @return the ray's length
     * <i>same as getLength()</i>
     */
    public final float length()
    {
        return ( direction.length() );
    }
    
    /**
     * Creates a clone.
     */
    @Override
    public Ray3f clone()
    {
        return ( new Ray3f( this ) );
    }
    
    /**
     * Sets this ray to the passed parameters.
     * 
     * @param ox the new origin point's x-coordinate
     * @param oy the new origin point's y-coordinate
     * @param oz the new origin point's z-coordinate
     * @param dx the new direction vector's x-coordinate
     * @param dy the new direction vector's y-coordinate
     * @param dz the new direction vector's z-coordinate
     * 
     * @return itself
     */
    public final Ray3f set( float ox, float oy, float oz, float dx, float dy, float dz )
    {
        this.origin.set( ox, oy, oz );
        this.direction.set( dx, dy, dz );
        
        return ( this );
    }
    
    /**
     * Sets this ray to the passed parameters.
     * 
     * @param origin the new origin point
     * @param direction the new direction vector
     * 
     * @return itself
     */
    public final Ray3f set( Tuple3f origin, Vector3f direction )
    {
        this.origin.set( origin );
        this.direction.set( direction );
        
        return ( this );
    }
    
    /**
     * Sets this ray to be equal to the passed one.
     * 
     * @return itself
     */
    public final Ray3f set( Ray3f ray )
    {
        return ( set( ray.getOrigin(), ray.getDirection() ) );
    }
    
    /**
     * Checks if the given ray equals this one.
     * 
     * @param ray the ray to test for equality
     */
    public boolean equals( Ray3f ray )
    {
        return ( this.origin.equals( ray.origin ) && this.direction.equals( ray.direction ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( o instanceof Ray3f )
            return ( equals( (Ray3f)o ) );
        
        return ( false );
    }
    
    /**
     * @return a String representation of this ray
     */
    @Override
    public String toString()
    {
        return ( this.getClass().getSimpleName() + ": " +
                "origin=(" + origin.getX() + ", " + origin.getY() + ", " + origin.getZ() + "), " +
                "direction=(" + direction.getX() + ", " + direction.getY() + ", " + direction.getZ() + ")");
    }
    
    /**
     * Serializes this instanc'es data into the byte array.
     * 
     * @param pos
     * @param buffer
     * 
     * @return the incremented position
     */
    public int serialize( int pos, final byte[] buffer )
    {
        origin.serialize( pos, buffer );
        pos += origin.getSerializationBufferSize();
        
        direction.serialize( pos, buffer );
        pos += direction.getSerializationBufferSize();
        
        return ( pos );
    }
    
    /**
     * Deserializes this instanc'es data from the byte array.
     * 
     * @param pos
     * @param buffer
     * 
     * @return the incremented position
     */
    public int deserialize( int pos, final byte[] buffer )
    {
        origin.deserialize( pos, buffer );
        pos += origin.getSerializationBufferSize();
        
        direction.deserialize( pos, buffer );
        pos += direction.getSerializationBufferSize();
        
        return ( pos );
    }
    
    /**
     * @return the necessary size for a serialization byte array.
     */
    protected int getSerializationBufferSize()
    {
        return ( origin.getSerializationBufferSize() + direction.getSerializationBufferSize() );
    }
    
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        final byte[] buffer = new byte[ getSerializationBufferSize() ];
        
        serialize( 0, buffer );
        
        out.write( buffer );
    }
    
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        final byte[] buffer = new byte[ getSerializationBufferSize() ];
        
        in.read( buffer );
        
        deserialize( 0, buffer );
    }
    
    
    /**
     * Creates a new Ray3f.
     * 
     * @param readOnly
     * @param origX
     * @param origY
     * @param origZ
     * @param direcX
     * @param direcY
     * @param direcZ
     */
    protected Ray3f( boolean readOnly, float origX, float origY, float origZ, float direcX, float direcY, float direcZ )
    {
        super();
        
        this.origin = new Point3f( readOnly, origX, origY, origZ );
        this.direction = new Vector3f( readOnly, direcX, direcY, direcZ );
    }
    
    /**
     * Creates a new Ray3f.
     * 
     * @param readOnly
     * @param origin the new origin point
     * @param direction the new direction vector
     */
    protected Ray3f( boolean readOnly, Point3f origin, Vector3f direction )
    {
        this( readOnly, origin.getX(), origin.getY(), origin.getZ(), direction.getX(), direction.getY(), direction.getZ() );
    }
    
    /**
     * Creates a new Ray3f.
     * 
     * @param readOnly
     */
    protected Ray3f( boolean readOnly )
    {
        this( readOnly, 0f, 0f, 0f, 0f, 0f, 0f );
    }
    
    /**
     * Clone constructor.
     * 
     * @param readOnly
     * @param template
     */
    protected Ray3f( boolean readOnly, Ray3f template )
    {
        this( readOnly, template.getOrigin(), template.getDirection() );
    }
    
    /**
     * Creates a new Ray3f.
     * 
     * @param origX
     * @param origY
     * @param origZ
     * @param direcX
     * @param direcY
     * @param direcZ
     */
    public Ray3f( float origX, float origY, float origZ, float direcX, float direcY, float direcZ )
    {
        this( false, origX, origY, origZ, direcX, direcY, direcZ );
    }
    
    /**
     * Creates a new Ray3f.
     * 
     * @param origin the new origin point
     * @param direction the new direction vector
     */
    public Ray3f( Point3f origin, Vector3f direction )
    {
        this( false, origin, direction );
    }
    
    /**
     * Creates a new Ray3f.
     */
    public Ray3f()
    {
        this( false );
    }
    
    /**
     * Clone constructor.
     * 
     * @param template
     */
    public Ray3f( Ray3f template )
    {
        this( false, template );
    }
    
    /**
     * Creates a new Ray3f.
     * 
     * @param origX
     * @param origY
     * @param origZ
     * @param direcX
     * @param direcY
     * @param direcZ
     */
    public static Ray3f newReadOnly( float origX, float origY, float origZ, float direcX, float direcY, float direcZ )
    {
        return ( new Ray3f( true, origX, origY, origZ, direcX, direcY, direcZ ) );
    }
    
    /**
     * Creates a new Ray3f.
     * 
     * @param origin the new origin point
     * @param direction the new direction vector
     */
    public static Ray3f newReadOnly( Point3f origin, Vector3f direction )
    {
        return ( new Ray3f( true, origin, direction ) );
    }
    
    /**
     * Creates a new Ray3f.
     */
    public static Ray3f newReadOnly()
    {
        return ( new Ray3f( true ) );
    }
    
    /**
     * Clone constructor.
     * 
     * @param template
     */
    public static Ray3f newReadOnly( Ray3f template )
    {
        return ( new Ray3f( true, template ) );
    }
    
    /**
     * Allocates an Ray3f instance from the pool.
     */
    public static Ray3f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Ray3f instance from the pool.
     */
    public static Ray3f fromPool( float ox, float oy, float oz, float dx, float dy, float dz )
    {
        return ( POOL.get().alloc( ox, oy, oz, dx, dy, dz ) );
    }
    
    /**
     * Allocates an Ray3f instance from the pool.
     */
    public static Ray3f fromPool( Tuple3f origin, Vector3f direction )
    {
        return ( POOL.get().alloc( origin, direction ) );
    }
    
    /**
     * Allocates an Ray3f instance from the pool.
     */
    public static Ray3f fromPool( Ray3f ray )
    {
        return ( fromPool( ray.getOrigin(), ray.getDirection() ) );
    }
    
    /**
     * Stores the given Ray3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Ray3f o )
    {
        POOL.get().free( o );
    }
}
