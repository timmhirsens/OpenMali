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
import org.openmali.vecmath2.pools.Quaternion4fPool;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * A 4 element quaternion represented by single precision floating point a,b,c,d
 * coordinates.
 * 
 * Inspired by Kenji Hiranabe's Quat4f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Arne Mueller
 */
public class Quaternion4f extends TupleNf< Quaternion4f > implements Externalizable
{
    private static final long serialVersionUID = -8111082760556105489L;
    
    public static final Quaternion4f ZERO = Quaternion4f.newReadOnly( 0f, 0f, 0f, 0f );
    public static final Quaternion4f IDENTITY = Quaternion4f.newReadOnly( 0f, 0f, 0f, 1f );
    
    public static final Quaternion4f ROT_PLUS_90_DEG_BY_X_AXIS;
    public static final Quaternion4f ROT_MINUS_90_DEG_BY_X_AXIS;
    public static final Quaternion4f ROT_PLUS_90_DEG_BY_Y_AXIS;
    public static final Quaternion4f ROT_MINUS_90_DEG_BY_Y_AXIS;
    public static final Quaternion4f ROT_PLUS_90_DEG_BY_Z_AXIS;
    public static final Quaternion4f ROT_MINUS_90_DEG_BY_Z_AXIS;
    
    static
    {
        Quaternion4f tmp = new Quaternion4f();
        tmp.setFromAxisAngle( 1f, 0f, 0f, +FastMath.PI_HALF );
        ROT_PLUS_90_DEG_BY_X_AXIS = new Quaternion4f( true, tmp );
        tmp.setFromAxisAngle( 1f, 0f, 0f, -FastMath.PI_HALF );
        ROT_MINUS_90_DEG_BY_X_AXIS = new Quaternion4f( true, tmp );
        tmp.setFromAxisAngle( 0f, 1f, 0f, +FastMath.PI_HALF );
        ROT_PLUS_90_DEG_BY_Y_AXIS = new Quaternion4f( true, tmp );
        tmp.setFromAxisAngle( 0f, 1f, 0f, -FastMath.PI_HALF );
        ROT_MINUS_90_DEG_BY_Y_AXIS = new Quaternion4f( true, tmp );
        tmp.setFromAxisAngle( 0f, 0f, 1f, +FastMath.PI_HALF );
        ROT_PLUS_90_DEG_BY_Z_AXIS = new Quaternion4f( true, tmp );
        tmp.setFromAxisAngle( 0f, 0f, 1f, -FastMath.PI_HALF );
        ROT_MINUS_90_DEG_BY_Z_AXIS = new Quaternion4f( true, tmp );
    }
    
    public static final Quaternion4f Z_UP_TO_Y_UP = ROT_MINUS_90_DEG_BY_X_AXIS;
    
    //private static final Quaternion4fPool POOL = new Quaternion4fPool( 128 );
    private static final ThreadLocal<Quaternion4fPool> POOL = new ThreadLocal<Quaternion4fPool>()
    {
        @Override
        protected Quaternion4fPool initialValue()
        {
            return ( new Quaternion4fPool( 128 ) );
        }
    };
    
    private Quaternion4f readOnlyInstance = null;
    
    /**
     * Sets the value of the A-element of this Quaternion.
     * 
     * @param a
     * 
     * @return itself
     */
    public final Quaternion4f setA( float a )
    {
        setValue( 0, a );
        
        return ( this );
    }
    
    /**
     * Sets the value of the A-element of this Quaternion.
     * 
     * @param a
     * 
     * @return itself
     */
    public final Quaternion4f a( float a )
    {
        setValue( 0, a );
        
        return ( this );
    }
    
    /**
     * Sets the value of the B-element of this Quaternion.
     * 
     * @param b
     * 
     * @return itself
     */
    public final Quaternion4f setB( float b )
    {
        setValue( 1, b );
        
        return ( this );
    }
    
    /**
     * Sets the value of the B-element of this Quaternion.
     * 
     * @param b
     * 
     * @return itself
     */
    public final Quaternion4f b( float b )
    {
        setValue( 1, b );
        
        return ( this );
    }
    
    /**
     * Sets the value of the C-element of this Quaternion.
     * 
     * @param c
     * 
     * @return itself
     */
    public final Quaternion4f setC( float c )
    {
        setValue( 2, c );
        
        return ( this );
    }
    
    /**
     * Sets the value of the C-element of this Quaternion.
     * 
     * @param c
     * 
     * @return itself
     */
    public final Quaternion4f c( float c )
    {
        setValue( 2, c );
        
        return ( this );
    }
    
	/**
     * Sets the value of the D-element of this Quaternion.
     * 
     * @param d
     * 
     * @return itself
     */
    public final Quaternion4f setD( float d )
    {
        setValue( 3, d );
        
        return ( this );
    }
    
    /**
     * Sets the value of the D-element of this Quaternion.
     * 
     * @param d
     * 
     * @return itself
     */
    public final Quaternion4f d( float d )
    {
        setValue( 3, d );
        
        return ( this );
    }
    
    /**
     * @return the value of the A-element of this Quaternion.
     */
    public final float getA()
    {
        return ( getValue( 0 ) );
    }
    
    /**
     * @return the value of the A-element of this Quaternion.
     */
    public final float a()
    {
        return ( getValue( 0 ) );
    }
    
    /**
     * @return the value of the B-element of this Quaternion.
     */
    public final float getB()
    {
        return ( getValue( 1 ) );
    }
    
    /**
     * @return the value of the B-element of this Quaternion.
     */
    public final float b()
    {
        return ( getValue( 1 ) );
    }
    
    /**
     * @return the value of the C-element of this Quaternion.
     */
    public final float getC()
    {
        return ( getValue( 2 ) );
    }
    
    /**
     * @return the value of the C-element of this Quaternion.
     */
    public final float c()
    {
        return ( getValue( 2 ) );
    }
    
    /**
     * @return the value of the D-element of this Quaternion.
     */
    public final float getD()
    {
        return ( getValue( 3 ) );
    }
    
    /**
     * @return the value of the D-element of this Quaternion.
     */
    public final float d()
    {
        return ( getValue( 3 ) );
    }
    
    
    /**
     * Computes the D-component from the A, B and C ones.
     * 
     * @return ifself
     */
    public final Quaternion4f computeD()
    {
        float t = 1.0f - ( getA() * getA() ) - ( getB() * getB() ) - ( getC() * getC() );
        
        if ( t < 0.0f )
            setD( 0.0f );
        else
            setD( -FastMath.sqrt( t ) );
        
        return ( this );
    }
    
    
    /**
     * Sets all values of this Quaternion4f to the specified ones.
     * 
     * @param a the a element to use
     * @param b the b element to use
     * @param c the c element to use
     * @param d the d element to use
     * 
     * @return itself
     */
    public final Quaternion4f set( float a, float b, float c, float d )
    {
        setA( a );
        setB( b );
        setC( c );
        setD( d );
        
        return ( this );
    }
    
    public final Vector3f getVectorComponent( Vector3f v )
    {
        for ( int i = 0; i < 3; i++ )
        {
            v.setValue( i, this.getValue( i ) );
        }
        
        return ( v );
    }
    
    /**
     * Sets the value of this quaternion to the identity quaternion.
     * 
     * @return itself
     */
    public final Quaternion4f setIdentity()
    {
        set( 0f, 0f, 0f, 1f );
        
        return ( this );
    }
    
    /**
     * Sets the value of this quaternion to the rotational component of the
     * passed matrix.
     * 
     * @param mat the matrix4f
     * 
     * @return itself
     */
    public final Quaternion4f set( Matrix4f mat )
    {
        setFromMat( mat.m00(), mat.m01(), mat.m02(),
                    mat.m10(), mat.m11(), mat.m12(),
                    mat.m20(), mat.m21(), mat.m22()
                  );
        
        return ( this );
    }
    
    /**
     * Sets the value of this quaternion to the rotational component of the
     * passed matrix.
     * 
     * @param mat the matrix3f
     * 
     * @return itself
     */
    public final Quaternion4f set( Matrix3f mat )
    {
        setFromMat( mat.m00(), mat.m01(), mat.m02(),
                    mat.m10(), mat.m11(), mat.m12(),
                    mat.m20(), mat.m21(), mat.m22()
                  );
        
        return ( this );
    }
    
    /**
     * Sets the value of this quaternion to the equivalent rotation of teh
     * AxisAngle argument.
     * 
     * @param aa3f the axis-angle
     * 
     * @return itself
     */
    public final Quaternion4f setFromAxisAngle( float aaX, float aaY, float aaZ, float aaAngle )
    {
        setValue( 0, aaX );
        setValue( 1, aaY );
        setValue( 2, aaZ );
        
        final float n = FastMath.sqrt( getA() * getA() + getB() * getB() + getC() * getC() );
        
        // zero-div may occur.
        final float s = FastMath.sin( 0.5f * aaAngle ) / n;
        mulValue( 0, s );
        mulValue( 1, s );
        mulValue( 2, s );
        setValue( 3, FastMath.cos( 0.5f * aaAngle ) );
        
        return ( this );
    }
    
    /**
     * Sets the value of this quaternion to the equivalent rotation of teh
     * AxisAngle argument.
     * 
     * @param aa3f the axis-angle
     * 
     * @return itself
     */
    public final Quaternion4f set( AxisAngle3f aa3f )
    {
        return ( setFromAxisAngle( aa3f.getX(), aa3f.getY(), aa3f.getZ(), aa3f.getAngle() ) );
    }
    
    public final Quaternion4f set( Vector3f v, float w ) 
    {
        for ( int i = 0; i < 3; i++ )
            this.setValue( i, v.getValue( i ) );
        
        this.setValue( 3, w );
        
        return ( this );
    }
    
    public final Quaternion4f set( Vector4f v ) 
    {
        for ( int i = 0; i < 4; i++ )
            this.setValue( i, v.getValue( i ) );
        
        return ( this );
    }
    
    // helper
    public final float getNorm()
    {
        return ( getA() * getA() + getB() * getB() + getC() * getC() + getD() * getD() );
    }    
    
    /**
     * Sets the value of this quaternion to quaternion inverse of quaternion quat.
     * 
     * @param quat the quaternion to be inverted
     * 
     * @return itself
     */
    public final Quaternion4f invert( Quaternion4f quat )
    {
        final float n = quat.getNorm();
        
        if ( n != 0f )
        {
            float n_ = 1f / n;
            
            // zero-div may occur.
            set( -quat.getA() * n_, -quat.getB() * n_, -quat.getC() * n_, +quat.getD() * n_ );
        }
        
        return ( this );
    }
    
    /**
     * Sets the value of this quaternion to the quaternion inverse of itself.
     * 
     * @return itself
     */
    public final Quaternion4f invert()
    {
        return ( invert( this ) );
    }
    
    /**
     * Sets the value of this quaternion to the conjugate of quaternion quat.
     * 
     * @param quat the source Quaternion
     * 
     * @return itself
     */
    public final Quaternion4f conjugate( Quaternion4f quat )
    {
        setA( -quat.getA() );
        setB( -quat.getB() );
        setC( -quat.getC() );
        setD(  quat.getD() );
        
        return ( this );
    }
    
    /**
     * Negates the value of each of this quaternion's a,b,c coordinates in
     * place.
     * 
     * @return itself
     */
    public final Quaternion4f conjugate()
    {
        setA( -getA() );
        setB( -getB() );
        setC( -getC() );
        
        return ( this );
    }
    
    /**
     * Sets the value of this quaternion to the normalized value of quaternion quat.
     * 
     * @param quat the quaternion to be normalized.
     * 
     * @return itself
     */
    public final Quaternion4f normalize( Quaternion4f quat )
    {
        final float n = FastMath.sqrt( quat.getNorm() );
        
        if ( n > 0f )
        {
            float n_ = 1f / n;
            
            mul( n_ );
        }
        
        return ( this );
    }
    
    /**
     * Normalizes the value of this quaternion in place.
     * 
     * @return itself
     */
    public final Quaternion4f normalize()
    {
        return ( normalize( this ) );
    }
    
    /**
     * Sets the value of this quaternion to the quaternion product of
     * quaternions q1 and q2 (this = q1 * q2). Note that this is safe for
     * aliasing (e.g. this can be q1 or q2).
     * 
     * @param quat1 the first quaternion
     * @param quat2 the second quaternion
     * 
     * @return itself
     */
    public final Quaternion4f mul( Quaternion4f quat1, Quaternion4f quat2 )
    {
        // store on stack for aliasing-safty
        set( quat1.getA() * quat2.getD() + quat1.getD() * quat2.getA() + quat1.getB() * quat2.getC() - quat1.getC() * quat2.getB(),
             quat1.getB() * quat2.getD() + quat1.getD() * quat2.getB() + quat1.getC() * quat2.getA() - quat1.getA() * quat2.getC(),
             quat1.getC() * quat2.getD() + quat1.getD() * quat2.getC() + quat1.getA() * quat2.getB() - quat1.getB() * quat2.getA(),
             quat1.getD() * quat2.getD() - quat1.getA() * quat2.getA() - quat1.getB() * quat2.getB() - quat1.getC() * quat2.getC()
           );
        
        return ( this );
    }
    
    /**
     * Sets the value of this quaternion to the quaternion product of itself and
     * q1 (this = this * q1).
     * 
     * @param quat2 the other quaternion
     * 
     * @return itself
     */
    public final Quaternion4f mul( Quaternion4f quat2 )
    {
        // store on stack for aliasing-safty
        set( getA() * quat2.getD() + getD() * quat2.getA() + getB() * quat2.getC() - getC() * quat2.getB(),
             getB() * quat2.getD() + getD() * quat2.getB() + getC() * quat2.getA() - getA() * quat2.getC(),
             getC() * quat2.getD() + getD() * quat2.getC() + getA() * quat2.getB() - getB() * quat2.getA(),
             getD() * quat2.getD() - getA() * quat2.getA() - getB() * quat2.getB() - getC() * quat2.getC()
           );
        
        return ( this );
    }
    
    /**
     * 
     * Multiplies quaternion q1 by the inverse of quaternion q2 and places the
     * value into this quaternion. The value of both argument quaternions is
     * preservered (this = q1 * q2^-1).
     * 
     * @param quat1 the left quaternion
     * @param quat2 the right quaternion
     * 
     * @return itself
     */
    public final Quaternion4f mulInverse( Quaternion4f quat1, Quaternion4f quat2 )
    {
        float n = getNorm();
        // zero-div may occur.
        n = (n == 0.0f ? n : 1.0f / n);
        // store on stack once for aliasing-safty
        set( ( ( quat1.getA() * quat2.getD() - quat1.getD() * quat2.getA() - quat1.getB() * quat2.getC() + quat1.getC() * quat2.getB() ) * n ),
             ( ( quat1.getB() * quat2.getD() - quat1.getD() * quat2.getB() - quat1.getC() * quat2.getA() + quat1.getA() * quat2.getC() ) * n ),
             ( ( quat1.getC() * quat2.getD() - quat1.getD() * quat2.getC() - quat1.getA() * quat2.getB() + quat1.getB() * quat2.getA() ) * n ),
             ( ( quat1.getD() * quat2.getD() + quat1.getA() * quat2.getA() + quat1.getB() * quat2.getB() + quat1.getC() * quat2.getC() ) * n )
           );
        
        return ( this );
    }
    
    /**
     * Multiplies this quaternion by the inverse of quaternion q1 and places the
     * value into this quaternion. The value of the argument quaternion is
     * preserved (this = this * q^-1).
     * 
     * @param quat2 the other quaternion
     * 
     * @return itself
     */
    public final Quaternion4f mulInverse( Quaternion4f quat2 )
    {
        float n = getNorm();
        // zero-div may occur.
        n = (n == 0.0f ? n : 1.0f / n);
        // store on stack once for aliasing-safty
        set( ( ( getA() * quat2.getD() - getD() * quat2.getA() - getB() * quat2.getC() + getC() * quat2.getB() ) * n ),
             ( ( getB() * quat2.getD() - getD() * quat2.getB() - getC() * quat2.getA() + getA() * quat2.getC() ) * n ),
             ( ( getC() * quat2.getD() - getD() * quat2.getC() - getA() * quat2.getB() + getB() * quat2.getA() ) * n ),
             ( ( getD() * quat2.getD() + getA() * quat2.getA() + getB() * quat2.getB() + getC() * quat2.getC() ) * n )
           );
        
        return ( this );
    }
    
    public final Quaternion4f mul( Tuple3f t, Quaternion4f out )
    {
        out.set( ( getD() * t.getX() ) + ( getB() * t.getZ() ) - ( getC() * t.getY() ),
                 ( getD() * t.getY() ) + ( getC() * t.getX() ) - ( getA() * t.getZ() ),
                 ( getD() * t.getZ() ) + ( getA() * t.getY() ) - ( getB() * t.getX() ),
                 -( getA() * t.getX() ) - ( getB() * t.getY() ) - ( getC() * t.getZ() )
               );
        
        return ( out );
    }
    
    public final Quaternion4f mul( Tuple3f t )
    {
        return ( mul( t, this ) );
    }
    
    public final <T extends Tuple3f> T transform( Tuple3f vector, T result )
    {
        Quaternion4f inv = Quaternion4f.fromPool();
        
        inv.set( -getA(), -getB(), -getC(), getD() );
        inv.normalize();
        
        Quaternion4f tmp = Quaternion4f.fromPool();
        
        mul( vector, tmp );
        tmp.mul( inv );
        
        result.set( tmp.getA(), tmp.getB(), tmp.getC() );
        
        Quaternion4f.toPool( tmp );
        Quaternion4f.toPool( inv );
        
        return ( result );
    }
    
    public final <T extends Tuple3f> T transform( T vector )
    {
        return ( transform( vector, vector ) );
    }
    
    /**
     * Computes scale * ( (v, 0f) * q ). 
     * 
     * @param scale
     * @param v
     * @param q
     * 
     * @return itself
     */
    public final Quaternion4f setMulScale( float scale, Vector3f v, Quaternion4f q )
    {
    	this.set( v, 0f );
    	this.mul( q );
    	this.scale( scale );
        
        return ( this );
    }
    
    /**
     * Changes in quaternion values wrt time for a given angular velocity
     * dq/dt = .5 * (0, w) * q
     * 
     * @param angularVelocity
     * @param passback
     * 
     * @return itself
     */
    public final Quaternion4f setDQDT( Vector3f angularVelocity, Quaternion4f passback )
    {
    	setMulScale( 0.5f, angularVelocity, passback );
    	
    	return ( this );
    }
    
    /**
     * Performs a great circle interpolation between this quaternion and the
     * quaternion parameter and places the result into this quaternion.
     * 
     * @param quat2 the other quaternion
     * @param alpha the alpha interpolation parameter
     */
    @Override
    public void interpolate( Quaternion4f quat2, float alpha )
    {
        // From Hoggar.
        normalize();
        final float n2 = FastMath.sqrt( quat2.getNorm() );
        // zero-div may occur.
        final float a2 = quat2.getA() / n2;
        final float b2 = quat2.getB() / n2;
        final float c2 = quat2.getC() / n2;
        final float d2 = quat2.getD() / n2;
        
        // t is cosine (dot product)
        float t = getA() * a2 + getB() * b2 + getC() * c2 + getD() * d2;
        
        // same quaternion (avoid domain error)
        if ( 1.0f <= Math.abs( t ) )
            return;
        
        // t is now theta
        t = FastMath.acos( t );
        
        final float sin_t = FastMath.sin( t );
        
        // same quaternion (avoid zero-div)
        if (sin_t == 0.0f)
            return;
        
        final float s = FastMath.sin( ( 1.0f - alpha ) * t ) / sin_t;
        t = FastMath.sin( alpha * t ) / sin_t;
        
        // set values
        setA( s * getA() + t * a2 );
        setB( s * getB() + t * b2 );
        setC( s * getC() + t * c2 );
        setD( s * getD() + t * d2 );
    }
    
    /**
     * A rotational interpolation
     * returns a quaternion that is between the passed in arguments
     * the amount is the bias toward q2. 0<=amount<=1 
     * amount = 0 would return q1. amount =1 would return q2
     * uses an implementation of SLERP, uses trig so not too fast
     * 
     * @param q1 unit quaternion 1
     * @param q2 unit quaternion 2
     * @param amount interpolation parameter, between 0 and 1
     * @param result the passback of the result
     * @return omega, the TOTAL angle between q1 and q2 on the unit hypershpere,
     * which the algorithm then scales by amount (note not the rotational angle, you must divide by 2 to get this
     */    
    public static float interpolateSLERP( Quaternion4f q1, Quaternion4f q2, float amount, Quaternion4f result )
    {
        //total angle we are rotating by:-
        
        float dotProduct = VecMathUtils.dot( q1, q2 );
        //if the dot product is 1 or -1 , then they are the same quaterion
        //degenerate case when the two quaternuions are the same
        //just return one of them
        if ( FastMath.epsilonEquals( dotProduct, 1, 0.00001f ) || FastMath.epsilonEquals( dotProduct, -1, 0.00001f ) )
        {
            result.set( q1 );
            return ( 0f );
        }
        
        //special cases
        //no interpolation, return the start and the angle is zero
        if ( amount == 0f )
        {
            result.set( q1 );
            return ( 0f );
        }
        
        //q & -q represent the same thing, so lets make sure that we only deal with accute angle by inverting
        //if necissary
        if ( dotProduct < 0f )
        {
            dotProduct = -dotProduct;
            q1.scale( -1 );
        }
        
        //full interpolation, return the end, and work out the angle
        if ( amount == 1f )
        {
            result.set( q2 );
            return ( FastMath.acos( dotProduct ) );
        }
        
        final float omega = (float)FastMath.acos( dotProduct );
        final float sinOmega = (float)FastMath.sin( omega );
        final float sinTOmega= (float)FastMath.sin( omega * amount );
        final float sin1minusTOmega= (float)FastMath.sin( omega * ( 1f - amount ) );
        
        //LH component of formula
        Vector4f tmp = Vector4f.fromPool();
        tmp.set( q1 );
        tmp.scale( sin1minusTOmega / sinOmega );
        
        //RH component of formula
        result.set( q2 );
        result.scale( sinTOmega / sinOmega );
        
        //add compionents together
        result.addValue( 0, tmp.getX() );
        result.addValue( 1, tmp.getY() );
        result.addValue( 2, tmp.getZ() );
        result.addValue( 3, tmp.getW() );
        
        Vector4f.toPool( tmp );
        
        assert ( omega < Math.PI && omega > -Math.PI );
        
        return ( omega );
    }
    
    /**
     * A rotational interpolation
     * returns a quaternion that is between the passed in arguments
     * the amount is the bias toward q2. 0<=amount<=1 
     * amount = 0 would return q1. amount =1 would return q2
     * uses an implementation of SLERP, uses trig so not too fast
     * 
     * @param q1 unit quaternion 1
     * @param q2 unit quaternion 2
     * @param amount interpolation parameter, between 0 and 1
     * @return omega, the TOTAL angle between q1 and q2 on the unit hypershpere,
     * which the algorithm then scales by amount (note not the rotational angle, you must divide by 2 to get this
     */    
    public float interpolateSLERP( Quaternion4f q1, Quaternion4f q2, float amount )
    {
        return ( interpolateSLERP( q1, q2, amount, this ) );
    }
    
    /**
     * A generalization of SLERP. Instead of lambda along the shortest, constant velocity path, the movement
     * to the target rotation is broken into 3 independant rotational components at the start_b rotation.
     * The returned rotation is an interpolation of these three components, controlled by three parameters.
     * If all the parameters are of the same value, then this method is equivelent to SLERP. This is needed by JOODE
     * to allow different angular ERP parameters for different dimentions for angular constraints in certain joints
     * input axis should be normalized
     * 
     * @param start
     * @param target
     * @param rotationAxis1
     * @param rotationAxis2
     * @param rotationAxis3
     * @param weights the weight to apply to each rotation component in order (technically not really a vector)
     * @param anglePassback this can be null, if not null, the angle each component
     * @param result
     */
    public static Quaternion4f weightedSLERP( Quaternion4f start, Quaternion4f target,
                                              Vector3f rotationAxis1,
                                              Vector3f rotationAxis2,
                                              Vector3f rotationAxis3,
                                              Vector3f weights, Vector3f anglePassback, Quaternion4f result )
    {
        Quaternion4f start_copy = Quaternion4f.fromPool();
        start_copy.set( start );
        
        float dotProduct = VecMathUtils.dot( start_copy, target );
        /*
         * if the dot product is 1 or -1 , then they are the same quaterion
         * degenerate case when the two quaternuions are the same
         * just return one of them
         */
        
        if ( FastMath.epsilonEquals( dotProduct, 1, 0.00001f ) || FastMath.epsilonEquals( dotProduct, -1, 0.00001f ) )
        {
            //set all elements to zero for the passback, as the target was the start_b
            if ( anglePassback != null )
                anglePassback.setZero();
            
            result.set( target );
            return ( result );
        }
        
        //q & -q represent the same thing, so lets make sure that we only deal with accute angle by inverting
        //if necissary
        if ( dotProduct < 0f )
        {
            dotProduct = -dotProduct;
            start_copy.scale( -1 );
        }
        
        assert ( FastMath.epsilonEquals( rotationAxis1.lengthSquared(), 1f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( rotationAxis2.lengthSquared(), 1f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( rotationAxis3.lengthSquared(), 1f, 0.0001f ) );
        
        //to break the target rotation into its components, we need to find its coordinates w.r.t
        //the rotational axis
        //the traget is a 3D point on a 4D UNIT hypershphere (as is the start_b rotation)
        //the three rotational axis provide three components to movements originating from the start_b location.
        //in SLERP a line is drawn directly from the start_b to the target. We need to first break the same movement
        //into the rotational components.
        //to determine the magnitude of a rotational component, a great circle is drawn from the start_b point, in the direction
        //of the component. Next an orthogonal(at the surface) great circle is drawn, that passes through the target point.
        //great cirles can be represented by planes that pass through the orogin, thus planes without constant translations,
        //which means just a normal is need to define the plane/great circle.
        
        //so first we define the three planes which describe the valid components of each rotational component
        Quaternion4f n1 = Quaternion4f.fromPool();
        Quaternion4f n2 = Quaternion4f.fromPool();
        Quaternion4f n3 = Quaternion4f.fromPool();
        
        start_copy.getDirection( rotationAxis1, n1 );
        start_copy.getDirection( rotationAxis2, n2 );
        start_copy.getDirection( rotationAxis3, n3 );
        
        assert ( FastMath.epsilonEquals( n1.getNorm(), 1f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( n2.getNorm(), 1f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( n3.getNorm(), 1f, 0.0001f ) );
        
        //to find the actual values we need to work out a plane that passes through the
        //target point, and that is right angles to the plane of that component
        //the job is made easier by the fact that the right angled plane will pass through
        //a point at the extremes of the unit circle to an orthogonal plane of the component plane,
        //as all the planes we have calculated are orthogonal, and we have their normal, the point at which each other passes
        //through IS the normalized vector. Thus to find the plane for component 1, we use the target point, and the two other
        //target plane normals and the origin 0,0,0,0
        
        Quaternion4f c1 = Quaternion4f.fromPool();
        Quaternion4f c2 = Quaternion4f.fromPool();
        Quaternion4f c3 = Quaternion4f.fromPool();
        
        Vector4f.getLinearHyperPlaneNormal( n2, n3, target, c1 );
        Vector4f.getLinearHyperPlaneNormal( n1, n3, target, c2 );
        Vector4f.getLinearHyperPlaneNormal( n1, n2, target, c3 );
        
        assert ( FastMath.epsilonEquals( c1.getNorm(), 1f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( c2.getNorm(), 1f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( c3.getNorm(), 1f, 0.0001f ) );
        
        //at this point we have the base normals, and the target transformed normals all in their perpendicularity
        //SLERP can be applid to the normals to work out new perpendicular planes representing
        //first however, we must remeber that the result of finding the normals will
        //not have the normals pointing in the same diriction
        
        Quaternion4f r1 = Quaternion4f.fromPool();
        Quaternion4f r2 = Quaternion4f.fromPool();
        Quaternion4f r3 = Quaternion4f.fromPool();
        
        //so we can just apply normal SLERP along the diferent directions
        //rememebering that in one case, the caller of the function would like the returned angles
        if ( anglePassback != null )
        {
            anglePassback.setX( interpolateSLERP( n1, c1, weights.getX(), r1) / 2f ); // divide by two to give real angle rotational changes, not quaternion angles
            anglePassback.setY( interpolateSLERP( n2, c2, weights.getY(), r2) / 2f );
            anglePassback.setZ( interpolateSLERP( n3, c3, weights.getZ(), r3) / 2f );
            
            Quaternion4f.toPool( c3 );
            Quaternion4f.toPool( c2 );
            Quaternion4f.toPool( c1 );
            
            Quaternion4f.toPool( n3 );
            Quaternion4f.toPool( n2 );
            Quaternion4f.toPool( n1 );
            
            /*
             * the angles bassed back are always posative
             * because they get computed via a cos
             * 
             * the user of the function will need to know what sign the angles are
             * 
             * to do this we differentiate the quaternion wrt each of the rotational component
             * we then check that whether the direction of change for the rotation
             * axis is in the same general direction of the different betwen the start_b and end
             * or in the opposite direcion by doting the rate of change by the linear change
             */
            
            // linear change
            Quaternion4f tmp = Quaternion4f.fromPool();
            tmp.set( target );
            tmp.sub( start_copy );
            
            /*
            for ( int i = 0; i < 3;i++ )
            {
                tmp.m[ i ] *= weights.m[ i ];
            }
            */
            
            // differentiate
            Quaternion4f tx = Quaternion4f.fromPool();
            Quaternion4f ty = Quaternion4f.fromPool();
            Quaternion4f tz = Quaternion4f.fromPool();

            tx.setDQDT( rotationAxis1, start_copy );
            ty.setDQDT( rotationAxis2, start_copy );
            tz.setDQDT( rotationAxis3, start_copy );
            
            //check whether in same direciton or opposite
            if ( VecMathUtils.dot( tx, tmp ) < 0f ) anglePassback.setX( -anglePassback.getX() );
            if ( VecMathUtils.dot( ty, tmp ) < 0f ) anglePassback.setY( -anglePassback.getY() );
            if ( VecMathUtils.dot( tz, tmp ) < 0f ) anglePassback.setZ( -anglePassback.getZ() );
        }
        else
        {
            interpolateSLERP( n1, c1, weights.getX(), r1 );
            interpolateSLERP( n2, c2, weights.getY(), r2 );
            interpolateSLERP( n3, c3, weights.getZ(), r3 );
            
            Quaternion4f.toPool( c3 );
            Quaternion4f.toPool( c2 );
            Quaternion4f.toPool( c1 );
            
            Quaternion4f.toPool( n3 );
            Quaternion4f.toPool( n2 );
            Quaternion4f.toPool( n1 );
        }
        
        /*
         * so the Rs contain the new plane normals
         * where these planes intersect is our new quaternion
         * there are three hyperpanes, so the solution will be an infinite line
         * 
         * taking the hyperplane of the normals actually also will find a points on it
         */
        
        Vector4f.getLinearHyperPlaneNormal( r1,r2, r3, result );
        
        //the result should lie on all the planes
        assert ( FastMath.epsilonEquals( VecMathUtils.dot( result, r1 ), 0f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( VecMathUtils.dot( result, r2 ), 0f, 0.0001f ) );
        assert ( FastMath.epsilonEquals( VecMathUtils.dot( result, r3 ), 0f, 0.0001f ) );
        
        Quaternion4f.toPool( r3 );
        Quaternion4f.toPool( r2 );
        Quaternion4f.toPool( r1 );
        
        result.normalize();
        
        return ( result );
    }
    
    /**
     * if this quaternion is rotated by an infetesimal amount by the provided axis, then this quaterion values
     * will move in the direction calulculated by this method. This is the tangital vector to the quaternion unit circle
     * in the direction of the rotation axis
     * (surely this would imply that it is equaly to dq_dt but this does not seem to be the case)
     */
    public final void getDirection( Vector3f rotationAxis, TupleNf< ? > passbackNormal )
    {
        Vector3f tmpV = Vector3f.fromPool();
        Vector3f tmpVa = Vector3f.fromPool();
        //extract the vector component of this quaternion
        getVectorComponent( tmpV );
        
        passbackNormal.setValue( 3, -tmpV.dot( rotationAxis ) );
        
        tmpVa.cross( tmpV, rotationAxis );
        for ( int i = 0; i < 3; i++ )
        {
            passbackNormal.setValue( i, tmpVa.getValue( i ) + this.getD() * rotationAxis.getValue( i ) );
        }
        
        VecMathUtils.normalize( passbackNormal );
        
        Vector3f.toPool( tmpV );
        Vector3f.toPool( tmpVa );
    }
    
    /**
     * gets the angles around each provided axis required to move q1
     * to q2
     * @param passback each angle is put in an element of the 3D vector
     * in the order of the rotation axis
     */
    public static void getAngles( Quaternion4f q1, Quaternion4f q2,
                                  Vector3f rotationAxis1,
                                  Vector3f rotationAxis2,
                                  Vector3f rotationAxis3,
                                  Vector3f passback )
    {
        Vector4f d1 = Vector4f.fromPool();
        Vector4f d2 = Vector4f.fromPool();
        Vector4f d3 = Vector4f.fromPool();
        
        Quaternion4f target = Quaternion4f.fromPool();
        target.set( q2 );
        
        if ( VecMathUtils.dot( q1, q2 ) < 0f )
        {
            target.scale( -1 );
        }
        
        //next first work out which direction each rotation axis takes us from the
        //start quaternion
        q1.getDirection( rotationAxis1, d1 );
        q1.getDirection( rotationAxis2, d2 );
        q1.getDirection( rotationAxis3, d3 );
        
        //the dirction plus the start quat defines a great circle
        //round the quaternion hypersphere
        //we need to determine where the end quaternion is defined
        //in terms of these great circles
        //each component will be the addition of two vectors:-
        //the first vector is a distance along the start vector(q1)
        //the second will be a distance along a direction vector
        
        final float distCommon = VecMathUtils.dot( target, q1 );
        Vector3f common = Vector3f.fromPool();
        
        //common.set(q1);
        q1.getVectorComponent( common );
        common.scale( distCommon );
        
        Vector3f targetVec = Vector3f.fromPool();
        target.getVectorComponent( targetVec );
        
        float dist1 = VecMathUtils.dot( targetVec, d1 );
        float dist2 = VecMathUtils.dot( targetVec, d2 );
        float dist3 = VecMathUtils.dot( targetVec, d3 );
        
        Vector3f.toPool( targetVec );
        
        d1.scale( dist1 );
        d2.scale( dist2 );
        d3.scale( dist3 );
        
        d1.add( common.getX(), common.getY(), common.getZ(), 0f );
        d2.add( common.getX(), common.getY(), common.getZ(), 0f );
        d3.add( common.getX(), common.getY(), common.getZ(), 0f );
        
        d1.normalize();
        d2.normalize();
        d3.normalize();
        
        float angle1 = VecMathUtils.epsilonEquals( q1, d1, 0.00001f ) ? 0f : FastMath.acos( VecMathUtils.dot( q1, d1 ) );
        float angle2 = VecMathUtils.epsilonEquals( q1, d2, 0.00001f ) ? 0f : FastMath.acos( VecMathUtils.dot( q1, d2 ) );
        float angle3 = VecMathUtils.epsilonEquals( q1, d3, 0.00001f ) ? 0f : FastMath.acos( VecMathUtils.dot( q1, d3 ) );
        
        /*
         * degeneracies occur when d1 == q1 i.e. no rotation in that axis
         * so catch these degeneracies by setting NaN to 0
         * also invert angles when neccisary by noticing whether a
         * negative projection onto the direction vector was uses
         */
        if ( Float.isNaN( angle1 ) )
        {
            angle1 = 0f;
        }
        else if ( dist1 < 0f )
        {
            angle1 = -angle1;
        }
        
        if ( Float.isNaN( angle2 ) )
        {
            angle2 = 0f;
        }
        else if ( dist2 < 0f )
        {
            angle2 = -angle2;
        }
        
        if ( Float.isNaN( angle3 ) )
        {
            angle3 = 0f;
        }
        else if ( dist3 < 0f ) 
        {
            angle3 = -angle3;
        }
        
        // times by two to get real angle as opposed to quaternion angles
        //passback.set(angle1*2, angle2*2,angle3*2);
        passback.set( angle1 / 2, angle2 / 2, angle3 / 2 );
        //passback.set( angle1, angle2, angle3 );
        
        Quaternion4f.toPool( target );
        Vector3f.toPool( common );
        Vector4f.toPool( d1 );
        Vector4f.toPool( d2 );
        Vector4f.toPool( d3 );
    }
    
    // helper method
    private final void setFromMat( float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22 )
    {
        // From Ken Shoemake
        // (ftp://ftp.cis.upenn.edu/pub/graphics/shoemake)
        
        float s;
        final float tr = m00 + m11 + m22;
        if ( tr >= 0.0f )
        {
            s = FastMath.sqrt( tr + 1.0f );
            setD( s * 0.5f );
            s = 0.5f / s;
            setA( ( m21 - m12 ) * s );
            setB( ( m02 - m20 ) * s );
            setC( ( m10 - m01 ) * s );
        }
        else
        {
            float max = Math.max( Math.max( m00, m11 ), m22 );
            if ( max == m00 )
            {
                s = FastMath.sqrt( m00 - (m11 + m22) + 1.0f );
                setA( s * 0.5f );
                s = 0.5f / s;
                setB( ( m01 + m10 ) * s );
                setC( ( m20 + m02 ) * s );
                setD( ( m21 - m12 ) * s );
            }
            else if ( max == m11 )
            {
                s = FastMath.sqrt( m11 - (m22 + m00) + 1.0f );
                setB( s * 0.5f );
                s = 0.5f / s;
                setC( ( m12 + m21 ) * s );
                setA( ( m01 + m10 ) * s );
                setD( ( m02 - m20 ) * s );
            }
            else
            {
                s = FastMath.sqrt( m22 - (m00 + m11) + 1.0f );
                setC( s * 0.5f );
                s = 0.5f / s;
                setA( ( m20 + m02 ) * s );
                setB( ( m12 + m21 ) * s );
                setD( ( m10 - m01 ) * s );
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Quaternion4f asReadOnly()
    {
        return ( new Quaternion4f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Quaternion4f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    
    /**
     * Returns true if the Object t1 is of type Tuple3f and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Tuple3f.
     * 
     * @param o  the Object with which the comparison is made
     * @return  true or false
     */ 
    @Override
    public final boolean equals( Object o )
    {
        return ( ( o != null ) && ( ( o instanceof Quaternion4f ) && equals( (Quaternion4f)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Quaternion4f clone()
    {
        return ( new Quaternion4f( this ) );
    }
    
    
    /**
     * Constructs and initializes a Quaternion4f from the specified xyzw coordinates.
     * 
     * @param readOnly
     * @param a the a coordinate
     * @param b the b coordinate
     * @param c the c coordinate
     * @param d the d scalar component
     */
    protected Quaternion4f( boolean readOnly, float a, float b, float c, float d )
    {
        super( readOnly, 4 );
        
        this.values[ 0 ] = a;
        this.values[ 1 ] = b;
        this.values[ 2 ] = c;
        this.values[ 3 ] = d;
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the array of length 4.
     * 
     * @param readOnly
     * @param values the array of length 4 containing abcd in order
     * @param isDirty
     * @param copy
     */
    protected Quaternion4f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, 4, copy );
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the array of length 4.
     * 
     * @param readOnly
     * @param quat the array of length 4 containing abcd in order
     */
    protected Quaternion4f( boolean readOnly, Quaternion4f quat )
    {
        super( readOnly, quat );
    }
    
    /**
     * Constructs and initializes a Quaternion4f to (0,0,0,0).
     * 
     * @param readOnly
     */
    protected Quaternion4f( boolean readOnly )
    {
        this( readOnly, 0f, 0f, 0f, 0f );
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the specified xyzw coordinates.
     * 
     * @param a the a coordinate
     * @param b the b coordinate
     * @param c the c coordinate
     * @param d the d scalar component
     */
    public Quaternion4f( float a, float b, float c, float d )
    {
        this( false, a, b, c ,d );
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the array of length 4.
     * 
     * @param values the array of length 4 containing abcd in order
     */
    public Quaternion4f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the array of length 4.
     * 
     * @param quat the array of length 4 containing abcd in order
     */
    public Quaternion4f( Quaternion4f quat )
    {
        this( false, quat );
    }
    
    /**
     * Constructs and initializes a Quaternion4f to (0,0,0,0).
     */
    public Quaternion4f()
    {
        this( false );
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the specified xyzw coordinates.
     * 
     * @param a the a coordinate
     * @param b the b coordinate
     * @param c the c coordinate
     * @param d the d scalar component
     */
    public static Quaternion4f newReadOnly( float a, float b, float c, float d )
    {
        return ( new Quaternion4f( true, a, b, c ,d ) );
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the array of length 4.
     * 
     * @param values the array of length 4 containing abcd in order
     */
    public static Quaternion4f newReadOnly( float[] values )
    {
        return ( new Quaternion4f( true, values, null, true ) );
    }
    
    /**
     * Constructs and initializes a Quaternion4f from the array of length 4.
     * 
     * @param quat the array of length 4 containing abcd in order
     */
    public static Quaternion4f newReadOnly( Quaternion4f quat )
    {
        return ( new Quaternion4f( true, quat ) );
    }
    
    /**
     * Constructs and initializes a Quaternion4f to (0,0,0,0).
     */
    public static Quaternion4f newReadOnly()
    {
        return ( new Quaternion4f( true ) );
    }
    
    /**
     * Allocates an Quaternion4f instance from the pool.
     */
    public static Quaternion4f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Quaternion4f instance from the pool.
     */
    public static Quaternion4f fromPool( float a, float b, float c, float d )
    {
        return ( POOL.get().alloc( a, b, c, d ) );
    }
    
    /**
     * Allocates an Quaternion4f instance from the pool.
     */
    public static Quaternion4f fromPool( Quaternion4f quat )
    {
        return ( fromPool( quat.getA(), quat.getB(), quat.getC(), quat.getD() ) );
    }
    
    /**
     * Stores the given Quaternion4f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Quaternion4f o )
    {
        POOL.get().free( o );
    }
}
