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
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class WereManager {

private static HashMap<Wolf, SuperNPlayer> wolvesMap = new HashMap<Wolf, SuperNPlayer>();

	// -------------------------------------------- //
	// 					Wolfbane					//
	// -------------------------------------------- //

	public boolean wolfbane(Player player){
		SuperNPlayer snplayer = SuperNManager.get(player);
		if(SNConfigHandler.wereWolfbaneRecipe.playerHasEnough(player)) {
			SuperNManager.sendMessage(snplayer, "You create a wolfbane potion!");
			SuperNManager.sendMessage(snplayer, SNConfigHandler.wereWolfbaneRecipe.getRecipeLine());
			SNConfigHandler.wereWolfbaneRecipe.removeFromPlayer(player);
			SuperNManager.cure(snplayer);
			return true;
		}else{
			SuperNManager.sendMessage(snplayer, "You cannot create a Wolfbane potion without the following: ");
			SuperNManager.sendMessage(snplayer, SNConfigHandler.wereWolfbaneRecipe.getRecipeLine());
			return false;
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
		if(SuperNManager.worldTimeIsNight(player)){
			if(snplayer.getPower() >= SNConfigHandler.werePowerSummonCost){
				int i = 0;
				for(Wolf wolf : wolvesMap.keySet()){
					if(wolvesMap.get(wolf).equals(snplayer)){
						i++;
					}
				}
				if(i<=4){
					Wolf wolf = (Wolf) player.getWorld().spawnCreature(player.getLocation(), CreatureType.WOLF);
					wolf.setTamed(true);
					wolf.setOwner(player);
					wolf.setHealth(20);
					wolvesMap.put(wolf, snplayer);
					SuperNManager.alterPower(snplayer, -SNConfigHandler.werePowerSummonCost, "Summoning wolf!");
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " summoned a wolf pet!");
					if(item.getAmount()==1){
						player.setItemInHand(null);
					}else{
						item.setAmount(player.getItemInHand().getAmount()-1);
					}
					return true;
				}else{
					SuperNManager.sendMessage(snplayer, "You already have all the wolves you can control.");
					return false;
				}
			}else{
				SuperNManager.sendMessage(snplayer, "Not enough power to summon.");
				return false;
			}
		}else{
			SuperNManager.sendMessage(snplayer, "Cannot use werewolf abilities during the day!");
			return false;
		}
	}
	
	public static HashMap<Wolf, SuperNPlayer> getWolves(){
		return wolvesMap;
	}
	
	public static void removeWolf(Wolf wolf){
		if(wolvesMap.containsKey(wolf)){
			wolvesMap.remove(wolf);
		}
	}
	
	public static void removePlayer(SuperNPlayer player){
		List<Wolf> removeWolf = new ArrayList<Wolf>();
		for(Wolf wolf : wolvesMap.keySet()){
			if(wolvesMap.get(wolf).equals(player)){
				wolf.setTamed(false);
				removeWolf.add(wolf);
			}
		}
		for(Wolf wolf : removeWolf){
			wolvesMap.remove(wolf);
		}
	}
}
