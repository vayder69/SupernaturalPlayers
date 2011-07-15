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

	public void summon(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(snplayer.getPower() > SNConfigHandler.werePowerSummonMin){
			Wolf wolf = (Wolf) player.getWorld().spawnCreature(player.getLocation(), CreatureType.WOLF);
			wolf.setTamed(true);
			wolf.setOwner(player);
			plugin.getSuperManager().alterPower(snplayer, SNConfigHandler.werePowerSummonCost, "Summoning wolf!");
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " summoned a wolf pet!");
		}
	}
}
