package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SNCommand;

import org.bukkit.entity.Player;

public class SNCommandSetBanish extends SNCommand {
	
	public SNCommandSetBanish() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeSupernatural = true;
		permissions = "supernatural.admin.command.setbanish";
		helpNameAndParams = "";
		helpDescription = "Sets the current location as the priests' banish location";
	}
	
	@Override
	public void perform() {
		
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		double currentX = senderPlayer.getLocation().getX();
		double currentY = senderPlayer.getLocation().getY();
		double currentZ = senderPlayer.getLocation().getZ();
		
		SNConfigHandler.priestBanishWorld = senderPlayer.getWorld().getName();
		SNConfigHandler.priestBanishLocationX = (int) currentX;
		SNConfigHandler.priestBanishLocationY = (int) currentY;
		SNConfigHandler.priestBanishLocationZ = (int) currentZ;
		SupernaturalsPlugin.saveData();
				
		this.sendMessage("Banish location set.");
	}
}
