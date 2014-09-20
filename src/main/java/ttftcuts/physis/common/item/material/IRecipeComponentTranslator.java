package ttftcuts.physis.common.item.material;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipeComponentTranslator {

	public ItemStack[] getRecipeComponents(IRecipe recipe);
	
	public boolean hasOreDictStick();
}
