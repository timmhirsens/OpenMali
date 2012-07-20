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
package org.openmali.spatial.bodies;

import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple3f;

/**
 * An Axis-Aligned Box.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public class Box extends Body implements java.io.Serializable
{
	private static final long serialVersionUID = -6277131116550693278L;
    
    /**
     * The lower corner of this bounding box
     */
    protected Point3f lower = new Point3f();
    
    /**
     * The upper corner of this bounding box
     */
    protected Point3f upper = new Point3f();
    
    // just for temporary use!
    private Tuple3f size = new Tuple3f();
    
    /**
     * {@inheritDoc}
     */
    public final boolean contains( float px, float py, float pz )
    {
        return ( Classifier.classifyBoxPoint( this, px, py, pz ) == Classifier.Classification.INSIDE );
    }
    
    /**
     * {@inheritDoc}
     */
    public final boolean contains( Point3f point )
    {
        return ( contains( point.getX(), point.getY(), point.getZ() ) );
    }
    
    private final void check()
    {
        /*
        if (upper.x < lower.x) throw new Error( "Improper Box values" ) );
        if (upper.y < lower.y) throw new Error("Improper Box values" ) );
        if (upper.z < lower.z) throw new Error("Improper Box values" ) );
        */
    }
    
    protected void onBoundsChanged()
    {
        setMaxCenterDistanceSquared( this.lower.distanceSquared( centerX, centerX, centerZ ) );
    }
    
    protected final void calcCenter()
    {
        setCenter( ( lower.getX() + upper.getX() ) / 2f,
                   ( lower.getY() + upper.getY() ) / 2f,
                   ( lower.getZ() + upper.getZ() ) / 2f
                 );
        
        onBoundsChanged();
    }
    
    public final void set( float lowerX, float lowerY, float lowerZ, float upperX, float upperY, float upperZ )
    {
        this.lower.set( lowerX, lowerY, lowerZ );
        this.upper.set( upperX, upperY, upperZ );
        
        calcCenter();
        
        check();
    }
    
    public final void set( Tuple3f lower, Tuple3f upper )
    {
        this.lower.set( lower );
        this.upper.set( upper );
        
        calcCenter();
        
        check();
    }
    
    public final void setLower( float x, float y, float z )
    {
        lower.set( x, y, z );
        
        calcCenter();
        
        check();
    }
    
    /**
     * Sets the lower corner of this bounding box
     */
    public final void setLower( Tuple3f point )
    {
        setLower( point.getX(), point.getY(), point.getZ() );
    }
    
    /**
     * Sets the x coordinate of the lower corner of this Box.
     * 
     * @param x
     */
    public final void setLowerX( float x )
    {
        lower.setX( x );
        
        calcCenter();
    }
    
    /**
     * Sets the y coordinate of the lower corner of this Box.
     * 
     * @param y
     */
    public final void setLowerY( float y )
    {
        lower.setY( y );
        
        calcCenter();
    }
    
    /**
     * Sets the z coordinate of the lower corner of this Box.
     * 
     * @param z
     */
    public final void setLowerZ( float z )
    {
        lower.setZ( z );
        
        calcCenter();
    }
    
    /**
     * @return the lower corner of this Box.
     */
    public final Point3f getLower()
    {
        return ( lower );
    }
    
    public final void getLower( Tuple3f point )
    {
        point.set( lower );
    }
    
    /**
     * @return the x coordinate of the lower corner of this Box.
     */
    public final float getLowerX()
    {
        return ( lower.getX() );
    }
    
    /**
     * @return the y coordinate of the lower corner of this Box.
     */
    public final float getLowerY()
    {
        return ( lower.getY() );
    }
    
    /**
     * @return the z coordinate of the lower corner of this Box.
     */
    public final float getLowerZ()
    {
        return ( lower.getZ() );
    }
    
    public final void setUpper( float x, float y, float z )
    {
        upper.set( x, y, z );
        
        calcCenter();
        
        check();
    }
    
    /**
     * Sets the upper corner of this box
     */
    public final void setUpper( Tuple3f point )
    {
        setUpper( point.getX(), point.getY(), point.getZ() );
    }
    
    /**
     * Sets the x coordinate of the upper corner of this Box.
     * 
     * @param x
     */
    public final void setUpperX( float x )
    {
        upper.setX( x );
        
        calcCenter();
    }
    
    /**
     * Sets the y coordinate of the upper corner of this Box.
     * 
     * @param y
     */
    public final void setUpperY( float y )
    {
        upper.setY( y );
        
        calcCenter();
    }
    
    /**
     * Sets the z coordinate of the upper corner of this Box.
     * 
     * @param z
     */
    public final void setUpperZ( float z )
    {
        upper.setZ( z );
        
        calcCenter();
    }
    
    /**
     * @return the upper corner of this box.
     */
    public final Point3f getUpper()
    {
        return ( upper );
    }
    
    public final void getUpper( Tuple3f point )
    {
        point.set( upper );
    }
    
    /**
     * @return the x coordinate of the upper corner of this Box.
     */
    public final float getUpperX()
    {
        return ( upper.getX() );
    }
    
    /**
     * @return the y coordinate of the upper corner of this Box.
     */
    public final float getUpperY()
    {
        return ( upper.getY() );
    }
    
    /**
     * @return the z coordinate of the upper corner of this Box.
     */
    public final float getUpperZ()
    {
        return ( upper.getZ() );
    }
    
    public final float getXSpan()
    {
        return ( upper.getX() - lower.getX() );
    }
    
    public final float getYSpan()
    {
        return ( upper.getY() - lower.getY() );
    }
    
    public final float getZSpan()
    {
        return ( upper.getZ() - lower.getZ() );
    }
    
    /**
     * Moves this Box to a position, where it's center is at the given location.
     * 
     * @param x
     * @param y
     * @param z
     */
    @Override
    public final void setCenter( float x, float y, float z )
    {
        final float halfXSpan = getXSpan() / 2f;
        final float halfYSpan = getYSpan() / 2f;
        final float halfZSpan = getZSpan() / 2f;
        
        lower.set( x - halfXSpan,
                   y - halfYSpan,
                   z - halfZSpan
                 );
        
        upper.set( x + halfXSpan,
                   y + halfYSpan,
                   z + halfZSpan
                 );
        
        super.setCenter( x, y, z );
    }
    
    /**
     * Resizes this Box to the given size.
     * 
     * @param xSpan
     * @param ySpan
     * @param zSpan
     */
    public void setSize( float xSpan, float ySpan, float zSpan )
    {
        final float halfSizeX = xSpan / 2f;
        final float halfSizeY = ySpan / 2f;
        final float halfSizeZ = zSpan / 2f;
        
        lower.set( centerX - halfSizeX, centerY - halfSizeY, centerZ - halfSizeZ );
        upper.set( centerX + halfSizeX, centerY + halfSizeY, centerZ + halfSizeZ );
        
        onBoundsChanged();
    }
    
    /**
     * Resizes this Box to the given size.
     */
    public final void setSize( Tuple3f span )
    {
        setSize( span.getX(), span.getY(), span.getZ() );
    }
    
    /**
     * Computes the x-, y-, and z-span of this Box and puts the values into <code>out</code>.
     * 
     * @param out
     */
    public final < T extends Tuple3f > T getSize( T out )
    {
        out.set( getXSpan(), getYSpan(), getZSpan() );
        
        return ( out );
    }
    
    /**
     * Computes the x-, y-, and z-span of this Box and puts the values into <code>out</code>.
     */
    public final Tuple3f getSize()
    {
        size.set( getXSpan(), getYSpan(), getZSpan() );
        
        return ( size );
    }
    
    /**
     * {@inheritDoc}
     */
    public final void combine( BodyInterface body )
    {
        if ( body instanceof Box )
        {
            Box box = (Box)body;
            combine_( box.getLower() );
            combine_( box.getUpper() );
            
            calcCenter();
        }
        else if ( body instanceof Sphere )
        {
            Sphere s = (Sphere)body;
            combine_( s.getCenterX() - s.getRadius(), s.getCenterY() - s.getRadius(), s.getCenterZ() - s.getRadius() );
            combine_( s.getCenterX() + s.getRadius(), s.getCenterY() + s.getRadius(), s.getCenterZ() + s.getRadius() );
            
            calcCenter();
        }
        else if ( body instanceof ConvexHull )
        {
            throw new Error( "ConvexHull not supported yet" );
        } 
        else
        {
            throw new Error( "Unknown Body type" );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public final void combine( BodyInterface[] bodies )
    {
        for ( int i = 0; i < bodies.length; i++ )
        {
            combine( bodies[ i ] );
        }
    }
    
    protected final void combine_( float x, float y, float z )
    {
        if ( x < lower.getX() )
            lower.setX( x );
        else if ( x > upper.getX() )
            upper.setX( x );
        
        if ( y < lower.getY() )
            lower.setY( y );
        else if ( y > upper.getY() )
            upper.setY( y );
        
        if ( z < lower.getZ() )
            lower.setZ( z );
        else if ( z > upper.getZ() )
            upper.setZ( z );
    }
    
    /**
     * {@inheritDoc}
     */
    public final void combine( float x, float y, float z )
    {
        combine_( x, y, z );
        
        calcCenter();
    }
    
    protected final void combine_( Point3f p )
    {
        combine_( p.getX(), p.getY(), p.getZ() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final void combine( Point3f p )
    {
        combine( p.getX(), p.getY(), p.getZ() );
    }
    
    /**
     * {@inheritDoc}
     */
    public final void combine( Point3f[] points )
    {
        for ( int i = 0; i < points.length; i++ )
        {
            combine_( points[ i ] );
        }
        
        calcCenter();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer( 128 );
        sb.append( this.getClass().getSimpleName() );
        sb.append( " { lower: (" );
        sb.append( lower.getX() );
        sb.append( ", " );
        sb.append( lower.getY() );
        sb.append( ", " );
        sb.append( lower.getZ() );
        sb.append( "), upper: (" );
        sb.append( upper.getX() );
        sb.append( ", " );
        sb.append( upper.getY() );
        sb.append( ", " );
        sb.append( upper.getZ() );
        sb.append( "), span: (" );
        sb.append( getXSpan() );
        sb.append( ", " );
        sb.append( getYSpan() );
        sb.append( ", " );
        sb.append( getZSpan() );
        sb.append( ") }" );
        
        return ( sb.toString() );
    }
    
    /**
     * Creates a new Box
     */
    public Box( float lowerX, float lowerY, float lowerZ, float upperX, float upperY, float upperZ )
    {
        super();
        
        lower.set( lowerX, lowerY, lowerZ );
        upper.set( upperX, upperY, upperZ );
        
        calcCenter();
    }
    
    /**
     * Creates a new Box
     */
    public Box( Tuple3f lower, Tuple3f upper )
    {
        this( lower.getX(), lower.getY(), lower.getZ(), upper.getX(), upper.getY(), upper.getZ() );
    }
    
    /**
     * Creates a new Box
     */
    public Box()
    {
        this( 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f );
    }
}
