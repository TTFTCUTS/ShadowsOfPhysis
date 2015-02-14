package ttftcuts.physis.common.worldgen;

import ttftcuts.physis.Physis;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class PhysisWorldGen {

	public static IWorldGenerator basicdigsite;
	
	public static void init() {
		basicdigsite = new WorldGenDigSiteBasic();
		
		Physis.logger.info("Registering world gen");
		GameRegistry.registerWorldGenerator(basicdigsite, 1);
	}
}
