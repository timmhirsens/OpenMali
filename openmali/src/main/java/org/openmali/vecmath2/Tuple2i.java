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

import org.openmali.vecmath2.pools.Tuple2iPool;

/**
 * A simple two-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's Tuple2i implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Tuple2i extends TupleNi< Tuple2i >
{
    private static final long serialVersionUID = 586023996166921455L;
    
    //private static final Tuple2iPool POOL = new Tuple2iPool( 128 );
    private static final ThreadLocal<Tuple2iPool> POOL = new ThreadLocal<Tuple2iPool>()
    {
        @Override
        protected Tuple2iPool initialValue()
        {
            return ( new Tuple2iPool( 128 ) );
        }
    };
    
    private Tuple2i readOnlyInstance = null;
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple2i setX( int x )
    {
        this.setValue( 0, x );
        
        return ( this );
    }
    
    /**
     * Sets the value of the y-element of this tuple.
     * 
     * @param y
     * 
     * @return itself
     */
    public final Tuple2i setY( int y )
    {
        this.setValue( 1, y );
        
        return ( this );
    }
    
    /**
     * @return the value of the x-element of this tuple.
     */
    public final int getX()
    {
        return ( this.getValue( 0 ) );
    }
    
    /**
     * @return the value of the y-element of this tuple.
     */
    public final int getY()
    {
        return ( this.getValue( 1 ) );
    }
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple2i x( int x )
    {
        this.setValue( 0, x );
        
        return ( this );
    }
    
    /**
     * Sets the value of the y-element of this tuple.
     * 
     * @param y
     * 
     * @return itself
     */
    public final Tuple2i y( int y )
    {
        this.setValue( 1, y );
        
        return ( this );
    }
    
    /**
     * @return the value of the x-element of this tuple.
     */
    public final int x()
    {
        return ( this.getValue( 0 ) );
    }
    
    /**
     * @return the value of the y-element of this tuple.
     */
    public final int y()
    {
        return ( this.getValue( 1 ) );
    }
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * 
     * @return itself
     */
    public final Tuple2i set( int x, int y )
    {
        setX( x );
        setY( y );
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i addX( int v )
    {
        this.values[ roTrick + 0 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i addY( int v )
    {
        this.values[ roTrick + 1 ] += v;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple2i add( int x, int y )
    {
        this.values[ roTrick + 0 ] += x;
        this.values[ roTrick + 1 ] += y;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i subX( int v )
    {
        this.values[ roTrick + 0 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i subY( int v )
    {
        this.values[ roTrick + 1 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple2i sub( int x, int y )
    {
        this.values[ roTrick + 0 ] -= x;
        this.values[ roTrick + 1 ] -= y;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's x value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i mulX( int v )
    {
        this.values[ roTrick + 0 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's y value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i mulY( int v )
    {
        this.values[ roTrick + 1 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple2i mul( int vx, int vy )
    {
        this.values[ roTrick + 0 ] *= vx;
        this.values[ roTrick + 1 ] *= vy;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this tuple's x value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i divX( int v )
    {
        this.values[ roTrick + 0 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this tuple's y value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple2i divY( int v )
    {
        this.values[ roTrick + 1 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple2i div( int vx, int vy )
    {
        this.values[ roTrick + 0 ] /= vx;
        this.values[ roTrick + 1 ] /= vy;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple2i scale( int factorX, int factorY )
    {
        mul( factorX, factorY );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2i asReadOnly()
    {
        return ( new Tuple2i( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2i getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return ( values[ 0 ] ^ values[ 1 ] );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Tuple2i tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            if ( tuple2.getValue( i ) != this.getValue( i ) )
                return ( false );
        }
        
        return ( true );
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
    public boolean equals( Object tuple2 )
    {
        return ( ( tuple2 != null ) && ( ( tuple2 instanceof Tuple2i ) && equals( (Tuple2i)tuple2 ) ) );
    }
    
    /**
     * Returns a string that contains the values of this Tuple2f. The form is
     * (x, y).
     * 
     * @return the String representation
     */
    @Override
    public String toString()
    {
        return "(" + getValue( 0 ) + ", " + getValue( 1 ) + ")";
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Tuple2i clone()
    {
        return ( new Tuple2i( this ) );
    }
    
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     */
    protected Tuple2i( boolean readOnly, int x, int y )
    {
        super( readOnly, 2 );
        
        this.values[ 0 ] = x;
        this.values[ 1 ] = y;
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 2)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Tuple2i( boolean readOnly, int[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 2, copy );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param readOnly
     * @param that the Tuple2i to copy the values from
     */
    protected Tuple2i( boolean readOnly, Tuple2i that )
    {
        super( readOnly, that );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param readOnly
     */
    protected Tuple2i( boolean readOnly )
    {
        this( readOnly, 0, 0 );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public Tuple2i( int x, int y )
    {
        this( false, x, y );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public Tuple2i( int[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param that the Tuple2i to copy the values from
     */
    public Tuple2i( Tuple2i that )
    {
        this( false, that );
    }
    
    /**
     * Creates a new Tuple2i instance.
     */
    public Tuple2i()
    {
        this( false, 0, 0 );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     */
    public static Tuple2i newReadOnly( int x, int y )
    {
        return ( new Tuple2i( true, x, y ) );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param values the values array (must be at least size 2)
     */
    public static Tuple2i newReadOnly( int[] values )
    {
        return ( new Tuple2i( true, values, null, true ) );
    }
    
    /**
     * Creates a new Tuple2i instance.
     * 
     * @param tuple the Tuple2i to copy the values from
     */
    public static Tuple2i newReadOnly( Tuple2i tuple )
    {
        return ( new Tuple2i( true, tuple ) );
    }
    
    /**
     * Creates a new Tuple2i instance.
     */
    public static Tuple2i newReadOnly()
    {
        return ( new Tuple2i( true ) );
    }
    
    /**
     * Allocates an Tuple2i instance from the pool.
     */
    public static Tuple2i fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Tuple2i instance from the pool.
     */
    public static Tuple2i fromPool( int x, int y )
    {
        return ( POOL.get().alloc( x, y ) );
    }
    
    /**
     * Allocates an Tuple2i instance from the pool.
     */
    public static Tuple2i fromPool( Tuple2i tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY() ) );
    }
    
    /**
     * Stores the given Tuple2i instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Tuple2i o )
    {
        POOL.get().free( o );
    }
}
