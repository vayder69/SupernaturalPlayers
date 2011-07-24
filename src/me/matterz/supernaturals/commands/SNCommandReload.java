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
		optionalParameters.add("type");
		senderMustBePlayer = false;
		permissions = "supernatural.admin.command.reload";
		helpNameAndParams = "";
		helpDescription = "Reload Config or Data files";
	}
	
	@Override
	public void perform() {
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		if(parameters.isEmpty()){
			this.sendMessage("Config file has been reloaded");
			SupernaturalsPlugin.reloadConfig();
		}else{
			if(parameters.get(0).equalsIgnoreCase("config")){
				this.sendMessage("Config file has been reloaded");
				SupernaturalsPlugin.reloadConfig();
			}else if(parameters.get(0).equalsIgnoreCase("data")){
				this.sendMessage("Data file has been reloaded");
				SupernaturalsPlugin.reloadData();
			}else{
				this.sendMessage("Invalid option.");
			}
		}
	}
}
