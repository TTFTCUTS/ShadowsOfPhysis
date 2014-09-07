package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.client.gui.journal.buttons.GuiButtonInvisible;

public class JournalPageSubIndex extends JournalPage {

	public static final int articlesPerPage = 24;
	public static final int articlesPerRow = 4;
	public static final int iconsize = 16; // fixed, or the shadow will be bork
	public static final int iconspacing = 15;
	public static final int iconsleft = 12;
	public static final int iconstop = 10;
	
	boolean setup = false;
	PageDefs.Category category;
	int offset;
	List<JournalArticle> articles = new ArrayList<JournalArticle>();
	
	public JournalPageSubIndex(PageDefs.Category category, int offset) {
		this.category = category;
		this.offset = offset;
	}
	
	@Override
	public void initGui(GuiJournal journal) {
		if (!setup) {
			setup = true;
			articles.clear();
			Set<JournalArticle> articleset = PageDefs.articleMap.get(this.category);
			if(articleset == null || articleset.size() == 0) { return; }
			
			articles.addAll(articleset);
			Collections.sort(articles, new Comparator<JournalArticle>() {
				@Override
				public int compare(JournalArticle o1, JournalArticle o2) {
					String t1 = o1.title;
					String t2 = o2.title;
					return t1.compareTo(t2);
				}
			});
			
			Physis.logger.info(articles.size());
			
			// and now trim to the page...
			articles = articles.subList(offset * articlesPerPage, Math.min(articles.size(), (offset+1) * articlesPerPage));
			
			Physis.logger.info(articles.size());
		}
	}
	
	@Override
	public void actionPerformed(GuiJournal journal, int id, GuiButton button) {
		int bid = button.id - id;
		
		JournalArticle article = articles.get(bid);
		if (article != null) {
			journal.mc.displayGuiScreen(new GuiJournal(article));
		}
	}
	
	@Override
	public List<GuiButton> getNavButtonsForPage(int id, int x, int y) {
		
		List<GuiButton> buttons = new ArrayList<GuiButton>();
		
		for(int i=0; i<articles.size(); i++) {
			int posx = i % articlesPerRow;
			int posy = (int) Math.floor(i / articlesPerRow);
			
			int iconx = x + iconsleft + posx * (iconspacing + iconsize);
			int icony = y + iconstop + posy * (iconspacing + iconsize);
			
			buttons.add(new GuiButtonInvisible(id+i, iconx - 2, icony - 2, iconsize + 4, iconsize + 4));
		}
		
		return buttons;
	}
	
	@Override
	public void drawPage(GuiJournal journal, int x, int y) {

		for(int i=0; i<articles.size(); i++) {
			int posx = i % articlesPerRow;
			int posy = (int) Math.floor(i / articlesPerRow);
			
			int iconx = x + iconsleft + posx * (iconspacing + iconsize);
			int icony = y + iconstop + posy * (iconspacing + iconsize);
			
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			journal.mc.renderEngine.bindTexture(GuiJournal.bookTextureRight);
			
			journal.drawTexturedModalRect(iconx-2, icony-2, 350,50, iconsize+4, iconsize+4);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}
}
