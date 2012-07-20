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

import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.TexCoord2f;
import org.openmali.vecmath2.TexCoord3f;
import org.openmali.vecmath2.TexCoord4f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;

/**
 * This class represents vertices.
 * A Vertex is composed of coordinates, normals, colors and texture-coordinates.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Vertex3f
{
    public static final int COORDINATES            = 1;
    public static final int NORMALS                = 2;
    public static final int COLORS                 = 4;
    public static final int TEXTURE_COORDINATES    = 8;
    public static final int VERTEX_ATTRIBUTES      = 16;
    
    private int features;
    private int texCoordSize = 0;
    
    private Point3f coord = null;
    private Vector3f normal = null;
    private Colorf color = null;
    private TexCoord2f texCoord2 = null;
    private TexCoord3f texCoord3 = null;
    private TexCoord4f texCoord4 = null;
    
    
    public void setFeatures( int features )
    {
        this.features = features;
    }
    
    public int getFeatures()
    {
        return ( features );
    }
    
    public void addFeature(int feature)
    {
        this.features |= feature;
    }
    
    public boolean hasFeature( int feature )
    {
        return ( ( this.features & feature ) > 0 );
    }
    
    public final int getTexCoordSize()
    {
        return ( texCoordSize );
    }
    
    
    public void setCoord( Tuple3f coord )
    {
        if ( this.coord == null )
            this.coord = new Point3f( coord );
        else
            this.coord.set( coord );
        
        addFeature( COORDINATES );
    }
    
    public void getCoord( Tuple3f coord )
    {
        coord.set( this.coord );
    }
    
    public Tuple3f getCoord()
    {
        return ( this.coord );
    }
    
    
    public void setNormal( Vector3f normal )
    {
        if ( this.normal == null )
            this.normal = new Vector3f( normal );
        else
            this.normal.set( normal );
        
        addFeature( NORMALS );
    }
    
    public void getNormal( Vector3f normal )
    {
        normal.set( this.normal );
    }
    
    public Vector3f getNormal()
    {
        return ( this.normal );
    }
    
    
    public void setColor( Colorf color )
    {
        if ( this.color == null )
            this.color = new Colorf( color );
        else
            this.color.set( color );
        
        addFeature( COLORS );
    }
    
    public void getColor( Colorf color )
    {
        color.set( this.color );
    }
    
    public Colorf getColor()
    {
        return ( this.color );
    }
    
    
    public void setTexCoord2( TexCoord2f texCoord )
    {
        if ( this.texCoord2 == null )
            this.texCoord2 = new TexCoord2f( texCoord );
        else
            this.texCoord2.set( texCoord );
        
        addFeature( TEXTURE_COORDINATES );
        texCoordSize = 2;
    }
    
    public void getTexCoord2( TexCoord2f texCoord )
    {
        texCoord.set( this.texCoord2 );
    }
    
    public TexCoord2f getTexCoord2()
    {
        return ( this.texCoord2 );
    }
    
    
    public void setTexCoord3( TexCoord3f texCoord )
    {
        if ( this.texCoord3 == null )
            this.texCoord3 = new TexCoord3f( texCoord );
        else
            this.texCoord3.set( texCoord );
        
        addFeature( TEXTURE_COORDINATES );
        texCoordSize = 3;
    }
    
    public void getTexCoord3( TexCoord3f texCoord )
    {
        texCoord.set( this.texCoord3 );
    }
    
    public TexCoord3f getTexCoord3()
    {
        return ( this.texCoord3 );
    }
    
    
    public void setTexCoord4( TexCoord4f texCoord )
    {
        if ( this.texCoord4 == null )
            this.texCoord4 = new TexCoord4f( texCoord );
        else
            this.texCoord4.set( texCoord );
        
        addFeature( TEXTURE_COORDINATES );
        texCoordSize = 4;
    }
    
    public void getTexCoord4( TexCoord4f texCoord )
    {
        texCoord.set( this.texCoord4 );
    }
    
    public TexCoord4f getTexCoord4()
    {
        return ( this.texCoord4 );
    }
    
    
    public void set( Tuple3f coord, Vector3f normal )
    {
        if ( coord != null )
        {
            addFeature( COORDINATES );
            setCoord( coord );
        }
        
        if ( normal != null )
        {
            addFeature( NORMALS );
            setNormal( normal );
        }
    }
    
    public void get( Tuple3f coord, Vector3f normal )
    {
        if ( ( coord != null ) && ( hasFeature( COORDINATES ) ) )
            getCoord( coord );
        if ( ( normal != null ) && ( hasFeature( NORMALS ) ) )
            getNormal( normal );
    }
    
    
    public void set( Tuple3f coord, Vector3f normal, Colorf color )
    {
        set( coord, normal );
        
        if ( color != null )
        {
            setColor( color );
        }
    }
    
    public void get( Tuple3f coord, Vector3f normal, Colorf color )
    {
        get( coord, normal );
        
        if ( ( color != null ) && ( hasFeature( COLORS ) ) )
        {
            getColor( color );
        }
    }
    
    
    public void set( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        set( coord, normal, color );
        
        if ( texCoord != null )
        {
            setTexCoord2( texCoord );
        }
    }
    
    public void get( Tuple3f coord, Vector3f normal, Colorf color, TexCoord2f texCoord )
    {
        get( coord, normal, color );
        
        if ( ( texCoord != null ) && ( hasFeature( TEXTURE_COORDINATES ) ) && ( texCoordSize == 2 ) )
            getTexCoord2( texCoord );
    }
    
    
    public void set( Tuple3f coord, Vector3f normal, Colorf color, TexCoord3f texCoord )
    {
        set( coord, normal, color );
        
        if ( texCoord != null )
        {
            setTexCoord3( texCoord );
        }
    }
    
    public void get( Tuple3f coord, Vector3f normal, Colorf color, TexCoord3f texCoord )
    {
        get( coord, normal, color );
        
        if ( ( texCoord != null ) && ( hasFeature( TEXTURE_COORDINATES ) ) && ( texCoordSize == 3 ) )
            getTexCoord3( texCoord );
    }
    
    
    public void set( Tuple3f coord, Vector3f normal, Colorf color, TexCoord4f texCoord )
    {
        set( coord, normal, color );
        
        if ( texCoord != null )
        {
            setTexCoord4( texCoord );
        }
    }
    
    public void get( Tuple3f coord, Vector3f normal, Colorf color, TexCoord4f texCoord )
    {
        get( coord, normal, color );
        
        if ( ( texCoord != null ) && ( hasFeature( TEXTURE_COORDINATES ) ) && ( texCoordSize == 4 ) )
            getTexCoord4( texCoord );
    }
    
    
    public Vertex3f( int features )
    {
        this.features = features | COORDINATES;
    }
    
    public Vertex3f()
    {
        this( COORDINATES | NORMALS | COLORS | TEXTURE_COORDINATES );
    }
}
