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
package org.openmali.spatial;

import java.util.List;

import org.openmali.vecmath2.Tuple3f;

/**
 * This is a utility class to abstract a vertex list.<br>
 * The vertices may come from an array or Tuple3f or a List ir Tuple3f or
 * a SpatialObjectInterface
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public final class VertexList implements VertexContainer
{
    public enum SourceType
    {
        CONTAINER,
        LIST,
        ARRAY;
    }
    
    private VertexContainer  vc   = null;
    private List<Tuple3f>    list  = null;
    private Tuple3f[]        array = null;
    
    private SourceType sourceType = null;
    
    public final SourceType getSourceType()
    {
        return ( sourceType );
    }
    
    /**
     * {@inheritDoc}
     */
    public int getVertexCount()
    {
        switch ( getSourceType() )
        {
            case CONTAINER:
                return ( vc.getVertexCount() );
            case LIST:
                return ( list.size() );
            case ARRAY:
                return ( array.length );
        }
        
        return ( -1 );
    }
    
    /**
     * {@inheritDoc}
     */
    public final boolean getVertex( int i, Tuple3f coord )
    {
        assert ( i >= 0 && i < getVertexCount() ) : "I must be >= 0 and < getVertexCount()";
        assert ( coord != null ) : "coord must not be null";
        
        if ( getSourceType() == null )
            return ( false );
        
        switch ( getSourceType() )
        {
            case CONTAINER:
                vc.getVertex( i, coord );
                
                return ( true );
                
            case LIST:
                coord.set( list.get( i ) );
                
                return ( true );
                
            case ARRAY:
                coord.set( array[ i ] );
                
                return ( true );
        }
        
        return ( false );
    }
    
    public final void set( VertexContainer vc )
    {
        this.vc = vc;
        this.list = null;
        this.array = null;
        
        this.sourceType = SourceType.CONTAINER;
    }
    
    public final void set( List<Tuple3f> list )
    {
        this.vc = null;
        this.list = list;
        this.array = null;
        
        this.sourceType = SourceType.LIST;
    }
    
    public final void set( Tuple3f[] array )
    {
        this.vc = null;
        this.list = null;
        this.array = array;
        
        this.sourceType = SourceType.ARRAY;
    }
    
    public VertexList()
    {
        super();        
    }
    
    public VertexList( VertexContainer vc )
    {
        this();
        
        set( vc );
    }
    
    public VertexList( List<Tuple3f> list )
    {
        this();
        
        set( list );
    }
    
    public VertexList( Tuple3f[] array )
    {
        this();
        
        set( array );
    }
}
