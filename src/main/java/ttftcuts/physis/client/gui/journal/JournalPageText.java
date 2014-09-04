package ttftcuts.physis.client.gui.journal;

import net.minecraft.client.gui.FontRenderer;
import ttftcuts.physis.client.gui.GuiJournal;

public class JournalPageText extends JournalPage {

	public String pageText;
	
	public JournalPageText(String text) {
		super();
		
		this.pageText = text;
	}
	
	@Override
	public void drawPage(GuiJournal journal, int x, int y) {
		FontRenderer renderer = journal.mc.fontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		renderer.setUnicodeFlag(true);
		
		String text = pageText.replace("@r", "@r@0").replace('@', '\u00a7');

		renderer.drawSplitString(text, x, y, GuiJournal.pageWidth, 0x000000);

		renderer.setUnicodeFlag(unicode);
	}
}
