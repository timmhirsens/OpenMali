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
import org.openmali.vecmath2.pools.AxisAngle3fPool;
import org.openmali.vecmath2.util.SerializationUtils;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * A 4 element axis angle represented by single precision floating point
 * x,y,z,angle components. An axis angle is a rotation of angle (radians) about
 * the vector (x,y,z).
 * 
 * Inspired by Kenji Hiranabe's AxisAngle3f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class AxisAngle3f extends Tuple3f implements Externalizable, Cloneable
{
    private static final long serialVersionUID = 3228663669018590981L;
    
    public static final AxisAngle3f ZERO = AxisAngle3f.newReadOnly( 0f, 0f, 0f, 0f );
    
    //private static final AxisAngle3fPool POOL = new AxisAngle3fPool( 32 );
    private static final ThreadLocal<AxisAngle3fPool> POOL = new ThreadLocal<AxisAngle3fPool>()
    {
        @Override
        protected AxisAngle3fPool initialValue()
        {
            return ( new AxisAngle3fPool( 128 ) );
        }
    };
    
    /**
     * The angle.
     */
    private float angle;
    
    
    /**
     * Sets the value of the angle of this tuple.
     * 
     * @param angle
     */
    public final void setAngle( float angle )
    {
        // This does nothing for RW instances, but crashes for RO instances.
        this.values[ roTrick + 0 ] = this.values[ 0 ];
        
        this.angle = angle;
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * @return the value of the angle of this tuple.
     */
    public final float getAngle()
    {
        return ( angle );
    }
    
    /**
     * Sets the value of this AxisAngle3f to the specified axis and angle.
     * 
     * @param axis the axis
     * @param angle the angle
     */
    public final void set( Tuple3f axis, float angle )
    {
        super.set( axis );
        
        this.setAngle( angle );
    }
    
    /**
     * Sets the value of this axis angle to the specified x,y,z,angle.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param angle the angle
     */
    public final void set( float x, float y, float z, float angle )
    {
        setX( x );
        setY( y );
        setZ( z );
        setAngle( angle );
    }
    
    /**
     * Sets the value of this axis angle from the 4 values specified in the
     * array.
     * 
     * @param values the array of length 4 containing x,y,z,angle in order
     * 
     * @return itself
     */
    @Override
    public final AxisAngle3f set( float[] values )
    {
        // ArrayIndexOutOfBounds is thrown if t.length < 4
        super.set( values );
        this.setAngle( values[ 3 ] );
        
        return ( this );
    }
    
    /**
     * Sets the value of this axis angle to the value of axis angle t1.
     * 
     * @param aa3f the axis angle to be copied
     */
    public final void set( AxisAngle3f aa3f )
    {
        super.set( (Tuple3f)aa3f );
        this.setAngle( aa3f.getAngle() );
    }
    
    /**
     * Gets the value of this axis angle into the array a of length four in
     * x,y,z,angle order.
     * 
     * @param buffer the array of length four
     */
    @Override
    public final void get( float[] buffer )
    {
        // ArrayIndexOutOfBounds is thrown if a.length < 4
        super.get( buffer );
        buffer[ 3 ] = this.getAngle();
    }
    
    /**
     * Sets the value of this axis-angle to the rotational component of the
     * passed matrix.
     * 
     * @param mat the matrix4f
     */
    public final void set( Matrix4f mat )
    {
        setFromMat( mat.m00(), mat.m01(), mat.m02(),
                    mat.m10(), mat.m11(), mat.m12(),
                    mat.m20(), mat.m21(), mat.m22()
                  );
    }
    
    /**
     * Sets the value of this axis-angle to the rotational component of the
     * passed matrix.
     * 
     * @param mat the matrix3f
     */
    public final void set( Matrix3f mat )
    {
        setFromMat( mat.m00(), mat.m01(), mat.m02(),
                    mat.m10(), mat.m11(), mat.m12(),
                    mat.m20(), mat.m21(), mat.m22()
                  );
    }
    
    /**
     * Sets the value of this axis-angle to the rotational equivalent of the
     * passed quaternion.
     * 
     * @param quat the Quat4f
     */
    public final void set( Quaternion4f quat )
    {
        setFromQuat( quat.getA(), quat.getB(), quat.getC(), quat.getD() );
    }
    
    /**
     * Adds v to this tuple's angle value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final AxisAngle3f  addAngle( float v )
    {
        this.setAngle( this.getAngle() + v );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this tuple's angle value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final AxisAngle3f subAngle( float v )
    {
        this.setAngle( this.getAngle() - v );
        
        return ( this );
    }
    
    /**
     * Multiplies this tuple's angle value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final AxisAngle3f mulAngle( float v )
    {
        this.setAngle( this.getAngle() * v );
        
        return ( this );
    }
    
    /**
     * Divides this tuple's angle value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final AxisAngle3f divAngle( float v )
    {
        this.setAngle( this.getAngle() / v );
        
        return ( this );
    }
    
    // helper method
    private final void setFromMat( float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22 )
    {
        // assuming M is normalized.
        
        final float cos = ( m00 + m11 + m22 - 1.0f ) * 0.5f;
        this.setX( m21 - m12 );
        this.setY( m02 - m20 );
        this.setZ( m10 - m01 );
        final float sin = 0.5f * FastMath.sqrt( getX() * getX() + getY() * getY() + getZ() * getZ() );
        this.setAngle( FastMath.atan2( sin, cos ) );
        
        /*
        // no need to normalize
        divX( n );
        divY( n );
        divZ( n );
        */
    }
    
    // helper method
    private final void setFromQuat( float x, float y, float z, float w )
    {
        // This logic can calculate angle without normalization.
        // The direction of (x,y,z) and the sign of rotation cancel
        // each other to calculate a right answer.
        
        final float sin_a2 = FastMath.sqrt( x * x + y * y + z * z ); // |sin a/2|, w = cos a/2 0 <= angle <= because 0 < sin_a2
        
        this.setX( x );
        this.setY( y );
        this.setZ( z );
        this.setAngle( 2.0f * FastMath.atan2( sin_a2, w ) );
    }
    
    /**
     * Returns a string that contains the values of this AxisAngle3f. The form
     * is (x,y,z,angle).
     * 
     * @return the String representation
     */
    @Override
    public String toString()
    {
        return ( "[ " + super.toString() + ", " + angle + " ]" );
    }
    
    /**
     * Returns a hash number based on the data values in this object. Two
     * different AxisAngle3f objects with identical data values (ie, returns
     * true for equals(AxisAngle3f) ) will return the same hash number. Two
     * vectors with different data members may return the same hash value,
     * although this is not likely.
     */
    @Override
    public int hashCode()
    {
        return ( VecMathUtils.floatToIntBits( getX() ) ^
                VecMathUtils.floatToIntBits( getY() ) ^
                VecMathUtils.floatToIntBits( getZ() ) ^
                VecMathUtils.floatToIntBits( getAngle() )
              );
    }
    
    /**
     * Returns true if all of the data members of AxisAngle3f t1 are equal to
     * the corresponding data members in this.
     * 
     * @param aa3f the AxisAngle with which the comparison is made
     */
    public boolean equals( AxisAngle3f aa3f )
    {
        if ( !super.equals( aa3f ) )
            return ( false );
        
        return ( aa3f.angle == this.angle );
    }
    
    /**
     * Returns true if the Object o1 is of type AxisAngle3f and all of the data
     * members of o1 are equal to the corresponding data members in this
     * AxisAngle3f.
     * 
     * @param o the object with which the comparison is made.
     */
    @Override
    public boolean equals(Object o)
    {
        return ( ( o != null ) && ( ( o instanceof AxisAngle3f ) && equals( (AxisAngle3f)o ) ) );
    }
    
    /**
     * Returns true if the L-infinite distance between this axis-angle and
     * axis-angle t1 is less than or equal to the epsilon parameter, otherwise
     * returns false. The L-infinite distance is equal to MAX[abs(x1-x2),
     * abs(y1-y2), abs(z1-z2), abs(angle1-angle2)].
     * 
     * @param aa3f the axis-angle to be compared to this axis-angle
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( AxisAngle3f aa3f, float epsilon )
    {
        if ( !super.epsilonEquals( aa3f, epsilon ) )
            return ( false );
        
        return ( ( Math.abs( aa3f.angle - this.angle ) <= epsilon) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AxisAngle3f clone()
    {
        return ( new AxisAngle3f( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int serialize( int pos, final byte[] buffer )
    {
        pos = super.serialize( pos, buffer );
        
        SerializationUtils.writeToBuffer( angle, pos, buffer );
        pos += 1;
        
        return ( pos );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int deserialize( int pos, final byte[] buffer )
    {
        pos = super.deserialize( pos, buffer );
        
        angle = SerializationUtils.readFloatFromBuffer( pos, buffer );
        pos += 1;
        
        return ( pos );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int getSerializationBufferSize()
    {
        return ( super.getSerializationBufferSize() + 4 );
    }
    
    
    /**
     * Constructs and initializes an AxisAngle3f from the specified x, y, z, and
     * angle.
     * 
     * @param readOnly
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param angle the angle.
     */
    protected AxisAngle3f( boolean readOnly, float x, float y, float z, float angle )
    {
        super( readOnly, x, y, z );
        
        this.angle = angle;
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the components contained
     * in the array.
     * 
     * @param readOnly
     * @param values the array of length 4 containing x,y,z,angle in order
     */
    protected AxisAngle3f( boolean readOnly, float[] values )
    {
        this( readOnly, values[ 0 ], values[ 1 ], values[ 2 ], values[ 3 ] );
    }
    
    /**
     * Constructs and initializes a AxisAngle3f from the specified AxisAngle3f.
     * 
     * @param readOnly
     * @param aa3f the AxisAngle3f containing the initialization x y z angle data
     */
    protected AxisAngle3f( boolean readOnly, AxisAngle3f aa3f )
    {
        this( readOnly, aa3f.getX(), aa3f.getY(), aa3f.getZ(), aa3f.getAngle() );
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the specified axis and
     * angle.
     * 
     * @param readOnly
     * @param axis the axis
     * @param angle the angle
     */
    protected AxisAngle3f( boolean readOnly, Tuple3f axis, float angle )
    {
        this( readOnly, axis.getX(), axis.getY(), axis.getZ(), angle );
    }
    
    /**
     * Constructs and initializes a AxisAngle3f to (0,0,1,0).
     * 
     * @param readOnly
     */
    protected AxisAngle3f( boolean readOnly )
    {
        this( readOnly, 0f, 0f, 0f, 0f );
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the specified x, y, z, and
     * angle.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param angle the angle.
     */
    public AxisAngle3f( float x, float y, float z, float angle )
    {
        this( false, x, y, z, angle );
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the components contained
     * in the array.
     * 
     * @param values the array of length 4 containing x,y,z,angle in order
     */
    public AxisAngle3f( float[] values )
    {
        this( false, values );
    }
    
    /**
     * Constructs and initializes a AxisAngle3f from the specified AxisAngle3f.
     * 
     * @param aa3f the AxisAngle3f containing the initialization x y z angle data
     */
    public AxisAngle3f( AxisAngle3f aa3f )
    {
        this( false, aa3f );
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the specified axis and
     * angle.
     * 
     * @param axis the axis
     * @param angle the angle
     */
    public AxisAngle3f( Tuple3f axis, float angle )
    {
        this( false, axis, angle );
    }
    
    /**
     * Constructs and initializes a AxisAngle3f to (0,0,1,0).
     */
    public AxisAngle3f()
    {
        this( false, 0f, 0f, 0f, 0f );
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the specified x, y, z, and
     * angle.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param angle the angle.
     */
    public static AxisAngle3f newReadOnly( float x, float y, float z, float angle )
    {
        return ( new AxisAngle3f( true, x, y, z, angle ) );
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the components contained
     * in the array.
     * 
     * @param values the array of length 4 containing x,y,z,angle in order
     */
    public static AxisAngle3f newReadOnly( float[] values )
    {
        return ( new AxisAngle3f( true, values ) );
    }
    
    /**
     * Constructs and initializes a AxisAngle3f from the specified AxisAngle3f.
     * 
     * @param aa3f the AxisAngle3f containing the initialization x y z angle data
     */
    public static AxisAngle3f newReadOnly( AxisAngle3f aa3f )
    {
        return ( new AxisAngle3f( true, aa3f ) );
    }
    
    /**
     * Constructs and initializes an AxisAngle3f from the specified axis and
     * angle.
     * 
     * @param axis the axis
     * @param angle the angle
     */
    public static AxisAngle3f newReadOnly( Tuple3f axis, float angle )
    {
        return ( new AxisAngle3f( true, axis, angle ) );
    }
    
    /**
     * Constructs and initializes a AxisAngle3f to (0,0,1,0).
     */
    public static AxisAngle3f newReadOnly()
    {
        return ( new AxisAngle3f( true, 0f, 0f, 0f, 0f ) );
    }
    
    /**
     * Allocates an AxisAngle3f instance from the pool.
     */
    public static AxisAngle3f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an AxisAngle3f instance from the pool.
     */
    public static AxisAngle3f fromPool( float x, float y, float z, float angle )
    {
        return ( POOL.get().alloc( x, y, z, angle ) );
    }
    
    /**
     * Allocates an AxisAngle3f instance from the pool.
     */
    public static AxisAngle3f fromPool( AxisAngle3f aa )
    {
        return ( fromPool( aa.getX(), aa.getY(), aa.getZ(), aa.getAngle() ) );
    }
    
    /**
     * Stores the given AxisAngle3f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( AxisAngle3f o )
    {
        POOL.get().free( o );
    }
}
