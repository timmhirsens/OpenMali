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

import org.openmali.vecmath2.pools.Tuple3iPool;

/**
 * A simple three-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's Tuple3i implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Tuple3i extends TupleNi< Tuple3i >
{
    private static final long serialVersionUID = 586023996166921455L;
    
    //private static final Tuple3iPool POOL = new Tuple3iPool( 128 );
    private static final ThreadLocal<Tuple3iPool> POOL = new ThreadLocal<Tuple3iPool>()
    {
        @Override
        protected Tuple3iPool initialValue()
        {
            return ( new Tuple3iPool( 128 ) );
        }
    };
    
    private Tuple3i readOnlyInstance = null;
    
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple3i setX( int x )
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
    public final Tuple3i setY( int y )
    {
        this.setValue( 1, y );
        
        return ( this );
    }
    
    /**
     * Sets the value of the z-element of this tuple.
     * 
     * @param z
     * 
     * @return itself
     */
    public final Tuple3i setZ( int z )
    {
        this.setValue( 2, z );
        
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
     * @return the value of the z-element of this tuple.
     */
    public final int getZ()
    {
        return ( this.getValue( 2 ) );
    }
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple3i x( int x )
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
    public final Tuple3i y( int y )
    {
        this.setValue( 1, y );
        
        return ( this );
    }
    
    /**
     * Sets the value of the z-element of this tuple.
     * 
     * @param z
     * 
     * @return itself
     */
    public final Tuple3i z( int z )
    {
        this.setValue( 2, z );
        
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
     * @return the value of the z-element of this tuple.
     */
    public final int z()
    {
        return ( this.getValue( 2 ) );
    }
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * 
     * @return itself
     */
    public final Tuple3i set( int x, int y, int z )
    {
        this.setValue( 0, x );
        this.setValue( 1, y );
        this.setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3i addX( int v )
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
    public final Tuple3i addY( int v )
    {
        this.values[ roTrick + 1 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's z value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3i addZ( int v )
    {
        this.values[ roTrick + 2 ] += v;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple3i add( int x, int y, int z )
    {
        this.values[ roTrick + 0 ] += x;
        this.values[ roTrick + 1 ] += y;
        this.values[ roTrick + 2 ] += z;
        
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
    public final Tuple3i subX( int v )
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
    public final Tuple3i subY( int v )
    {
        this.values[ roTrick + 1 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's z value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3i subZ( int v )
    {
        this.values[ roTrick + 2 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple3i sub( int x, int y, int z )
    {
        this.values[ roTrick + 0 ] -= x;
        this.values[ roTrick + 1 ] -= y;
        this.values[ roTrick + 2 ] -= z;
        
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
    public final Tuple3i mulX( int v )
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
    public final Tuple3i mulY( int v )
    {
        this.values[ roTrick + 1 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's z value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3i mulZ( int v )
    {
        this.values[ roTrick + 2 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's values with vx, vy.
     * 
     * @param vx
     * @param vy
     * @param vz
     * 
     * @return itself
     */
    public final Tuple3i mul( int vx, int vy, int vz )
    {
        this.values[ roTrick + 0 ] *= vx;
        this.values[ roTrick + 1 ] *= vy;
        this.values[ roTrick + 2 ] *= vz;
        
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
    public final Tuple3i divX( int v )
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
    public final Tuple3i divY( int v )
    {
        this.values[ roTrick + 1 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this tuple's z value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple3i divZ( int v )
    {
        this.values[ roTrick + 2 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple3i div( int vx, int vy, int vz )
    {
        this.values[ roTrick + 0 ] /= vx;
        this.values[ roTrick + 1 ] /= vy;
        this.values[ roTrick + 2 ] /= vz;
        
        this.isDirty[ 0 ] = true;
        
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
    public final Tuple3i scale( int factorX, int factorY, int factorZ )
    {
        mul( factorX, factorY, factorZ );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3i asReadOnly()
    {
        return ( new Tuple3i( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3i getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Returns a hash number based on the data values in this object. Two
     * different Tuple3f objects with identical data values (ie, returns true
     * for equals(Tuple3f) ) will return the same hash number. Two vectors with
     * different data members may return the same hash value, although this is
     * not likely.
     */
    @Override
    public int hashCode()
    {
        return ( values[ 0 ] ^ values[ 1 ] ^ values[ 2 ] );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Tuple3i tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            if ( tuple2.getValue( i ) != this.getValue( i ) )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object tuple2 )
    {
        return ( ( tuple2 != null ) && ( ( tuple2 instanceof Tuple3i ) && equals( (Tuple3i)tuple2 ) ) );
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
        return "(" + getValue( 0 ) + ", " + getValue( 1 ) + ", " + getValue( 2 ) + ")";
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Tuple3i clone()
    {
        return ( new Tuple3i( this ) );
    }
    
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    protected Tuple3i( boolean readOnly, int x, int y, int z )
    {
        super( readOnly, 3 );
        
        this.values[ 0 ] = x;
        this.values[ 1 ] = y;
        this.values[ 2 ] = z;
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Tuple3i( boolean readOnly, int[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 3, copy );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param readOnly
     * @param that the Tuple3i to copy the values from
     */
    protected Tuple3i( boolean readOnly, Tuple3i that )
    {
        super( readOnly, that );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param readOnly
     */
    protected Tuple3i( boolean readOnly )
    {
        this( readOnly, 0, 0, 0 );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public Tuple3i( int x, int y, int z )
    {
        this( false, x, y, z );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param values the values array (must be at least size 4)
     */
    public Tuple3i( int[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param that the Tuple3i to copy the values from
     */
    public Tuple3i( Tuple3i that )
    {
        this( false, that );
    }
    
    /**
     * Creates a new Tuple3i instance.
     */
    public Tuple3i()
    {
        this( false, 0, 0, 0 );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     */
    public static Tuple3i newReadOnly( int x, int y, int z )
    {
        return ( new Tuple3i( true, x, y, z ) );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Tuple3i newReadOnly( int[] values )
    {
        return ( new Tuple3i( true, values, null, true ) );
    }
    
    /**
     * Creates a new Tuple3i instance.
     * 
     * @param tuple the Tuple3i to copy the values from
     */
    public static Tuple3i newReadOnly( Tuple3i tuple )
    {
        return ( new Tuple3i( true, tuple ) );
    }
    
    /**
     * Creates a new Tuple3i instance.
     */
    public static Tuple3i newReadOnly()
    {
        return ( new Tuple3i( true ) );
    }
    
    /**
     * Allocates an Tuple3i instance from the pool.
     */
    public static Tuple3i fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Tuple3i instance from the pool.
     */
    public static Tuple3i fromPool( int x, int y, int z )
    {
        return ( POOL.get().alloc( x, y, z ) );
    }
    
    /**
     * Allocates an Tuple3i instance from the pool.
     */
    public static Tuple3i fromPool( Tuple3i tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ() ) );
    }
    
    /**
     * Stores the given Tuple3i instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Tuple3i o )
    {
        POOL.get().free( o );
    }
}
