package me.matterz.supernaturals.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.util.config.Configuration;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.util.Recipes;

public class SNConfigHandler {

	public static SupernaturalsPlugin plugin;
	
	//Config variables
	public static Configuration config;
	public static boolean debugMode;
	public static boolean vampireKillSpreadCurse;
	public static boolean ghoulKillSpreadCurse;
	public static boolean wereKillSpreadCurse;
	public static boolean vampireBurnInSunlight;
	public static boolean vampireBurnMessageEnabled;
	public static boolean wolfTruce;
	public static boolean enableColors;
	public static double spreadChance;
	public static double vampireDamageFactor;
	public static double ghoulDamageFactor;
	public static double woodFactor;
	public static double vampireDamageReceivedFactor;
	public static double ghoulDamageReceivedFactor;
	public static double jumpDeltaSpeed;
	public static double dashDeltaSpeed;
	public static double ghoulHealthGained;
	public static double wereHealthGained;
	public static double vampireAltarInfectMaterialRadius;
	public static double vampireAltarCureMaterialRadius;
	public static double vampireTimePowerGained;
	public static double vampireTimeHealthGained;
	public static double vampireHealthCost;
	public static double wereDamageFall;
	public static double wereDamageFactor;
	public static double priestDamageFactorAttackSuper;
	public static double priestDamageFactorAttackHuman;
	public static double priestDrainFactor;
	public static double hunterPowerArrowDamage;
	public static int jumpBloodCost;
	public static int dashBloodCost;
	public static int truceBreakTime;
	public static int vampireAltarInfectMaterialSurroundCount;
	public static int vampireAltarCureMaterialSurroundCount;
	public static int vampirePowerStart;
	public static int ghoulPowerStart;
	public static int ghoulDamageWater;
	public static int werePowerStart;
	public static int vampireDeathPowerPenalty;
	public static int ghoulDeathPowerPenalty;
	public static int wereDeathPowerPenalty;
	public static int priestDeathPowerPenalty;
	public static int vampireKillPowerCreatureGain;
	public static int ghoulKillPowerCreatureGain;
	public static int wereKillPowerCreatureGain;
	public static int vampireKillPowerPlayerGain;
	public static int ghoulKillPowerPlayerGain;
	public static int wereKillPowerPlayerGain;
	public static int vampireCombustFireTicks;
	public static int vampireDrowningCost;
	public static int vampireTeleportCost;
	public static int priestPowerBanish;
	public static int priestPowerHeal;
	public static int priestPowerCure;
	public static int priestPowerExorcise;
	public static int priestPowerDrain;
	public static int priestHealAmount;
	public static int priestPowerStart;
	public static int priestFireTicks;
	public static int werePowerSummonCost;
	public static int werePowerFood;
	public static int ghoulPowerSummonCost;
	public static int demonHealing;
	public static int demonDeathPowerPenalty;
	public static int demonPowerFireball;
	public static int demonPowerSnare;
	public static int demonSnareDuration;
	public static int demonPowerGain;
	public static int demonPowerLoss;
	public static int hunterDeathPowerPenalty;
	public static int hunterPowerArrowFire;
	public static int hunterPowerArrowTriple;
	public static int hunterPowerArrowGrapple;
	public static int hunterPowerArrowPower;
	public static int hunterCooldown;
	public static int demonKillPowerCreatureGain;
	public static int demonKillPowerPlayerGain;
	public static int hunterKillPowerPlayerGain;
	public static int hunterFallReduction;
	public static int hunterFireArrowFireTicks;
	public static String vampireAltarInfectMaterial;
	public static String vampireAltarCureMaterial;
	public static String vampireAltarInfectMaterialSurround;
	public static String vampireAltarCureMaterialSurround;
	public static String priestAltarMaterial;
	public static String wolfMaterial;
	public static String wolfbaneMaterial;
	public static String ghoulMaterial;
	public static String vampireMaterial;
	public static String jumpMaterial;
	public static String dashMaterial;
	public static String demonMaterial;
	public static String demonSnareMaterial;
	public static Location vampireTeleportLocation;
	public static Location priestChurchLocation;
	public static Location priestBanishLocation;
	public static List<String> supernaturalTypes = new ArrayList<String>();
	public static List<String> hunterArrowTypes = new ArrayList<String>();
	
	public static List<Material> woodMaterials = new ArrayList<Material>();
	public static List<CreatureType> vampireTruce = new ArrayList<CreatureType>();
	public static List<Material> foodMaterials = new ArrayList<Material>();
	public static List<Material> ghoulWeapons = new ArrayList<Material>();
	public static List<Material> ghoulWeaponImmunity = new ArrayList<Material>();
	public static List<CreatureType> ghoulTruce = new ArrayList<CreatureType>();
	public static List<Material> priestSpellMaterials = new ArrayList<Material>();
	public static HashMap<Material, Integer> priestDonationMap = new HashMap<Material, Integer>();
	public static List<Material> burnableBlocks = new ArrayList<Material>();
	public static List<Material> hunterArmor = new ArrayList<Material>();
	
	public static String vampireTeleportWorld;
	public static int vampireTeleportLocationX;
	public static int vampireTeleportLocationY;
	public static int vampireTeleportLocationZ;
	public static String priestChurchWorld;
	public static int priestChurchLocationX;
	public static int priestChurchLocationY;
	public static int priestChurchLocationZ;
	public static String priestBanishWorld;
	public static int priestBanishLocationX;
	public static int priestBanishLocationY;
	public static int priestBanishLocationZ;
	
	private static List<String> ghoulWeaponsString = new ArrayList<String>();
	private static List<String> ghoulWeaponImmunityString = new ArrayList<String>();
	private static List<String> woodMaterialsString = new ArrayList<String>();
	private static List<String> vampireTruceString = new ArrayList<String>();
	private static List<String> foodMaterialsString = new ArrayList<String>();
	private static List<String> ghoulTruceString = new ArrayList<String>();
	private static List<String> vampireAltarInfectMaterialsString = new ArrayList<String>();
	private static List<String> vampireAltarCureMaterialsString = new ArrayList<String>();
	private static List<Integer> vampireAltarCureQuantities = new ArrayList<Integer>();
	private static List<Integer> vampireAltarInfectQuantities = new ArrayList<Integer>();
	private static List<String> priestMaterialsString = new ArrayList<String>();
	private static List<String> priestAltarMaterialsString = new ArrayList<String>();
	private static List<Integer> priestAltarQuantities = new ArrayList<Integer>();
	private static List<String> priestDonationMaterialsString = new ArrayList<String>();
	private static List<String> wereWolfbaneMaterialsString = new ArrayList<String>();
	private static List<Integer> wereWolfbaneQuantities = new ArrayList<Integer>();
	private static List<Integer> priestDonationRewards = new ArrayList<Integer>();
	private static List<String> burnableBlocksString = new ArrayList<String>();
	private static List<String> hunterArmorString = new ArrayList<String>();
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>();
	public static HashSet<Byte> transparent = new HashSet<Byte>();
	
	public static Recipes vampireAltarInfectRecipe = new Recipes();
	public static Recipes vampireAltarCureRecipe = new Recipes();
	public static Recipes priestAltarRecipe = new Recipes();
	public static Recipes wereWolfbaneRecipe = new Recipes();
	
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
		
		transparent.add((byte)Material.WATER.getId());
		transparent.add((byte)Material.STATIONARY_WATER.getId());
		transparent.add((byte)Material.AIR.getId());
	}
	
	public SNConfigHandler(SupernaturalsPlugin instance){
		SNConfigHandler.plugin = instance;
	}
	
	public static void getConfiguration(){ 
		config = plugin.getConfiguration();
		loadValues(config);
	}
	
	public static void loadValues(Configuration config){
		debugMode = config.getBoolean("DebugMode", true);
		enableColors = config.getBoolean("EnableChatColors", true);
		truceBreakTime = config.getInt("Supernatural.Truce.BreakTime", 120000);
		supernaturalTypes = config.getStringList("Supernatural.Types", null);
		spreadChance = config.getDouble("Supernatural.SpreadChance", 0.35);

		woodMaterialsString = config.getStringList("Material.Wooden", null);
		foodMaterialsString = config.getStringList("Material.Food", null);

		jumpMaterial = config.getString("Vampire.Materials.Jump", "RED_ROSE");

		vampirePowerStart = config.getInt("Vampire.Power.Start", 10000);
		vampireKillSpreadCurse = config.getBoolean("Vampire.Kill.SpreadCurse",true);
		vampireTimePowerGained = config.getDouble("Vampire.Time.PowerGained", 15);
		vampireKillPowerCreatureGain = config.getInt("Vampire.Power.Kill.CreatureGain", 100);
		vampireKillPowerPlayerGain = config.getInt("Vampire.Power.Kill.PlayerGain", 500);
		vampireDeathPowerPenalty = config.getInt("Vampire.Power.DeathPenalty", 10000);
		vampireDamageFactor = config.getDouble("Vampire.DamageFactor.AttackBonus", 0.3);
		vampireDamageReceivedFactor = config.getDouble("Vampire.DamageFactor.DefenseBonus", 0.8);
		woodFactor = config.getDouble("Vampire.DamageFactor.Wood", 1.5);
		vampireBurnInSunlight = config.getBoolean("Vampire.Burn.InSunlight", true);
		vampireBurnMessageEnabled = config.getBoolean("Vampire.Burn.MessageEnabled", true);
		vampireCombustFireTicks = config.getInt("Vampire.Burn.FireTicks", 3);

		jumpDeltaSpeed = config.getDouble("Vampire.JumpDelta", 1.2);
		jumpBloodCost = config.getInt("Vampire.Power.JumpCost", 1000);
		vampireTimeHealthGained = config.getDouble("Vampire.Time.HealthGained", 0.5);
		vampireHealthCost = config.getDouble("Vampire.Power.HealingCost",60);
		vampireDrowningCost = config.getInt("Vampire.Power.DrowningCost", 90);
		vampireTeleportCost = config.getInt("Vampire.Power.TeleportCost", 9000);
		vampireTruceString = config.getStringList("Vampire.Truce.Creatures", null);
		vampireMaterial = config.getString("Vampire.Spell.Material", "BOOK");

		vampireAltarInfectMaterial = config.getString("Vampire.Altar.Infect.Material","GOLD_BLOCK");
		vampireAltarInfectMaterialSurround = config.getString("Vampire.Altar.Infect.Surrounding.Material","OBSIDIAN");
		vampireAltarInfectMaterialRadius = config.getDouble("Vampire.Altar.Infect.Surrounding.Radius",7D);
		vampireAltarInfectMaterialSurroundCount = config.getInt("Vampire.Altar.Infect.Surrounding.Count",20);
		vampireAltarInfectMaterialsString = config.getStringList("Vampire.Altar.Infect.Recipe.Materials", null);
		vampireAltarInfectQuantities = config.getIntList("Vampire.Altar.Infect.Recipe.Quantities", null);

		vampireAltarCureMaterial = config.getString("Vampire.Altar.Cure.Material","LAPIS_BLOCK");
		vampireAltarCureMaterialSurround = config.getString("Vampire.Altar.Cure.Surrounding.Material","GLOWSTONE");
		vampireAltarCureMaterialRadius = config.getDouble("Vampire.Altar.Cure.Surrounding.Radius", 7D);
		vampireAltarCureMaterialSurroundCount = config.getInt("Vampire.Altar.Cure.Surrounding.Count",20);
		vampireAltarCureMaterialsString = config.getStringList("Vampire.Altar.Cure.Recipe.Materials", null);
		vampireAltarCureQuantities = config.getIntList("Vampire.Altar.Cure.Recipe.Quantities", null);

		vampireTeleportWorld = config.getString("Vampire.Teleport.World", "world");
		vampireTeleportLocationX = config.getInt("Vampire.Teleport.Location.X", 0);
		vampireTeleportLocationY = config.getInt("Vampire.Teleport.Location.Y", 80);
		vampireTeleportLocationZ = config.getInt("Vampire.Teleport.Location.Z", 0);

		priestChurchWorld = config.getString("Priest.Church.World", "world");
		priestChurchLocationX = config.getInt("Priest.Church.Location.X", 0);
		priestChurchLocationY = config.getInt("Priest.Church.Location.Y", 80);
		priestChurchLocationZ = config.getInt("Priest.Church.Location.Z", 0);
		priestBanishWorld = config.getString("Priest.Banish.World", "world");
		priestBanishLocationX = config.getInt("Priest.Banish.Location.X", 0);
		priestBanishLocationY = config.getInt("Priest.Banish.Location.Y", 80);
		priestBanishLocationZ = config.getInt("Priest.Banish.Location.Z", 0);

		priestPowerStart = config.getInt("Priest.Power.StartingAmount", 10000);
		priestDeathPowerPenalty = config.getInt("Priest.Power.DeathPenalty", 1500);
		priestDamageFactorAttackSuper = config.getDouble("Priest.DamageFactor.AttackBonusSuper", 1.0);
		priestDamageFactorAttackHuman = config.getDouble("Priest.DamageFactor.AttackBonusHuman", 0);
		priestPowerBanish = config.getInt("Priest.Power.Banish", 4000);
		priestPowerHeal = config.getInt("Priest.Power.HealOther", 1000);
		priestHealAmount = config.getInt("Priest.Spell.HealAmount", 10);
		priestPowerExorcise = config.getInt("Priest.Power.Exorcise", 9000);
		priestPowerCure = config.getInt("Priest.Power.Cure", 1000);
		priestPowerDrain = config.getInt("Priest.Power.Drain", 1000);
		priestDrainFactor = config.getDouble("Priest.Spell.DrainFactor", 0.15);
		priestAltarMaterial = config.getString("Priest.Church.AltarMaterial","DIAMOND_BLOCK");
		priestMaterialsString = config.getStringList("Priest.Spell.Material", null);
		priestAltarMaterialsString = config.getStringList("Priest.Church.Recipe.Materials", null);
		priestAltarQuantities = config.getIntList("Priest.Church.Recipe.Quantities", null);
		priestDonationMaterialsString = config.getStringList("Priest.Church.Donation.Materials", null);
		priestDonationRewards = config.getIntList("Priest.Church.Donation.Rewards", null);

		ghoulPowerStart = config.getInt("Ghoul.Power.Start", 5000);
		ghoulKillSpreadCurse = config.getBoolean("Ghoul.Kill.SpreadCurse", true);
		ghoulKillPowerCreatureGain = config.getInt("Ghoul.Power.Kill.CreatureGain", 200);
		ghoulKillPowerPlayerGain = config.getInt("Ghoul.Power.Kill.PlayerGain", 1000);
		ghoulDeathPowerPenalty = config.getInt("Ghoul.Power.DeathPenalty", 2000);
		ghoulDamageReceivedFactor = config.getDouble("Ghoul.DamageFactor.DefenseBonus", 0.65);
		ghoulWeaponsString = config.getStringList("Material.Weapons", null);
		ghoulTruceString = config.getStringList("Ghoul.TruceString", null);
		ghoulDamageFactor = config.getDouble("Ghoul.DamageFactor.AttackBonus", 2);
		ghoulDamageWater = config.getInt("Ghoul.WaterDamage", 4);
		ghoulHealthGained = config.getDouble("Ghoul.Time.HealthGained", 0.1);
		ghoulMaterial = config.getString("Ghoul.Summon.Material", "PORK");
		ghoulPowerSummonCost = config.getInt("Ghoul.Power.Summon", 1000);
		ghoulWeaponImmunityString = config.getStringList("Ghoul.Immunity", null);
		dashDeltaSpeed = config.getDouble("Were.DashDelta", 4);
		dashBloodCost = config.getInt("Were.Power.Dash", 400);

		werePowerStart = config.getInt("Were.Power.Start", 5000);
		wereKillSpreadCurse = config.getBoolean("Were.Kill.SpreadCurse", true);
		wereKillPowerCreatureGain = config.getInt("Were.Power.Kill.CreatureGain", 20);
		wereKillPowerPlayerGain = config.getInt("Were.Power.Kill.PlayerGain", 100);
		werePowerFood = config.getInt("Were.Power.Food", 100);
		wereDeathPowerPenalty = config.getInt("Were.Power.DeathPenalty", 2000);
		wereDamageFall = config.getDouble("Were.DamageFactor.Fall", 0.5);
		wereDamageFactor = config.getDouble("Were.DamageFactor.AttackBonus", 5);
		wereHealthGained = config.getDouble("Were.Time.HealthGained", 0.2);
		wolfMaterial = config.getString("Were.Material.Summon", "PORK");
		werePowerSummonCost = config.getInt("Were.Power.Summon", 2000);
		wolfTruce = config.getBoolean("Were.WolfTruce", true);
		dashMaterial = config.getString("Were.Material.Dash", "FEATHER");
		wolfbaneMaterial = config.getString("Were.Wolfbane.Trigger", "BOWL");
		wereWolfbaneMaterialsString = config.getStringList("Were.Wolfbane.Materials", null);
		wereWolfbaneQuantities = config.getIntList("Were.Wolfbane.Quantities", null);
		
		demonHealing = config.getInt("Demon.Healing", 1);
		demonDeathPowerPenalty = config.getInt("Demon.Power.DeathPenalty", 1000);
		demonMaterial = config.getString("Demon.FireballMaterial", "REDSTONE");
		demonPowerFireball = config.getInt("Demon.Power.Fireball", 200);
		demonPowerGain = config.getInt("Demon.Power.Gain", 30);
		demonPowerLoss = config.getInt("Demon.Power.Loss", 10);
		demonKillPowerCreatureGain = config.getInt("Demon.Power.CreatureKill", 10);
		demonKillPowerPlayerGain = config.getInt("Demon.Power.PlayerKill", 10);
		demonPowerSnare = config.getInt("Demon.Power.Snare", 200);
		demonSnareDuration = config.getInt("Demon.Snare.Duration", 10000);
		demonSnareMaterial = config.getString("Demon.Snare.Material", "INK_SACK");
		
		hunterDeathPowerPenalty = config.getInt("WitchHunter.Power.DeathPenalty", 1000);
		hunterPowerArrowFire = config.getInt("WitchHunter.Power.ArrowFire", 200);
		hunterPowerArrowTriple = config.getInt("WitchHunter.Power.ArrowTriple", 200);
		hunterPowerArrowGrapple = config.getInt("WitchHunter.Power.ArrowGrapple", 1000);
		hunterPowerArrowPower = config.getInt("WitchHunter.Power.ArrowPower", 1000);
		hunterPowerArrowDamage = config.getDouble("WitchHunter.ArrowPower.DamageFactor", 2.0);
		hunterArmorString = config.getStringList("WitchHunter.Armor", null);
		hunterCooldown = config.getInt("WitchHunter.PowerArrow.Cooldown", 15000);
		hunterFallReduction = config.getInt("WitchHunter.FallReduction", 2);
		hunterFireArrowFireTicks = config.getInt("WitchHunter.FireArrow.FireTicks", 100);
		hunterArrowTypes = config.getStringList("WitchHunter.ArrowTypes", null);
		
		if(supernaturalTypes.size() == 0){
			supernaturalTypes.add("human");
			supernaturalTypes.add("vampire");
			supernaturalTypes.add("werewolf");
			supernaturalTypes.add("ghoul");
			supernaturalTypes.add("priest");
			supernaturalTypes.add("demon");
			supernaturalTypes.add("witchhunter");
			config.setProperty("Supernatural.Types", supernaturalTypes);
		}
		
		if(woodMaterialsString.size() == 0){
			woodMaterialsString.add("STICK");
			woodMaterialsString.add("WOOD_AXE");
			woodMaterialsString.add("WOOD_HOE");
			woodMaterialsString.add("WOOD_PICKAXE");
			woodMaterialsString.add("WOOD_SPADE");
			woodMaterialsString.add("WOOD_SWORD");
			woodMaterialsString.add("BOW");
			config.setProperty("Material.Wooden", woodMaterialsString);
		}
		
		if(foodMaterialsString.size() == 0){
			foodMaterialsString.add("APPLE");
			foodMaterialsString.add("BREAD");
			foodMaterialsString.add("COOKED_FISH");
			foodMaterialsString.add("GRILLED_PORK");
			foodMaterialsString.add("GOLDEN_APPLE");
			foodMaterialsString.add("MUSHROOM_SOUP");
			foodMaterialsString.add("RAW_FISH");
			foodMaterialsString.add("PORK");
			foodMaterialsString.add("CAKE");
			foodMaterialsString.add("COOKIE");
			config.setProperty("Material.Food", foodMaterialsString);
		}
		
		if(burnableBlocksString.size() == 0){
			burnableBlocksString.add("GRASS");
			burnableBlocksString.add("LEAVES");
			burnableBlocksString.add("AIR");
			burnableBlocksString.add("SEEDS");
			burnableBlocksString.add("WOOD");
			burnableBlocksString.add("BOOKSHELF");
			config.setProperty("BurnableBlocks", burnableBlocksString);
		}
		
		if(vampireTruceString.size() == 0){
			vampireTruceString.add("CREEPER");
			vampireTruceString.add("SKELETON");
			vampireTruceString.add("ZOMBIE");
			config.setProperty("Vampire.Truce.Creatures", vampireTruceString);
		}
		
		if(vampireAltarInfectMaterialsString.size() == 0){
			vampireAltarInfectMaterialsString.add("MUSHROOM_SOUP");
			vampireAltarInfectMaterialsString.add("BONE");
			vampireAltarInfectMaterialsString.add("SULPHUR");
			vampireAltarInfectMaterialsString.add("REDSTONE");
			config.setProperty("Vampire.Altar.Infect.Recipe.Materials", vampireAltarInfectMaterialsString);
		}
		
		if(vampireAltarInfectQuantities.size() == 0){
			vampireAltarInfectQuantities.add(1);
			vampireAltarInfectQuantities.add(10);
			vampireAltarInfectQuantities.add(10);
			vampireAltarInfectQuantities.add(10);
			config.setProperty("Vampire.Altar.Infect.Recipe.Quantities",vampireAltarInfectQuantities);
		}
		
		if(vampireAltarCureMaterialsString.size() == 0){
			vampireAltarCureMaterialsString.add("WATER_BUCKET");
			vampireAltarCureMaterialsString.add("MILK_BUCKET");
			vampireAltarCureMaterialsString.add("SUGAR");
			vampireAltarCureMaterialsString.add("WHEAT");
			config.setProperty("Vampire.Altar.Cure.Recipe.Materials", vampireAltarCureMaterialsString);
		}
		
		if(vampireAltarCureQuantities.size() == 0){
			vampireAltarCureQuantities.add(1);
			vampireAltarCureQuantities.add(1);
			vampireAltarCureQuantities.add(20);
			vampireAltarCureQuantities.add(20);
			config.setProperty("Vampire.Altar.Cure.Recipe.Quantities",vampireAltarCureQuantities);
		}
		
		if(priestAltarMaterialsString.size() == 0){
			priestAltarMaterialsString.add("DIAMOND");
			priestAltarMaterialsString.add("GOLD_INGOT");
			priestAltarMaterialsString.add("GLOWSTONE_DUST");
			priestAltarMaterialsString.add("REDSTONE");
			priestAltarMaterialsString.add("BREAD");
			config.setProperty("Priest.Church.Recipe.Materials", priestAltarMaterialsString);
		}
		
		if(priestAltarQuantities.size() == 0){
			priestAltarQuantities.add(2);
			priestAltarQuantities.add(4);
			priestAltarQuantities.add(4);
			priestAltarQuantities.add(8);
			priestAltarQuantities.add(30);
			config.setProperty("Priest.Church.Recipe.Quantities", priestAltarQuantities);
		}
		
		if(priestMaterialsString.size() == 0){
			priestMaterialsString.add("FEATHER"); 	//Banish
			priestMaterialsString.add("SUGAR");		//Exorcise
			priestMaterialsString.add("FLINT");		//Cure
			priestMaterialsString.add("PAPER");		//Heal
			priestMaterialsString.add("BOOK");		//Drain Power
			config.setProperty("Priest.Spell.Material", priestMaterialsString);
		}
		
		if(priestDonationMaterialsString.size() == 0){
			priestDonationMaterialsString.add("APPLE");
			priestDonationMaterialsString.add("RAW_FISH");
			priestDonationMaterialsString.add("COOKED_FISH");
			priestDonationMaterialsString.add("GRILLED_PORK");
			priestDonationMaterialsString.add("BREAD");
			config.setProperty("Priest.Church.Donation.Materials", priestDonationMaterialsString);
		}
		
		if(priestDonationRewards.size() == 0){
			priestDonationRewards.add(9000);
			priestDonationRewards.add(510);
			priestDonationRewards.add(500);
			priestDonationRewards.add(210);
			priestDonationRewards.add(80);
			config.setProperty("Priest.Church.Donation.Rewards", priestDonationRewards);
		}
		
		if(ghoulWeaponsString.size() == 0){
			ghoulWeaponsString.add("BOW");
			ghoulWeaponsString.add("STICK");
			ghoulWeaponsString.add("WOOD_SWORD");
			ghoulWeaponsString.add("WOOD_PICKAXE");
			ghoulWeaponsString.add("WOOD_SPADE");
			ghoulWeaponsString.add("WOOD_AXE");
			ghoulWeaponsString.add("WOOD_HOE");
			ghoulWeaponsString.add("STONE_SWORD");
			ghoulWeaponsString.add("STONE_PICKAXE");
			ghoulWeaponsString.add("STONE_SPADE");
			ghoulWeaponsString.add("STONE_AXE");
			ghoulWeaponsString.add("STONE_HOE");
			ghoulWeaponsString.add("IRON_SWORD");
			ghoulWeaponsString.add("IRON_PICKAXE");
			ghoulWeaponsString.add("IRON_SPADE");
			ghoulWeaponsString.add("IRON_AXE");
			ghoulWeaponsString.add("IRON_HOE");
			ghoulWeaponsString.add("GOLD_SWORD");
			ghoulWeaponsString.add("GOLD_PICKAXE");
			ghoulWeaponsString.add("GOLD_SPADE");
			ghoulWeaponsString.add("GOLD_AXE");
			ghoulWeaponsString.add("GOLD_HOE");
			ghoulWeaponsString.add("DIAMOND_SWORD");
			ghoulWeaponsString.add("DIAMOND_PICKAXE");
			ghoulWeaponsString.add("DIAMOND_SPADE");
			ghoulWeaponsString.add("DIAMOND_AXE");
			ghoulWeaponsString.add("DIAMOND_HOE");
			config.setProperty("Material.Weapons", ghoulWeaponsString);
		}
		
		if(ghoulWeaponImmunityString.size() == 0){
			ghoulWeaponImmunityString.add("DIAMOND_SWORD");
			config.setProperty("Ghoul.Immunity", ghoulWeaponImmunityString);
		}
		
		if(ghoulTruceString.size() == 0){
			ghoulTruceString.add("CREEPER");
			ghoulTruceString.add("SKELETON");
			ghoulTruceString.add("ZOMBIE");
			ghoulTruceString.add("PIG_ZOMBIE");
			ghoulTruceString.add("GIANT");
			config.setProperty("Ghoul.Truce.Creatures", ghoulTruceString);
		}
		
		if(wereWolfbaneMaterialsString.size() == 0){
			wereWolfbaneMaterialsString.add("YELLOW_FLOWER");
			wereWolfbaneMaterialsString.add("RED_ROSE");
			wereWolfbaneMaterialsString.add("RED_MUSHROOM");
			wereWolfbaneMaterialsString.add("BROWN_MUSHROOM");
			wereWolfbaneMaterialsString.add("BOWL");
			config.setProperty("Were.Wolfbane.Materials", wereWolfbaneMaterialsString);
		}
		
		if(wereWolfbaneQuantities.size() == 0){
			wereWolfbaneQuantities.add(10);
			wereWolfbaneQuantities.add(10);
			wereWolfbaneQuantities.add(10);
			wereWolfbaneQuantities.add(10);
			wereWolfbaneQuantities.add(1);
			config.setProperty("Were.Wolfbane.Quantities", wereWolfbaneQuantities);
		}
		
		if(hunterArmorString.size() == 0){
			hunterArmorString.add("AIR");
			hunterArmorString.add("LEATHER_HELMET");
			hunterArmorString.add("LEATHER_CHESTPLATE");
			hunterArmorString.add("LEATHER_LEGGINGS");
			hunterArmorString.add("LEATHER_BOOTS");
			config.setProperty("WitchHunter.Armor", hunterArmorString);
		}

		if(hunterArrowTypes.size() == 0){
			hunterArrowTypes.add("fire");
			hunterArrowTypes.add("triple");
			hunterArrowTypes.add("power");
			hunterArrowTypes.add("grapple");
			hunterArrowTypes.add("normal");
			config.setProperty("WitchHunter.ArrowTypes", hunterArrowTypes);
		}
		
		config.save();
		
		for(String wood : woodMaterialsString){
			woodMaterials.add(Material.getMaterial(wood));
		}
		
		for(String food : foodMaterialsString){
			foodMaterials.add(Material.getMaterial(food));
		}
		
		for(String block : burnableBlocksString){
			burnableBlocks.add(Material.getMaterial(block));
		}
		
		for(String creature : vampireTruceString){
			CreatureType cType = CreatureType.valueOf(creature);
			if(cType!=null)
				vampireTruce.add(cType);
		}
		
		for(String material : priestMaterialsString){
			priestSpellMaterials.add(Material.getMaterial(material));
		}
		
		for(String creature : ghoulTruceString){
			CreatureType cType = CreatureType.valueOf(creature);
			if(cType!=null)
				ghoulTruce.add(cType);
		}
		
		for(String weapon : ghoulWeaponsString){
			ghoulWeapons.add(Material.getMaterial(weapon));
		}
		
		for(String weapon : ghoulWeaponImmunityString){
			ghoulWeaponImmunity.add(Material.getMaterial(weapon));
		}
		
		for(String armor : hunterArmorString){
			hunterArmor.add(Material.getMaterial(armor));
		}
		
		for(int i=0; i<vampireAltarInfectMaterialsString.size();i++){
			Material material = Material.getMaterial(vampireAltarInfectMaterialsString.get(i));
			int quantity=1;
			try{
				quantity = vampireAltarInfectQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Vampire Infect Altar Quantities!");
			}				
			vampireAltarInfectRecipe.materialQuantities.put(material,quantity);
		}
		
		for(int i=0; i<vampireAltarCureMaterialsString.size();i++){
			Material material = Material.getMaterial(vampireAltarCureMaterialsString.get(i));
			int quantity=1;
			try{
				quantity = vampireAltarCureQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Vampire Cure Altar Quantities!");
			}
			vampireAltarCureRecipe.materialQuantities.put(material,quantity);
		}
		
		for(int i=0; i<priestAltarMaterialsString.size();i++){
			Material material = Material.getMaterial(priestAltarMaterialsString.get(i));
			int quantity=1;
			try{
				quantity = priestAltarQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Priest Altar Quantities!");
			}
			priestAltarRecipe.materialQuantities.put(material,quantity);
		}
		
		for(int i=0; i<wereWolfbaneMaterialsString.size();i++){
			Material material = Material.getMaterial(wereWolfbaneMaterialsString.get(i));
			int quantity = 1;
			try{
				quantity = wereWolfbaneQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Wolfbane Quantities!");
			}
			wereWolfbaneRecipe.materialQuantities.put(material,quantity);
		}
		
		for(int i=0; i<priestDonationMaterialsString.size(); i++){
			Material material = Material.getMaterial(priestDonationMaterialsString.get(i));
			int reward = 1;
			try{
				reward = priestDonationRewards.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid priest donation reward!");
			}
			priestDonationMap.put(material,reward);
		}
		
		priestChurchLocation = new Location(plugin.getServer().getWorld(priestChurchWorld), priestChurchLocationX, priestChurchLocationY, priestChurchLocationZ);
		priestBanishLocation = new Location(plugin.getServer().getWorld(priestBanishWorld), priestBanishLocationX, priestBanishLocationY, priestBanishLocationZ);
		vampireTeleportLocation = new Location(plugin.getServer().getWorld(vampireTeleportWorld), vampireTeleportLocationX, vampireTeleportLocationY, vampireTeleportLocationZ);
	}
	
	public static void saveConfig(){
		config.save();
	}
	
	public static void reloadConfig(){
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Reloaded configuration file");
		}
		config.load();
		loadValues(config);
	}
	
	public static Configuration getConfig(){
		return config;
	}
	
}
