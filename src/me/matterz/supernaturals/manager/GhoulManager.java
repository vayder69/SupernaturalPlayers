package me.matterz.supernaturals.manager;

import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class GhoulManager {
	
	// -------------------------------------------- //
	// 				Water Damage					//
	// -------------------------------------------- //
	
	public void waterAdvanceTime(Player player){
		if(player.isDead())
			return;
		Material material = player.getLocation().getBlock().getType();
		
		if(material == Material.STATIONARY_WATER || material == Material.WATER){
			int health = (player.getHealth()-SNConfigHandler.ghoulDamageWater);
			if(health<0)
				health=0;
			player.setHealth(health);
			SupernaturalManager.sendMessage(SupernaturalManager.get(player), "Ghouls disintegrate in water!  Get Out Quick!");
		}
	}
	
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
	
	public void summon(Player player, ItemStack item){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(!SupernaturalsPlugin.instance.getSpawn(player)){
			SupernaturalManager.sendMessage(snplayer, "You cannot summon here.");
			return;
		}
		if((snplayer.getPower() > SNConfigHandler.ghoulPowerSummonCost)){
			player.getWorld().spawnCreature(player.getLocation(), CreatureType.ZOMBIE);
			SupernaturalManager.alterPower(snplayer, -SNConfigHandler.ghoulPowerSummonCost, "Summoning a Zombie!");
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " summoned a Zombie!");
			if(item.getAmount()==1){
				player.setItemInHand(null);
			}else{
				item.setAmount(player.getItemInHand().getAmount()-1);
			}
		} else {
			SupernaturalManager.sendMessage(snplayer, "Not enough power to summon.");
		}
	}
}
