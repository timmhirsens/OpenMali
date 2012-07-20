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
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Tuple3f;

/**
 * Util class for Maths.
 *
 * @author Marvin Froehlich (aka Qudus)
 * @author Amos Wenger (aka BlueSky)
 */
public class MatrixUtils
{
    /**
     * Converts a Matrix3f to a Tuple3f with Euler angles.
     * 
     * @param matrix the Matrix3f to be converted
     */
    public static void matrixToEuler( Matrix3f matrix, Tuple3f euler )
    {
        if ( matrix.m10() == 1.0f )
        {
            euler.setX( 0.0f );
            euler.setY( FastMath.atan2( matrix.m02(), matrix.m22() ) );
            euler.setZ( FastMath.asin( -matrix.m10() ) );
        }
        else if ( matrix.m10() == -1.0f )
        {
            euler.setX( 0.0f );
            euler.setY( FastMath.atan2( matrix.m02(), matrix.m22() ) );
            euler.setZ( FastMath.asin( -matrix.m10() ) );
        }
        else
        {
            euler.setX( FastMath.atan2( -matrix.m12(), matrix.m11() ) );
            euler.setY( FastMath.atan2( -matrix.m20(), matrix.m00() ) );
            euler.setZ( FastMath.asin( matrix.m10() ) );
        }
    }

    /**
     * Converts a Matrix3f to a Tuple3f with Euler angles.
     * 
     * @param matrix the Matrix3f to be converted
     * 
     * @return the Vector3f containing the euler angles
     */
    public static Tuple3f matrixToEuler( Matrix3f matrix )
    {
        Tuple3f euler = new Tuple3f();
        
        matrixToEuler( matrix, euler );
        
        return ( euler );
    }

    /**
     * Converts a Matrix4f to a Tuple3f with Euler angles.
     * 
     * @param matrix the Matrix4f to be converted
     */
    public static void matrixToEuler( Matrix4f matrix, Tuple3f euler )
    {
        if ( matrix.m10() == 1.0f )
        {
            euler.setX( 0.0f );
            euler.setY( FastMath.atan2( matrix.m02(), matrix.m22() ) );
            euler.setZ( FastMath.asin( -matrix.m10() ) );
        }
        else if ( matrix.m10() == -1.0f )
        {
            euler.setX( 0.0f );
            euler.setY( FastMath.atan2( matrix.m02(), matrix.m22() ) );
            euler.setZ( FastMath.asin( -matrix.m10() ) );
        }
        else
        {
            euler.setX( FastMath.atan2( -matrix.m12(), matrix.m11() ) );
            euler.setY( FastMath.atan2( -matrix.m20(), matrix.m00() ) );
            euler.setZ( FastMath.asin( matrix.m10() ) );
        }
    }

    /**
     * Converts a Matrix4f to a Vector3f with Euler angles.
     * 
     * @param matrix the Matrix4f to be converted
     * 
     * @return the Tuple3f containing the euler angles
     */
    public static Tuple3f matrixToEuler( Matrix4f matrix )
    {
        Tuple3f euler = new Tuple3f();
        
        matrixToEuler( matrix, euler );
        
        return ( euler );
    }

    /**
     * Converts Euler angles to a Matrix3f.
     * 
     * @param eulerX the x-Euler-angle
     * @param eulerY the y-Euler-angle
     * @param eulerZ the z-Euler-angle
     * @param matrix the Matrix3f instance to write rotational values to
     */
    public static void eulerToMatrix3f( float eulerX, float eulerY, float eulerZ, Matrix3f matrix )
    {
        final float sx = FastMath.sin( eulerX );
        final float sy = FastMath.sin( eulerY );
        final float sz = FastMath.sin( eulerZ );
        final float cx = FastMath.cos( eulerX );
        final float cy = FastMath.cos( eulerY );
        final float cz = FastMath.cos( eulerZ );
        
        matrix.set( 0, 0, cy * cz );
        matrix.set( 0, 1, -( cx * sz ) + ( sx * sy * cz ) );
        matrix.set( 0, 2, ( sx * sz ) + ( cx * sy * cz ) );
        matrix.set( 1, 0, cy * sz );
        matrix.set( 1, 1, ( cx * cz ) + ( sx * sy * sz ) );
        matrix.set( 1, 2, -( sx * cz ) + ( cx * sy * sz ) );
        matrix.set( 2, 0, -sy );
        matrix.set( 2, 1, sx * cy );
        matrix.set( 2, 2, cx * cy );
    }
    
    /**
     * Converts Euler angles to a Matrix3f.
     * 
     * @param euler the Tuple3f containing all three Euler angles
     * @param matrix the Matrix3f instance to write rotational values to
     */
    public static void eulerToMatrix3f( Tuple3f euler, Matrix3f matrix )
    {
        eulerToMatrix3f( euler.getX(), euler.getY(), euler.getZ(), matrix );
    }
    
    /**
     * Converts Euler angles to a Matrix3f.
     * 
     * @param eulerX the x-Euler-angle
     * @param eulerY the y-Euler-angle
     * @param eulerZ the z-Euler-angle
     * 
     * @return the new Matrix3f instance reflecting the rotation
     */
    public static Matrix3f eulerToMatrix3f( float eulerX, float eulerY, float eulerZ )
    {
        Matrix3f matrix = new Matrix3f();
        
        eulerToMatrix3f( eulerX, eulerY, eulerZ, matrix );
        
        return ( matrix );
    }

    /**
     * Converts Euler angles to a Matrix3f.
     * 
     * @param euler the Tuple3f containing all three Euler angles
     * 
     * @return the new Matrix3f instance reflecting the rotation
     */
    public static Matrix3f eulerToMatrix3f( Tuple3f euler )
    {
        Matrix3f matrix = new Matrix3f();
        
        eulerToMatrix3f( euler.getX(), euler.getY(), euler.getZ(), matrix );
        
        return ( matrix );
    }
    
    /**
     * Converts Euler angles to a Matrix4f.
     * 
     * @param eulerX the x-Euler-angle
     * @param eulerY the y-Euler-angle
     * @param eulerZ the z-Euler-angle
     * @param matrix the Matrix4f instance to write rotational values to
     */
    public static void eulerToMatrix4f( float eulerX, float eulerY, float eulerZ, Matrix4f matrix )
    {
        final float sx = FastMath.sin( eulerX );
        final float sy = FastMath.sin( eulerY );
        final float sz = FastMath.sin( eulerZ );
        final float cx = FastMath.cos( eulerX );
        final float cy = FastMath.cos( eulerY );
        final float cz = FastMath.cos( eulerZ );
        
        matrix.set( 0, 0, cy * cz );
        matrix.set( 0, 1, -( cx * sz ) + ( sx * sy * cz ) );
        matrix.set( 0, 2, ( sx * sz) + (cx * sy * cz ) );
        matrix.set( 1, 0, cy * sz );
        matrix.set( 1, 1, ( cx * cz ) + ( sx * sy * sz ) );
        matrix.set( 1, 2, -( sx * cz ) + ( cx * sy * sz ) );
        matrix.set( 2, 0, -sy );
        matrix.set( 2, 1, sx * cy );
        matrix.set( 2, 2, cx * cy );
        matrix.set( 3, 3, 1 );
    }
    
    /**
     * Converts Euler angles to a Matrix4f.
     * 
     * @param euler the Tuple3f containing all three Euler angles
     * @param matrix the Matrix4f instance to write rotational values to
     */
    public static void eulerToMatrix4f( Tuple3f euler, Matrix4f matrix )
    {
        eulerToMatrix4f( euler.getX(), euler.getY(), euler.getZ(), matrix );
    }
    
    /**
     * Converts Euler angles to a Matrix4f.
     * 
     * @param euler the Tuple3f containing all three Euler angles
     * 
     * @return the new Matrix4f instance reflecting the rotation
     */
    public static Matrix4f eulerToMatrix4f( Tuple3f euler )
    {
        Matrix4f matrix = new Matrix4f();
        
        eulerToMatrix4f( euler.getX(), euler.getY(), euler.getZ(), matrix );
        
        return ( matrix );
    }
    
    /**
     * Converts Euler angles to a Matrix4f.
     * 
     * @param eulerX the x-Euler-angle
     * @param eulerY the y-Euler-angle
     * @param eulerZ the z-Euler-angle
     * 
     * @return the new Matrix4f instance reflecting the rotation
     */
    public static Matrix4f eulerToMatrix4f( float eulerX, float eulerY, float eulerZ )
    {
        Matrix4f matrix = new Matrix4f();
        
        eulerToMatrix4f( eulerX, eulerY, eulerZ, matrix );
        
        return ( matrix );
    }
    
    /**
     * Creates a 3x3 rotation matrix by a specified vector.
     * 
     * @param axisX the x-component of the vector (axis) to rotate about
     * @param axisY the y-component of the vector (axis) to rotate about
     * @param axisZ the z-component of the vector (axis) to rotate about
     * @param angle the angle to rotate by
     * @param out the Matrix3f to write the result to
     */
    public static final void getRotationMatrix(float axisX, float axisY, float axisZ, float angle, Matrix3f out)
    {
        final float length = FloatUtils.vectorLength( axisX, axisY, axisZ );
        final float v1 = (axisX / length);
        final float v2 = (axisY / length);
        final float v3 = (axisZ / length);
        
        final float v1q = v1 * v1;
        final float v2q = v2 * v2;
        final float v3q = v3 * v3;
        
        final float a = angle;
        final float sin_a = FastMath.sin( a );
        final float cos_a = FastMath.cos( a );
        
        final float m11 = ( cos_a + (v1q * (1 - cos_a)) );
        final float m12 = ( (v1 * v2 * (1 - cos_a)) - (v3 * sin_a) );
        final float m13 = ( (v1 * v3 * (1 - cos_a)) + (v2 * sin_a) );
        final float m21 = ( (v2 * v1 * (1 - cos_a)) + (v3 * sin_a) );
        final float m22 = ( cos_a + (v2q * (1 - cos_a)) );
        final float m23 = ( (v2 * v3 * (1 - cos_a)) - (v1 * sin_a) );
        final float m31 = ( (v3 * v1 * (1 - cos_a)) - (v2 * sin_a) );
        final float m32 = ( (v3 * v2 * (1 - cos_a)) + (v1 * sin_a) );
        final float m33 = ( cos_a + (v3q * (1 - cos_a)) );
        
        /*
        out.set( m11, m12, m13,
                 m21, m22, m23,
                 m31, m32, m33
               );
        */
        out.m00( m11 );
        out.m01( m12 );
        out.m02( m13 );
        out.m10( m21 );
        out.m11( m22 );
        out.m12( m23 );
        out.m20( m31 );
        out.m21( m32 );
        out.m22( m33 );
    }
    
    /**
     * Creates a 3x3 rotation matrix by a specified vector.
     * 
     * @param axisX the vector (axis) to rotate around
     * @param axisY the vector (axis) to rotate around
     * @param axisZ the vector (axis) to rotate around
     * @param angle the angle to rotate by
     * @return the created 3x3 rotation matrix
     */
    public static final Matrix3f getRotationMatrix( float axisX, float axisY, float axisZ, float angle )
    {
        final Matrix3f result = new Matrix3f();
        
        getRotationMatrix( axisX, axisY, axisZ, angle, result );
        
        return ( result );
    }
    
    /**
     * Creates a 3x3 rotation matrix by the x-axis.
     * 
     * @param angle the angle to rotate by
     * @return the created 3x3 rotation matrix
     */
    public static final Matrix3f getRotationMatrixX( float angle )
    {
        return ( getRotationMatrix( 1.0f, 0.0f, 0.0f, angle ) );
    }
    
    /**
     * Creates a 3x3 rotation matrix by the y-axis.
     * 
     * @param angle the angle to rotate by
     * @return the created 3x3 rotation matrix
     */
    public static final Matrix3f getRotationMatrixY( float angle )
    {
        return ( getRotationMatrix( 0.0f, 1.0f, 0.0f, angle ) );
    }
    
    /**
     * Creates a 3x3 rotation matrix by the z-axis.
     * 
     * @param angle the angle to rotate by
     * @return the created 3x3 rotation matrix
     */
    public static final Matrix3f getRotationMatrixZ( float angle )
    {
        return ( getRotationMatrix( 0.0f, 0.0f, 1.0f, angle ) );
    }
    
    /**
     * Creates a 3x3 rotation matrix by a specified vector.
     * 
     * @param axis Rotation axis
     * @param angle the angle to rotate by
     * @param out the Matrix3f to write the result to
     */
    public static void getRotationMatrix( Tuple3f axis, float angle, Matrix3f out )
    {
        getRotationMatrix( axis.getX(), axis.getY(), axis.getZ(), angle, out );
    }
    
    /**
     * Creates a 3x3 rotation matrix by a specified vector.
     * 
     * @param axis Rotation axis
     * @param angle the angle to rotate by
     * @return the created 3x3 rotation matrix
     */
    public static Matrix3f getRotationMatrix( Tuple3f axis, float angle )
    {
        return ( getRotationMatrix( axis.getX(), axis.getY(), axis.getZ(), angle ) );
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
    public static final Matrix3f computeRotation( float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, int normalize, Matrix3f result )
    {
        AxisAngle3f rotation = AxisAngle3f.fromPool();
        
        FloatUtils.computeRotation( v1x, v1y, v1z, v2x, v2y, v2z, normalize, rotation );
        
        result.set( rotation );
        
        AxisAngle3f.toPool( rotation );
        
        return ( result );
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
    public static final Matrix3f computeRotation( Tuple3f v1, Tuple3f v2, int normalize, Matrix3f result )
    {
        AxisAngle3f rotation = AxisAngle3f.fromPool();
        
        TupleUtils.computeRotation( v1, v2, normalize, rotation );
        
        result.set( rotation );
        
        AxisAngle3f.toPool( rotation );
        
        return ( result );
    }
}
