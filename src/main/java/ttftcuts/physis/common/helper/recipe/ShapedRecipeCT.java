package ttftcuts.physis.common.helper.recipe;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
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
		
		return r.recipeItems.clone();
	}
	
	@Override
	public ItemStack[][] getRecipeComponentVariants(Object recipe) {
		if (!(recipe instanceof ShapedRecipes)) { return null; }
		ShapedRecipes r = (ShapedRecipes)recipe;
		
		ItemStack[][] out = new ItemStack[r.recipeItems.length][];
		for (int i=0; i<r.recipeItems.length; i++) {
			ItemStack[] s = {r.recipeItems[i]};
			out[i] = s;
		}
		return out;
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
	public JournalPage getJournalRecipePage(Object... recipes) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for (Object recipe : recipes) {
			if (!(recipe instanceof ShapedRecipes)) { return null; }
			ShapedRecipes r = (ShapedRecipes)recipe;
			stacks.add(this.getRecipeOutput(r));
		}
		
		return new JournalPageCraftingRecipe(stacks.toArray(new ItemStack[stacks.size()]));
	}
}
