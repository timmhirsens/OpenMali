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

import java.io.PrintStream;

import org.openmali.spatial.AxisIndicator;
import org.openmali.spatial.PlaneIndicator;
import org.openmali.spatial.SpatialNode;
import org.openmali.spatial.bodies.Box;
import org.openmali.spatial.bounds.Bounds;
import org.openmali.spatial.bounds.BoundsType;

/**
 * This is a non-standard implementation of an {@link QuadTree}'s cell.
 * It provides the possibility to use extended cells. These extended cells
 * are cells, that don't cover an octant of the parent cell,
 * but a half (one of six) or a quadrant (one of twelve).
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class QuadCell<T extends SpatialNode> extends Box {
	private static final long serialVersionUID = 3514614577953214775L;

	private final PlaneIndicator plane;
	private final AxisIndicator upAxis;

	private final int level;

	private final float centerW;
	private final float centerD;

	private final float width;
	private final float depth;
	private final float height;

	private final float halfWidth;
	private final float halfDepth;

	// regular octree-cells:
	private QuadCell<T> quFrontLeft = null;
	private QuadCell<T> quFrontRight = null;
	private QuadCell<T> quBackLeft = null;
	private QuadCell<T> quBackRight = null;

	private QuadCell<T> hLeft = null;
	private QuadCell<T> hRight = null;
	private QuadCell<T> hBack = null;
	private QuadCell<T> hFront = null;

	private final boolean useExtendedCells;

	public final PlaneIndicator getPlane() {
		return (plane);
	}

	public final AxisIndicator getUpAxis() {
		return (upAxis);
	}

	/*
	 * @SuppressWarnings( "unchecked" ) private final T[] newTArray( int length
	 * ) { return ( (T[])new Object[ length ] ); }
	 */
	private final Object[] newTArray(int length) {
		return (new Object[length]);
	}

	private Object[] nodes = newTArray(8);
	private int numNodes = 0;

	private boolean hasChildCells = false;

	/**
	 * @return true, if extended cells are <b>potentially</b> used.
	 * This doesn't necessarily mean, that extended cells are currently in use.
	 */
	public final boolean usesExtendedCells() {
		return (useExtendedCells);
	}

	/**
	 * @return this {@link QuadCell}'s level in the {@link QuadTree} (root-cell has 0).
	 */
	public final int getLevel() {
		return (level);
	}

	/*
	 * public final float getCenterX() { super.getCenterX() return ( centerX );
	 * }
	 * 
	 * public final float getCenterY() { return ( centerY ); }
	 * 
	 * public final float getCenterZ() { return ( centerZ ); }
	 */

	/**
	 * @return the width of this {@link QuadCell}.
	 */
	public final float getWidth() {
		return (width);
	}

	/**
	 * @return the depth of this {@link QuadCell}.
	 */
	public final float getDepth() {
		return (depth);
	}

	/**
	 * @return the height of this {@link QuadCell}.
	 */
	public final float getHeight() {
		return (height);
	}

	/**
	 * @return the half width of this {@link QuadCell}.
	 */
	public final float getHalfWidth() {
		return (halfWidth);
	}

	/**
	 * @return the half depth of this {@link QuadCell}.
	 */
	public final float getHalfDepth() {
		return (halfDepth);
	}

	public final QuadCell<T> getCellQuFrontLeft() {
		return (quFrontLeft);
	}

	public final QuadCell<T> getCellQuFrontRight() {
		return (quFrontRight);
	}

	public final QuadCell<T> getCellQuBackLeft() {
		return (quBackLeft);
	}

	public final QuadCell<T> getCellQuBackRight() {
		return (quBackRight);
	}

	public final int getNumNodes() {
		return (numNodes);
	}

	public final QuadCell<T> getCellHLeft() {
		return (hLeft);
	}

	public final QuadCell<T> getCellHRight() {
		return (hRight);
	}

	public final QuadCell<T> getCellHBack() {
		return (hBack);
	}

	public final QuadCell<T> getCellHFront() {
		return (hFront);
	}

	/**
	 * @param index
	 * 
	 * @return one node, that is stored in this {@link QuadCell}.
	 */
	@SuppressWarnings("unchecked")
	public final T getNode(int index) {
		return ((T) nodes[index]);
	}

	/**
	 * @return true, if this {@link QuadCell} currently has child {@link QuadCell}s.
	 * This also includes extended cells!
	 */
	public final boolean hasChildCells() {
		return (hasChildCells);
	}

	private void reinsertNodes(int minNodesBeforeSplit, int maxLevelForExtendedCells) {
		int pushedNodes = 0;

		for (int i = 0; i < getNumNodes(); i++) {
			QuadCell<T> cell = findChildCell(getNode(i).getWorldBounds(), getNode(i).getWorldBounds().getType(), minNodesBeforeSplit, maxLevelForExtendedCells, true);

			if (cell != this) {
				cell.insertNode_(getNode(i));
				nodes[i] = null;

				pushedNodes++;
			}
		}

		if (pushedNodes > 0) {
			Object[] temp = newTArray(getNumNodes() - pushedNodes);

			int j = 0;
			for (int i = 0; i < getNumNodes(); i++) {
				if (getNode(i) != null) {
					temp[j++] = getNode(i);
				}
			}

			this.nodes = temp;
			this.numNodes -= pushedNodes;
		}
	}

	private QuadCell<T> createFrontLeftQuadrant(int maxLevelForExtendedCells) {
		float childCenterX;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterX = centerX - width / 4f;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterX = centerX;
		}

		float childCenterY;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterY = centerY;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterY = centerY - depth / 4f;
		}

		float childCenterZ;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterZ = centerZ + depth / 4f;
		} else if (plane == PlaneIndicator.X_Y_PLANE) {
			childCenterZ = centerZ;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterZ = centerZ - width / 4f;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, halfWidth, halfDepth, height, useExtendedCells && (level < maxLevelForExtendedCells)));
	}

	private QuadCell<T> createFrontRightQuadrant(int maxLevelForExtendedCells) {
		float childCenterX;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterX = centerX + width / 4f;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterX = centerX;
		}

		float childCenterY;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterY = centerY;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterY = centerY - depth / 4f;
		}

		float childCenterZ;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterZ = centerZ + depth / 4f;
		} else if (plane == PlaneIndicator.X_Y_PLANE) {
			childCenterZ = centerZ;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterZ = centerZ + width / 4f;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, halfWidth, halfDepth, height, useExtendedCells && (level < maxLevelForExtendedCells)));
	}

	private QuadCell<T> createBackLeftQuadrant(int maxLevelForExtendedCells) {
		float childCenterX;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterX = centerX - width / 4f;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterX = centerX;
		}

		float childCenterY;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterY = centerY;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterY = centerY + depth / 4f;
		}

		float childCenterZ;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterZ = centerZ - depth / 4f;
		} else if (plane == PlaneIndicator.X_Y_PLANE) {
			childCenterZ = centerZ;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterZ = centerZ - width / 4f;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, halfWidth, halfDepth, height, useExtendedCells && (level < maxLevelForExtendedCells)));
	}

	private QuadCell<T> createBackRightQuadrant(int maxLevelForExtendedCells) {
		float childCenterX;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterX = centerX + width / 4f;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterX = centerX;
		}

		float childCenterY;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterY = centerY;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterY = centerY + depth / 4f;
		}

		float childCenterZ;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterZ = centerZ - depth / 4f;
		} else if (plane == PlaneIndicator.X_Y_PLANE) {
			childCenterZ = centerZ;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterZ = centerZ + width / 4f;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, halfWidth, halfDepth, height, useExtendedCells && (level < maxLevelForExtendedCells)));
	}

	private QuadCell<T> createLeftHalf()// ( int maxLevelForExtendedCells )
	{
		float childCenterX;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterX = centerX - width / 4f;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterX = centerX;
		}

		float childCenterY = centerY;

		float childCenterZ;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterZ = centerZ;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterZ = centerZ - width / 4f;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, halfWidth, depth, height, false));
	}

	private QuadCell<T> createRightHalf()// ( int maxLevelForExtendedCells )
	{
		float childCenterX;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterX = centerX + width / 4f;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterX = centerX;
		}

		float childCenterY = centerY;

		float childCenterZ;
		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			childCenterZ = centerZ;
		} else// if ( plane == PlaneIndicator.Z_Y_PLANE )
		{
			childCenterZ = centerZ + width / 4f;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, halfWidth, depth, height, false));
	}

	private QuadCell<T> createFrontHalf()// ( int maxLevelForExtendedCells )
	{
		float childCenterX = centerX;

		float childCenterY;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterY = centerY;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterY = centerY - depth / 4f;
		}

		float childCenterZ;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterZ = centerZ + depth / 4f;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterZ = centerZ;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, width, halfDepth, height, false));
	}

	private QuadCell<T> createBackHalf()// ( int maxLevelForExtendedCells )
	{
		float childCenterX = centerX;

		float childCenterY;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterY = centerY;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterY = centerY + depth / 4f;
		}

		float childCenterZ;
		if (plane == PlaneIndicator.X_Z_PLANE) {
			childCenterZ = centerZ - depth / 4f;
		} else// if ( ( plane == PlaneIndicator.X_Y_PLANE ) || ( plane ==
				// PlaneIndicator.Z_Y_PLANE ) )
		{
			childCenterZ = centerZ;
		}

		return (new QuadCell<T>(getLevel() + 1, childCenterX, childCenterY, childCenterZ, plane, width, halfDepth, height, false));
	}

	protected QuadCell<T> findChildCell(Bounds bounds, BoundsType type, int minNodesBeforeSplit, int maxLevelForExtendedCells, boolean ignoreReinserting) {
		if (!ignoreReinserting) {
			if (this.getNumNodes() < minNodesBeforeSplit) {
				return (this);
			} else if (this.getNumNodes() == minNodesBeforeSplit) {
				reinsertNodes(minNodesBeforeSplit, maxLevelForExtendedCells);
			}
		}

		// First check: left or right (or spanning)...

		if (BoundsHelper.getMaxX(plane, bounds, type) <= this.centerW) {
			// Check left half...

			if (BoundsHelper.getMinDepth(plane, bounds, type) >= this.centerD) {
				// Found front-left cell!

				if (quFrontLeft == null) {
					this.quFrontLeft = createFrontLeftQuadrant(maxLevelForExtendedCells);
					hasChildCells = true;
				}

				return (quFrontLeft.findChildCell(bounds, minNodesBeforeSplit, maxLevelForExtendedCells));
			}

			if (BoundsHelper.getMaxDepth(plane, bounds, type) <= this.centerD) {
				// Found back-left cell!

				if (quBackLeft == null) {
					this.quBackLeft = createBackLeftQuadrant(maxLevelForExtendedCells);
					hasChildCells = true;
				}

				return (quBackLeft.findChildCell(bounds, minNodesBeforeSplit, maxLevelForExtendedCells));
			}

			// bounds span the left front-back region!

			if (useExtendedCells) {
				// Found left half!

				if (hLeft == null) {
					this.hLeft = createLeftHalf();// ( maxLevelForExtendedCells
													// );
					hasChildCells = true;
				}

				return (hLeft);
			}

			return (this);
		}

		// Bounds don't fit into left half! => Check right half...

		if (BoundsHelper.getMinX(plane, bounds, type) >= this.centerW) {
			// Check right half...

			if (BoundsHelper.getMinDepth(plane, bounds, type) >= this.centerD) {
				// Found front-right cell!

				if (quFrontRight == null) {
					this.quFrontRight = createFrontRightQuadrant(maxLevelForExtendedCells);
					hasChildCells = true;
				}

				return (quFrontRight.findChildCell(bounds, minNodesBeforeSplit, maxLevelForExtendedCells));
			}

			if (BoundsHelper.getMaxDepth(plane, bounds, type) <= this.centerD) {
				// Found back-right cell!

				if (quBackRight == null) {
					this.quBackRight = createBackRightQuadrant(maxLevelForExtendedCells);
					hasChildCells = true;
				}

				return (quBackRight.findChildCell(bounds, minNodesBeforeSplit, maxLevelForExtendedCells));
			}

			// bounds span the right front-back region!

			if (useExtendedCells) {
				// Found right half!

				if (hRight == null) {
					this.hRight = createRightHalf();// (
													// maxLevelForExtendedCells
													// );
					hasChildCells = true;
				}

				return (hRight);
			}

			return (this);
		}

		// bounds span the left and right halfs!

		if (useExtendedCells) {
			// Check front and back halfs...

			if (BoundsHelper.getMinDepth(plane, bounds, type) >= this.centerD) {
				// Found front half!

				if (hFront == null) {
					this.hFront = createFrontHalf();// (
													// maxLevelForExtendedCells
													// );
					hasChildCells = true;
				}

				return (hFront);
			}

			if (BoundsHelper.getMaxDepth(plane, bounds, type) <= this.centerD) {
				// Found back half!

				if (hBack == null) {
					this.hBack = createBackHalf();// ( maxLevelForExtendedCells
													// );
					hasChildCells = true;
				}

				return (hBack);
			}
		}

		return (this);
	}

	protected QuadCell<T> findChildCell(Bounds bounds, int minNodesBeforeSplit, int maxLevelForExtendedCells) {
		/*
		 * BoundsType boundsType = bounds.getType();
		 * 
		 * if ( boundsType == BoundsType.AABB ) return ( findChildCell( bounds,
		 * bounds.getType(), minNodesBeforeSplit, maxLevelForExtendedCells,
		 * false ) ); else if ( boundsType == BoundsType.SPHERE )
		 */
		return (findChildCell(bounds, bounds.getType(), minNodesBeforeSplit, maxLevelForExtendedCells, false));

		// return ( this );
	}

	protected QuadCell<T> findChildCell(T node, int minNodesBeforeSplit, int maxLevelForExtendedCells) {
		return (findChildCell(node.getWorldBounds(), minNodesBeforeSplit, maxLevelForExtendedCells));
	}

	private final void insertNode_(T node) {
		final double growFactor = 1.0;

		if (numNodes == nodes.length) {
			Object[] nodes2 = newTArray((int) ((numNodes + 1) * growFactor));
			System.arraycopy(nodes, 0, nodes2, 0, numNodes);
			nodes = nodes2;
		}

		nodes[numNodes++] = node;
		node.setTreeCell(this);

		/*
		 * float minY = +Float.MAX_VALUE; float maxY = -Float.MAX_VALUE; for (
		 * int i = 0; i < numNodes; i++ ) {
		 * 
		 * }
		 */
	}

	/**
	 * Inserts a node into this {@link QuadCell} or a child (or child of a child, etc.).
	 * 
	 * @param node
	 * @param minNodesBeforeSplit
	 * @param maxLevelForExtendedCells
	 * 
	 * @return the chosen cell's level.
	 */
	public int insertNode(T node, int minNodesBeforeSplit, int maxLevelForExtendedCells) {
		QuadCell<T> cell = findChildCell(node, minNodesBeforeSplit, maxLevelForExtendedCells);

		cell.insertNode_(node);

		return (cell.getLevel());
	}

	private final void removeNode_(T node) {
		int index = -1;
		for (int i = 0; i < numNodes; i++) {
			if (nodes[i] == node) {
				index = i;
				break;
			}
		}

		/*
		 * We don't need to check this, since the precondition to enter this
		 * method is, that the node is in this cell!
		 */
		// if ( index == -1 )
		// return;

		System.arraycopy(nodes, index + 1, nodes, index, numNodes - index - 1);
		numNodes--;

		node.setTreeCell(null);
	}

	/**
	 * Removes a node from this cell (or a child, etc.).
	 * 
	 * @param node
	 * 
	 * @return true, if the node was contained in this cell (or a child, etc.).
	 */
	@SuppressWarnings("unchecked")
	public boolean removeNode(T node) {
		final Object treeCellObj = node.getTreeCell();
		if (treeCellObj == null)
			return (false);

		QuadCell<T> treeCell = (QuadCell<T>) treeCellObj;
		treeCell.removeNode_(node);

		return (true);
	}

	/**
	 * Removes all nodes from this cell and also removes all child cells.
	 */
	public void clear() {
		for (int i = 0; i < nodes.length; i++)
			nodes[i] = null;

		numNodes = 0;

		quBackLeft = null;
		quBackRight = null;
		quFrontLeft = null;
		quFrontRight = null;

		hLeft = null;
		hRight = null;
		hBack = null;
		hFront = null;
	}

	private static final void printIndentation(int indentation, PrintStream ps) {
		for (int i = 0; i < indentation; i++)
			ps.print("  ");
	}

	private void dumpCellSize(PrintStream ps) {
		if (width == depth)
			ps.print(width);
		else
			ps.print(width + ", " + depth);

		ps.print(", (" + height + ")");
	}

	@SuppressWarnings("unchecked")
	void dump(String cellName, int indentation, PrintStream ps) {
		printIndentation(indentation, ps);
		ps.print(this.getClass().getSimpleName() + " \"" + cellName + "\" ( level: " + getLevel() + ", center: [ " + centerX + ", " + centerY + ", " + centerZ + "], size: ");
		dumpCellSize(ps);
		ps.println(" )");

		printIndentation(indentation, ps);
		ps.println("{");

		printIndentation(indentation + 1, ps);

		if (numNodes == 0) {
			ps.println("Nodes:");

			printIndentation(indentation + 1, ps);
			ps.println("[No nodes]");
		} else {
			ps.println("Nodes (" + numNodes + "):");

			for (int i = 0; i < numNodes; i++) {
				printIndentation(indentation + 1, ps);
				ps.println(nodes[i] + ", " + ((T) nodes[i]).getWorldBounds());
			}
		}

		if (hasChildCells()) {
			printIndentation(indentation + 1, ps);
			ps.println("");

			dump(hLeft, "hLeft", indentation + 1, ps);
			dump(hRight, "hRight", indentation + 1, ps);
			dump(hBack, "hBack", indentation + 1, ps);
			dump(hFront, "hFront", indentation + 1, ps);

			dump(quBackLeft, "quBackLeft", indentation + 1, ps);
			dump(quBackRight, "quBackRight", indentation + 1, ps);
			dump(quFrontLeft, "quFrontLeft", indentation + 1, ps);
			dump(quFrontRight, "quFrontRight", indentation + 1, ps);
		}

		printIndentation(indentation, ps);
		ps.println("}");
	}

	static final void dump(QuadCell<?> cell, String cellName, int indentation, PrintStream ps) {
		if (cell == null)
			return;

		cell.dump(cellName, indentation, ps);
	}

	public void dump() {
		dump(this, "root", 0, System.out);
	}

	private static final float getLowerX(float centerX, PlaneIndicator plane, float width, float height) {
		switch (plane) {
		case X_Z_PLANE:
		case X_Y_PLANE:
			return (centerX - width / 2f);
		case Z_Y_PLANE:
			return (centerX - height / 2f);
		}

		throw new Error("Should not happen!");
	}

	private static final float getLowerY(float centerY, PlaneIndicator plane, float depth, float height) {
		switch (plane) {
		case X_Z_PLANE:
			return (centerY - height / 2f);
		case X_Y_PLANE:
		case Z_Y_PLANE:
			return (centerY - depth / 2f);
		}

		throw new Error("Should not happen!");
	}

	private static final float getLowerZ(float centerZ, PlaneIndicator plane, float width, float depth, float height) {
		switch (plane) {
		case X_Z_PLANE:
			return (centerZ - depth / 2f);
		case X_Y_PLANE:
			return (centerZ - height / 2f);
		case Z_Y_PLANE:
			return (centerZ - width / 2f);
		}

		throw new Error("Should not happen!");
	}

	private static final float getUpperX(float centerX, PlaneIndicator plane, float width, float height) {
		switch (plane) {
		case X_Z_PLANE:
		case X_Y_PLANE:
			return (centerX + width / 2f);
		case Z_Y_PLANE:
			return (centerX + height / 2f);
		}

		throw new Error("Should not happen!");
	}

	private static final float getUpperY(float centerY, PlaneIndicator plane, float depth, float height) {
		switch (plane) {
		case X_Z_PLANE:
			return (centerY + height / 2f);
		case X_Y_PLANE:
		case Z_Y_PLANE:
			return (centerY + depth / 2f);
		}

		throw new Error("Should not happen!");
	}

	private static final float getUpperZ(float centerZ, PlaneIndicator plane, float width, float depth, float height) {
		switch (plane) {
		case X_Z_PLANE:
			return (centerZ + depth / 2f);
		case X_Y_PLANE:
			return (centerZ + height / 2f);
		case Z_Y_PLANE:
			return (centerZ + width / 2f);
		}

		throw new Error("Should not happen!");
	}

	protected QuadCell(int level, float centerX, float centerY, float centerZ, PlaneIndicator plane, float width, float depth, float height, boolean useExtendedCells) {
		super(getLowerX(centerX, plane, width, height), getLowerY(centerY, plane, depth, height), getLowerZ(centerZ, plane, width, depth, height), getUpperX(centerX, plane, width, height), getUpperY(
				centerY, plane, depth, height), getUpperZ(centerZ, plane, width, depth, height));

		this.level = level;

		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;

		this.plane = plane;
		this.upAxis = plane.getNormalAxis();

		if ((plane == PlaneIndicator.X_Z_PLANE) || (plane == PlaneIndicator.X_Y_PLANE)) {
			this.centerW = centerX;
		} else if (plane == PlaneIndicator.Z_Y_PLANE) {
			this.centerW = centerZ;
		} else {
			throw new Error("Should not happen!");
		}

		if (plane == PlaneIndicator.X_Z_PLANE) {
			this.centerD = centerZ;
		} else if ((plane == PlaneIndicator.X_Y_PLANE) || (plane == PlaneIndicator.Z_Y_PLANE)) {
			this.centerD = centerY;
		} else {
			throw new Error("Should not happen!");
		}

		this.width = width;
		this.depth = depth;
		this.height = height;

		this.halfWidth = width / 2f;
		this.halfDepth = depth / 2f;

		this.useExtendedCells = useExtendedCells;

		nodes = new Object[4];
	}

	public QuadCell(int level, float centerX, float centerY, float centerZ, PlaneIndicator plane, float width, float depth, float height) {
		this(level, centerX, centerY, centerZ, plane, width, depth, height, true);
	}

	public QuadCell(int level, float centerX, float centerY, float centerZ, PlaneIndicator plane, float size, float height, boolean useExtendedCells) {
		this(level, centerX, centerY, centerZ, plane, size, size, height, useExtendedCells);
	}

	public QuadCell(int level, float centerX, float centerY, float centerZ, PlaneIndicator plane, float size, float height) {
		this(level, centerX, centerY, centerZ, plane, size, size, height, false);
	}
}
