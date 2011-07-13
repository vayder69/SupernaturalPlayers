package me.matterz.supernaturals.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.matterz.supernaturals.SupernaturalsPlugin;

public class SNCommands {

	public static SupernaturalsPlugin plugin;
	
	public SNCommands(SupernaturalsPlugin instance){
		SNCommands.plugin=instance;
	}
	
	public void sendHelp(CommandSender sender){
		sender.sendMessage(ChatColor.WHITE + "-----[ " + ChatColor.LIGHT_PURPLE + " Supernaturals Help " + ChatColor.WHITE + " ]-----");
    	sender.sendMessage(ChatColor.LIGHT_PURPLE + "/sn help - Show this information.");
	}
}
