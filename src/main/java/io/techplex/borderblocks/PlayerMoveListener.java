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
	private PlayerPerms perms;

	public PlayerMoveListener(BorderBlocksPlugin plugin, PlayerPerms perms) {
		this.plugin = plugin;
		this.perms = perms;
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

			Optional<Location> overriding = perms.testMoveTo(player,from, to, MoveType.EMBARK, true);

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

			Optional<Location> overriding = perms.testMoveTo(player, event.getFrom(), event.getTo(), MoveType.RIDE, false);

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
		Optional<Location> overriding = perms.testMoveTo(player, event.getFrom(), event.getTo(), MoveType.MOVE, false);

		if (overriding.isPresent()) {
			Location override = overriding.get();

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
}
