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
 * This a mutable derivation of the {@link Long} class.
 * Most of the code is borrowed from the {@link Long} class.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class MutableLong extends Number implements Comparable<MutableLong> {
	private static final long serialVersionUID = -9049404274126384264L;

	/**
	 * The value of the <code>MutableLong</code>.
	 * 
	 * @serial
	 */
	private long value;

	public final void setValue(long value) {
		this.value = value;
	}

	/**
	 * Returns the value of this <code>MutableLong</code> as a
	 * <code>byte</code>.
	 */
	@Override
	public final byte byteValue() {
		return ((byte) value);
	}

	/**
	 * Returns the value of this <code>MutableLong</code> as a
	 * <code>short</code>.
	 */
	@Override
	public final short shortValue() {
		return ((short) value);
	}

	/**
	 * Returns the value of this <code>MutableLong</code> as an
	 * <code>int</code>.
	 */
	@Override
	public final int intValue() {
		return ((int) value);
	}

	/**
	 * Returns the value of this <code>MutableLong</code> as a
	 * <code>long</code> value.
	 */
	@Override
	public final long longValue() {
		return ((long) value);
	}

	/**
	 * Returns the value of this <code>MutableLong</code> as a
	 * <code>float</code>.
	 */
	@Override
	public final float floatValue() {
		return ((float) value);
	}

	/**
	 * Returns the value of this <code>MutableLong</code> as a
	 * <code>double</code>.
	 */
	@Override
	public double doubleValue() {
		return ((double) value);
	}

	/**
	 * Returns a <code>String</code> object representing this
	 * <code>MutableLong</code>'s value.  The value is converted to signed
	 * decimal representation and returned as a string, exactly as if
	 * the <code>long</code> value were given as an argument to the
	 * {@link java.lang.Long#toString(long)} method.
	 * 
	 * @return  a string representation of the value of this object in
	 *      base&nbsp;10.
	 */
	@Override
	public String toString() {
		return (String.valueOf(value));
	}

	/**
	 * Returns a hash code for this <code>MutableLong</code>. The result is
	 * the exclusive OR of the two halves of the primitive
	 * <code>long</code> value held by this <code>MutableLong</code>
	 * object. That is, the hashcode is the value of the expression:
	 * <blockquote><pre>
	 * (int)(this.longValue()^(this.longValue()&gt;&gt;&gt;32))
	 * </pre></blockquote>
	 * 
	 * @return  a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return ((int) (value ^ (value >>> 32)));
	}

	/**
	 * Compares this object to the specified object.  The result is
	 * <code>true</code> if and only if the argument is not
	 * <code>null</code> and is a <code>MutableLong</code> object that
	 * contains the same <code>long</code> value as this object.
	 * 
	 * @param   obj   the object to compare with.
	 * @return  <code>true</code> if the objects are the same;
	 *          <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MutableLong) {
			return (value == ((MutableLong) obj).longValue());
		}

		if (obj instanceof Long) {
			return (value == ((Long) obj).longValue());
		}

		return (false);
	}

	/**
	 * Compares two <code>MutableLong</code> objects numerically.
	 * 
	 * @param   anotherLong   the <code>MutableLong</code> to be compared.
	 * @return  the value <code>0</code> if this <code>MutableLong</code> is
	 *      equal to the argument <code>MutableLong</code>; a value less than
	 *      <code>0</code> if this <code>MutableLong</code> is numerically less
	 *      than the argument <code>MutableLong</code>; and a value greater 
	 *      than <code>0</code> if this <code>MutableLong</code> is numerically
	 *       greater than the argument <code>MutableLong</code> (signed
	 *       comparison).
	 * @since   1.2
	 */
	public int compareTo(MutableLong anotherLong) {
		final long thisVal = this.value;
		final long anotherVal = anotherLong.value;

		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}

	/**
	 * Constructs a newly allocated <code>MutableLong</code> object that
	 * represents the specified <code>long</code> argument.
	 * 
	 * @param   value   the value to be represented by the 
	 *          <code>MutableLong</code> object.
	 */
	public MutableLong(long value) {
		this.value = value;
	}

	/**
	 * Constructs a newly allocated <code>MutableLong</code> object that
	 * represents the <code>long</code> value indicated by the
	 * <code>String</code> parameter. The string is converted to a
	 * <code>long</code> value in exactly the manner used by the
	 * <code>parseLong</code> method for radix 10.
	 * 
	 * @param      s   the <code>String</code> to be converted to a 
	 *         <code>MutableLong</code>.
	 * @exception  NumberFormatException  if the <code>String</code> does not
	 *               contain a parsable <code>long</code>.
	 * @see        java.lang.Long#parseLong(java.lang.String, int)
	 */
	public MutableLong(String s) throws NumberFormatException {
		this.value = Long.parseLong(s, 10);
	}

	/**
	 * Returns a <code>MutableLong</code> object holding the value
	 * extracted from the specified <code>String</code> when parsed
	 * with the radix given by the second argument.  The first
	 * argument is interpreted as representing a signed
	 * <code>long</code> in the radix specified by the second
	 * argument, exactly as if the arguments were given to the {@link
	 * Long#parseLong(java.lang.String, int)} method. The result is a
	 * <code>MutableLong</code> object that represents the <code>long</code>
	 * value specified by the string.
	 * <p>
	 * In other words, this method returns a <code>MutableLong</code> object equal 
	 * to the value of:
	 * 
	 * <blockquote><code>
	 * new MutableLong(Long.parseLong(s, radix))
	 * </code></blockquote>
	 * 
	 * @param      s       the string to be parsed
	 * @param      radix   the radix to be used in interpreting <code>s</code>
	 * @return     a <code>MutableLong</code> object holding the value
	 *             represented by the string argument in the specified
	 *             radix.
	 * @exception  NumberFormatException  If the <code>String</code> does not
	 *             contain a parsable <code>long</code>.
	 */
	public static MutableLong valueOf(String s, int radix) throws NumberFormatException {
		return new MutableLong(Long.parseLong(s, radix));
	}

	/**
	 * Returns a <code>MutableLong</code> object holding the value
	 * of the specified <code>String</code>. The argument is
	 * interpreted as representing a signed decimal <code>long</code>,
	 * exactly as if the argument were given to the {@link
	 * Long#parseLong(java.lang.String)} method. The result is a
	 * <code>MutableLong</code> object that represents the integer value
	 * specified by the string.
	 * <p>
	 * In other words, this method returns a <code>MutableLong</code> object
	 * equal to the value of:
	 * 
	 * <blockquote><pre>
	 * new MutableLong(Long.parseLong(s))
	 * </pre></blockquote>
	 * 
	 * @param      s   the string to be parsed.
	 * @return     a <code>MutableLong</code> object holding the value
	 *             represented by the string argument.
	 * @exception  NumberFormatException  If the string cannot be parsed
	 *              as a <code>long</code>.
	 */
	public static MutableLong valueOf(String s) throws NumberFormatException {
		return new MutableLong(Long.parseLong(s, 10));
	}

	/**
	 * Returns a <tt>MutableLong</tt> instance representing the specified
	 * <tt>long</tt> value.
	 * If a new <tt>MutableLong</tt> instance is not required, this method
	 * should generally be used in preference to the constructor
	 * {@link #MutableLong(long)}, as this method is likely to yield
	 * significantly better space and time performance by caching
	 * frequently requested values.
	 * 
	 * @param  l a long value.
	 * @return a <tt>MutableLong</tt> instance representing <tt>l</tt>.
	 * @since  1.5
	 */
	public static MutableLong valueOf(long l) {
		return (new MutableLong(l));
	}
}
