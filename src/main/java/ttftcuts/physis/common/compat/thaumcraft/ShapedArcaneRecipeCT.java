package ttftcuts.physis.common.compat.thaumcraft;

import java.util.List;
import java.util.Map.Entry;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import ttftcuts.physis.client.gui.journal.JournalPage;
import ttftcuts.physis.common.helper.recipe.IRecipeComponentTranslator;
import ttftcuts.physis.common.helper.recipe.RecipeHelper;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
import net.minecraft.item.ItemStack;

public class ShapedArcaneRecipeCT implements IRecipeComponentTranslator {

	@Override
	public ItemStack getRecipeOutput(Object recipe) {
		if (!(recipe instanceof ShapedArcaneRecipe)) { return null; }
		return ((ShapedArcaneRecipe)recipe).getRecipeOutput();
	}
	
	@Override
	public ItemStack[] getRecipeComponents(Object recipe) {
		if (!(recipe instanceof ShapedArcaneRecipe)) { return null; }
		ShapedArcaneRecipe r = (ShapedArcaneRecipe)recipe;
		
		Object[] objects = r.getInput();
		ItemStack[] items = new ItemStack[objects.length];
		
		for (int i=0; i<objects.length; i++) {
			if (objects[i] == null) { continue; }
			items[i] = PhysisToolMaterial.getRecipeCompStack(objects[i]);
		}
		
		return items;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ItemStack[][] getRecipeComponentVariants(Object recipe) {
		if (!(recipe instanceof ShapedArcaneRecipe)) { return null; }
		ShapedArcaneRecipe r = (ShapedArcaneRecipe)recipe;
		
		Object[] objects = r.getInput();
		ItemStack[][] items = new ItemStack[objects.length][];
		
		for (int i=0; i<objects.length; i++) {
			if (objects[i] == null) { 
				items[i] = new ItemStack[1];
				continue;	
			}
			
			if (objects[i] instanceof ItemStack) {
				ItemStack[] l = {(ItemStack)objects[i]};
				items[i] = l;
			}
			else if (objects[i] instanceof List) {
				List<ItemStack> l = (List<ItemStack>)objects[i];
				items[i] = RecipeHelper.getStackVariants(l);
			}
		}
		
		return items;
	}

	@Override
	public boolean hasOreDictStick() {
		return true;
	}
	
	@Override
	public void registerRecipe(Object sourceRecipe, ItemStack output, Object... inputs) {
		if (!(sourceRecipe instanceof ShapedArcaneRecipe)) { return; }
		ShapedArcaneRecipe r = (ShapedArcaneRecipe)sourceRecipe;
		
		AspectList aspectList = r.getAspects().copy();
		for(Entry<Aspect, Integer> aspect : aspectList.aspects.entrySet()) {
			aspect.setValue((int)Math.round(0.4 * aspect.getValue()));
		}
		
		ThaumcraftApi.addArcaneCraftingRecipe(r.research, output, aspectList, inputs);
	}

	@Override
	public JournalPage getJournalRecipePage(Object... recipes) {
		return null;
	}

}
