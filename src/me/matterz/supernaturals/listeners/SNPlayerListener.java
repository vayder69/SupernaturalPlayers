package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

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
		
		SuperNPlayer snplayer = plugin.getSuperManager().get(event.getPlayer());
		Material itemMaterial = event.getMaterial();
		
		if(snplayer.isVampire())
		{
			if(SNConfigHandler.foodMaterials.contains(itemMaterial.toString()))
			{
				plugin.getSuperManager().sendMessage(snplayer, "Vampires can't eat food. You must drink blood instead.");
				event.setCancelled(true);
				return;
			}
			
			if (SNConfigHandler.jumpMaterials.contains(event.getMaterial().toString())) 
			{
				plugin.getSuperManager().jump(event.getPlayer(), SNConfigHandler.jumpDeltaSpeed, false);
			}
		}
		
		if ( action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		if (blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterial)) {
			plugin.getSuperManager().useAltarInfect(event.getPlayer(), event.getClickedBlock());
		} else if (blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarCureMaterial)) {
			plugin.getSuperManager().useAltarCure(event.getPlayer(), event.getClickedBlock());
		}
	}
}
