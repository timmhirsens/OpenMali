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
package org.openmali.spatial.quadtree;

import org.openmali.spatial.PlaneIndicator;
import org.openmali.spatial.bounds.BoundingBox;
import org.openmali.spatial.bounds.BoundingSphere;
import org.openmali.spatial.bounds.Bounds;
import org.openmali.spatial.bounds.BoundsType;

/**
 * Insert comment here.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
class BoundsHelper {
	public static final float getMinX(PlaneIndicator plane, Bounds bounds, BoundsType type) {
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getLowerX());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterX() - ((BoundingSphere) bounds).getRadius());
		} else if (plane == PlaneIndicator.Z_Y_PLANE) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getLowerZ());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterZ() - ((BoundingSphere) bounds).getRadius());
		}

		return (0f);
	}

	public static final float getMinDepth(PlaneIndicator plane, Bounds bounds, BoundsType type) {
		if (plane == PlaneIndicator.X_Z_PLANE) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getLowerZ());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterZ() - ((BoundingSphere) bounds).getRadius());
		} else if ((plane == PlaneIndicator.X_Y_PLANE) || (plane == PlaneIndicator.Z_Y_PLANE)) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getLowerY());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterY() - ((BoundingSphere) bounds).getRadius());
		}

		return (0f);
	}

	public static final float getMaxX(PlaneIndicator plane, Bounds bounds, BoundsType type) {
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getUpperX());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterX() + ((BoundingSphere) bounds).getRadius());
		} else if (plane == PlaneIndicator.Z_Y_PLANE) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getUpperZ());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterZ() + ((BoundingSphere) bounds).getRadius());
		}

		return (0f);
	}

	public static final float getMaxDepth(PlaneIndicator plane, Bounds bounds, BoundsType type) {
		if (plane == PlaneIndicator.X_Z_PLANE) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getUpperZ());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterZ() + ((BoundingSphere) bounds).getRadius());
		} else if ((plane == PlaneIndicator.X_Y_PLANE) || (plane == PlaneIndicator.Z_Y_PLANE)) {
			if (type == BoundsType.AABB)
				return (((BoundingBox) bounds).getUpperY());
			else if (type == BoundsType.SPHERE)
				return (((BoundingSphere) bounds).getCenterY() + ((BoundingSphere) bounds).getRadius());
		}

		return (0f);
	}
}
