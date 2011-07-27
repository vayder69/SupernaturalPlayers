/*
 * Supernatural Players Plugin for Bukkit
 * Copyright (C) 2011  Matt Walker <mmw167@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package me.matterz.supernaturals;

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
	
	// -------------------------------------------- //
	// 					Parameters					//
	// -------------------------------------------- //
	
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
		this.truceTimer = 0;
	}
	
	public int getTruceTimer(){
		return this.truceTimer;
	}
	
	public void setTruceTimer(int timer){
		this.truceTimer=timer;
	}
	
	// -------------------------------------------- //
	// 					Booleans					//
	// -------------------------------------------- //
	
	public boolean isSuper(){
		if(this.getType().equalsIgnoreCase("human") 
				|| this.getType().equalsIgnoreCase("priest") 
				|| this.getType().equalsIgnoreCase("witchhunter"))
			return false;
		return true;
	}
	
	public boolean isHuman(){
		if(this.getType().equalsIgnoreCase("human"))
			return true;
		return false;
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
	
	public boolean isWere(){
		if(this.getType().equalsIgnoreCase("werewolf")){
			return true;
		}
		return false;
	}
	
	public boolean isGhoul(){
		if(this.getType().equalsIgnoreCase("ghoul")){
			return true;
		}
		return false;
	}
	
	public boolean isHunter(){
		if(this.getType().equalsIgnoreCase("witchhunter")){
			return true;
		}
		return false;
	}
	
	public boolean isDemon(){
		if(this.getType().equalsIgnoreCase("demon")){
			return true;
		}
		return false;
	}
	
	public double scale(double input){
		double powerPercentage = input*(this.getPower()/10000);
		return powerPercentage;
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
		return this.limitDouble(d, 0, 10000);
	}
}
