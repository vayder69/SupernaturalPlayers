package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;

public class SNCommandReload extends SNCommand {
	public String msgLoadSuccess = "Loaded %s.";
	public String msgLoadFail = "FAILED to load %s.";
	public String msgSaveSuccess = "Saved %s.";
	public String msgSaveFail = "FAILED to save %s.";
	
	public SNCommandReload() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "supernatural.admin.command.reload";
	}
	
	@Override
	public void perform() {	
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		this.sendMessage("All config and player has been reloaded");
		SupernaturalsPlugin.instance.reloadData();
	}
}
