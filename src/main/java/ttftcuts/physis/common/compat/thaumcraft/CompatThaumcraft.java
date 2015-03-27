package ttftcuts.physis.common.compat.thaumcraft;

import java.util.Iterator;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import ttftcuts.physis.common.compat.CompatModule;
import ttftcuts.physis.common.helper.recipe.RecipeHelper;
import ttftcuts.physis.common.helper.recipe.RecipeListGetter;

public class CompatThaumcraft extends CompatModule {

	public RecipeListGetter thaumcraftRecipeList;
	
	@Override
	public void initEnd(FMLInitializationEvent event, boolean client) {
		thaumcraftRecipeList = new RecipeListGetter() {
			@Override
			public Iterator<?> getIterator() {
				return ThaumcraftApi.getCraftingRecipes().listIterator();
			}
		};
		RecipeHelper.registerRecipeListGetter(thaumcraftRecipeList);
		RecipeHelper.addRecipeComponentTranslator(thaumcraftRecipeList, ShapedArcaneRecipe.class, new ShapedArcaneRecipeCT());
	}
}
