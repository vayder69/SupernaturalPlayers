package me.matterz.supernaturals.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.util.config.Configuration;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.util.Recipes;

public class SNConfigHandler {

	public static SupernaturalsPlugin plugin;
	
	//Config variables
	public Configuration config;
	public static boolean debugMode;
	public static boolean vampireKillSpreadCurse;
	public static boolean vampireBurnInSunlight;
	public static double vampireDamageFactor;
	public static double woodFactor;
	public static double vampireDamageReceivedFactor;
	public static double jumpDeltaSpeed;
	public static double jumpBloodCost;
	public static double vampireAltarInfectMaterialRadius;
	public static double vampireAltarCureMaterialRadius;
	public static int truceBreakTime;
	public static int maxPower;
	public static int timerInterval;
	public static int vampireAltarInfectMaterialSurroundCount;
	public static int vampireAltarCureMaterialSurroundCount;
	public static int vampireCombustFromTime;
	public static int vampireCombustToTime;
	public static int vampirePowerGainedOverTime;
	public static int vampireAltarInfectStartingPower;
	public static int vampireDeathPowerPenalty;
	public static int vampireKillPowerGain;
	public static int vampireCombustFireTicks;
	public static String vampireAltarInfectMaterial;
	public static String vampireAltarCureMaterial;
	public static String vampireAltarInfectMaterialSurround;
	public static String vampireAltarCureMaterialSurround;
	public static List<String> woodMaterials = new ArrayList<String>();
	public static List<String> vampireTruce = new ArrayList<String>();
	public static List<String> foodMaterials = new ArrayList<String>();
	public static List<String> jumpMaterials = new ArrayList<String>();
	public static List<String> vampireAltarInfectMaterials = new ArrayList<String>();
	public static List<String> vampireAltarCureMaterials = new ArrayList<String>();
	public static List<String> supernaturalTypes = new ArrayList<String>();
	public static List<Integer> vampireAltarCureQuantities = new ArrayList<Integer>();
	public static List<Integer> vampireAltarInfectQuantities = new ArrayList<Integer>();
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>();
	
	public static Recipes vampireAltarInfectRecipe = new Recipes();
	public static Recipes vampireAltarCureRecipe = new Recipes();
	
	static{
		materialOpacity.put(Material.AIR, 0D);
		materialOpacity.put(Material.SAPLING, 0.3D);
		materialOpacity.put(Material.LEAVES, 0.3D);
		materialOpacity.put(Material.GLASS, 0.5D);
		materialOpacity.put(Material.YELLOW_FLOWER, 0.1D);
		materialOpacity.put(Material.RED_ROSE, 0.1D);
		materialOpacity.put(Material.BROWN_MUSHROOM, 0.1D);
		materialOpacity.put(Material.RED_MUSHROOM, 0.1D);
		materialOpacity.put(Material.TORCH, 0.1D);
		materialOpacity.put(Material.FIRE, 0D);
		materialOpacity.put(Material.MOB_SPAWNER, 0.3D);
		materialOpacity.put(Material.REDSTONE_WIRE, 0D);
		materialOpacity.put(Material.CROPS, 0.2D);
		materialOpacity.put(Material.SIGN, 0.1D);
		materialOpacity.put(Material.SIGN_POST, 0.2D);
		materialOpacity.put(Material.LEVER, 0.1D);
		materialOpacity.put(Material.STONE_PLATE, 0D);
		materialOpacity.put(Material.WOOD_PLATE, 0D);
		materialOpacity.put(Material.REDSTONE_TORCH_OFF, 0.1D);
		materialOpacity.put(Material.REDSTONE_TORCH_ON, 0.1D);
		materialOpacity.put(Material.STONE_BUTTON, 0D);
		materialOpacity.put(Material.SUGAR_CANE_BLOCK, 0.3D);
		materialOpacity.put(Material.FENCE, 0.2D);
		materialOpacity.put(Material.DIODE_BLOCK_OFF, 0D);
		materialOpacity.put(Material.DIODE_BLOCK_ON, 0D);
	}
	
	public SNConfigHandler(SupernaturalsPlugin instance){
		SNConfigHandler.plugin = instance;
	}
	
	public void getConfiguration(){
		  
		config = plugin.getConfiguration();
		truceBreakTime = config.getInt("Supernatural.Truce.BreakTime", 60000);
		maxPower = config.getInt("Supernatural.MaxAllowedPower", 10000);
		supernaturalTypes = config.getStringList("Supernatural.Types", null);
		
		woodMaterials = config.getStringList("Vampire.Materials.Wooden", null);
		foodMaterials = config.getStringList("Vampire.Materials.Food", null);
		
		jumpMaterials = config.getStringList("Vampire.Jump.Materials", null);
		jumpDeltaSpeed = config.getDouble("Vampire.Jump.Delta", 3);
		jumpBloodCost = config.getDouble("Vampire.Jump.PowerCost", 3);
		
		vampireKillSpreadCurse = config.getBoolean("Vampire.Kill.SpreadCurse",true);
		vampireKillPowerGain = config.getInt("Vampire.Kill.PowerGain", 100);
		vampireDeathPowerPenalty = config.getInt("Vampire.Death.PowerPenalty", 200);
		vampireDamageFactor = config.getDouble("Vampire.Damage.Factor", 1.1);
		vampireDamageReceivedFactor = config.getDouble("Vampire.Damage.ReceivedFactor", 0.9);
		woodFactor = config.getDouble("Vampire.Damage.WoodFactor", 1.5);
		vampireBurnInSunlight = config.getBoolean("Vampire.Burn.InSunlight", true);
		vampireCombustFireTicks = config.getInt("Vampire.Burn.FireTicks", 1);
		
		vampireTruce = config.getStringList("Vampire.Truce.Creatures", null);
		vampireCombustFromTime = config.getInt("Vampire.Combust.FromTime", 0);
		vampireCombustToTime = config.getInt("Vampire.Combust.ToTime", 12400);
		vampirePowerGainedOverTime = config.getInt("Vampire.PowerGained.OverTime", 1);
		vampireAltarInfectStartingPower = config.getInt("Vampire.Altar.Infect.StartingPower", 1000);
		
		vampireAltarInfectMaterial = config.getString("Vampire.Altar.Infect.Material","GOLD_BLOCK");
		vampireAltarInfectMaterialSurround = config.getString("Vampire.Altar.Infect.Surrounding.Material","OBSIDIAN");
		vampireAltarInfectMaterialRadius = config.getDouble("Vampire.Altar.Infect.Surrounding.Radius",7D);
		vampireAltarInfectMaterialSurroundCount = config.getInt("Vampire.Altar.Infect.Surrounding.Count",20);
		vampireAltarInfectMaterials = config.getStringList("Vampire.Altar.Infect.Recipe.Materials", null);
		vampireAltarInfectQuantities = config.getIntList("Vampire.Altar.Infect.Recipe.Quantities", null);
		
		vampireAltarCureMaterial = config.getString("Vampire.Altar.Cure.Material","LAPIS_BLOCK");
		vampireAltarCureMaterialSurround = config.getString("Vampire.Altar.Cure.Surrounding.Material","GLOWSTONE");
		vampireAltarCureMaterialRadius = config.getDouble("Vampire.Altar.Cure.Surrounding.Radius", 7D);
		vampireAltarCureMaterialSurroundCount = config.getInt("Vampire.Altar.Cure.Surrounding.Count",20);
		vampireAltarCureMaterials = config.getStringList("Vampire.Altar.Cure.Recipe.Materials", null);
		vampireAltarCureQuantities = config.getIntList("Vampire.Altar.Cure.Recipe.Quantities", null);
		
		if(supernaturalTypes.size() == 0){
			supernaturalTypes.add("human");
			supernaturalTypes.add("vampire");
			supernaturalTypes.add("werewolf");
			supernaturalTypes.add("ghoul");
			supernaturalTypes.add("priest");
			config.setProperty("Supernatural.Types", supernaturalTypes);
		}
		
		if(woodMaterials.size() == 0){
			woodMaterials.add("STICK");
			woodMaterials.add("WOOD_AXE");
			woodMaterials.add("WOOD_HOE");
			woodMaterials.add("WOOD_PICKAXE");
			woodMaterials.add("WOOD_SPADE");
			woodMaterials.add("WOOD_SWORD");
			config.setProperty("Vampire.Materials.Wooden", woodMaterials);
		}
		
		if(foodMaterials.size() == 0){
			foodMaterials.add("APPLE");
			foodMaterials.add("BREAD");
			foodMaterials.add("COOKED_FISH");
			foodMaterials.add("GRILLED_PORK");
			foodMaterials.add("GOLDEN_APPLE");
			foodMaterials.add("MUSHROOM_SOUP");
			foodMaterials.add("RAW_FISH");
			foodMaterials.add("PORK");
			config.setProperty("Vampire.Materials.Food", foodMaterials);
		}
		
		if(jumpMaterials.size() == 0){
			jumpMaterials.add("RED_ROSE");
			config.setProperty("Vampire.Jump.Materials", jumpMaterials);
		}
		
		if(vampireTruce.size() == 0){
			vampireTruce.add("CREEPER");
			vampireTruce.add("GHAST");
			vampireTruce.add("SKELETON");
			vampireTruce.add("SPIDER");
			vampireTruce.add("ZOMBIE");
			config.setProperty("Vampire.Truce.Creatures", vampireTruce);
		}
		
		if(vampireAltarInfectMaterials.size() == 0){
			vampireAltarInfectMaterials.add("MUSHROOM_SOUP");
			vampireAltarInfectMaterials.add("BONE");
			vampireAltarInfectMaterials.add("SULPHUR");
			vampireAltarInfectMaterials.add("REDSTONE");
			config.setProperty("Vampire.Altar.Infect.Recipe.Materials", vampireAltarInfectMaterials);
		}
		
		if(vampireAltarInfectQuantities.size() == 0){
			vampireAltarInfectQuantities.add(1);
			vampireAltarInfectQuantities.add(10);
			vampireAltarInfectQuantities.add(10);
			vampireAltarInfectQuantities.add(10);
			config.setProperty("Vampire.Altar.Infect.Recipe.Quantities",vampireAltarInfectQuantities);
		}
		
		if(vampireAltarCureMaterials.size() == 0){
			vampireAltarCureMaterials.add("WATER_BUCKET");
			vampireAltarCureMaterials.add("DIAMOND");
			vampireAltarCureMaterials.add("SUGAR");
			vampireAltarCureMaterials.add("WHEAT");
			config.setProperty("Vampire.Altar.Cure.Recipe.Materials", vampireAltarCureMaterials);
		}
		
		if(vampireAltarCureQuantities.size() == 0){
			vampireAltarCureQuantities.add(1);
			vampireAltarCureQuantities.add(1);
			vampireAltarCureQuantities.add(20);
			vampireAltarCureQuantities.add(20);
			config.setProperty("Vampire.Altar.Cure.Recipe.Quantities",vampireAltarCureQuantities);
		}
		
		timerInterval = config.getInt("TimerInterval", 30000);
		debugMode = config.getBoolean("DebugMode", true);
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
