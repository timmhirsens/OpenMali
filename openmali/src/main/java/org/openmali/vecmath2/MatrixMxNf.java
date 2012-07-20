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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

import org.openmali.FastMath;
import org.openmali.decomposition.*;
import org.openmali.vecmath2.util.SerializationUtils;
import org.openmali.vecmath2.util.VecMathUtils;

/**
 * A float precision, general, real, two dimensional m x n matrix class.
 * Row and column numbering is zero-based.
 * The representation is row major.
 * 
 * Inspired by Kenji Hiranabe's GMatrix implementation
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Arne Mueller
 */
public class MatrixMxNf implements Cloneable
{
    /**
     * The data of the Matrix.
     * (1D array. The (i, j) element is stored in elementData[ i * col + j ])
     */
    protected final float[] values;
    
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
    protected final int roTrick;
    
    /*
     * This boolean is implemented as a one-elemental array
     * to be able to share its value with a read-only instance.
     */
    protected final boolean[] isDirty;
    
    private MatrixMxNf readOnlyInstance = null;
    
    private MatrixMxNf TEMP_MAT = null;
    private VectorNf TEMP_VEC1 = null;
    private VectorNf TEMP_VEC2 = null;
    private VectorNf TEMP_VEC3 = null;
    
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
        if ( isReadOnly() )
            throw new Error( "This instance is read-only." );
        
        final boolean oldValue = this.isDirty[ 0 ];
        
        this.isDirty[ 0 ] = false;
        
        return ( oldValue );
    }
    
    /**
     * @return This Matrix' dirty-flag
     */
    public final boolean isDirty()
    {
        return ( isDirty[ 0 ] );
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
     * 
     * @return itself
     */
    public final MatrixMxNf set( int row, int column, float value )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" ) );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols ) );
        */
        
        this.values[ roTrick + dataBegin + row * colSkip + column ] = value;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    protected final MatrixMxNf set( int row, int colSkip, int column, float value )
    {
        this.values[ roTrick + dataBegin + row * colSkip + column ] = value;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Retrieves the value at the specified row and column of this matrix.
     * 
     * @param row the row number to be retrieved (zero indexed)
     * @param column the column number to be retrieved (zero indexed)
     * @return the value at the indexed element
     */
    public final float get( int row, int column )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" ) );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols ) );
        */
        
        return ( values[ dataBegin + row * colSkip + column ] );
    }
    
    protected final float get( int row, int colSkip, int column )
    {
        return ( values[ dataBegin + row * colSkip + column ] );
    }
    
    /**
     * Adds the given summand to the element identified by row and column.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param column the column number to be modified (zero indexed)
     * @param summand the value to add
     * 
     * @return itself
     */
    public final MatrixMxNf add( int row, int column, float summand )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" ) );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols ) );
        */
        
        this.values[ roTrick + dataBegin + row * colSkip + column ] += summand;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts the given summand from the element identified by row and column.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param column the column number to be modified (zero indexed)
     * @param value the value to subtract
     * 
     * @return itself
     */
    public final MatrixMxNf sub( int row, int column, float value )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" ) );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols ) );
        */
        
        this.values[ roTrick + dataBegin + row * colSkip + column ] -= value;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies the element identified by row and column with the given factor.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param column the column number to be modified (zero indexed)
     * @param factor the factor to multiply by
     * 
     * @return itself
     */
    public final MatrixMxNf mul( int row, int column, float factor )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" ) );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols ) );
        */
        
        this.values[ roTrick + dataBegin + row * colSkip + column ] *= factor;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Devides the element identified by row and column with the given divisor.
     * 
     * @param row the row number to be modified (zero indexed)
     * @param column the column number to be modified (zero indexed)
     * @param divisor the factor to multiply by
     * 
     * @return itself
     */
    public final MatrixMxNf div( int row, int column, float divisor )
    {
        //assert ( (0 <= row) && (row < getNumRows()) && (0 <= column) && (column < getNumCols()) );
        
        /*
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( column < 0 )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " < 0" ) );
        if ( cols <= column )
            throw new ArrayIndexOutOfBoundsException( "column:" + column + " > matrix's nCol:" + cols ) );
        */
        
        this.values[ roTrick + dataBegin + row * colSkip + column ] /= divisor;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Copies the values from the array into the specified row of this matrix.
     * 
     * @param row the row of this matrix into which the array values will be copied.
     * @param values the source array
     * 
     * @return itself
     */
    public final MatrixMxNf setRow( int row, float[] values )
    {
        /*
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( values.length < cols )
            throw new ArrayIndexOutOfBoundsException( "array length:" + values.length + " < matrix's nCol=" + cols ) );
        */
        
        System.arraycopy( values, 0, this.values, roTrick + dataBegin + row * colSkip, this.cols );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Copies the values from the array into the specified row of this matrix.
     * 
     * @param row the row of this matrix into which the vector values will be copied.
     * @param tuple the source vector
     * 
     * @return itself
     */
    public final MatrixMxNf setRow( int row, TupleNf< ? > tuple )
    {
        /*
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        
        final int vecSize = vector.getSize();
		
        if ( vecSize < cols )
            throw new ArrayIndexOutOfBoundsException( "vector's size:" + vecSize + " < matrix's nCol=" + cols ) );
        */
        
        for ( int i = 0; i < cols; i++ )
        {
            this.set( row, i, tuple.getValue( i ) );
            // if may use package friendly accessibility, would do;
            // System.arraycopy(vector.elementData, 0, elementData, row*nCol, nCol);
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Places the values of the specified row into the array parameter.
     * 
     * @param row the target row number
     * @param buffer the array into which the row values will be placed
     */
    public final void getRow( int row, float[] buffer )
    {
        /*
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( buffer.length < cols )
            throw new ArrayIndexOutOfBoundsException( "array length:" + buffer.length + " smaller than matrix's nCol:" + cols ) );
        */
        
        System.arraycopy( this.values, dataBegin + row * colSkip, buffer, 0, cols );
    }
    
    /**
     * Places the values of the specified row into the vector parameter.
     * 
     * @param row the target row number
     * @param tuple the vector into which the row values will be placed
     */
    public final void getRow( int row, TupleNf< ? > tuple )
    {
        /*
        if ( rows <= row )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " > matrix's nRow:" + rows ) );
        if ( row < 0 )
            throw new ArrayIndexOutOfBoundsException( "row:" + row + " < 0" ) );
        if ( vector.getSize() < cols )
            throw new ArrayIndexOutOfBoundsException( "vector size:" + vector.getSize() + " smaller than matrix's nCol:" + cols ) );
        */
        
        for ( int i = 0; i < cols; i++ )
        {
            tuple.setValue( i, this.get( row, i ) );
        }
    }
    
    /**
     * Copies the values from the array into the specified column of this matrix.
     * 
     * @param col the column of this matrix into which the array values will be copied.
     * @param values the source array
     * 
     * @return itself
     */
    public final MatrixMxNf setColumn( int col, float[] values )
    {
        /*
        if ( cols <= col )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " > matrix's nCol=" + cols ) );
        if ( col < 0 )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " < 0" ) );
		
        if ( values.length < rows )
            throw new ArrayIndexOutOfBoundsException( "array length:" + values.length + " < matrix's nRow:" + rows ) );
        */
        
        for ( int i = 0; i < rows; i++ )
            this.set( i, col, values[ i ] );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Copy the values from the array into the specified column of this matrix.
     * 
     * @param col the column of this matrix into which the vector values will be copied.
     * @param tuple the source vector
     * 
     * @return itself
     */
    public final MatrixMxNf setColumn( int col, TupleNf< ? > tuple )
    {
        /*
        if ( cols <= col )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " > matrix's nCol=" + cols ) );
        if ( col < 0 )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " < 0" ) );
        
        final int vecSize = vector.getSize();
		
        if ( vecSize < rows )
            throw new ArrayIndexOutOfBoundsException( "vector size:" + vecSize + " < matrix's nRow=" + rows ) );
        */
        
        for ( int i = 0; i < rows; i++ )
            this.set( i, col, tuple.getValue( i ) );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Places the values of the specified column into the array parameter.
     * 
     * @param col the target column number
     * @param buffer the array into which the column values will be placed
     */
    public final void getColumn( int col, float[] buffer )
    {
        /*
        if ( cols <= col )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " > matrix's nCol:" + cols ) );
        if ( col < 0 )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " < 0" ) );
        if ( buffer.length < rows )
            throw new ArrayIndexOutOfBoundsException( "array.length:" + buffer.length + " < matrix's nRow=" + rows ) );
        */
        
        for ( int i = 0; i < rows; i++ )
            buffer[ i ] = this.get( i, col );
    }
    
    /**
     * Places the values of the specified column into the vector parameter.
     * 
     * @param col the target column number
     * @param tuple the vector into which the column values will be placed
     */
    public final void getColumn( int col, TupleNf< ? > tuple )
    {
        /*
        if ( cols <= col )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " > matrix's nCol:" + cols ) );
        if ( col < 0 )
            throw new ArrayIndexOutOfBoundsException( "col:" + col + " < 0" ) );
        if ( vector.getSize() < rows )
            throw new ArrayIndexOutOfBoundsException( "vector size:" + vector.getSize() + " < matrix's nRow:" + rows ) );
        */
        
        for ( int i = 0; i < rows; i++ )
            tuple.setValue( i, this.get( i, col ) );
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the row major source array
     * @param offset the offset in the (source) values array
     */
    public final void setRowMajor( final float[] values, int offset )
    {
        if ( !isSubMatrix() )
        {
            System.arraycopy( values, offset, this.values, roTrick + 0, rows * cols );
        }
        else
        {
            for ( int r = 0; r < rows; r++ )
            {
                System.arraycopy( values, offset + r * cols, this.values, roTrick + dataBegin + r * colSkip, cols );
            }
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the row major source array
     */
    public final void setRowMajor( final float[] values )
    {
        setRowMajor( values, 0 );
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the column major source array
     * @param offset the offset in the (source) values array
     */
    public final void setColumnMajor( final float[] values, int offset )
    {
        int i = 0;
        for ( int c = 0; c < getNumCols(); c++ )
        {
            for ( int r = 0; r < getNumRows(); r++ )
            {
                set( r, c, values[ offset + i++ ] );
            }
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the column major source array
     */
    public final void setColumnMajor( final float[] values )
    {
        setColumnMajor( values, 0 );
    }
    
    /**
     * Sets the value of this matrix to that of the Matrix3f provided.
     * 
     * @param mat the source matrix
     */
    public void set( Matrix3f mat )
    {
        // This implementation is in 'no automatic size grow' policy.
        // When size mismatch, exception will be thrown from the below.
        this.values[ roTrick + dataBegin + 0 ] = mat.m00();
        this.values[ roTrick + dataBegin + 1 ] = mat.m01();
        this.values[ roTrick + dataBegin + 2 ] = mat.m02();
        this.values[ roTrick + dataBegin + colSkip + 0 ] = mat.m10();
        this.values[ roTrick + dataBegin + colSkip + 1 ] = mat.m11();
        this.values[ roTrick + dataBegin + colSkip + 2 ] = mat.m12();
        this.values[ roTrick + dataBegin + 2 * colSkip + 0 ] = mat.m20();
        this.values[ roTrick + dataBegin + 2 * colSkip + 1 ] = mat.m21();
        this.values[ roTrick + dataBegin + 2 * colSkip + 2 ] = mat.m22();
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets the value of this matrix to that of the Matrix4f provided.
     * 
     * @param mat the source matrix
     */
    public final void set( Matrix4f mat )
    {
        // This implementation is in 'no automatic size grow' policy.
        // When size mismatch, exception will be thrown from the below.
        this.values[ roTrick + dataBegin + 0 ] = mat.m00();
        this.values[ roTrick + dataBegin + 1 ] = mat.m01();
        this.values[ roTrick + dataBegin + 2 ] = mat.m02();
        this.values[ roTrick + dataBegin + 3 ] = mat.m03();
        this.values[ roTrick + dataBegin + colSkip + 0 ] = mat.m10();
        this.values[ roTrick + dataBegin + colSkip + 1 ] = mat.m11();
        this.values[ roTrick + dataBegin + colSkip + 2 ] = mat.m12();
        this.values[ roTrick + dataBegin + colSkip + 3 ] = mat.m13();
        this.values[ roTrick + dataBegin + 2 * colSkip + 0 ] = mat.m20();
        this.values[ roTrick + dataBegin + 2 * colSkip + 1 ] = mat.m21();
        this.values[ roTrick + dataBegin + 2 * colSkip + 2 ] = mat.m22();
        this.values[ roTrick + dataBegin + 2 * colSkip + 3 ] = mat.m23();
        this.values[ roTrick + dataBegin + 3 * colSkip + 0 ] = mat.m30();
        this.values[ roTrick + dataBegin + 3 * colSkip + 1 ] = mat.m31();
        this.values[ roTrick + dataBegin + 3 * colSkip + 2 ] = mat.m32();
        this.values[ roTrick + dataBegin + 3 * colSkip + 3 ] = mat.m33();
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets the value of this matrix to the values found in matrix mat.
     * 
     * @param mat the source matrix
     */
    public final void set( MatrixMxNf mat )
    {
        if ( ( mat.getNumRows() < getNumRows() ) || ( mat.getNumCols() < getNumCols() ) )
            throw new ArrayIndexOutOfBoundsException( "mat smaller than this matrix" );
        
        if ( !isSubMatrix() )
        {
            System.arraycopy( mat.values, 0, this.values, roTrick + 0, rows * cols );
        }
        else
        {
            for ( int r = 0; r < rows; r++ )
            {
                System.arraycopy( mat.values, mat.dataBegin + r * mat.colSkip, this.values, roTrick + dataBegin + r * colSkip, cols );
            }
        }
        
        this.isDirty[ 0 ] = true;
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the row major source array
     * @param offset the offset in the (target) values array
     */
    public final void getRowMajor( final float[] values, int offset )
    {
        if ( !isSubMatrix() )
        {
            System.arraycopy( this.values, 0, values, offset, rows * cols );
        }
        else
        {
            for ( int r = 0; r < rows; r++ )
            {
                System.arraycopy( this.values, dataBegin + r * colSkip, values, offset + r * cols, cols );
            }
        }
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the row major source array
     */
    public final void getRowMajor( final float[] values )
    {
        getRowMajor( values, 0 );
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the column major source array
     * @param offset the offset in the (target) values array
     */
    public final void getColumnMajor( final float[] values, int offset )
    {
        int i = 0;
        for ( int c = 0; c < getNumCols(); c++ )
        {
            for ( int r = 0; r < getNumRows(); r++ )
            {
                values[ offset + i++ ] = get( r, c );
            }
        }
    }
    
    /**
     * Sets the value of this matrix to the values found in the array parameter.
     * The values are copied in one row at a time, in row major fashion. The
     * array should be at least equal in length to the number of matrix rows
     * times the number of matrix columns in this matrix.
     * 
     * @param values the column major source array
     */
    public final void getColumnMajor( final float[] values )
    {
        getColumnMajor( values, 0 );
    }
    
    /**
     * Places the values in the upper 3x3 of this Matrix into the matrix mat.
     * 
     * @param mat The matrix that will hold the new values
     */
    public void get( Matrix3f mat )
    {
        for ( int i = 0; i < 3; i++ )
            for ( int j = 0; j < 3; j++ )
                mat.set( i, j, get( i, j ) );
    }
    
    /**
     * Places the values in the upper 4x4 of this GMatrix into the matrix mat.
     * 
     * @param mat The matrix that will hold the new values
     */
    public final void get( Matrix4f mat )
    {
        for ( int i = 0; i < 4; i++ )
            for ( int j = 0; j < 4; j++ )
                mat.set( i, j, get( i, j ) );
    }
    
    /**
     * Places the values in the this matrix into the matrix mat;
     * mat should be at least as large as this Matrix.
     * 
     * @param mat The matrix that will hold the new values
     */
    public final void get( MatrixMxNf mat )
    {
        // spec does not completely mirrors set(Matrix).
        // need error check.
        
        if ( mat.rows < rows || mat.cols < cols )
            throw new IllegalArgumentException( "mat matrix is smaller than this matrix." );
        
        if ( mat.colSkip == this.colSkip )
        {
            System.arraycopy( this.values, dataBegin, mat.values, dataBegin, this.getNumRows() * this.getNumCols() );
        }
        else
        {
            for ( int i = 0; i < this.getNumRows(); i++ )
            {
                System.arraycopy( this.values, dataBegin + i * colSkip, mat.values, mat.dataBegin + i * mat.colSkip, this.getNumCols() );
            }
        }
    }
    
    /**
     * Sets this matrix to a uniform scale matrix; all of the values are reset.
     * 
     * @param scale the new scale value
     * 
     * @return itself
     */
    public MatrixMxNf setScale( float scale )
    {
        setZero();
        
        int min = rows < cols ? rows : cols;
        
        for ( int i = 0; i < min; i++ )
            this.values[ roTrick + dataBegin + i * colSkip + i ] = scale;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets all the values in this matrix to zero.
     * 
     * @return itself
     */
    public final MatrixMxNf setZero()
    {
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                set( r, c, 0 );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets this GMatrix to the identity matrix.
     * 
     * @return itself
     */
    public final MatrixMxNf setIdentity()
    {
        setZero();
        
        final int min = rows < cols ? rows : cols;
        for ( int i = 0; i < min; i++ )
            this.set( i, i, 1.0f );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Negates the value of this matrix: this = -this.
     * 
     * @return itself
     */
    public final MatrixMxNf negate()
    {
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                set( r, c, -get( r, c ) );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the negation of the Matrix parameter.
     * 
     * @param m The source matrix
     * 
     * @return itself
     */
    public final MatrixMxNf negate( MatrixMxNf m )
    {
        set( m );
        negate();
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts this matrix from the identity matrix and puts the values back
     * into this (this = I - this).
     * 
     * @return itself
     */
    public final MatrixMxNf identityMinus()
    {
        negate();
        
        int min = rows < cols ? rows : cols;
        for ( int i = 0; i < min; i++ )
            this.values[ roTrick + dataBegin + i * colSkip + i ] += 1.0;
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Inverts this matrix in place.
     * 
     * @return itself
     */
    public MatrixMxNf invert()
    {
        if ( rows != cols )
            throw new ArrayIndexOutOfBoundsException( "not a square matrix" );
        final int n = rows;
        
        if ( TEMP_MAT == null )
            this.TEMP_MAT = new MatrixMxNf( rows, rows );
        if ( TEMP_VEC1 == null )
            this.TEMP_VEC1 = new VectorNf( rows );
        if ( TEMP_VEC2 == null )
            this.TEMP_VEC2 = new VectorNf( rows );
        if ( TEMP_VEC3 == null )
            this.TEMP_VEC3 = new VectorNf( rows );
        
        MatrixMxNf LU = TEMP_MAT;
        VectorNf permutation = TEMP_VEC1;
        VectorNf column = TEMP_VEC2;
        VectorNf unit = TEMP_VEC3;
        LUD( LU, permutation );
        
        for ( int j = 0; j < n; j++ )
        {
            unit.setZero();
            unit.setValue( j, 1.0f );
            column.LUDBackSolve( LU, unit, permutation );
            setColumn( j, column );
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Inverts matrix m and places the new values into this matrix.
     * Matrix m is not modified.
     * 
     * @param m the matrix to be inverted
     * 
     * @return itself
     */
    public MatrixMxNf invert( MatrixMxNf m )
    {
        set( m );
        
        invert();
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Transposes this matrix in place, this can only be done on square matrices. Attempting
     * to call this method on a non-square matrix throws a runtime exception. See the other
     * transpose method for transposing non-square matrices.
     * 
     * @return itself
     */
    public MatrixMxNf transpose()
    {
        if ( !isSquare() )
            throw new RuntimeException( "In-place transpose() only works on square matrices" );
        
        for ( int i = 0; i < rows; i++ )
        {
            for ( int j = i + 1; j < cols; j++ ) // note j starts at (i + 1)
            {
                final float tmp = this.get( i, j );
                
                this.set( i, j, this.get( j, i ) );
                this.set( j, i, tmp );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Places the matrix values of the transpose of matrix mat into this matrix.<br>
     * If matrix mat is MxN then this matrix must be NxM to hold the transpose.
     * 
     * @param mat the matrix to be transposed (but not modified)
     * 
     * @return itself
     */
    public MatrixMxNf transpose( MatrixMxNf mat )
    {
        if ( ( mat.getNumRows() != this.getNumCols() ) || ( mat.getNumCols() != this.getNumRows() ) )
            throw new IllegalArgumentException( "Failed to transpose matrix due to size mismatch. Transposing an MxN matrix requires a destination matrix to be sized NxM" );
        
        // Copy the matrix over.
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                set( r, c, mat.get( c, r ) );
            }
        }
        
        return ( this );
    }
    
    /**
     * Calculates the One-norm.
     * 
     * @return maximum column sum.
     */
    public float norm1()
    {
        float f = 0f;
        
        for ( int j = 0; j < getNumCols(); j++ )
        {
            float s = 0f;
            for ( int i = 0; i < getNumRows(); i++ )
            {
                s += Math.abs( get( i, j ) );
            }
            
            f = Math.max( f, s );
        }
        
        return ( f );
    }
    
    /**
     * Calculates the One-norm.
     * 
     * @return maximum singular value.
     */
    public final float norm2()
    {
        return ( new SingularValueDecomposition( this ).norm2() );
    }
    
    /**
     * Calculates the Infinity-norm.
     * 
     * @return maximum maximum row sum.
     */
    public float normInfinity()
    {
        float f = 0f;
        for ( int i = 0; i < getNumRows(); i++ )
        {
            float s = 0f;
            for ( int j = 0; j < getNumCols(); j++ )
            {
                s += Math.abs( get( i, j ) );
            }
            
            f = Math.max( f, s );
        }
        
        return ( f );
    }
    
    /**
     * Calculates the Frobenius-norm.
     * 
     * @return sqrt of sum of squares of all elements.
     */
    public float normFrobenius()
    {
        float f = 0f;
        for ( int i = 0; i < getNumRows(); i++ )
        {
            for ( int j = 0; j < getNumCols(); j++ )
            {
                f = FastMath.hypot( f, get( i, j ) );
            }
        }
        
        return ( f );
    }
    
    /**
     * Tests whether a submatrix is positive definite. This tests
     * the upper-left <i>n</i>x<i>n</i> submatrix.
     * 
     * @param n the size of the submatrix to be tested
     * 
     * @return <code>true</code> if the submatrix is positive definite,
     * and <code>false</code> otherwise
     */
    public boolean isPositiveDefinite( int n )
    {
        /*
         * Extract the upper-left nxn submatrix from matrix a. In some cases,
         * for example, a is 4x4 and we're only using the upper-left 3x3.
         */
        final MatrixMxNf upperLeft;
        if ( ( this.getNumRows() == n ) && ( this.getNumCols() == n ) )
            upperLeft = this;
        else
            upperLeft = this.getSharedSubMatrix( 0, 0, n, n );
        
        /*
         * Not every positive definite matrix is symmetric, but a matrix
         * M is positive definite, if its symmetric part, (M+transpose(M))/2,
         * is positive definite. So we obtain the symmetric part and check it
         * (while factoring out the division by 2):
         */
        for ( int i = 0; i < n; i++ )
        {
            upperLeft.set( i, i, upperLeft.get( i, i ) * 2f );
            for ( int j = 0; j <= i; j++ )
            {
                float s = upperLeft.get( i, j ) + upperLeft.get( j, i );
                upperLeft.set( i, j, s );
                upperLeft.set( j, i, s );
            }
        }
        
        return ( new CholeskyDecomposition( upperLeft ).isSPD() );
    }
    
    /**
     * Tests whether the matrix is positive definite.
     * 
     * @return <code>true</code> if the matrix is positive definite,
     * and <code>false</code> otherwise
     */
    public boolean isPositiveDefinite()
    {
        if ( !isSquare() )
            return ( false );
        
        return ( isPositiveDefinite( getNumRows() ) );
    }
    
    /**
     * @return the trace of this matrix.
     */
    public final float trace()
    {
        int min = rows < cols ? rows : cols;
        
        float trace = 0.0f;
        
        for ( int i = 0; i < min; i++ )
            trace += this.values[ dataBegin + i * colSkip + i ];
        
        return ( trace );
    }
    
    /**
     * Computes the effective numerical rank, obtained from SVD.
     * @return Matrix rank
     */
    public final int rank()
    {
        return ( new SingularValueDecomposition( this ).rank() );
    }
    
    /**
     * Matrix condition (2 norm)
     * @return ratio of largest to smallest singular value.
     */
    public final float cond()
    {
        return ( new SingularValueDecomposition( this ).cond() );
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
    public final void copySubMatrix( int rowSource, int colSource, int numRows, int numCols, int rowDest, int colDest, MatrixMxNf target )
    {
        if ( rowSource < 0 || colSource < 0 || rowDest < 0 || colDest < 0 )
            throw new ArrayIndexOutOfBoundsException( "rowSource, colSource, rowDest, colDest < 0." );
        else if ( rows < numRows + rowSource || cols < numCols + colSource )
            throw new ArrayIndexOutOfBoundsException( "Source Matrix too small." );
        else if ( target.rows < numRows + rowDest || target.cols < numCols + colDest )
            throw new ArrayIndexOutOfBoundsException( "Target Matrix too small." );
        
        for ( int i = 0; i < numRows; i++ )
            for ( int j = 0; j < numCols; j++ )
                target.set( ( i + rowDest ), ( j + colDest ), this.get( ( i + rowSource ), ( j + colSource ) ) );
    }
    
    /**
     * Adds a scalar to this Matrix.
     * 
     * @param scalar
     * 
     * @return itself
     */
    public final MatrixMxNf add( float scalar )
    {
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                values[ roTrick + dataBegin + r * colSkip + c ] += scalar;
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to sum of itself and matrix m2.
     * 
     * @param m2 the other matrix
     * 
     * @return itself
     */
    public final MatrixMxNf add( MatrixMxNf m2 )
    {
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m2.rows + "x" + m2.cols + ")." );
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                values[ roTrick + dataBegin + r * colSkip + c ] += m2.get( r, c );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     * 
     * @return itself
     */
    public final MatrixMxNf add( MatrixMxNf m1, MatrixMxNf m2 )
    {
        if ( rows != m1.rows || cols != m1.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m1.rows + "x" + m1.cols + ")." );
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m2:(" + m2.rows + "x" + m2.cols + ")." );
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                set( r, c, m1.get( r, c ) + m2.get( r, c ) );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Subtracts a scalar from this Matrix.
     * 
     * @param scalar
     * 
     * @return itself
     */
    public final MatrixMxNf sub( float scalar )
    {
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                values[ roTrick + dataBegin + r * colSkip + c ] -= scalar;
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of itself and
     * matrix m2 (this = this - m2).
     * 
     * @param m2 the other matrix
     * 
     * @return itself
     */
    public final MatrixMxNf sub( MatrixMxNf m2 )
    {
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m2.rows + "x" + m2.cols + ")." );
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                values[ roTrick + dataBegin + r * colSkip + c ] -= m2.get( r, c );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the matrix difference of matrices m1 and
     * m2 (this = m1 - m2).
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     * 
     * @return itself
     */
    public final MatrixMxNf sub( MatrixMxNf m1, MatrixMxNf m2 )
    {
        if ( rows != m1.rows || cols != m1.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m1:(" + m1.rows + "x" + m1.cols + ")." );
        if ( rows != m2.rows || cols != m2.cols )
            throw new IllegalArgumentException( "this:(" + rows + "x" + cols + ") != m2:(" + m2.rows + "x" + m2.cols + ")." );
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                set( r, c, m1.get( r, c ) - m2.get( r, c ) );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies this Matrix with a scalar.
     * 
     * @param scalar
     * 
     * @return itself
     */
    public MatrixMxNf mul( float scalar )
    {
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                values[ roTrick + dataBegin + r * colSkip + c ] *= scalar;
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying the two
     * argument matrices together (this = m1 * m2).
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     * 
     * @return itself
     */
    public MatrixMxNf mul( MatrixMxNf m1, MatrixMxNf m2 )
    {
        // for alias-safety, decided to new float[ cols* rows ].
        // Is there any good way to avoid this big new ?
        if ( rows != m1.rows )
            throw new ArrayIndexOutOfBoundsException( "rows:" + rows + " != m1.rows:" + m1.rows );
        if ( cols != m2.cols )
            throw new ArrayIndexOutOfBoundsException( "cols:" + cols + " != m2.cols:" + m2.cols );
        if ( m1.cols != m2.rows )
            throw new ArrayIndexOutOfBoundsException( "m1.cols:" + m1.cols + " != m2.rows:" + m2.rows );
        
        float[] newData = new float[ cols * rows ];
        for ( int i = 0; i < rows; i++ )
        {
            for ( int j = 0; j < cols; j++ )
            {
                float sum = 0.0f;
                for ( int k = 0; k < m1.cols; k++ )
                    sum += m1.get( i, k ) * m2.get( k, j );
                newData[ i * cols + j ] = sum;
            }
        }
        
        setRowMajor( newData );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying itself with
     * matrix m1 (this = this * m1).
     * 
     * @param mat2 the other matrix
     * 
     * @return itself
     */
    public MatrixMxNf mul( MatrixMxNf mat2 )
    {
        // alias-safe.
        mul( this, mat2 );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying the two
     * argument matrices <strong>componentwisely</strong> (this = m1 x m2).
     * 
     * @param m1 the first matrix
     * @param m2 the second matrix
     * 
     * @return itself
     */
    public final MatrixMxNf mulComp( MatrixMxNf m1, MatrixMxNf m2 )
    {
        // for alias-safety, decided to new float[ cols* rows ].
        // Is there any good way to avoid this big new ?
        if ( ( rows != m1.rows ) || ( rows != m2.rows ) )
            throw new ArrayIndexOutOfBoundsException( "rows:" + rows + " != m1.rows:" + m1.rows );
        if ( ( cols != m1.cols ) || ( cols != m2.cols ) )
            throw new ArrayIndexOutOfBoundsException( "cols:" + cols + " != m2.cols:" + m2.cols );
        
        for ( int i = 0; i < getNumRows(); i++ )
        {
            for ( int j = 0; j < getNumCols(); j++ )
            {
                set( i, j, m1.get( i, j ) * m2.get( i, j ) );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Sets the value of this matrix to the result of multiplying this matrix
     * and the other one <strong>componentwisely</strong> (this = this x m2).
     * 
     * @param mat2 the other matrix
     * 
     * @return itself
     */
    public final MatrixMxNf mulComp( MatrixMxNf mat2 )
    {
        // alias-safe.
        mulComp( this, mat2 );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Computes the outer product of the two vectors; multiplies the the first
     * vector by the transpose of the second vector and places the matrix result
     * into this matrix. This matrix must be as big or bigger than
     * getSize(v1) x getSize(v2).
     * 
     * @param v1 the first vector, treated as a row vector
     * @param v2 the second vector, treated as a column vector
     * 
     * @return itself
     */
    public MatrixMxNf mul( TupleNf< ? > v1, TupleNf< ? > v2 )
    {
        if ( rows < v1.getSize() )
            throw new IllegalArgumentException( "rows:" + rows + " < v1.getSize():" + v1.getSize() );
        if ( cols < v2.getSize() )
            throw new IllegalArgumentException( "cols:" + cols + " < v2.getSize():" + v2.getSize() );
        
        for ( int i = 0; i < rows; i++ )
            for ( int j = 0; j < cols; j++ )
                this.set( i, j, v1.getValue( i ) * v2.getValue( j ) );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
     * and places the result into this.
     * 
     * @param m1 The matrix on the left hand side of the multiplication
     * @param m2 The matrix on the right hand side of the multiplication
     * 
     * @return itself
     */
    public MatrixMxNf mulTransposeBoth( MatrixMxNf m1, MatrixMxNf m2 )
    {
        mul( m2, m1 );
        
        transpose();
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies the transpose of matrix m1 times the matrix m2, and places the
     * result into this.
     * 
     * @param m1 The matrix on the left hand side of the multiplication
     * @param m2 The matrix on the right hand side of the multiplication
     * 
     * @return itself
     */
    public MatrixMxNf mulTransposeLeft( MatrixMxNf m1, MatrixMxNf m2 )
    {
        transpose( m1 );
        
        mul( m2 );
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Multiplies matrix m1 times the transpose of matrix m2, and places the
     * result into this. <br>
     * Not alias-save!
     * 
     * @return itself
     */
    public MatrixMxNf mulTransposeRight( MatrixMxNf m1, MatrixMxNf m2 )
    {
        if ( m1.cols != m2.cols || this.rows != m1.rows || this.cols != m2.rows )
            throw new ArrayIndexOutOfBoundsException( "matrices mismatch" );
        
        for ( int i = 0; i < rows; i++ )
        {
            for ( int j = 0; j < cols; j++ )
            {
                float sum = 0.0f;
                for ( int k = 0; k < m1.cols; k++ )
                    sum += m1.get( i, k ) * m2.get( j, k );
                
                this.set( i, j, sum );
            }
        }
        
        this.isDirty[ 0 ] = true;
        
        return ( this );
    }
    
    /**
     * Solves this * X = m2.
     * 
     * @param m2 right hand side
     * @param result if this is square, least squares solution otherwise
     */
    public final void solve( MatrixMxNf m2, MatrixMxNf result )
    {
        if ( this.isSquare() )
            result.set( new LUDecomposition( this ).solve( m2 ) );
        else
            new QRDecomposition( this ).solve( m2, result );
    }
    
    /**
     * Performs gaussian elimination with parial pivoting. Input matrix must be a square matrix,
     * input RHS must be a vector of the same size.
     * the passback bust be the size of the matrix.
     * Each equation should be encoded as a row of the matrix
     * eg
     * 2x + 3y = 7
     * 5x - 3y = 8
     * [2, 3
     * 5,-3] = [7,8]'
     * [cooeffecients] = [rhs]
     * the result, passback, will contain instanciationsof the unkowns that satisfy the equations
     * or if degenerate, the method will return false<br>
     * IMPORTANT: This method will change b and this Matrix!<br>
     * 
     * mmh {@link VectorNf#LUDBackSolve(MatrixMxNf, VectorNf, VectorNf)} seems to do the same. For large Matrices it is probably even more GC-friendly.
     */
    public boolean solve( TupleNf< ? > b, TupleNf< ? > passback )
    {
        if ( !this.isSquare() )
            throw new Error( "This matrix must be a square matrix." );
        
        final int N = b.getSize();
        if ( N != getNumCols() )
            return ( false );
        
        // cast to TupleNf to make use of inlining
        TupleNf< ? > tmp = (TupleNf< ? >)VecMathUtils.getVectorFromPool( N );
        TupleNf< ? > maxRow = (TupleNf< ? >)VecMathUtils.getVectorFromPool( N );
        
        //System.out.println( "A = " + A );
        
        for ( int p = 0; p < N; p++ )
        {
            // find pivot row and swap
            int max = p;
            for ( int i = p + 1; i < N; i++ )
            {
                if ( Math.abs( get( i, p ) ) > Math.abs( get( max, p ) ) )
                {
                    max = i;
                }
            }
            getRow( max, maxRow );
            
            getRow( p, tmp );
            setRow( p, maxRow );
            setRow( max, tmp );
            float t = b.getValue( p );
            b.setValue( p, b.getValue( max ) );
            b.setValue( max, t );
            
            // singular
            if ( get( p, p ) == 0.0f )
            {
                VecMathUtils.putVectorToPool( (VectorInterface< ?, ? >)maxRow );
                VecMathUtils.putVectorToPool( (VectorInterface< ?, ? >)tmp );
                
                return ( false );
            }
            
            // pivot within A and b
            for ( int i = p + 1; i < N; i++ )
            {
                float alpha = get( i, p ) / get( p, p );
                b.subValue( i, alpha * b.getValue( p ) );
                for ( int j = p; j < N; j++ )
                    set( i, j, get( i, j ) - alpha * get( p, j ) );
            }
        }
        
        // back substitution
        for ( int i = N - 1; i >= 0; i-- )
        {
            float sum = 0;
            for ( int j = i + 1; j < N; j++ )
                sum += get( i, j ) * passback.getValue( j );
            passback.setValue( i, ( b.getValue( i ) - sum ) / get( i, i ) );
        }
        
        VecMathUtils.putVectorToPool( (VectorInterface< ?, ? >)maxRow );
        VecMathUtils.putVectorToPool( (VectorInterface< ?, ? >)tmp );
        
        return ( true );
    }
    
    private final void setDiag( int i, float value )
    {
        this.values[ roTrick + dataBegin + i * colSkip + i ] = value;
    }
    
    private final float getDiag( int i )
    {
        return ( this.values[ dataBegin + i * colSkip + i ] );
    }
    
    private final float dpythag( float a, float b )
    {
        final float absa = Math.abs( a );
        final float absb = Math.abs( b );
        
        if ( absa > absb )
        {
            if ( absa == 0.0f )
                return ( 0.0f );
            
            float term = absb / absa;
            if ( Math.abs( term ) <= Float.MIN_VALUE )
                return ( absa );
            
            return ( absa * FastMath.sqrt( 1.0f + term * term ) );
        }
        
        if ( absb == 0.0f )
            return ( 0.0f );
        
        float term = absa / absb;
        if ( Math.abs( term ) <= Float.MIN_VALUE )
            return ( absb );
        
        return ( absb * FastMath.sqrt( 1.0f + term * term ) );
    }
    
    /**
     * Finds the singular value decomposition (SVD) of this matrix such that
     * this = U*W*transpose(V); and returns the rank of this matrix; the values
     * of U,W,V are all overwritten. Note that the matrix V is output as V, and
     * not transpose(V). If this matrix is mxn, then U is mxm, W is a diagonal
     * matrix that is mxn, and V is nxn. Using the notation W = diag(w), then
     * the inverse of this matrix is: inverse(this) = V*diag(1/w)*tranpose(U),
     * where diag(1/w) is the same matrix as W except that the reciprocal of
     * each of the diagonal components is used.
     * 
     * @param u The computed U matrix in the equation this = U*W*transpose(V)
     * @param w The computed W matrix in the equation this = U*W*transpose(V)
     * @param v The computed V matrix in the equation this = U*W*transpose(V)
     * 
     * @return The rank of this matrix.
     */
    public int SVD( MatrixMxNf u, MatrixMxNf w, MatrixMxNf v )
    {
        if ( u.rows != rows || u.cols != rows )
            throw new ArrayIndexOutOfBoundsException( "The U Matrix invalid size" );
        if ( v.rows != cols || v.cols != cols )
            throw new ArrayIndexOutOfBoundsException( "The V Matrix invalid size" );
        if ( w.cols != cols || w.rows != rows )
            throw new ArrayIndexOutOfBoundsException( "The W Matrix invalid size" );
        
        final int m = rows;
        final int n = cols;
        final int imax = m > n ? m : n;
        final float[] U = u.values;
        final float[] V = v.values;
        int i, its, j, jj, k, l = 0, nm = 0;
        float anorm, c, f, g, h, s, scale, x, y, z;
        final float[] rv1 = new float[ n ];
        
        // copy this to [u]
        this.get( u );
        // pad 0.0 to the other elements.
        for ( i = m; i < imax; i++ )
            for ( j = 0; j < imax; j++ )
                U[ u.roTrick + i * m + j ] = 0.0f;
        for ( j = n; j < imax; j++ )
            for ( i = 0; i < imax; i++ )
                U[ u.roTrick + i * m + j ] = 0.0f;
        u.isDirty[ 0 ] = true;
        // pad 0.0 to w
        w.setZero();
        
        g = scale = anorm = 0.0f;
        for ( i = 0; i < n; i++ )
        {
            l = i + 1;
            rv1[ i ] = scale * g;
            g = s = scale = 0.0f;
            if ( i < m )
            {
                for ( k = i; k < m; k++ )
                    scale += Math.abs( U[ k * m + i ] );
                
                if ( scale != 0.0f )
                {
                    for ( k = i; k < m; k++ )
                    {
                        U[ u.roTrick + k * m + i ] /= scale;
                        s += U[ k * m + i ] * U[ k * m + i ];
                    }
                    f = U[ i * m + i ];
                    
                    // #define SIGN(a,b) ((b) >= 0.0 ? fabs(a) : -fabs(a))
                    // g = -SIGN(sqrt(s),f);
                    
                    g = ( f < 0.0f ? FastMath.sqrt( s ) : -FastMath.sqrt( s ) );
                    h = f * g - s;
                    U[ u.roTrick + i * m + i ] = f - g;
                    for ( j = l; j < n; j++ )
                    {
                        for ( s = 0.0f, k = i; k < m; k++ )
                            s += U[ k * m + i ] * U[ k * m + j ];
                        f = s / h;
                        for ( k = i; k < m; k++ )
                            U[ u.roTrick + k * m + j ] += f * U[ k * m + i ];
                    }
                    
                    for ( k = i; k < m; k++ )
                        U[ u.roTrick + k * m + i ] *= scale;
                }
            }
            
            w.setDiag( i, scale * g );
            g = s = scale = 0.0f;
            if ( i < m && i != n - 1 )
            {
                for ( k = l; k < n; k++ )
                    scale += Math.abs( U[ i * m + k ] );
                if ( scale != 0.0f )
                {
                    for ( k = l; k < n; k++ )
                    {
                        U[ u.roTrick + i * m + k ] /= scale;
                        s += U[ i * m + k ] * U[ i * m + k ];
                    }
                    f = U[ i * m + l ];
                    
                    // #define SIGN(a,b) ((b) >= 0.0f ? fabs( a ) : -fabs( a ))
                    // g = -SIGN( sqrt( s ), f );
                    
                    g = ( f < 0.0f ? FastMath.sqrt( s ) : -FastMath.sqrt( s ) );
                    h = f * g - s;
                    U[ u.roTrick + i * m + l ] = f - g;
                    for ( k = l; k < n; k++ )
                        rv1[ k ] = U[ i * m + k ] / h;
                    
                    for ( j = l; j < m; j++ )
                    {
                        for ( s = 0.0f, k = l; k < n; k++ )
                            s += U[ j * m + k ] * U[ i * m + k ];
                        
                        for ( k = l; k < n; k++ )
                            U[ u.roTrick + j * m + k ] += s * rv1[ k ];
                    }
                    
                    for ( k = l; k < n; k++ )
                        U[ u.roTrick + i * m + k ] *= scale;
                }
            }
            
            // anorm=MAX2INT(anorm,(Math.abs(w.getDiag(i))+Math.abs(rv1[i])));
            float a1 = Math.abs( w.getDiag( i ) ) + Math.abs( rv1[ i ] );
            if ( a1 > anorm )
                anorm = a1;
        }
        
        for ( i = n - 1; i >= 0; i-- )
        {
            if ( i < n - 1 )
            {
                if ( g != 0.0f )
                {
                    for ( j = l; j < n; j++ )
                        V[ v.roTrick + j * n + i ] = ( U[ i * m + j ] / U[ i * m + l ] ) / g;
                    for ( j = l; j < n; j++ )
                    {
                        for ( s = 0.0f, k = l; k < n; k++ )
                            s += U[ i * m + k ] * V[ k * n + j ];
                        for ( k = l; k < n; k++ )
                            V[ v.roTrick + k * n + j ] += s * V[ k * n + i ];
                    }
                }
                for ( j = l; j < n; j++ )
                    V[ v.roTrick + i * n + j ] = V[ j * n + i ] = 0.0f;
            }
            V[ v.roTrick + i * n + i ] = 1.0f;
            g = rv1[ i ];
            l = i;
        }
        
        v.isDirty[ 0 ] = true;
        // for (i = IMIN( m, n ) - 1; i >= 0; i--) {
        int imin = m < n ? m : n;
        for ( i = imin - 1; i >= 0; i-- )
        {
            l = i + 1;
            g = w.getDiag( i );
            for ( j = l; j < n; j++ )
                U[ u.roTrick + i * m + j ] = 0.0f;
            
            if ( g != 0.0f )
            {
                g = 1.0f / g;
                for ( j = l; j < n; j++ )
                {
                    for ( s = 0.0f, k = l; k < m; k++ )
                        s += U[ k * m + i ] * U[ k * m + j ];
                    f = ( s / U[ i * m + i ] ) * g;
                    for ( k = i; k < m; k++ )
                        U[ u.roTrick + k * m + j ] += f * U[ k * m + i ];
                }
                
                for ( j = i; j < m; j++ )
                    U[ u.roTrick + j * m + i ] *= g;
            }
            else
            {
                for ( j = i; j < m; j++ )
                    U[ u.roTrick + j * m + i ] = 0.0f;
            }
            ++U[ u.roTrick + i * m + i ];
        }
        
        for ( k = n - 1; k >= 0; k-- )
        {
            for ( its = 1; its <= 30; its++ )
            {
                boolean flag = true;
                for ( l = k; l >= 0; l-- )
                {
                    nm = l - 1;
                    if ( ( Math.abs( rv1[ l ] ) + anorm ) == anorm )
                    {
                        flag = false;
                        break;
                    }
                    
                    if ( ( Math.abs( w.getDiag( nm ) ) + anorm ) == anorm )
                        break;
                }
                
                if ( flag )
                {
                    c = 0.0f;
                    s = 1.0f;
                    for ( i = l; i <= k; i++ )
                    {
                        f = s * rv1[ i ];
                        rv1[ i ] = c * rv1[ i ];
                        if ( ( Math.abs( f ) + anorm ) == anorm )
                            break;
                        
                        g = w.getDiag( i );
                        h = dpythag( f, g );
                        w.setDiag( i, h );
                        h = 1.0f / h;
                        c = g * h;
                        s = -f * h;
                        
                        for ( j = 0; j < m; j++ )
                        {
                            y = U[ j * m + nm ];
                            z = U[ j * m + i ];
                            U[ u.roTrick + j * m + nm ] = y * c + z * s;
                            U[ u.roTrick + j * m + i ] = z * c - y * s;
                        }
                    }
                }
                
                z = w.getDiag( k );
                if ( l == k )
                {
                    if ( z < 0.0f )
                    {
                        w.setDiag( k, -z );
                        for ( j = 0; j < n; j++ )
                            V[ v.roTrick + j * n + k ] = -V[ j * n + k ];
                    }
                    
                    break; /* NORMAL EXIT */
                }
                
                if ( its == 30f )
                {
                    return ( 0 ); // not solved.
                }
                
                x = w.getDiag( l );
                nm = k - 1;
                y = w.getDiag( nm );
                g = rv1[ nm ];
                h = rv1[ k ];
                f = ( ( y - z ) * ( y + z ) + ( g - h ) * ( g + h ) ) / ( 2.0f * h * y );
                g = dpythag( f, 1.0f );
                
                // #define SIGN(a, b) ((b) >= 0.0f ? fabs(a) : -fabs(a))
                // f=((x - z) * (x + z) + h * ((y / (f + SIGN(g, f))) - h)) / x;
                
                f = ( ( x - z ) * ( x + z ) + h * ( ( y / ( f + ( f >= 0.0f ? Math.abs( g ) : -Math.abs( g ) ) ) ) - h ) ) / x;
                c = s = 1.0f;
                for ( j = l; j <= nm; j++ )
                {
                    i = j + 1;
                    g = rv1[ i ];
                    y = w.getDiag( i );
                    h = s * g;
                    g = c * g;
                    z = dpythag( f, h );
                    rv1[ j ] = z;
                    c = f / z;
                    s = h / z;
                    f = x * c + g * s;
                    g = g * c - x * s;
                    h = y * s;
                    y *= c;
                    
                    for ( jj = 0; jj < n; jj++ )
                    {
                        x = V[ jj * n + j ];
                        z = V[ jj * n + i ];
                        V[ v.roTrick + jj * n + j ] = x * c + z * s;
                        V[ v.roTrick + jj * n + i ] = z * c - x * s;
                    }
                    
                    z = dpythag( f, h );
                    w.setDiag( j, z );
                    if ( z != 0.0f )
                    {
                        z = 1.0f / z;
                        c = f * z;
                        s = h * z;
                    }
                    
                    f = c * g + s * y;
                    x = c * y - s * g;
                    for ( jj = 0; jj < m; jj++ )
                    {
                        y = U[ jj * m + j ];
                        z = U[ jj * m + i ];
                        U[ u.roTrick + jj * m + j ] = y * c + z * s;
                        U[ u.roTrick + jj * m + i ] = z * c - y * s;
                    }
                }
                
                rv1[ l ] = 0.0f;
                rv1[ k ] = f;
                w.setDiag( k, x );
            }
        }
        
        // find the number of non-zero w which is the rank of this matrix
        int rank = 0;
        for ( i = 0; i < n; i++ )
            if ( w.getDiag( i ) > 0.0f )
                rank++;
        
        return ( rank );
    }
    
    private final void swapRows( int i, int j )
    {
        float tmp;
        
        for ( int k = 0; k < cols; k++ )
        {
            tmp = this.values[ dataBegin + i * colSkip + k ];
            this.values[ roTrick + dataBegin + i * colSkip + k ] = this.values[ dataBegin + j * colSkip + k ];
            this.values[ roTrick + dataBegin + j * colSkip + k ] = tmp;
        }
    }
    
    /**
     * LU Decomposition; this matrix must be a square matrix; the LU GMatrix
     * parameter must be the same size as this matrix. The matrix LU will be
     * overwritten as the combination of a lower diagonal and upper diagonal
     * matrix decompostion of this matrix; the diagonal elements of L (unity)
     * are not stored. The VectorNf parameter records the row permutation
     * effected by the partial pivoting, and is used as a parameter to the
     * VectorNf method LUDBackSolve to solve sets of linear equations. This
     * method returns +/- 1 depending on whether the number of row interchanges
     * was even or odd, respectively.
     * 
     * @param permutation The row permutation effected by the partial pivoting
     * @return +-1 depending on whether the number of row interchanges was even
     *         or odd respectively
     */
    public int LUD( MatrixMxNf lu, VectorNf permutation )
    {
        // note: this is from William H. Press et.al Numerical Recipes in C.
        // hiranabe modified 1-n indexing to 0-(n-1), and not to use implicit
        // scaling factors(which uses 'new' and I don't belive relative pivot is
        // better)
        // I fixed some bugs in NRC, which are missing permutation handling.
        if ( rows != cols )
            throw new ArrayIndexOutOfBoundsException( "not a square matrix" );
        final int n = rows;
        if ( n != lu.rows )
            throw new ArrayIndexOutOfBoundsException( "this.nRow:" + n + " != LU.nRow:" + lu.rows );
        if ( n != lu.cols )
            throw new ArrayIndexOutOfBoundsException( "this.nCol:" + n + " != LU.nCol:" + lu.cols );
        if ( permutation.getSize() < n )
            throw new ArrayIndexOutOfBoundsException( "permutation.size:" + permutation.getSize() + " < this.nCol:" + n );
        
        if ( this != lu )
            lu.set( this );
        
        final float[] a = lu.values;
        
        int even = 1; // permutation Odd/Even
        
        // initialize index
        for ( int i = 0; i < n; i++ )
            permutation.setValue( i, i );
        
        // start Crout's method
        for ( int j = 0; j < n; j++ )
        {
            float big, dum, sum;
            int imax; // the pivot row number
            
            // upper portion (U)
            for ( int i = 0; i < j; i++ )
            {
                sum = a[ i * n + j ];
                for ( int k = 0; k < i; k++ )
                {
                    if ( a[ i * n + k ] != 0.0f && a[ k * n + j ] != 0.0f )
                        sum -= a[ i * n + k ] * a[ k * n + j ];
                }
                a[ lu.roTrick + i * n + j ] = sum;
            }
            big = 0.0f;
            imax = j;
            
            // lower part (L)
            for ( int i = j; i < n; i++ )
            {
                sum = a[ i * n + j ];
                for ( int k = 0; k < j; k++ )
                {
                    if ( a[ i * n + k ] != 0.0f && a[ k * n + j ] != 0.0f )
                        sum -= a[ i * n + k ] * a[ k * n + j ];
                }
                a[ lu.roTrick + i * n + j ] = sum;
                dum = Math.abs( sum );
                if ( dum >= big )
                {
                    big = dum;
                    imax = i; // imax is the pivot
                }
            }
            
            if ( j != imax ) // if pivot is not on the diagonal
            {
                // swap rows
                lu.swapRows( imax, j );
                float tmp = permutation.getValue( imax );
                permutation.setValue( imax, permutation.getValue( j ) );
                permutation.setValue( j, tmp );
                even = -even;
            }
            
            // zero-div occurs.
            // if ( a[ j ][ j ] == 0.0f )
            if ( j != n - 1 )
            {
                dum = 1.0f / a[ j * n + j ];
                for ( int i = j + 1; i < n; i++ )
                    a[ lu.roTrick + i * n + j ] *= dum;
            }
            
        } // end of for j
        
        return ( even );
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
    public void interpolate( MatrixMxNf m1, MatrixMxNf m2, float alpha )
    {
        if ( ( m1.getNumCols() != m2.getNumCols() ) ||
             ( m1.getNumRows() != m2.getNumRows() ) ||
             ( m1.getNumRows() != this.getNumRows() ) ||
             ( m1.getNumRows() != this.getNumRows() ) )
            throw new IllegalArgumentException( "All three matrices must be of tame size" );
        
        for ( int r = 0; r < getNumRows(); r++ )
        {
            for ( int c = 0; c < getNumCols(); c++ )
            {
                this.set( r, c, m1.get( r, c ) + ( ( m2.get( r, c ) - m1.get( r, c ) ) * alpha ) );
            }
        }
        
        this.isDirty[ 0 ] = true;
    }
    
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
        int hash = 0;
        for ( int r = 0; r < rows; r++ )
        {
            for ( int c = 0; c < cols; c++ )
            {
                int bits = VecMathUtils.floatToIntBits( get( r, c ) );
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
    public boolean equals( MatrixMxNf mat2 )
    {
        if ( mat2 == null )
            return ( false );
        if ( mat2.rows != rows )
            return ( false );
        if ( mat2.cols != cols )
            return ( false );
        
        for ( int i = 0; i < rows; i++ )
            for ( int j = 0; j < cols; j++ )
                if ( this.get( i, j ) != mat2.get( i, j ) )
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
        return ( ( o != null ) && ( ( o instanceof MatrixMxNf ) && equals( (MatrixMxNf)o ) ) );
    }
    
    /**
     * Returns true if the L-infinite distance between this matrix and matrix m1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[i=0,1,2, . . .n ; j=0,1,2, . . .n ;
     * abs(this.m(i,j) - m1.m(i,j)] .
     * 
     * @param mat2 The matrix to be compared to this matrix
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( MatrixMxNf mat2, float epsilon )
    {
        if ( mat2.rows != rows )
            return ( false );
        if ( mat2.cols != cols )
            return ( false );
        
        for ( int i = 0; i < rows; i++ )
            for ( int j = 0; j < cols; j++ )
                if ( epsilon < Math.abs( this.get( i, j ) - mat2.get( i, j ) ) )
                    return ( false );
        
        return ( true );
    }
    
    private final StringBuffer tmpSB = new StringBuffer();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final String nl = System.getProperty( "line.separator" );
        
        tmpSB.setLength( 0 );
        
        tmpSB.append( "[" );
        tmpSB.append( nl );
        
        for ( int i = 0; i < rows; i++ )
        {
            tmpSB.append( "  [ " );
            for ( int j = 0; j < cols; j++ )
            {
                final int oldLen = tmpSB.length();
                tmpSB.append( this.get( i, j ) );
                final int dl = tmpSB.length() - oldLen;
                if (/* (j < cols - 1) &&*/ ( dl < 16 ) )
                {
                    for ( int s = dl; s < 16; s++ )
                        tmpSB.append( " " );
                }
            }
            
            if ( i + 1 < rows )
            {
                tmpSB.append( " ]" );
                tmpSB.append( nl );
            }
            else
            {
                tmpSB.append( " ]" );
                tmpSB.append( nl );
                tmpSB.append( "]" );
            }
        }
        
        return ( tmpSB.toString() );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MatrixMxNf clone()
    {
        MatrixMxNf obj = new MatrixMxNf( rows, cols );
        
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
     * @see #MatrixMxNf(int, int, int, int, float[])
     */
    public MatrixMxNf getSharedSubMatrix( boolean readOnly, int beginRow, int beginCol, int rows, int cols )
    {
        final int begin = dataBegin + beginCol + beginRow * colSkip; // don't forget that we might operate already on a submatrix!
        // colskip stays same, cause rows stay rows ;)
        
        if ( ( rows == 3 ) && ( cols == 3 ) )
            return ( new SubMatrix3f( readOnly, begin, colSkip, values ) );
        else if ( ( rows == 4 ) && ( cols == 4 ) )
            return ( new SubMatrix4f( readOnly, begin, colSkip, values ) );
        else
            return ( new MatrixMxNf( readOnly, begin, colSkip, rows, cols, values, null ) );
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
    public MatrixMxNf getSharedSubMatrix( int beginRow, int beginCol, int rows, int cols )
    {
        return ( getSharedSubMatrix( false, beginRow, beginCol, rows, cols ) );
    }
    
    /**
     * @return a new instance sharing the values array with this instance.
     * The new instance is read-only. Changes to this instance will be reflected
     * in the new read-only-instance.
     * 
     * @see #getReadOnly()
     */
    public MatrixMxNf asReadOnly()
    {
        return ( new MatrixMxNf( true, this.dataBegin, this.colSkip, this.rows, this.cols, this.values, this.isDirty ) );
    }
    
    /**
     * @return a single instance sharing the values array with this instance (one unique instance per 'master-instance').
     * The instance is read-only. Changes to this instance will be reflected
     * in the read-only-instance.
     * 
     * @see #asReadOnly()
     */
    public MatrixMxNf getReadOnly()
    {
        if ( readOnlyInstance == null )
            readOnlyInstance = asReadOnly();
        
        return ( readOnlyInstance );
    }
    
    /**
     * Serializes this instanc'es data into the byte array.
     * 
     * @param pos
     * @param buffer
     * 
     * @return the incremented position
     */
    public int serialize( int pos, final byte[] buffer )
    {
        final int n = rows * cols;
        for ( int i = 0; i < n; i++ )
        {
            SerializationUtils.writeToBuffer( values[ i ], pos, buffer );
            pos += 4;
        }
        
        SerializationUtils.writeToBuffer( isDirty, pos, buffer );
        pos += 1;
        
        return ( pos );
    }
    
    /**
     * Deserializes this instanc'es data from the byte array.
     * 
     * @param pos
     * @param buffer
     * 
     * @return the incremented position
     */
    public int deserialize( int pos, final byte[] buffer )
    {
        final int n = rows * cols;
        for ( int i = 0; i < n; i++ )
        {
            values[ i ] = SerializationUtils.readFloatFromBuffer( pos, buffer );
            pos += 4;
        }
        
        isDirty[ 0 ] = SerializationUtils.readBoolFromBuffer( pos, buffer );
        pos += 1;
        
        return ( pos );
    }
    
    /**
     * @return the necessary size for a serialization byte array.
     */
    protected int getSerializationBufferSize()
    {
        return ( 4 * rows * cols + 1 );
    }
    
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        final byte[] buffer = new byte[ getSerializationBufferSize() ];
        
        serialize( 0, buffer );
        
        out.write( buffer );
    }
    
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        final byte[] buffer = new byte[ getSerializationBufferSize() ];
        
        in.read( buffer );
        
        deserialize( 0, buffer );
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
    protected MatrixMxNf( boolean readOnly, int rows, int cols )
    {
        if ( rows < 0 )
            throw new NegativeArraySizeException( rows + " < 0" );
        if ( cols < 0 )
            throw new NegativeArraySizeException( cols + " < 0" );
        
        this.rows = rows;
        this.cols = cols;
        this.values = new float[ rows * cols ];
        this.dataBegin = 0;
        this.colSkip = cols;
        
        Arrays.fill( this.values, 0.0f );
        
        final int min = rows < cols ? rows : cols;
        for ( int i = 0; i < min; i++ )
            this.values[ dataBegin + i * colSkip + i ] = 1.0f;
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[] { false };
    }
    
    /**
     * Constructs an rows by cols matrix initialized to the values in the matrix
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
    protected MatrixMxNf( boolean readOnly, int rows, int cols, float[] values )
    {
        if ( rows < 0 )
            throw new NegativeArraySizeException( rows + " < 0" );
        if ( cols < 0 )
            throw new NegativeArraySizeException( cols + " < 0" );
        
        this.rows = rows;
        this.cols = cols;
        this.values = new float[ rows * cols ];
        this.dataBegin = 0;
        this.colSkip = cols;
        
        final int size = rows * cols;
        System.arraycopy( values, 0, this.values, 0, size );
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[] { false };
    }
    
    /**
     * Constructs a new MatrixMxNf and copies the initial values from the parameter
     * matrix.
     * 
     * @param readOnly
     * @param matrix the source of the initial values of the new GMatrix
     */
    protected MatrixMxNf( boolean readOnly, MatrixMxNf matrix )
    {
        this.rows = matrix.rows;
        this.cols = matrix.cols;
        this.dataBegin = 0;
        this.colSkip = cols;
        this.values = new float[ rows * cols ];
        
        set( matrix );
        //System.arraycopy( matrix.values, 0, this.values, 0, this.values.length );
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        this.isDirty = new boolean[] { false };
    }
    
    /**
     * Hidden constructor for #sharedSubMatrixMxNf(MatrixMxNf, int, int) and #getSharedSubMatrix(int, int, int, int).
     * 
     * @param readOnly
     * @param dataBegin
     * @param colskip
     * @param rows
     * @param cols
     * @param values
     * @param isDirty
     */
    protected MatrixMxNf( boolean readOnly, int dataBegin, int colskip, int rows, int cols, float[] values, boolean[] isDirty )
    {
        this.dataBegin = dataBegin;
        this.colSkip = colskip;
        this.cols = cols;
        this.rows = rows;
        this.values = values;
        
        this.roTrick = readOnly ? -Integer.MAX_VALUE + values.length : 0;
        if ( isDirty == null )
            this.isDirty = new boolean[] { false };
        else
            this.isDirty = isDirty;
    }
    
    /**
     * Constructs an nRow by nCol identity matrix. Note that even though row and
     * column numbering begins with zero, nRow and nCol will be one larger than
     * the maximum possible matrix index values.
     * 
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     */
    public MatrixMxNf( int rows, int cols )
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
    public MatrixMxNf( int rows, int cols, float[] values )
    {
        this( false, rows, cols, values );
    }
    
    /**
     * Constructs a new GMatrix and copies the initial values from the parameter
     * matrix.
     * 
     * @param matrix the source of the initial values of the new GMatrix
     */
    public MatrixMxNf( MatrixMxNf matrix )
    {
        this( false, matrix );
    }
    
    /**
     * Hidden constructor for #sharedSubMatrixMxNf(MatrixMxNf, int, int) and #getSharedSubMatrix(int, int, int, int).
     * 
     * @param dataBegin
     * @param colskip
     * @param values
     */
    protected MatrixMxNf( int dataBegin, int colskip, int rows, int cols, float[] values )
    {
        this( false, dataBegin, colskip, rows, cols, values, null );
    }
    
    /**
     * Constructs an nRow by nCol identity matrix. Note that even though row and
     * column numbering begins with zero, nRow and nCol will be one larger than
     * the maximum possible matrix index values.
     * 
     * @param rows number of rows in this matrix.
     * @param cols number of columns in this matrix.
     */
    public static MatrixMxNf newReadOnly( int rows, int cols )
    {
        return ( new MatrixMxNf( true, rows, cols ) );
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
    public static MatrixMxNf newReadOnly( int rows, int cols, float[] values )
    {
        return ( new MatrixMxNf( true, rows, cols, values ) );
    }
    
    /**
     * Constructs a new GMatrix and copies the initial values from the parameter
     * matrix.
     * 
     * @param matrix the source of the initial values of the new GMatrix
     */
    public static MatrixMxNf newReadOnly( MatrixMxNf matrix )
    {
        return ( new MatrixMxNf( true, matrix ) );
    }
    
    /**
     * Creates a shared Submatrix of mat. Does the same as {@link #getSharedSubMatrix(int, int, int, int)}.
     * 
     * @param mat
     * @param beginRow
     * @param beginCol
     * @param rows
     * @param cols
     * 
     * @return the shared matrix
     */
    public static MatrixMxNf sharedSubMatrixMxNf( MatrixMxNf mat, int beginRow, int beginCol, int rows, int cols, boolean readOnly )
    {
        return ( mat.getSharedSubMatrix( readOnly, beginRow, beginCol, rows, cols ) );
    }
    
    /**
     * Creates a shared Submatrix of mat. Does the same as {@link #getSharedSubMatrix(int, int, int, int)}.
     * 
     * @param mat
     * @param beginRow
     * @param beginCol
     * @param rows
     * @param cols
     * 
     * @return the shared matrix
     */
    public static MatrixMxNf sharedSubMatrixMxNf( MatrixMxNf mat, int beginRow, int beginCol, int rows, int cols )
    {
        return ( mat.getSharedSubMatrix( beginRow, beginCol, rows, cols ) );
    }
}
