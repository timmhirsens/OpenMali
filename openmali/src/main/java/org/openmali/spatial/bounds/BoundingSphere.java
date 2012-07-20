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

import org.openmali.spatial.VertexContainer;
import org.openmali.spatial.VertexList;
import org.openmali.spatial.bodies.Body;
import org.openmali.spatial.bodies.Box;
import org.openmali.spatial.bodies.ConvexHull;
import org.openmali.spatial.bodies.IntersectionFactory;
import org.openmali.spatial.bodies.Sphere;
import org.openmali.errorhandling.UnsupportedFunction;
import org.openmali.FastMath;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Ray3f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;

/**
 * A spherical bounding volume. It has two associated values:
 * the center point and the radius of the sphere.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public class BoundingSphere extends Sphere implements Bounds
{
	private static final long serialVersionUID = 4408387188891021551L;
    
    // a temporary vertex list abstractor
    private VertexList vertexList = new VertexList();
    
    /**
     * {@inheritDoc}
     */
    public final BoundsType getType()
    {
        return ( BoundsType.SPHERE );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection )
    {
        return ( IntersectionFactory.sphereIntersectsRay( this, rayOrigin, rayDirection, intersection ) );
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
        return ( intersects( ray, null ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Bounds bo )
    {
        if ( bo instanceof Box )
        {
            return ( IntersectionFactory.sphereIntersectsBox( this, (Box)bo ) );
        }
        else if ( bo instanceof Sphere )
        {
            return ( IntersectionFactory.sphereIntersectsSphere( this, (Sphere)bo ) );
        }
        else if ( bo instanceof ConvexHull )
        {
            throw new Error( "ConvexHull not supported yet" );
        } 
        else
        {
            throw new Error( "unknown bounds type" );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Bounds[] bos )
    {
        for ( int i = 0; i < bos.length; i++ )
        {
            if ( intersects( bos[ i ] ) )
                return ( true );
        }
        
        return ( false );
    }
    
    /**
     * {@inheritDoc}
     */
    public Bounds closestIntersection( Bounds[] boundsObjects )
    {
        throw new UnsupportedFunction();
    }
    
    private Point3f tmpP = new Point3f();
    
    /**
     * {@inheritDoc}
     */
    public void transform( Matrix4f trans )
    {
        tmpP.set( centerX, centerY, centerZ );
        trans.transform( tmpP );
        setCenter( tmpP );
        
        // now scale the radius
        setRadius( getMaxCenterDistance() * trans.getScale() );
    }
    
    /**
     * {@inheritDoc}
     */
    public void transform( Bounds bounds, Matrix4f trans )
    {
        // this can be a non-optimal but correct
        set( bounds );
        transform( trans );
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Box boundsObject )
    {
        Point3f lower = boundsObject.getLower();
        Point3f upper = boundsObject.getUpper();
        this.setCenter( (upper.getX() + lower.getX()) / 2f, (upper.getY() + lower.getY()) / 2f, (upper.getZ() + lower.getZ()) / 2f );
        this.setRadius( lower.distance( upper ) / 2f );
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Sphere boundsObject )
    {
        setCenter( boundsObject.getCenter() );
        setRadius( boundsObject.getRadius() );
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Bounds boundsObject )
    {
        if ( boundsObject instanceof Sphere )
        {
            set( (Sphere)boundsObject );
        }
        else if ( boundsObject instanceof Box )
        {
            set( (Box)boundsObject );
        }
        else if ( boundsObject instanceof ConvexHull )
        {
            throw new Error( "ConvexHull not supported yet" );
        }
        else
        {
            throw new Error( "unknown bounds type: " + boundsObject );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Bounds[] bos )
    {
        if ( bos.length > 0 )
            this.set( bos[ 0 ] );
        
        for ( int i = 1; i < bos.length; i++ )
            combine( (Body)bos[ i ] );
    }
    
    /**
     * Calculates a tight bounding sphere over a set of points in 3D.
     * This method calculates the AABB and calculates the bounding sphere
     * from it.<br>
     * This is somewhat chaper than computing through distances.
     * 
     * @param source the Vertex-source
     * @param sphere the sphere to put values to
     */
    @SuppressWarnings("unused")
    private static final void computeByAABB( final VertexContainer source, final Sphere sphere )
    {
        final int numVerts = source.getVertexCount();
        
        Point3f xmin = Point3f.fromPool( +Float.MAX_VALUE, +Float.MAX_VALUE, +Float.MAX_VALUE );
        Point3f xmax = Point3f.fromPool( -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE );
        Point3f ymin = Point3f.fromPool( +Float.MAX_VALUE, +Float.MAX_VALUE, +Float.MAX_VALUE );
        Point3f ymax = Point3f.fromPool( -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE );
        Point3f zmin = Point3f.fromPool( +Float.MAX_VALUE, +Float.MAX_VALUE, +Float.MAX_VALUE );
        Point3f zmax = Point3f.fromPool( -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE );
        
        Point3f vertex = Point3f.fromPool();
        
        for ( int i = 0; i < numVerts; i++ )
        {
            source.getVertex( i, vertex );
            
            //System.out.println( vertex );
            
            if ( vertex.getX() < xmin.getX() )
            {
                xmin.set( vertex );
            }
            
            if ( vertex.getX() > xmax.getX() )
            {
                xmax.set( vertex );
            }
            
            if ( vertex.getY() < ymin.getY() )
            {
                ymin.set( vertex );
            }
            
            if ( vertex.getY() > ymax.getY() )
            {
                ymax.set( vertex );
            }
            
            if ( vertex.getZ() < zmin.getZ() )
            {
                zmin.set( vertex );
            }
            
            if ( vertex.getZ() > zmax.getZ() )
            {
                zmax.set( vertex );
            }
        }
        
        //System.out.println( xmin + ", " + xmax );
        //System.out.println( ymin + ", " + ymax );
        
        sphere.setCenter( xmin.getX() + (xmax.getX() - xmin.getX()) * 0.5f,
                          ymin.getY() + (ymax.getY() - ymin.getY()) * 0.5f,
                          zmin.getZ() + (zmax.getZ() - zmin.getZ()) * 0.5f );
        
        Vector3f radiusVec = Vector3f.fromPool();
        Point3f  min       = Point3f.fromPool();
        Point3f  max       = Point3f.fromPool();
        
        min.set( xmin.getX(), ymin.getY(), zmin.getZ() );
        max.set( xmax.getX(), ymax.getY(), zmax.getZ() );
        
        radiusVec.sub( max, min );
        
        sphere.setRadius( radiusVec.length() * 0.5f );
        
        Point3f.toPool( max );
        Point3f.toPool( min );
        Vector3f.toPool( radiusVec );
        
        Point3f.toPool( vertex );
        
        Point3f.toPool( xmin );
        Point3f.toPool( xmax );
        
        Point3f.toPool( ymin );
        Point3f.toPool( ymax );
        
        Point3f.toPool( zmin );
        Point3f.toPool( zmax );
    }
    
    /**
     * Calculates a tight bounding sphere over a set of points in 3D.
     * This first computes the "average vertex", which is the sphere's center.
     * Then it searches the maximum (squared) distance of all vertices to the
     * center. The sphere's radius is the sqrt of this max squared distance.
     * 
     * @param source the Vertex-source
     * @param sphere
     */
    private static final void computeExactly( final VertexContainer source, final Sphere sphere )
    {
        final int numVerts = source.getVertexCount();
        
        Point3f vertex = Point3f.fromPool();
        
        /*
         * FIRST PASS:
         * 
         * find the "average vertex", which is the sphere's center...
         */
        Point3f avgVertex = Point3f.fromPool();
        Point3f sum = Point3f.fromPool();
        
        avgVertex.set( 0.0f, 0.0f, 0.0f );
        sum.set( 0.0f, 0.0f, 0.0f );
        
        for ( int i = 0; i < numVerts; i++ )
        {
            source.getVertex( i, vertex );
            
            sum.add( vertex );
            
            // apply a part-result to avoid float-overflows
            if ( i % 100 == 0 )
            {
                sum.div( numVerts );
                
                avgVertex.add( sum );
                sum.set( 0.0f, 0.0f, 0.0f );
            }
        }
        
        sum.div( numVerts );
        
        avgVertex.add( sum );
        sum.set( 0.0f, 0.0f, 0.0f );
        
        sphere.setCenter( avgVertex );
        
        Point3f.toPool( sum );
        Point3f.toPool( avgVertex );
        
        
        /*
         * SECOND PASS:
         * 
         * Find the maximum (squared) distance of all vertices to the center...
         */
        final Point3f center = sphere.getCenter();
        float maxDist_sq = 0.0f;
        
        for ( int i = 0; i < numVerts; i++ )
        {
            source.getVertex( i, vertex );
            
            float dist_sq = vertex.distanceSquared( center );
            if ( dist_sq > maxDist_sq )
                maxDist_sq = dist_sq;
        }
        
        sphere.setRadius( FastMath.sqrt( maxDist_sq ) );
        
        Point3f.toPool( vertex );
    }
    
    /**
     * Calculates a tight bounding sphere over a set of points in 3D.
     * 
     * @param source the Vertex-source
     */
    public void compute( final VertexContainer source )
    {
        computeExactly( source, this );
    }
    
    /**
     * Routine to calculate tight bounding sphere over a set of points in 3D.
     * This contains the routine find_bounding_sphere(),
     * the struct definition, and the globals used for parameters.
     * The abs() of all coordinates must be < Float.MAX_VALUE.<br>
     * <br>
     * Code written by Jack Ritter and Lyle Rains.
     * 
     * @param coords the Vertices
     */
    public void compute( final List<Tuple3f> coords )
    {
        vertexList.set( coords );
        
        compute( vertexList );
    }
    
    /**
     * Routine to calculate tight bounding sphere over a set of points in 3D.
     * This contains the routine find_bounding_sphere(),
     * the struct definition, and the globals used for parameters.
     * The abs() of all coordinates must be < Float.MAX_VALUE.<br>
     * <br>
     * Code written by Jack Ritter and Lyle Rains.
     * 
     * @param coords the Vertices
     */
    public void compute( final Tuple3f[] coords )
    {
        vertexList.set( coords );
        
        compute( vertexList );
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
     * Constructs a new BoundingShpere object.
     * 
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param radius
     */
    public BoundingSphere( float centerX, float centerY, float centerZ, float radius )
    {
        super( centerX, centerY, centerZ, radius );
    }
    
    /**
     * Constructs a new BoundingShpere object.
     */
    public BoundingSphere( Tuple3f center, float radius )
    {
        this( center.getX(), center.getY(), center.getZ(), radius );
    }
    
    /**
     * Constructs a new BoundingShpere object.
     */
    public BoundingSphere()
    {
        this( 0f, 0f, 0f, 0f );
    }
    
    /**
     * Constructs a new BoundingShpere object.
     */
    public BoundingSphere( Bounds bo )
    {
        this();
        
        set( bo );
    }
    
    /**
     * Constructs a new BoundingShpere object.
     */
    public BoundingSphere( Bounds[] bos )
    {
        this();
        
        set( bos );
    }
    
    /**
     * Routine to calculate tight bounding sphere over a set of points in 3D.
     * This contains the routine find_bounding_sphere(),
     * the struct definition, and the globals used for parameters.
     * The abs() of all coordinates must be < Float.MAX_VALUE.
     * 
     * Code written by Jack Ritter and Lyle Rains.<br>
     * <br>
     * @param source the Vertex-source
     * 
     * @return the BoundingSphere
     */
    public static BoundingSphere newBoundingSphere( final VertexContainer source )
    {
        BoundingSphere bs = new BoundingSphere();
        
        bs.compute( source );
        
        return ( bs );
    }
    
    /**
     * Routine to calculate tight bounding sphere over a set of points in 3D.
     * This contains the routine find_bounding_sphere(),
     * the struct definition, and the globals used for parameters.
     * The abs() of all coordinates must be < Float.MAX_VALUE.
     * 
     * Code written by Jack Ritter and Lyle Rains.<br>
     * <br>
     * @param coords the vertex list
     * 
     * @return the BoundingSphere
     */
    public static BoundingSphere newBoundingSphere( final List<Tuple3f> coords )
    {
        BoundingSphere bs = new BoundingSphere();
        
        bs.compute( coords );
        
        return ( bs );
    }
    
    /**
     * Routine to calculate tight bounding sphere over a set of points in 3D.
     * This contains the routine find_bounding_sphere(),
     * the struct definition, and the globals used for parameters.
     * The abs() of all coordinates must be < Float.MAX_VALUE.
     * 
     * Code written by Jack Ritter and Lyle Rains.<br>
     * <br>
     * @param coords the vertex list
     * 
     * @return the BoundingSphere
     */
    public static BoundingSphere newBoundingSphere( final Tuple3f[] coords )
    {
        BoundingSphere bs = new BoundingSphere();
        
        bs.compute( coords );
        
        return ( bs );
    }
}
