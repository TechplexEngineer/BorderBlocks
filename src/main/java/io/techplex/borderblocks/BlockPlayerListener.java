/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author techplex
 */
public class BlockPlayerListener implements Listener {
	
	private PlayerPerms perms;
	
	public BlockPlayerListener(PlayerPerms perms) {
		this.perms = perms;
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
        Block removed = event.getBlock();
        Location l = removed.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Player p = event.getPlayer();
        
        if (! perms.canPlayerDigHere(p, x, y, z)) {
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
        
        if (! perms.canPlayerBuildHere(p, x, y, z)) {
            event.setCancelled(true);
            p.sendMessage("Sorry you cannot place blocks here.");
        }
        
        
    }
    


    
}
