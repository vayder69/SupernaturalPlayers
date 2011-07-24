package me.matterz.supernaturals.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.manager.WereManager;
import me.matterz.supernaturals.util.EntityUtil;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SNEntityMonitor extends EntityListener {
	
	private static SupernaturalsPlugin plugin;
	private static HashMap<Player, ArrayList<String>> hunterApps = new HashMap<Player, ArrayList<String>>();
	
	public SNEntityMonitor(SupernaturalsPlugin instance){
		SNEntityMonitor.plugin = instance;
	}
	
	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow)event.getEntity();
            if(plugin.getHunterManager().getArrowMap().containsKey(arrow)){
            	String arrowType = plugin.getHunterManager().getArrowMap().get(arrow);
            	if(arrowType.equalsIgnoreCase("grapple")){
            		Player player = (Player)arrow.getShooter();
                    plugin.getHunterManager().startGrappling(player, arrow.getLocation());
            	}else if(arrowType.equalsIgnoreCase("fire")){
            		arrow.getLocation();
            		Block block = arrow.getWorld().getBlockAt(arrow.getLocation());
            		if(SNConfigHandler.burnableBlocks.contains(block.getType())){
            			block.setType(Material.FIRE);
            		}
            	}
            	plugin.getHunterManager().removeArrow(arrow);
            }
        }
    }
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){
		if(event.isCancelled()){
			return;
		}
		
		if(event instanceof EntityDamageByProjectileEvent){
			EntityDamageByProjectileEvent edbpEvent = (EntityDamageByProjectileEvent)event;
			
			//Define local fields
			Entity victim = event.getEntity();
			
			Entity damager = edbpEvent.getDamager();
			Player pDamager;
			SuperNPlayer snDamager;
			
			//For further interest that attacker must be a player.
			if(damager instanceof Player){
				pDamager = (Player)damager;
			}else if(damager instanceof Wolf){
				Wolf wolf = (Wolf) damager;
				if(!wolf.isTamed()){
					return;
				}
				if(!(wolf.getOwner() instanceof Player)){
					return;
				}
				pDamager = (Player) wolf.getOwner();
			}else{
				return;
			}
		
			snDamager = SupernaturalManager.get(pDamager);
			
			if(victim instanceof Creature){
				Creature cVictim = (Creature)victim;
				
				//Break vampire truce
				if(snDamager.isVampire() && SNConfigHandler.vampireTruce.contains(EntityUtil.creatureTypeFromEntity(cVictim))){
					plugin.getSuperManager().truceBreak(snDamager);
				} else if(snDamager.isGhoul() && SNConfigHandler.ghoulTruce.contains(EntityUtil.creatureTypeFromEntity(cVictim))){
					plugin.getSuperManager().truceBreak(snDamager);
				}
			}
		} else if(!(event instanceof EntityDamageByEntityEvent)){
			return;
		}
		
		EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent)event;
		
		// Define local fields
		Entity victim = event.getEntity();
		
		Entity damager = edbeEvent.getDamager();
		Player pDamager;
		SuperNPlayer snDamager;
		
		//For further interest that attacker must be a player.
		if(!(damager instanceof Player)){
			return;
		}

		pDamager = (Player)damager;
		snDamager = SupernaturalManager.get(pDamager);
		
		if(victim instanceof Creature){
			Creature cVictim = (Creature)victim;
			
			//Break vampire truce
			if(snDamager.isVampire() && SNConfigHandler.vampireTruce.contains(EntityUtil.creatureTypeFromEntity(cVictim))){
				plugin.getSuperManager().truceBreak(snDamager);
			} else if(snDamager.isGhoul() && SNConfigHandler.ghoulTruce.contains(EntityUtil.creatureTypeFromEntity(cVictim))){
				plugin.getSuperManager().truceBreak(snDamager);
			}
		}
	}
	
	public void onEntityDeath(EntityDeathEvent event){
		
		Entity entity = event.getEntity();
		
		Player pDamager = null;
		
		if(entity instanceof Creature){
			Event e = entity.getLastDamageCause();
			Entity damager = null;
			if(e instanceof EntityDamageByEntityEvent){
				damager = ((EntityDamageByEntityEvent) e).getDamager();
				
			} else if(e instanceof EntityDamageByProjectileEvent){
				damager = ((EntityDamageByEntityEvent) e).getDamager();
			}
			
			if(damager!=null){
				if(damager instanceof Player){
					pDamager = (Player) damager;
				}else if(damager instanceof Wolf){
					Wolf wolf = (Wolf) damager;
					if(!wolf.isTamed()){
						return;
					}
					if(!(wolf.getOwner() instanceof Player)){
						return;
					}
					pDamager = (Player) wolf.getOwner();
				}else{
					return;
				}
				SuperNPlayer snDamager = SupernaturalManager.get(pDamager);
				SupernaturalManager.killEvent(snDamager, null);
			}
			if(entity instanceof Wolf){
				WereManager.removeWolf((Wolf) entity);
			}
			return;
		}
		
		if(!(entity instanceof Player)) {
			return;
		}
		Player pVictim = (Player) entity;
		SuperNPlayer snplayer = SupernaturalManager.get(pVictim);
		SupernaturalManager.deathEvent((Player) entity);
		
		Entity damager = null;
		Event e = entity.getLastDamageCause();
		if(e instanceof EntityDamageByEntityEvent){
			damager = ((EntityDamageByEntityEvent) e).getDamager();
		} else if(e instanceof EntityDamageByProjectileEvent){
			damager = ((EntityDamageByEntityEvent) e).getDamager();	
		}
		
		if(damager!=null){
			if(damager instanceof Player){
				pDamager = (Player) damager;
				SuperNPlayer snDamager = SupernaturalManager.get(pDamager);
				if(SNConfigHandler.debugMode){
					SupernaturalsPlugin.log("Player "+snDamager.getName()+" has killed "+snplayer.getName());
				}
				if(snplayer.isHunter()){
					if(snDamager.equals(snplayer)){
						SupernaturalManager.sendMessage(snplayer, "You have killed yourself!");
						SupernaturalManager.sendMessage(snplayer, "This action, voluntary or not, has rescinded your status as a WitchHunter.");
						SupernaturalManager.cure(snplayer);
						if(SNConfigHandler.debugMode){
							SupernaturalsPlugin.log("Player "+pDamager.getName()+" cured themselves.");
						}
					}
				}else if(snDamager.isHuman()){
					ArrayList<String> supersKilled = new ArrayList<String>();
					if(hunterApps.containsKey(pDamager)){
						supersKilled = hunterApps.get(pDamager);
						if(!supersKilled.contains(snplayer.getType())){
							supersKilled.add(snplayer.getType());
							if(supersKilled.size()>=3){
								plugin.getHunterManager().invite(snDamager);
							}
						}
					}else{
						supersKilled.add(snplayer.getType());
					}
					hunterApps.put(pDamager, supersKilled);
				}
			}else if(damager instanceof Wolf){
				Wolf wolf = (Wolf) damager;
				if(!wolf.isTamed()){
					return;
				}
				if(!(wolf.getOwner() instanceof Player)){
					return;
				}
				pDamager = (Player) wolf.getOwner();
				if(SNConfigHandler.debugMode){
					SupernaturalsPlugin.log("Player "+pDamager.getName()+" has killed "+snplayer.getName()+" with wolf.");
				}
			}else{
				return;
			}
			SuperNPlayer snDamager = SupernaturalManager.get(pDamager);
			SupernaturalManager.killEvent(snDamager, snplayer);
		}
	}
}
