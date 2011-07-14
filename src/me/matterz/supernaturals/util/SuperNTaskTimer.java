package me.matterz.supernaturals.util;

import java.util.TimerTask;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SupernaturalManager;

public class SuperNTaskTimer extends TimerTask {
	
	private long lastRunTime = System.currentTimeMillis();
	private	SupernaturalsPlugin plugin;

	public SuperNTaskTimer(SupernaturalsPlugin plugin){
		this.plugin = plugin;
	}
	@Override
	public void run() {
		long now = System.currentTimeMillis();
		long delta = ((now+(long)0.01) - lastRunTime);
		this.lastRunTime = now;
		
		// Tick each online player
		for(SuperNPlayer snplayer : SupernaturalManager.findAllOnline()) {
			plugin.getSuperManager().advanceTime(snplayer, (int) delta);
		}
	}
}
