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

import org.openmali.FastMathd;
import org.openmali.vecmath2.util.SerializationUtils;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * This is a base-class for all double tuples like Tuple2d, Tuple3d, VectorNd, etc.
 * 
 * @param <T> the generic parameter for coefficients
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class TupleNd< T extends TupleNd< T > >
{
    private final int N;
    protected final double[] values;
    
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
    @SuppressWarnings( "unchecked" )
    public final T setValue( int i, double v )
    {
        this.values[ roTrick + i ] = v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final double getValue( int i )
    {
        return ( this.values[ i ] );
    }
    
    /**
     * Writes the contents of this tuple to a doubleBuffer.<br>
     * The buffer is automatically cleared (before) and flipped (after).
     * 
     * @param buffer
     */
    public final java.nio.DoubleBuffer writeToBuffer( java.nio.DoubleBuffer buffer )
    {
        buffer.clear();
        
        buffer.put( values );
        
        buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this tuple to a doubleBuffer.<br>
     * The buffer is automatically cleared (before) and flipped (after).
     * 
     * @param buffer
     */
    public static final java.nio.DoubleBuffer writeToBuffer( TupleNd< ? >[] tuples, java.nio.DoubleBuffer buffer )
    {
        buffer.clear();
        
        for ( int i = 0; i < tuples.length; i++ )
        {
            buffer.put( tuples[ i ].values );
        }
        
        buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T fill( double f )
    {
        java.util.Arrays.fill( this.values, roTrick + 0, getSize(), f );
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T addValue( int i, double v )
    {
        values[ roTrick + i ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T subValue( int i, double v )
    {
        values[ roTrick + i ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T mulValue( int i, double v )
    {
        values[ roTrick + i ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T mul( double v )
    {
        for ( int i = 0; i < N; i++ )
            values[ roTrick + i ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T divValue( int i, double v )
    {
        values[ roTrick + i ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T div( double v )
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
    public T set( double[] values )
    {
        System.arraycopy( values, 0, this.values, roTrick + 0, N );
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T set( TupleNd< ? > tuple )
    {
        System.arraycopy( tuple.values, 0, this.values, roTrick + 0, Math.min( this.getSize(), tuple.getSize() ) );
        
        this.isDirty[ 0 ] = true;
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    public void get( double[] buffer )
    {
        System.arraycopy( this.values, 0, buffer, 0, N );
    }
    
    /**
     * {@inheritDoc}
     */
    public final void get( TupleNd< ? > buffer )
    {
        System.arraycopy( this.values, 0, buffer.values, 0, Math.min( this.getSize(), buffer.getSize() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T setZero()
    {
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, 0d );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
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
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
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
    @SuppressWarnings( "unchecked" )
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
    @SuppressWarnings( "unchecked" )
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
    @SuppressWarnings( "unchecked" )
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
    @SuppressWarnings( "unchecked" )
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
    @SuppressWarnings( "unchecked" )
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
    @SuppressWarnings( "unchecked" )
    public final T scale( double factor, T tuple )
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
    @SuppressWarnings( "unchecked" )
    public final T scale( double factor )
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
    @SuppressWarnings( "unchecked" )
    public final T scaleAdd( double factor, T tuple1, T tuple2 )
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
    @SuppressWarnings( "unchecked" )
    public final T scaleAdd( double factor, T tuple2 )
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
    @SuppressWarnings( "unchecked" )
    public final T clampMin( double min )
    {
        for ( int i = 0; i < N; i++ )
        {
            if ( this.values[ i ] < min )
                this.setValue( i, min );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMax( double max )
    {
        for ( int i = 0; i < N; i++ )
        {
            if ( this.getValue( i ) > max )
                this.setValue( i, max );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T clamp( double min, double max )
    {
        clampMin( min );
        clampMax( max );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T clamp( double min, double max, T tuple )
    {
        set( tuple );
        
        clamp( min, max );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMin( double min, T tuple )
    {
        set( tuple );
        clampMin( min );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMax( double max, T tuple )
    {
        set( tuple );
        clampMax( max );
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T round( T tuple, int decPlaces )
    {
        final double pow = FastMathd.pow( 10.0d, decPlaces );
        
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, (int)( tuple.getValue( i ) * pow ) / pow );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public final T round( int decPlaces )
    {
        final double pow = FastMathd.pow( 10.0d, decPlaces );
        
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, (int)( this.getValue( i ) * pow ) / pow );
        }
        
        return ( (T)this );
    }
    
    /**
     * {@inheritDoc}
     */
    public void interpolate( T t2, double alpha )
    {
        final double beta = 1 - alpha;
        
        for ( int i = 0; i < N; i++ )
        {
            this.setValue( i, beta * this.getValue( i ) + alpha * t2.getValue( i ) );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public final void interpolate( T t1, T t2, double alpha )
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
    @SuppressWarnings( "unchecked" )
    public boolean equals( Object o )
    {
        if ( o == null )
            return ( false );
        
        try
        {
            return ( equals( (T)o ) );
        }
        catch ( ClassCastException e )
        {
            return ( false );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean epsilonEquals( T v2, double epsilon )
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
            long bits = VecMathUtils.doubleToLongBits( this.getValue( i ) );
            hash ^= ( bits ^ ( bits >> 32 ) );
        }
        
        return ( hash );
    }
    
    private static final StringBuffer TMP_SB = new StringBuffer();
    
    /**
     * Returns a string that contains the values of this Vector.
     * 
     * @return the String representation
     */
    @Override
    public String toString()
    {
        TMP_SB.setLength( 0 );
        
        TMP_SB.append( "( " );
        for ( int i = 0; i < N - 1; i++ )
        {
            TMP_SB.append( getValue( i ) );
            TMP_SB.append( ", " );
        }
        TMP_SB.append( getValue( N - 1 ) );
        TMP_SB.append( " )" );
        
        return ( this.getClass().getSimpleName() + TMP_SB.toString() );
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
            values[ i ] = SerializationUtils.readDoubleFromBuffer( pos, buffer );
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
     * Creates a new TupleNd instance.
     * 
     * @param readOnly
     * @param n the number of elements
     */
    protected TupleNd( boolean readOnly, int n )
    {
        this.N = n;
        this.values = new double[ n ];
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[ 1 ];
    }
    
    /**
     * Creates a new TupleNd instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty the isDirty-value
     * @param n the number of elements
     * @param copy copy the array?
     */
    protected TupleNd( boolean readOnly, double[] values, boolean[] isDirty, int n, boolean copy )
    {
        this.N = n;
        if ( copy )
        {
            this.values = new double[ n ];
            System.arraycopy( values, 0, this.values, 0, N );
            if ( isDirty == null )
                this.isDirty = new boolean[]
                {
                    false
                };
            else
                this.isDirty = new boolean[]
                {
                    isDirty[ 0 ]
                };
            ;
        }
        else
        {
            this.values = values;
            this.isDirty = isDirty;
        }
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * Creates a new TupleNd instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size n)
     * @param n the number of elements to copy
     */
    protected TupleNd( boolean readOnly, double[] values, int n )
    {
        this( readOnly, values, null, n, true );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param readOnly
     * @param that the TupleNd to copy the values from
     */
    protected TupleNd( boolean readOnly, TupleNd< ? > that )
    {
        this( readOnly, that.values, null, that.values.length, true );
    }
    
    /**
     * Creates a new TupleNd instance.
     * 
     * @param n the number of elements
     */
    public TupleNd( int n )
    {
        this( false, n );
    }
    
    /*
     * Creates a new TupleNd instance.
     * 
     * @param values the values array (must be at least size 3)
     * @param n the number of elements
     * @param copy copy the array?
     */
    /*
    public TupleNd( double[] values, int n, boolean copy )
    {
        this( false, values, n, copy );
    }
    */

    /**
     * Creates a new TupleNd instance.
     * 
     * @param values the values array (must be at least size n)
     * @param n the number of elements to copy
     */
    public TupleNd( double[] values, int n )
    {
        this( false, values, n );
    }
    
    /**
     * Creates a new Tuple3d instance.
     * 
     * @param that the TupleNd to copy the values from
     */
    public TupleNd( TupleNd< ? > that )
    {
        this( false, that );
    }
}
