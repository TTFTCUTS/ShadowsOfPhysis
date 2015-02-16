package ttftcuts.physis.common.item.material;

import cpw.mods.fml.common.registry.GameRegistry;
import ttftcuts.physis.Physis;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedRecipeCT implements IRecipeComponentTranslator {

	@Override
	public ItemStack getRecipeOutput(Object recipe) {
		if (!(recipe instanceof ShapedRecipes)) { return null; }
		return ((ShapedRecipes)recipe).getRecipeOutput();
	}
	
	@Override
	public ItemStack[] getRecipeComponents(Object recipe) {
		if (!(recipe instanceof ShapedRecipes)) { return null; }
		ShapedRecipes r = (ShapedRecipes)recipe;
		
		Physis.logger.info("Shaped Recipe - " + r.getRecipeOutput().getDisplayName() +": "+r.recipeItems.length +", "+r.recipeItems.toString());
		
		return r.recipeItems;
	}

	@Override
	public boolean hasOreDictStick() {
		return false;
	}

	@Override
	public void registerRecipe(Object sourceRecipe, ItemStack output, Object... inputs) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, inputs));
	}

}
