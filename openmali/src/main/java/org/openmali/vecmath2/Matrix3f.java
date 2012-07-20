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

import org.openmali.FastMath;
import org.openmali.vecmath2.pools.Matrix3fPool;
import org.openmali.vecmath2.util.MatrixUtils;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * A single precision floating point 3 by 3 matrix.
 * 
 * Inspired by Kenji Hiranabe's Matrix3f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Matrix3f extends MatrixMxNf implements Externalizable, Cloneable
{
    private static final long serialVersionUID = 7821487009107178638L;
    
    public static final Matrix3f ZERO = Matrix3f.newReadOnly( 0f, 0f, 0f,
                                                              0f, 0f, 0f,
                                                              0f, 0f, 0f
                                                            );
    
    public static final Matrix3f IDENTITY = Matrix3f.newReadOnly( 1f, 0f, 0f,
                                                                  0f, 1f, 0f,
                                                                  0f, 0f, 1f
                                                                );
    
    public static final Matrix3f ROT_PLUS_90_DEG_BY_X_AXIS;
    public static final Matrix3f ROT_MINUS_90_DEG_BY_X_AXIS;
    public static final Matrix3f ROT_PLUS_90_DEG_BY_Y_AXIS;
    public static final Matrix3f ROT_MINUS_90_DEG_BY_Y_AXIS;
    public static final Matrix3f ROT_PLUS_90_DEG_BY_Z_AXIS;
    public static final Matrix3f ROT_MINUS_90_DEG_BY_Z_AXIS;
    
    static
    {
        Matrix3f tmp = MatrixUtils.getRotationMatrix( Vector3f.POSITIVE_X_AXIS, +FastMath.PI_HALF );
        ROT_PLUS_90_DEG_BY_X_AXIS = new Matrix3f( true, tmp );
        MatrixUtils.getRotationMatrix( Vector3f.POSITIVE_X_AXIS, -FastMath.PI_HALF, tmp );
        ROT_MINUS_90_DEG_BY_X_AXIS = new Matrix3f( true, tmp );
        MatrixUtils.getRotationMatrix( Vector3f.POSITIVE_Y_AXIS, +FastMath.PI_HALF, tmp );
        ROT_PLUS_90_DEG_BY_Y_AXIS = new Matrix3f( true, tmp );
        MatrixUtils.getRotationMatrix( Vector3f.POSITIVE_Y_AXIS, -FastMath.PI_HALF, tmp );
        ROT_MINUS_90_DEG_BY_Y_AXIS = new Matrix3f( true, tmp );
        MatrixUtils.getRotationMatrix( Vector3f.POSITIVE_Z_AXIS, +FastMath.PI_HALF, tmp );
        ROT_PLUS_90_DEG_BY_Z_AXIS = new Matrix3f( true, tmp );
        MatrixUtils.getRotationMatrix( Vector3f.POSITIVE_Z_AXIS, -FastMath.PI_HALF, tmp );
        ROT_MINUS_90_DEG_BY_Z_AXIS = new Matrix3f( true, tmp );
    }
    
    public static final Matrix3f Z_UP_TO_Y_UP = ROT_MINUS_90_DEG_BY_X_AXIS;
    
    //private static final Matrix3fPool POOL = new Matrix3fPool( 128 );
    private static final ThreadLocal<Matrix3fPool> POOL = new ThreadLocal<Matrix3fPool>()
    {
        @Override
        protected Matrix3fPool initialValue()
        {
            return ( new Matrix3fPool( 128 ) );
        }
    };
    
    private Matrix3f readOnlyInstance = null;
    
    protected static final int M = 3;
    protected static final int N = 3;
    
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
    
    public final Matrix3f m00( float v )
    {
        set( 0, 0, v );
        
        return ( this );
    }
    
    public final Matrix3f m01( float v )
    {
        set( 0, 1, v );
        
        return ( this );
    }
    
    public final Matrix3f m02( float v )
    {
        set( 0, 2, v );
        
        return ( this );
    }
    
    public final Matrix3f m10( float v )
    {
        set( 1, 0, v );
        
        return ( this );
    }
    
    public final Matrix3f m11( float v )
    {
        set( 1, 1, v );
        
        return ( this );
    }
    
    public final Matrix3f m12( float v )
    {
        set( 1, 2, v );
        
        return ( this );
    }
    
    public final Matrix3f m20( float v )
    {
        set( 2, 0, v );
        
        return ( this );
    }
    
    public final Matrix3f m21( float v )
    {
        set( 2, 1, v );
        
        return ( this );
    }
    
    public final Matrix3f m22( float v )
    {
        set( 2, 2, v );
        
        return ( this );
    }
    
    /**
     * Sets the specified row of this matrix3d to the three values provided.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param x the first column element
     * @param y the second column element
     * @param z the third column element
     * 
     * @return itself
     */
    public final Matrix3f setRow( int row, float x, float y, float z )
    {
        if ( ( row >= 0 ) && ( row <= M ) )
        {
             this.set( row, 0, x );
             this.set( row, 1, y );
             this.set( row, 2, z );
             
             return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "row must be 0 to 2 and is " + row );
    }
    
    /**
     * Sets the specified row of this matrix3d to the Vector provided.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param t3f the replacement row
     * 
     * @return itself
     */
    public final Matrix3f setRow( int row, Tuple3f t3f )
    {
        if ( ( row >= 0 ) && ( row <= M ) )
        {
             set( row, 0, t3f.getX() );
             set( row, 1, t3f.getY() );
             set( row, 2, t3f.getZ() );
             
             return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "row must be 0 to 2 and is " + row );
    }
    
    /**
     * Copies the matrix values in the specified row into the vector parameter.
     * 
     * @param row the matrix row
     * @param t3f The vector into which the matrix row values will be copied
     * 
     * @return itself
     */
    public final Matrix3f getRow( int row, Tuple3f t3f )
    {
        if ( ( row >= 0 ) && ( row <= M ) )
        {
             t3f.setX( get( row, 0 ) );
             t3f.setY( get( row, 1 ) );
             t3f.setZ( get( row, 2 ) );
             
             return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "row must be 0 to 2 and is " + row );
    }
    
    /**
     * Sets the specified column of this matrix3d to the three values provided.
     * 
     * @param column the column number to be modified (zero indexed)
     * @param x the first row element
     * @param y the second row element
     * @param z the third row element
     * 
     * @return itself
     */
    public final Matrix3f setColumn( int column, float x, float y, float z )
    {
        if ( ( column >= 0 ) && ( column <= N ) )
        {
            set( 0, column, x );
            set( 1, column, y );
            set( 2, column, z );
            
            return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "column must be 0 to 2 and is " + column );
    }
    
    /**
     * Sets the specified column of this matrix3d to the vector provided.
     * 
     * @param column the column number to be modified (zero indexed)
     * @param t3f the replacement column
     * 
     * @return itself
     */
    public final Matrix3f setColumn( int column, Tuple3f t3f )
    {
        if ( ( column >= 0 ) && ( column <= N ) )
        {
            set( 0, column, t3f.getX() );
            set( 1, column, t3f.getY() );
            set( 2, column, t3f.getZ() );
            
            return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "column must be 0 to 2 and is " + column );
    }
    
    /**
     * Copies the matrix values in the specified column into the vector
     * parameter.
     * 
     * @param column the matrix column
     * @param t3f The vector into which the matrix row values will be copied
     * 
     * @return itself
     */
    public final Matrix3f getColumn( int column, Tuple3f t3f )
    {
        if ( ( column >= 0 ) && ( column <= N ) )
        {
            t3f.setX( get( 0, column ) );
            t3f.setY( get( 1, column ) );
            t3f.setZ( get( 2, column ) );
            
            return ( this );
        }
        
        throw new ArrayIndexOutOfBoundsException( "column must be 0 to 2 and is " + column );
    }
    
    /**
     * Sets the value of this matrix to a scale matrix with the passed scale
     * amount.
     * 
     * @param scale the scale factor for the matrix
     * 
     * @return itself
     */
    public final Matrix3f set( float scale )
    {
        for ( int r = 0; r < M; r++ )
        {
            for ( int c = 0; c < N; c++ )
            {
                if ( r == c )
                    set( r, c, scale );
                else
                    set( r, c, 0.0f );
            }
        }
        
        return ( this );
    }
    
    /**
     * Performs an SVD normalization of this matrix to calculate and return the
     * uniform scale factor. This matrix is not modified.
     * 
     * @return the scale factor of this matrix
     */
    public final float getScale()
    {
        return ( SVD( null ) );
    }
    
    /**
     * Sets the value of this matrix to the matrix conversion of the (single
     * precision) quaternion argument.
     * 
     * @param quat the quaternion to be converted
     */
    public final void set( Quaternion4f quat )
    {
        setFromQuat( quat.getA(), quat.getB(), quat.getC(), quat.getD() );
    }
    
    /**
     * Sets the value of this matrix to the matrix conversion of the single
     * precision axis and angle argument.
     * 
     * @param aa3f the axis and angle to be converted
     */
    public final void set( AxisAngle3f aa3f )
    {
        setFromAxisAngle( aa3f.getX(), aa3f.getY(), aa3f.getZ(), aa3f.getAngle() );
    }
    
    /**
     * Sets 9 values
     * 
     * @param m00
     * @param m01
     * @param m02
     * @param m10
     * @param m11
     * @param m12
     * @param m20
     * @param m21
     * @param m22
     * 
     * @return itself
     */
    public final Matrix3f set( float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22 )
    {
        m00( m00 );
        m01( m01 );
        m02( m02 );
        m10( m10 );
        m11( m11 );
        m12( m12 );
        m20( m20 );
        m21( m21 );
        m22( m22 );
        
        return ( this );
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
        
        buffer.put( values[ 0 ] ).put( values[ 3 ] ).put( values[ 6 ] ).
               put( values[ 1 ] ).put( values[ 4 ] ).put( values[ 7 ] ).
               put( values[ 2 ] ).put( values[ 5 ] ).put( values[ 8 ] );
        
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
        
        buffer.put( values[ 0 ] ).put( values[ 3 ] ).put( values[ 6 ] ).
               put( values[ 1 ] ).put( values[ 4 ] ).put( values[ 7 ] ).
               put( values[ 2 ] ).put( values[ 5 ] ).put( values[ 8 ] );
        
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
    public static java.nio.FloatBuffer writeToBuffer( Matrix3f[] matrices, java.nio.FloatBuffer buffer, boolean clear, boolean flip )
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
    public static java.nio.FloatBuffer writeToBuffer( Matrix3f[] matrices, java.nio.FloatBuffer buffer, int position, boolean clear, boolean flip )
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
        buffer.get( values, 0, 1 ).get( values, 3, 1 ).get( values, 6, 1 ).
               get( values, 1, 1 ).get( values, 4, 1 ).get( values, 7, 1 ).
               get( values, 2, 1 ).get( values, 5, 1 ).get( values, 8, 1 );
        
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
        
        buffer.get( values, 0, 1 ).get( values, 3, 1 ).get( values, 6, 1 ).
               get( values, 1, 1 ).get( values, 4, 1 ).get( values, 7, 1 ).
               get( values, 2, 1 ).get( values, 5, 1 ).get( values, 8, 1 );
        
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
    public static java.nio.FloatBuffer readFromBuffer( Matrix3f[] matrices, java.nio.FloatBuffer buffer )
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
    public static java.nio.FloatBuffer readFromBuffer( Matrix3f[] matrices, java.nio.FloatBuffer buffer, int position )
    {
        buffer.position( position );
        
        for ( int i = 0; i < matrices.length; i++ )
        {
            matrices[ i ].readFromBuffer( buffer );
        }
        
        return ( buffer );
    }
    
    /**
     * Sets the value of this matrix equal to the negation of of the Matrix3f
     * parameter.
     * 
     * @param mat The source matrix
     * 
     * @return itself
     */
    public final Matrix3f negate( Matrix3f mat )
    {
        set( mat );
        negate();
        
        return ( this );
    }
    
    /**
     * Computes the determinant of this matrix.
     * 
     * @return the determinant of the matrix
     */
    public final float determinant()
    {
        // less *,+,- calculation than expanded expression.
        return ( m00() * ( m11() * m22() - m21() * m12() ) - m01() * ( m10() * m22() - m20() * m12() ) + m02() * ( m10() * m21() - m20() * m11() ) );
    }
    
    /**
     * Performs singular value decomposition normalization of this matrix.
     * 
     * @return itself
     */
    public final Matrix3f normalize()
    {
        SVD( this );
        
        return ( this );
    }
    
    /**
     * Perform singular value decomposition normalization of matrix m1 and place
     * the normalized values into this.
     * 
     * @param mat Provides the matrix values to be normalized
     * 
     * @return itself
     */
    public final Matrix3f normalize( Matrix3f mat )
    {
        set( mat );
        SVD( this );
        
        return ( this );
    }
    
    /**
     * Performs cross product normalization of this matrix.
     * 
     * @return itself
     */
    public final Matrix3f normalizeCP()
    {
        // domain error may occur
        float s = FastMath.pow( determinant(), -1.0f / 3.0f );
        mul( s );
        
        return ( this );
    }
    
    /**
     * Performs cross product normalization of matrix m1 and place the normalized
     * values into this.
     * 
     * @param mat Provides the matrix values to be normalized
     * 
     * @return itself
     */
    public final Matrix3f normalizeCP( Matrix3f mat )
    {
        set( mat );
        normalizeCP();
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the transpose of the argument matrix
     * 
     * @param mat the matrix to be transposed
     * 
     * @return itself
     */
    public final Matrix3f transpose( Matrix3f mat )
    {
        super.transpose( mat );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix3f mul( float scalar )
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
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to its inverse.
     */
    @Override
    public Matrix3f invert()
    {
        float d = determinant();
        if ( d == 0.0f )
            return ( this );
        d = 1.0f / d;
        
        if ( isReadOnly() )
            throw new Error( "This is a read-only Matrix" );
        
        // alias-safe way.
        set( values[4] * values[8] - values[5] * values[7], values[2] * values[7] - values[1] * values[8], values[1] * values[5] - values[2] * values[4],
             values[5] * values[6] - values[3] * values[8], values[0] * values[8] - values[2] * values[6], values[2] * values[3] - values[0] * values[5],
             values[3] * values[7] - values[4] * values[6], values[1] * values[6] - values[0] * values[7], values[0] * values[4] - values[1] * values[3]
           );
        
        mul( d );
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix inverse of the passed matrix mat.
     * 
     * @param mat the matrix to be inverted
     */
    public final void invert( Matrix3f mat )
    {
        set( mat );
        
        invert();
    }
    
    /**
     * Transform the vector vec using this Matrix3f and place the result into
     * vecOut.
     * 
     * @param t3f the single precision vector to be transformed
     * @param result the vector into which the transformed values are placed
     */
    public final void transform( Tuple3f t3f, Tuple3f result )
    {
        // alias-safe
        result.set( m00() * t3f.getX() + m01() * t3f.getY() + m02() * t3f.getZ(),
                    m10() * t3f.getX() + m11() * t3f.getY() + m12() * t3f.getZ(),
                    m20() * t3f.getX() + m21() * t3f.getY() + m22() * t3f.getZ()
                  );
    }
    
    /**
     * Transform the vector vec using this Matrix3f and place the result back
     * into t3f.
     * 
     * @param t3f the single precision vector to be transformed
     */
    public final void transform( Tuple3f t3f )
    {
        // alias-safe
        transform( t3f, t3f );
    }
    
    /**
     * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public final void add( Matrix3f m1, Matrix3f m2 )
    {
        for ( int i = 0; i < M; i++ )
        {
            for ( int j = 0; j < N; j++ )
            {
                set( i, j, m1.get( i, j ) + m2.get( i, j ) );
            }
        }
    }
    
    /**
     * Sets the value of this matrix to sum of itself and matrix m2.
     * 
     * @param m2 the other matrix
     */
    public final void add( Matrix3f m2 )
    {
        for ( int i = 0; i < M; i++ )
        {
            for ( int j = 0; j < N; j++ )
            {
                set( i, j, this.get( i, j ) + m2.get( i, j ) );
            }
        }
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of matrices m1 and m2.
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public final void sub( Matrix3f m1, Matrix3f m2 )
    {
        for ( int i = 0; i < M; i++ )
        {
            for ( int j = 0; j < N; j++ )
            {
                set( i, j, m1.get( i, j ) - m2.get( i, j ) );
            }
        }
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of itself and
     * matrix m1 (this = this - m1).
     * 
     * @param m2 the other matrix
     */
    public final void sub( Matrix3f m2 )
    {
        for ( int i = 0; i < M; i++ )
        {
            for ( int j = 0; j < N; j++ )
            {
                set( i, j, this.get( i, j ) - m2.get( i, j ) );
            }
        }
    }
    
    /**
     * Sets the value of this matrix to a rotation matrix about the x axis by
     * the passed angle.
     * 
     * @param angle the angle to rotate about the X axis in radians
     */
    public final void rotX( float angle )
    {
        final float c = FastMath.cos( angle );
        final float s = FastMath.sin( angle );
        
        set( 0, 0, 1.0f );
        set( 0, 1, 0.0f );
        set( 0, 2, 0.0f );
        set( 1, 0, 0.0f );
        set( 1, 1, c );
        set( 1, 2, -s );
        set( 2, 0, 0.0f );
        set( 2, 1, s );
        set( 2, 2, c );
    }
    
    /**
     * Sets the value of this matrix to a rotation matrix about the y axis by
     * the passed angle.
     * 
     * @param angle the angle to rotate about the Y axis in radians
     */
    public final void rotY( float angle )
    {
        final float c = FastMath.cos( angle );
        final float s = FastMath.sin( angle );
        
        set( 0, 0, c );
        set( 0, 1, 0.0f );
        set( 0, 2, s );
        set( 1, 0, 0.0f );
        set( 1, 1, 1.0f );
        set( 1, 2, 0.0f );
        set( 2, 0, -s );
        set( 2, 1, 0.0f );
        set( 2, 2, c );
    }
    
    /**
     * Sets the value of this matrix to a rotation matrix about the z axis by
     * the passed angle.
     * 
     * @param angle the angle to rotate about the Z axis in radians
     */
    public final void rotZ( float angle )
    {
        final float c = FastMath.cos( angle );
        final float s = FastMath.sin( angle );
        
        set( 0, 0, c );
        set( 0, 1, -s );
        set( 0, 2, 0.0f );
        set( 1, 0, s );
        set( 1, 1, c );
        set( 1, 2, 0.0f );
        set( 2, 0, 0.0f );
        set( 2, 1, 0.0f );
        set( 2, 2, 1.0f );
    }
    
    /**
     * Multiplies each element of matrix m1 by a scalar and places the result
     * into this. Matrix m1 is not modified.
     * 
     * @param scalar The scalar multiplier.
     * @param mat The original matrix.
     */
    public final void mul( float scalar, Matrix3f mat )
    {
        set( mat );
        
        mul( scalar );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying the two
     * argument matrices together.
     * 
     * @param mat1 the first matrix
     * @param mat2 the second matrix
     */
    public final void mul( Matrix3f mat1, Matrix3f mat2 )
    {
        // alias-safe way.
        set( mat1.m00() * mat2.m00() + mat1.m01() * mat2.m10() + mat1.m02() * mat2.m20(),
             mat1.m00() * mat2.m01() + mat1.m01() * mat2.m11() + mat1.m02() * mat2.m21(),
             mat1.m00() * mat2.m02() + mat1.m01() * mat2.m12() + mat1.m02() * mat2.m22(),
             mat1.m10() * mat2.m00() + mat1.m11() * mat2.m10() + mat1.m12() * mat2.m20(),
             mat1.m10() * mat2.m01() + mat1.m11() * mat2.m11() + mat1.m12() * mat2.m21(),
             mat1.m10() * mat2.m02() + mat1.m11() * mat2.m12() + mat1.m12() * mat2.m22(),
             mat1.m20() * mat2.m00() + mat1.m21() * mat2.m10() + mat1.m22() * mat2.m20(),
             mat1.m20() * mat2.m01() + mat1.m21() * mat2.m11() + mat1.m22() * mat2.m21(),
             mat1.m20() * mat2.m02() + mat1.m21() * mat2.m12() + mat1.m22() * mat2.m22()
           );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying itself with
     * matrix mat.
     * 
     * @param mat the other matrix
     */
    public final void mul( Matrix3f mat )
    {
        mul( this, mat );
    }
    
    public final void mul( Tuple3f vec, Tuple3f t3dOut )
    {
        t3dOut.set( ( ( m00() * vec.getX() ) + ( m01() * vec.getY() ) + ( m02() * vec.getZ() ) ),
                    ( ( m10() * vec.getX() ) + ( m11() * vec.getY() ) + ( m12() * vec.getZ() ) ),
                    ( ( m20() * vec.getX() ) + ( m21() * vec.getY() ) + ( m22() * vec.getZ() ) )
                  );
    }
    
    /**
     * Multiplies this matrix by matrix m1, does an SVD normalization of the
     * result, and places the result back into this matrix
     * this = SVDnorm(this * m1).
     * 
     * @param mat the matrix on the right hand side of the multiplication
     */
    public final void mulNormalize( Matrix3f mat )
    {
        mul( mat );
        SVD( this );
    }
    
    /**
     * Multiplies matrix m1 by matrix m2, does an SVD normalization of the
     * result, and places the result into this matrix this = SVDnorm(m1*m2).
     * 
     * @param mat1 the matrix on the left hand side of the multiplication
     * @param mat2 the matrix on the right hand side of the multiplication
     */
    public final void mulNormalize( Matrix3f mat1, Matrix3f mat2 )
    {
        mul( mat1, mat2 );
        SVD( this );
    }
    
    /**
     * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
     * and places the result into this.
     * 
     * @param mat1 The matrix on the left hand side of the multiplication
     * @param mat2 The matrix on the right hand side of the multiplication
     */
    public final void mulTransposeBoth( Matrix3f mat1, Matrix3f mat2 )
    {
        mul( mat2, mat1 );
        transpose();
    }
    
    /**
     * Multiplies matrix m1 times the transpose of matrix m2, and places the
     * result into this.
     * 
     * @param m1 The matrix on the left hand side of the multiplication
     * @param m2 The matrix on the right hand side of the multiplication
     */
    public final void mulTransposeRight( Matrix3f m1, Matrix3f m2 )
    {
        // alias-safe way.
        set( 0, 0, m1.m00() * m2.m00() + m1.m01() * m2.m01() + m1.m02() * m2.m02() );
        set( 0, 1, m1.m00() * m2.m10() + m1.m01() * m2.m11() + m1.m02() * m2.m12() );
        set( 0, 2, m1.m00() * m2.m20() + m1.m01() * m2.m21() + m1.m02() * m2.m22() );
        set( 1, 0, m1.m10() * m2.m00() + m1.m11() * m2.m01() + m1.m12() * m2.m02() );
        set( 1, 1, m1.m10() * m2.m10() + m1.m11() * m2.m11() + m1.m12() * m2.m12() );
        set( 1, 2, m1.m10() * m2.m20() + m1.m11() * m2.m21() + m1.m12() * m2.m22() );
        set( 2, 0, m1.m20() * m2.m00() + m1.m21() * m2.m01() + m1.m22() * m2.m02() );
        set( 2, 1, m1.m20() * m2.m10() + m1.m21() * m2.m11() + m1.m22() * m2.m12() );
        set( 2, 2, m1.m20() * m2.m20() + m1.m21() * m2.m21() + m1.m22() * m2.m22() );
    }
    
    /**
     * Multiplies the transpose of matrix m1 times matrix m2, and places the
     * result into this.
     * 
     * @param mat1 The matrix on the left hand side of the multiplication
     * @param mat2 The matrix on the right hand side of the multiplication
     */
    public final void mulTransposeLeft( Matrix3f mat1, Matrix3f mat2 )
    {
        // alias-safe way.
        set( 0, 0, mat1.m00() * mat2.m00() + mat1.m10() * mat2.m10() + mat1.m20() * mat2.m20() );
        set( 0, 1, mat1.m00() * mat2.m01() + mat1.m10() * mat2.m11() + mat1.m20() * mat2.m21() );
        set( 0, 2, mat1.m00() * mat2.m02() + mat1.m10() * mat2.m12() + mat1.m20() * mat2.m22() );
        set( 1, 0, mat1.m01() * mat2.m00() + mat1.m11() * mat2.m10() + mat1.m21() * mat2.m20() );
        set( 1, 1, mat1.m01() * mat2.m01() + mat1.m11() * mat2.m11() + mat1.m21() * mat2.m21() );
        set( 1, 2, mat1.m01() * mat2.m02() + mat1.m11() * mat2.m12() + mat1.m21() * mat2.m22() );
        set( 2, 0, mat1.m02() * mat2.m00() + mat1.m12() * mat2.m10() + mat1.m22() * mat2.m20() );
        set( 2, 1, mat1.m02() * mat2.m01() + mat1.m12() * mat2.m11() + mat1.m22() * mat2.m21() );
        set( 2, 2, mat1.m02() * mat2.m02() + mat1.m12() * mat2.m12() + mat1.m22() * mat2.m22() );
    }
    
    /**
     * Sets this matrix, so that<br> 
     * this.transform(a) = v x a
     * 
     * @param v the vector to compute the crossproduct to
     */
    public final void setCross( Vector3f v )
    {
        set( 0, 1, -v.getZ() );
        set( 0, 2, +v.getY() );
        set( 1, 0, +v.getZ() );
        set( 1, 2, -v.getX() );
        set( 2, 0, -v.getY() );
        set( 2, 1, +v.getX() );
    }
    
    /**
     * Sets this matrix, so that<br> 
     * this.transform(a) = v x a<br>
     * by setting pos = false a negative version gets written
     * @param v the vector to compute the crossproduct to
     * @param notInverted if <code>true</code> positive version or <code>false</code> to get a negative one
     */
    public final void setCross( Vector3f v, boolean notInverted )
    {
    	if ( notInverted )
        {
            setCross( v );
        }
    	else
        {
            set( 0, 1, +v.getZ() );
            set( 0, 2, -v.getY() );
            set( 1, 0, -v.getZ() );
            set( 1, 2, +v.getX() );
            set( 2, 0, +v.getY() );
            set( 2, 1, -v.getX() );
    	}
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
    public void interpolate( Matrix3f m1, Matrix3f m2, float alpha )
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
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix3f asReadOnly()
    {
        return ( new Matrix3f( true, this.dataBegin, this.colSkip, this.values, this.isDirty ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix3f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Returns a hash number based on the data values in this object. Two
     * different Matrix3f objects with identical data values (ie, returns true
     * for equals(Matrix3f) ) will return the same hash number. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash value
     */
    @Override
    public int hashCode()
    {
        return ( VecMathUtils.floatToIntBits( m00() ) ^ VecMathUtils.floatToIntBits( m01() ) ^ VecMathUtils.floatToIntBits( m02() ) ^
                VecMathUtils.floatToIntBits( m10() ) ^ VecMathUtils.floatToIntBits( m11() ) ^ VecMathUtils.floatToIntBits( m12() ) ^ 
                VecMathUtils.floatToIntBits( m20() ) ^ VecMathUtils.floatToIntBits( m21() ) ^ VecMathUtils.floatToIntBits( m22() )
              );
    }
    
    /**
     * Returns true if all of the data members of Matrix3f m1 are equal to the
     * corresponding data members in this Matrix3f.
     * 
     * @param m2 The matrix with which the comparison is made.
     * @return true or false
     */
    public boolean equals( Matrix3f m2 )
    {
        if ( m2 == null )
            return ( false );
        
        for ( int r = 0; r < M; r++ )
        {
            for ( int c = 0; c < N; c++ )
            {
                if ( this.get( r, c ) != m2.get( r, c ) )
                    return ( false );
            }
        }
        
        return ( true );
    }
    
    /**
     * Returns true if the Object o1 is of type Matrix3f and all of the data
     * members of t1 are equal to the corresponding data members in this
     * Matrix3f.
     * 
     * @param o the object with which the comparison is made.
     */
    @Override
    public boolean equals( Object o )
    {
        return ( ( o != null ) && ( ( o instanceof Matrix3f ) && ( equals( (Matrix3f)o ) ) ) );
    }
    
    /**
     * Returns true if the L-infinite distance between this matrix and matrix m1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[i=0,1,2,3 ; j=0,1,2,3 ;
     * abs(this.m(i,j) - m1.m(i,j)]
     * 
     * @param m2 The matrix to be compared to this matrix
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( Matrix3f m2, float epsilon )
    {
        for ( int r = 0; r < M; r++ )
        {
            for ( int c = 0; c < N; c++ )
            {
                if ( Math.abs( this.get( r, c ) - m2.get( r, c ) ) > epsilon )
                    return ( false );
            }
        }
        
        return ( true );
    }
    
    /**
     * Performs SVD on this matrix and gets scale and rotation. Rotation is
     * placed into rot.
     * 
     * @param rot the rotation factor.
     * @return scale factor
     */
    private final float SVD( Matrix3f rot )
    {
        // this is a simple svd.
        // Not complete but fast and reasonable.
        
        /*
         * SVD scale factors(squared) are the 3 roots of
         *  | xI - M*MT | = 0.
         * 
         * This will be expanded as follows
         * 
         * x^3 - A x^2 + B x - C = 0
         * 
         * where A, B, C can be denoted by 3 roots x0, x1, x2.
         * 
         * A = (x0+x1+x2), B = (x0x1+x1x2+x2x0), C = x0x1x2.
         * 
         * An avarage of x0,x1,x2 is needed here. C^(1/3) is a cross product
         * normalization factor. So here, I use A/3. Note that x should be
         * sqrt'ed for the actual factor.
         */
        
        final float s = FastMath.sqrt( ( m00() * m00() +
                                         m10() * m10() +
                                         m20() * m20() +
                                         m01() * m01() +
                                         m11() * m11() +
                                         m21() * m21() +
                                         m02() * m02() +
                                         m12() * m12() +
                                         m22() * m22()
                                       ) / 3.0f );
        
        // zero-div may occur.
        final float t = ( s == 0.0f ? 0.0f : 1.0f / s );
        
        if ( rot != null )
        {
            if ( rot != this )
                rot.set( this );
            rot.mul( t );
        }
        
        return ( s );
    }
    
    private final void setFromQuat( float a, float b, float c, float d )
    {
        final float n = a * a + b * b + c * c + d * d;
        final float s = (n > 0.0f) ? (2.0f / n) : 0.0f;
        
        final float xs = a * s, ys = b * s, zs = c * s;
        final float wx = d * xs, wy = d * ys, wz = d * zs;
        final float xx = a * xs, xy = a * ys, xz = a * zs;
        final float yy = b * ys, yz = b * zs, zz = c * zs;
        
        this.set( 0, 0, 1.0f - (yy + zz ) );
        this.set( 0, 1, xy - wz );
        this.set( 0, 2, xz + wy );
        this.set( 1, 0, xy + wz );
        this.set( 1, 1, 1.0f - ( xx + zz ) );
        this.set( 1, 2, yz - wx );
        this.set( 2, 0, xz - wy );
        this.set( 2, 1, yz + wx );
        this.set( 2, 2, 1.0f - ( xx + yy ) );
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
     * {@inheritDoc}
     */
    @Override
    public Matrix3f clone()
    {
        return ( new Matrix3f( this ) );
    }
    
    /**
     * Constructs and initializes a Matrix3f to all zeros.
     */
    protected Matrix3f( boolean readOnly )
    {
        super( readOnly, 3, 3 );
    }
    
    /**
     * Constructs and initializes a Matrix3f from the specified nine values.
     * 
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     */
    protected Matrix3f( boolean readOnly, float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22 )
    {
        this( readOnly );
        
        this.values[ 0 * getNumCols() + 0 ] = m00;
        this.values[ 0 * getNumCols() + 1 ] = m01;
        this.values[ 0 * getNumCols() + 2 ] = m02;
        this.values[ 1 * getNumCols() + 0 ] = m10;
        this.values[ 1 * getNumCols() + 1 ] = m11;
        this.values[ 1 * getNumCols() + 2 ] = m12;
        this.values[ 2 * getNumCols() + 0 ] = m20;
        this.values[ 2 * getNumCols() + 1 ] = m21;
        this.values[ 2 * getNumCols() + 2 ] = m22;
    }
    
    /**
     * Constructs and initializes a Matrix3f from the specified 9 element array.
     * this.m00 =v[0], this.m01=v[1], etc.
     * 
     * @param values the array of length 9 containing in order
     */
    protected Matrix3f( boolean readOnly, float[] values )
    {
        this( readOnly );
        
        final int size = getNumRows() * getNumCols();
        System.arraycopy( values, 0, this.values, 0, size );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix3f parameter.
     * 
     * @param mat The source matrix.
     */
    protected Matrix3f( boolean readOnly, Matrix3f mat )
    {
        this( readOnly,
              mat.m00(), mat.m01(), mat.m02(),
              mat.m10(), mat.m11(), mat.m12(),
              mat.m20(), mat.m21(), mat.m22()
            );
    }

    /**
     * Hidden constructor for {@link #sharedSubMatrix3f(MatrixMxNf, int, int)}.
     * 
     * @param readOnly
     * @param dataBegin
     * @param colSkip
     * @param values
     * @param isDirty
     * 
     * @see #sharedSubMatrix3f(MatrixMxNf, int, int)
     * @see MatrixMxNf#MatrixMxNf(int, int, int, int, float[])
     */
    protected Matrix3f( boolean readOnly, int dataBegin, int colSkip, float[] values, boolean[] isDirty )
    {
        super( readOnly, dataBegin, colSkip, 3, 3, values, isDirty );
    }
    
    /**
     * Constructs and initializes a Matrix3f to all zeros.
     */
    public Matrix3f()
    {
        this( false );
    }
    
    /**
     * Constructs and initializes a Matrix3f from the specified nine values.
     * 
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     */
    public Matrix3f( float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22 )
    {
        this( false, m00, m01, m02, m10, m11, m12, m20, m21, m22 );
    }
    
    /**
     * Constructs and initializes a Matrix3f from the specified 9 element array.
     * this.m00 =v[0], this.m01=v[1], etc.
     * 
     * @param values the array of length 9 containing in order
     */
    public Matrix3f( float[] values )
    {
        this( false, values );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix3f parameter.
     * 
     * @param mat The source matrix.
     */
    public Matrix3f( Matrix3f mat )
    {
        this( false, mat );
    }
    
    /**
     * Constructs and initializes a Matrix3f to all zeros.
     */
    public static Matrix3f newReadOnly()
    {
        return ( new Matrix3f( true ) );
    }
    
    /**
     * Constructs and initializes a Matrix3f from the specified nine values.
     * 
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     */
    public static Matrix3f newReadOnly( float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22 )
    {
        return ( new Matrix3f( true, m00, m01, m02, m10, m11, m12, m20, m21, m22 ) );
    }
    
    /**
     * Constructs and initializes a Matrix3f from the specified 9 element array.
     * this.m00 =v[0], this.m01=v[1], etc.
     * 
     * @param values the array of length 9 containing in order
     */
    public static Matrix3f newReadOnly( float[] values )
    {
        return ( new Matrix3f( true, values ) );
    }
    
    /**
     * Constructs a new matrix with the same values as the Matrix3f parameter.
     * 
     * @param mat The source matrix.
     */
    public static Matrix3f newReadOnly( Matrix3f mat )
    {
        return ( new Matrix3f( true, mat ) );
    }

    /**
     * Hidden constructor for {@link #sharedSubMatrix3f(MatrixMxNf, int, int)}.
     * 
     * @param dataBegin
     * @param colSkip
     * @param values
     * @param isDirty
     * 
     * @see #sharedSubMatrix3f(MatrixMxNf, int, int)
     * @see MatrixMxNf#MatrixMxNf(int, int, int, int, float[])
     */
    protected Matrix3f( int dataBegin, int colSkip, float[] values, boolean[] isDirty )
    {
        this( false, dataBegin, colSkip, values, isDirty );
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
     * @see #Matrix3f(int, int, float[])
     */
    public static Matrix3f sharedSubMatrix3f( MatrixMxNf mat, int beginRow, int beginCol )
    {
        return ( new Matrix3f( beginRow, beginCol, mat.values, null ) );
    }
    
    /**
     * Allocates an Matrix3f instance from the pool.
     */
    public static Matrix3f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Stores the given Matrix3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Matrix3f o )
    {
        POOL.get().free( o );
    }
}
