package me.matterz.supernaturals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.matterz.supernaturals.commands.SNCommandAdminHelp;
import me.matterz.supernaturals.commands.SNCommandReload;
import me.matterz.supernaturals.commands.SNCommandPower;
import me.matterz.supernaturals.commands.SNCommandBurnTimes;
import me.matterz.supernaturals.commands.SNCommandCure;
import me.matterz.supernaturals.commands.SNCommandPowerGain;
import me.matterz.supernaturals.commands.SNCommandHelp;
import me.matterz.supernaturals.commands.SNCommandList;
import me.matterz.supernaturals.commands.SNCommandSave;
import me.matterz.supernaturals.commands.SNCommandCurse;
import me.matterz.supernaturals.commands.SNCommandSetChurch;
import me.matterz.supernaturals.commands.SNCommandVersion;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.io.SNPlayerHandler;
import me.matterz.supernaturals.light.LightMapManager;
import me.matterz.supernaturals.listeners.SNEntityListener;
import me.matterz.supernaturals.listeners.SNEntityMonitor;
import me.matterz.supernaturals.listeners.SNPlayerListener;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.util.TextUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class SupernaturalsPlugin extends JavaPlugin {
	public static SupernaturalsPlugin instance;
	
	private final SNConfigHandler snConfig = new SNConfigHandler(this);
	
	private final SNEntityListener entityListener = new SNEntityListener(this);
	private final SNPlayerListener playerListener = new SNPlayerListener(this);
	private final SNEntityMonitor entityMonitor = new SNEntityMonitor(this);
	
	private SupernaturalManager superManager = new SupernaturalManager(this);
	private LightMapManager lightManager = new LightMapManager();
	
	public List<SNCommand> commands = new ArrayList<SNCommand>();
	
	public static PermissionHandler permissionHandler;
	
	public SupernaturalsPlugin(){
		SupernaturalsPlugin.instance = this;
	}
	
	public SupernaturalManager getSuperManager(){
		return superManager;
	}
	
	public SNConfigHandler getConfigManager(){
		return snConfig;
	}
	
	public LightMapManager getLightManager(){
		return lightManager;
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
		
		// Add the commands
		commands.add(new SNCommandHelp());
		commands.add(new SNCommandAdminHelp());
		commands.add(new SNCommandPower());
		commands.add(new SNCommandReload());
		commands.add(new SNCommandSave());
		commands.add(new SNCommandCurse());
		commands.add(new SNCommandCure());
		commands.add(new SNCommandList());
		commands.add(new SNCommandVersion());
		commands.add(new SNCommandBurnTimes());
		commands.add(new SNCommandSetChurch());
		commands.add(new SNCommandPowerGain());
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Priority.High, this);
		pm.registerEvent(Type.PLAYER_CHAT, this.playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_JOIN, this.playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_ANIMATION, this.playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_KICK, this.playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_MOVE, this.playerListener, Priority.High, this);
		
		pm.registerEvent(Type.ENTITY_DAMAGE, this.entityListener, Priority.Highest, this);
		pm.registerEvent(Type.ENTITY_TARGET, this.entityListener, Priority.Normal, this);
		
		pm.registerEvent(Type.ENTITY_DAMAGE, this.entityMonitor, Priority.Monitor, this);
		pm.registerEvent(Type.ENTITY_DEATH, this.entityMonitor, Priority.Monitor, this);
		
        PluginDescriptionFile pdfFile = this.getDescription();
        log(pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled.");
        
        snConfig.getConfiguration();

	    loadData();
	    superManager.startTimer();
	    setupPermissions();
	}
	
	// -------------------------------------------- //
	// 				Chat Commands					//
	// -------------------------------------------- //
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			List<String> parameters = new ArrayList<String>(Arrays.asList(args));
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(((Player) sender).getName() + " used command: " + commandLabel 
						+ " with args: " + TextUtil.implode(parameters, ", "));
			this.handleCommand(sender, parameters);
			return true;
		}
		return false;
	}
	
	public void handleCommand(CommandSender sender, List<String> parameters) {
		if (parameters.size() == 0) {
			for (SNCommand vampcommand : this.commands) {
				if (vampcommand.getName().equalsIgnoreCase("help")) {
					vampcommand.execute(sender, parameters);
					return;
				}
			}
			sender.sendMessage(ChatColor.RED+"Unknown command. Try /sn help");
			return;
		}
		
		String command = parameters.get(0).toLowerCase();
		parameters.remove(0);
		
		for (SNCommand vampcommand : this.commands) {
			if (command.equals(vampcommand.getName())) {
				vampcommand.execute(sender, parameters);
				return;
			}
		}
		
		sender.sendMessage(ChatColor.RED+"Unknown command \""+command+"\". Try /sn help");
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
	// 				Permissions						//
	// -------------------------------------------- //
	
	private void setupPermissions() {
	    if (permissionHandler != null) {
	        return;
	    }
	    
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (permissionsPlugin == null) {
	        log("Permission system not detected, defaulting to OP");
	        return;
	    }
	    
	    permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	    log("Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}
	
	// -------------------------------------------- //
	// 					Logging						//
	// -------------------------------------------- //
	
	 public static void log(String msg) {
		 log(Level.INFO, msg);
	 }
	 
	 public static void log(Level level, String msg) {
		 Logger.getLogger("Minecraft").log(level, "["+instance.getDescription().getFullName()+"] "+msg);
	}

}
