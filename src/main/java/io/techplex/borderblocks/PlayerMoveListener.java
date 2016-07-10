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
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
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

	public String loc2str(Location loc) {
		return "<"+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+">";
	}
	/**
	 * Raised when an entity enters a vehicle.
	 * @param event 
	 */
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entity = event.getEntered();

        if (entity instanceof Player) {
            Player player = (Player) entity;
			Location from = player.getLocation().clone();
			Location to = event.getVehicle().getLocation();
			plugin.getLogger().info(loc2str(from)+" "+loc2str(to));
			
			Optional<Location> overriding = testMoveTo(player,from, to, MoveType.EMBARK, true);

            if (overriding.isPresent()) {
                event.setCancelled(true);
            }
        }
    }
	@EventHandler(priority = EventPriority.HIGH)
	public void onVehicleMove(VehicleMoveEvent event) {
		Vehicle vehicle = event.getVehicle();
		Entity entity = vehicle.getPassenger();
		if (entity instanceof Player) {
            Player player = (Player) entity;
			
			Optional<Location> overriding = testMoveTo(player, event.getFrom(), event.getTo(), MoveType.RIDE, false);
			
			if (overriding.isPresent()) {
				Location override = overriding.get();
			
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        Optional<Location> overriding = testMoveTo(player, event.getFrom(), event.getTo(), MoveType.MOVE, false);

        if (overriding.isPresent()) {
			Location override = overriding.get();
//            override.setPitch(event.getTo().getPitch());
//            override.setYaw(event.getTo().getYaw());

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
    public Optional<Location> testMoveTo(Player player, Location from, Location to, MoveType moveType, boolean forced) {
		
//		if (from.getWorld() == to.getWorld()) {

			int x1 = to.getBlockX();
			int x2 = x1;
			if (from != null) {
				x1 = Math.min(from.getBlockX(), to.getBlockX());
				x2 = Math.max(to.getBlockX(), from.getBlockX());
			}
			
			int z1 = to.getBlockZ();
			int z2 = z1;
			if (from != null) {
				z1 = Math.min(from.getBlockZ(), to.getBlockZ());
				z2 = Math.max(to.getBlockZ(), from.getBlockZ());
				//do we need to do the same thing for Z? @todo
			}
			
			for(; x1 <= x2; x1++) {
				for(; z1 <= z2; z1++) {
					for(int y=0; y<255; y++) {
						Block b = to.getWorld().getBlockAt(x1, y, z1);
						if (BorderBlocks.isBorderBlock(b)) {
							plugin.getLogger().info("Border Block Found!");

							Location reject = from.clone();
							if (from != null) {
								double vx = -from.getX() + to.getX();
								double vy = -from.getY() + to.getY();
								double vz = -from.getZ() + to.getZ();
								
								vx = vx/Math.abs(vx);
								vy = vy/Math.abs(vy);
								vz = vz/Math.abs(vz);

								Vector v = new Vector(vx, vy, vz);
//								v.multiply(2);
			//					plugin.getLogger().info(""+v);


//								reject.subtract();
							}

							return Optional.of(reject);
						} 
					}
				}
			}

//		} else {
//			plugin.getLogger().info("Player changed worlds! From: "+from.getWorld().getName()+" To: "+to.getWorld().getName());
//		}
		return Optional.empty();
    }

}
