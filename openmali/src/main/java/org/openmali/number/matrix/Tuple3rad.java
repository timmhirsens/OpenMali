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

import org.openmali.number.Radical1;

/**
 * @author Tom Larkworthy
 */
public class Tuple3rad extends TupleNrad<Tuple3rad>
{
    public static final Tuple3rad X_POS;
    public static final Tuple3rad X_NEG;
    public static final Tuple3rad Y_POS;
    public static final Tuple3rad Y_NEG;
    public static final Tuple3rad Z_POS;
    public static final Tuple3rad Z_NEG;
    
    private static final Radical1 op1 = new Radical1();
    private static final Radical1 op2 = new Radical1();
    private static final Radical1 op3 = new Radical1();
    private static final Radical1 op4 = new Radical1();
    private static final Radical1 op5 = new Radical1();
    private static final Radical1 op6 = new Radical1();
    private static final Radical1 op7 = new Radical1();
    private static final Radical1 op8 = new Radical1();
    private static final Radical1 op9 = new Radical1();
    
    /**
     * a read only structure providing integer access to the 6 principle cartesian direcion tuples
     * in the order
     * 0 = X_POS
     * 1 = X_NEG
     * 2 = Y_POS
     * 3 = Y_NEG
     * 4 = Z_POS
     * 5 = Z_NEG
     */
    public static final Tuple3rad[] CARTESIAN_DIRS;
    static
    {
        Tuple3rad xPos = new Tuple3rad();
        Tuple3rad xNeg = new Tuple3rad();
        Tuple3rad yPos = new Tuple3rad();
        Tuple3rad yNeg = new Tuple3rad();
        Tuple3rad zPos = new Tuple3rad();
        Tuple3rad zNeg = new Tuple3rad();
        
        xPos.set( 0, new Radical1( 1, 1, 1 ) );
        xNeg.set( 0, new Radical1( -1, 1, 1 ) );
        yPos.set( 1, new Radical1( 1, 1, 1 ) );
        yNeg.set( 1, new Radical1( -1, 1, 1 ) );
        zPos.set( 2, new Radical1( 1, 1, 1 ) );
        zNeg.set( 2, new Radical1( -1, 1, 1 ) );
        
        X_POS = new Tuple3rad( true, xPos );
        X_NEG = new Tuple3rad( true, xNeg );
        Y_POS = new Tuple3rad( true, yPos );
        Y_NEG = new Tuple3rad( true, yNeg );
        Z_POS = new Tuple3rad( true, zPos );
        Z_NEG = new Tuple3rad( true, zNeg );
        
        CARTESIAN_DIRS = new Tuple3rad[ 6 ];
        CARTESIAN_DIRS[ 0 ] = X_POS;
        CARTESIAN_DIRS[ 1 ] = X_NEG;
        CARTESIAN_DIRS[ 2 ] = Y_POS;
        CARTESIAN_DIRS[ 3 ] = Y_NEG;
        CARTESIAN_DIRS[ 4 ] = Z_POS;
        CARTESIAN_DIRS[ 5 ] = Z_NEG;
    }
    
    public final void set( Radical1 x, Radical1 y, Radical1 z )
    {
        values[ 0 ].set( x );
        values[ 1 ].set( y );
        values[ 2 ].set( z );
    }
    
    public final Radical1 getX( Radical1 passback )
    {
        values[ 0 ].get( passback );
        
        return ( passback );
    }
    
    public final Radical1 getY( Radical1 passback )
    {
        values[ 1 ].get( passback );
        
        return ( passback );
    }
    
    public Radical1 getZ( Radical1 passback )
    {
        values[ 2 ].get( passback );
        
        return ( passback );
    }
    
    public final Radical1 getXR()
    {
        return ( values[ 0 ] );
    }
    
    public final Radical1 getYR()
    {
        return ( values[ 1 ] );
    }
    
    public final Radical1 getZR()
    {
        return ( values[ 2 ] );
    }
    
    public static final Tuple3rad cross( Tuple3rad a, Tuple3rad b, Tuple3rad result )
    {
        Tuple3rad v1 = a;//found cross product in wrong direction so flip
        Tuple3rad v2 = b;
        // i.e. safe when a.cross(a, b)
        result.set( Radical1.sub( Radical1.mul( v1.getYR(), v2.getZR(), op1 ), Radical1.mul( v1.getZR(), v2.getYR(), op2 ), op3 ), Radical1.sub( Radical1.mul( v1.getZR(), v2.getXR(), op4 ), Radical1.mul( v1.getXR(), v2.getZR(), op5 ), op6 ), Radical1.sub( Radical1.mul( v1.getXR(), v2.getYR(), op7 ), Radical1.mul( v1.getYR(), v2.getXR(), op8 ), op9 ) );
        
        return ( result );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple3rad clone()
    {
        return ( new Tuple3rad( this ) );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        
        Radical1 tmp = new Radical1();
        
        buf.append( "(" );
        buf.append( get( 0, tmp ) ).append( "," );
        buf.append( get( 1, tmp ) ).append( "," );
        buf.append( get( 2, tmp ) );
        buf.append( ")" );
        
        return ( buf.toString() );
    }
    
    protected Tuple3rad( boolean readOnly )
    {
        super( readOnly, 3 );
    }
    
    protected Tuple3rad( boolean readOnly, Tuple3rad value )
    {
        super( readOnly, value );
    }
    
    public Tuple3rad()
    {
        this( false );
    }
    
    public Tuple3rad( Tuple3rad value )
    {
        this( false, value );
    }
    
    public Tuple3rad( Radical1 x, Radical1 y, Radical1 z )
    {
        this();
        set( x, y, z );
    }
    
    public static final Tuple3rad newReadOnly( Tuple3rad value )
    {
        return ( new Tuple3rad( true, value ) );
    }
}
