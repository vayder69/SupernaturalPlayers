package me.matterz.supernaturals;

import me.matterz.supernaturals.io.SNConfigHandler;

public class SuperNPlayer implements Comparable<SuperNPlayer>{
	
	private String playername;
	private String superType = "human";
	private String oldSuperType = "human";
	private double oldSuperPower = 0;
	private boolean supernatural = false;
	private boolean vampire = false;
	private double superPower = 0;
	private boolean truce = true;
	private int truceTimer = 0;
	
	public SuperNPlayer(){}
	
	public SuperNPlayer(String playername){
		this.playername=playername;
		this.superType = "human";
		this.oldSuperType = "human";
		this.oldSuperPower = 0;
		this.supernatural = false;
		this.vampire = false;
		this.superPower = 0;
		this.truce = true;
		this.truceTimer = 0;
	}
	
	public SuperNPlayer(String playername, String superType, double superPower, String oldSuperType, double oldSuperPower, boolean truce, int truceTimer){
		this.playername = playername;
		this.superType = superType;
		this.oldSuperType = oldSuperType;
		this.oldSuperPower = oldSuperPower;		
		if(superType.equalsIgnoreCase("human")){
			this.supernatural = false;
			this.vampire = false;
		} else {
			this.supernatural = true;
			if(superType.equalsIgnoreCase("vampire"))
				this.vampire = true;
			else
				this.vampire = false;
		}
		this.superPower = superPower;
		this.truce = truce;
		this.truceTimer = truceTimer;
	}
	
	 @Override
    public int compareTo(SuperNPlayer o) {
        double oPower = o.getPower();

        if (superPower < oPower)
            return 1;
        else if (superPower > oPower)
            return -1;
        else
            return 0;
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
		return this.supernatural;
	}
	
	public void setSuper(boolean natural){
		this.supernatural = natural;
	}
	
	public boolean isVampire(){
		return this.vampire;
	}
	
	public void setVampire(boolean vampire){
		this.vampire = vampire;
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
