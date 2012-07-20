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

import org.openmali.vecmath2.pools.Tuple2dPool;

/**
 * A simple three-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's Tuple2d implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Tuple2d extends TupleNd< Tuple2d > implements Externalizable
{
    private static final long serialVersionUID = -1375260704936534068L;
    
    //private static final Tuple2dPool POOL = new Tuple2dPool( 128 );
    private static final ThreadLocal<Tuple2dPool> POOL = new ThreadLocal<Tuple2dPool>()
    {
        @Override
        protected Tuple2dPool initialValue()
        {
            return ( new Tuple2dPool( 128 ) );
        }
    };
    
    private Tuple2d readOnlyInstance = null;
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * 
     * @return itself
     */
    public final Tuple2d set( double x, double y )
    {
        setX( x );
        setY( y );
        
        return ( this );
    }
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple2d setX( double x )
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
    public final Tuple2d setY( double y )
    {
        setValue( 1, y );
        
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
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple2d x( double x )
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
    public final Tuple2d y( double y )
    {
        setValue( 1, y );
        
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
     * Adds v to this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d addX( double v )
    {
        addValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d addY( double v )
    {
        addValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param x
     * @param y
     * 
     * @return itself
     */
    public final Tuple2d add( double x, double y )
    {
        addValue( 0, x );
        addValue( 1, y );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d subX( double v )
    {
        subValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d subY( double v )
    {
        subValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param x
     * @param y
     * 
     * @return itself
     */
    public final Tuple2d sub( double x, double y )
    {
        subValue( 0, x );
        subValue( 1, y );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's x value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d mulX( double v )
    {
        mulValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's y value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d mulY( double v )
    {
        mulValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's values with vx, vy.
     * 
     * @param vx
     * @param vy
     * 
     * @return itself
     */
    public final Tuple2d mul( double vx, double vy )
    {
        mulValue( 0, vx );
        mulValue( 1, vy );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's x value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d divX( double v )
    {
        divValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's y value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2d divY( double v )
    {
        divValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's values by vx, vy, vz.
     * 
     * @param vx
     * @param vy
     * 
     * @return itself
     */
    public final Tuple2d div( double vx, double vy )
    {
        divValue( 0, vx );
        divValue( 1, vy );
        
        return ( this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1.
     * 
     * @param factorX
     * @param factorY
     * 
     * @return itself
     */
    public final Tuple2d scale( double factorX, double factorY )
    {
        mul( factorX, factorY );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2d asReadOnly()
    {
        return ( new Tuple2d( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2d getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Returns true if the Object t1 is of type Tuple3d and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Tuple2d.
     * 
     * @param tuple2  the Object with which the comparison is made
     * @return  true or false
     */
    @Override
    public boolean equals( Object tuple2 )
    {
        return ( ( tuple2 != null ) && ( ( tuple2 instanceof Tuple2d ) && equals( (Tuple2d)tuple2 ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Tuple2d clone()
    {
        try
        {
            return ( (Tuple2d)super.clone() );
        }
        catch ( CloneNotSupportedException ex )
        {
            throw new InternalError();
        }
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     */
    protected Tuple2d( boolean readOnly, double x, double y )
    {
        super( readOnly, 2 );
        
        this.values[ 0 ] = x;
        this.values[ 1 ] = y;
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 2)
     */
    protected Tuple2d( boolean readOnly, double[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 2, copy );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param readOnly
     * @param that the Tuple2d to copy the values from
     */
    protected Tuple2d( boolean readOnly, Tuple2d that )
    {
        super( readOnly, that );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param readOnly
     */
    protected Tuple2d( boolean readOnly )
    {
        this( readOnly, 0.0d, 0.0d );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public Tuple2d( double x, double y )
    {
        this( false, x, y );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public Tuple2d( double[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param that the Tuple2d to copy the values from
     */
    public Tuple2d( Tuple2d that )
    {
        this( false, that );
    }
    
    /**
     * Creates a new Tuple2d instance.
     */
    public Tuple2d()
    {
        this( false, 0d, 0d );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public static Tuple2d newReadOnly( double x, double y )
    {
        return ( new Tuple2d( true, x, y ) );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public static Tuple2d newReadOnly( double[] values )
    {
        return ( new Tuple2d( true, values, null, true ) );
    }
    
    /**
     * Creates a new Tuple2d instance.
     * 
     * @param tuple the Tuple2d to copy the values from
     */
    public static Tuple2d newReadOnly( Tuple2d tuple )
    {
        return ( new Tuple2d( true, tuple ) );
    }
    
    /**
     * Creates a new Tuple2d instance.
     */
    public static Tuple2d newReadOnly()
    {
        return ( new Tuple2d( true ) );
    }
    
    /**
     * Allocates an Tuple2d instance from the pool.
     */
    public static Tuple2d fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Tuple2d instance from the pool.
     */
    public static Tuple2d fromPool( double x, double y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Tuple2d instance from the pool.
     */
    public static Tuple2d fromPool( Tuple2d tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Tuple2d instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Tuple2d o )
    {
        POOL.get().free( o );
    }
}
