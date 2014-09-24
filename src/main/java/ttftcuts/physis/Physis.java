package ttftcuts.physis;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.common.CommonProxy;
import ttftcuts.physis.common.PhysisCreativeTab;
import ttftcuts.physis.common.artifact.ArtifactHandler;
import ttftcuts.physis.common.helper.LocalizationHelper;

@Mod(modid = Physis.MOD_ID, name = "Shadows Of Physis", version = "$version", dependencies = "")
public class Physis {

    public static final String MOD_ID = "physis";
    public static final Logger logger = LogManager.getLogger(MOD_ID);
    //public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    public static PhysisCreativeTab creativeTab;
    
    @Mod.Instance
    public static Physis instance;
    
    @SidedProxy(serverSide="ttftcuts.physis.common.CommonProxy", clientSide="ttftcuts.physis.client.ClientProxy")
    public static CommonProxy proxy;
    
    public static LocalizationHelper text;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	text = new LocalizationHelper();
    	creativeTab = new PhysisCreativeTab();
    	
    	PhysisAPI.artifactHandler = new ArtifactHandler();
    	
    	proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
}
