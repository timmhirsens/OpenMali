/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
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
package org.openmali.types.primitives;

import sun.misc.FloatingDecimal;

/**
 * This a mutable derivation of the {@link Float} class.
 * Most of the code is borrowed from the {@link Float} class.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class MutableFloat extends Number implements Comparable< MutableFloat >
{
    private static final long serialVersionUID = -1901001518826913935L;
    
    /**
     * The value of the MutableFloat.
     * 
     * @serial
     */
    private float value;
    
    public final void setValue( float value )
    {
        this.value = value;
    }
    
    /**
     * Returns <code>true</code> if this <code>MutableFloat</code> value is a
     * Not-a-Number (NaN), <code>false</code> otherwise.
     * 
     * @return  <code>true</code> if the value represented by this object is
     *          NaN; <code>false</code> otherwise.
     */
    public final boolean isNaN()
    {
        return ( Float.isNaN( value ) );
    }
    
    /**
     * Returns <code>true</code> if this <code>MutableFloat</code> value is
     * infinitely large in magnitude, <code>false</code> otherwise.
     * 
     * @return  <code>true</code> if the value represented by this object is
     *          positive infinity or negative infinity;
     *          <code>false</code> otherwise.
     */
    public final boolean isInfinite()
    {
        return ( Float.isInfinite( value ) );
    }
    
    /**
     * Returns a string representation of this <code>MutableFloat</code> object.
     * The primitive <code>float</code> value represented by this object
     * is converted to a <code>String</code> exactly as if by the method
     * <code>toString</code> of one argument.
     * 
     * @return  a <code>String</code> representation of this object.
     * @see java.lang.Float#toString(float)
     */
    @Override
    public String toString()
    {
        return ( String.valueOf( value ) );
    }
    
    /**
     * Returns the value of this <code>MutableFloat</code> as a
     * <code>byte</code> (by casting to a <code>byte</code>).
     * 
     * @return  the <code>float</code> value represented by this object
     *          converted to type <code>byte</code>
     */
    @Override
    public final byte byteValue()
    {
        return ( (byte)value );
    }
    
    /**
     * Returns the value of this <code>MutableFloat</code> as a
     * <code>short</code> (by casting to a <code>short</code>).
     * 
     * @return  the <code>float</code> value represented by this object
     *          converted to type <code>short</code>
     * @since JDK1.1
     */
    @Override
    public final short shortValue()
    {
        return ( (short)value );
    }
    
    /**
     * Returns the value of this <code>MutableFloat</code> as an
     * <code>int</code> (by casting to type <code>int</code>).
     * 
     * @return  the <code>float</code> value represented by this object
     *          converted to type <code>int</code>
     */
    @Override
    public final int intValue()
    {
        return ( (int)value );
    }
    
    /**
     * Returns value of this <code>MutableFloat</code> as a <code>long</code>
     * (by casting to type <code>long</code>).
     * 
     * @return  the <code>float</code> value represented by this object
     *          converted to type <code>long</code>
     */
    @Override
    public final long longValue()
    {
        return ( (long)value );
    }
    
    /**
     * Returns the <code>float</code> value of this <code>MutableFloat</code>
     * object.
     * 
     * @return the <code>float</code> value represented by this object 
     */
    @Override
    public final float floatValue()
    {
        return ( value );
    }
    
    /**
     * Returns the <code>double</code> value of this
     * <code>MutableFloat</code> object.
     * 
     * @return the <code>float</code> value represented by this 
     *         object is converted to type <code>double</code> and the 
     *         result of the conversion is returned.  
     */
    @Override
    public double doubleValue()
    {
        return ( (double)value );
    }
    
    /**
     * Returns a hash code for this <code>MutableFloat</code> object. The
     * result is the integer bit representation, exactly as produced
     * by the method {@link Float#floatToIntBits(float)}, of the primitive
     * <code>float</code> value represented by this <code>MutableFloat</code>
     * object.
     * 
     * @return a hash code value for this object.  
     */
    @Override
    public int hashCode()
    {
        return ( Float.floatToIntBits( value ) );
    }
    
    /**
     * Compares this object against the specified object.  The result
     * is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>Float</code> object that
     * represents a <code>float</code> with the same value as the
     * <code>float</code> represented by this object. For this
     * purpose, two <code>float</code> values are considered to be the
     * same if and only if the method {@link Float#floatToIntBits(float)}
     * returns the identical <code>int</code> value when applied to
     * each.
     * <p>
     * Note that in most cases, for two instances of class
     * <code>Float</code>, <code>f1</code> and <code>f2</code>, the value
     * of <code>f1.equals(f2)</code> is <code>true</code> if and only if
     * <blockquote><pre>
     *   f1.floatValue() == f2.floatValue()
     * </pre></blockquote>
     * <p>
     * also has the value <code>true</code>. However, there are two exceptions:
     * <ul>
     * <li>If <code>f1</code> and <code>f2</code> both represent
     *     <code>Float.NaN</code>, then the <code>equals</code> method returns
     *     <code>true</code>, even though <code>Float.NaN==Float.NaN</code>
     *     has the value <code>false</code>.
     * <li>If <code>f1</code> represents <code>+0.0f</code> while
     *     <code>f2</code> represents <code>-0.0f</code>, or vice
     *     versa, the <code>equal</code> test has the value
     *     <code>false</code>, even though <code>0.0f==-0.0f</code>
     *     has the value <code>true</code>.
     * </ul>
     * This definition allows hash tables to operate properly.
     * 
     * @param obj the object to be compared
     * @return  <code>true</code> if the objects are the same;
     *          <code>false</code> otherwise.
     * @see java.lang.Float#floatToIntBits(float)
     */
    @Override
    public boolean equals( Object obj )
    {
        if ( ( obj instanceof MutableFloat ) && ( Float.floatToIntBits( ( (MutableFloat)obj ).value ) == Float.floatToIntBits( value ) ) )
            return ( true );
        
        if ( ( obj instanceof Float ) && ( Float.floatToIntBits( ( (Float)obj ).floatValue() ) == Float.floatToIntBits( value ) ) )
            return ( true );
        
        return ( false );
    }
    
    /**
     * Compares two <code>MutableFloat</code> objects numerically.  There are
     * two ways in which comparisons performed by this method differ
     * from those performed by the Java language numerical comparison
     * operators (<code>&lt;, &lt;=, ==, &gt;= &gt;</code>) when
     * applied to primitive <code>float</code> values:
     * <ul><li>
     *      <code>Float.NaN</code> is considered by this method to
     *      be equal to itself and greater than all other
     *      <code>float</code> values
     *      (including <code>Float.POSITIVE_INFINITY</code>).
     * <li>
     *      <code>0.0f</code> is considered by this method to be greater
     *      than <code>-0.0f</code>.
     * </ul>
     * This ensures that the <i>natural ordering</i> of <tt>Float</tt>
     * objects imposed by this method is <i>consistent with equals</i>.
     * 
     * @param   anotherFloat   the <code>Float</code> to be compared.
     * @return  the value <code>0</code> if <code>anotherFloat</code> is
     *      numerically equal to this <code>MutableFloat</code>; a value
     *      less than <code>0</code> if this <code>MutableFloat</code>
     *      is numerically less than <code>anotherFloat</code>;
     *      and a value greater than <code>0</code> if this
     *      <code>MutableFloat</code> is numerically greater than
     *      <code>anotherFloat</code>.
     *      
     * @since   1.2
     * @see Comparable#compareTo(Object)
     */
    public int compareTo( MutableFloat anotherFloat )
    {
        return ( Float.compare( value, anotherFloat.value ) );
    }
    
    /**
     * Constructs a newly allocated <code>MutableFloat</code> object that
     * represents the primitive <code>float</code> argument.
     * 
     * @param   value   the value to be represented by the <code>MutableFloat</code>.
     */
    public MutableFloat( float value )
    {
        this.value = value;
    }
    
    /**
     * Constructs a newly allocated <code>MutableFloat</code> object that
     * represents the argument converted to type <code>float</code>.
     * 
     * @param   value   the value to be represented by the <code>MutableFloat</code>.
     */
    public MutableFloat( double value )
    {
        this.value = (float)value;
    }
    
    /**
     * Constructs a newly allocated <code>MutableFloat</code> object that 
     * represents the floating-point value of type <code>float</code> 
     * represented by the string. The string is converted to a 
     * <code>float</code> value as if by the <code>valueOf</code> method. 
     * 
     * @param      s   a string to be converted to a <code>MutableFloat</code>.
     * @exception  NumberFormatException  if the string does not contain a
     *               parsable number.
     * @see        java.lang.Float#valueOf(java.lang.String)
     */
    public MutableFloat( String s ) throws NumberFormatException
    {
        // REMIND: this is inefficient
        this( valueOf( s ).floatValue() );
    }
    
    /**
     * Returns a <code>MutableFloat</code> object holding the
     * <code>float</code> value represented by the argument string
     * <code>s</code>.
     * 
     * <p>If <code>s</code> is <code>null</code>, then a
     * <code>NullPointerException</code> is thrown.
     * 
     * <p>Leading and trailing whitespace characters in <code>s</code>
     * are ignored.  Whitespace is removed as if by the {@link
     * String#trim} method; that is, both ASCII space and control
     * characters are removed. The rest of <code>s</code> should
     * constitute a <i>FloatValue</i> as described by the lexical
     * syntax rules:
     * 
     * <blockquote>
     * <dl>
     * <dt><i>FloatValue:</i>
     * <dd><i>Sign<sub>opt</sub></i> <code>NaN</code>
     * <dd><i>Sign<sub>opt</sub></i> <code>Infinity</code>
     * <dd><i>Sign<sub>opt</sub> FloatingPointLiteral</i>
     * <dd><i>Sign<sub>opt</sub> HexFloatingPointLiteral</i>
     * <dd><i>SignedInteger</i>
     * </dl>
     * 
     * <p>
     * 
     * <dl>
     * <dt><i>HexFloatingPointLiteral</i>:
     * <dd> <i>HexSignificand BinaryExponent FloatTypeSuffix<sub>opt</sub></i>
     * </dl>
     * 
     * <p>
     * 
     * <dl>
     * <dt><i>HexSignificand:</i>
     * <dd><i>HexNumeral</i>
     * <dd><i>HexNumeral</i> <code>.</code>
     * <dd><code>0x</code> <i>HexDigits<sub>opt</sub> 
     *     </i><code>.</code><i> HexDigits</i>
     * <dd><code>0X</code><i> HexDigits<sub>opt</sub> 
     *     </i><code>.</code> <i>HexDigits</i>
     * </dl>
     * 
     * <p>
     * 
     * <dl>
     * <dt><i>BinaryExponent:</i>
     * <dd><i>BinaryExponentIndicator SignedInteger</i>
     * </dl>
     * 
     * <p>
     * 
     * <dl>
     * <dt><i>BinaryExponentIndicator:</i>
     * <dd><code>p</code>
     * <dd><code>P</code>
     * </dl>
     * 
     * </blockquote>
     * 
     * where <i>Sign</i>, <i>FloatingPointLiteral</i>,
     * <i>HexNumeral</i>, <i>HexDigits</i>, <i>SignedInteger</i> and
     * <i>FloatTypeSuffix</i> are as defined in the lexical structure
     * sections of the of the <a
     * href="http://java.sun.com/docs/books/jls/html/">Java Language
     * Specification</a>. If <code>s</code> does not have the form of
     * a <i>FloatValue</i>, then a <code>NumberFormatException</code>
     * is thrown. Otherwise, <code>s</code> is regarded as
     * representing an exact decimal value in the usual
     * &quot;computerized scientific notation&quot; or as an exact
     * hexadecimal value; this exact numerical value is then
     * conceptually converted to an &quot;infinitely precise&quot;
     * binary value that is then rounded to type <code>float</code>
     * by the usual round-to-nearest rule of IEEE 754 floating-point
     * arithmetic, which includes preserving the sign of a zero
     * value. Finally, a <code>MutableFloat</code> object representing this
     * <code>float</code> value is returned.
     * 
     * <p>To interpret localized string representations of a
     * floating-point value, use subclasses of java.text.NumberFormat.
     * 
     * <p>Note that trailing format specifiers, specifiers that
     * determine the type of a floating-point literal
     * (<code>1.0f</code> is a <code>float</code> value;
     * <code>1.0d</code> is a <code>double</code> value), do
     * <em>not</em> influence the results of this method.  In other
     * words, the numerical value of the input string is converted
     * directly to the target floating-point type.  In general, the
     * two-step sequence of conversions, string to <code>double</code>
     * followed by <code>double</code> to <code>float</code>, is
     * <em>not</em> equivalent to converting a string directly to
     * <code>float</code>.  For example, if first converted to an
     * intermediate <code>double</code> and then to
     * <code>float</code>, the string<br>
     * <code>"1.00000017881393421514957253748434595763683319091796875001d"</code><br>
     * results in the <code>float</code> value
     * <code>1.0000002f</code>; if the string is converted directly to
     * <code>float</code>, <code>1.000000<b>1</b>f</code> results.
     * 
     * <p>To avoid calling this method on a invalid string and having
     * a <code>NumberFormatException</code> be thrown, the documentation
     * for {@link Double#valueOf(String)} lists a regular
     * expression which can be used to screen the input.
     * 
     * @param      s   the string to be parsed.
     * @return     a <code>MutableFloat</code> object holding the value
     *             represented by the <code>String</code> argument.
     * @exception  NumberFormatException  if the string does not contain a
     *               parsable number.  
     */
    public static MutableFloat valueOf( String s ) throws NumberFormatException
    {
        return ( new MutableFloat( FloatingDecimal.readJavaFormatString( s ).floatValue() ) );
    }
    
    /**
     * Returns a <tt>MutableFloat</tt> instance representing the specified
     * <tt>float</tt> value.
     * If a new <tt>MutableFloat</tt> instance is not required, this method
     * should generally be used in preference to the constructor
     * {@link #MutableFloat(float)}, as this method is likely to yield
     * significantly better space and time performance by caching
     * frequently requested values.
     * 
     * @param  f a float value.
     * @return a <tt>MutableFloat</tt> instance representing <tt>f</tt>.
     * @since  1.5
     */
    public static MutableFloat valueOf( float f )
    {
        return ( new MutableFloat( f ) );
    }
}
