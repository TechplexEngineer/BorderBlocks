/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author techplex
 */
public class Main extends JavaPlugin {
    private Logger log;
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
        log = getLogger();
        log.info("Starting BorderBlocks Plugin");
        PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
        Location loc1 = new Location(getServer().getWorld("world"), -8, 62, -65);
        Location loc2 = new Location(getServer().getWorld("world"), -4, 62, -69);
        State.getInstance().addRestrictedArea(loc1, loc2);
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
        State.getInstance().cleanup();
    }
}
