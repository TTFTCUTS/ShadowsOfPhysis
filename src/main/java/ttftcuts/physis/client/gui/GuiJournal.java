package ttftcuts.physis.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.*;
import ttftcuts.physis.client.gui.journal.buttons.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiJournal extends GuiScreen {

	public static final int bookWidth = 350;
	public static final int bookHeight = 240;
	private int left;
	private int top;
	
	public static final ResourceLocation bookTextureLeft = new ResourceLocation(Physis.MOD_ID+":textures/gui/journal_left.png");
	public static final ResourceLocation bookTextureRight = new ResourceLocation(Physis.MOD_ID+":textures/gui/journal_right.png");
	
	public static final int pageWidth = 140;
	public static final int pageHeight = 190;
	
	public static final int pageXLeft = 20;
	public static final int pageXRight = 190;
	public static final int pageY = 18;
	
	public List<JournalPage> pages;
	public int currentPage = 0;
	
	private GuiButtonJournal buttonForward;
	private GuiButtonJournal buttonBack;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		pages = new ArrayList<JournalPage>();
		pages.add(new JournalPageText("Page 1"));
		pages.add(new JournalPageText("Page 2"));
		pages.add(new JournalPageText("Page 3"));
		pages.add(new JournalPageText("Page 4"));
		pages.add(new JournalPageText("Page 5"));
		
		this.left = this.width / 2 - bookWidth / 2;
		this.top = this.height / 2 - bookHeight / 2;
		
		this.buttonList.clear();
		
		buttonForward = new GuiButtonJournal(0, left + bookWidth - 35, top + 205, 20,13, 350, 13);
		this.buttonList.add(buttonForward);
		
		buttonBack = new GuiButtonJournal(1, left + 15, top + 205, 20,13, 350, 0);
		this.buttonList.add(buttonBack);
		
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
	
		this.updateButtons();
	}
	
	private void updateButtons() {
		
		if (currentPage == 0 || pages.size() <= 2) {
			buttonBack.enabled = false;
			buttonBack.visible = false;
		} else {
			buttonBack.enabled = true;
			buttonBack.visible = true;
		}
		
		if (currentPage >= pages.size()-1 || pages.size() <= 2) {
			buttonForward.enabled = false;
			buttonForward.visible = false;
		} else {
			buttonForward.enabled = true;
			buttonForward.visible = true;
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if(mc.gameSettings.keyBindInventory.getKeyCode() == par2) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}

		super.keyTyped(par1, par2);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(bookTextureLeft);
		drawTexturedModalRect(left, top, 0, 0, 256, bookHeight);
		mc.renderEngine.bindTexture(bookTextureRight);
		drawTexturedModalRect(left + 256, top, 0, 0, bookWidth - 256, bookHeight);
		
		if ( pages.size() >= currentPage + 1 ) {
			// render left page
			pages.get(currentPage).drawPage(this, this.left + pageXLeft, this.top + pageY);
		}
		if ( pages.size() >= currentPage + 2 ) {
			// render right page
			pages.get(currentPage + 1).drawPage(this, this.left + pageXRight, this.top + pageY);
		}
		
		super.drawScreen(par1, par2, par3);
	}
}
