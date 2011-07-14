package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SNCommandPowerGain extends SNCommand
{
	public SNCommandPowerGain() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "supernatural.admin.command.powergain";
		requiredParameters.add("playername");
		requiredParameters.add("blood");
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		String playername = parameters.get(0);
		Player player = SupernaturalsPlugin.instance.getServer().getPlayer(playername);
		if (player == null) {
			this.sendMessage("Player not found!");
			return;
		}
		double powerGain = Double.parseDouble(parameters.get(1));
		if(powerGain>10000D){
			powerGain=10000;
		}
		
		this.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.RED + " has been powered up!");
		SupernaturalsPlugin.instance.getSuperManager();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SupernaturalsPlugin.instance.getSuperManager().alterPower(snplayer, powerGain);
	}
}
