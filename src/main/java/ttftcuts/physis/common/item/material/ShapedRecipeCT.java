package ttftcuts.physis.common.item.material;

import ttftcuts.physis.Physis;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedRecipeCT implements IRecipeComponentTranslator {

	@Override
	public ItemStack[] getRecipeComponents(IRecipe recipe) {
		if (!(recipe instanceof ShapedRecipes)) { return null; }
		ShapedRecipes r = (ShapedRecipes)recipe;
		
		Physis.logger.info("Shaped Recipe - " + r.getRecipeOutput().getDisplayName() +": "+r.recipeItems.length +", "+r.recipeItems.toString());
		
		return r.recipeItems;
	}

	@Override
	public boolean hasOreDictStick() {
		return false;
	}

}
