package ttftcuts.physis.common.worldgen;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.worldgen.structure.prop.PropTypes;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class PhysisWorldGen {

	public static IWorldGenerator basicdigsite;
	
	public static void init() {
		basicdigsite = new WorldGenDigSiteBasic();
		
		PropTypes.init();
		
		Physis.logger.info("Registering world gen");
		GameRegistry.registerWorldGenerator(basicdigsite, 1);
	}
}
