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
package org.openmali.angle;

import java.util.Random;

import org.openmali.FastMath;
import org.openmali.test.Test;
import org.openmali.vecmath2.Tuple2f;
import org.openmali.vecmath2.Vector2f;

/**
 * Class to compute angle vectors makes with the origin and vectors
 * from angle and optionally length.
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public abstract class Angle {

	/**
	 * @return The angle that the vector makes with the origin (0, 1)
	 */
	public static float angle(float x, float y) {

		return FastMath.toDeg(FastMath.atan2(x, y));

	}

	/**
	 * @return The angle that the vector makes with the origin (0, 1)
	 */
	public static float angle(Tuple2f t) {

		return FastMath.toDeg(FastMath.atan2(t.getX(), t.getY()));

	}

	/**
	 * @return A vector which makes a certain angle with the origin (0, 1)
	 */
	public static Vector2f vec(float angle) {

		Vector2f vec = new Vector2f(FastMath.sin(FastMath.toRad(angle)), FastMath.cos(FastMath.toRad(angle)));

		return vec;

	}

	/**
	 * @return A vector which makes a certain angle with the origin (0, 1)
	 */
	public static Vector2f vec(float angle, float length) {

		Vector2f vec = new Vector2f(FastMath.sin(FastMath.toRad(angle)), FastMath.cos(FastMath.toRad(angle)));
		vec.scale(length);

		return vec;

	}

	/**
	 * Main method : test
	 */
	public static void main(String[] argv) {

		Random ran = new Random(1846506);

		Vector2f vec = new Vector2f(ran.nextFloat(), ran.nextFloat());
		vec.normalize();
		float angle = angle(vec);
		System.out.println("Angle with vec " + vec + " = " + angle);

		Vector2f vec2 = vec(angle);
		System.out.println("Vec with angle " + angle + " = " + vec2);

		vec.round(3);
		vec2.round(3);
		if (vec.equals(vec2)) {
			Test.passed(1);
		} else {
			Test.failed(1);
		}

		float len = ran.nextFloat();
		Vector2f vec3 = vec(angle, len);
		float len2 = vec3.length();
		System.out.println("Double-scaled vec = " + angle + " = " + vec3 + " of length " + len2 + " should be " + len);

		len = NumberUtil.round(len, 3);
		len2 = NumberUtil.round(len2, 3);
		if (len2 == len) {
			Test.passed(2);
		} else {
			Test.failed(2);
		}

	}

}
