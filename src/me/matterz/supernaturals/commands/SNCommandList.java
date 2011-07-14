package me.matterz.supernaturals.commands;

import java.util.ArrayList;
import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.util.TextUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class SNCommandList extends SNCommand {
	
	public SNCommandList() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "supernatural.command.list";
		helpNameAndParams = "list";
		helpDescription = "List supernaturals on the server.";
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		List<String> vampires = new ArrayList<String>();
		
		for (SuperNPlayer snplayer : SupernaturalsPlugin.instance.getSuperManager().getSupernaturals()) {
			if (snplayer.isVampire()) {
				vampires.add(snplayer.getName());
			}
		}
		
		// Create Messages
		List<String> messages = new ArrayList<String>();
		messages.add(" ");
		messages.add("= Vampires: "+ ChatColor.WHITE + TextUtil.implode(vampires, ", "));
		
		// Send them
		this.sendMessage(messages);
	}
}
