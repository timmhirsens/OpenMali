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
package org.openmali.spatial.polygons;

import org.openmali.FastMath;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Ray3f;
import org.openmali.vecmath2.TexCoord2f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.openmali.vecmath2.Vertex3f;

/**
 * A Triangle is composed of three vertices.
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Arne Mueller [added ray-intersection methods]
 * @author Amos Wenger (aka BlueSky)
 * @author Andrew Hanson (aka Patheros) [Made Triangle GC-friendly]
 * @author Mathias Henze (aka Cylab) [made Triangle thread-safe]
 */
public class Triangle extends Polygon
{
    private Point3f coordA = null;
    private Point3f coordB = null;
    private Point3f coordC = null;
    
    private Vector3f normalA = null;
    private Vector3f normalB = null;
    private Vector3f normalC = null;
    
    private Colorf colorA = null;
    private Colorf colorB = null;
    private Colorf colorC = null;
    
    private TexCoord2f texCoordA = null;
    private TexCoord2f texCoordB = null;
    private TexCoord2f texCoordC = null;
    
    private Vector3f faceNormal = null;
    
    private int vertexIndexA = -1;
    private int vertexIndexB = -1;
    private int vertexIndexC = -1;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setFeatures( int features )
    {
        super.setFeatures( features );
        
        if ( hasFeature( Vertex3f.COORDINATES ) )
        {
            if ( this.coordA == null )
                this.coordA = new Point3f();
            if ( this.coordB == null )
                this.coordB = new Point3f();
            if ( this.coordC == null )
                this.coordC = new Point3f();
        }
        
        if ( hasFeature( Vertex3f.NORMALS ) )
        {
            if ( this.normalA == null )
                this.normalA = new Vector3f();
            if ( this.normalB == null )
                this.normalB = new Vector3f();
            if ( this.normalC == null )
                this.normalC = new Vector3f();
        }
        
        if ( hasFeature( Vertex3f.COLORS ) )
        {
            if ( this.colorA == null )
                this.colorA = new Colorf();
            if ( this.colorB == null )
                this.colorB = new Colorf();
            if ( this.colorC == null )
                this.colorC = new Colorf();
        }
        
        if ( hasFeature( Vertex3f.TEXTURE_COORDINATES ) )
        {
            if ( this.texCoordA == null )
                this.texCoordA = new TexCoord2f();
            if ( this.texCoordB == null )
                this.texCoordB = new TexCoord2f();
            if ( this.texCoordC == null )
                this.texCoordC = new TexCoord2f();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addFeature( int feature )
    {
        super.addFeature( feature );
        
        if ( ( feature & Vertex3f.COORDINATES ) != 0 )
        {
            if ( this.coordA == null )
                this.coordA = new Point3f();
            if ( this.coordB == null )
                this.coordB = new Point3f();
            if ( this.coordC == null )
                this.coordC = new Point3f();
        }
        
        if ( ( feature & Vertex3f.NORMALS ) != 0 )
        {
            if ( this.normalA == null )
                this.normalA = new Vector3f();
            if ( this.normalB == null )
                this.normalB = new Vector3f();
            if ( this.normalC == null )
                this.normalC = new Vector3f();
        }
        
        if ( ( feature & Vertex3f.COLORS ) != 0 )
        {
            if ( this.colorA == null )
                this.colorA = new Colorf();
            if ( this.colorB == null )
                this.colorB = new Colorf();
            if ( this.colorC == null )
                this.colorC = new Colorf();
        }
        
        if ( ( feature & Vertex3f.TEXTURE_COORDINATES ) != 0 )
        {
            if ( this.texCoordA == null )
                this.texCoordA = new TexCoord2f();
            if ( this.texCoordB == null )
                this.texCoordB = new TexCoord2f();
            if ( this.texCoordC == null )
                this.texCoordC = new TexCoord2f();
        }
    }
    
    public void setVertexCoordA( Tuple3f coord )
    {
        addFeature( Vertex3f.COORDINATES );
        
        this.coordA.set( coord );
    }
    
    public void setVertexCoordB( Tuple3f coord )
    {
        addFeature( Vertex3f.COORDINATES );
        
        this.coordB.set( coord );
    }
    
    public void setVertexCoordC( Tuple3f coord )
    {
        addFeature( Vertex3f.COORDINATES );
        
        this.coordC.set( coord );
    }
    
    public void getVertexCoordA( Tuple3f coord )
    {
        coord.set( this.coordA );
    }
    
    public Point3f getVertexCoordA()
    {
        return ( this.coordA );
    }
    
    public void getVertexCoordB( Tuple3f coord )
    {
        coord.set( this.coordB );
    }
    
    public Point3f getVertexCoordB()
    {
        return ( this.coordB );
    }
    
    public void getVertexCoordC( Tuple3f coord )
    {
        coord.set( this.coordC );
    }
    
    public Point3f getVertexCoordC()
    {
        return ( this.coordC );
    }
    
    
    public void setVertexCoords( Tuple3f coordA, Tuple3f coordB, Tuple3f coordC )
    {
        setVertexCoordA( coordA );
        setVertexCoordB( coordB );
        setVertexCoordC( coordC );
    }
    
    public void getVertexCoords( Tuple3f coordA, Tuple3f coordB, Tuple3f coordC )
    {
        getVertexCoordA( coordA );
        getVertexCoordB( coordB );
        getVertexCoordC( coordC );
    }
    
    
    public void setVertexNormalA( Vector3f normal )
    {
        addFeature( Vertex3f.NORMALS );
        
        this.normalA.set( normal );
    }
    
    public void setVertexNormalB( Vector3f normal )
    {
        addFeature( Vertex3f.NORMALS );
        
        this.normalB.set( normal );
    }
    
    public void setVertexNormalC( Vector3f normal )
    {
        addFeature( Vertex3f.NORMALS );
        
        this.normalC.set( normal );
    }
    
    public void getVertexNormalA( Vector3f normal )
    {
        normal.set( this.normalA );
    }
    
    public Vector3f getVertexNormalA()
    {
        return ( this.normalA );
    }
    
    public void getVertexNormalB( Vector3f normal )
    {
        normal.set( this.normalB );
    }
    
    public Vector3f getVertexNormalB()
    {
        return ( this.normalB );
    }
    
    public void getVertexNormalC( Vector3f normal )
    {
        normal.set( this.normalC );
    }
    
    public Vector3f getVertexNormalC()
    {
        return ( this.normalC );
    }
    
    
    public void setVertexNormals( Vector3f normalA, Vector3f normalB, Vector3f normalC )
    {
        setVertexNormalA( normalA );
        setVertexNormalB( normalB );
        setVertexNormalC( normalC );
    }
    
    public void getVertexNormals( Vector3f normalA, Vector3f normalB, Vector3f normalC )
    {
        getVertexNormalA( normalA );
        getVertexNormalB( normalB );
        getVertexNormalC( normalC );
    }
    
    
    public void setVertexColorA( Colorf color )
    {
        addFeature( Vertex3f.COLORS );
        
        this.colorA.set( color );
    }
    
    public void setVertexColorB( Colorf color )
    {
        addFeature( Vertex3f.COLORS );
        
        this.colorB.set( color );
    }
    
    public void setVertexColorC( Colorf color )
    {
        addFeature( Vertex3f.COLORS );
        
        this.colorC.set( color );
    }
    
    public void getVertexColorA( Colorf color )
    {
        color.set( this.colorA );
    }
    
    public Colorf getVertexColorA()
    {
        return ( this.colorA );
    }
    
    public void getVertexColorB( Colorf color )
    {
        color.set( this.colorB );
    }
    
    public Colorf getVertexColorB()
    {
        return ( this.colorB );
    }
    
    public void getVertexColorC( Colorf color )
    {
        color.set( this.colorC );
    }
    
    public Colorf getVertexColorC()
    {
        return ( this.colorC );
    }
    
    
    public void setVertexColors( Colorf colorA, Colorf colorB, Colorf colorC )
    {
        setVertexColorA( colorA );
        setVertexColorB( colorB );
        setVertexColorC( colorC );
    }
    
    public void getVertexColors( Colorf colorA, Colorf colorB, Colorf colorC )
    {
        getVertexColorA( colorA );
        getVertexColorB( colorB );
        getVertexColorC( colorC );
    }
    
    
    public void setVertexTexCoordA( TexCoord2f texCoord )
    {
        addFeature( Vertex3f.TEXTURE_COORDINATES );
        setTexCoordsSize( 2 );
        
        this.texCoordA.set( texCoord );
    }
    
    public void setVertexTexCoordB( TexCoord2f texCoord )
    {
        addFeature( Vertex3f.TEXTURE_COORDINATES );
        setTexCoordsSize( 2 );
        
        this.texCoordB.set( texCoord );
    }
    
    public void setVertexTexCoordC( TexCoord2f texCoord )
    {
        addFeature( Vertex3f.TEXTURE_COORDINATES );
        setTexCoordsSize( 2 );
        
        this.texCoordC.set( texCoord );
    }
    
    public void getVertexTexCoordA( TexCoord2f texCoord )
    {
        texCoord.set( this.texCoordA );
    }
    
    public TexCoord2f getVertexTexCoordA()
    {
        return ( this.texCoordA );
    }
    
    public void getVertexTexCoordB( TexCoord2f texCoord )
    {
        texCoord.set( this.texCoordB );
    }
    
    public TexCoord2f getVertexTexCoordB()
    {
        return ( this.texCoordB );
    }
    
    public void getVertexTexCoordC( TexCoord2f texCoord )
    {
        texCoord.set( this.texCoordC );
    }
    
    public TexCoord2f getVertexTexCoordC()
    {
        return ( this.texCoordC );
    }
    
    
    public void setVertexTexCoords( TexCoord2f texCoordA, TexCoord2f texCoordB, TexCoord2f texCoordC )
    {
        setVertexTexCoordA( texCoordA );
        setVertexTexCoordB( texCoordB );
        setVertexTexCoordC( texCoordC );
    }
    
    public void getVertexTexCoords( TexCoord2f texCoordA, TexCoord2f texCoordB, TexCoord2f texCoordC )
    {
        getVertexTexCoordA( texCoordA );
        getVertexTexCoordB( texCoordB );
        getVertexTexCoordC( texCoordC );
    }
    
    
    public void setVertexA( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        if ( coord == null )
            removeFeature( Vertex3f.COORDINATES );
        else
            setVertexCoordA( coord );
        
        if ( normal == null )
            removeFeature( Vertex3f.NORMALS );
        else
            setVertexNormalA( normal );
        
        if ( color == null )
            removeFeature( Vertex3f.COLORS );
        else
            setVertexColorA( color );
        
        if ( texCoord == null )
            removeFeature( Vertex3f.TEXTURE_COORDINATES );
        else
            setVertexTexCoordA( texCoord );
    }
    
    public void getVertexA( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        if ( ( coord != null ) && ( hasFeature( Vertex3f.COORDINATES ) ) )
            getVertexCoordA( coord );
        if ( ( normal != null ) && ( hasFeature( Vertex3f.NORMALS ) ) )
            getVertexNormalA( normal );
        if ( ( color != null ) && ( hasFeature( Vertex3f.COLORS ) ) )
            getVertexColorA( color );
        if ( ( texCoord != null ) && ( hasFeature( Vertex3f.TEXTURE_COORDINATES ) ) && ( getTexCoordsSize() == 2 ) )
            getVertexTexCoordA( texCoord );
    }
    
    
    public void setVertexB( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        if ( coord == null )
            removeFeature( Vertex3f.COORDINATES );
        else
            setVertexCoordB( coord );
        
        if ( normal == null )
            removeFeature( Vertex3f.NORMALS );
        else
            setVertexNormalB( normal );
        
        if ( color == null )
            removeFeature( Vertex3f.COLORS );
        else
            setVertexColorB( color );
        
        if ( texCoord == null )
            removeFeature( Vertex3f.TEXTURE_COORDINATES );
        else
            setVertexTexCoordB( texCoord );
    }
    
    public void getVertexB( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        if ( ( coord != null ) && ( hasFeature( Vertex3f.COORDINATES ) ) )
            getVertexCoordB( coord );
        if ( ( normal != null ) && ( hasFeature( Vertex3f.NORMALS ) ) )
            getVertexNormalB( normal );
        if ( ( color != null ) && ( hasFeature( Vertex3f.COLORS ) ) )
            getVertexColorB( color );
        if ( ( texCoord != null ) && ( hasFeature( Vertex3f.TEXTURE_COORDINATES ) ) && ( getTexCoordsSize() == 2 ) )
            getVertexTexCoordB( texCoord );
    }
    
    
    public void setVertexC( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        if ( coord == null )
            removeFeature( Vertex3f.COORDINATES );
        else
            setVertexCoordC( coord );
        
        if ( normal == null )
            removeFeature( Vertex3f.NORMALS );
        else
            setVertexNormalC( normal );
        
        if ( color == null )
            removeFeature( Vertex3f.COLORS );
        else
            setVertexColorC( color );
        
        if ( texCoord == null )
            removeFeature( Vertex3f.TEXTURE_COORDINATES );
        else
            setVertexTexCoordC( texCoord );
    }
    
    public void getVertexC( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        if ( ( coord != null ) && ( hasFeature( Vertex3f.COORDINATES ) ) )
            getVertexCoordC( coord );
        if ( ( normal != null ) && ( hasFeature( Vertex3f.NORMALS ) ) )
            getVertexNormalC( normal );
        if ( ( color != null ) && ( hasFeature( Vertex3f.COLORS ) ) )
            getVertexColorC( color );
        if ( ( texCoord != null ) && ( hasFeature( Vertex3f.TEXTURE_COORDINATES ) ) && ( getTexCoordsSize() == 2 ) )
            getVertexTexCoordC( texCoord );
    }
    
    
    /**
     * Calculates angle between the edges AC and AB.
     */
    public float getAngleA()
    {
        Vector3f tmpVec1 = Vector3f.fromPool();
        Vector3f tmpVec2 = Vector3f.fromPool();
        
        tmpVec1.sub( getVertexCoordC(), getVertexCoordA() );
        tmpVec2.sub( getVertexCoordB(), getVertexCoordA() );
        final float angle = tmpVec1.angle( tmpVec2 );
        
        Vector3f.toPool( tmpVec2 );
        Vector3f.toPool( tmpVec1 );
        
        return ( angle );
    }
    
    /**
     * Calculates angle between the edges BA and BC.
     */
    public float getAngleB()
    {
        Vector3f tmpVec1 = Vector3f.fromPool();
        Vector3f tmpVec2 = Vector3f.fromPool();
        
        tmpVec1.sub( getVertexCoordA(), getVertexCoordB() );
        tmpVec2.sub( getVertexCoordC(), getVertexCoordB() );
        final float angle = tmpVec1.angle( tmpVec2 );
        
        Vector3f.toPool( tmpVec2 );
        Vector3f.toPool( tmpVec1 );
        
        return ( angle );
    }
    
    /**
     * Calculates angle between the edges CB and CA.
     */
    public float getAngleC()
    {
        Vector3f tmpVec1 = Vector3f.fromPool();
        Vector3f tmpVec2 = Vector3f.fromPool();
        
        tmpVec1.sub( getVertexCoordB(), getVertexCoordC() );
        tmpVec2.sub( getVertexCoordA(), getVertexCoordC() );
        final float angle = tmpVec1.angle( tmpVec2 );
        
        Vector3f.toPool( tmpVec2 );
        Vector3f.toPool( tmpVec1 );
        
        return ( angle );
    }
    
    
    /**
     * Calculates the face normal from the cross product of edge AC and AB.
     * 
     * @param faceNormal
     */
    public void getFaceNormalACAB( Vector3f faceNormal )
    {
        Vector3f tmpVec1 = Vector3f.fromPool();
        Vector3f tmpVec2 = Vector3f.fromPool();
        
        tmpVec1.sub( getVertexCoordC(), getVertexCoordA() );
        tmpVec2.sub( getVertexCoordB(), getVertexCoordA() );
        
        faceNormal.cross( tmpVec1, tmpVec2 );
        faceNormal.normalize();
        
        Vector3f.toPool( tmpVec2 );
        Vector3f.toPool( tmpVec1 );
    }
    
    /**
     * Calculates the face normal from the cross product of edge AC and AB.
     */
    public Vector3f getFaceNormalACAB()
    {
        getFaceNormalACAB( faceNormal );
        
        return ( faceNormal );
    }
    
    
    /**
     * Calculates the face normal from the cross product of edge BA and BC.
     * 
     * @param faceNormal
     */
    public void getFaceNormalBABC( Vector3f faceNormal )
    {
        Vector3f tmpVec1 = Vector3f.fromPool();
        Vector3f tmpVec2 = Vector3f.fromPool();
        
        tmpVec1.sub( getVertexCoordA(), getVertexCoordB() );
        tmpVec2.sub( getVertexCoordC(), getVertexCoordB() );
        
        faceNormal.cross( tmpVec1, tmpVec2 );
        faceNormal.normalize();
        
        Vector3f.toPool( tmpVec2 );
        Vector3f.toPool( tmpVec1 );
    }
    
    /**
     * Calculates the face normal from the cross product of edge BA and BC.
     */
    public Vector3f getFaceNormalBABC()
    {
        getFaceNormalBABC( faceNormal );
        
        return ( faceNormal );
    }
    
    
    /**
     * Calculates the face normal from the cross product of edge AB and AC.
     * 
     * @param faceNormal
     */
    public void getFaceNormalCBCA( Vector3f faceNormal )
    {
        Vector3f tmpVec1 = Vector3f.fromPool();
        Vector3f tmpVec2 = Vector3f.fromPool();
        
        tmpVec1.sub( getVertexCoordB(), getVertexCoordC() );
        tmpVec2.sub( getVertexCoordA(), getVertexCoordC() );
        
        faceNormal.cross( tmpVec1, tmpVec2 );
        faceNormal.normalize();
        
        Vector3f.toPool( tmpVec2 );
        Vector3f.toPool( tmpVec1 );
    }
    
    /**
     * Calculates the face normal from the cross product of edge AB and AC.
     */
    public Vector3f getFaceNormalCBCA()
    {
        getFaceNormalCBCA( faceNormal );
        
        return ( faceNormal );
    }
    
    /**
     * Calculates the face normal and writes it to the parameter.
     * 
     * @param faceNormal
     */
    public void getFaceNormal( Vector3f faceNormal )
    {
        if ( !hasFeature( Vertex3f.NORMALS ) )
            throw new NullPointerException( "You need vertex normals to calculate the face normal" );
        
        Vector3f tmpVec4 = Vector3f.fromPool();
        Vector3f tmpVec5 = Vector3f.fromPool();
        Vector3f tmpVec6 = Vector3f.fromPool();
        
        try
        {
            getFaceNormalACAB( tmpVec4 );
            if ( ( getVertexNormalA() != null ) && ( tmpVec4.angle( getVertexNormalA() ) > FastMath.PI_HALF ) )
                tmpVec4.scale( -1f );
            
            getFaceNormalBABC( tmpVec5 );
            if ( ( getVertexNormalB() != null ) && ( tmpVec5.angle( getVertexNormalB() ) > FastMath.PI_HALF ) )
                tmpVec5.scale( -1f );
            
            getFaceNormalCBCA( tmpVec6 );
            if ( ( getVertexNormalC() != null ) && ( tmpVec6.angle( getVertexNormalC() ) > FastMath.PI_HALF ) )
                tmpVec6.scale( -1f );
            
            final float angleAB = tmpVec4.angle( tmpVec5 );
            final float angleAC = tmpVec4.angle( tmpVec6 );
            
            if ( ( angleAB > 0.001f ) && ( angleAC > 0.001f ) )
            {
                faceNormal.set( tmpVec5 );
                return;
            }
            
            final float angleBC = tmpVec5.angle( tmpVec6 );
            
            if ( ( angleBC > 0.001f ) && ( angleAB > 0.001f ) )
            {
                faceNormal.set( tmpVec6 );
                return;
            }
            
            if ( ( angleBC > 0.001f ) && ( angleAC > 0.001f ) )
            {
                faceNormal.set( tmpVec4 );
                return;
            }
            
            faceNormal.set( tmpVec4 );
        }
        finally
        {
            Vector3f.toPool( tmpVec6 );
            Vector3f.toPool( tmpVec5 );
            Vector3f.toPool( tmpVec4 );
        }
    }
    
    /**
     * Calculates and returns the face normal.
     */
    public Vector3f getFaceNormal()
    {
        if ( faceNormal == null )
            faceNormal = new Vector3f();
        
        getFaceNormal( faceNormal );
        
        return ( faceNormal );
    }
    
    
    /**
     * Sets the index of the vertexA (just meta info).
     * 
     * @param index
     */
    public void setVertexIndexA( int index )
    {
        this.vertexIndexA = index;
    }
    
    /**
     * @return the index of the vertexA (just meta info).
     */
    public int getVertexIndexA()
    {
        return ( vertexIndexA );
    }
    
    /**
     * Sets the index of the vertexB (just meta info).
     * 
     * @param index
     */
    public void setVertexIndexB( int index )
    {
        this.vertexIndexB = index;
    }
    
    /**
     * @return the index of the vertexB (just meta info).
     */
    public int getVertexIndexB()
    {
        return ( vertexIndexB );
    }
    
    /**
     * Sets the index of the vertexC (just meta info).
     * 
     * @param index
     */
    public void setVertexIndexC( int index )
    {
        this.vertexIndexC = index;
    }
    
    /**
     * @return the index of the vertexC (just meta info).
     */
    public int getVertexIndexC()
    {
        return ( vertexIndexC );
    }
    
    /**
     * Sets the indices of the vertices A, B, C (just meta info).
     * 
     * @param indexA
     * @param indexB
     * @param indexC
     */
    public void setVertexIndices( int indexA, int indexB, int indexC )
    {
        setVertexIndexA( indexA );
        setVertexIndexB( indexB );
        setVertexIndexC( indexC );
    }
    
    
    public int sign3D( Tuple3f a, Tuple3f b, Tuple3f c, Tuple3f d )
    {
        Matrix3f tmpMat = Matrix3f.fromPool();
        
        tmpMat.setRow( 0, a.getX() - d.getX(), a.getY() - d.getY(), a.getZ() - d.getZ() );
        tmpMat.setRow( 1, b.getX() - d.getX(), b.getY() - d.getY(), b.getZ() - d.getZ() );
        tmpMat.setRow( 2, c.getX() - d.getX(), c.getY() - d.getY(), c.getZ() - d.getZ() );
        
        final float det = tmpMat.determinant();
        final float EPSILON = 0.00001f;
        
        Matrix3f.toPool( tmpMat );
        
        if ( det > EPSILON )
            return ( 1 );
        else if ( det < -EPSILON )
            return ( -1 );
        else
            return ( 0 );
    }
    
    /**
     * Does a quick ray-intersection test, that doesn't very precise.
     * It provides a reliable negative-boolean result.
     * 
     * @param pickRay
     * 
     * @return true, if an intersection is possible
     */
    public boolean quickIntersectionTest( Ray3f pickRay )
    {
        Point3f tmpPnt = Point3f.fromPool();

        tmpPnt.scaleAdd( 100000.0f, pickRay.getDirection(), pickRay.getOrigin() );
        final int i = sign3D( tmpPnt, coordA, pickRay.getOrigin(), coordB );
        final int j = sign3D( tmpPnt, coordC, coordB, pickRay.getOrigin() );
        final int k = sign3D( tmpPnt, coordA, coordC, pickRay.getOrigin() );
        
        Point3f.toPool( tmpPnt );
        
        if ( i == 0 && j == 0 )
            return ( true ); // intersects in C
        if ( i == 0 && k == 0 )
            return ( true ); // intersects in A
        if ( j == 0 && k == 0 )
            return ( true ); // intersects in B
        if ( i == 0 && j == k )
            return ( true ); // intersects in AC
        if ( j == 0 && i == k )
            return ( true ); // intersects in BC
        if ( k == 0 && j == i )
            return ( true ); // intersects in AB
        if ( i == j && j == k )
            return ( true ); // intersects inside
        
        return ( false ); // does not intersect
    }
    
    /**
     * Tests the triangle for intersection with a ray.
     * 
     * @param rayOrigin
     * @param rayDirection
     * 
     * @return the distance between the ray origin and the intersection point
     */
    public float intersects( Point3f rayOrigin, Vector3f rayDirection )
    {
        // map tmp vectors to local names
        Vector3f e1 = Vector3f.fromPool();
        Vector3f e2 = Vector3f.fromPool();
        Vector3f p = Vector3f.fromPool();
        Vector3f q = Vector3f.fromPool();
        Vector3f s = Vector3f.fromPool();
        
        try
        {
            // test raydirection
            e1.sub( coordB, coordA );
            e2.sub( coordC, coordA );
            p.cross( rayDirection, e2 );
            float a = e1.dot( p );
            
            // if the result is "zero", the pick ray is parallel to the triangle
            if ( ( -0.00001f < a ) && ( a < 0.00001f ) )
            {
                return ( -1f );
            }
            
            float f = 1.0f / a;
            
            // compute barycentric coordinates
            s.sub( rayOrigin, coordA );
            final float u = f * s.dot( p );
            if ( ( 0.0f > u ) || ( u > 1.0f ) )
            {
                return ( -1f );
            }
            
            q.cross( s, e1 );
            final float v = f * rayDirection.dot( q );
            if ( ( 0.0f > u ) || ( u + v > 1.0f ) )
            {
                return ( -1f );
            }
            
            // compute the intersection point on the ray
            final float l = f * e2.dot( q );
            if ( l < 0.0f )
            {
                return ( -1f );
            }
            
            return ( l * l );
        }
        finally
        {
            Vector3f.toPool( s );
            Vector3f.toPool( q );
            Vector3f.toPool( p );
            Vector3f.toPool( e2 );
            Vector3f.toPool( e1 );
        }
    }
    
    /**
     * Tests the triangle for intersection with a ray.
     * 
     * @param ray
     * 
     * @return the distance between the ray origin and the intersection point
     */
    public float intersects( Ray3f ray )
    {
        return ( intersects( ray.getOrigin(), ray.getDirection() ) );
    }
    
    /**
     * Tests the triangle for intersection with a ray.<br>
     * This firs uses quickIntersectionTest() to cheaply test for a possible intersection.
     * 
     * @param ray
     * @param nearestDist the nearest distance to be accepted (for optimizations)
     * 
     * @return the distance between the ray origin and the intersection point
     */
    public float intersects( Ray3f ray, float nearestDist )
    {
        /*
         * first check, if any intersection with the triangle can result in a
         * nearer result.
         * if this is possible, check if there is an intersection with the
         * triangle.
         */
        if ( quickIntersectionTest( ray ) )
        {
            // if there is an intersection return the exact position
            return ( intersects( ray ) );
        }
        
        return ( -1f );
    }
    
    public void transform( Matrix4f matrix )
    {
        matrix.transform( this.coordA );
        matrix.transform( this.coordB );
        matrix.transform( this.coordC );
    }
    
    
    public Triangle( int features, int texCoordsSize )
    {
        super( features, texCoordsSize );
        
        // The fields above are initialized after the super constructor.
        // So we need to apply features again to make sure, coords, normals, etc.
        // are initialized according to the features.
        
        setFeatures( getFeatures() );
        setTexCoordsSize( getTexCoordsSize() );
    }
    
    public Triangle()
    {
        //this( Vertex3f.COORDINATES | Vertex3f.NORMALS | Vertex3f.COLORS | Vertex3f.TEXTURE_COORDINATES, 2 );
        this( Vertex3f.COORDINATES, 2 );
    }
    
    /*
    public static final void main( String[] args )
    {
        Triangle trian = new Triangle( COORDINATES | NORMALS );
        
        trian.setVertexA( new Point3f( 0, 0, 0 ), new Vector3f( 0, 0, +1 ), null, null );
        trian.setVertexB( new Point3f( 1, 0, 0 ), new Vector3f( 0, 0, +1 ), null, null );
        trian.setVertexC( new Point3f( 1, 1, 0 ), new Vector3f( 0, 0, -1 ), null, null );
        
        System.out.println( trian.getFaceNormal() );
    }
    */
}
