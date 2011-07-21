package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SNPlayerMonitor extends PlayerListener {
	
private SupernaturalsPlugin plugin;
	
	public SNPlayerMonitor(SupernaturalsPlugin instance){
		this.plugin = instance;
	}
	
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event){
		if(SupernaturalManager.get(event.getPlayer()).isHunter()){
			event.getPlayer().setSneaking(true);
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event){	    
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		
		if(snplayer.isHuman()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.WHITE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.WHITE + "Human " + player.getName() + ChatColor.GOLD + " has joined the server.");
		}else if(snplayer.isVampire()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.DARK_PURPLE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "Vampire " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isWere()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.BLUE+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.BLUE + "Werewolf " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isGhoul()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.DARK_GRAY+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.DARK_GRAY + "Ghoul " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isPriest()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.GOLD+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.GOLD + "Priest " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isHunter()){
//			player.setSneaking(true);
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.GREEN+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.GREEN + "WitchHunter " + player.getName() + ChatColor.GOLD + " has joined the server.");
		} else if(snplayer.isDemon()){
			player.setDisplayName(player.getDisplayName().trim().replace(player.getName(), ChatColor.RED+player.getName()));
			plugin.getServer().broadcastMessage(ChatColor.RED + "Demon " + player.getName() + ChatColor.GOLD + " has joined the server.");
		}
		
	}
}
