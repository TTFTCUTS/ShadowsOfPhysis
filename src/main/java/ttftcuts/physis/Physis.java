package ttftcuts.physis;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.common.CommonProxy;
import ttftcuts.physis.common.PhysisBlocks;
import ttftcuts.physis.common.PhysisCreativeTab;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.artifact.ArtifactHandler;
import ttftcuts.physis.common.helper.LocalizationHelper;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutBuilder;

@Mod(modid = Physis.MOD_ID, name = "Shadows Of Physis", version = "$GRADLEVERSION", dependencies = "after:ThermalFoundation;after:ThermalExpansion")
public class Physis {

    public static final String MOD_ID = "physis";
    public static final Logger logger = LogManager.getLogger(MOD_ID);
   
    public static PhysisCreativeTab creativeTab;
    public static PhysisCreativeTab socketableTab;
    public static PhysisCreativeTab digsiteTab;
    public static OddOneOutBuilder oooBuilder;
    
    @Mod.Instance
    public static Physis instance;
    
    @SidedProxy(serverSide="ttftcuts.physis.common.CommonProxy", clientSide="ttftcuts.physis.client.ClientProxy")
    public static CommonProxy proxy;
        
    public static LocalizationHelper text;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	text = new LocalizationHelper();
    	creativeTab = new PhysisCreativeTab("items");
    	socketableTab = new PhysisCreativeTab("socketable");
    	digsiteTab = new PhysisCreativeTab("digsite");
    	
    	PhysisAPI.artifactHandler = new ArtifactHandler();

    	proxy.preInit(event);
    	
    	creativeTab.setDisplayStack(new ItemStack(PhysisItems.journal));
    	socketableTab.setDisplayStack(new ItemStack(PhysisItems.socketable));
    	digsiteTab.setDisplayStack(new ItemStack(PhysisBlocks.digSiteDirt));
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
    
    @Mod.EventHandler
    public void loadFinished(FMLLoadCompleteEvent event) {
    	proxy.loadFinished(event);
    }
    
    @Mod.EventHandler
    public void serverPreStarting(FMLServerAboutToStartEvent event) {
    	proxy.serverPreStarting(event);
    }
    
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	proxy.serverStarting(event);
    }
    
    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
    	proxy.serverStopping(event);
    }
    
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
    	proxy.serverStopped(event);
    }
}
