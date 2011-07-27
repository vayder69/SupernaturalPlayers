/*
 * Supernatural Players Plugin for Bukkit
 * Copyright (C) 2011  Matt Walker <mmw167@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SuperNManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.material.Door;

public class SNPlayerListener extends PlayerListener{

	public static SupernaturalsPlugin plugin;
	private String permissions = "supernatural.player.shrineuse";
	private String permissions2 = "supernatural.player.wolfbane";
	private String worldPermission = "supernatural.world.disabled";
	
	public SNPlayerListener(SupernaturalsPlugin instance){
		SNPlayerListener.plugin = instance;
	}
	
//	@Override
//	public void onPlayerToggleSneak(PlayerToggleSneakEvent event){
//		Player player = event.getPlayer();
//		SuperNPlayer snplayer = SupernaturalManager.get(player);
//		if(snplayer.isHunter()){
//			player.setSneaking(true);
//			event.setCancelled(true);
//		}
//	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		Action action = event.getAction();		
		
		if(!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_AIR)) && event.isCancelled()){
			return;
		}
		
		Player player = event.getPlayer();
		
		if(SupernaturalsPlugin.hasPermissions(player, worldPermission) && SNConfigHandler.multiworld)
			return;
		
		SuperNPlayer snplayer = SuperNManager.get(player);
		boolean cancelled = false;
		Material itemMaterial = event.getMaterial();
		
		Location blockLoc;
		
		Block block = event.getClickedBlock();
		if(action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK)){
			try{
				blockLoc = block.getLocation();
			}catch(NullPointerException e){
				SupernaturalsPlugin.log("Door trying to close.");
				event.setCancelled(true);
				return;
			}
				
			if(block.getType().equals(Material.IRON_DOOR_BLOCK)){
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName()+" activated an Iron Door.");
				for(int x = blockLoc.getBlockX()-2; x < blockLoc.getBlockX()+3; x++){
					for(int y = blockLoc.getBlockY()-2; y < blockLoc.getBlockY()+3; y++){
						for(int z = blockLoc.getBlockZ()-2; z < blockLoc.getBlockZ()+3; z++){
							Location newLoc = new Location(block.getWorld(), x, y, z);
							Block newBlock = newLoc.getBlock();
							if(newBlock.getType().equals(Material.SIGN) || newBlock.getType().equals(Material.WALL_SIGN)){
								if(SNConfigHandler.debugMode)
									SupernaturalsPlugin.log(snplayer.getName()+" found a sign.");
								Sign sign = (Sign) newBlock.getState();
								String[] text = sign.getLines();
								for(int i = 0; i < text.length; i++){
									if(SNConfigHandler.debugMode)
										SupernaturalsPlugin.log("The sign says: "+text[i]);
									if(text[i].contains(SNConfigHandler.hunterHallMessage)){
										if(plugin.getHunterManager().doorIsOpening(blockLoc)){
											if(SNConfigHandler.debugMode)
												SupernaturalsPlugin.log("Cancelled door event.");
											event.setCancelled(true);
											return;
										}
										Door door = (Door) block.getState().getData();
										boolean open = plugin.getHunterManager().doorEvent(player, block, door);
										event.setCancelled(open);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){
			if(player.getItemInHand()==null){
				return;
			}
			
			if(snplayer.isVampire()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
					SuperNManager.jump(player, SNConfigHandler.jumpDeltaSpeed, true);
					event.setCancelled(true);
					return;
				}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.vampireMaterial)){
					plugin.getVampireManager().teleport(player);
					event.setCancelled(true);
					return;
				}
			}else if(snplayer.isWere()){
					if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.wolfMaterial)){
						if(SuperNManager.worldTimeIsNight(player)){
							plugin.getWereManager().summon(player);
							event.setCancelled(true);
							return;
						}else{
							SuperNManager.sendMessage(snplayer, "Cannot use this ability during the day.");
							return;
						}
					}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.wolfbaneMaterial)){
						if(!SupernaturalsPlugin.hasPermissions(player, permissions2)){
							return;
						}
						if(SuperNManager.worldTimeIsNight(player)){
							SuperNManager.sendMessage(snplayer, "Cannot cure lycanthropy during the night.");
							return;
						}else{
							plugin.getWereManager().wolfbane(player);
							event.setCancelled(true);
							return;
						}
					}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.dashMaterial)){
						if(SuperNManager.worldTimeIsNight(player)){
							SuperNManager.jump(event.getPlayer(), SNConfigHandler.dashDeltaSpeed, false);
							event.setCancelled(true);
							return;
						}else{
							SuperNManager.sendMessage(snplayer, "Cannot use this ability during the day.");
							return;
						}
					}
			}else if(snplayer.isGhoul()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.ghoulMaterial)){
					plugin.getGhoulManager().summon(player);
					event.setCancelled(true);
					return;
				}
			}else if(snplayer.isPriest()){
				if(SNConfigHandler.priestSpellMaterials.contains(itemMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " is attempting to cast a spell...");
					Player victim = plugin.getSuperManager().getTarget(player);
					if(victim == null)
						return;
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(victim.getName()+" is targetted by spell.");
					if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(0))){
						plugin.getPriestManager().banish(player, victim);
						cancelled = false;
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(1))){
						plugin.getPriestManager().exorcise(player, victim);
						cancelled = false;
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(2))){
						cancelled = plugin.getPriestManager().cure(player, victim, itemMaterial);
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(3))){
						cancelled = plugin.getPriestManager().heal(player, victim);
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(4))){
						plugin.getPriestManager().drainPower(player, victim);
						cancelled = false;
					}
					if(!event.isCancelled())
						event.setCancelled(cancelled);
					return;
				}
			}else if(snplayer.isDemon()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.demonMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(player.getName()+" is casting FIREBALL with "+itemMaterial.toString());
					cancelled = plugin.getDemonManager().fireball(player);
					if(!event.isCancelled() && cancelled)
						event.setCancelled(true);
					return;
				}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.demonSnareMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(player.getName()+" is casting SNARE with "+itemMaterial.toString());
					Player target = plugin.getSuperManager().getTarget(player);
					cancelled = plugin.getDemonManager().snare(player, target);
					if(!event.isCancelled() && cancelled)
						event.setCancelled(true);
					return;
				}
			}else if(snplayer.isHunter()){
				if(player.getItemInHand().getType().equals(Material.BOW)){
					plugin.getHunterManager().changeArrowType(snplayer);
					return;
				}
			}
		}
		
		if(!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))){
			return;
		}
		
		if(snplayer.isHunter()){
			if(player.getItemInHand().getType().equals(Material.BOW)){
				if(player.getInventory().contains(Material.ARROW)){
					cancelled = plugin.getHunterManager().shoot(player);
					if(cancelled){
						event.setUseInteractedBlock(Event.Result.DENY);
						event.setCancelled(true);
					}
					return;
				}else{
					return;
				}
			}
		}
		
		if(action.equals(Action.RIGHT_CLICK_AIR)){
			if(SNConfigHandler.foodMaterials.contains(itemMaterial)){
				if(snplayer.isVampire())
				{
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " attempted to eat " + itemMaterial.toString());
					SuperNManager.sendMessage(snplayer, "Vampires can't eat food. You must drink blood instead.");
					event.setCancelled(true);
					return;
				}else if(snplayer.isWere()){
					if(itemMaterial.equals(Material.BREAD)){
						SuperNManager.sendMessage(snplayer, "Werewolves do not gain power from Bread.");
						return;
					}else{
						SuperNManager.alterPower(snplayer, SNConfigHandler.werePowerFood, "Eating!");
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log(snplayer.getName() + " ate " + itemMaterial.toString() + " to gain " + SNConfigHandler.werePowerFood + " power!");
						return;
					}
				}
				return;
			}
			return;
		}else if(!(action.equals(Action.RIGHT_CLICK_BLOCK))){
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		if(!SupernaturalsPlugin.hasPermissions(player, permissions)){
			return;
		}
		
		if(blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Vampire Infect Altar.");
			plugin.getVampireManager().useAltarInfect(player, event.getClickedBlock());
		}else if(blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarCureMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Vampire Cure Altar.");
			plugin.getVampireManager().useAltarCure(player, event.getClickedBlock());
		}else if(blockMaterial == Material.getMaterial(SNConfigHandler.priestAltarMaterial)){
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Priest Altar.");
			plugin.getPriestManager().useAltar(player);
		}
	}
	
	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		if(event.isCancelled()){
			return;
		}
		
		if(SupernaturalsPlugin.hasPermissions(event.getPlayer(), worldPermission) && SNConfigHandler.multiworld)
			return;
		
		if ((event.getLeaveMessage().contains("Flying")) || (event.getReason().contains("Flying"))) {
			SuperNPlayer snplayer = SuperNManager.get(event.getPlayer());
			if(snplayer.isVampire()&& event.getPlayer().getItemInHand().getType().toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
				event.setCancelled(true);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(event.getPlayer().getName() + " was not kicked for flying as a vampire.");
			} 
		}
	}
}
