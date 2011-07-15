package me.matterz.supernaturals.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LightUtil {
	
	private static Map<String, List<Player>> lightMap = new HashMap<String, List<Player>>();
	
	public static void illuminate(Player player, Location location){
		int LocationX = location.getBlockX();
		int LocationY = location.getBlockY();
		int LocationZ = location.getBlockZ();
		
		int radius = SNConfigHandler.priestLightRadius;
		int extendedRadius = radius + 3;
		int intensity = SNConfigHandler.priestLightIntensity;
		
		CraftWorld world = (CraftWorld) player.getWorld();
		List<Player> playerList = new ArrayList<Player>();
		
		for(int x = -extendedRadius; x <= extendedRadius; x++){
			for(int y = -extendedRadius; y <= extendedRadius; y++){
				for(int z = -extendedRadius; z <= extendedRadius; z++){
					
					Vector origin = new Vector(LocationX, LocationY, LocationZ);
					Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);

					if(v.isInSphere(origin, radius)){
						int newIntensity;
						int currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);

						Location blockLocation = new Location(world, LocationX+x, LocationY+y, LocationZ+z);
						String locationString = (world.getName()+":"+blockLocation.getBlockX()+":"+blockLocation.getBlockY()+":"+blockLocation.getBlockZ());
						if(!lightMap.containsKey(locationString)){
							playerList.add(player);
							lightMap.put(locationString, playerList);
							newIntensity = currentInt + intensity;
							world.getHandle().b(EnumSkyBlock.BLOCK, LocationX + x, LocationY + y, LocationZ + z, newIntensity);
						}else{
							playerList = lightMap.get(locationString);
							if(!playerList.contains(player)){
								playerList.add(player);
								newIntensity = currentInt + intensity;
								world.getHandle().b(EnumSkyBlock.BLOCK, LocationX + x, LocationY + y, LocationZ + z, newIntensity);
								lightMap.put(locationString, playerList);
							}
						}
					}else if(v.isInSphere(origin,extendedRadius)){
						Location blockLocation = new Location(world, LocationX+x, LocationY+y, LocationZ+z);
						String locationString = (world.getName()+":"+blockLocation.getBlockX()+":"+blockLocation.getBlockY()+":"+blockLocation.getBlockZ());
						if(lightMap.containsKey(locationString)){
							playerList = lightMap.get(locationString);
							if(playerList.contains(player)){
								int currentInt = world.getHandle().getLightLevel(LocationX + x, LocationY + y, LocationZ + z);
								world.getHandle().b(EnumSkyBlock.BLOCK, LocationX + x, LocationY + y, LocationZ + z, (currentInt - SNConfigHandler.priestLightIntensity));
								playerList.remove(player);
								if(playerList.isEmpty())
									lightMap.remove(locationString);
							}
						}
					}
				}
			}
		}
	}
	
	public static void purgePlayer(Player player){
		List<Player> playerList = new ArrayList<Player>();
		for(String key : lightMap.keySet()){
			playerList = lightMap.get(key);
			if(playerList.contains(player)){
				String[] locationStrings = key.split(":");
				CraftWorld world = (CraftWorld) SupernaturalsPlugin.instance.getServer().getWorld(locationStrings[0]);
				int currentInt = world.getHandle().getLightLevel(Integer.parseInt(locationStrings[1]), Integer.parseInt(locationStrings[2]), Integer.parseInt(locationStrings[3]));
				world.getHandle().b(EnumSkyBlock.BLOCK, Integer.parseInt(locationStrings[1]), Integer.parseInt(locationStrings[2]), Integer.parseInt(locationStrings[3]), (currentInt - SNConfigHandler.priestLightIntensity));
				playerList.remove(player);
				if(playerList.isEmpty()){
					lightMap.remove(locationStrings);
				}
			}
		}
	}
}
