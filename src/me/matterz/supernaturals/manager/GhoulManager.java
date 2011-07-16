package me.matterz.supernaturals.manager;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class GhoulManager {
	
	// -------------------------------------------- //
	// 			Regenerate Feature					//
	// -------------------------------------------- //
	
	public void regenAdvanceTime(Player player, int milliseconds){
		if(player.isDead())
			return;
		
		int currentHealth = player.getHealth();
		
		// Only regenerate if hurt.
		if(currentHealth == 20){
			return;
		}
		
		// Calculate blood and health deltas
		double deltaSeconds = milliseconds/1000D;
		double deltaHeal = deltaSeconds * SNConfigHandler.ghoulHealthGained;
		
		int healthDelta = (int)deltaHeal;
		int targetHealth = currentHealth + healthDelta;
		if(targetHealth > 20)
			targetHealth = 20;
		player.setHealth(targetHealth);
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Regen Ghoul Event: player " + player.getName() + " gained " + healthDelta + " health.");
		}
	}
	
	// -------------------------------------------- //
	// 					Summonings					//
	// -------------------------------------------- //
	
	public void summon(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if((snplayer.getPower() > SNConfigHandler.ghoulPowerSummonMin)){
			player.getWorld().spawnCreature(player.getLocation(), CreatureType.ZOMBIE);
			SupernaturalManager.alterPower(snplayer, -SNConfigHandler.ghoulPowerSummonCost, "Summoning a Zombie!");
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " summoned a Zombie!");
		} else {
			SupernaturalManager.sendMessage(snplayer, "Not enough power to summon.");
		}
	}
}
