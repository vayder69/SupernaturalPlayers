package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

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
	private static ArrayList<SuperNPlayer> bountyList = new ArrayList<SuperNPlayer>();
	
	// -------------------------------------------- //
	// 					Bounties					//
	// -------------------------------------------- //
	
	public static ArrayList<SuperNPlayer> getBountyList(){
		return bountyList;
	}
	
	public static boolean checkBounty(SuperNPlayer snplayer){
		if(bountyList.contains(snplayer))
			return true;
		return false;
	}
	
	public static boolean removeBounty(SuperNPlayer snplayer){
		if(bountyList.contains(snplayer)){
			bountyList.remove(snplayer);
			return true;
		}
		return false;
	}
	
	public static void addBounty(){
		List<SuperNPlayer> targets = SuperNManager.getSupernaturals();
		boolean bountyFound = false;
		Random generator = new Random();
		int count = 0;
		
		while(!bountyFound){
			int randomIndex = generator.nextInt(targets.size());
			SuperNPlayer sntarget = targets.get(randomIndex);
			
			if(!bountyList.contains(sntarget) && sntarget.isSuper()){
				bountyList.add(sntarget);
				SupernaturalsPlugin.instance.getServer().broadcastMessage(ChatColor.WHITE+sntarget.getName()+ChatColor.RED+" has been added to the WitchHunter target list!");
				bountyFound = true;
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log("Bounty created on "+sntarget.getName());
				return;
			}
			count++;
			if(count>50)
				return;
		}
	}
	
	public static void updateBounties(){
		List<SuperNPlayer> snplayers = new ArrayList<SuperNPlayer>();
		
		if(bountyList.isEmpty())
			return;
		
		for(SuperNPlayer snplayer : bountyList){
			if(!snplayer.isSuper()){
				snplayers.add(snplayer);
			}
		}
		
		for(SuperNPlayer snplayer : snplayers){
			removeBounty(snplayer);
			addBounty();
		}
	}
	
	public static void createBounties(){
		List<SuperNPlayer> targets = SuperNManager.getSupernaturals();
		if(targets.size()==0){
			SupernaturalsPlugin.log(Level.WARNING, "No targets found for WitchHunters!");
			return;
		}
			
		int numberFound = 0;
		Random generator = new Random();
		int count = 0;
		
		while(numberFound < SNConfigHandler.hunterMaxBounties){
			int randomIndex = generator.nextInt(targets.size());
			SuperNPlayer sntarget = targets.get(randomIndex);
			if(!bountyList.contains(sntarget) && sntarget.isSuper()){
				bountyList.add(sntarget);
				numberFound++;
				SupernaturalsPlugin.instance.getServer().broadcastMessage(ChatColor.WHITE+sntarget.getName()+ChatColor.RED+" has been added to the WitchHunter target list!");
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log("Bounty created on "+sntarget.getName());
			}
			count++;
			if(count>100)
				return;
		}
	}
	
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
		
		SuperNPlayer snplayer = SuperNManager.get(player);
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
				block.setTypeIdAndData(71, (byte)(block.getData()+4), false);
				newBlock.setTypeIdAndData(71,(byte)(newBlock.getData()+4), false);
			}else{
				newLoc = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ());
				newBlock = newLoc.getBlock();
				block.setTypeIdAndData(71, (byte)(block.getData()+4), false);
				newBlock.setTypeIdAndData(71, (byte)(newBlock.getData()+4), false);
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
		SuperNManager.sendMessage(snplayer, "WitchHunters Only!");
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
			block.setTypeIdAndData(71, (byte)(block.getData()-4), false);
			newBlock.setTypeIdAndData(71, (byte)(newBlock.getData()-4), false);
		}else{
			newLoc = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ());
			newBlock = newLoc.getBlock();
			block.setTypeIdAndData(71, (byte)(block.getData()-4), false);
			newBlock.setTypeIdAndData(71, (byte)(newBlock.getData()-4), false);
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
            	SuperNManager.sendMessage(snplayer, "You have been invited to join the WitchHunter society!");
        		SuperNManager.sendMessage(snplayer, "If you wish to accept this invitation visit a WitchHunters' Hall");
        		if(!playerInvites.contains(snplayer))
        			playerInvites.add(snplayer);
            }
        }, 200);
	}
	
	public boolean join(SuperNPlayer snplayer){
		if(playerInvites.contains(snplayer)){
			SuperNManager.sendMessage(snplayer, "Welcome to the WitchHunter society!");
			SuperNManager.curse(snplayer, "witchhunter", SNConfigHandler.hunterPowerStart);
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
		SuperNManager.sendMessage(snplayer, "Changed to arrow type: " +ChatColor.WHITE+ nextType);
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
		
		final SuperNPlayer snplayer = SuperNManager.get(player);
		
		if(!SupernaturalsPlugin.instance.getPvP(player)){
			SuperNManager.sendMessage(snplayer, "You cannot use special arrows in non-PvP areas.");
			return false;
		}
		
		if(drainedPlayers.contains(player)){
			player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.ARROW, 1));
			SuperNManager.sendMessage(snplayer, "You are still recovering from Power Shot.");
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
				SuperNManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowFire, "Fire Arrow!");
				Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				arrow.setFireTicks(SNConfigHandler.hunterFireArrowFireTicks);
				return true;
			}else{
				SuperNManager.sendMessage(snplayer, "Not enough power to shoot Fire Arrows!");
				SuperNManager.sendMessage(snplayer, "Switching to normal arrows.");
				hunterMap.put(snplayer, "normal");
				return false;
			}
		}else if(arrowType.equalsIgnoreCase("triple")){
			if(snplayer.getPower()>SNConfigHandler.hunterPowerArrowTriple){
				SuperNManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowTriple, "Triple Arrow!");
				final Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
	                public void run() {
	                	splitArrow(player, arrow);
	                }
	            }, 4);
				return true;
			}else{
				SuperNManager.sendMessage(snplayer, "Not enough power to shoot Triple Arrows!");
				SuperNManager.sendMessage(snplayer, "Switching to normal arrows.");
				hunterMap.put(snplayer, "normal");
				return false;
			}
		}else if(arrowType.equalsIgnoreCase("power")){
			if(snplayer.getPower()>SNConfigHandler.hunterPowerArrowPower){
				SuperNManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowPower, "Power Arrow!");
				Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				drainedPlayers.add(player);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName()+" is drained.");
				SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
	                public void run() {
	                	drainedPlayers.remove(player);
	                	if(player.isOnline())
	                		SuperNManager.sendMessage(snplayer, "You can shoot again!");
	                	SupernaturalsPlugin.log(snplayer.getName()+" is no longer drained.");
	                }
	            }, (SNConfigHandler.hunterCooldown/50));
				return true;
			}else{
				SuperNManager.sendMessage(snplayer, "Not enough power to shoot Power Arrows!");
				SuperNManager.sendMessage(snplayer, "Switching to normal arrows.");
				hunterMap.put(snplayer, "normal");
				return false;
			}
		}else if(arrowType.equalsIgnoreCase("grapple")){
			if(snplayer.getPower()>SNConfigHandler.hunterPowerArrowGrapple){
				SuperNManager.alterPower(snplayer, -SNConfigHandler.hunterPowerArrowGrapple, "Grapple Arrow!");
				Arrow arrow = player.shootArrow();
				arrowMap.put(arrow, arrowType);
				return true;
			}else{
				SuperNManager.sendMessage(snplayer, "Not enough power to shoot Grapple Arrow!");
				SuperNManager.sendMessage(snplayer, "Switching to normal arrows.");
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
