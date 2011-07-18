package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SNCommandConvert extends SNCommand {

	public SNCommandConvert() {	
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		optionalParameters.add("playername");
		requiredParameters.add("supernaturalType");
		permissions = "supernatural.admin.command.curse";
		helpNameAndParams = "convert [playername] [supernaturalType]";
		helpDescription = "Instantly turn a player into a supernatural.";
	}
	
	@Override
	public void perform(){
		
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		if(parameters.size()==1){
			String superType = parameters.get(0).toLowerCase();
			
			if(!SNConfigHandler.supernaturalTypes.contains(superType)){
				this.sendMessage("Supernatural Type invalid!");
				return;
			}
			
			SuperNPlayer snplayer = SupernaturalManager.get(senderPlayer);
			
			if(snplayer.getType().equalsIgnoreCase(superType)){
				this.sendMessage(ChatColor.WHITE + senderPlayer.getName() + ChatColor.RED + " is already a " 
						+ ChatColor.WHITE + superType +ChatColor.RED + " !");
			}else if(snplayer.getOldType().equalsIgnoreCase(superType)){
				this.sendMessage(ChatColor.WHITE + senderPlayer.getName() + ChatColor.RED + " was turned BACK into a " 
						+ ChatColor.WHITE + superType +ChatColor.RED + " !");
				SupernaturalManager.revert(snplayer);
			}else{
				this.sendMessage(ChatColor.WHITE + senderPlayer.getName() + ChatColor.RED + " was turned into a " 
						+ ChatColor.WHITE + superType +ChatColor.RED + " !");
				SupernaturalManager.curse(snplayer, superType);
			}
		}else{
			String playername = parameters.get(0);
			String superType = parameters.get(1).toLowerCase();
			Player player = SupernaturalsPlugin.instance.getServer().getPlayer(playername);
	
			if (player == null){
				this.sendMessage("Player not found!");
				return;
			}
			
			if(!SNConfigHandler.supernaturalTypes.contains(superType)){
				this.sendMessage("Supernatural Type invalid!");
				return;
			}
			
			SuperNPlayer snplayer = SupernaturalManager.get(player);
			
			if(snplayer.getType().equalsIgnoreCase(superType)){
				this.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.RED + " is already a " 
						+ ChatColor.WHITE + superType +ChatColor.RED + " !");
			}else if(snplayer.getOldType().equalsIgnoreCase(superType)){
				this.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.RED + " was turned BACK into a " 
						+ ChatColor.WHITE + superType +ChatColor.RED + " !");
				SupernaturalManager.revert(snplayer);
			}else{
				this.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.RED + " was turned into a " 
						+ ChatColor.WHITE + superType +ChatColor.RED + " !");
				SupernaturalManager.curse(snplayer, superType);
			}
		}
	}
}
