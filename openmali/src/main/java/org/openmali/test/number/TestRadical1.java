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
package org.openmali.test.number;

import org.openmali.FastMath;
import org.openmali.number.Radical1;
import org.openmali.number.Rational;
import org.openmali.number.matrix.Tuple3rad;

/**
 */
public class TestRadical1 extends TestCase {
	private static final int ITERATIONS = 1000;

	public static Radical1 randomRadical(int terms) {
		Radical1 result = new Radical1();
		for (int i = 0; i < terms; i++) {
			result.addTerm(new Rational(FastMath.randomInt(10), FastMath.randomInt(9) + 1), FastMath.randomInt(5) + 1);
		}

		return (result);
	}

	public void testAdd() {
		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 a = randomRadical(2);
			Radical1 b = randomRadical(2);

			float validation = a.floatValue() + b.floatValue();

			Radical1 ans = Radical1.add(a, b, a);

			assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), 0.0001f));
		}
	}

	public void testMul() {
		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 a = randomRadical(2);
			Radical1 b = randomRadical(2);

			float validation = a.floatValue() * b.floatValue();

			Radical1 ans = Radical1.mul(a, b, a);

			assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), 0.0001f));
		}
	}

	public void testMulZero() {
		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 a = randomRadical(2);
			Radical1 b = Radical1.ZERO;

			float validation = a.floatValue() * b.floatValue();

			Radical1 ans = Radical1.mul(b, a, a);

			assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), 0.0001f));
		}
	}

	public void testSub() {
		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 a = randomRadical(2);
			Radical1 b = randomRadical(2);

			float validation = a.floatValue() - b.floatValue();

			Radical1 ans = Radical1.sub(a, b, a);

			assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), 0.0001f));
		}
	}

	public void testEquals() {
		assertTrue(new Tuple3rad().equals(new Tuple3rad()));

		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 a = randomRadical(2);
			Radical1 b = a.clone();

			Radical1 op = randomRadical(5);

			Radical1.sub(b, op, b);
			Radical1.add(b, op, b);

			assertTrue(a.equals(b));
		}
	}

	public void testHashCode() {
		assertTrue(new Tuple3rad().equals(new Tuple3rad()));

		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 a = randomRadical(2);
			Radical1 b = a.clone();

			Radical1 op = randomRadical(5);
			Radical1.sub(b, op, b);
			Radical1.add(b, op, b);

			assertTrue(a.hashCode() == b.hashCode());
		}
	}

	public void testBounds() {
		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 num = randomRadical(3);

			Rational upper = new Rational();
			Rational lower = new Rational();

			num.getBounds(upper, lower);

			System.out.println("upper = " + upper);
			System.out.println("lower = " + lower);

			assertTrue(upper.floatValue() >= num.floatValue());
			assertTrue(lower.floatValue() <= num.floatValue());
		}
	}

	public void testParse() {
		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 num = Radical1.sub(randomRadical(3), randomRadical(3), new Radical1());

			String str = num.toString();

			// str = "3*√2+1/4*√3+√5";
			System.out.println("str = " + str);

			Radical1 tst = Radical1.parseRadical1(str);

			assertEquals(tst, num);
		}
	}

	public void testMod() {
		System.out.println(Radical1.mod(new Radical1(-1, 1, 2), new Rational(1), new Radical1()));

		int success = 0;
		for (int t = 0; t < ITERATIONS; t++) {
			Radical1 num = randomRadical(3);
			Radical1 ans = randomRadical(3);

			float validation = num.floatValue() % 1;

			System.out.println("num = " + num);
			System.out.println("num = " + num.floatValue());

			System.out.println("num % 1 = " + validation);

			try {
				Radical1.mod(num, new Rational(1), ans);

				System.out.println("ans = " + ans);
				System.out.println("ans = " + ans.floatValue());

				assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), .0001f));
				success++;
			} catch (ArithmeticException e) {
				System.out.println("***could not perform mod in radical math***");
			}
		}

		System.out.println("mod successes = " + success + "/" + ITERATIONS);
	}
}
