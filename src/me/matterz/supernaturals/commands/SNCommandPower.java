package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.entity.Player;


public class SNCommandPower extends SNCommand {
	
	public SNCommandPower() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeSupernatural = true;
		permissions = "supernatural.command.power";
		helpNameAndParams = "";
		helpDescription = "See current power level";
	}
	
	@Override
	public void perform() {
		
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		SupernaturalsPlugin.instance.getSuperManager();
		SuperNPlayer snplayer = SupernaturalManager.get(senderPlayer);
				
		this.sendMessage("Your current power level is: " + (int) snplayer.getPower());
	}
}