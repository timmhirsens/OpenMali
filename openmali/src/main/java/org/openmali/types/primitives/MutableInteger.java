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

/**
 * This a mutable derivation of the {@link Integer} class.
 * Most of the code is borrowed from the {@link Integer} class.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class MutableInteger extends Number implements Comparable<MutableInteger> {
	private static final long serialVersionUID = -2756653905745194271L;

	/**
	 * The value of the <code>MutableInteger</code>.
	 * 
	 * @serial
	 */
	private int value;

	public final void setValue(int value) {
		this.value = value;
	}

	/**
	 * Returns the value of this <code>MutableInteger</code> as a
	 * <code>byte</code>.
	 */
	@Override
	public final byte byteValue() {
		return ((byte) value);
	}

	/**
	 * Returns the value of this <code>MutableInteger</code> as a
	 * <code>short</code>.
	 */
	@Override
	public final short shortValue() {
		return ((short) value);
	}

	/**
	 * Returns the value of this <code>MutableInteger</code> as an
	 * <code>int</code>.
	 */
	@Override
	public final int intValue() {
		return (value);
	}

	/**
	 * Returns the value of this <code>MutableInteger</code> as a
	 * <code>long</code>.
	 */
	@Override
	public final long longValue() {
		return ((long) value);
	}

	/**
	 * Returns the value of this <code>MutableInteger</code> as a
	 * <code>float</code>.
	 */
	@Override
	public final float floatValue() {
		return ((float) value);
	}

	/**
	 * Returns the value of this <code>MutableInteger</code> as a
	 * <code>double</code>.
	 */
	@Override
	public final double doubleValue() {
		return ((double) value);
	}

	/**
	 * Returns a <code>String</code> object representing this
	 * <code>MutableInteger</code>'s value. The value is converted to signed
	 * decimal representation and returned as a string, exactly as if
	 * the integer value were given as an argument to the {@link
	 * java.lang.Integer#toString(int)} method.
	 * 
	 * @return  a string representation of the value of this object in
	 *          base&nbsp;10.
	 */
	@Override
	public String toString() {
		return (String.valueOf(value));
	}

	/**
	 * Returns a hash code for this <code>MutableInteger</code>.
	 * 
	 * @return  a hash code value for this object, equal to the 
	 *          primitive <code>int</code> value represented by this 
	 *          <code>MutableInteger</code> object. 
	 */
	@Override
	public int hashCode() {
		return (value);
	}

	/**
	 * Compares this object to the specified object.  The result is
	 * <code>true</code> if and only if the argument is not
	 * <code>null</code> and is an <code>MutableInteger</code> object that
	 * contains the same <code>int</code> value as this object.
	 * 
	 * @param   obj   the object to compare with.
	 * @return  <code>true</code> if the objects are the same;
	 *          <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MutableInteger) {
			return (value == ((MutableInteger) obj).intValue());
		}

		if (obj instanceof Integer) {
			return (value == ((Integer) obj).intValue());
		}

		return (false);
	}

	/**
	 * Compares two <code>MutableInteger</code> objects numerically.
	 * 
	 * @param   anotherInteger   the <code>MutableInteger</code> to be compared.
	 * @return  the value <code>0</code> if this <code>MutableInteger</code> is
	 *      equal to the argument <code>MutableInteger</code>; a value less than
	 *      <code>0</code> if this <code>MutableInteger</code> is numerically less
	 *      than the argument <code>MutableInteger</code>; and a value greater 
	 *      than <code>0</code> if this <code>MutableInteger</code> is numerically
	 *       greater than the argument <code>MutableInteger</code> (signed
	 *       comparison).
	 * @since   1.2
	 */
	public int compareTo(MutableInteger anotherInteger) {
		final int thisVal = this.value;
		final int anotherVal = anotherInteger.value;

		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}

	/**
	 * Constructs a newly allocated <code>MutableInteger</code> object that
	 * represents the specified <code>int</code> value.
	 * 
	 * @param   value   the value to be represented by the 
	 *          <code>MutableInteger</code> object.
	 */
	public MutableInteger(int value) {
		this.value = value;
	}

	/**
	 * Constructs a newly allocated <code>MutableInteger</code> object that
	 * represents the <code>int</code> value indicated by the
	 * <code>String</code> parameter. The string is converted to an
	 * <code>int</code> value in exactly the manner used by the
	 * <code>parseInt</code> method for radix 10.
	 * 
	 * @param      s   the <code>String</code> to be converted to an
	 *                 <code>MutableInteger</code>.
	 * @exception  NumberFormatException  if the <code>String</code> does not
	 *               contain a parsable integer.
	 * @see        java.lang.Integer#parseInt(java.lang.String, int)
	 */
	public MutableInteger(String s) throws NumberFormatException {
		this.value = Integer.parseInt(s, 10);
	}

	/**
	 * Returns an <code>MutableInteger</code> object holding the value
	 * extracted from the specified <code>String</code> when parsed
	 * with the radix given by the second argument. The first argument
	 * is interpreted as representing a signed integer in the radix
	 * specified by the second argument, exactly as if the arguments
	 * were given to the {@link Integer#parseInt(java.lang.String, int)}
	 * method. The result is an <code>MutableInteger</code> object that
	 * represents the integer value specified by the string.
	 * <p>
	 * In other words, this method returns an <code>MutableInteger</code>
	 * object equal to the value of:
	 * 
	 * <blockquote><code>
	 * new MutableInteger(Integer.parseInt(s, radix))  
	 * </code></blockquote>
	 * 
	 * @param      s   the string to be parsed.
	 * @param      radix the radix to be used in interpreting <code>s</code>
	 * @return     an <code>MutableInteger</code> object holding the value
	 *             represented by the string argument in the specified
	 *             radix.
	 * @exception NumberFormatException if the <code>String</code>
	 *        does not contain a parsable <code>int</code>.
	 */
	public static MutableInteger valueOf(String s, int radix) throws NumberFormatException {
		return (new MutableInteger(Integer.parseInt(s, radix)));
	}

	/**
	 * Returns an <code>MutableInteger</code> object holding the
	 * value of the specified <code>String</code>. The argument is
	 * interpreted as representing a signed decimal integer, exactly
	 * as if the argument were given to the {@link
	 * Integer#parseInt(java.lang.String)} method. The result is an
	 * <code>MutableInteger</code> object that represents the integer value
	 * specified by the string.
	 * <p>
	 * In other words, this method returns an <code>MutableInteger</code>
	 * object equal to the value of:
	 * 
	 * <blockquote><code>
	 * new MutableInteger(Integer.parseInt(s))
	 * </code></blockquote>
	 * 
	 * @param      s   the string to be parsed.
	 * @return     an <code>MutableInteger</code> object holding the value
	 *             represented by the string argument.
	 * @exception  NumberFormatException  if the string cannot be parsed 
	 *             as an integer.
	 */
	public static MutableInteger valueOf(String s) throws NumberFormatException {
		return (new MutableInteger(Integer.parseInt(s, 10)));
	}

	/**
	 * Returns a <tt>MutableInteger</tt> instance representing the specified
	 * <tt>int</tt> value.
	 * If a new <tt>MutableInteger</tt> instance is not required, this method
	 * should generally be used in preference to the constructor
	 * {@link #MutableInteger(int)}, as this method is likely to yield
	 * significantly better space and time performance by caching
	 * frequently requested values.
	 * 
	 * @param  i an <code>int</code> value.
	 * @return a <tt>MutableInteger</tt> instance representing <tt>i</tt>.
	 * @since  1.5
	 */
	public static MutableInteger valueOf(int i) {
		return (new MutableInteger(i));
	}
}
