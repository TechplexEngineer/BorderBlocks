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
        Block removed = event.getBlock();
        System.out.println(removed);
        Location l = removed.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Player p = event.getPlayer();
        
        if (! Perms.canPlayerDigHere(p, x, y, z)) {
            event.setCancelled(true);
            p.sendMessage("Sorry you cannot remove blocks here.");
        }
    }
    
    @EventHandler
	public void onPlace(BlockPlaceEvent event) {
        
        Block placed = event.getBlockPlaced();
        Location l = placed.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Player p = event.getPlayer();
        
        if (! Perms.canPlayerBuildHere(p, x, y, z)) {
            event.setCancelled(true);
            p.sendMessage("Sorry you cannot place blocks here.");
        }
        
        
    }
    


    
}
