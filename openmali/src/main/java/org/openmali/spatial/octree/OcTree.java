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
package org.openmali.spatial.octree;

import org.openmali.spatial.SpatialNode;
import org.openmali.vecmath2.Tuple3f;

/**
 * This is a non-standard {@link OcTree} implementation.
 * It provides the possibility to use extended cells. These extended cells
 * are cells, that don't cover an octant of the parent cell,
 * but a half (one of six) or a quadrant (one of twelve).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class OcTree<T extends SpatialNode>
{
    private final OcCell<T> rootCell;
    
    private int minNodesBeforeSplit = 8;
    private int maxLevelForExtendedCells = 0;
    
    private int maxLevel = 0;
    
    /**
     * @return the {@link OcTree}'s root {@link OcCell}.
     */
    public final OcCell<T> getRootCell()
    {
        return ( rootCell );
    }
    
    /**
     * @return the maximum currently used level of cells in this {@link OcTree}.
     */
    public final int getMaxLevel()
    {
        return ( maxLevel );
    }
    
    /**
     * @return the x-center of this {@link OcTree} (or of the root cell).
     */
    public final float getCenterX()
    {
        return ( rootCell.getCenterX() );
    }
    
    /**
     * @return the y-center of this {@link OcTree} (or of the root cell).
     */
    public final float getCenterY()
    {
        return ( rootCell.getCenterY() );
    }
    
    /**
     * @return the z-center of this {@link OcTree} (or of the root cell).
     */
    public final float getCenterZ()
    {
        return ( rootCell.getCenterZ() );
    }
    
    /**
     * @return the x-size of this {@link OcTree} (or of the root cell).
     */
    public final float getSizeX()
    {
        return ( rootCell.getSizeX() );
    }
    
    /**
     * @return the y-size of this {@link OcTree} (or of the root cell).
     */
    public final float getSizeY()
    {
        return ( rootCell.getSizeY() );
    }
    
    /**
     * @return the z-size of this {@link OcTree} (or of the root cell).
     */
    public final float getSizeZ()
    {
        return ( rootCell.getSizeZ() );
    }
    
    /**
     * Sets the minimum number of nodes, that must be inserted into a cell
     * to make the cell be splitted.
     * 
     * @param minNodesBeforeSplit
     */
    public void setMinNodesBeforeSplit( int minNodesBeforeSplit )
    {
        this.minNodesBeforeSplit = minNodesBeforeSplit;
    }
    
    /**
     * @return the minimum number of nodes, that must be inserted into a cell
     * to make the cell be splitted.
     */
    public final int getMinNodesBeforeSplit()
    {
        return ( minNodesBeforeSplit );
    }
    
    /**
     * Sets the maximum number of nodes, that can be inserted into a cell
     * before it is splitted into extended cells.
     * 
     * @param maxLevelForExtendedCells
     */
    public void setMaxLevelForExtendedCells( int maxLevelForExtendedCells )
    {
        this.maxLevelForExtendedCells = maxLevelForExtendedCells;
    }
    
    /**
     * @return the maximum number of nodes, that can be inserted into a cell
     * before it is splitted into extended cells.
     */
    public final int getMaxLevelForExtendedCells()
    {
        return ( maxLevelForExtendedCells );
    }
    
    /**
     * Inserts a node into the {@link OcTree}. This first searches the
     * {@link OcCell} to insert and then places the node into the cell.
     * 
     * @param node
     * 
     * @return the level of the {@link OcCell}, the node has been inserted to.
     */
    public final int insertNode( T node )
    {
        int level = rootCell.insertNode( node, getMinNodesBeforeSplit(), getMaxLevelForExtendedCells() );
        
        if ( level > maxLevel )
            maxLevel = level;
        
        return ( level );
    }
    
    /**
     * Removes the node from the {@link OcTree} (from its container cell).
     * 
     * @param node
     */
    public final void removeNode( T node )
    {
        rootCell.removeNode( node );
    }
    
    /**
     * Removes all nodes from this {@link OcTree}.
     */
    public final void clear()
    {
        rootCell.clear();
    }
    
    /**
     * This method must be called after a node's position or size has changed.
     * You don't need to call this method, if you exactly know, that the
     * modification won't affect the node's placement in the tree.
     * 
     * @param node
     */
    public final void updateNodePosition( T node )
    {
        if ( rootCell.removeNode( node ) )
            insertNode( node );
    }
    
    /**
     * Dumps the whole {@link OcTree} to System.out.
     */
    public void dump()
    {
        System.out.println( this.getClass().getSimpleName() + " ( max-level: " + getMaxLevel() + " )" );
        
        rootCell.dump();
    }
    
    public OcTree( float centerX, float centerY, float centerZ, float sizeX, float sizeY, float sizeZ, boolean useExtendedCells )
    {
        this.rootCell = new OcCell<T>( 0, centerX, centerY, centerZ, sizeX, sizeY, sizeZ, useExtendedCells );
    }
    
    public OcTree( Tuple3f center, float sizeX, float sizeY, float sizeZ, boolean useExtendedCells )
    {
        this( center.getX(), center.getY(), center.getZ(), sizeX, sizeY, sizeZ, useExtendedCells );
    }
    
    public OcTree( float centerX, float centerY, float centerZ, float size, boolean useExtendedCells )
    {
        this.rootCell = new OcCell<T>( 0, centerX, centerY, centerZ, size, useExtendedCells );
    }
    
    public OcTree( Tuple3f center, float size, boolean useExtendedCells )
    {
        this( center.getX(), center.getY(), center.getZ(), size, useExtendedCells );
    }
    
    /*
    private static class MyNode implements SpatialNode
    {
        private org.openmali.spatial.bounds.Bounds worldBounds;
        private Object treeCell = null;
        
        public void setTreeCell( Object treeCell )
        {
            this.treeCell = treeCell;
        }
        
        public Object getTreeCell()
        {
            return ( treeCell );
        }
        
        public org.openmali.spatial.bounds.Bounds getWorldBounds()
        {
            return ( worldBounds );
        }
        
        @Override
        public String toString()
        {
            return ( super.toString() + ": " + worldBounds.toString() );
        }
        
        public MyNode( float centerX, float centerY, float centerZ, float size, boolean useBoundingSphere )
        {
            if ( useBoundingSphere )
                this.worldBounds = new org.openmali.spatial.bounds.BoundingSphere( centerX, centerY, centerZ, size / 2f );
            else
                this.worldBounds = new org.openmali.spatial.bounds.BoundingBox( centerX - size / 2f, centerY - size / 2f, centerZ - size / 2f, centerX + size / 2f, centerY + size / 2f, centerZ + size / 2f );
        }
    }
    
    public static void main( String[] args )
    {
        OcTree<MyNode> ocTree = new OcTree<MyNode>( 0f, 0f, 0f, 6f );
        
        ocTree.insertNode( new MyNode( -4.5f, -4.5f, -4.5f, 6f, true ) );
        
        ocTree.dump();
    }
    */
}
