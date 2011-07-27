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

import java.util.ArrayList;
import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SuperNManager;
import me.matterz.supernaturals.util.EntityUtil;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

public class SNEntityListener extends EntityListener{

	private SupernaturalsPlugin plugin;
	private String worldPermission = "supernatural.world.disabled";
	
	private boolean projectileCalled = false;
	private String arrowType = "normal";
	private List<Player> demons = new ArrayList<Player>();
	
	public SNEntityListener(SupernaturalsPlugin instance){
		this.plugin = instance;
	}
	
	@Override
	public void onEntityExplode(EntityExplodeEvent event){
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log("Entity Explode event with "+event.getEntity().getClass().getSimpleName());
		if(event.getEntity() instanceof Fireball){
			Fireball fireball = (Fireball) event.getEntity();
			if(fireball.getShooter() instanceof Player){
				if(SupernaturalsPlugin.hasPermissions((Player) fireball.getShooter(), worldPermission) && SNConfigHandler.multiworld)
					return;
				for(Entity entity : fireball.getNearbyEntities(3, 3, 3)){
					if(entity instanceof LivingEntity){
						LivingEntity lEntity = (LivingEntity) entity;
						if(entity instanceof Player){
							Player player = (Player) entity;
							SuperNPlayer snplayer = SuperNManager.get(player);
							if(snplayer.isDemon())
								continue;
							if(!SupernaturalsPlugin.instance.getPvP(player))
								continue;
						}
						lEntity.damage(SNConfigHandler.demonFireballDamage, fireball);
						lEntity.setFireTicks(200);
					}
				}
			}
		}
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){
		if(event.isCancelled()){
			return;
		}
		
		Entity victim;
		Player pVictim;
		SuperNPlayer snpVictim;

		
		EntityDamageByEntityEvent edbeEvent;
		
		Entity damager;
		Player pDamager;
		SuperNPlayer snpDamager;
		
		double damage;
		
		victim = event.getEntity();
		
		//Modify victim player damage
		if(victim instanceof Player){
			pVictim = (Player) victim;
			if(SupernaturalsPlugin.hasPermissions(pVictim, worldPermission) && SNConfigHandler.multiworld)
				return;
			snpVictim = SuperNManager.get(pVictim);
			if(snpVictim.isVampire()){
				if(event.getCause().equals(DamageCause.DROWNING)){
					if(snpVictim.getPower() > SNConfigHandler.vampireDrowningCost){
						SuperNManager.alterPower(snpVictim, -SNConfigHandler.vampireDrowningCost, "Water!");
						event.setCancelled(true);
						return;
					}else{
						SuperNManager.sendMessage(snpVictim, "Not enough power to prevent water damage!");
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
			}else if(snpVictim.isDemon()){
				if(event.getCause().equals(DamageCause.LAVA) 
						|| event.getCause().equals(DamageCause.FIRE) 
						|| event.getCause().equals(DamageCause.BLOCK_EXPLOSION)
						|| event.getCause().equals(DamageCause.ENTITY_EXPLOSION)
						|| event.getCause().equals(DamageCause.FIRE_TICK)){
					final Player dPlayer = pVictim;
					if(!demons.contains(dPlayer)){
						demons.add(dPlayer);
						plugin.getDemonManager().heal(pVictim);
						if(event.getCause().equals(DamageCause.FIRE_TICK))
							SuperNManager.alterPower(snpVictim, SNConfigHandler.demonPowerGain, "Fire!");
						SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SupernaturalsPlugin.instance, new Runnable() {
			                public void run() {
			                	demons.remove(dPlayer);
			                }
			            }, 41);
					}
					pVictim.setFireTicks(0);
					event.setCancelled(true);
					return;
				}
			}else if(snpVictim.isHunter()){
				if(event.getCause().equals(DamageCause.FALL)){
					int newDamage = event.getDamage()-SNConfigHandler.hunterFallReduction;
					if(newDamage < 0)
						newDamage = 0;
					event.setDamage(newDamage);
					return;
				}
			}
		}
		
		//player damager events
		if(event instanceof EntityDamageByEntityEvent){
			edbeEvent = (EntityDamageByEntityEvent) event;
			damager = edbeEvent.getDamager();
			damage = event.getDamage();
		}else
			return;
		if(!(damager instanceof Player))
			return;	
		
		if(edbeEvent instanceof EntityDamageByProjectileEvent){
			projectileCalled = true;
			EntityDamageByProjectileEvent edpeEvent = (EntityDamageByProjectileEvent) edbeEvent;
			if(edpeEvent.getProjectile() instanceof Arrow){
				Arrow arrow = (Arrow) edpeEvent.getProjectile();
				if(plugin.getHunterManager().getArrowMap().containsKey(arrow)){
					arrowType = plugin.getHunterManager().getArrowType(arrow);
				}else{
					arrowType = "normal";
				}
			}
			return;
		}else{
			pDamager = (Player)damager;
			snpDamager = SuperNManager.get(pDamager);
			ItemStack item = pDamager.getItemInHand();
			
			if(projectileCalled){
				if(arrowType.equalsIgnoreCase("power")){
					damage += damage * SNConfigHandler.hunterPowerArrowDamage;
				}else if(arrowType.equalsIgnoreCase("fire")){
					victim.setFireTicks(SNConfigHandler.hunterFireArrowFireTicks);
				}
				projectileCalled = false;
			}
			
			//Modify damage if damager is a supernatural
			if(snpDamager.isVampire()){
				damage += damage * snpDamager.scale(SNConfigHandler.vampireDamageFactor);
			}else if(snpDamager.isGhoul()){
				if(SNConfigHandler.ghoulWeapons.contains(item.getType())){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(pDamager.getName() + " was not allowed to use "+item.getType().toString());
					SuperNManager.sendMessage(snpDamager, "Ghouls do no damage with weapons!");
					damage=0;
				}else{
					damage += damage * snpDamager.scale(SNConfigHandler.ghoulDamageFactor);
					}
			}else if(snpDamager.isWere()){
				if(SuperNManager.worldTimeIsNight(pDamager)){
					if(SNConfigHandler.ghoulWeapons.contains(item.getType())){
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log(pDamager.getName() + " was not allowed to use "+item.getType().toString());
						SuperNManager.sendMessage(snpDamager, "Werewolves cannot use weapons at night!");
						damage=0;
					}else{
						damage += damage * snpDamager.scale(SNConfigHandler.wereDamageFactor);
					}
				}
			}else if(snpDamager.isPriest()){
				damage = plugin.getPriestManager().priestAttack(pDamager, victim, damage);
			}else if(snpDamager.isHunter()){
				if(SNConfigHandler.ghoulWeapons.contains(item.getType())){
					if(!item.getType().equals(Material.BOW)){
						damage = 0;
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log(pDamager.getName() + " was not allowed to use "+item.getType().toString());
						SuperNManager.sendMessage(snpDamager, "WitchHunters cannot use melee weapons!");
					}
				}
			}
			
			// Modify damage if victim is a supernatural
			if(victim instanceof Player){
				pVictim = (Player)victim;
				snpVictim = SuperNManager.get(pVictim);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(pDamager.getName() + " attacked " + pVictim.getName() + " with " + item.getType().toString());
				if(snpVictim.isVampire()){
					if(SNConfigHandler.woodMaterials.contains(item.getType())){
						damage += (damage * SNConfigHandler.woodFactor);
						SuperNManager.sendMessage(snpVictim, "Vampires have a weakness to wood!");
					}else{
						damage -= (damage * snpVictim.scale((1-SNConfigHandler.vampireDamageReceivedFactor)));
					}
				}else if(snpVictim.isGhoul()){
					if(SNConfigHandler.ghoulWeaponImmunity.contains(item.getType())){
						damage = 0;
						SuperNManager.sendMessage(snpDamager, "Ghouls are immune to that weapon!");
					}else{
						damage -= (damage * snpVictim.scale((1-SNConfigHandler.ghoulDamageReceivedFactor)));
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
		
		if(event.getEntity()==null){
			return;
		}
		
		if(SupernaturalsPlugin.hasPermissions((Player) event.getTarget(), worldPermission) && SNConfigHandler.multiworld)
			return;
		
		SuperNPlayer snplayer = SuperNManager.get((Player)event.getTarget());

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