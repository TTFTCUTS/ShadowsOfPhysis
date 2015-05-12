package ttftcuts.physis.common.helper.recipe;

import net.minecraft.item.ItemStack;

public class RecipeDisplayData {
	public ItemStack output;
	public ItemStack[][] inputs;
	
	public RecipeDisplayData(ItemStack output, ItemStack[]... inputs) {
		this.output = output;
		this.inputs = inputs;
	}
}
