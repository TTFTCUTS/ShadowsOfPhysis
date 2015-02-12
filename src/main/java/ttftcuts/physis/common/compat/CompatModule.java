package ttftcuts.physis.common.compat;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModule {
	
	public CompatModule() {}
	
	public void preInit(FMLPreInitializationEvent event, boolean client) {}
	public void init(FMLInitializationEvent event, boolean client) {}
	public void postInit(FMLPostInitializationEvent event, boolean client) {}
}
