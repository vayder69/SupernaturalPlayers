package me.matterz.supernaturals.listeners;

import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

public class SNPlayerListener extends PlayerListener{

	public static SupernaturalsPlugin plugin;
	
	public SNPlayerListener(SupernaturalsPlugin instance){
		SNPlayerListener.plugin = instance;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.isCancelled()){
			return;
		}
		
		Action action = event.getAction();
		
		if(!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)){
			return;
		}
		
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		Material itemMaterial = event.getMaterial();
		
		if(SNConfigHandler.foodMaterials.contains(itemMaterial)){
			if(snplayer.isVampire())
			{
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName() + " attempted to eat " + itemMaterial.toString());
				SupernaturalManager.sendMessage(snplayer, "Vampires can't eat food. You must drink blood instead.");
				event.setCancelled(true);
				return;
			}else if(snplayer.isWere()){
				SupernaturalManager.alterPower(snplayer, SNConfigHandler.werePowerFood, "Eating!");
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName() + " ate " + itemMaterial.toString() + " to gain " + SNConfigHandler.werePowerFood + " power!");
				return;
			}
		}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
			if(snplayer.isVampire()){
				plugin.getVampireManager().jump(event.getPlayer(), SNConfigHandler.dashDeltaSpeed, false);
				event.setCancelled(true);
				return;
			}
		}
		
		if (action != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		if(blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggerd a Vampire Infect Altar.");
			plugin.getVampireManager().useAltarInfect(player, event.getClickedBlock());
		}else if (blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarCureMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggerd a Vampire Cure Altar.");
			plugin.getVampireManager().useAltarCure(player, event.getClickedBlock());
		}
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event){
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		ItemStack item = player.getItemInHand();
		if(event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)){
			if(snplayer.isVampire()){
				if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
					plugin.getVampireManager().jump(player, SNConfigHandler.jumpDeltaSpeed, true);
				}
			}else if(snplayer.isWere()){
				if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.wolfMaterial)){
					plugin.getWereManager().summon(player);
					if(item.getAmount()==1){
						player.setItemInHand(null);
					}else{
						item.setAmount(player.getItemInHand().getAmount()-1);
					}
				}
			}else if(snplayer.isGhoul()){
				if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.ghoulMaterial)){
					plugin.getGhoulManager().summon(player);
					if(item.getAmount()==1){
						player.setItemInHand(null);
					}else{
						item.setAmount(player.getItemInHand().getAmount()-1);
					}
				}
			}else if(snplayer.isPriest()){
				if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.priestMaterial)){
					List<Block> blocks = player.getLineOfSight(null, 20);
					List<Entity> entities = player.getNearbyEntities(21, 21, 21);
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " is attempting to use banish...");
					for(Block block : blocks){
						for(Entity entity : entities){
							if(entity instanceof Player){
								Player victim = (Player) entity;
								Location location = victim.getLocation();
								Location feetLocation = new Location(location.getWorld(), location.getX(), location.getY()-1, location.getZ());
								Location groundLocation = new Location(location.getWorld(), location.getX(), location.getY()-2, location.getZ());
								if(location.getBlock().equals(block) || feetLocation.getBlock().equals(block) || groundLocation.getBlock().equals(block)){
									if(SNConfigHandler.debugMode)
										SupernaturalsPlugin.log(victim.getName()+" is targetted by banish.");
									plugin.getPriestManager().banish(player, victim);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		if(event.isCancelled()){
			return;
		}
		if ((event.getLeaveMessage().contains("Flying")) || (event.getReason().contains("Flying"))) {
			SuperNPlayer snplayer = SupernaturalManager.get(event.getPlayer());
			if(snplayer.isVampire()&& event.getPlayer().getItemInHand().getType().toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
				event.setCancelled(true);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(event.getPlayer().getName() + " was not kicked for flying as a vampire.");
			} 
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event){	    
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		if(snplayer.isHuman()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.WHITE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.WHITE + "Human " + event.getPlayer().getName() + ChatColor.GOLD + " has joined the server.");
		}else if(snplayer.isVampire()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.DARK_PURPLE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "Vampire " + event.getPlayer().getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isWere()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.BLUE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.BLUE + "Werewolf " + event.getPlayer().getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isGhoul()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.DARK_RED+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "Ghoul " + event.getPlayer().getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isPriest()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.GOLD+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.GOLD + "Priest " + event.getPlayer().getName() + ChatColor.GOLD + " has joined the server.");
		}
		
	}
}
