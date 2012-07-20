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
package org.openmali.number;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openmali.FastMath;
import org.openmali.number.matrix.Matrix3rad;
import org.openmali.number.matrix.Matrix4rad;
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Matrix4f;

/**
 * Helper functionality for conversion of Radical1 to floats and vice versa
 * 
 * @author Tom Larkworthy
 */
public class RadicalUtils {
	public static HashSet<Radical1> approximators = new HashSet<Radical1>();

	private static Radical1 tmp = new Radical1();

	static {
		approximators.add(new Radical1(1));
		approximators.add(new Radical1(-1));
		approximators.add(new Radical1(0));
		approximators.add(new Radical1(new Rational(1, 2)));
		approximators.add(new Radical1(new Rational(-1, 2)));
		approximators.add(new Radical1(1, 2, 3));
		approximators.add(new Radical1(-1, 2, 3));
		approximators.add(new Radical1(1, 2, 2));
		approximators.add(new Radical1(-1, 2, 2));
	}

	/**
	 * converts the normal floating point representation into the radical form
	 * this only works for certain trigonomic constants like 30 degree angles etc.
	 * @param rotation
	 * @return
	 */
	public static Matrix4rad convert(Matrix4f rotation) {
		Matrix4rad res = new Matrix4rad();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				res.set(i, j, convert(rotation.get(i, j)));
			}
		}

		return (res);
	}

	public static Matrix4f convert(Matrix4rad rotation) {
		Matrix4f res = new Matrix4f();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				res.set(i, j, rotation.get(i, j, tmp).floatValue());
			}
		}

		return (res);
	}

	public static Matrix4f convert(Matrix4rad rotation, Matrix4f passback) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				passback.set(i, j, rotation.get(i, j, tmp).floatValue());
			}
		}

		return (passback);
	}

	public static Matrix3rad convert(Matrix3f matrix3f) {
		Matrix3rad res = new Matrix3rad();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				res.set(i, j, convert(matrix3f.get(i, j)));
			}
		}

		return (res);
	}

	public static Radical1 convert(float val) {
		float e = 0.0001f;

		Radical1 result = null;
		for (Radical1 appriximator : approximators) {
			if (FastMath.epsilonEquals(val, appriximator.floatValue(), e)) {
				result = appriximator.clone();
			}
		}
		if (result == null)
			throw new RuntimeException("for " + val);

		assert FastMath.epsilonEquals(result.floatValue(), val, e) : val;

		return (result);
	}

	/**
	 * this is a useful IO class. It takes a set of matrix radicals, and uses extensive
	 * sharing of rational and radical1 instances to produce an equivelent represenentaions
	 * which uses dramatically less numbers of individual objects (so this can be saved alot faster)
	 * However, note the resultant set becomes read only though
	 * @param data
	 * @return
	 */
	public static HashSet<Matrix4rad> removeRedundantRadicals(Set<Matrix4rad> data) {
		HashMap<Radical1, Radical1> radicalInstances = new HashMap<Radical1, Radical1>();
		HashMap<Rational, Rational> rationalInstances = new HashMap<Rational, Rational>();
		HashMap<Integer, Integer> integerInstances = new HashMap<Integer, Integer>();

		HashSet<Matrix4rad> result = new HashSet<Matrix4rad>();

		Radical1 tmp = new Radical1();

		for (Matrix4rad rad : data) {
			Matrix4rad newMatrix4rad = rad.clone();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					newMatrix4rad.setReference(i, j, getCachedRational1(rad.get(i, j, tmp), radicalInstances, rationalInstances, integerInstances));
				}
			}
			result.add(newMatrix4rad);
			assert newMatrix4rad.equals(rad);
			assert newMatrix4rad.hashCode() == rad.hashCode();
		}

		return (result);
	}

	private static Radical1 getCachedRational1(Radical1 value, HashMap<Radical1, Radical1> radicalCache, HashMap<Rational, Rational> rationalCache, HashMap<Integer, Integer> integerCache) {
		Radical1 ref = radicalCache.get(value);

		if (ref == null) {
			ref = new Radical1(value);
			radicalCache.put(ref, ref);

			// remove redundant rational objects here as well
			HashMap<Integer, Rational> newRadicandSet = new HashMap<Integer, Rational>();

			for (Map.Entry<Integer, Rational> entry : ref.radicands.entrySet()) {
				newRadicandSet.put(getCachedInteger(entry.getKey(), integerCache), getCachedRational(entry.getValue(), rationalCache));
			}
			ref.radicands = newRadicandSet;
		}

		return (ref);
	}

	private static Rational getCachedRational(Rational rational, HashMap<Rational, Rational> rationalCache) {
		Rational ref = rationalCache.get(rational);
		if (ref == null) {
			rationalCache.put(rational, rational);
			ref = rational;
		}

		return (ref);
	}

	private static Integer getCachedInteger(Integer key, HashMap<Integer, Integer> integerCache) {
		Integer ref = integerCache.get(key);
		if (ref == null) {
			integerCache.put(key, key);
			ref = key;
		}

		return (ref);
	}
}
