package me.matterz.supernaturals.light;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LightMapManager {
	
	private static List<LightMap> lightmaps = new ArrayList<LightMap>();
	
	public List<LightMap> getLightMaps(){
		return lightmaps;
	}	
	
	public void addLightMap(Player player, Location location, int intensity){
		for(LightMap lightmap : lightmaps){
			if(lightmap.getPlayer().equals(player) && lightmap.getLocation().equals(location)){
				lightmap.setIntensity(intensity);
				return;
			}
		}
		
		LightMap newlightmap = new LightMap(player, location, intensity);
		lightmaps.add(newlightmap);
	}
	
	public LightMap getLightMap(Player player, Location location){
		for(LightMap lightmap : lightmaps){
			if(lightmap.getPlayer().equals(player) && lightmap.getLocation().equals(location)){
				return lightmap;
			}
		}
		return null;
	}
	
	public void removeLightMap(Player player, Location location){
		LightMap lightMap = this.getLightMap(player, location);
		lightmaps.remove(lightMap);		
	}
	
	public void removeLightMap(LightMap lightmap){
		lightmaps.remove(lightmap);
	}

}
