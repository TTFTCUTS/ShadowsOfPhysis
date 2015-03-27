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
		
		for (ItemStack trowel : trowels) {
			Object recipe = RecipeHelper.getRecipe(trowel);
			if (recipe == null) { continue; }
			
			IRecipeComponentTranslator translator = RecipeHelper.getTranslatorForRecipe(recipe);
			if (translator == null) { continue; }
			
			JournalPage page = translator.getJournalRecipePage(recipe);
			if (page != null) {
				modified.add(page);
			}
		}
		Physis.logger.info(modified.size());
		return modified;
	}

}
