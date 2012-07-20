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

import java.io.DataInput;
import java.io.IOException;

import org.openmali.FastMath;
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.TupleNf;
import org.openmali.vecmath2.Vector2f;
import org.openmali.vecmath2.Vector3f;
import org.openmali.vecmath2.Vector4f;
import org.openmali.vecmath2.VectorInterface;
import org.openmali.vecmath2.VectorNf;

/**
 * Utilities for vecmath classes.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public final class VecMathUtils
{
    /**
     * @return an instance from the appropriate pool,
     * if size is 2, 3 or 4. Otherwise this returns a new instance.
     * 
     * @param size
     */
    public static final VectorInterface< ?, ? > getVectorFromPool( int size )
    {
        switch ( size )
        {
            case 2:
                return ( Vector2f.fromPool() );
            case 3:
                return ( Vector3f.fromPool() );
            case 4:
                return ( Vector4f.fromPool() );
        }
        
        return ( new VectorNf( size ) );
    }
    
    /**
     * Puts a vector back to its pool.<br>
     * If the vector's size is not 2, 3, or 4, the vector is discarded.
     * 
     * @param vector
     */
    // cylab: does not compile on NetBeans!
    //public static final void putVectorToPool( VectorInterface< ?, ? > vector )
    @SuppressWarnings( "unchecked" )
    public static final void putVectorToPool( VectorInterface vector )
    {
        switch ( vector.getSize() )
        {
            case 2:
                Vector2f.toPool( (Vector2f)vector );
                return;
            case 3:
                Vector3f.toPool( (Vector3f)vector );
                return;
            case 4:
                Vector4f.toPool( (Vector4f)vector );
                return;
        }
    }
    
    /**
     * @return the squared distance betweeen this point and the other one
     */
    public static final float distanceSquared( TupleNf< ? > p1, TupleNf< ? > p2 )
    {
        float result = 0.0f;
        
        for ( int i = 0; i < p1.getSize(); i++ )
        {
            final float d = p1.getValue( i ) - p2.getValue( i );
            result += d * d;
        }
        
        return ( result );
    }
    
    /**
     * @return the distance betweeen this point and the other one
     */
    public static final float distance( TupleNf< ? > p1, TupleNf< ? > p2 )
    {
        return ( FastMath.sqrt( distanceSquared( p1, p2 ) ) );
    }
    
    /**
     * Returns the sum of the squares of this vector (its length sqaured in
     * n-dimensional space).
     * 
     * @param vec
     * 
     * @return length squared of this vector
     */
    public static final float getNormSquared( TupleNf< ? > vec )
    {
        float s = 0.0f;
        for ( int i = 0; i < vec.getSize(); i++ )
        {
            s += vec.getValue( i ) * vec.getValue( i );
        }
        
        return ( s );
    }
    
    /**
     * Returns the square root of the sum of the squares of this vector (its
     * length in n-dimensional space).
     * 
     * @param vec
     * 
     * @return length of this vector
     */
    public static final float getNorm( TupleNf< ? > vec )
    {
        return ( FastMath.sqrt( getNormSquared( vec ) ) );
    }
    
    /**
     * Normalizes the given vector.
     * 
     * @param v
     */
    public static final void normalize( TupleNf< ? > v )
    {
        final float len = getNorm( v );
        
        // zero-div may happen.
        for ( int i = 0; i < v.getSize(); i++ )
            v.divValue( i, len );
    }
    
    /**
     * Computes the squared length of this vector.
     * 
     * @param vec
     * 
     * @return the squared length of this vector
     */
    public static final float lengthSquared( TupleNf< ? > vec )
    {
        float result = 0.0f;
        
        for ( int i = 0; i < vec.getSize(); i++ )
        {
            result += vec.getValue( i ) * vec.getValue( i );
        }
        
        return ( result );
    }
    
    /**
     * Computes the length of the (x,y,z)-component of this vector.
     * If you want to have also the w-component in the computation, please use #getNorm()
     * 
     * @param vec
     * 
     * @return the length of this vector, only cares about (x,y,z).
     */
    public static final float length( TupleNf< ? > vec )
    {
        return ( FastMath.sqrt( lengthSquared( vec ) ) );
    }
    
    /**
     * Sets this vector to be the vector cross product of vectors v1 and v2.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     */
    public static final void cross( TupleNf< ? > v1, TupleNf< ? > v2, TupleNf< ? > out )
    {
        final int n = out.getSize();
        
        for ( int i = 0; i < n; i++ )
        {
            out.setValue( i, v1.getValue( ( i + 1 ) % n ) * v2.getValue( ( i + 2 ) % n ) - v1.getValue( ( i + 2 ) % n ) * v2.getValue( ( i + 1 ) % n ) );
        }
    }
    
    /**
     * Returns the dot product of vector v1 and vector v2.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * 
     * @return the dot product of v1 and v2
     */
    public static final float dot( TupleNf< ? > v1, TupleNf< ? > v2 )
    {
        if ( v1.getSize() != v2.getSize() )
            throw new IllegalArgumentException( "this.size:" + v1.getSize() + " != v1.size:" + v2.getSize() );
        
        float sum = 0.0f;
        for ( int i = 0; i < v1.getSize(); ++i )
            sum += v1.getValue( i ) * v2.getValue( i );
        
        return ( sum );
    }
    
    /**
     * Returns the (n-space) angle in radians between this vector and the vector
     * parameter; the return value is constrained to the range [0,PI].
     * 
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The angle in radians in the range [0,PI]
     */
    public float angle( TupleNf< ? > v1, TupleNf< ? > v2 )
    {
        return ( FastMath.acos( dot( v1, v2 ) / getNorm( v1 ) / getNorm( v2 ) ) );
    }
    
    /**
     * Returns true if all of the data members of GVector vector1 are equal to
     * the corresponding data members in this GVector.
     * 
     * @param t1 the tuple with which the comparison is made.
     * @param t2 the vector with which the comparison is made.
     * 
     * @return true or false
     */
    public static boolean equals( TupleNf< ? > t1, TupleNf< ? > t2 )
    {
        if ( t1 == t2 )
            return ( true );
        if ( t1 == null )
            return ( false );
        if ( t2 == null )
            return ( false );
        if ( t1.getSize() != t2.getSize() )
            return ( false );
        
        for ( int i = 0; i < t1.getSize(); i++ )
        {
            if ( t1.getValue( i ) != t2.getValue( i ) )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * Returns true if the L-infinite distance between this vector and vector v1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2), . . . ].
     * 
     * @param t1 the tuple with which the comparison is made.
     * @param t2 the vector with which the comparison is made.
     * @param epsilon the threshold value
     */
    public static final boolean epsilonEquals( TupleNf< ? > t1, TupleNf< ? > t2, float epsilon )
    {
        if ( t1 == t2 )
            return ( true );
        if ( t1 == null )
            return ( false );
        if ( t2 == null )
            return ( false );
        if ( t1.getSize() != t2.getSize() )
            return ( false );
        
        for ( int i = 0; i < t1.getSize(); i++ )
        {
            if ( Math.abs( t1.getValue( i ) - t2.getValue( i ) ) > epsilon )
                return ( false );
        }
        
        return ( true );
    }
    
    /**
     * Returns the representation of the specified floating-point
     * value according to the IEEE 754 floating-point "single format"
     * bit layout, after first mapping -0.0 to 0.0. This method is
     * identical to Float.floatToIntBits(float) except that an integer
     * value of 0 is returned for a floating-point value of
     * -0.0f. This is done for the purpose of computing a hash code
     * that satisfies the contract of hashCode() and equals(). The
     * equals() method in each vecmath class does a pair-wise "=="
     * test on each floating-point field in the class (e.g., x, y, and
     * z for a Tuple3f). Since 0.0f&nbsp;==&nbsp;-0.0f returns true,
     * we must also return the same hash code for two objects, one of
     * which has a field with a value of -0.0f and the other of which
     * has a cooresponding field with a value of 0.0f.
     *
     * @param f an input floating-point number
     * @return the integer bits representing that floating-point
     * number, after first mapping -0.0f to 0.0f
     */
    public static final int floatToIntBits( final float f )
    {
        // Check for +0 or -0
        return ( ( f == 0.0f ) ? 0 : Float.floatToIntBits( f ) );
    }
    
    /**
     * Returns the representation of the specified floating-point
     * value according to the IEEE 754 floating-point "double format"
     * bit layout, after first mapping -0.0 to 0.0. This method is
     * identical to Double.doubleToLongBits(double) except that an integer
     * value of 0 is returned for a floating-point value of
     * -0.0d. This is done for the purpose of computing a hash code
     * that satisfies the contract of hashCode() and equals(). The
     * equals() method in each vecmath class does a pair-wise "=="
     * test on each floating-point field in the class (e.g., x, y, and
     * z for a Tuple3d). Since 0.0d&nbsp;==&nbsp;-0.0d returns true,
     * we must also return the same hash code for two objects, one of
     * which has a field with a value of -0.0d and the other of which
     * has a corresponding field with a value of 0.0d.
     *
     * @param f an input floating-point number
     * @return the integer bits representing that floating-point
     * number, after first mapping -0.0f to 0.0f
     */
    public static final long doubleToLongBits( final double d )
    {
        // Check for +0 or -0
        return ( ( d == 0.0d ) ? 0l : Double.doubleToLongBits( d ) );
    }
    
    /**
     * Reads a Tuple3f from the InputStream.
     * 
     * @param <T>
     * 
     * @param in
     * @param t
     * 
     * @return the tuple t back again
     * 
     * @throws IOException
     */
    public static final <T extends Tuple3f> T readTuple3f( DataInput in, T t ) throws IOException
    {
        t.set( in.readFloat(),
               in.readFloat(),
               in.readFloat()
             );
        
        return ( t );
    }
    
    /**
     * Reads a Tuple3f from the InputStream.
     * 
     * @param <T>
     * 
     * @param in
     * @param t
     * 
     * @return the tuple t back again
     * 
     * @throws IOException
     */
    public static final <M extends Matrix3f> M readMatrix3f( DataInput in, M m ) throws IOException
    {
        float m00 = in.readFloat();
        float m10 = in.readFloat();
        float m20 = in.readFloat();
        float m01 = in.readFloat();
        float m11 = in.readFloat();
        float m21 = in.readFloat();
        float m02 = in.readFloat();
        float m12 = in.readFloat();
        float m22 = in.readFloat();
        
        m.set( m00, m01, m02,
               m10, m11, m12,
               m20, m21, m22
             );
        
        return ( m );
    }
    
    /**
     * Do not construct an instance of this class.
     */
    private VecMathUtils()
    {
    }
}
