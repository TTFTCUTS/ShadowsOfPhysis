package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import scala.actors.threadpool.Arrays;
import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.common.helper.recipe.IRecipeComponentTranslator;
import ttftcuts.physis.common.helper.recipe.RecipeHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class JournalPageCraftingRecipe extends JournalPageRecipe {
	
	IRecipe recipe;
	
	public JournalPageCraftingRecipe(ItemStack outputstack) {
		super(outputstack);
	}
	
	public JournalPageCraftingRecipe(ItemStack outputstack, IRecipe recipe) {
		this(outputstack);
		this.recipe = recipe;
	}

	@Override
	public void getRecipeData() {
		Object r = RecipeHelper.getRecipe(outputstack);
		if (r instanceof IRecipe) {
			this.recipe = (IRecipe)r;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void drawRecipe(GuiJournal journal, int x, int y, int mousex, int mousey) {
		boolean show = this.canView();
		
		IRecipeComponentTranslator translator = RecipeHelper.getTranslatorForRecipe(this.recipe);
		ItemStack[] inputs = translator.getRecipeComponents(this.recipe);
		ItemStack output = translator.getRecipeOutput(recipe);
		
		journal.mc.renderEngine.bindTexture(craftingtextures);
		
		int ox = (GuiJournal.pageWidth - 64)/2;
		int oy = 36;
		
		journal.drawTexturedModalRect(x + ox - 32, y + oy - 8, 0, 0, 128, 128);
		
		//this.drawItemStack(journal, this.outputstack, x + ox + 24, y + oy, mousex, mousey, !this.canView());
		this.drawItemStack(journal, output, x + ox + 24, y + oy, mousex, mousey, !this.canView());
		
		oy += 36;
		
		if (!this.canView()) {
			List<ItemStack> extend = new ArrayList<ItemStack>(Arrays.asList(inputs));
			if (extend.size() < 9) {
				int add = 9-extend.size();
				for (int i=0; i<add; i++) {
					extend.add(null);
				}
			}
			Random rand = new Random(output.getDisplayName().hashCode());
			Collections.shuffle(extend, rand);
			inputs = extend.toArray(inputs);
		}
		
		for (int i=0; i<inputs.length; i++) {
			int gx=i%3;
			int gy=i/3;

			if (inputs[i] != null && inputs[i].getItem() != null) {
				this.drawItemStack(journal, inputs[i], x + ox + gx * 24, y + oy + gy * 24, mousex, mousey, !show);
			}
		}
		
		/*if (!show) {
			GL11.glColor4f(1f, 1f, 1f, 1f);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			journal.mc.renderEngine.bindTexture(craftingtextures);
			oy -=24;
			GL11.glDepthMask(false);
			journal.drawTexturedModalRect(x + ox - 32, y + oy - 8, 128, 128, 128, 128);
			GL11.glDepthMask(true);
		}*/
	}
}
