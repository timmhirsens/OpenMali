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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openmali.FastMath;

/**
 * Represents a simple subset of radicals, namely, a sum of rational*sqrt(rational). e.g. 4 + sqrt(3) + 5*sqrt(2)
 * The class was designed for use with rotation matrices, where triginomic constants like sin(pi/2) can be represented in
 * simple radical forms.
 * 
 * @author Tom Larkworthy
 */
public class Radical1 implements Serializable {
	private static final long serialVersionUID = 6504477689753898414L;

	// map be between radicands (value) inside the sqrt sign)
	// and the multiplier for that term (bit outside the sqrt)
	// so the index is the radicand. The value is the multiplier
	// the index will always be 1 or over
	public HashMap<Integer, Rational> radicands = new HashMap<Integer, Rational>();

	public static final Rational upperTmp = new Rational();
	public static final Rational lowerTmp = new Rational();
	public static final Rational upperTmp2 = new Rational();
	public static final Rational lowerTmp2 = new Rational();
	public static Radical1 ONE = new Radical1(1, 1, 1);
	public static Radical1 ZERO = new Radical1(0, 1, 1);
	public static Object MINUS_ONE = new Radical1(-1, 1, 1);

	public final void setZero() {
		radicands.clear();
	}

	public final void set(Radical1 value) {
		radicands.clear();
		for (Integer radicand : value.radicands.keySet()) {
			radicands.put(radicand, value.radicands.get(radicand).clone());
		}
	}

	public final void get(Radical1 passback) {
		passback.set(this);
	}

	public final float floatValue() {
		float val = 0;
		for (Integer radicand : radicands.keySet()) {
			float multiplier = radicands.get(radicand).floatValue();
			float radicandValue = FastMath.sqrt(radicand.floatValue());
			val += multiplier * radicandValue;
		}

		return (val);
	}

	public final Radical1 negate(Radical1 passback) {
		sub(new Radical1(), this, passback);

		return (passback);
	}

	public Radical1 negate() {
		for (Rational rational : radicands.values()) {
			rational.negate();
		}

		return (this);
	}

	public void absolute(Radical1 passback) {
		if (floatValue() <= 0) {
			// TODO: do analytically?
			negate(passback);
		} else {
			passback.set(this);
		}
	}

	public Radical1 mul(Rational factor, Radical1 passback) {
		HashMap<Integer, Rational> result = new HashMap<Integer, Rational>();

		for (Integer radicand : radicands.keySet()) {
			Rational multiplier = radicands.get(radicand);

			result.put(radicand, Rational.mul(multiplier, factor, new Rational()));
		}
		passback.radicands.clear();
		passback.radicands.putAll(result);

		return (passback);
	}

	/**
	 * A radical is comprised of several terms e.g. this radical has 3 terms: 4 + 3*sqrt(2) + 2/3 * sqrt(3).
	 * Term 1 is 4 * sqrt(1), term 2 is 3*sqrt(2), term 3 is 2/3 * sqrt(3).
	 * The radicand terms are 1, 2 and 3 respectively. The multipliers are 4, 3 and 2/3 respectively.
	 * In this class of radicands, multipliers can be rational and radicands are always posative integers.
	 * This method allows just one term to be set (note this will overite the value of anyother multiplier with the
	 * same radicand VALUE)
	 */
	public void setTerm(Rational multiplier, Integer radicand) {
		// the radicand needs to be simplified
		// e.g. sqrt(24) should become 2*sqrt(6)
		// we work this out by taking the prime factorization, and noting the
		// presence of square numbers, denoted by pairs of primes
		// so sqrt(24) is sqrt(2*2*2*3)
		// the pair of 2's make a square, so these are removed and moved to the
		// outside
		// =2*sqrt(2*3)

		int inside;
		int outside;
		if (!PrimeFactorization.isPrime(radicand)) {
			outside = 1;
			inside = 1;
			List<Integer> primes = PrimeFactorization.getPrimeFactorization(radicand, new ArrayList<Integer>());

			int previousPrime = -1;
			for (Integer prime : primes) {
				if (prime == previousPrime) {
					outside *= prime;
					inside /= prime;
					previousPrime = -1;// now forget the previous, we don't want
										// 2*2*2 being simplified twice
				} else {
					inside *= prime;
					previousPrime = prime;
				}
			}
		} else {
			inside = radicand;
			outside = 1;
		}

		multiplier = Rational.mul(multiplier, new Rational(outside), new Rational());
		radicand = inside;

		if (!multiplier.equals(Rational.ZERO))
			radicands.put(radicand, multiplier);
	}

	/**
	 * A radical is comprised of several terms e.g. this radical has 3 terms: 4 + 3*sqrt(2) + 2/3 * sqrt(3).
	 * Term 1 is 4 * sqrt(1), term 2 is 3*sqrt(2), term 3 is 2/3 * sqrt(3).
	 * The radicand terms are 1, 2 and 3 respectively. The multipliers are 4, 3 and 2/3 respectively.
	 * In this class of radicands, multipliers can be rational numbers as can the radicands.
	 * This method allows a rational radical argument such as sqrt(1/3), however the denominator is rationalized
	 * for storage, so sqrt(1/3) = 1/3 * sqrt(3)
	 */
	public void setTerm(Rational multiplier, Rational radicand) {
		// multiply the radicand by the the root of the denominator, which ends
		// up removing the denominator from the
		// radicand, but moving it to the outside

		Rational resultantRadicand = Rational.mul(radicand, new Rational(radicand.getDenominator() * radicand.getDenominator(), 1), new Rational());
		assert resultantRadicand.getDenominator() == 1;

		int numerator = resultantRadicand.getNumerator() + resultantRadicand.getWhole();

		Rational resultantMultiplier = Rational.mul(multiplier, new Rational(1, radicand.getDenominator()), new Rational());

		setTerm(resultantMultiplier, numerator);
	}

	/**
	 * A radical is comprised of several terms e.g. this radical has 3 terms: 4 + 3*sqrt(2) + 2/3 * sqrt(3).
	 * Term 1 is 4 * sqrt(1), term 2 is 3*sqrt(2), term 3 is 2/3 * sqrt(3).
	 * The radicand terms are 1, 2 and 3 respectively. The multipliers are 4, 3 and 2/3 respectively.
	 * In this class of radicands, multipliers can be rational and radicands are always posative integers.
	 * This method allows just a single term to be added to the current value, not the actual values will
	 * be intergrated into the expression if possible.
	 */
	public void addTerm(Rational multiplier, Integer radicand) {
		// the radicand needs to be simplified
		// e.g. sqrt(24) should become 2*sqrt(6)
		// we work this out by taking the prime factorization, and noting the
		// presence of square numbers, denoted by pairs of primes
		// so sqrt(24) is sqrt(2*2*2*3)
		// the pair of 2's make a square, so these are removed and moved to the
		// outside
		// =2*sqrt(2*3)

		int inside;
		int outside;
		if (!PrimeFactorization.isPrime(radicand)) {
			outside = 1;
			inside = 1;
			List<Integer> primes = PrimeFactorization.getPrimeFactorization(radicand, new ArrayList<Integer>());

			int previousPrime = -1;
			for (Integer prime : primes) {
				if (prime == previousPrime) {
					outside *= prime;
					inside /= prime;
					previousPrime = -1;// now forget the previous, we don't want
										// 2*2*2 being simplified twice
				} else {
					inside *= prime;
					previousPrime = prime;
				}
			}
		} else {
			inside = radicand;
			outside = 1;
		}

		multiplier = Rational.mul(multiplier, new Rational(outside), new Rational());
		radicand = inside;

		Rational previousMultiplier = radicands.get(radicand);

		if (previousMultiplier == null) {
			if (!multiplier.equals(Rational.ZERO))
				radicands.put(radicand, multiplier);
		} else {
			Rational.add(previousMultiplier, multiplier, previousMultiplier);

			if (previousMultiplier.equals(Rational.ZERO))
				radicands.remove(radicand);
		}
	}

	/**
	 * Retreives reasonably tight rational bounds.
	 * 
	 * @param upperPassback
	 * @param lowerPassback
	 */
	public void getBounds(Rational upperPassback, Rational lowerPassback) {
		upperPassback.set(0, 0, 1);
		lowerPassback.set(0, 0, 1);

		for (Integer radicand : radicands.keySet()) {
			RadicandBounds.getBounds(radicand, upperTmp, lowerTmp);

			Rational.mul(upperTmp, radicands.get(radicand), upperTmp);
			Rational.mul(lowerTmp, radicands.get(radicand), lowerTmp);

			Rational.add(upperPassback, upperTmp, upperPassback);
			Rational.add(lowerPassback, lowerTmp, lowerPassback);
		}
	}

	/**
	 * result = a * b;
	 * can be performed in place, i.e. result can be the same argument as a parameter
	 */
	public static Radical1 add(Radical1 a, Radical1 b, Radical1 result) {
		HashMap<Integer, Rational> radicands = new HashMap<Integer, Rational>();

		HashSet<Integer> totalRadicands = new HashSet<Integer>();
		totalRadicands.addAll(a.radicands.keySet());
		totalRadicands.addAll(b.radicands.keySet());

		for (Integer radicand : totalRadicands) {
			Rational multiplierA = a.radicands.get(radicand);
			Rational multiplierB = b.radicands.get(radicand);

			Rational resultMultiplier;

			// we try to anly add terms if we really have to becuase its slow
			if (multiplierA != null && multiplierB != null) {
				// the one case where a genuine add is needed
				resultMultiplier = Rational.add(multiplierA, multiplierB, new Rational());
			} else if (multiplierA != null) {
				resultMultiplier = new Rational(multiplierA);
			} else {
				assert multiplierB != null;
				resultMultiplier = new Rational(multiplierB);
			}

			if (!resultMultiplier.equals(Rational.ZERO))
				radicands.put(radicand, resultMultiplier);
		}
		result.radicands.clear();
		result.radicands.putAll(radicands);

		return (result);
	}

	/**
	 * result = a * b;
	 * can be performed in place, i.e. result can be the same argument as a parameter
	 */
	public static Radical1 sub(Radical1 a, Radical1 b, Radical1 result) {
		HashMap<Integer, Rational> radicands = new HashMap<Integer, Rational>();

		HashSet<Integer> totalRadicands = new HashSet<Integer>();
		totalRadicands.addAll(a.radicands.keySet());
		totalRadicands.addAll(b.radicands.keySet());

		for (Integer radicand : totalRadicands) {
			Rational multiplierA = a.radicands.get(radicand);
			Rational multiplierB = b.radicands.get(radicand);

			Rational resultMultiplier = new Rational();
			if (multiplierA != null)
				Rational.add(multiplierA, resultMultiplier, resultMultiplier);
			if (multiplierB != null)
				Rational.sub(resultMultiplier, multiplierB, resultMultiplier);

			if (!resultMultiplier.equals(Rational.ZERO))
				radicands.put(radicand, resultMultiplier);
		}
		result.radicands.clear();
		result.radicands.putAll(radicands);

		return (result);
	}

	/**
	 * result = a * b in radicand math. Can be performed in place i.e. a or b can be the same object as the result
	 * without problems
	 */
	public static Radical1 mul(Radical1 a, Radical1 b, Radical1 result) {

		Radical1 tmp = new Radical1();

		// like multiplying out brackets, every term in a is multiplied by every
		// term in b
		for (Integer radicandA : a.radicands.keySet()) {
			for (Integer radicandB : b.radicands.keySet()) {
				Rational multiplierA = a.radicands.get(radicandA);
				Rational multiplierB = b.radicands.get(radicandB);

				Rational resultantMultiplier = Rational.mul(multiplierA, multiplierB, new Rational());
				int resultantRadicand = radicandA * radicandB;

				tmp.addTerm(resultantMultiplier, resultantRadicand);

			}
		}

		Iterator<Integer> radicandIter = tmp.radicands.keySet().iterator();
		while (radicandIter.hasNext()) {
			Integer radicand = (Integer) radicandIter.next();
			if (tmp.radicands.get(radicand).equals(Rational.ZERO)) {
				tmp.radicands.get(radicand).equals(Rational.ZERO);
				radicandIter.remove();
			}
		}

		result.radicands.clear();
		result.radicands.putAll(tmp.radicands);

		return (result);
	}

	/**
	 * This performs rad % rational in closed form ... HOWEVER,
	 * this might actually fail to compute, if bounds cannot be found tight enough
	 * 
	 * This implementation of a mod always returns 0 < ans < mod
	 * @param rad
	 * @param mod
	 * @param passback
	 * @throws ArithmeticException if tight bounds can't be found
	 */
	public static Radical1 mod(Radical1 rad, Rational mod, Radical1 passback) {
		Radical1 modRad = new Radical1(mod);

		rad.getBounds(upperTmp2, lowerTmp2);

		passback.set(rad);

		while (upperTmp2.compareTo(mod) >= 0 && lowerTmp2.compareTo(mod) >= 0) {
			sub(passback, modRad, passback);
			Rational.sub(upperTmp2, mod, upperTmp2);
			Rational.sub(lowerTmp2, mod, lowerTmp2);
		}

		while (upperTmp2.compareTo(Rational.ZERO) < 0 && lowerTmp2.compareTo(Rational.ZERO) < 0) {
			add(passback, modRad, passback);
			Rational.add(upperTmp2, mod, upperTmp2);
			Rational.add(lowerTmp2, mod, lowerTmp2);
		}

		if (upperTmp2.compareTo(mod) != lowerTmp2.compareTo(mod)) {
			throw new ArithmeticException("Cannot find bounds tight enough to perform provably correct mod operation");
		}

		return (passback);
	}

	/**
	 * compativle with the parse implementation (see Parser and parseRadicand1)
	 * 
	 * @return a String representation
	 */
	@Override
	public String toString() {
		if (radicands.size() == 0)
			return "0";

		StringBuilder buf = new StringBuilder();

		for (Integer radicand : radicands.keySet()) {
			buf.append("+");
			if (!radicands.get(radicand).equals(Rational.ONE))
				buf.append(radicands.get(radicand)).append("*");

			if (radicand == 1) {
				if (!radicands.get(radicand).equals(Rational.ONE)) {
					buf.replace(buf.length() - 1, buf.length(), "");
				} else {
					buf.append("1");
				}
			} else {
				buf.append("\u221A").append(radicand);
			}
		}

		buf.replace(0, 1, ""); // remove first +

		return (buf.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return (true);
		if ((o == null) || (getClass() != o.getClass()))
			return (false);

		Radical1 radical1 = (Radical1) o;

		if (!radicands.equals(radical1.radicands))
			return (false);

		return (true);
	}

	public static final boolean equals(Radical1 a, Radical1 b) {
		return (a.equals(b));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return (radicands.hashCode());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Radical1 clone() {
		Radical1 rad = new Radical1();
		rad.set(this);

		return (rad);
	}

	public Radical1(Radical1 value) {
		this.set(value);
	}

	public Radical1() {
	}

	/**
	 * helper to instanciate integer values (sqrt(1) * x/1 terms)
	 * 
	 * @param value
	 */
	public Radical1(int value) {
		this.setTerm(new Rational(value, 1), 1);
	}

	/**
	 * helper term to instanciate radicals with no sqrt terms (or sqrt(1) terms)
	 * 
	 * @param rational
	 */
	public Radical1(Rational rational) {
		this.setTerm(rational, 1);
	}

	/**
	 * helper term to instanciate radicals with just one term
	 * 
	 * @param rational
	 */
	public Radical1(Rational rational, int radical) {
		this.addTerm(rational, radical);
	}

	/**
	 * helper term to instanciate radicals with just one term
	 */
	public Radical1(int nom, int den, int radical) {
		this.addTerm(new Rational(nom, den), radical);
	}

	public static Radical1 parseRadical1(String string) {
		StringReader in = new StringReader(string);
		Radical1 ret;
		try {
			ret = new Parser(in).radicand1();
			in.close();

			return (ret);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (null);
	}

	/*
	 * public static void main( String[] args ) { Radical1 sqrt2 = new
	 * Radical1(); sqrt2.setTerm( new Rational( 1 ), new Rational( 2 ) );
	 * System.out.println( "sqrt2 = " + sqrt2 );
	 * 
	 * assert sqrt2.floatValue() == FastMath.sqrt( 2 );
	 * 
	 * { Radical1 simplify1 = new Radical1();
	 * 
	 * simplify1.setTerm( new Rational( 1 ), new Rational( 1, 3 ) ); //1/sqrt(3)
	 * System.out.println( "1/sqrt(3) = " + simplify1 ); //shoudl simplify to
	 * 1/3 * sqrt(3) }
	 * 
	 * { Radical1 simplify1 = new Radical1();
	 * 
	 * simplify1.setTerm( new Rational( 7 ), new Rational( 1, 11 ) );
	 * //1/sqrt(3) System.out.println( "7/sqrt(11) = " + simplify1 ); //shoudl
	 * simplify to 7/11 * sqrt(11) }
	 * 
	 * { Radical1 simplify1 = new Radical1();
	 * 
	 * simplify1.setTerm( new Rational( 1 ), new Rational( 24 ) ); //1/sqrt(3)
	 * System.out.println( "sqrt(24) = " + simplify1 ); //should simplify to
	 * 2*sqrt(6) }
	 * 
	 * Radical1 two = Radical1.mul( sqrt2, sqrt2, new Radical1() );
	 * 
	 * System.out.println( "sqrt(2)*sqrt(2) = " + two + " = " + two.floatValue()
	 * ); }
	 */
}
