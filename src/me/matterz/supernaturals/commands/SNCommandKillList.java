package me.matterz.supernaturals.commands;

import java.util.ArrayList;
import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.HunterManager;
import me.matterz.supernaturals.manager.SNCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SNCommandKillList extends SNCommand {

	public SNCommandKillList() {	
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "supernatural.command.killlist";
		helpNameAndParams = "convert [playername] [supernaturalType]";
		helpDescription = "Instantly turn a player into a supernatural.";
	}
	
	@Override
	public void perform(){
		
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		ArrayList<SuperNPlayer> bountyList = HunterManager.getBountyList();
		
		// Create Messages
		List<String> messages = new ArrayList<String>();
		messages.add("*** "+ChatColor.WHITE +"Current WitchHunter Targets "+ChatColor.RED +"***");
		for(SuperNPlayer snplayer : bountyList){
			messages.add(ChatColor.WHITE+snplayer.getName());
		}
		
		// Send them
		this.sendMessage(messages);
	}
}
