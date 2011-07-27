package me.matterz.supernaturals.manager;

import org.bukkit.Material;
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
			SupernaturalManager.sendMessage(SupernaturalManager.get(player), "Ghouls disintegrate in water!  Get Out Quick!");
		}
	}
	
	// -------------------------------------------- //
	// 					Summonings					//
	// -------------------------------------------- //
	
	public boolean summon(Player player){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		ItemStack item = player.getItemInHand();
		if(!SupernaturalsPlugin.instance.getSpawn(player)){
			SupernaturalManager.sendMessage(snplayer, "You cannot summon here.");
			return false;
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
			return true;
		} else {
			SupernaturalManager.sendMessage(snplayer, "Not enough power to summon.");
			return false;
		}
	}
}
