package me.matterz.supernaturals.manager;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class WereManager {
	
private SupernaturalsPlugin plugin;
	
	public WereManager(SupernaturalsPlugin plugin) {
		this.plugin=plugin;
	}

	// -------------------------------------------- //
	// 					Summonings					//
	// -------------------------------------------- //
	
	public void summon(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if((snplayer.getPower() > SNConfigHandler.werePowerSummonMin) && plugin.getSuperManager().worldTimeIsNight(player)){
			Wolf wolf = (Wolf) player.getWorld().spawnCreature(player.getLocation(), CreatureType.WOLF);
			wolf.setTamed(true);
			wolf.setOwner(player);
			plugin.getSuperManager().alterPower(snplayer, SNConfigHandler.werePowerSummonCost, "Summoning wolf!");
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " summoned a wolf pet!");
		}
	}
	
	// -------------------------------------------- //
	// 			Regenerate Feature					//
	// -------------------------------------------- //
	
	public void regenAdvanceTime(Player player, int milliseconds){
		if(!plugin.getSuperManager().worldTimeIsNight(player)){
			return;
		}
		
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Regen Event: player " + player.getName());
		}
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
			SupernaturalsPlugin.log("Regen Event: player " + player.getName() + " gained " + healthDelta + " health.");
		}
	}
}
