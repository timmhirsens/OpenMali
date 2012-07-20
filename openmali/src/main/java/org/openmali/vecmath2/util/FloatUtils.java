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
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple3f;

/**
 * This class provides static helper methods for floats to be used instead
 * of complex objects.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class FloatUtils
{
    /**
     * @return the squared length of a vector
     */
    public static final float vectorLengthSquared( float vx, float vy, float vz )
    {
        return ( (vx * vx) + (vy * vy) + (vz * vz) );
    }
    
    /**
     * @return the length of a vector
     */
    public static final float vectorLength( float vx, float vy, float vz )
    {
        return ( FastMath.sqrt( vectorLengthSquared( vx, vy, vz ) ) );
    }
    
    /**
     * @return the squared distance of two points
     */
    public static final float pointsDistanceSquared( float px1, float py1, float pz1, float px2, float py2, float pz2 )
    {
        float result = 0.0f;
        
        float d = px1 - px2;
        result += d * d;
        d = py1 - py2;
        result += d * d;
        d = pz1 - pz2;
        result += d * d;
        
        return ( result );
    }
    
    /**
     * @return the distance of two points
     */
    public static final float pointsDistance( float px1, float py1, float pz1, float px2, float py2, float pz2 )
    {
        return ( FastMath.sqrt( pointsDistanceSquared( px1, py1, pz1, px2, py2, pz2) ) );
    }
    
    /**
     * @return the squared distance of two points
     */
    public static final float pointsDistanceSquared( Point3f p1, float px2, float py2, float pz2 )
    {
        float result = 0.0f;
        
        float d = p1.getX() - px2;
        result += d * d;
        d = p1.getY() - py2;
        result += d * d;
        d = p1.getZ() - pz2;
        result += d * d;
        
        return ( result );
    }
    
    /**
     * @return the distance of two points
     */
    public static final float pointsDistance( Point3f p1, float px2, float py2, float pz2 )
    {
        return ( FastMath.sqrt( pointsDistanceSquared( p1, px2, py2, pz2) ) );
    }
    
    /**
     * Sets the result-vector to be the vector cross product of vectors v1 and v2.
     * 
     * @param v1x the first vector
     * @param v1y the first vector
     * @param v1z the first vector
     * @param v2x the second vector
     * @param v2y the second vector
     * @param v2z the second vector
     * @param result the result vector
     * 
     * @return the result vector back again
     */
    public static final < T extends Tuple3f > T cross( float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, T result )
    {
        result.set( v1y * v2z - v1z * v2y,
                    v1z * v2x - v1x * v2z,
                    v1x * v2y - v1y * v2x
                  );
        
        return ( result );
    }
    
    /**
     * Computes the dot product of the vectors v1 and vector v2.
     * 
     * @param v1x the first vector
     * @param v1y the first vector
     * @param v1z the first vector
     * @param v2x the second vector
     * @param v2y the second vector
     * @param v2z the second vector
     */
    public static final float dot( float v1x, float v1y, float v1z, float v2x, float v2y, float v2z )
    {
        return ( v1x * v2x + v1y * v2y + v1z * v2z );
    }
    
    /**
     * Returns the angle in radians between the vectors v1 and v2.
     * The return value is constrained to the range [0,PI].
     * 
     * @param v1x the first vector
     * @param v1y the first vector
     * @param v1z the first vector
     * @param v2x the second vector
     * @param v2y the second vector
     * @param v2z the second vector
     * @return the angle in radians in the range [0,PI]
     */
    public static final float angle( float v1x, float v1y, float v1z, float v2x, float v2y, float v2z )
    {
        float vDot = dot( v1x, v1y, v1z, v2x, v2y, v2z ) / ( vectorLength( v1x, v1y, v1z ) * vectorLength( v2x, v2y, v2z ) );
        
        if ( vDot < -1.0f )
            vDot = -1.0f;
        if ( vDot >  1.0f )
            vDot =  1.0f;
        
        return ( FastMath.acos( vDot ) );
    }
    
    /**
     * Computes the rotation between the vectors v1 ans v2.
     * 
     * @param v1x the first vector
     * @param v1y the first vector
     * @param v1z the first vector
     * @param v2x the second vector
     * @param v2y the second vector
     * @param v2z the second vector
     * @param normalize normalize input vectors (bitmask)
     * @param result the result object
     * 
     * @return the result object back again
     */
    public static final AxisAngle3f computeRotation( float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, int normalize, AxisAngle3f result )
    {
        if ( ( normalize & 3 ) != 0 )
        {
            Tuple3f t = Tuple3f.fromPool();
            
            t.set( v1x, v1y, v1z );
            TupleUtils.normalizeVector( t );
            v1x = t.getX();
            v1y = t.getY();
            v1z = t.getZ();
            
            t.set( v2x, v2y, v2z );
            TupleUtils.normalizeVector( t );
            v2x = t.getX();
            v2y = t.getY();
            v2z = t.getZ();
            
            Tuple3f.toPool( t );
        }
        else if ( ( normalize & 1 ) != 0 )
        {
            Tuple3f t = Tuple3f.fromPool();
            
            t.set( v1x, v1y, v1z );
            TupleUtils.normalizeVector( t );
            v1x = t.getX();
            v1y = t.getY();
            v1z = t.getZ();
            
            Tuple3f.toPool( t );
        }
        else if ( ( normalize & 2 ) != 0 )
        {
            Tuple3f t = Tuple3f.fromPool();
            
            t.set( v2x, v2y, v2z );
            TupleUtils.normalizeVector( t );
            v2x = t.getX();
            v2y = t.getY();
            v2z = t.getZ();
            
            Tuple3f.toPool( t );
        }
        
        cross( v2x, v2y, v2z, v1x, v1y, v1z, result );
        
        float angle = FastMath.acos( dot( v2x, v2y, v2z, v1x, v1y, v1z ) );
        
        result.setAngle( angle );
        
        return ( result );
    }
}
