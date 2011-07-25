package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class DemonManager {
	
	private HashMap<Block, Location> webMap = new HashMap<Block, Location>();
	private ArrayList<Player> demonApps = new ArrayList<Player>();
	
	// -------------------------------------------- //
	// 				Demon Applications				//
	// -------------------------------------------- //
	
	public void checkInventory(Player player){
		PlayerInventory inv = player.getInventory();
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log("Player teleported to Nether.  Checking inventory...");
		ItemStack helmet = inv.getHelmet();
		ItemStack chestplate = inv.getChestplate();
		ItemStack leggings = inv.getLeggings();
		ItemStack boots = inv.getBoots();
		if(helmet.getType().equals(Material.LEATHER_HELMET)){
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log("Leather Helm");
			if(chestplate.getType().equals(Material.LEATHER_CHESTPLATE)){
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log("Leather Chest");
				if(leggings.getType().equals(Material.LEATHER_LEGGINGS)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log("Leather Legs");
					if(boots.getType().equals(Material.LEATHER_BOOTS)){
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log("Leather Boots");
						demonApps.add(player);
						return;
					}
				}
			}
		}
	}
	
	public boolean checkPlayerApp(Player player){
		if(demonApps.contains(player)){
			demonApps.remove(player);
			return true;
		}
		return false;
	}
	
	// -------------------------------------------- //
	// 					WebMap						//
	// -------------------------------------------- //
	
	public HashMap<Block, Location> getWebs(){
		return this.webMap;
	}
	
	public void removeWeb(Block block){
		webMap.remove(block);
	}
	
	// -------------------------------------------- //
	// 					Power Loss					//
	// -------------------------------------------- //
	
	public void powerAdvanceTime(Player player, int seconds){
		if(!player.getWorld().getEnvironment().equals(Environment.NETHER)){
			SuperNPlayer snplayer = SupernaturalManager.get(player);
			SupernaturalManager.alterPower(snplayer, -(SNConfigHandler.demonPowerLoss*seconds));
		}
	}
	
	// -------------------------------------------- //
	// 						Healing					//
	// -------------------------------------------- //
	
	public void heal(Player player){
		if(player.isDead() || player.getHealth() == 20)
			return;
		
		int health = player.getHealth();
		health += SNConfigHandler.demonHealing;
		if(health>20)
			health=20;
		player.setHealth(health);
	}
	
	// -------------------------------------------- //
	// 					Spells						//
	// -------------------------------------------- //
	
	public boolean fireball(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(!SupernaturalsPlugin.instance.getPvP(player)){
			SupernaturalManager.sendMessage(snplayer, "You cannot shoot fireballs in non-PvP areas");
			return false;
		}
		if(snplayer.getPower() < SNConfigHandler.demonPowerFireball){
			SupernaturalManager.sendMessage(snplayer, "Not enough power to cast fireball!");
			return false;
		}
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(player.getWorld(), 
				player.getLocation().getYaw(), player.getLocation().getPitch());
		Fireball fireball = player.getWorld().spawn(loc, Fireball.class);
		fireball.setShooter(player);
		fireball.setYield(0);
		SupernaturalManager.alterPower(SupernaturalManager.get(player), -SNConfigHandler.demonPowerFireball, "Fireball!");
		ItemStack item = player.getItemInHand();
		if(item.getAmount()==1){
			player.setItemInHand(null);
		}else{
			item.setAmount(player.getItemInHand().getAmount()-1);
		}
		return true;
	}
	
	public boolean snare(Player player, Player target){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(snplayer.getPower() < SNConfigHandler.demonPowerSnare){
			SupernaturalManager.sendMessage(snplayer, "Not enough power to cast snare!");
			return false;
		}
		Block block;
		
		if(target == null){
			block = player.getTargetBlock(null, 20);
		}else{
			block = target.getLocation().getBlock();
		}
		
		final Location loc = block.getLocation();

		for(int x = loc.getBlockX()-1; x < loc.getBlockX()+2; x++){
			for(int y = loc.getBlockY()-1; y < loc.getBlockY()+2; y++){
				for(int z = loc.getBlockZ()-1; z < loc.getBlockZ()+2; z++){
					Location newLoc = new Location(block.getWorld(), x, y, z);
					Block newBlock = newLoc.getBlock();
					if(newBlock.getTypeId()==0){
						newBlock.setType(Material.WEB);
						webMap.put(newBlock, loc);
					}
				}
			}
		}
		
		SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
            public void run() {
            	List<Block> blocks = new ArrayList<Block>();
            	for(Block block : webMap.keySet()){
            		if(webMap.get(block).equals(loc)){
            			block.setType(Material.AIR);
            			blocks.add(block);            			
            		}
            	}
            	for(Block block : blocks){
            		webMap.remove(block);
            		if(SNConfigHandler.debugMode)
    					SupernaturalsPlugin.log("Removed web block.");
            	}
            }
        }, (SNConfigHandler.demonSnareDuration/50));
		
		ItemStack item = player.getItemInHand();
		if(item.getAmount()==1){
			player.setItemInHand(null);
		}else{
			item.setAmount(player.getItemInHand().getAmount()-1);
		}
		
		SupernaturalManager.alterPower(SupernaturalManager.get(player), -SNConfigHandler.demonPowerSnare, "Snare!");
		return true;
	}

}
