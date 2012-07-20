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

import org.openmali.vecmath2.pools.Tuple3fPool;

/**
 * A simple three-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's Tuple3f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Tuple3f extends TupleNf< Tuple3f > implements Externalizable, TupleInterface< Tuple3f >
{
    private static final long serialVersionUID = 6808120658881162114L;
    
    //private static final Tuple3fPool POOL = new Tuple3fPool( 128 );
    private static final ThreadLocal<Tuple3fPool> POOL = new ThreadLocal<Tuple3fPool>()
    {
        @Override
        protected Tuple3fPool initialValue()
        {
            return ( new Tuple3fPool( 128 ) );
        }
    };
    
    private Tuple3f readOnlyInstance = null;
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * 
     * @return itself
     */
    public final Tuple3f set( float x, float y, float z )
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
    public final Tuple3f setX( float x )
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
    public final Tuple3f setY( float y )
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
    public final Tuple3f setZ( float z )
    {
        setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * @return the value of the x-element of this tuple.
     */
    public final float getX()
    {
        return ( getValue( 0 ) );
    }
    
    /**
     * @return the value of the y-element of this tuple.
     */
    public final float getY()
    {
        return ( getValue( 1 ) );
    }
    
    /**
     * @return the value of the z-element of this tuple.
     */
    public final float getZ()
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
    public final Tuple3f x( float x )
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
    public final Tuple3f y( float y )
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
    public final Tuple3f z( float z )
    {
        setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * @return the value of the x-element of this tuple.
     */
    public final float x()
    {
        return ( getValue( 0 ) );
    }
    
    /**
     * @return the value of the y-element of this tuple.
     */
    public final float y()
    {
        return ( getValue( 1 ) );
    }
    
    /**
     * @return the value of the z-element of this tuple.
     */
    public final float z()
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
    public final Tuple3f addX( float v )
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
    public final Tuple3f addY( float v )
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
    public final Tuple3f addZ( float v )
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
    public final Tuple3f add( float x, float y, float z )
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
    public final Tuple3f subX( float v )
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
    public final Tuple3f subY( float v )
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
    public final Tuple3f subZ( float v )
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
    public final Tuple3f sub( float x, float y, float z )
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
    public final Tuple3f mulX( float v )
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
    public final Tuple3f mulY( float v )
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
    public final Tuple3f mulZ( float v )
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
    public final Tuple3f mul( float vx, float vy, float vz )
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
    public final Tuple3f divX( float v )
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
    public final Tuple3f divY( float v )
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
    public final Tuple3f divZ( float v )
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
    public final Tuple3f div( float vx, float vy, float vz )
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
    public final Tuple3f scale( float factorX, float factorY, float factorZ )
    {
        mul( factorX, factorY, factorZ );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3f asReadOnly()
    {
        return ( new Tuple3f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Returns true if the Object t1 is of type Tuple3f and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Tuple3f.
     * 
     * @param o  the Object with which the comparison is made
     * @return  true or false
     */ 
    @Override
    public boolean equals( Object o )
    {
        return ( ( o != null ) && ( ( o instanceof Tuple3f ) && equals( (Tuple3f)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Tuple3f clone()
    {
        return ( new Tuple3f( this ) );
    }
    
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    protected Tuple3f( boolean readOnly, float x, float y, float z )
    {
        super( readOnly, 3 );
        
        this.values[ 0 ] = x;
        this.values[ 1 ] = y;
        this.values[ 2 ] = z;
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Tuple3f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 3, copy );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param readOnly
     * @param that the Tuple3f to copy the values from
     */
    protected Tuple3f( boolean readOnly, Tuple3f that )
    {
        super( readOnly, that );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param readOnly
     */
    protected Tuple3f( boolean readOnly )
    {
        this( readOnly, 0.0f, 0.0f, 0.0f );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public Tuple3f( float x, float y, float z )
    {
        this( false, x, y, z );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public Tuple3f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param that the Tuple3f to copy the values from
     */
    public Tuple3f( Tuple3f that )
    {
        this( false, that );
    }
    
    /**
     * Creates a new Tuple3f instance.
     */
    public Tuple3f()
    {
        this( false, 0f, 0f, 0f );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public static Tuple3f newReadOnly( float x, float y, float z )
    {
        return ( new Tuple3f( true, x, y, z ) );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Tuple3f newReadOnly( float[] values )
    {
        return ( new Tuple3f( true, values, null, true ) );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param tuple the Tuple3f to copy the values from
     */
    public static Tuple3f newReadOnly( Tuple3f tuple )
    {
        return ( new Tuple3f( true, tuple ) );
    }
    
    /**
     * Creates a new Tuple3f instance.
     */
    public static Tuple3f newReadOnly()
    {
        return ( new Tuple3f( true ) );
    }
    
    /**
     * Allocates an Tuple3f instance from the pool.
     */
    public static Tuple3f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Tuple3f instance from the pool.
     */
    public static Tuple3f fromPool( float x, float y, float z )
    {
        return ( POOL.get().alloc( x, y, z ) );
    }
    
    /**
     * Allocates an Tuple3f instance from the pool.
     */
    public static Tuple3f fromPool( Tuple3f tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ() ) );
    }
    
    /**
     * Stores the given Tuple3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Tuple3f o )
    {
        POOL.get().free( o );
    }
}
