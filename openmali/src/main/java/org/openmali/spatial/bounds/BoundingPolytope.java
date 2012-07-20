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
package org.openmali.spatial.bounds;

import java.util.List;

import org.openmali.errorhandling.UnsupportedFunction;
import org.openmali.spatial.VertexContainer;
import org.openmali.spatial.bodies.Box;
import org.openmali.spatial.bodies.ConvexHull;
import org.openmali.spatial.bodies.IntersectionFactory;
import org.openmali.spatial.bodies.Plane;
import org.openmali.spatial.bodies.Sphere;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Ray3f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.openmali.vecmath2.Vector4f;

/**
 * A set of planes that prescribe a convex, closed polygonal bounding region.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public class BoundingPolytope extends ConvexHull implements Bounds
{
    /**
     * {@inheritDoc}
     */
    public final BoundsType getType()
    {
        return ( BoundsType.POLYTOPE );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection )
    {
        return ( IntersectionFactory.convexHullIntersectsRay( this, rayOrigin, rayDirection, intersection ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Ray3f ray, Tuple3f intersection )
    {
        return ( intersects( ray.getOrigin(), ray.getDirection(), intersection ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Point3f rayOrigin, Vector3f rayDirection )
    {
        return ( intersects( rayOrigin, rayDirection, null ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Ray3f ray )
    {
        return ( intersects( ray.getOrigin(), ray.getDirection() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Bounds bo )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Bounds[] bos )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * Computes a new BoundingPolytope that bounds the volume
     * created by the intersection of this BoundingPolytope
     * with another Bounds object
     * 
     * @param bo
     */
    public BoundingPolytope intersectNew( Bounds bo )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * compute a new BoundingPolytope that bounds the volume
     * created by the intersection of this BoundingPolytope
     * with an array of Bounds objects
     * 
     * @param bos
     */
    public BoundingPolytope intersectNew( Bounds[] bos )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public Bounds closestIntersection( Bounds[] boundsObjects )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void transform( Bounds bounds, Matrix4f trans )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void transform( Matrix4f trans )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * @param planes
     */
    private static Plane[] convertToPlanes( Vector4f[] planes )
    {
        Plane[] pl = new Plane[ planes.length ];
        for ( int i =0; i < planes.length; i++ )
        {
            pl[ i ] = new Plane( planes[ i ].getX(), planes[ i ].getY(), planes[ i ].getZ(), planes[ i ].getW() );
        }
        
        return ( pl );
    }
    
    /**
     * Sets the bounding planes of this BoundingPolytope.
     */
    public void setPlanes( Vector4f[] planes )
    {
        slabs = convertToPlanes( planes );
    }
    
    /**
     * Gets the bounding planes of this BoundingPolytope.
     */
    public void getPlanes( Vector4f[] into )
    {
        for ( int i = 0; i < slabs.length; i++ )
        {
            into[ i ].set( slabs[ i ].getNormal() );
            into[ i ].setW( slabs[ i ].getD() );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Box boundsObject )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Sphere boundsObject )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Bounds[] bounds )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Bounds boundsObject )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void compute( VertexContainer source )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void compute( List<Tuple3f> coords )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public void compute( Tuple3f[] coords )
    {
        throw new UnsupportedFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return ( super.toString() );
    }
    
    /**
     * Constructs a new BoundingPolytope object.
     */
    public BoundingPolytope()
    {
        super( null );
    }
    
    /**
     * Constructs a new BoundingPolytope object.
     */
    public BoundingPolytope( Vector4f[] planes )
    {
        super( convertToPlanes( planes ) ); 
    }
    
    /**
     * Constructs a new BoundingPolytope object.
     */
    public BoundingPolytope( Bounds bo )
    {
        this();
        
        set( bo );
    }
    
    /**
     * Constructs a new BoundingPolytope object.
     */
    public BoundingPolytope( Bounds[] bos )
    {
        this();
        
        set( bos );
    }
}
