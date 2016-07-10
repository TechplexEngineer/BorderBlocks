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

import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

/**
 * Handle player move events and ensure that they don't move as prohibited by
 * border blocks.
 * @author techplex
 */
public class PlayerMoveListener implements Listener {
	
	private BorderBlocksPlugin plugin;
	private Location lastValid;
	
    public PlayerMoveListener(BorderBlocksPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(this, plugin);

    }
// We'll assume that if they spawn somewhere they are allowed to be there.
//    @EventHandler
//    public void onPlayerRespawn(PlayerRespawnEvent event) {
//        Player player = event.getPlayer();
//
//        testMoveTo(player, event.getRespawnLocation(), MoveType.RESPAWN, true);
//    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entity = event.getEntered();
        if (entity instanceof Player) {
            Player player = (Player) entity;

            if (null != testMoveTo(player, null, event.getVehicle().getLocation(), MoveType.EMBARK, true)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
		
        Optional<Location> overriding = testMoveTo(player, event.getFrom(), event.getTo(), MoveType.MOVE, false);

        if (overriding.isPresent()) {
			Location override = overriding.get();
            override.setPitch(event.getTo().getPitch());
            override.setYaw(event.getTo().getYaw());

            event.setTo(override.clone());

            Entity vehicle = player.getVehicle();
            if (vehicle != null) {
                vehicle.eject();

                Entity current = vehicle;
                while (current != null) {
                    current.eject();
                    vehicle.setVelocity(new Vector());
                    if (vehicle instanceof LivingEntity) {
                        vehicle.teleport(override.clone());
                    } else {
                        vehicle.teleport(override.clone().add(0, 1, 0));
                    }
                    current = current.getVehicle();
                }

                player.teleport(override.clone().add(0, 1, 0));

                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.teleport(override.clone().add(0, 1, 0));
                    }
                }, 1);
            }
        }
    }
	
	/**
     * Test movement to the given location.
     *
     * <p>If a non-null {@link Location} is returned, the player should be
     * at that location instead of where the player has tried to move to.</p>
     *
     * <p>If the {@code moveType} is cancellable
     * ({@link MoveType#isCancellable()}, then the last valid location will
     * be set to the given one.</p>
     *
     * @param player The player
     * @param to The new location
	 * @param from the previous location
     * @param moveType The type of move
     * @param forced Whether to force a check
     * @return The overridden location, if the location is being overridden
     */
    public Optional<Location> testMoveTo(Player player, Location to, Location from, MoveType moveType, boolean forced) {
		
		if (from.getWorld() == to.getWorld()) {
			int x1 = Math.min(from.getBlockX(), to.getBlockX());

			for(int y=0; y<255; y++) {
				Block b = from.getWorld().getBlockAt(x1, y, from.getBlockZ());
				if (BorderBlocks.isBorderBlock(b)) {

					double vx = from.getX() - to.getX();
					double vy = from.getY() - to.getY();
					double vz = from.getZ() - to.getZ();

					Vector v = new Vector(vx, vy, vz);
					v.multiply(2);
					plugin.getLogger().info(""+v);
					Location reject = from.clone();

					reject.subtract(v);

					return Optional.of(reject);
				} 
			}

		} else {
			plugin.getLogger().info("Player changed worlds! From: "+from.getWorld().getName()+" To: "+to.getWorld().getName());
		}
		return Optional.empty();
    }

}
