package ttftcuts.physis.common.item.material;

import net.minecraft.item.ItemStack;

public interface IRecipeComponentTranslator {
	public ItemStack getRecipeOutput(Object recipe);
	
	public ItemStack[] getRecipeComponents(Object recipe);
	
	public boolean hasOreDictStick();
	
	public void registerRecipe(Object sourceRecipe, ItemStack output, Object... inputs);
}
