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
package org.openmali.vecmath2.util;

import org.openmali.FastMath;
import org.openmali.vecmath2.AxisAngle3f;
import org.openmali.vecmath2.Tuple3f;

/**
 * Some functions which can operate on Tuples, since some of the functions for
 * points are not available to vectors in general.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 * @author Amos Wenger (aka BlueSky)
 */
public final class TupleUtils
{
    private TupleUtils()
    {
    }
    
    /**
     * Normalize the input vector.
     * 
     * @param vector the input vector to normalize
     * 
     * @return the input vector back again
     */
    public static final < T extends Tuple3f > T normalizeVector( T vector )
    {
        final float l = vectorLength( vector );
        
        // zero-div may occur.
        vector.divX( l );
        vector.divY( l );
        vector.divZ( l );
        
        return ( vector );
    }
    
    public static float distanceSquared( Tuple3f a, Tuple3f b )
    {
        float dx = a.getX() - b.getX();
        float dy = a.getY() - b.getY();
        float dz = a.getZ() - b.getZ();
        
        return ( (dx * dx) + (dy * dy) + (dz * dz) );
    }
    
    /**
     * @return the squared length of a vector
     */
    public static final float vectorLengthSquared( Tuple3f v )
    {
        return ( FloatUtils.vectorLengthSquared( v.getX(), v.getY(), v.getZ() ) );
    }
    
    /**
     * @return the length of a vector
     */
    public static final float vectorLength( Tuple3f v )
    {
        return ( FloatUtils.vectorLength( v.getX(), v.getY(), v.getZ() ) );
    }
    
    /**
     * Sets the result-vector to be the vector cross product of vectors v1 and v2.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @param result the result vector
     * 
     * @return the result vector back again
     */
    public static final < T extends Tuple3f > T cross( Tuple3f v1, Tuple3f v2, T result )
    {
        // store in tmp once for aliasing-safty
        // i.e. safe when a.cross(a, b)
        result.set( v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                    v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                    v1.getX() * v2.getY() - v1.getY() * v2.getX()
                  );
        
        return ( result );
    }
    
    /**
     * Computes the dot product of the vectors v1 and vector v2.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     */
    public static final float dot( Tuple3f v1, Tuple3f v2 )
    {
        return ( v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ() );
    }
    
    /**
     * Returns the angle in radians between the vectors v1 and v2.
     * The return value is constrained to the range [0,PI].
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the angle in radians in the range [0,PI]
     */
    public static final float angle( Tuple3f v1, Tuple3f v2 )
    {
        float vDot = dot( v1, v2 ) / ( vectorLength( v1 ) * vectorLength( v2 ) );
        
        if ( vDot < -1.0f )
            vDot = -1.0f;
        if ( vDot >  1.0f )
            vDot =  1.0f;
        
        return ( FastMath.acos( vDot ) );
    }
    
    public static int compareTuple( Tuple3f ta, Tuple3f tb )
    {
        if ( ta.getX() < tb.getX() )
            return ( -1 );
        else if ( ta.getX() > tb.getX() )
            return ( 1 );
        
        else if ( ta.getY() < tb.getY() )
            return ( -1 );
        else if ( ta.getY() > tb.getY() )
            return ( 1 );
        
        if ( ta.getZ() < tb.getZ() )
            return ( -1 );
        else if ( ta.getZ() > tb.getZ() )
            return ( 1 );
        
        else
            return ( 0 );
    }
    
    /**
     * Limits the tuple in a rectangle.
     * 
     * @param tuple
     * @param minx
     * @param miny
     * @param maxx
     * @param maxy
     * 
     * @return true if was outside
     */
    public static boolean limit( Tuple3f tuple, float minx, float miny, float maxx, float maxy )
    {
        
        boolean outside = false;
        
        if ( tuple.getX() < minx )
        {
            tuple.setX( minx );
            outside = true;
        }
        else if ( tuple.getX() > maxx )
        {
            tuple.setX( maxx );
            outside = true;
        }
        
        if ( tuple.getY() < miny )
        {
            tuple.setY( miny );
            outside = true;
        }
        else if ( tuple.getY() > maxy )
        {
            tuple.setY( maxy );
            outside = true;
        }
        
        return ( outside );
    }
    
    /**
     * Limits the tuple into a rectangle (0, 0, maxx, maxy).
     * 
     * @param tuple
     * @param maxx
     * @param maxy
     * 
     * @return true if was outside
     */
    public static boolean limit( Tuple3f tuple, float maxx, float maxy )
    {
        boolean outside = false;
        
        if ( tuple.getX() < 0f )
        {
            tuple.setX( 0f );
            outside = true;
        }
        else if ( tuple.getX() > maxx )
        {
            tuple.setX( maxx );
            outside = true;
        }
        
        if ( tuple.getY() < 0f )
        {
            tuple.setY( 0f );
            outside = true;
        }
        else if ( tuple.getY() > maxy )
        {
            tuple.setY( maxy );
            outside = true;
        }
        
        return ( outside );
    }
    
    /**
     * Computes the rotation between the vectors v1 ans v2.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @param normalize normalize input vectors (bitmask)
     * @param result the result object
     * 
     * @return the result object back again
     */
    public static final AxisAngle3f computeRotation( Tuple3f v1, Tuple3f v2, int normalize, AxisAngle3f result )
    {
        if ( ( normalize & 1 ) != 0 )
        {
            normalizeVector( v1 );
        }
        if ( ( normalize & 2 ) != 0 )
        {
            normalizeVector( v2 );
        }
        
        cross( v2, v1, result );
        
        float angle = FastMath.acos( dot( v2, v1 ) );
        
        result.setAngle( angle );
        
        return ( result );
    }
}
