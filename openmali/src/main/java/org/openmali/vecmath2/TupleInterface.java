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

/**
 * This is the base interface for all tuple types.
 * <b>Using it may slow down execution time because of prevented inlining.</b>
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface TupleInterface< T extends TupleNf< T > >
{
    /**
     * @return Is this tuple a read-only one?
     */
    public boolean isReadOnly();
    
    /**
     * Marks this tuple non-dirty.
     * Any value-manipulation will mark it dirty again.
     * 
     * @return the old value
     */
    public boolean setClean();
    
    /**
     * @return This tuple's dirty-flag
     */
    public boolean isDirty();
    
    /**
     * @return this Tuple's size().
     */
    public int getSize();
    
    /**
     * Sets the i-th value of this tuple.
     * 
     * @return itself
     */
    public TupleInterface< T > setValue( int i, float v );
    
    /**
     * Sets the i-th value of this tuple.
     */
    public float getValue( int i );
    
    /**
     * Sets all values of this TupleNf to f.
     * 
     * @param f
     * 
     * @return itself
     */
    public TupleInterface< T > fill( float f );
    
    /**
     * Adds v to this tuple's i'th value.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     * 
     * @return itself
     */
    public TupleInterface< T > addValue( int i, float v );
    
    /**
     * Subtracts v of this tuple's i'th value.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     * 
     * @return itself
     */
    public TupleInterface< T > subValue( int i, float v );
    
    /**
     * Multiplies v to this tuple's i'th value.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     * 
     * @return itself
     */
    public TupleInterface< T > mulValue( int i, float v );
    
    /**
     * Multiplies all components of this tuple with v.
     * 
     * @param v modification amount
     * 
     * @return itself
     */
    public TupleInterface< T > mul( float v );
    
    /**
     * Divides this tuple's i'th value by v.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     * 
     * @return itself
     */
    public TupleInterface< T > divValue( int i, float v );
    
    /**
     * Divides all components of this tuple by v.
     * 
     * @param v modification amount
     * 
     * @return itself
     */
    public TupleInterface< T > div( float v );
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size getSize())
     * @param offset the offset in the (source) values array
     * 
     * @return itself
     */
    public TupleInterface< T > set( float[] values, int offset );
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size getSize())
     * 
     * @return itself
     */
    public TupleInterface< T > set( float[] values );
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param tuple the tuple to be copied
     * 
     * @return itself
     */
    public TupleInterface< T > set( TupleNf< ? > tuple );
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     * @param offset the offset in the (target) buffer array
     */
    public void get( float[] buffer, int offset );
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     */
    public void get( float[] buffer );
    
    /**
     * Writes all values of this Tuple to the specified buffer Tuple.
     * 
     * @param buffer the buffer Tuple to write the values to
     */
    public void get( TupleNf< ? > buffer );
    
    /**
     * Sets all components to zero.
     * 
     * @return itself
     */
    public TupleInterface< T > setZero();
    
    /**
     * Negates the value of this vector in place.
     * 
     * @return itself
     */
    public TupleInterface< T > negate();
    
    /**
     * Sets the value of this tuple to the negation of tuple that.
     * 
     * @param tuple the source vector
     * 
     * @return itself
     */
    public TupleInterface< T > negate( T tuple );
    
    /**
     * Sets each component of the tuple parameter to its absolute value and
     * places the modified values into this tuple.
     * 
     * @return itself
     */
    public TupleInterface< T > absolute();
    
    /**
     * Sets each component of the tuple parameter to its absolute value and
     * places the modified values into this tuple.
     * 
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    public TupleInterface< T > absolute( T tuple );
    
    /**
     * Sets the value of this tuple to the vector sum of tuples t1 and t2.
     * 
     * @param tuple1 the first tuple
     * @param tuple2 the second tuple
     * 
     * @return itself
     */
    public TupleInterface< T > add( T tuple1, T tuple2 );
    
    /**
     * Sets the value of this tuple to the vector sum of itself and tuple t1.
     * 
     * @param tuple2 the other tuple
     * 
     * @return itself
     */
    public TupleInterface< T > add( T tuple2 );
    
    /**
     * Sets the value of this tuple to the vector difference of tuple t1 and t2
     * (this = t1 - t2).
     * 
     * @param tuple1 the first tuple
     * @param tuple2 the second tuple
     * 
     * @return itself
     */
    public TupleInterface< T > sub( T tuple1, T tuple2 );
    
    /**
     * Sets the value of this tuple to the vector difference of itself and tuple
     * t1 (this = this - t1).
     * 
     * @param tuple2 the other tuple
     * 
     * @return itself
     */
    public TupleInterface< T > sub( T tuple2 );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1.
     * 
     * @param factor the scalar value
     * @param tuple the source tuple
     * 
     * @return itself
     */
    public TupleInterface< T > scale( float factor, T tuple );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself.
     * 
     * @param factor the scalar value
     * 
     * @return itself
     */
    public TupleInterface< T > scale( float factor );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1 and
     * then adds tuple t2 (this = s*t1 + t2).
     * 
     * @param factor the scalar value
     * @param tuple1 the tuple to be multipled
     * @param tuple2 the tuple to be added
     * 
     * @return itself
     */
    public TupleInterface< T > scaleAdd( float factor, T tuple1, T tuple2 );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself and
     * then adds tuple t1 (this = s*this + t1).
     * 
     * @param factor the scalar value
     * @param tuple2 the tuple to be added
     * 
     * @return itself
     */
    public TupleInterface< T > scaleAdd( float factor, T tuple2 );
    
    /**
     * Clamps the minimum value of this tuple to the min parameter.
     * 
     * @param min the lowest value in this tuple after clamping
     * 
     * @return itself
     */
    public TupleInterface< T > clampMin( float min );
    
    /**
     * Clamps the maximum value of this tuple to the max parameter.
     * 
     * @param max the highest value in the tuple after clamping
     * 
     * @return itself
     */
    public TupleInterface< T > clampMax( float max );
    
    /**
     * Clamps this tuple to the range [min, max].
     * 
     * @param min the lowest value in this tuple after clamping
     * @param max the highest value in this tuple after clamping
     * 
     * @return itself
     */
    public TupleInterface< T > clamp( float min, float max );
    
    /**
     * Clamps the tuple parameter to the range [min, max] and places the values
     * into this tuple.
     * 
     * @param min the lowest value in the tuple after clamping
     * @param max the highest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    public TupleInterface< T > clamp( float min, float max, T tuple );
    
    /**
     * Clamps the minimum value of the tuple parameter to the min parameter and
     * places the values into this tuple.
     * 
     * @param min the lowest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    public TupleInterface< T > clampMin( float min, T tuple );
    
    /**
     * Clamps the maximum value of the tuple parameter to the max parameter and
     * places the values into this tuple.
     * 
     * @param max the highest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     * 
     * @return itself
     */
    public TupleInterface< T > clampMax( float max, T tuple );
    
    /**
     * Rounds this tuple to the given number of decimal places.
     * 
     * @param decPlaces
     * 
     * @return itself
     */
    public TupleInterface< T > round( T tuple, int decPlaces );
    
    /**
     * Rounds this tuple to the given number of decimal places.
     * 
     * @param decPlaces
     * 
     * @return itself
     */
    public TupleInterface< T > round( int decPlaces );
    
    /**
     * Linearly interpolates between this tuple and tuple t2 and places the
     * result into this tuple: this = (1 - alpha) * this + alpha * t1.
     * 
     * @param t2 the first tuple
     * @param alpha the alpha interpolation parameter
     */
    public void interpolate( T t2, float alpha );
    
    /**
     * Linearly interpolates between tuples t1 and t2 and places the result into
     * this tuple: this = (1 - alpha) * t1 + alpha * t2.
     * 
     * @param t1 the first tuple
     * @param t2 the second tuple
     * @param alpha the alpha interpolation parameter
     */
    public void interpolate( T t1, T t2, float alpha );
    
    /**
     * Returns true if the L-infinite distance between this vector and vector v1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2), . . . ].
     * 
     * @param v2 The vector to be compared to this vector
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( T v2, float epsilon );
}
