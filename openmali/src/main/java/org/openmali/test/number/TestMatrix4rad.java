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
package org.openmali.test.number;

import org.openmali.number.Radical1;
import org.openmali.number.RadicalUtils;
import org.openmali.number.matrix.Matrix4rad;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.AxisAngle3f;
import org.openmali.vecmath2.Vector3f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.FastMath;

import java.util.*;

/**
 */
public class TestMatrix4rad extends TestCase
{
    private static final int ITERATIONS = 1000;
    
    public static final Collection<Float> angleOptions = new ArrayList<Float>();
    static
    {
        angleOptions.add( -FastMath.PI_HALF );
        angleOptions.add( -FastMath.PI / 3 );
        angleOptions.add( -FastMath.PI / 6 );
        angleOptions.add( 0.0f );
        angleOptions.add( FastMath.PI_HALF );
        angleOptions.add( FastMath.PI / 3 );
        angleOptions.add( FastMath.PI / 6 );
    }
    
    public static Vector3f randomPrincipleAxis()
    {
        Vector3f ret = new Vector3f();
        
        ret.setValue( FastMath.randomInt( 3 ), 1 );
        
        return ( ret );
    }
    
    public static Vector3f randomPrincipleHalfAxis()
    {
        Vector3f ret = new Vector3f();
        
        ret.setValue( FastMath.randomInt( 3 ), FastMath.randomInt( 2 ) * 2 - 1 );
        
        return ( ret );
    }
    
    private static Object selectRandom( Collection<?> c )
    {
        int selection = FastMath.randomInt( c.size() );
        Iterator<?> iter = c.iterator();
        Object res = null;
        for ( int i = 0; i <= selection; i++ )
        {
            res = iter.next();
        }
        
        return ( res );
    }
    
    /**
     * creates a random radical matrix4, it also provides the floating point equivelent
     * @param floatPassback can be null if not needed
     * @return
     */
    public static Matrix4rad randomMatrix4( Matrix4f floatPassback )
    {
        Matrix4rad result = new Matrix4rad();
        
        Matrix4f seed = new Matrix4f();
        seed.setIdentity();
        
        //set the rotation to be a random agle (tat can be approximated by a radical
        //in a random principle axis
        seed.setRotation( new AxisAngle3f( randomPrincipleAxis(), (Float)selectRandom( angleOptions ) ) );
        
        seed.setTranslation( new Tuple3f( ( (Radical1)selectRandom( RadicalUtils.approximators ) ).floatValue(), ( (Radical1)selectRandom( RadicalUtils.approximators ) ).floatValue(), ( (Radical1)selectRandom( RadicalUtils.approximators ) ).floatValue() ) );
        
        for ( int i = 0; i < 4; i++ )
        {
            for ( int j = 0; j < 4; j++ )
            {
                result.set( i, j, RadicalUtils.convert( seed.get( i, j ) ) );
            }
        }
        
        if ( floatPassback != null )
            floatPassback.set( seed );
        
        return ( result );
    }
    
    public void testDet()
    {
        for ( int i = 0; i < ITERATIONS; i++ )
        {
            Matrix4f verification = new Matrix4f();
            Matrix4rad test = randomMatrix4( verification );
            
            if ( i % 2 == 0 )
            {
                verification.transpose();
                test.transpose();
            }
            
            float ver = verification.determinant();
            Radical1 tst = test.determinant();
            
            System.out.println( "test = " + test );
            System.out.println( "verification = " + verification );
            
            System.out.println( "tst = " + tst );
            System.out.println( "ver = " + ver );
            
            assertTrue( FastMath.epsilonEquals( tst.floatValue(), ver, 0.0001f ) );
        }
    }
    
    public void testInv()
    {
        for ( int i = 0; i < ITERATIONS; i++ )
        {
            Matrix4f verification = new Matrix4f();
            Matrix4rad test = randomMatrix4( verification );
            
            if ( i % 2 == 0 )
            {
                verification.transpose();
                test.transpose();
            }
            
            verification.invert();
            test.invert();
            
            System.out.println( "test = " + test );
            System.out.println( "verification = " + verification );
            
            assertTrue( test.epsilonEquals( verification, 0.0001f ) );
        }
    }
    
    public void testParse()
    {
        for ( int i = 0; i < ITERATIONS; i++ )
        {
            Matrix4rad m = randomMatrix4( null );
            
            Matrix4rad t = Matrix4rad.parseMatrix4rad( m.toString() );
            
            assertEquals( m, t );
        }
    }
}
