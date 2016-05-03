/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author techplex
 */
public class Perms {
    public static boolean canBuildPlayerHere(Player player, int x, int y, int z) {
        //remember in MC y is the altitude while x and z are the lat and lon

        //are we in a restricted area
        if ( ! State.getInstance().isInRestrictedArea(new Location(player.getWorld(), x, y, z)))
        {
            System.out.println("Not in Restricted Area");
            return true;
        }
        else
        {
            System.out.println("In Restricted Area");
            boolean allowBuild = State.getInstance().isStudentBuildingEnabled(); //is student building enabled
            
            //search down for build allow of build deny block
            //start at block y and look downward for special blocks which prevent building, stopping at a depth of -64 or the first build allow or disallow block
            //if we don't find a build disallow block and building is enabled return true
            for (int alt = y - 1; alt > alt - 64 && alt >= 0; --alt) {
                Block b = new Location(player.getWorld(), x, alt, z).getBlock();
                if (BorderBlocks.isBuildAllowBlock(b)) {
                    allowBuild = true;
                    break;
                }
                if (BorderBlocks.isBuildDisallowBlock(b)) {
                    allowBuild = false;
                    break;
                }
                if (BorderBlocks.isBorderBlock(b)) {
                    allowBuild = false;
                    break;
                }
            }
            //If we have determined that the player can build
            //starting a block y look up for a border block. if found, return false, else true
            if (allowBuild) {
                for (int alt = y + 1; alt <= 255; ++alt) {
                    Block b = new Location(player.getWorld(), x, alt, z).getBlock();
                    if (BorderBlocks.isBorderBlock(b)) {
                        allowBuild = false;
                        break;
                    }
                }
            }

            return allowBuild;
        }
    }
}
