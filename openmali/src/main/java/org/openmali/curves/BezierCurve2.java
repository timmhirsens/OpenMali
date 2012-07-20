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
package org.openmali.curves;

import org.openmali.FastMath;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple3f;

/**
 * A quadric Bezier Curve (degree 2)
 * B(t) = (1-t)^2*P0 + 2*t*(1-t)*P1 + t^2*P2	0 < t < 1
 * 
 * @author Ludovic Marce
 * @since 04 sept. 2007
 * <a href="http://en.wikipedia.org/wiki/Bezier_curve">
 */
public class BezierCurve2 {
	private final Point3f[] points;

	/**
	 * @return the array of base-points for this Bezier Curve.
	 */
	public final Point3f[] getBasePoints() {
		return (points);
	}

	/**
	 * @return the n-th base-point for this Bezier Curve.
	 */
	public final Point3f getBasePoint(int i) {
		return (points[i]);
	}

	/**
	 * @param i with 0 < t < 1
	 * @param p the point output object to write the calculated point on the Bezier Curve to
	 */
	public final <P extends Tuple3f> P getPoint(float i, P p) {
		if ((i < 0) || (i > 1)) {
			throw new IllegalArgumentException("i must be in range [0..1]");
		}

		p.setX(FastMath.pow2(1 - i) * points[0].getX() + (2 * i * (1 - i) * points[1].getX()) + FastMath.pow2(i) * points[2].getX());
		p.setY(FastMath.pow2(1 - i) * points[0].getY() + (2 * i * (1 - i) * points[1].getY()) + FastMath.pow2(i) * points[2].getY());
		p.setZ(FastMath.pow2(1 - i) * points[0].getZ() + (2 * i * (1 - i) * points[1].getZ()) + FastMath.pow2(i) * points[2].getZ());

		return (p);
	}

	/**
	 * @param i with 0 < t < 1
	 * @return one point on the Bezier curve
	 */
	public final Point3f getPoint(float i) {
		return (getPoint(i, new Point3f()));
	}

	/**
	 * @param result result.length Point3fs from the Bezier Curve
	 */
	public final Tuple3f[] getPoints(Tuple3f[] result) {
		for (int i = 0; i < result.length; i++) {
			if (result[i] == null)
				result[i] = new Point3f();

			getPoint(i / (float) (result.length - 1), result[i]);
		}

		return (result);
	}

	/**
	 * @param result result.length Point3fs from the Bezier Curve
	 */
	public final Point3f[] getPoints(Point3f[] result) {
		return ((Point3f[]) getPoints((Tuple3f[]) result));
	}

	/**
	 * @param numPoints
	 * @return numPoints * Point3f  from the Bezier curve
	 */
	public final Point3f[] getPoints(int numPoints) {
		return (getPoints(new Point3f[numPoints]));
	}

	public BezierCurve2(Tuple3f startPoint, Tuple3f midPoint, Tuple3f endPoint) {
		this.points = new Point3f[3];

		points[0] = new Point3f(startPoint);
		points[1] = new Point3f(midPoint);
		points[2] = new Point3f(endPoint);
	}

	public BezierCurve2(Point3f[] points) {
		this.points = new Point3f[3];

		System.arraycopy(points, 0, this.points, 0, 3);
	}
}
