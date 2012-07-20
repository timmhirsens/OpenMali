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

import org.openmali.vecmath2.pools.Tuple4iPool;

/**
 * A simple four-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's Tuple4i implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Tuple4i extends TupleNi< Tuple4i >
{
    private static final long serialVersionUID = 586023996166921455L;
    
    //private static final Tuple4iPool POOL = new Tuple4iPool( 128 );
    private static final ThreadLocal<Tuple4iPool> POOL = new ThreadLocal<Tuple4iPool>()
    {
        @Override
        protected Tuple4iPool initialValue()
        {
            return ( new Tuple4iPool( 128 ) );
        }
    };
    
    private Tuple4i readOnlyInstance = null;
    
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple4i setX( int x )
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
    public final Tuple4i setY( int y )
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
    public final Tuple4i setZ( int z )
    {
        this.setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * Sets the value of the w-element of this tuple.
     * 
     * @param w
     * 
     * @return itself
     */
    public final Tuple4i setW( int w )
    {
        this.setValue( 3, w );
        
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
     * @return the value of the w-element of this tuple.
     */
    public final int getW()
    {
        return ( this.getValue( 3 ) );
    }
    
    /**
     * Sets the value of the x-element of this tuple.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Tuple4i x( int x )
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
    public final Tuple4i y( int y )
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
    public final Tuple4i z( int z )
    {
        this.setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * Sets the value of the w-element of this tuple.
     * 
     * @param w
     * 
     * @return itself
     */
    public final Tuple4i w( int w )
    {
        this.setValue( 3, w );
        
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
     * @return the value of the w-element of this tuple.
     */
    public final int w()
    {
        return ( this.getValue( 3 ) );
    }
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     * 
     * @return itself
     */
    public final Tuple4i setValues( int x, int y, int z, int w )
    {
        this.setValue( 0, x );
        this.setValue( 1, y );
        this.setValue( 2, z );
        this.setValue( 3, w );
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple4i addX( int v )
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
    public final Tuple4i addY( int v )
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
    public final Tuple4i addZ( int v )
    {
        this.values[ roTrick + 2 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this tuple's w value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple4i addW( int v )
    {
        this.values[ roTrick + 3 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param x
     * @param y
     * @param z
     * @param w
     * 
     * @return itself
     */
    public final Tuple4i add( int x, int y, int z, int w )
    {
        this.values[ roTrick + 0 ] += x;
        this.values[ roTrick + 1 ] += y;
        this.values[ roTrick + 2 ] += z;
        this.values[ roTrick + 3 ] += w;
        
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
    public final Tuple4i subX( int v )
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
    public final Tuple4i subY( int v )
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
    public final Tuple4i subZ( int v )
    {
        this.values[ roTrick + 2 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's w value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple4i subW( int v )
    {
        this.values[ roTrick + 3 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param x
     * @param y
     * @param z
     * @param w
     * 
     * @return itself
     */
    public final Tuple4i sub( int x, int y, int z, int w )
    {
        this.values[ roTrick + 0 ] -= x;
        this.values[ roTrick + 1 ] -= y;
        this.values[ roTrick + 2 ] -= z;
        this.values[ roTrick + 3 ] -= w;
        
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
    public final Tuple4i mulX( int v )
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
    public final Tuple4i mulY( int v )
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
    public final Tuple4i mulZ( int v )
    {
        this.values[ roTrick + 2 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's w value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple4i mulW( int v )
    {
        this.values[ roTrick + 3 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's values with vx, vy.
     * 
     * @param vx
     * @param vy
     * @param vz
     * @param vw
     * 
     * @return itself
     */
    public final Tuple4i mul( int vx, int vy, int vz, int vw )
    {
        this.values[ roTrick + 0 ] *= vx;
        this.values[ roTrick + 1 ] *= vy;
        this.values[ roTrick + 2 ] *= vz;
        this.values[ roTrick + 3 ] *= vw;
        
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
    public final Tuple4i divX( int v )
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
    public final Tuple4i divY( int v )
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
    public final Tuple4i divZ( int v )
    {
        this.values[ roTrick + 2 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this tuple's w value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Tuple4i divW( int v )
    {
        this.values[ roTrick + 3 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this tuple's values by vx, vy, vz.
     * 
     * @param vx
     * @param vy
     * @param vz
     * @param vw
     * 
     * @return itself
     */
    public final Tuple4i div( int vx, int vy, int vz, int vw )
    {
        this.values[ roTrick + 0 ] /= vx;
        this.values[ roTrick + 1 ] /= vy;
        this.values[ roTrick + 2 ] /= vz;
        this.values[ roTrick + 3 ] /= vw;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1.
     * 
     * @param factorX
     * @param factorY
     * @param factorZ
     * @param factorW
     * 
     * @return itself
     */
    public final Tuple4i scale( int factorX, int factorY, int factorZ, int factorW )
    {
        mul( factorX, factorY, factorZ, factorW );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple4i asReadOnly()
    {
        return ( new Tuple4i( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple4i getReadOnly()
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
        return ( values[ 0 ] ^ values[ 1 ] ^ values[ 2 ] ^ values[ 3 ] );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Tuple4i tuple2 )
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
        return ( ( tuple2 != null ) && ( ( tuple2 instanceof Tuple4i ) && equals( (Tuple4i)tuple2 ) ) );
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
        return "(" + getValue( 0 ) + ", " + getValue( 1 ) + ", " + getValue( 2 ) + ", " + getValue( 3 ) + ")";
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Tuple4i clone()
    {
        return ( new Tuple4i( this ) );
    }
    
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     */
    protected Tuple4i( boolean readOnly, int x, int y, int z, int w )
    {
        super( readOnly, 4 );
        
        this.values[ 0 ] = x;
        this.values[ 1 ] = y;
        this.values[ 2 ] = z;
        this.values[ 3 ] = w;
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 4)
     * @param isDirty the isDirty-value
     * @param copy copy the array?
     */
    protected Tuple4i( boolean readOnly, int[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 4, copy );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param readOnly
     * @param that the Tuple4i to copy the values from
     */
    protected Tuple4i( boolean readOnly, Tuple4i that )
    {
        super( readOnly, that );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param readOnly
     */
    protected Tuple4i( boolean readOnly )
    {
        this( readOnly, 0, 0, 0, 0 );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     */
    public Tuple4i( int x, int y, int z, int w )
    {
        this( false, x, y, z, w );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param values the values array (must be at least size 4)
     */
    public Tuple4i( int[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param that the Tuple4i to copy the values from
     */
    public Tuple4i( Tuple4i that )
    {
        this( false, that );
    }
    
    /**
     * Creates a new Tuple4i instance.
     */
    public Tuple4i()
    {
        this( false, 0, 0, 0, 0 );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     */
    public static Tuple4i newReadOnly( int x, int y, int z, int w )
    {
        return ( new Tuple4i( true, x, y, z, w ) );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param values the values array (must be at least size 4)
     */
    public static Tuple4i newReadOnly( int[] values )
    {
        return ( new Tuple4i( true, values, null, true ) );
    }
    
    /**
     * Creates a new Tuple4i instance.
     * 
     * @param tuple the Tuple4i to copy the values from
     */
    public static Tuple4i newReadOnly( Tuple4i tuple )
    {
        return ( new Tuple4i( true, tuple ) );
    }
    
    /**
     * Creates a new Tuple4i instance.
     */
    public static Tuple4i newReadOnly()
    {
        return ( new Tuple4i( true ) );
    }
    
    /**
     * Allocates an Tuple4i instance from the pool.
     */
    public static Tuple4i fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Tuple4i instance from the pool.
     */
    public static Tuple4i fromPool( int x, int y, int z, int w )
    {
        return ( POOL.get().alloc( x, y, z, w ) );
    }
    
    /**
     * Allocates an Tuple4i instance from the pool.
     */
    public static Tuple4i fromPool( Tuple4i tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ(), tuple.getW() ) );
    }
    
    /**
     * Stores the given Tuple4i instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Tuple4i o )
    {
        POOL.get().free( o );
    }
}
