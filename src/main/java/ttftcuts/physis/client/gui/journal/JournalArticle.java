package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.PageDefs.Category;
import ttftcuts.physis.common.story.Knowledge;

public class JournalArticle {
	public String title;
	public Category category;
	public List<JournalPage> pages = new ArrayList<JournalPage>();
	public ItemStack iconstack;
	public ResourceLocation icontexture;
	public Map<String, Integer> requirements = new HashMap<String, Integer>();
	
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
	
	public List<JournalPage> getPages() {
		return this.pages;
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
	
	public JournalArticle addRequirement(String name, int value) {
		this.requirements.put(name, value);
		return this;
	}
	
	public boolean canView() {
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		
		for (Entry<String, Integer> entry : this.requirements.entrySet()) {
			int k = Knowledge.get(p, entry.getKey());
			if (k < entry.getValue()) {
				// insufficient knowledge to view!
				return false;
			}
		}
		
		return true;
	}
	
	public String getTranslatedName() {
		return Physis.text.translate(Physis.text.titlePrefix + this.title);
	}
}
