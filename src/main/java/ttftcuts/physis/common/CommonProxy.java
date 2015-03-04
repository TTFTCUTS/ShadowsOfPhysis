package ttftcuts.physis.common;

import net.minecraftforge.common.MinecraftForge;
import scala.util.Random;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.common.artifact.LootSystem;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.compat.PhysisIntegration;
import ttftcuts.physis.common.crafting.PhysisCraftingRecipes;
import ttftcuts.physis.common.file.ServerData;
import ttftcuts.physis.common.file.ServerData.ServerDataHandler;
import ttftcuts.physis.common.handler.ArtifactEventHandler;
import ttftcuts.physis.common.handler.ChestGenHandler;
import ttftcuts.physis.common.handler.GuiHandler;
import ttftcuts.physis.common.handler.ItemDestructionHandler;
import ttftcuts.physis.common.handler.ServerTickHandler;
import ttftcuts.physis.common.item.ItemTrowel;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import ttftcuts.physis.common.network.packet.PacketGuiMessage;
import ttftcuts.physis.common.network.packet.PacketPlayerUpdate;
import ttftcuts.physis.common.network.packet.PacketStorySeed;
import ttftcuts.physis.common.network.packet.PacketWorldTime;
import ttftcuts.physis.common.story.PhysisStoryVars;
import ttftcuts.physis.common.story.StoryEngine;
import ttftcuts.physis.common.story.StoryEngine.StorySeedHandler;
import ttftcuts.physis.common.worldgen.PhysisWorldGen;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutBuilder;
import ttftcuts.physis.utils.ModFinder;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {		
		PhysisIntegration.preInitStart(event, false);
		
		ModFinder.init();		
		PhysisAPI.init();
		
		PhysisStoryVars.init();
		
    	PhysisItems.init();
    	PhysisBlocks.init();
    	PhysisArtifacts.init();

    	PhysisIntegration.loadModules();
    	
    	PageDefs.init();
    	
    	PhysisCraftingRecipes.init();    	
    	
    	ChestGenHandler.init();
    	
    	Physis.oooBuilder = new OddOneOutBuilder();
    	
    	PhysisToolMaterial.initDefaultTranslators();
    	
    	PhysisWorldGen.init();
    	
    	PhysisIntegration.preInitEnd(event, false);
	}
	
	public void init(FMLInitializationEvent event) {
		PhysisIntegration.initStart(event, false);
		
    	NetworkRegistry.INSTANCE.registerGuiHandler(Physis.instance, new GuiHandler());
    	
    	FMLCommonHandler.instance().bus().register(new ServerTickHandler());
    	MinecraftForge.EVENT_BUS.register(new ArtifactEventHandler());
    	MinecraftForge.EVENT_BUS.register(new ServerDataHandler());
    	FMLCommonHandler.instance().bus().register(new ServerDataHandler());
    	FMLCommonHandler.instance().bus().register(new StorySeedHandler());
    	MinecraftForge.EVENT_BUS.register(new ItemDestructionHandler());
    	
    	networkSetup();
    	LootSystem.init();
    	
    	PhysisIntegration.initEnd(event, false);
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		PhysisIntegration.postInitStart(event, false);

		PhysisIntegration.postInitEnd(event, false);
	}
	
	public void loadFinished(FMLLoadCompleteEvent event) {
		PhysisToolMaterial.buildMaterials();
		ItemTrowel.buildRecipes();
		
		PhysisIntegration.loadFinished(event, false);
	}
	
	public void serverPreStarting(FMLServerAboutToStartEvent event) {
		ServerData.reload(false);
	}
	
	public void serverStarting(FMLServerStartingEvent event) { 
		Physis.oooBuilder.start();

		// set up the story!
		long seed = event.getServer().worldServers[0].getWorldInfo().getSeed();
		Random r = new Random(seed);
		long storySeed = r.nextLong();
		
		StoryEngine.reload(storySeed, false);
	}
	
	public void serverStopping(FMLServerStoppingEvent event) {
		
	}
	
	public void serverStopped(FMLServerStoppedEvent event) {
		Physis.oooBuilder.stop();
	}
		
	private void networkSetup() {
		PhysisPacketHandler.registerPacketHandler(new PacketGuiMessage(), 0);
		PhysisPacketHandler.registerPacketHandler(new PacketWorldTime(), 1);
		PhysisPacketHandler.registerPacketHandler(new PacketStorySeed(), 2);
		PhysisPacketHandler.registerPacketHandler(new PacketPlayerUpdate(), 3);
		
		PhysisPacketHandler.init();
	}
}
