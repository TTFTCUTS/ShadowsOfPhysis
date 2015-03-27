package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.client.gui.button.GuiButtonInvisible;
import ttftcuts.physis.common.helper.PhysisRenderHelper;
import ttftcuts.physis.utils.NaturalOrderComparator;

public class JournalPageSubIndex extends JournalPage {

	public static final int articlesPerPage = 24;
	public static final int articlesPerRow = 4;
	public static final int iconsize = 16; // fixed, or the shadow will be bork
	public static final int shadowborder = 4;
	public static final int iconspacing = 16;
	public static final int iconsleft = 14;
	public static final int iconstop = 7;
	
	boolean setup = false;
	PageDefs.Category category;
	int offset;
	List<JournalArticle> articles = new ArrayList<JournalArticle>();
	
	public JournalPageSubIndex(PageDefs.Category category, int offset) {
		this.category = category;
		this.offset = offset;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initGui(GuiJournal journal) {
		if (!setup) {
			setup = true;
			articles.clear();
			Set<JournalArticle> articleset = PageDefs.articleMap.get(this.category);
			if(articleset == null || articleset.size() == 0) { return; }
			
			articles.addAll(articleset);
			Collections.sort(articles, new Comparator<JournalArticle>() {
				NaturalOrderComparator comp = new NaturalOrderComparator();
				
				@Override
				public int compare(JournalArticle o1, JournalArticle o2) {
					String t1 = o1.title;
					String t2 = o2.title;
					return comp.compare(t1, t2);
				}
			});

			// and now trim to the page...
			articles = articles.subList(offset * articlesPerPage, Math.min(articles.size(), (offset+1) * articlesPerPage));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void actionPerformed(GuiJournal journal, int id, GuiButton button) {
		int bid = button.id - id;
		
		JournalArticle article = articles.get(bid);
		if (article != null) {
			journal.mc.displayGuiScreen(new GuiJournal(article));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiButton> getNavButtonsForPage(int id, int x, int y) {
		
		List<GuiButton> buttons = new ArrayList<GuiButton>();
		
		for(int i=0; i<articles.size(); i++) {
			int posx = i % articlesPerRow;
			int posy = (int) Math.floor(i / articlesPerRow);
			
			int iconx = x + iconsleft + posx * (iconspacing + iconsize);
			int icony = y + iconstop + posy * (iconspacing + iconsize);
			
			GuiButton button = new GuiButtonInvisible(id+i, iconx - 2, icony - 2, iconsize + 4, iconsize + 4);
			if (!articles.get(i).canView()) {
				button.enabled = false;
			}
			
			buttons.add(button);
		}
		
		return buttons;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawPage(GuiJournal journal, int x, int y, int mousex, int mousey) {
		float jz = journal.getZLevel();
		journal.setZLevel(jz+10);
		
		for(int i=0; i<articles.size(); i++) {
			JournalArticle article = articles.get(i);
			boolean show = article.canView();
			
			int posx = i % articlesPerRow;
			int posy = (int) Math.floor(i / articlesPerRow);
			
			int iconx = x + iconsleft + posx * (iconspacing + iconsize);
			int icony = y + iconstop + posy * (iconspacing + iconsize);
			
			if (mousex >= iconx - shadowborder && mousex < iconx + iconsize + shadowborder && mousey >= icony - shadowborder && mousey < icony + iconsize + shadowborder) {
				journal.setTooltip(article.getTranslatedName());
				if (!show) {
					journal.setTooltipRenderer(Physis.runeFontRenderer);
				}
			}
			
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			journal.mc.renderEngine.bindTexture(GuiJournal.bookTextureRight);
			
			journal.drawTexturedModalRect(iconx-shadowborder, icony-shadowborder, 350,50, iconsize+shadowborder*2, iconsize+shadowborder*2);

			GL11.glDisable(GL11.GL_BLEND);
			
			if (show) {
				if (article.iconstack != null) {
					// render stack
					PhysisRenderHelper.renderItemStack(article.iconstack, iconx, icony, true, false, false);
				} else if (article.icontexture != null) {
					// render texture icon
					journal.mc.renderEngine.bindTexture(article.icontexture);
					journal.drawTexturedModalRect(iconx, icony, 0,0, iconsize, iconsize);
				} else {
					// render default icon
				}
			} else {
				GL11.glColor4f(1.0f, 0.85f, 0.5f, 1f);
				if (article.iconstack != null) {
					PhysisRenderHelper.renderItemStack(article.iconstack, iconx, icony, true, false, true);
				}
				GL11.glColor4f(1f, 1f, 1f, 1f);
			}

			GL11.glDisable(GL11.GL_ALPHA_TEST);
			
			if (!show) {
				//journal.silhouette(iconx-shadowborder-journal.left, icony-shadowborder - journal.top, iconsize+shadowborder*2, iconsize+shadowborder*2, false);
				//journal.drawBGOverlay(iconx-shadowborder-journal.left, icony-shadowborder - journal.top, iconsize+shadowborder*2, iconsize+shadowborder*2, 0.55f, false);
				journal.drawBGOverlay(iconx-shadowborder-journal.left, icony-shadowborder - journal.top, iconsize+shadowborder*2, iconsize+shadowborder*2, 0.55f);
			}
			GL11.glColor4f(1F, 1F, 1F, 1F);
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
		
		journal.setZLevel(jz);
	}
}
