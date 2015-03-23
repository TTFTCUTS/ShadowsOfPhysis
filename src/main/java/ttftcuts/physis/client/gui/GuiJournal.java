package ttftcuts.physis.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.*;
import ttftcuts.physis.client.gui.button.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiJournal extends GuiScreen {

	public static final int bookWidth = 350;
	public static final int bookHeight = 240;
	public int left;
	public int top;
	
	public static final ResourceLocation bookTextureLeft = new ResourceLocation(Physis.MOD_ID+":textures/gui/journal_left.png");
	public static final ResourceLocation bookTextureRight = new ResourceLocation(Physis.MOD_ID+":textures/gui/journal_right.png");
	
	public static final int pageWidth = 140;
	public static final int pageHeight = 190;
	
	public static final int pageXLeft = 20;
	public static final int pageXRight = 190;
	public static final int pageY = 18;
	
	public static final int bIdOffsetLeft = 100;
	public static final int bIdOffsetRight = 200;
	
	public List<JournalPage> pages = new ArrayList<JournalPage>();
	public int currentPage = 0;
	
	protected GuiButtonJournal buttonForward;
	protected GuiButtonJournal buttonBack;
	
	protected boolean isIndex = false;
	protected JournalArticle article;
	
	protected List<String> tooltip = new ArrayList<String>();
	
	public GuiJournal() {}
	
	public GuiJournal(JournalPage... args) {
		for (JournalPage page : args) {
			pages.add(page);
		}
	}
	
	public GuiJournal(JournalArticle article) {
		this.article = article;
		if (article == PageDefs.index) {
			this.isIndex = true;
		}
		for (JournalPage page : article.pages) {
			pages.add(page);
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		for (JournalPage page: pages) {
			page.initGui(this);
		}

		this.updateButtons();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			if (pages.size() > currentPage + 2) {
				currentPage += 2;
			}
		}
		
		if (button.id == 1) {
			if (currentPage > 0) {
				currentPage -= 2;
			}
		}
	
		if (button.id >= bIdOffsetLeft) {
			if (button.id >= bIdOffsetRight) {
				pages.get(currentPage+1).actionPerformed(this, bIdOffsetRight, button);
			} else {
				pages.get(currentPage).actionPerformed(this, bIdOffsetLeft, button);
			}
		}
		
		this.updateButtons();
	}
	
	@SuppressWarnings("unchecked")
	protected void updateButtons() {
		this.left = this.width / 2 - bookWidth / 2;
		this.top = this.height / 2 - bookHeight / 2;
		
		this.buttonList.clear();
		
		buttonForward = new GuiButtonJournal(0, left + bookWidth - 35, top + 205, 20,13, 350, 13);
		this.buttonList.add(buttonForward);
		
		buttonBack = new GuiButtonJournal(1, left + 15, top + 205, 20,13, 350, 0);
		this.buttonList.add(buttonBack);
		
		buttonBack.enabled = buttonBack.visible = !(currentPage == 0 || pages.size() <= 2);
		
		buttonForward.enabled = buttonForward.visible = !(currentPage >= pages.size()-2 || pages.size() <= 2);
		
		if ( pages.size() >= currentPage + 1 ) {
			addPageButtons(currentPage, bIdOffsetLeft, pageXLeft);
		}
		if ( pages.size() >= currentPage + 2 ) {
			addPageButtons(currentPage + 1, bIdOffsetRight, pageXRight);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addPageButtons(int pageid, int boffset, int pageleft) {
		List<GuiButton> pagebuttons = pages.get(pageid).getNavButtonsForPage(boffset, this.left + pageleft, this.top + pageY);
		if (pagebuttons != null) {
			this.buttonList.addAll(pagebuttons);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if(!isIndex && par2 == 1) { // keycode for escape
			if (this.article != null && this.article.category != null) {
				mc.displayGuiScreen(new GuiJournal(this.article.category.sectionMenu));
			} else {
				mc.displayGuiScreen(new GuiJournal(PageDefs.index));
			}
			return;
		}

		super.keyTyped(par1, par2);
	}
	
	public void drawBookBackground() {
		mc.renderEngine.bindTexture(bookTextureLeft);
		drawTexturedModalRect(left, top, 0, 0, 256, bookHeight);
		mc.renderEngine.bindTexture(bookTextureRight);
		drawTexturedModalRect(left + 256, top, 0, 0, bookWidth - 256, bookHeight);
	}
	
	public void drawBookBackground(int x, int y, int w, int h) {
		if (x <= 256 && x+w > 0) {
			// texture 1
			mc.renderEngine.bindTexture(bookTextureLeft);
			drawTexturedModalRect(left+x, top+y, x, y, Math.min(256-x, w), Math.min(bookHeight-y, h));
		}
		if (x+w > 256 && x <= bookWidth) {
			// texture 2
			mc.renderEngine.bindTexture(bookTextureRight);
			int rx = Math.max(0, x-256);
			drawTexturedModalRect(left + 256 + rx, top+y, rx, y, bookWidth-256-rx, Math.min(bookHeight-y, h));
		}
	}
	
	public void drawBGOverlay(int x, int y, int w, int h, float alpha) {
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		this.zLevel += 250.0f;
		
		GL11.glColor4f(1F, 1F, 1F, alpha);
		this.drawBookBackground(x,y,w,h);
		
		this.zLevel -= 250.0f;
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.tooltip.clear();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.drawBookBackground();
		
		if ( pages.size() >= currentPage + 1 ) {
			// render left page
			pages.get(currentPage).drawPage(this, this.left + pageXLeft, this.top + pageY, mouseX, mouseY);
		}
		if ( pages.size() >= currentPage + 2 ) {
			// render right page
			pages.get(currentPage + 1).drawPage(this, this.left + pageXRight, this.top + pageY, mouseX, mouseY);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (tooltip.size() > 0) {
			this.drawCustomTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	public void setTooltip(List<String> lines) {
		this.tooltip = lines;
	}
	
	public void setTooltip(String... args) {
		List<String> lines = new ArrayList<String>();
		for(String line:args) {
			lines.add(line);
		}
		this.setTooltip(lines);
	}
	
	public void drawCustomTooltip(int x, int y, List<String> lines) {
		this.drawHoveringText(lines, x, y, mc.fontRenderer);
	}
	
	public void drawCustomTooltip(int x, int y, String... args) {
		List<String> lines = new ArrayList<String>();
		for(String line:args) {
			lines.add(line);
		}
		this.drawCustomTooltip(x, y, lines);
	}
	
	public float getZLevel() {
		return this.zLevel;
	}
	public void setZLevel(float z) {
		this.zLevel = z;
	}
}
