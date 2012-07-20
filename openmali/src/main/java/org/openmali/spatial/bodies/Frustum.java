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

import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Vector3f;

/**
 * A viewing frustum.
 * 
 * @author cas
 * @author Marvin Froehlich (aka Qudus)
 */
public class Frustum
{
    private final Plane planeRight  = new Plane();
    private final Plane planeLeft   = new Plane();
    private final Plane planeBottom = new Plane();
    private final Plane planeTop    = new Plane();
    private final Plane planeFar    = new Plane();
    private final Plane planeNear   = new Plane();
    
    private final Matrix4f matrix = new Matrix4f();
    
    public Matrix4f getMatrix()
    {
        return ( matrix );
    }
    
    public final void setPlaneRight( Plane plane )
    {
        this.planeRight.set( plane );
    }
    
    public final Plane getPlaneRight()
    {
        return ( planeRight );
    }
    
    public final Plane getPlaneRight( Plane plane )
    {
        plane.set( planeRight );
        
        return ( plane );
    }
    
    public final void setPlaneLeft( Plane plane )
    {
        this.planeLeft.set( plane );
    }
    
    public final Plane getPlaneLeft()
    {
        return ( planeLeft );
    }
    
    public final Plane getPlaneLeft( Plane plane )
    {
        plane.set( planeLeft );
        
        return ( plane );
    }
    
    public final void setPlaneBottom( Plane plane )
    {
        this.planeBottom.set( plane );
    }
    
    public final Plane getPlaneBottom()
    {
        return ( planeBottom );
    }
    
    public final Plane getPlaneBottom( Plane plane )
    {
        plane.set( planeBottom );
        
        return ( plane );
    }
    
    public final void setPlaneTop( Plane plane )
    {
        this.planeTop.set( plane );
    }
    
    public final Plane getPlaneTop()
    {
        return ( planeTop );
    }
    
    public final Plane getPlaneTop( Plane plane )
    {
        plane.set( planeTop );
        
        return ( plane );
    }
    
    public final void setPlaneFar( Plane plane )
    {
        this.planeFar.set( plane );
    }
    
    public final Plane getPlaneFar()
    {
        return ( planeFar );
    }
    
    public final Plane getPlaneFar( Plane plane )
    {
        plane.set( planeFar );
        
        return ( plane );
    }
    
    public final void setPlaneNear( Plane plane )
    {
        this.planeNear.set( plane );
    }
    
    public final Plane getPlaneNear()
    {
        return ( planeNear );
    }
    
    public final Plane getPlaneNear( Plane plane )
    {
        plane.set( planeNear );
        
        return ( plane );
    }
    
    /**
     * Quick check to see if an orthogonal bounding box is inside the frustum
     */
    private static final boolean quickClassify( Plane p, Box box )
    {
        if ( p.distanceTo( box.getLowerX(), box.getLowerY(), box.getLowerZ() ) > 0.0f )
            return ( true );
        if ( p.distanceTo( box.getUpperX(), box.getLowerY(), box.getLowerZ() ) > 0.0f )
            return ( true );
        if ( p.distanceTo( box.getLowerX(), box.getUpperY(), box.getLowerZ() ) > 0.0f )
            return ( true );
        if ( p.distanceTo( box.getUpperX(), box.getUpperY(), box.getLowerZ() ) > 0.0f )
            return ( true );
        if ( p.distanceTo( box.getLowerX(), box.getLowerY(), box.getUpperZ() ) > 0.0f )
            return ( true );
        if ( p.distanceTo( box.getUpperX(), box.getLowerY(), box.getUpperZ() ) > 0.0f )
            return ( true );
        if ( p.distanceTo( box.getLowerX(), box.getUpperY(), box.getUpperZ() ) > 0.0f )
            return ( true );
        if ( p.distanceTo( box.getUpperX(), box.getUpperY(), box.getUpperZ() ) > 0.0f )
            return ( true );
        
        return ( false );
    }
    
    /**
     * Quick check to see if an orthogonal bounding box is inside the frustum
     */
    public final Classifier.Classification quickClassify( Box box )
    {
        if ( !quickClassify( planeRight, box ) )
            return ( Classifier.Classification.OUTSIDE );
        if ( !quickClassify( planeLeft, box ) )
            return ( Classifier.Classification.OUTSIDE );
        if ( !quickClassify( planeBottom, box ) )
            return ( Classifier.Classification.OUTSIDE );
        if ( !quickClassify( planeTop, box ) )
            return ( Classifier.Classification.OUTSIDE );
        if ( !quickClassify( planeFar, box ) )
            return ( Classifier.Classification.OUTSIDE );
        if ( !quickClassify( planeNear, box ) )
            return ( Classifier.Classification.OUTSIDE );
        
        // We make no attempt to determine whether it's fully inside or not.
        return ( Classifier.Classification.SPANNING );
    }
    
    /**
     * Intersect the frustum with a plane. The result is returned in a set of points which
     * make a quadrilateral. If the frustum does not intersect the plane then the function
     * 
     * @param p
     * @param quad
     * 
     * @return false and the points are left untouched.
     * 
     * The array of points passed in must have a size equal to 4.
     */
    public final boolean intersects( Plane p, Vector3f[] quad )
    {
        return ( false );
    }
    
    /**
     * Extract the frustum from the incoming projections and modelview matrices.
     */
    public final void compute( Matrix4f proj, Matrix4f modl )
    {
        //matrix.set( proj );
        matrix.mul( proj, modl );
        
        // Now get the frustum's 6 clipping planes
        
        // Extract the numbers for the RIGHT plane
        planeRight.set( matrix.m03() - matrix.m00(),
                        matrix.m13() - matrix.m10(),
                        matrix.m23() - matrix.m20(),
                        matrix.m33() - matrix.m30()
                      );
        
        // Extract the numbers for the LEFT plane
        planeLeft.set( matrix.m03() + matrix.m00(),
                       matrix.m13() + matrix.m10(),
                       matrix.m23() + matrix.m20(),
                       matrix.m33() + matrix.m30()
                     );
        
        // Extract the BOTTOM plane
        planeBottom.set( matrix.m03() + matrix.m01(),
                         matrix.m13() + matrix.m11(),
                         matrix.m23() + matrix.m21(),
                         matrix.m33() + matrix.m31()
                       );
        
        /// Extract the TOP plane
        planeTop.set( matrix.m03() - matrix.m01(),
                      matrix.m13() - matrix.m11(),
                      matrix.m23() - matrix.m21(),
                      matrix.m33() - matrix.m31()
                    );
        
        // Extract the FAR plane
        planeFar.set( matrix.m03() - matrix.m02(),
                      matrix.m13() - matrix.m12(),
                      matrix.m23() - matrix.m22(),
                      matrix.m33() - matrix.m32()
                    );
        
        // Extract the NEAR plane
        planeNear.set( matrix.m03() + matrix.m02(),
                       matrix.m13() + matrix.m12(),
                       matrix.m23() + matrix.m22(),
                       matrix.m33() + matrix.m32()
                     );
    }
    
    /**
     * Extract the frustum from the incoming projections and modelview matrices.
     */
    public final Matrix4f computeInverse( Matrix4f proj ) // assume MODEL_VIEW_MATRIX = IDENTITY
    {
        matrix.m00( ( planeLeft.getA() - planeRight.getA() ) / 2.0f );
        matrix.m01( ( planeBottom.getA() - planeTop.getA() ) / 2.0f );
        matrix.m02( ( planeNear.getA() - planeFar.getA() ) / 2.0f );
        matrix.m03( planeRight.getA() + matrix.m00() );
        
        matrix.m10( ( planeLeft.getB() - planeRight.getB() ) / 2.0f );
        matrix.m11( ( planeBottom.getB() - planeTop.getB() ) / 2.0f );
        matrix.m12( ( planeNear.getB() - planeFar.getB() ) / 2.0f );
        matrix.m13( planeRight.getB() + matrix.m10() );
        
        matrix.m20( ( planeLeft.getC() - planeRight.getC() ) / 2.0f );
        matrix.m21( ( planeBottom.getC() - planeTop.getC() ) / 2.0f );
        matrix.m22( ( planeNear.getC() - planeFar.getC() ) / 2.0f );
        matrix.m23( planeRight.getC() + matrix.m20() );
        
        matrix.m30( ( planeLeft.getD() - planeRight.getD() ) / 2.0f );
        matrix.m31( ( planeBottom.getD() - planeTop.getD() ) / 2.0f );
        matrix.m32( ( planeNear.getD() - planeFar.getD() ) / 2.0f );
        matrix.m33( planeRight.getD() + matrix.m30() );
        
        if ( proj != null )
            proj.set( matrix );
        
        return ( matrix );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer( 512 );
        
        sb.append( this.getClass().getSimpleName() );
        sb.append( " { " );
        sb.append( "Left: " );
        sb.append( planeLeft );
        sb.append( ", Right: " );
        sb.append( planeRight );
        sb.append( ", Bottom:" );
        sb.append( planeBottom );
        sb.append( ", Top:" );
        sb.append( planeTop );
        sb.append( ", Far:" );
        sb.append( planeFar );
        sb.append( ", Near:" );
        sb.append( planeNear );
        sb.append( " }" );
        
        return ( sb.toString() );
    }
    
    /**
     * Frustum constructor comment.
     */
    public Frustum()
    {
        super();
    }
}
