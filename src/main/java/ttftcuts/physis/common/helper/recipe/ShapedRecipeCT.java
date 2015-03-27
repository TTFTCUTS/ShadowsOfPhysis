package ttftcuts.physis.common.helper.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.JournalPage;
import ttftcuts.physis.client.gui.journal.JournalPageCraftingRecipe;
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

	@Override
	public JournalPage getJournalRecipePage(Object recipe) {
		if (!(recipe instanceof ShapedRecipes)) { return null; }
		ShapedRecipes r = (ShapedRecipes)recipe;
		
		return new JournalPageCraftingRecipe(this.getRecipeOutput(r), r);
	}
}
