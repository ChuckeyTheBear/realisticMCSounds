package com.sonicether.soundphysics;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {

	public static final Config instance;
	private Configuration forgeConfig;

	// general
	public static float rolloffFactor;
	public static float globalReverbGain;
	public static float globalReverbBrightness;
	public static float soundDistanceAllowance;
	public static float globalBlockAbsorption;
	public static float globalBlockReflectance;
	public static float airAbsorption;
	public static float underwaterFilter;

	// performance
	public static boolean skipRainOcclusionTracing;
	public static int environmentEvaluationRays;
	public static boolean simplerSharedAirspaceSimulation;

	// block properties
	public static float stoneReflectivity;
	public static float woodReflectivity;
	public static float groundReflectivity;
	public static float plantReflectivity;
	public static float metalReflectivity;
	public static float glassReflectivity;
	public static float clothReflectivity;
	public static float sandReflectivity;
	public static float snowReflectivity;

	private static final String categoryGeneral = "General";
	private static final String categoryPerformance = "Performance";
	private static final String categoryMaterialProperties = "Material properties";

	static {
		instance = new Config();
	}

	private Config() {
	}

	public void preInit(final FMLPreInitializationEvent event) {
		this.forgeConfig = new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}

	public void init(final FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(SoundPhysics.modid)) {
			syncConfig();
		}
	}

	public List<IConfigElement> getConfigElements() {
		final ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();

		list.add(new ConfigElement(this.forgeConfig.getCategory(Config.categoryGeneral)));
		list.add(new ConfigElement(this.forgeConfig.getCategory(Config.categoryPerformance)));
		list.add(new ConfigElement(this.forgeConfig.getCategory(Config.categoryMaterialProperties)));

		return list;
	}

	private void syncConfig() {
		//Sound Tuning
		rolloffFactor = this.forgeConfig.getFloat("Attenuation Factor", categoryGeneral, 1.0f, 0.2f, 1.0f, "Factor of intensity of sound to distance, lower numbers are louder. Default 1.0");
		globalReverbGain = this.forgeConfig.getFloat("Global Reverb Gain", categoryGeneral, 1.0f, 0.1f, 2.0f, "Factor of volume gained due to reverberations. Higher numbers are louder. Default 1.0");
		globalReverbBrightness = this.forgeConfig.getFloat("Global Reverb Brightness", categoryGeneral, 1.0f, 0.1f, 2.0f, "The brightness of reverberation. Higher values result in more high frequencies in reverberation. Lower values give a more muffled sound to the reverb.");
		globalBlockAbsorption = this.forgeConfig.getFloat("Global Block Absorption", categoryGeneral, 1.0f, 0.1f, 4.0f, "Amount of sound that blocks will absorb.");
		globalBlockReflectance = this.forgeConfig.getFloat("Global Block Reflectance", categoryGeneral, 1.0f, 0.1f, 4.0f, "The global amount of sound reflectance energy of all blocks. Lower values cause less reverb.");
		soundDistanceAllowance = this.forgeConfig.getFloat("Sound Distance Allowance", categoryGeneral, 4.0f, 1.0f, 6.0f, "Change the distance that sounds will play at. Values too high can cause issues with the sound encoding.");
		airAbsorption = this.forgeConfig.getFloat("Air Absorption", categoryGeneral, 1.0f, 0.0f, 5.0f, "A value controlling how much air absorbs high frequencies with distance. Higher values mean that sounds will be generally quieter.");
		underwaterFilter = this.forgeConfig.getFloat("Underwater Filter", categoryGeneral, 0.8f, 0.0f, 1.0f, "Changes how much the sound changes while underwater. 0.0 disables the effect.");

		// performance
		skipRainOcclusionTracing = this.forgeConfig.getBoolean("Skip Rain Occlusion Tracing", categoryPerformance, true, "Will minorly increase performance during rain.");
		environmentEvaluationRays = this.forgeConfig.getInt("Environment Evaluation Rays", categoryPerformance, 32, 8, 64, "The number of rays to generate when a sound is made. a large number will result in better sound at the cost of performance");
		simplerSharedAirspaceSimulation = this.forgeConfig.getBoolean("Simpler Shared Airspace Simulation", categoryPerformance, false, "Uses a cheaper method to check if the player interacts with a sound. Enabling to true will increase performance at a quality loss.");

		// material properties
		stoneReflectivity = this.forgeConfig.getFloat("Stone Reflectivity", categoryMaterialProperties, 0.95f, 0.0f, 1.0f, "Sound Reflectivity for Stone.");
		woodReflectivity = this.forgeConfig.getFloat("Wood Reflectivity", categoryMaterialProperties, 0.7f, 0.0f, 1.0f, "Sound Reflectivity for Wood.");
		groundReflectivity = this.forgeConfig.getFloat("Ground Reflectivity", categoryMaterialProperties, 0.3f, 0.0f, 1.0f, "Sound Reflectivity for Ground (dirt, gravel).");
		plantReflectivity = this.forgeConfig.getFloat("Foliage Reflectivity", categoryMaterialProperties, 0.2f, 0.0f, 1.0f, "Sound Reflectivity for Foliage (grass, leaves).");
		metalReflectivity = this.forgeConfig.getFloat("Metal Reflectivity", categoryMaterialProperties, 0.97f, 0.0f, 1.0f, "Sound Reflectivity for Metal.");
		glassReflectivity = this.forgeConfig.getFloat("Glass Reflectivity", categoryMaterialProperties, 0.5f, 0.0f, 1.0f, "Sound Reflectivity for Glass.");
		clothReflectivity = this.forgeConfig.getFloat("Cloth Reflectivity", categoryMaterialProperties, 0.25f, 0.0f, 1.0f, "Sound Reflectivity for Cloth (wool).");
		sandReflectivity = this.forgeConfig.getFloat("Sand Reflectivity", categoryMaterialProperties, 0.2f, 0.0f, 1.0f, "Sound Reflectivity for Sand.");
		snowReflectivity = this.forgeConfig.getFloat("Snow Reflectivity", categoryMaterialProperties, 0.2f, 0.0f, 1.0f, "Sound reflectivity for Snow.");

		if (this.forgeConfig.hasChanged()) {
			this.forgeConfig.save();
			SoundPhysics.applyConfigChanges();
		}
	}

}
