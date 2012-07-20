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
package org.openmali.types.twodee;

import org.openmali.vecmath2.Tuple2i;

/**
 * A basic 2-dimensional unpositioned rectangle.
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Kevin Finley (aka Horati)
 */
public class Dim2i implements Sized2i {
	private static final Dim2iPool POOL = new Dim2iPool();

	private int width, height;
	protected boolean isDirty = true;

	/**
	 * @return true, if this object has been modified since the last setClean() call.
	 * 
	 * @see #setClean()
	 */
	public final boolean isDirty() {
		return (isDirty);
	}

	/**
	 * Marks this object as not dirty.
	 */
	public void setClean() {
		this.isDirty = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Dim2i setSize(int width, int height) {
		if ((width == this.width) && (height == this.height))
			return (this);

		this.width = width;
		this.height = height;

		this.isDirty = true;

		return (this);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setWidth(int width) {
		setSize(width, this.height);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setHeight(int height) {
		setSize(this.width, height);
	}

	/**
	 * @return the rectangle's width
	 */
	public final int getWidth() {
		return (width);
	}

	/**
	 * @return the rectangle's height
	 */
	public final int getHeight() {
		return (height);
	}

	/**
	 * {@inheritDoc}
	 */
	public final float getAspect() {
		if (getHeight() != 0)
			return ((float) getWidth() / (float) getHeight());

		return (0f);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Dim2i setSize(Sized2iRO size) {
		return (setSize(size.getWidth(), size.getHeight()));
	}

	/**
	 * {@inheritDoc}
	 */
	public final Dim2i setSize(Tuple2i size) {
		return (setSize(size.getX(), size.getY()));
	}

	/**
	 * @return this size as a tuple.
	 */
	public <T extends Tuple2i> T toTuple(T tuple) {
		tuple.set(getWidth(), getHeight());

		return (tuple);
	}

	/**
	 * @return this size as a tuple.
	 */
	public Tuple2i toTuple() {
		return (new Tuple2i(width, height));
	}

	/**
	 * Sets this rectangle's coordinates to the given rectangle's ones.
	 * 
	 * @param width the rectangle's width
	 * @param height the rectangle's height
	 */
	public final Dim2i set(int width, int height) {
		return (setSize(width, height));
	}

	/**
	 * Sets this rectangle's coordinates to the given rectangle's ones.
	 * 
	 * @param size
	 */
	public final Dim2i set(Sized2iRO size) {
		return (setSize(size.getWidth(), size.getHeight()));
	}

	/**
	 * Adds the deltas to the values.
	 * 
	 * @param dw
	 * @param dh
	 */
	public final Dim2i add(int dw, int dh) {
		this.width += dw;
		this.height += dh;

		return (this);
	}

	/**
	 * Adds the deltas to the values.
	 * 
	 * @param dw
	 */
	public final Dim2i addW(int dw) {
		this.width += dw;

		return (this);
	}

	/**
	 * Adds the deltas to the values.
	 * 
	 * @param dh
	 */
	public final Dim2i addH(int dh) {
		this.height += dh;

		return (this);
	}

	/**
	 * Multiplies the values with these factors.
	 * 
	 * @param factW
	 * @param factH
	 */
	public final Dim2i scale(int factW, int factH) {
		this.width *= factW;
		this.height *= factH;

		return (this);
	}

	/**
	 * Multiplies the values with these factors.
	 * 
	 * @param factW
	 */
	public final Dim2i scaleW(int factW) {
		this.width *= factW;

		return (this);
	}

	/**
	 * Multiplies the values with these factors.
	 * 
	 * @param factH
	 */
	public final Dim2i scaleH(int factH) {
		this.height *= factH;

		return (this);
	}

	public boolean equals(Sized2iRO rect) {
		if (rect == null)
			return (false);

		if (rect == this)
			return (true);

		return ((rect.getWidth() == this.getWidth()) && (rect.getHeight() == this.getHeight()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return (false);

		if (!(o instanceof Sized2iRO))
			return (false);

		return (equals((Sized2iRO) o));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (this.getClass().getSimpleName() + "( width = " + getWidth() + ", height = " + getHeight() + ", aspect = " + getAspect() + " )");
	}

	/**
	 * Creates a new 2-dimensional unpositioned rectangle.
	 * 
	 * @param width the rectangle's width
	 * @param height the rectangle's height
	 */
	public Dim2i(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Creates a new 2-dimensional unpositioned rectangle and copies the template's coordinates.
	 * 
	 * @param template
	 */
	public Dim2i(Sized2iRO template) {
		this(template.getWidth(), template.getHeight());
	}

	/**
	 * Creates a new 2-dimensional unpositioned rectangle with zero position and size.
	 */
	public Dim2i() {
		this(0, 0);
	}

	public static final Dim2i fromPool() {
		return (POOL.alloc());
	}

	public static final Dim2i fromPool(int width, int height) {
		Dim2i inst = POOL.alloc();

		inst.set(width, height);

		return (inst);
	}

	public static final void toPool(Dim2i dim) {
		POOL.free(dim);
	}
}
