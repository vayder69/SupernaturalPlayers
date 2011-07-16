package me.matterz.supernaturals.manager;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class PriestManager {
	
private SupernaturalsPlugin plugin;

	private Map<SuperNPlayer, String> playerList = new HashMap<SuperNPlayer, String>();
	
	public PriestManager(SupernaturalsPlugin plugin) {
		this.plugin=plugin;
	}
	
	// -------------------------------------------- //
	// 					Damage						//
	// -------------------------------------------- //
	
	public float priestAttack(Player priest, Entity victim, float damage){
		if(victim instanceof Animals){
			damage = 0;
		} else if(victim instanceof Player){
			Player pVictim = (Player) victim;
			SuperNPlayer snvictim = SupernaturalManager.get(pVictim);
			if(snvictim.isSuper()){
				pVictim.setFireTicks(pVictim.getMaxFireTicks());
				damage *= SNConfigHandler.priestDamageFactorAttack;
			} else{
				damage *= SNConfigHandler.priestDamageFactorAttack;
			}
		}
		return damage;
	}
	
	// -------------------------------------------- //
	// 					Lighting					//
	// -------------------------------------------- //
	
	public void priestLight(){
		// Adjust each Priest
		for(SuperNPlayer snplayer : plugin.getSuperManager().getSupernaturals()) {
			if(snplayer.isPriest()){
				int radius = SNConfigHandler.priestLightRadius;
				int LocationX;
				int LocationY;
				int LocationZ;
				int currentInt;
				int newInt;
				//Light Removal
				if(playerList.containsKey(snplayer)){
					String locationString = playerList.get(snplayer);
					String[] locationStrings = locationString.split(":");
					LocationX = Integer.parseInt(locationStrings[1]);
					LocationY = Integer.parseInt(locationStrings[2]);
					LocationZ = Integer.parseInt(locationStrings[3]);
					CraftWorld world = (CraftWorld) SupernaturalsPlugin.instance.getServer().getWorld(locationStrings[0]);
					for(int x = -radius; x <= radius; x++){
						for(int y = -radius; y <= radius; y++){
							for(int z = -radius; z <= radius; z++){
								
								Vector origin = new Vector(LocationX, LocationY, LocationZ);
								Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);

								if(v.isInSphere(origin, radius)){
									currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
									newInt = (currentInt - SNConfigHandler.priestLightIntensity);
									if(newInt<0)
										newInt=0;
									world.getHandle().b(EnumSkyBlock.BLOCK, LocationX+x, LocationY+y, LocationZ+z, newInt);
								}
							}
						}
					}
					playerList.remove(snplayer);
				}
				//Light Addition
				Player player = plugin.getServer().getPlayer(snplayer.getName());
				if(player==null){
					continue;
				}
				Location location = player.getLocation();
				LocationX = (int)location.getX();
				LocationY = (int)location.getY();
				LocationZ = (int)location.getZ();
				CraftWorld world = (CraftWorld) player.getWorld();
				String locationString = (world.getName()+":"+LocationX+":"+LocationY+":"+LocationZ);
				for(int x = -radius; x <= radius; x++){
					for(int y = -radius; y <= radius; y++){
						for(int z = -radius; z <= radius; z++){
							
							Vector origin = new Vector(LocationX, LocationY, LocationZ);
							Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);

							if(v.isInSphere(origin, radius)){
								currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
								newInt = (currentInt + SNConfigHandler.priestLightIntensity);
								if(newInt>15)
									newInt=15;
								world.getHandle().b(EnumSkyBlock.BLOCK, LocationX+x, LocationY+y, LocationZ+z, newInt);
							}
						}
					}
					playerList.put(snplayer,locationString);
				}
			}
		}
	}

}
