package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.ClientProxy;
import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.client.gui.button.GuiButtonInvisible;

public class JournalPageIndex extends JournalPage {

	private static final int buttonWidth = 120;
	private static final int buttonHeight = 13;
	private static final int buttonSpacing = 25;
	private static final int buttonTop = 30;
	
	private List<PageDefs.Category> categories = new ArrayList<PageDefs.Category>();
	private boolean setup = false;
	
	public JournalPageIndex(int offset) {
		Physis.logger.info("Instantiated");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initGui(GuiJournal journal) {
		if (!setup) {
			categories.clear();
			for(PageDefs.Category cat : PageDefs.Category.values()) {
				categories.add(cat);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void actionPerformed(GuiJournal journal, int id, GuiButton button) {
		int bid = button.id - id;
		
		JournalArticle article = categories.get(bid).sectionMenu;
		if (article != null) {
			journal.mc.displayGuiScreen(new GuiJournal(article));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiButton> getNavButtonsForPage(int id, int x, int y) {
		
		List<GuiButton> buttons = new ArrayList<GuiButton>();
		
		for(int i=0; i<categories.size(); i++) {
			buttons.add(new GuiButtonInvisible(id+i, x + GuiJournal.pageWidth / 2 - buttonWidth / 2, y + buttonTop + (buttonHeight+buttonSpacing)*i, buttonWidth,buttonHeight));
		}
		
		return buttons;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawPage(GuiJournal journal, int x, int y, int mousex, int mousey) {
		FontRenderer renderer = this.canView() ? journal.mc.fontRenderer : ClientProxy.runeFontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		renderer.setUnicodeFlag(false);
		
		for(int i=0; i<categories.size(); i++) {
			String categoryname = categories.get(i).name;
			String text = Physis.text.translate(Physis.text.categoryPrefix + categoryname);
			renderer.drawString(text, x + (GuiJournal.pageWidth / 2) - (renderer.getStringWidth(text) / 2), y + buttonTop + 3 + (buttonHeight+buttonSpacing)*i, 0x000000);
			
			GL11.glColor4f(1F, 1F, 1F, 1F);
			journal.mc.renderEngine.bindTexture(GuiJournal.bookTextureRight);
			int width = renderer.getStringWidth(text);
			if (width + 38 <= GuiJournal.pageWidth) {
				journal.drawTexturedModalRect(x + (GuiJournal.pageWidth / 2) - (width / 2) - 19, y + buttonTop + (buttonHeight+buttonSpacing)*i, 350, 26, 16, 13);
				journal.drawTexturedModalRect(x + (GuiJournal.pageWidth / 2) + (width / 2) + 3, y + buttonTop + (buttonHeight+buttonSpacing)*i, 366, 26, 16, 13);
			}
		}

		renderer.setUnicodeFlag(unicode);
	}
}
