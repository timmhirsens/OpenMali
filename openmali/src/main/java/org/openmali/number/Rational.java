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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

/**
 * Represents a rational number. i.e. an integer over an integer. In order to represent a wide range of values,
 * the data is stored as x + a/b instead of just a/b. So the resultant range of values is
 * slightly wider than the normal integer range.
 * addition, subtraction,multiplication and division are defined.
 *
 * @author Tom Larkworthy
 */
public class Rational implements Comparable<Rational>, Serializable
{
    private static final long serialVersionUID = -9092546606834628521L;
    
    /**
     * should the rational numbers be simplified after every operation?
     */
    public static boolean AUTO_SIMPLIFY = true;
    //value at which approximations are forces, chosen as multiplication involve
    //multiplying ints, so they should be less than the square root to avoid overflow
    public static int APPROXIMATION_THRESHOLD = (int)Math.floor( Math.sqrt( Integer.MAX_VALUE / 2.0 ) );
    
    //the whole was interoduced to keep the value of the numerator small when large numbers are represented
    private int whole;// value = whole + num / den;
    private int num; //numerator
    private int den; //denominator, always posative
    public static final Rational ZERO = new Rational( 0, 0, 1 );
    public static final Rational ONE = new Rational( 1, 0, 1 );
    
    private static final Rational tmp = new Rational( 1, 0, 1 );
    
    public final Rational set( int whole, int num, int den )
    {
        this.whole = whole;
        this.num = num;
        this.den = den;
        check();
        autoSimplify();
        
        return ( this );
    }
    
    /**
     * initializes the rational to an APPROXIMATION of the value of the float parameter,
     * note you should avoid using this method where possible
     */
    public final void set( float value )
    {
        //use the fact we have a bottom level of precision
        float denf = APPROXIMATION_THRESHOLD;
        num = (int)( value * denf );
        den = APPROXIMATION_THRESHOLD;
        check();
        autoSimplify();
    }
    
    public final void set( Rational radical )
    {
        this.num = radical.num;
        this.den = radical.den;
        this.whole = radical.whole;
    }
    
    public final void negate()
    {
        this.whole = -whole;
        this.num = -num;
    }
    
    public final float floatValue()
    {
        return ( whole + ( (float)num ) / den );
    }
    
    public final int getDenominator()
    {
        return ( den );
    }
    
    public final int getWhole()
    {
        return ( whole );
    }
    
    public final int getNumerator()
    {
        return ( num );
    }
    
    /**
     * attepts to simplify the rational number by dividing by the greatest common devisor, note this is done
     * automatically if AUTO_SIMPLIFY is set to true
     */
    public void simplify()
    {
        int gcd = gcd( num, den );
        num /= gcd;
        den /= gcd;
        
        if ( den < 0 )
        {
            den = -den;
            num = -num;
        }
        
        if ( whole > 0 && num < 0 )
        {
            num = den + num;
            whole--;
        }
        
        if ( whole < 0 && num > 0 )
        {
            num = -( den - num );
            whole++;
        }
        
        if ( num > den )
        {
            whole += num / den;
            num = num % den;
        }
        
        check();
    }
    
    /**
     * performed after every operation, either a simplification is applied automatically
     * if the auto_simplify flag is true, or the expression is simplified if the numerator or denominator gets too
     * large (over the square root of the largest representable integer)
     */
    private void autoSimplify()
    {
        if ( AUTO_SIMPLIFY )
            simplify();
        
        if ( den > APPROXIMATION_THRESHOLD )
        {
            System.err.print( this + " had to be simplified to " );
            simplify();
            approx();
            System.err.println( this + " consider using floating point instead" );
        }
    }
    
    /**
     * ensures the num and den are withing approximation bounds, by rounding if necissary
     */
    private void approx()
    {
        while ( den > APPROXIMATION_THRESHOLD )
        {
            // we will try and get the exprssion to simplify by moving the num and den by increments of
            // 1, and try to minimize the damage to the number
            
            // work out analytical damage, x, of peturbing the number
            // num = a/b
            // 4 options, adding or subtracting 1 from a or b
            // 1: (a + 1) / b = a/b + 1/b
            // 2: (a - 1) / b = a/b - 1/b
            // 3: a / (b + 1) = a/b - a/(b(b+1))
            // 4: a / (b - 1) = a/b + a/(b(b-1))
            
            int a = Math.abs( num );
            int b = den;
            
            boolean aIsEven = a % 2 == 0;
            boolean bIsEven = b % 2 == 0;
            
            if ( aIsEven ^ bIsEven )
            {
                // XOR
                // if only one is odd
                // then we need to perturb just one of the parameters
                // we will choose the one that minimizes the damage
                // peterbing the nominator alters the number by 1/b
                // peterbing the denominator alters the number by either
                // a/(b(b+1)) or a/(b(b-1)
                // but as b is always posative, then option 3 is always better
                // eg 1/4, 1/5 or 1/3, 1/5 is nearer 1/4
                // so is adding 1 to the denominator
                // cause less damage than adding or subtracting one from the nominator?
                
                boolean option1DamIsLessThanOption3 = 1 < a / ( b + 1 );
                
                if ( option1DamIsLessThanOption3 )
                {
                    num++;
                }
                else
                {
                    den--;
                }
                
                //sanity checks
                float option1Dam = Math.abs( 1.0f / a );
                float option3Dam = Math.abs( a / ( b * ( (float)b + 1 ) ) );
                assert option1DamIsLessThanOption3 == option1Dam < option3Dam;
            }
            else
            {
                // both are odd (if they were both even , the sums would have simplified ealier)
                // both numerator and denominator need to be peterbed by one to make them
                // divisible by 2
                den++; // increasing denominator always does the minimum damage
                // and decreases the value of the sum
                num++; // counterbalances increase
            }
            
            check();
            simplify();
        }
    }
    
    /**
     * result = a+b
     * this operation can be performed in place i.e. the passback can be the same object as a or b
     */
    public static Rational add( Rational a, Rational b, Rational passback )
    {
        long al = a.num;
        long bl = a.den;
        long cl = b.num;
        long dl = b.den;
        
        assert Math.abs( al ) <= APPROXIMATION_THRESHOLD;
        assert Math.abs( bl ) <= APPROXIMATION_THRESHOLD;
        assert Math.abs( cl ) <= APPROXIMATION_THRESHOLD;
        assert Math.abs( dl ) <= APPROXIMATION_THRESHOLD;
        assert Math.abs( al * dl ) <= Integer.MAX_VALUE / 2;
        assert Math.abs( cl * bl ) <= Integer.MAX_VALUE / 2;
        assert Math.abs( al * dl + cl * bl ) <= Integer.MAX_VALUE;
        
        passback.num = a.num * b.den + b.num * a.den;
        passback.den = a.den * b.den;
        passback.whole = a.whole + b.whole;
        passback.check();
        passback.autoSimplify();
        
        return ( passback );
    }
    
    /**
     * result = a-b
     * this operation can be performed in place i.e. the passback can be the same object as a or b
     */
    public static Rational sub( Rational a, Rational b, Rational passback )
    {
        //(w + a/b) - (z + c/d) =
        //w - z + (a/b - c/d)
        //w - z + (ad/bd - cb/db)
        //w - z + ((ad- cb)/bd)
        //so we will simplify them is seperate Rational expressions and add them on later
        
        passback.num = a.num * b.den - b.num * a.den;
        passback.den = a.den * b.den;
        passback.whole = a.whole - b.whole;
        passback.check();
        passback.autoSimplify();
        
        return ( passback );
    }
    
    //ensures den is posative, and num < den
    private void check()
    {
        if ( den < 0 )
        {
            den = -den;
            num = -num;
        }
        
        if ( Math.abs( num ) >= den )
        {
            whole += num / den;
            num = num % den;
        }
        
        assert den > 0;
        assert num < den;
    }
    
    /**
     * result = a*b
     * this operation can be performed in place i.e. the passback can be the same object as a or b
     */
    public static Rational mul( Rational a, Rational b, Rational passback )
    {
        if ( passback == a )
        {
            tmp.set( a );
            a = tmp;
        }
        if ( passback == b )
        {
            tmp.set( b );
            b = tmp;
        }
        //(w + a/b) * (z + c/d) =
        //wz + ac/bd + w*(c/d) + z*(a/b)   <- last terms can easily cause overflow
        //so we will simplify them is seperate Rational expressions and add them on later
        //first we compute the first two terms which will not cause overflow:-
        passback.num = a.num * b.num;
        passback.den = a.den * b.den;
        passback.whole = a.whole * b.whole;
        passback.check();
        passback.autoSimplify();
        
        //calc 3rd term, and add on to result
        Rational term = new Rational( a.whole * b.num, b.den );
        add( passback, term, passback );
        //calc 4th term, and add on to result
        term.set( 0, b.whole * a.num, a.den );
        add( passback, term, passback );
        
        return ( passback );
    }
    
    /**
     * result = a/b
     * this operation can be performed in place i.e. the passback can be the same object as a or b
     */
    public static Rational div( Rational a, Rational b, Rational passback )
    {
        //(w + a/b) / (z + c/d) =
        //((wb + a)/b) * (d/(zd+c))
        //i.e.
        //(wb + a)*d
        //---------
        //b*(zd+c)
        
        //but both denominator or numerator may overflow
        //terms (wb + a) and  (zd+c) on their own do not
        
        int n1 = a.whole * a.den + a.num; //should not overflow (checked in asserts below)
        int n2 = b.den;
        int d1 = a.den;
        int d2 = b.whole * b.den + b.num;
        
        assert (long)a.num + (long)a.whole * (long)a.den <= Integer.MAX_VALUE;
        assert (long)b.num + (long)b.whole * (long)b.den <= Integer.MAX_VALUE;
        
        //result will be n1*n2 / d1*d2
        //we will associate the big numbers into one rational
        //and the small numbers, some simplification will result, but its the most likely to remain as accurate
        Rational bigNumbers = new Rational( n1, d2 );
        Rational smallNumbersInv = new Rational( n2, d1 );
        
        //then we multiply the two together to obtain the result of the division
        
        mul( bigNumbers, smallNumbersInv, passback );
        
        return ( passback );
    }
    
    /**
     * finds the greatest common divisor of two integers
     *
     * @return greatest common divosor of the two arguments
     * @author Jens Chr. Godskesen
     */
    public static int gcd( int a, int b )
    {
        while ( b != 0 )
        {
            int c = a % b;
            a = b;
            b = c;
        }
        
        return ( a );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Rational clone()
    {
        return ( new Rational( this ) );
    }
    
    /**
     * returns a string representation of the number, note: this is compatable with
     * parseRational and the Parser class
     * 
     * @return the string representation
     */
    @Override
    public String toString()
    {
        if ( num == 0 )
        {
            return ( String.valueOf( whole ) );
        }
        else if ( den == 1 )
        {
            return ( whole + num + "" );
        }
        else if ( whole == 0 )
        {
            return ( num + "/" + den );
        }
        else
        {
            return ( "(" + whole + ( num > 0 ? "+" : "" ) + num + "/" + den + ")" );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return ( true );
        
        if ( o == null || getClass() != o.getClass() )
            return ( false );
        
        Rational rational = (Rational)o;
        
        if ( den != rational.den )
            return ( false );
        if ( num != rational.num )
            return ( false );
        if ( whole != rational.whole )
            return ( false );
        
        return ( true );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        int result;
        result = whole;
        result = 31 * result + num;
        result = 31 * result + den;
        
        return ( result );
    }
    
    public int compareTo( Rational o )
    {
        long a = num + whole * den;
        int b = den;
        long c = o.num + o.whole * o.den;
        int d = o.den;
        
        if ( b * c == a * d )
            return ( 0 );
        else if ( ( b * d > 0 && a * d < b * c ) || ( b * d < 0 && a * d > b * c ) )
            return ( -1 );
        
        return ( 1 );
    }
    
    public Rational( int num, int den )
    {
        set( 0, num, den );
    }
    
    public Rational()
    {
        this.den = 1;
    }
    
    /**
     * this = whole + num/den
     */
    public Rational( int whole, int num, int den )
    {
        set( whole, num, den );
    }
    
    /**
     * initializes the rational to an APPROXIMATION of the value of the float parameter,
     * note you should avoid using this method where possible
     *
     * @param val
     */
    public Rational( float val )
    {
        set( val );
    }
    
    public Rational( int val )
    {
        this.den = 1;
        this.num = 0;
        this.whole = val;
    }
    
    public Rational( Rational val )
    {
        this.whole = val.whole;
        this.den = val.den;
        this.num = val.num;
    }
    
    /**
     * Reads a single rational number from a string
     * 
     * @return the parsed Rational
     */
    public static Rational parseRational( String string )
    {
        try
        {
            StringReader in = new StringReader( string );
            Rational result = new Parser( in ).rational();
            in.close();
            
            return ( result );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        
        return ( null );
    }
    
    /*
    public static boolean testRndAddition()
    {
        float a = (float)( Math.random() * 10000 - 5000 );
        float b = (float)( Math.random() * 10000 - 5000 );
        
        Rational Ar = new Rational( a );
        Rational Br = new Rational( b );
        
        Rational result = add( Ar, Br, new Rational() );
        float r = a + b;
        System.out.println( a + " + " + b + " = " + r + "~=" );
        System.out.println( Ar + " + " + Br + " = " + result );
        System.out.println( Ar.floatValue() + " + " + Br.floatValue() + " = " + result.floatValue() );
        
        boolean passed = Math.abs( r - result.floatValue() ) < .001f;
        System.out.println( "passed? " + passed );
        
        return ( passed );
    }
    
    public static boolean testRndSubtraction()
    {
        float a = (float)( Math.random() * 10000 - 5000 );
        float b = (float)( Math.random() * 10000 - 5000 );
        
        Rational Ar = new Rational( a );
        Rational Br = new Rational( b );
        
        Rational result = sub( Ar, Br, new Rational() );
        float r = a - b;
        System.out.println( a + " - " + b + " = " + r + "~=" );
        System.out.println( Ar + " - " + Br + " = " + result );
        System.out.println( Ar.floatValue() + " - " + Br.floatValue() + " = " + result.floatValue() );
        
        boolean passed = Math.abs( r - result.floatValue() ) < .001f;
        System.out.println( "passed? " + passed );
        
        return ( passed );
    }
    
    public static boolean testRndMultiplication()
    {
        float a = (float)( Math.random() * 10000 - 5000 );
        float b = (float)( Math.random() * 10000 - 5000 );
        
        Rational Ar = new Rational( a );
        Rational Br = new Rational( b );
        
        Rational result = mul( Ar, Br, new Rational() );
        float r = a * b;
        System.out.println( a + " * " + b + " = " + r + "~=" );
        System.out.println( Ar + " * " + Br + " = " + result );
        System.out.println( Ar.floatValue() + " * " + Br.floatValue() + " = " + result.floatValue() );
        
        boolean passed = Math.abs( r - result.floatValue() ) < 10f;
        System.out.println( "passed? " + passed );
        
        return ( passed );
    }
    
    public static boolean testRndDivision()
    {
        float a = (float)( Math.random() * 10000 - 5000 );
        float b = (float)( Math.random() * 10000 - 5000 );
        
        Rational Ar = new Rational( a );
        Rational Br = new Rational( b );
        
        Rational result = div( Ar, Br, new Rational() );
        float r = a / b;
        System.out.println( a + " / " + b + " = " + r + "~=" );
        System.out.println( Ar + " / " + Br + " = " + result );
        System.out.println( Ar.floatValue() + " / " + Br.floatValue() + " = " + result.floatValue() );
        
        boolean passed = Math.abs( r - result.floatValue() ) < .1f;
        System.out.println( "passed? " + passed );
        
        return ( passed );
    }
    
    public static boolean testRndComparison()
    {
        Rational Ar = new Rational( (int)( Math.random() * 10 ) + 1, (int)( Math.random() * 10 ) + 1 );
        Rational Br = new Rational( (int)( Math.random() * 10 ) + 1, (int)( Math.random() * 10 ) + 1 );
        
        boolean passed = Ar.compareTo( Br ) == Float.compare( Ar.floatValue(), Br.floatValue() );
        System.out.println( "Ar = " + Ar.floatValue() );
        System.out.println( "Br = " + Br.floatValue() );
        System.out.println( Ar + " < " + Br + "? " + Ar.compareTo( Br ) );
        
        return ( passed );
    }
    
    public static boolean testFloatApprox()
    {
        float val = (float)( Math.random() * 10000 - 5000 );
        
        Rational rat = new Rational( val );
        
        float tst = rat.floatValue();
        
        System.out.println( "val = " + val );
        System.out.println( "tst = " + tst );
        boolean passed = Math.abs( tst - val ) < .001f;
        System.out.println( "passed? " + passed );
        
        return ( passed );
    }
    
    public static void main( String[] args )
    {
        for ( int i = 0; i < 1000; i++ )
        {
            assert testFloatApprox();
        }
        for ( int i = 0; i < 1000; i++ )
        {
            assert testRndAddition();
        }
        for ( int i = 0; i < 1000; i++ )
        {
            assert testRndSubtraction();
        }
        
        for ( int i = 0; i < 1000; i++ )
        {
            assert testRndMultiplication();
        }
        
        for ( int i = 0; i < 1000; i++ )
        {
            assert testRndDivision();
        }
        for ( int i = 0; i < 1000; i++ )
        {
            assert testRndComparison();
        }
    }
    */
}
