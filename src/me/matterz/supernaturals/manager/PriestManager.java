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

package me.matterz.supernaturals.manager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class PriestManager {
	
	// -------------------------------------------- //
	// 					Church						//
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public void useAltar(Player player){
		Location location = player.getLocation();
		World world = location.getWorld();
		int locX = location.getBlockX();
		int locY = location.getBlockY();
		int locZ = location.getBlockZ();
		SuperNPlayer snplayer = SuperNManager.get(player);
		int amount = 0;
		int delta = 0;
		if(world.getName().equalsIgnoreCase(SNConfigHandler.priestChurchWorld)){
			if(Math.abs(locX-SNConfigHandler.priestChurchLocationX) <= 10){
				if(Math.abs(locY-SNConfigHandler.priestChurchLocationY) <= 10){
					if(Math.abs(locZ-SNConfigHandler.priestChurchLocationZ) <= 10){
						if(snplayer.isPriest()){
							if(player.getItemInHand().getType().equals(Material.COAL)){
								SuperNManager.sendMessage(snplayer, "The Church excommunicates you!");
								SuperNManager.cure(snplayer);
							}else{
								PlayerInventory inv = player.getInventory();
								ItemStack[] items = inv.getContents();
								for(Material mat : SNConfigHandler.priestDonationMap.keySet()){
									for(ItemStack itemStack : items){
										if(itemStack!=null){
											if(itemStack.getType().equals(mat)){
												amount += itemStack.getAmount();
											}
										}
									}
									delta += (amount * SNConfigHandler.priestDonationMap.get(mat));
									amount = 0;
								}
								for(Material mat: SNConfigHandler.priestDonationMap.keySet()){
									inv.remove(mat);
								}
								player.updateInventory();
								SuperNManager.sendMessage(snplayer, "The Church accepts your gracious donations of Bread, Fish, Grilled Pork and Apples.");
								SuperNManager.alterPower(snplayer, delta, "Donations!");
							}
						}else{
							SuperNManager.sendMessage(snplayer, "The Church Altar radiates holy power.");
							if(snplayer.isSuper()) {
								SuperNManager.sendMessage(snplayer, "The holy power of the Church tears you asunder!");
								EntityDamageEvent event = new EntityDamageEvent(player, DamageCause.BLOCK_EXPLOSION, 20);
								player.setLastDamageCause(event);
								player.setHealth(0);
								if(snplayer.isGhoul()){
								double random = Math.random();
									if(random<(SNConfigHandler.spreadChance-0.1)){
										SuperNManager.cure(snplayer);
									}
								}
								return;
							}
							if(SNConfigHandler.priestAltarRecipe.playerHasEnough(player)) {
								SuperNManager.sendMessage(snplayer, "You donate these items to the Church:");
								SuperNManager.sendMessage(snplayer, SNConfigHandler.priestAltarRecipe.getRecipeLine());
								SuperNManager.sendMessage(snplayer, "The Church recognizes your holy spirit and accepts you into the priesthood.");
								SNConfigHandler.priestAltarRecipe.removeFromPlayer(player);
								SuperNManager.curse(snplayer, "priest", SNConfigHandler.priestPowerStart);
							}else{
								SuperNManager.sendMessage(snplayer, "The Church judges your intended donate insufficient.  You must gather the following: ");
								SuperNManager.sendMessage(snplayer, SNConfigHandler.priestAltarRecipe.getRecipeLine());
							}
						}
					}
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// 					Spells						//
	// -------------------------------------------- //
	
	public boolean banish(Player player, Player victim){
		SuperNPlayer snplayer = SuperNManager.get(player);
		SuperNPlayer snvictim = SuperNManager.get(victim);
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SuperNManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return false;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerBanish){
			if(snvictim.isSuper()){
				SuperNManager.alterPower(snplayer, -SNConfigHandler.priestPowerBanish, "Banished "+victim.getName());
				SuperNManager.sendMessage(snvictim, "You were banished by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
				victim.teleport(SNConfigHandler.priestBanishLocation);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				return true;
			}
			SuperNManager.sendMessage(snplayer, "Can only banish supernatural players.");
			return false;
		}else{
			SuperNManager.sendMessage(snplayer, "Not enough power to banish.");
			return false;
		}
	}
	
	public boolean heal(Player player, Player victim){
		SuperNPlayer snplayer = SuperNManager.get(player);
		SuperNPlayer snvictim = SuperNManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerHeal){
			if(!snvictim.isSuper() && victim.getHealth()<20 && !victim.isDead()){
				SuperNManager.alterPower(snplayer, -SNConfigHandler.priestPowerHeal, "Healed "+victim.getName());
				SuperNManager.sendMessage(snvictim, "You were healed by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
				int health = victim.getHealth()+SNConfigHandler.priestHealAmount;
				if(health>20)
					health=20;
				victim.setHealth(health);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				return true;
			}else{
				SuperNManager.sendMessage(snplayer, "Player cannot be healed.");
				return false;
			}
		}else{
			SuperNManager.sendMessage(snplayer, "Not enough power to heal.");
			return false;
		}
	}
	
	public boolean exorcise(Player player, Player victim){
		SuperNPlayer snplayer = SuperNManager.get(player);
		SuperNPlayer snvictim = SuperNManager.get(victim);
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SuperNManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return false;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerExorcise){
			if(snvictim.isSuper()){
				SuperNManager.alterPower(snplayer, -SNConfigHandler.priestPowerExorcise, "Exorcised "+victim.getName());
				SuperNManager.sendMessage(snvictim, "You were exorcised by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
				SuperNManager.cure(snvictim);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				return true;
			}else{
				SuperNManager.sendMessage(snplayer, "Only supernatural players can be exorcised.");
				return false;
			}
		}else{
			SuperNManager.sendMessage(snplayer, "Not enough power to exorcise.");
			return false;
		}
	}
	
	public boolean cure(Player player, Player victim, Material material){
		SuperNPlayer snplayer = SuperNManager.get(player);
		SuperNPlayer snvictim = SuperNManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerCure){
			if(snvictim.isSuper()){
				if(victim.getItemInHand().getType().equals(material)){
					SuperNManager.alterPower(snplayer, -SNConfigHandler.priestPowerCure, "Cured "+victim.getName());
					SuperNManager.sendMessage(snvictim, ChatColor.WHITE+snplayer.getName()+ChatColor.RED+" has restored your humanity");
					SuperNManager.cure(snvictim);
					ItemStack item = player.getItemInHand();
					ItemStack item2 = victim.getItemInHand();
					if(item.getAmount()==1){
						player.setItemInHand(null);
					}else{
						item.setAmount(player.getItemInHand().getAmount()-1);
					}
					if(item2.getAmount()==1){
						victim.setItemInHand(null);
					}else{
						item2.setAmount(victim.getItemInHand().getAmount()-1);
					}
					return true;
				}else{
					SuperNManager.sendMessage(snplayer, ChatColor.WHITE+snvictim.getName()+ChatColor.RED
							+" is not holding "+ChatColor.WHITE+material.toString()+ChatColor.RED+".");
					return false;
				}
			}else{
				SuperNManager.sendMessage(snplayer, "You can only cure supernatural players.");
				return false;
			}
		}else{
			SuperNManager.sendMessage(snplayer, "Not enough power to cure.");
			return false;
		}
	}
	
	public boolean drainPower(Player player, Player victim){
		SuperNPlayer snplayer = SuperNManager.get(player);
		SuperNPlayer snvictim = SuperNManager.get(victim);
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SuperNManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return false;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerDrain){
			if(snvictim.isSuper()){
				double power = snvictim.getPower();
				power *= SNConfigHandler.priestDrainFactor;
				SuperNManager.alterPower(snplayer, -SNConfigHandler.priestPowerDrain, "Drained  "+snvictim.getName()+"'s power!");
				SuperNManager.alterPower(snvictim, -power, "Drained by "+snplayer.getName());
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				return true;
			}else{
				SuperNManager.sendMessage(snplayer, "Only supernatural players can be power drained.");
				return false;
			}
		}else{
			SuperNManager.sendMessage(snplayer, "Not enough power to drain power.");
			return false;
		}
	}
	
	// -------------------------------------------- //
	// 					Damage						//
	// -------------------------------------------- //
	
	public double priestAttack(Player priest, Entity victim, double damage){
		if((victim instanceof Animals) && !(victim instanceof Wolf)){
			SuperNManager.sendMessage(SuperNManager.get(priest), "You cannot hurt innocent animals.");
			damage = 0;
		}else if(victim instanceof Player){
			Player pVictim = (Player) victim;
			if(!SupernaturalsPlugin.instance.getPvP(pVictim))
				return damage;
			SuperNPlayer snvictim = SuperNManager.get(pVictim);
			if(snvictim.isSuper()){
				pVictim.setFireTicks(SNConfigHandler.priestFireTicks);
				damage += damage * SuperNManager.get(priest).scale(SNConfigHandler.priestDamageFactorAttackSuper);
			}else{
				damage += damage * SuperNManager.get(priest).scale(SNConfigHandler.priestDamageFactorAttackHuman); 
			}
		}else if(victim instanceof Monster){
			Monster mVictim = (Monster) victim;
			mVictim.setFireTicks(SNConfigHandler.priestFireTicks);
		}
		return damage;
	}
}