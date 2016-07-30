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

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
		BorderBlocksPlugin plugin = this;

		PluginManager pm = Bukkit.getPluginManager();

		PluginState state = new PluginState(this);
		PlayerPerms perms = new PlayerPerms(this, state);

		pm.registerEvents(new BlockPlayerListener(perms), this);
		pm.registerEvents(new PlayerMoveListener(this, perms), this);
		
		this.getCommand("allowStudentBuilding").setExecutor(new CommandExecutor() {
            
			//@return true if a valid command, otherwise false
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (args.length == 1) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						if ( ! state.isTeacher(p)) {
							sender.sendMessage("Sorry you must be a teacher to run this command.");
							return false;
						}
					}
					boolean tval = args[0].equalsIgnoreCase("true");
					boolean fval = args[0].equalsIgnoreCase("false");
					if (tval || fval) {
						String msg = "Setting allowStudentBuilding to %s";
						sender.sendMessage(String.format(msg, tval));
						return true;
					} else {
						sender.sendMessage("Invalid command.");
						return false;
					}
				}
                String msg = "Student building is ";
				
				if (state.isStudentBuildingEnabled()) {
					msg += "enabled";
				} else {
					msg += "disabled";
				}
				
                sender.sendMessage(msg);
                
                return true;
            }
        });
		
		this.getCommand("teacher").setExecutor(new CommandExecutor() {
            
			//@return true if a valid command, otherwise false
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (!state.isTeacher(p)) {
						sender.sendMessage("Sorry you must be a teacher to run this command.");
						return false;
					}
				}
				if (args.length == 1) {
					
					if (args[0].equalsIgnoreCase("list")) {
						ArrayList<OfflinePlayer> teachers = state.getTeachers();
						sender.sendMessage(String.format("There are currently %d teachers.", teachers.size()));
						for (OfflinePlayer teacher : teachers) {
							sender.sendMessage(teacher.getName());
						}
						return true;
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("add")) {
						Player p = plugin.getServer().getPlayer(args[1]);
						if (p == null) {
							sender.sendMessage(String.format("Player '%s' not found.", args[1]));
							return false;
						}
						state.addTeacher(p.getUniqueId());
						sender.sendMessage(String.format("Player '%s' is now a Teacher.", args[1]));
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("remove")) {
						Player p = plugin.getServer().getPlayer(args[1]);
						if (p == null) {
							sender.sendMessage(String.format("Player '%s' not found.", args[1]));
							return false;
						}
						state.removeTeacher(p.getUniqueId());
						sender.sendMessage(String.format("Player '%s' is no longer a Teacher.", args[1]));
					}
				}
				return false;
            }
        });


	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {
	}
}
