package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.util.EntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

public class SNEntityListener extends EntityListener{

	private SupernaturalsPlugin plugin;
	
	public SNEntityListener(SupernaturalsPlugin instance){
		this.plugin = instance;
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
		
		double damage=0;
		double bonusDamage=0;
		
		victim = event.getEntity();
		
		//Modify victim player damage
		if(victim instanceof Player){
			pVictim = (Player) victim;
			snpVictim = SupernaturalManager.get(pVictim);
			if(snpVictim.isVampire()){
				if(event.getCause().equals(DamageCause.DROWNING)){
					if(snpVictim.getPower() > SNConfigHandler.vampireDrowningCost){
						SupernaturalManager.alterPower(snpVictim, -SNConfigHandler.vampireDrowningCost, "Water!");
						event.setCancelled(true);
						return;
					}else{
						SupernaturalManager.sendMessage(snpVictim, "Not enough power to prevent water damage!");
						return;
					}
				}else if(event.getCause().equals(DamageCause.FALL)){
						event.setCancelled(true);
						return;
				}
			}else if(snpVictim.isWere()){
				if(event.getCause().equals(DamageCause.FALL)){
					event.setDamage((int)(event.getDamage()*SNConfigHandler.wereDamageFall));
					return;
				}
			}else if(snpVictim.isGhoul()){
				if(event.getCause().equals(DamageCause.FALL)){
					event.setCancelled(true);
					return;
				}
			}
		}
		
		//player damager events
		if(event instanceof EntityDamageByProjectileEvent){
			edbpEvent = (EntityDamageByProjectileEvent) event;
			damager = edbpEvent.getDamager();
			damage = event.getDamage();
			
			if(!(damager instanceof Player))
				return;	
			
			pDamager = (Player)damager;
			snpDamager = SupernaturalManager.get(pDamager);
			ItemStack item = pDamager.getItemInHand();
			
			//Modify damage if damager is a supernatural
			if(snpDamager.isVampire()){
				bonusDamage = damage * snpDamager.scaleAttack(SNConfigHandler.vampireDamageFactor);
				damage = damage + bonusDamage;
			}else if(snpDamager.isGhoul()){
				SupernaturalsPlugin.log(pDamager.getName() + " was not allowed to use "+item.getType().toString());
				SupernaturalManager.sendMessage(snpDamager, "Ghouls do no damage with weapons!");
				damage=0;
			}else if(snpDamager.isWere()){
				SupernaturalsPlugin.log("Werewolf attack event!");
				if(SupernaturalManager.worldTimeIsNight(pDamager)){
					SupernaturalsPlugin.log(pDamager.getName() + " was not allowed to use "+item.getType().toString());
					SupernaturalManager.sendMessage(snpDamager, "Werewolves cannot use weapons at night!");
					damage=0;
				}
			}else if(snpDamager.isPriest()){
				damage = plugin.getPriestManager().priestAttack(pDamager, victim, damage);
			}
			
			// Modify damage if victim is a supernatural
			if(victim instanceof Player){
				pVictim = (Player)victim;
				snpVictim = SupernaturalManager.get(pVictim);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(pDamager.getName() + " attacked " + pVictim.getName() + " with " + item.getType().toString());
				if(snpVictim.isVampire()){
					if(SNConfigHandler.woodMaterials.contains(item.getType())){
						bonusDamage = damage * SNConfigHandler.woodFactor; 
						damage = damage + bonusDamage;
						SupernaturalManager.sendMessage(snpVictim, "Vampires have a weakness to wood!");
					}else{
						bonusDamage = damage * snpVictim.scaleDefense(SNConfigHandler.vampireDamageReceivedFactor);
						damage = damage - bonusDamage;
					}
				}else if(snpVictim.isGhoul()){
					if(SNConfigHandler.ghoulWeaponImmunity.contains(item.getType())){
						damage = 0;
						SupernaturalManager.sendMessage(snpDamager, "Ghouls are immune to that weapon!");
					}else{
						bonusDamage = damage * snpVictim.scaleDefense(SNConfigHandler.ghoulDamageReceivedFactor); 
						damage = damage - bonusDamage;
					}
				}else if(snpDamager.isWere()){
					if(SupernaturalManager.worldTimeIsNight(pDamager)){
						bonusDamage = damage * snpVictim.scaleDefense(SNConfigHandler.wereDamageReceivedFactor);
						damage = damage - bonusDamage;
					}
				}		
			}
			event.setDamage((int)Math.round(damage));
			return;
		}else if(event instanceof EntityDamageByEntityEvent){
			edbeEvent = (EntityDamageByEntityEvent) event;
			damager = edbeEvent.getDamager();
			damage = event.getDamage();
			
			if(!(damager instanceof Player))
				return;	
			
			pDamager = (Player)damager;
			snpDamager = SupernaturalManager.get(pDamager);
			ItemStack item = pDamager.getItemInHand();
			
			//Modify damage if damager is a supernatural
			if(snpDamager.isVampire()){
				damage += damage * snpDamager.scaleAttack(SNConfigHandler.vampireDamageFactor);
			}else if(snpDamager.isGhoul()){
				if(SNConfigHandler.ghoulWeapons.contains(item.getType())){
						SupernaturalsPlugin.log(pDamager.getName() + " was not allowed to use "+item.getType().toString());
						SupernaturalManager.sendMessage(snpDamager, "Ghouls do no damage with weapons!");
						damage=0;
				}else{
					bonusDamage = damage * snpDamager.scaleAttack(SNConfigHandler.ghoulDamageFactor);
					damage = damage + bonusDamage;
					}
			}else if(snpDamager.isWere()){
				SupernaturalsPlugin.log("Werewolf attack event!");
				if(SupernaturalManager.worldTimeIsNight(pDamager)){
					if(SNConfigHandler.ghoulWeapons.contains(item.getType())){
						SupernaturalsPlugin.log(pDamager.getName() + " was not allowed to use "+item.getType().toString());
						SupernaturalManager.sendMessage(snpDamager, "Werewolves cannot use weapons at night!");
						damage=0;
					}else{
						if(victim instanceof Player)
							SupernaturalsPlugin.log(pDamager.getName() + " did "+damage+" to "+((Player) victim).getName());
						bonusDamage = damage * snpDamager.scaleAttack(SNConfigHandler.wereDamageFactor);
						damage = damage + bonusDamage;
					}
				}
			}else if(snpDamager.isPriest()){
				damage = plugin.getPriestManager().priestAttack(pDamager, victim, damage);
			}
			
			// Modify damage if victim is a supernatural
			if(victim instanceof Player){
				pVictim = (Player)victim;
				snpVictim = SupernaturalManager.get(pVictim);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(pDamager.getName() + " attacked " + pVictim.getName() + " with " + item.getType().toString());
				if(snpVictim.isVampire()){
					if(SNConfigHandler.woodMaterials.contains(item.getType())){
						bonusDamage = damage * SNConfigHandler.woodFactor; 
						damage = damage + bonusDamage;
						SupernaturalManager.sendMessage(snpVictim, "Vampires have a weakness to wood!");
					}else{
						bonusDamage = damage * snpVictim.scaleDefense(SNConfigHandler.vampireDamageReceivedFactor);
						damage = damage - bonusDamage;
					}
				}else if(snpVictim.isGhoul()){
					if(SNConfigHandler.ghoulWeaponImmunity.contains(item.getType())){
						damage = 0;
						SupernaturalManager.sendMessage(snpDamager, "Ghouls are immune to that weapon!");
					}else{
						bonusDamage = damage * snpVictim.scaleDefense(SNConfigHandler.ghoulDamageReceivedFactor);
						damage = damage - bonusDamage;
					}
				}else if(snpDamager.isWere()){
					if(SupernaturalManager.worldTimeIsNight(pDamager)){
						bonusDamage = damage * snpVictim.scaleDefense(SNConfigHandler.wereDamageReceivedFactor);
						damage = damage - bonusDamage;
					}
				}		
			}
			event.setDamage((int)Math.round(damage));
		} 		
	}
	
	@Override
	public void onEntityTarget(EntityTargetEvent event){
		if(event.isCancelled()){
			return;
		}
		if(!(event.getTarget() instanceof Player)){
			return;
		}
		if(event.getEntity().equals(null))
			return;
		
		SuperNPlayer snplayer = SupernaturalManager.get((Player)event.getTarget());

		if(!snplayer.getTruce()) {
			return;
		}
		if(EntityUtil.creatureTypeFromEntity(event.getEntity())==null){
			return;
		}
		if(snplayer.isVampire() && SNConfigHandler.vampireTruce.contains(EntityUtil.creatureTypeFromEntity(event.getEntity()))){
			event.setCancelled(true);
		} else if(snplayer.isGhoul() && SNConfigHandler.ghoulTruce.contains(EntityUtil.creatureTypeFromEntity(event.getEntity()))){
			event.setCancelled(true);
		} else if(snplayer.isWere() && SNConfigHandler.wolfTruce && EntityUtil.creatureNameFromEntity(event.getEntity()).equalsIgnoreCase("wolf")){
			event.setCancelled(true);
		}
	}	
}
