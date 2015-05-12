package ttftcuts.physis.common.helper.recipe;

import ttftcuts.physis.client.gui.journal.JournalPage;
import net.minecraft.item.ItemStack;

public interface IRecipeComponentTranslator {
	public ItemStack getRecipeOutput(Object recipe);
	
	public ItemStack[] getRecipeComponents(Object recipe);
	public ItemStack[][] getRecipeComponentVariants(Object recipe);
	
	public boolean hasOreDictStick();
	
	public void registerRecipe(Object sourceRecipe, ItemStack output, Object... inputs);
	
	public JournalPage getJournalRecipePage(Object... recipes);
}
