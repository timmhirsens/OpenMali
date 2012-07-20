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

import java.util.ArrayList;

import org.openmali.types.twodee.util.ResizeListener2i;

/**
 * A basic 2-dimensional unpositioned rectangle.
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Kevin Finley (aka Horati)
 */
public class ExtDim2i extends Dim2i implements ExtSized2i {
	private final ArrayList<ResizeListener2i> resizeListeners = new ArrayList<ResizeListener2i>();

	/**
	 * {@inheritDoc}
	 * Notification takes place in the thread that called this.setSize(...). 
	 */
	public void addResizeListener(ResizeListener2i listener) {
		this.resizeListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeResizeListener(ResizeListener2i listener) {
		this.resizeListeners.remove(listener);
	}

	protected void fireResizeEvent(int oldWidth, int oldHeight, int newWidth, int newHeight) {
		for (int i = 0; i < resizeListeners.size(); i++) {
			resizeListeners.get(i).onObjectResized(this, oldWidth, oldHeight, newWidth, newHeight);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExtDim2i setSize(int width, int height) {
		final int oldWidth = getWidth();
		final int oldHeight = getHeight();

		if ((oldWidth != width) || (oldHeight != height)) {
			super.setSize(width, height);

			fireResizeEvent(oldWidth, oldHeight, width, height);
		}
		/*
		 * else { super.setSize( width, height ); }
		 */

		return (this);
	}

	/**
	 * Creates a new 2-dimensional unpositioned rectangle.
	 * 
	 * @param width the rectangle's width
	 * @param height the rectangle's height
	 */
	public ExtDim2i(int width, int height) {
		super(width, height);
	}

	/**
	 * Creates a new 2-dimensional unpositioned rectangle and copies the template's coordinates.
	 * 
	 * @param template
	 */
	public ExtDim2i(Sized2iRO template) {
		this(template.getWidth(), template.getHeight());
	}

	/**
	 * Creates a new 2-dimensional unpositioned rectangle with zero position and size.
	 */
	public ExtDim2i() {
		this(0, 0);
	}
}
