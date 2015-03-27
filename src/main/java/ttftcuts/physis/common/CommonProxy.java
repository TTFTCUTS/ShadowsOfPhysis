package ttftcuts.physis.common;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.client.gui.journal.JournalArticle;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.common.artifact.LootSystem;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.compat.PhysisIntegration;
import ttftcuts.physis.common.crafting.PhysisCraftingRecipes;
import ttftcuts.physis.common.file.PhysisWorldSavedData;
import ttftcuts.physis.common.file.ServerData;
import ttftcuts.physis.common.file.ServerData.ServerDataHandler;
import ttftcuts.physis.common.handler.ArtifactEventHandler;
import ttftcuts.physis.common.handler.ChestGenHandler;
import ttftcuts.physis.common.handler.GuiHandler;
import ttftcuts.physis.common.handler.ItemDestructionHandler;
import ttftcuts.physis.common.handler.ServerTickHandler;
import ttftcuts.physis.common.helper.recipe.RecipeHelper;
import ttftcuts.physis.common.item.ItemTrowel;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import ttftcuts.physis.common.network.packet.PacketGuiMessage;
import ttftcuts.physis.common.network.packet.PacketPlayerUpdate;
import ttftcuts.physis.common.network.packet.PacketWorldData;
import ttftcuts.physis.common.network.packet.PacketWorldTime;
import ttftcuts.physis.common.story.Knowledge;
import ttftcuts.physis.common.story.PhysisStoryVars;
import ttftcuts.physis.common.story.StoryEngine;
import ttftcuts.physis.common.file.PhysisWorldSavedData.WorldDataHandler;
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
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {		
		PhysisIntegration.preInitStart(event, false);
		
		ModFinder.init();		
		PhysisAPI.init();
		
		PhysisStoryVars.init();
		Knowledge.init();
		
    	PhysisItems.init();
    	PhysisBlocks.init();
    	PhysisArtifacts.init();

    	PhysisIntegration.loadModules();
    	
    	PageDefs.init();
    	
    	PhysisCraftingRecipes.init();    	
    	
    	ChestGenHandler.init();
    	
    	Physis.oooBuilder = new OddOneOutBuilder();
    	
    	RecipeHelper.initDefaultTranslators();
    	
    	PhysisWorldGen.init();
    	
    	PhysisIntegration.preInitEnd(event, false);
	}
	
	public void init(FMLInitializationEvent event) {
		PhysisIntegration.initStart(event, false);
		
    	NetworkRegistry.INSTANCE.registerGuiHandler(Physis.instance, new GuiHandler());
    	
    	FMLCommonHandler.instance().bus().register(new ServerTickHandler());
    	MinecraftForge.EVENT_BUS.register(new ArtifactEventHandler());
    	FMLCommonHandler.instance().bus().register(new WorldDataHandler());
    	MinecraftForge.EVENT_BUS.register(new ServerDataHandler());
    	FMLCommonHandler.instance().bus().register(new ServerDataHandler());
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
		//RecipeHelper.parseRecipes();
		PhysisToolMaterial.buildMaterials();
		ItemTrowel.buildRecipes();
		
		PhysisIntegration.loadFinished(event, false);
	}
	
	public void serverPreStarting(FMLServerAboutToStartEvent event) {
		//ServerData.reload(false);
	}
	
	public void serverStarting(FMLServerStartingEvent event) { 
		RecipeHelper.parseRecipes();
		Physis.oooBuilder.start();

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			World world = MinecraftServer.getServer().worldServers[0];
			PhysisWorldSavedData.load(world);
		}
		ServerData.reload(false);
		
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
		PhysisPacketHandler.registerPacketHandler(new PacketPlayerUpdate(), 2);
		PhysisPacketHandler.registerPacketHandler(new PacketWorldData(), 3);
		
		PhysisPacketHandler.init();
	}
	
	public void doArticlePopup(JournalArticle article) {
		
	}
}
