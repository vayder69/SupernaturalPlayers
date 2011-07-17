package me.matterz.supernaturals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.matterz.supernaturals.commands.SNCommandAdmin;
import me.matterz.supernaturals.commands.SNCommandClasses;
import me.matterz.supernaturals.commands.SNCommandReload;
import me.matterz.supernaturals.commands.SNCommandPower;
import me.matterz.supernaturals.commands.SNCommandCure;
import me.matterz.supernaturals.commands.SNCommandHelp;
import me.matterz.supernaturals.commands.SNCommandList;
import me.matterz.supernaturals.commands.SNCommandSave;
import me.matterz.supernaturals.commands.SNCommandConvert;
import me.matterz.supernaturals.commands.SNCommandSetBanish;
import me.matterz.supernaturals.commands.SNCommandSetChurch;
import me.matterz.supernaturals.commands.SNCommandSetCoven;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.io.SNPlayerHandler;
import me.matterz.supernaturals.listeners.SNEntityListener;
import me.matterz.supernaturals.listeners.SNEntityMonitor;
import me.matterz.supernaturals.listeners.SNPlayerListener;
import me.matterz.supernaturals.listeners.SNPlayerMonitor;
import me.matterz.supernaturals.manager.GhoulManager;
import me.matterz.supernaturals.manager.PriestManager;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.manager.VampireManager;
import me.matterz.supernaturals.manager.WereManager;
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
	private final SNPlayerMonitor playerMonitor = new SNPlayerMonitor(this);
	private final SNEntityMonitor entityMonitor = new SNEntityMonitor(this);
	
	private SupernaturalManager superManager = new SupernaturalManager(this);
	private VampireManager vampManager = new VampireManager();
	private PriestManager priestManager = new PriestManager(this);
	private WereManager wereManager = new WereManager();
	private GhoulManager ghoulManager = new GhoulManager();
	
	public List<SNCommand> commands = new ArrayList<SNCommand>();
	
	private static File dataFolder;
	
	public static PermissionHandler permissionHandler;
	
	public SupernaturalsPlugin(){
		SupernaturalsPlugin.instance = this;
	}
	
	// -------------------------------------------- //
	// 					Managers					//
	// -------------------------------------------- //
	
	public SupernaturalManager getSuperManager(){
		return superManager;
	}
	
	public SNConfigHandler getConfigManager(){
		return snConfig;
	}
	
	public VampireManager getVampireManager(){
		return vampManager;
	}
	
	public PriestManager getPriestManager(){
		return priestManager;
	}
	
	public WereManager getWereManager(){
		return wereManager;
	}
	
	public GhoulManager getGhoulManager(){
		return ghoulManager;
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
		commands.add(new SNCommandAdmin());
		commands.add(new SNCommandPower());
		commands.add(new SNCommandReload());
		commands.add(new SNCommandSave());
		commands.add(new SNCommandConvert());
		commands.add(new SNCommandCure());
		commands.add(new SNCommandList());
		commands.add(new SNCommandClasses());
		commands.add(new SNCommandSetChurch());
		commands.add(new SNCommandSetBanish());
		commands.add(new SNCommandSetCoven());
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Priority.Lowest, this);
		pm.registerEvent(Type.PLAYER_KICK, this.playerListener, Priority.Low, this);
		
		pm.registerEvent(Type.PLAYER_ANIMATION, this.playerMonitor, Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_JOIN, this.playerMonitor, Priority.Monitor, this);
		
		pm.registerEvent(Type.ENTITY_DAMAGE, this.entityListener, Priority.Highest, this);
		pm.registerEvent(Type.ENTITY_TARGET, this.entityListener, Priority.Normal, this);
		
		pm.registerEvent(Type.ENTITY_DAMAGE, this.entityMonitor, Priority.Monitor, this);
		pm.registerEvent(Type.ENTITY_DEATH, this.entityMonitor, Priority.Monitor, this);
		
        PluginDescriptionFile pdfFile = this.getDescription();
        log(pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled.");
        
        dataFolder = getDataFolder();
        
        SNConfigHandler.getConfiguration();

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
	
	public static void saveData(){
		File file = new File(dataFolder, "data.yml");
        SNPlayerHandler.save(SupernaturalManager.getSupernaturals(), file);
        
        SNConfigHandler.saveConfig();
	}
	
	public static void loadData(){
		File file = new File(dataFolder, "data.yml");
		SupernaturalManager.setSupernaturals(SNPlayerHandler.load(file));
	}
	
	public static void reloadData(){
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Reloading config and data...");
		}
		File file = new File(dataFolder, "data.yml");
		SupernaturalManager.setSupernaturals(SNPlayerHandler.load(file));
        
		SNConfigHandler.reloadConfig();
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
