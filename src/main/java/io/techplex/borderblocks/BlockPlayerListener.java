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
