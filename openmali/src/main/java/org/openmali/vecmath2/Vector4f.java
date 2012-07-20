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
import org.openmali.vecmath2.pools.Vector4fPool;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * A simple 4-dimensional float-Vector implementation.
 * 
 * Inspired by Kenji Hiranabe's Vector4f implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Arne Mueller
 */
public class Vector4f extends VectorNf implements Externalizable, VectorInterface< VectorNf, VectorNf >
{
    private static final long serialVersionUID = 378659999358250332L;
    
    public static final Vector4f ZERO = Vector4f.newReadOnly( 0f, 0f, 0f, 0f );
    
    //private static final Vector4fPool POOL = new Vector4fPool( 32 );
    private static final ThreadLocal<Vector4fPool> POOL = new ThreadLocal<Vector4fPool>()
    {
        @Override
        protected Vector4fPool initialValue()
        {
            return ( new Vector4fPool( 128 ) );
        }
    };
    
    private Vector4f readOnlyInstance = null;
    
    
    /**
     * Sets the value of the x-element of this vector.
     * 
     * @param x
     * 
     * @return itself
     */
    public final Vector4f setX( float x )
    {
        setValue( 0, x );
        
        return ( this );
    }
    
    /**
     * Sets the value of the y-element of this vector.
     * 
     * @param y
     * 
     * @return itself
     */
    public final Vector4f setY( float y )
    {
        setValue( 1, y );
        
        return ( this );
    }
    
    /**
     * Sets the value of the z-element of this vector.
     * 
     * @param z
     * 
     * @return itself
     */
    public final Vector4f setZ( float z )
    {
        setValue( 2, z );
        
        return ( this );
    }
    
    /**
     * Sets the value of the w-element of this vector.
     * 
     * @param w
     * 
     * @return itself
     */
    public final Vector4f setW( float w )
    {
        setValue( 3, w );
        
        return ( this );
    }
    
    /**
     * @return the value of the x-element of this tuple.
     */
    public final float getX()
    {
        return ( getValue( 0 ) );
    }
    
    /**
     * @return the value of the y-element of this tuple.
     */
    public final float getY()
    {
        return ( getValue( 1 ) );
    }
    
    /**
     * @return the value of the z-element of this tuple.
     */
    public final float getZ()
    {
        return ( getValue( 2 ) );
    }
    
    /**
     * @return the value of the w-element of this tuple.
     */
    public final float getW()
    {
        return ( getValue( 3 ) );
    }
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     * 
     * @return itself
     */
    public final Vector4f set( float x, float y, float z, float w )
    {
        setX( x );
        setY( y );
        setZ( z );
        setW( w );
        
        return ( this );
    }
    
    /**
     * Sets this vector's xyz components to the ones of the given vector
     * and w to w.
     * 
     * @param v
     * @param w
     * 
     * @return itself
     */
    public final Vector4f set( Vector3f v, float w ) 
    {
        setX( v.getX() );
        setY( v.getY() );
        setZ( v.getZ() );
        setW( w );
        
        return ( this );
    }
    
    /**
     * Adds v to this vector's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f addX( float v )
    {
        this.addValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Adds v to this vector's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f addY( float v )
    {
        this.addValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Adds v to this vector's z value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f addZ( float v )
    {
        this.addValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Adds v to this vector's w value.
     * 
     * @param w
     * 
     * @return itself
     */
    public final Vector4f addW( float w )
    {
        this.addValue( 3, w );
        
        return ( this );
    }
    
    /**
     * Adds the given parameters to this tuple's values.
     * 
     * @param x
     * @param y
     * @param z
     * @param w
     * 
     * @return itself
     */
    public final Vector4f add( float x, float y, float z, float w )
    {
        this.addX( x );
        this.addY( y );
        this.addZ( z );
        this.addW( w );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this vector's x value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f subX( float v )
    {
        this.subValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this vector's y value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f subY( float v )
    {
        this.subValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this vector's z value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f subZ( float v )
    {
        this.subValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Subtracts v from this vector's w value.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f subW( float v )
    {
        this.subValue( 3, v );
        
        return ( this );
    }
    
    /**
     * Subtracts the given parameters from this vector's values.
     * 
     * @param x
     * @param y
     * @param z
     * @param w
     * 
     * @return itself
     */
    public final Vector4f sub( float x, float y , float z, float w )
    {
        this.subX( x );
        this.subY( y );
        this.subZ( z );
        this.subW( w );
        
        return ( this );
    }    
    
    /**
     * Multiplies this vector's x value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f mulX( float v )
    {
        this.mulValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this vector's y value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f mulY( float v )
    {
        this.mulValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this vector's z value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f mulZ( float v )
    {
        this.mulValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this vector's w value with v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f mulW( float v )
    {
        this.mulValue( 3, v );
        
        return ( this );
    }
    
    /**
     * Multiplies this vector's values with vx, vy, vz.
     * 
     * @param vx
     * @param vy
     * @param vz
     * @param vw
     * 
     * @return itself
     */
    public final Vector4f mul( float vx, float vy , float vz, float vw )
    {
        this.mulValue( 0, vx );
        this.mulValue( 1, vy );
        this.mulValue( 2, vz );
        this.mulValue( 3, vw );
        
        return ( this );
    }
    
    /**
     * Sets the value of this vector to the scalar multiplication of vector t1.
     * 
     * @param factorX
     * @param factorY
     * @param factorZ
     * @param factorW
     * 
     * @return itself
     */
    public final Vector4f scale( float factorX, float factorY , float factorZ , float factorW )
    {
        mul( factorX, factorY, factorZ, factorW );
        
        return ( this );
    }
    
    /**
     * Divides this vector's x value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f divX( float v )
    {
        this.divValue( 0, v );
        
        return ( this );
    }
    
    /**
     * Divides this vector's y value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f divY( float v )
    {
        this.divValue( 1, v );
        
        return ( this );
    }
    
    /**
     * Divides this vector's z value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f divZ( float v )
    {
        this.divValue( 2, v );
        
        return ( this );
    }
    
    /**
     * Divides this vector's w value by v.
     * 
     * @param v
     * 
     * @return itself
     */
    public final Vector4f divW( float v )
    {
        this.divValue( 3, v );
        
        return ( this );
    }
    
    /**
     * Divides this vector's values by vx, vy, vz.
     * 
     * @param vx
     * @param vy
     * 
     * @return itself
     */
    public final Vector4f div( float vx, float vy , float vz , float vw )
    {
        this.divValue( 0, vx );
        this.divValue( 1, vy );
        this.divValue( 2, vz );
        this.divValue( 3, vw );
        
        return ( this );
    }
    
    /**
     * Sets all values of this vector to the specified ones.
     * 
     * @param tuple the tuple to be copied
     * 
     * @return itself
     */
    public final Vector4f set( Tuple3f tuple )
    {
        set( tuple.getValue( 0 ), tuple.getValue( 1 ), tuple.getValue( 2 ), 0f );
        
        return ( this );
    }
    
    /**
     * Writes all values of this Tuple to the specified buffer Tuple.
     * 
     * @param buffer the buffer Tuple to write the values to
     */
    public final void get( Tuple3f buffer )
    {
        buffer.set( getValue( 0 ), getValue( 1 ), getValue( 2 ) );
    }
    
    /**
     * Sets the value of this tuple to the vector difference of tuple t1 and t2
     * (this = t1 - t2).
     * 
     * @param tuple1 the first tuple
     * @param tuple2 the second tuple
     * 
     * @return itself
     */
    public final Vector4f sub( Tuple3f tuple1, Tuple3f tuple2 )
    {
        for ( int i = 0; i < tuple2.getSize(); i++ )
        {
            this.setValue( i, tuple1.getValue( i ) - tuple2.getValue( i ) );
        }
        
        this.setValue( 3, 0f );
        
        return ( this );
    }
    
    /**
     * Sets the value of this tuple to the vector difference of itself and tuple
     * t1 (this = this - t1).
     * 
     * @param tuple2 the other tuple
     * 
     * @return itself
     */
    public final Vector4f sub( Tuple3f tuple2 )
    {
        for ( int i = 0; i <tuple2. getSize(); i++ )
        {
            this.setValue( i, -tuple2.getValue( i ) );
        }
        
        return ( this );
    }
    
    /**
     * Sets this vector to be the vector cross product of vectors v1 and v2.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * 
     * @return itself
     */
    public final Vector4f cross( Vector4f v1, Vector4f v2 )
    {
        // store in tmp once for aliasing-safty
        // i.e. safe when a.cross(a, b)
        set( v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
             v1.getZ() * v2.getW() - v1.getW() * v2.getZ(),
             v1.getW() * v2.getX() - v1.getX() * v2.getW(),
             v1.getX() * v2.getY() - v1.getY() * v2.getX()
           );
        
        return ( this );
    }
    
    
    /**
     * Computes the squared length of the (x,y,z)-component of this vector.
     * If you want to have also the w-component in the computation, please use {@link #getNormSquared()}
     * @return the squared length of this vector, only cares about (x,y,z).
     * @see #getNormSquared()
     */
    public final float lengthSquared3()
    {
        float result = 0.0f;
        
        for ( int i = 0; i < 3; i++ )
        {
            result += getValue( i ) * getValue( i );
        }
        
        return ( result );
    }
    
    /**
     * computes the length of the (x,y,z)-component of this vector.
     * If you want to have also the w-component in the computation, please use {@link #getNorm()}
     * @return the length of this vector, only cares about (x,y,z).
     * @see #getNorm()
     */
    public final float length3()
    {
        return ( FastMath.sqrt( lengthSquared3() ) );
    }
    
    /**
     * Computes the L-1 (Manhattan) distance between this point and point p1.
     * The L-1 distance is equal to abs(x1 - x2) + abs(y1 - y2) + abs(z1 - z2) + abs(w1 - w2).
     * <br> wouldn't it be wiser to compute, L1 only over (x,y,z), because w is mostly only a helper variable?
     * 
     * @param v2 the other point
     * @return L-1 distance
     */
    public final float distanceL1( VectorNf v2 )
    {
        float result = 0.0f;
        
        for ( int i = 0; i < getSize(); i++ )
        {
            result += Math.abs( getValue( i ) -v2.getValue( i ) );
        }
        
        return ( result );
    }
    
    /**
     * returns the normal of a linear hyperplane (that is a 4D hyperplane passing through the origin)
     * from three points. i.e. Ax1 + Bx2 + Cx3 + Dx4 = 0
     * deals with degeneracies, if the points are colinear though, and so no unique plane can be found
     * then the method fills the passback with NaN.
     * zero garbage.
     * slow method, needs optimizing
     * 
     * @param p1
     * @param p2
     * @param p3
     * @param normal
     */
    public static TupleNf< ? > getLinearHyperPlaneNormal( TupleNf< ? > p1, TupleNf< ? > p2, TupleNf< ? > p3, TupleNf< ? > normal ) 
    {
        /*
         * Ax1 + Bx2 + Cx3 + Dx4 = 0
         * there are 4 unkowns but three points
         * but the unkowns can be scales of each other, so we can remove one of the unkowns
         * let D = -1 and work out everyhting around it
         */
        
        /*
         * Ax1 + Bx2 + Cx3  = x4
         * now there are three unkowns and three equations, so we can solve using normal linear solving techniques
         * remebering that we assumed D = -1
         */
        
        /*
         * note:
         * if the component of D should have been zero, we have a problem
         * in which case we should try assuming a different component is -1
         */
        Matrix3f A = Matrix3f.fromPool();
        Matrix3f Atmp = Matrix3f.fromPool();
        Vector3f B = Vector3f.fromPool();
        Vector3f tmp = Vector3f.fromPool();
        
        A.set( 0, 0, p1.getValue( 0 ) ); // put first three components (A,B & C) of p1 into equation matrix
        A.set( 0, 1, p1.getValue( 1 ) );
        A.set( 0, 2, p1.getValue( 2 ) );
        
        A.set( 1, 0, p2.getValue( 0 ) ); // put first three components of p2 into equation matrix
        A.set( 1, 1, p2.getValue( 1 ) );
        A.set( 1, 2, p2.getValue( 2 ) );
        
        A.set( 2, 0, p3.getValue( 0 ) ); // put first three components of p1 into equation matrix
        A.set( 2, 1, p3.getValue( 1 ) );
        A.set( 2, 2, p3.getValue( 2 ) );
        
        B.setValue( 0, p1.getValue( 3 ) );    // put last component of all points into the rhs of solution equation (the D component)
        B.setValue( 1, p2.getValue( 3 ) );
        B.setValue( 2, p3.getValue( 3 ) );
        
        int minus1Assumption = 3; // meaning D is first assumed to have minus one component
        for ( int i = 0; i < 4; i++ ) // each iteration we try to solve the system
        {
            tmp.set( B ); // copy RHS to tmp, we might need it later
            Atmp.set( A ); // copy A to tmp, we might need it later

            if ( A.solve( B, normal ) ) // this destroyes the old A and B!
            { 
                // we sucessfully solved the equation
                
                //System.out.println( "A = " + A );
                //System.out.println( "B = " + B );
                //System.out.println( "normal = " + normal );
                
                /*
                 * However, raher than running with the solution, if the
                 * assumed component is very small in compasison with the
                 * others, we have some numerical issues.
                 * So we will only accept the solution, if the other components
                 * have not been scaled to too large a degree.
                 */
                /*
                if ( ( normal.max() < 1.1f ) && ( normal.min() > -1.1f ) )
                {
                    System.out.println( "accepted" );
                    break; // quit loop and use the solution
                }// Didn't solve accuracy problems!
                // otherwise we try a different component equal to one
                */
            }
            //if we are here we failed to solve the system of equations
            //so lets try assuming a different component has a non-negative value
            minus1Assumption--;
            
            
            if ( minus1Assumption == -1 )
                break; // we have not solved the equation and have run out of possible options
            
            A.set( Atmp );
            A.getColumn( minus1Assumption, B ); // copy the next column of A to B
            A.setColumn( minus1Assumption, tmp ); // put the previous B into equation matrix
            // thus if D was on RHS then after one failed operation
            // A will contain A,B,D and the RHS will contain C
        }
        
        /*
         * The found solution needs to be rearranged becuase of how we solved it.
         * If we solved it
         *     1st iteration: A contains ABC and rhs contains D, minusAssumption =3
         *     2nd iteration: A contains ABD and rhs contains C, minusAssumption =2
         *     3rd iteration: A contains ACD and rhs contains B, minusAssumption =1
         *     4th iteration: A contains BCD and rhs contains A, minusAssumption =0
         *     5th iteration: no solution found, minusAssumption =-1
         */
        
        if ( minus1Assumption == -1 )
        {
            normal.fill( Float.NaN );
        }
        else
        {
            //shift solved components up to their correct place
            
            for ( int i = 3; i > minus1Assumption; i-- )
            {
                normal.setValue( i, normal.getValue( i - 1 ) ); // shift components over
            }
            // now put our assumption in the correct location
            normal.setValue( minus1Assumption, -1 );
            normal.scale( -1 ); // this scaling should be unnecessary, unless we prefer 1 over -1
            VecMathUtils.normalize( normal );
        }
        
        Matrix3f.toPool( A );
        Matrix3f.toPool( Atmp );
        Vector3f.toPool( B );
        Vector3f.toPool( tmp );
        
        return ( normal );
    }
    
    /**
     * returns the normal of a linear hyperplane (that is a 4D hyperplane passing through the origin)
     * from three points. i.e. Ax1 + Bx2 + Cx3 + Dx4 = 0
     * deals with degeneracies, if the points are colinear though, and so no unique plane can be found
     * then the method fills the passback with NaN.
     * zero garbage.
     * slow method, needs optimizing
     * 
     * @param p1
     * @param p2
     * @param p3
     */
    public final void getLinearHyperPlaneNormal( TupleNf< ? > p1, TupleNf< ? > p2, TupleNf< ? > p3 ) 
    {
        getLinearHyperPlaneNormal( p1, p2, p3, this );
    }
    
    /**
     * Computes the L-infinite distance between this point and point p1. The
     * L-infinite distance is equal to MAX[abs(x1 - x2), abs(y1 - y2), abs(z1 - z2), abs(w1 - w2)].
     * <br> wouldn't it be wiser to compute, Linf only over (x,y,z), because w is mostly only a helper variable?
     * @param v2 the other point
     * @return L-infinite distance
     */
    public final float distanceLinf( VectorNf v2 )
    {
        float result = 0.0f;
        
        for ( int i = 0; i < getSize(); i++ )
        {
            result += Math.max( Math.abs( getValue( i ) - v2.getValue( i ) ), result );
        }
        
        return ( result );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector4f asReadOnly()
    {
        return ( new Vector4f( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector4f getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    
    /**
     * Returns true if the Object t1 is of type Vector4f and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Vector4f.
     * 
     * @param o  the Object with which the comparison is made
     * @return  true or false
     */ 
    @Override
    public boolean equals( Object o )
    {
        return ( ( o != null ) && ( ( o instanceof Vector4f ) && equals( (Vector4f)o ) ) );
    }
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     */
    @Override
    public Vector4f clone()
    {
        return ( new Vector4f( this ) );
    }
    
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param readOnly
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     */
    protected Vector4f( boolean readOnly, float x, float y, float z, float w )
    {
        super( readOnly, 4 );
        
        values[ 0 ] = x;
        values[ 1 ] = y;
        values[ 2 ] = z;
        values[ 3 ] = w;
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param readOnly
     * @param values the values array (must be at least size 4)
     * @param isDirty
     * @param copy
     */
    protected Vector4f( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, copy );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param readOnly
     * @param vec the Vector4f to copy the values from
     */
    protected Vector4f( boolean readOnly, Vector4f vec )
    {
        super( readOnly, vec );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param readOnly
     */
    protected Vector4f( boolean readOnly )
    {
        this( readOnly, 0f, 0f, 0f, 0f );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     */
    public Vector4f( float x, float y, float z, float w )
    {
        this( false, x, y, z, w );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param values the values array (must be at least size 4)
     */
    public Vector4f( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param vec the Vector4f to copy the values from
     */
    public Vector4f( Vector4f vec )
    {
        this( false, vec );
    }
    
    /**
     * Creates a new Vector4f instance.
     */
    public Vector4f()
    {
        this( false );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param x the x element to use
     * @param y the y element to use
     * @param z the z element to use
     * @param w the w element to use
     */
    public static Vector4f newReadOnly( float x, float y, float z, float w )
    {
        return ( new Vector4f( true, x, y, z, w ) );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param values the values array (must be at least size 4)
     */
    public static Vector4f newReadOnly( float[] values )
    {
        return ( new Vector4f( true, values, null, true ) );
    }
    
    /**
     * Creates a new Vector4f instance.
     * 
     * @param vec the Vector4f to copy the values from
     */
    public static Vector4f newReadOnly( Vector4f vec )
    {
        return ( new Vector4f( true, vec ) );
    }
    
    /**
     * Creates a new Vector4f instance.
     */
    public static Vector4f newReadOnly()
    {
        return ( new Vector4f( true ) );
    }
    
    /**
     * Allocates an Vector4f instance from the pool.
     */
    public static Vector4f fromPool()
    {
        return ( POOL.get().alloc() );
    }
    
    /**
     * Allocates an Vector4f instance from the pool.
     */
    public static Vector4f fromPool( float x, float y, float z, float w )
    {
        return ( POOL.get().alloc( x, y, z, w ) );
    }
    
    /**
     * Allocates an Vector4f instance from the pool.
     */
    public static Vector4f fromPool( Vector4f tuple )
    {
        return ( fromPool( tuple.getX(), tuple.getY(), tuple.getZ(), tuple.getW() ) );
    }
    
    /**
     * Stores the given Vector4f instance in the pool.
     * 
     * @param o
     */
    public static void toPool( Vector4f o )
    {
        POOL.get().free( o );
    }
}
