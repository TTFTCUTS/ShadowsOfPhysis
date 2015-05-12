package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.PageDefs.Category;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.helper.recipe.IRecipeComponentTranslator;
import ttftcuts.physis.common.helper.recipe.RecipeHelper;
import ttftcuts.physis.common.item.ItemTrowel;

public class JournalArticleTrowels extends JournalArticle {

	public JournalArticleTrowels(String title, Category cat, JournalPage... args) {
		super(title, cat, args);
	}
	
	@Override
	public List<JournalPage> getPages() {
		List<JournalPage> modified = new ArrayList<JournalPage>();
		modified.addAll(super.getPages());
		
		modified.add(new JournalPageCraftingRecipe(new ItemStack(Blocks.carpet)));
		
		Item titem = PhysisItems.trowel;
		List<ItemStack> trowels = new ArrayList<ItemStack>();
		titem.getSubItems(titem, titem.getCreativeTab(), trowels);

		for (ItemStack troweltype : trowels) {
			List<Object> recipes = new ArrayList<Object>();
			ItemStack[] handlevariants = ((ItemTrowel)titem).getHandleVariantsForStack(troweltype);
			
			Object recipe = RecipeHelper.getRecipe(troweltype);
			if (recipe == null) { continue; }
			
			IRecipeComponentTranslator translator = RecipeHelper.getTranslatorForRecipe(recipe);
			if (translator == null) { continue; }
			
			for (ItemStack trowel : handlevariants) {
				Object hrecipe = RecipeHelper.getRecipe(trowel);
				if (hrecipe == null) { continue; }
				
				IRecipeComponentTranslator htranslator = RecipeHelper.getTranslatorForRecipe(hrecipe);
				
				if (htranslator == translator) {
					recipes.add(hrecipe);
					Physis.logger.info("Adding trowel recipe for "+trowel.getDisplayName()+", wool colour: "+trowel.getTagCompound().getInteger(ItemTrowel.HANDLETAG));
				}
			}
			
			JournalPage page = translator.getJournalRecipePage(recipes.toArray());
			if (page != null) {
				modified.add(page);
			}
		}
		return modified;
	}

}
