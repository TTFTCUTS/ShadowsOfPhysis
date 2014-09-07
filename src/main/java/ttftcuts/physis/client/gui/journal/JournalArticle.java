package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import ttftcuts.physis.client.gui.journal.PageDefs.Category;

public class JournalArticle {
	public String title;
	public Category category;
	public List<JournalPage> pages = new ArrayList<JournalPage>();
	public ItemStack iconstack;
	public ResourceLocation icontexture;
	
	public JournalArticle(String title, Category cat, JournalPage... args) {
		this.title = title;
		this.category = cat;
		for(JournalPage page : args) {
			pages.add(page);
		}
		if (cat != null) {
			PageDefs.articleMap.put(cat, this);
		}
	}
	
	public JournalArticle setStack(ItemStack stack) {
		this.iconstack = stack;
		this.icontexture = null;
		return this;
	}
	
	public JournalArticle setIcon(ResourceLocation icon) {
		this.iconstack = null;
		this.icontexture = icon;
		return this;
	}
}
