package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
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
	
	public void deathEvent(SuperNPlayer snplayer){
		if(!snplayer.isHuman()){
			if(snplayer.isVampire()){
				this.alterPower(snplayer, -SNConfigHandler.vampireDeathPowerPenalty, "You died!");
			} else if(snplayer.isGhoul()){
				this.alterPower(snplayer, -SNConfigHandler.ghoulDeathPowerPenalty, "You died!");
			} else if(snplayer.isWere()){
				this.alterPower(snplayer, -SNConfigHandler.wereDeathPowerPenalty, "You died!");
			} else if(snplayer.isPriest()){
				this.alterPower(snplayer, -SNConfigHandler.priestDeathPowerPenalty, "You died!");
			}
		}
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
		
		if (snplayer.isVampire()) {
			if(taskCounter%10==0)
				plugin.getVampireManager().moveAdvanceTime(snplayer);
			if(((int)taskCounter)%30==0){
				plugin.getVampireManager().combustAdvanceTime(player, 3000);
				plugin.getVampireManager().truceBreakAdvanceTime(snplayer, 3000);
				plugin.getVampireManager().gainPowerAdvanceTime(snplayer, 3000);
			}else if(taskCounter==0){
				plugin.getVampireManager().regenAdvanceTime(player, 30000);
			}
		}
	}
	
	public boolean worldTimeIsNight(Player player) {
		long time = player.getWorld().getTime() % 24000;
		
		if (time < 0 || time > 12400) 
			return true;
		
		return false; 
	}
}
