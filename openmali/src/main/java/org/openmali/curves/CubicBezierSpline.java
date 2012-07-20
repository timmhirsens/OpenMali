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

import static org.openmali.FastMath.pow2;
import static org.openmali.FastMath.pow3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple2f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;

/**
 * A cubic Bezier Curve (degree 3)
 * 
 * @author Mathias Henze (aka cylab)
 */
public class CubicBezierSpline {
	private static class SUMapEntry {
		public float s;
		public int subcurve;
		public final float u;

		public SUMapEntry(float s, int subcurve, float u) {
			this.s = s;
			this.subcurve = subcurve;
			this.u = u;
		}
	}

	private final float defaultErrorTolerance = 0.0001f;
	private final LinkedList<Point3f> points = new LinkedList<Point3f>();
	private final ArrayList<SUMapEntry> suMapping;
	private Point3f[] basePoints;
	private Point3f[] controlPoints;
	private float length;

	public float getLength() {
		return (length);
	}

	/**
	 * @return the array of base-points for this Bézier Spline.
	 */
	public final Point3f[] getBasePoints() {
		return (basePoints);
	}

	/**
	 * @return the n-th base-point for this Bézier Spline.
	 */
	public Point3f getBasePoint(int n) {
		return (basePoints[n]);
	}

	/**
	 * Adds a new Point into the nearest spline segment.
	 * The control points will be generated automatically.
	 * 
	 * @param point the coordinates of the new point
	 */
	public void addBasePoint(Tuple3f point) {
		addBasePoint(closestSubcurve(point) + 1, point);
	}

	/**
	 * Adds a new Point at the specified index and shifts the already exiting points one index further.
	 * The control points will be generated automatically.
	 * 
	 * @param index the index to add the point
	 * @param point the coordinates of the new point
	 */
	public void addBasePoint(int index, Tuple3f point) {
		if (index < 0 || index > basePoints.length)
			throw new ArrayIndexOutOfBoundsException("The index " + index + " is out of 0 < index < " + basePoints.length + "!");
		if (point == null)
			throw new IllegalArgumentException("Parameter point may not be null!");

		Point3f[] tmp = new Point3f[basePoints.length + 1];

		if (index > 0)
			System.arraycopy(basePoints, 0, tmp, 0, index);

		if (index < basePoints.length)
			System.arraycopy(basePoints, index, tmp, index + 1, basePoints.length - index);

		tmp[index] = new Point3f(point);
		basePoints = tmp;

		tmp = new Point3f[(basePoints.length - 1) * 2];

		if (index > 0)
			System.arraycopy(controlPoints, 0, tmp, 0, index * 2 - 1);

		if (index < basePoints.length)
			System.arraycopy(controlPoints, index * 2 - 1, tmp, index * 2 + 1, controlPoints.length - index * 2 + 1);

		controlPoints = tmp;
		generateControlPoints(index, index + 1);
	}

	/**
	 * @return the array of base-points for this Bézier Spline.
	 */
	public final Point3f[] getControlPoints() {
		return (controlPoints);
	}

	/**
	 * @return the n-th control-point for this Bézier Spline.
	 */
	public Point3f getControlPoint(int n) {
		return (controlPoints[n]);
	}

	public List<Point3f> getPoints() {
		return (points);
	}

	public static <P extends Tuple3f> P evalPoint(Tuple3f p0, Tuple3f p1, Tuple3f p2, Tuple3f p3, float t, P p) {
		p.setX(pow3(1 - t) * p0.getX() + 3 * t * pow2(1 - t) * p1.getX() + 3 * pow2(t) * (1 - t) * p2.getX() + pow3(t) * p3.getX());
		p.setY(pow3(1 - t) * p0.getY() + 3 * t * pow2(1 - t) * p1.getY() + 3 * pow2(t) * (1 - t) * p2.getY() + pow3(t) * p3.getY());
		p.setZ(pow3(1 - t) * p0.getZ() + 3 * t * pow2(1 - t) * p1.getZ() + 3 * pow2(t) * (1 - t) * p2.getZ() + pow3(t) * p3.getZ());

		return (p);
	}

	public static Point3f evalPoint(Tuple3f p0, Tuple3f p1, Tuple3f p2, Tuple3f p3, float t) {
		return (evalPoint(p0, p1, p2, p3, t, new Point3f()));
	}

	/**
	 * @param result result.length Point3fs from the Bézier Curve
	 */
	public static Tuple3f[] evalPoints(Tuple3f p0, Tuple3f p1, Tuple3f p2, Tuple3f p3, Tuple3f[] result) {
		for (int i = 0; i < result.length; i++) {
			if (result[i] == null)
				result[i] = new Point3f();

			evalPoint(p0, p1, p2, p3, i / (float) (result.length - 1), result[i]);
		}

		return (result);
	}

	/**
	 * @param result result.length Point3fs from the Bézier Curve
	 */
	public static final Point3f[] evalPoints(Tuple3f p0, Tuple3f p1, Tuple3f p2, Tuple3f p3, Point3f[] result) {
		return ((Point3f[]) evalPoints(p0, p1, p2, p3, (Tuple3f[]) result));
	}

	/**
	 * @param numPoints
	 * @return numPoints * Point3f  from the Bézier curve
	 */
	public static Point3f[] evalPoints(Tuple3f p0, Tuple3f p1, Tuple3f p2, Tuple3f p3, int numPoints) {
		return (evalPoints(p0, p1, p2, p3, new Point3f[numPoints]));
	}

	/**
	 * @param s with 0 <= s <= 1
	 * @param p the point output object to write the calculated point on the Bézier Spline to
	 */
	public <P extends Tuple3f> P samplePoint(float s, P p) {
		if (s < 0f)
			s = 0f;
		if (s > 1f)
			s = 1f;

		int subcurve = 0;
		float u = 0;

		reparameterization: {
			SUMapEntry min = suMapping.get(0);
			SUMapEntry max;
			for (int i = 1; i < suMapping.size(); i++) {
				max = suMapping.get(i);

				if ((max.s > s) && (s >= min.s)) {
					subcurve = min.subcurve;
					// interpolating the parameter
					u = ((max.s - s) / (max.s - min.s)) * min.u + ((s - min.s) / (max.s - min.s)) * max.u;

					break reparameterization;
				}

				min = max;
			}

			// fallback
			subcurve = basePoints.length - 2;
			u = 1;
		}

		P result = evalPoint(basePoints[subcurve], controlPoints[subcurve * 2], controlPoints[subcurve * 2 + 1], basePoints[subcurve + 1], u, p);

		return (result);
	}

	/**
	 * @param t with 0 < t < 1
	 * @return one point on the Bézier Spline
	 */
	public final Point3f samplePoint(float t) {
		return (samplePoint(t, new Point3f()));
	}

	/**
	 * @param result result.length Point3fs from the Bézier Curve
	 */
	public Tuple3f[] samplePoints(Tuple3f[] result) {
		for (int i = 0; i < result.length; i++) {
			if (result[i] == null)
				result[i] = new Point3f();

			samplePoint(i / (float) (result.length - 1), result[i]);
		}

		return (result);
	}

	/**
	 * @param result result.length Point3fs from the Bézier Curve
	 */
	public final Point3f[] samplePoints(Point3f[] result) {
		return ((Point3f[]) samplePoints((Tuple3f[]) result));
	}

	/**
	 * @param numPoints
	 * @return numPoints * Point3f  from the Bézier curve
	 */
	public Point3f[] samplePoints(int numPoints) {
		final Point3f[] points = new Point3f[numPoints];

		samplePoints(points);

		return (points);
	}

	public void update() {
		final int subcurves = basePoints.length - 1;

		points.clear();
		length = 0;

		for (int i = 0; i < subcurves; i++) {
			points.add(basePoints[i]);
			points.add(controlPoints[i * 2]);
			points.add(controlPoints[i * 2 + 1]);
		}

		points.add(basePoints[subcurves]);

		ListIterator<Point3f> iter = points.listIterator();
		suMapping.clear();
		float[] runlength = new float[] { 0 };
		for (int i = 0; i < subcurves; i++) {
			subdivideLength(i, 0, 1, iter, suMapping, runlength, defaultErrorTolerance);
		}

		length = runlength[0];
		// normalize the s parameter
		for (int i = 0; i < suMapping.size(); i++) {
			suMapping.get(i).s /= length;
		}
	}

	/**
	 * Generates control points for the given base point range.
	 * 
	 * The range is given by indices from start (inclusive) to end (exclusive).
	 * 
	 * @param start the first basePoint to generate control points for
	 * @param end the first basePoint _not_ to generate control points for
	 */
	public void generateControlPoints(int start, int end) {
		final int maxI = basePoints.length - 1;

		Tuple3f p0m1 = Tuple3f.fromPool();
		p0m1.set(basePoints[0]);
		p0m1.sub(basePoints[1]);
		p0m1.add(basePoints[0]);

		Tuple3f pnp1 = Tuple3f.fromPool();
		pnp1.set(basePoints[basePoints.length - 1]);
		pnp1.sub(basePoints[basePoints.length - 2]);
		pnp1.add(basePoints[basePoints.length - 1]);

		Tuple3f tmp = Tuple3f.fromPool();
		int j = (start - 1) * 2;
		Point3f cp = null;
		float tl = 1f / 5f; // tangent length factor

		for (int i = start - 1; i < end; i++) {
			Tuple3f pim1 = i < 1 ? p0m1 : basePoints[i - 1];
			Tuple3f pi = i < 0 ? p0m1 : basePoints[i];
			Tuple3f pip1 = i + 1 > maxI ? pnp1 : basePoints[i + 1];
			Tuple3f pip2 = i + 2 > maxI ? pnp1 : basePoints[i + 2];

			if (j >= 0 && i < maxI && i >= start) {
				cp = new Point3f(pip1);
				cp.sub(pim1);
				cp.mul(tl);
				cp.add(pi);
				controlPoints[j++] = cp;
			} else
				j++;

			if (j > 0 && i < maxI && i < end - 1) {
				cp = new Point3f(pi);
				cp.sub(pip2);
				cp.mul(tl);
				cp.add(pip1);
				controlPoints[j++] = cp;
			} else
				j++;
		}
		Tuple3f.toPool(p0m1);
		Tuple3f.toPool(pnp1);
		Tuple3f.toPool(tmp);
		update();
	}

	/**
	 * Computes the point on this spline that is closest to the given point.
	 * 
	 * Additionally it computes the subcurve the point is on, the spline-equation parameter
	 * and the squared distance between the two points
	 * 
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @param z Z coordinate of the point to test
	 * @param resultPoint will contain the point on this spline that is closest to the given point
	 * @param resultParams will contain the spline-equation parameter as x and the squared distance as y
	 * 
	 * @return the subcurve, the closest point lies on.
	 */
	public int closestPoint(float x, float y, float z, Tuple3f resultPoint, Tuple2f resultParams) {
		float closestDistance = Float.MAX_VALUE;
		float closestParameter = 0;
		int closestSubcurve = 0;
		float u, proj, v2, d;
		Vector3f p0 = Vector3f.fromPool();
		Vector3f p1 = Vector3f.fromPool();
		Vector3f p = Vector3f.fromPool();
		Vector3f v = Vector3f.fromPool();
		Vector3f w = Vector3f.fromPool();
		Vector3f tmp = Vector3f.fromPool();

		// cycle through the segments found by subdividing the spline
		for (int i = 0; i < suMapping.size() - 1; i++) {
			// set up a line segment
			SUMapEntry entry0 = suMapping.get(i);
			SUMapEntry entry1 = suMapping.get(i + 1);
			evalPoint(basePoints[entry0.subcurve], controlPoints[entry0.subcurve * 2], controlPoints[entry0.subcurve * 2 + 1], basePoints[entry0.subcurve + 1], entry0.u, p0);
			evalPoint(basePoints[entry1.subcurve], controlPoints[entry1.subcurve * 2], controlPoints[entry1.subcurve * 2 + 1], basePoints[entry1.subcurve + 1], entry1.u, p1);

			// we can use the line segment parameter "t" to determine if the
			// point is outside of the line segment or not
			// t= (w dot v)/(v dot v)
			v.set(p1).sub(p0);
			w.set(x, y, z).sub(p0);

			// if the numerator of this fraction is <= 0, then the point lies
			// "before" p0:
			proj = w.dot(v);
			if (proj <= 0) {
				// check, if we have a new closest distance
				d = w.lengthSquared();
				if (d < closestDistance) {
					if (resultPoint != null)
						resultPoint.set(p0);
					closestDistance = d;
					closestParameter = entry0.s;
					closestSubcurve = entry0.subcurve;
				}
			}
			// otherwise check the other end of the line segment
			else {
				// if the numerator is greater than the denominator, the point
				// lies "behind" p1
				v2 = v.dot(v);
				if (proj >= v2) {
					// check, if we have a new closest distance
					tmp.set(x, y, z).sub(p1);
					d = tmp.lengthSquared();
					if (d < closestDistance) {
						if (resultPoint != null)
							resultPoint.set(p1);
						closestDistance = d;
						closestParameter = entry1.s;
						closestSubcurve = entry0.subcurve;
					}
				}
				// if the numerator is less than the denominator but greater
				// than 0, the closest point lies on the line segment
				else {
					// compute the point on the spline and check, if we have a
					// new closest distance
					u = entry0.u + (proj / v2) * (entry1.u - entry0.u);
					evalPoint(basePoints[entry0.subcurve], controlPoints[entry0.subcurve * 2], controlPoints[entry0.subcurve * 2 + 1], basePoints[entry0.subcurve + 1], u, p);
					tmp.set(x, y, z).sub(p);
					d = tmp.lengthSquared();
					if (d < closestDistance) {
						if (resultPoint != null)
							resultPoint.set(p);
						closestDistance = d;
						closestParameter = entry0.s + (proj / v2) * (entry1.s - entry0.s);
						closestSubcurve = entry0.subcurve;
					}
				}
			}
		}
		Vector3f.toPool(tmp);
		Vector3f.toPool(w);
		Vector3f.toPool(v);
		Vector3f.toPool(p);
		Vector3f.toPool(p0);
		Vector3f.toPool(p1);
		if (resultParams != null) {
			resultParams.setX(closestParameter);
			resultParams.setY(closestDistance);
		}
		return closestSubcurve;
	}

	/**
	 * Computes the point on this spline that is closest to the given point.
	 * 
	 * Additionally it computes the subcurve the point is on, the spline-equation parameter
	 * and the squared distance between the two points
	 * 
	 * @param point the point to test
	 * @param resultPoint will contain the point on this spline that is closest to the given point
	 * @param resultParams will contain the spline-equation parameter as x and the squared distance as y
	 * 
	 * @return the subcurve, the closest point lies on.
	 */
	public int closestPoint(Tuple3f point, Tuple3f resultPoint, Tuple2f resultParams) {
		return closestPoint(point.getX(), point.getY(), point.getZ(), resultPoint, resultParams);
	}

	/**
	 * Computes the point on this spline that is closest to the given point.
	 * 
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @param z Z coordinate of the point to test
	 * @param resultPoint will contain the point on this spline that is closest to the given point
	 * 
	 * @return the subcurve, the closest point lies on.
	 */
	public int closestPoint(float x, float y, float z, Tuple3f resultPoint) {
		return closestPoint(x, y, z, resultPoint, null);
	}

	/**
	 * Computes the point on this spline that is closest to the given point.
	 * 
	 * @param point the point to test
	 * @param resultPoint will contain the point on this spline that is closest to the given point
	 * 
	 * @return the subcurve, the closest point lies on.
	 */
	public int closestPoint(Tuple3f point, Tuple3f resultPoint) {
		return closestPoint(point.getX(), point.getY(), point.getZ(), resultPoint, null);
	}

	/**
	 * Finds the index of the subcurve with the point, that is closest to the given point.
	 * 
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @param z Z coordinate of the point to test
	 *
	 * @return the index of the line segment formed by basePoint[index] and basePoint[index+1]
	 */
	public int closestSubcurve(float x, float y, float z) {
		return closestPoint(x, y, z, null, null);
	}

	/**
	 * Finds the index of the subcurve with the point, that is closest to the given point.
	 * 
	 * @param point the point to test
	 * @return the index of the line segment formed by basePoint[index] and basePoint[index+1]
	 */
	public int closestSubcurve(Tuple3f point) {
		return closestPoint(point.getX(), point.getY(), point.getZ(), null, null);
	}

	/**
	 * Computes the paramter that would give the closest point on this spline to the given point.
	 * 
	 * @param x X coordinate of a point 
	 * @param y Y coordinate of a point 
	 * @param z Z coordinate of a point 
	 * @return the parameter that can be feed to samplePoint() to get the nearest point on this spline
	 */
	public float closestParameter(float x, float y, float z) {
		Tuple2f tmp = Tuple2f.fromPool();
		closestPoint(x, y, z, null, tmp);
		final float param = tmp.getX();
		Tuple2f.toPool(tmp);
		return param;
	}

	/**
	 * Computes the paramter that would give the closest point on this spline to the given point.
	 * 
	 * @param p the coordinates of a point 
	 * @return the parameter that can be feed to samplePoint() to get the nearest point on this spline
	 */
	public float closestParameter(Tuple3f p) {
		return closestParameter(p.getX(), p.getY(), p.getZ());
	}

	/**
	 * Computes the nearest distance between this spline and a point
	 * 
	 * @param x X coordinate of a point 
	 * @param y Y coordinate of a point 
	 * @param z Z coordinate of a point 
	 * 
	 * @return the distance between the point and the spline
	 */
	public float squaredDistanceTo(float x, float y, float z) {
		Tuple2f tmp = Tuple2f.fromPool();
		closestPoint(x, y, z, null, tmp);
		final float dist = tmp.getY();
		Tuple2f.toPool(tmp);
		return dist;
	}

	/**
	 * Computes the nearest distance between this spline and a point
	 * 
	 * @param p the coordinates of a point 
	 * @return the distance between the point and the spline
	 */
	public float squaredDistanceTo(Tuple3f p) {
		return squaredDistanceTo(p.getX(), p.getY(), p.getZ());
	}

	private void subdivideLength(int subcurve, float u1, float u2, ListIterator<Point3f> points, List<SUMapEntry> suMapping, float[] runlength, float errorTolerance) {
		// get the points from the list
		Point3f p0 = points.next();
		Point3f p1 = points.next();
		Point3f p2 = points.next();
		Point3f p3 = points.next();

		// The iterator needs to point to p3 for the next segment in the parent
		// recursion level
		points.previous();

		// remove the control points
		points.previous();
		points.remove();
		points.previous();
		points.remove();

		/*
		 * If the length of the path connecting the base and control points is
		 * near the length of a straight line between the endpoints, this bezier
		 * is nearly a straight line.
		 */
		float lineLenght = ((Vector3f) new Vector3f(p3).sub(p0)).length();
		float pathLength = ((Vector3f) new Vector3f(p1).sub(p0)).length() + ((Vector3f) new Vector3f(p2).sub(p1)).length() + ((Vector3f) new Vector3f(p3).sub(p2)).length();
		float avgLength = 0.5f * (pathLength + lineLenght);
		float dl = pathLength - lineLenght;

		// If the error is acceptable, add the average length to the runlength
		// and add an entry to the suMapping.
		if (dl * dl / avgLength < errorTolerance) {
			suMapping.add(new SUMapEntry(runlength[0], subcurve, u1));
			runlength[0] += avgLength;

			if (u2 == 1)
				suMapping.add(new SUMapEntry(runlength[0], subcurve, u2));

			return;
		}

		// if not, use de Casteljau's midpoint subdivision
		Point3f h = (Point3f) new Point3f(p1).add(p2).div(2);
		Point3f l1 = (Point3f) new Point3f(p0).add(p1).div(2);
		Point3f l2 = (Point3f) new Point3f(l1).add(h).div(2);
		Point3f r2 = (Point3f) new Point3f(p2).add(p3).div(2);
		Point3f r1 = (Point3f) new Point3f(h).add(r2).div(2);
		Point3f m = (Point3f) new Point3f(l2).add(r1).div(2);
		// Point3f l0 = p0;
		// Point3f r3 = p3;

		// Add the newly computed points to the points list.
		points.add(l1);
		points.add(l2);
		points.add(m);
		points.add(r1);
		points.add(r2);

		// Rewind the iterator.
		for (int i = 0; i < 6; i++)
			points.previous();

		// Check length and subdivide, if neccessary.
		subdivideLength(subcurve, u1, (u1 + u2) / 2, points, suMapping, runlength, errorTolerance); // l-points

		subdivideLength(subcurve, (u1 + u2) / 2, u2, points, suMapping, runlength, errorTolerance); // r-points

	}

	/**
	 * Constructs a Bézier Spline with composed of cubic Bézier curves.
	 * 
	 * @param points
	 */
	public CubicBezierSpline(Tuple3f[] points) {
		this(points, null);
	}

	/**
	 * Constructs a Bézier Spline with composed of cubic Bézier curves.
	 * 
	 * @param basePoints
	 * @param controlPoints
	 */
	public CubicBezierSpline(Tuple3f[] basePoints, Tuple3f[] controlPoints) {
		if ((basePoints == null) || (basePoints.length < 2)) {
			throw (new IllegalArgumentException("Argument 'basePoints' needs to be non null and at least of length 2"));
		}
		if ((controlPoints != null) && (controlPoints.length < (basePoints.length - 1) * 2)) {
			throw (new IllegalArgumentException("Argument 'controlPoints' needs to be non null and has to contain at least two control points per patch."));
		}

		this.basePoints = new Point3f[basePoints.length];
		this.controlPoints = new Point3f[(basePoints.length - 1) * 2];
		this.suMapping = new ArrayList<SUMapEntry>((basePoints.length - 1) * 16);

		for (int i = 0; i < basePoints.length; i++) {
			this.basePoints[i] = new Point3f(basePoints[i]);
		}

		if (controlPoints != null) {
			for (int i = 0; i < controlPoints.length; i++) {
				this.controlPoints[i] = new Point3f(controlPoints[i]);
			}
			update();
		} else {
			generateControlPoints(0, basePoints.length);
		}
	}
}
