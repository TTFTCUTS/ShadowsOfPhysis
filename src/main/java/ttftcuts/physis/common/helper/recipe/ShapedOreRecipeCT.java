package ttftcuts.physis.common.helper.recipe;

import ttftcuts.physis.client.gui.journal.JournalPage;
import ttftcuts.physis.client.gui.journal.JournalPageCraftingRecipe;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
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
			
			/*int[] ids = OreDictionary.getOreIDs(items[i]);
			for (int j=0; j<ids.length; j++) {
				Physis.logger.info("Ore for "+items[i]+": "+OreDictionary.getOreName(ids[j]));
			}*/
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

	@Override
	public JournalPage getJournalRecipePage(Object recipe) {
		if (!(recipe instanceof ShapedOreRecipe)) { return null; }
		ShapedOreRecipe r = (ShapedOreRecipe)recipe;
		
		return new JournalPageCraftingRecipe(this.getRecipeOutput(r), r);
	}

}
