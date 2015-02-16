package ttftcuts.physis.common.compat;

import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.compat.baubles.CompatBaubles;
import ttftcuts.physis.common.compat.thaumcraft.CompatThaumcraft;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PhysisIntegration {

	private static List<CompatModule> modules = new ArrayList<CompatModule>();
	
	public static void loadModules() {
		registerModule(CompatBaubles.class, "Baubles");
		registerModule(CompatTravellersGear.class, "TravellersGear");
		registerModule(CompatThaumcraft.class, "Thaumcraft");
		registerModule(CompatThaumicTinkerer.class, "ThaumicTinkerer");
	}
	
	public static void registerModule(Class<? extends CompatModule> clazz, String modid) {
		if (Loader.isModLoaded(modid)) {
			try {
				modules.add(clazz.newInstance());
				Physis.logger.info("Loaded compatibility module: "+modid);
			} catch (Exception e) {
				Physis.logger.warn("Failed to initialize compat module: "+modid, e);
			}
		}
	}
	
	public static void preInitStart(FMLPreInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.preInitStart(event, client);
		}
	}
	public static void preInitEnd(FMLPreInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.preInitEnd(event, client);
		}
	}
	
	public static void initStart(FMLInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.initStart(event, client);
		}
	}
	public static void initEnd(FMLInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.initEnd(event, client);
		}
	}
	
	public static void postInitStart(FMLPostInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.postInitStart(event, client);
		}
	}
	public static void postInitEnd(FMLPostInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.postInitEnd(event, client);
		}
	}
}
