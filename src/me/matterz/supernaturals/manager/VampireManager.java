package me.matterz.supernaturals.manager;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.GeometryUtil;

public class VampireManager{

	// -------------------------------------------- //
	// 					Power Altering				//
	// -------------------------------------------- //
	
	public void gainPowerAdvanceTime(SuperNPlayer snplayer, int milliseconds){
		double deltaSeconds = milliseconds / 1000D;
		double deltaPower = deltaSeconds * SNConfigHandler.vampireTimePowerGained;
		SupernaturalManager.alterPower(snplayer, deltaPower);
	}
	
	// -------------------------------------------- //
	// 					Movement					//
	// -------------------------------------------- //
	
	public void teleport(Player player, ItemStack item){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(snplayer.getPower()>SNConfigHandler.vampireTeleportCost){
			SupernaturalManager.alterPower(snplayer, -SNConfigHandler.vampireTeleportCost, "Teleport!");
			player.teleport(SNConfigHandler.vampireTeleportLocation);
			if(item.getAmount()==1){
				player.setItemInHand(null);
			}else{
				item.setAmount(player.getItemInHand().getAmount()-1);
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to teleport.");
		}
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
			SupernaturalManager.curse(snplayer, "vampire", SNConfigHandler.vampirePowerStart);
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
		if (!snplayer.isVampire()) {
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
			SupernaturalManager.cure(snplayer);
		}
		else
		{
			SupernaturalManager.sendMessage(snplayer, "To use it you need to collect these ingredients:");
			SupernaturalManager.sendMessage(snplayer, SNConfigHandler.vampireAltarCureRecipe.getRecipeLine());
		}
	}
	
	// -------------------------------------------- //
	// 					Combustion					//
	// -------------------------------------------- //
	
	public boolean combustAdvanceTime(Player player, long milliseconds) {
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
		
		if ((player.getWorld().getEnvironment().equals(Environment.NETHER)) 
				|| SupernaturalManager.worldTimeIsNight(player) || this.isUnderRoof(player) || material == Material.STATIONARY_WATER
				|| material == Material.WATER || playerWorld.hasStorm())
		{
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
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
}
