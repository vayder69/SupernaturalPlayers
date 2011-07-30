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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.EntityUtil;
import me.matterz.supernaturals.util.SNTaskTimer;

public class SuperNManager {

	private SupernaturalsPlugin plugin;
	private String worldPermission = "supernatural.world.disabled";
	private static String permissions = "supernatural.admin.infinitepower";
	
	private static List<SuperNPlayer> supernaturals = new ArrayList<SuperNPlayer>();
	private transient int taskCounter = 0;
	private static int timer;
	
	public SuperNManager(SupernaturalsPlugin plugin){
		this.plugin = plugin;
	}
	
	// -------------------------------------------- //
	// 				Data Management					//
	// -------------------------------------------- //
	
	public static List<SuperNPlayer> getSupernaturals(){
		return supernaturals;
	}
	
	public static void setSupernaturals(List<SuperNPlayer> supernaturals){
		SuperNManager.supernaturals = supernaturals;
	}
	
	public static SuperNPlayer get(String playername){
		for(SuperNPlayer supernatural : supernaturals){
			if(supernatural.getName().equalsIgnoreCase(playername)){
				return supernatural;
			}
		}
		
		SuperNPlayer snplayer = new SuperNPlayer(playername);
		supernaturals.add(snplayer);
		return snplayer;
	}
	
	public static SuperNPlayer get(Player player){
		for(SuperNPlayer supernatural : supernaturals){
			if(supernatural.getName().equalsIgnoreCase(player.getName())){
				return supernatural;
			}
		}
		SuperNPlayer snplayer = new SuperNPlayer(player.getName());
		supernaturals.add(snplayer);
		return snplayer;
	}	
	
	public static Set<SuperNPlayer> findAllOnline() {
		Set<SuperNPlayer> snplayers = new HashSet<SuperNPlayer>();
		for (Player player : SupernaturalsPlugin.instance.getServer().getOnlinePlayers()) {
			snplayers.add(get(player));
		}
		return snplayers;
	}
	
	// -------------------------------------------- //
	// 			Supernatural Conversions			//
	// -------------------------------------------- //
	
	public static void convert(SuperNPlayer snplayer, String superType) {
		convert(snplayer, superType, 0);
	}
	
	public static void convert(SuperNPlayer snplayer, String superType, int powerLevel) {
		if(!SNConfigHandler.supernaturalTypes.contains(superType))
			return;
		String type = superType.toLowerCase();
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(type);
		snplayer.setPower(powerLevel);
		
		snplayer.setTruce(true);
		
		SuperNManager.sendMessage(snplayer, "You are now a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " turned into a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		
		updateName(snplayer);
		HunterManager.updateBounties();
//		if(snplayer.getType().equalsIgnoreCase("hunter"))
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(true);
//		else
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(false);
		if(snplayer.getOldType().equals("werewolf"))
			WereManager.removePlayer(snplayer);
		
		SupernaturalsPlugin.saveData();
	}
	
	public static void cure(SuperNPlayer snplayer){
		if(snplayer.getOldType().equals("priest")){
			revert(snplayer);
			return;
		}
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType("human");
		snplayer.setPower(0);
		
		snplayer.setTruce(true);
		
		updateName(snplayer);
		HunterManager.updateBounties();
//		if(snplayer.getType().equalsIgnoreCase("hunter"))
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(true);
//		else
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(false);
		if(snplayer.getOldType().equals("werewolf"))
			WereManager.removePlayer(snplayer);
		
		SuperNManager.sendMessage(snplayer, "You have been restored to humanity!");
		SupernaturalsPlugin.log(snplayer.getName() + " was restored to humanity!");
		SupernaturalsPlugin.saveData();
	}
	
	public static void revert(SuperNPlayer snplayer){
		String oldType = snplayer.getOldType();
		double oldPower = snplayer.getOldPower();
		
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(oldType);
		snplayer.setPower(oldPower);
		
		snplayer.setTruce(true);
		
		updateName(snplayer);
		HunterManager.updateBounties();
//		if(snplayer.getType().equalsIgnoreCase("hunter"))
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(true);
//		else
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(false);
		if(snplayer.getOldType().equals("werewolf"))
			WereManager.removePlayer(snplayer);
		
		SuperNManager.sendMessage(snplayer, "You been reverted to your previous state of being a " 
				+ ChatColor.WHITE + oldType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " was reverted to the previous state of being a " +oldType+"!");
		SupernaturalsPlugin.saveData();
		
	}
	
	// -------------------------------------------- //
	// 					Power Altering				//
	// -------------------------------------------- //
	
	public static void alterPower(SuperNPlayer snplayer, double delta){
		if(SupernaturalsPlugin.hasPermissions(SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()), permissions))
			if(delta<0)
				return;
		snplayer.setPower((snplayer.getPower() + delta));
	}
	
	public static void alterPower(SuperNPlayer snplayer, double delta, String reason){
		if(SupernaturalsPlugin.hasPermissions(SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()), permissions))
			if(delta<0)
				return;
		alterPower(snplayer, delta);
		SuperNManager.sendMessage(snplayer, "Power: "+ChatColor.WHITE+(int)snplayer.getPower()+ChatColor.RED+" ("+ChatColor.WHITE+(int)delta+ChatColor.RED+") "+reason);
	}
	
	public static void killEvent(SuperNPlayer damager, SuperNPlayer victim){
		if(victim==null){
			if(damager.isVampire()){
				alterPower(damager, SNConfigHandler.vampireKillPowerCreatureGain, "Creature death!");
			}else if(damager.isGhoul()){
				alterPower(damager, SNConfigHandler.ghoulKillPowerCreatureGain, "Creature death!");
			}else if(damager.isWere()){
				alterPower(damager, SNConfigHandler.wereKillPowerCreatureGain, "Creature death!");
			}else if(damager.isDemon()){
				alterPower(damager, SNConfigHandler.demonKillPowerCreatureGain, "Creature death!");
			}else if(damager.isHunter()){
				if(SNConfigHandler.hunterKillPowerCreatureGain>0)
					alterPower(damager, SNConfigHandler.hunterKillPowerCreatureGain, "Creature death!");
			}
		}else{
			double random = Math.random();
			if(damager.isVampire()){
				if(victim.getPower() > SNConfigHandler.vampireKillPowerPlayerGain){
					alterPower(damager, SNConfigHandler.vampireKillPowerPlayerGain, "Player killed!");
				}else{
					SuperNManager.sendMessage(damager, "You cannot gain power from a player with no power themselves.");
				}
				if(SNConfigHandler.vampireKillSpreadCurse && !victim.isSuper())
				{

					if(random<SNConfigHandler.spreadChance){
						SuperNManager.sendMessage(victim, "You feel your heart stop! You have contracted vampirism.");
						convert(victim, "vampire");
					}
				}
			}else if(damager.isGhoul()){
				if(victim.getPower() > SNConfigHandler.ghoulKillPowerPlayerGain){
					alterPower(damager, SNConfigHandler.ghoulKillPowerPlayerGain, "Player killed!");
				}else{
					SuperNManager.sendMessage(damager, "You cannot gain power from a player with no power themselves.");
				}
				if(SNConfigHandler.ghoulKillSpreadCurse && !victim.isSuper())
				{
					if(random<SNConfigHandler.spreadChance){
						SuperNManager.sendMessage(victim, "Your body dies... You feel a deep hatred for the living.");
						convert(victim, "ghoul");
					}
				}
			}else if(damager.isWere()){
				if(victim.getPower() > SNConfigHandler.wereKillPowerPlayerGain){
					alterPower(damager, SNConfigHandler.wereKillPowerPlayerGain, "Player killed!");
				}else{
					SuperNManager.sendMessage(damager, "You cannot gain power from a player with no power themselves.");
				}
				if(SNConfigHandler.wereKillSpreadCurse && !victim.isSuper() && worldTimeIsNight(SupernaturalsPlugin.instance.getServer().getPlayer(victim.getName())))
				{
					if(random<SNConfigHandler.spreadChance){
						SuperNManager.sendMessage(victim, "Your basic nature changes... You feel more in touch with your animal side.");
						convert(victim, "werewolf");
					}
				}
			}else if(damager.isDemon()){
				if(victim.getPower() > SNConfigHandler.demonKillPowerPlayerGain){
					alterPower(damager, SNConfigHandler.demonKillPowerPlayerGain, "Player killed!");
				}else{
					SuperNManager.sendMessage(damager, "You cannot gain power from a player with no power themselves.");
				}
			}else if(damager.isHunter()){
				if(victim.getPower() > SNConfigHandler.hunterKillPowerPlayerGain){
					alterPower(damager, SNConfigHandler.hunterKillPowerPlayerGain, "Player killed!");
					if(HunterManager.checkBounty(victim)){
						alterPower(damager, SNConfigHandler.hunterBountyCompletion, "Bounty Fulfilled!");
						HunterManager.removeBounty(victim);
						HunterManager.addBounty();
					}
				}else{
					SuperNManager.sendMessage(damager, "You cannot gain power from a player with no power themselves.");
				}
			}
		}
	}
	
	public static void deathEvent(Player player){
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log("Player died.");
		
		SuperNPlayer snplayer = get(player);
		Entity damager = null;
		EntityDamageEvent e = player.getLastDamageCause();
		
		if(!snplayer.isSuper()){
			if(snplayer.isPriest()){
				alterPower(snplayer, -SNConfigHandler.priestDeathPowerPenalty, "You died!");
			}else if(snplayer.isHunter()){
				alterPower(snplayer, -SNConfigHandler.hunterDeathPowerPenalty, "You died!");
			}else if(snplayer.isHuman()){
				SupernaturalsPlugin.instance.getHunterManager().removePlayerApp(player);
			}
			
			if(e==null)
				return;
			
			if(e.getCause().equals(DamageCause.LAVA) || e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.FIRE_TICK)){
				if(player.getWorld().getEnvironment().equals(Environment.NETHER)){
					if(SupernaturalsPlugin.instance.getDemonManager().checkPlayerApp(player)){
						sendMessage(snplayer, "Hellfire races through your veins!");
						convert(snplayer, "demon", SNConfigHandler.demonPowerStart);
					}
				}
			}
			
			if(e instanceof EntityDamageByEntityEvent){
				damager = ((EntityDamageByEntityEvent) e).getDamager();
			} else if(e instanceof EntityDamageByProjectileEvent){
				damager = ((EntityDamageByEntityEvent) e).getDamager();	
			}
			
			if(damager!=null){
				double random = Math.random();
				if(random<SNConfigHandler.spreadChance){
					if(player.getWorld().getEnvironment().equals(Environment.NETHER)){
						if(damager instanceof PigZombie){
							convert(snplayer, "ghoul", SNConfigHandler.ghoulPowerStart);
							sendMessage(snplayer, "You have been transformed into a Ghoul!");
						}
					}else if((damager instanceof Wolf)){
						if(!(((Wolf)damager).isTamed()) && worldTimeIsNight(player)){
							convert(snplayer, "werewolf", SNConfigHandler.werePowerStart);
							sendMessage(snplayer, "You have mutated into a werewolf!");
						}
					}
				}
			}
		}else{
			if(snplayer.isVampire()){
				alterPower(snplayer, -SNConfigHandler.vampireDeathPowerPenalty, "You died!");
			} else if(snplayer.isGhoul()){
				alterPower(snplayer, -SNConfigHandler.ghoulDeathPowerPenalty, "You died!");
			} else if(snplayer.isWere()){
				alterPower(snplayer, -SNConfigHandler.wereDeathPowerPenalty, "You died!");
			} else if(snplayer.isDemon()){
				alterPower(snplayer, -SNConfigHandler.demonDeathPowerPenalty, "You died!");
				if(e==null)
					return;
				if(e.getCause().equals(DamageCause.DROWNING) && player.getWorld().getTemperature(player.getLocation().getBlockX(), player.getLocation().getBlockZ())<0.6){
					if(snplayer.isDemon()){
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log("Demon drowned.  Checking inventory...");
						if(player.getInventory().contains(Material.SNOW_BALL, SNConfigHandler.demonSnowballAmount)){
							sendMessage(snplayer, "Your icy death has cooled the infernal fires raging within your body.");
							cure(snplayer);
							if(SNConfigHandler.debugMode)
								SupernaturalsPlugin.log("Snowballs found!");
						}
					}
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// 					Movement					//
	// -------------------------------------------- //
	
	public static boolean jump(Player player, double deltaSpeed, boolean upOnly) {
		SuperNPlayer snplayer = SuperNManager.get(player);
		
		if(upOnly){
			if(snplayer.getPower() - SNConfigHandler.jumpBloodCost <= 0) {
				SuperNManager.sendMessage(snplayer, "Not enough Power to jump.");
				return false;
			}else{
				SuperNManager.alterPower(snplayer, -SNConfigHandler.jumpBloodCost, "SuperJump!");
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName() + " used jump!");
			}
		}else{
			if(snplayer.getPower() - SNConfigHandler.dashBloodCost <= 0) {
				SuperNManager.sendMessage(snplayer, "Not enough Power to dash.");
				return false;
			}else{
				SuperNManager.alterPower(snplayer, -SNConfigHandler.dashBloodCost, "Dash!");
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName() + " used dash!");
			}
		}
		
		Vector vjadd;
		if (upOnly) {
			vjadd = new Vector(0, 1, 0);
		} else {
			Vector vhor = player.getLocation().getDirection();
			vjadd = new Vector(vhor.getX(),0,vhor.getZ());
			vjadd.normalize();
		}
		vjadd.multiply(deltaSpeed);
		
		player.setVelocity(player.getVelocity().add(vjadd));
		return true;
	}
	
	// -------------------------------------------- //
	// 		Monster Truce Feature (Passive)			//
	// -------------------------------------------- //
	
	public boolean truceIsBroken(SuperNPlayer snplayer) {
		return snplayer.getTruce();
	}
	
	public void truceBreak(SuperNPlayer snplayer) {
		if(!snplayer.isSuper()){
			snplayer.setTruce(true);
			return;
		}
		if(snplayer.getTruce()) {
			SuperNManager.sendMessage(snplayer, "You temporarily broke your truce with monsters!");
		}
		snplayer.setTruce(false);
		snplayer.setTruceTimer(SNConfigHandler.truceBreakTime);
	}
	
	public static void truceRestore(SuperNPlayer snplayer){
		SuperNManager.sendMessage(snplayer, "Your truce with monsters has been restored!");
		snplayer.setTruce(true);
		snplayer.setTruceTimer(0);
		
		// Untarget the player.
		Player player = SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName());
		for(LivingEntity entity : player.getWorld().getLivingEntities()){
			if(!(entity instanceof Creature)){
				continue;
			}
			
			if(snplayer.isVampire() && SNConfigHandler.vampireTruce.contains(EntityUtil.creatureTypeFromEntity(entity))){
				Creature creature = (Creature)entity;
				LivingEntity target = creature.getTarget();
				if((target != null && creature.getTarget().equals(player))){
					creature.setTarget(null);
				}
			} else if(snplayer.isGhoul() && SNConfigHandler.ghoulTruce.contains(EntityUtil.creatureTypeFromEntity(entity))){
				Creature creature = (Creature)entity;
				LivingEntity target = creature.getTarget();
				if((target != null && creature.getTarget().equals(player))){
					creature.setTarget(null);
				}
			} else if(snplayer.isWere() && SNConfigHandler.wolfTruce && EntityUtil.creatureNameFromEntity(entity).equalsIgnoreCase("wolf")){
				Creature creature = (Creature)entity;
				LivingEntity target = creature.getTarget();
				if((target != null && creature.getTarget().equals(player))){
					creature.setTarget(null);
				}
			}
		}
	}
	
	public void truceBreakAdvanceTime(SuperNPlayer snplayer, int milliseconds){
		if(snplayer.getTruce()){
			return;
		}
		
		this.truceBreakTimeLeftAlter(snplayer, -milliseconds);
	}
	
	public int truceBreakTimeLeftGet(SuperNPlayer snplayer){
		return snplayer.getTruceTimer();
	}
	
	private void truceBreakTimeLeftAlter(SuperNPlayer snplayer, int delta){
		if ((snplayer.getTruceTimer() + delta) < 0) {
			truceRestore(snplayer);
		} else {
			snplayer.setTruceTimer(snplayer.getTruceTimer() + delta);
		}
		SupernaturalsPlugin.saveData();
	}
	
	// -------------------------------------------- //
	// 			Regenerate Feature					//
	// -------------------------------------------- //
	
	public void regenAdvanceTime(Player player, int milliseconds){		
		if(player.isDead())
			return;
		
		SuperNPlayer snplayer = SuperNManager.get(player);
		int currentHealth = player.getHealth();
		
		if(currentHealth == 20){
			return;
		}
		
		double deltaSeconds = milliseconds/1000D;;
		double deltaHeal;
		
		if(snplayer.isVampire()){
			if(snplayer.getPower() <= SNConfigHandler.vampireHealthCost){
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log("Regen Event: Vampire player " + player.getName() + " not enough power!");
				return;
			}
			
			deltaHeal = deltaSeconds * SNConfigHandler.vampireTimeHealthGained;
			SuperNManager.alterPower(snplayer, -SNConfigHandler.vampireHealthCost, "Healing!");
			
		}else if(snplayer.isGhoul()){
			if(player.getWorld().hasStorm() && !plugin.getGhoulManager().isUnderRoof(player))
				return;
			deltaHeal = deltaSeconds * SNConfigHandler.ghoulHealthGained;
		}else{
			if(!worldTimeIsNight(player))
				return;
			deltaHeal = deltaSeconds * SNConfigHandler.wereHealthGained;
		}
		
		int healthDelta = (int)deltaHeal;
		int targetHealth = currentHealth + healthDelta;
		if(targetHealth > 20)
			targetHealth = 20;
		try{
			player.setHealth(targetHealth);
		}catch(IllegalArgumentException e){
			SupernaturalsPlugin.log("Attempted to regen dead player.");
		}
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Regen Event: player " + player.getName() + " gained " + healthDelta + " health.");
		}
	}
	
	// -------------------------------------------- //
	// 					Armor						//
	// -------------------------------------------- //
	
	public void armorCheck(Player player){
		PlayerInventory inv = player.getInventory();
		ItemStack helmet = inv.getHelmet();
		ItemStack chest = inv.getChestplate();
		ItemStack leggings = inv.getLeggings();
		ItemStack boots = inv.getBoots();
		
		SuperNPlayer snplayer = SuperNManager.get(player);
		
		if(snplayer.isPriest() || snplayer.isDemon()){
			if(helmet.getTypeId()!=0){
				inv.setHelmet(null);
				dropItem(player, helmet);
			}
			if(chest.getTypeId()!=0){
				inv.setChestplate(null);
				dropItem(player, chest);
			}
			if(leggings.getTypeId()!=0){
				inv.setLeggings(null);
				dropItem(player, leggings);
			}
			if(boots.getTypeId()!=0){
				inv.setBoots(null);
				dropItem(player, boots);
			}
		}else if(snplayer.isHunter()){
			if(!(SNConfigHandler.hunterArmor.contains(helmet.getType()))){
				inv.setHelmet(null);
				dropItem(player, helmet);
			}
			if(!(SNConfigHandler.hunterArmor.contains(chest.getType()))){
				inv.setChestplate(null);
				dropItem(player, chest);
			}
			if(!(SNConfigHandler.hunterArmor.contains(leggings.getType()))){
				inv.setLeggings(null);
				dropItem(player, leggings);
			}
			if(!(SNConfigHandler.hunterArmor.contains(boots.getType()))){
				inv.setBoots(null);
				dropItem(player, boots);
			}
		}
	}
	
	public void dropItem(Player player, ItemStack item){
		SuperNPlayer snplayer = SuperNManager.get(player);
		SuperNManager.sendMessage(snplayer, "Your class cannot wear this type of armor!");
		Item newItem = player.getWorld().dropItem(player.getLocation(), item);
		newItem.setItemStack(item);
	}
	
	// -------------------------------------------- //
	// 					Targetting					//
	// -------------------------------------------- //
	
	public Player getTarget(Player player){
		List<Block> blocks = player.getLineOfSight(SNConfigHandler.transparent, 20);
		List<Entity> entities = player.getNearbyEntities(21, 21, 21);
		for(Block block : blocks){
			for(Entity entity : entities){
				if(entity instanceof Player){
					Player victim = (Player) entity;
					Location location = victim.getLocation();
					Location feetLocation = new Location(location.getWorld(), location.getX(), location.getY()-1, location.getZ());
					Location groundLocation = new Location(location.getWorld(), location.getX(), location.getY()-2, location.getZ());
					if(location.getBlock().equals(block) || feetLocation.getBlock().equals(block) || groundLocation.getBlock().equals(block)){
						return victim;
					}
				}
			}
		}
		return null;
	}
	
	// -------------------------------------------- //
	// 					Messages					//
	// -------------------------------------------- //
	
	public static void sendMessage(SuperNPlayer snplayer, String message) {
		Player player = SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName());
		if(!player.isOnline())
			return;
		player.sendMessage(ChatColor.RED + message);
	}
	
	public static void sendMessage(SuperNPlayer snplayer, List<String> messages) {
		for(String message : messages) {
			SuperNManager.sendMessage(snplayer, message);
		}
	}
	
	public static void updateName(SuperNPlayer snplayer){
		Player player = SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName());
		String name = player.getName();
		String displayname = player.getDisplayName().trim();
		String updatedname;
		ChatColor color;
		
		if(snplayer.isPriest())
			color=ChatColor.GOLD;
		else if(snplayer.isVampire())
			color=ChatColor.DARK_PURPLE;
		else if(snplayer.isGhoul())
			color=ChatColor.DARK_GRAY;
		else if(snplayer.isWere())
			color=ChatColor.BLUE;
		else if(snplayer.isHunter())
			color=ChatColor.GREEN;
		else if(snplayer.isDemon())
			color=ChatColor.RED;
		else
			color=ChatColor.WHITE;
		
		if(displayname.contains("§."+name)){
			updatedname = displayname.replaceFirst(" §."+name, " "+color+name);
		} else
			updatedname = displayname.replaceFirst(name, color+name);
		
		if(SNConfigHandler.enableColors)
			player.setDisplayName(updatedname);
	}
	
	// -------------------------------------------- //
	// 					TimeKeeping					//
	// -------------------------------------------- //
	
	public static boolean worldTimeIsNight(Player player) {
		long time = player.getWorld().getTime() % 24000;
		
		if (time < 0 || time > 12400) 
			return true;
		
		return false; 
	}
	
	public static void startTimer(){
		timer = SupernaturalsPlugin.instance.getServer().getScheduler().scheduleSyncRepeatingTask(SupernaturalsPlugin.instance,new SNTaskTimer(SupernaturalsPlugin.instance),0,20);
		if(timer == -1)
			SupernaturalsPlugin.log(Level.WARNING,"Timer failed!");
	}
	
	public static void cancelTimer(){
		SupernaturalsPlugin.instance.getServer().getScheduler().cancelTask(timer);
	}
	
	public void advanceTime(SuperNPlayer snplayer) {
		Player player = plugin.getServer().getPlayer(snplayer.getName());
		
		if(SupernaturalsPlugin.hasPermissions(player, worldPermission) && SNConfigHandler.multiworld)
			return;
		
		taskCounter++;
		if(taskCounter>= 30){
			taskCounter = 0;
		}
		
		if(snplayer.isVampire()) {
			if(taskCounter%3==0){
				regenAdvanceTime(player, 3000);
			}
			if(taskCounter%3==0){
				plugin.getVampireManager().combustAdvanceTime(player, 3000);
				plugin.getVampireManager().gainPowerAdvanceTime(snplayer, 3000);
			}
		}else if(snplayer.isGhoul()){
			if(taskCounter%10==0){
				regenAdvanceTime(player, 10000);
			}
			plugin.getGhoulManager().waterAdvanceTime(player);
		}else if(snplayer.isWere()){
			if(taskCounter%5==0){
				regenAdvanceTime(player, 5000);
			}
		}else if(snplayer.isPriest() || snplayer.isDemon() || snplayer.isHunter()){
			armorCheck(player);
		}
		
		if(snplayer.isDemon()){
			if(taskCounter%5==0){
				plugin.getDemonManager().powerAdvanceTime(player, 5);
			}
		}
		
		if(snplayer.isSuper()){
			if(taskCounter%3==0){
				truceBreakAdvanceTime(snplayer, 3000);
			}
		}
		
	}
}
