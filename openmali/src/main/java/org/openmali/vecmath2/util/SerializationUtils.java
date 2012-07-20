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
package org.openmali.vecmath2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * The {@link SerializationUtils} provide static methods, that read/write
 * float and int data to/from a byte array.
 * 
 * @author maguila007
 * @author Marvin Froehlich (aka Qudus)
 */
public class SerializationUtils
{
    /**
     * Writes an int as four bytes to the specified position of the byte array.
     * 
     * @param i the int to write
     * @param pos the position to write to (and the succeeding three bytes)
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final int i, final int pos, final byte[] buffer )
    {
        buffer[ pos + 0 ] = (byte)( i >> 24 );
        buffer[ pos + 1 ] = (byte)( i >> 16 );
        buffer[ pos + 2 ] = (byte)( i >>  8 );
        buffer[ pos + 3 ] = (byte)( i >>  0 );
    }
    
    /**
     * Writes an int-array as four bytes each to the specified position of the byte array.
     * 
     * @param ints the int-array to write
     * @param start the index of the first  int to write
     * @param length the number of ints to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final int[] ints, final int start, final int length, int pos, final byte[] buffer )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            writeToBuffer( ints[ i ], pos, buffer );
            pos += 4;
        }
    }
    
    /**
     * Writes an int-array as four bytes each to the specified position of the byte array.
     * 
     * @param ints the int-array to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final int[] ints, final int pos, final byte[] buffer )
    {
        writeToBuffer( ints, 0, ints.length, pos, buffer );
    }
    
    /**
     * Writes a float as four bytes to the specified position of the byte array.
     * 
     * @param f the float to write
     * @param pos the position to write to (and the succeeding three bytes)
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final float f, final int pos, final byte[] buffer )
    {
        writeToBuffer( Float.floatToRawIntBits( f ), pos, buffer );
    }
    
    /**
     * Writes a float-array as four bytes each to the specified position of the byte array.
     * 
     * @param floats the float-array to write
     * @param start the index of the first  float to write
     * @param length the number of floats to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final float[] floats, final int start, final int length, int pos, final byte[] buffer )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            writeToBuffer( floats[ i ], pos, buffer );
            pos += 4;
        }
    }
    
    /**
     * Writes a double as eight bytes to the specified position of the byte array.
     * 
     * @param d the double to write
     * @param pos the position to write to (and the succeeding three bytes)
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final double d, final int pos, final byte[] buffer )
    {
        writeToBuffer( Double.doubleToRawLongBits( d ), pos, buffer );
    }
    
    /**
     * Writes a double-array as four bytes each to the specified position of the byte array.
     * 
     * @param doubles the double-array to write
     * @param start the index of the first double to write
     * @param length the number of doubles to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final double[] doubles, final int start, final int length, int pos, final byte[] buffer )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            writeToBuffer( doubles[ i ], pos, buffer );
            pos += 4;
        }
    }
    
    /**
     * Writes an int-array as four bytes each to the specified position of the byte array.
     * 
     * @param floats the float-array to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final float[] floats, final int pos, final byte[] buffer )
    {
        writeToBuffer( floats, 0, floats.length, pos, buffer );
    }
    
    /**
     * Writes a boolean as one byte to the specified position of the byte array.
     * 
     * @param b the boolean to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final boolean b, final int pos, final byte[] buffer )
    {
        buffer[ pos ] = (byte)( b ? 1 : 0 );
    }
    
    /**
     * Writes a boolean-array as one byte each to the specified position of the byte array.
     * 
     * @param bools the boolean-array to write
     * @param start the index of the first  boolean to write
     * @param length the number of booleans to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final boolean[] bools, final int start, final int length, int pos, final byte[] buffer )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            writeToBuffer( bools[ i ], pos, buffer );
            pos += 1;
        }
    }
    
    /**
     * Writes a boolean-array as one byte each to the specified position of the byte array.
     * 
     * @param bools the boolean-array to write
     * @param pos the position to write to
     * @param buffer the byte array to write to
     */
    public static final void writeToBuffer( final boolean[] bools, final int pos, final byte[] buffer )
    {
        writeToBuffer( bools, 0, bools.length, pos, buffer );
    }
    
    /**
     * Reads an int from four bytes at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * 
     * @return the read int
     */
    public static final int readIntFromBuffer( int pos, final byte[] buffer )
    {
        final int byte3 = ( buffer[ pos + 0 ] & 0x000000FF ) << 24;
        final int byte2 = ( buffer[ pos + 1 ] & 0x000000FF ) << 16;
        final int byte1 = ( buffer[ pos + 2 ] & 0x000000FF ) <<  8;
        final int byte0 = ( buffer[ pos + 3 ] & 0x000000FF ) <<  0;
        
        return ( byte3 + byte2 + byte1 + byte0 );
    }
    
    /**
     * Reads an int-array from four bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the int-array to write values to
     * @param start the index of the first int to read
     * @param length the number of ints to read
     */
    public static final void readIntsFromBuffer( int pos, final byte[] buffer, final int[] out, final int start, final int length )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            out[ i ] = readIntFromBuffer( pos, buffer );
            pos += 4;
        }
    }
    
    /**
     * Reads an int-array from four bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the int-array to write values to
     */
    public static final void readIntsFromBuffer( final int pos, final byte[] buffer, final int[] out )
    {
        readIntsFromBuffer( pos, buffer, out, 0, out.length );
    }
    
    /**
     * Reads an long from eight bytes at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * 
     * @return the read long
     */
    public static final long readLongFromBuffer( int pos, final byte[] buffer )
    {
        final int byte7 = ( buffer[ pos + 0 ] & 0x00000000000000FF ) << 64;
        final int byte6 = ( buffer[ pos + 1 ] & 0x00000000000000FF ) << 48;
        final int byte5 = ( buffer[ pos + 2 ] & 0x00000000000000FF ) << 40;
        final int byte4 = ( buffer[ pos + 3 ] & 0x00000000000000FF ) << 32;
        final int byte3 = ( buffer[ pos + 4 ] & 0x00000000000000FF ) << 24;
        final int byte2 = ( buffer[ pos + 5 ] & 0x00000000000000FF ) << 16;
        final int byte1 = ( buffer[ pos + 6 ] & 0x00000000000000FF ) <<  8;
        final int byte0 = ( buffer[ pos + 7 ] & 0x00000000000000FF ) <<  0;
        
        return ( byte7 + byte6 + byte5 + byte4 + byte3 + byte2 + byte1 + byte0 );
    }
    
    /**
     * Reads an long-array from eight bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the long-array to write values to
     * @param start the index of the first long to read
     * @param length the number of longs to read
     */
    public static final void readLongsFromBuffer( int pos, final byte[] buffer, final long[] out, final int start, final int length )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            out[ i ] = readLongFromBuffer( pos, buffer );
            pos += 8;
        }
    }
    
    /**
     * Reads an long-array from eight bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the long-array to write values to
     */
    public static final void readLongsFromBuffer( final int pos, final byte[] buffer, final long[] out )
    {
        readLongsFromBuffer( pos, buffer, out, 0, out.length );
    }
    
    /**
     * Reads a float from four bytes at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * 
     * @return the read float
     */
    public static final float readFloatFromBuffer( final int pos, final byte[] buffer )
    {
        return ( Float.intBitsToFloat( readIntFromBuffer( pos, buffer ) ) );
    }
    
    /**
     * Reads a float-array from four bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the float-array to write values to
     * @param start the index of the first float to read
     * @param length the number of floats to read
     */
    public static final void readFloatsFromBuffer( int pos, final byte[] buffer, final float[] out, final int start, final int length )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            out[ i ] = readFloatFromBuffer( pos, buffer );
            pos += 4;
        }
    }
    
    /**
     * Reads a float-array from four bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the float-array to write values to
     */
    public static final void readFloatsFromBuffer( final int pos, final byte[] buffer, final float[] out )
    {
        readFloatsFromBuffer( pos, buffer, out, 0, out.length );
    }
    
    /**
     * Reads a double from eight bytes at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * 
     * @return the read double
     */
    public static final double readDoubleFromBuffer( final int pos, final byte[] buffer )
    {
        return ( Double.longBitsToDouble( readLongFromBuffer( pos, buffer ) ) );
    }
    
    /**
     * Reads a double-array from eight bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the double-array to write values to
     * @param start the index of the first double to read
     * @param length the number of doubles to read
     */
    public static final void readDoublesFromBuffer( int pos, final byte[] buffer, final double[] out, final int start, final int length )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            out[ i ] = readDoubleFromBuffer( pos, buffer );
            pos += 4;
        }
    }
    
    /**
     * Reads a double-array from eight bytes each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the double-array to write values to
     */
    public static final void readDoublesFromBuffer( final int pos, final byte[] buffer, final double[] out )
    {
        readDoublesFromBuffer( pos, buffer, out, 0, out.length );
    }
    
    /**
     * Reads a boolean from one byte at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * 
     * @return the read boolean
     */
    public static final boolean readBoolFromBuffer( int pos, final byte[] buffer )
    {
        return ( ( buffer[ pos ] & 0x000000FF ) != 0 );
    }
    
    /**
     * Reads a boolean-array from one byte each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the boolean-array to write values to
     * @param start the index of the first boolean to read
     * @param length the number of booleans to read
     */
    public static final void readBoolsFromBuffer( int pos, final byte[] buffer, final boolean[] out, final int start, final int length )
    {
        final int end = start + length;
        for ( int i = start; i < end; i++ )
        {
            out[ i ] = readBoolFromBuffer( pos, buffer );
            pos += 1;
        }
    }
    
    /**
     * Reads a boolean-array from one byte each at the specified position of the byte array.
     * 
     * @param pos the position to start reading at
     * @param buffer the byte array to read from
     * @param out the boolean-array to write values to
     */
    public static final void readBoolsFromBuffer( final int pos, final byte[] buffer, final boolean[] out )
    {
        readBoolsFromBuffer( pos, buffer, out, 0, out.length );
    }
    
    /**
     * Stores a Serializable Object.
     * 
     * @param object
     * @param out
     * 
     * @throws IOException
     */
    public static void saveObject( Object object, OutputStream out ) throws IOException
    {
        if ( !( object instanceof Serializable ) )
            throw new IllegalArgumentException( "The object is not Serializable!" );
        
        new ObjectOutputStream( out ).writeObject( object );
    }
    
    /**
     * Stores a Serializable Object.
     * 
     * @param object
     * @param filename
     * 
     * @throws IOException
     */
    public static void saveObject( Object object, String filename ) throws IOException
    {
        saveObject( object, new FileOutputStream( filename ) );
    }
    
    /**
     * Stores a Serializable Object.
     * 
     * @param object
     * @param file
     * 
     * @throws IOException
     */
    public static void saveObject( Object object, File file ) throws IOException
    {
        saveObject( object, new FileOutputStream( file ) );
    }
    
    /**
     * Loads an Object.
     * 
     * @param in
     * 
     * @return the object
     * 
     * @throws IOException
     */
    public static Object loadObject( InputStream in ) throws IOException, ClassNotFoundException
    {
        return ( new ObjectInputStream( in ).readObject() );
    }
    
    /**
     * Loads an Object.
     * 
     * @param filename
     * 
     * @return the object
     * 
     * @throws IOException
     */
    public static Object loadObject( String filename ) throws IOException, ClassNotFoundException
    {
        return loadObject( new FileInputStream( filename ) );
    }
    
    /**
     * Loads an Object.
     * 
     * @param file
     * 
     * @return the object
     * 
     * @throws IOException
     */
    public static Object loadObject( File file ) throws IOException, ClassNotFoundException
    {
        return loadObject( new FileInputStream( file ) );
    }
}
