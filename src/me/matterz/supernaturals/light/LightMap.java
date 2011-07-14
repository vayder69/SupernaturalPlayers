package me.matterz.supernaturals.light;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LightMap {
	
	private Player player;
	private Location location;
	private int intensity;
	
	public LightMap(Player player, Location location, Integer intensity){
		this.player = player;
		this.intensity = intensity;
		this.location = location;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public Location getLocation(){
		return this.location;
	}
	
	public int getIntensity(){
		return this.intensity;
	}
	
	public void setIntensity(int intensity){
		this.intensity=intensity;
	}

}
