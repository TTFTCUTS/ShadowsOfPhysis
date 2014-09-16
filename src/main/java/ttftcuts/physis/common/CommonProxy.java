package ttftcuts.physis.common;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.common.handler.GuiHandler;
import ttftcuts.physis.common.item.PhysisToolMaterial;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
    	PhysisItems.init();
    	PhysisBlocks.init();
    	
    	PageDefs.init();
    	NetworkRegistry.INSTANCE.registerGuiHandler(Physis.instance, new GuiHandler());
	}
	
	public void init(FMLInitializationEvent event) {
		PhysisToolMaterial.buildMaterials();
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
