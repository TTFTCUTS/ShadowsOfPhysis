package ttftcuts.physis.common;

import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.crafting.AddSocketRecipe;
import ttftcuts.physis.common.file.ServerData;
import ttftcuts.physis.common.handler.ArtifactEventHandler;
import ttftcuts.physis.common.handler.GuiHandler;
import ttftcuts.physis.common.handler.ServerTickHandler;
import ttftcuts.physis.common.item.ItemTrowel;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
import ttftcuts.physis.common.item.material.ShapedOreRecipeCT;
import ttftcuts.physis.common.item.material.ShapedRecipeCT;
import ttftcuts.physis.common.network.PacketGuiMessage;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutBuilder;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		ServerData.init();
		
    	PhysisItems.init();
    	PhysisBlocks.init();
    	PhysisArtifacts.init();
    	
    	PageDefs.init();
    	NetworkRegistry.INSTANCE.registerGuiHandler(Physis.instance, new GuiHandler());
    	FMLCommonHandler.instance().bus().register(new ServerTickHandler());
    	MinecraftForge.EVENT_BUS.register(new ArtifactEventHandler());
    	networkSetup();
    	
    	Physis.oooBuilder = new OddOneOutBuilder();
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
		PhysisToolMaterial.addRecipeComponentTranslator(ShapedRecipes.class, new ShapedRecipeCT());
		PhysisToolMaterial.addRecipeComponentTranslator(ShapedOreRecipe.class, new ShapedOreRecipeCT());
		
		PhysisToolMaterial.buildMaterials();
		
		ItemTrowel.buildRecipes();
		GameRegistry.addRecipe(new AddSocketRecipe());
	}
	
	public void serverStarting(FMLServerStartingEvent event) { 
		Physis.oooBuilder.start();
	}
	
	public void serverStopped(FMLServerStoppedEvent event) {
		Physis.oooBuilder.stop();
	}
		
	private void networkSetup() {
		PhysisPacketHandler.registerPacketHandler(new PacketGuiMessage(), 0);
		
		PhysisPacketHandler.init();
	}
}
