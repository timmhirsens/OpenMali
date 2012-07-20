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
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;

/**
 * Line is L(t) = P+t*D for any real-valued t.<br>
 * D is not necessarily unit length.<br>
 * <br>
 * <i>Thanks to magic software for this one.</i>
 *
 * @author cas
 * @author Marvin Froehlich (aka Qudus)
 */
public class Line
{
    private Point3f origin;
    private Vector3f direction;
    
    /**
     * @return the line's origin
     */
    public Point3f getOrigin()
    {
        return ( origin );
    }
    
    /**
     * Sets the line's origin.
     * 
     * @param x
     * @param y
     * @param z
     */
    public void setOrigin( float x, float y, float z )
    {
        this.origin.setX( x );
        this.origin.setY( y );
        this.origin.setZ( z );
    }
    
    /**
     * Sets the line's origin.
     * 
     * @param origin new origin
     */
    public void setOrigin( Tuple3f origin )
    {
        setOrigin( origin.getX(), origin.getY(), origin.getZ() );
    }
    
    /**
     * @return the line's direction
     */
    public Vector3f getDirection()
    {
        return ( direction );
    }
    
    /**
     * Sets the line's direction.
     * 
     * @param x
     * @param y
     * @param z
     */
    public void setDirection( float x, float y, float z )
    {
        this.direction.setX( x );
        this.direction.setY( y );
        this.direction.setZ( z );
    }
    
    /**
     * Sets the line's direction.
     * 
     * @param direction new direction
     */
    public void setDirection( Tuple3f direction )
    {
        setDirection( direction.getX(), direction.getY(), direction.getZ() );
    }
    
    /**
     * @return the line's length
     * <i>same as length()</i>
     */
    public float getLength()
    {
        return ( direction.length() );
    }
    
    /**
     * @return the line's length
     * <i>same as getLength()</i>
     */
    public float length()
    {
        return ( direction.length() );
    }
    
    /**
     * Creates a clone.
     */
    @Override
    public Line clone()
    {
        return ( new Line( this ) );
    }
    
    /**
     * Sets this line to the passed parameters.
     * 
     * @param origin the new origin point
     * @param direction the new direction vector
     */
    public void set( Point3f origin, Vector3f direction )
    {
        this.origin.set( origin );
        this.direction.set( direction );
    }
    
    /**
     * Sets this line to be equal to the passed one.
     */
    public void set( Line line )
    {
        set( line.getOrigin(), line.getDirection() );
    }
    
    /**
     * Checks if the given line equals this one.
     * 
     * @param line the ray to test for equality
     */
    public boolean equals( Line line )
    {
        return ( this.origin.equals( line.origin ) && this.direction.equals( line.direction ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( o instanceof Line )
            return ( equals( (Line)o ) );
        
        return ( false );
    }
    
    /**
     * @return a String representation of this ray
     */
    @Override
    public String toString()
    {
        return ( this.getClass().getName() + " { " +
                "origin=(" + origin.getX() + ", " + origin.getY() + ", " + origin.getZ() + "), " +
                "direction=(" + direction.getX() + ", " + direction.getY() + ", " + direction.getZ() + ") }");
    }
    
    /**
     * Creates a new Line.
     * 
     * @param origX
     * @param origY
     * @param origZ
     * @param direcX
     * @param direcY
     * @param direcZ
     */
    public Line( float origX, float origY, float origZ, float direcX, float direcY, float direcZ )
    {
        super();
        
        this.origin = new Point3f( origX, origY, origZ );
        this.direction = new Vector3f( direcX, direcY, direcZ );
    }
    
    /**
     * Creates a new Line.
     * 
     * @param origin the new origin point
     * @param direction the new direction vector
     */
    public Line( Point3f origin, Vector3f direction )
    {
        this( origin.getX(), origin.getY(), origin.getZ(), direction.getX(), direction.getY(), direction.getZ() );
    }
    
    /**
     * Creates a new Line.
     */
    public Line()
    {
        this( 0f, 0f, 0f, 0f, 0f, 0f );
    }
    
    /**
     * Clone constructor.
     */
    public Line( Line template )
    {
        this();
        
        this.set( template );
    }
}
