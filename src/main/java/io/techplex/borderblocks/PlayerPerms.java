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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author techplex
 */
public class PlayerPerms {
	
	private PluginState state;
	
	public PlayerPerms(PluginState state) {
		this.state = state;
	}
	
	
    public boolean canPlayerBuildHere(Player player, int x, int y, int z) {
        //remember in MC y is the altitude while x and z are the lat and lon

		boolean allowBuild = state.isStudentBuildingEnabled(); //is student building enabled

		allowBuild = blocksAllowBuilding(allowBuild, player, x, y, z);

		return allowBuild;

        
    }
    public boolean canPlayerDigHere(Player player, int x, int y, int z) {
        //remember in MC y is the altitude while x and z are the lat and lon
        Location loc = new Location(player.getWorld(), x, y, z);
        //are we in a restricted area


		if (BorderBlocks.isSpecialBlock(loc.getBlock())) {
			return false;
		}
		boolean allowDig = state.isStudentBuildingEnabled(); //is student building enabled

		allowDig = blocksAllowBuilding(allowDig, player, x, y, z);

		return allowDig;
        
    }
    private static boolean blocksAllowBuilding(boolean allowChange, Player player, int x, int y, int z) {
        //search down for build allow of build deny block
        //start at block y and look downward for special blocks which prevent building, stopping at a depth of -64 or the first build allow or disallow block
        //if we don't find a build disallow block and building is enabled return true
        for (int alt = y - 1; alt > alt - 64 && alt >= 0; --alt) {
            Block b = new Location(player.getWorld(), x, alt, z).getBlock();
            if (BorderBlocks.isBuildAllowBlock(b)) {
                allowChange = true;
                break;
            }
            if (BorderBlocks.isBuildDisallowBlock(b)) {
                allowChange = false;
                break;
            }
            if (BorderBlocks.isBorderBlock(b)) {
                allowChange = false;
                break;
            }
        }
        //If we have determined that the player can build
        //starting a block y look up for a border block. if found, return false, else true
        if (allowChange) {
            for (int alt = y + 1; alt <= 255; ++alt) {
                Block b = new Location(player.getWorld(), x, alt, z).getBlock();
                if (BorderBlocks.isBorderBlock(b)) {
                    allowChange = false;
                    break;
                }
            }
        }

        return allowChange;
    }     
}
