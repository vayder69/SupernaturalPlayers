package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.util.LightUtil;

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
			
			if(SNConfigHandler.jumpMaterials.contains(itemMaterial)){
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(snplayer.getName() + " used jump with " + itemMaterial.toString());
				SupernaturalManager.jump(event.getPlayer(), SNConfigHandler.jumpDeltaSpeed, false);
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
			plugin.getSuperManager().useAltarInfect(event.getPlayer(), event.getClickedBlock());
		} else if (blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarCureMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggerd a Vampire Cure Altar.");
			plugin.getSuperManager().useAltarCure(event.getPlayer(), event.getClickedBlock());
		}
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		SuperNPlayer snplayer = SupernaturalManager.get(event.getPlayer());
		if (!snplayer.isVampire()) {
			return;
		}
		
		if(event.getAnimationType() == PlayerAnimationType.ARM_SWING){
			
			if(SNConfigHandler.jumpMaterials.contains(event.getPlayer().getItemInHand().getType())){
				SupernaturalManager.jump(event.getPlayer(), SNConfigHandler.jumpDeltaSpeed, true);
				if(SNConfigHandler.debugMode){
					SupernaturalsPlugin.log(snplayer.getName() + " used jump with " + event.getPlayer().getItemInHand().getType().toString());
				}
				return;
			}
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		if(!snplayer.isPriest()){
			return;
		}
		
		Location newLocation = event.getTo();
		Location oldLocation = event.getFrom();
		
		if(newLocation.getBlockX()==oldLocation.getBlockX())
			if(newLocation.getBlockY()==oldLocation.getBlockY())
				if(newLocation.getBlockZ()==oldLocation.getBlockZ())
					return;
		
		LightUtil.illuminate(player,newLocation);
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
					SupernaturalsPlugin.log(event.getPlayer().getName() + " was not kicked for flying.");
			} 
		}	
	}
}
