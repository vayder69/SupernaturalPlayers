package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashMap;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.ArrowUtil;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.util.Vector;

public class HunterManager {

	private HashMap<Arrow, String> arrowMap = new HashMap<Arrow, String>();
	private HashMap<SuperNPlayer, String> hunterMap = new HashMap<SuperNPlayer, String>();
	private ArrayList<Player> grapplingPlayers = new ArrayList<Player>();
	private ArrayList<Player> drainedPlayers = new ArrayList<Player>();
	private ArrayList<Location> hallDoors = new ArrayList<Location>();
	private static HashMap<Player, ArrayList<String>> hunterApps = new HashMap<Player, ArrayList<String>>();
	private ArrayList<SuperNPlayer> playerInvites = new ArrayList<SuperNPlayer>();
	
	// -------------------------------------------- //
	// 					Doors						//
	// -------------------------------------------- //
	
	private void addDoorLocation(Location location){
		if(!hallDoors.contains(location))
			hallDoors.add(location);
	}
	
	private void removeDoorLocation(Location location){
		hallDoors.remove(location);
	}
	
	public boolean doorIsOpening(Location location){
		if(hallDoors.contains(location))
			return true;
		return false;
	}
	
	public boolean doorEvent(Player player, Block block, Door door){
		if(door.isOpen())
			return true;
		
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log(player.getName()+" activated a WitchHunters' Hall.");
		
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		boolean open = false;
		
		final Location loc = block.getLocation();
		Location newLoc;
		Block newBlock;
		
		if(snplayer.isHuman())
			open = join(snplayer);
		
		if(snplayer.isHunter() || (snplayer.isHuman() && open)){
			if(door.isTopHalf()){
				newLoc = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()-1,loc.getBlockZ());
				newBlock = newLoc.getBlock();
				block.setTypeIdAndData(71, (byte)13, false);
				newBlock.setTypeIdAndData(71, (byte)5, false);
			}else{
				newLoc = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ());
				newBlock = newLoc.getBlock();
				block.setTypeIdAndData(71, (byte)5, false);
				newBlock.setTypeIdAndData(71, (byte)13, false);
			}
			
			addDoorLocation(loc);
			addDoorLocation(newLoc);

			SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
	            public void run() {
	            	closeDoor(loc);
	            }
	        }, 20);
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log("WitchHunter door is set open.");
			return true;
		}
		SupernaturalManager.sendMessage(snplayer, "WitchHunters Only!");
		return true;
	}
	
	private void closeDoor(Location loc){
		Block block = loc.getBlock();
		Door door = (Door) block.getState().getData();
		if(!door.isOpen())
			return;
		
		Location newLoc;
		Block newBlock;
		
		if(door.isTopHalf()){
			newLoc = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()-1,loc.getBlockZ());
			newBlock = newLoc.getBlock();
			block.setTypeIdAndData(71, (byte)9, false);
			newBlock.setTypeIdAndData(71, (byte)1, false);
		}else{
			newLoc = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ());
			newBlock = newLoc.getBlock();
			block.setTypeIdAndData(71, (byte)1, false);
			newBlock.setTypeIdAndData(71, (byte)9, false);
		}
		
		removeDoorLocation(loc);
		removeDoorLocation(newLoc);
	}
	
	
	// -------------------------------------------- //
	// 					Join Event					//
	// -------------------------------------------- //
	
	public ArrayList<String> getPlayerApp(Player player){
		return hunterApps.get(player);
	}
	
	public void addPlayerApp(Player player, ArrayList<String> kills){
		hunterApps.put(player, kills);
	}
	
	public boolean playerHasApp(Player player){
		if(hunterApps.containsKey(player))
			return true;
		return false;
	}
	
	public void removePlayerApp(Player player){
		if(hunterApps.containsKey(player))
			hunterApps.remove(player);
	}
	
	public void invite(final SuperNPlayer snplayer){
		SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
            public void run() {
            	SupernaturalManager.sendMessage(snplayer, "You have been invited to join the WitchHunter society!");
        		SupernaturalManager.sendMessage(snplayer, "If you wish to accept this invitation visit a WitchHunters' Hall");
        		if(!playerInvites.contains(snplayer))
        			playerInvites.add(snplayer);
            }
        }, 200);
	}
	
	public boolean join(SuperNPlayer snplayer){
		if(playerInvites.contains(snplayer)){
			SupernaturalManager.sendMessage(snplayer, "Welcome to the WitchHunter society!");
			SupernaturalManager.curse(snplayer, "witchhunter", SNConfigHandler.hunterPowerStart);
			return true;
		}
		return false;
	}
	
	// -------------------------------------------- //
	// 				Arrow Management				//
	// -------------------------------------------- //
	
	public boolean changeArrowType(SuperNPlayer snplayer){
		String currentType = hunterMap.get(snplayer);
		if(currentType == null){
			currentType = "normal";
		}
		
		String nextType = "normal";
		
		for(int i = 0; i < SNConfigHandler.hunterArrowTypes.size(); i++){
			if(SNConfigHandler.hunterArrowTypes.get(i).equalsIgnoreCase(currentType)){
				int newI = i + 1;
				if(newI >= SNConfigHandler.hunterArrowTypes.size())
					newI = 0;
				nextType = SNConfigHandler.hunterArrowTypes.get(newI);
				break;
			}
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
	
	// -------------------------------------------- //
	// 					Attacks						//
	// -------------------------------------------- //
	
	public boolean shoot(final Player player){
		final SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(drainedPlayers.contains(player)){
			player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.ARROW, 1));
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
				arrow.setFireTicks(SNConfigHandler.hunterFireArrowFireTicks);
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
