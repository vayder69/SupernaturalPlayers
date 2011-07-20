package me.matterz.supernaturals.manager;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public class DemonManager {
	
	public void heal(Player player){
		if(player.isDead() || player.getHealth() == 20)
			return;
		
		int health = player.getHealth();
		health += SNConfigHandler.demonHealing;
		if(health>20)
			health=20;
		player.setHealth(health);
		player.setFireTicks(0);
		if(SNConfigHandler.debugMode)
			SupernaturalsPlugin.log(player.getName()+" was healed to "+health+" by fire");
	}
	
	public void fireball(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(snplayer.getPower() < SNConfigHandler.demonPowerFireball){
			SupernaturalManager.sendMessage(snplayer, "Not enough power to cast fireball!");
			return;
		}
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(player.getWorld(), 
				player.getLocation().getYaw(), player.getLocation().getPitch());
		Fireball fireball = player.getWorld().spawn(loc, Fireball.class);
		fireball.setShooter(player);
		SupernaturalManager.alterPower(SupernaturalManager.get(player), -SNConfigHandler.demonPowerFireball, "Fireball!");
	}

}
