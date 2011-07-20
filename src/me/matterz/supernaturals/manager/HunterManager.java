package me.matterz.supernaturals.manager;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HunterManager {

	public void sneak(Player player){
		player.setSneaking(true);
	}
	
	public void changeArrowType(SuperNPlayer snplayer){
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
	}
}
