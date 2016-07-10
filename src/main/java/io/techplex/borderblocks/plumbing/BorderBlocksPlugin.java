/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks.plumbing;

import io.techplex.borderblocks.PlayerPerms;
import io.techplex.borderblocks.PluginState;
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
		PlayerPerms perms = new PlayerPerms(state);
        
		pm.registerEvents(new BlockPlayerListener(perms), this);
		
		
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    }
}
