package me.matterz.supernaturals.util;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SuperNManager;

public class SNTaskTimer implements Runnable {
	private	SupernaturalsPlugin plugin;

	public SNTaskTimer(SupernaturalsPlugin plugin){
		this.plugin = plugin;
	}
	@Override
	public void run() {		
		// Tick each online player
		for(SuperNPlayer snplayer : SuperNManager.findAllOnline()) {
			plugin.getSuperManager().advanceTime(snplayer);
		}
	}
}
