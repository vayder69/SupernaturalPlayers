package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class SNCommandPower extends SNCommand {
	
	public SNCommandPower() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeSupernatural = true;
		permissions = "supernatural.command.power";
		optionalParameters.add("playername");
		optionalParameters.add("power");
		helpNameAndParams = "power [amount] | power [playername] [amount]";
		helpDescription = "See current power level";
	}
	
	@Override
	public void perform() {
		
		Player senderPlayer = (Player) sender;
		String permissions2 = "supernatural.admin.command.power";
		
		if(parameters.isEmpty()){
			if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
				this.sendMessage("You do not have permissions to use this command.");
				return;
			}
			SuperNPlayer snplayer = SupernaturalManager.get(senderPlayer);
					
			this.sendMessage("You are a "+ChatColor.WHITE+snplayer.getType()+ChatColor.RED+" and your current power level is: " +ChatColor.WHITE+ (int) snplayer.getPower());
			return;
		} else {
			if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions2)){
				this.sendMessage("You do not have permissions to use this command.");
				return;
			}
			if(parameters.size()==1){
				double powerGain;
				
				try{
					powerGain = Double.parseDouble(parameters.get(0));
				} catch(NumberFormatException e) {
					this.sendMessage("Invalid Number.");
					return;
				}
				if(powerGain>=10000D){
					powerGain=9999;
				}
				
				SuperNPlayer snplayer = SupernaturalManager.get(senderPlayer);
				SupernaturalManager.alterPower(snplayer, powerGain, "Admin boost!");
			}else{
				String playername = parameters.get(0);
				Player player = SupernaturalsPlugin.instance.getServer().getPlayer(playername);
				if (player == null) {
					this.sendMessage("Player not found!");
					return;
				}
				double powerGain;
				
				try{
					powerGain = Double.parseDouble(parameters.get(1));
				} catch(NumberFormatException e) {
					this.sendMessage("Invalid Number.");
					return;
				}
				if(powerGain>=10000D){
					powerGain=9999;
				}
				this.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.RED + " has been powered up!");
				SuperNPlayer snplayer = SupernaturalManager.get(player);
				SupernaturalManager.alterPower(snplayer, powerGain, "Admin boost!");
			}
		}
	}
}