package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.util.EntityUtil;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class SNEntityMonitor extends EntityListener {
	
	public static SupernaturalsPlugin plugin;
	
	public SNEntityMonitor(SupernaturalsPlugin instance){
		SNEntityMonitor.plugin = instance;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){
		if(event.isCancelled()){
			return;
		}
		
		if(event instanceof EntityDamageByProjectileEvent){
			EntityDamageByProjectileEvent edbpEvent = (EntityDamageByProjectileEvent)event;
			
			// Define local fields
			Entity victim = event.getEntity();
			
			Entity damager = edbpEvent.getDamager();
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
					Player pDamager = (Player) damager;
					SuperNPlayer snDamager = SupernaturalManager.get(pDamager);
					plugin.getSuperManager().killEvent(snDamager, null);
				}
			}
			return;
		}
		
		if(!(entity instanceof Player)) {
			return;
		}
		SuperNPlayer snplayer = SupernaturalManager.get((Player)entity);
		
		plugin.getSuperManager().deathEvent((Player) entity);
		
		Entity damager = null;
		Event e = entity.getLastDamageCause();
		if(e instanceof EntityDamageByEntityEvent){
			damager = ((EntityDamageByEntityEvent) e).getDamager();
		} else if(e instanceof EntityDamageByProjectileEvent){
			damager = ((EntityDamageByEntityEvent) e).getDamager();	
		}
		
		if(damager!=null){
			if(damager instanceof Player){
				Player pDamager = (Player) damager;
				SuperNPlayer snDamager = SupernaturalManager.get(pDamager);
				plugin.getSuperManager().killEvent(snDamager, snplayer);
			}
		}
	}
}
