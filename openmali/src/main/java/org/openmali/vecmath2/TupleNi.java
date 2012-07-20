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

import org.openmali.vecmath2.util.SerializationUtils;

/**
 * A simple N-dimensional tuple implementation.
 * 
 * Inspired by Kenji Hiranabe's implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class TupleNi< T extends TupleNi< T > > implements Externalizable
{
    protected final int N;
    protected final int[] values;
    
    protected final int roTrick;
    
    /*
     * This boolean is implemented as a one-elemental array
     * to be able to share its value with a read-only instance.
     */
    protected final boolean[] isDirty;
    
    
    /**
     * @return Is this tuple a read-only one?
     */
    public final boolean isReadOnly()
    {
        return ( roTrick != 0 );
    }
    
    /**
     * Marks this tuple non-dirty.
     * Any value-manipulation will mark it dirty again.
     * 
     * @return the old value
     */
    public final boolean setClean()
    {
        if ( isReadOnly() )
            throw new Error( "This instance is read-only." );
        
        final boolean oldValue = this.isDirty[ 0 ];
        
        this.isDirty[ 0 ] = false;
        
        return ( oldValue );
    }
    
    /**
     * @return This tuple's dirty-flag
     */
    public final boolean isDirty()
    {
        return ( isDirty[ 0 ] );
    }
    
    /**
     * @return this Tuple's size().
     */
    public final int getSize()
    {
        return ( N );
    }
    
    /**
     * Sets the value of the i-th element of this tuple.
     * 
     * @param i
     * @param v
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T setValue( int i, int v )
    {
        this.values[ roTrick + i ] = v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * @return the value of the i-th element of this tuple.
     */
    public final int getValue( int i )
    {
        return ( this.values[ i ] );
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size 3)
     * @param offset the offset int the (source) values array
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T set( int[] values, int offset )
    {
        System.arraycopy( values, offset, this.values, roTrick + 0, N );
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size 3)
     * 
     * @return itself
     */
    public final T set( int[] values )
    {
        return ( set( values, 0 ) );
    }
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param tuple the tuple to be copied
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T set( T tuple )
    {
        System.arraycopy( tuple.values, 0, this.values, roTrick + 0, N );
        
        return ( (T)this );
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     * @param offset the offset in the (target) buffer array
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T get( int[] buffer, int offset )
    {
        System.arraycopy( this.values, 0, buffer, offset, N );
        
        return ( (T)this );
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     * 
     * @return itself
     */
    public final T get( int[] buffer )
    {
        return ( get( buffer, 0 ) );
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer Tuple.
     * 
     * @param buffer the buffer Tuple to write the values to
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T get( T buffer )
    {
        System.arraycopy( this.values, 0, buffer.values, 0, N );
        
        return ( (T)this );
    }
    
    /**
     * Sets all components to zero.
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T setZero()
    {
        for ( int i = 0; i < getSize(); i++ )
            this.setValue( i, 0 );
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the negation of tuple that.
     * 
     * @param tuple the source vector
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T negate( T tuple )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, -tuple.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * Negates the value of this vector in place.
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T negate()
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] *= -1f;
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets each component of the tuple parameter to its absolute value and
     * places the modified values into this tuple.
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T absolute()
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = Math.abs( this.values[ i ] );
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets each component of the tuple parameter to its absolute value and
     * places the modified values into this tuple.
     * 
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T absolute( T tuple )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = Math.abs( tuple.values[ i ] );
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the vector sum of tuples t1 and t2.
     * 
     * @param tuple1 the first tuple
     * @param tuple2 the second tuple
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T add( T tuple1, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, tuple1.getValue( i ) + tuple2.getValue( i ) );
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the vector sum of itself and tuple t1.
     * 
     * @param tuple2 the other tuple
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T add( T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, this.getValue( i ) + tuple2.getValue( i ) );
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the vector difference of tuple t1 and t2
     * (this = t1 - t2).
     * 
     * @param tuple1 the first tuple
     * @param tuple2 the second tuple
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T sub( T tuple1, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, tuple1.values[ i ] - tuple2.values[ i ] );
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the vector difference of itself and tuple
     * t1 (this = this - t1).
     * 
     * @param tuple2 the other tuple
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T sub( T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, this.values[ i ] - tuple2.values[ i ] );
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1.
     * 
     * @param factor the scalar value
     * @param tuple the source tuple
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T scale( int factor, T tuple )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, factor * tuple.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself.
     * 
     * @param factor the scalar value
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T scale( int factor )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, this.getValue( i ) * factor );
        }
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1 and
     * then adds tuple t2 (this = s*t1 + t2).
     * 
     * @param factor the scalar value
     * @param tuple1 the tuple to be multipled
     * @param tuple2 the tuple to be added
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T scaleAdd( int factor, T tuple1, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, factor * tuple1.values[ i ] + tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself and
     * then adds tuple t1 (this = s*this + t1).
     * 
     * @param factor the scalar value
     * @param tuple2 the tuple to be added
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T scaleAdd( int factor, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, factor * this.values[ i ] + tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * Clamps the minimum value of this tuple to the min parameter.
     * 
     * @param min the lowest value in this tuple after clamping
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMin( int min )
    {
        for ( int i = 0; i < N; i++ )
        {
            if (this.getValue( i ) < min )
                this.setValue( i, min );
        }
        
        return ( (T)this );
    }
    
    /**
     * Clamps the maximum value of this tuple to the max parameter.
     * 
     * @param max the highest value in the tuple after clamping
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMax( int max )
    {
        for ( int i = 0; i < N; i++ )
        {
            if (this.getValue( i ) > max )
                this.setValue( i, max );
        }
        
        return ( (T)this );
    }
    
    /**
     * Clamps this tuple to the range [min, max].
     * 
     * @param min the lowest value in this tuple after clamping
     * @param max the highest value in this tuple after clamping
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clamp( int min, int max )
    {
        clampMin( min );
        clampMax( max );
        
        return ( (T)this );
    }
    
    /**
     * Clamps the tuple parameter to the range [min, max] and places the values
     * into this tuple.
     * 
     * @param min the lowest value in the tuple after clamping
     * @param max the highest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clamp( int min, int max, T tuple )
    {
        set( tuple );
        
        clamp( min, max );
        
        return ( (T)this );
    }
    
    /**
     * Clamps the minimum value of the tuple parameter to the min parameter and
     * places the values into this tuple.
     * 
     * @param min the lowest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMin( int min, T tuple )
    {
        set( tuple );
        clampMin( min );
        
        return ( (T)this );
    }
    
    /**
     * Clamps the maximum value of the tuple parameter to the max parameter and
     * places the values into this tuple.
     * 
     * @param max the highest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMax( int max, T tuple )
    {
        set( tuple );
        clampMax( max );
        
        return ( (T)this );
    }
    
    /**
     * @return a new instance sharing the values array with this instance.
     * The new instance is read-only. Changes to this instance will be reflected
     * in the new read-only-instance.
     * 
     * @see #getReadOnly()
     */
    public abstract T asReadOnly();
    
    /**
     * @return a single instance sharing the values array with this instance (one unique instance per 'master-instance').
     * The instance is read-only. Changes to this instance will be reflected
     * in the read-only-instance.
     * 
     * @see #asReadOnly()
     */
    public abstract T getReadOnly();
    
    /**
     * Returns a hash number based on the data values in this object. Two
     * different Tuple3f objects with identical data values (ie, returns true
     * for equals(Tuple3f) ) will return the same hash number. Two vectors with
     * different data members may return the same hash value, although this is
     * not likely.
     */
    @Override
    public abstract int hashCode();
    
    /**
     * Returns true if all of the data members of GVector vector1 are equal to
     * the corresponding data members in this GVector.
     * 
     * @param t2 The vector with which the comparison is made.
     * @return true or false
     */
    public boolean equals( T t2 )
    {
        if ( t2 == null )
            return ( false );
        if ( this.N != t2.N )
            return ( false );
        
        for ( int i = 0; i < N; i++ )
        {
            if ( this.getValue( i ) != t2.getValue( i ) )
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
        return ( ( tuple2 != null ) && ( ( tuple2 instanceof TupleNi ) && equals( (TupleNi<?>)tuple2 ) ) );
    }
    
    /**
     * Returns a string that contains the values of this Tuple2f. The form is
     * (x, y).
     * 
     * @return the String representation
     */
    @Override
    public abstract String toString();
    
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
        for ( int i = 0; i < N; i++ )
        {
            SerializationUtils.writeToBuffer( values[ i ], pos, buffer );
            pos += 4;
        }
        
        SerializationUtils.writeToBuffer( isDirty, pos, buffer );
        pos += 1;
        
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
        for ( int i = 0; i < N; i++ )
        {
            values[ i ] = SerializationUtils.readIntFromBuffer( pos, buffer );
            pos += 4;
        }
        
        isDirty[ 0 ] = SerializationUtils.readBoolFromBuffer( pos, buffer );
        pos += 1;
        
        return ( pos );
    }
    
    /**
     * @return the necessary size for a serialization byte array.
     */
    protected int getSerializationBufferSize()
    {
        return ( 4 * N + 1 );
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
     * Creates a new TupleNi instance.
     * 
     * @param readOnly
     * @param n the number of elements
     */
    protected TupleNi( boolean readOnly, int n )
    {
        this.N = n;
        this.values = new int[ n ];
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[ 1 ];
    }
    
    /**
     * Creates a new TupleNi instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size n)
     * @param isDirty the isDirty-value
     * @param n the number of elements
     * @param copy copy the array?
     */
    protected TupleNi( boolean readOnly, int[] values, boolean[] isDirty, int n, boolean copy )
    {
        this.N = n;
        if ( copy )
        {
            this.values = new int[ n ];
            System.arraycopy( values, 0, this.values, 0, N );
            if ( isDirty == null )
                this.isDirty = new boolean[] { false };
            else
                this.isDirty = new boolean[] { isDirty[ 0 ] };;
        }
        else
        {
            this.values = values;
            this.isDirty = isDirty;
        }
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * Creates a new TupleNf instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size n)
     * @param n the number of elements to copy
     */
    protected TupleNi( boolean readOnly, int[] values, int n )
    {
        this( readOnly, values, null, n, true );
    }
    
    /**
     * Creates a new TupleNi instance.
     * 
     * @param readOnly
     * @param that the TupleNi to copy the values from
     */
    protected TupleNi( boolean readOnly, TupleNi< ? > that )
    {
        this( readOnly, that.values, null, that.values.length, true );
    }
    
    /**
     * Creates a new TupleNi instance.
     * 
     * @param n the number of elements
     */
    public TupleNi( int n )
    {
        this( false, n );
    }
    
    /*
     * Creates a new TupleNi instance.
     * 
     * @param values the values array (must be at least size n)
     * @param n the number of elements
     * @param copy copy the array?
     */
    /*
    public TupleNf( int[] values, int n, boolean copy )
    {
        this( false, values, n, copy );
    }
    */
    
    /**
     * Creates a new TupleNi instance.
     * 
     * @param values the values array (must be at least size n)
     * @param n the number of elements to copy
     */
    public TupleNi( int[] values, int n )
    {
        this( false, values, n );
    }
    
    /**
     * Creates a new TupleNi instance.
     * 
     * @param that the TupleNi to copy the values from
     */
    public TupleNi( TupleNi< ? > that )
    {
        this( false, that );
    }
}
