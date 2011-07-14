package me.matterz.supernaturals;

import me.matterz.supernaturals.io.SNConfigHandler;

public class SuperNPlayer{
	
	private String playername;
	private String superType = "human";
	private String oldSuperType = "human";
	private double oldSuperPower = 0;
	private double superPower = 0;
	private boolean truce = true;
	private int truceTimer = 0;
	
	public SuperNPlayer(){}
	
	public SuperNPlayer(String playername){
		this.playername=playername;
		this.superType = "human";
		this.oldSuperType = "human";
		this.oldSuperPower = 0;
		this.superPower = 0;
		this.truce = true;
		this.truceTimer = 0;
	}
	
	public SuperNPlayer(String playername, String superType, double superPower, String oldSuperType, double oldSuperPower, boolean truce, int truceTimer){
		this.playername = playername;
		this.superType = superType;
		this.oldSuperType = oldSuperType;
		this.oldSuperPower = oldSuperPower;		
		this.superPower = superPower;
		this.truce = truce;
		this.truceTimer = truceTimer;
	}
	
	public String getName(){
		return this.playername;
	}
	
	public void setName(String name){
		this.playername=name;
	}
	
	public String getType(){
		return this.superType;
	}
	
	public void setType(String type){
		this.superType=type;
	}
	
	public String getOldType(){
		return this.oldSuperType;
	}
	
	public void setOldType(String type){
		this.oldSuperType=type;
	}
	
	public double getOldPower(){
		return this.oldSuperPower;
	}
	
	public void setOldPower(double amount){
		this.oldSuperPower=amount;
	}
	
	public boolean isSuper(){
		if(this.getType().equalsIgnoreCase("human"))
			return false;
		return true;
	}
	
	public boolean isVampire(){
		if(this.getType().equalsIgnoreCase("vampire"))
			return true;
		return false;
	}
	
	public boolean isPriest(){
		if(this.getType().equalsIgnoreCase("priest")){
			return true;
		}
		return false;
	}
	
	public boolean hasTruce(){
		if(this.getType().equalsIgnoreCase("vampire") || this.getType().equalsIgnoreCase("ghoul")){
			return true;
		}
		return false;
	}
	
	public double getPower(){
		return this.superPower;
	}
	
	public void setPower(double amount){
		this.superPower = this.limitDouble(amount);
	}
	
	public boolean getTruce(){
		return this.truce;
	}
	
	public void setTruce(boolean truce){
		this.truce = truce;
	}
	
	public int getTruceTimer(){
		return this.truceTimer;
	}
	
	public void setTruceTimer(int timer){
		this.truceTimer=timer;
	}
	
	public boolean isOnline(){
		return SupernaturalsPlugin.instance.getServer().getPlayer(playername) != null;
	}
	
	// -------------------------------------------- //
	// 			Limiting value of double			//
	// -------------------------------------------- //
	public double limitDouble(double d, double min, double max){
		if (d < min){
			return min;
		}
		if (d > max){
			return max;
		}
		return d;
	}
	
	public double limitDouble(double d){
		return this.limitDouble(d, 0, SNConfigHandler.maxPower);
	}
}
