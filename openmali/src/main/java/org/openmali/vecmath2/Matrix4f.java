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
package org.openmali.vecmath2;

import java.io.Externalizable;
import java.util.Arrays;

import org.openmali.FastMath;
import org.openmali.vecmath2.pools.Matrix4fPool;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * A single precision floating point 4 by 4 matrix.
 * 
 * Inspired by Kenji Hiranabe's GMatrix implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Matrix4f extends MatrixMxNf implements Externalizable
{
    private static final long serialVersionUID = 7087741531605103802L;
    
    public static final Matrix4f ZERO = Matrix4f.newReadOnly( 0f, 0f, 0f, 0f,
                                                              0f, 0f, 0f, 0f,
                                                              0f, 0f, 0f, 0f,
                                                              0f, 0f, 0f, 0f
                                                            );
    
    public static final Matrix4f IDENTITY = Matrix4f.newReadOnly( 1f, 0f, 0f, 0f,
                                                                  0f, 1f, 0f, 0f,
                                                                  0f, 0f, 1f, 0f,
                                                                  0f, 0f, 0f, 1f
                                                                );
    
    public static final Matrix4f ROT_PLUS_90_DEG_BY_X_AXIS;
    public static final Matrix4f ROT_MINUS_90_DEG_BY_X_AXIS;
    public static final Matrix4f ROT_PLUS_90_DEG_BY_Y_AXIS;
    public static final Matrix4f ROT_MINUS_90_DEG_BY_Y_AXIS;
    public static final Matrix4f ROT_PLUS_90_DEG_BY_Z_AXIS;
    public static final Matrix4f ROT_MINUS_90_DEG_BY_Z_AXIS;
    
    static
    {
        ROT_PLUS_90_DEG_BY_X_AXIS = new Matrix4f( true, Matrix3f.ROT_PLUS_90_DEG_BY_X_AXIS );
        ROT_MINUS_90_DEG_BY_X_AXIS = new Matrix4f( true, Matrix3f.ROT_MINUS_90_DEG_BY_X_AXIS );
        ROT_PLUS_90_DEG_BY_Y_AXIS = new Matrix4f( true, Matrix3f.ROT_PLUS_90_DEG_BY_Y_AXIS );
        ROT_MINUS_90_DEG_BY_Y_AXIS = new Matrix4f( true, Matrix3f.ROT_MINUS_90_DEG_BY_Y_AXIS );
        ROT_PLUS_90_DEG_BY_Z_AXIS = new Matrix4f( true, Matrix3f.ROT_PLUS_90_DEG_BY_Z_AXIS );
        ROT_MINUS_90_DEG_BY_Z_AXIS = new Matrix4f( true, Matrix3f.ROT_MINUS_90_DEG_BY_Z_AXIS );
    }
    
    public static final Matrix4f Z_UP_TO_Y_UP = ROT_MINUS_90_DEG_BY_X_AXIS;
    
    //private static final Matrix4fPool POOL = new Matrix4fPool( 128 );
    private static final ThreadLocal<Matrix4fPool> POOL = new ThreadLocal<Matrix4fPool>()
    {
        @Override
        protected Matrix4fPool initialValue()
        {
            return ( new Matrix4fPool( 128 ) );
        }
    };
    
    private Matrix4f readOnlyInstance = null;
    
    protected static final int M = 4;
    protected static final int N = 4;
    
    public final float m00()
    {
        return ( get( 0, 0 ) );
    }
    
    public final float m01()
    {
        return ( get( 0, 1 ) );
    }
    
    public final float m02()
    {
        return ( get( 0, 2 ) );
    }
    
    public final float m03()
    {
        return ( get( 0, 3 ) );
    }
    
    public final float m10()
    {
        return ( get( 1, 0 ) );
    }
    
    public final float m11()
    {
        return ( get( 1, 1 ) );
    }
    
    public final float m12()
    {
        return ( get( 1, 2 ) );
    }
    
    public final float m13()
    {
        return ( get( 1, 3 ) );
    }
    
    public final float m20()
    {
        return ( get( 2, 0 ) );
    }
    
    public final float m21()
    {
        return ( get( 2, 1 ) );
    }
    
    public final float m22()
    {
        return ( get( 2, 2 ) );
    }
    
    public final float m23()
    {
        return ( get( 2, 3 ) );
    }
    
    public final float m30()
    {
        return ( get( 3, 0 ) );
    }
    
    public final float m31()
    {
        return ( get( 3, 1 ) );
    }
    
    public final float m32()
    {
        return ( get( 3, 2 ) );
    }
    
    public final float m33()
    {
        return ( get( 3, 3 ) );
    }
    
    public final Matrix4f m00( float v )
    {
        set( 0, 0, v );
        
        return ( this );
    }
    
    public final Matrix4f m01( float v )
    {
        set( 0, 1, v );
        
        return ( this );
    }
    
    public final Matrix4f m02( float v )
    {
        set( 0, 2, v );
        
        return ( this );
    }
    
    public final Matrix4f m03( float v )
    {
        set( 0, 3, v );
        
        return ( this );
    }
    
    public final Matrix4f m10( float v )
    {
        set( 1, 0, v );
        
        return ( this );
    }
    
    public final Matrix4f m11( float v )
    {
        set( 1, 1, v );
        
        return ( this );
    }
    
    public final Matrix4f m12( float v )
    {
        set( 1, 2, v );
        
        return ( this );
    }
    
    public final Matrix4f m13( float v )
    {
        set( 1, 3, v );
        
        return ( this );
    }
    
    public final Matrix4f m20( float v )
    {
        set( 2, 0, v );
        
        return ( this );
    }
    
    public final Matrix4f m21( float v )
    {
        set( 2, 1, v );
        
        return ( this );
    }
    
    public final Matrix4f m22( float v )
    {
        set( 2, 2, v );
        
        return ( this );
    }
    
    public final Matrix4f m23( float v )
    {
        set( 2, 3, v );
        
        return ( this );
    }
    
    public final Matrix4f m30( float v )
    {
        set( 3, 0, v );
        
        return ( this );
    }
    
    public final Matrix4f m31( float v )
    {
        set( 3, 1, v );
        
        return ( this );
    }
    
    public final Matrix4f m32( float v )
    {
        set( 3, 2, v );
        
        return ( this );
    }
    
    public final Matrix4f m33( float v )
    {
        set( 3, 3, v );
        
        return ( this );
    }
    
    
    
    /**
     * Sets 16 values
     * 
     * @param m00
     * @param m01
     * @param m02
     * @param m03
     * @param m10
     * @param m11
     * @param m12
     * @param m13
     * @param m20
     * @param m21
     * @param m22
     * @param m23
     * @param m30
     * @param m31
     * @param m32
     * @param m33
     * 
     * @return itself
     */
    public final Matrix4f set( float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33 )
    {
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        if ( !this.isSubMatrix() )
        {
            values[ 0 ] = m00;
            values[ 1 ] = m01;
            values[ 2 ] = m02;
            values[ 3 ] = m03;
            values[ 4 ] = m10;
            values[ 5 ] = m11;
            values[ 6 ] = m12;
            values[ 7 ] = m13;
            values[ 8 ] = m20;
            values[ 9 ] = m21;
            values[ 10 ] = m22;
            values[ 11 ] = m23;
            values[ 12 ] = m30;
            values[ 13 ] = m31;
            values[ 14 ] = m32;
            values[ 15 ] = m33;
            
            this.isDirty[ 0 ] = true;
        }
        else
        {
            m00( m00 );
            m01( m01 );
            m02( m02 );
            m03( m03 );
            m10( m10 );
            m11( m11 );
            m12( m12 );
            m13( m13 );
            m20( m20 );
            m21( m21 );
            m22( m22 );
            m23( m23 );
            m30( m30 );
            m31( m31 );
            m32( m32 );
            m33( m33 );
        }
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to a scale matrix with the passed scale
     * amount.
     * 
     * @param scale the scale factor for the matrix
     * 
     * @return itself
     */
    public final Matrix4f set( float scale )
    {
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        if ( !this.isSubMatrix() )
        {
            values[ 0 ] = scale;
            values[ 1 ] = 0f;
            values[ 2 ] = 0f;
            values[ 3 ] = 0f;
            values[ 4 ] = 0f;
            values[ 5 ] = scale;
            values[ 6 ] = 0f;
            values[ 7 ] = 0f;
            values[ 8 ] = 0f;
            values[ 9 ] = 0f;
            values[ 10 ] = scale;
            values[ 11 ] = 0f;
            values[ 12 ] = 0f;
            values[ 13 ] = 0f;
            values[ 14 ] = 0f;
            values[ 15 ] = 0f;
            
            this.isDirty[ 0 ] = true;
        }
        else
        {
            this.m00( scale );
            this.m01( 0.0f );
            this.m02( 0.0f );
            this.m03( 0.0f );
            this.m10( 0.0f );
            this.m11( scale );
            this.m12( 0.0f );
            this.m13( 0.0f );
            this.m20( 0.0f );
            this.m21( 0.0f );
            this.m22( scale );
            this.m23( 0.0f );
            this.m30( 0.0f );
            this.m31( 0.0f );
            this.m32( 0.0f );
            this.m33( 1.0f );
        }
        
        return ( this );
    }
    
    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * values in the single precision Matrix3f argument; the other elements of
     * this matrix are initialized as if this were an identity matrix (ie,
     * affine matrix with no translational component).
     * 
     * @param mat the 3x3 matrix
     */
    @Override
    public void set( Matrix3f mat )
    {
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        values[ 0 ] = mat.m00();
        values[ 1 ] = mat.m01();
        values[ 2 ] = mat.m02();
        values[ 3 ] = 0f;
        values[ 4 ] = mat.m10();
        values[ 5 ] = mat.m11();
        values[ 6 ] = mat.m12();
        values[ 7 ] = 0f;
        values[ 8 ] = mat.m20();
        values[ 9 ] = mat.m21();
        values[ 10 ] = mat.m22();
        values[ 11 ] = 0f;
        values[ 12 ] = 0f;
        values[ 13 ] = 0f;
        values[ 14 ] = 0f;
        values[ 15 ] = 1f;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets the value of this matrix to a translate matrix by the passed
     * translation value.
     * 
     * @param pos the translation amount
     * 
     * @return itself
     */
    public final Matrix4f set( Tuple3f pos )
    {
        setIdentity();
        setTranslation( pos );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to a scale and translation matrix; scale is
     * not applied to the translation and all of the matrix values are modified.
     * 
     * @param scale the scale factor for the matrix
     * @param pos the translation amount
     * 
     * @return itself
     */
    public final Matrix4f set( float scale, Tuple3f pos )
    {
        set( scale );
        setTranslation( pos );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to a scale and translation matrix; the
     * translation is scaled by the scale factor and all of the matrix values
     * are modified.
     * 
     * @param pos the translation amount
     * @param scale the scale factor for the matrix
     * 
     * @return itself
     */
    public final Matrix4f set( Tuple3f pos, float scale )
    {
        this.m00( scale );
        this.m01( 0.0f );
        this.m02( 0.0f );
        this.m03( scale * pos.getX() );
        this.m10( 0.0f );
        this.m11( scale );
        this.m12( 0.0f );
        this.m13( scale * pos.getY() );
        this.m20( 0.0f );
        this.m21( 0.0f );
        this.m22( scale );
        this.m23( scale * pos.getZ() );
        this.m30( 0.0f );
        this.m31( 0.0f );
        this.m32( 0.0f );
        this.m33( 1.0f );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix from the rotation expressed by the rotation
     * matrix m1, the translation t1, and the scale s. The translation is not
     * modified by the scale.
     * 
     * @param mat The rotation component
     * @param pos The translation component
     * @param scale The scale component
     * 
     * @return itself
     */
    public final Matrix4f set( Matrix3f mat, Tuple3f pos, float scale )
    {
        setRotationScale( mat );
        mulRotationScale( scale );
        setTranslation( pos );
        set( 3, 3, 1.0f );
        
        return ( this );
    }
    
    /**
     * Modifies the translational components of this matrix to the values of the
     * Vector3f argument; the other values of this matrix are not modified.
     * 
     * @param transX the translational x-component
     * @param transY the translational y-component
     * @param transZ the translational z-component
     * 
     * @return itself
     */
    public final Matrix4f setTranslation( float transX, float transY, float transZ )
    {
        set( 0, 3, transX );
        set( 1, 3, transY );
        set( 2, 3, transZ );
        
        return ( this );
    }
    
    /**
     * Modifies the translational components of this matrix to the values of the
     * Vector3f argument; the other values of this matrix are not modified.
     * 
     * @param trans the translational component
     * 
     * @return itself
     */
    public final Matrix4f setTranslation( Tuple3f trans )
    {
        set( 0, 3, trans.getX() );
        set( 1, 3, trans.getY() );
        set( 2, 3, trans.getZ() );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix conversion of the single
     * precision quaternion argument.
     * 
     * @param quat the quaternion to be converted
     * 
     * @return itself
     */
    public final Matrix4f set( Quaternion4f quat )
    {
        setFromQuat( quat.getA(), quat.getB(), quat.getC(), quat.getD() );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix conversion of the single
     * precision axis and angle argument.
     * 
     * @param aa3f the axis and angle to be converted
     * 
     * @return itself
     */
    public final Matrix4f set( AxisAngle3f aa3f )
    {
        setFromAxisAngle( aa3f.getX(), aa3f.getY(), aa3f.getZ(), aa3f.getAngle() );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix from the rotation expressed by the
     * quaternion q1, the translation t1, and the scale s.
     * 
     * @param quat the rotation expressed as a quaternion
     * @param pos the translation
     * @param scale the scale value
     * 
     * @return itself
     */
    public final Matrix4f set( Quaternion4f quat, Tuple3f pos, float scale )
    {
        set( quat );
        mulRotationScale( scale );
        this.set( 0, 3, pos.getX() );
        this.set( 1, 3, pos.getY() );
        this.set( 2, 3, pos.getZ() );
        
        return ( this );
    }
    
    /**
     * Sets the scale component of the current matrix by factoring out the
     * current scale (by doing an SVD) from the rotational component and
     * multiplying by the new scale.
     * 
     * @param scale the new scale amount
     * 
     * @return itself
     */
    @Override
    public final Matrix4f setScale( float scale )
    {
        SVD( null, this );
        mulRotationScale( scale );
        
        return ( this );
    }
    
    /**
     * Performs an SVD normalization of this matrix in order to acquire the
     * normalized rotational component; the values are placed into the Matrix3f
     * parameter.
     * 
     * @param mat matrix into which the rotational component is placed
     */
    @Override
    public final void get( Matrix3f mat )
    {
        SVD( mat, null );
    }
    
    /**
     * Performs an SVD normalization of this matrix to calculate the rotation as
     * a 3x3 matrix, the translation, and the scale. None of the matrix values
     * are modified.
     * 
     * @param mat The normalized matrix representing the rotation
     * @param pos The translation component
     * @return The scale component of this transform
     */
    public final float get( Matrix3f mat, Tuple3f pos )
    {
        get( pos );
        
        return ( SVD( mat, null ) );
    }
    
    /**
     * Performs an SVD normalization of this matrix in order to acquire the
     * normalized rotational component; the values are placed into the Quat4f
     * parameter.
     * 
     * @param quat quaternion into which the rotation component is placed
     */
    public final void get( Quaternion4f quat )
    {
        quat.set( this );
        quat.normalize();
    }
    
    /**
     * Writes the contents of this matrix column-wise to a FloatBuffer.<br>
     * 
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     * 
     * @return itself
     */
    public java.nio.FloatBuffer writeToBuffer( java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.put( values[ 0 ] ).put( values[ 4 ] ).put( values[ 8 ] ).put( values[ 12 ] ).
               put( values[ 1 ] ).put( values[ 5 ] ).put( values[ 9 ] ).put( values[ 13 ] ).
               put( values[ 2 ] ).put( values[ 6 ] ).put( values[ 10 ] ).put( values[ 14 ] ).
               put( values[ 3 ] ).put( values[ 7 ] ).put( values[ 11 ] ).put( values[ 15 ] );
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this matrix column-wise to a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     * 
     * @return itself
     */
    public java.nio.FloatBuffer writeToBuffer( java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        buffer.put( values[ 0 ] ).put( values[ 4 ] ).put( values[ 8 ] ).put( values[ 12 ] ).
               put( values[ 1 ] ).put( values[ 5 ] ).put( values[ 9 ] ).put( values[ 13 ] ).
               put( values[ 2 ] ).put( values[ 6 ] ).put( values[ 10 ] ).put( values[ 14 ] ).
               put( values[ 3 ] ).put( values[ 7 ] ).put( values[ 11 ] ).put( values[ 15 ] );
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this matrix column-wise to a FloatBuffer.<br>
     * 
     * @param matrices
     * @param buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     * 
     * @return itself
     */
    public static java.nio.FloatBuffer writeToBuffer( Matrix4f[] matrices, java.nio.FloatBuffer buffer, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        for ( int i = 0; i < matrices.length; i++ )
        {
            matrices[ i ].writeToBuffer( buffer, false, false );
        }
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Writes the contents of this matrix column-wise to a FloatBuffer.<br>
     * 
     * @param matrices
     * @param buffer
     * @param position position in the buffer
     * @param clear clear the buffer before writing data?
     * @param flip flip the buffer after writing data?
     * 
     * @return itself
     */
    public static java.nio.FloatBuffer writeToBuffer( Matrix4f[] matrices, java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
    {
        if ( clear )
            buffer.clear();
        
        buffer.position( position );
        
        for ( int i = 0; i < matrices.length; i++ )
        {
            matrices[ i ].writeToBuffer( buffer, false, false );
        }
        
        if ( flip )
            buffer.flip();
        
        return ( buffer );
    }
    
    /**
     * Reads the contents of this matrix column-wise from a FloatBuffer.<br>
     * 
     * @param buffer
     * 
     * @return itself
     */
    public java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer )
    {
        buffer.get( values, 0, 1 ).get( values, 4, 1 ).get( values, 8, 1 ).get( values, 12, 1 ).
               get( values, 1, 1 ).get( values, 5, 1 ).get( values, 9, 1 ).get( values, 13, 1 ).
               get( values, 2, 1 ).get( values, 6, 1 ).get( values, 10, 1 ).get( values, 14, 1 ).
               get( values, 3, 1 ).get( values, 7, 1 ).get( values, 11, 1 ).get( values, 15, 1 );
        
        return ( buffer );
    }
    
    /**
     * Reads the contents of this matrix column-wise from a FloatBuffer.<br>
     * 
     * @param buffer
     * @param position position in the buffer
     * 
     * @return itself
     */
    public java.nio.FloatBuffer readFromBuffer( java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        buffer.get( values, 0, 1 ).get( values, 4, 1 ).get( values, 8, 1 ).get( values, 12, 1 ).
               get( values, 1, 1 ).get( values, 5, 1 ).get( values, 9, 1 ).get( values, 13, 1 ).
               get( values, 2, 1 ).get( values, 6, 1 ).get( values, 10, 1 ).get( values, 14, 1 ).
               get( values, 3, 1 ).get( values, 7, 1 ).get( values, 11, 1 ).get( values, 15, 1 );
        
        return ( buffer );
    }
    
    /**
     * Reads the contents of this matrix column-wise from a FloatBuffer.<br>
     * 
     * @param matrices
     * @param buffer
     * 
     * @return itself
     */
    public static java.nio.FloatBuffer readFromBuffer( Matrix4f[] matrices, java.nio.FloatBuffer buffer )
    {
        for ( int i = 0; i < matrices.length; i++ )
        {
            matrices[ i ].readFromBuffer( buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * Reads the contents of this matrix column-wise from a FloatBuffer.<br>
     * 
     * @param matrices
     * @param buffer
     * @param position position in the buffer
     * 
     * @return itself
     */
    public static java.nio.FloatBuffer readFromBuffer( Matrix4f[] matrices, java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        for ( int i = 0; i < matrices.length; i++ )
        {
            matrices[ i ].readFromBuffer( buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * Retrieves the translational components of this matrix.
     * 
     * @param trans the vector that will receive the translational component
     */
    public final void get( Tuple3f trans )
    {
        trans.set( m03(), m13(), m23() );
    }
    
    /**
     * Gets the upper 3x3 values of this matrix and places them into the matrix mat.
     * 
     * @param mat The matrix that will hold the values
     */
    public final void getRotationScale( Matrix3f mat )
    {
        for ( int i = 0; i < 3; i++ )
        {
            for ( int j = 0; j < 3; j++ )
            {
                mat.set( i, j, this.get( i, j ) );
            }
        }
    }
    
    /**
     * Performs an SVD normalization of this matrix to calculate and return the
     * uniform scale factor. This matrix is not modified.
     * 
     * @return the scale factor of this matrix
     */
    public final float getScale()
    {
        return ( SVD( (Matrix3f)null ) );
    }
    
    /**
     * Replaces the upper 3x3 matrix values of this matrix with the values in
     * the matrix mat.
     * 
     * @param mat The matrix that will be the new upper 3x3
     * 
     * @return itself
     */
    public final Matrix4f setRotationScale( Matrix3f mat )
    {
        for ( int i = 0; i < 3; i++ )
        {
            for ( int j = 0; j < 3; j++ )
            {
                this.set( i, j, mat.get( i, j ) );
            }
        }
        
        return ( this );
    }
    
    /**
     * Sets the specified row of this Matrix4f to the four values provided.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param x the first column element
     * @param y the second column element
     * @param z the third column element
     * @param w the fourth column element
     * 
     * @return itself
     */
    public final Matrix4f setRow( int row, float x, float y, float z, float w )
    {
        if ( ( row >= 0 ) && ( row <= 4 ) )
        {
             this.set( row, 0, x );
             this.set( row, 1, y );
             this.set( row, 2, z );
             this.set( row, 3, w );
             
             return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "row must be 0 to 4 and is " + row );
    }
    
    /**
     * Sets the specified row of this matrix4f to the Vector provided.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param values the replacement row
     * 
     * @return itself
     */
    public final Matrix4f setRow( int row, Vector4f values )
    {
        if ( ( row >= 0 ) && ( row <= 4 ) )
        {
             this.set( row, 0, values.getX() );
             this.set( row, 1, values.getY() );
             this.set( row, 2, values.getZ() );
             this.set( row, 3, values.getW() );
             
             return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "row must be 0 to 4 and is " + row );
    }
    
    /**
     * Copies the matrix values in the specified row into the vector parameter.
     * 
     * @param row the matrix row
     * @param buffer The vector into which the matrix row values will be copied
     * 
     * @return itself
     */
    public final Matrix4f getRow( int row, Vector4f buffer )
    {
        if ( ( row >= 0 ) && ( row <= 4 ) )
        {
            buffer.setX( get( row, 0 ) );
            buffer.setY( get( row, 1 ) );
            buffer.setZ( get( row, 2 ) );
            buffer.setW( get( row, 3 ) );
            
            return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "row must be 0 to 4 and is " + row );
    }
    
    /**
     * Sets the specified column of this matrix4f to the four values provided.
     * 
     * @param column the column number to be modified (zero indexed)
     * @param x the first row element
     * @param y the second row element
     * @param z the third row element
     * @param w the fourth row element
     * 
     * @return itself
     */
    public final Matrix4f setColumn( int column, float x, float y, float z, float w )
    {
        if ( ( column >= 0 ) && ( column <= 4 ) )
        {
            this.set( 0, column, x );
            this.set( 1, column, y );
            this.set( 2, column, z );
            this.set( 3, column, w );
            
            return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "column must be 0 to 4 and is " + column );
    }
    
    /**
     * Sets the specified column of this matrix4f to the vector provided.
     * 
     * @param column the column number to be modified (zero indexed)
     * @param values the replacement column
     * 
     * @return itself
     */
    public final Matrix4f setColumn( int column, Vector4f values )
    {
        if ( ( column >= 0 ) && ( column <= 4 ) )
        {
            this.set( 0, column, values.getX() );
            this.set( 1, column, values.getY() );
            this.set( 2, column, values.getZ() );
            this.set( 3, column, values.getW() );
            
            return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "column must be 0 to 4 and is " + column );
    }
    
    /**
     * Copies the matrix values in the specified column into the vector
     * parameter.
     * 
     * @param column the matrix column
     * @param buffer The vector into which the matrix column values will be copied
     */
    public final void getColumn( int column, Vector4f buffer )
    {
        if ( ( column >= 0 ) && ( column <= 4 ) )
        {
            buffer.setX( get( 0, column ) );
            buffer.setY( get( 1, column ) );
            buffer.setZ( get( 2, column ) );
            buffer.setW( get( 3, column ) );
        }
        else
        {
            throw new ArrayIndexOutOfBoundsException( "column must be 0 to 4 and is " + column );
        }
    }
    
    /**
     * Computes the determinant of this matrix.
     * 
     * @return the determinant of the matrix
     */
    public final float determinant()
    {
        // less *,+,- calculation than expanded expression.
        return (m00() * m11() - m01() * m10()) * (m22() * m33() - m23() * m32()) -
               (m00() * m12() - m02() * m10()) * (m21() * m33() - m23() * m31()) +
               (m00() * m13() - m03() * m10()) * (m21() * m32() - m22() * m31()) +
               (m01() * m12() - m02() * m11()) * (m20() * m33() - m23() * m30()) -
               (m01() * m13() - m03() * m11()) * (m20() * m32() - m22() * m30()) +
               (m02() * m13() - m03() * m12()) * (m20() * m31() - m21() * m30());
    }
    
    /**
     * Sets the value of this matrix to the transpose of the argument matrix
     * 
     * @param mat the matrix to be transposed
     * 
     * @return itself
     */
    public final Matrix4f transpose( Matrix4f mat )
    {
        super.transpose( mat );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix equal to the negation of of the Matrix4f
     * parameter.
     * 
     * @param m1 The source matrix
     * 
     * @return itself
     */
    public final Matrix4f negate( Matrix4f m1 )
    {
        set( m1 );
        negate();
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix4f invert()
    {
        float d = determinant();
        if ( d == 0.0f )
            return ( this );
        d = 1.0f / d;
        
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        // alias-safe way.
        // less *,+,- calculation than expanded expression.
        set( values[ 5] * (values[10] * values[15] - values[11] * values[14]) + values[ 6] * (values[11] * values[13] - values[ 9] * values[15]) + values[ 7] * (values[ 9] * values[14] - values[10] * values[13]),
             values[ 9] * (values[ 2] * values[15] - values[ 3] * values[14]) + values[10] * (values[ 3] * values[13] - values[ 1] * values[15]) + values[11] * (values[ 1] * values[14] - values[ 2] * values[13]),
             values[13] * (values[ 2] * values[ 7] - values[ 3] * values[ 6]) + values[14] * (values[ 3] * values[ 5] - values[ 1] * values[ 7]) + values[15] * (values[ 1] * values[ 6] - values[ 2] * values[ 5]),
             values[ 1] * (values[ 7] * values[10] - values[ 6] * values[11]) + values[ 2] * (values[ 5] * values[11] - values[ 7] * values[ 9]) + values[ 3] * (values[ 6] * values[ 9] - values[ 5] * values[10]),
             values[ 6] * (values[ 8] * values[15] - values[11] * values[12]) + values[ 7] * (values[10] * values[12] - values[ 8] * values[14]) + values[ 4] * (values[11] * values[14] - values[10] * values[15]),
             values[10] * (values[ 0] * values[15] - values[ 3] * values[12]) + values[11] * (values[ 2] * values[12] - values[ 0] * values[14]) + values[ 8] * (values[ 3] * values[14] - values[ 2] * values[15]),
             values[14] * (values[ 0] * values[ 7] - values[ 3] * values[ 4]) + values[15] * (values[ 2] * values[ 4] - values[ 0] * values[ 6]) + values[12] * (values[ 3] * values[ 6] - values[ 2] * values[ 7]),
             values[ 2] * (values[ 7] * values[ 8] - values[ 4] * values[11]) + values[ 3] * (values[ 4] * values[10] - values[ 6] * values[ 8]) + values[ 0] * (values[ 6] * values[11] - values[ 7] * values[10]),
             values[ 7] * (values[ 8] * values[13] - values[ 9] * values[12]) + values[ 4] * (values[ 9] * values[15] - values[11] * values[13]) + values[ 5] * (values[11] * values[12] - values[ 8] * values[15]),
             values[11] * (values[ 0] * values[13] - values[ 1] * values[12]) + values[ 8] * (values[ 1] * values[15] - values[ 3] * values[13]) + values[ 9] * (values[ 3] * values[12] - values[ 0] * values[15]),
             values[15] * (values[ 0] * values[ 5] - values[ 1] * values[ 4]) + values[12] * (values[ 1] * values[ 7] - values[ 3] * values[ 5]) + values[13] * (values[ 3] * values[ 4] - values[ 0] * values[ 7]),
             values[ 3] * (values[ 5] * values[ 8] - values[ 4] * values[ 9]) + values[ 0] * (values[ 7] * values[ 9] - values[ 5] * values[11]) + values[ 1] * (values[ 4] * values[11] - values[ 7] * values[ 8]),
             values[ 4] * (values[10] * values[13] - values[ 9] * values[14]) + values[ 5] * (values[ 8] * values[14] - values[10] * values[12]) + values[ 6] * (values[ 9] * values[12] - values[ 8] * values[13]),
             values[ 8] * (values[ 2] * values[13] - values[ 1] * values[14]) + values[ 9] * (values[ 0] * values[14] - values[ 2] * values[12]) + values[10] * (values[ 1] * values[12] - values[ 0] * values[13]),
             values[12] * (values[ 2] * values[ 5] - values[ 1] * values[ 6]) + values[13] * (values[ 0] * values[ 6] - values[ 2] * values[ 4]) + values[14] * (values[ 1] * values[ 4] - values[ 0] * values[ 5]),
             values[ 0] * (values[ 5] * values[10] - values[ 6] * values[ 9]) + values[ 1] * (values[ 6] * values[ 8] - values[ 4] * values[10]) + values[ 2] * (values[ 4] * values[ 9] - values[ 5] * values[ 8])
           );
        
        mul( d );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix inverse of the passed matrix
     * m1.
     * 
     * @param mat the matrix to be inverted
     * 
     * @return itself
     */
    public final Matrix4f invert( Matrix4f mat )
    {
        set( mat );
        invert();
        
        return ( this );
    }
    
    /**
     * Adds a scalar to each component of the matrix m1 and places the result
     * into this. Matrix m1 is not modified.
     * 
     * @param scalar The scalar adder.
     * @param mat The original matrix values.
     * 
     * @return itself
     */
    public final Matrix4f add( float scalar, Matrix4f mat )
    {
        set( mat );
        add( scalar );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
     * 
     * @param mat1 the first matrix
     * @param mat2 the second matrix
     * 
     * @return itself
     */
    public final Matrix4f add( Matrix4f mat1, Matrix4f mat2 )
    {
        set( mat1 );
        add( mat2 );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to sum of itself and matrix m1.
     * 
     * @param mat2 the other matrix
     * 
     * @return itself
     */
    public final Matrix4f add( Matrix4f mat2 )
    {
        for ( int i = 0; i < M; i++ )
        {
            for ( int j = 0; j < N; j++ )
            {
                set( i, j, this.get( i, j ) + mat2.get( i, j ) );
            }
        }
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of itself and
     * matrix m1 (this = this - m1).
     * 
     * @param mat2 the other matrix
     * 
     * @return itself
     */
    public final Matrix4f sub( Matrix4f mat2 )
    {
        for ( int i = 0; i < M; i++ )
        {
            for ( int j = 0; j < N; j++ )
            {
                set( i, j, this.get( i, j ) - mat2.get( i, j ) );
            }
        }
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of matrices m1 and m2.
     * 
     * @param mat1 the first matrix
     * @param mat2 the second matrix
     * 
     * @return itself
     */
    public final Matrix4f sub( Matrix4f mat1, Matrix4f mat2 )
    {
        set( mat1 );
        sub( mat2 );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to a rotation matrix about the x axis by
     * the passed angle.
     * 
     * @param angle the angle to rotate about the X axis in radians
     * 
     * @return itself
     */
    public final Matrix4f rotX( float angle )
    {
        final float c = FastMath.cos( angle );
        final float s = FastMath.sin( angle );
        
        this.m00( 1.0f );
        this.m01( 0.0f );
        this.m02( 0.0f );
        //this.m03( 0.0f );
        this.m10( 0.0f );
        this.m11( c );
        this.m12( -s );
        //this.m13( 0.0f );
        this.m20( 0.0f );
        this.m21( s );
        this.m22( c );
        //this.m23( 0.0f );
        this.m30( 0.0f );
        this.m31( 0.0f );
        this.m32( 0.0f );
        this.m33( 1.0f );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to a rotation matrix about the y axis by
     * the passed angle.
     * 
     * @param angle the angle to rotate about the Y axis in radians
     * 
     * @return itself
     */
    public final Matrix4f rotY( float angle )
    {
        final float c = FastMath.cos( angle );
        final float s = FastMath.sin( angle );
        
        this.m00( c );
        this.m01( 0.0f );
        this.m02( s );
        //this.m03( 0.0f );
        this.m10( 0.0f );
        this.m11( 1.0f );
        this.m12( 0.0f );
        //this.m13( 0.0f );
        this.m20( -s );
        this.m21( 0.0f );
        this.m22( c );
        //this.m23( 0.0f );
        this.m30( 0.0f );
        this.m31( 0.0f );
        this.m32( 0.0f );
        this.m33( 1.0f );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to a rotation matrix about the z axis by
     * the passed angle.
     * 
     * @param angle the angle to rotate about the Z axis in radians
     * 
     * @return itself
     */
    public final Matrix4f rotZ(float angle)
    {
        final float c = FastMath.cos( angle );
        final float s = FastMath.sin( angle );
        
        this.m00( c );
        this.m01( -s );
        this.m02( 0.0f );
        //this.m03( 0.0f );
        this.m10( s );
        this.m11( c );
        this.m12( 0.0f );
        //this.m13( 0.0f );
        this.m20( 0.0f );
        this.m21( 0.0f );
        this.m22( 1.0f );
        //this.m23( 0.0f );
        this.m30( 0.0f );
        this.m31( 0.0f );
        this.m32( 0.0f );
        this.m33( 1.0f );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix4f mul( float scalar )
    {
        values[ 0 ] *= scalar;
        values[ 1 ] *= scalar;
        values[ 2 ] *= scalar;
        values[ 3 ] *= scalar;
        values[ 4 ] *= scalar;
        values[ 5 ] *= scalar;
        values[ 6 ] *= scalar;
        values[ 7 ] *= scalar;
        values[ 8 ] *= scalar;
        values[ 9 ] *= scalar;
        values[ 10 ] *= scalar;
        values[ 11 ] *= scalar;
        values[ 12 ] *= scalar;
        values[ 13 ] *= scalar;
        values[ 14 ] *= scalar;
        values[ 15 ] *= scalar;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies each element of matrix m1 by a scalar and places the result
     * into this. Matrix mat is not modified.
     * 
     * @param scalar The scalar multiplier.
     * @param mat The original matrix.
     * 
     * @return itself
     */
    public final Matrix4f mul( float scalar, Matrix4f mat )
    {
        set( mat );
        mul( scalar );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying itself with
     * matrix mat.
     * 
     * @param mat the other matrix
     * 
     * @return itself
     */
    public final Matrix4f mul( Matrix4f mat )
    {
        mul( this, mat );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying the two
     * argument matrices together.
     * 
     * @param mat1 the first matrix
     * @param mat2 the second matrix
     * 
     * @return itself
     */
    public Matrix4f mul( Matrix4f mat1, Matrix4f mat2 )
    {
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        if ( !mat1.isSubMatrix() && !mat1.isSubMatrix() ) // No idea why, but the presence of this selection makes it faster!
        {
            // alias-safe way.
            set( mat1.values[0] * mat2.values[0] + mat1.values[1] * mat2.values[4] + mat1.values[2] * mat2.values[8] + mat1.values[3] * mat2.values[12],
                 mat1.values[0] * mat2.values[1] + mat1.values[1] * mat2.values[5] + mat1.values[2] * mat2.values[9] + mat1.values[3] * mat2.values[13],
                 mat1.values[0] * mat2.values[2] + mat1.values[1] * mat2.values[6] + mat1.values[2] * mat2.values[10] + mat1.values[3] * mat2.values[14],
                 mat1.values[0] * mat2.values[3] + mat1.values[1] * mat2.values[7] + mat1.values[2] * mat2.values[11] + mat1.values[3] * mat2.values[15],
                 mat1.values[4] * mat2.values[0] + mat1.values[5] * mat2.values[4] + mat1.values[6] * mat2.values[8] + mat1.values[7] * mat2.values[12],
                 mat1.values[4] * mat2.values[1] + mat1.values[5] * mat2.values[5] + mat1.values[6] * mat2.values[9] + mat1.values[7] * mat2.values[13],
                 mat1.values[4] * mat2.values[2] + mat1.values[5] * mat2.values[6] + mat1.values[6] * mat2.values[10] + mat1.values[7] * mat2.values[14],
                 mat1.values[4] * mat2.values[3] + mat1.values[5] * mat2.values[7] + mat1.values[6] * mat2.values[11] + mat1.values[7] * mat2.values[15],
                 mat1.values[8] * mat2.values[0] + mat1.values[9] * mat2.values[4] + mat1.values[10] * mat2.values[8] + mat1.values[11] * mat2.values[12],
                 mat1.values[8] * mat2.values[1] + mat1.values[9] * mat2.values[5] + mat1.values[10] * mat2.values[9] + mat1.values[11] * mat2.values[13],
                 mat1.values[8] * mat2.values[2] + mat1.values[9] * mat2.values[6] + mat1.values[10] * mat2.values[10] + mat1.values[11] * mat2.values[14],
                 mat1.values[8] * mat2.values[3] + mat1.values[9] * mat2.values[7] + mat1.values[10] * mat2.values[11] + mat1.values[11] * mat2.values[15],
                 mat1.values[12] * mat2.values[0] + mat1.values[13] * mat2.values[4] + mat1.values[14] * mat2.values[8] + mat1.values[15] * mat2.values[12],
                 mat1.values[12] * mat2.values[1] + mat1.values[13] * mat2.values[5] + mat1.values[14] * mat2.values[9] + mat1.values[15] * mat2.values[13],
                 mat1.values[12] * mat2.values[2] + mat1.values[13] * mat2.values[6] + mat1.values[14] * mat2.values[10] + mat1.values[15] * mat2.values[14],
                 mat1.values[12] * mat2.values[3] + mat1.values[13] * mat2.values[7] + mat1.values[14] * mat2.values[11] + mat1.values[15] * mat2.values[15]
               );
        }
        
        return ( this );
    }
    
    /**
     * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
     * and places the result into this.
     * 
     * @param mat1 The matrix on the left hand side of the multiplication
     * @param mat2 The matrix on the right hand side of the multiplication
     * 
     * @return itself
     */
    public final Matrix4f mulTransposeBoth( Matrix4f mat1, Matrix4f mat2 )
    {
        mul( mat2, mat1 );
        transpose();
        
        return ( this );
    }
    
    /**
     * Multiplies the transpose of matrix m1 times matrix m2, and places the
     * result into this.
     * 
     * @param mat1 The matrix on the left hand side of the multiplication
     * @param mat2 The matrix on the right hand side of the multiplication
     * 
     * @return itself
     */
    public final Matrix4f mulTransposeLeft( Matrix4f mat1, Matrix4f mat2 )
    {
        // alias-safe way.
        set( mat1.get(0, 0) * mat2.get(0, 0) + mat1.get(1, 0) * mat2.get(1, 0) + mat1.get(2, 0) * mat2.get(2, 0) + mat1.get(3, 0) * mat2.get(3, 0),
             mat1.get(0, 0) * mat2.get(0, 1) + mat1.get(1, 0) * mat2.get(1, 1) + mat1.get(2, 0) * mat2.get(2, 1) + mat1.get(3, 0) * mat2.get(3, 1),
             mat1.get(0, 0) * mat2.get(0, 2) + mat1.get(1, 0) * mat2.get(1, 2) + mat1.get(2, 0) * mat2.get(2, 2) + mat1.get(3, 0) * mat2.get(3, 2),
             mat1.get(0, 0) * mat2.get(0, 3) + mat1.get(1, 0) * mat2.get(1, 3) + mat1.get(2, 0) * mat2.get(2, 3) + mat1.get(3, 0) * mat2.get(3, 3),
             mat1.get(0, 1) * mat2.get(0, 0) + mat1.get(1, 1) * mat2.get(1, 0) + mat1.get(2, 1) * mat2.get(2, 0) + mat1.get(3, 1) * mat2.get(3, 0),
             mat1.get(0, 1) * mat2.get(0, 1) + mat1.get(1, 1) * mat2.get(1, 1) + mat1.get(2, 1) * mat2.get(2, 1) + mat1.get(3, 1) * mat2.get(3, 1),
             mat1.get(0, 1) * mat2.get(0, 2) + mat1.get(1, 1) * mat2.get(1, 2) + mat1.get(2, 1) * mat2.get(2, 2) + mat1.get(3, 1) * mat2.get(3, 2),
             mat1.get(0, 1) * mat2.get(0, 3) + mat1.get(1, 1) * mat2.get(1, 3) + mat1.get(2, 1) * mat2.get(2, 3) + mat1.get(3, 1) * mat2.get(3, 3),
             mat1.get(0, 2) * mat2.get(0, 0) + mat1.get(1, 2) * mat2.get(1, 0) + mat1.get(2, 2) * mat2.get(2, 0) + mat1.get(3, 2) * mat2.get(3, 0),
             mat1.get(0, 2) * mat2.get(0, 1) + mat1.get(1, 2) * mat2.get(1, 1) + mat1.get(2, 2) * mat2.get(2, 1) + mat1.get(3, 2) * mat2.get(3, 1),
             mat1.get(0, 2) * mat2.get(0, 2) + mat1.get(1, 2) * mat2.get(1, 2) + mat1.get(2, 2) * mat2.get(2, 2) + mat1.get(3, 2) * mat2.get(3, 2),
             mat1.get(0, 2) * mat2.get(0, 3) + mat1.get(1, 2) * mat2.get(1, 3) + mat1.get(2, 2) * mat2.get(2, 3) + mat1.get(3, 2) * mat2.get(3, 3),
             mat1.get(0, 3) * mat2.get(0, 0) + mat1.get(1, 3) * mat2.get(1, 0) + mat1.get(2, 3) * mat2.get(2, 0) + mat1.get(3, 3) * mat2.get(3, 0),
             mat1.get(0, 3) * mat2.get(0, 1) + mat1.get(1, 3) * mat2.get(1, 1) + mat1.get(2, 3) * mat2.get(2, 1) + mat1.get(3, 3) * mat2.get(3, 1),
             mat1.get(0, 3) * mat2.get(0, 2) + mat1.get(1, 3) * mat2.get(1, 2) + mat1.get(2, 3) * mat2.get(2, 2) + mat1.get(3, 3) * mat2.get(3, 2),
             mat1.get(0, 3) * mat2.get(0, 3) + mat1.get(1, 3) * mat2.get(1, 3) + mat1.get(2, 3) * mat2.get(2, 3) + mat1.get(3, 3) * mat2.get(3, 3)
           );
        
        return ( this );
    }
    
    /**
     * Multiplies matrix m1 times the transpose of matrix m2, and places the
     * result into this.
     * 
     * @param mat1 The matrix on the left hand side of the multiplication
     * @param mat2 The matrix on the right hand side of the multiplication
     * 
     * @return itself
     */
    public final Matrix4f mulTransposeRight( Matrix4f mat1, Matrix4f mat2 )
    {
        // alias-safe way.
        set( mat1.get(0, 0) * mat2.get(0, 0) + mat1.get(0, 1) * mat2.get(0, 1) + mat1.get(0, 2) * mat2.get(0, 2) + mat1.get(0, 3) * mat2.get(0, 3),
             mat1.get(0, 0) * mat2.get(1, 0) + mat1.get(0, 1) * mat2.get(1, 1) + mat1.get(0, 2) * mat2.get(1, 2) + mat1.get(0, 3) * mat2.get(1, 3),
             mat1.get(0, 0) * mat2.get(2, 0) + mat1.get(0, 1) * mat2.get(2, 1) + mat1.get(0, 2) * mat2.get(2, 2) + mat1.get(0, 3) * mat2.get(2, 3),
             mat1.get(0, 0) * mat2.get(3, 0) + mat1.get(0, 1) * mat2.get(3, 1) + mat1.get(0, 2) * mat2.get(3, 2) + mat1.get(0, 3) * mat2.get(3, 3),
             mat1.get(1, 0) * mat2.get(0, 0) + mat1.get(1, 1) * mat2.get(0, 1) + mat1.get(1, 2) * mat2.get(0, 2) + mat1.get(1, 3) * mat2.get(0, 3),
             mat1.get(1, 0) * mat2.get(1, 0) + mat1.get(1, 1) * mat2.get(1, 1) + mat1.get(1, 2) * mat2.get(1, 2) + mat1.get(1, 3) * mat2.get(1, 3),
             mat1.get(1, 0) * mat2.get(2, 0) + mat1.get(1, 1) * mat2.get(2, 1) + mat1.get(1, 2) * mat2.get(2, 2) + mat1.get(1, 3) * mat2.get(2, 3),
             mat1.get(1, 0) * mat2.get(3, 0) + mat1.get(1, 1) * mat2.get(3, 1) + mat1.get(1, 2) * mat2.get(3, 2) + mat1.get(1, 3) * mat2.get(3, 3),
             mat1.get(2, 0) * mat2.get(0, 0) + mat1.get(2, 1) * mat2.get(0, 1) + mat1.get(2, 2) * mat2.get(0, 2) + mat1.get(2, 3) * mat2.get(0, 3),
             mat1.get(2, 0) * mat2.get(1, 0) + mat1.get(2, 1) * mat2.get(1, 1) + mat1.get(2, 2) * mat2.get(1, 2) + mat1.get(2, 3) * mat2.get(1, 3),
             mat1.get(2, 0) * mat2.get(2, 0) + mat1.get(2, 1) * mat2.get(2, 1) + mat1.get(2, 2) * mat2.get(2, 2) + mat1.get(2, 3) * mat2.get(2, 3),
             mat1.get(2, 0) * mat2.get(3, 0) + mat1.get(2, 1) * mat2.get(3, 1) + mat1.get(2, 2) * mat2.get(3, 2) + mat1.get(2, 3) * mat2.get(3, 3),
             mat1.get(3, 0) * mat2.get(0, 0) + mat1.get(3, 1) * mat2.get(0, 1) + mat1.get(3, 2) * mat2.get(0, 2) + mat1.get(3, 3) * mat2.get(0, 3),
             mat1.get(3, 0) * mat2.get(1, 0) + mat1.get(3, 1) * mat2.get(1, 1) + mat1.get(3, 2) * mat2.get(1, 2) + mat1.get(3, 3) * mat2.get(1, 3),
             mat1.get(3, 0) * mat2.get(2, 0) + mat1.get(3, 1) * mat2.get(2, 1) + mat1.get(3, 2) * mat2.get(2, 2) + mat1.get(3, 3) * mat2.get(2, 3),
             mat1.get(3, 0) * mat2.get(3, 0) + mat1.get(3, 1) * mat2.get(3, 1) + mat1.get(3, 2) * mat2.get(3, 2) + mat1.get(3, 3) * mat2.get(3, 3)
           );
        
        return ( this );
    }
    
    /**
     * Transform the vector vec using this Matrix4f and place the result into
     * vecOut.
     * 
     * @param vec the single precision vector to be transformed
     * @param vecOut the vector into which the transformed values are placed
     */
    public final void transform( Vector4f vec, Vector4f vecOut )
    {
        // alias-safe
        vecOut.set( m00() * vec.getX() + m01() * vec.getY() + m02() * vec.getZ() + m03() * vec.getW(),
                    m10() * vec.getX() + m11() * vec.getY() + m12() * vec.getZ() + m13() * vec.getW(),
                    m20() * vec.getX() + m21() * vec.getY() + m22() * vec.getZ() + m23() * vec.getW(),
                    m30() * vec.getX() + m31() * vec.getY() + m32() * vec.getZ() + m33() * vec.getW()
                  );
    }
    
    /**
     * Transform the vector vec using this Matrix4f and place the result back
     * into vec.
     * 
     * @param vec the single precision vector to be transformed
     */
    public final void transform( Vector4f vec )
    {
        transform( vec, vec );
    }
    
    /**
     * Transforms the point parameter with this Matrix4f and places the result
     * into pointOut. The fourth element of the point input paramter is assumed
     * to be one.
     * 
     * @param point the input point to be transformed.
     * @param pointOut the transformed point
     */
    public final void transform( Point3f point, Point3f pointOut )
    {
        pointOut.set( m00() * point.getX() + m01() * point.getY() + m02() * point.getZ() + m03(),
                      m10() * point.getX() + m11() * point.getY() + m12() * point.getZ() + m13(),
                      m20() * point.getX() + m21() * point.getY() + m22() * point.getZ() + m23()
                    );
    }
    
    /**
     * Transforms the point parameter with this Matrix4f and places the result
     * back into point. The fourth element of the point input paramter is
     * assumed to be one.
     * 
     * @param point the input point to be transformed.
     */
    public final void transform( Point3f point )
    {
        transform( point, point );
    }
    
    /**
     * Transforms the normal parameter by this Matrix4f and places the value
     * into normalOut. The fourth element of the normal is assumed to be zero.
     * 
     * @param vec the input normal to be transformed.
     * @param vecOut the transformed normal
     */
    public final void transform( Vector3f vec, Vector3f vecOut )
    {
        vecOut.set( m00() * vec.getX() + m01() * vec.getY() + m02() * vec.getZ(),
                    m10() * vec.getX() + m11() * vec.getY() + m12() * vec.getZ(),
                    m20() * vec.getX() + m21() * vec.getY() + m22() * vec.getZ()
                  );
    }
    
    /**
     * Transforms the normal parameter by this transform and places the value
     * back into normal. The fourth element of the normal is assumed to be zero.
     * 
     * @param vec the input normal to be transformed.
     */
    public final void transform( Vector3f vec )
    {
        transform( vec, vec );
    }
    
    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * values in the single precision Matrix3f argument; the other elements of
     * this matrix are unchanged; a singular value decomposition is performed on
     * this object's upper 3x3 matrix to factor out the scale, then this
     * object's upper 3x3 matrix components are replaced by the passed rotation
     * components, and then the scale is reapplied to the rotational components.
     * 
     * @param mat single precision 3x3 matrix
     * 
     * @return itself
     */
    public final Matrix4f setRotation( Matrix3f mat )
    {
        final float scale = SVD( (Matrix3f)null );
        setRotationScale( mat );
        mulRotationScale( scale );
        
        return ( this );
    }
    
    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * equivalent values of the quaternion argument; the other elements of this
     * matrix are unchanged; a singular value decomposition is performed on this
     * object's upper 3x3 matrix to factor out the scale, then this object's
     * upper 3x3 matrix components are replaced by the matrix equivalent of the
     * quaternion, and then the scale is reapplied to the rotational components.
     * 
     * @param quat the quaternion that specifies the rotation
     * 
     * @return itself
     */
    public final Matrix4f setRotation( Quaternion4f quat )
    {
        final float scale = SVD( (Matrix3f)null, (Matrix4f)null );
        
        // save other values
        final float tx = get( 0, 3 );
        final float ty = get( 1, 3 );
        final float tz = get( 2, 3 );
        final float w0 = get( 3, 0 );
        final float w1 = get( 3, 1 );
        final float w2 = get( 3, 2 );
        final float w3 = get( 3, 3 );
        
        set( quat );
        mulRotationScale( scale );
        
        // set back
        set( 0, 3, tx );
        set( 1, 3, ty );
        set( 2, 3, tz );
        set( 3, 0, w0 );
        set( 3, 1, w1 );
        set( 3, 2, w2 );
        set( 3, 3, w3 );
        
        return ( this );
    }
    
    /**
     * Sets the rotational component (upper 3x3) of this matrix to the matrix
     * equivalent values of the axis-angle argument; the other elements of this
     * matrix are unchanged; a singular value decomposition is performed on this
     * object's upper 3x3 matrix to factor out the scale, then this object's
     * upper 3x3 matrix components are replaced by the matrix equivalent of the
     * axis-angle, and then the scale is reapplied to the rotational components.
     * 
     * @param aa the axis-angle to be converted (x, y, z, angle)
     * 
     * @return itself
     */
    public final Matrix4f setRotation( AxisAngle3f aa )
    {
        final float scale = SVD( (Matrix3f)null, (Matrix4f)null );
        // save other values
        final float tx = get( 0, 3 );
        final float ty = get( 1, 3 );
        final float tz = get( 2, 3 );
        final float w0 = get( 3, 0 );
        final float w1 = get( 3, 1 );
        final float w2 = get( 3, 2 );
        final float w3 = get( 3, 3 );
        
        set( aa );
        mulRotationScale( scale );
        
        // set back
        set( 0, 3, tx );
        set( 1, 3, ty );
        set( 2, 3, tz );
        set( 3, 0, w0 );
        set( 3, 1, w1 );
        set( 3, 2, w2 );
        set( 3, 3, w3 );
        
        return ( this );
    }
    
    /**
     * Builds a rotation matrix.
     * 
     * @param tup
     * 
     * @return itself
     */
    public final Matrix4f setRotation( Tuple3f tup )
    {
        final float cx = FastMath.cos( tup.getX() );
        final float sx = FastMath.sin( tup.getX() );
        final float cy = FastMath.cos( tup.getY() );
        final float sy = FastMath.sin( tup.getY() );
        final float cz = FastMath.cos( tup.getZ() );
        final float sz = FastMath.sin( tup.getZ() );
        
        this.m00( cy * cz );
        this.m01( cy * sz );
        this.m02( -sy );
        
        this.m10( sx * sy * cz - cx * sz );
        this.m11( sx * sy * sz + cx * cz );
        this.m12( sx * cy );
        
        this.m20( cx * sy * cz + sx * sz );
        this.m21( cx * sy * sz - sx * cz );
        this.m22( cx * cy );
        
        this.m33( 1.0f );
        
        return ( this );
    }
    
    /**
     * Builds an inverse rotation matrix.
     * 
     * @param tup
     * 
     * @return itself
     */
    public final Matrix4f setInvRotation( Tuple3f tup )
    {
        final float cx = FastMath.cos( tup.getX() );
        final float sx = FastMath.sin( tup.getX() );
        final float cy = FastMath.cos( tup.getY() );
        final float sy = FastMath.sin( tup.getY() );
        final float cz = FastMath.cos( tup.getZ() );
        final float sz = FastMath.sin( tup.getZ() );
        
        this.m00( cy * cz );
        this.m10( cy * sz );
        this.m20( -sy );
        
        this.m01( sx * sy * cz - cx * sz );
        this.m11( sx * sy * sz + cx * cz );
        this.m21( sx * cy );
        
        this.m02( cx * sy * cz + sx * sz );
        this.m12( cx * sy * sz - sx * cz );
        this.m22( cx * cy );
        
        return ( this );
    }
    
    /**
     * Interpolates each value of this Matrix by the value alpha.
     * 
     * Mxy = M1xy + ( ( M2xy - M1xy ) * alpha )
     * 
     * @param m1
     * @param m2
     * @param alpha
     * @param interpolateLastLine
     */
    public void interpolate( Matrix4f m1, Matrix4f m2, float alpha, boolean interpolateLastLine )
    {
        if ( this.isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        this.values[ 0 ] = m1.values[ 0 ] + ( ( m2.values[ 0 ] - m1.values[ 0 ] ) * alpha );
        this.values[ 1 ] = m1.values[ 1 ] + ( ( m2.values[ 1 ] - m1.values[ 1 ] ) * alpha );
        this.values[ 2 ] = m1.values[ 2 ] + ( ( m2.values[ 2 ] - m1.values[ 2 ] ) * alpha );
        this.values[ 3 ] = m1.values[ 3 ] + ( ( m2.values[ 3 ] - m1.values[ 3 ] ) * alpha );
        this.values[ 4 ] = m1.values[ 4 ] + ( ( m2.values[ 4 ] - m1.values[ 4 ] ) * alpha );
        this.values[ 5 ] = m1.values[ 5 ] + ( ( m2.values[ 5 ] - m1.values[ 5 ] ) * alpha );
        this.values[ 6 ] = m1.values[ 6 ] + ( ( m2.values[ 6 ] - m1.values[ 6 ] ) * alpha );
        this.values[ 7 ] = m1.values[ 7 ] + ( ( m2.values[ 7 ] - m1.values[ 7 ] ) * alpha );
        this.values[ 8 ] = m1.values[ 8 ] + ( ( m2.values[ 8 ] - m1.values[ 8 ] ) * alpha );
        this.values[ 9 ] = m1.values[ 9 ] + ( ( m2.values[ 9 ] - m1.values[ 9 ] ) * alpha );
        this.values[ 10 ] = m1.values[ 10 ] + ( ( m2.values[ 10 ] - m1.values[ 10 ] ) * alpha );
        this.values[ 11 ] = m1.values[ 11 ] + ( ( m2.values[ 11 ] - m1.values[ 11 ] ) * alpha );
        if ( interpolateLastLine )
        {
            this.values[ 12 ] = m1.values[ 12 ] + ( ( m2.values[ 12 ] - m1.values[ 12 ] ) * alpha );
            this.values[ 13 ] = m1.values[ 13 ] + ( ( m2.values[ 13 ] - m1.values[ 13 ] ) * alpha );
            this.values[ 14 ] = m1.values[ 14 ] + ( ( m2.values[ 14 ] - m1.values[ 14 ] ) * alpha );
            this.values[ 15 ] = m1.values[ 15 ] + ( ( m2.values[ 15 ] - m1.values[ 15 ] ) * alpha );
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Interpolates each value of this Matrix by the value alpha.
     * 
     * Mxy = M1xy + ( ( M2xy - M1xy ) * alpha )
     * 
     * @param m1
     * @param m2
     * @param alpha
     */
    public final void interpolate( Matrix4f m1, Matrix4f m2, float alpha )
    {
        interpolate( m1, m2, alpha, true );
    }
    
    /**
     * Returns a hash number based on the data values in this object. Two
     * different Matrix4f objects with identical data values (ie, returns true
     * for equals(Matrix4f) ) will return the same hash number. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash value
     */
    @Override
    public int hashCode() {
        return ( VecMathUtils.floatToIntBits( m00() ) ^ VecMathUtils.floatToIntBits( m01() ) ^ VecMathUtils.floatToIntBits( m02() ) ^ VecMathUtils.floatToIntBits( m03() ) ^ 
                VecMathUtils.floatToIntBits( m10() ) ^ VecMathUtils.floatToIntBits( m11() ) ^ VecMathUtils.floatToIntBits( m12() ) ^ VecMathUtils.floatToIntBits( m13() ) ^
                VecMathUtils.floatToIntBits( m20() ) ^ VecMathUtils.floatToIntBits( m21() ) ^ VecMathUtils.floatToIntBits( m22() ) ^ VecMathUtils.floatToIntBits( m23() ) ^
                VecMathUtils.floatToIntBits( m30() ) ^ VecMathUtils.floatToIntBits( m31() ) ^ VecMathUtils.floatToIntBits( m32() ) ^ VecMathUtils.floatToIntBits( m33() )
              );
    }
    
    
    /**
     * Returns true if all of the data members of Matrix4f m1 are equal to the
     * corresponding data members in this Matrix4f.
     * 
     * @param mat2 The matrix with which the comparison is made.
     * @return true or false
     */
    public boolean equals( Matrix4f mat2 )
    {
        return ( super.equals( mat2 ) );
    }
    
    /**
     * Returns true if the Object o1 is of type Matrix4f and all of the data
     * members of t1 are equal to the corresponding data members in this
     * Matrix4f.
     * 
     * @param o the object with which the comparison is made.
     */
    @Override
    public boolean equals( Object o )
    {
        return ( o != null && ( ( o instanceof Matrix4f ) && equals( (Matrix4f)o ) ) );
    }
    
    /**
     * Returns true if the L-infinite distance between this matrix and matrix m1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[i=0,1,2,3 ; j=0,1,2,3 ;
     * abs(this.m(i,j) - m1.m(i,j)]
     * 
     * @param mat2 The matrix to be compared to this matrix
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( Matrix4f mat2, float epsilon )
    {
        return ( super.epsilonEquals( mat2, epsilon ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix4f clone()
    {
        return ( new Matrix4f( this ) );
    }
    
    /**
     * Performs SVD on this matrix and gets scale and rotation. Rotation is
     * placed into rot.
     * 
     * @param rot3 the rotation factor(Matrix3d).
     * @param rot4 the rotation factor(Matrix4f) only upper 3x3 elements are
     *            changed.
     * @return scale factor
     */
    private final float SVD( Matrix3f rot3, Matrix4f rot4 )
    {
        // this is a simple svd.
        // Not complete but fast and reasonable.
        // See comment in Matrix3d.
        
        final float s = FastMath.sqrt( ( get( 0, 0 ) * get( 0, 0 ) +
                                         get( 1, 0 ) * get( 1, 0 ) +
                                         get( 2, 0 ) * get( 2, 0 ) +
                                         get( 0, 1 ) * get( 0, 1 ) +
                                         get( 1, 1 ) * get( 1, 1 ) +
                                         get( 2, 1 ) * get( 2, 1 ) +
                                         get( 0, 2 ) * get( 0, 2 ) +
                                         get( 1, 2 ) * get( 1, 2 ) +
                                         get( 2, 2 ) * get( 2, 2 )
                                     ) / 3.0f );
        
        // zero-div may occur.
        final float t = ( s == 0.0f ? 0.0f : 1.0f / s );
        
        if ( rot3 != null )
        {
            this.getRotationScale( rot3 );
            rot3.mul( t );
        }
        
        if ( rot4 != null )
        {
            if ( rot4 != this )
                rot4.setRotationScale( this ); // private method
            rot4.mulRotationScale( t ); // private method
        }
        
        return ( s );
    }
    
    /**
     * Performs SVD on this matrix and gets the scale and the pure rotation. The
     * pure rotation is placed into rot.
     * 
     * @param rot the rotation factor.
     * @return scale factor
     */
    private final float SVD( Matrix3f rot )
    {
        // this is a simple svd.
        // Not complete but fast and reasonable.
        // See comment in Matrix3d.
        
        final float s = FastMath.sqrt( ( get( 0, 0 ) * get( 0, 0 ) +
                                         get( 1, 0 ) * get( 1, 0 ) +
                                         get( 2, 0 ) * get( 2, 0 ) +
                                         get( 0, 1 ) * get( 0, 1 ) +
                                         get( 1, 1 ) * get( 1, 1 ) +
                                         get( 2, 1 ) * get( 2, 1 ) +
                                         get( 0, 2 ) * get( 0, 2 ) +
                                         get( 1, 2 ) * get( 1, 2 ) +
                                         get( 2, 2 ) * get( 2, 2 )
                                       ) / 3.0f );
        
        // zero-div may occur.
        final float t = ( s == 0.0f ? 0.0f : 1.0f / s );
        
        if ( rot != null )
        {
            this.getRotationScale( rot );
            rot.mul( t );
        }
        
        return ( s );
    }
    
    /**
     * Multiplies 3x3 upper elements of this matrix by a scalar. The other
     * elements are unchanged.
     */
    private final void mulRotationScale( float scale )
    {
        set( 0, 0, get( 0, 0 ) * scale );
        set( 0, 1, get( 0, 1 ) * scale );
        set( 0, 2, get( 0, 2 ) * scale );
        set( 1, 0, get( 1, 0 ) * scale );
        set( 1, 1, get( 1, 1 ) * scale );
        set( 1, 2, get( 1, 2 ) * scale );
        set( 2, 0, get( 2, 0 ) * scale );
        set( 2, 1, get( 2, 1 ) * scale );
        set( 2, 2, get( 2, 2 ) * scale );
    }
    
    /**
     * Sets only 3x3 upper elements of this matrix to that of m1. The other
     * elements are unchanged.
     */
    private final void setRotationScale( Matrix4f that )
    {
        this.set( 0, 0, that.get( 0, 0 ) );
        this.set( 0, 1, that.get( 0, 1 ) );
        this.set( 0, 2, that.get( 0, 2 ) );
        this.set( 1, 0, that.get( 1, 0 ) );
        this.set( 1, 1, that.get( 1, 1 ) );
        this.set( 1, 2, that.get( 1, 2 ) );
        this.set( 2, 0, that.get( 2, 0 ) );
        this.set( 2, 1, that.get( 2, 1 ) );
        this.set( 2, 2, that.get( 2, 2 ) );
    }
    
    private final void setFromQuat( float a, float b, float c, float d )
    {
        final float n = a * a + b * b + c * c + d * d;
        final float s = (n > 0.0f) ? (2.0f / n) : 0.0f;
        
        final float xs = a * s, ys = b * s, zs = c * s;
        final float wx = d * xs, wy = d * ys, wz = d * zs;
        final float xx = a * xs, xy = a * ys, xz = a * zs;
        final float yy = b * ys, yz = b * zs, zz = c * zs;
        
        setIdentity();
        
        set( 0, 0, 1.0f - ( yy + zz ) );
        set( 0, 1, xy - wz );
        set( 0, 2, xz + wy );
        set( 1, 0, xy + wz );
        set( 1, 1, 1.0f - ( xx + zz ) );
        set( 1, 2, yz - wx );
        set( 2, 0, xz - wy );
        set( 2, 1, yz + wx );
        set( 2, 2, 1.0f - ( xx + yy ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix4f asReadOnly()
    {
        return ( new Matrix4f( true, this.dataBegin, this.colSkip, this.values, this.isDirty ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix4f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    private final void setFromAxisAngle( float x, float y, float z, float angle )
    {
        // Taken from Rick's which is taken from Wertz. pg. 412
        // Bug Fixed and changed into right-handed by hiranabe
        // zero-div may occur
        final float n = 1.0f / FastMath.sqrt( x * x + y * y + z * z );
        
        x *= n;
        y *= n;
        z *= n;
        
        final float c = FastMath.cos( angle );
        final float s = FastMath.sin( angle );
        final float omc = 1.0f - c;
        
        this.set( 0, 0, c + x * x * omc );
        this.set( 1, 1, c + y * y * omc );
        this.set( 2, 2, c + z * z * omc );
        
        float tmp1 = x * y * omc;
        float tmp2 = z * s;
        this.set( 0, 1, tmp1 - tmp2 );
        this.set( 1, 0, tmp1 + tmp2 );
        
        tmp1 = x * z * omc;
        tmp2 = y * s;
        this.set( 0, 2, tmp1 + tmp2 );
        this.set( 2, 0, tmp1 - tmp2 );
        
        tmp1 = y * z * omc;
        tmp2 = x * s;
        this.set( 1, 2, tmp1 - tmp2 );
        this.set( 2, 1, tmp1 + tmp2 );
    }
    
    
    /**
     * Constructs and initializes a Matrix4f to all zeros.
     * 
     * @param readOnly
     */
    protected Matrix4f( boolean readOnly )
    {
        super( readOnly, 4, 4 );
        
        Arrays.fill( this.values, 0.0f );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the specified 16 values.
     * 
     * @param readOnly
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m03 the [0][3] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m13 the [1][3] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     * @param m23 the [2][3] element
     * @param m30 the [3][0] element
     * @param m31 the [3][1] element
     * @param m32 the [3][2] element
     * @param m33 the [3][3] element
     */
    protected Matrix4f( boolean readOnly, float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33 )
    {
        super( readOnly, 4, 4 );
        
        this.values[ 0 * getNumCols() + 0 ] = m00;
        this.values[ 0 * getNumCols() + 1 ] = m01;
        this.values[ 0 * getNumCols() + 2 ] = m02;
        this.values[ 0 * getNumCols() + 3 ] = m03;
        this.values[ 1 * getNumCols() + 0 ] = m10;
        this.values[ 1 * getNumCols() + 1 ] = m11;
        this.values[ 1 * getNumCols() + 2 ] = m12;
        this.values[ 1 * getNumCols() + 3 ] = m13;
        this.values[ 2 * getNumCols() + 0 ] = m20;
        this.values[ 2 * getNumCols() + 1 ] = m21;
        this.values[ 2 * getNumCols() + 2 ] = m22;
        this.values[ 2 * getNumCols() + 3 ] = m23;
        this.values[ 3 * getNumCols() + 0 ] = m30;
        this.values[ 3 * getNumCols() + 1 ] = m31;
        this.values[ 3 * getNumCols() + 2 ] = m32;
        this.values[ 3 * getNumCols() + 3 ] = m33;
    }
    
    /**
     * Constructs and initializes a Matrix4f from the specified 16 element
     * array. this.m00 =v[0], this.m01=v[1], etc.
     * 
     * @param readOnly
     * @param values the array of length 16 containing in order
     */
    protected Matrix4f( boolean readOnly, float[] values )
    {
        super( readOnly, 4, 4 );
        
        final int size = getNumRows() * getNumCols();
        System.arraycopy( values, 0, this.values, 0, size );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the quaternion, translation,
     * and scale values; the scale is applied only to the rotational components
     * of the matrix (upper 3x3) and not to the translational components.
     * 
     * @param readOnly
     * @param rot The quaternion value representing the rotational component
     * @param pos The translational component of the matrix
     * @param scale The scale value applied to the rotational components
     */
    protected Matrix4f( boolean readOnly, Quaternion4f rot, Tuple3f pos, float scale )
    {
        super( readOnly, 4, 4 );
        
        setFromQuat( rot.getA(), rot.getB(), rot.getC(), rot.getD() );
        
        this.values[ 0 * getNumCols() + 0 ] *= scale;
        this.values[ 0 * getNumCols() + 1 ] *= scale;
        this.values[ 0 * getNumCols() + 2 ] *= scale;
        this.values[ 1 * getNumCols() + 0 ] *= scale;
        this.values[ 1 * getNumCols() + 1 ] *= scale;
        this.values[ 1 * getNumCols() + 2 ] *= scale;
        this.values[ 2 * getNumCols() + 0 ] *= scale;
        this.values[ 2 * getNumCols() + 1 ] *= scale;
        this.values[ 2 * getNumCols() + 2 ] *= scale;
        
        this.values[ 0 * getNumCols() + 3 ] = pos.getX();
        this.values[ 1 * getNumCols() + 3 ] = pos.getY();
        this.values[ 2 * getNumCols() + 3 ] = pos.getZ();
    }
    
    /**
     * Constructs and initializes a Matrix4f from the rotation matrix,
     * translation, and scale values; the scale is applied only to the
     * rotational components of the matrix (upper 3x3) and not to the
     * translational components.
     * 
     * @param readOnly
     * @param pos The translational components of the matrix
     * @param rot The rotation matrix representing the rotational components
     * @param scale The scale value applied to the rotational components
     */
    protected Matrix4f( boolean readOnly, Tuple3f pos, Matrix3f rot, float scale )
    {
        super( readOnly, 4, 4 );
        
        this.values[ 0 ] = rot.m00();
        this.values[ 1 ] = rot.m01();
        this.values[ 2 ] = rot.m02();
        this.values[ getNumCols() + 0 ] = rot.m10();
        this.values[ getNumCols() + 1 ] = rot.m11();
        this.values[ getNumCols() + 2 ] = rot.m12();
        this.values[ 2 * getNumCols() + 0 ] = rot.m20();
        this.values[ 2 * getNumCols() + 1 ] = rot.m21();
        this.values[ 2 * getNumCols() + 2 ] = rot.m22();
        this.values[ 3 * getNumCols() + 0 ] = 0.0f;
        this.values[ 3 * getNumCols() + 1 ] = 0.0f;
        this.values[ 3 * getNumCols() + 2 ] = 0.0f;
        this.values[ 3 * getNumCols() + 3 ] = 1.0f;
        
        this.values[ 0 * getNumCols() + 0 ] *= scale;
        this.values[ 0 * getNumCols() + 1 ] *= scale;
        this.values[ 0 * getNumCols() + 2 ] *= scale;
        this.values[ 1 * getNumCols() + 0 ] *= scale;
        this.values[ 1 * getNumCols() + 1 ] *= scale;
        this.values[ 1 * getNumCols() + 2 ] *= scale;
        this.values[ 2 * getNumCols() + 0 ] *= scale;
        this.values[ 2 * getNumCols() + 1 ] *= scale;
        this.values[ 2 * getNumCols() + 2 ] *= scale;
        
        this.values[ 0 * getNumCols() + 3 ] = pos.getX();
        this.values[ 1 * getNumCols() + 3 ] = pos.getY();
        this.values[ 2 * getNumCols() + 3 ] = pos.getZ();
        
        this.values[ 3 * getNumCols() + 3 ] = 1.0f;
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix4f parameter.
     * 
     * @param readOnly
     * @param mat The source matrix.
     */
    protected Matrix4f( boolean readOnly, Matrix4f mat )
    {
        this( readOnly,
              mat.m00(), mat.m01(), mat.m02(), mat.m03(),
              mat.m10(), mat.m11(), mat.m12(), mat.m13(),
              mat.m20(), mat.m21(), mat.m22(), mat.m23(),
              mat.m30(), mat.m31(), mat.m32(), mat.m33()
            );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix3f parameter.
     * 
     * @param readOnly
     * @param mat The source matrix.
     */
    protected Matrix4f( boolean readOnly, Matrix3f mat )
    {
        this( readOnly,
              mat.m00(), mat.m01(), mat.m02(), 0f,
              mat.m10(), mat.m11(), mat.m12(), 0f,
              mat.m20(), mat.m21(), mat.m22(), 0f,
              0f, 0f, 0f, 1f
            );
    }

    /**
     * Hidden constructor for #sharedSubMatrix3f(MatrixMxNf, int, int).
     * 
     * @param readOnly
     * @param dataBegin
     * @param colSkip
     * @param values
     * @param isDirty
     */
    protected Matrix4f( boolean readOnly, int dataBegin, int colSkip, float[] values, boolean[] isDirty )
    {
        super( readOnly, dataBegin, colSkip, 4, 4, values, isDirty );
    }
    
    /**
     * Constructs and initializes a Matrix4f to all zeros.
     */
    public Matrix4f()
    {
        this( false );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the specified 16 values.
     * 
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m03 the [0][3] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m13 the [1][3] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     * @param m23 the [2][3] element
     * @param m30 the [3][0] element
     * @param m31 the [3][1] element
     * @param m32 the [3][2] element
     * @param m33 the [3][3] element
     */
    public Matrix4f( float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33 )
    {
        this( false, m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the specified 16 element
     * array. this.m00 =v[0], this.m01=v[1], etc.
     * 
     * @param values the array of length 16 containing in order
     */
    public Matrix4f( float[] values )
    {
        this( false, values );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the quaternion, translation,
     * and scale values; the scale is applied only to the rotational components
     * of the matrix (upper 3x3) and not to the translational components.
     * 
     * @param rot The quaternion value representing the rotational component
     * @param pos The translational component of the matrix
     * @param scale The scale value applied to the rotational components
     */
    public Matrix4f( Quaternion4f rot, Tuple3f pos, float scale )
    {
        this( false, rot, pos, scale );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the rotation matrix,
     * translation, and scale values; the scale is applied only to the
     * rotational components of the matrix (upper 3x3) and not to the
     * translational components.
     * 
     * @param pos The translational components of the matrix
     * @param rot The rotation matrix representing the rotational components
     * @param scale The scale value applied to the rotational components
     */
    public Matrix4f( Tuple3f pos, Matrix3f rot, float scale )
    {
        this( false, pos, rot, scale );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix4f parameter.
     * 
     * @param mat The source matrix.
     */
    public Matrix4f( Matrix4f mat )
    {
        this( false, mat );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix3f parameter.
     * 
     * @param mat The source matrix.
     */
    public Matrix4f( Matrix3f mat )
    {
        this( false, mat );
    }

    /**
     * Hidden constructor for #sharedSubMatrix3f(MatrixMxNf, int, int).
     * 
     * @param dataBegin
     * @param colSkip
     * @param values
     * @param isDirty
     */
    protected Matrix4f( int dataBegin, int colSkip, float[] values, boolean[] isDirty )
    {
        this( false, dataBegin, colSkip, values, isDirty );
    }
    
    /**
     * Constructs and initializes a Matrix4f to all zeros.
     */
    public static Matrix4f newReadOnly()
    {
        return ( new Matrix4f( true ) );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the specified 16 values.
     * 
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m03 the [0][3] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m13 the [1][3] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     * @param m23 the [2][3] element
     * @param m30 the [3][0] element
     * @param m31 the [3][1] element
     * @param m32 the [3][2] element
     * @param m33 the [3][3] element
     */
    public static Matrix4f newReadOnly( float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33 )
    {
        return ( new Matrix4f( true, m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 ) );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the specified 16 element
     * array. this.m00 =v[0], this.m01=v[1], etc.
     * 
     * @param values the array of length 16 containing in order
     */
    public static Matrix4f newReadOnly( float[] values )
    {
        return ( new Matrix4f( true, values ) );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the quaternion, translation,
     * and scale values; the scale is applied only to the rotational components
     * of the matrix (upper 3x3) and not to the translational components.
     * 
     * @param rot The quaternion value representing the rotational component
     * @param pos The translational component of the matrix
     * @param scale The scale value applied to the rotational components
     */
    public static Matrix4f newReadOnly( Quaternion4f rot, Tuple3f pos, float scale )
    {
        return ( new Matrix4f( true, rot, pos, scale ) );
    }
    
    /**
     * Constructs and initializes a Matrix4f from the rotation matrix,
     * translation, and scale values; the scale is applied only to the
     * rotational components of the matrix (upper 3x3) and not to the
     * translational components.
     * 
     * @param pos The translational components of the matrix
     * @param rot The rotation matrix representing the rotational components
     * @param scale The scale value applied to the rotational components
     */
    public static Matrix4f newReadOnly( Tuple3f pos, Matrix3f rot, float scale )
    {
        return ( new Matrix4f( true, pos, rot, scale ) );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix4f parameter.
     * 
     * @param mat The source matrix.
     */
    public static Matrix4f newReadOnly( Matrix4f mat )
    {
        return ( new Matrix4f( true, mat ) );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix3f parameter.
     * 
     * @param mat The source matrix.
     */
    public static Matrix4f newReadOnly( Matrix3f mat )
    {
        return ( new Matrix4f( true, mat ) );
    }
    
    /**
     * Creates a Submatrix of mat, that begins in beginRow and beginCol.<br>
     * Example: let mat be a 4x4 matrix, and we want to have a 2x2 submatrix at position (1,2):<br>
     * <tt>
     * x x x x <br>
     * x x y y <br>
     * x x y y <br>
     * x x x x <br> </tt>
     * 
     * the y's mark the fetched Submatrix.
     * <br>
     * The produced submatrix works on the same data array as mat, so changes are seen on the other one respectively.
     * 
     * @param mat the parent matrix
     * @param beginRow the row to start this matrix at
     * @param beginCol the column to start this matrix at
     * 
     * @return the new shared submatrix
     * 
     * @see #Matrix4f(int, int, float[])
     */
    public static Matrix4f sharedSubMatrix4f( MatrixMxNf mat, int beginRow, int beginCol )
    {
        return ( new Matrix4f( beginRow, beginCol, mat.values, null ) );
    }
    
    /**
     * Allocates an Matrix4f instance from the pool.
     */
    public static Matrix4f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Stores the given Matrix3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Matrix4f o )
    {
        POOL.get().free( o );
    }
}
