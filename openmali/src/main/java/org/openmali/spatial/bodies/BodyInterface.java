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

import org.openmali.vecmath2.Point3f;

/**
 * Simple interface for the {@link Body} class.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface BodyInterface
{
    /**
     * Tests, if the given point is inside of this Body.
     * 
     * @param px
     * @param py
     * @param pz
     * 
     * @return true, if it is inside
     */
    public abstract boolean contains( float px, float py, float pz );
    
    /**
     * Tests, if the given point is inside of this Body.
     * 
     * @param point
     * 
     * @return true, if it is inside
     */
    public abstract boolean contains( Point3f point );
    
    /**
     * Combines this Body with a bounding object.
     * 
     * @param body
     */
    public abstract void combine( BodyInterface body );
    
    /**
     * Combines this Body with an array of bounding objects.
     * 
     * @param bodies
     */
    public abstract void combine( BodyInterface[] bodies );
    
    /**
     * Combines this Body with a point.
     * 
     * @param x
     * @param y
     * @param z
     */
    public abstract void combine( float x, float y, float z );
    
    /**
     * Combines this Body with a point.
     * 
     * @param point
     */
    public abstract void combine( Point3f point );
    
    /**
     * Combine this Body with an array of points.
     * 
     * @param points
     */
    public abstract void combine( Point3f[] points );
}
