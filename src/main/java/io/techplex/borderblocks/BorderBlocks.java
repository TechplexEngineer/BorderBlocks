/*
 * Copyright (C) 2016 techplex
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.techplex.borderblocks;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author techplex
 */
public class BorderBlocks {
    
    private static boolean isMaterialAndData(Block b, Material mat, byte data) {
        return (b.getType() == mat&& b.getState().getData().getData() == data);
    }
    
    public static boolean isSpecialBlock(Block b) {
        return isBuildAllowBlock(b) ||
        isBuildDisallowBlock(b) ||
        isBorderBlock(b);
    }
    public static boolean isBuildAllowBlock(Block b) {
        return isMaterialAndData(b, Material.STAINED_CLAY, DyeColor.LIME.getData());
    }
    public static boolean isBuildDisallowBlock(Block b) {
        return isMaterialAndData(b, Material.STAINED_CLAY, DyeColor.RED.getData());              
    }
    
    public static boolean isBorderBlock(Block b) {
       return isMaterialAndData(b, Material.STAINED_CLAY, DyeColor.YELLOW.getData());             
    }
}
