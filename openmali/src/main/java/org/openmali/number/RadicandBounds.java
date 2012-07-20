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

import java.util.HashMap;

/**
 * This class stores a cache of known (inclusive) rational bounds for sqrt radicands.
 * We keep a cache becuase we don't expect a program to have that many different
 * options of radicands used.
 * 
 * NOT IMPLEMENTED: improving bounds at runtime using bisection methodology
 * 
 * @author Tom Larkworthy
 */
public class RadicandBounds
{
    public static HashMap<Integer, Rational> upperBounds = new HashMap<Integer, Rational>();
    public static HashMap<Integer, Rational> lowerBounds = new HashMap<Integer, Rational>();
    
    public static final Rational tmp1 = new Rational();
    public static final Rational tmp2 = new Rational();
    
    public static final int PRECISION = 100;
    
    public static void getBounds( Integer radicand, Rational upperPassback, Rational lowerPassback )
    {
        if ( radicand == 1 )
        {
            upperPassback.set( 1, 0, 1 );
            lowerPassback.set( 1, 0, 1 );
            return;
        }
        
        assert radicand >= 1;
        Rational upper = upperBounds.get( radicand );
        if ( upper == null )
        {
            upper = new Rational( (int)( Math.sqrt( radicand ) * PRECISION ) + 1, PRECISION );
            
            assert upper.floatValue() > Math.sqrt( radicand );
            upperBounds.put( radicand, upper );
        }
        
        Rational lower = lowerBounds.get( radicand );
        if ( lower == null )
        {
            lower = new Rational( (int)( Math.sqrt( radicand ) * PRECISION ), PRECISION );
            
            assert lower.floatValue() < Math.sqrt( radicand );
            lowerBounds.put( radicand, lower );
        }
        
        upperPassback.set( upper );
        lowerPassback.set( lower );
    }
    
    public static void main( String[] args )
    {
        Rational upper = new Rational();
        Rational lower = new Rational();
        
        int radicand = 3;
        System.out.println( "Math.sqrt(radicand) = " + Math.sqrt( radicand ) );
        
        getBounds( radicand, upper, lower );
        
        System.out.println( "upper = " + upper );
        System.out.println( "lower = " + lower );
        System.out.println( "upper = " + upper.floatValue() );
        System.out.println( "lower = " + lower.floatValue() );
    }
}
