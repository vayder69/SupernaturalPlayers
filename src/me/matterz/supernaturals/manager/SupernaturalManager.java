package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.World;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.EntityUtil;
import me.matterz.supernaturals.util.GeometryUtil;
import me.matterz.supernaturals.util.SuperNTaskTimer;

public class SupernaturalManager {

	private SupernaturalsPlugin plugin;
	private static List<SuperNPlayer> supernaturals = new ArrayList<SuperNPlayer>();
	
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
		String type = superType.toLowerCase();
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(type);
		snplayer.setPower(0);
		
		if(superType.equalsIgnoreCase("human")){
			snplayer.setSuper(false);
		}else{
			if(superType.equalsIgnoreCase("vampire"))
				snplayer.setVampire(true);
			else
				snplayer.setVampire(false);
		}
		
		SupernaturalManager.sendMessage(snplayer, "You are now a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " turned into a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		
		plugin.saveData();
	}
	
	public void curse(SuperNPlayer snplayer, String superType, int powerLevel) {
		String type = superType.toLowerCase();
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(type);
		snplayer.setPower(powerLevel);
		
		if(superType.equalsIgnoreCase("human")){
			snplayer.setSuper(false);
		}else{
			if(superType.equalsIgnoreCase("vampire"))
				snplayer.setVampire(true);
			else
				snplayer.setVampire(false);
		}
		
		SupernaturalManager.sendMessage(snplayer, "You are now a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " turned into a " + ChatColor.WHITE + superType + ChatColor.RED + "!");
		
		plugin.saveData();
	}
	
	public void cure(SuperNPlayer snplayer){
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType("human");
		snplayer.setPower(0);
		
		snplayer.setSuper(false);
		snplayer.setVampire(false);
		SupernaturalManager.sendMessage(snplayer, "You have been cured of any curses!");
		SupernaturalsPlugin.log(snplayer.getName() + " was cured of any curses!");
		plugin.saveData();
	}
	
	public void revert(SuperNPlayer snplayer){
		String oldType = snplayer.getOldType();
		double oldPower = snplayer.getOldPower();
		
		snplayer.setOldType(snplayer.getType());
		snplayer.setOldPower(snplayer.getPower());
		
		snplayer.setType(oldType);
		snplayer.setPower(oldPower);
		
		if(oldType.equalsIgnoreCase("human")){
			snplayer.setSuper(false);
		}else{
			if(oldType.equalsIgnoreCase("vampire"))
				snplayer.setVampire(true);
			else
				snplayer.setVampire(false);
		}
		
		SupernaturalManager.sendMessage(snplayer, "You been reverted to your previous state of being a " 
				+ ChatColor.WHITE + oldType + ChatColor.RED + "!");
		SupernaturalsPlugin.log(snplayer.getName() + " was reverted to the previous state of being a " 
				+ ChatColor.WHITE + oldType + ChatColor.RED + "!");
		plugin.saveData();
		
	}
	
	// -------------------------------------------- //
	// 					Power Altering				//
	// -------------------------------------------- //
	
	public static void alterPower(SuperNPlayer snplayer, double delta){
		snplayer.setPower(snplayer.getPower() + delta);
		SupernaturalsPlugin.instance.saveData();
	}
	
	public static void alterPower(SuperNPlayer snplayer, double delta, String reason){
		SupernaturalManager.alterPower(snplayer, delta);
		SupernaturalManager.sendMessage(snplayer, String.format("%1$.1f power %2$+.1f %3$s", snplayer.getPower(), delta, reason));
	}
	
	public void bloodDrink(SuperNPlayer snplayer, double amount, String source) {
		if (snplayer.getPower() == 10000D) {
			return;
		}
		SupernaturalManager.alterPower(snplayer, amount, String.format("from %s", source));
	}
	
	public void gainPowerAdvanceTime(SuperNPlayer snplayer){
		SupernaturalManager.alterPower(snplayer, SNConfigHandler.vampirePowerGainedOverTime);
		if(SNConfigHandler.debugMode)
			SupernaturalManager.sendMessage(snplayer, "Power gained over time");
	}
	
	// -------------------------------------------- //
	// 					Jump ability				//
	// -------------------------------------------- //
	
	public static void jump(Player player, double deltaSpeed, boolean upOnly) {
		
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		if (snplayer.getPower() - SNConfigHandler.jumpBloodCost <= 0) {
			SupernaturalManager.sendMessage(snplayer, "Not enough power to superjump.");
			return;
		}
		
		if(upOnly){
			alterPower(snplayer, -SNConfigHandler.jumpBloodCost, "SuperJump!");
		}else{
			alterPower(snplayer, -SNConfigHandler.jumpBloodCost, "Dash!");
		}
		
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
		
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		// The altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterialSurround), 
				SNConfigHandler.vampireAltarInfectMaterialRadius);
		if (count == 0) {
			return;
		}
		
		if (count < SNConfigHandler.vampireAltarInfectMaterialSurroundCount) {
			SupernaturalManager.sendMessage(snplayer, "Something happens... The gold draws energy from the obsidian... But there don't seem to be enough obsidian nearby.");
			return;
		}
		
		// Always examine first
		SupernaturalManager.sendMessage(snplayer, "This altar looks really evil.");
		
		// Is Vampire
		if (snplayer.isVampire()) {
			SupernaturalManager.sendMessage(snplayer, "This is of no use to you as you are already a vampire.");
			return;
		} else if (snplayer.isSuper()) {
			SupernaturalManager.sendMessage(snplayer, "This is of no use to you as you are already supernatural.");
			return;
		}
		
		// Is healthy and thus can be infected...
		if (SNConfigHandler.vampireAltarInfectRecipe.playerHasEnough(player)) {
			SupernaturalManager.sendMessage(snplayer, "You use these items on the altar:");
			SupernaturalManager.sendMessage(snplayer, SNConfigHandler.vampireAltarInfectRecipe.getRecipeLine());
			SupernaturalManager.sendMessage(snplayer, "The gold draws energy from the obsidian... The energy rushes through you and you feel a bitter cold...");
			SNConfigHandler.vampireAltarInfectRecipe.removeFromPlayer(player);
			this.curse(snplayer, "vampire", SNConfigHandler.vampireAltarInfectStartingPower);
		} else {
			SupernaturalManager.sendMessage(snplayer, "To use it you need to collect these ingredients:");
			SupernaturalManager.sendMessage(snplayer, SNConfigHandler.vampireAltarInfectRecipe.getRecipeLine());
		}
	}
	
	public void useAltarCure(Player player, Block centerBlock) {		
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		//Altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Material.getMaterial(SNConfigHandler.vampireAltarCureMaterialSurround), 
				SNConfigHandler.vampireAltarCureMaterialRadius);
		if (count == 0) {
			return;
		}
		
		if (count < SNConfigHandler.vampireAltarCureMaterialSurroundCount) {
			SupernaturalManager.sendMessage(snplayer, "Something happens... The lapiz draws energy from the glowstone... But there don't seem to be enough glowstone nearby.");
			return;
		}
		
		// Always examine first
		SupernaturalManager.sendMessage(snplayer, "This altar looks pure and clean.");
		
		// If healthy
		if ( !snplayer.isVampire()) {
			SupernaturalManager.sendMessage(snplayer, "It can probably cure curses, but you feel fine.");
			return;
		}
		
		// Is vampire and thus can be cured...
		else if(SNConfigHandler.vampireAltarCureRecipe.playerHasEnough(player))
		{
			SupernaturalManager.sendMessage(snplayer, "You use these items on the altar:");
			SupernaturalManager.sendMessage(snplayer, SNConfigHandler.vampireAltarCureRecipe.getRecipeLine());
			SupernaturalManager.sendMessage(snplayer, "The lapiz draws energy from the glowstone... Then the energy rushes through you and you feel pure and clean.");
			SNConfigHandler.vampireAltarCureRecipe.removeFromPlayer(player);
			this.cure(snplayer);
		}
		else
		{
			SupernaturalManager.sendMessage(snplayer, "To use it you need to collect these ingredients:");
			SupernaturalManager.sendMessage(snplayer, SNConfigHandler.vampireAltarCureRecipe.getRecipeLine());
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
	// 					Combustion					//
	// -------------------------------------------- //
	
	public boolean combustAdvanceTime(Player player, long milliseconds) {
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Combust Event with " + milliseconds + " milliseconds");
		}
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if (!this.standsInSunlight(player))
			return false;
		
		if(!SNConfigHandler.vampireBurnInSunlight)
			return false;
		
		// We assume the next tick will be in milliseconds.
		int ticksTillNext = (int) (milliseconds / 1000D * 20D); // 20 minecraft ticks is a second.
		ticksTillNext += 5; // just to be on the safe side.
		
		if (player.getFireTicks() <= 0){
			SupernaturalManager.sendMessage(snplayer, "Vampires burn in sunlight! Take cover!");
		}
		
		player.setFireTicks(ticksTillNext + SNConfigHandler.vampireCombustFireTicks);
		return true;
	}
	
	public boolean standsInSunlight(Player player){
		
		// No need to set on fire if the water will put the fire out at once.
		Material material = player.getLocation().getBlock().getType();
		World playerWorld = player.getWorld();
		
		if ((player.getWorld().getEnvironment() == Environment.NETHER) 
				|| this.worldTimeIsNight(player) || this.isUnderRoof(player) || material == Material.STATIONARY_WATER
				|| material == Material.WATER || playerWorld.hasStorm() || playerWorld.isThundering())
		{
			return false;
		}
		return true;
	}
	
	public boolean isUnderRoof(Player player) {
		/*
		We start checking opacity 2 blocks up.
		As Max Y is 127 there CAN be a roof over the player if he is standing in block 125:
		127 Solid Block
		126 
		125 Player
		However if he is standing in 126 there is no chance.
		*/
		boolean retVal = false;
		Block blockCurrent = player.getLocation().getBlock();

		if (player.getLocation().getY() >= 126)
		{
			retVal = false;
		}
		else
		{
			blockCurrent = blockCurrent.getFace(BlockFace.UP, 1);
				
			double opacityAccumulator = 0;
			Double opacity;
		
			while (blockCurrent.getY() + 1 <= 127) 
			{
				blockCurrent = blockCurrent.getRelative(BlockFace.UP);
			
				opacity = SNConfigHandler.materialOpacity.get(blockCurrent.getType());
				if (opacity == null) {
					retVal = true; // Blocks not in that map have opacity 1;
					break;
				}
			
				opacityAccumulator += opacity;
				if (opacityAccumulator >= 1.0D) {
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
	
	public boolean worldTimeIsNight(Player player) {
		long time = player.getWorld().getTime() % 24000;
		
		if (time < SNConfigHandler.vampireCombustFromTime || time > SNConfigHandler.vampireCombustToTime) 
			return true;
		
		return false; 
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
	// 					Timer						//
	// -------------------------------------------- //
	
	public void startTimer(){
		timer.schedule(new SuperNTaskTimer(plugin),0,SNConfigHandler.timerInterval);
	}
	
	public void cancelTimer(){
		timer.cancel();
	}
	
	public void advanceTime(SuperNPlayer snplayer, int milliseconds) {
		Player player = plugin.getServer().getPlayer(snplayer.getName());
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Advance Time with " + milliseconds + " milliseconds for " + snplayer.getName() + " " + player.getName());
		}
		if (snplayer.isSuper()){
			if (snplayer.isVampire()) {
				this.combustAdvanceTime(player, milliseconds);
				this.truceBreakAdvanceTime(snplayer, milliseconds);
				this.gainPowerAdvanceTime(snplayer);
			}
		}
	}
}
