package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;

public class SNPlayerListener extends PlayerListener{

	public static SupernaturalsPlugin plugin;
	
	public SNPlayerListener(SupernaturalsPlugin instance){
		SNPlayerListener.plugin = instance;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		Action action = event.getAction();
		
		if(action != Action.RIGHT_CLICK_AIR && event.isCancelled()){
			return;
		}
		
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		Material itemMaterial = event.getMaterial();
		
		
		if(action == Action.RIGHT_CLICK_AIR){
			if(SNConfigHandler.foodMaterials.contains(itemMaterial)){
				if(snplayer.isVampire())
				{
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " attempted to eat " + itemMaterial.toString());
					SupernaturalManager.sendMessage(snplayer, "Vampires can't eat food. You must drink blood instead.");
					event.setCancelled(true);
					return;
				}else if(snplayer.isWere()){
					if(itemMaterial.equals(Material.BREAD)){
						SupernaturalManager.sendMessage(snplayer, "Werewolves do not gain power from Bread.");
						return;
					}else{
						SupernaturalManager.alterPower(snplayer, SNConfigHandler.werePowerFood, "Eating!");
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log(snplayer.getName() + " ate " + itemMaterial.toString() + " to gain " + SNConfigHandler.werePowerFood + " power!");
						return;
					}
				}
			}
		}else if (action != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		if(blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Vampire Infect Altar.");
			plugin.getVampireManager().useAltarInfect(player, event.getClickedBlock());
		}else if(blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarCureMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Vampire Cure Altar.");
			plugin.getVampireManager().useAltarCure(player, event.getClickedBlock());
		}else if(blockMaterial == Material.getMaterial(SNConfigHandler.priestAltarMaterial)){
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Priest Altar.");
			plugin.getPriestManager().useAltar(player);
		}
	}
	
	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		if(event.isCancelled()){
			return;
		}
		if ((event.getLeaveMessage().contains("Flying")) || (event.getReason().contains("Flying"))) {
			SuperNPlayer snplayer = SupernaturalManager.get(event.getPlayer());
			if(snplayer.isVampire()&& event.getPlayer().getItemInHand().getType().toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
				event.setCancelled(true);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(event.getPlayer().getName() + " was not kicked for flying as a vampire.");
			} 
		}
	}
}
