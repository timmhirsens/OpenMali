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
package org.openmali.number;

import org.openmali.number.matrix.Matrix4rad;

import java.io.StreamTokenizer;
import java.io.Reader;
import java.io.IOException;

/**
 * A hand written top down parser for Rational, Radicand1 and Matrix4 classes
 * 
 * @author Tom Larkworthy
 */
public class Parser
{
    /**
     * This is the char of the radical sign.
     * To avoid codepage conflicts, it is not haardcoded as a char,
     * but converted from its int representation.
     */
    private static final char RADICAL_SIGN = (char)251;
    
    private StreamTokenizer tokenizer;
    
    private int num;//tmp for passing back fractions from fraction()
    private int den;//tmp for passing back fractions from fraction()
    
    private Rational rational;//passback for radicand1Term
    private int radicand;//passback for radicand1Term
    
    public Parser( Reader in )
    {
        tokenizer = new StreamTokenizer( in );
        tokenizer.parseNumbers();
        tokenizer.ordinaryChar( '-' );
        tokenizer.ordinaryChar( '+' );
        tokenizer.ordinaryChar( '/' );
        tokenizer.ordinaryChar( '*' );
    }
    
    public StreamTokenizer getTokenizer()
    {
        return ( tokenizer );
    }
    
    /**
     * Reads a 4x4 radical matrix from the tokenizer
     * [ [radicand x 4] x4]
     * 
     * @return the read matrix
     */
    public Matrix4rad matrix4rad() throws IOException
    {
        Matrix4rad ret = new Matrix4rad();
        tokenizer.nextToken();
        assert tokenizer.ttype == '[';
        for ( int i = 0; i < 4; i++ )
        {
            tokenizer.nextToken();
            assert tokenizer.ttype == '[';
            ret.set( i, 0, radicand1() );
            ret.set( i, 1, radicand1() );
            ret.set( i, 2, radicand1() );
            ret.set( i, 3, radicand1() );
            tokenizer.nextToken();
            assert tokenizer.ttype == ']';
            
        }
        tokenizer.nextToken();
        assert tokenizer.ttype == ']';
        
        return ( ret );
    }
    
    /**
     * RADICAND1 <- RADICAND1_TERM {"+" RADICAND1_TERM}*
     * @return the radical
     */
    public Radical1 radicand1() throws IOException
    {
        Radical1 result = new Radical1();
        
        radicand1Term();
        result.setTerm( rational, radicand );
        
        while ( true )
        {
            
            if ( tokenizer.nextToken() == '+' )
            {
                radicand1Term();
                result.setTerm( rational, radicand );
            }
            else
            {
                tokenizer.pushBack();
                return ( result );
            }
        }
    }
    
    /**
     * RADICAND1_TERM <- RATIONAL {"*" RADICAND} | RADICAND
     */
    public void radicand1Term() throws IOException
    {
        if ( tokenizer.nextToken() == StreamTokenizer.TT_WORD && tokenizer.sval.charAt( 0 ) == RADICAL_SIGN )
        {
            rational = new Rational( 1 );
            tokenizer.pushBack();
            radicand();
        }
        else
        {
            tokenizer.pushBack();
            rational = rational();
            if ( tokenizer.nextToken() == '*' )
            {
                radicand();
            }
            else
            {
                radicand = 1;
                tokenizer.pushBack();
            }
        }
    }
    
    /**
     * For some stupid reason, StreamTokenizer refuses to seperate
     * the sqrt sign with the following digits. It recognizes the whole expression
     * as one big word.
     */
    private void radicand() throws IOException
    {
        int token = tokenizer.nextToken();
        
        if ( token == StreamTokenizer.TT_WORD )
        {
            assert tokenizer.sval.charAt( 0 ) == RADICAL_SIGN;
            radicand = Integer.parseInt( tokenizer.sval.substring( 1 ) );
        }
        else
        {
            System.err.println( "unexpected parse problems, whitespace after " + RADICAL_SIGN + " ?" );
            radicand = -1;
        }
    }
    
    /**
     * RATIONAL <- FRACTION || ( INTEGER +? FRACTION ) ||
     */
    public Rational rational() throws IOException
    {
        int type = tokenizer.nextToken();
        
        if ( type == '(' )
        {
            int whole = integer();
            
            if ( tokenizer.nextToken() == '+' )
            {
                //ignore
            }
            else
            {
                tokenizer.pushBack();
            }
            
            fraction();
            
            Rational ret = new Rational( whole, num, den );
            
            assert tokenizer.nextToken() == ')';
            
            return ( ret );
        }
        
        tokenizer.pushBack();
        fraction();
        
        return ( new Rational( num, den ) );
    }
    
    /**
     * FRACTION <= INTEGER { '/' NATURAL}?
     */
    public void fraction() throws IOException
    {
        num = integer();
        if ( tokenizer.nextToken() == '/' )
        {
            den = natural();
        }
        else
        {
            den = 1;
            tokenizer.pushBack();
        }
    }
    
    /**
     * INTEGER <- {+|-}? natural
     */
    public int integer() throws IOException
    {
        int val = ( optionalSign() ? 1 : -1 ) * natural();
        
        return ( val );
    }
    
    private boolean optionalSign() throws IOException
    {
        boolean positive = true;
        if ( tokenizer.nextToken() != StreamTokenizer.TT_NUMBER )
        {
            if ( tokenizer.ttype == '+' )
            {
                
            }
            else if ( tokenizer.ttype == '-' )
            {
                positive = !positive;
            }
            else
            {
                System.out.println( "err" );
            }
        }
        else
        {
            assert tokenizer.ttype == StreamTokenizer.TT_NUMBER;
            tokenizer.pushBack();
        }
        
        return ( positive );
    }
    
    /**
     * A posative integer (no signs infront of digits)
     */
    public int natural() throws IOException
    {
        int type = tokenizer.nextToken();
        assert type == StreamTokenizer.TT_NUMBER : tokenizer.sval;
        
        return ( (int)tokenizer.nval );
    }
    
    /**
     * A floating point number of the form
     * {+,-}? (dddddddd)?.(dddddd)? {E {+,-}? NATURAL}?
     * (no dangling f like in java
     * 
     * @return the float
     * @throws IOException
     */
    public float floatNum() throws IOException
    {
        boolean positive = optionalSign();
        
        int type = tokenizer.nextToken();
        assert type == StreamTokenizer.TT_NUMBER : tokenizer.sval;
        float val = (float)tokenizer.nval * ( positive ? 1 : -1 );
        
        tokenizer.nextToken();
        
        if ( "E".equals( tokenizer.sval ) )
        {
            int exponent = integer();
            
            val = (float)( val * Math.pow( 10, exponent ) );
        }
        else
        {
            tokenizer.pushBack();
        }
        
        return ( val );
    }
}
