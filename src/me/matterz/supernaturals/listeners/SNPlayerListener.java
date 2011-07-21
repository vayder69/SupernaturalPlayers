package me.matterz.supernaturals.listeners;

import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;

public class SNPlayerListener extends PlayerListener{

	public static SupernaturalsPlugin plugin;
	
	public SNPlayerListener(SupernaturalsPlugin instance){
		SNPlayerListener.plugin = instance;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		Action action = event.getAction();		
		
		if(!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_AIR)) && event.isCancelled()){
			return;
		}
		
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		boolean cancelled = false;
		Material itemMaterial = event.getMaterial();
		
		if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){
			if(player.getItemInHand()==null){
				return;
			}
			
			if(snplayer.isVampire()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
					SupernaturalManager.jump(player, SNConfigHandler.jumpDeltaSpeed, true);
					event.setCancelled(true);
					return;
				}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.vampireMaterial)){
					plugin.getVampireManager().teleport(player);
					event.setCancelled(true);
					return;
				}
			}else if(snplayer.isWere()){
					if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.wolfMaterial)){
						if(SupernaturalManager.worldTimeIsNight(player)){
							plugin.getWereManager().summon(player);
							event.setCancelled(true);
							return;
						}else{
							SupernaturalManager.sendMessage(snplayer, "Cannot use this ability during the day.");
							return;
						}
					}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.wolfbaneMaterial)){
						if(SupernaturalManager.worldTimeIsNight(player)){
							SupernaturalManager.sendMessage(snplayer, "Cannot cure lycanthropy during the night.");
							return;
						}else{
							plugin.getWereManager().wolfbane(player);
							event.setCancelled(true);
							return;
						}
					}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.dashMaterial)){
						if(SupernaturalManager.worldTimeIsNight(player)){
							SupernaturalManager.jump(event.getPlayer(), SNConfigHandler.dashDeltaSpeed, false);
							event.setCancelled(true);
							return;
						}else{
							SupernaturalManager.sendMessage(snplayer, "Cannot use this ability during the day.");
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
									}else{
										plugin.getPriestManager().drainPower(player, victim);
										cancelled = false;
									}
									if(!event.isCancelled())
										event.setCancelled(cancelled);
									return;
								}
							}
						}
					}
				}
			}else if(snplayer.isDemon()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.demonMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(player.getName()+" is casting FIREBALL with "+itemMaterial.toString());
					cancelled = plugin.getDemonManager().fireball(player);
					if(!event.isCancelled())
						event.setCancelled(cancelled);
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
					SupernaturalManager.sendMessage(snplayer, "Vampires can't eat food. You must drink blood instead.");
					event.setCancelled(true);
					return;
				}else if(snplayer.isWere()){
					if(itemMaterial.equals(Material.BREAD)){
						SupernaturalManager.sendMessage(snplayer, "Werewolves do not gain power from Bread.");
						return;
					}else{
						SupernaturalManager.alterPower(snplayer, SNConfigHandler.werePowerFood, "Eating!");
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
		if ((event.getLeaveMessage().contains("Flying")) || (event.getReason().contains("Flying"))) {
			SuperNPlayer snplayer = SupernaturalManager.get(event.getPlayer());
			if(snplayer.isVampire()&& event.getPlayer().getItemInHand().getType().toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
				event.setCancelled(true);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(event.getPlayer().getName() + " was not kicked for flying as a vampire.");
			} 
		}
	}
}
