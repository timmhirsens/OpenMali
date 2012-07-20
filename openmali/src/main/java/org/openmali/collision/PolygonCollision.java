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
package org.openmali.collision;

import org.openmali.vecmath2.Tuple2f;
import org.openmali.vecmath2.Tuple3f;

public abstract class PolygonCollision {

	/**
	 * @param data
	 *            The polygon data
	 * @param point
	 *            The point to test
	 * @return true if the point is strictly in the polygon, false if it's
	 *         strictly out, and false or true if it's on the boundary
	 */
	public static boolean contains(Tuple2f[] data, Tuple2f point) {

		int i, j;
		boolean c = false;

		for (i = 0, j = (data.length - 1); i < data.length; j = i++) {

			if ((((data[i].getY() <= point.getY()) && (point.getY() < data[j].getY())) || ((data[j].getY() <= point.getY()) && (point.getY() < data[i].getY())))
					&& (point.getX() < (data[j].getX() - data[i].getX()) * (point.getY() - data[i].getY()) / (data[j].getY() - data[i].getY()) + data[i].getX())) {
				c = !c;
			}
		}

		return c;

	}

	/**
	 * @param data
	 *            The polygon data
	 * @param point
	 *            The point to test
	 * @return true if the point is strictly in the polygon, false if it's
	 *         strictly out, and false or true if it's on the boundary
	 */
	public static boolean contains(Tuple3f[] data, Tuple2f point) {

		int i, j;
		boolean c = false;

		for (i = 0, j = (data.length - 1); i < data.length; j = i++) {

			if ((((data[i].getY() <= point.getY()) && (point.getY() < data[j].getY())) || ((data[j].getY() <= point.getY()) && (point.getY() < data[i].getY())))
					&& (point.getX() < (data[j].getX() - data[i].getX()) * (point.getY() - data[i].getY()) / (data[j].getY() - data[i].getY()) + data[i].getX())) {
				c = !c;
			}
		}

		return c;

	}

}
