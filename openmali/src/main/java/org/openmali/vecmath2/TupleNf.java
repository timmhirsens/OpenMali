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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openmali.FastMath;
import org.openmali.vecmath2.util.SerializationUtils;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * This is a base-class for all float tuples like Tuple2f, Tuple3f, VectorNf, etc.
 * 
 * @param <T> the generic parameter for coefficients
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class TupleNf< T extends TupleNf< T > > implements TupleInterface< T >
{
    private final int N;
    protected final float[] values;
    
    protected final int roTrick;
    
    /*
     * This boolean is implemented as a one-elemental array
     * to be able to share its value with a read-only instance.
     */
    protected final boolean[] isDirty;
    
    
    /**
     * {@inheritDoc}
     */
    public final boolean isReadOnly()
    {
        return ( roTrick != 0 );
    }
    
    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public final boolean isDirty()
    {
        return ( isDirty[ 0 ] );
    }
    
    /**
     * {@inheritDoc}
     */
    public final int getSize()
    {
        return ( N );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T setValue( int i, float v )
    {
         this.values[ roTrick + i ] = v;
         
         this.isDirty[ 0 ] = true;
         
         return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float getValue( int i )
    {
         return ( this.values[ i ] );
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public final java.nio.FloatBuffer writeToBuffer( java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.put( values );
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public final java.nio.FloatBuffer writeToBuffer( java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        buffer.put( values );
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param tuples
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( TupleNf< ? >[] tuples, java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        for ( int i = 0; i < tuples.length; i++ )
        {
            tuples[ i ].writeToBuffer( buffer, false, false );
        }
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param tuples
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( TupleNf< ? >[] tuples, java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        for ( int i = 0; i < tuples.length; i++ )
        {
            tuples[ i ].writeToBuffer( buffer, false, false );
        }
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Reads the contents for this tuple from a FloatBuffer.<br>
     * 
     * @param buffer
     */
    public final java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer )
    {
        buffer.get( values );
        
        return ( buffer );
    }
    
    /**
     * Reads the contents for this tuple from a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     */
    public final java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        buffer.get( values );
        
        return ( buffer );
    }
    
    /**
     * Reads the contents for this tuple from a FloatBuffer.<br>
     * 
     * @param buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( TupleNf< ? >[] tuples, java.nio.FloatBuffer buffer )
    {
        for ( int i = 0; i < tuples.length; i++ )
        {
            tuples[ i ].readFromBuffer( buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * Reads the contents for this tuple from a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( TupleNf< ? >[] tuples, java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        for ( int i = 0; i < tuples.length; i++ )
        {
            tuples[ i ].readFromBuffer( buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T fill( float f )
    {
        java.util.Arrays.fill( this.values, roTrick + 0, getSize(), f );
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T addValue( int i, float v )
    {
        values[ roTrick + i ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T subValue( int i, float v )
    {
        values[ roTrick + i ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T mulValue( int i, float v )
    {
        values[ roTrick + i ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T mul( float v )
    {
        for ( int i = 0; i < N; i++ )
            values[ roTrick + i ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T divValue( int i, float v )
    {
        values[ roTrick + i ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T div( float v )
    {
        for ( int i = 0; i < N; i++ )
            values[ roTrick + i ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public T set( float[] values, int offset )
    {
        System.arraycopy( values, offset, this.values, roTrick + 0, N );
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    public T set( float[] values )
    {
        return ( set( values, 0 ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T set( TupleNf< ? > tuple )
    {
        System.arraycopy( tuple.values, 0, this.values, roTrick + 0, Math.min( this.getSize(), tuple.getSize() ) );
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    public void get( float[] buffer, int offset )
    {
        System.arraycopy( this.values, 0, buffer, offset, N );
    }
    
    /**
     * {@inheritDoc}
     */
    public void get( float[] buffer )
    {
        get( buffer, 0 );
    }
    
    /**
     * {@inheritDoc}
     */
    public final void get( TupleNf< ? > buffer )
    {
        System.arraycopy( this.values, 0, buffer.values, 0, Math.min( this.getSize(), buffer.getSize() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T setZero()
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, 0f );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T negate()
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, -this.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T negate( T tuple )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, -tuple.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T absolute()
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, Math.abs( this.values[ i ] ) );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T absolute( T tuple )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, Math.abs( tuple.values[ i ] ) );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T add( T tuple1, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, tuple1.values[ i ] + tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T add( T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, this.values[ i ] + tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T sub( T tuple1, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, tuple1.values[ i ] - tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T sub( T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.subValue( i, tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T scale( float factor, T tuple )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, factor * tuple.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T scale( float factor )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.mulValue( i, factor );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T scaleAdd( float factor, T tuple1, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, factor * tuple1.values[ i ] + tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T scaleAdd( float factor, T tuple2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, factor * this.values[ i ] + tuple2.values[ i ] );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T clampMin( float min )
    {
        for ( int i = 0; i < N; i++ )
        {
            if (this.values[ i ] < min )
                this.setValue( i, min );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T clampMax( float max )
    {
        for ( int i = 0; i < N; i++ )
        {
            if (this.getValue( i ) > max )
                this.setValue( i, max );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T clamp( float min, float max )
    {
        clampMin( min );
        clampMax( max );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T clamp( float min, float max, T tuple )
    {
        set( tuple );
        
        clamp( min, max );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T clampMin( float min, T tuple )
    {
        set( tuple );
        clampMin( min );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T clampMax( float max, T tuple )
    {
        set( tuple );
        clampMax( max );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T round( T tuple, int decPlaces )
    {
        final float pow = FastMath.pow( 10.0f, decPlaces );
        
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, (int)( tuple.getValue( i ) * pow ) / pow );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final T round( int decPlaces )
    {
        final float pow = FastMath.pow( 10.0f, decPlaces );
        
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, (int)( this.getValue( i ) * pow ) / pow );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    public void interpolate( T t2, float alpha )
    {
        final float beta = 1.0f - alpha;
        
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, beta * this.getValue( i ) + alpha * t2.getValue( i ) );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public final void interpolate( T t1, T t2, float alpha )
    {
        set( t1 );
        
        interpolate( t2, alpha );
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
     * Returns true if all of the data members of GVector vector1 are equal to
     * the corresponding data members in this GVector.
     * 
     * @param v2 The vector with which the comparison is made.
     * @return true or false
     */
    public boolean equals( T v2 )
    {
        if ( v2 == null )
            return ( false );
        if ( this.N != v2.N )
            return ( false );
        
        for ( int i = 0; i < N; i++ )
        {
            if ( this.getValue( i ) != v2.getValue( i ) )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals( Object o )
    {
        if ( o == null )
            return ( false );
        
        try
        {
            return ( equals( (T)o ) );
        }
        catch( ClassCastException e )
        {
            return ( false );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean epsilonEquals( T v2, float epsilon )
    {
        if ( this.N != v2.N )
            return ( false );
        
        for ( int i = 0; i < this.N; i++ )
        {
            if ( Math.abs( this.getValue( i ) - v2.getValue( i ) ) > epsilon )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * Returns a hash number based on the data values in this object. Two
     * different GMatrix objects with identical data values (ie, returns true
     * for equals(GMatrix) ) will return the same hash number. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash value
     */
    @Override
    public int hashCode()
    {
        int hash = 0;
        for ( int i = 0; i < this.N; i++ )
        {
            int bits = VecMathUtils.floatToIntBits( this.getValue( i ) );
            hash ^= ( bits ^ ( bits >> 32 ) );
        }
        
        return ( hash );
    }
    
    /**
     * Returns a string that contains the values of this Vector.
     * 
     * @return the String representation
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.setLength( 0 );
        
        sb.append( "( " );
        for ( int i = 0; i < N - 1; i++ )
        {
            sb.append( getValue( i ) );
            sb.append( ", " );
        }
        sb.append( getValue( N - 1 ) );
        sb.append( " )" );
        
        return ( this.getClass().getSimpleName() + sb.toString() );
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
            values[ i ] = SerializationUtils.readFloatFromBuffer( pos, buffer );
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
     * Creates a new TupleNf instance.
     * 
     * @param readOnly
     * @param n the number of elements
     */
    protected TupleNf( boolean readOnly, int n )
    {
        this.N = n;
        this.values = new float[ n ];
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[ 1 ];
    }
    
    /**
     * Creates a new TupleNf instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param n the number of elements
     * @param copy copy the array?
     */
    protected TupleNf( boolean readOnly, float[] values, boolean[] isDirty, int n, boolean copy )
    {
        this.N = n;
        if ( copy )
        {
            this.values = new float[ n ];
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
    protected TupleNf( boolean readOnly, float[] values, int n )
    {
        this( readOnly, values, null, n, true );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param readOnly
     * @param that the TupleNf to copy the values from
     */
    protected TupleNf( boolean readOnly, TupleNf< ? > that )
    {
        this( readOnly, that.values, null, that.values.length, true );
    }
    
    /**
     * Creates a new TupleNf instance.
     * 
     * @param n the number of elements
     */
    public TupleNf( int n )
    {
        this( false, n );
    }
    
    /*
     * Creates a new TupleNf instance.
     * 
     * @param values the values array (must be at least size 3)
     * @param n the number of elements
     * @param copy copy the array?
     */
    /*
    public TupleNf( float[] values, int n, boolean copy )
    {
        this( false, values, n, copy );
    }
    */
    
    /**
     * Creates a new TupleNf instance.
     * 
     * @param values the values array (must be at least size n)
     * @param n the number of elements to copy
     */
    public TupleNf( float[] values, int n )
    {
        this( false, values, n );
    }
    
    /**
     * Creates a new Tuple3f instance.
     * 
     * @param that the TupleNf to copy the values from
     */
    public TupleNf( TupleNf< ? > that )
    {
        this( false, that );
    }
}
