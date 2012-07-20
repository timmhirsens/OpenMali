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

import java.util.Random;

import org.openmali.FastMath;
import org.openmali.number.Rational;

/**
 */
public class TestRational extends TestCase {
	static Random rnd = new Random();

	static int ITERATIONS = 1000;

	public static Rational randomRational() {
		return new Rational(rnd.nextInt(20) - 10, rnd.nextInt(9) + 1);
	}

	public void testAdd() {
		for (int t = 0; t < ITERATIONS; t++) {
			Rational a = randomRational();
			Rational b = randomRational();

			float validation = a.floatValue() + b.floatValue();

			Rational ans = Rational.add(a, b, a);

			assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), .0001f));

		}
	}

	public void testMul() {
		for (int t = 0; t < ITERATIONS; t++) {
			Rational a = randomRational();
			Rational b = randomRational();

			float validation = a.floatValue() * b.floatValue();

			Rational ans = Rational.mul(a, b, a);

			assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), .0001f));

		}
	}

	public void testSub() {
		for (int t = 0; t < ITERATIONS; t++) {
			Rational a = randomRational();
			Rational b = randomRational();

			float validation = a.floatValue() - b.floatValue();

			Rational ans = Rational.sub(a, b, a);

			assertTrue(FastMath.epsilonEquals(validation, ans.floatValue(), .0001f));

		}
	}

	public void testEquals() {
		for (int t = 0; t < ITERATIONS; t++) {
			Rational a = randomRational();
			Rational b = a.clone();
			Rational op = randomRational();

			Rational.sub(b, op, b);
			Rational.add(b, op, b);

			System.out.println(a);
			System.out.println(b);

			assertTrue(a.equals(b));

		}
	}

	public void testParse() {
		for (int t = 0; t < ITERATIONS; t++) {
			Rational a = randomRational();

			Rational ta = Rational.parseRational(a.toString());

			System.out.println("ta = " + ta);
			assertTrue(a.equals(ta));
		}
	}
}
