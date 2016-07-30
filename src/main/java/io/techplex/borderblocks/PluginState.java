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
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manage the plugin state and allow for serialization for storage
 * @author techplex
 */
public class PluginState {
	private final JavaPlugin plugin;
	
	//set to true to allow students to build in restricted areas not over build allow blocks.
	private boolean studentBuildingEnabled;
	private final ArrayList<OfflinePlayer> teachers;
	
	//Set to true when this class needs to be persisted.
	private boolean dirtybit;

	public PluginState(JavaPlugin plugin) {
		this.plugin = plugin;
		
		studentBuildingEnabled = plugin.getConfig().getBoolean("allowStudentBuilding");
 		teachers = new ArrayList<>(); //@todo this should come from the persisted data

		
		dirtybit = true;
	}

	public boolean isStudentBuildingEnabled() {
		dirtybit = true;
		return studentBuildingEnabled;
	}

	public void setStudentBuildingEnabled(boolean enabled) {
		this.studentBuildingEnabled = enabled;
	}
	
	
	
	public boolean isTeacher(Player p) {
		OfflinePlayer op = p;
		return teachers.contains(op);
	}

	public ArrayList<OfflinePlayer> getTeachers() {
		return teachers;
	}
	
	public void addTeacher(UUID id) {
		dirtybit = true;
		teachers.add(plugin.getServer().getOfflinePlayer(id));
	}

	public void removeTeacher(UUID id) {
		teachers.remove(plugin.getServer().getOfflinePlayer(id));
	}
}
