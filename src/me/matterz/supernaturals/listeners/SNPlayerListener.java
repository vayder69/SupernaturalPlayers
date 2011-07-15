package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class SNPlayerListener extends PlayerListener{

	public static SupernaturalsPlugin plugin;
	
	public SNPlayerListener(SupernaturalsPlugin instance){
		SNPlayerListener.plugin = instance;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.isCancelled()){
			return;
		}
		
		Action action = event.getAction();
		
		if(!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)){
			return;
		}
	
		SuperNPlayer snplayer = SupernaturalManager.get(event.getPlayer());
		Material itemMaterial = event.getMaterial();
		
		if(snplayer.isVampire())
		{
			if(SNConfigHandler.foodMaterials.contains(itemMaterial)){
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName() + " attempted to eat " + itemMaterial.toString());
				SupernaturalManager.sendMessage(snplayer, "Vampires can't eat food. You must drink blood instead.");
				event.setCancelled(true);
				return;
			}
		}
		
		if ( action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		if (blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggerd a Vampire Infect Altar.");
			plugin.getVampireManager().useAltarInfect(event.getPlayer(), event.getClickedBlock());
		} else if (blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarCureMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggerd a Vampire Cure Altar.");
			plugin.getVampireManager().useAltarCure(event.getPlayer(), event.getClickedBlock());
		}
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		ItemStack item = player.getItemInHand();
		if(event.getAnimationType() == PlayerAnimationType.ARM_SWING){
			if (snplayer.isVampire()) {
				if(SNConfigHandler.jumpMaterials.contains(item.getType())){
					plugin.getVampireManager().jump(player, SNConfigHandler.jumpDeltaSpeed, true);
					return;
				}
			}else if(snplayer.isWere()){
				if(item.getType().toString().equals(SNConfigHandler.wolfMaterial)){
					plugin.getWereManager().summon(player);
				}
			}
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.isCancelled()){
			return;
		}
		Location oldLocation = event.getFrom();
		Location newLocation = event.getTo();
	
		
		if(oldLocation.getBlock().equals(newLocation.getBlock())){
			return;
		}
		
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		if(snplayer.isVampire()){
			if(SNConfigHandler.jumpMaterials.contains(event.getPlayer().getItemInHand().getType())){
				plugin.getVampireManager().jump(event.getPlayer(), SNConfigHandler.dashDeltaSpeed, false);
			}
		}
	}
	
	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		if(event.isCancelled()){
			return;
		}
		if ((event.getLeaveMessage().contains("Flying")) || (event.getReason().contains("Flying"))) {
			SuperNPlayer snplayer = SupernaturalManager.get(event.getPlayer());
			if(snplayer.isVampire()&& SNConfigHandler.jumpMaterials.contains(event.getPlayer().getItemInHand().getType())){
				event.setCancelled(true);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(event.getPlayer().getName() + " was not kicked for flying as a vampire.");
			} 
		}
	}
}
