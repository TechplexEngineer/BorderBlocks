/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

/**
 *
 * @author techplex
 */
public class PluginState {
    //set to true to allow students to build in restricted areas not over build allow blocks.
    private boolean studentBuildingEnabled = false;
    
    //list of restricted areas
    private List<RestrictedArea> areas = new ArrayList<RestrictedArea>(){};
	
    public PluginState() {
	}
    
    /**
	 * Static variables are not cleared when bukkit reloads the plugin.
	 */
	public void cleanup() {
        areas.clear();
	}


    public boolean isStudentBuildingEnabled() {
        return studentBuildingEnabled;
    }

    public void setStudentBuildingEnabled(boolean enabled) {
        this.studentBuildingEnabled = enabled;
    }

   
    
    
}
