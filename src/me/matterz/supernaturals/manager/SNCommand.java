package me.matterz.supernaturals.manager;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SNCommand {
	public List<String> requiredParameters;
	public List<String> optionalParameters;
	public String permissions;
	public String helpNameAndParams;
	public String helpDescription;
	public boolean senderMustBePlayer;
	public boolean senderMustBeSupernatural;
	public CommandSender sender;
	public List<String> parameters;
		
	public SNCommand() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		permissions = "";
		senderMustBePlayer = false;
		senderMustBeSupernatural = false;
		helpNameAndParams = "fail!";
		helpDescription = "no description";
	}
	
	public String getName() {
		String name = this.getClass().getName().toLowerCase();
		if (name.lastIndexOf('.') > 0) {
		    name = name.substring(name.lastIndexOf('.')+1);
		}
		return name.substring(9);
	}
	
	public String getBaseName() {
		return "sn";
	}
	
	public void execute(CommandSender sender, List<String> parameters) {
		this.sender = sender;
		this.parameters = parameters;
		
		if (!validateCall()) {
			sendMessage("Try /sn help");
			return;
		}
		
		perform();
	}
	
	public void perform() {
		
	}
	
	public void sendMessage(String message) {
		sender.sendMessage(ChatColor.RED+message);
	}
	
	public void sendMessage(List<String> messages) {
		for(String message : messages) {
			this.sendMessage(message);
		}
	}
	
	// Test if the number of params is correct.
	public boolean validateCall() {
		
		if ( this.senderMustBePlayer && ! (sender instanceof Player)) {
			sendMessage("This command can only be used by ingame players.");
			return false;
		}
		
		if (parameters.size() < requiredParameters.size()) {
			int missing = requiredParameters.size() - parameters.size();
			sendMessage("Missing parameters. You must enter "+missing+" more.");
			return false;
		}
		
		if (parameters.size() > requiredParameters.size() + optionalParameters.size()) {
			sendMessage("To many parameters.");
			return false;
		}
		
		return true;
	}
}
