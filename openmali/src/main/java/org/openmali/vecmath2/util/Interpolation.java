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
package org.openmali.vecmath2.util;

import org.openmali.FastMath;
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;

/**
 * Utils for animation interpolation.
 * 
 * @author Matias Leone (aka Maguila)
 * @author Amos Wenger (aka BlueSky)
 */
public class Interpolation
{
    /**
     * Normalized Linear Interpolation between two Quaternions
     * 
     * @param quatOrigin
     *                origin quaternion
     * @param quatDestiny
     *                destiny quaternion
     * @param delta
     *                interpolation time
     * @param toInterpolate
     */
    public final static void nlerp( Quaternion4f quatOrigin, Quaternion4f quatDestiny, float delta, Quaternion4f toInterpolate )
    {
        Quaternion4f res = new Quaternion4f( quatDestiny );
        res.sub( quatOrigin );
        res.scale( delta );
        res.add( quatOrigin );
        
        toInterpolate.set( fastNormalize( res ) );
    }
    
    /**
     * Fast quaternion normalization - fast method
     * 
     * @param quat
     * @return quaternion normalized
     */
    public static Quaternion4f fastNormalize( Quaternion4f quat )
    {
        final float s = quat.getA() * quat.getA() + quat.getB() * quat.getB() + quat.getC() * quat.getC() + quat.getD() * quat.getD();
        float k = isqrtApproxInNeighborhood( s );
        
        if ( s < 0.83042395f )
        {
            k *= isqrtApproxInNeighborhood( s );
            
            if ( s < 0.30174562f )
            {
                k *= isqrtApproxInNeighborhood( s );
            }
        }
        
        quat.mul( k );
        
        return ( quat );
        
    }
    
    /**
     * Constants used by isqrtApproxInNeighborhood();
     */
    private static final float NEIGHBORHOOD = 0.959066f;
    private static final float SCALE = 1.000311f;
    private static final float ADDITIVE_CONSTANT = SCALE / FastMath.sqrt( NEIGHBORHOOD );
    private static final float FACTOR = SCALE * ( -0.5f / ( NEIGHBORHOOD * FastMath.sqrt( NEIGHBORHOOD ) ) );
    
    private static float isqrtApproxInNeighborhood( float s )
    {
        return ( ADDITIVE_CONSTANT + FACTOR * ( s - NEIGHBORHOOD ) );
    }
    
    /**
     * Quaternion normalization - traditional method
     * 
     * @param quat4f
     */
    @SuppressWarnings("unused")
    private static Quaternion4f normalize( Quaternion4f quat4f )
    {
        float lengthSq = quat4f.getA() * quat4f.getA() + quat4f.getB() * quat4f.getB() +
                         quat4f.getC() * quat4f.getC() + quat4f.getD() * quat4f.getD();
        if ( lengthSq == 0.0f )
        {
            return ( quat4f );
        }
        if ( lengthSq != 1.0f )
        {
            float scale = ( 1.0f / FastMath.sqrt( lengthSq ) );
            quat4f.mul( scale );
        }
        
        return ( quat4f );
    }
    
    /**
     * Performs Spherical Linear Interpolation (SLERP) between two quaternions.
     * 
     * @param quatOrigin
     * @param quatDestiny
     * @param deltaT
     * @param toInterpolate
     */
    @SuppressWarnings("unused")
    private static final void slerp( Quaternion4f quatOrigin, Quaternion4f quatDestiny, float deltaT, Quaternion4f toInterpolate )
    {
        float dot = quatOrigin.getA() * quatDestiny.getA() + quatOrigin.getB() * quatDestiny.getB() +
                    quatOrigin.getC() * quatDestiny.getC() + quatOrigin.getD() * quatDestiny.getD();
        
        float sign;
        if ( dot < 0.0f )
        {
            dot = -dot;
            sign = -1.0f;
        }
        else
        {
            sign = 1.0f;
        }
        if ( dot >= 0.99999f )
        {
            toInterpolate.set( quatOrigin );
            return;
        }
        float angle = FastMath.acos( dot );
        float denom = FastMath.sin( angle );
        float weight1 = FastMath.sin( ( 1.0f - deltaT ) * angle ) / denom;
        float weight2 = sign * FastMath.sin( deltaT * angle ) / denom;
        
        toInterpolate.set( weight1 * quatOrigin.getA() + weight2 * quatDestiny.getA(),
                           weight1 * quatOrigin.getB() + weight2 * quatDestiny.getB(),
                           weight1 * quatOrigin.getC() + weight2 * quatDestiny.getC(),
                           weight1 * quatOrigin.getD() + weight2 * quatDestiny.getD()
                         );
        return;
    }
    
    /**
     * Interpolate between two Tuple3fs in 3D space.
     * 
     * @param toInterpolate
     * @param tupleOrigin
     *                The keyframe "before" now
     * @param delta
     *                The time that has passed since the last update.
     * @param tupleDestiny
     *                The keyframe "after" now
     */
    public static void interpolate(Tuple3f tupleOrigin, Tuple3f tupleDestiny, float delta,  Tuple3f toInterpolate )
    {
        final Vector3f deltaVec = Vector3f.fromPool();
        
        // space distance beetween both "keyframes"
        deltaVec.sub( tupleDestiny, tupleOrigin );
        
        // interpolate translation/scale with delta
        deltaVec.scale( delta );
        toInterpolate.set( tupleOrigin );
        toInterpolate.add( deltaVec );
        
        Vector3f.toPool( deltaVec );
    }
}
