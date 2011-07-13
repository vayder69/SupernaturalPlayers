package me.matterz.supernaturals.io;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.config.Configuration;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.util.Recipes;

public class SNConfigHandler {

	public static SupernaturalsPlugin plugin;
	
	//Config variables
	public Configuration config;
	public static double vampireDamageFactor;
	public static List<String> woodMaterials;
	private List<String> defaultWoodMaterials;
	public static double woodFactor;
	public static double vampireDamageReceivedFactor;
	public static List<String> vampireTruce;
	private List<String> defaultVampireTruce;
	public static int truceBreakTime;
	public static List<String> foodMaterials;
	private List<String> defaultFoodMaterials;
	public static List<String> jumpMaterials;
	private List<String> defaultJumpMaterials;
	public static double jumpDeltaSpeed;
	public static double jumpBloodCost;
	public static int maxPower;
	public static String vampireAltarInfectMaterial;
	public static String vampireAltarCureMaterial;
	public static String vampireAltarInfectMaterialSurround;
	public static double vampireAltarInfectMaterialRadius;
	public static int vampireAltarInfectMaterialSurroundCount;
	public static List<String> vampireAltarInfectMaterials;
	private List<String> defaultVampireAltarInfectMaterials;
	public static List<Integer> vampireAltarInfectQuantities;
	private List<Integer> defaultVampireAltarInfectQuantities;
	public static int timerInterval;
	public static String vampireAltarCureMaterialSurround;
	public static double vampireAltarCureMaterialRadius;
	public static int vampireAltarCureMaterialSurroundCount;
	public static List<String> vampireAltarCureMaterials;
	private List<String> defaultVampireAltarCureMaterials;
	public static List<Integer> vampireAltarCureQuantities;
	private List<Integer> defaultVampireAltarCureQuantities;
	
	public static Recipes vampireAltarInfectRecipe = new Recipes();
	public static Recipes vampireAltarCureRecipe = new Recipes();
	
	public SNConfigHandler(SupernaturalsPlugin instance){
		SNConfigHandler.plugin = instance;
	}
	
	public void getConfiguration(){
		defaultWoodMaterials.add("STICK");
		defaultWoodMaterials.add("WOOD_AXE");
		defaultWoodMaterials.add("WOOD_HOE");
		defaultWoodMaterials.add("WOOD_PICKAXE");
		defaultWoodMaterials.add("WOOD_SPADE");
		defaultWoodMaterials.add("WOOD_SWORD");
		   
		defaultVampireTruce.add("CREEPER");
		defaultVampireTruce.add("GHAST");
		defaultVampireTruce.add("SKELETON");
		defaultVampireTruce.add("SPIDER");
		defaultVampireTruce.add("ZOMBIE");
		   
		defaultFoodMaterials.add("APPLE");
		defaultFoodMaterials.add("BREAD");
		defaultFoodMaterials.add("COOKED_FISH");
		defaultFoodMaterials.add("GRILLED_PORK");
		defaultFoodMaterials.add("GOLDEN_APPLE");
		defaultFoodMaterials.add("MUSHROOM_SOUP");
		defaultFoodMaterials.add("RAW_FISH");
		defaultFoodMaterials.add("PORK");
		   
		defaultJumpMaterials.add("RED_ROSE");
		
		defaultVampireAltarInfectMaterials.add("MUSHROOM_SOUP");
		defaultVampireAltarInfectMaterials.add("BONE");
		defaultVampireAltarInfectMaterials.add("SULPHUR");
		defaultVampireAltarInfectMaterials.add("REDSTONE");
		
		defaultVampireAltarInfectQuantities.add(1);
		defaultVampireAltarInfectQuantities.add(10);
		defaultVampireAltarInfectQuantities.add(10);
		defaultVampireAltarInfectQuantities.add(10);
		
		defaultVampireAltarCureMaterials.add("WATER_BUCKET");
		defaultVampireAltarCureMaterials.add("DIAMOND");
		defaultVampireAltarCureMaterials.add("SUGAR");
		defaultVampireAltarCureMaterials.add("WHEAT");
		
		defaultVampireAltarCureQuantities.add(1);
		defaultVampireAltarCureQuantities.add(1);
		defaultVampireAltarCureQuantities.add(20);
		defaultVampireAltarCureQuantities.add(20);
		  
		config = plugin.getConfiguration();
		vampireDamageFactor = config.getDouble("VampireDamageFactor", 1.1);
		woodMaterials = config.getStringList("WoodenMaterials", defaultWoodMaterials);
		woodFactor = config.getDouble("VampireWoodDamageFactor", 1.5);
		vampireDamageReceivedFactor = config.getDouble("VampireDamageReceivedFactor", 0.9);
		vampireTruce = config.getStringList("VampireTruceCreatures", defaultVampireTruce);
		truceBreakTime = config.getInt("TruceBreakTime", 60000);
		foodMaterials = config.getStringList("FoodMaterials", defaultFoodMaterials);
		jumpMaterials = config.getStringList("JumpMaterials", defaultJumpMaterials);
		jumpDeltaSpeed = config.getDouble("VampireJumpDelta", 3);
		jumpBloodCost = config.getDouble("VampireJumpBloodCost", 3);
		maxPower = config.getInt("MaxAllowedPower", 10000);
		vampireAltarInfectMaterial = config.getString("VampireAltarInfectMaterial","GOLD_BLOCK");
		vampireAltarCureMaterial = config.getString("VampireAltarCureMaterial","LAPIS_BLOCK");
		vampireAltarInfectMaterialSurround = config.getString("VampireInfectAltarSurroudingMaterial","OBSIDIAN");
		vampireAltarInfectMaterialRadius = config.getDouble("VampireInfectAltarSurroundingMaterialRadius",7D);
		vampireAltarInfectMaterialSurroundCount = config.getInt("VampireInfectAltarMaterialSurroundCount",20);
		vampireAltarInfectMaterials = config.getStringList("VampireInfectAltarMaterials", defaultVampireAltarInfectMaterials);
		vampireAltarInfectQuantities = config.getIntList("VampireInfectAltarQuantities", defaultVampireAltarInfectQuantities);
		timerInterval = config.getInt("TimerInterval", 1000);
		vampireAltarCureMaterialSurround = config.getString("VampireCureAltarSurroudingMaterial","GLOWSTONE");
		vampireAltarCureMaterialRadius = config.getDouble("VampireCureAltarSurroundingMaterialRadius", 7D);
		vampireAltarCureMaterialSurroundCount = config.getInt("VampireCureAltarMaterialSurroundCount",20);
		vampireAltarCureMaterials = config.getStringList("VampireCureAltarMaterials", defaultVampireAltarCureMaterials);
		vampireAltarCureQuantities = config.getIntList("VampireCureAltarQuantities", defaultVampireAltarCureQuantities);
		config.save();
		
		for(int i=0; i<vampireAltarInfectMaterials.size();i++){
			Material material = Material.getMaterial(vampireAltarInfectMaterials.get(i));
			int quantity=1;
			try{
				quantity = vampireAltarInfectQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Vampire Infect Altar Quantities!");
			}				
			vampireAltarInfectRecipe.materialQuantities.put(material,quantity);
		}
		
		for(int i=0; i<vampireAltarCureMaterials.size();i++){
			Material material = Material.getMaterial(vampireAltarCureMaterials.get(i));
			int quantity=1;
			try{
				quantity = vampireAltarCureQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Vampire Cure Altar Quantities!");
			}
			vampireAltarCureRecipe.materialQuantities.put(material,quantity);
		}
	}
	
	public void saveConfig(){
		config.save();
	}
	
	public void reloadConfig(){
		config = new Configuration(new File(plugin.getDataFolder().getPath() + "/config.yml"));
	}
}
