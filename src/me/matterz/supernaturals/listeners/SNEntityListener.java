package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.EntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

public class SNEntityListener extends EntityListener{

	public static SupernaturalsPlugin plugin;
	
	public SNEntityListener(SupernaturalsPlugin instance){
		SNEntityListener.plugin = instance;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){
		if(event.isCancelled()){
			return;
		}
		
		Entity victim;
		Player pVictim;
		SuperNPlayer snpVictim;
		
		EntityDamageByProjectileEvent edbpEvent;
		EntityDamageByEntityEvent edbeEvent;
		
		Entity damager;
		Player pDamager;
		SuperNPlayer snpDamager;
		
		float damage;
		
		victim = event.getEntity();
		
		//Modify victim player damage
		if(victim instanceof Player){
			pVictim = (Player) victim;
			snpVictim = plugin.getSuperManager().get(pVictim);
		
			if(snpVictim.isVampire() && (event.getCause() == DamageCause.DROWNING)){
				event.setCancelled(true);
				return;
			} else if(snpVictim.isVampire() && (event.getCause() == DamageCause.FALL)){
				event.setCancelled(true);
				return;
			}
		}
			
		//Modify attack damage events
		if(event.getCause() != DamageCause.ENTITY_ATTACK){
			return;
		}
		
		//Projectile event handling
		if(event instanceof EntityDamageByProjectileEvent){
			edbpEvent = (EntityDamageByProjectileEvent) event;
			damager = edbpEvent.getDamager();
			if(!(damager instanceof Player))
				return;	
			
			pDamager = (Player)damager;
			snpDamager = plugin.getSuperManager().get(pDamager);
			
			damage = event.getDamage();
			// Modify damage if victim is a vampire
			
			if(victim instanceof Player){
				pVictim = (Player)victim;
				snpVictim = plugin.getSuperManager().get(pVictim);
				if(snpVictim.isVampire()){
					if(SNConfigHandler.woodMaterials.contains(pDamager.getItemInHand().getType().toString())){
						damage *= SNConfigHandler.woodFactor;
						plugin.getSuperManager().sendMessage(snpVictim, "Vampires have a weakness to wood!");
					}else{
						damage *= SNConfigHandler.vampireDamageReceivedFactor;
					}
				}
			}
			
		//Melee event handling
		}else if(!(event instanceof EntityDamageByEntityEvent)){
			return;
		}
		
		edbeEvent = (EntityDamageByEntityEvent) event;
		damager = edbeEvent.getDamager();
		if(!(damager instanceof Player))
			return;	
		
		pDamager = (Player)damager;
		snpDamager = plugin.getSuperManager().get(pDamager);
		
		damage = event.getDamage();
		
		//Modify damage if damager is a vampire
		if(snpDamager.isVampire()){
			damage *= SNConfigHandler.vampireDamageFactor;
		}
		
		// Modify damage if victim is a vampire
		if(victim instanceof Player){
			pVictim = (Player)victim;
			snpVictim = plugin.getSuperManager().get(pVictim);
			if(snpVictim.isVampire()){
				if(SNConfigHandler.woodMaterials.contains(pDamager.getItemInHand().getType().toString())){
					damage *= SNConfigHandler.woodFactor;
					plugin.getSuperManager().sendMessage(snpVictim, "Vampires have a weakness to wood!");
				}else{
					damage *= SNConfigHandler.vampireDamageReceivedFactor;
				}
			}
		}
		
		event.setDamage(Math.round(damage));
	}
	
	@Override
	public void onEntityTarget(EntityTargetEvent event){
		if(event.isCancelled()){
			return;
		}
		
		// If a player is targeted...
		if(!(event.getTarget() instanceof Player)){
			return;
		}
		
		// ... by creature that cares about the truce with vampires ...
		if(!(SNConfigHandler.vampireTruce.contains(EntityUtil.creatureNameFromEntity(event.getEntity())))){
			return;
		}
		
		SuperNPlayer snplayer = plugin.getSuperManager().get((Player)event.getTarget());
		
		// ... and that player is a vampire ...
		if(!snplayer.isVampire()) {
			return;
		}
		
		// ... that has not recently done something to break the truce...
		if(!snplayer.getTruce()) {
			return;
		}
		
		// Then the creature will not attack.
		event.setCancelled(true);
	}
}
