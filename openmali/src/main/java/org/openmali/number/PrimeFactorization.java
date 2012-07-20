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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tom Larkworthy
 */
public class PrimeFactorization {
	/**
	 * precomputed values
	 */
	private static List<List<Integer>> precomutedFactorization = new ArrayList<List<Integer>>();

	static {
		precomutedFactorization.add(null);// for the zero entry
		for (int i = 1; i < 1000; i++) {
			precomutedFactorization.add(getPrimeFactorization(i, new ArrayList<Integer>()));
		}
	}

	/**
	 * returns the prime factors for the argument. The resultant list is in order, smallest primes first.
	 * passback should be emptied before calling this method
	 * e.g. factorization of 12 is 2*2*3
	 * e.g. factorization of 1 is 1 (special case, one not normally returned)
	 */
	public static List<Integer> getPrimeFactorization(int integer, List<Integer> passback) {
		assert integer > 0;
		if (integer == 1) {
			passback.add(1);
			return (passback);
		}

		if (precomutedFactorization.size() < integer) {
			passback.addAll(precomutedFactorization.get(integer));
			return (passback);
		}

		recursiveFactor(integer, passback);

		Collections.sort(passback);

		return (passback);
	}

	private static void recursiveFactor(int integer, List<Integer> passback) {
		if (integer < precomutedFactorization.size()) {
			passback.addAll(precomutedFactorization.get(integer));
			return;
		}

		int smallFactor = 1;
		int largeFactor = integer;
		for (int i = 2; i <= Math.sqrt(integer); i++) {
			if (integer % i == 0) {// no remainder on division, we found a
									// factor, note i = 1 would find this too
				smallFactor = i;
				largeFactor = integer / i;
			}
		}

		if (largeFactor == integer) {
			passback.add(largeFactor);
		} else {

			recursiveFactor(largeFactor, passback);
			recursiveFactor(smallFactor, passback);
		}
	}

	public static boolean isPrime(int number) {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		getPrimeFactorization(number, tmp);
		if (tmp.size() == 1)
			return (true);

		return (false);
	}

	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(2);
		list.add(3);

		ArrayList<Integer> tst = new ArrayList<Integer>();
		getPrimeFactorization(12, tst);
		System.out.println("tst = " + tst);
		assert tst.equals(list);

		for (int i = 1; i < 20; i++) {
			if (isPrime(i)) {
				System.out.println(i + " is prime");
			} else {
				System.out.println(i + " is not prime");
			}
		}

		for (int i = 1; i < 20; i++) {
			tst.clear();
			System.out.println(i + " = " + getPrimeFactorization(i, tst));
		}
	}
}
