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

import org.openmali.vecmath2.pools.Tuple3dPool;

/**
 * A simple three-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's Tuple3d implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Tuple3d extends TupleNd< Tuple3d > implements Externalizable
{
    //private static final Tuple3dPool POOL = new Tuple3dPool( 128 );
    private static final ThreadLocal<Tuple3dPool> POOL = new ThreadLocal<Tuple3dPool>()
    {
        @Override
        protected Tuple3dPool initialValue()
        {
            return ( new Tuple3dPool( 128 ) );
        }
    };
    
    private Tuple3d readOnlyInstance = null;
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * 
     * @return itself
     */
    public final Tuple3d set( double x, double y, double z )
    {
        setX( x );
        setY( y );
        setZ( z );
        
        return ( this );
    }
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple3d setX( double x )
    {
        setValue( 0, x );
        
        return ( this );
    }
    
    /**
     * Sets the value of the y-element of this tuple.
     * 
     * @param y
     * 
     * @return itself
     */
    public final Tuple3d setY( double y )
    {
        setValue( 1, y );
        
        return ( this );
    }
    
    /**
     * Sets the value of the z-element of this tuple.
     * 
     * @param z
     * 
     * @return itself
     */
    public final Tuple3d setZ( double z )
    {
        setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * @return the value of the x-element of this tuple.
     */
    public final double getX()
    {
        return ( getValue( 0 ) );
    }
    
    /**
     * @return the value of the y-element of this tuple.
     */
    public final double getY()
    {
        return ( getValue( 1 ) );
    }
    
    /**
     * @return the value of the z-element of this tuple.
     */
    public final double getZ()
    {
        return ( getValue( 2 ) );
    }
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple3d x( double x )
    {
        setValue( 0, x );
        
        return ( this );
    }
    
    /**
     * Sets the value of the y-element of this tuple.
     * 
     * @param y
     * 
     * @return itself
     */
    public final Tuple3d y( double y )
    {
        setValue( 1, y );
        
        return ( this );
    }
    
    /**
     * Sets the value of the z-element of this tuple.
     * 
     * @param z
     * 
     * @return itself
     */
    public final Tuple3d z( double z )
    {
        setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * @return the value of the x-element of this tuple.
     */
    public final double x()
    {
        return ( getValue( 0 ) );
    }
    
    /**
     * @return the value of the y-element of this tuple.
     */
    public final double y()
    {
        return ( getValue( 1 ) );
    }
    
    /**
     * @return the value of the z-element of this tuple.
     */
    public final double z()
    {
        return ( getValue( 2 ) );
    }
    
    /**
     * Adds v to this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d addX( double v )
    {
        this.addValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d addY( double v )
    {
        this.addValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's z value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d addZ( double v )
    {
        this.addValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param x
     * @param y
     * @param z
     * 
     * @return itself
     */
    public final Tuple3d add( double x, double y, double z )
    {
        this.addValue( 0, x );
        this.addValue( 1, y );
        this.addValue( 2, z );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d subX( double v )
    {
        this.subValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d subY( double v )
    {
        this.subValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's z value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d subZ( double v )
    {
        this.subValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param x
     * @param y
     * @param z
     * 
     * @return itself
     */
    public final Tuple3d sub( double x, double y, double z )
    {
        this.subValue( 0, x );
        this.subValue( 1, y );
        this.subValue( 2, z );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's x value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d mulX( double v )
    {
        this.mulValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's y value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d mulY( double v )
    {
        this.mulValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's z value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d mulZ( double v )
    {
        this.mulValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's values with vx, vy, vz.
     * 
     * @param vx
     * @param vy
     * @param vz
     * 
     * @return itself
     */
    public final Tuple3d mul( double vx, double vy, double vz )
    {
        this.mulValue( 0, vx );
        this.mulValue( 1, vy );
        this.mulValue( 2, vz );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's x value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d divX( double v )
    {
        this.divValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's y value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d divY( double v )
    {
        this.divValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's z value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3d divZ( double v )
    {
        this.divValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's values by vx, vy, vz.
     * 
     * @param vx
     * @param vy
     * @param vz
     * 
     * @return itself
     */
    public final Tuple3d div( double vx, double vy, double vz )
    {
        this.divValue( 0, vx );
        this.divValue( 1, vy );
        this.divValue( 2, vz );
        
        return ( this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1.
     * 
     * @param factorX
     * @param factorY
     * @param factorZ
     * 
     * @return itself
     */
    public final Tuple3d scale( double factorX, double factorY, double factorZ )
    {
        mul( factorX, factorY, factorZ );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3d asReadOnly()
    {
        return ( new Tuple3d( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3d getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Returns true if the Object t1 is of type Tuple3d and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Tuple3d.
     * 
     * @param o  the Object with which the comparison is made
     * @return  true or false
     */
    @Override
    public boolean equals( Object o )
    {
        return ( ( o != null ) && ( ( o instanceof Tuple3d ) && equals( (Tuple3d)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Tuple3d clone()
    {
        try
        {
            return ( (Tuple3d)super.clone() );
        }
        catch ( CloneNotSupportedException ex )
        {
            throw new InternalError();
        }
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    protected Tuple3d( boolean readOnly, double x, double y, double z )
    {
        super( readOnly, 3 );
        
        this.values[ 0 ] = x;
        this.values[ 1 ] = y;
        this.values[ 2 ] = z;
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Tuple3d( boolean readOnly, double[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 3, copy );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param readOnly
     * @param that the Tuple3d to copy the values from
     */
    protected Tuple3d( boolean readOnly, Tuple3d that )
    {
        super( readOnly, that );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param readOnly
     */
    protected Tuple3d( boolean readOnly )
    {
        this( readOnly, 0.0d, 0.0d, 0.0d );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public Tuple3d( double x, double y, double z )
    {
        this( false, x, y, z );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public Tuple3d( double[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param that the Tuple3d to copy the values from
     */
    public Tuple3d( Tuple3d that )
    {
        this( false, that );
    }
    
    /**
     * Creates a new Tuple3d instance.
     */
    public Tuple3d()
    {
        this( false, 0d, 0d, 0d );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public static Tuple3d newReadOnly( double x, double y, double z )
    {
        return ( new Point3d( true, x, y, z ) );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Tuple3d newReadOnly( double[] values )
    {
        return ( new Point3d( true, values, null, true ) );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param tuple the Tuple3d to copy the values from
     */
    public static Tuple3d newReadOnly( Tuple3d tuple )
    {
        return ( new Point3d( true, tuple ) );
    }
    
    /**
     * Creates a new Tuple3d instance.
     */
    public static Tuple3d newReadOnly()
    {
        return ( new Point3d( true ) );
    }
    
    /**
     * Allocates an Tuple3d instance from the pool.
     */
    public static Tuple3d fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Tuple3d instance from the pool.
     */
    public static Tuple3d fromPool( double x, double y, double z )
    {
        return ( POOL.get().alloc( x, y, z ) );
    }
    
    /**
     * Allocates an Tuple3d instance from the pool.
     */
    public static Tuple3d fromPool( Tuple3d tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ() ) );
    }
    
    /**
     * Stores the given Tuple3d instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Tuple3d o )
    {
        POOL.get().free( o );
    }
}
