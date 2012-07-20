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
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Ray3f;
import org.openmali.vecmath2.Tuple2f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;

/**
 * This class provides static methods to test different bodies for intersection.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public final class IntersectionFactory
{
    /**
     * Tests two Spheres for intersection.
     * 
     * @param x1
     * @param y1
     * @param z1
     * @param r1
     * @param x2
     * @param y2
     * @param z2
     * @param r2
     * 
     * @return true, if the two Bodies intersect
     */
    public static boolean sphereIntersectsSphere( float x1, float y1, float z1, float r1,
                                                  float x2, float y2, float z2, float r2 )
    {
        final float dx = x1 - x2;
        final float dy = y1 - y2;
        final float dz = z1 - z2;
        final float d_sq = (dx * dx) + (dy * dy) + (dz * dz);
        
        if ( d_sq > ( (r1 + r2) * (r1 + r2) ) )
            return ( false );
        
        return ( true );
    }
    
    /**
     * Tests two Spheres for intersection.
     * 
     * @param sphere1
     * @param x2
     * @param y2
     * @param z2
     * @param r2
     * 
     * @return true, if the two Bodies intersect
     */
    public static boolean sphereIntersectsSphere( Sphere sphere1, float x2, float y2, float z2, float r2 )
    {
        return ( sphereIntersectsSphere( sphere1.getCenterX(), sphere1.getCenterY(), sphere1.getCenterZ(), sphere1.getRadius(),
                                        x2, y2, z2, r2 ) );
    }
    
    /**
     * Tests two Spheres for intersection.
     * 
     * @param sphere1
     * @param sphere2
     * 
     * @return true, if the two Bodies intersect
     */
    public static boolean sphereIntersectsSphere( Sphere sphere1, Sphere sphere2 )
    {
        return ( sphereIntersectsSphere( sphere1.getCenterX(), sphere1.getCenterY(), sphere1.getCenterZ(), sphere1.getRadius(),
                                        sphere2.getCenterX(), sphere2.getCenterY(), sphere2.getCenterZ(), sphere2.getRadius() ) );
    }
    
    private static float distance( float point, float min, float max )
    {
        if ( point < min )
            return ( min - point );
        else if ( point > max )
            return ( point - max );
        else
            return ( 0 );
    }
    
    /**
     * Tests a Sphere and a Box for intersection.
     * 
     * @param sphereX
     * @param sphereY
     * @param sphereZ
     * @param sphereR
     * @param boxLowerX
     * @param boxLowerY
     * @param boxLowerZ
     * @param boxUpperX
     * @param boxUpperY
     * @param boxUpperZ
     * 
     * @return true, if the two Bodies intersect
     */
    public static final boolean sphereIntersectsBox( float sphereX, float sphereY, float sphereZ, float sphereR,
                                                     float boxLowerX, float boxLowerY, float boxLowerZ,
                                                     float boxUpperX, float boxUpperY, float boxUpperZ )
    {
        float d = 0.0f;
        float s;
        
        //find the square of the distance from the sphere to the box...
        
        s = distance( sphereX, boxLowerX, boxUpperX );
        d += s * s;
        
        s = distance( sphereY, boxLowerY, boxUpperY );
        d += s * s;
        
        s = distance( sphereZ, boxLowerZ, boxUpperZ );
        d += s * s;
        
        return ( d <= sphereR * sphereR );
    }
    
    /**
     * Tests a Sphere and a Box for intersection.
     * 
     * @param sphereX
     * @param sphereY
     * @param sphereZ
     * @param sphereR
     * @param boxLower
     * @param boxUpper
     * 
     * @return true, if the two Bodies intersect
     */
    public static final boolean sphereIntersectsBox( float sphereX, float sphereY, float sphereZ, float sphereR,
                                                     Tuple3f boxLower, Tuple3f boxUpper )
    {
        return ( sphereIntersectsBox( sphereX, sphereY, sphereZ, sphereR, boxLower.getX(), boxLower.getY(), boxLower.getZ(), boxUpper.getX(), boxUpper.getY(), boxUpper.getZ() ) );
    }
    
    /**
     * Tests a Sphere and a Box for intersection.
     * 
     * @param sphere
     * @param boxLower
     * @param boxUpper
     * 
     * @return true, if the two Bodies intersect
     */
    public static final boolean sphereIntersectsBox( Sphere sphere, Tuple3f boxLower, Tuple3f boxUpper )
    {
        return ( sphereIntersectsBox( sphere, boxLower, boxUpper ) );
    }
    
    /**
     * Tests a Sphere and a Box for intersection.
     * 
     * @param sphereX
     * @param sphereY
     * @param sphereZ
     * @param sphereR
     * @param box
     * 
     * @return true, if the two Bodies intersect
     */
    public static final boolean sphereIntersectsBox( float sphereX, float sphereY, float sphereZ, float sphereR, Box box )
    {
        return ( sphereIntersectsBox( sphereX, sphereY, sphereZ, sphereR, box.getLower(), box.getUpper() ) );
    }
    
    /**
     * Tests a Sphere and a Box for intersection.
     * 
     * @param sphere
     * @param box
     * 
     * @return true, if the two Bodies intersect
     */
    public static final boolean sphereIntersectsBox( Sphere sphere, Box box )
    {
        return ( sphereIntersectsBox( sphere, box.getLower(), box.getUpper() ) );
    }
    
    /**
     * Tests two Boxes for intersection.
     * 
     * @param lowerX1
     * @param lowerY1
     * @param lowerZ1
     * @param upperX1
     * @param upperY1
     * @param upperZ1
     * @param lowerX2
     * @param lowerY2
     * @param lowerZ2
     * @param upperX2
     * @param upperY2
     * @param upperZ2
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsBox( float lowerX1, float lowerY1, float lowerZ1,
                                            float upperX1, float upperY1, float upperZ1,
                                            float lowerX2, float lowerY2, float lowerZ2,
                                            float upperX2, float upperY2, float upperZ2 )
    {
        if ( upperX1 < lowerX2 )
            return ( false );
        
        if ( upperY1 < lowerY2 )
            return ( false );
        
        if ( upperZ1 < lowerZ2 )
            return ( false );
        
        if ( upperX2 < lowerX1 )
            return ( false );
        
        if ( upperY2 < lowerY1 )
            return ( false );
        
        if ( upperZ2 < lowerZ1 )
            return ( false );
        
        return ( true );
    }
    
    /**
     * Tests two Boxes for intersection.
     * 
     * @param lower1
     * @param upper1
     * @param lower2
     * @param upper2
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsBox( Tuple3f lower1, Tuple3f upper1, Tuple3f lower2, Tuple3f upper2 )
    {
        return ( boxIntersectsBox( lower1.getX(), lower1.getY(), lower1.getZ(), upper1.getX(), upper1.getY(), upper1.getZ(),
                                  lower2.getX(), lower2.getY(), lower2.getZ(), upper2.getX(), upper2.getY(), upper2.getZ() ) );
    }
    
    /**
     * Tests two Boxes for intersection.
     * 
     * @param box1
     * @param box2
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsBox( Box box1, Box box2 )
    {
        return ( boxIntersectsBox( box1.getLower(), box1.getUpper(), box2.getLower(), box2.getUpper() ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere.
     * 
     * @param rayOrigin
     * @param rayDirection
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRay( Tuple3f sphereCenter, final float sphereRadiusSquared,
                                               Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection )
    {
        /*
         * ray-sphere intersection test from Graphics Gems p.388
         * TODO: There is a bug in this Graphics Gem. If the origin of the ray
         *       is *inside* the sphere being tested, it reports the wrong
         *       intersection location. This code has a fix for the bug.
         */
        
        // notation:
        // point E  = rayOrigin
        // point O  = sphere center
        Vector3f EO = Vector3f.fromPool();
        EO.set( sphereCenter );
        EO.sub( rayOrigin );
        
        Vector3f V = Vector3f.fromPool();
        V.set( rayDirection );
        
        float dist2 = EO.lengthSquared();
        
        //final float radius_sq = sphereRadius * sphereRadius;
        
        // Bug Fix For Gem, if origin is *inside* the sphere, invert the
        // direction vector so that we get a valid intersection location.
        if ( dist2 < sphereRadiusSquared )
        {
            V.negate();
        }
        
        float v = EO.dot( V );
        // need to multiply the dot product result by |EO| (magnitude of EO vector)
        // in order to find correct distance of side of triangle.
        // formula:  EO.dot( V ) = v / ( EO.length() )
        // NOTE: the distance "v" is different than direction Vector V in diagram.
        v *= FastMath.sqrt( dist2 );
        
        final float dist = sphereRadiusSquared - ( dist2 - ( v * v ) );
        
        boolean result = false;
        
        if ( dist > 0.0f )
        {
            if ( intersection != null )
            {
                float d = FastMath.sqrt( dist );
                V.scale( v - d );
                intersection.add( rayOrigin, V );
            }
            
            result = true;
        }
        
        Vector3f.toPool( V );
        Vector3f.toPool( EO );
        
        return ( result );
    }
    
    /**
     * Does a ray intersection test with a Sphere.
     * 
     * @param sphereCenter
     * @param sphereRadiusSquared
     * @param rayOrigin
     * @param rayDirection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRay( Tuple3f sphereCenter, float sphereRadiusSquared, Point3f rayOrigin, Vector3f rayDirection )
    {
        return ( sphereIntersectsRay( sphereCenter, sphereRadiusSquared, rayOrigin, rayDirection, null ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere.
     * 
     * @param sphereCenter
     * @param sphereRadiusSquared
     * @param ray
     * @param intersect
     * 
     * @return true for an intersection
     */
    public static boolean intersectsRay( Tuple3f sphereCenter, float sphereRadiusSquared, Ray3f ray, Tuple3f intersect )
    {
        return ( sphereIntersectsRay( sphereCenter, sphereRadiusSquared, ray.getOrigin(), ray.getDirection(), intersect ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere.
     * 
     * @param sphere
     * @param rayOrigin
     * @param rayDirection
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRay( Sphere sphere, Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection )
    {
        return ( sphereIntersectsRay( sphere.getCenter(), sphere.getRadiusSquared(), rayOrigin, rayDirection, intersection ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere.
     * 
     * @param sphere
     * @param rayOrigin
     * @param rayDirection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRay( Sphere sphere, Point3f rayOrigin, Vector3f rayDirection )
    {
        return ( sphereIntersectsRay( sphere, rayOrigin, rayDirection, null ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere.
     * 
     * @param sphere
     * @param ray
     * @param intersect
     * 
     * @return true for an intersection
     */
    public static boolean intersectsRay( Sphere sphere, Ray3f ray, Tuple3f intersect )
    {
        return ( sphereIntersectsRay( sphere, ray.getOrigin(), ray.getDirection(), intersect ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere.
     * 
     * @param sphere
     * @param ray
     * 
     * @return true for an intersection
     */
    public static boolean intersectsRay( Sphere sphere, Ray3f ray )
    {
        return ( sphereIntersectsRay( sphere, ray.getOrigin(), ray.getDirection(), null ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @param sphereCenter
     * @param sphereRadiusSquared
     * @param rayOrigin
     * @param rayDirection
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRayInFront( Tuple3f sphereCenter, float sphereRadiusSquared, Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection )
    {
        Vector3f sect = Vector3f.fromPool();
        boolean hit = sphereIntersectsRay( sphereCenter, sphereRadiusSquared, rayOrigin, rayDirection, sect );
        
        boolean result = false;
        
        if (hit)
        {
            Vector3f dir = Vector3f.fromPool();
            dir.sub( sect, rayOrigin );
            
            float dot = dir.dot( rayDirection );
            
            if ( dot >= 0 )
            { // then it's in front!
                if ( intersection != null )
                {
                    intersection.set( sect );
                }
                
                result = true;
            }
            
            Vector3f.toPool( dir );
        }
        
        Vector3f.toPool( sect );
        
        return ( result );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @param sphereCenter
     * @param sphereRadiusSquared
     * @param ray
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRayInFront( Tuple3f sphereCenter, float sphereRadiusSquared, Ray3f ray, Tuple3f intersection )
    {
        return ( sphereIntersectsRayInFront( sphereCenter, sphereRadiusSquared, ray.getOrigin(), ray.getDirection(), intersection ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @param sphereCenter
     * @param sphereRadiusSquared
     * @param rayOrigin
     * @param rayDirection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRayInFront( Tuple3f sphereCenter, float sphereRadiusSquared, Point3f rayOrigin, Vector3f rayDirection )
    {
        return ( sphereIntersectsRayInFront( sphereCenter, sphereRadiusSquared, rayOrigin, rayDirection, null ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @return true for an intersection
     * 
     * @param sphereCenter
     * @param sphereRadiusSquared
     * @param ray
     */
    public static boolean sphereIntersectsRayInFront( Tuple3f sphereCenter, float sphereRadiusSquared, Ray3f ray )
    {
        return ( sphereIntersectsRayInFront( sphereCenter, sphereRadiusSquared, ray, null ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @param sphere
     * @param rayOrigin
     * @param rayDirection
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRayInFront( Sphere sphere, Point3f rayOrigin, Vector3f rayDirection, Tuple3f intersection )
    {
        return ( sphereIntersectsRayInFront( sphere.getCenter(), sphere.getRadiusSquared(), rayOrigin, rayDirection, null ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @param sphere
     * @param ray
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRayInFront( Sphere sphere, Ray3f ray, Tuple3f intersection )
    {
        return ( sphereIntersectsRayInFront( sphere, ray.getOrigin(), ray.getDirection(), intersection ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @param sphere
     * @param rayOrigin
     * @param rayDirection
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRayInFront( Sphere sphere, Point3f rayOrigin, Vector3f rayDirection )
    {
        return ( sphereIntersectsRayInFront( sphere, rayOrigin, rayDirection, null ) );
    }
    
    /**
     * Does a ray intersection test with a Sphere and checks,
     * if the intersection is in front.
     * 
     * @param sphere
     * @param ray
     * 
     * @return true for an intersection
     */
    public static boolean sphereIntersectsRayInFront( Sphere sphere, Ray3f ray )
    {
        return ( sphereIntersectsRayInFront( sphere, ray, null ) );
    }
    
    private static final float EPSILON_ANGLE = 0.00001f;
    
    // min is inside x, max is inside y
    private static boolean boxIntersectsRayStep( Tuple2f xMin_yMax, float vd, float vn )
    {
        if ( Math.abs( vd ) < EPSILON_ANGLE )
        {
            if ( vn > 0.0f )
                return ( false );
            
            return ( true ); // Direction is parallel to the slab (no intersection)
        }
        
        /* ray not parallel - get distance to plane */
        float t = -vn / vd;
        
        if ( vd < 0.0f )
        {
            /* front face - T is a near point */
            if ( t > xMin_yMax.getY() ) 
                return ( false );
            
            if ( t > xMin_yMax.getX() )
            {
                /* hit near face */
                xMin_yMax.setX( t );
            }
        }
        else
        {
            /* back face - T is a far point */
            if ( t < xMin_yMax.getX() ) 
                return ( false );
            
            if ( t < xMin_yMax.getY() )
            {
                /* hit far face */
                xMin_yMax.setY( t );
            }
        }
        
        return ( true );
    }
    
    /*
    private static float side( R, L )
    {
        return ( R2 * L3 + R5 * L1 + R4 * L0 + R1 * L5 + R0 * L4 + R3 * L2 );
    }
    */
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLowerX
     * @param boxLowerY
     * @param boxLowerZ
     * @param boxUpperX
     * @param boxUpperY
     * @param boxUpperZ
     * @param origin
     * @param dir
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( float boxLowerX, float boxLowerY, float boxLowerZ,
                                            float boxUpperX, float boxUpperY, float boxUpperZ,
                                            Point3f origin, Vector3f dir, Tuple3f intersection )
    {
        final float rayOriX = origin.getX();
        final float rayOriY = origin.getY();
        final float rayOriZ = origin.getZ();
        final float rayDirX = dir.getX();
        final float rayDirY = dir.getY();
        final float rayDirZ = dir.getZ();
        
        Tuple2f tmp = Tuple2f.fromPool( Float.MIN_VALUE, Float.MAX_VALUE );
        
        boolean hit = boxIntersectsRayStep( tmp, -rayDirX, boxLowerX - rayOriX ) &&
                      boxIntersectsRayStep( tmp, -rayDirY, boxLowerY - rayOriY ) &&
                      boxIntersectsRayStep( tmp, -rayDirZ, boxLowerZ - rayOriZ ) &&
                      boxIntersectsRayStep( tmp, +rayDirX, rayOriX - boxUpperX ) &&
                      boxIntersectsRayStep( tmp, +rayDirY, rayOriY - boxUpperY ) &&
                      boxIntersectsRayStep( tmp, +rayDirZ, rayOriZ - boxUpperZ );
        
        float tnear = tmp.getX();
        float tfar  = tmp.getY();
        
        Tuple2f.toPool( tmp );
        
        if ( !hit )
            return ( false );
        
        float tresult = 0.0f;
        if ( tnear >= 0.0f )
        {
            /* outside, hitting front face */
            tresult = tnear;
        }
        else if ( tfar >= 0.0f )
        {
            /* inside, hitting back face */
            tresult = tfar;
        }
        else
        {
            return ( false );
        }
        
        if ( intersection != null )
        {
            intersection.scaleAdd( tresult, dir, origin );
        }
        
        return ( true );
    }
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLowerX
     * @param boxLowerY
     * @param boxLowerZ
     * @param boxUpperX
     * @param boxUpperY
     * @param boxUpperZ
     * @param ray
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( float boxLowerX, float boxLowerY, float boxLowerZ,
                                            float boxUpperX, float boxUpperY, float boxUpperZ,
                                            Ray3f ray, Tuple3f intersection )
    {
        return ( boxIntersectsRay( boxLowerX, boxLowerY, boxLowerZ,
                                  boxUpperX, boxUpperY, boxUpperZ,
                                  ray.getOrigin(), ray.getDirection(), intersection ) );
    }
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLower
     * @param boxUpper
     * @param origin
     * @param dir
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( Tuple3f boxLower, Tuple3f boxUpper,
                                            Point3f origin, Vector3f dir, Tuple3f intersection )
    {
        return ( boxIntersectsRay( boxLower.getX(), boxLower.getY(), boxLower.getZ(),
                                  boxUpper.getX(), boxUpper.getY(), boxUpper.getZ(),
                                  origin, dir, intersection ) );
    }
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLower
     * @param boxUpper
     * @param ray
     * @param intersection
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( Tuple3f boxLower, Tuple3f boxUpper,
                                            Ray3f ray, Tuple3f intersection )
    {
        return ( boxIntersectsRay( boxLower, boxUpper,
                                  ray.getOrigin(), ray.getDirection(), intersection ) );
    }
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLowerX
     * @param boxLowerY
     * @param boxLowerZ
     * @param boxUpperX
     * @param boxUpperY
     * @param boxUpperZ
     * @param origin
     * @param dir
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( float boxLowerX, float boxLowerY, float boxLowerZ,
                                            float boxUpperX, float boxUpperY, float boxUpperZ,
                                            Point3f origin, Vector3f dir )
    {
        return ( boxIntersectsRay( boxLowerX, boxLowerY, boxLowerZ, boxUpperX, boxUpperY, boxUpperZ, origin, dir, null ) );
    }
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLowerX
     * @param boxLowerY
     * @param boxLowerZ
     * @param boxUpperX
     * @param boxUpperY
     * @param boxUpperZ
     * @param ray
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( float boxLowerX, float boxLowerY, float boxLowerZ,
                                            float boxUpperX, float boxUpperY, float boxUpperZ,
                                            Ray3f ray )
    {
        return ( boxIntersectsRay( boxLowerX, boxLowerY, boxLowerZ,
                                  boxUpperX, boxUpperY, boxUpperZ,
                                  ray.getOrigin(), ray.getDirection() ) );
    }
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLower
     * @param boxUpper
     * @param origin
     * @param dir
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( Tuple3f boxLower, Tuple3f boxUpper,
                                            Point3f origin, Vector3f dir )
    {
        return ( boxIntersectsRay( boxLower.getX(), boxLower.getX(), boxLower.getZ(),
                                  boxUpper.getX(), boxUpper.getY(), boxUpper.getZ(),
                                  origin, dir ) );
    }
    
    /**
     * Tests a Box for intersection with a Ray.
     * 
     * @param boxLower
     * @param boxUpper
     * @param ray
     * 
     * @return true for an intersection
     */
    public static boolean boxIntersectsRay( Tuple3f boxLower, Tuple3f boxUpper, Ray3f ray )
    {
        return ( boxIntersectsRay( boxLower, boxUpper,
                                  ray.getOrigin(), ray.getDirection() ) );
    }
    
    public static boolean convexHullIntersectsRay( ConvexHull hull, Point3f origin, Vector3f dir, Tuple3f intersection )
    {
        float tfar = Float.POSITIVE_INFINITY;
        float tnear = Float.NEGATIVE_INFINITY;
        
        for ( int i=0; i< hull.slabs.length; ++i )
        { 
            float vd = hull.slabs[ i ].getNormal().dot( dir );
            float vn = hull.slabs[ i ].distanceTo( origin );
            if ( Math.abs( vd ) < EPSILON_ANGLE )
            {
                if (vn > 0)
                    return ( false );
                
                return ( true ); // Direction is parallel to the slab (no intersection)
            }
            
            /* ray not parallel - get distance to plane */
            float t = -vn/vd;
            
            if ( vd < 0.0f )
            {
                /* front face - T is a near point */
                if ( t > tfar ) 
                    return ( false );
                
                if ( t > tnear )
                {
                    /* hit near face */
                    tnear = t ;
                }
            }
            else
            {
                /* back face - T is a far point */
                if ( t < tnear ) 
                    return ( false );
                
                if ( t < tfar )
                {
                    /* hit far face */
                    tfar = t;
                }
            }
        }
        
        /* survived all tests */
        
        float tresult = 0;
        if ( tnear >= 0.0f )
        {
            /* outside, hitting front face */
            tresult = tnear;
        }
        else if ( tfar >= 0.0f )
        {
            /* inside, hitting back face */
            tresult = tfar;
        }
        else
        {
            return ( false );
        }
        
        if ( intersection != null )
            intersection.scaleAdd( tresult, dir, origin );
        
        return ( true );
    }
    
    public static boolean convexHullIntersectsRay( ConvexHull hull, Ray3f ray, Tuple3f intersection )
    {
        return ( convexHullIntersectsRay( hull, ray.getOrigin(), ray.getDirection(), intersection ) );
    }
}
