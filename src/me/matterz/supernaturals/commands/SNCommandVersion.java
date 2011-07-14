package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;

public class SNCommandVersion extends SNCommand {
	public SNCommandVersion() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "supernatural.command.powergain";
		helpNameAndParams = "version";
		helpDescription = "Would display "+SupernaturalsPlugin.instance.getDescription().getFullName();
	}
	
	@Override
	public void perform() {
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		this.sendMessage("You are running "+SupernaturalsPlugin.instance.getDescription().getFullName());
	}
}
