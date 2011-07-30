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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Boat;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class GhoulManager {
	
	private String permissions = "supernatural.player.preventwaterdamage";
	
	// -------------------------------------------- //
	// 				Water Damage					//
	// -------------------------------------------- //
	
	public void waterAdvanceTime(Player player){
		if(player.isDead())
			return;
		if(SupernaturalsPlugin.hasPermissions(player, permissions))
			return;
		if(player.isInsideVehicle()){
			if(player.getVehicle() instanceof Boat){
				return;
			}
		}
		
		Material material = player.getLocation().getBlock().getType();
		
		if(material == Material.STATIONARY_WATER || material == Material.WATER){
			int health = (player.getHealth()-SNConfigHandler.ghoulDamageWater);
			if(health<0)
				health=0;
			player.setHealth(health);
			EntityDamageEvent event = new EntityDamageEvent(player, DamageCause.DROWNING, SNConfigHandler.ghoulDamageWater);
			player.setLastDamageCause(event);
			SuperNManager.sendMessage(SuperNManager.get(player), "Ghouls disintegrate in water!  Get Out Quick!");
		}
	}
	
	// -------------------------------------------- //
	// 					Summonings					//
	// -------------------------------------------- //
	
	public boolean summon(Player player){
		SuperNPlayer snplayer = SuperNManager.get(player);
		ItemStack item = player.getItemInHand();
		if(!SupernaturalsPlugin.instance.getSpawn(player)){
			SuperNManager.sendMessage(snplayer, "You cannot summon here.");
			return false;
		}
		if((snplayer.getPower() > SNConfigHandler.ghoulPowerSummonCost)){
			player.getWorld().spawnCreature(player.getLocation(), CreatureType.ZOMBIE);
			SuperNManager.alterPower(snplayer, -SNConfigHandler.ghoulPowerSummonCost, "Summoning a Zombie!");
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " summoned a Zombie!");
			if(item.getAmount()==1){
				player.setItemInHand(null);
			}else{
				item.setAmount(player.getItemInHand().getAmount()-1);
			}
			return true;
		} else {
			SuperNManager.sendMessage(snplayer, "Not enough power to summon.");
			return false;
		}
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
			while (blockCurrent.getY() + 1 <= 127) 
			{
				blockCurrent = blockCurrent.getRelative(BlockFace.UP);
			
				if(!blockCurrent.getType().equals(Material.AIR)){
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
}
