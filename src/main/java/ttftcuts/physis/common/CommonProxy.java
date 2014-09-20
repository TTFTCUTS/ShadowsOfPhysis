package ttftcuts.physis.common;

import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.common.handler.GuiHandler;
import ttftcuts.physis.common.item.ItemTrowel;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
import ttftcuts.physis.common.item.material.ShapedOreRecipeCT;
import ttftcuts.physis.common.item.material.ShapedRecipeCT;
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
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
		PhysisToolMaterial.addRecipeComponentTranslator(ShapedRecipes.class, new ShapedRecipeCT());
		PhysisToolMaterial.addRecipeComponentTranslator(ShapedOreRecipe.class, new ShapedOreRecipeCT());
		
		PhysisToolMaterial.buildMaterials();
		
		ItemTrowel.buildRecipes();
	}
}
