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
package org.openmali;

import java.util.Random;

/**
 * The FastMath class provides fast and optimized mathematical functions.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class FastMath
{
    public static final float PI = (float)Math.PI;
    public static final float PI_HALF = PI / 2.0f;
    public static final float TWO_PI = 2.0f * PI;
    public static final float INV_PI = 1.0f / PI;
    public static final float INV_TWO_PI = 1.0f / TWO_PI;
    public static final float E = (float)Math.E;
    
    public static final float DEG_TO_RAD = PI / 180.0f;
    public static final float RAD_TO_DEG = 180.0f / PI;
    
    private static final Random random = new Random( System.nanoTime() );
    
    public static final double LOG10_TO_LOG2d = 3.3219280948873626;
    public static final float LOG10_TO_LOG2 = (float)LOG10_TO_LOG2d;
    
    protected static int precision = 0x100000;
    private static float[] sinTable = null,
                           //asinTable = null,
                           //sinhTable = null,
                           cosTable = null,
                           //acosTable = null,
                           //coshTable = null,
                           tanTable = null;
                           //atanTable = null,
                           //atan2Table = null,
                           //tanhTable = null;
    
    public static final void setPrecision( int precision )
    {
        if ( FastMath.precision != precision )
        {
            sinTable = null;
            //asinTable = null;
            //sinhTable = null;
            cosTable = null;
            //acosTable = null;
            //coshTable = null;
            tanTable = null;
            //atanTable = null;
            //atan2Table = null;
            //tanhTable = null;
        }
        
        FastMath.precision = precision;
    }
    
    public static final int getPrecision()
    {
        return ( precision );
    }
    
    private static final int radToIndex( float radians )
    {
        return ( (int)( ( radians / TWO_PI ) * (float)precision ) & ( precision - 1 ) );
    }
    
    public static final float sin( float x )
    {
        if ( sinTable == null )
        {
            sinTable = new float[ precision ];
            
            final float rad_slice = TWO_PI / (float)precision;

            for ( int i = 0; i < precision; i++ )
            {
                sinTable[ i ] = (float)java.lang.Math.sin( (float)i * rad_slice );
            }
        }
        
        return ( sinTable[ radToIndex( x ) ] );
    }
    
    public static final float asin( float x )
    {
        return ( (float)Math.asin( x ) );
    }
    
    public static final float sinh( float x )
    {
        return ( (float)Math.sinh( x ) );
    }
    
    public static final float cos( float x )
    {
        if ( cosTable == null )
        {
            cosTable = new float[ precision ];
            
            final float rad_slice = TWO_PI / (float)precision;

            for ( int i = 0; i < precision; i++ )
            {
                cosTable[ i ] = (float)java.lang.Math.cos( (float)i * rad_slice );
            }
        }
        
        return ( cosTable[ radToIndex( x ) ] );
    }
    
    public static final float acos( float x )
    {
        return ( (float)Math.acos( x ) );
    }
    
    public static final float cosh( float x )
    {
        return ( (float)Math.cosh( x ) );
    }
    
    public static final float tan( float x )
    {
        if ( tanTable == null )
        {
            tanTable = new float[ precision ];
            
            final float rad_slice = TWO_PI / (float)precision;

            for ( int i = 0; i < precision; i++ )
            {
                tanTable[ i ] = (float)java.lang.Math.tan( (float)i * rad_slice );
            }
        }
        
        return ( tanTable[ radToIndex( x ) ] );
    }
    
    public static final float atan( float x )
    {
        return ( (float)Math.atan( x ) );
    }
    
    public static final float atan2( float y, float x )
    {
        return ( (float)Math.atan2( y, x ) );
    }
    
    public static final float tanh( float x )
    {
        return ( (float)Math.tanh( x ) );
    }
    
    public static final float cbrt( float x )
    {
        return ( (float)Math.cbrt( x ) );
    }
    
    public static final float sqrt( float x )
    {
        return ( (float)Math.sqrt( x ) );
    }
    
    public static final float invSqrt( float x )
    {
        return ( 1.0f / (float)Math.sqrt( x ) );
    }
    
    public static final float ceil( float x )
    {
        return ( (float)Math.ceil( x ) );
    }
    
    public static final float floor( float x )
    {
        return ( (float)Math.floor( x ) );
    }
    
    /**
     * @see Math#log(double)
     * 
     * @param x
     * 
     * @return the natural logarithm (base e)
     */
    public static final float log( float x )
    {
        return ( (float)Math.log( x ) );
    }
    
    public static final float log10( float x )
    {
        return ( (float)Math.log10( x ) );
    }
    
    public static final float log1p( float x )
    {
        return ( (float)Math.log1p( x ) );
    }
    
    /**
     * @param x
     * 
     * @return the logarithm to base 2
     */
    public static final float log2( float x )
    {
        return ( (float)( Math.log( x ) * LOG10_TO_LOG2d ) );
    }
    
    public static final float toDeg( float x )
    {
        return ( (float)Math.toDegrees( x ) );
    }
    
    public static final float toRad( float x )
    {
        return ( (float)Math.toRadians( x ) );
    }
    
    public static final float pow( float base, float exp )
    {
        return ( (float)Math.pow( base, exp ) );
    }
    
    public static final float pow2( float base )
    {
        return ( pow( base, 2f ) );
    }
    
    public static final float pow3( float base )
    {
        return ( pow( base, 3f ) );
    }
    
    public static final int pow( int base, int exp )
    {
        return ( (int)Math.pow( base, exp ) );
    }
    
    public static final boolean epsilonEquals( float v1, float v2, float epsilon )
    {
    	return ( Math.abs( v1 - v2 ) < epsilon );
    }
    
    /**
     * sqrt(a^2 + b^2) without under/overflow.
     */
    public static final float hypot( float a, float b )
    {
        if ( Math.abs( a ) > Math.abs( b ) )
        {
            float r = b / a;
            r = Math.abs( a ) * FastMath.sqrt( 1f + r * r );
            
            return ( r );
        }
        else if ( b != 0f )
        {
            float r = a / b;
            r = Math.abs( b ) * FastMath.sqrt( 1f + r * r );
            
            return ( r );
        }
        else
        {
            return ( 0f );
        }
    }
    
    /**
     * Helper class to return result tuple of frexp()
     */
    public static class FRExpResultf
    {
        /**
        * normalised mantissa
        */
        public float mantissa;
        /**
        * exponent of floating point representation
        */
        public int exponent;
    }
    
    /**
     * An implementation of the C standard library frexp() function.
     * 
     * @param value the number to split
     * @param result
     * 
     * @return a touple of normalised mantissa and exponent
     */
    public static final FRExpResultf frexp( float value, FRExpResultf result )
    {
        int i = 0;
        if ( value != 0.0f )
        {
            int sign = 1;
            if ( value < 0f )
            {
                sign = -1;
                value = -value;
            }
            // slow...
            while ( value < 0.5f )
            {
                value = value * 2.0f;
                i = i - 1;
            }
            while ( value >= 1.0f )
            {
                value = value * 0.5f;
                i = i + 1;
            }
            value = value * sign;
        }
        
        result.mantissa = value;
        result.exponent = i;
        
        return ( result );
    }
    
    /**
     * An implementation of the C standard library frexp() function.
     * 
     * @param value the number to split
     * @return a touple of normalised mantissa and exponent
     */
    public static final FRExpResultf frexp( float value )
    {
        return ( frexp( value, new FRExpResultf() ) );
    }
    
    /**
     * Gets the Random generator.
     * 
     * @return the Random generator.
     */
    public static final Random getRandom()
    {
        return ( random );
    }
    
    /**
     * Returns the next pseudorandom, uniformly distributed <code>float</code>
     * value between <code>0.0</code> and <code>1.0</code> from this random
     * number generator's sequence. <p>
     * The general contract of <tt>nextFloat</tt> is that one <tt>float</tt> 
     * value, chosen (approximately) uniformly from the range <tt>0.0f</tt> 
     * (inclusive) to <tt>1.0f</tt> (exclusive), is pseudorandomly
     * generated and returned. All 2<font size="-1"><sup>24</sup></font> 
     * possible <tt>float</tt> values of the form 
     * <i>m&nbsp;x&nbsp</i>2<font size="-1"><sup>-24</sup></font>, where 
     * <i>m</i> is a positive integer less than 2<font size="-1"><sup>24</sup>
     * </font>, are produced with (approximately) equal probability. The 
     * method <tt>nextFloat</tt> is implemented by class <tt>Random</tt> as 
     * follows:
     * <blockquote><pre>
     * public float nextFloat() {
     *      return next(24) / ((float)(1 << 24));
     * }</pre></blockquote>
     * The hedge "approximately" is used in the foregoing description only 
     * because the next method is only approximately an unbiased source of 
     * independently chosen bits. If it were a perfect source or randomly 
     * chosen bits, then the algorithm shown would choose <tt>float</tt> 
     * values from the stated range with perfect uniformity.<p>
     * [In early versions of Java, the result was incorrectly calculated as:
     * <blockquote><pre>
     * return next(30) / ((float)(1 << 30));</pre></blockquote>
     * This might seem to be equivalent, if not better, but in fact it 
     * introduced a slight nonuniformity because of the bias in the rounding 
     * of floating-point numbers: it was slightly more likely that the 
     * low-order bit of the significand would be 0 than that it would be 1.] 
     *
     * @return  the next pseudorandom, uniformly distributed <code>float</code>
     *          value between <code>0.0</code> and <code>1.0</code> from this
     *          random number generator's sequence.
     */
    public static final float randomFloat()
    {
        return ( random.nextFloat() );
    }
    
    /**
     * Generates the next random float (minimum: 0 inclusive).
     * 
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final float randomFloat( float max )
    {
        return ( random.nextFloat() * max );
    }
    
    /**
     * Generates the next random float.
     * 
     * @param min the minimum number (inclusive)
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final float randomFloat( float min, float max )
    {
        return ( min + ( random.nextFloat() * ( max - min ) ) );
    }
    
    /**
     * Generates the next random int (minimum: 0 inclusive, maximum: Integer.MAX_VALUE exclusive).
     * 
     * @return the generated number.
     */
    public static final int randomInt()
    {
        return ( random.nextInt() );
    }
    
    /**
     * Generates the next random int (minimum: 0 inclusive).
     * 
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final int randomInt( int max )
    {
        return ( random.nextInt( max ) );
    }
    
    /**
     * Generates the next random int.
     * 
     * @param min the minimum number (inclusive)
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final int randomInt( int min, int max )
    {
        return ( min + ( random.nextInt( max - min ) ) );
    }
    
    /**
     * Generates the next random long (minimum: 0 inclusive, maximum: Long.MAX_VALUE exclusive).
     * 
     * @return the generated number.
     */
    public static final long randomLong()
    {
        return ( random.nextLong() );
    }
    
    /**
     * Generates the next random long (minimum: 0 inclusive).
     * 
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final long randomLong( long max )
    {
        return ( random.nextLong() * ( max / Long.MAX_VALUE ) );
    }
    
    /**
     * Generates the next random long.
     * 
     * @param min the minimum number (inclusive)
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final long randomLong( long min, long max )
    {
        return ( min + randomLong( max - min ) );
    }
}
