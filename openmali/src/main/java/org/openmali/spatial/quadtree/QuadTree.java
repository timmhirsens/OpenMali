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
package org.openmali.spatial.quadtree;

import org.openmali.spatial.AxisIndicator;
import org.openmali.spatial.PlaneIndicator;
import org.openmali.spatial.SpatialNode;
import org.openmali.vecmath2.Tuple3f;

/**
 * This is a non-standard {@link QuadTree} implementation.
 * It provides the possibility to use extended cells. These extended cells
 * are cells, that don't cover a quadant of the parent cell,
 * but also a half (one of four).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class QuadTree<T extends SpatialNode> {
	public static final PlaneIndicator DEFAULT_PLANE = PlaneIndicator.X_Z_PLANE;

	private final QuadCell<T> rootCell;

	private int minNodesBeforeSplit = 4;
	private int maxLevelForExtendedCells = 0;

	private int maxLevel = 0;

	/**
	 * @return the plane covered by this {@link QuadTree}.
	 */
	public final PlaneIndicator getPlane() {
		return (rootCell.getPlane());
	}

	/**
	 * @return the axis used as up-axis by this {@link QuadTree}.
	 */
	public final AxisIndicator getUpAxis() {
		return (rootCell.getUpAxis());
	}

	/**
	 * @return the {@link QuadTree}'s root {@link QuadCell}.
	 */
	public final QuadCell<T> getRootCell() {
		return (rootCell);
	}

	/**
	 * @return the maximum currently used level of cells in this {@link QuadTree}.
	 */
	public final int getMaxLevel() {
		return (maxLevel);
	}

	/**
	 * @return the x-center of this {@link QuadTree} (or of the root cell).
	 */
	public final float getCenterX() {
		return (rootCell.getCenterX());
	}

	/**
	 * @return the y-center of this {@link QuadTree} (or of the root cell).
	 */
	public final float getCenterY() {
		return (rootCell.getCenterY());
	}

	/**
	 * @return the z-center of this {@link QuadTree} (or of the root cell).
	 */
	public final float getCenterZ() {
		return (rootCell.getCenterZ());
	}

	/**
	 * @return the width of this {@link QuadTree} (or of the root cell).
	 */
	public final float getWidth() {
		return (rootCell.getWidth());
	}

	/**
	 * @return the depth of this {@link QuadTree} (or of the root cell).
	 */
	public final float getDepth() {
		return (rootCell.getDepth());
	}

	/**
	 * Sets the minimum number of nodes, that must be inserted into a cell
	 * to make the cell be splitted.
	 * 
	 * @param minNodesBeforeSplit
	 */
	public void setMinNodesBeforeSplit(int minNodesBeforeSplit) {
		this.minNodesBeforeSplit = minNodesBeforeSplit;
	}

	/**
	 * @return the minimum number of nodes, that must be inserted into a cell
	 * to make the cell be splitted.
	 */
	public final int getMinNodesBeforeSplit() {
		return (minNodesBeforeSplit);
	}

	/**
	 * Sets the maximum number of nodes, that can be inserted into a cell
	 * before it is splitted into extended cells.
	 * 
	 * @param maxLevelForExtendedCells
	 */
	public void setMaxLevelForExtendedCells(int maxLevelForExtendedCells) {
		this.maxLevelForExtendedCells = maxLevelForExtendedCells;
	}

	/**
	 * @return the maximum number of nodes, that can be inserted into a cell
	 * before it is splitted into extended cells.
	 */
	public final int getMaxLevelForExtendedCells() {
		return (maxLevelForExtendedCells);
	}

	/**
	 * Inserts a node into the {@link QuadTree}. This first searches the
	 * {@link QuadCell} to insert and then places the node into the cell.
	 * 
	 * @param node
	 * 
	 * @return the level of the {@link QuadCell}, the node has been inserted to.
	 */
	public final int insertNode(T node) {
		int level = rootCell.insertNode(node, getMinNodesBeforeSplit(), getMaxLevelForExtendedCells());

		if (level > maxLevel)
			maxLevel = level;

		return (level);
	}

	/**
	 * Removes the node from the {@link QuadTree} (from its container cell).
	 * 
	 * @param node
	 */
	public final void removeNode(T node) {
		rootCell.removeNode(node);
	}

	/**
	 * Removes all nodes from this {@link QuadTree}.
	 */
	public final void clear() {
		rootCell.clear();
	}

	/**
	 * This method must be called after a node's position or size has changed.
	 * You don't need to call this method, if you exactly know, that the
	 * modification won't affect the node's placement in the tree.
	 * 
	 * @param node
	 */
	public final void updateNodePosition(T node) {
		if (rootCell.removeNode(node))
			insertNode(node);
	}

	/**
	 * Dumps the whole {@link QuadTree} to System.out.
	 */
	public void dump() {
		System.out.println(this.getClass().getSimpleName() + " ( max-level: " + getMaxLevel() + " )");

		rootCell.dump();
	}

	public QuadTree(float centerX, float centerY, float centerZ, PlaneIndicator plane, float width, float depth, float height, boolean useExtendedCells) {
		this.rootCell = new QuadCell<T>(0, centerX, centerY, centerZ, plane, width, depth, height, useExtendedCells);
	}

	public QuadTree(Tuple3f center, PlaneIndicator plane, float width, float depth, float height, boolean useExtendedCells) {
		this(center.getX(), center.getY(), center.getZ(), plane, width, depth, height, useExtendedCells);
	}

	public QuadTree(float centerX, float centerY, float centerZ, PlaneIndicator plane, float size, float height, boolean useExtendedCells) {
		this.rootCell = new QuadCell<T>(0, centerX, centerY, centerZ, plane, size, height, useExtendedCells);
	}

	public QuadTree(Tuple3f center, PlaneIndicator plane, float size, float height, boolean useExtendedCells) {
		this(center.getX(), center.getY(), center.getZ(), plane, size, height, useExtendedCells);
	}

	private static final PlaneIndicator getPlane(AxisIndicator upAxis) {
		switch (upAxis) {
		case POSITIVE_Y_AXIS:
			return (PlaneIndicator.X_Z_PLANE);
		case POSITIVE_Z_AXIS:
			return (PlaneIndicator.X_Y_PLANE);
		case NEGATIVE_X_AXIS:
			return (PlaneIndicator.Z_Y_PLANE);
		default:
			throw new IllegalArgumentException("upAxis");
		}
	}

	public QuadTree(float centerX, float centerY, float centerZ, AxisIndicator upAxis, float width, float depth, float height, boolean useExtendedCells) {
		this(centerX, centerY, centerZ, getPlane(upAxis), width, depth, height, useExtendedCells);
	}

	public QuadTree(Tuple3f center, AxisIndicator upAxis, float width, float depth, float height, boolean useExtendedCells) {
		this(center.getX(), center.getY(), center.getZ(), upAxis, width, depth, height, useExtendedCells);
	}

	public QuadTree(float centerX, float centerY, float centerZ, AxisIndicator upAxis, float size, float height, boolean useExtendedCells) {
		this(centerX, centerY, centerZ, getPlane(upAxis), size, height, useExtendedCells);
	}

	public QuadTree(Tuple3f center, AxisIndicator upAxis, float size, float height, boolean useExtendedCells) {
		this(center.getX(), center.getY(), center.getZ(), upAxis, size, height, useExtendedCells);
	}

	public QuadTree(float centerX, float centerY, float centerZ, float width, float depth, float height, boolean useExtendedCells) {
		this(centerX, centerY, centerZ, DEFAULT_PLANE, width, depth, height, useExtendedCells);
	}

	public QuadTree(Tuple3f center, float width, float depth, float height, boolean useExtendedCells) {
		this(center.getX(), center.getY(), center.getZ(), width, depth, height, useExtendedCells);
	}

	public QuadTree(float centerX, float centerY, float centerZ, float size, float height, boolean useExtendedCells) {
		this(centerX, centerY, centerZ, DEFAULT_PLANE, size, height, useExtendedCells);
	}

	public QuadTree(Tuple3f center, float size, float height, boolean useExtendedCells) {
		this(center.getX(), center.getY(), center.getZ(), size, height, useExtendedCells);
	}

	/*
	 * private static class MyNode implements SpatialNode { private
	 * org.openmali.spatial.bounds.Bounds worldBounds; private Object treeCell =
	 * null;
	 * 
	 * public void setTreeCell( Object treeCell ) { this.treeCell = treeCell; }
	 * 
	 * public Object getTreeCell() { return ( treeCell ); }
	 * 
	 * public org.openmali.spatial.bounds.Bounds getWorldBounds() { return (
	 * worldBounds ); }
	 * 
	 * @Override public String toString() { return ( super.toString() + ": " +
	 * worldBounds.toString() ); }
	 * 
	 * public MyNode( float centerX, float centerY, float centerZ, float size,
	 * boolean useBoundingSphere ) { if ( useBoundingSphere ) this.worldBounds =
	 * new org.openmali.spatial.bounds.BoundingSphere( centerX, centerY,
	 * centerZ, size / 2f ); else this.worldBounds = new
	 * org.openmali.spatial.bounds.BoundingBox( centerX - size / 2f, centerY -
	 * size / 2f, centerZ - size / 2f, centerX + size / 2f, centerY + size / 2f,
	 * centerZ + size / 2f ); } }
	 * 
	 * public static void main( String[] args ) { OcTree<MyNode> ocTree = new
	 * OcTree<MyNode>( 0f, 0f, 0f, 6f );
	 * 
	 * ocTree.insertNode( new MyNode( -4.5f, -4.5f, -4.5f, 6f, true ) );
	 * 
	 * ocTree.dump(); }
	 */
}
