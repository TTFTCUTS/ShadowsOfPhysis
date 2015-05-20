package ttftcuts.physis.common.worldgen;

import ttftcuts.physis.common.worldgen.structure.BlockPalette.BlockPalettes;
import ttftcuts.physis.common.worldgen.structure.prop.PropTypes;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class PhysisWorldGen {

	public static IWorldGenerator basicdigsite;
	
	public static void init() {
		basicdigsite = new WorldGenDigSiteBasic();
		
		PropTypes.init();
		BlockPalettes.init();
		
		GameRegistry.registerWorldGenerator(basicdigsite, 1);
	}
}
