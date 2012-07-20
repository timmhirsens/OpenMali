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
package org.openmali.number.matrix;

import java.io.Serializable;

import org.openmali.number.Radical1;
import org.openmali.number.Rational;
import org.openmali.vecmath2.MatrixMxNf;

/**
 * A general matrix implementing basic radical algebra. The implementation is aimed for applications where trigonomic
 * constants are applicable, where exact arithmatic can be performed and is useful, and where speed is not an issue.
 * 
 * @author Tom Larkworthy
 */
public class MatrixMxNrad implements Serializable, Cloneable
{
    private static final long serialVersionUID = 5363921506759370621L;
    
    /**
     * The data of the Matrix.
     * (1D array. The (i, j) element is stored in elementData[ i * col + j ])
     */
    protected final Radical1[] values;
    
    /**
     * The number of rows in this matrix.
     */
    private final int rows;
    
    /**
     * The number of columns in this matrix.
     */
    private final int cols;
    
    /**
     * A matrix might be part of another, larger matrix.
     * Because we don't want to copy changes on the backing-matrix everytime to the submatrix,
     * they share the same data-array.<br>
     * 
     * <code>dataBegin</code> describes, where the data of this matrix begins.<br>
     * For normal use this is 0.
     */
    protected final int dataBegin;
    
    /**
     * A matrix might be part of another, larger matrix.
     * Because we don't want to copy changes on the backing-matrix everytime to the submatrix,
     * they share the same data-array.<br>
     * 
     * <code>colskip</code> describes, how many columns the submatrix has, so we really are able to simulate the submatrix.<br>
     * For normal use this is equal to {@link #cols}.
     */
    protected final int colSkip;
    
    /**
     * This is used to costlessly make the Matrix read-only.
     * In case of a read-only Matrix this value will be the negative ten-th of values.length.
     * This will cause an ArrayIndexOutOfBoundsException when a read-only Matrix gets manipulated.
     */
    private final int roTrick;
    
    private boolean isDirty = false;
    
    /**
     * @return number of rows in this matrix
     */
    public final int getNumRows()
    {
        return ( rows );
    }
    
    /**
     * @return number of columns in this matrix
     */
    public final int getNumCols()
    {
        return ( cols );
    }
    
    /**
     * @return Is this Matrix a square-Matrix?
     */
    public final boolean isSquare()
    {
        return ( getNumRows() == getNumCols() );
    }
    
    /**
     * @return Is this Matrix a read-only-Matrix?
     */
    public final boolean isReadOnly()
    {
        return ( roTrick != 0 );
    }
    
    /**
     * Marks this Matrix non-dirty.
     * Any value-manipulation will mark it dirty again.
     *
     * @return the old value
     */
    public final boolean setClean()
    {
        final boolean oldValue = this.isDirty;
        
        this.isDirty = false;
        
        return ( oldValue );
    }
    
    /**
     * @return This Matrix' dirty-flag
     */
    public final boolean isDirty()
    {
        return ( isDirty );
    }
    
    /**
     * @return <code>true</code>, if this Matrix is backed by another (usually larger) matrix.
     */
    public final boolean isSubMatrix()
    {
        return ( ( dataBegin != 0 ) || ( colSkip != cols ) );
    }
    
    /**
     * Modifies the value at the specified row and column of this matrix.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param column the column number to be modified (zero indexed)
     * @param value the new matrix element value
     */
    public final void set( int row, int column, Radical1 value )
    {
        //assert ( ( 0 <= row ) && ( row < getNumRows() ) && ( 0 <= column ) && ( column < getNumCols() ) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols );
        */
        
        this.values[ roTrick + dataBegin + row * colSkip + column ].set( value );
        
        this.isDirty = true;
    }
    
    /**
     * sets the underlying reference of this matrix. Note you can seriously mess up an
     * object instance by using this. Consider the object read only for saftey.
     * 
     * @param row
     * @param column
     * @param ref
     */
    public void setReference( int row, int column, Radical1 ref )
    {
        this.values[ roTrick + dataBegin + row * colSkip + column ] = ref;
    }
    
    /**
     * Retrieves the value at the specified row and column of this matrix.
     * 
     * @param row the row number to be retrieved (zero indexed)
     * @param column the column number to be retrieved (zero indexed)
     * @return the value at the indexed element
     */
    public final Radical1 get( int row, int column, Radical1 passback )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols );
        */
        
        values[ dataBegin + row * colSkip + column ].get( passback );
        
        return ( passback );
    }
    
    public final Radical1 getReference( int row, int column )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols );
        */

        return values[ dataBegin + row * colSkip + column ];
    }
    
    public void set( MatrixMxNrad values )
    {
        Radical1 tmp = new Radical1();
        for ( int i = 0; i < rows; i++ )
        {
            for ( int j = 0; j < rows; j++ )
            {
                this.set( i, j, values.get( i, j, tmp ) );
            }
        }
    }
    
    /**
     * Places the values of the specified row into the vector parameter.
     * 
     * @param row the target row number
     * @param tuple the vector into which the row values will be placed
     */
    public final void getRow( int row, TupleNrad<?> tuple )
    {
        Radical1 tmp = new Radical1();
        
        for ( int i = 0; i < cols; i++ )
        {
            tuple.set( i, this.get( row, i, tmp ) );
        }
    }
    
    /**
     * Copy the values from the array into the specified column of this matrix.
     * 
     * @param col the column of this matrix into which the vector values will be copied.
     * @param tuple the source vector
     */
    public final void setColumn( int col, TupleNrad<?> tuple )
    {
        Radical1 tmp = new Radical1();
        for ( int i = 0; i < rows; i++ )
            this.set( i, col, tuple.get( i, tmp ) );
        
        this.isDirty = true;
    }
    
    /**
     * Places the values of the specified column into the vector parameter.
     * 
     * @param col the target column number
     * @param tuple the vector into which the column values will be placed
     */
    public final void getColumn( int col, TupleNrad<?> tuple )
    {
        Radical1 tmp = new Radical1();
        /*
        if ( cols <= col )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " > matrix's nCol:" + cols );
        if ( col < 0 )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " < 0" );
        if ( vector.getSize() < rows )
            throw new ArrayIndexOutOfBoundsException( "vector size:" + vector.getSize() + " < matrix's nRow:" + rows );
        */

        for ( int i = 0; i < rows; i++ )
            tuple.set( i, this.get( i, col, tmp ) );
    }
    
    /**
     * Sets all the values in this matrix to zero.
     */
    public final void setZero()
    {
        Radical1 zero = new Radical1();
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                set( r, c, zero );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Sets this GMatrix to the identity matrix.
     */
    public final void setIdentity()
    {
        setZero();
        Radical1 one = new Radical1( 1 );
        
        final int min = rows < cols ? rows : cols;
        for ( int i = 0; i < min; i++ )
            this.set( i, i, one );
        
        this.isDirty = true;
    }
    
    /**
     * Negates the value of this matrix: this = -this.
     */
    public final void negate()
    {
        Radical1 tmp = new Radical1();
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                set( r, c, get( r, c, tmp ).negate( tmp ) );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Sets the value of this matrix to the negation of the Matrix parameter.
     * 
     * @param m The source matrix
     */
    public final void negate( MatrixMxNrad m )
    {
        set( m );
        negate();
        
        this.isDirty = true;
    }
    
    /**
     * Transposes this matrix in place.
     */
    public void transpose()
    {
        Radical1 tmp = new Radical1();
        Radical1 tmp2 = new Radical1();
        for ( int i = 0; i < rows; i++ )
        {
            for ( int j = i + 1; j < cols; j++ ) // note j starts at (i + 1)
            {
                this.get( i, j, tmp );
                
                this.set( i, j, this.get( j, i, tmp2 ) );
                this.set( j, i, tmp );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Places the matrix values of the transpose of matrix mat into this matrix.
     * 
     * @param mat the matrix to be transposed (but not modified)
     */
    public void transpose( MatrixMxNrad mat )
    {
        set( mat );
        
        transpose();
        
        this.isDirty = true;
    }
    
    /**
     * Copies a sub-matrix derived from this matrix into the target matrix. The
     * upper left of the sub-matrix is located at (rowSource, colSource); the
     * lower right of the sub-matrix is located at
     * (lastRowSource,lastColSource). The sub-matrix is copied into the the
     * target matrix starting at (rowDest, colDest).
     * 
     * @param rowSource the top-most row of the sub-matrix
     * @param colSource the left-most column of the sub-matrix
     * @param numRows the number of rows in the sub-matrix
     * @param numCols the number of columns in the sub-matrix
     * @param rowDest the top-most row of the position of the copied sub-matrix
     *            within the target matrix
     * @param colDest the left-most column of the position of the copied
     *            sub-matrix within the target matrix
     * @param target the matrix into which the sub-matrix will be copied
     */
    public final void copySubMatrix( int rowSource, int colSource, int numRows, int numCols, int rowDest, int colDest, MatrixMxNrad target )
    {
        Radical1 tmp = new Radical1();
        if ( rowSource < 0 || colSource < 0 || rowDest < 0 || colDest < 0 )
            throw new ArrayIndexOutOfBoundsException( "rowSource, colSource, rowDest, colDest < 0." );
        if ( rows < numRows + rowSource || cols < numCols + colSource )
            throw new ArrayIndexOutOfBoundsException( "Source Matrix too small." );
        if ( target.rows < numRows + rowDest || target.cols < numCols + colDest )
            throw new ArrayIndexOutOfBoundsException( "Target Matrix too small." );
        
        for ( int i = 0; i < numRows; i++ )
            for ( int j = 0; j < numCols; j++ )
                target.set( ( i + rowDest ), ( j + colDest ), this.get( ( i + rowSource ), ( j + colSource ), tmp ) );
    }
    
    /**
     * Adds a scalar to this Matrix.
     * 
     * @param scalar
     */
    public final void add( float scalar )
    {
        Radical1 value = new Radical1();
        value.addTerm( new Rational( scalar ), 1 );
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                Radical1.add( values[ roTrick + dataBegin + r * colSkip + c ], value, values[ roTrick + dataBegin + r * colSkip + c ] );
                
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Sets the value of this matrix to sum of itself and matrix m2.
     * 
     * @param m2 the other matrix
     */
    public final void add( MatrixMxNrad m2 )
    {
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m2.rows + "x" + m2.cols + ")." );
        
        Radical1 tmp = new Radical1();
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                Radical1.add( values[ roTrick + dataBegin + r * colSkip + c ], m2.get( r, c, tmp ), values[ roTrick + dataBegin + r * colSkip + c ] );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public final void add( MatrixMxNrad m1, MatrixMxNrad m2 )
    {
        if ( rows != m1.rows || cols != m1.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m1.rows + "x" + m1.cols + ")." );
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m2:(" + m2.rows + "x" + m2.cols + ")." );
        
        Radical1 tmp1 = new Radical1();
        Radical1 tmp2 = new Radical1();
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                Radical1.add( m1.get( r, c, tmp1 ), m2.get( r, c, tmp2 ), values[ roTrick + dataBegin + r * colSkip + c ] );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Subtracts a scalar from this Matrix.
     * 
     * @param scalar
     */
    public final void sub( float scalar )
    {
        add( -scalar );
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of itself and
     * matrix m2 (this = this - m2).
     * 
     * @param m2 the other matrix
     */
    public final void sub( MatrixMxNrad m2 )
    {
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m2.rows + "x" + m2.cols + ")." );
        
        Radical1 tmp = new Radical1();
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                Radical1.sub( values[ roTrick + dataBegin + r * colSkip + c ], m2.get( r, c, tmp ), values[ roTrick + dataBegin + r * colSkip + c ] );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of matrices m1 and
     * m2 (this = m1 - m2).
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public final void sub( MatrixMxNrad m1, MatrixMxNrad m2 )
    {
        if ( rows != m1.rows || cols != m1.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m1.rows + "x" + m1.cols + ")." );
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m2:(" + m2.rows + "x" + m2.cols + ")." );
        Radical1 tmp1 = new Radical1();
        Radical1 tmp2 = new Radical1();
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                Radical1.sub( m1.get( r, c, tmp1 ), m2.get( r, c, tmp2 ), values[ roTrick + dataBegin + r * colSkip + c ] );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Multiplies this Matrix with a scalar.
     * 
     * @param scalar
     */
    public final void mul( float scalar )
    {
        Radical1 operand = new Radical1();
        operand.addTerm( new Rational( scalar ), 1 );
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                Radical1.mul( values[ roTrick + dataBegin + r * colSkip + c ], operand, values[ roTrick + dataBegin + r * colSkip + c ] );
            }
        }
        
        this.isDirty = true;
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying the two
     * argument matrices together (this = m1 * m2).
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     */
    public void mul( MatrixMxNrad m1, MatrixMxNrad m2 )
    {
        // for alias-safety, decided to new float[ cols* rows ].
        // Is there any good way to avoid this big new ?
        if ( rows != m1.rows )
            throw new ArrayIndexOutOfBoundsException( "rows:" + rows + " != m1.rows:" + m1.rows );
        if ( cols != m2.cols )
            throw new ArrayIndexOutOfBoundsException( "cols:" + cols + " != m2.cols:" + m2.cols );
        if ( m1.cols != m2.rows )
            throw new ArrayIndexOutOfBoundsException( "m1.cols:" + m1.cols + " != m2.rows:" + m2.rows );
        
        Radical1 tmp3 = new Radical1();
        MatrixMxNrad newData = new MatrixMxNrad( rows, cols );
        
        for ( int i = 0; i < rows; i++ )
        {
            for ( int j = 0; j < cols; j++ )
            {
                Radical1 sum = new Radical1();
                for ( int k = 0; k < m1.cols; k++ )
                {
                    Radical1.mul( m1.getReference( i, k ), m2.getReference( k, j ), tmp3 );
                    Radical1.add( sum, tmp3, sum );
                }
                newData.set( i, j, sum );
                ;
            }
        }
        
        set( newData );
        
        this.isDirty = true;
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying itself with
     * matrix m1 (this = this * m1).
     * 
     * @param gmat the other matrix
     */
    public void mul( MatrixMxNrad gmat )
    {
        // alias-safe.
        mul( this, gmat );
        
        this.isDirty = true;
    }
    
    /**
     * Computes the outer product of the two vectors; multiplies the the first
     * vector by the transpose of the second vector and places the matrix result
     * into this matrix. This matrix must be as big or bigger than
     * getSize(v1) x getSize(v2).
     * 
     * @param v1 the first vector, treated as a row vector
     * @param v2 the second vector, treated as a column vector
     */
    public void mul( TupleNrad<?> v1, TupleNrad<?> v2 )
    {
        if ( rows < v1.getSize() )
            throw new IllegalArgumentException( "rows:" + rows + " < v1.getSize():" + v1.getSize() );
        if ( cols < v2.getSize() )
            throw new IllegalArgumentException( "cols:" + cols + " < v2.getSize():" + v2.getSize() );
        
        Radical1 tmp1 = new Radical1();
        Radical1 tmp2 = new Radical1();
        Radical1 tmp3 = new Radical1();
        
        for ( int i = 0; i < rows; i++ )
            for ( int j = 0; j < cols; j++ )
                this.set( i, j, Radical1.mul( v1.get( i, tmp1 ), v2.get( j, tmp2 ), tmp3 ) );
        
        this.isDirty = true;
    }
    
    /**
     * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
     * and places the result into this.
     * 
     * @param m1 The matrix on the left hand side of the multiplication
     * @param m2 The matrix on the right hand side of the multiplication
     */
    public void mulTransposeBoth( MatrixMxNrad m1, MatrixMxNrad m2 )
    {
        mul( m2, m1 );
        
        transpose();
        
        this.isDirty = true;
    }
    
    /**
     * Multiplies the transpose of matrix m1 times the matrix m2, and places the
     * result into this.
     * 
     * @param m1 The matrix on the left hand side of the multiplication
     * @param m2 The matrix on the right hand side of the multiplication
     */
    public void mulTransposeLeft( MatrixMxNrad m1, MatrixMxNrad m2 )
    {
        transpose( m1 );
        
        mul( m2 );
        
        this.isDirty = true;
    }
    
    /*
    private final void setDiag( int i, Radical1 value )
    {
        this.values[ roTrick + dataBegin + i * colSkip + i ] = value.clone();
    }
    
    private final Radical1 getDiag( int i, Radical1 passback )
    {
        return this.get( i, i, passback );
    }
    
    private final float dpythag( float a, float b )
    {
        float absa = Math.abs( a );
        float absb = Math.abs( b );
        if ( absa > absb )
        {
            if ( absa == 0.0f )
                return ( 0.0f );
            float term = absb / absa;
            if ( Math.abs( term ) <= Float.MIN_VALUE )
                return ( absa );
            return ( absa * FastMath.sqrt( 1.0f + term * term ) );
        }
        else
        {
            if ( absb == 0.0f )
                return ( 0.0f );
            float term = absa / absb;
            if ( Math.abs( term ) <= Float.MIN_VALUE )
                return ( absb );
            
            return ( absb * FastMath.sqrt( 1.0f + term * term ) );
        }
    }
    
    private final void swapRows( int i, int j )
    {
        Radical1 tmp1 = new Radical1();
        Radical1 tmp2 = new Radical1();
        
        for ( int k = 0; k < cols; k++ )
        {
            get( i, k, tmp1 );
            get( j, k, tmp2 );
            set( i, k, tmp2 );
            set( j, k, tmp1 );
        }
    }
    */
    
    /**
     * Returns a hash number based on the data values in this object. Two
     * different GMatrix objects with identical data values (ie, returns true
     * for equals(GMatrix) ) will return the same hash number. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash value
     */
    @Override
    public int hashCode()
    {
        Radical1 tmp = new Radical1();
        int hash = 0;
        for ( int r = 0; r < rows; r++ )
        {
            for ( int c = 0; c < cols; c++ )
            {
                int bits = get( r, c, tmp ).hashCode();
                hash ^= ( bits ^ ( bits >> 32 ) );
            }
        }
        
        return ( hash );
    }
    
    /**
     * Returns true if all of the data members of Matrix4d m1 are equal to the
     * corresponding data members in this Matrix4d.
     * 
     * @param mat2 The matrix with which the comparison is made.
     * @return true or false
     */
    public boolean equals( MatrixMxNrad mat2 )
    {
        if ( mat2 == null )
            return ( false );
        if ( mat2.rows != rows )
            return ( false );
        if ( mat2.cols != cols )
            return ( false );
        for ( int i = 0; i < rows; i++ )
            for ( int j = 0; j < cols; j++ )
                if ( !this.getReference( i, j ).equals( mat2.getReference( i, j ) ) )
                    return ( false );
        
        return ( true );
    }
    
    /**
     * Returns true if the Object o1 is of type GMatrix and all of the data
     * members of t1 are equal to the corresponding data members in this
     * Matrix.
     * 
     * @param o the object with which the comparison is made.
     */
    @Override
    public boolean equals( Object o )
    {
        return ( ( o != null ) && ( ( o instanceof MatrixMxNrad ) && equals( (MatrixMxNrad)o ) ) );
    }
    
    private final StringBuffer tmpSB = new StringBuffer();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        Radical1 tmp = new Radical1();
        
        final String nl = System.getProperty( "line.separator" );
        
        tmpSB.setLength( 0 );
        
        tmpSB.append( "[" );
        tmpSB.append( nl );
        
        for ( int i = 0; i < rows; i++ )
        {
            tmpSB.append( "  [" );
            for ( int j = 0; j < cols; j++ )
            {
                if ( 0 < j )
                    tmpSB.append( "\t" );
                tmpSB.append( this.get( i, j, tmp ) );
            }
            if ( i + 1 < rows )
            {
                tmpSB.append( "]" );
                tmpSB.append( nl );
            }
            else
            {
                tmpSB.append( "] ]" );
            }
        }
        
        return ( tmpSB.toString() );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MatrixMxNrad clone()
    {
        MatrixMxNrad obj = new MatrixMxNrad( rows, cols );
        
        obj.set( this );
        
        return ( obj );
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
     * @param readOnly
     * @param beginRow the row to start this matrix at
     * @param beginCol the column to start this matrix at
     * @param rows
     * @param cols
     * 
     * @return the new shared submatrix
     *
     */
    public MatrixMxNrad getSharedSubMatrix( boolean readOnly, int beginRow, int beginCol, int rows, int cols )
    {
        final int begin = dataBegin + beginCol + beginRow * colSkip; // don't forget that we might operate already on a submatrix!
        // colskip stays same, cause rows stay rows ;)
        return ( new MatrixMxNrad( readOnly, begin, colSkip, rows, cols, values ) );
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
     * @param beginRow the row to start this matrix at
     * @param beginCol the column to start this matrix at
     * @param rows
     * @param cols
     * 
     * @return the new shared submatrix
     */
    public MatrixMxNrad getSharedSubMatrix( int beginRow, int beginCol, int rows, int cols )
    {
        return ( getSharedSubMatrix( false, beginRow, beginCol, rows, cols ) );
    }
    
    /**
     * Constructs an nRow by nCol identity matrix. Note that even though row and
     * column numbering begins with zero, nRow and nCol will be one larger than
     * the maximum possible matrix index values.
     * 
     * @param readOnly
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     */
    protected MatrixMxNrad( boolean readOnly, int rows, int cols )
    {
        if ( rows < 0 )
            throw new NegativeArraySizeException( rows + " < 0" );
        if ( cols < 0 )
            throw new NegativeArraySizeException( cols + " < 0" );
        
        this.rows = rows;
        this.cols = cols;
        this.values = new Radical1[ rows * cols ];
        for ( int i = 0; i < values.length; i++ )
            values[ i ] = new Radical1();
        
        this.dataBegin = 0;
        this.colSkip = cols;
        
        setIdentity();
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * Constructs an nRow by nCol matrix initialized to the values in the matrix
     * array. The array values are copied in one row at a time in row major
     * fashion. The array should be at least nRow*nCol in length. Note that even
     * though row and column numbering begins with zero, nRow and nCol will be
     * one larger than the maximum possible matrix index values.
     * 
     * @param readOnly
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     * @param values a 1D array that specifies a matrix in row major fashion
     */
    protected MatrixMxNrad( boolean readOnly, int rows, int cols, Radical1[] values )
    {
        if ( rows < 0 )
            throw new NegativeArraySizeException( rows + " < 0" );
        if ( cols < 0 )
            throw new NegativeArraySizeException( cols + " < 0" );
        
        this.rows = rows;
        this.cols = cols;
        this.values = new Radical1[ rows * cols ];
        for ( int i = 0; i < values.length; i++ )
            this.values[ i ] = values[ i ].clone();
        
        this.dataBegin = 0;
        this.colSkip = cols;
        
        final int size = rows * cols;
        System.arraycopy( values, 0, this.values, 0, size );
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * Constructs a new GMatrix and copies the initial values from the parameter
     * matrix.
     * 
     * @param readOnly
     * @param matrix the source of the initial values of the new GMatrix
     */
    protected MatrixMxNrad( boolean readOnly, MatrixMxNrad matrix )
    {
        this.rows = matrix.rows;
        this.cols = matrix.cols;
        this.dataBegin = 0;
        this.colSkip = cols;
        this.values = new Radical1[ rows * cols ];
        for ( int i = 0; i < values.length; i++ )
            values[ i ] = new Radical1();
        
        set( matrix );
        //System.arraycopy( matrix.values, 0, this.values, 0, this.values.length );
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * @param readOnly
     * @param dataBegin
     * @param colskip
     * @param values
     * 
     * @see #getSharedSubMatrix(int, int, int, int)
     * @see MatrixMxNf#MatrixMxNf(int, int, int, int, float[])
     */
    protected MatrixMxNrad( boolean readOnly, int dataBegin, int colskip, int rows, int cols, Radical1[] values )
    {
        this.dataBegin = dataBegin;
        this.colSkip = colskip;
        this.cols = cols;
        this.rows = rows;
        this.values = values;
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
    }
    
    /**
     * Constructs an nRow by nCol identity matrix. Note that even though row and
     * column numbering begins with zero, nRow and nCol will be one larger than
     * the maximum possible matrix index values.
     * 
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     */
    public MatrixMxNrad( int rows, int cols )
    {
        this( false, rows, cols );
    }
    
    /**
     * Constructs an nRow by nCol matrix initialized to the values in the matrix
     * array. The array values are copied in one row at a time in row major
     * fashion. The array should be at least nRow*nCol in length. Note that even
     * though row and column numbering begins with zero, nRow and nCol will be
     * one larger than the maximum possible matrix index values.
     * 
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     * @param values a 1D array that specifies a matrix in row major fashion
     */
    public MatrixMxNrad( int rows, int cols, Radical1[] values )
    {
        this( false, rows, cols, values );
    }
    
    /**
     * Constructs a new GMatrix and copies the initial values from the parameter
     * matrix.
     * 
     * @param matrix the source of the initial values of the new GMatrix
     */
    public MatrixMxNrad( MatrixMxNrad matrix )
    {
        this( false, matrix );
    }
    
    /**
     * @param dataBegin
     * @param colskip
     * @param values
     * 
     * @see #getSharedSubMatrix(int, int, int, int)
     * @see MatrixMxNf#MatrixMxNf(int, int, int, int, float[])
     */
    protected MatrixMxNrad( int dataBegin, int colskip, int rows, int cols, Radical1[] values )
    {
        this( false, dataBegin, colskip, rows, cols, values );
    }
    
    /**
     * Constructs an nRow by nCol identity matrix. Note that even though row and
     * column numbering begins with zero, nRow and nCol will be one larger than
     * the maximum possible matrix index values.
     * 
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     */
    public static MatrixMxNrad newReadOnly( int rows, int cols )
    {
        return ( new MatrixMxNrad( true, rows, cols ) );
    }
    
    /**
     * Constructs an nRow by nCol matrix initialized to the values in the matrix
     * array. The array values are copied in one row at a time in row major
     * fashion. The array should be at least nRow*nCol in length. Note that even
     * though row and column numbering begins with zero, nRow and nCol will be
     * one larger than the maximum possible matrix index values.
     * 
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     * @param values a 1D array that specifies a matrix in row major fashion
     */
    public static MatrixMxNrad newReadOnly( int rows, int cols, Radical1[] values )
    {
        return ( new MatrixMxNrad( true, rows, cols, values ) );
    }
    
    /**
     * Constructs a new GMatrix and copies the initial values from the parameter
     * matrix.
     * 
     * @param matrix the source of the initial values of the new GMatrix
     */
    public static MatrixMxNrad newReadOnly( MatrixMxNrad matrix )
    {
        return ( new MatrixMxNrad( true, matrix ) );
    }
    
    /**
     * creates a shared Submatrix of mat. Does the same as {@link #getSharedSubMatrix(int, int, int, int)}
     * @see #getSharedSubMatrix(int, int, int, int)
     */
    public static MatrixMxNf sharedSubMatrixMxNrad( MatrixMxNf mat, int beginRow, int beginCol, int rows, int cols, boolean readOnly )
    {
        return ( mat.getSharedSubMatrix( readOnly, beginRow, beginCol, rows, cols ) );
    }
    
    /**
     * creates a shared Submatrix of mat. Does the same as {@link #getSharedSubMatrix(int, int, int, int)}
     * @see #getSharedSubMatrix(int, int, int, int)
     */
    public static MatrixMxNrad sharedSubMatrixMxNrad( MatrixMxNrad mat, int beginRow, int beginCol, int rows, int cols )
    {
        return ( mat.getSharedSubMatrix( beginRow, beginCol, rows, cols ) );
    }
}
