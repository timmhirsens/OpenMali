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
import org.openmali.spatial.VertexList;
import org.openmali.spatial.bodies.Body;
import org.openmali.spatial.bodies.Box;
import org.openmali.spatial.bodies.ConvexHull;
import org.openmali.spatial.bodies.IntersectionFactory;
import org.openmali.spatial.bodies.Sphere;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Ray3f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;

/**
 * Axis aligned bounding box volumes
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public class BoundingBox extends Box implements Bounds
{
	private static final long serialVersionUID = 6353321413525340703L;
    
    // a temporary vertex list abstractor
    private VertexList vertexList = new VertexList();
    
    /**
     * {@inheritDoc}
     */
    public final BoundsType getType()
    {
        return ( BoundsType.AABB );
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean intersects( Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection )
    {
        return ( IntersectionFactory.boxIntersectsRay( getLower(), getUpper(), rayOrigin, rayDirection, intersection ) );
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
            return ( IntersectionFactory.boxIntersectsBox( (Box)bo, this ) );
        
        if ( bo instanceof Sphere )
            return ( IntersectionFactory.sphereIntersectsBox( (Sphere)bo, this ) );
        
        if ( bo instanceof ConvexHull )
            throw new Error( "ConvexHull not supported yet" );
        
        throw new Error( "unknown Bounds type" );
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
    
    /**
     * {@inheritDoc}
     */
    public void transform( Matrix4f trans )
    {
        // brute force translation, lossy if any rotation is present
        final Tuple3f l = getLower();
        final Tuple3f u = getUpper();
        final float lx = l.getX();
        final float ly = l.getY();
        final float lz = l.getZ();
        final float ux = u.getX();
        final float uy = u.getY();
        final float uz = u.getZ();
        
        Point3f p = Point3f.fromPool();
        
        p.set( lx, ly, lz); trans.transform( p ); setLower( p ); setUpper( p );
        p.set( lx, ly, uz); trans.transform( p ); combine_( p );
        p.set( lx, uy, lz); trans.transform( p ); combine_( p );
        p.set( lx, uy, uz); trans.transform( p ); combine_( p );
        
        p.set( ux, ly, lz); trans.transform( p ); combine_( p );
        p.set( ux, ly, uz); trans.transform( p ); combine_( p );
        p.set( ux, uy, lz); trans.transform( p ); combine_( p );
        p.set( ux, uy, uz); trans.transform( p ); combine_( p );
        
        Point3f.toPool( p );
        
        calcCenter();
    }
    
    /**
     * {@inheritDoc}
     */
    public void transform( Bounds bounds, Matrix4f trans )
    {
        // certainly non-optimal, optimize later
        set( bounds );
        transform( trans );
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Box boundsObject )
    {
        setLower( boundsObject.getLower() );
        setUpper( boundsObject.getUpper() );
        
        calcCenter();
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Sphere boundsObject )
    {
        final Sphere s = boundsObject;
        
        setLower( s.getCenterX() - s.getRadius(), s.getCenterY() - s.getRadius(), s.getCenterZ() - s.getRadius() );
        setUpper( s.getCenterX() + s.getRadius(), s.getCenterY() + s.getRadius(), s.getCenterZ() + s.getRadius() );
        
        calcCenter();
    }
    
    /**
     * {@inheritDoc}
     */
    public void set( Bounds bo )
    {
        if ( bo instanceof Box )
        {
            set( (Box)bo );
        }
        else if ( bo instanceof Sphere )
        {
            set( (Sphere)bo );
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
    public void set( Bounds[] bos )
    {
        if ( bos.length > 0 )
            this.set( bos[ 0 ] );
        
        for ( int i = 1; i < bos.length; i++ )
            combine( (Body)bos[ i ] );
    }
    
    /**
     * Computes the AABB of a set of coordinates.
     * 
     * @param source the vectex-source
     */
    private final void computeAABB( final VertexContainer source )
    {
        final int n = source.getVertexCount();
        
        setLower( Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE );
        setUpper( -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE );
        
        Point3f coord = Point3f.fromPool();
        
        for ( int i = 0; i < n; i++ )
        {
            /*
            if ( source.getVertexCount() != n )
            {
                throw new Error( "We started with size " + n + " and now it is " + source.getVertexCount() ) );
            }
            */
            
            source.getVertex( i, coord );
            
            if ( coord.getX() < lower.getX() )
                lower.setX( coord.getX() );
            /*else */if ( coord.getX() > upper.getX() )
                upper.setX( coord.getX() );
            
            if ( coord.getY() < lower.getY() )
                lower.setY( coord.getY() );
            /*else */if ( coord.getY() > upper.getY() )
                upper.setY( coord.getY() );
            
            if ( coord.getZ() < lower.getZ() )
                lower.setZ( coord.getZ() );
            /*else */if ( coord.getZ() > upper.getZ() )
                upper.setZ( coord.getZ() );
        }
        
        Point3f.toPool( coord );
        
        calcCenter();
    }
    
    /**
     * Computes the AABB of a set of coordinates.
     * 
     * @param source the vectex-source
     */
    public void compute( final VertexContainer source )
    {
        computeAABB( source );
    }
    
    /**
     * Computes the AABB of a set of coordinates.
     * 
     * @param coords a List of coordinates
     */
    public void compute( final List<Tuple3f> coords )
    {
        vertexList.set( coords );
        
        compute( vertexList );
    }
    
    /**
     * Computes the AABB of a set of coordinates.
     * 
     * @param coords a List of coordinates
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
     * Create a new empty bounding box
     */
    public BoundingBox()
    {
    }
    
    /**
     * Creates a new Box
     */
    public BoundingBox( float lowerX, float lowerY, float lowerZ, float upperX, float upperY, float upperZ )
    {
        super( lowerX, lowerY, lowerZ, upperX, upperY, upperZ );
    }
    
    /**
     * Create a new bounding box with lower and upper
     * corners specified
     */
    public BoundingBox( Tuple3f lower, Tuple3f upper )
    {
        super( lower, upper );
    }
    
    /**
     * Create a new bounding box enclosing bounds bo
     */
    public BoundingBox( Bounds bo )
    {
        set( bo );
    }
    
    /**
     * Create a new bounding box enclosing bounds bos
     */
    public BoundingBox( Bounds[] bos )
    {
        set( bos );
    } 
    
    /**
     * Computes the AABB of a set of coordinates.
     * 
     * @param source the vertex source
     * 
     * @return a Box
     */
    public static BoundingBox newAABB( final VertexContainer source )
    {
        BoundingBox bb = new BoundingBox();
        
        bb.compute( source );
        
        return ( bb );
    }
    
    /**
     * Computes the AABB of a set of coordinates.
     * 
     * @param coords a List of coordinates (Vector3fs)
     * 
     * @return a Box
     */
    public static BoundingBox newAABB( final List<Tuple3f> coords )
    {
        BoundingBox bb = new BoundingBox();
        
        bb.compute( coords );
        
        return ( bb );
    }
    
    /**
     * Computes the AABB of a set of coordinates.
     * 
     * @param coords a List of coordinates (Vector3fs)
     * 
     * @return a Box
     */
    public static BoundingBox newAABB( final Tuple3f[] coords )
    {
        BoundingBox bb = new BoundingBox();
        
        bb.compute( coords );
        
        return ( bb );
    }
}
