package ttftcuts.physis.common.item.material;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedOreRecipeCT implements IRecipeComponentTranslator {
	
	@Override
	public ItemStack getRecipeOutput(Object recipe) {
		if (!(recipe instanceof ShapedOreRecipe)) { return null; }
		return ((ShapedOreRecipe)recipe).getRecipeOutput();
	}
	
	@Override
	public ItemStack[] getRecipeComponents(Object recipe) {
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
	
	@Override
	public void registerRecipe(Object sourceRecipe, ItemStack output, Object... inputs) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, inputs));
	}

}
