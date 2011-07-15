package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SupernaturalsPlugin;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SNPlayerMonitor extends PlayerListener {
	
public static SupernaturalsPlugin plugin;
	
	public SNPlayerMonitor(SupernaturalsPlugin instance){
		SNPlayerListener.plugin = instance;
	}

	public void onPlayerQuit(PlayerQuitEvent event){
	}
	
	public void onPlayerTeleport(PlayerTeleportEvent event){
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event){
	}
	
	public void onPlayerPortal(PlayerPortalEvent event){
	}
}
