package me.matterz.supernaturals.commands;

import me.matterz.supernaturals.SupernaturalsPlugin;

import org.bukkit.entity.Player;

public class SNCommandRestartTask extends SNCommandReload {
	
	public SNCommandRestartTask() {
		permissions = "supernatural.admin.command.task";
		helpNameAndParams = "";
		helpDescription = "Restarts the task timer.";
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		SupernaturalsPlugin.restartTask();
		this.sendMessage("Task Timer has been restarted.");
	}
}
