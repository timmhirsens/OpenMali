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
package org.openmali.spatial.bodies;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openmali.pooling.ObjectPool;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Ray3f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.openmali.vecmath2.util.FloatUtils;

/**
 * A Plane.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public final class Plane implements Externalizable {
	private static final long serialVersionUID = 9114612905166638720L;

	private static final ObjectPool<Plane> POOL = new ObjectPool<Plane>(32) {
		@Override
		protected Plane newInstance() {
			return (new Plane());
		}
	};

	private static final float EPSILON = 0.000001f;

	private final Vector3f normal = new Vector3f();
	private float nx, ny, nz;
	private float d = 0.0f;

	public void setNormal(Vector3f normal) {
		nx = normal.getX();
		ny = normal.getY();
		nz = normal.getZ();

		normalize();
	}

	/**
	 * @return the normal to this Plane.
	 */
	public final Vector3f getNormal() {
		normal.set(nx, ny, nz);

		return (normal);
	}

	public void setA(float a) {
		nx = a;

		normalize();
	}

	public final float getA() {
		return (nx);
	}

	public final float getNX() {
		return (nx);
	}

	public void setB(float b) {
		ny = b;

		normalize();
	}

	public final float getB() {
		return (ny);
	}

	public final float getNY() {
		return (ny);
	}

	public void setC(float c) {
		nz = c;

		normalize();
	}

	public final float getC() {
		return (nz);
	}

	public final float getNZ() {
		return (nz);
	}

	public void setD(float d) {
		this.d = d;

		normalize();
	}

	public final float getD() {
		return (d);
	}

	/**
	 * Classifies a point with respect to the plane.
	 * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
	 * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	public final float distanceTo(float x, float y, float z) {
		return ((nx * x) + (ny * y) + (nz * z) + d);
	}

	/**
	 * Classifies a point with respect to the plane.
	 * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
	 * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	public final float distanceTo(Tuple3f v) {
		return (distanceTo(v.getX(), v.getY(), v.getZ()));
	}

	/**
	 * Computes the intersection line between this plane and incoming plane rkPlane1.
	 * The intersection is returned in rkLine unless the planes do not intersect,
	 * in which case the function returns false and no data is changed.
	 *
	 * Thanks to magic software for this one.
	 */
	public boolean intersects(Plane rkPlane1, Line rkLine) {
		// If Cross(N0,N1) is zero, then either planes are parallel and
		// separated
		// or the same plane. In both cases, 'false' is returned. Otherwise,
		// the intersection line is
		//
		// L(t) = t*Cross(N0,N1) + c0*N0 + c1*N1
		//
		// for some coefficients c0 and c1 and for t any real number (the line
		// parameter). Taking dot products with the normals,
		//
		// d0 = Dot(N0,L) = c0*Dot(N0,N0) + c1*Dot(N0,N1)
		// d1 = Dot(N1,L) = c0*Dot(N0,N1) + c1*Dot(N1,N1)
		//
		// which are two equations in two unknowns. The solution is
		//
		// c0 = (Dot(N1,N1)*d0 - Dot(N0,N1)*d1)/det
		// c1 = (Dot(N0,N0)*d1 - Dot(N0,N1)*d0)/det
		//
		// where det = Dot(N0,N0)*Dot(N1,N1)-Dot(N0,N1)^2.

		// float fN00 = getNormal().lengthSquared();
		float fN00 = FloatUtils.vectorLengthSquared(nx, ny, nz);
		// float fN01 = getNormal().dot( rkPlane1.getNormal() );
		float fN01 = FloatUtils.dot(nx, ny, nz, rkPlane1.nx, rkPlane1.ny, rkPlane1.nz);
		// float fN11 = rkPlane1.getNormal().lengthSquared();
		float fN11 = FloatUtils.vectorLengthSquared(rkPlane1.nx, rkPlane1.ny, rkPlane1.nz);
		float fDet = fN00 * fN11 - fN01 * fN01;

		if (Math.abs(fDet) < EPSILON)
			return (false);

		float fInvDet = 1.0f / fDet;
		float fC0 = (fN11 * d - fN01 * rkPlane1.d) * fInvDet;
		float fC1 = (fN00 * rkPlane1.d - fN01 * d) * fInvDet;

		// rkLine.getDirection().cross( getNormal(), rkPlane1.getNormal() );
		FloatUtils.cross(nx, ny, nz, rkPlane1.nx, rkPlane1.ny, rkPlane1.nz, rkLine.getDirection());
		// temp.set( getNormal() );
		// temp.scale( fC0 );
		rkLine.getOrigin().set(nx * fC0, ny * fC0, nz * fC0);
		// temp.set( rkPlane1.getNormal() );
		// temp.scale( fC1 );
		// rkLine.getOrigin().add( temp );
		rkLine.getOrigin().add(rkPlane1.nx * fC1, rkPlane1.ny * fC1, rkPlane1.nz * fC1);

		return (true);
	}

	/**
	 * @param rayOrigin
	 * @param dir
	 * @param intersection
	 * 
	 * @return NaN if ray is parallel to plane, t which has to be multipled by dir to get intersection point
	 */
	public float rayIntersectionParametric(Point3f rayOrigin, Vector3f dir, Tuple3f intersection) {
		if (Math.abs(dir.dot(getNormal())) < EPSILON)
			return (Float.NaN);
		// dir does not have to be normalized here
		// in case of normalization, it would be divided by length before dot
		// here and
		// then t would have to be multipled by length afterwards
		// n dot (dir/length) = (n dot dir)/length
		// so no need to divide/multiply here
		float t = -distanceTo(rayOrigin) / getNormal().dot(dir);

		if (intersection != null) {
			intersection.scaleAdd(t, dir, rayOrigin);
		}

		return (t);
	}

	/**
	 * @param ray
	 * @param intersection
	 * 
	 * @return NaN if ray is parallel to plane, t which has to be multipled by dir to get intersection point
	 */
	public float rayIntersectionParametric(Ray3f ray, Tuple3f intersection) {
		return rayIntersectionParametric(ray.getOrigin(), ray.getDirection(), intersection);
	}

	/**
	 * Does a ray intersection test with <code>this</code>.
	 * 
	 * @param rayOrigin
	 * @param rayDirection
	 * @param intersection
	 * 
	 * @return true for an intersection
	 */
	public boolean intersects(Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection) {
		if (rayIntersectionParametric(rayOrigin, rayDirection, intersection) >= 0f)
			return (true);

		return (false);
	}

	/**
	 * Does a ray intersection test with <code>this</code>.
	 * 
	 * @param ray
	 * @param intersection
	 * 
	 * @return true for an intersection
	 */
	public boolean intersects(Ray3f ray, Tuple3f intersection) {
		return (intersects(ray.getOrigin(), ray.getDirection(), intersection));
	}

	/**
	 * Does a ray intersection test with <code>this</code>.
	 * 
	 * @param rayOrigin
	 * @param rayDirection
	 * 
	 * @return true for an intersection
	 */
	public boolean intersects(Point3f rayOrigin, Vector3f rayDirection) {
		return (intersects(rayOrigin, rayDirection, null));
	}

	/**
	 * Does a ray intersection test with <code>this</code>.
	 * 
	 * @param ray
	 * 
	 * @return true for an intersection
	 */
	public boolean intersects(Ray3f ray) {
		return (intersects(ray, null));
	}

	public boolean segmentIntersection(Point3f from, Point3f to, Tuple3f intersect) {
		Vector3f dir = Vector3f.fromPool();
		dir.sub(to, from);
		float t = rayIntersectionParametric(from, dir, intersect);
		Vector3f.toPool(dir);
		if (Math.abs(t) <= 1f)
			return (true);

		return (false);
	}

	public float intersectsSegment(Tuple3f va, Tuple3f vb, Tuple3f intersection) {
		final float da = va.getX() * nx + va.getY() * ny + va.getZ() * nz + d;
		final float db = vb.getX() * nx + vb.getY() * ny + vb.getZ() * nz + d;

		// System.out.println( "da = " + da );
		// System.out.println( "db = " + db );

		if (da <= 0.0f && db >= 0.0f) {
			final float s = da / (da - db); // intersection factor (between 0
											// and 1)
			// System.out.println( "s = " + s );

			if (intersection != null) {
				intersection.setX(va.getX() + s * (vb.getX() - va.getX()));
				intersection.setY(va.getY() + s * (vb.getY() - va.getY()));
				intersection.setZ(va.getZ() + s * (vb.getZ() - va.getZ()));
			}

			return (Math.abs(da));
		} else if (db <= 0.0f && da >= 0.0f) {
			final float s = da / (da - db); // intersection factor (between 0
											// and 1)
			// System.out.println( "s = " + s );

			if (intersection != null) {
				intersection.setX(va.getX() + s * (vb.getX() - va.getX()));
				intersection.setY(va.getY() + s * (vb.getY() - va.getY()));
				intersection.setZ(va.getZ() + s * (vb.getZ() - va.getZ()));
			}

			return (Math.abs(da));
		}

		return (Float.NEGATIVE_INFINITY);
	}

	public boolean intersects(Plane rkPlane1) {
		// temp.cross( getNormal(), rkPlane1.getNormal() );
		// final float length_sq = temp.lengthSquared();

		float crossX = ny * rkPlane1.nz - nz * rkPlane1.ny;
		float crossY = nz * rkPlane1.nx - nx * rkPlane1.nz;
		float crossZ = nx * rkPlane1.ny - ny * rkPlane1.nx;

		final float length_sq = FloatUtils.vectorLengthSquared(crossX, crossY, crossZ);

		return (length_sq > EPSILON);
	}

	/**
	 * Normalizes this plane (i.e. the vector (a,b,c) becomes unit length)
	 * This also scales d to compensate.
	 * 
	 * Creation date: (05/02/2001 22:45:55)
	 */
	public Plane normalize() {
		float t = FloatUtils.vectorLength(nx, ny, nz);

		nx /= t;
		ny /= t;
		nz /= t;
		d /= t;

		return (this);
	}

	/**
	 * readExternal method comment.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		nx = in.readFloat();
		ny = in.readFloat();
		nz = in.readFloat();
		d = in.readFloat();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(nx);
		out.writeFloat(ny);
		out.writeFloat(nz);
		out.writeFloat(d);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(64);

		sb.append(this.getClass().getSimpleName());
		sb.append(" { A:");
		sb.append(nx);
		sb.append(", B:");
		sb.append(ny);
		sb.append(", C:");
		sb.append(nz);
		sb.append(", D:");
		sb.append(d);
		sb.append(" }");

		return (sb.toString());
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (04/02/2001 21:59:03)
	 */
	public void set(float a, float b, float c, float d) {
		this.nx = a;
		this.ny = b;
		this.nz = c;
		this.d = d;

		normalize();
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (04/02/2001 21:59:03)
	 */
	public void set(Plane plane) {
		this.nx = plane.nx;
		this.ny = plane.ny;
		this.nz = plane.nz;
		this.d = plane.d;

		// normalize();
	}

	/**
	 * Plane constructor comment.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public Plane(float a, float b, float c, float d) {
		super();

		this.nx = a;
		this.ny = b;
		this.nz = c;
		this.d = d;

		normalize();
	}

	/**
	 * Plane constructor comment.
	 * 
	 * @param normal
	 * @param d
	 */
	public Plane(Vector3f normal, float d) {
		this(normal.getX(), normal.getY(), normal.getZ(), d);
	}

	/**
	 * Plane constructor comment.
	 * 
	 * @param plane
	 */
	public Plane(Plane plane) {
		this(plane.nx, plane.ny, plane.nz, plane.d);
	}

	/**
	 * Useless Plane constructor.
	 */
	public Plane() {
		this(1.0f, 0.0f, 0.0f, 0.0f);
	}

	/**
	 * @return a Plane-instance from the pool.
	 */
	public static final Plane fromPool() {
		return (POOL.alloc());
	}

	/**
	 * Pushes a Plane-instance to the pool.
	 * 
	 * @param plane
	 */
	public static final void toPool(Plane plane) {
		POOL.free(plane);
	}
}
