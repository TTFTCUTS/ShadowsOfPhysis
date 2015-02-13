package ttftcuts.physis.common.compat;

import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.compat.baubles.CompatBaubles;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PhysisIntegration {

	private static List<CompatModule> modules = new ArrayList<CompatModule>();
	
	public static void loadModules() {
		registerModule(CompatBaubles.class, "Baubles");
		registerModule(CompatTravellersGear.class, "TravellersGear");
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
	
	public static void preInit(FMLPreInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.preInit(event, client);
		}
	}
	
	public static void init(FMLInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.init(event, client);
		}
	}
	
	public static void postInit(FMLPostInitializationEvent event, boolean client) {
		for (CompatModule module : modules) {
			module.postInit(event, client);
		}
	}
}
