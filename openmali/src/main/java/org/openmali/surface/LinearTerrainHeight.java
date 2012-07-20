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
package org.openmali.surface;

import org.openmali.FastMath;

/**
 * Used to evaluate the height of a terrain at any point of this terrain.
 * 
 * In a nutshell, it gives z = f(x,y) with a 2D array of height samples,
 * interpolated in a linear fashion.
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public class LinearTerrainHeight {
    
    public static float evaluateZ(float[][] height, float hx, float hy,
            float x, float y) {
        
        //System.out.println("For coords = "+x+", "+y);
        
        // Divide coords by cellSize so we have unit squares to work with
        x = x / hx;
        y = y / hy;
        
        //System.out.println("Divided, now : "+x+", "+y);
        
        // Compute floor X and ceil X
        int mx = (int) FastMath.floor(x);
        int px = (int) FastMath.ceil(x);
        
        // Avoid mx == px
        if (mx == px) {
            //System.out.println("Avoid mx==px !!!!!!");
            px += 1;
        }
        
        // Compute floor Y and ceil Y
        int my = (int) FastMath.floor(y);
        int py = (int) FastMath.ceil(y);
        
        // Avoid my == py
        if (my == py) {
            //System.out.println("Avoid my==py !!!!!!");
            py += 1;
        }
        
        // Compute height on (mx, py)-(px, py) line
        float e1 = getZ(height, mx, py) * (1 - (x - mx))
        + getZ(height, px, py) * (1 - (px - x));
        
        // Compute height on (mx, my)-(px, my) line
        float e2 = getZ(height, mx, my) * (1 - (x - mx))
        + getZ(height, px, my) * (1 - (px - x));
        
        // Compute height on (x, y)
        float h = e1 * (1 - (py - y)) + e2 * (1 - (y - my));
        
        //System.out.println("Found height : "+h);
        
        return h;
        
    }
    
    public static int getDimX(float[][] height) {
        
        return height[0].length;
        
    }
    
    public static int getDimY(float[][] height) {
        
        return height.length;
        
    }
    
    public static float getZ(float[][] height, int x, int y) {
        
        float z = 0f;
        
        int rx = Math.min(x, getDimX(height) - 1);
        rx = Math.max(rx, 0);
        int ry = Math.min(y, getDimY(height) - 1);
        ry = Math.max(ry, 0);
        
        /*
        if(x != rx) {
            System.out.println("X fixed. Was "+x+", now "+rx);
        }
        if(y != ry) {
            System.out.println("Y fixed. Was "+y+", now "+ry);
        }
        */
        
        try {
            z = height[rx][ry];
            /*
            if(x != rx || y != ry) {
                System.out.println("Gives height "+z);
            }
            */
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        
        return z;
        
    }
    
}
