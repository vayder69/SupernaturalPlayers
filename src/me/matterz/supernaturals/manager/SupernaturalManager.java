package me.matterz.supernaturals.manager;

import java.util.List;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.EntityUtil;
import me.matterz.supernaturals.util.GeometryUtil;
import me.matterz.supernaturals.util.SuperNTaskTimer;

public class SupernaturalManager {

	private SupernaturalsPlugin plugin;
	private static List<SuperNPlayer> supernaturals;
	private static List<SuperNPlayer> onlineSupers;
	
	private Timer timer;
	
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
	
	public SuperNPlayer get(String playername){
		for(SuperNPlayer supernatural : supernaturals){
			if(supernatural.getName()==playername){
				return supernatural;
			}
		}
		
		SuperNPlayer snplayer = new SuperNPlayer(playername,false,0,true,0);
		supernaturals.add(snplayer);
		return snplayer;
	}
	
	public SuperNPlayer get(Player player){
		for(SuperNPlayer supernatural : supernaturals){
			if(supernatural.getName()==player.getName()){
				return supernatural;
			}
		}
		
		SuperNPlayer snplayer = new SuperNPlayer(player.getName(),false,0,true,0);
		supernaturals.add(snplayer);
		return snplayer;
	}	
	public static List<SuperNPlayer> findAllOnline() {
		for(SuperNPlayer supernatural : supernaturals){
			if(!onlineSupers.contains(supernatural))
				onlineSupers.add(supernatural);
		}
		return onlineSupers;
	}
	
	// -------------------------------------------- //
	// 			Supernatural Conversions			//
	// -------------------------------------------- //
	
	public void turn(SuperNPlayer snplayer) {
		snplayer.setVampire(true);
		
		this.sendMessage(snplayer, "You are now a vampire!");
		SupernaturalsPlugin.log(snplayer.getName() + " turned into a vampire.");
		
		plugin.saveData();
	}
	
	public void cure(SuperNPlayer snplayer) {
		snplayer.setVampire(false);
		this.sendMessage(snplayer, "You have been cured from the vampirism!");
		SupernaturalsPlugin.log(snplayer.getName() + " was cured and is no longer a vampire.");
		plugin.saveData();
	}
	
	// -------------------------------------------- //
	// 					Power Altering				//
	// -------------------------------------------- //
	
	public void alterPower(SuperNPlayer snplayer, double delta){
		snplayer.setPower(snplayer.getPower() + delta);
	}
	
	public void alterPower(SuperNPlayer snplayer, double delta, String reason){
		this.alterPower(snplayer, delta);
		this.sendMessage(snplayer, String.format("%1$.1f blood %2$+.1f %3$s", snplayer.getPower(), delta, reason));
	}
	
	public void bloodDrink(SuperNPlayer snplayer, double amount, String source) {
		if (snplayer.getPower() == 10000D) {
			return;
		}
		this.alterPower(snplayer, amount, String.format("from %s", source));
	}
	
	// -------------------------------------------- //
	// 					Jump ability				//
	// -------------------------------------------- //
	
	public void jump(Player player, double deltaSpeed, boolean upOnly) {
		
		SuperNPlayer snplayer = this.get(player);
		
		if (snplayer.getPower() - SNConfigHandler.jumpBloodCost <= 0) {
			this.sendMessage(snplayer, "Not enough blood to jump.");
			return;
		}
		
		alterPower(snplayer, -SNConfigHandler.jumpBloodCost, "Jump!");
		
		Vector vjadd;
		if (upOnly) {
			vjadd = new Vector(0, 1, 0);
		} else {
			vjadd = player.getLocation().getDirection();
			vjadd.normalize();
		}
		vjadd.multiply(deltaSpeed);
		vjadd.setY(vjadd.getY() / 2.5D); // Compensates for the "in air friction" that not applies to y-axis.
		
		player.setVelocity(player.getVelocity().add(vjadd));
	}
	
	// -------------------------------------------- //
	// 				Altar Usage						//
	// -------------------------------------------- //
	
	public void useAltarInfect(Player player, Block centerBlock) {
		
		SuperNPlayer snplayer = this.get(player);
		
		// The altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterialSurround), 
				SNConfigHandler.vampireAltarInfectMaterialRadius);
		if (count == 0) {
			return;
		}
		
		if (count < SNConfigHandler.vampireAltarInfectMaterialSurroundCount) {
			this.sendMessage(snplayer, "Something happens... The gold draws energy from the obsidian... But there don't seem to be enough obsidian nearby.");
			return;
		}
		
		// Always examine first
		this.sendMessage(snplayer, "This altar looks really evil.");
		
		// Is Vampire
		if (snplayer.isVampire()) {
			this.sendMessage(snplayer, "This is of no use to you as you are already a vampire.");
			return;
		}
		
		// Is healthy and thus can be infected...
		if (SNConfigHandler.vampireAltarInfectRecipe.playerHasEnough(player)) {
			this.sendMessage(snplayer, "You use these items on the altar:");
			this.sendMessage(snplayer, SNConfigHandler.vampireAltarInfectRecipe.getRecipeLine());
			this.sendMessage(snplayer, "The gold draws energy from the obsidian... The energy rushes through you and you feel a bitter cold...");
			SNConfigHandler.vampireAltarInfectRecipe.removeFromPlayer(player);
			this.turn(snplayer);
		} else {
			this.sendMessage(snplayer, "To use it you need to collect these ingredients:");
			this.sendMessage(snplayer, SNConfigHandler.vampireAltarInfectRecipe.getRecipeLine());
		}
	}
	
	public void useAltarCure(Player player, Block centerBlock) {		
		SuperNPlayer snplayer = this.get(player);
		
		//Altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Material.getMaterial(SNConfigHandler.vampireAltarCureMaterialSurround), 
				SNConfigHandler.vampireAltarCureMaterialRadius);
		if (count == 0) {
			return;
		}
		
		if (count < SNConfigHandler.vampireAltarCureMaterialSurroundCount) {
			this.sendMessage(snplayer, "Something happens... The lapiz draws energy from the glowstone... But there don't seem to be enough glowstone nearby.");
			return;
		}
		
		// Always examine first
		this.sendMessage(snplayer, "This altar looks pure and clean.");
		
		// If healthy
		if ( !snplayer.isVampire()) {
			this.sendMessage(snplayer, "It can probably cure curses, but you feel fine.");
			return;
		}
		
		// Is vampire and thus can be cured...
		else if(SNConfigHandler.vampireAltarCureRecipe.playerHasEnough(player))
		{
			this.sendMessage(snplayer, "You use these items on the altar:");
			this.sendMessage(snplayer, SNConfigHandler.vampireAltarCureRecipe.getRecipeLine());
			this.sendMessage(snplayer, "The lapiz draws energy from the glowstone... Then the energy rushes through you and you feel pure and clean.");
			SNConfigHandler.vampireAltarCureRecipe.removeFromPlayer(player);
			this.cure(snplayer);
		}
		else
		{
			this.sendMessage(snplayer, "To use it you need to collect these ingredients:");
			this.sendMessage(snplayer, SNConfigHandler.vampireAltarCureRecipe.getRecipeLine());
		}
	}
	
	// -------------------------------------------- //
	// 				Every Tick						//
	// -------------------------------------------- //
	
	public void advanceTime(SuperNPlayer snplayer, int milliseconds) {
		if (snplayer.isVampire()) {
			this.truceBreakAdvanceTime(snplayer, milliseconds);
		}
	}
	
	// -------------------------------------------- //
	// 		Monster Truce Feature (Passive)			//
	// -------------------------------------------- //
	
	public boolean truceIsBroken(SuperNPlayer snplayer) {
		return snplayer.getTruce();
	}
	
	public void truceBreak(SuperNPlayer snplayer) {
		if(snplayer.getTruce()) {
			this.sendMessage(snplayer, "You temporarily broke your truce with monsters!");
		}
		snplayer.setTruce(false);
		snplayer.setTruceTimer(SNConfigHandler.truceBreakTime);
	}
	
	public void truceRestore(SuperNPlayer snplayer){
		this.sendMessage(snplayer, "Your truce with monsters has been restored!");
		snplayer.setTruce(true);
		snplayer.setTruceTimer(0);
		
		// Untarget the player.
		Player player = plugin.getServer().getPlayer(snplayer.getName());
		for(LivingEntity entity : player.getWorld().getLivingEntities()){
			if(!(entity instanceof Creature)){
				continue;
			}
			if(!SNConfigHandler.vampireTruce.contains(EntityUtil.creatureTypeFromEntity(entity))){
				continue;
			}
			
			Creature creature = (Creature)entity;
			LivingEntity target = creature.getTarget();
			
			if(!(target != null && creature.getTarget().equals(player))){
				continue;
			}
			creature.setTarget(null);
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
	
	private void truceBreakTimeLeftSet(SuperNPlayer snplayer, int milliseconds){
		if (milliseconds < 0) {
			this.truceRestore(snplayer);
		} else {
			snplayer.setTruceTimer(milliseconds);
		}
		plugin.saveData();
	}
	
	private void truceBreakTimeLeftAlter(SuperNPlayer snplayer, int delta){
		this.truceBreakTimeLeftSet(snplayer, snplayer.getTruceTimer() + delta);
	}
	
	// -------------------------------------------- //
	// 					Messages					//
	// -------------------------------------------- //
	
	public void sendMessage(SuperNPlayer snplayer, String message) {
		Player player = plugin.getServer().getPlayer(snplayer.getName());
		player.sendMessage(ChatColor.RED + message);
	}
	
	public void sendMessage(SuperNPlayer snplayer, List<String> messages) {
		for(String message : messages) {
			this.sendMessage(snplayer, message);
		}
	}
	
	// -------------------------------------------- //
	// 					Timer						//
	// -------------------------------------------- //
	
	public void startTimer(){
		timer.schedule(new SuperNTaskTimer(plugin), 0, //initial delay
		        SNConfigHandler.timerInterval); //subsequent rate
	}
	
	public void cancelTimer(){
		timer.cancel();
	}
}
