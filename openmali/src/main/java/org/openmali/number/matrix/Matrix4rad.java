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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringReader;

import org.openmali.FastMath;
import org.openmali.number.Parser;
import org.openmali.number.Radical1;
import org.openmali.vecmath2.Matrix4f;

/**
 * Represenents a spatial transformation.
 * <p/>
 * The underlying number representation is radical1. This allows classic trigonomic constants to be
 * explicitly represented (such as sqrt(2)/2). You can completely avoid floating point rounding issues for
 * very speacial cases of geometry. The motivation for this functionality was angles of 30,45,60 degrees
 * which naturally cancel during use (on a plane)
 * 
 * @author Tom Larkworthy
 */
public class Matrix4rad extends MatrixMxNrad implements Externalizable {
	private static final Radical1 l1 = new Radical1();
	private static final Radical1 l2 = new Radical1();
	private static final Radical1 l3 = new Radical1();
	private static final Radical1 l4 = new Radical1();
	private static final Radical1 l5 = new Radical1();
	private static final Radical1 l6 = new Radical1();
	private static final Radical1 l7 = new Radical1();
	private static final Radical1 l8 = new Radical1();
	private static final Radical1 l9 = new Radical1();
	private static final Radical1 l10 = new Radical1();
	private static final Radical1 l11 = new Radical1();
	private static final Radical1 l12 = new Radical1();
	private static final Radical1 l13 = new Radical1();
	private static final Radical1 l14 = new Radical1();
	private static final Radical1 l15 = new Radical1();
	private static final Radical1 l16 = new Radical1();

	private static final Radical1 op1 = new Radical1();
	private static final Radical1 op2 = new Radical1();
	private static final Radical1 op3 = new Radical1();
	private static final Radical1 op4 = new Radical1();
	private static final Radical1 op5 = new Radical1();
	private static final Radical1 op6 = new Radical1();

	public static final Matrix4rad IDENTITY = Matrix4rad.newReadOnly();

	public Radical1 m00(Radical1 passback) {
		return (get(0, 0, passback));
	}

	public final Radical1 m01(Radical1 passback) {
		return (get(0, 1, passback));
	}

	public final Radical1 m02(Radical1 passback) {
		return (get(0, 2, passback));
	}

	public final Radical1 m03(Radical1 passback) {
		return (get(0, 3, passback));
	}

	public final Radical1 m10(Radical1 passback) {
		return (get(1, 0, passback));
	}

	public final Radical1 m11(Radical1 passback) {
		return (get(1, 1, passback));
	}

	public final Radical1 m12(Radical1 passback) {
		return (get(1, 2, passback));
	}

	public final Radical1 m13(Radical1 passback) {
		return (get(1, 3, passback));
	}

	public final Radical1 m20(Radical1 passback) {
		return (get(2, 0, passback));
	}

	public final Radical1 m21(Radical1 passback) {
		return (get(2, 1, passback));
	}

	public final Radical1 m22(Radical1 passback) {
		return (get(2, 2, passback));
	}

	public final Radical1 m23(Radical1 passback) {
		return (get(2, 3, passback));
	}

	public final Radical1 m30(Radical1 passback) {
		return (get(3, 0, passback));
	}

	public final Radical1 m31(Radical1 passback) {
		return (get(3, 1, passback));
	}

	public final Radical1 m32(Radical1 passback) {
		return (get(3, 2, passback));
	}

	public final Radical1 m33(Radical1 passback) {
		return (get(3, 3, passback));
	}

	public Radical1 m00R() {
		return (getReference(0, 0));
	}

	public final Radical1 m01R() {
		return (getReference(0, 1));
	}

	public final Radical1 m02R() {
		return (getReference(0, 2));
	}

	public final Radical1 m03R() {
		return (getReference(0, 3));
	}

	public final Radical1 m10R() {
		return (getReference(1, 0));
	}

	public final Radical1 m11R() {
		return (getReference(1, 1));
	}

	public final Radical1 m12R() {
		return (getReference(1, 2));
	}

	public final Radical1 m13R() {
		return (getReference(1, 3));
	}

	public final Radical1 m20R() {
		return (getReference(2, 0));
	}

	public final Radical1 m21R() {
		return (getReference(2, 1));
	}

	public final Radical1 m22R() {
		return (getReference(2, 2));
	}

	public final Radical1 m23R() {
		return (getReference(2, 3));
	}

	public final Radical1 m30R() {
		return (getReference(3, 0));
	}

	public final Radical1 m31R() {
		return (getReference(3, 1));
	}

	public final Radical1 m32R() {
		return (getReference(3, 2));
	}

	public final Radical1 m33R() {
		return (getReference(3, 3));
	}

	public void setRotation(Matrix3rad rot) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				set(i, j, rot.getReference(i, j));
			}
		}
	}

	public Matrix3rad getRotation(Matrix3rad passback) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				get(i, j, passback.getReference(i, j));
			}
		}

		return (passback);
	}

	public void setTranslation(Tuple3rad d) {
		for (int i = 0; i < 3; i++) {
			set(i, 3, d.getReference(i));
		}
	}

	public Tuple3rad getTranslation(Tuple3rad passback) {
		for (int i = 0; i < 3; i++) {
			get(i, 3, passback.getReference(i));
		}

		return (passback);
	}

	public static Matrix4rad mul(Matrix4rad op1, Matrix4rad op2, Matrix4rad result) {
		result.mul(op1, op2);

		return (result);
	}

	/**
	 * numerically tests whether they are approximately the same
	 * 
	 * @param other
	 * @param e
	 * @return
	 */
	public boolean epsilonEquals(Matrix4f other, float e) {
		Radical1 passback = new Radical1();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (!FastMath.epsilonEquals(other.get(i, j), this.get(i, j, passback).floatValue(), e))
					return false;
			}
		}

		return (true);
	}

	@Override
	public Matrix4rad clone() {
		Radical1 tmp = new Radical1();
		Matrix4rad result = new Matrix4rad();
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				result.set(i, j, get(i, j, tmp));

		return (result);
	}

	public final Radical1 determinant() {
		l1.setZero();
		l2.setZero();
		l3.setZero();
		l4.setZero();
		l5.setZero();
		l6.setZero();

		Radical1.mul((Radical1.sub(Radical1.mul(m00R(), m11R(), op1), Radical1.mul(m01R(), m10R(), op2), op3)),
				(Radical1.sub(Radical1.mul(m22R(), m33R(), op4), Radical1.mul(m23R(), m32R(), op5), op6)), l1);
		Radical1.mul((Radical1.sub(Radical1.mul(m00R(), m12R(), op1), Radical1.mul(m02R(), m10R(), op2), op3)),
				(Radical1.sub(Radical1.mul(m21R(), m33R(), op4), Radical1.mul(m23R(), m31R(), op5), op6)), l2);
		Radical1.mul((Radical1.sub(Radical1.mul(m00R(), m13R(), op1), Radical1.mul(m03R(), m10R(), op2), op3)),
				(Radical1.sub(Radical1.mul(m21R(), m32R(), op4), Radical1.mul(m22R(), m31R(), op5), op6)), l3);
		Radical1.mul((Radical1.sub(Radical1.mul(m01R(), m12R(), op1), Radical1.mul(m02R(), m11R(), op2), op3)),
				(Radical1.sub(Radical1.mul(m20R(), m33R(), op4), Radical1.mul(m23R(), m30R(), op5), op6)), l4);
		Radical1.mul((Radical1.sub(Radical1.mul(m01R(), m13R(), op1), Radical1.mul(m03R(), m11R(), op2), op3)),
				(Radical1.sub(Radical1.mul(m20R(), m32R(), op4), Radical1.mul(m22R(), m30R(), op5), op6)), l5);
		Radical1.mul((Radical1.sub(Radical1.mul(m02R(), m13R(), op1), Radical1.mul(m03R(), m12R(), op2), op3)),
				(Radical1.sub(Radical1.mul(m02R(), m31R(), op4), Radical1.mul(m21R(), m30R(), op5), op6)), l6);

		Radical1 ans = new Radical1();

		Radical1.sub(l1, l2, ans);
		Radical1.add(ans, l3, ans);
		Radical1.add(ans, l4, ans);
		Radical1.sub(ans, l5, ans);
		Radical1.add(ans, l6, ans);

		// see Matrix4f.det for an explination
		return (ans);
	}

	private static Radical1 mulR(Radical1 a, Radical1 b, Radical1 ans) {
		return (Radical1.mul(a, b, ans));
	}

	private static Radical1 addR(Radical1 a, Radical1 b, Radical1 ans) {
		return (Radical1.add(a, b, ans));
	}

	private static Radical1 subR(Radical1 a, Radical1 b, Radical1 ans) {
		return (Radical1.sub(a, b, ans));
	}

	/**
	 * this invert function inverts the matrix. This only works for the special case when the determinent is
	 * 1 i.e. there is no scaling representated by this transformation. This is true for rotation & translation
	 * and combinations thereof matrices.
	 * (our representation of radical1 can't divide by radicals, so we can't rescale)
	 */
	public final void invert() {
		Radical1 s = determinant();
		if (s.equals(Radical1.ZERO))
			return;

		if (!s.equals(Radical1.ONE))
			throw new ArithmeticException("can't invert radical expressions that have non-1 determinant (i.e. that rescale)");

		// see Matrix4f.invert for an explination

		l1.setZero();
		l2.setZero();
		l3.setZero();
		l4.setZero();
		l5.setZero();
		l6.setZero();
		l7.setZero();
		l8.setZero();
		l9.setZero();
		l10.setZero();
		l11.setZero();
		l12.setZero();
		l13.setZero();
		l14.setZero();
		l15.setZero();
		l16.setZero();

		calcPartInverse(5, 10, 15, 11, 14, op1, op2, op3, op4, l1);
		calcPartInverse(6, 11, 13, 9, 15, op1, op2, op3, op4, l1);
		calcPartInverse(7, 9, 14, 10, 13, op1, op2, op3, op4, l1);

		calcPartInverse(9, 2, 15, 3, 14, op1, op2, op3, op4, l2);
		calcPartInverse(10, 3, 13, 1, 15, op1, op2, op3, op4, l2);
		calcPartInverse(11, 1, 14, 2, 13, op1, op2, op3, op4, l2);

		calcPartInverse(13, 2, 7, 3, 6, op1, op2, op3, op4, l3);
		calcPartInverse(14, 3, 5, 1, 7, op1, op2, op3, op4, l3);
		calcPartInverse(15, 1, 6, 2, 5, op1, op2, op3, op4, l3);

		calcPartInverse(1, 7, 10, 6, 11, op1, op2, op3, op4, l4);
		calcPartInverse(2, 5, 11, 7, 9, op1, op2, op3, op4, l4);
		calcPartInverse(3, 6, 9, 5, 10, op1, op2, op3, op4, l4);

		calcPartInverse(6, 8, 15, 11, 12, op1, op2, op3, op4, l5);
		calcPartInverse(7, 10, 12, 8, 14, op1, op2, op3, op4, l5);
		calcPartInverse(4, 11, 14, 10, 15, op1, op2, op3, op4, l5);

		calcPartInverse(10, 0, 15, 3, 12, op1, op2, op3, op4, l6);
		calcPartInverse(11, 2, 12, 0, 14, op1, op2, op3, op4, l6);
		calcPartInverse(8, 3, 14, 2, 15, op1, op2, op3, op4, l6);

		calcPartInverse(14, 0, 7, 3, 4, op1, op2, op3, op4, l7);
		calcPartInverse(15, 2, 4, 0, 6, op1, op2, op3, op4, l7);
		calcPartInverse(12, 3, 6, 2, 7, op1, op2, op3, op4, l7);

		calcPartInverse(2, 7, 8, 4, 11, op1, op2, op3, op4, l8);
		calcPartInverse(3, 4, 10, 6, 8, op1, op2, op3, op4, l8);
		calcPartInverse(0, 6, 11, 7, 10, op1, op2, op3, op4, l8);

		calcPartInverse(7, 8, 13, 9, 12, op1, op2, op3, op4, l9);
		calcPartInverse(4, 9, 15, 11, 13, op1, op2, op3, op4, l9);
		calcPartInverse(5, 11, 12, 8, 15, op1, op2, op3, op4, l9);

		calcPartInverse(11, 0, 13, 1, 12, op1, op2, op3, op4, l10);
		calcPartInverse(8, 1, 15, 3, 13, op1, op2, op3, op4, l10);
		calcPartInverse(9, 3, 12, 0, 15, op1, op2, op3, op4, l10);

		calcPartInverse(15, 0, 5, 1, 4, op1, op2, op3, op4, l11);
		calcPartInverse(12, 1, 7, 3, 5, op1, op2, op3, op4, l11);
		calcPartInverse(13, 3, 4, 0, 7, op1, op2, op3, op4, l11);

		calcPartInverse(3, 5, 8, 4, 9, op1, op2, op3, op4, l12);
		calcPartInverse(0, 7, 9, 5, 11, op1, op2, op3, op4, l12);
		calcPartInverse(1, 4, 11, 7, 8, op1, op2, op3, op4, l12);

		calcPartInverse(4, 10, 13, 9, 14, op1, op2, op3, op4, l13);
		calcPartInverse(5, 8, 14, 10, 12, op1, op2, op3, op4, l13);
		calcPartInverse(6, 9, 12, 8, 13, op1, op2, op3, op4, l13);

		calcPartInverse(8, 2, 13, 1, 14, op1, op2, op3, op4, l14);
		calcPartInverse(9, 0, 14, 2, 12, op1, op2, op3, op4, l14);
		calcPartInverse(10, 1, 12, 0, 13, op1, op2, op3, op4, l14);

		calcPartInverse(12, 2, 5, 1, 6, op1, op2, op3, op4, l15);
		calcPartInverse(13, 0, 6, 2, 4, op1, op2, op3, op4, l15);
		calcPartInverse(14, 1, 4, 0, 5, op1, op2, op3, op4, l15);

		calcPartInverse(0, 5, 10, 6, 9, op1, op2, op3, op4, l16);
		calcPartInverse(1, 6, 8, 4, 10, op1, op2, op3, op4, l16);
		calcPartInverse(2, 4, 9, 5, 8, op1, op2, op3, op4, l16);

		values[0].set(l1);
		values[1].set(l2);
		values[2].set(l3);
		values[3].set(l4);
		values[4].set(l5);
		values[5].set(l6);
		values[6].set(l7);
		values[7].set(l8);
		values[8].set(l9);
		values[9].set(l10);
		values[10].set(l11);
		values[11].set(l12);
		values[12].set(l13);
		values[13].set(l14);
		values[14].set(l15);
		values[15].set(l16);

		if (s.equals(Radical1.MINUS_ONE))
			negate();
	}

	/**
	 * helper method for implementation of the inverse function
	 */
	private void calcPartInverse(int i, int i1, int i2, int i3, int i4, Radical1 op1, Radical1 op2, Radical1 op3, Radical1 op4, Radical1 l1) {
		addR(l1, mulR(values[i], subR(mulR(values[i1], values[i2], op3), mulR(values[i3], values[i4], op4), op2), op1), l1);
	}

	/**
	 * sets this object to be the inverse of the argument
	 * 
	 * @param toInvert
	 */
	public final void invert(Matrix4rad toInvert) {
		this.set(toInvert);
		invert();
	}

	public static Matrix4rad parseMatrix4rad(String str) {
		StringReader in = new StringReader(str);

		try {
			Matrix4rad ret = new Parser(in).matrix4rad();
			in.close();

			return (ret);
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return (null);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getReference(0, 3)); // write the translational parts
		out.writeObject(getReference(1, 3));
		out.writeObject(getReference(2, 3));

		out.writeObject(getReference(0, 0)); // write the top square
		out.writeObject(getReference(0, 1));
		out.writeObject(getReference(1, 0));
		out.writeObject(getReference(1, 1));

		out.writeObject(getReference(0, 2)); // write the redundant bottom
												// square
		out.writeObject(getReference(1, 2));
		out.writeObject(getReference(2, 0));
		out.writeObject(getReference(2, 1));
		out.writeObject(getReference(2, 2));

		/*
		 * boolean topRightSign = getReference( 0, 2 ).isPositive(); boolean
		 * midRightSign = getReference( 1, 2 ).isPositive(); boolean botLeftSign
		 * = getReference( 2, 0 ).isPositive(); boolean botMidSign =
		 * getReference( 2, 1 ).isPositive(); boolean botRightSign =
		 * getReference( 2, 2 ).isPositive();
		 */
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setReference(0, 3, (Radical1) in.readObject());
		setReference(1, 3, (Radical1) in.readObject());
		setReference(2, 3, (Radical1) in.readObject());

		setReference(0, 0, (Radical1) in.readObject());
		setReference(0, 1, (Radical1) in.readObject());
		setReference(1, 0, (Radical1) in.readObject());
		setReference(1, 1, (Radical1) in.readObject());

		setReference(0, 2, (Radical1) in.readObject());
		setReference(1, 2, (Radical1) in.readObject());
		setReference(2, 0, (Radical1) in.readObject());
		setReference(2, 1, (Radical1) in.readObject());
		setReference(2, 2, (Radical1) in.readObject());

		/*
		 * // main properties of rotation matrices are that their rows and
		 * column elements squared, // sum to 1, but we have to get the sign
		 * information elsewhere setReference( 0, 2,
		 * getRemainingRotationElement( getReference( 0, 0 ), getReference( 0, 1
		 * ), new Radical1() ) ); setReference( 1, 2,
		 * getRemainingRotationElement( getReference( 1, 0 ), getReference( 1, 1
		 * ), new Radical1() ) );
		 * 
		 * setReference( 2, 0, getRemainingRotationElement( getReference( 0, 0
		 * ), getReference( 1, 0 ), new Radical1() ) ); setReference( 2, 1,
		 * getRemainingRotationElement( getReference( 0, 1 ), getReference( 1, 1
		 * ), new Radical1() ) );
		 * 
		 * setReference( 2, 2, getRemainingRotationElement( getReference( 0, 2
		 * ), getReference( 1, 2 ), new Radical1() ) );
		 */
	}

	/**
	 * For use caclulating redundant values of 3x3 rotation matrices
	 * ans = 1 - e1^2 + e2^2
	 */
	/*
	 * private Radical1 getRemainingRotationElement(Radical1 element1, Radical1
	 * element2, Radical1 passback) { //square elements Radical1.mul(element1,
	 * element1, tmp1); Radical1.mul(element2, element2, tmp2);
	 * 
	 * Radical1.add(passback, Radical1.ONE, passback); Radical1.sub(passback,
	 * Radical1.ONE, tmp1); Radical1.sub(passback, Radical1.ONE, tmp2);
	 * 
	 * return passback; }
	 */

	protected Matrix4rad(boolean readOnly) {
		super(readOnly, 4, 4);
	}

	/**
	 * Constructs and initializes a Matrix4f to all zeros.
	 */
	public Matrix4rad() {
		this(false);
	}

	public Matrix4rad(Tuple3rad translation, Matrix3rad rotation) {
		this();

		this.setTranslation(translation);
		this.setRotation(rotation);
	}

	/**
	 * Constructs and initializes a Matrix4f to all zeros.
	 */
	public static Matrix4rad newReadOnly() {
		return (new Matrix4rad(true));
	}
}
