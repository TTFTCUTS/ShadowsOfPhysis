package ttftcuts.physis.client.gui.journal;

import net.minecraft.client.gui.FontRenderer;
import ttftcuts.physis.client.gui.GuiJournal;

public class JournalPageText extends JournalPage {

	String pageText;
	
	public JournalPageText(String text) {
		super();
		
		this.pageText = text;
	}
	
	@Override
	public void drawPage(GuiJournal journal, int x, int y) {
		FontRenderer renderer = journal.mc.fontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		renderer.setUnicodeFlag(true);
		
		renderer.drawString(pageText, x, y, 0);
		
		renderer.setUnicodeFlag(unicode);
	}
}
