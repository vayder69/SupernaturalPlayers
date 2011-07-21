package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashMap;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.ArrowUtil;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HunterManager {

	private HashMap<Arrow, String> arrowMap = new HashMap<Arrow, String>();
	private HashMap<SuperNPlayer, String> hunterMap = new HashMap<SuperNPlayer, String>();
	private ArrayList<Player> grapplingPlayers = new ArrayList<Player>();
	private ArrayList<Player> drainedPlayers = new ArrayList<Player>();
	
	public void sneak(Player player){
		player.setSneaking(true);
	}
	
	public boolean changeArrowType(SuperNPlayer snplayer){
		String currentType = hunterMap.get(snplayer);
		if(currentType == null){
			currentType = "normal";
		}
		String nextType;
		
		if(currentType.equalsIgnoreCase("normal")){
			nextType = "fire";
		}else if(currentType.equalsIgnoreCase("fire")){
			nextType = "triple";
		}else if(currentType.equalsIgnoreCase("triple")){
			nextType = "grapple";
		}else if(currentType.equalsIgnoreCase("grapple")){
			nextType = "power";
		}else{
			nextType = "normal";
		}

		hunterMap.put(snplayer, nextType);
		SupernaturalManager.sendMessage(snplayer, "Changed to arrow type: " +ChatColor.WHITE+ nextType);
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log(snplayer.getName()+" changed to arrow type: "+nextType);
		return true;
	}
	
	public String getArrowType(Arrow arrow){
		return arrowMap.get(arrow);
	}
	
	public HashMap<Arrow, String> getArrowMap(){
		return this.arrowMap;
	}
	
	public void removeArrow(final Arrow arrow){
		SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
            public void run() {
            	arrowMap.remove(arrow);
            }
        }, 20);
	}
	
	public boolean shoot(final Player player){
		final SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(drainedPlayers.contains(player)){
			SupernaturalManager.sendMessage(snplayer, "You are still recovering from Power Shot.");
			return true;
		}
		
		String arrowType = hunterMap.get(snplayer);
		if(arrowType == null){
			arrowType = "normal";
		}
		
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log(snplayer.getName()+" is firing "+arrowType+" arrows.");
		
		if(arrowType.equalsIgnoreCase("fire")){
			if(snplayer.getPower()>SNConfigHandler.hunterPowerArrowFire){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowFire, "Fire Arrow!");
				Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				arrow.setFireTicks(300);
				return true;
			}else{
				SupernaturalManager.sendMessage(snplayer, "Not enough power to shoot Fire Arrows!");
				SupernaturalManager.sendMessage(snplayer, "Switching to normal arrows.");
				hunterMap.put(snplayer, "normal");
				return false;
			}
		}else if(arrowType.equalsIgnoreCase("triple")){
			if(snplayer.getPower()>SNConfigHandler.hunterPowerArrowTriple){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowTriple, "Triple Arrow!");
				final Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
	                public void run() {
	                	splitArrow(player, arrow);
	                }
	            }, 4);
				return true;
			}else{
				SupernaturalManager.sendMessage(snplayer, "Not enough power to shoot Triple Arrows!");
				SupernaturalManager.sendMessage(snplayer, "Switching to normal arrows.");
				hunterMap.put(snplayer, "normal");
				return false;
			}
		}else if(arrowType.equalsIgnoreCase("power")){
			if(snplayer.getPower()>SNConfigHandler.hunterPowerArrowPower){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowPower, "Power Arrow!");
				Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				drainedPlayers.add(player);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName()+" is drained.");
				SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
	                public void run() {
	                	drainedPlayers.remove(player);
	                	if(player.isOnline())
	                		SupernaturalManager.sendMessage(snplayer, "You can shoot again!");
	                	SupernaturalsPlugin.log(snplayer.getName()+" is no longer drained.");
	                }
	            }, (SNConfigHandler.hunterCooldown/50));
				return true;
			}else{
				SupernaturalManager.sendMessage(snplayer, "Not enough power to shoot Power Arrows!");
				SupernaturalManager.sendMessage(snplayer, "Switching to normal arrows.");
				hunterMap.put(snplayer, "normal");
				return false;
			}
		}else if(arrowType.equalsIgnoreCase("grapple")){
			if(snplayer.getPower()>SNConfigHandler.hunterPowerArrowGrapple){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowGrapple, "Grapple Arrow!");
				Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				return true;
			}else{
				SupernaturalManager.sendMessage(snplayer, "Not enough power to shoot Grapple Arrow!");
				SupernaturalManager.sendMessage(snplayer, "Switching to normal arrows.");
				hunterMap.put(snplayer, "normal");
				return false;
			}
		}else{
			return false;
		}
	}
	
	public void splitArrow(final Player player, final Arrow arrow){
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log(player.getName()+"'s triple arrow event.");
		player.shootArrow();
		String arrowType = arrowMap.get(arrow);
		if(arrowType.equals("triple")){
			arrowMap.put(arrow, "double");
			SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
                public void run() {
                	splitArrow(player, arrow);
                }
            }, 4);
		}else{
			arrowMap.remove(arrow);
		}
	}
	
	 public boolean isGrappling(Player player) {
	       return grapplingPlayers.contains(player);
	   }
	    
    public void startGrappling(Player player, Location targetLocation) {
        if(isGrappling(player)) return;
        ArrowUtil gh = new ArrowUtil(player, targetLocation);
        grapplingPlayers.add(player);
        SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, gh);
    }
    
    public void stopGrappling(final Player player) {
        if(isGrappling(player)) {
            Vector v = new Vector(0, 0, 0);
            player.setVelocity(v);
            SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
                public void run() {
                    grapplingPlayers.remove(player);
                }
            }, 20*2);
        }
    }
}
