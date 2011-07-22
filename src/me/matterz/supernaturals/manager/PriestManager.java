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
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		int amount = 0;
		int delta = 0;
		if(world.getName().equalsIgnoreCase(SNConfigHandler.priestChurchWorld)){
			if(Math.abs(locX-SNConfigHandler.priestChurchLocationX) <= 10){
				if(Math.abs(locY-SNConfigHandler.priestChurchLocationY) <= 10){
					if(Math.abs(locZ-SNConfigHandler.priestChurchLocationZ) <= 10){
						if(snplayer.isPriest()){
							if(player.getItemInHand().getType().equals(Material.COAL)){
								SupernaturalManager.sendMessage(snplayer, "The Church excommunicates you!");
								SupernaturalManager.cure(snplayer);
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
								SupernaturalManager.sendMessage(snplayer, "The Church accepts your gracious donations of Bread, Fish, Grilled Pork and Apples.");
								SupernaturalManager.alterPower(snplayer, delta, "Donations!");
							}
						}else{
							SupernaturalManager.sendMessage(snplayer, "The Church Altar radiates holy power.");
							if(!snplayer.isHuman()) {
								SupernaturalManager.sendMessage(snplayer, "The holy power of the Church tears you asunder!");
								EntityDamageEvent event = new EntityDamageEvent(player, DamageCause.BLOCK_EXPLOSION, 20);
								player.setLastDamageCause(event);
								player.setHealth(0);
								if(snplayer.isGhoul()){
								double random = Math.random();
									if(random<(SNConfigHandler.spreadChance-0.1)){
										SupernaturalManager.cure(snplayer);
									}
								}
								return;
							}
							if(SNConfigHandler.priestAltarRecipe.playerHasEnough(player)) {
								SupernaturalManager.sendMessage(snplayer, "You donate these items to the Church:");
								SupernaturalManager.sendMessage(snplayer, SNConfigHandler.priestAltarRecipe.getRecipeLine());
								SupernaturalManager.sendMessage(snplayer, "The Church recognizes your holy spirit and accepts you into the priesthood.");
								SNConfigHandler.priestAltarRecipe.removeFromPlayer(player);
								SupernaturalManager.curse(snplayer, "priest", SNConfigHandler.priestPowerStart);
							}else{
								SupernaturalManager.sendMessage(snplayer, "The Church judges your intended donate insufficient.  You must gather the following: ");
								SupernaturalManager.sendMessage(snplayer, SNConfigHandler.priestAltarRecipe.getRecipeLine());
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
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SupernaturalManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return false;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerBanish){
			if(snvictim.isSuper()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerBanish, "Banished "+victim.getName());
				SupernaturalManager.sendMessage(snvictim, "You were banished by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
				victim.teleport(SNConfigHandler.priestBanishLocation);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				return true;
			}
			SupernaturalManager.sendMessage(snplayer, "Can only banish supernatural players.");
			return false;
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to banish.");
			return false;
		}
	}
	
	public boolean heal(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerHeal){
			if(!snvictim.isSuper() && victim.getHealth()<20 && !victim.isDead()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerHeal, "Healed "+victim.getName());
				SupernaturalManager.sendMessage(snvictim, "You were healed by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
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
				SupernaturalManager.sendMessage(snplayer, "Player cannot be healed.");
				return false;
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to heal.");
			return false;
		}
	}
	
	public boolean exorcise(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SupernaturalManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return false;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerExorcise){
			if(snvictim.isSuper()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerExorcise, "Exorcised "+victim.getName());
				SupernaturalManager.sendMessage(snvictim, "You were exorcised by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
				SupernaturalManager.cure(snvictim);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				return true;
			}else{
				SupernaturalManager.sendMessage(snplayer, "Only supernatural players can be exorcised.");
				return false;
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to exorcise.");
			return false;
		}
	}
	
	public boolean cure(Player player, Player victim, Material material){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerCure){
			if(snvictim.isSuper()){
				if(victim.getItemInHand().getType().equals(material)){
					SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerCure, "Cured "+victim.getName());
					SupernaturalManager.sendMessage(snvictim, ChatColor.WHITE+snplayer.getName()+ChatColor.RED+" has restored your humanity");
					SupernaturalManager.cure(snvictim);
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
					SupernaturalManager.sendMessage(snplayer, ChatColor.WHITE+snvictim.getName()+ChatColor.RED
							+" is not holding "+ChatColor.WHITE+material.toString()+ChatColor.RED+".");
					return false;
				}
			}else{
				SupernaturalManager.sendMessage(snplayer, "You can only cure supernatural players.");
				return false;
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to cure.");
			return false;
		}
	}
	
	public boolean drainPower(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SupernaturalManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return false;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerDrain){
			if(snvictim.isSuper()){
				double power = snvictim.getPower();
				power *= SNConfigHandler.priestDrainFactor;
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerDrain, "Drained  "+snvictim.getName()+"'s power!");
				SupernaturalManager.alterPower(snvictim, -power, "Drained by "+snplayer.getName());
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				return true;
			}else{
				SupernaturalManager.sendMessage(snplayer, "Only supernatural players can be power drained.");
				return false;
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to drain power.");
			return false;
		}
	}
	
	// -------------------------------------------- //
	// 					Damage						//
	// -------------------------------------------- //
	
	public double priestAttack(Player priest, Entity victim, double damage){
		if((victim instanceof Animals) && !(victim instanceof Wolf)){
			SupernaturalManager.sendMessage(SupernaturalManager.get(priest), "You cannot hurt innocent animals.");
			damage = 0;
		}else if(victim instanceof Player){
			Player pVictim = (Player) victim;
			if(!SupernaturalsPlugin.instance.getPvP(pVictim))
				return damage;
			SuperNPlayer snvictim = SupernaturalManager.get(pVictim);
			if(snvictim.isSuper()){
				pVictim.setFireTicks(SNConfigHandler.priestFireTicks);
				damage += damage * SupernaturalManager.get(priest).scale(SNConfigHandler.priestDamageFactorAttackSuper);
			}else{
				damage += damage * SupernaturalManager.get(priest).scale(SNConfigHandler.priestDamageFactorAttackHuman); 
			}
		}else if(victim instanceof Monster){
			Monster mVictim = (Monster) victim;
			mVictim.setFireTicks(SNConfigHandler.priestFireTicks);
		}
		return damage;
	}
}