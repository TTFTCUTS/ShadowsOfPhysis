package ttftcuts.physis.client.gui.journal;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import ttftcuts.physis.client.gui.GuiJournal;

public class JournalPageTitle extends JournalPageText {

	public String titleText;
	
	public JournalPageTitle(String title, String text) {
		super(text);
		titleText = title;
	}

	@Override
	public void drawPage(GuiJournal journal, int x, int y) {
		FontRenderer renderer = journal.mc.fontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		
		renderer.setUnicodeFlag(false);
		
		String title = titleText.replace("@r", "@r@0").replace('@', '\u00a7');
		renderer.drawString(title, x + (GuiJournal.pageWidth / 2) - (renderer.getStringWidth(title) / 2), y + 6, 0x000000);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		journal.mc.renderEngine.bindTexture(GuiJournal.bookTextureRight);
		journal.drawTexturedModalRect(x + (GuiJournal.pageWidth / 2) - 20, y + 15, 350, 39, 40, 9);
		
		int width = renderer.getStringWidth(title);
		if (width + 38 <= GuiJournal.pageWidth) {
			journal.drawTexturedModalRect(x + (GuiJournal.pageWidth / 2) - (width / 2) - 19, y + 3, 350, 26, 16, 13);
			journal.drawTexturedModalRect(x + (GuiJournal.pageWidth / 2) + (width / 2) + 3, y + 3, 366, 26, 16, 13);
		}
		
		renderer.setUnicodeFlag(true);
		
		String text = pageText.replace("@r", "@r@0").replace('@', '\u00a7');
		renderer.drawSplitString(text, x, y + 27, GuiJournal.pageWidth, 0x000000);

		renderer.setUnicodeFlag(unicode);
	}
}
