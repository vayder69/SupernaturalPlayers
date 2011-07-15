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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
		
		// For further interest this must be a close combat attack by another entity
		if(event.getCause() != DamageCause.ENTITY_ATTACK){
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
				//A vampire attacked a creature
				Creature cVictim = (Creature)victim;
				
				//Break vampire truce
				if(snDamager.isVampire() && SNConfigHandler.vampireTruce.contains(EntityUtil.creatureTypeFromEntity(cVictim))){
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
			//A vampire attacked a creature
			Creature cVictim = (Creature)victim;
			
			//Break vampire truce
			if(snDamager.isVampire() && SNConfigHandler.vampireTruce.contains(EntityUtil.creatureTypeFromEntity(cVictim))){
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
					if(snDamager.isVampire()){
						SupernaturalManager.alterPower(snDamager, SNConfigHandler.vampireKillPowerCreatureGain, "Creature death!");
					} else if(snDamager.isGhoul()){
						SupernaturalManager.alterPower(snDamager, SNConfigHandler.ghoulKillPowerCreatureGain, "Creature death!");
					} else if(snDamager.isWere()){
						SupernaturalManager.alterPower(snDamager, SNConfigHandler.wereKillPowerCreatureGain, "Creature death!");
					}
				}
			}
		}
		
		if(!(entity instanceof Player)) {
			return;
		}
		SuperNPlayer snplayer = SupernaturalManager.get((Player)entity);
		
		if(snplayer.isSuper()){
			if(snplayer.isVampire()){
				SupernaturalManager.alterPower(snplayer, SNConfigHandler.vampireDeathPowerPenalty, "You died!");
			} else if(snplayer.isGhoul()){
				SupernaturalManager.alterPower(snplayer, SNConfigHandler.ghoulDeathPowerPenalty, "You died!");
			} else if(snplayer.isWere()){
				SupernaturalManager.alterPower(snplayer, SNConfigHandler.wereDeathPowerPenalty, "You died!");
			} else if(snplayer.isPriest()){
				SupernaturalManager.alterPower(snplayer, SNConfigHandler.priestDeathPowerPenalty, "You died!");
			}
		}
		
		Entity damager = null;
		Event e = entity.getLastDamageCause();
		if(e instanceof EntityDamageByEntityEvent){
			damager = ((EntityDamageByEntityEvent) e).getDamager();
		} else if(e instanceof EntityDamageByProjectileEvent){
			damager = ((EntityDamageByEntityEvent) e).getDamager();	
		}
		
		if(damager instanceof Player){
			Player pDamager = (Player) damager;
			SuperNPlayer snDamager = SupernaturalManager.get(pDamager);
			if(snDamager.isVampire()){
				SupernaturalManager.alterPower(snDamager, SNConfigHandler.vampireKillPowerPlayerGain, "Player killed!");
				if(SNConfigHandler.vampireKillSpreadCurse && (snplayer.getType().equalsIgnoreCase("human") || snplayer.getType().equalsIgnoreCase("priest")))
				{
					SupernaturalManager.sendMessage(snplayer, "You feel your heart stop! You have contracted vampirism.");
					plugin.getSuperManager().curse(snplayer, "vampire");
				}
			} else if(snDamager.isGhoul()){
				SupernaturalManager.alterPower(snDamager, SNConfigHandler.ghoulKillPowerPlayerGain, "Player killed!!");
				if(SNConfigHandler.ghoulKillSpreadCurse && (snplayer.getType().equalsIgnoreCase("human") || snplayer.getType().equalsIgnoreCase("priest")))
				{
					SupernaturalManager.sendMessage(snplayer, "Your body dies... You feel a deep hatred for the living.");
					plugin.getSuperManager().curse(snplayer, "ghoul");
				}
			} else if(snDamager.isWere()){
				SupernaturalManager.alterPower(snDamager, SNConfigHandler.ghoulKillPowerPlayerGain, "Player killed!!");
				if(SNConfigHandler.wereKillSpreadCurse && (snplayer.getType().equalsIgnoreCase("human") || snplayer.getType().equalsIgnoreCase("priest")))
				{
					SupernaturalManager.sendMessage(snplayer, "Your basic nature changes... You feel more in touch with your animal side.");
					plugin.getSuperManager().curse(snplayer, "werewolf");
				}
			}
		}
	}
}
