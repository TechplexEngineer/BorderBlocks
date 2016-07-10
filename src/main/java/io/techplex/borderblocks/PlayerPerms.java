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
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author techplex
 */
public class PlayerPerms {

	private PluginState state;
	private final BorderBlocksPlugin plugin;
	private final String teacherPerm = "borderblocks.teacher";

	public PlayerPerms(BorderBlocksPlugin plugin, PluginState state) {
		this.state = state;
		this.plugin = plugin;
	}

	public boolean canPlayerBuildHere(Player player, int x, int y, int z) {
		//remember in MC y is the altitude while x and z are the lat and lon
		if (player.hasPermission(teacherPerm)) {
			return true;
		}
		boolean allowBuild = state.isStudentBuildingEnabled(); //is student building enabled

		allowBuild = blocksAllowBuilding(allowBuild, player, x, y, z);

		return allowBuild;


	}
	public boolean canPlayerDigHere(Player player, int x, int y, int z) {

		if (player.hasPermission(teacherPerm)) {
			return true;
		}
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
	/**
	 * Test movement to the given location.
	 *
	 * <p>
	 * If a non-null {@link Location} is returned, the player should be at that
	 * location instead of where the player has tried to move to.</p>
	 *
	 * <p>
	 * If the {@code moveType} is cancellable ({@link MoveType#isCancellable()},
	 * then the last valid location will be set to the given one.</p>
	 *
	 * @param player The player
	 * @param to The new location
	 * @param from the previous location
	 * @param moveType The type of move
	 * @param forced Whether to force a check
	 * @return The overridden location, if the location is being overridden
	 */
	public Optional<Location> testMoveTo(Player player, Location from, Location to, MoveType moveType, boolean forced) {

		if (player.hasPermission(teacherPerm)) {
			return Optional.empty();
		}

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

		for (; x1 <= x2; x1++) {
			for (; z1 <= z2; z1++) {
				for (int y = 0; y < 255; y++) {
					Block b = to.getWorld().getBlockAt(x1, y, z1);
					if (BorderBlocks.isBorderBlock(b)) {
						plugin.getLogger().info("Border Block Found!");

						Location reject = from.clone();
						if (from != null) {
							double vx = -from.getX() + to.getX();
							double vy = -from.getY() + to.getY();
							double vz = -from.getZ() + to.getZ();

							vx = vx / Math.abs(vx);
							vy = vy / Math.abs(vy);
							vz = vz / Math.abs(vz);

							Vector v = new Vector(vx, vy, vz);
//							v.multiply(2);
//							plugin.getLogger().info(""+v);

//							reject.subtract();
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
