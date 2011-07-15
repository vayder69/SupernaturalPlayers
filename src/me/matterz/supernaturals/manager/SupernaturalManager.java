package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.EntityUtil;
import me.matterz.supernaturals.util.SuperNTaskTimer;

public class SupernaturalManager {

	protected SupernaturalsPlugin plugin;
	
	private static List<SuperNPlayer> supernaturals = new ArrayList<SuperNPlayer>();
	private transient int taskCounter = 0;
	
	private Timer timer = new Timer();
	
	public SupernaturalManager(SupernaturalsPlugin plugin){
		this.plugin = plugin;
	}
	
	// -------------------------------------------- //
	// 				Data Management					//
	// -------------------------------------------- //
	
	public List<SuperNPlayer> getSupernaturals(){
		return supernaturals;
	}
	
	public void setSupernaturals(List<SuperNPlayer> supernaturals){
		SupernaturalManager.supernaturals = supernaturals;
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
			snplayers.add(SupernaturalManager.get(player));
		}
		return snplayers;
	}
	
	// -------------------------------------------- //
	// 			Supernatural Conversions			//
	// -------------------------------------------- //
	
	public void curse(SuperNPlayer snplayer, String superType) {
		this.curse(snplayer, superType, 0);
	}
	
	public void curse(SuperNPlayer snplayer, String superType, int powerLevel) {
		String type = superType.toLowerCase();
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(type);
		snplayer.setPower(powerLevel);
		
		snplayer.setTruce(true);
		snplayer.setMove(true);
		snplayer.setTruceTimer(0);
		
		SupernaturalManager.sendMessage(snplayer, "You are now a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " turned into a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		
		plugin.saveData();
	}
	
	public void cure(SuperNPlayer snplayer){
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType("human");
		snplayer.setPower(0);
		
		snplayer.setTruce(true);
		snplayer.setMove(true);
		snplayer.setTruceTimer(0);
		
		SupernaturalManager.sendMessage(snplayer, "You have been restored to humanity!");
		SupernaturalsPlugin.log(snplayer.getName() + " was restored to humanity!");
		plugin.saveData();
	}
	
	public void revert(SuperNPlayer snplayer){
		String oldType = snplayer.getOldType();
		double oldPower = snplayer.getOldPower();
		
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(oldType);
		snplayer.setPower(oldPower);
		
		snplayer.setTruce(true);
		snplayer.setMove(true);
		snplayer.setTruceTimer(0);
		
		SupernaturalManager.sendMessage(snplayer, "You been reverted to your previous state of being a " 
				+ ChatColor.WHITE + oldType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " was reverted to the previous state of being a " 
				+ ChatColor.WHITE + oldType + ChatColor.RED + "!");
		plugin.saveData();
		
	}
	
	// -------------------------------------------- //
	// 					Power Altering				//
	// -------------------------------------------- //
	
	public void alterPower(SuperNPlayer snplayer, double delta){
		snplayer.setPower((snplayer.getPower() + delta));
	}
	
	public void alterPower(SuperNPlayer snplayer, double delta, String reason){
		this.alterPower(snplayer, delta);
		SupernaturalManager.sendMessage(snplayer, "Power: "+ChatColor.WHITE+(int)snplayer.getPower()+ChatColor.RED+" ("+ChatColor.WHITE+(int)delta+ChatColor.RED+") "+reason);
	}
	
	public void bloodDrink(SuperNPlayer snplayer, double amount, String source) {
		if (snplayer.getPower() == 10000D) {
			return;
		}
		this.alterPower(snplayer, amount, String.format("from %s", source));
	}
	
	public void killEvent(SuperNPlayer damager, SuperNPlayer victim){
		if(victim==null){
			if(damager.isVampire()){
				this.alterPower(damager, SNConfigHandler.vampireKillPowerCreatureGain, "Creature death!");
			} else if(damager.isGhoul()){
				this.alterPower(damager, SNConfigHandler.ghoulKillPowerCreatureGain, "Creature death!");
			} else if(damager.isWere()){
				this.alterPower(damager, SNConfigHandler.wereKillPowerCreatureGain, "Creature death!");
			}
		}else{
			if(damager.isVampire()){
				this.alterPower(damager, SNConfigHandler.vampireKillPowerPlayerGain, "Player killed!");
				if(SNConfigHandler.vampireKillSpreadCurse && !victim.isSuper())
				{
					SupernaturalManager.sendMessage(victim, "You feel your heart stop! You have contracted vampirism.");
					this.curse(victim, "vampire");
				}
			} else if(damager.isGhoul()){
				this.alterPower(damager, SNConfigHandler.ghoulKillPowerPlayerGain, "Player killed!!");
				if(SNConfigHandler.ghoulKillSpreadCurse && !victim.isSuper())
				{
					SupernaturalManager.sendMessage(victim, "Your body dies... You feel a deep hatred for the living.");
					this.curse(victim, "ghoul");
				}
			} else if(damager.isWere()){
				this.alterPower(damager, SNConfigHandler.ghoulKillPowerPlayerGain, "Player killed!!");
				if(SNConfigHandler.wereKillSpreadCurse && !victim.isSuper())
				{
					SupernaturalManager.sendMessage(victim, "Your basic nature changes... You feel more in touch with your animal side.");
					this.curse(victim, "werewolf");
				}
			}
		}
	}
	
	public void deathEvent(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(!snplayer.isSuper()){
			if(snplayer.isPriest()){
				this.alterPower(snplayer, -SNConfigHandler.priestDeathPowerPenalty, "You died!");
			}
			
			Entity damager = null;
			Event e = player.getLastDamageCause();
			if(e instanceof EntityDamageByEntityEvent){
				damager = ((EntityDamageByEntityEvent) e).getDamager();
			} else if(e instanceof EntityDamageByProjectileEvent){
				damager = ((EntityDamageByEntityEvent) e).getDamager();	
			}
			
			if(damager!=null){
				double random = Math.random();
				if(random>0.5){
					if((damager instanceof Ghast) && player.getWorld().getEnvironment().equals(Environment.NETHER)){
						this.curse(snplayer, "ghoul", SNConfigHandler.ghoulPowerStart);
						SupernaturalManager.sendMessage(snplayer, "You have been transformed into a Ghoul!");
					}else if((damager instanceof Wolf)){
						if(!(((Wolf)damager).isTamed()) && this.worldTimeIsNight(player)){
							this.curse(snplayer, "werewolf", SNConfigHandler.werePowerStart);
							SupernaturalManager.sendMessage(snplayer, "You have mutated into a werewolf!");
						}
					}
				}
			}
		}else{
			if(snplayer.isVampire()){
				this.alterPower(snplayer, -SNConfigHandler.vampireDeathPowerPenalty, "You died!");
			} else if(snplayer.isGhoul()){
				this.alterPower(snplayer, -SNConfigHandler.ghoulDeathPowerPenalty, "You died!");
			} else if(snplayer.isWere()){
				this.alterPower(snplayer, -SNConfigHandler.wereDeathPowerPenalty, "You died!");
			}
		}
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
			SupernaturalManager.sendMessage(snplayer, "You temporarily broke your truce with monsters!");
		}
		snplayer.setTruce(false);
		snplayer.setTruceTimer(SNConfigHandler.truceBreakTime);
	}
	
	public void truceRestore(SuperNPlayer snplayer){
		SupernaturalManager.sendMessage(snplayer, "Your truce with monsters has been restored!");
		snplayer.setTruce(true);
		snplayer.setTruceTimer(0);
		
		// Untarget the player.
		Player player = plugin.getServer().getPlayer(snplayer.getName());
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
			this.truceRestore(snplayer);
		} else {
			snplayer.setTruceTimer(snplayer.getTruceTimer() + delta);
		}
		plugin.saveData();
	}
	
	// -------------------------------------------- //
	// 					Messages					//
	// -------------------------------------------- //
	
	public static void sendMessage(SuperNPlayer snplayer, String message) {
		Player player = SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName());
		player.sendMessage(ChatColor.RED + message);
	}
	
	public static void sendMessage(SuperNPlayer snplayer, List<String> messages) {
		for(String message : messages) {
			SupernaturalManager.sendMessage(snplayer, message);
		}
	}
	
	// -------------------------------------------- //
	// 					TimeKeeping					//
	// -------------------------------------------- //
	
	public boolean worldTimeIsNight(Player player) {
		long time = player.getWorld().getTime() % 24000;
		
		if (time < 0 || time > 12400) 
			return true;
		
		return false; 
	}
	
	public void startTimer(){
		timer.schedule(new SuperNTaskTimer(plugin),0,100);
	}
	
	public void cancelTimer(){
		timer.cancel();
	}
	
	public void advanceTime(SuperNPlayer snplayer, int milliseconds) {
		Player player = plugin.getServer().getPlayer(snplayer.getName());
		taskCounter++;
		if(taskCounter>= 300){
			taskCounter = 0;
		}
		
		if(snplayer.isVampire()) {
			if(taskCounter%10==0)
				plugin.getVampireManager().moveAdvanceTime(snplayer);
			if(taskCounter%30==0){
				plugin.getVampireManager().combustAdvanceTime(player, 3000);
				plugin.getVampireManager().gainPowerAdvanceTime(snplayer, 3000);
			}else if(taskCounter==0){
				plugin.getVampireManager().regenAdvanceTime(player, 30000);
			}
		}else if(snplayer.isGhoul()){
			if(taskCounter%30==0){
				plugin.getGhoulManager().regenAdvanceTime(player, 3000);
			}
		}else if(snplayer.isWere()){
			if(taskCounter%30==0){
				plugin.getGhoulManager().regenAdvanceTime(player, 3000);
			}
		}
		
		if(snplayer.isSuper()){
			if(taskCounter%30==0){
				this.truceBreakAdvanceTime(snplayer, 3000);
			}
		}
		
	}
}
