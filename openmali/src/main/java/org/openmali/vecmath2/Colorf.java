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

import org.openmali.vecmath2.pools.ColorfPool;
import org.openmali.vecmath2.util.ColorUtils;

import org.openmali.vecmath2.util.SerializationUtils;

/**
 * A simple float-based color implementation with or without alpha channel.
 * 
 * Inspired by Kenji Hiranabe's Color3f/Color4f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Colorf implements Externalizable
{
    private static final long serialVersionUID = -818575512943622856L;
    
    //private static final ColorPool POOL = new ColorPool( 32 );
    private static final ThreadLocal<ColorfPool> POOL = new ThreadLocal<ColorfPool>()
    {
        @Override
        protected ColorfPool initialValue()
        {
            return ( new ColorfPool( 128 ) );
        }
    };
    
    protected static final int N = 4;
    protected final float[] values;
    protected boolean hasAlpha;
    
    protected final int roTrick;
    
    /*
     * This boolean is implemented as a one-elemental array
     * to be able to share its value with a read-only instance.
     */
    protected final boolean[] isDirty;
    
    private Colorf readOnlyInstance = null;
    
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
     * Creates a gray of the given intensity.<br>
     * This is euqal to new Color3f(intensity, intensity, intensity);
     * 
     * @param intensity the gray's intensity to be applied to all three components.
     * 
     * @return the new gray color
     */
    public static Colorf createGray( float intensity )
    {
        return ( new Colorf( intensity, intensity, intensity ) );
    }
    
    /**
     * The color white. In the default sRGB space.
     */
    public static final Colorf WHITE = newReadOnly( java.awt.Color.WHITE );
    
    /**
     * The color white and fully transparent. In the default RGBA space.
     */
    public static final Colorf WHITE_TRANSPARENT = newReadOnly( WHITE, 1.0f );
    
    /**
     * The color light gray. In the default sRGB space.
     */
    public static final Colorf LIGHT_GRAY = newReadOnly( java.awt.Color.GRAY );
    
    /**
     * A 10% gray. In the default sRGB space.
     */
    public static final Colorf GRAY10 = newReadOnly( 0.9f );
    
    /**
     * A 20% gray. In the default sRGB space.
     */
    public static final Colorf GRAY20 = newReadOnly( 0.8f );
    
    /**
     * A 25% gray. In the default sRGB space.
     */
    public static final Colorf GRAY25 = newReadOnly( 0.75f );
    
    /**
     * A 30% gray. In the default sRGB space.
     */
    public static final Colorf GRAY30 = newReadOnly( 0.7f );
    
    /**
     * A 40% gray. In the default sRGB space.
     */
    public static final Colorf GRAY40 = newReadOnly( 0.6f );
    
    /**
     * A 50% gray. In the default sRGB space.
     */
    public static final Colorf GRAY50 = newReadOnly( 0.5f );
    
    /**
     * A 60% gray. In the default sRGB space.
     */
    public static final Colorf GRAY60 = newReadOnly( 0.4f );
    
    /**
     * A 70% gray. In the default sRGB space.
     */
    public static final Colorf GRAY70 = newReadOnly( 0.3f );
    
    /**
     * A 75% gray. In the default sRGB space.
     */
    public static final Colorf GRAY75 = newReadOnly( 0.25f );
    
    /**
     * A 80% gray. In the default sRGB space.
     */
    public static final Colorf GRAY80 = newReadOnly( 0.2f );
    
    /**
     * A 90% gray. In the default sRGB space.
     */
    public static final Colorf GRAY90 = newReadOnly( 0.1f );
    
    /**
     * The color gray. In the default sRGB space.
     */
    public static final Colorf GRAY = newReadOnly( java.awt.Color.GRAY );
    
    /**
     * The color dark gray. In the default sRGB space.
     */
    public static final Colorf DARK_GRAY = newReadOnly( java.awt.Color.DARK_GRAY );
    
    /**
     * The color black. In the default sRGB space.
     */
    public static final Colorf BLACK = newReadOnly( java.awt.Color.BLACK );
    
    /**
     * The color black and fully transparent. In the default RGBA space.
     */
    public static final Colorf BLACK_TRANSPARENT = newReadOnly( BLACK, 1.0f );
    
    /**
     * The color brown. In the default sRGB space.
     */
    public static final Colorf BROWN = Colorf.parseReadOnlyColor( "#6D493B" );
    
    /**
     * The color brown. In the default sRGB space.
     */
    public static final Colorf LIGHT_BROWN = Colorf.parseReadOnlyColor( "#91624F" );
    
    /**
     * The color brown. In the default sRGB space.
     */
    public static final Colorf DARK_BROWN = Colorf.parseReadOnlyColor( "#583B30" );
    
    /**
     * The color red. In the default sRGB space.
     */
    public static final Colorf RED = newReadOnly( java.awt.Color.RED );
    
    /**
     * The color pink. In the default sRGB space.
     */
    public static final Colorf PINK = newReadOnly( java.awt.Color.PINK );
    
    /**
     * The color orange. In the default sRGB space.
     */
    public static final Colorf ORANGE = newReadOnly( java.awt.Color.ORANGE );
    
    /**
     * The color yellow. In the default sRGB space.
     */
    public static final Colorf YELLOW = newReadOnly( java.awt.Color.YELLOW );
    
    /**
     * The color green. In the default sRGB space.
     */
    public static final Colorf GREEN = newReadOnly( java.awt.Color.GREEN );
    
    /**
     * The color magenta. In the default sRGB space.
     */
    public static final Colorf MAGENTA = newReadOnly( java.awt.Color.MAGENTA );
    
    /**
     * The color cyan. In the default sRGB space.
     */
    public static final Colorf CYAN = newReadOnly( java.awt.Color.CYAN );
    
    /**
     * The color blue. In the default sRGB space.
     */
    public static final Colorf BLUE = newReadOnly( java.awt.Color.BLUE );
    
    /**
     * @return this Vector's size().
     */
    public final int getSize()
    {
        return ( N );
    }
    
    /**
     * @return if this Colorf has an alpha channel.
     */
    public final boolean hasAlpha()
    {
        return ( hasAlpha );
    }
    
    /**
     * Sets color from awt.Color.
     * 
     * @param color awt color
     */
    public final void set( java.awt.Color color )
    {
        setRed( ((float)color.getRed()) / 255.0f );
        setGreen( ((float)color.getGreen()) / 255.0f );
        setBlue( ((float)color.getBlue()) / 255.0f );
        
        hasAlpha = ( color.getAlpha() < 255 );
        if ( hasAlpha )
            setAlpha( ((float)(255 - color.getAlpha())) / 255.0f );
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size 4 for with-alpha else 3)
     * @param offset the offset in the (source) values array
     * @param alpha
     */
    public final void set( float[] values, int offset, boolean alpha )
    {
        if ( alpha )
        {
            System.arraycopy( values, 0, this.values, roTrick + 0, 4 );
            hasAlpha = true;
        }
        else
        {
            System.arraycopy( values, 0, this.values, roTrick + 0, 3 );
            hasAlpha = false;
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size 4 for with-alpha else 3)
     * @param alpha
     */
    public final void set( float[] values, boolean alpha )
    {
        set( values, 0, alpha );
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size 3)
     */
    public final void set( float[] values )
    {
        set( values, 0, ( values.length > 3 ) && ( values[ 3 ] > 0.0f ) );
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size 4)
     */
    public final void setBytes( byte[] values )
    {
        if ( values.length > 3 )
        {
            setRedByte( values[ 0 ] );
            setGreenByte( values[ 1 ] );
            setBlueByte( values[ 2 ] );
            setAlphaByte( values[ 3 ] );
        }
        else
        {
            setRedByte( values[ 0 ] );
            setGreenByte( values[ 1 ] );
            setBlueByte( values[ 2 ] );
            
            hasAlpha = false;
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size 4)
     */
    public final void setInts( int[] values )
    {
        if ( values.length > 3 )
        {
            setRedInt( values[ 0 ] );
            setGreenInt( values[ 1 ] );
            setBlueInt( values[ 2 ] );
            setAlphaInt( values[ 3 ] );
        }
        else
        {
            setRedInt( values[ 0 ] );
            setGreenInt( values[ 1 ] );
            setBlueInt( values[ 2 ] );
            
            hasAlpha = false;
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param color the tuple to be copied
     */
    public final void set( Colorf color )
    {
        System.arraycopy( color.values, 0, this.values, roTrick + 0, N );
        hasAlpha = color.hasAlpha;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets all values of this color to the specified ones.
     * 
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     * @param a the alpha element to use
     */
    public final void set( float r, float g, float b, float a )
    {
        setRed( r );
        setGreen( g );
        setBlue( b );
        setAlpha( a );
        
        this.hasAlpha = ( a >= 0.0f );
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets all values of this color to the specified ones.
     * 
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     */
    public final void set( float r, float g, float b )
    {
        setRed( r );
        setGreen( g );
        setBlue( b );
        
        this.hasAlpha = false;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Gets java.awt.Color.
     * 
     * @return AWT color
     */
    public final java.awt.Color getAWTColor()
    {
        if ( hasAlpha() )
            return ( new java.awt.Color( getRed(), getGreen(), getBlue(), 1f - getAlpha() ) );
        
        return ( new java.awt.Color( getRed(), getGreen(), getBlue() ) );
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     * @param offset the offset in the (target) buffer array
     */
    public final void get( float[] buffer, int offset )
    {
        final int n = hasAlpha() ? 4 : 3;
        System.arraycopy( this.values, 0, buffer, offset, n );
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
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     */
    public final void getBytes( byte[] buffer )
    {
        buffer[ 0 ] = getRedByte();
        buffer[ 1 ] = getGreenByte();
        buffer[ 2 ] = getBlueByte();
        
        if ( this.hasAlpha() && ( buffer.length >= 4 ) )
            buffer[ 3 ] = getAlphaByte();
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     */
    public final void getInts( int[] buffer )
    {
        buffer[ 0 ] = getRedInt();
        buffer[ 1 ] = getGreenInt();
        buffer[ 2 ] = getBlueInt();
        
        if ( this.hasAlpha() && ( buffer.length >= 4 ) )
            buffer[ 3 ] = getAlphaInt();
    }
    
    /**
     * Writes all values of this vector to the specified buffer vector.
     * 
     * @param buffer the buffer vector to write the values to
     */
    public final void get( Colorf buffer )
    {
        System.arraycopy( this.values, 0, buffer.values, 0, N );
        buffer.hasAlpha = this.hasAlpha;
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param forceAlpha if true, an alpha value is written to the buffer regardless of this color having an alpha value of not.
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public final java.nio.FloatBuffer writeToBuffer( boolean forceAlpha, java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        if ( hasAlpha() )
            buffer.put( values );
        else if ( forceAlpha )
            buffer.put( values[ 0 ] ).put( values[ 1 ] ).put( values[ 2 ] ).put( 0f );
        else
            buffer.put( values[ 0 ] ).put( values[ 1 ] ).put( values[ 2 ] );
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param forceAlpha if true, an alpha value is written to the buffer regardless of this color having an alpha value of not.
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public final java.nio.FloatBuffer writeToBuffer( boolean forceAlpha, java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        return ( writeToBuffer( forceAlpha, buffer, false, flip ) );
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
        return ( writeToBuffer( false, buffer, clear, flip ) );
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
        
        return ( writeToBuffer( false, buffer, false, flip ) );
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param colors
     * @param forceAlpha if true, an alpha value is written to the buffer regardless of this color having an alpha value of not.
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( Colorf[] colors, boolean forceAlpha, java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        for ( int i = 0; i < colors.length; i++ )
        {
            colors[ i ].writeToBuffer( forceAlpha, buffer, false, false );
        }
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this tuple to a FloatBuffer.<br>
     * 
     * @param colors
     * @param forceAlpha if true, an alpha value is written to the buffer regardless of this color having an alpha value of not.
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( Colorf[] colors, boolean forceAlpha, java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        return ( writeToBuffer( colors, forceAlpha, buffer, false, flip ) );
    }
    
    /**
     * Writes the contents of the colors to a FloatBuffer.<br>
     * 
     * @param colors
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( Colorf[] colors, java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        return ( writeToBuffer( colors, false, buffer, clear, flip ) );
    }
    
    /**
     * Writes the contents of the colors to a FloatBuffer.<br>
     * 
     * @param colors
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     */
    public static final java.nio.FloatBuffer writeToBuffer( Colorf[] colors, java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        return ( writeToBuffer( colors, false, buffer, false, flip ) );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param buffer
     * @param forceAlpha if true, an alpha value is read from the buffer regardless of this color having an alpha value of not.
     */
    public final java.nio.FloatBuffer readFromBuffer( boolean forceAlpha, java.nio.FloatBuffer buffer )
    {
        if ( hasAlpha() || forceAlpha )
            buffer.get( values );
        else
            buffer.get( values, 0, 3 );
        
        return ( buffer );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param forceAlpha if true, an alpha value is read from the buffer regardless of this color having an alpha value of not.
     * @param buffer
     * @param position position in the buffer
     */
    public final java.nio.FloatBuffer readFromBuffer( boolean forceAlpha, java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        if ( hasAlpha() || forceAlpha )
            buffer.get( values );
        else
            buffer.get( values, 0, 3 );
        
        return ( buffer );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param buffer
     */
    public final java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer )
    {
        return ( readFromBuffer( false, buffer ) );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     */
    public final java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer, int position )
    {
        return ( readFromBuffer( false, buffer, position ) );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param colors
     * @param forceAlpha if true, an alpha value is written to the buffer regardless of this color having an alpha value of not.
     * @param buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( Colorf[] colors, boolean forceAlpha, java.nio.FloatBuffer buffer )
    {
        for ( int i = 0; i < colors.length; i++ )
        {
            colors[ i ].readFromBuffer( forceAlpha, buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param colors
     * @param forceAlpha if true, an alpha value is written to the buffer regardless of this color having an alpha value of not.
     * @param buffer
     * @param position position in the buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( Colorf[] colors, boolean forceAlpha, java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        return ( readFromBuffer( colors, forceAlpha, buffer ) );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param colors
     * @param buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( Colorf[] colors, java.nio.FloatBuffer buffer )
    {
        return ( readFromBuffer( colors, false, buffer ) );
    }
    
    /**
     * Reads the data of one tuple from a FloatBuffer.<br>
     * 
     * @param colors
     * @param buffer
     * @param position position in the buffer
     */
    public static final java.nio.FloatBuffer readFromBuffer( Colorf[] colors, java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        return ( readFromBuffer( colors, false, buffer ) );
    }
    
    /**
     * Sets the Red color component.
     */
    public final void setRed( float red )
    {
        this.values[ roTrick + 0 ] = red;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Red color component.
     */
    public final float getRed()
    {
        return ( values[ 0 ] );
    }
    
    /**
     * Sets the Red color component.
     */
    public final void r( float red )
    {
        this.values[ roTrick + 0 ] = red;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Red color component.
     */
    public final float r()
    {
        return ( values[ 0 ] );
    }
    
    /**
     * Sets the Red color component.
     */
    public final void setRedByte( byte red )
    {
        this.values[ roTrick + 0 ] = ( red & 0x000000FF ) / 255f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Red color component.
     */
    public final byte getRedByte()
    {
        return ( (byte)( values[ 0 ] * 255 ) );
    }
    
    /**
     * Sets the Red color component.
     */
    public final void setRedInt( int red )
    {
        this.values[ roTrick + 0 ] = red / 255f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Red color component.
     */
    public final int getRedInt()
    {
        return ( (int)( values[ 0 ] * 255 ) );
    }
    
    /**
     * Sets the Green color component.
     */
    public final void setGreen( float green )
    {
        this.values[ roTrick + 1 ] = green;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Green color component.
     */
    public final float getGreen()
    {
        return ( values[ 1 ] );
    }
    
    /**
     * Sets the Green color component.
     */
    public final void g( float green )
    {
        this.values[ roTrick + 1 ] = green;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Green color component.
     */
    public final float g()
    {
        return ( values[ 1 ] );
    }
    
    /**
     * Sets the Green color component.
     */
    public final void setGreenByte( byte green )
    {
        this.values[ roTrick + 1 ] = ( green & 0x000000FF ) / 255f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Green color component.
     */
    public final byte getGreenByte()
    {
        return ( (byte)( values[ 1 ] * 255 ) );
    }
    
    /**
     * Sets the Green color component.
     */
    public final void setGreenInt( int green )
    {
        this.values[ roTrick + 1 ] = green / 255f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Green color component.
     */
    public final int getGreenInt()
    {
        return ( (int)( values[ 1 ] * 255 ) );
    }
    
    /**
     * Sets the Blue color component.
     */
    public final void setBlue( float blue )
    {
        this.values[ roTrick + 2 ] = blue;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Blue color component.
     */
    public final float getBlue()
    {
        return ( values[ 2 ] );
    }
    
    /**
     * Sets the Blue color component.
     */
    public final void b( float blue )
    {
        this.values[ roTrick + 2 ] = blue;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Blue color component.
     */
    public final float b()
    {
        return ( values[ 2 ] );
    }
    
    /**
     * Sets the Blue color component.
     */
    public final void setBlueByte( byte blue )
    {
        this.values[ roTrick + 2 ] = ( blue & 0x000000FF ) / 255f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Blue color component.
     */
    public final byte getBlueByte()
    {
        return ( (byte)( values[ 2 ] * 255 ) );
    }
    
    /**
     * Sets the Blue color component.
     */
    public final void setBlueInt( int blue )
    {
        this.values[ roTrick + 2 ] = blue / 255f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Blue color component.
     */
    public final int getBlueInt()
    {
        return ( (int)( values[ 2 ] * 255 ) );
    }
    
    /**
     * Sets the value of the alpha-element of this color.
     * 
     * @param alpha
     */
    public final void setAlpha( float alpha )
    {
        this.values[ roTrick + 3 ] = alpha;
        
        this.hasAlpha = ( alpha >= 0.0f );
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the value of the alpha-element of this color.
     */
    public final float getAlpha()
    {
        if ( hasAlpha() )
            return ( values[ 3 ] );
        
        return ( 0f );
    }
    
    /**
     * Sets the value of the alpha-element of this color.
     * 
     * @param alpha
     */
    public final void a( float alpha )
    {
        this.values[ roTrick + 3 ] = alpha;
        
        this.hasAlpha = ( alpha >= 0.0f );
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the value of the alpha-element of this color.
     */
    public final float a()
    {
        if ( hasAlpha() )
            return ( values[ 3 ] );
        
        return ( 0f );
    }
    
    /**
     * Sets the Alpha value component.
     */
    public final void setAlphaByte( byte alpha )
    {
        this.values[ roTrick + 3 ] = ( alpha & 0x000000FF ) / 255f;
        
        this.hasAlpha = ( this.values[ roTrick + 3 ] >= 0.0f );
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Alhpa value component.
     */
    public final byte getAlphaByte()
    {
        if ( hasAlpha() )
            return ( (byte)( values[ 3 ] * 255 ) );
        
        return ( (byte)0 );
    }
    
    /**
     * Sets the Alpha value component.
     */
    public final void setAlphaInt( int alpha )
    {
        this.values[ roTrick + 3 ] = alpha / 255f;
        
        this.hasAlpha = ( this.values[ roTrick + 3 ] >= 0.0f );
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the Alhpa value component.
     */
    public final int getAlphaInt()
    {
        if ( hasAlpha() )
            return ( (int)( values[ 3 ] * 255 ) );
        
        return ( 0 );
    }
    
    /**
     * Sets all components to zero.
     * 
     * @return itself
     */
    public final Colorf setZero()
    {
        for ( int i = 0; i < 3; i++ )
        {
            this.values[ roTrick + i ] = 0f;
        }
        
        if ( hasAlpha() )
            this.values[ roTrick + 3 ] = 0.0f;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this color's red value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf addRed( float v )
    {
        this.values[ roTrick + 0 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this color's green value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf addGreen( float v )
    {
        this.values[ roTrick + 1 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this color's blue value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf addBlue( float v )
    {
        this.values[ roTrick + 2 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds v to this color's alpha value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf addAlpha( float v )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 3 ] += v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this color's red value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf subRed( float v )
    {
        this.values[ roTrick + 0 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this color's green value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf subGreen( float v )
    {
        this.values[ roTrick + 1 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this color's blue value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf subBlue( float v )
    {
        this.values[ roTrick + 2 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts v from this color's alpha value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf subAlpha( float v )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 3 ] -= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this color's red value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf mulRed( float v )
    {
        this.values[ roTrick + 0 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this color's green value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf mulGreen( float v )
    {
        this.values[ roTrick + 1 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this color's blue value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf mulBlue( float v )
    {
        this.values[ roTrick + 2 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this color's alpha value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf mulAlpha( float v )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 3 ] *= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this color's values with vr, vg, vb, va.
     * 
     * @param vr
     * @param vg
     * @param vb
     * @param va
     * 
     * @return itself
     */
    public final Colorf mul( float vr, float vg, float vb, float va )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 0 ] *= vr;
        this.values[ roTrick + 1 ] *= vg;
        this.values[ roTrick + 2 ] *= vb;
        this.values[ roTrick + 3 ] *= va;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this color's values with vr, vg, vb.
     * 
     * @param vr
     * @param vg
     * @param vb
     * 
     * @return itself
     */
    public final Colorf mul( float vr, float vg, float vb )
    {
        this.values[ roTrick + 0 ] *= vr;
        this.values[ roTrick + 1 ] *= vg;
        this.values[ roTrick + 2 ] *= vb;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself.
     * 
     * @param factor the scalar value
     * 
     * @return itself
     */
    public final Colorf mul( float factor )
    {
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            this.values[ roTrick + i ] *= factor;
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this color's red value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf divRed( float v )
    {
        this.values[ roTrick + 0 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this color's green value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf divGreen( float v )
    {
        this.values[ roTrick + 1 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this color's blue value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf divBlue( float v )
    {
        this.values[ roTrick + 2 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this color's alpha value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Colorf divAlpha( float v )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 3 ] /= v;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this color's values by vr, vg, vb, va.
     * 
     * @param vr
     * @param vg
     * @param vb
     * @param va
     * 
     * @return itself
     */
    public final Colorf div( float vr, float vg, float vb, float va )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 0 ] /= vr;
        this.values[ roTrick + 1 ] /= vg;
        this.values[ roTrick + 2 ] /= vb;
        this.values[ roTrick + 3 ] /= va;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Divides this color's values by vr, vg, vb.
     * 
     * @param vr
     * @param vg
     * @param vb
     * 
     * @return itself
     */
    public final Colorf div( float vr, float vg, float vb )
    {
        this.values[ roTrick + 0 ] /= vr;
        this.values[ roTrick + 1 ] /= vg;
        this.values[ roTrick + 2 ] /= vb;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this color to the vector sum of colors color1 and color2.
     * 
     * @param color1 the first color
     * @param color2 the second color
     * 
     * @return itself
     */
    public final Colorf add( Colorf color1, Colorf color2 )
    {
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            this.values[ roTrick + i ] = color1.values[ i ] + color2.values[ i ];
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this tuple to the vector sum of itself and tuple t1.
     * 
     * @param color2 the other tuple
     * 
     * @return itself
     */
    public final Colorf add( Colorf color2 )
    {
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            this.values[ roTrick + i ] += color2.values[ i ];
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param r
     * @param g
     * @param b
     * @param a
     * 
     * @return itself
     */
    public final Colorf add( float r, float g, float b, float a )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 0 ] += r;
        this.values[ roTrick + 1 ] += g;
        this.values[ roTrick + 2 ] += b;
        this.values[ roTrick + 3 ] += a;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param r
     * @param g
     * @param b
     * 
     * @return itself
     */
    public final Colorf add( float r, float g, float b )
    {
        this.values[ roTrick + 0 ] += r;
        this.values[ roTrick + 1 ] += g;
        this.values[ roTrick + 2 ] += b;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this color to the vector difference of color color1 and color2
     * (this = color1 - color2).
     * 
     * @param color1 the first color
     * @param color2 the second color
     * 
     * @return itself
     */
    public final Colorf sub( Colorf color1, Colorf color2 )
    {
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            this.values[ roTrick + i ] = color1.values[ i ] - color2.values[ i ];
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this color to the vector difference of itself and color2
     * (this = this - color2).
     * 
     * @param color2 the other color
     * 
     * @return itself
     */
    public final Colorf sub( Colorf color2 )
    {
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            this.values[ roTrick + i ] -= color2.values[ i ];
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param r
     * @param g
     * @param b
     * @param a
     * 
     * @return itself
     */
    public final Colorf sub( float r, float g, float b, float a )
    {
        if ( !hasAlpha() )
            throw new UnsupportedOperationException( "no alpha channel" );
        
        this.values[ roTrick + 0 ] -= r;
        this.values[ roTrick + 1 ] -= g;
        this.values[ roTrick + 2 ] -= b;
        this.values[ roTrick + 3 ] -= a;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this tuple's values.
     * 
     * @param r
     * @param g
     * @param b
     * 
     * @return itself
     */
    public final Colorf sub( float r, float g, float b )
    {
        this.values[ roTrick + 0 ] -= r;
        this.values[ roTrick + 1 ] -= g;
        this.values[ roTrick + 2 ] -= b;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Clamps the minimum value of this tuple to the min parameter.
     * 
     * @param min the lowest value in this tuple after clamping
     * 
     * @return itself
     */
    public final Colorf clampMin( float min )
    {
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            if (this.values[ i ] < min )
                this.values[ roTrick + i ] = min;
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Clamps the maximum value of this tuple to the max parameter.
     * 
     * @param max the highest value in the tuple after clamping
     * 
     * @return itself
     */
    public final Colorf clampMax( float max )
    {
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            if (this.values[ i ] > max )
                this.values[ roTrick + i ] = max;
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Clamps this tuple to the range [min, max].
     * 
     * @param min the lowest value in this tuple after clamping
     * @param max the highest value in this tuple after clamping
     * 
     * @return itself
     */
    public final Colorf clamp( float min, float max )
    {
        clampMin( min );
        clampMax( max );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
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
    public final Colorf clamp( float min, float max, Colorf vec )
    {
        set( vec );
        
        clamp( min, max );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
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
    public final Colorf clampMin( float min, Colorf vec )
    {
        set( vec );
        clampMin( min );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
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
    public final Colorf clampMax( float max, Colorf vec )
    {
        set( vec );
        clampMax( max );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Linearly interpolates between this tuple and tuple t2 and places the
     * result into this tuple: this = (1 - alpha) * this + alpha * t1.
     * 
     * @param color2 the first tuple
     * @param val the alpha interpolation parameter
     * 
     * @return itself
     */
    public final Colorf interpolate( Colorf color2, float val )
    {
        final float beta = 1.0f - val;
        
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            this.values[ roTrick + i ] = beta * this.values[ i ] + val * color2.values[ i ];
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Linearly interpolates between tuples t1 and t2 and places the result into
     * this tuple: this = (1 - alpha) * t1 + alpha * t2.
     * 
     * @param color1 the first tuple
     * @param color2 the second tuple
     * @param val the interpolation parameter
     * 
     * @return itself
     */
    public final Colorf interpolate( Colorf color1, Colorf color2, float val )
    {
        final float beta = 1.0f - val;
        
        final int n = hasAlpha() ? 4 : 3;
        
        for ( int i = 0; i < n; i++ )
        {
            this.values[ roTrick + i ] = beta * color1.values[ i ] + val * color2.values[ i ];
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    private static int floatToIntBits( final float f )
    {
        // Check for +0 or -0
        return ( (f == 0.0f) ? 0 : Float.floatToIntBits( f ) );
    }
    
    /**
     * @return a new instance sharing the values array with this instance.
     * The new instance is read-only. Changes to this instance will be reflected
     * in the new read-only-instance.
     * 
     * @see #getReadOnly()
     */
    public Colorf asReadOnly()
    {
        return ( new Colorf( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * @return a single instance sharing the values array with this instance (one unique instance per 'master-instance').
     * The instance is read-only. Changes to this instance will be reflected
     * in the read-only-instance.
     * 
     * @see #asReadOnly()
     */
    public Colorf getReadOnly()
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
        final int rbits = floatToIntBits( values[ 0 ] );
        final int gbits = floatToIntBits( values[ 1 ] );
        final int bbits = floatToIntBits( values[ 2 ] );
        
        if ( hasAlpha() )
            return ( rbits ^ gbits ^ bbits ^ floatToIntBits( values[ 3 ] ) );
        
        return ( rbits ^ gbits ^ bbits );
    }
    
    /**
     * Returns true if all of the data members of Tuple3f t1 are equal to the
     * corresponding data members in this
     * 
     * @param color2 the color with which the comparison is made.
     */
    public boolean equals( Colorf color2 )
    {
        if ( this.hasAlpha() != color2.hasAlpha() )
            return ( false );
        
        final int n = hasAlpha() ? 4 : 3;
        for ( int i = 0; i < n; i++ )
        {
            if ( color2.values[ i ] != this.values[ i ] )
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
        return ( ( o != null ) && ( ( o instanceof Colorf ) && equals( (Colorf)o ) ) );
    }
    
    /**
     * Returns true if the L-infinite distance between this tuple and tuple t1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2)].
     * 
     * @param color2 the color to be compared to this color
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( Colorf color2, float epsilon )
    {
        if ( this.hasAlpha() != color2.hasAlpha() )
            return ( false );
        
        final int n = hasAlpha() ? 4 : 3;
        for ( int i = 0; i < n; i++ )
        {
            if ( Math.abs( color2.values[ i ] - this.values[ i ] ) > epsilon )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * @return a hex-String represending the RGB-values of this color.
     */
    public final String toHexString()
    {
        return ( ColorUtils.colorToHex( this ) );
    }
    
    /**
     * Returns a string that contains the values of this Colorf.
     * The form is ( red = f, green = f, blue = f, alpha = f ).
     * 
     * @return the String representation
     */
    @Override
    public String toString()
    {
        String str = "Color ( red = " + getRed() + ", green = " + getGreen() + ", blue = " + getBlue();
        
        if ( hasAlpha() )
            return ( str + ", alpha = " + getAlpha() + " )" );
        
        return ( str + " )" );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Colorf clone()
    {
        return ( new Colorf( this ) );
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
        for ( int i = 0; i < values.length; i++ )
        {
            SerializationUtils.writeToBuffer( values[ i ], pos, buffer );
            pos += 4;
        }
        
        SerializationUtils.writeToBuffer( hasAlpha, pos, buffer );
        pos += 1;
        SerializationUtils.writeToBuffer( isDirty[ 0 ], pos, buffer );
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
        for ( int i = 0; i < values.length; i++ )
        {
            values[ i ] = SerializationUtils.readFloatFromBuffer( pos, buffer );
            pos += 4;
        }
        
        hasAlpha = SerializationUtils.readBoolFromBuffer( pos, buffer );
        pos += 1;
        isDirty[ 0 ] = SerializationUtils.readBoolFromBuffer( pos, buffer );
        pos += 1;
        
        return ( pos );
    }
    
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        final byte[] buffer = new byte[ N * 4 + 2 ];
        
        serialize( 0, buffer );
        
        out.write( buffer );
    }
    
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        final byte[] buffer = new byte[ N * 4 + 2 ];
        
        in.read( buffer );
        
        deserialize( 0, buffer );
    }
    
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param readOnly
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     * @param a the alpha channel to use
     */
    protected Colorf( boolean readOnly, float r, float g, float b, float a )
    {
        this.values = new float[] { r, g, b, a };
        this.hasAlpha = ( a >= 0.0f );
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[] { false };
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param readOnly
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     */
    protected Colorf( boolean readOnly, float r, float g, float b )
    {
        this( readOnly, r, g, b, -1f );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param readOnly
     * @param intensity the gray intensity (used for all three r,g,b values
     */
    protected Colorf( boolean readOnly, float intensity )
    {
        this( readOnly, intensity, intensity, intensity );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 3)
     * @param isDirty
     * @param copy
     */
    protected Colorf( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        if ( copy )
        {
            this.values = new float[ N ];
            if ( values.length >= N )
            {
                System.arraycopy( values, 0, this.values, 0, N );
            }
            else
            {
                System.arraycopy( values, 0, this.values, 0, 3 );
                this.values[ 3 ] = -1f;
            }
            
            this.isDirty = new boolean[] { false };
        }
        else
        {
            this.values = values;
            this.isDirty = isDirty;
        }
        this.hasAlpha = ( values.length > 3 ) && ( values[ 3 ] >= 0f );
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param readOnly
     * @param color the Colorf to copy the values from
     */
    protected Colorf( boolean readOnly, Colorf color )
    {
        this( readOnly, color.values, null, true );
        
        this.hasAlpha = color.hasAlpha;
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param readOnly
     */
    protected Colorf( boolean readOnly )
    {
        this( readOnly, 0f, 0f, 0f, -1f );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param readOnly
     * @param color the Color to copy the values from
     */
    protected Colorf( boolean readOnly, java.awt.Color color )
    {
        super();
        
        this.values = new float[] { 0f, 0f, 0f, 0f };
        
        this.values[ 0 ] = ((float)color.getRed()) / 255.0f;
        this.values[ 1 ] = ((float)color.getGreen()) / 255.0f;
        this.values[ 2 ] = ((float)color.getBlue()) / 255.0f;
        
        this.hasAlpha = ( color.getAlpha() < 255 );
        if ( hasAlpha )
            this.values[ 3 ] = ((float)(255 - color.getAlpha())) / 255.0f;
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[] { false };
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     * @param a the alpha channel to use
     */
    public Colorf( float r, float g, float b, float a )
    {
        this( false, r, g, b, a );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     */
    public Colorf( float r, float g, float b )
    {
        this( false, r, g, b );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param intensity the gray intensity (used for all three r,g,b values
     */
    public Colorf( float intensity )
    {
        this( false, intensity );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public Colorf( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param color the Colorf to copy the values from
     */
    public Colorf( Colorf color )
    {
        this( false, color );
    }
    
    /**
     * Creates a new Colorf instance.
     * 
     * @param rgbColor the Colorf to copy the RGB-values from
     * @param alpha the value for the alpha channel
     */
    public Colorf( Colorf rgbColor, float alpha )
    {
        this( false, rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), alpha );
    }
    
    /**
     * Creates a new Colorf instance.
     */
    public Colorf()
    {
        this( false );
    }
    
    public Colorf( java.awt.Color color )
    {
        this( false, color );
    }
    
    /**
     * Creates a new read-only Colorf instance.
     * 
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     * @param a the alpha channel to use
     */
    public static Colorf newReadOnly( float r, float g, float b, float a )
    {
        return ( new Colorf( true, r, g, b, a ) );
    }
    
    /**
     * Creates a new read-only Colorf instance.
     * 
     * @param r the red element to use
     * @param g the green element to use
     * @param b the blue element to use
     */
    public static Colorf newReadOnly( float r, float g, float b )
    {
        return ( new Colorf( true, r, g, b ) );
    }
    
    /**
     * Creates a new read-only Colorf instance.
     * 
     * @param intensity the gray intensity (used for all three r,g,b values
     */
    public static Colorf newReadOnly( float intensity )
    {
        return ( new Colorf( true, intensity ) );
    }
    
    /**
     * Creates a new read-only Colorf instance.
     * 
     * @param values the values array (must be at least size 3)
     */
    public static Colorf newReadOnly( float[] values )
    {
        return ( new Colorf( true, values, null, true ) );
    }
    
    /**
     * Creates a new read-only Colorf instance.
     * 
     * @param color the Colorf to copy the values from
     */
    public static Colorf newReadOnly( Colorf color )
    {
        return ( new Colorf( true, color ) );
    }
    
    /**
     * Creates a new read-only Colorf instance.
     * 
     * @param rgbColor the Colorf to copy the RGB-values from
     * @param alpha the value for the alpha channel
     */
    public static Colorf newReadOnly( Colorf rgbColor, float alpha )
    {
        return ( new Colorf( true, rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), alpha ) );
    }
    
    /**
     * Creates a new read-only Colorf instance.
     */
    public static Colorf newReadOnly()
    {
        return ( new Colorf( true ) );
    }
    
    public static Colorf newReadOnly( java.awt.Color color )
    {
        return ( new Colorf( true, color ) );
    }
    
    /**
     * Parses a color-hex-String into the color buffer.<br>
     * The color must be of the form "#XXXXXX" or "XXXXXX", where
     * the Xes must be hex chars.
     * 
     * @param hexString
     * @param buffer
     * 
     * @return the color back again
     */
    public static Colorf parseColor( String hexString, Colorf buffer )
    {
        ColorUtils.hexToColor( hexString, buffer );
        
        return ( buffer );
    }
    
    /**
     * Parses a color-hex-String into a Colorf instance.<br>
     * The color must be of the form "#RRGGBB" or "RRGGBB" or "#RRGGBBAA" or "RRGGBBAA", where
     * the letters must be hex chars.
     * 
     * @param hexString
     * 
     * @return the new Colorf instance
     */
    public static Colorf parseColor( String hexString )
    {
        return ( parseColor( hexString, new Colorf() ) );
    }
    
    /**
     * Parses a color-hex-String into a new Colorf instance.<br>
     * The color must be of the form "#RRGGBB" or "RRGGBB" or "#RRGGBBAA" or "RRGGBBAA", where
     * the letters must be hex chars.
     * 
     * @param hexString
     * 
     * @return the new Colorf instance
     */
    public static Colorf parseReadOnlyColor( String hexString )
    {
        float[] values = new float[ 4 ];
        
        ColorUtils.hexToColor( hexString, values );
        
        return ( new Colorf( true, values, null, true ) );
    }
    
    /**
     * Allocates an Colorf instance from the pool.
     */
    public static Colorf fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Colorf instance from the pool.
     */
    public static Colorf fromPool( float r, float g, float b, float a )
    {
        return ( POOL.get().alloc( r, g, b, a ) );
    }
    
    /**
     * Allocates an Colorf instance from the pool.
     */
    public static Colorf fromPool( float r, float g, float b )
    {
        return ( POOL.get().alloc( r, g, b ) );
    }
    
    /**
     * Allocates an Colorf instance from the pool.
     */
    public static Colorf fromPool( Colorf color )
    {
        if (color.hasAlpha())
            return ( fromPool( color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() ) );
        
        return ( fromPool( color.getRed(), color.getGreen(), color.getBlue() ) );
    }
    
    /**
     * Stores the given Colorf instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Colorf o )
    {
        POOL.get().free( o );
    }
}
