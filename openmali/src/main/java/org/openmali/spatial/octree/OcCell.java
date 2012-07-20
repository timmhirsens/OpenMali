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

import java.io.PrintStream;

import org.openmali.spatial.SpatialNode;
import org.openmali.spatial.bodies.Box;
import org.openmali.spatial.bounds.Bounds;
import org.openmali.spatial.bounds.BoundsType;

/**
 * This is a non-standard implementation of an {@link OcTree}'s cell.
 * It provides the possibility to use extended cells. These extended cells
 * are cells, that don't cover an octant of the parent cell,
 * but a half (one of six) or a quadrant (one of twelve).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class OcCell<T extends SpatialNode> extends Box
{
    private static final long serialVersionUID = 5236486961328161769L;
    
    private final int level;
    
    private final float sizeX;
    private final float sizeY;
    private final float sizeZ;
    
    private final float halfSizeX;
    private final float halfSizeY;
    private final float halfSizeZ;
    
    // regular octree-cells:
    private OcCell<T> ocUpperFrontLeft   = null;
    private OcCell<T> ocUpperFrontRight  = null;
    private OcCell<T> ocUpperBackLeft    = null;
    private OcCell<T> ocUpperBackRight   = null;
    private OcCell<T> ocLowerFrontLeft   = null;
    private OcCell<T> ocLowerFrontRight  = null;
    private OcCell<T> ocLowerBackLeft    = null;
    private OcCell<T> ocLowerBackRight   = null;
    
    // extended cells:
    private OcCell<T> quUpperBack   = null;
    private OcCell<T> quUpperFront  = null;
    private OcCell<T> quUpperLeft   = null;
    private OcCell<T> quUpperRight  = null;
    private OcCell<T> quLowerBack   = null;
    private OcCell<T> quLowerFront  = null;
    private OcCell<T> quLowerLeft   = null;
    private OcCell<T> quLowerRight  = null;
    private OcCell<T> quBackLeft    = null;
    private OcCell<T> quBackRight   = null;
    private OcCell<T> quFrontLeft   = null;
    private OcCell<T> quFrontRight  = null;
    
    private OcCell<T> hUpper  = null;
    private OcCell<T> hLower  = null;
    private OcCell<T> hLeft   = null;
    private OcCell<T> hRight  = null;
    private OcCell<T> hBack   = null;
    private OcCell<T> hFront  = null;
    
    private final boolean useExtendedCells;
    
    /*
    @SuppressWarnings( "unchecked" )
    private final T[] newTArray( int length )
    {
        return ( (T[])new Object[ length ] );
    }
    */
    private final Object[] newTArray( int length )
    {
        return ( new Object[ length ] );
    }
    
    private Object[] nodes = newTArray( 8 );
    private int numNodes = 0;
    
    private boolean hasChildCells = false;
    
    /**
     * @return true, if extended cells are <b>potentially</b> used.
     * This doesn't necessarily mean, that extended cells are currently in use.
     */
    public final boolean usesExtendedCells()
    {
        return ( useExtendedCells );
    }
    
    /**
     * @return this {@link OcCell}'s level in the {@link OcTree} (root-cell has 0).
     */
    public final int getLevel()
    {
        return ( level );
    }
    
    /*
    public final float getCenterX()
    {
        super.getCenterX()
        return ( centerX );
    }
    
    public final float getCenterY()
    {
        return ( centerY );
    }
    
    public final float getCenterZ()
    {
        return ( centerZ );
    }
    */
    
    /**
     * @return the x-size of this {@link OcCell}.
     */
    public final float getSizeX()
    {
        return ( sizeX );
    }
    
    /**
     * @return the y-size of this {@link OcCell}.
     */
    public final float getSizeY()
    {
        return ( sizeY );
    }
    
    /**
     * @return the z-size of this {@link OcCell}.
     */
    public final float getSizeZ()
    {
        return ( sizeZ );
    }
    
    /**
     * @return the half x-size of this {@link OcCell}.
     */
    public final float getHalfSizeX()
    {
        return ( halfSizeX );
    }
    
    /**
     * @return the half y-size of this {@link OcCell}.
     */
    public final float getHalfSizeY()
    {
        return ( halfSizeY );
    }
    
    /**
     * @return the half z-size of this {@link OcCell}.
     */
    public final float getHalfSizeZ()
    {
        return ( halfSizeZ );
    }
    
    public final OcCell<T> getCellOcUpperFrontLeft()
    {
        return ( ocUpperFrontLeft );
    }
    
    public final OcCell<T> getCellOcUpperFrontRight()
    {
        return ( ocUpperFrontRight );
    }
    
    public final OcCell<T> getCellOcUpperBackLeft()
    {
        return ( ocUpperBackLeft );
    }
    
    public final OcCell<T> getCellOcUpperBackRight()
    {
        return ( ocUpperBackRight );
    }
    
    public final OcCell<T> getCellOcLowerFrontLeft()
    {
        return ( ocLowerFrontLeft );
    }
    
    public final OcCell<T> getCellOcLowerFrontRight()
    {
        return ( ocLowerFrontRight );
    }
    
    public final OcCell<T> getCellOcLowerBackLeft()
    {
        return ( ocLowerBackLeft );
    }
    
    public final OcCell<T> getCellOcLowerBackRight()
    {
        return ( ocLowerBackRight );
    }
    
    public final int getNumNodes()
    {
        return ( numNodes );
    }
    
    public final OcCell<T> getCellQuUpperBack()
    {
        return ( quUpperBack );
    }
    
    public final OcCell<T> getCellQuUpperFront()
    {
        return ( quUpperFront );
    }
    
    public final OcCell<T> getCellQuUpperLeft()
    {
        return ( quUpperLeft );
    }
    
    public final OcCell<T> getCellQuUpperRight()
    {
        return ( quUpperRight );
    }
    
    public final OcCell<T> getCellQuLowerBack()
    {
        return ( quLowerBack );
    }
    
    public final OcCell<T> getCellQuLowerFront()
    {
        return ( quLowerFront );
    }
    
    public final OcCell<T> getCellQuLowerLeft()
    {
        return ( quLowerLeft );
    }
    
    public final OcCell<T> getCellQuLowerRight()
    {
        return ( quLowerRight );
    }
    
    public final OcCell<T> getCellQuBackLeft()
    {
        return ( quBackLeft );
    }
    
    public final OcCell<T> getCellQuBackRight()
    {
        return ( quBackRight );
    }
    
    public final OcCell<T> getCellQuFrontLeft()
    {
        return ( quFrontLeft );
    }
    
    public final OcCell<T> getCellQuFrontRight()
    {
        return ( quFrontRight );
    }
    
    
    public final OcCell<T> getCellHUpper()
    {
        return ( hUpper );
    }
    
    public final OcCell<T> getCellHLower()
    {
        return ( hLower );
    }
    
    public final OcCell<T> getCellHLeft()
    {
        return ( hLeft );
    }
    
    public final OcCell<T> getCellHRight()
    {
        return ( hRight );
    }
    
    public final OcCell<T> getCellHBack()
    {
        return ( hBack );
    }
    
    public final OcCell<T> getCellHFront()
    {
        return ( hFront );
    }
    
    
    /**
     * @param index
     * 
     * @return one node, that is stored in this {@link OcCell}.
     */
    @SuppressWarnings( "unchecked" )
    public final T getNode( int index )
    {
        return ( (T)nodes[ index ] );
    }
    
    /**
     * @return true, if this {@link OcCell} currently has child {@link OcCell}s.
     * This also includes extended cells!
     */
    public final boolean hasChildCells()
    {
        return ( hasChildCells );
    }
    
    private void reinsertNodes( int minNodesBeforeSplit, int maxLevelForExtendedCells )
    {
        int pushedNodes = 0;
        
        for ( int i = 0; i < getNumNodes(); i++ )
        {
            OcCell<T> cell = findChildCell( getNode( i ).getWorldBounds(), getNode( i ).getWorldBounds().getType(), minNodesBeforeSplit, maxLevelForExtendedCells, true );
            
            if ( cell != this )
            {
                cell.insertNode_( getNode( i ) );
                nodes[i] = null;
                
                pushedNodes++;
            }
        }
        
        if ( pushedNodes > 0 )
        {
            Object[] temp = newTArray( getNumNodes() - pushedNodes );
            
            int j = 0;
            for ( int i = 0; i < getNumNodes(); i++ )
            {
                if ( getNode( i ) != null )
                {
                    temp[j++] = getNode( i );
                }
            }
            
            this.nodes = temp;
            this.numNodes -= pushedNodes;
        }
    }
    
    protected OcCell<T> findChildCell( Bounds bounds, BoundsType type, int minNodesBeforeSplit, int maxLevelForExtendedCells, boolean ignoreReinserting )
    {
        if ( !ignoreReinserting )
        {
            if ( this.getNumNodes() < minNodesBeforeSplit )
            {
                return ( this );
            }
            else if ( this.getNumNodes() == minNodesBeforeSplit )
            {
                reinsertNodes( minNodesBeforeSplit, maxLevelForExtendedCells );
            }
        }
        
        // First check: upper or lower (or spanning)...
        
        if ( BoundsHelper.getMinY( bounds, type ) >= this.centerY )
        {
            // Check upper half...
            
            if ( BoundsHelper.getMinZ( bounds, type ) >= this.centerZ )
            {
                // Check upper front-half...
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found upper-front-right cell!
                    
                    if ( ocUpperFrontRight == null )
                    {
                        this.ocUpperFrontRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY + sizeY / 4f, centerZ + sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocUpperFrontRight.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found upper-front-left cell!
                    
                    if ( ocUpperFrontLeft == null )
                    {
                        this.ocUpperFrontLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY + sizeY / 4f, centerZ + sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocUpperFrontLeft.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                // bounds span the upper-front left-right region!
                
                if ( useExtendedCells )
                {
                    // Found upper-font-quadrant!
                    
                    if ( quUpperFront == null )
                    {
                        this.quUpperFront = new OcCell<T>( getLevel() + 1, centerX, centerY + sizeY / 4f, centerZ + sizeZ / 4f, sizeX, halfSizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quUpperFront );
                }
                
                return ( this );
            }
            
            if ( BoundsHelper.getMaxZ( bounds, type ) <= this.centerZ )
            {
                // Check upper back-half...
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found upper-back-right cell!
                    
                    if ( ocUpperBackRight == null )
                    {
                        this.ocUpperBackRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY + sizeY / 4f, centerZ - sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocUpperBackRight.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found upper-back-left cell!
                    
                    if ( ocUpperBackLeft == null )
                    {
                        this.ocUpperBackLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY + sizeY / 4f, centerZ - sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocUpperBackLeft.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                // bounds span the upper-back left-right-region
                
                if ( useExtendedCells )
                {
                    // Found upper-back-quadrant!
                    
                    if ( quUpperBack == null )
                    {
                        this.quUpperBack = new OcCell<T>( getLevel() + 1, centerX, centerY + sizeY / 4f, centerZ - sizeZ / 4f, sizeX, halfSizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quUpperBack );
                }
                
                return ( this );
            }
            
            // bounds span the upper front-back-region
            
            if ( useExtendedCells )
            {
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found upper-left cell!
                    
                    if ( quUpperLeft == null )
                    {
                        this.quUpperLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY + sizeY / 4f, centerZ, halfSizeX, halfSizeY, sizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quUpperLeft );
                }
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found upper-right cell!
                    
                    if ( quUpperRight == null )
                    {
                        this.quUpperRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY + sizeY / 4f, centerZ, halfSizeX, halfSizeY, sizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quUpperRight );
                }
                
                if ( hUpper == null )
                {
                    this.hUpper = new OcCell<T>( getLevel() + 1, centerX, centerY + sizeY / 4f, centerZ, sizeX, halfSizeY, sizeZ, false );
                    hasChildCells = true;
                }
                
                return ( hUpper );
            }
            
            return ( this );
        }
        
        if ( BoundsHelper.getMaxY( bounds, type ) <= this.centerY )
        {
            // Check lower half...
            
            if ( BoundsHelper.getMinZ( bounds, type ) >= this.centerZ )
            {
                // Check lower front-half...
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found lower-front-right cell!
                    
                    if ( ocLowerFrontRight == null )
                    {
                        this.ocLowerFrontRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY - sizeY / 4f, centerZ + sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocLowerFrontRight.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found lower-front-left cell!
                    
                    if ( ocLowerFrontLeft == null )
                    {
                        this.ocLowerFrontLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY - sizeY / 4f, centerZ + sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocLowerFrontLeft.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                // bounds span the lower-front left-right region!
                
                if ( useExtendedCells )
                {
                    // Found lower-front-quadrant!
                    
                    if ( quLowerFront == null )
                    {
                        this.quLowerFront = new OcCell<T>( getLevel() + 1, centerX, centerY - sizeY / 4f, centerZ + sizeZ / 4f, sizeX, halfSizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quLowerFront );
                }
                
                return ( this );
            }
            
            if ( BoundsHelper.getMaxZ( bounds, type ) <= this.centerZ )
            {
                // Check lower back-half...
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found lower-back-right cell!
                    
                    if ( ocLowerBackRight == null )
                    {
                        this.ocLowerBackRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY - sizeY / 4f, centerZ - sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocLowerBackRight.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found lower-back-left cell!
                    
                    if ( ocLowerBackLeft == null )
                    {
                        this.ocLowerBackLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY - sizeY / 4f, centerZ - sizeZ / 4f, halfSizeX, halfSizeY, halfSizeZ, useExtendedCells && ( level < maxLevelForExtendedCells ) );
                        hasChildCells = true;
                    }
                    
                    return ( ocLowerBackLeft.findChildCell( bounds, minNodesBeforeSplit, maxLevelForExtendedCells ) );
                }
                
                // bounds span the lower-back left-right-region
                
                if ( useExtendedCells )
                {
                    // Found lower-back-quadrant!
                    
                    if ( quLowerBack == null )
                    {
                        this.quLowerBack = new OcCell<T>( getLevel() + 1, centerX, centerY - sizeY / 4f, centerZ - sizeZ / 4f, sizeX, halfSizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quLowerBack );
                }
                
                return ( this );
            }
            
            // bounds span the lower front-back-region
            
            if ( useExtendedCells )
            {
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found lower-left cell!
                    
                    if ( quLowerLeft == null )
                    {
                        this.quLowerLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY - sizeY / 4f, centerZ, halfSizeX, halfSizeY, sizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quLowerLeft );
                }
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found lower-right cell!
                    
                    if ( quLowerRight == null )
                    {
                        this.quLowerRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY - sizeY / 4f, centerZ, halfSizeX, halfSizeY, sizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quLowerRight );
                }
                
                if ( hLower == null )
                {
                    this.hLower = new OcCell<T>( getLevel() + 1, centerX, centerY - sizeY / 4f, centerZ, sizeX, halfSizeY, sizeZ, false );
                    hasChildCells = true;
                }
                
                return ( hLower );
            }
            
            return ( this );
        }
        
        // bounds span the upper-lower-region
        
        if ( useExtendedCells )
        {
            if ( BoundsHelper.getMinZ( bounds, type ) >= this.centerZ )
            {
                // Checking front-half...
                
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found front-left cell!
                    
                    if ( quFrontLeft == null )
                    {
                        this.quFrontLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY, centerZ + sizeZ / 4f, halfSizeX, sizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quFrontLeft );
                }
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found front-right cell!
                    
                    if ( quFrontRight == null )
                    {
                        this.quFrontRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY, centerZ + sizeZ / 4f, halfSizeX, sizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quFrontRight );
                }
                
                // Found front-half!
                
                if ( hFront == null )
                {
                    this.hFront = new OcCell<T>( getLevel() + 1, centerX, centerY, centerZ + sizeZ / 4f, sizeX, sizeY, halfSizeZ, false );
                    hasChildCells = true;
                }
                
                return ( hFront );
            }
            
            if ( BoundsHelper.getMaxZ( bounds, type ) <= this.centerZ )
            {
                // Checking back-half...
                
                if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
                {
                    // Found back-left cell!
                    
                    if ( quBackLeft == null )
                    {
                        this.quBackLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY, centerZ - sizeZ / 4f, halfSizeX, sizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quBackLeft );
                }
                
                if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
                {
                    // Found back-right cell!
                    
                    if ( quBackRight == null )
                    {
                        this.quBackRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY, centerZ - sizeZ / 4f, halfSizeX, sizeY, halfSizeZ, false );
                        hasChildCells = true;
                    }
                    
                    return ( quBackRight );
                }
                
                // Found back-half!
                
                if ( hBack == null )
                {
                    this.hBack = new OcCell<T>( getLevel() + 1, centerX, centerY, centerZ - sizeZ / 4f, sizeX, sizeY, halfSizeZ, false );
                    hasChildCells = true;
                }
                
                return ( hBack );
            }
            
            if ( BoundsHelper.getMaxX( bounds, type ) <= this.centerX )
            {
                // Found left-half!
                
                if ( hLeft == null )
                {
                    this.hLeft = new OcCell<T>( getLevel() + 1, centerX - sizeX / 4f, centerY, centerZ, halfSizeX, sizeY, sizeZ, false );
                    hasChildCells = true;
                }
                
                return ( hLeft );
            }
            
            if ( BoundsHelper.getMinX( bounds, type ) >= this.centerX )
            {
                // Found right-half!
                
                if ( hRight == null )
                {
                    this.hRight = new OcCell<T>( getLevel() + 1, centerX + sizeX / 4f, centerY, centerZ, halfSizeX, sizeY, sizeZ, false );
                    hasChildCells = true;
                }
                
                return ( hRight );
            }
        }
        
        return ( this );
    }
    
    protected OcCell<T> findChildCell( Bounds bounds, int minNodesBeforeSplit, int maxLevelForExtendedCells )
    {
        switch ( bounds.getType() )
        {
            case AABB:
            case SPHERE:
                return ( findChildCell( bounds, bounds.getType(), minNodesBeforeSplit, maxLevelForExtendedCells, false ) );
        }
        
        return ( this );
    }
    
    protected OcCell<T> findChildCell( T node, int minNodesBeforeSplit, int maxLevelForExtendedCells )
    {
        return ( findChildCell( node.getWorldBounds(), minNodesBeforeSplit, maxLevelForExtendedCells ) );
    }
    
    private final void insertNode_( T node )
    {
        final double growFactor = 1.0;
        
        if ( numNodes == nodes.length )
        {
            Object[] nodes2 = newTArray( (int)( ( numNodes + 1 ) * growFactor ) );
            System.arraycopy( nodes, 0, nodes2, 0, numNodes );
            nodes = nodes2;
        }
        
        nodes[ numNodes++ ] = node;
        node.setTreeCell( this );
    }
    
    /**
     * Inserts a node into this {@link OcCell} or a child (or child of a child, etc.).
     * 
     * @param node
     * @param minNodesBeforeSplit
     * @param maxLevelForExtendedCells
     * 
     * @return the chosen cell's level.
     */
    public int insertNode( T node, int minNodesBeforeSplit, int maxLevelForExtendedCells )
    {
        OcCell<T> cell = findChildCell( node, minNodesBeforeSplit, maxLevelForExtendedCells );
        
        cell.insertNode_( node );
        
        return ( cell.getLevel() );
    }
    
    private final void removeNode_( T node )
    {
        int index = -1;
        for ( int i = 0; i < numNodes; i++ )
        {
            if ( nodes[ i ] == node )
            {
                index = i;
                break;
            }
        }
        
        /*
         * We don't need to check this, since the precondition
         * to enter this method is, that the node is in this cell!
         */
        //if ( index == -1 )
        //    return;
        
        System.arraycopy( nodes, index + 1, nodes, index, numNodes - index - 1 );
        numNodes--;
        
        node.setTreeCell( null );
    }
    
    /**
     * Removes a node from this cell (or a child, etc.).
     * 
     * @param node
     * 
     * @return true, if the node was contained in this cell (or a child, etc.).
     */
    @SuppressWarnings( "unchecked" )
    public boolean removeNode( T node )
    {
        final Object treeCellObj = node.getTreeCell();
        if ( treeCellObj == null )
            return ( false );
        
        OcCell<T> treeCell = (OcCell<T>)treeCellObj;
        treeCell.removeNode_( node );
        
        return ( true );
    }
    
    /**
     * Removes all nodes from this cell and also removes all child cells.
     */
    public void clear()
    {
        for ( int i = 0; i < nodes.length; i++ )
            nodes[ i ] = null;
        
        numNodes = 0;
        
        ocUpperFrontLeft   = null;
        ocUpperFrontRight  = null;
        ocUpperBackLeft    = null;
        ocUpperBackRight   = null;
        ocLowerFrontLeft   = null;
        ocLowerFrontRight  = null;
        ocLowerBackLeft    = null;
        ocLowerBackRight   = null;
        
        quUpperBack   = null;
        quUpperFront  = null;
        quUpperLeft   = null;
        quUpperRight  = null;
        quLowerBack   = null;
        quLowerFront  = null;
        quLowerLeft   = null;
        quLowerRight  = null;
        quBackLeft    = null;
        quBackRight   = null;
        quFrontLeft   = null;
        quFrontRight  = null;
        
        hUpper  = null;
        hLower  = null;
        hLeft   = null;
        hRight  = null;
        hBack   = null;
        hFront  = null;
    }
    
    private static final void printIndentation( int indentation, PrintStream ps )
    {
        for ( int i = 0; i < indentation; i++ )
            ps.print( "  " );
    }
    
    private void dumpCellSize( PrintStream ps )
    {
        if ( ( sizeX == sizeY ) && ( sizeY == sizeZ ) )
            ps.print( sizeX );
        else
            ps.print( sizeX + ", " + sizeY + ", " + sizeZ );
    }
    
    @SuppressWarnings( "unchecked" )
    void dump( String cellName, int indentation, PrintStream ps )
    {
        printIndentation( indentation, ps );
        ps.print( this.getClass().getSimpleName() + " \"" + cellName + "\" ( level: " + getLevel() + ", center: [ " + centerX + ", " + centerY + ", " + centerZ + "], size: " );
        dumpCellSize( ps );
        ps.println( " )" );
        
        printIndentation( indentation, ps );
        ps.println( "{" );
        
        printIndentation( indentation + 1, ps );
        
        if ( numNodes == 0 )
        {
            ps.println( "Nodes:" );
            
            printIndentation( indentation + 1, ps );
            ps.println( "[No nodes]" );
        }
        else
        {
            ps.println( "Nodes (" + numNodes + "):" );
            
            for ( int i = 0; i < numNodes; i++ )
            {
                printIndentation( indentation + 1, ps );
                ps.println( nodes[ i ] + ", " + ((T)nodes[ i ]).getWorldBounds() );
            }
        }
        
        if ( hasChildCells() )
        {
            printIndentation( indentation + 1, ps );
            ps.println( "" );
            
            dump( hUpper, "hUpper", indentation + 1, ps );
            dump( hLower, "hLower", indentation + 1, ps );
            dump( hLeft, "hLeft", indentation + 1, ps );
            dump( hRight, "hRight", indentation + 1, ps );
            dump( hBack, "hBack", indentation + 1, ps );
            dump( hFront, "hFront", indentation + 1, ps );
            
            dump( quUpperBack, "quUpperBack", indentation + 1, ps );
            dump( quUpperFront, "quUpperFront", indentation + 1, ps );
            dump( quUpperLeft, "quUpperLeft", indentation + 1, ps );
            dump( quUpperRight, "quUpperRight", indentation + 1, ps );
            dump( quLowerBack, "quLowerBack", indentation + 1, ps );
            dump( quLowerFront, "quLowerFront", indentation + 1, ps );
            dump( quLowerLeft, "quLowerLeft", indentation + 1, ps );
            dump( quLowerRight, "quLowerRight", indentation + 1, ps );
            dump( quBackLeft, "quBackLeft", indentation + 1, ps );
            dump( quBackRight, "quBackRight", indentation + 1, ps );
            dump( quFrontLeft, "quFrontLeft", indentation + 1, ps );
            dump( quFrontRight, "quFrontRight", indentation + 1, ps );
            
            dump( ocUpperFrontLeft, "ocUpperFrontLeft", indentation + 1, ps );
            dump( ocUpperFrontRight, "ocUpperFrontRight", indentation + 1, ps );
            dump( ocUpperBackLeft, "ocUpperBackLeft", indentation + 1, ps );
            dump( ocUpperBackRight, "ocUpperBackRight", indentation + 1, ps );
            dump( ocLowerFrontLeft, "ocLowerFrontLeft", indentation + 1, ps );
            dump( ocLowerFrontRight, "ocLowerFrontRight", indentation + 1, ps );
            dump( ocLowerBackLeft, "ocLowerBackLeft", indentation + 1, ps );
            dump( ocLowerBackRight, "ocLowerBackRight", indentation + 1, ps );
        }
        
        printIndentation( indentation, ps );
        ps.println( "}" );
    }
    
    static final void dump( OcCell<?> cell, String cellName, int indentation, PrintStream ps )
    {
        if ( cell == null )
            return;
        
        cell.dump( cellName, indentation, ps );
    }
    
    public void dump()
    {
        dump( this, "root", 0, System.out );
    }
    
    protected OcCell( int level, float centerX, float centerY, float centerZ, float sizeX, float sizeY, float sizeZ, boolean useExtendedCells )
    {
        super( centerX - sizeX / 2f, centerY - sizeY / 2f, centerZ - sizeZ / 2f, centerX + sizeX / 2f, centerY + sizeY / 2f, centerZ + sizeZ / 2f );
        
        this.level = level;
        
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        
        this.halfSizeX = sizeX / 2f;
        this.halfSizeY = sizeY / 2f;
        this.halfSizeZ = sizeZ / 2f;
        
        this.useExtendedCells = useExtendedCells;
        
        nodes = new Object[ 8 ];
    }
    
    public OcCell( int level, float centerX, float centerY, float centerZ, float sizeX, float sizeY, float sizeZ )
    {
        this( level, centerX, centerY, centerZ, sizeX, sizeY, sizeZ, true );
    }
    
    public OcCell( int level, float centerX, float centerY, float centerZ, float size, boolean useExtendedCells )
    {
        this( level, centerX, centerY, centerZ, size, size, size, useExtendedCells );
    }
    
    public OcCell( int level, float centerX, float centerY, float centerZ, float size )
    {
        this( level, centerX, centerY, centerZ, size, size, size, false );
    }
}
