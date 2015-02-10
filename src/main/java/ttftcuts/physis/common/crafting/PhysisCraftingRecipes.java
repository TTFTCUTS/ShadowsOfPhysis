package ttftcuts.physis.common.crafting;

import ttftcuts.physis.common.PhysisBlocks;
import ttftcuts.physis.common.PhysisItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class PhysisCraftingRecipes {
	
	public static void init() {
		Object diamond = OreDictionary.getOres("gemDiamond").size() == 0 ? new ItemStack(Items.diamond) : "gemDiamond";
		
		addRecipe(new AddSocketRecipe());
		
		addShapedOreRecipe(new ItemStack(PhysisBlocks.socketTable),
			"GSG", "WTW", "GCG",
			'G', new ItemStack(Items.gold_ingot),
			'S', new ItemStack(PhysisItems.component, 1, 0),
			'W', "plankWood",
			'T', new ItemStack(Blocks.crafting_table),
			'C', new ItemStack(Blocks.chest)
		);
		
		addShapedOreRecipe(new ItemStack(PhysisBlocks.socketJukebox),
			" D ", "SJS", " S ",
			'D', diamond,
			'S', new ItemStack(PhysisItems.component, 1, 0),
			'J', new ItemStack(Blocks.jukebox)
		);
		
		GameRegistry.addSmelting(PhysisItems.socketable, new ItemStack(PhysisItems.component, 3, 0), 25);
	}
	
	private static IRecipe addRecipe(IRecipe recipe) {
		GameRegistry.addRecipe(recipe);
		return recipe;
	}
	
	//@SuppressWarnings("unused")
	private static IRecipe addShapedOreRecipe(ItemStack output, Object... recipe) {
		IRecipe r = new ShapedOreRecipe(output, recipe);
		GameRegistry.addRecipe(r);
		return r;
	}
	
	@SuppressWarnings("unused")
	private static IRecipe addShapelessRecipe(ItemStack output, Object... recipe) {
		IRecipe r = new ShapelessOreRecipe(output, recipe);
		GameRegistry.addRecipe(r);
		return r;
	}
}
