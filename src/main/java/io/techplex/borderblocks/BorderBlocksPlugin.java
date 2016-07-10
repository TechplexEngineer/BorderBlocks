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

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author techplex
 */
public class BorderBlocksPlugin extends JavaPlugin {
	private Logger log;
	public FileConfiguration config;

	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		log = getLogger();
		log.info("Starting BorderBlocks Plugin");

		PluginManager pm = Bukkit.getPluginManager();

		PluginState state = new PluginState();
		PlayerPerms perms = new PlayerPerms(this, state);

		pm.registerEvents(new BlockPlayerListener(perms), this);
		pm.registerEvents(new PlayerMoveListener(this, perms), this);


	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {
	}
}
