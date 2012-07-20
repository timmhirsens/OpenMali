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
package org.openmali.test;

public class VMTest {
	// private static final Random RND = new Random( System.nanoTime() );
	// private static final float EPSILON = 0.00001f;
	// private static final boolean USE_READ_ONLY = false;
	//
	// @SuppressWarnings("unused")
	// private static class TestMatrices4
	// {
	// private static final float[] newRandomValues()
	// {
	// final float[] values = new float[ 16 ];
	//
	// for ( int i = 0; i < 4; i++ )
	// {
	// for ( int j = 0; j < 4; j++ )
	// {
	// values[ i * 4 + j ] = RND.nextFloat() * 1000f;
	// }
	// }
	//
	// return ( values );
	// }
	//
	// private static final org.openmali.vecmath.Matrix4f newRandomMatrix_vm1(
	// float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// return ( new org.openmali.vecmath.Matrix4f( values ) );
	// }
	//
	// private static final org.openmali.vecmath2.Matrix4f newRandomMatrix_vm2(
	// boolean readOnly, float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// if ( readOnly )
	// return ( org.openmali.vecmath2.Matrix4f.newReadOnly( values ) );
	//
	// return ( new org.openmali.vecmath2.Matrix4f( values ) );
	// }
	//
	// private final float[] valuesA = newRandomValues();
	// private final float[] valuesB = newRandomValues();
	//
	// public org.openmali.vecmath.Matrix4f mat1a = newRandomMatrix_vm1( valuesA
	// );
	// public org.openmali.vecmath.Matrix4f mat1b = newRandomMatrix_vm1( valuesB
	// );
	// public org.openmali.vecmath.Matrix4f mat1c = newRandomMatrix_vm1( null );
	//
	// public org.openmali.vecmath2.Matrix4f mat2a = newRandomMatrix_vm2(
	// USE_READ_ONLY, valuesA );
	// public org.openmali.vecmath2.Matrix4f mat2b = newRandomMatrix_vm2(
	// USE_READ_ONLY, valuesB );
	// public org.openmali.vecmath2.Matrix4f mat2c = newRandomMatrix_vm2( false,
	// null );
	//
	// private final boolean compare( org.openmali.vecmath.Matrix4f mat1,
	// org.openmali.vecmath2.Matrix4f mat2 )
	// {
	// for ( int i = 0; i < 4; i++ )
	// {
	// for ( int j = 0; j < 4; j++ )
	// {
	// final float valA = get( mat1, i, j );
	// final float valB = mat2.get( i, j );
	//
	// if ( Math.abs( valA - valB ) > EPSILON )
	// return ( false );
	// }
	// }
	//
	// return ( true );
	// }
	//
	// public final int compare()
	// {
	// int result = 0;
	//
	// result |= compare( mat1a, mat2a ) ? 1 : 0;
	// result |= compare( mat1b, mat2b ) ? 2 : 0;
	// result |= compare( mat1c, mat2c ) ? 4 : 0;
	//
	// return ( result );
	// }
	//
	// private final void dump( org.openmali.vecmath.Matrix4f mat1,
	// org.openmali.vecmath2.Matrix4f mat2 )
	// {
	// System.out.println( mat1.m00 + ", " + mat2.m00() );
	// System.out.println( mat1.m01 + ", " + mat2.m01() );
	// System.out.println( mat1.m02 + ", " + mat2.m02() );
	// System.out.println( mat1.m03 + ", " + mat2.m03() );
	// System.out.println( mat1.m10 + ", " + mat2.m10() );
	// System.out.println( mat1.m11 + ", " + mat2.m11() );
	// System.out.println( mat1.m12 + ", " + mat2.m12() );
	// System.out.println( mat1.m13 + ", " + mat2.m13() );
	// System.out.println( mat1.m20 + ", " + mat2.m20() );
	// System.out.println( mat1.m21 + ", " + mat2.m21() );
	// System.out.println( mat1.m22 + ", " + mat2.m22() );
	// System.out.println( mat1.m23 + ", " + mat2.m23() );
	// System.out.println( mat1.m30 + ", " + mat2.m30() );
	// System.out.println( mat1.m31 + ", " + mat2.m31() );
	// System.out.println( mat1.m32 + ", " + mat2.m32() );
	// System.out.println( mat1.m33 + ", " + mat2.m33() );
	// }
	//
	// private final void dump( int type )
	// {
	// if ( ( type & 1 ) > 0 )
	// dump( mat1a, mat2a );
	//
	// if ( ( type & 2 ) > 0 )
	// dump( mat1b, mat2b );
	//
	// if ( ( type & 4 ) > 0 )
	// dump( mat1c, mat2c );
	// }
	// }
	//
	// @SuppressWarnings("unused")
	// private static class TestMatrices3
	// {
	// private static final float[] newRandomValues()
	// {
	// final float[] values = new float[ 9 ];
	//
	// for ( int i = 0; i < 3; i++ )
	// {
	// for ( int j = 0; j < 3; j++ )
	// {
	// values[ i * 3 + j ] = RND.nextFloat() * 1000f;
	// }
	// }
	//
	// return ( values );
	// }
	//
	// private static final org.openmali.vecmath.Matrix3f newRandomMatrix_vm1(
	// float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// return ( new org.openmali.vecmath.Matrix3f( values ) );
	// }
	//
	// private static final org.openmali.vecmath2.Matrix3f newRandomMatrix_vm2(
	// boolean readOnly, float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// if ( readOnly )
	// return ( org.openmali.vecmath2.Matrix3f.newReadOnly( values ) );
	//
	// return ( new org.openmali.vecmath2.Matrix3f( values ) );
	// }
	//
	// private final float[] valuesA = newRandomValues();
	// private final float[] valuesB = newRandomValues();
	//
	// public org.openmali.vecmath.Matrix3f mat1a = newRandomMatrix_vm1( valuesA
	// );
	// public org.openmali.vecmath.Matrix3f mat1b = newRandomMatrix_vm1( valuesB
	// );
	// public org.openmali.vecmath.Matrix3f mat1c = newRandomMatrix_vm1( null );
	//
	// public org.openmali.vecmath2.Matrix3f mat2a = newRandomMatrix_vm2(
	// USE_READ_ONLY, valuesA );
	// public org.openmali.vecmath2.Matrix3f mat2b = newRandomMatrix_vm2(
	// USE_READ_ONLY, valuesB );
	// public org.openmali.vecmath2.Matrix3f mat2c = newRandomMatrix_vm2( false,
	// null );
	//
	// private final boolean compare( org.openmali.vecmath.Matrix3f mat1,
	// org.openmali.vecmath2.Matrix3f mat2 )
	// {
	// for ( int i = 0; i < 3; i++ )
	// {
	// for ( int j = 0; j < 3; j++ )
	// {
	// final float valA = get( mat1, i, j );
	// final float valB = mat2.get( i, j );
	//
	// if ( Math.abs( valA - valB ) > EPSILON )
	// return ( false );
	// }
	// }
	//
	// return ( true );
	// }
	//
	// public final int compare()
	// {
	// int result = 0;
	//
	// result |= compare( mat1a, mat2a ) ? 1 : 0;
	// result |= compare( mat1b, mat2b ) ? 2 : 0;
	// result |= compare( mat1c, mat2c ) ? 4 : 0;
	//
	// return ( result );
	// }
	//
	// private final void dump( org.openmali.vecmath.Matrix3f mat1,
	// org.openmali.vecmath2.Matrix3f mat2 )
	// {
	// System.out.println( mat1.m00 + ", " + mat2.m00() );
	// System.out.println( mat1.m01 + ", " + mat2.m01() );
	// System.out.println( mat1.m02 + ", " + mat2.m02() );
	// System.out.println( mat1.m10 + ", " + mat2.m10() );
	// System.out.println( mat1.m11 + ", " + mat2.m11() );
	// System.out.println( mat1.m12 + ", " + mat2.m12() );
	// System.out.println( mat1.m20 + ", " + mat2.m20() );
	// System.out.println( mat1.m21 + ", " + mat2.m21() );
	// System.out.println( mat1.m22 + ", " + mat2.m22() );
	// }
	//
	// private final void dump( int type )
	// {
	// if ( ( type & 1 ) > 0 )
	// dump( mat1a, mat2a );
	//
	// if ( ( type & 2 ) > 0 )
	// dump( mat1b, mat2b );
	//
	// if ( ( type & 4 ) > 0 )
	// dump( mat1c, mat2c );
	// }
	// }
	//
	// @SuppressWarnings("unused")
	// private static class TestVectors3
	// {
	// private static final float[] newRandomValues()
	// {
	// final float[] values = new float[ 3 ];
	//
	// for ( int i = 0; i < 3; i++ )
	// {
	// values[ i ] = RND.nextFloat() * 1000f;
	// }
	//
	// return ( values );
	// }
	//
	// private static final org.openmali.vecmath.Vector3f newRandomVector_vm1(
	// float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// return ( new org.openmali.vecmath.Vector3f( values ) );
	// }
	//
	// private static final org.openmali.vecmath2.Vector3f newRandomVector_vm2(
	// boolean readOnly, float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// if ( readOnly )
	// return ( org.openmali.vecmath2.Vector3f.newReadOnly( values ) );
	//
	// return ( new org.openmali.vecmath2.Vector3f( values ) );
	// }
	//
	// private final float[] valuesA = newRandomValues();
	// private final float[] valuesB = newRandomValues();
	//
	// public org.openmali.vecmath.Vector3f vec1a = newRandomVector_vm1( valuesA
	// );
	// public org.openmali.vecmath.Vector3f vec1b = newRandomVector_vm1( valuesB
	// );
	// public org.openmali.vecmath.Vector3f vec1c = newRandomVector_vm1( null );
	//
	// public org.openmali.vecmath2.Vector3f vec2a = newRandomVector_vm2(
	// USE_READ_ONLY, valuesA );
	// public org.openmali.vecmath2.Vector3f vec2b = newRandomVector_vm2(
	// USE_READ_ONLY, valuesB );
	// public org.openmali.vecmath2.Vector3f vec2c = newRandomVector_vm2( false,
	// null );
	//
	// private final boolean compare( org.openmali.vecmath.Vector3f vec1,
	// org.openmali.vecmath2.Vector3f vec2 )
	// {
	// for ( int i = 0; i < 3; i++ )
	// {
	// final float valA = get( vec1, i );
	// final float valB = vec2.getValue( i );
	//
	// if ( Math.abs( valA - valB ) > EPSILON )
	// return ( false );
	// }
	//
	// return ( true );
	// }
	//
	// public final int compare()
	// {
	// int result = 0;
	//
	// result |= compare( vec1a, vec2a ) ? 1 : 0;
	// result |= compare( vec1b, vec2b ) ? 2 : 0;
	// result |= compare( vec1c, vec2c ) ? 4 : 0;
	//
	// return ( result );
	// }
	//
	// private final void dump( org.openmali.vecmath.Vector3f vec1,
	// org.openmali.vecmath2.Vector3f vec2 )
	// {
	// System.out.println( vec1.x + ", " + vec2.getX() );
	// System.out.println( vec1.y + ", " + vec2.getY() );
	// System.out.println( vec1.z + ", " + vec2.getZ() );
	// }
	//
	// private final void dump( int type )
	// {
	// if ( ( type & 1 ) > 0 )
	// dump( vec1a, vec2a );
	//
	// if ( ( type & 2 ) > 0 )
	// dump( vec1b, vec2b );
	//
	// if ( ( type & 4 ) > 0 )
	// dump( vec1c, vec2c );
	// }
	// }
	//
	// @SuppressWarnings("unused")
	// private static class TestPoints3
	// {
	// private static final float[] newRandomValues()
	// {
	// final float[] values = new float[ 3 ];
	//
	// for ( int i = 0; i < 3; i++ )
	// {
	// values[ i ] = RND.nextFloat() * 1000f;
	// }
	//
	// return ( values );
	// }
	//
	// private static final org.openmali.vecmath.Point3f newRandomPoint_vm1(
	// float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// return ( new org.openmali.vecmath.Point3f( values ) );
	// }
	//
	// private static final org.openmali.vecmath2.Point3f newRandomPoint_vm2(
	// boolean readOnly, float[] values )
	// {
	// if ( values == null )
	// values = newRandomValues();
	//
	// if ( readOnly )
	// return ( org.openmali.vecmath2.Point3f.newReadOnly( values ) );
	//
	// return ( new org.openmali.vecmath2.Point3f( values ) );
	// }
	//
	// private final float[] valuesA = newRandomValues();
	// private final float[] valuesB = newRandomValues();
	//
	// public org.openmali.vecmath.Point3f pt1a = newRandomPoint_vm1( valuesA );
	// public org.openmali.vecmath.Point3f pt1b = newRandomPoint_vm1( valuesB );
	// public org.openmali.vecmath.Point3f pt1c = newRandomPoint_vm1( null );
	//
	// public org.openmali.vecmath2.Point3f pt2a = newRandomPoint_vm2(
	// USE_READ_ONLY, valuesA );
	// public org.openmali.vecmath2.Point3f pt2b = newRandomPoint_vm2(
	// USE_READ_ONLY, valuesB );
	// public org.openmali.vecmath2.Point3f pt2c = newRandomPoint_vm2( false,
	// null );
	//
	// private final boolean compare( org.openmali.vecmath.Point3f pt1,
	// org.openmali.vecmath2.Point3f pt2 )
	// {
	// for ( int i = 0; i < 3; i++ )
	// {
	// final float valA = get( pt1, i );
	// final float valB = pt2.getValue( i );
	//
	// if ( Math.abs( valA - valB ) > EPSILON )
	// return ( false );
	// }
	//
	// return ( true );
	// }
	//
	// public final int compare()
	// {
	// int result = 0;
	//
	// result |= compare( pt1a, pt2a ) ? 1 : 0;
	// result |= compare( pt1b, pt2b ) ? 2 : 0;
	// result |= compare( pt1c, pt2c ) ? 4 : 0;
	//
	// return ( result );
	// }
	//
	// private final void dump( org.openmali.vecmath.Point3f pt1,
	// org.openmali.vecmath2.Point3f pt2 )
	// {
	// System.out.println( pt1.x + ", " + pt2.getX() );
	// System.out.println( pt1.y + ", " + pt2.getY() );
	// System.out.println( pt1.z + ", " + pt2.getZ() );
	// }
	//
	// private final void dump( int type )
	// {
	// if ( ( type & 1 ) > 0 )
	// dump( pt1a, pt2a );
	//
	// if ( ( type & 2 ) > 0 )
	// dump( pt1b, pt2b );
	//
	// if ( ( type & 4 ) > 0 )
	// dump( pt1c, pt2c );
	// }
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void set( org.openmali.vecmath.Matrix4f mat, int r,
	// int c, float val )
	// {
	// switch ( r )
	// {
	// case 0:
	// switch ( c )
	// {
	// case 0:
	// mat.m00 = val;
	// return;
	// case 1:
	// mat.m01 = val;
	// return;
	// case 2:
	// mat.m02 = val;
	// return;
	// case 3:
	// mat.m03 = val;
	// return;
	// }
	// break;
	//
	// case 1:
	// switch ( c )
	// {
	// case 0:
	// mat.m10 = val;
	// return;
	// case 1:
	// mat.m11 = val;
	// return;
	// case 2:
	// mat.m12 = val;
	// return;
	// case 3:
	// mat.m13 = val;
	// return;
	// }
	// break;
	//
	// case 2:
	// switch ( c )
	// {
	// case 0:
	// mat.m20 = val;
	// return;
	// case 1:
	// mat.m21 = val;
	// return;
	// case 2:
	// mat.m22 = val;
	// return;
	// case 3:
	// mat.m23 = val;
	// return;
	// }
	// break;
	//
	// case 3:
	// switch ( c )
	// {
	// case 0:
	// mat.m30 = val;
	// return;
	// case 1:
	// mat.m31 = val;
	// return;
	// case 2:
	// mat.m32 = val;
	// return;
	// case 3:
	// mat.m33 = val;
	// return;
	// }
	// break;
	// }
	//
	// throw new Error( "Illegal matrix address (" + r + ", " + c + ")." );
	// }
	//
	// private static final float get( org.openmali.vecmath.Matrix4f mat, int r,
	// int c )
	// {
	// switch ( r )
	// {
	// case 0:
	// switch ( c )
	// {
	// case 0:
	// return ( mat.m00 );
	// case 1:
	// return ( mat.m01 );
	// case 2:
	// return ( mat.m02 );
	// case 3:
	// return ( mat.m03 );
	// }
	// break;
	//
	// case 1:
	// switch ( c )
	// {
	// case 0:
	// return ( mat.m10 );
	// case 1:
	// return ( mat.m11 );
	// case 2:
	// return ( mat.m12 );
	// case 3:
	// return ( mat.m13 );
	// }
	// break;
	//
	// case 2:
	// switch ( c )
	// {
	// case 0:
	// return ( mat.m20 );
	// case 1:
	// return ( mat.m21 );
	// case 2:
	// return ( mat.m22 );
	// case 3:
	// return ( mat.m23 );
	// }
	// break;
	//
	// case 3:
	// switch ( c )
	// {
	// case 0:
	// return ( mat.m30 );
	// case 1:
	// return ( mat.m31 );
	// case 2:
	// return ( mat.m32 );
	// case 3:
	// return ( mat.m33 );
	// }
	// break;
	// }
	//
	// throw new Error( "Illegal matrix address (" + r + ", " + c + ")." );
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void set( org.openmali.vecmath.Matrix3f mat, int r,
	// int c, float val )
	// {
	// switch ( r )
	// {
	// case 0:
	// switch ( c )
	// {
	// case 0:
	// mat.m00 = val;
	// return;
	// case 1:
	// mat.m01 = val;
	// return;
	// case 2:
	// mat.m02 = val;
	// return;
	// }
	// break;
	//
	// case 1:
	// switch ( c )
	// {
	// case 0:
	// mat.m10 = val;
	// return;
	// case 1:
	// mat.m11 = val;
	// return;
	// case 2:
	// mat.m12 = val;
	// return;
	// }
	// break;
	//
	// case 2:
	// switch ( c )
	// {
	// case 0:
	// mat.m20 = val;
	// return;
	// case 1:
	// mat.m21 = val;
	// return;
	// case 2:
	// mat.m22 = val;
	// return;
	// }
	// break;
	// }
	//
	// throw new Error( "Illegal matrix address (" + r + ", " + c + ")." );
	// }
	//
	// private static final float get( org.openmali.vecmath.Matrix3f mat, int r,
	// int c )
	// {
	// switch ( r )
	// {
	// case 0:
	// switch ( c )
	// {
	// case 0:
	// return ( mat.m00 );
	// case 1:
	// return ( mat.m01 );
	// case 2:
	// return ( mat.m02 );
	// }
	// break;
	//
	// case 1:
	// switch ( c )
	// {
	// case 0:
	// return ( mat.m10 );
	// case 1:
	// return ( mat.m11 );
	// case 2:
	// return ( mat.m12 );
	// }
	// break;
	//
	// case 2:
	// switch ( c )
	// {
	// case 0:
	// return ( mat.m20 );
	// case 1:
	// return ( mat.m21 );
	// case 2:
	// return ( mat.m22 );
	// }
	// break;
	// }
	//
	// throw new Error( "Illegal matrix address (" + r + ", " + c + ")." );
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void set( org.openmali.vecmath.Tuple3f tup, int i,
	// float val )
	// {
	// switch ( i )
	// {
	// case 0:
	// tup.x = val;
	// return;
	// case 1:
	// tup.y = val;
	// return;
	// case 2:
	// tup.z = val;
	// return;
	// }
	//
	// throw new Error( "Illegal tuple element (" + i + ")." );
	// }
	//
	// private static final float get( org.openmali.vecmath.Tuple3f tup, int i )
	// {
	// switch ( i )
	// {
	// case 0:
	// return ( tup.x );
	// case 1:
	// return ( tup.y );
	// case 2:
	// return ( tup.z );
	// }
	//
	// throw new Error( "Illegal tuple element (" + i + ")." );
	// }
	//
	//
	// @SuppressWarnings("unused")
	// private static final void testMul4( TestMatrices4 mats, final int STEPS )
	// {
	// System.out.println( "test: mul4()" );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// //mats.mat1a.m00 = (float)i;
	// mats.mat1c.mul( mats.mat1a, mats.mat1b );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// //mats.mat1a.set( 0, 0, (float)i );
	// mats.mat2c.mul( mats.mat2a, mats.mat2b );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + mats.compare() );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testMul3( TestMatrices3 mats, final int STEPS )
	// {
	// System.out.println( "test: mul3()" );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// //mats.mat1a.m00 = (float)i;
	// mats.mat1c.mul( mats.mat1a, mats.mat1b );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// //mats.mat1a.set( 0, 0, (float)i );
	// mats.mat2c.mul( mats.mat2a, mats.mat2b );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + mats.compare() );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testMul4Scalar( TestMatrices4 mats, final int
	// STEPS )
	// {
	// System.out.println( "test: mul4Scalar()" );
	//
	// mats.mat1c.set( mats.mat1a );
	// mats.mat2c.set( mats.mat2a );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1c.mul( RND.nextFloat() );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2c.mul( RND.nextFloat() );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + mats.compare() );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testMat4ToMat4( TestMatrices4 mats, final int
	// STEPS )
	// {
	// System.out.println( "test: mat4ToMat4()" );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1c.set( mats.mat1a );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2c.set( mats.mat2a );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + mats.compare() );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testDeterminant4( TestMatrices4 mats, final int
	// STEPS )
	// {
	// System.out.println( "test: determinant4()" );
	//
	// final float[] valuesE = new float[ STEPS ];
	// final float[] valuesF = new float[ STEPS ];
	//
	// final long tE0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// valuesE[ i ] = mats.mat1a.determinant();
	// }
	//
	// final long tE = System.currentTimeMillis() - tE0;
	//
	// final long tF0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// valuesF[ i ] = mats.mat2a.determinant();
	// }
	//
	// final long tF = System.currentTimeMillis() - tF0;
	//
	// System.out.println( "tE: " + tE );
	// System.out.println( "tF: " + tF );
	// int numCorrect = 0;
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// if ( Math.abs( valuesE[ i ] - valuesF[ i ] ) < EPSILON )
	// numCorrect++;
	// }
	//
	// System.out.println( "equal: " + numCorrect + " / " + ( STEPS - numCorrect
	// ) );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testInvert4( TestMatrices4 mats, final int
	// STEPS )
	// {
	// System.out.println( "test: invert4()" );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// //mats.mat1c.set( mats.mat1a );
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1c.invert( mats.mat1a );
	// //mats.mat1c.invert();
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// //mats.mat2c.set( mats.mat2a );
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2c.invert( mats.mat2a );
	// //mats.mat2c.invert();
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + mats.compare() );
	// //mats.dump( 4 );
	// System.out.println();
	// }
	//
	// private static final void testInvert3( TestMatrices3 mats, final int
	// STEPS )
	// {
	// System.out.println( "test: invert3()" );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// //mats.mat1c.set( mats.mat1a );
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1c.invert( mats.mat1a );
	// //mats.mat1c.invert();
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// //mats.mat2c.set( mats.mat2a );
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2c.invert( mats.mat2a );
	// //mats.mat2c.invert();
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + mats.compare() );
	// //mats.dump( 4 );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testReadElementsMat4( TestMatrices4 mats, final
	// int STEPS )
	// {
	// System.out.println( "test: readElementsMat4()" );
	//
	// boolean result = true;
	//
	// result = result && ( Math.abs( mats.mat1a.m00 - mats.mat2a.m00() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m01 - mats.mat2a.m01() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m02 - mats.mat2a.m02() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m03 - mats.mat2a.m03() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m10 - mats.mat2a.m10() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m11 - mats.mat2a.m11() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m12 - mats.mat2a.m12() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m13 - mats.mat2a.m13() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m20 - mats.mat2a.m20() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m21 - mats.mat2a.m21() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m22 - mats.mat2a.m22() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m23 - mats.mat2a.m23() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m30 - mats.mat2a.m30() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m31 - mats.mat2a.m31() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m32 - mats.mat2a.m32() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m33 - mats.mat2a.m33() ) <
	// EPSILON );
	//
	// System.out.println( "equal: " + result );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testReadElementsMat3( TestMatrices3 mats, final
	// int STEPS )
	// {
	// System.out.println( "test: readElementsMat3()" );
	//
	// boolean result = true;
	//
	// result = result && ( Math.abs( mats.mat1a.m00 - mats.mat2a.m00() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m01 - mats.mat2a.m01() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m02 - mats.mat2a.m02() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m10 - mats.mat2a.m10() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m11 - mats.mat2a.m11() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m12 - mats.mat2a.m12() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m20 - mats.mat2a.m20() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m21 - mats.mat2a.m21() ) <
	// EPSILON );
	// result = result && ( Math.abs( mats.mat1a.m22 - mats.mat2a.m22() ) <
	// EPSILON );
	//
	// System.out.println( "equal: " + result );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testMat3ToMat4( TestMatrices4 mats, final int
	// STEPS )
	// {
	// System.out.println( "test: mat3ToMat4()" );
	//
	// TestMatrices3 mats3 = new TestMatrices3();
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1c.set( mats3.mat1a );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2c.set( mats3.mat2a );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + mats.compare() );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testReadElementsVec3( TestVectors3 vecs, final
	// int STEPS )
	// {
	// System.out.println( "test: readElementsVec3()" );
	//
	// boolean result = true;
	//
	// result = result && ( Math.abs( vecs.vec1a.x - vecs.vec2a.getX() ) <
	// EPSILON );
	// result = result && ( Math.abs( vecs.vec1a.y - vecs.vec2a.getY() ) <
	// EPSILON );
	// result = result && ( Math.abs( vecs.vec1a.z - vecs.vec2a.getZ() ) <
	// EPSILON );
	//
	// System.out.println( "equal: " + result );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testReadElementsPoints3( TestPoints3 pts, final
	// int STEPS )
	// {
	// System.out.println( "test: readElementsVec3()" );
	//
	// boolean result = true;
	//
	// result = result && ( Math.abs( pts.pt1a.x - pts.pt2a.getX() ) < EPSILON
	// );
	// result = result && ( Math.abs( pts.pt1a.y - pts.pt2a.getY() ) < EPSILON
	// );
	// result = result && ( Math.abs( pts.pt1a.z - pts.pt2a.getZ() ) < EPSILON
	// );
	//
	// System.out.println( "equal: " + result );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testTransformVec43( TestMatrices4 mats, final
	// int STEPS )
	// {
	// System.out.println( "test: transformVec43()" );
	//
	// final TestVectors3 vecs = new TestVectors3();
	// vecs.vec1c.set( vecs.vec1a );
	// vecs.vec2c.set( vecs.vec2a );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1a.transform( vecs.vec1c );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2a.transform( vecs.vec2c );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + vecs.compare() );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testTransformPoint43( TestMatrices4 mats, final
	// int STEPS )
	// {
	// System.out.println( "test: transformVec43()" );
	//
	// final TestPoints3 pts = new TestPoints3();
	// pts.pt1c.set( pts.pt1a );
	// pts.pt2c.set( pts.pt2a );
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1a.transform( pts.pt1c );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2a.transform( pts.pt2c );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + pts.compare() );
	// System.out.println();
	// }
	//
	// @SuppressWarnings("unused")
	// private static final void testMulVec33( TestMatrices3 mats, final int
	// STEPS )
	// {
	// System.out.println( "test: transformVec43()" );
	//
	// final TestVectors3 vecs = new TestVectors3();
	//
	// final long tA0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat1a.mul( vecs.vec1a, vecs.vec1c );
	// }
	//
	// final long tA = System.currentTimeMillis() - tA0;
	//
	// final long tB0 = System.currentTimeMillis();
	//
	// for ( int i = 0; i < STEPS; i++ )
	// {
	// mats.mat2a.mul( vecs.vec2a, vecs.vec2c );
	// }
	//
	// final long tB = System.currentTimeMillis() - tB0;
	//
	// System.out.println( "tA: " + tA );
	// System.out.println( "tB: " + tB );
	// System.out.println( "equal: " + vecs.compare() );
	// System.out.println();
	// }
	//
	// private static final void testMatrix4f()
	// {
	// final int STEPS = 1000000;
	//
	// //testMul4( new TestMatrices4(), STEPS );
	// //testMul3( new TestMatrices3(), STEPS );
	// //testMul4Scalar( new TestMatrices4(), STEPS );
	// //testMat4ToMat4( new TestMatrices4(), STEPS );
	// //testDeterminant4( new TestMatrices4(), STEPS );
	// //testInvert4( new TestMatrices4(), STEPS );
	// testInvert3( new TestMatrices3(), STEPS );
	// //testReadElementsMat4( new TestMatrices4(), STEPS );
	// //testReadElementsMat3( new TestMatrices3(), STEPS );
	// //testReadElementsVec3( new TestVectors3(), STEPS );
	// //testReadElementsPoints3( new TestPoints3(), STEPS );
	// //testMat3ToMat4( new TestMatrices4(), STEPS );
	// //testTransformVec43( new TestMatrices4(), STEPS );
	// //testTransformPoint43( new TestMatrices4(), STEPS );
	// //testMulVec33( new TestMatrices3(), STEPS );
	// }
	//
	// public static final void main( String[] args )
	// {
	// testMatrix4f();
	// }
}
