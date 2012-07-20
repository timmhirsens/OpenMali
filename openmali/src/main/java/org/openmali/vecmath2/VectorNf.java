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

/**
 * A float precision, general one dimensional vector class.
 * Index numbering is zero-based.
 * 
 * Inspired by Kenji Hiranabe's GVector implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Arne Mueller
 */
public class VectorNf extends TupleNf< VectorNf > implements Externalizable, VectorInterface< VectorNf, VectorNf >
{
    private static final long serialVersionUID = 8171027992467938290L;
    
    private VectorNf readOnlyInstance = null;
    
    /**
     * Returns the sum of the squares of this vector (its length sqaured in
     * n-dimensional space).
     * <p>
     * 
     * @return length squared of this vector
     */
    public final float getNormSquared()
    {
        float s = 0.0f;
        for ( int i = 0; i < getSize(); i++ )
        {
            s += getValue( i ) * getValue( i );
        }
        
        return ( s );
    }
    
    /**
     * Returns the square root of the sum of the squares of this vector (its
     * length in n-dimensional space).
     * 
     * @return length of this vector
     */
    public final float getNorm()
    {
        return ( FastMath.sqrt( getNormSquared() ) );
    }
    
    /**
     * {@inheritDoc}
     */
    public final VectorNf normalize()
    {
        final float len = getNorm();
        
        // zero-div may happen.
        for ( int i = 0; i < getSize(); i++ )
            divValue( i, len );
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final VectorNf normalize( VectorNf v )
    {
        set( v );
        
        normalize();
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public float lengthSquared()
    {
        return ( getNormSquared() );
    }
    
    /**
     * {@inheritDoc}
     */
    public float length()
    {
        return ( getNorm() );
    }
    
    /**
     * <p>
     * Multiplies matrix m1 times Vector v1 and places the result into this vector
     * (this = m1 * v1).
     * </p>
     * <p>
     * ATTENTION! This method is not alias-safe!
     * </p>
     * 
     * @param m The matrix in the multiplication
     * @param v The vector that is multiplied
     */
    public void mul( MatrixMxNf m, VectorNf v )
    {
        // note: this implementatin is NOT alias-safe!
        // i.e. v.mul(M, v) does not comutes right.
        
        final int vSize = v.getSize();
        final int numCols = m.getNumCols();
        final int numRows = m.getNumRows();
        
        if ( vSize != numCols )
            throw new IllegalArgumentException( "v1.size:" + vSize + " != m1.nCol:" + numCols );
        if ( this.getSize() != numRows )
            throw new IllegalArgumentException( "this.size:" + this.getSize() + " != m1.nRow:" + numRows );
        
        for ( int i = 0; i < getSize(); i++ )
        {
            float sum = 0.0f;
            for ( int j = 0; j < numCols; j++ )
            {
                sum += m.get( i, j ) * v.getValue( j );
            }
            
            this.setValue( i, sum );
        }
    }
    
    /**
     * <p>
     * Multiplies the transpose of vector v1 (ie, v1 becomes a row vector with
     * respect to the multiplication) times matrix m1 and places the result into
     * this vector (this = transpose(v1)*m1). The result is technically a row
     * vector, but the GVector class only knows about column vectors, and so the
     * result is stored as a column vector.
     * </p>
     * <p>
     * ATTENTION! This method is not alias-safe!
     * </p>
     * 
     * @param v1 The vector that is temporarily transposed
     * @param m The matrix in the multiplication
     */
    public void mul( VectorNf v1, MatrixMxNf m ) 
    {
        // note: this implementatin is NOT alias-safe!
        // i.e. v.mul(M,v) does not comutes right.
        
        // note: no 'auto-grow' policy.
        
        final int v1Size = v1.getSize();
        final int cols = m.getNumCols();
        final int rows = m.getNumRows();
        
        if ( v1Size != rows )
            throw new IllegalArgumentException( "v1.size:" + v1Size + " != m1.nRow:" + rows );
        if ( this.getSize() != cols )
            throw new IllegalArgumentException( "this.size:" + this.getSize() + " != m1.nCol:" + cols );
        
        for ( int i = 0; i < getSize(); i++ )
        {
            float sum = 0.0f;
            for ( int j = 0; j < rows; j++ )
            {
                sum += m.get( j, i ) * v1.getValue( j );
            }
            
            this.setValue( i, sum );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public final VectorNf cross( VectorNf v1, VectorNf v2 )
    {
        final int n = this.getSize();
        
        for ( int i = 0; i < n; i++ )
        {
            this.setValue( i, v1.getValue( ( i + 1 ) % n ) * v2.getValue( ( i + 2 ) % n ) - v1.getValue( ( i + 2 ) % n ) * v2.getValue( ( i + 1 ) % n ) );
        }
        
        return ( this );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float dot( VectorNf v2 )
    {
        if ( this.getSize() != v2.getSize() )
            throw new IllegalArgumentException( "this.size:" + this.getSize() + " != v1.size:" + v2.getSize() );
        
        float sum = 0.0f;
        for ( int i = 0; i < getSize(); ++i )
            sum += this.getValue( i ) * v2.getValue( i );
        
        return ( sum );
    }
    
    /**
     * {@inheritDoc}
     */
    public final float angle( VectorNf v2 )
    {
        return ( FastMath.acos( dot( v2 ) / this.getNorm() / v2.getNorm() ) );
    }
    
    /**
     * Solves for x in Ax = b, where x is this vector (nx1), A is mxn, b is mx1,
     * and A = U*W*transpose(V); U,W,V must be precomputed and can be found by
     * taking the singular value decomposition (SVD) of A using the method SVD
     * found in the GMatrix class.
     * 
     * @param U The U matrix produced by the GMatrix method SVD
     * @param W The W matrix produced by the GMatrix method SVD
     * @param V The V matrix produced by the GMatrix method SVD
     * @param b The b vector in the linear equation Ax = b
     */
    public void SVDBackSolve( MatrixMxNf U, MatrixMxNf W, MatrixMxNf V, VectorNf b)
    {
        if ( this.getSize() != U.getNumRows() || getSize() != U.getNumCols() )
            throw new ArrayIndexOutOfBoundsException( "this.size:" + this.getSize() + " != U.nRow,nCol:" + U.getNumRows() + "," + U.getNumCols() );
        if ( this.getSize() != W.getNumRows() )
            throw new ArrayIndexOutOfBoundsException( "this.size:" + this.getSize() + " != W.nRow:" + W.getNumRows() );
        if ( b.getSize() != W.getNumCols() )
            throw new ArrayIndexOutOfBoundsException( "b.size:" + b.getSize() + " != W.nCol:" + W.getNumCols() );
        if ( b.getSize() != V.getNumRows() || b.getSize() != V.getNumCols())
            throw new ArrayIndexOutOfBoundsException( "b.size:" + this.getSize() + " != V.nRow,nCol:" + V.getNumRows() + "," + V.getNumCols() );
        
        final int m = U.getNumRows(); // this.elementCount
        final int n = V.getNumRows(); // b.elementCount
        final float[] tmp = new float[ n ];
        
        for ( int j = 0; j < n; j++ )
        {
            float s = 0.0f;
            float wj = W.get( j, j );
            if ( wj != 0.0f )
            {
                for ( int i = 0; i < m; i++ )
                    s += U.get( i, j ) * b.getValue( i );
                s /= wj;
            }
            tmp[ j ] = s;
        }
        for ( int j = 0; j < n; j++ )
        {
            float s = 0.0f;
            for ( int jj = 0; jj < n; jj++ )
                s += V.get( j, jj ) * tmp[ jj ];
            this.setValue( j, s );
        }
    }
    
    /**
     * LU Decomposition Back Solve; this method takes the LU matrix and the
     * permutation vector produced by the GMatrix method LUD and solves the
     * equation (LU)*x = b by placing the solution vector x into this vector.
     * This vector should be the same length or longer than b.
     * 
     * @param lu The matrix into which the lower and upper decompositions have
     *            been placed
     * @param b The b vector in the equation (LU)*x = b
     * @param permutation The row permuations that were necessary to produce the
     *            LU matrix parameter
     */
    public void LUDBackSolve( MatrixMxNf lu, VectorNf b, VectorNf permutation )
    {
        // not alias-safe with b and this!
        
        // note: this is from William H. Press et.al Numerical Recipes in C.
        // hiranabe modified 1-n indexing to 0-(n-1).
        // I fixed some bugs in NRC, which are bad permutation handling.
        
        if ( this.getSize() != b.getSize() )
            throw new ArrayIndexOutOfBoundsException( "this.size:" + this.getSize() + " != b.size:" + b.getSize() );
        if ( this.getSize() != lu.getNumRows() )
            throw new ArrayIndexOutOfBoundsException( "this.size:" + this.getSize() + " != LU.nRow:" + lu.getNumRows() );
        if ( this.getSize() != lu.getNumCols() )
            throw new ArrayIndexOutOfBoundsException( "this.size:" + this.getSize() + " != LU.nCol:" + lu.getNumCols() );
        
        final int n = this.getSize();
        
        /* make permutated b (b'=Pb) */
        for ( int i = 0; i < n; i++ )
        {
            // not alias-safe!
            this.setValue( i, b.getValue( (int)permutation.getValue( i ) ) );
        }
        
        /* forward substitution Ux' = b' */
        int ii = -1;
        for ( int i = 0; i < n; i++ )
        {
            float sum = this.getValue( i );
            if ( 0 <= ii )
            {
                for ( int j = ii; j <= i - 1; j++ )
                    sum -= lu.get( i, j ) * this.getValue( j );
            }
            else if ( sum != 0.0f )
            {
                /* found the first non-zero x */
                ii = i;
            }
            this.setValue( i, sum );
        }
        
        /* backward substitution, solve x' */
        for ( int i = n - 1; i >= 0; i-- )
        {
            float sum = this.getValue( i );
            for ( int j = i + 1; j < n; j++ )
                sum -= lu.get( i, j ) * this.getValue( j );
            
            // zero-div may occur
            this.setValue( i, sum / lu.get( i, i ) );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VectorNf asReadOnly()
    {
        return ( new VectorNf( true, this.values, this.isDirty, false ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VectorNf getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone()
    {
        return ( new VectorNf( this ) );
    }
    
    
    /**
     * Constructs a new generalized mathematic Vector with zero elements; length
     * reprents the number of elements in the vector.
     * 
     * @param readOnly
     * @param length number of elements in this vector.
     */
    protected VectorNf( boolean readOnly, int length )
    {
        super( readOnly, length );
    }
    
    /**
     * Constructs a new generalized mathematic Vector with zero elements; length
     * reprents the number of elements in the vector. !! this comment is a bug
     * in Sun's API !!
     * 
     * @param readOnly
     * @param values the values for the new vector.
     * @param isDirty
     * @param copy
     */
    protected VectorNf( boolean readOnly, float[] values, boolean[] isDirty, boolean copy )
    {
        super( readOnly, values, isDirty, values.length, copy );
    }
    
    /**
     * Constructs a new Vector by copying length elements from the array
     * parameter. The parameter length must be less than or equal to
     * vector.length.
     * 
     * @param readOnly
     * @param values The array from which the values will be copied.
     * @param start the start index to begin copying at
     * @param length The number of values copied from the array.
     */
    protected VectorNf( boolean readOnly, float[] values, int start, int length )
    {
        // ArrayIndexOutOfBounds occurs if length > vector.length
        this( readOnly, length );
        
        System.arraycopy( values, start, this.values, 0, length );
    }
    
    /**
     * Constructs a new Vector and copies the initial values from the parameter vector.
     * 
     * @param readOnly
     * @param vector the source for the new Vector's initial values
     */
    protected VectorNf( boolean readOnly, TupleNf< ? > vector )
    {
        super( readOnly, vector );
    }
    
    /**
     * Constructs a new generalized mathematic Vector with zero elements; length
     * reprents the number of elements in the vector.
     * 
     * @param length number of elements in this vector.
     */
    public VectorNf( int length )
    {
        this( false, length );
    }
    
    /**
     * Constructs a new generalized mathematic Vector with zero elements; length
     * reprents the number of elements in the vector. !! this comment is a bug
     * in Sun's API !!
     * 
     * @param values the values for the new vector.
     */
    public VectorNf( float[] values )
    {
        this( false, values, null, true );
    }
    
    /**
     * Constructs a new Vector by copying length elements from the array
     * parameter. The parameter length must be less than or equal to
     * vector.length.
     * 
     * @param values The array from which the values will be copied.
     * @param start the start index to begin copying at
     * @param length The number of values copied from the array.
     */
    public VectorNf( float[] values, int start, int length )
    {
        this( false, values, start, length );
    }
    
    /**
     * Constructs a new Vector and copies the initial values from the parameter vector.
     * 
     * @param vector the source for the new Vector's initial values
     */
    public VectorNf( TupleNf< ? > vector )
    {
        this( false, vector );
    }
    
    /**
     * Constructs a new generalized mathematic Vector with zero elements; length
     * reprents the number of elements in the vector.
     * 
     * @param length number of elements in this vector.
     */
    public static VectorNf newReadOnly( int length )
    {
        return ( new VectorNf( true, length ) );
    }
    
    /**
     * Constructs a new generalized mathematic Vector with zero elements; length
     * reprents the number of elements in the vector. !! this comment is a bug
     * in Sun's API !!
     * 
     * @param values the values for the new vector.
     */
    public static VectorNf newReadOnly( float[] values )
    {
        return ( new VectorNf( true, values, null, true ) );
    }
    
    /**
     * Constructs a new Vector by copying length elements from the array
     * parameter. The parameter length must be less than or equal to
     * vector.length.
     * 
     * @param values The array from which the values will be copied.
     * @param start the start index to begin copying at
     * @param length The number of values copied from the array.
     */
    public static VectorNf newReadOnly( float[] values, int start, int length )
    {
        return ( new VectorNf( true, values, start, length ) );
    }
    
    /**
     * Constructs a new Vector and copies the initial values from the parameter vector.
     * 
     * @param vector the source for the new Vector's initial values
     */
    public static VectorNf newReadOnly( TupleNf< ? > vector )
    {
        return ( new VectorNf( true, vector ) );
    }
}
