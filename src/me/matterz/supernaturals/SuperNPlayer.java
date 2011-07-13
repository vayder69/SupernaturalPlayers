package me.matterz.supernaturals;

import me.matterz.supernaturals.io.SNConfigHandler;

public class SuperNPlayer implements Comparable<SuperNPlayer>{
	
	private String playername;
	private boolean vampire = false;
	private double superPower = 0;
	private boolean truce = true;
	private int truceTimer = 0;
	
	public SuperNPlayer() {}
	
	public SuperNPlayer(String playername, boolean isVampire, double superPower, boolean truce, int truceTimer){
		this.playername = playername;
		this.vampire = isVampire;
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
