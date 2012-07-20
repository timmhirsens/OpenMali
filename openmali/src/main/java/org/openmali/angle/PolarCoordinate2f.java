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

import org.openmali.FastMath;
import org.openmali.vecmath2.Vector2f;

/**
 * Two-dimensional floating point polar coordinate.
 * 
 * @author David Yazel
 */
public class PolarCoordinate2f
{
    /** Radius */
    private float radius;
    
    /** Angle */
    private float angle;
    
    /**
     * @return the radius
     */
    public float getRadius()
    {
        return ( radius );
    }
    
    /**
     * @return the angle
     */
    public float getAngle()
    {
        return ( angle );
    }
    
    /**
     * Set the polar coordinates.
     * 
     * @param radius The new radius
     * @param angle The new angle, in degrees
     */
    public void set( float radius, float angle )
    {
        this.radius = radius;
        this.angle = angle;
    }
    
    /**
     * Sets the polar coordinates from another polar coordinate.
     * 
     * @param src The source polar coordinate
     */
    public void set( PolarCoordinate2f src )
    {
        this.radius = src.radius;
        this.angle = src.angle;
    }
    
    /**
     * Adds this polar coordinate to the specified vector2f
     * 
     * @param vec The vector to modify
     * @return vec
     */
    public Vector2f add( Vector2f vec )
    {
        vec.addX( FastMath.cos( FastMath.toRad( angle ) ) * radius );
        vec.addY( FastMath.cos( FastMath.toRad( angle + 90.0f ) ) * radius );
        vec.subX( FastMath.sin( FastMath.toRad( angle ) ) * radius );
        vec.subY( FastMath.sin( FastMath.toRad( angle + 90.0f ) ) * radius );
        
        return ( vec );
    }
    
    /**
     * Adds this polar coordinate to the specified vector2f and place
     * the result in a destination vector2f
     * 
     * @param src The source vector to modify
     * @param dest The destination result
     */
    public void add( Vector2f src, Vector2f dest )
    {
        dest.setX( FastMath.cos( FastMath.toRad( angle ) ) * radius );
        dest.setY( FastMath.cos( FastMath.toRad( angle + 90.0f ) ) * radius );
        dest.subX( FastMath.sin( FastMath.toRad( angle ) ) * radius );
        dest.subY( FastMath.sin( FastMath.toRad( angle + 90.0f ) ) * radius );
    }
    
    /**
     * Constructor for PolarCoordinate2f.
     */
    public PolarCoordinate2f()
    {
        super();
    }
}
