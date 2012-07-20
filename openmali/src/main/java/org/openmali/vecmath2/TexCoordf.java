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
import java.util.Arrays;

import org.openmali.vecmath2.util.SerializationUtils;

/**
 * A simple abstract Texture-Coordinate.<br>
 * The order is (s, t, p, q).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class TexCoordf< T extends TexCoordf< T > >
{
    protected final int N;
    protected final float[] values;
    
    protected final int roTrick;
    protected boolean isDirty = false;
    
    
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
        final boolean oldValue = this.isDirty;
        
        this.isDirty = false;
        
        return ( oldValue );
    }
    
    /**
     * @return This tuple's dirty-flag
     */
    public final boolean isDirty()
    {
        return ( isDirty );
    }
    
    /**
     * @return this Vector's size().
     */
    public final int getSize()
    {
        return ( N );
    }
    
    /**
     * Sets all values of this TexCoord to the specified ones.
     * 
     * @param values the values array (must be at least size n)
     * @param offset the offset in the (source) values array
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T set( float[] values, int offset )
    {
        System.arraycopy( values, 0, this.values, roTrick + 0, N );
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets all values of this TexCoord to the specified ones.
     * 
     * @param values the values array (must be at least size 1)
     * 
     * @return itself
     */
    public final T set( float[] values )
    {
        return ( set( values, 0 ) );
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param texCoord the texCoord to be copied
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T set( T texCoord )
    {
        System.arraycopy( texCoord.values, 0, this.values, roTrick + 0, N );
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     * @param offset the offset in the (target) buffer array
     */
    public final void get( float[] buffer, int offset )
    {
        System.arraycopy( this.values, 0, buffer, offset, N );
        
        this.isDirty = true;
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     */
    public final void get( float[] buffer )
    {
        get( buffer, 0 );
    }
    
    /**
     * Writes all values of this vector to the specified buffer vector.
     * 
     * @param buffer the buffer vector to write the values to
     */
    public final void get( T buffer )
    {
        System.arraycopy( this.values, 0, buffer.values, 0, Math.min( this.N, buffer.N ) );
        
        this.isDirty = true;
    }
    
    /**
     * Writes the contents of this TexCoords to a FloatBuffer.<br>
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
     * Writes the contents of this TexCoords to a FloatBuffer.<br>
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
     * Writes the contents of this TexCoords to a FloatBuffer.<br>
     * 
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( TexCoordf<?>[] texCoords, java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        for ( int i = 0; i < texCoords.length; i++ )
        {
            texCoords[ i ].writeToBuffer( buffer, false, false );
        }
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this TexCoords to a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( TexCoordf<?>[] texCoords, java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        for ( int i = 0; i < texCoords.length; i++ )
        {
            texCoords[ i ].writeToBuffer( buffer, false, false );
        }
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Reads the contents of this TexCoords from a FloatBuffer.<br>
     * 
     * @param buffer
     */
    public final java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer )
    {
        buffer.get( values );
        
        return ( buffer );
    }
    
    /**
     * Reads the contents of this TexCoords from a FloatBuffer.<br>
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
     * Reads the contents of this TexCoords from a FloatBuffer.<br>
     * 
     * @param buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( TexCoordf<?>[] texCoords, java.nio.FloatBuffer buffer )
    {
        for ( int i = 0; i < texCoords.length; i++ )
        {
            texCoords[ i ].readFromBuffer( buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * Reads the contents of this TexCoords from a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( TexCoordf<?>[] texCoords, java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        for ( int i = 0; i < texCoords.length; i++ )
        {
            texCoords[ i ].readFromBuffer( buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * Sets all components to zero.
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T setZero()
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = 0f;
        }
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this texCoord to the vector sum of colors texCoord1 and texCoord2.
     * 
     * @param texCoord1 the first texCoord
     * @param texCoord2 the second texCoord
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T add( T texCoord1, T texCoord2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = texCoord1.values[ i ] + texCoord2.values[ i ];
        }
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the vector sum of itself and tuple t1.
     * 
     * @param texCoord2 the other tuple
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T add( T texCoord2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] += texCoord2.values[ i ];
        }
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this texCoord to the vector difference of texCoord texCoord1 and texCoord2
     * (this = texCoord1 - texCoord2).
     * 
     * @param texCoord1 the first texCoord
     * @param texCoord2 the second texCoord
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T sub( T texCoord1, T texCoord2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = texCoord1.values[ i ] - texCoord2.values[ i ];
        }
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this texCoord to the vector difference of itself and texCoord2
     * (this = this - texCoord2).
     * 
     * @param texCoord2 the other texCoord
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T sub( T texCoord2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] -= texCoord2.values[ i ];
        }
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1 and
     * then adds tuple t2 (this = s*t1 + t2).
     * 
     * @param factor the scalar value
     * @param texCoord1 the tuple to be multipled
     * @param texCoord2 the tuple to be added
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T scaleAdd( float factor, T texCoord1, T texCoord2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = factor * texCoord1.values[ i ] + texCoord2.values[ i ];
        }
        
        this.isDirty = true;
        
        return ( (T)this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself and
     * then adds tuple t1 (this = s*this + t1).
     * 
     * @param factor the scalar value
     * @param texCoord2 the tuple to be added
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T scaleAdd( float factor, T texCoord2 )
    {
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = factor * this.values[ i ] + texCoord2.values[ i ];
        }
        
        this.isDirty = true;
        
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
    public final T clampMin( float min )
    {
        for ( int i = 0; i < N; i++ )
        {
            if (this.values[ i ] < min )
                this.values[ roTrick + i ] = min;
        }
        
        this.isDirty = true;
        
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
    public final T clampMax( float max )
    {
        for ( int i = 0; i < N; i++ )
        {
            if (this.values[ i ] > max )
                this.values[ roTrick + i ] = max;
        }
        
        this.isDirty = true;
        
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
    public final T clamp( float min, float max )
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
     * @param vec the source tuple, which will not be modified
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clamp( float min, float max, T vec )
    {
        set( vec );
        
        clamp( min, max );
        
        return ( (T)this );
    }
    
    /**
     * Clamps the minimum value of the tuple parameter to the min parameter and
     * places the values into this tuple.
     * 
     * @param min the lowest value in the tuple after clamping
     * @param vec the source tuple, which will not be modified
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMin( float min, T vec )
    {
        set( vec );
        clampMin( min );
        
        return ( (T)this );
    }
    
    /**
     * Clamps the maximum value of the tuple parameter to the max parameter and
     * places the values into this tuple.
     * 
     * @param max the highest value in the tuple after clamping
     * @param vec the source tuple, which will not be modified
     * 
     * @return itself
     */
    @SuppressWarnings( "unchecked" )
    public final T clampMax( float max, T vec )
    {
        set( vec );
        clampMax( max );
        
        return ( (T)this );
    }
    
    /**
     * Linearly interpolates between this tuple and tuple t2 and places the
     * result into this tuple: this = (1 - alpha) * this + alpha * t1.
     * 
     * @param texCoord2 the first tuple
     * @param alpha the alpha interpolation parameter
     */
    public final void interpolate( T texCoord2, float alpha )
    {
        final float beta = 1.0f - alpha;
        
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = beta * this.values[ i ] + alpha * texCoord2.values[ i ];
        }
        
        this.isDirty = true;
    }
    
    /**
     * Linearly interpolates between tuples t1 and t2 and places the result into
     * this tuple: this = (1 - alpha) * t1 + alpha * t2.
     * 
     * @param texCoord1 the first tuple
     * @param texCoord2 the second tuple
     * @param alpha the interpolation parameter
     */
    public final void interpolate( T texCoord1, T texCoord2, float alpha )
    {
        final float beta = 1.0f - alpha;
        
        for ( int i = 0; i < N; i++ )
        {
            this.values[ roTrick + i ] = beta * texCoord1.values[ i ] + alpha * texCoord2.values[ i ];
        }
        
        this.isDirty = true;
    }
    
    private static int floatToIntBits( final float f )
    {
        // Check for +0 or -0
        return ( (f == 0.0f) ? 0 : Float.floatToIntBits( f ) );
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
        int bits = 0;
        
        for ( int i = 0; i < N; i++ )
            bits ^= floatToIntBits( values[ i ] );
        
        return ( bits );
    }
    
    /**
     * Returns true if all of the data members of Tuple3f t1 are equal to the
     * corresponding data members in this
     * 
     * @param texCoord2 the texCoord with which the comparison is made.
     */
    public boolean equals( TexCoordf<?> texCoord2 )
    {
        if ( this.N != texCoord2.N )
            return ( false );
        
        for ( int i = 0; i < N; i++ )
        {
            if ( texCoord2.values[ i ] != this.values[ i ] )
                return ( false );
        }
        
        return ( true );
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
        return ( ( o != null ) && ( ( o instanceof TexCoordf ) && equals( (TexCoordf<?>)o ) ) );
    }
    
    /**
     * Returns true if the L-infinite distance between this tuple and tuple t1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2)].
     * 
     * @param texCoord2 the texCoord to be compared to this texCoord
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( T texCoord2, float epsilon )
    {
        if ( this.N != texCoord2.N )
            return ( false );
        
        for ( int i = 0; i < N; i++ )
        {
            if ( Math.abs( texCoord2.values[ i ] - this.values[ i ] ) > epsilon )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * Returns a string that contains the values of this TexCoordf.
     * The form is ( S = s, T = t, blue = p, Q = q ).
     * 
     * @return the String representation
     */
    @Override
    public String toString()
    {
        String str = "TexCoord ( ";
        if ( N >= 1 )
            str += "S = " + values[ 0 ];
        if ( N >= 2 )
            str += ", T = " + values[ 1 ];
        if ( N >= 3 )
            str += ", P = " + values[ 2 ];
        if ( N >= 4 )
            str += ", Q = " + values[ 3 ];
        
        return ( str + " )" );
    }
    
    
    protected static final float[] newArray( final float[] template, final int length )
    {
        final float[] result = new float[ length ];
        
        System.arraycopy( template, 0, result, 0, Math.min( template.length, length ) );
        
        if ( template.length < length )
        {
            Arrays.fill( result, template.length, length, 0.0f );
        }
        
        return ( result );
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
        
        isDirty = SerializationUtils.readBoolFromBuffer( pos, buffer );
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
     * Creates a new TexCoord1f instance.
     * 
     * @param values the values array (must be at least size 1)
     */
    public TexCoordf( boolean readOnly, float[] values )
    {
        super();
        
        this.values = values;
        this.N = values.length;
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * Creates a new TexCoord1f instance.
     * 
     * @param values the values array (must be at least size 1)
     */
    public TexCoordf( float[] values )
    {
        this( false, values );
    }
}
