package ttftcuts.physis.common;

import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.common.crafting.AddSocketRecipe;
import ttftcuts.physis.common.handler.GuiHandler;
import ttftcuts.physis.common.item.ItemTrowel;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
import ttftcuts.physis.common.item.material.ShapedOreRecipeCT;
import ttftcuts.physis.common.item.material.ShapedRecipeCT;
import ttftcuts.physis.puzzle.OddOneOutBuilder;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public CommonProxy() {
		Physis.logger.info("COMMON PROXY");
	}
	
	public void preInit(FMLPreInitializationEvent event) {
    	PhysisItems.init();
    	PhysisBlocks.init();
    	
    	PageDefs.init();
    	NetworkRegistry.INSTANCE.registerGuiHandler(Physis.instance, new GuiHandler());
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
	
	public void serverStarting(FMLServerStartingEvent event) { Physis.logger.info("Starting server"); }
	
	public void serverStopped(FMLServerStoppedEvent event) {}
	
	
	public void initOddOneOutSolver(FMLPreInitializationEvent event) {
		Physis.logger.info("Init builder "+ event.getSide());
		Physis.oooBuilder = new OddOneOutBuilder();
	}
	
	public void startOddOneOutSolver(FMLServerStartingEvent event) {
		Physis.logger.info("Start builder");
		Physis.oooBuilder.start();
	}
	
	public void stopOddOneOutSolver(FMLServerStoppedEvent event) {
		Physis.logger.info("Stop builder");
		Physis.oooBuilder.stop();
	}
	
	public void requestOddOneOutPuzzle(int difficulty, TileEntity tile) {
		Physis.logger.info("Requesting puzzle");
		Physis.oooBuilder.requestPuzzle(difficulty, tile);
	}
		
}
