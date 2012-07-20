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
package org.openmali.number.matrix;

import java.util.Arrays;

import org.openmali.number.Radical1;
import org.openmali.number.Rational;

/**
 * @author Tom Larkworthy
 */
public class TupleNrad<T extends TupleNrad<T>> {
	private final int N;
	protected final Radical1[] values;

	protected final int roTrick;

	Radical1 TMP = new Radical1();

	/**
	 * @return Is this tuple a read-only one?
	 */
	public final boolean isReadOnly() {
		return (roTrick != 0);
	}

	/**
	 * @return this Tuple's size().
	 */
	public final int getSize() {
		return (N);
	}

	/**
	 * Sets the i-th value of this tuple.
	 */
	public final void set(int i, Radical1 v) {
		this.values[roTrick + i].set(v);
	}

	/**
	 * Sets the i-th value of this tuple.
	 */
	public final Radical1 get(int i, Radical1 passback) {
		passback.set(values[i]);

		return (passback);
	}

	/**
	 * @param index
	 * @return the i'th value of this tuple object reference
	 */
	public final Radical1 getReference(int index) {
		return (values[index]);
	}

	/**
	 * Sets all values of this TupleNf to f.
	 * 
	 * @param value the value
	 */
	public final void fill(Radical1 value) {
		for (int i = 0; i < values.length; i++)
			values[i].set(value);
	}

	/**
	 * Adds v to this tuple's i'th value.
	 * 
	 * @param i the index of the value to modify
	 * @param v modification amount
	 */
	public final void add(int i, Radical1 v) {
		Radical1.add(values[i], v, values[i]);
	}

	/**
	 * Subtracts v of this tuple's i'th value.
	 * 
	 * @param i the index of the value to modify
	 * @param v modification amount
	 */
	public final void sub(int i, Radical1 v) {
		Radical1.sub(values[i], v, values[i]);
	}

	/**
	 * Multiplies v to this tuple's i'th value.
	 * 
	 * @param i the index of the value to modify
	 * @param v modification amount
	 */
	public final void mul(int i, Radical1 v) {
		Radical1.mul(values[i], v, values[i]);
	}

	/**
	 * Multiplies all components of this tuple with v.
	 * 
	 * @param v modification amount
	 */
	public final void mul(Radical1 v) {
		for (int i = 0; i < N; i++)
			Radical1.mul(values[i], v, values[i]);
	}

	public Radical1 dot(Tuple3rad other, Radical1 passback) {
		if (passback == null)
			passback = new Radical1();
		passback.setZero();

		for (int i = 0; i < N; i++) {
			Radical1.add(Radical1.mul(this.getReference(i), other.getReference(i), TMP), passback, passback);
		}

		return (passback);
	}

	/**
	 * Divides this tuple's i'th value by v.
	 * 
	 * @param i the index of the value to modify
	 * @param v modification amount
	 */
	public final void div(int i, Radical1 v) {
		throw new UnsupportedOperationException("implements yourself and check it in");
		// Radical1.div( values[i], v, values[i] );
		// this.isDirty = true;
	}

	/**
	 * Divides all components of this tuple by v.
	 * 
	 * @param v modification amount
	 */
	public final void div(Radical1 v) {
		throw new UnsupportedOperationException("implements yourself and check it in");
		// for ( int i = 0; i < N; i++ )
		// Radical1.div( values[i], v, values[i] );

		// this.isDirty = true;
	}

	/**
	 * Sets all values of this Tuple to the specified ones.
	 * 
	 * @param values the values array (must be at least size getSize())
	 */
	public void set(Radical1[] values) {
		for (int i = 0; i < N; i++)
			values[i].set(values[i]);
	}

	/**
	 * Sets all three values of this Tuple to the specified ones.
	 * 
	 * @param tuple the tuple to be copied
	 */
	public final void set(TupleNrad<?> tuple) {
		for (int i = 0; i < N; i++)
			values[i].set(tuple.values[i]);
	}

	/**
	 * Writes all values of this Tuple to the specified buffer.
	 * 
	 * @param buffer the buffer array to write the values to
	 */
	public void get(TupleNrad<?> buffer) {
		for (int i = 0; i < N; i++)
			buffer.values[i].set(values[i]);
	}

	/**
	 * Sets all components to zero.
	 */
	public final void setZero() {
		for (int i = 0; i < N; i++)
			values[i].setZero();
	}

	/**
	 * Negates the value of this vector in place.
	 */
	public final void negate() {
		for (int i = 0; i < N; i++)
			values[i].negate(values[i]);
	}

	/**
	 * Sets the value of this tuple to the negation of tuple that.
	 * 
	 * @param tuple the source vector
	 */
	public final void negate(T tuple) {
		for (int i = 0; i < N; i++)
			values[i].negate(tuple.values[i]);
	}

	/**
	 * Sets each component of the tuple parameter to its absolute value and
	 * places the modified values into this tuple.
	 */
	public final void absolute() {
		for (int i = 0; i < N; i++)
			values[i].absolute(values[i]);
	}

	/**
	 * Sets each component of the tuple parameter to its absolute value and
	 * places the modified values into this tuple.
	 * 
	 * @param tuple the source tuple, which will not be modified
	 */
	public final void absolute(T tuple) {
		for (int i = 0; i < N; i++)
			values[i].absolute(tuple.values[i]);
	}

	/**
	 * Sets the value of this tuple to the vector sum of tuples t1 and t2.
	 * 
	 * @param tuple1 the first tuple
	 * @param tuple2 the second tuple
	 */
	public final void add(T tuple1, T tuple2) {
		for (int i = 0; i < N; i++)
			Radical1.add(tuple1.values[i], tuple2.values[i], values[i]);
	}

	/**
	 * Sets the value of this tuple to the vector sum of itself and tuple t1.
	 * 
	 * @param tuple2 the other tuple
	 */
	public final void add(T tuple2) {
		for (int i = 0; i < N; i++)
			Radical1.add(values[i], tuple2.values[i], values[i]);
	}

	/**
	 * Sets the value of this tuple to the vector difference of tuple t1 and t2
	 * (this = t1 - t2).
	 * 
	 * @param tuple1 the first tuple
	 * @param tuple2 the second tuple
	 */
	public final void sub(T tuple1, T tuple2) {
		for (int i = 0; i < N; i++)
			Radical1.sub(tuple1.values[i], tuple2.values[i], values[i]);
	}

	/**
	 * Sets the value of this tuple to the vector difference of itself and tuple
	 * t1 (this = this - t1).
	 * 
	 * @param tuple2 the other tuple
	 */
	public final void sub(T tuple2) {
		for (int i = 0; i < N; i++)
			Radical1.sub(values[i], tuple2.values[i], values[i]);
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication of tuple t1.
	 * 
	 * @param factor the scalar value
	 * @param tuple the source tuple
	 */
	public final void scale(Rational factor, T tuple) {
		for (int i = 0; i < N; i++)
			tuple.values[i].mul(factor, values[i]);
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication of itself.
	 * 
	 * @param factor the scalar value
	 */
	public final void scale(Rational factor) {
		for (int i = 0; i < N; i++)
			values[i].mul(factor, values[i]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result;
		result = N;
		result = 31 * result + Arrays.hashCode(values);

		return (result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TupleNrad))
			return (false);

		TupleNrad<?> other = (TupleNrad<?>) obj;

		if (other.getSize() != getSize())
			return (false);

		for (int i = 0; i < values.length; i++) {
			if (!values[i].equals(other.values[i]))
				return (false);
		}

		return (true);
	}

	/**
	 * Creates a new TupleNf instance.
	 * 
	 * @param readOnly
	 * @param n the number of elements
	 */
	protected TupleNrad(boolean readOnly, int n) {
		this.N = n;
		this.values = new Radical1[n];
		for (int i = 0; i < N; i++) {
			values[i] = new Radical1();
		}

		this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
	}

	/**
	 * Creates a new TupleNf instance.
	 * 
	 * @param readOnly
	 * @param values the values array (must be at least size 3)
	 * @param n the number of elements
	 * @param copy copy the array?
	 */
	protected TupleNrad(boolean readOnly, Radical1[] values, int n, boolean copy) {
		this.N = n;
		if (copy) {
			this.values = new Radical1[n];
			for (int i = 0; i < N; i++) {
				this.values[i] = new Radical1(values[i]);
			}
		} else {
			this.values = values;
		}

		this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
	}

	/**
	 * Creates a new TupleNf instance.
	 * 
	 * @param readOnly
	 * @param values the values array (must be at least size n)
	 * @param n the number of elements to copy
	 */
	protected TupleNrad(boolean readOnly, Radical1[] values, int n) {
		this(readOnly, values, n, true);
	}

	/**
	 * Creates a new Tuple3f instance.
	 * 
	 * @param readOnly
	 * @param that the TupleNf to copy the values from
	 */
	protected TupleNrad(boolean readOnly, TupleNrad<?> that) {
		this(readOnly, that.values, that.values.length, true);
	}

	/**
	 * Creates a new TupleNf instance.
	 *
	 * @param n the number of elements
	 */
	public TupleNrad(int n) {
		this(false, n);
	}

	/**
	 * Creates a new TupleNf instance.
	 * 
	 * @param values the values array (must be at least size 3)
	 * @param n the number of elements
	 * @param copy copy the array?
	 */
	public TupleNrad(Radical1[] values, int n, boolean copy) {
		this(false, values, n, copy);
	}

	/**
	 * Creates a new TupleNf instance.
	 * 
	 * @param values the values array (must be at least size n)
	 * @param n the number of elements to copy
	 */
	public TupleNrad(Radical1[] values, int n) {
		this(false, values, n);
	}

	/**
	 * Creates a new Tuple3f instance.
	 * 
	 * @param that the TupleNf to copy the values from
	 */
	public TupleNrad(TupleNrad<?> that) {
		this(false, that);
	}
}
