/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Colorable;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;

/**
 *
 * @author techplex
 */
public class PlayerListener implements Listener {
	

	/**
	 * Handle the removal of turtles when blocks are broken
	 * @param event 
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
//         Block b = event.getBlock();
//        if (b.getType() == Material.WOOL) {
//            Colorable md = ((Colorable)b.getState().getData());
//            System.out.println(md.getColor());
//        }
//        if (b.getType() == Material.STAINED_CLAY) {
//            MaterialData md = b.getState().getData();
//            System.out.println(md+" | "+DyeColor.getByData(md.getData())+" | "+md.getData()+" | "+DyeColor.RED.getData());
//            
//        }
//        System.out.println("TBA: "+BorderBlocks.isTurtleBuildAllowBlock(b));
    }
    
    @EventHandler
	public void onPlace(BlockPlaceEvent event) {
        
        Block placed = event.getBlockPlaced();
        Location l = placed.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Player p = event.getPlayer();

        String id = (new Location(p.getWorld(), x, y-1, z)).getBlock().toString();
        
//        p.sendMessage("You placed a block above: "+id);
        
        if (! Perms.canBuildPlayerHere(p, x, y, z)) {
            event.setCancelled(true);
            p.sendMessage("Sorry you cannot place blocks here.");
        }
        
        
    }
    


    
}
