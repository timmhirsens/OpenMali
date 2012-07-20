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
 * This a mutable derivation of the {@link Byte} class.
 * Most of the code is borrowed from the {@link Byte} class.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class MutableByte extends Number implements Comparable<MutableByte> {
	private static final long serialVersionUID = -3873625958907769639L;

	/**
	 * The value of the <code>MutableByte</code>.
	 * 
	 * @serial
	 */
	private byte value;

	public final void setValue(byte value) {
		this.value = value;
	}

	/**
	 * Returns the value of this <code>MutableByte</code> as a
	 * <code>byte</code>.
	 */
	@Override
	public final byte byteValue() {
		return (value);
	}

	/**
	 * Returns the value of this <code>MutableByte</code> as a
	 * <code>short</code>.
	 */
	@Override
	public final short shortValue() {
		return ((short) value);
	}

	/**
	 * Returns the value of this <code>MutableByte</code> as an
	 * <code>int</code>.
	 */
	@Override
	public final int intValue() {
		return ((int) value);
	}

	/**
	 * Returns the value of this <code>MutableByte</code> as a
	 * <code>long</code>.
	 */
	@Override
	public final long longValue() {
		return ((long) value);
	}

	/**
	 * Returns the value of this <code>MutableByte</code> as a
	 * <code>float</code>.
	 */
	@Override
	public float floatValue() {
		return ((float) value);
	}

	/**
	 * Returns the value of this <code>MutableByte</code> as a
	 * <code>double</code>.
	 */
	@Override
	public double doubleValue() {
		return ((double) value);
	}

	/**
	 * Returns a <code>String</code> object representing this
	 * <code>MutableByte</code>'s value.  The value is converted to signed
	 * decimal representation and returned as a string, exactly as if
	 * the <code>byte</code> value were given as an argument to the
	 * {@link java.lang.Byte#toString(byte)} method.
	 * 
	 * @return  a string representation of the value of this object in
	 *          base&nbsp;10.
	 */
	@Override
	public String toString() {
		return (String.valueOf((int) value));
	}

	/**
	 * Returns a hash code for this <code>MutableByte</code>.
	 */
	@Override
	public int hashCode() {
		return ((int) value);
	}

	/**
	 * Compares this object to the specified object.  The result is
	 * <code>true</code> if and only if the argument is not
	 * <code>null</code> and is a <code>MutableByte</code> object that
	 * contains the same <code>byte</code> value as this object.
	 * 
	 * @param obj   the object to compare with
	 * @return      <code>true</code> if the objects are the same;
	 *          <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MutableByte) {
			return (value == ((MutableByte) obj).byteValue());
		}

		if (obj instanceof Byte) {
			return (value == ((Byte) obj).byteValue());
		}

		return (false);
	}

	/**
	 * Compares two <code>MutableByte</code> objects numerically.
	 * 
	 * @param   anotherByte   the <code>MutableByte</code> to be compared.
	 * @return  the value <code>0</code> if this <code>MutableByte</code> is
	 *      equal to the argument <code>MutableByte</code>; a value less than
	 *      <code>0</code> if this <code>MutableByte</code> is numerically less
	 *      than the argument <code>MutableByte</code>; and a value greater than
	 *       <code>0</code> if this <code>MutableByte</code> is numerically
	 *       greater than the argument <code>MutableByte</code> (signed
	 *       comparison).
	 * @since   1.2
	 */
	public int compareTo(MutableByte anotherByte) {
		return (this.value - anotherByte.value);
	}

	/**
	 * Constructs a newly allocated <code>MutableByte</code> object that
	 * represents the specified <code>byte</code> value.
	 * 
	 * @param value the value to be represented by the 
	 *          <code>MutableByte</code>.
	 */
	public MutableByte(byte value) {
		this.value = value;
	}

	/**
	 * Constructs a newly allocated <code>MutableByte</code> object that
	 * represents the <code>byte</code> value indicated by the
	 * <code>String</code> parameter. The string is converted to a
	 * <code>byte</code> value in exactly the manner used by the
	 * <code>parseByte</code> method for radix 10.
	 * 
	 * @param s     the <code>String</code> to be converted to a 
	 *          <code>MutableByte</code>
	 * @exception   NumberFormatException If the <code>String</code> 
	 *          does not contain a parsable <code>byte</code>.
	 * @see        java.lang.Byte#parseByte(java.lang.String, int)
	 */
	public MutableByte(String s) throws NumberFormatException {
		this.value = Byte.parseByte(s, 10);
	}

	/**
	 * Returns a <tt>MutableByte</tt> instance representing the specified
	 * <tt>byte</tt> value.
	 * If a new <tt>MutableByte</tt> instance is not required, this method
	 * should generally be used in preference to the constructor
	 * {@link #MutableByte(byte)}, as this method is likely to yield
	 * significantly better space and time performance by caching
	 * frequently requested values.
	 * 
	 * @param  b a byte value.
	 * @return a <tt>MutableByte</tt> instance representing <tt>b</tt>.
	 * @since  1.5
	 */
	public static MutableByte valueOf(byte b) {
		return (new MutableByte(b));
	}

	/**
	 * Returns a <code>MutableByte</code> object holding the value
	 * extracted from the specified <code>String</code> when parsed
	 * with the radix given by the second argument. The first argument
	 * is interpreted as representing a signed <code>byte</code> in
	 * the radix specified by the second argument, exactly as if the
	 * argument were given to the {@link Boolean#parseBoolean(String)}
	 * method. The result is a <code>MutableByte</code> object that
	 * represents the <code>byte</code> value specified by the string.
	 * <p> In other words, this method returns a <code>MutableByte</code> object
	 * equal to the value of:
	 * 
	 * <blockquote><code>
	 * new MutableByte(Byte.parseByte(s, radix))
	 * </code></blockquote>
	 * 
	 * @param s     the string to be parsed
	 * @param radix     the radix to be used in interpreting <code>s</code>
	 * @return      a <code>MutableByte</code> object holding the value 
	 *          represented by the string argument in the 
	 *          specified radix.
	 * @exception   NumberFormatException If the <code>String</code> does 
	 *          not contain a parsable <code>byte</code>.
	 */
	public static MutableByte valueOf(String s, int radix) throws NumberFormatException {
		return (new MutableByte(Byte.parseByte(s, radix)));
	}

	/**
	 * Returns a <code>MutableByte</code> object holding the value
	 * given by the specified <code>String</code>. The argument is
	 * interpreted as representing a signed decimal <code>byte</code>,
	 * exactly as if the argument were given to the {@link
	 * Boolean#parseBoolean(String)} method. The result is a
	 * <code>MutableByte</code> object that represents the <code>byte</code>
	 * value specified by the string.  <p> In other words, this method
	 * returns a <code>MutableByte</code> object equal to the value of:
	 * 
	 * <blockquote><code>
	 * new MutableByte(Byte.parseByte(s))
	 * </code></blockquote>
	 * 
	 * @param s     the string to be parsed
	 * @return      a <code>MutableByte</code> object holding the value
	 *          represented by the string argument
	 * @exception   NumberFormatException If the <code>String</code> does
	 *          not contain a parsable <code>byte</code>.
	 */
	public static MutableByte valueOf(String s) throws NumberFormatException {
		return (valueOf(s, 10));
	}
}
