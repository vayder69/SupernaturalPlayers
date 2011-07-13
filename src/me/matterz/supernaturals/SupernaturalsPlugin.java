package me.matterz.supernaturals;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.io.SNPlayerHandler;
import me.matterz.supernaturals.listeners.SNEntityListener;
import me.matterz.supernaturals.listeners.SNPlayerListener;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.util.SNCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SupernaturalsPlugin extends JavaPlugin {
	public static SupernaturalsPlugin instance;
	
	private final SNCommands commands = new SNCommands(this);
	private final SNConfigHandler snConfig = new SNConfigHandler(this);
	
	private final SNEntityListener entityListener = new SNEntityListener(this);
	private final SNPlayerListener playerListener = new SNPlayerListener(this);
	
	private SupernaturalManager superManager = new SupernaturalManager(this);
	
	public SupernaturalsPlugin(){
		SupernaturalsPlugin.instance = this;
	}
	
	public SupernaturalManager getSuperManager(){
		return superManager;
	}
	
	public SNConfigHandler getConfigManager(){
		return snConfig;
	}
	
	// -------------------------------------------- //
	// 			Plugin Enable/Disable				//
	// -------------------------------------------- //
	
	@Override
	public void onDisable() {
		superManager.cancelTimer();
		saveData();
		PluginDescriptionFile pdfFile = this.getDescription();
        log(pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled.");
		
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_JOIN, this.playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_CHAT, this.playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Priority.Normal, this);
		pm.registerEvent(Type.ENTITY_DAMAGE, this.entityListener, Priority.Highest, this);
		
        PluginDescriptionFile pdfFile = this.getDescription();
        log(pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled.");
        
        snConfig.getConfiguration();

	    loadData();
	    superManager.startTimer();        
	}
	
	// -------------------------------------------- //
	// 				Chat Commands					//
	// -------------------------------------------- //
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{		
		if(args.length == 0){
			commands.sendHelp(sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be used by in-game players.");
			return true;
		}
		
		String commandName = args[0].toLowerCase();
		String[] trimmedArgs = new String[args.length - 1];
		
		for (int i = 0; i < args.length - 1; i++)
            trimmedArgs[i] = args[i + 1];
		
		if (commandName.equalsIgnoreCase("help"))
			commands.sendHelp(sender);
		//else if(commandName.equalsIgnoreCase("level"))
		//	commands.sendLevel(sender,trimmedArgs);
		
		return false;
	}
	
	// -------------------------------------------- //
	// 				Data Management					//
	// -------------------------------------------- //
	
	public void saveData(){
		File file = new File(getDataFolder(), "data.yml");
        SNPlayerHandler.save(superManager.getSupernaturals(), file);
        
        snConfig.saveConfig();
	}
	
	public void loadData(){
		File file = new File(getDataFolder(), "data.yml");
		superManager.setSupernaturals(SNPlayerHandler.load(file));
	}
	
	public void reloadData(){
		File file = new File(getDataFolder(), "data.yml");
		superManager.setSupernaturals(SNPlayerHandler.load(file));
        
        snConfig.reloadConfig();
	}
	
	// -------------------------------------------- //
	// 					Logging						//
	// -------------------------------------------- //
	
	 public static void log(String msg) {
		 log(Level.INFO, "["+instance.getDescription().getFullName()+"] " + msg);
	 }
	 
	 public static void log(Level level, String msg) {
		 Logger.getLogger("Minecraft").log(level, "["+instance.getDescription().getFullName()+"] "+msg);
	}

}
