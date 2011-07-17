package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SNCommand;

import org.bukkit.entity.Player;

public class SNCommandSetChurch extends SNCommand {
	public SNCommandSetChurch() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeSupernatural = true;
		permissions = "supernatural.admin.command.setchurch";
		helpNameAndParams = "";
		helpDescription = "Sets the current location as the church";
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
		
		SNConfigHandler.priestChurchWorld = senderPlayer.getWorld().getName();
		SNConfigHandler.priestChurchLocationX = (int) currentX;
		SNConfigHandler.priestChurchLocationY = (int) currentY;
		SNConfigHandler.priestChurchLocationZ = (int) currentZ;
		SNConfigHandler.priestChurchLocation = senderPlayer.getLocation();
		SupernaturalsPlugin.saveData();
				
		this.sendMessage("Church location set.");
	}
}
