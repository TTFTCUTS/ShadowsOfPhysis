package ttftcuts.physis.common.item.material;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedOreRecipeCT implements IRecipeComponentTranslator {

	@Override
	public ItemStack[] getRecipeComponents(IRecipe recipe) {
		if (!(recipe instanceof ShapedOreRecipe)) { return null; }
		ShapedOreRecipe r = (ShapedOreRecipe)recipe;
		
		Object[] objects = r.getInput();
		ItemStack[] items = new ItemStack[objects.length];
		
		for (int i=0; i<objects.length; i++) {
			if (objects[i] == null) { continue; }
			items[i] = PhysisToolMaterial.getRecipeCompStack(objects[i]);
		}
		
		return items;
	}

	@Override
	public boolean hasOreDictStick() {
		return true;
	}

}
