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
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.EntityUtil;
import me.matterz.supernaturals.util.SuperNTaskTimer;

public class SupernaturalManager {

	private SupernaturalsPlugin plugin;
	
	private static List<SuperNPlayer> supernaturals = new ArrayList<SuperNPlayer>();
	private transient int taskCounter = 0;
	
	private Timer timer = new Timer();
	
	public SupernaturalManager(SupernaturalsPlugin plugin){
		this.plugin = plugin;
	}
	
	// -------------------------------------------- //
	// 				Data Management					//
	// -------------------------------------------- //
	
	public static List<SuperNPlayer> getSupernaturals(){
		return supernaturals;
	}
	
	public static void setSupernaturals(List<SuperNPlayer> supernaturals){
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
			snplayers.add(get(player));
		}
		return snplayers;
	}
	
	// -------------------------------------------- //
	// 			Supernatural Conversions			//
	// -------------------------------------------- //
	
	public static void curse(SuperNPlayer snplayer, String superType) {
		curse(snplayer, superType, 0);
	}
	
	public static void curse(SuperNPlayer snplayer, String superType, int powerLevel) {
		String type = superType.toLowerCase();
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(type);
		snplayer.setPower(powerLevel);
		
		snplayer.setTruce(true);
		
		SupernaturalManager.sendMessage(snplayer, "You are now a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " turned into a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		
		updateName(snplayer);
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
//		if(snplayer.getType().equalsIgnoreCase("hunter"))
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(true);
//		else
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(false);
		if(snplayer.getOldType().equals("werewolf"))
			WereManager.removePlayer(snplayer);
		
		SupernaturalManager.sendMessage(snplayer, "You have been restored to humanity!");
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
//		if(snplayer.getType().equalsIgnoreCase("hunter"))
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(true);
//		else
//			SupernaturalsPlugin.instance.getServer().getPlayer(snplayer.getName()).setSneaking(false);
		if(snplayer.getOldType().equals("werewolf"))
			WereManager.removePlayer(snplayer);
		
		SupernaturalManager.sendMessage(snplayer, "You been reverted to your previous state of being a " 
				+ ChatColor.WHITE + oldType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " was reverted to the previous state of being a " 
				+ ChatColor.WHITE + oldType + ChatColor.RED + "!");
		SupernaturalsPlugin.saveData();
		
	}
	
	// -------------------------------------------- //
	// 					Power Altering				//
	// -------------------------------------------- //
	
	public static void alterPower(SuperNPlayer snplayer, double delta){
		snplayer.setPower((snplayer.getPower() + delta));
	}
	
	public static void alterPower(SuperNPlayer snplayer, double delta, String reason){
		alterPower(snplayer, delta);
		SupernaturalManager.sendMessage(snplayer, "Power: "+ChatColor.WHITE+(int)snplayer.getPower()+ChatColor.RED+" ("+ChatColor.WHITE+(int)delta+ChatColor.RED+") "+reason);
	}
	
	public static void bloodDrink(SuperNPlayer snplayer, double amount, String source) {
		if (snplayer.getPower() == 10000D) {
			return;
		}
		alterPower(snplayer, amount, String.format("from %s", source));
	}
	
	public static void killEvent(SuperNPlayer damager, SuperNPlayer victim){
		if(victim==null){
			if(damager.isVampire()){
				alterPower(damager, SNConfigHandler.vampireKillPowerCreatureGain, "Creature death!");
			} else if(damager.isGhoul()){
				alterPower(damager, SNConfigHandler.ghoulKillPowerCreatureGain, "Creature death!");
			} else if(damager.isWere()){
				alterPower(damager, SNConfigHandler.wereKillPowerCreatureGain, "Creature death!");
			}
		}else{
			double random = Math.random();
			if(damager.isVampire()){
				alterPower(damager, SNConfigHandler.vampireKillPowerPlayerGain, "Player killed!");
				if(SNConfigHandler.vampireKillSpreadCurse && !victim.isSuper())
				{

					if(random<SNConfigHandler.spreadChance){
						SupernaturalManager.sendMessage(victim, "You feel your heart stop! You have contracted vampirism.");
						curse(victim, "vampire");
					}
				}
			} else if(damager.isGhoul()){
				alterPower(damager, SNConfigHandler.ghoulKillPowerPlayerGain, "Player killed!!");
				if(SNConfigHandler.ghoulKillSpreadCurse && !victim.isSuper())
				{
					if(random<SNConfigHandler.spreadChance){
						SupernaturalManager.sendMessage(victim, "Your body dies... You feel a deep hatred for the living.");
						curse(victim, "ghoul");
					}
				}
			} else if(damager.isWere()){
				alterPower(damager, SNConfigHandler.wereKillPowerPlayerGain, "Player killed!!");
				if(SNConfigHandler.wereKillSpreadCurse && !victim.isSuper() && worldTimeIsNight(SupernaturalsPlugin.instance.getServer().getPlayer(victim.getName())))
				{
					if(random<SNConfigHandler.spreadChance){
						SupernaturalManager.sendMessage(victim, "Your basic nature changes... You feel more in touch with your animal side.");
						curse(victim, "werewolf");
					}
				}
			}
		}
	}
	
	public static void deathEvent(Player player){
		SuperNPlayer snplayer = get(player);
		if(!snplayer.isSuper()){
			if(snplayer.isPriest()){
				alterPower(snplayer, -SNConfigHandler.priestDeathPowerPenalty, "You died!");
			}else if(snplayer.isHunter()){
				alterPower(snplayer, -SNConfigHandler.hunterDeathPowerPenalty, "You died!");
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
				if(random<SNConfigHandler.spreadChance){
					if((damager instanceof PigZombie) && player.getWorld().getEnvironment().equals(Environment.NETHER)){
						curse(snplayer, "ghoul", SNConfigHandler.ghoulPowerStart);
						SupernaturalManager.sendMessage(snplayer, "You have been transformed into a Ghoul!");
					}else if((damager instanceof Wolf)){
						if(!(((Wolf)damager).isTamed()) && worldTimeIsNight(player)){
							curse(snplayer, "werewolf", SNConfigHandler.werePowerStart);
							SupernaturalManager.sendMessage(snplayer, "You have mutated into a werewolf!");
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
			}
		}
	}
	
	// -------------------------------------------- //
	// 					Movement					//
	// -------------------------------------------- //
	
	public static boolean jump(Player player, double deltaSpeed, boolean upOnly) {
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		if(upOnly){
			if(snplayer.getPower() - SNConfigHandler.jumpBloodCost <= 0) {
				SupernaturalManager.sendMessage(snplayer, "Not enough Power to jump.");
				return false;
			}else{
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.jumpBloodCost, "SuperJump!");
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName() + " used jump!");
			}
		}else{
			if(snplayer.getPower() - SNConfigHandler.dashBloodCost <= 0) {
				SupernaturalManager.sendMessage(snplayer, "Not enough Power to dash.");
				return false;
			}else{
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.dashBloodCost, "Dash!");
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
			SupernaturalManager.sendMessage(snplayer, "You temporarily broke your truce with monsters!");
		}
		snplayer.setTruce(false);
		snplayer.setTruceTimer(SNConfigHandler.truceBreakTime);
	}
	
	public static void truceRestore(SuperNPlayer snplayer){
		SupernaturalManager.sendMessage(snplayer, "Your truce with monsters has been restored!");
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
		
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		int currentHealth = player.getHealth();
		
		if(currentHealth == 20){
			return;
		}
		
		double deltaSeconds = milliseconds/1000D;;
		double deltaHeal;
		double deltaBlood;
		
		if(snplayer.isVampire()){
			if(snplayer.getPower() <= SNConfigHandler.vampireHealthCost){
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log("Regen Event: Vampire player " + player.getName() + " not enough power!");
				return;
			}
			deltaHeal = deltaSeconds * SNConfigHandler.vampireTimeHealthGained;
			deltaBlood = -deltaHeal * SNConfigHandler.vampireHealthCost;
			
			if(snplayer.getPower() + deltaBlood <= SNConfigHandler.vampireHealthCost){
				deltaBlood = 0;
				deltaHeal = -deltaBlood / SNConfigHandler.vampireHealthCost;
			}
			SupernaturalManager.alterPower(snplayer, deltaBlood, "Healing!");
		}else if(snplayer.isGhoul()){
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
		
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
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
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SupernaturalManager.sendMessage(snplayer, "Your class cannot wear this type of armor!");
		Item newItem = player.getWorld().dropItem(player.getLocation(), item);
		newItem.setItemStack(item);
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
	
	public void startTimer(){
		timer.schedule(new SuperNTaskTimer(plugin),0,1000);
	}
	
	public void cancelTimer(){
		timer.cancel();
	}
	
	public void advanceTime(SuperNPlayer snplayer, int milliseconds) {
		Player player = plugin.getServer().getPlayer(snplayer.getName());
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
		
		if(snplayer.isSuper()){
			if(taskCounter%3==0){
				truceBreakAdvanceTime(snplayer, 3000);
			}
		}
		
	}
}
