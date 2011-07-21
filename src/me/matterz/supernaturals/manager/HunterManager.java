package me.matterz.supernaturals.manager;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HunterManager {

	public void sneak(Player player){
		player.setSneaking(true);
	}
	
	public boolean changeArrowType(SuperNPlayer snplayer){
		String currentType = snplayer.getArrowType();
		String nextType;
		
		if(currentType.equalsIgnoreCase("normal")){
			nextType = "fire";
		}else if(currentType.equalsIgnoreCase("fire")){
			nextType = "triple";
		}else
			nextType = "normal";
		
		snplayer.setArrowType(nextType);
		SupernaturalManager.sendMessage(snplayer, "Changed to arrow type: " +ChatColor.WHITE+ nextType);
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log(snplayer.getName()+" changed to arrow type: "+nextType);
		return true;
	}
	
	public boolean shoot(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log(snplayer.getName()+"is firing "+snplayer.getArrowType()+" arrows.");
		
		if(snplayer.getArrowType().equalsIgnoreCase("fire")){
			Arrow arrow = player.shootArrow();
			arrow.setFireTicks(300);
			return true;
		}else if(snplayer.getArrowType().equalsIgnoreCase("triple")){
			Arrow arrow1 = player.shootArrow();
			Vector arrow1Vec = arrow1.getLocation().getDirection();
			double x = arrow1Vec.getX();
			double z = arrow1Vec.getZ();
			double newX = x*0.9;
			double newZ = z+(x*0.1);
			Vector newVector = new Vector(-newX, -arrow1Vec.getY(), -newZ);
			Arrow arrow2 = player.shootArrow();
			arrow2.setVelocity(newVector);
			newX = x*1.1;
			newZ = z-(x*0.1);
			newVector = new Vector(-newX, -arrow1Vec.getY(), -newZ);
			Arrow arrow3 = player.shootArrow();
			arrow3.setVelocity(newVector);
			return true;
		}else{
			return false;
		}
	}
}
