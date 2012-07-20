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
package org.openmali.distance;

import org.openmali.FastMath;
import org.openmali.vecmath2.Tuple2f;
import org.openmali.vecmath2.Tuple3f;

/**
 * Class used to compute distance between two points in a plane
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public abstract class Distance2D {
	
	/**
	 * @return The distance between the two points
	 */
	public static float dist(float t1x, float t1y, float t2x, float t2y) {
		
		return FastMath.sqrt(
		(t1x - t2x) * (t1x - t2x)
		+ (t1y - t2y) * (t1y - t2y)
		);
		
	}
	
	/**
	 * @return The distance between the two points
	 */
	public static float dist(Tuple2f t1, Tuple2f t2) {
		
		return FastMath.sqrt(
		(t1.getX() - t2.getX()) * (t1.getX() - t2.getX())
		+ (t1.getY() - t2.getY()) * (t1.getY() - t2.getY())
		);
		
	}
	
	/**
	 * @return The distance between the two points
	 */
	public static float dist(Tuple3f p1, Tuple3f p2) {

		return FastMath.sqrt(
				(p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
				+ (p1.getY() - p2.getY()) * (p1.getY() - p2.getY())
				);
		
	}

	/**
	 * @return The squared distance between the two points
	 */
	public static float squaredDist(Tuple2f t1, Tuple2f t2) {
		
		return 	(t1.getX() - t2.getX()) * (t1.getX() - t2.getX())
				+ (t1.getY() - t2.getY()) * (t1.getY() - t2.getY());
		
	}

}
