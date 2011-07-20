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
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class SNPlayerMonitor extends PlayerListener {
	
private SupernaturalsPlugin plugin;
	
	public SNPlayerMonitor(SupernaturalsPlugin instance){
		this.plugin = instance;
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event){
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		ItemStack item = player.getItemInHand();
		if(event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)){
			if(snplayer.isVampire()){
				if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
					SupernaturalManager.jump(player, SNConfigHandler.jumpDeltaSpeed, true);
				}else if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.vampireMaterial)){
					plugin.getVampireManager().teleport(player, item);
				}
			}else if(snplayer.isWere()){
				if(SupernaturalManager.worldTimeIsNight(player)){
					if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.wolfMaterial)){
						plugin.getWereManager().summon(player, item);
					}else if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.wolfbaneMaterial)){
						SupernaturalManager.sendMessage(snplayer, "Cannot cure lycanthropy during the night.");
					}else if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.dashMaterial)){
						SupernaturalManager.jump(event.getPlayer(), SNConfigHandler.dashDeltaSpeed, false);
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log(snplayer.getName() + " used dash!");
					}
				}else{
					if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.wolfbaneMaterial)){
						plugin.getWereManager().wolfbane(player);
					}
				}
			}else if(snplayer.isGhoul()){
				if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.ghoulMaterial)){
					plugin.getGhoulManager().summon(player, item);
				}
			}else if(snplayer.isPriest()){
				if(SNConfigHandler.priestSpellMaterials.contains(item.getType())){
					List<Block> blocks = player.getLineOfSight(SNConfigHandler.transparent, 20);
					List<Entity> entities = player.getNearbyEntities(21, 21, 21);
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " is attempting to cast a spell...");
					for(Block block : blocks){
						for(Entity entity : entities){
							if(entity instanceof Player){
								Player victim = (Player) entity;
								Location location = victim.getLocation();
								Location feetLocation = new Location(location.getWorld(), location.getX(), location.getY()-1, location.getZ());
								Location groundLocation = new Location(location.getWorld(), location.getX(), location.getY()-2, location.getZ());
								if(location.getBlock().equals(block) || feetLocation.getBlock().equals(block) || groundLocation.getBlock().equals(block)){
									if(SNConfigHandler.debugMode)
										SupernaturalsPlugin.log(victim.getName()+" is targetted by spell.");
									if(item.getType().equals(SNConfigHandler.priestSpellMaterials.get(0))){
										plugin.getPriestManager().banish(player, victim);
									}else if(item.getType().equals(SNConfigHandler.priestSpellMaterials.get(1))){
										plugin.getPriestManager().exorcise(player, victim);
									}else if(item.getType().equals(SNConfigHandler.priestSpellMaterials.get(2))){
										plugin.getPriestManager().cure(player, victim, item.getType());
									}else if(item.getType().equals(SNConfigHandler.priestSpellMaterials.get(3))){
										plugin.getPriestManager().heal(player, victim);
									}else{
										plugin.getPriestManager().drainPower(player, victim);
									}
									return;
								}
							}
						}
					}
				}
			}else if(snplayer.isDemon()){
				if(item.getType().toString().equalsIgnoreCase(SNConfigHandler.demonMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(player.getName()+" is casting FIREBALL with "+item.getType().toString());
					plugin.getDemonManager().fireball(player);
				}
			}else if(snplayer.isHunter()){
				if(item.getType().equals(Material.BOW)){
					plugin.getHunterManager().changeArrowType(snplayer);
				}
			}
		}
	}
	
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event){
		if(SupernaturalManager.get(event.getPlayer()).isHunter()){
			event.getPlayer().setSneaking(true);
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event){	    
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		if(snplayer.isHuman()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.WHITE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.WHITE + "Human " + player.getName() + ChatColor.GOLD + " has joined the server.");
		}else if(snplayer.isVampire()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.DARK_PURPLE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "Vampire " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isWere()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.BLUE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.BLUE + "Werewolf " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isGhoul()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.DARK_GRAY+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.DARK_GRAY + "Ghoul " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isPriest()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.GOLD+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.GOLD + "Priest " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isHunter()){
			player.setSneaking(true);
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.GREEN+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.GREEN + "WitchHunter " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isDemon()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.RED+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.RED + "Demon " + player.getName() + ChatColor.GOLD + " has joined the server.");
		}
		
	}
}
