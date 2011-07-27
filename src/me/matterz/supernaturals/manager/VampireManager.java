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

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.util.GeometryUtil;

public class VampireManager{
	
	private String permissions = "supernatural.player.preventsundamage";

	// -------------------------------------------- //
	// 					Power Altering				//
	// -------------------------------------------- //
	
	public void gainPowerAdvanceTime(SuperNPlayer snplayer, int milliseconds){
		double deltaSeconds = milliseconds / 1000D;
		double deltaPower = deltaSeconds * SNConfigHandler.vampireTimePowerGained;
		SuperNManager.alterPower(snplayer, deltaPower);
	}
	
	// -------------------------------------------- //
	// 					Movement					//
	// -------------------------------------------- //
	
	public boolean teleport(Player player){
		SuperNPlayer snplayer = SuperNManager.get(player);
		ItemStack item = player.getItemInHand();
		if(snplayer.getPower()>SNConfigHandler.vampireTeleportCost){
			SuperNManager.alterPower(snplayer, -SNConfigHandler.vampireTeleportCost, "Teleport!");
			player.teleport(SNConfigHandler.vampireTeleportLocation);
			if(item.getAmount()==1){
				player.setItemInHand(null);
			}else{
				item.setAmount(player.getItemInHand().getAmount()-1);
			}
			return true;
		}else{
			SuperNManager.sendMessage(snplayer, "Not enough power to teleport.");
			return false;
		}
	}
	
	// -------------------------------------------- //
	// 				Altar Usage						//
	// -------------------------------------------- //
	
	public void useAltarInfect(Player player, Block centerBlock) {

		SuperNPlayer snplayer = SuperNManager.get(player);

		// The altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterialSurround), 
				SNConfigHandler.vampireAltarInfectMaterialRadius);
		if (count == 0) {
			return;
		}

		if (count < SNConfigHandler.vampireAltarInfectMaterialSurroundCount) {
			SuperNManager.sendMessage(snplayer, "Something happens... The "
					+SNConfigHandler.vampireAltarInfectMaterial.toLowerCase()
					+" draws energy from the "+SNConfigHandler.vampireAltarInfectMaterialSurround.toLowerCase()
					+"... But there doesn't seem to be enough "+SNConfigHandler.vampireAltarInfectMaterialSurround.toLowerCase()
					+" nearby.");
			return;
		}

		// Always examine first
		SuperNManager.sendMessage(snplayer, "This altar looks really evil.");

		// Is Vampire
		if (snplayer.isVampire()) {
			SuperNManager.sendMessage(snplayer, "This is of no use to you as you are already a vampire.");
			return;
		} else if (snplayer.isSuper()) {
			SuperNManager.sendMessage(snplayer, "This is of no use to you as you are already supernatural.");
			return;
		}

		// Is healthy and thus can be infected...
		if (SNConfigHandler.vampireAltarInfectRecipe.playerHasEnough(player)) {
			SuperNManager.sendMessage(snplayer, "You use these items on the altar:");
			SuperNManager.sendMessage(snplayer, SNConfigHandler.vampireAltarInfectRecipe.getRecipeLine());
			SuperNManager.sendMessage(snplayer, "The "
					+SNConfigHandler.vampireAltarInfectMaterial.toLowerCase()
					+" draws energy from the "+SNConfigHandler.vampireAltarInfectMaterialSurround.toLowerCase()
					+"... The energy rushes through you and you feel a bitter cold...");
			SNConfigHandler.vampireAltarInfectRecipe.removeFromPlayer(player);
			SuperNManager.curse(snplayer, "vampire", SNConfigHandler.vampirePowerStart);
		} else {
			SuperNManager.sendMessage(snplayer, "To use it you need to collect these ingredients:");
			SuperNManager.sendMessage(snplayer, SNConfigHandler.vampireAltarInfectRecipe.getRecipeLine());
		}
	}

	public void useAltarCure(Player player, Block centerBlock) {		
		SuperNPlayer snplayer = SuperNManager.get(player);

		//Altar must be big enough
		int count = GeometryUtil.countNearby(centerBlock, Material.getMaterial(SNConfigHandler.vampireAltarCureMaterialSurround), 
				SNConfigHandler.vampireAltarCureMaterialRadius);
		if (count == 0) {
			return;
		}

		if (count < SNConfigHandler.vampireAltarCureMaterialSurroundCount) {
			SuperNManager.sendMessage(snplayer, "Something happens... The "
					+SNConfigHandler.vampireAltarCureMaterial.toLowerCase()
					+" draws energy from the "+SNConfigHandler.vampireAltarCureMaterialSurround.toLowerCase()
					+"... But there doesn't seem to be enough "+SNConfigHandler.vampireAltarCureMaterialSurround.toLowerCase()
					+" nearby.");
			return;
		}

		// Always examine first
		SuperNManager.sendMessage(snplayer, "This altar looks pure and clean.");

		// If healthy
		if (!snplayer.isVampire()) {
			SuperNManager.sendMessage(snplayer, "It can probably cure curses, but you feel fine.");
			return;
		}

		// Is vampire and thus can be cured...
		else if(SNConfigHandler.vampireAltarCureRecipe.playerHasEnough(player))
		{
			SuperNManager.sendMessage(snplayer, "You use these items on the altar:");
			SuperNManager.sendMessage(snplayer, SNConfigHandler.vampireAltarCureRecipe.getRecipeLine());
			SuperNManager.sendMessage(snplayer, "The "
					+SNConfigHandler.vampireAltarCureMaterial.toLowerCase()
					+" draws energy from the "+SNConfigHandler.vampireAltarCureMaterialSurround.toLowerCase()
					+"... Then the energy rushes through you and you feel pure and clean.");
			SNConfigHandler.vampireAltarCureRecipe.removeFromPlayer(player);
			SuperNManager.cure(snplayer);
		}
		else
		{
			SuperNManager.sendMessage(snplayer, "To use it you need to collect these ingredients:");
			SuperNManager.sendMessage(snplayer, SNConfigHandler.vampireAltarCureRecipe.getRecipeLine());
		}
	}
	
	// -------------------------------------------- //
	// 					Combustion					//
	// -------------------------------------------- //
	
	public boolean combustAdvanceTime(Player player, long milliseconds) {
		SuperNPlayer snplayer = SuperNManager.get(player);
		if (!this.standsInSunlight(player))
			return false;
		
		if(!SNConfigHandler.vampireBurnInSunlight)
			return false;
		
		// We assume the next tick will be in milliseconds.
		int ticksTillNext = (int) (milliseconds / 1000D * 20D); // 20 minecraft ticks is a second.
		ticksTillNext += 5; // just to be on the safe side.
		
		if (player.getFireTicks() <= 0 && SNConfigHandler.vampireBurnMessageEnabled){
			SuperNManager.sendMessage(snplayer, "Vampires burn in sunlight! Take cover!");
		}
		
		player.setFireTicks(ticksTillNext + SNConfigHandler.vampireCombustFireTicks);
		return true;
	}
	
	public boolean standsInSunlight(Player player){
		// No need to set on fire if the water will put the fire out at once.
		Material material = player.getLocation().getBlock().getType();
		World playerWorld = player.getWorld();
		
		if(SupernaturalsPlugin.hasPermissions(player, permissions))
			return false;
		
		if ((player.getWorld().getEnvironment().equals(Environment.NETHER)) 
				|| SuperNManager.worldTimeIsNight(player) || this.isUnderRoof(player) || material == Material.STATIONARY_WATER
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
