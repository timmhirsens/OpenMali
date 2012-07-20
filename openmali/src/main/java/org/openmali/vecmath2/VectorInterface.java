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
 * Common interface for all vector types.<br>
 * <b>Using it may slow down execution time because of prevented inlining.</b>
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface VectorInterface< T extends TupleNf< T >, V extends TupleNf< ? > > extends TupleInterface< T >
{
    /**
     * @return the squared length of this vector
     */
    public float lengthSquared();
    
    /**
     * @return the length of this vector
     */
    public float length();
    
    /**
     * Normalizes this vector in place.
     * 
     * @return itself
     */
    public VectorInterface< T, V > normalize();
    
    /**
     * Sets the value of this vector to the normalization of vector v1.
     * 
     * @param vector the un-normalized vector
     * 
     * @return itself
     */
    public VectorInterface< T, V > normalize( V vector );
    
    /**
     * Sets this vector to be the vector cross product of vectors v1 and v2.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * 
     * @return itself
     */
    public VectorInterface< T, V > cross( V v1, V v2 );
    
    /**
     * Computes the dot product of the this vector and vector v2.
     * 
     * @param vector2 the other vector
     */
    public float dot( V vector2 );
    
    /**
     * Returns the angle in radians between this vector and the vector
     * parameter; the return value is constrained to the range [0,PI].
     * 
     * @param vector2 the other vector
     * @return the angle in radians in the range [0,PI]
     */
    public float angle( V vector2 );
}
