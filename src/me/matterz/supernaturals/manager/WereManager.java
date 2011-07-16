package me.matterz.supernaturals.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class WereManager {

private static HashMap<Wolf, SuperNPlayer> wolvesMap = new HashMap<Wolf, SuperNPlayer>();

	// -------------------------------------------- //
	// 					Summonings					//
	// -------------------------------------------- //
	
	public void summon(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if((snplayer.getPower() > SNConfigHandler.werePowerSummonMin) && SupernaturalManager.worldTimeIsNight(player)){
			Wolf wolf = (Wolf) player.getWorld().spawnCreature(player.getLocation(), CreatureType.WOLF);
			wolf.setTamed(true);
			wolf.setOwner(player);
			wolvesMap.put(wolf, snplayer);
			SupernaturalManager.alterPower(snplayer, -SNConfigHandler.werePowerSummonCost, "Summoning wolf!");
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " summoned a wolf pet!");
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to summon.");
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
	
	// -------------------------------------------- //
	// 			Regenerate Feature					//
	// -------------------------------------------- //
	
	public void regenAdvanceTime(Player player, int milliseconds){
		if(!SupernaturalManager.worldTimeIsNight(player)){
			return;
		}
		
		if(player.isDead())
			return;
		
		int currentHealth = player.getHealth();
		
		// Only regenerate if hurt.
		if(currentHealth == 20){
			return;
		}
		
		// Calculate blood and health deltas
		double deltaSeconds = milliseconds/1000D;
		double deltaHeal = deltaSeconds * SNConfigHandler.wereHealthGained;
		
		int healthDelta = (int)deltaHeal;
		int targetHealth = currentHealth + healthDelta;
		if(targetHealth > 20)
			targetHealth = 20;
		player.setHealth(targetHealth);
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Regen Were Event: player " + player.getName() + " gained " + healthDelta + " health.");
		}
	}
}
