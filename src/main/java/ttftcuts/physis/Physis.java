package ttftcuts.physis;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ttftcuts.physis.common.PhysisCreativeTab;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.handler.GuiHandler;

@Mod(modid = Physis.MOD_ID, name = "Shadows Of Physis", version = "$version", dependencies = "")
public class Physis {

    public static final String MOD_ID = "physis";
    public static final Logger logger = LogManager.getLogger(MOD_ID);
    //public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    public static PhysisCreativeTab creativeTab;
    
    @Mod.Instance
    public static Physis instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	creativeTab = new PhysisCreativeTab();
    	
    	PhysisItems.init();
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
}
