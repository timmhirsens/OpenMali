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

import org.openmali.FastMath;
import org.openmali.vecmath2.Tuple3f;

/**
 * Base class for all geometric Bodies.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class Body implements BodyInterface
{
    protected float centerX, centerY, centerZ;
    protected float maxCenterDist, maxCenterDistSquared;
    
    protected boolean distComputed = false;
    
    /**
     * Sets this Bodie's center.
     * 
     * @param x
     * @param y
     * @param z
     */
    public void setCenter( float x, float y, float z )
    {
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
    }
    
    
    /**
     * Sets this Bodie's center.
     * 
     * @param x
     * @param y
     * @param z
     */
    public final void setCenter( Tuple3f center )
    {
        setCenter( center.getX(), center.getY(), center.getZ() );
    }
    
    /**
     * @return the x-coordinate of the center.
     */
    public final float getCenterX()
    {
        return ( centerX );
    }
    
    /**
     * @return the y-coordinate of the center.
     */
    public final float getCenterY()
    {
        return ( centerY );
    }
    
    /**
     * @return the z-coordinate of the center.
     */
    public final float getCenterZ()
    {
        return ( centerZ );
    }
    
    public final < T extends Tuple3f > T getCenter( T center )
    {
        center.set( centerX, centerY, centerZ );
        
        return ( center );
    }
    
    /**
     * Sets the maximum distance to the Bodie's center (squared).
     * For a sphere this is the (squared) radius.
     * 
     * @param maxCenterDistSquared
     */
    protected void setMaxCenterDistanceSquared( float maxCenterDistSquared )
    {
        this.maxCenterDistSquared = maxCenterDistSquared;
        this.distComputed = false;
    }
    
    /**
     * @return the maximum distance to the Bodie's center (squared).
     * For a sphere this is the (squared) radius.
     */
    public final float getMaxCenterDistanceSquared()
    {
        return ( maxCenterDistSquared );
    }
    
    /**
     * @return the maximum distance to the Bodie's center.
     * For a sphere this is the radius.
     */
    public final float getMaxCenterDistance()
    {
        if ( !distComputed )
        {
            maxCenterDist = FastMath.sqrt( maxCenterDistSquared );
            distComputed = true;
        }
        
        return ( maxCenterDist );
    }
}
