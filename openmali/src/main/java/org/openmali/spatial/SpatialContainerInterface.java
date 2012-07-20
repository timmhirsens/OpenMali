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

import org.openmali.vecmath2.Tuple3f;

/**
 * <p>A spatial container holds objects which implement the
 * SpatialObjectInterface.  Different implementations of spatial
 * containers will have different capabilities and will implement
 * different interfaces to satisfy those capaibilies.  FrustumCulledInterface
 * and OcclusionCulledInterface are such implmentations</p>
 *
 * <p>Note: Some spatial containers might themselves be spatial objects.  This
 * allows for an octree being used for the entire world, but a bsp tree being
 * used for a model or even a whole subscene.  This then allows for large
 * sub-scenes to be used as occluders.  Another example is to put the terrain cells
 * into the oct-tree so that occlusion checks and raycasting checks can
 * be used.</p>
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public interface SpatialContainerInterface< T >
{
    /**
     * Inject the specified object into the container.  The handle
     * returned is necessary to update or remove the object.
     * @param object Object to be inserted
     * @return Spatial handle for the injected object
     */
    SpatialHandle< T > inject( Tuple3f center, float radius, Object object );
    
    SpatialHandle< T > inject( VertexContainer object );
    
    /**
     * Notifies the container that the size or location of the object has been
     * changed. For some implementations this will be extremly slow and costly,
     * while others, like SphereTrees this is extremely fast and efficient.
     * @param handle
     */
    void changed( SpatialHandle< T > handle );
    
    /**
     * Remove the object from the spatial container.
     * @param handle
     */
    void remove( SpatialHandle< T > handle );
    
    /**
     * Empties all the entries from the container
     */
    void empty();
}
