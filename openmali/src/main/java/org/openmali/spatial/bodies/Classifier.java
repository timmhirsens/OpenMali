/**
 * Copyright (c) 2003-2007, Xith3D Project Group all rights reserved.
 * 
 * Portions based on the Java3D interface, Copyright by Sun Microsystems.
 * Many thanks to the developers of Java3D and Sun Microsystems for their
 * innovation and design.
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
 * Neither the name of the 'Xith3D Project Group' nor the names of its
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
package org.openmali.spatial.bodies;

import org.openmali.FastMath;
import org.openmali.spatial.bounds.Bounds;
import org.openmali.spatial.bounds.BoundsType;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.util.FloatUtils;

/**
 * Classifiable things can be classified with respect to each other.
 * 
 * @author cas
 * @author Marvin Froehlich (aka Qudus)
 */
public final class Classifier {
	public static enum Classification {
		INSIDE, OUTSIDE, SPANNING, ENCLOSING;

		public Classification add(Classification c) {
			switch (this) {
			case INSIDE: {
				if (c == OUTSIDE)
					return (SPANNING);
				else if (c == ENCLOSING)
					return (c);
				else
					return (this);
			}

			case OUTSIDE: {
				if (c == INSIDE)
					return (SPANNING);
				else if (c == ENCLOSING)
					return (c);
				else
					return (this);
			}

			case SPANNING: {
				if (c == ENCLOSING)
					return (c);

				return (this);
			}

			case ENCLOSING: {
				return (this);
			}
			}

			return (this);
		}

		public Classification invert() {
			switch (this) {
			case INSIDE: {
				return (ENCLOSING);
			}

			case OUTSIDE: {
				return (this);
			}

			case SPANNING: {
				return (this);
			}

			case ENCLOSING: {
				return (INSIDE);
			}
			}

			return (this);
		}
	}

	public static enum PlaneClassification {
		IN_FRONT_OF, BEHIND, SPANNING, COINCIDENT;

		public PlaneClassification add(PlaneClassification c) {
			switch (this) {
			case IN_FRONT_OF: {
				if ((c == BEHIND) || (c == SPANNING))
					return (SPANNING);

				return (this);
			}

			case BEHIND: {
				if ((c == IN_FRONT_OF) || (c == SPANNING))
					return (SPANNING);

				return (this);
			}

			case SPANNING: {
				return (SPANNING);
			}

			case COINCIDENT: {
				return (c);
			}
			}

			return (this);
		}
	}

	/**
	 * Classifies a Point against a Sphere.
	 * 
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param r
	 * @param px
	 * @param py
	 * @param pz
	 */
	public static Classification classifySpherePoint(float cx, float cy, float cz, float r, float px, float py, float pz) {
		float dx = cx - px;
		float dy = cy - py;
		float dz = cz - pz;

		if ((dx * dx) + (dy * dy) + (dz * dz) <= r * r)
			return (Classification.INSIDE);

		return (Classification.OUTSIDE);
	}

	/**
	 * Classifies a Point against a Sphere.
	 * 
	 * @param sphereC
	 * @param sphereR
	 * @param px
	 * @param py
	 * @param pz
	 */
	public static Classification classifySpherePoint(Tuple3f sphereC, float sphereR, float px, float py, float pz) {
		return (classifySpherePoint(sphereC.getX(), sphereC.getY(), sphereC.getZ(), sphereR, px, py, pz));
	}

	/**
	 * Classifies a Point against a Sphere.
	 * 
	 * @param sphereC
	 * @param sphereR
	 * @param point
	 */
	public static Classification classifySpherePoint(Tuple3f sphereC, float sphereR, Point3f point) {
		return (classifySpherePoint(sphereC, sphereR, point.getX(), point.getY(), point.getZ()));
	}

	/**
	 * Classifies a Point against a Sphere.
	 * 
	 * @param sphere
	 * @param point
	 * @return the classification
	 */
	public static Classification classifySpherePoint(Sphere sphere, Point3f point) {
		return (classifySpherePoint(sphere.centerX, sphere.centerY, sphere.centerZ, sphere.getMaxCenterDistance(), point.getX(), point.getY(), point.getZ()));
	}

	/**
	 * Classifies a Point against a Sphere.
	 * 
	 * @param sphere
	 * @param px
	 * @param py
	 * @param pz
	 * @return the classification
	 */
	public static Classification classifySpherePoint(Sphere sphere, float px, float py, float pz) {
		return (classifySpherePoint(sphere.centerX, sphere.centerY, sphere.centerZ, sphere.getMaxCenterDistance(), px, py, pz));
	}

	/**
	 * Classifies a Sphere against a Sphere.
	 * 
	 * @param cx1
	 * @param cy1
	 * @param cz1
	 * @param r1
	 * @param cx2
	 * @param cy2
	 * @param cz2
	 * @param r2
	 */
	public static Classification classifySphereSphere(float cx1, float cy1, float cz1, float r1, float cx2, float cy2, float cz2, float r2) {
		float dx = cx1 - cx2;
		float dy = cy1 - cy2;
		float dz = cz1 - cz2;

		float d = FastMath.sqrt((dx * dx) + (dy * dy) + (dz * dz));

		if (d + r2 < r1)
			return (Classification.INSIDE);
		else if (d + r1 < r2)
			return (Classification.ENCLOSING);
		else if (d < r2 + r1)
			return (Classification.SPANNING);
		else if (d > r1 + r2)
			return (Classification.OUTSIDE);
		else
			return (Classification.ENCLOSING);
	}

	/**
	 * Classifies a Sphere against a Sphere.
	 * 
	 * @param center1
	 * @param radius1
	 * @param center2
	 * @param radius2
	 */
	public static Classification classifySphereSphere(Tuple3f center1, float radius1, Tuple3f center2, float radius2) {
		return (classifySphereSphere(center1.getX(), center1.getY(), center1.getZ(), radius1, center2.getX(), center2.getY(), center2.getZ(), radius2));
	}

	/**
	 * Classifies a Sphere against a Sphere.
	 * 
	 * @param sphere1
	 * @param sphere2
	 */
	public static Classification classifySphereSphere(Sphere sphere1, Sphere sphere2) {
		return (classifySphereSphere(sphere1.centerX, sphere1.centerY, sphere1.centerZ, sphere1.getMaxCenterDistance(), sphere2.centerX, sphere2.centerY, sphere2.centerZ,
				sphere2.getMaxCenterDistance()));
	}

	/**
	 * Classifies a Box against a Sphere.
	 * 
	 * @param sphere
	 * @param boxLowerX
	 * @param boxLowerY
	 * @param boxLowerZ
	 * @param boxUpperX
	 * @param boxUpperY
	 * @param boxUpperZ
	 */
	public static Classification classifySphereBox(Sphere sphere, float boxLowerX, float boxLowerY, float boxLowerZ, float boxUpperX, float boxUpperY, float boxUpperZ) {
		return (classifyBoxSphere(boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ, sphere.centerX, sphere.centerY, sphere.centerZ, sphere.getMaxCenterDistance()).invert());
	}

	/**
	 * Classifies a Box against a Sphere.
	 * 
	 * @param sphere
	 * @param boxLower
	 * @param boxUpper
	 */
	public static Classification classifySphereBox(Sphere sphere, Tuple3f boxLower, Tuple3f boxUpper) {
		return (classifyBoxSphere(boxLower.getX(), boxLower.getY(), boxLower.getZ(), boxUpper.getX(), boxUpper.getY(), boxUpper.getZ(), sphere.centerX, sphere.centerY, sphere.centerZ,
				sphere.getMaxCenterDistance()).invert());
	}

	/**
	 * Classifies a Box against a Sphere.
	 * 
	 * @param sphere
	 * @param box
	 */
	public static Classification classifySphereBox(Sphere sphere, Box box) {
		return (classifyBoxSphere(box, sphere).invert());
	}

	/**
	 * Classify this sphere with respect to a plane
	 */
	public static PlaneClassification classifySpherePlane(Sphere sphere, Plane plane) {
		float d = plane.distanceTo(sphere.centerX, sphere.centerY, sphere.centerZ);
		float d_sq = (d < 0f) ? -d * d : d * d;
		float sphereRadius_sq = sphere.getMaxCenterDistanceSquared();

		if (d_sq >= sphereRadius_sq)
			return (PlaneClassification.IN_FRONT_OF);
		else if (d_sq <= -sphereRadius_sq)
			return (PlaneClassification.BEHIND);
		else
			return (PlaneClassification.SPANNING);
	}

	/**
	 * Classifies a Point against a Box.
	 * 
	 * @param boxLowerX
	 * @param boxLowerY
	 * @param boxLowerZ
	 * @param boxUpperX
	 * @param boxUpperY
	 * @param boxUpperZ
	 * @param px
	 * @param py
	 * @param pz
	 */
	public static Classification classifyBoxPoint(float boxLowerX, float boxLowerY, float boxLowerZ, float boxUpperX, float boxUpperY, float boxUpperZ, float px, float py, float pz) {
		if ((px >= boxLowerX) && (py >= boxLowerY) && (pz >= boxLowerZ) && (px <= boxUpperX) && (py <= boxUpperY) && (pz <= boxUpperZ)) {
			return (Classification.INSIDE);
		}

		return (Classification.OUTSIDE);
	}

	/**
	 * Classifies a Point against a Box.
	 * 
	 * @param boxLower
	 * @param boxUpper
	 * @param point
	 */
	public static Classification classifyBoxPoint(Tuple3f boxLower, Tuple3f boxUpper, Point3f point) {
		return (classifyBoxPoint(boxLower.getX(), boxLower.getY(), boxLower.getZ(), boxUpper.getX(), boxUpper.getY(), boxUpper.getZ(), point.getX(), point.getY(), point.getZ()));
	}

	/**
	 * Classifies a Point against a Box.
	 * 
	 * @param box
	 * @param point
	 */
	public static Classification classifyBoxPoint(Box box, Point3f point) {
		return (classifyBoxPoint(box.getLowerX(), box.getLowerY(), box.getLowerZ(), box.getUpperX(), box.getUpperY(), box.getUpperZ(), point.getX(), point.getY(), point.getZ()));
	}

	/**
	 * Classifies a Point against a Box.
	 * 
	 * @param box
	 * @param px
	 * @param py
	 * @param pz
	 */
	public static Classification classifyBoxPoint(Box box, float px, float py, float pz) {
		Tuple3f boxLower = box.getLower();
		Tuple3f boxUpper = box.getUpper();

		return (classifyBoxPoint(boxLower.getX(), boxLower.getY(), boxLower.getZ(), boxUpper.getX(), boxUpper.getY(), boxUpper.getZ(), px, py, pz));
	}

	private static final boolean boxEnclosesSphere(float boxLowerX, float boxLowerY, float boxLowerZ, float boxUpperX, float boxUpperY, float boxUpperZ, float x, float y, float z, float radius) {
		float centerX = (boxLowerX + boxUpperX) / 2.0f;
		float centerY = (boxLowerY + boxUpperY) / 2.0f;
		float centerZ = (boxLowerZ + boxUpperZ) / 2.0f;

		float sizeX = boxUpperX - boxLowerX;
		float sizeY = boxUpperY - boxLowerY;
		float sizeZ = boxUpperZ - boxLowerZ;

		float minSpan;
		if (sizeX < sizeY) {
			if (sizeX < sizeZ)
				minSpan = sizeX;
			else
				minSpan = sizeZ;
		} else if (sizeY < sizeZ) {
			minSpan = sizeY;
		} else {
			minSpan = sizeZ;
		}

		float dx = centerX - x;
		float dy = centerY - y;
		float dz = centerZ - z;

		float d = FastMath.sqrt((dx * dx) + (dy * dy) + (dz * dz));

		return (d + radius < minSpan);
	}

	/**
	 * Classifies a Sphere against a Box.
	 * 
	 * @param boxLowerX
	 * @param boxLowerY
	 * @param boxLowerZ
	 * @param boxUpperX
	 * @param boxUpperY
	 * @param boxUpperZ
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	public static Classification classifyBoxSphere(float boxLowerX, float boxLowerY, float boxLowerZ, float boxUpperX, float boxUpperY, float boxUpperZ, float x, float y, float z, float radius) {
		if ((x >= boxLowerX - radius) && (y >= boxLowerY - radius) && (z >= boxLowerZ - radius) && (x <= boxUpperX + radius) && (y <= boxUpperY + radius) && (z <= boxUpperZ + radius)) {
			return (Classification.INSIDE);
		} else if (boxEnclosesSphere(boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ, x, y, z, radius)) {
			return (Classification.ENCLOSING);
		} else if (IntersectionFactory.sphereIntersectsBox(boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ, x, y, z, radius)) {
			return (Classification.SPANNING);
		} else {
			return (Classification.OUTSIDE);
		}
	}

	/**
	 * Classifies a Sphere against a Box.
	 * 
	 * @param boxLower
	 * @param boxUpper
	 * @param sphereCenter
	 * @param radius
	 */
	public static Classification classifyBoxSphere(Tuple3f boxLower, Tuple3f boxUpper, Tuple3f sphereCenter, float radius) {
		return (classifyBoxSphere(boxLower.getX(), boxLower.getY(), boxLower.getZ(), boxUpper.getX(), boxUpper.getY(), boxUpper.getZ(), sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ(),
				radius));
	}

	/**
	 * Classifies a Sphere against a Box.
	 * 
	 * @param box
	 * @param sphere
	 */
	public static Classification classifyBoxSphere(Box box, Sphere sphere) {
		return (classifyBoxSphere(box.getLowerX(), box.getLowerY(), box.getLowerZ(), box.getUpperX(), box.getUpperY(), box.getUpperZ(), sphere.centerX, sphere.centerY, sphere.centerZ,
				sphere.getMaxCenterDistance()));
	}

	/**
	 * Classifies a Box against a Box.
	 * 
	 * @param lowerX1
	 * @param lowerY1
	 * @param lowerZ1
	 * @param upperX1
	 * @param upperY1
	 * @param upperZ1
	 * @param lowerX2
	 * @param lowerY2
	 * @param lowerZ2
	 * @param upperX2
	 * @param upperY2
	 * @param upperZ2
	 */
	public static Classification classifyBoxBox(float lowerX1, float lowerY1, float lowerZ1, float upperX1, float upperY1, float upperZ1, float lowerX2, float lowerY2, float lowerZ2, float upperX2,
			float upperY2, float upperZ2) {
		if (((lowerX1 <= lowerX2 && upperX1 >= upperX2) || (lowerX1 >= lowerX2 && lowerX1 <= upperX2) || (upperX1 >= lowerX2 && upperX1 <= upperX2))
				&& ((lowerY1 <= lowerY2 && upperY1 >= upperY2) || (lowerY1 >= lowerY2 && lowerY1 <= upperY2) || (upperY1 >= lowerY2 && upperY1 <= upperY2))
				&& ((lowerZ1 <= lowerZ2 && upperZ1 >= upperZ2) || (lowerZ1 >= lowerZ2 && lowerZ1 <= upperZ2) || (upperZ1 >= lowerZ2 && upperZ1 <= upperZ2))) {
			// The boxes intersect. Check to see if one encloses the other:

			if ((lowerX1 <= lowerX2) && (lowerY1 <= lowerY2) && (lowerZ1 <= lowerZ2) && (upperX1 >= upperX2) && (upperY1 >= upperY2) && (upperZ1 >= upperZ2))
				return (Classification.INSIDE);
			else if ((lowerX1 > lowerX2) && (lowerY1 > lowerY2) && (lowerZ1 > lowerZ2) && (upperX1 < upperX2) && (upperY1 < upperY2) && (upperZ1 < upperZ2))
				return (Classification.ENCLOSING);
			else
				return (Classification.SPANNING);
		}

		return (Classification.OUTSIDE);
	}

	/**
	 * Classifies a Box against a Box.
	 * 
	 * @param boxLower1
	 * @param boxUpper1
	 * @param boxLower2
	 * @param boxUpper2
	 */
	public static Classification classifyBoxBox(Tuple3f boxLower1, Tuple3f boxUpper1, Tuple3f boxLower2, Tuple3f boxUpper2) {
		return (classifyBoxBox(boxLower1.getX(), boxLower1.getY(), boxLower1.getZ(), boxUpper1.getX(), boxUpper1.getY(), boxUpper1.getZ(), boxLower2.getX(), boxLower2.getY(), boxLower2.getZ(),
				boxUpper2.getX(), boxUpper2.getY(), boxUpper2.getZ()));
	}

	/**
	 * Classifies a Box against a Box.
	 * 
	 * @param box1
	 * @param box2
	 */
	public static Classification classifyBoxBox(Box box1, Box box2) {
		return (classifyBoxBox(box1.getLower(), box1.getUpper(), box2.getLower(), box2.getUpper()));
	}

	/**
	 * Classifies a Sphere against a Bounds object.
	 * 
	 * @param sphere1
	 * @param bounds
	 */
	public static Classification classifySphereBounds(Sphere sphere1, Bounds bounds) {
		if (bounds.getType() == BoundsType.SPHERE)
			return (classifySphereSphere(sphere1, (Sphere) bounds));
		else if (bounds.getType() == BoundsType.AABB)
			return (classifySphereBox(sphere1, (Box) bounds));
		else
			// throw new IllegalArgumentException( "Bounds type " +
			// bounds.getType() + " is not (yet) supported." ) );
			return (null);
	}

	/**
	 * Classifies a Box against a Bounds object.
	 * 
	 * @param box1
	 * @param bounds
	 */
	public static Classification classifyBoxBounds(Box box1, Bounds bounds) {
		if (bounds.getType() == BoundsType.SPHERE)
			return (classifyBoxSphere(box1, (Sphere) bounds));
		else if (bounds.getType() == BoundsType.AABB)
			return (classifyBoxBox(box1, (Box) bounds));
		else
			// throw new IllegalArgumentException( "Bounds type " +
			// bounds.getType() + " is not (yet) supported." ) );
			return (null);
	}

	/**
	 * Classifies a two Bounds objects agains each others.
	 * 
	 * @param bounds1
	 * @param bounds2
	 */
	public static Classification classifyBoundsBounds(Bounds bounds1, Bounds bounds2) {
		if (bounds1.getType() == BoundsType.SPHERE) {
			if (bounds2.getType() == BoundsType.SPHERE)
				return (classifySphereSphere((Sphere) bounds1, (Sphere) bounds2));
			else if (bounds2.getType() == BoundsType.AABB)
				return (classifySphereBox((Sphere) bounds1, (Box) bounds2));
		} else if (bounds1.getType() == BoundsType.AABB) {
			if (bounds2.getType() == BoundsType.SPHERE)
				return (classifyBoxSphere((Box) bounds1, (Sphere) bounds2));
			else if (bounds2.getType() == BoundsType.AABB)
				return (classifyBoxBox((Box) bounds1, (Box) bounds2));
		}

		// throw new IllegalArgumentException( "Bounds type " +
		// bounds2.getType() + " is not (yet) supported." ) );
		return (null);
	}

	private static final float EPSILON = 0.000001f;

	/**
	 * Classifies a point with respect to the plane.
	 * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
	 * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	private static float distancePlanePoint(float planeNx, float planeNy, float planeNz, float planeD, float px, float py, float pz) {
		return ((planeNx * px) + (planeNy * py) + (planeNz * pz) + planeD);
	}

	/**
	 * Classifies a point with respect to the plane.
	 * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
	 * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	@SuppressWarnings("unused")
	private static float distancePlanePoint(float planeNx, float planeNy, float planeNz, float planeD, Tuple3f point) {
		float px = point.getX();
		float py = point.getY();
		float pz = point.getZ();

		return ((planeNx * px) + (planeNy * py) + (planeNz * pz) + planeD);
	}

	/**
	 * Classifies a point with respect to the plane.
	 * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
	 * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	public static PlaneClassification classifyPlanePoint(float planeNx, float planeNy, float planeNz, float planeD, float x, float y, float z) {
		float distance = distancePlanePoint(planeNx, planeNy, planeNz, planeD, x, y, z);

		if (distance < -EPSILON)
			return (PlaneClassification.BEHIND);
		else if (distance > EPSILON)
			return (PlaneClassification.IN_FRONT_OF);
		else
			return (PlaneClassification.COINCIDENT);
	}

	/**
	 * Classifies a point with respect to the plane.
	 * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
	 * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	public static PlaneClassification classifyPlanePoint(Plane plane, float px, float py, float pz) {
		float distance = plane.distanceTo(px, py, pz);

		if (distance < -EPSILON)
			return (PlaneClassification.BEHIND);
		else if (distance > EPSILON)
			return (PlaneClassification.IN_FRONT_OF);
		else
			return (PlaneClassification.COINCIDENT);
	}

	/**
	 * Classifies a point with respect to the plane.
	 * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
	 * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	public static PlaneClassification classifyPlanePoint(Plane plane, Tuple3f point) {
		return (classifyPlanePoint(plane, point.getX(), point.getY(), point.getZ()));
	}

	/**
	 * Classifies a collection of points with respect to the plane. We classify each point and OR the results together.
	 */
	public static PlaneClassification classifyPlanePoints(Plane plane, Tuple3f[] points) {
		PlaneClassification c = PlaneClassification.COINCIDENT;

		for (int i = 0; i < points.length; i++)
			c = c.add(classifyPlanePoint(plane, points[i]));

		return (c);
	}

	/*
	 * // Classify the box with respect to a plane public static
	 * PlaneClassification classifyPlaneBox( Plane plane, float boxLowerX, float
	 * boxLowerY, float boxLowerZ, float boxUpperX, float boxUpperY, float
	 * boxUpperZ ) { // Classify each point in turn PlaneClassification
	 * classification = classifyPlanePoint( plane, boxLowerX, boxLowerY,
	 * boxLowerZ ); // Won't be Plane.SPANNING yet...
	 * 
	 * classification = classification.add( classifyPlanePoint( plane,
	 * boxLowerX, boxLowerY, boxUpperZ ) ); if ( classification ==
	 * PlaneClassification.SPANNING ) // Leave early if it's foregone... return
	 * ( PlaneClassification.SPANNING ); classification = classification.add(
	 * classifyPlanePoint( plane, boxLowerX, boxUpperY, boxLowerZ ) ); if (
	 * classification == PlaneClassification.SPANNING ) return (
	 * PlaneClassification.SPANNING ); classification = classification.add(
	 * classifyPlanePoint( plane, boxLowerX, boxUpperY, boxUpperZ ) ); if (
	 * classification == PlaneClassification.SPANNING ) return (
	 * PlaneClassification.SPANNING ); classification = classification.add(
	 * classifyPlanePoint( plane, boxUpperX, boxLowerY, boxLowerZ ) ); if (
	 * classification == PlaneClassification.SPANNING ) return (
	 * PlaneClassification.SPANNING ); classification = classification.add(
	 * classifyPlanePoint( plane, boxUpperX, boxLowerY, boxUpperZ ) ); if (
	 * classification == PlaneClassification.SPANNING ) return (
	 * PlaneClassification.SPANNING ); classification = classification.add(
	 * classifyPlanePoint( plane, boxUpperX, boxUpperY, boxLowerZ ) ); if (
	 * classification == PlaneClassification.SPANNING ) return (
	 * PlaneClassification.SPANNING ); classification = classification.add(
	 * classifyPlanePoint( plane, boxUpperX, boxUpperY, boxUpperZ ) );
	 * 
	 * return ( classification ); }
	 */

	// Classify the box with respect to a plane
	public static PlaneClassification classifyPlaneBox(Plane plane, float boxLowerX, float boxLowerY, float boxLowerZ, float boxUpperX, float boxUpperY, float boxUpperZ) {
		float vminX;
		float vminY;
		float vminZ;
		float vmaxX;
		float vmaxY;
		float vmaxZ;

		if (plane.getA() >= 0f) {
			vminX = boxLowerX;
			vmaxX = boxUpperX;
		} else {
			vminX = boxUpperX;
			vmaxX = boxLowerX;
		}

		if (plane.getB() >= 0f) {
			vminY = boxLowerY;
			vmaxY = boxUpperY;
		} else {
			vminY = boxUpperY;
			vmaxY = boxLowerY;
		}

		if (plane.getC() >= 0f) {
			vminZ = boxLowerZ;
			vmaxZ = boxUpperZ;
		} else {
			vminZ = boxUpperZ;
			vmaxZ = boxLowerZ;
		}

		if (FloatUtils.dot(plane.getA(), plane.getB(), plane.getC(), vminX, vminY, vminZ) + plane.getD() > 0f)
			return (PlaneClassification.IN_FRONT_OF);
		else if (FloatUtils.dot(plane.getA(), plane.getB(), plane.getC(), vmaxX, vmaxY, vmaxZ) + plane.getD() >= 0f)
			return (PlaneClassification.SPANNING);
		else
			return (PlaneClassification.BEHIND);
	}

	// Classify the box with respect to a plane
	public static PlaneClassification classifyPlaneBox(Plane plane, Tuple3f boxLower, Tuple3f boxUpper) {
		return (classifyPlaneBox(plane, boxLower.getX(), boxLower.getY(), boxLower.getZ(), boxUpper.getX(), boxUpper.getY(), boxUpper.getZ()));
	}

	// Classify the box with respect to a plane
	public static PlaneClassification classifyPlaneBox(Plane plane, Box box) {
		return (classifyPlaneBox(plane, box.getLower(), box.getUpper()));
	}

	/**
	 * Classifies a Point against a Frustum.
	 * 
	 * @param frustum
	 * @param x
	 * @param y
	 * @param z
	 */
	public static final Classification classifyFrustumPoint(Frustum frustum, float x, float y, float z) {
		if (classifyPlanePoint(frustum.getPlaneNear(), x, y, z) == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classifyPlanePoint(frustum.getPlaneLeft(), x, y, z) == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classifyPlanePoint(frustum.getPlaneRight(), x, y, z) == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classifyPlanePoint(frustum.getPlaneBottom(), x, y, z) == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classifyPlanePoint(frustum.getPlaneTop(), x, y, z) == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classifyPlanePoint(frustum.getPlaneFar(), x, y, z) == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else
			return (Classification.INSIDE);
	}

	/**
	 * Classifies a Point against a Frustum.
	 * 
	 * @param frustum
	 * @param point
	 */
	public static final Classification classifyFrustumPoint(Frustum frustum, Point3f point) {
		return (classifyFrustumPoint(frustum, point.getX(), point.getY(), point.getZ()));
	}

	/**
	 * Classifies a Sphere against a Frustum.
	 * 
	 * @param frustum
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	public static final Classification classifyFrustumSphere(Frustum frustum, float x, float y, float z, float radius) {
		int count = 0;

		float d = frustum.getPlaneNear().distanceTo(x, y, z);

		if (d <= -radius)
			return (Classification.OUTSIDE);

		if (d >= radius)
			count++;

		d = frustum.getPlaneLeft().distanceTo(x, y, z);

		if (d <= -radius)
			return (Classification.OUTSIDE);

		if (d >= radius)
			count++;

		d = frustum.getPlaneRight().distanceTo(x, y, z);

		if (d <= -radius)
			return (Classification.OUTSIDE);

		if (d >= radius)
			count++;

		d = frustum.getPlaneBottom().distanceTo(x, y, z);

		if (d <= -radius)
			return (Classification.OUTSIDE);

		if (d >= radius)
			count++;

		d = frustum.getPlaneTop().distanceTo(x, y, z);

		if (d <= -radius)
			return (Classification.OUTSIDE);
		else if (d >= radius)
			count++;

		d = frustum.getPlaneFar().distanceTo(x, y, z);

		if (d <= -radius)
			return (Classification.OUTSIDE);

		if (d >= radius)
			count++;

		if (count == 6)
			return (Classification.INSIDE);

		return (Classification.SPANNING);
	}

	/**
	 * Classifies a Sphere against a Frustum.
	 * 
	 * @param frustum
	 * @param sphereCenter
	 * @param radius
	 */
	public static final Classification classifyFrustumSphere(Frustum frustum, Tuple3f sphereCenter, float radius) {
		int count = 0;

		float d = frustum.getPlaneNear().distanceTo(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ());

		if (d <= -radius)
			return (Classification.OUTSIDE);
		else if (d >= radius)
			count++;

		d = frustum.getPlaneLeft().distanceTo(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ());

		if (d <= -radius)
			return (Classification.OUTSIDE);
		else if (d >= radius)
			count++;

		d = frustum.getPlaneRight().distanceTo(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ());

		if (d <= -radius)
			return (Classification.OUTSIDE);
		else if (d >= radius)
			count++;

		d = frustum.getPlaneBottom().distanceTo(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ());

		if (d <= -radius)
			return (Classification.OUTSIDE);
		else if (d >= radius)
			count++;

		d = frustum.getPlaneTop().distanceTo(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ());

		if (d <= -radius)
			return (Classification.OUTSIDE);
		else if (d >= radius)
			count++;

		d = frustum.getPlaneFar().distanceTo(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ());

		if (d <= -radius)
			return (Classification.OUTSIDE);
		else if (d >= radius)
			count++;

		if (count == 6)
			return (Classification.INSIDE);

		return (Classification.SPANNING);
	}

	/**
	 * Classifies a Sphere against a Frustum.
	 * 
	 * @param frustum
	 * @param sphere
	 */
	public static final Classification classifyFrustumSphere(Frustum frustum, Sphere sphere) {
		int count = 0;

		PlaneClassification classification = classifySpherePlane(sphere, frustum.getPlaneNear());

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifySpherePlane(sphere, frustum.getPlaneLeft());

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifySpherePlane(sphere, frustum.getPlaneRight());

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifySpherePlane(sphere, frustum.getPlaneBottom());

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifySpherePlane(sphere, frustum.getPlaneTop());

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifySpherePlane(sphere, frustum.getPlaneFar());

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		if (count == 6)
			return (Classification.INSIDE);

		return (Classification.SPANNING);
	}

	/**
	 * Classifies a Box against a Frustum.
	 * 
	 * @param frustum
	 * @param boxLowerX
	 * @param boxLowerY
	 * @param boxLowerZ
	 * @param boxUpperX
	 * @param boxUpperY
	 * @param boxUpperZ
	 */
	public static Classification classifyFrustumBox(Frustum frustum, float boxLowerX, float boxLowerY, float boxLowerZ, float boxUpperX, float boxUpperY, float boxUpperZ) {
		int count = 0;

		PlaneClassification classification = classifyPlaneBox(frustum.getPlaneNear(), boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ);

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifyPlaneBox(frustum.getPlaneLeft(), boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ);

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifyPlaneBox(frustum.getPlaneRight(), boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ);

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifyPlaneBox(frustum.getPlaneBottom(), boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ);

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifyPlaneBox(frustum.getPlaneTop(), boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ);

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		classification = classifyPlaneBox(frustum.getPlaneFar(), boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ);

		if (classification == PlaneClassification.BEHIND)
			return (Classification.OUTSIDE);
		else if (classification == PlaneClassification.IN_FRONT_OF)
			count++;

		if (count == 6)
			return Classification.INSIDE;

		return (Classification.SPANNING);
	}

	/**
	 * Classifies a Box against a Frustum.
	 * 
	 * @param frustum
	 * @param boxLower
	 * @param boxUpper
	 */
	public static Classification classifyFrustumBox(Frustum frustum, Tuple3f boxLower, Tuple3f boxUpper) {
		return (classifyFrustumBox(frustum, boxLower.getX(), boxLower.getY(), boxLower.getZ(), boxUpper.getX(), boxUpper.getY(), boxUpper.getZ()));
	}

	/**
	 * Classifies a Box against a Frustum.
	 * 
	 * @param frustum
	 * @param box
	 */
	public static final Classification classifyFrustumBox(Frustum frustum, Box box) {
		return (classifyFrustumBox(frustum, box.getLowerX(), box.getLowerY(), box.getLowerZ(), box.getUpperX(), box.getUpperY(), box.getUpperZ()));
	}

	/**
	 * Classifies a Frustum against a Bounds object.
	 * 
	 * @param frustum
	 * @param bounds
	 */
	public static Classification classifyFrustumBounds(Frustum frustum, Bounds bounds) {
		if (bounds.getType() == BoundsType.SPHERE)
			return (classifyFrustumSphere(frustum, (Sphere) bounds));
		else if (bounds.getType() == BoundsType.AABB)
			return (classifyFrustumBox(frustum, (Box) bounds));
		else
			// throw new IllegalArgumentException( "Bounds type " +
			// bounds.getType() + " is not (yet) supported." ) );
			return (null);
	}
}
