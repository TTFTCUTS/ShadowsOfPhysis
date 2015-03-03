package ttftcuts.physis.common.compat;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModule {
	
	public CompatModule() {}
	
	public void preInitStart(FMLPreInitializationEvent event, boolean client) {}
	public void preInitEnd(FMLPreInitializationEvent event, boolean client) {}
	public void initStart(FMLInitializationEvent event, boolean client) {}
	public void initEnd(FMLInitializationEvent event, boolean client) {}
	public void postInitStart(FMLPostInitializationEvent event, boolean client) {}
	public void postInitEnd(FMLPostInitializationEvent event, boolean client) {}
	public void loadFinished(FMLLoadCompleteEvent event, boolean client) {}
}
