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

import org.openmali.vecmath2.pools.Tuple2fPool;

/**
 * A simple three-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's Tuple2f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Tuple2f extends TupleNf< Tuple2f > implements Externalizable, TupleInterface< Tuple2f >
{
    private static final long serialVersionUID = -1375260704936534068L;
    
    //private static final Tuple2fPool POOL = new Tuple2fPool( 128 );
    private static final ThreadLocal<Tuple2fPool> POOL = new ThreadLocal<Tuple2fPool>()
    {
        @Override
        protected Tuple2fPool initialValue()
        {
            return ( new Tuple2fPool( 128 ) );
        }
    };
    
    private Tuple2f readOnlyInstance = null;
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * 
     * @return itself
     */
    public final Tuple2f set( float x, float y )
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
    public final Tuple2f setX( float x )
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
    public final Tuple2f setY( float y )
    {
        setValue( 1, y );
        
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
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple2f x( float x )
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
    public final Tuple2f y( float y )
    {
        setValue( 1, y );
        
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
     * Adds v to this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2f addX( float v )
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
    public final Tuple2f addY( float v )
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
    public final Tuple2f add( float x, float y )
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
    public final Tuple2f subX( float v )
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
    public final Tuple2f subY( float v )
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
    public final Tuple2f sub( float x, float y )
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
    public final Tuple2f mulX( float v )
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
    public final Tuple2f mulY( float v )
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
    public final Tuple2f mul( float vx, float vy )
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
    public final Tuple2f divX( float v )
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
    public final Tuple2f divY( float v )
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
    public final Tuple2f div( float vx, float vy )
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
    public final Tuple2f scale( float factorX, float factorY )
    {
        mul( factorX, factorY );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2f asReadOnly()
    {
        return ( new Tuple2f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Returns true if the Object t1 is of type Tuple3f and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Tuple2f.
     * 
     * @param tuple2  the Object with which the comparison is made
     * @return  true or false
     */ 
    @Override
    public boolean equals(Object tuple2)
    {
        return ( ( tuple2 != null ) && ( ( tuple2 instanceof Tuple2f ) && equals( (Tuple2f)tuple2 ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Tuple2f clone()
    {
        return ( new Tuple2f( this ) );
    }
    
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     */
    protected Tuple2f( boolean readOnly, float x, float y )
    {
        super( readOnly, 2 );
        
        this.values[ 0 ] = x;
        this.values[ 1 ] = y;
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 2)
     */
    protected Tuple2f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 2, copy );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param readOnly
     * @param that the Tuple2f to copy the values from
     */
    protected Tuple2f( boolean readOnly, Tuple2f that )
    {
        super( readOnly, that );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param readOnly
     */
    protected Tuple2f( boolean readOnly )
    {
        this( readOnly, 0.0f, 0.0f );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public Tuple2f( float x, float y )
    {
        this( false, x, y );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public Tuple2f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param that the Tuple2f to copy the values from
     */
    public Tuple2f( Tuple2f that )
    {
        this( false, that );
    }
    
    /**
     * Creates a new Tuple2f instance.
     */
    public Tuple2f()
    {
        this( false, 0f, 0f );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public static Tuple2f newReadOnly( float x, float y )
    {
        return ( new Tuple2f( true, x, y ) );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public static Tuple2f newReadOnly( float[] values )
    {
        return ( new Tuple2f( true, values, null, true ) );
    }
    
    /**
     * Creates a new Tuple2f instance.
     * 
     * @param tuple the Tuple2f to copy the values from
     */
    public static Tuple2f newReadOnly( Tuple2f tuple )
    {
        return ( new Tuple2f( true, tuple ) );
    }
    
    /**
     * Creates a new Tuple2f instance.
     */
    public static Tuple2f newReadOnly()
    {
        return ( new Tuple2f( true ) );
    }
    
    /**
     * Allocates an Tuple2f instance from the pool.
     */
    public static Tuple2f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Tuple2f instance from the pool.
     */
    public static Tuple2f fromPool( float x, float y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Tuple2f instance from the pool.
     */
    public static Tuple2f fromPool( Tuple2f tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Tuple2f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Tuple2f o )
    {
        POOL.get().free( o );
    }
}
