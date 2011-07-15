package me.matterz.supernaturals.manager;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class GhoulManager {
	
private SupernaturalsPlugin plugin;
	
	public GhoulManager(SupernaturalsPlugin plugin) {
		this.plugin=plugin;
	}
	
	// -------------------------------------------- //
	// 					Movement					//
	// -------------------------------------------- //
	
	public void move(Player player){
		Vector vjadd = player.getLocation().getDirection();
		vjadd.normalize();
		vjadd.multiply(SNConfigHandler.ghoulMoveSpeed);
		
		player.setVelocity(player.getVelocity().add(vjadd));
	}
	
	// -------------------------------------------- //
	// 			Regenerate Feature					//
	// -------------------------------------------- //
	
	public void regenAdvanceTime(Player player, int milliseconds){
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
		double deltaHeal = deltaSeconds * SNConfigHandler.ghoulHealthGained;
		
		int healthDelta = (int)deltaHeal;
		int targetHealth = currentHealth + healthDelta;
		if(targetHealth > 20)
			targetHealth = 20;
		player.setHealth(targetHealth);
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Regen Event: player " + player.getName() + " gained " + healthDelta + " health.");
		}
	}
	
	// -------------------------------------------- //
	// 					Summonings					//
	// -------------------------------------------- //
	
	public void summon(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if((snplayer.getPower() > SNConfigHandler.ghoulPowerSummonMin)){
			player.getWorld().spawnCreature(player.getLocation(), CreatureType.PIG_ZOMBIE);
			plugin.getSuperManager().alterPower(snplayer, SNConfigHandler.ghoulPowerSummonCost, "Summoning PigZombie!");
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " summoned a PigZombie pet!");
		}
	}
}
