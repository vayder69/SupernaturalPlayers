package me.matterz.supernaturals.commands;

import org.bukkit.entity.Player;

import me.matterz.supernaturals.SupernaturalsPlugin;

public class SNCommandSave extends SNCommandReload {
	
	public SNCommandSave() {
		permissions = "supernatural.admin.command.save";
		helpNameAndParams = "";
		helpDescription = "Save data from disk.";
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		SupernaturalsPlugin.instance.saveData();
		this.sendMessage("All config/player data has been saved!");
	}
}
