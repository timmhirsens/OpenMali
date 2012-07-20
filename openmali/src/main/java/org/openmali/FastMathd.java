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
public class FastMathd
{
    public static final double PI = Math.PI;
    public static final double PI_HALF = PI / 2.0;
    public static final double TWO_PI = 2.0 * PI;
    public static final double INV_PI = 1.0 / PI;
    public static final double INV_TWO_PI = 1.0 / TWO_PI;
    public static final double E = Math.E;
    
    public static final double DEG_TO_RAD = PI / 180.0;
    public static final double RAD_TO_DEG = 180.0 / PI;
    
    private static final Random random = new Random( System.nanoTime() );
    
    public static final double LOG10_TO_LOG2d = 3.3219280948873626;
    public static final double LOG10_TO_LOG2 = LOG10_TO_LOG2d;
    
    private static int precision = 0x100000;
    private static double[] sinTable = null,
    // asinTable = null,
                    // sinhTable = null,
                    cosTable = null,
                    // acosTable = null,
                    // coshTable = null,
                    tanTable = null;
    
    // atanTable = null,
    // atan2Table = null,
    // tanhTable = null;
    
    public static final void setPrecision( int precision )
    {
        if ( FastMathd.precision != precision )
        {
            sinTable = null;
            // asinTable = null;
            // sinhTable = null;
            cosTable = null;
            // acosTable = null;
            // coshTable = null;
            tanTable = null;
            // atanTable = null;
            // atan2Table = null;
            // tanhTable = null;
        }
        
        FastMathd.precision = precision;
    }
    
    public static final int getPrecision()
    {
        return ( precision );
    }
    
    private static final int radToIndex( double radians )
    {
        return ( (int)( ( radians / TWO_PI ) * precision ) & ( precision - 1 ) );
    }
    
    public static final double sin( double x )
    {
        if ( sinTable == null )
        {
            sinTable = new double[ precision ];
            
            final double rad_slice = TWO_PI / precision;
            
            for ( int i = 0; i < precision; i++ )
            {
                sinTable[ i ] = java.lang.Math.sin( i * rad_slice );
            }
        }
        
        return ( sinTable[ radToIndex( x ) ] );
    }
    
    public static final double asin( double x )
    {
        return ( Math.asin( x ) );
    }
    
    public static final double sinh( double x )
    {
        return ( Math.sinh( x ) );
    }
    
    public static final double cos( double x )
    {
        if ( cosTable == null )
        {
            cosTable = new double[ precision ];
            
            final double rad_slice = TWO_PI / precision;
            
            for ( int i = 0; i < precision; i++ )
            {
                cosTable[ i ] = java.lang.Math.cos( i * rad_slice );
            }
        }
        
        return ( cosTable[ radToIndex( x ) ] );
    }
    
    public static final double acos( double x )
    {
        return ( Math.acos( x ) );
    }
    
    public static final double cosh( double x )
    {
        return ( Math.cosh( x ) );
    }
    
    public static final double tan( double x )
    {
        if ( tanTable == null )
        {
            tanTable = new double[ precision ];
            
            final double rad_slice = TWO_PI / precision;
            
            for ( int i = 0; i < precision; i++ )
            {
                tanTable[ i ] = java.lang.Math.tan( i * rad_slice );
            }
        }
        
        return ( tanTable[ radToIndex( x ) ] );
    }
    
    public static final double atan( double x )
    {
        return ( Math.atan( x ) );
    }
    
    public static final double atan2( double y, double x )
    {
        return ( Math.atan2( y, x ) );
    }
    
    public static final double tanh( double x )
    {
        return ( Math.tanh( x ) );
    }
    
    public static final double cbrt( double x )
    {
        return ( Math.cbrt( x ) );
    }
    
    public static final double sqrt( double x )
    {
        return ( Math.sqrt( x ) );
    }
    
    public static final double invSqrt( double x )
    {
        return ( 1.0 / Math.sqrt( x ) );
    }
    
    public static final double ceil( double x )
    {
        return ( Math.ceil( x ) );
    }
    
    public static final double floor( double x )
    {
        return ( Math.floor( x ) );
    }
    
    /**
     * @see Math#log
     * 
     * @param x
     * 
     * @return the natural logarithm (base e)
     */
    public static final double log( double x )
    {
        return ( Math.log( x ) );
    }
    
    public static final double log10( double x )
    {
        return ( Math.log10( x ) );
    }
    
    public static final double log1p( double x )
    {
        return ( Math.log1p( x ) );
    }
    
    /**
     * @param x
     * 
     * @return the logarithm to base 2
     */
    public static final double log2( double x )
    {
        return ( ( Math.log( x ) * LOG10_TO_LOG2d ) );
    }
    
    public static final double toDeg( double x )
    {
        return ( Math.toDegrees( x ) );
    }
    
    public static final double toRad( double x )
    {
        return ( Math.toRadians( x ) );
    }
    
    public static final double pow( double base, double exp )
    {
        return ( Math.pow( base, exp ) );
    }
    
    public static final double pow2( double base )
    {
        return ( pow( base, 2d ) );
    }
    
    public static final double pow3( double base )
    {
        return ( pow( base, 3d ) );
    }
    
    public static final boolean epsilonEquals( double v1, double v2, double epsilon )
    {
        return ( Math.abs( v1 - v2 ) < epsilon );
    }
    
    /**
     * sqrt(a^2 + b^2) without under/overflow.
     */
    public static final double hypot( double a, double b )
    {
        if ( Math.abs( a ) > Math.abs( b ) )
        {
            double r = b / a;
            r = Math.abs( a ) * FastMathd.sqrt( 1d + r * r );
            
            return ( r );
        }
        else if ( b != 0d )
        {
            double r = a / b;
            r = Math.abs( b ) * FastMathd.sqrt( 1d + r * r );
            
            return ( r );
        }
        else
        {
            return ( 0d );
        }
    }
    
    /**
     * Helper class to return result tuple of frexp()
     */
    public static class FRExpResultd
    {
        /**
        * normalised mantissa
        */
        public double mantissa;
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
    public static final FRExpResultd frexp( double value, FRExpResultd result )
    {
        int i = 0;
        if ( value != 0.0 )
        {
            int sign = 1;
            if ( value < 0 )
            {
                sign = -1;
                value = -value;
            }
            // slow...
            while ( value < 0.5 )
            {
                value = value * 2.0;
                i = i - 1;
            }
            while ( value >= 1.0 )
            {
                value = value * 0.5;
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
    public static final FRExpResultd frexp( double value )
    {
        return ( frexp( value, new FRExpResultd() ) );
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
    public static final double randomDouble()
    {
        return ( random.nextDouble() );
    }
    
    /**
     * Generates the next random double (minimum: 0 inclusive).
     * 
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final double randomDouble( float max )
    {
        return ( random.nextDouble() * max );
    }
    
    /**
     * Generates the next random double.
     * 
     * @param min the minimum number (inclusive)
     * @param max the maximum number (exclusive)
     * 
     * @return the generated number.
     */
    public static final double randomDouble( float min, float max )
    {
        return ( min + ( random.nextDouble() * ( max - min ) ) );
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
