package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SNCommand;

import org.bukkit.entity.Player;

public class SNCommandSetCoven extends SNCommand {
	public SNCommandSetCoven() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeSupernatural = true;
		permissions = "supernatural.admin.command.setcoven";
		helpNameAndParams = "";
		helpDescription = "Sets the current location as the vampire coven";
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
		
		SNConfigHandler.vampireTeleportWorld = senderPlayer.getWorld().getName();
		SNConfigHandler.vampireTeleportLocationX = (int) currentX;
		SNConfigHandler.vampireTeleportLocationY = (int) currentY;
		SNConfigHandler.vampireTeleportLocationZ = (int) currentZ;
		SNConfigHandler.vampireTeleportLocation = senderPlayer.getLocation();
		
		SNConfigHandler.getConfig().setProperty("Vampire.Teleport.World", SNConfigHandler.vampireTeleportWorld);
		SNConfigHandler.getConfig().setProperty("Vampire.Teleport.Location.X", SNConfigHandler.vampireTeleportLocationX);
		SNConfigHandler.getConfig().setProperty("Vampire.Teleport.Location.Y", SNConfigHandler.vampireTeleportLocationY);
		SNConfigHandler.getConfig().setProperty("Vampire.Teleport.Location.Z", SNConfigHandler.vampireTeleportLocationZ);
		
		SupernaturalsPlugin.saveData();
				
		this.sendMessage("Coven location set.");
	}
}