package ttftcuts.physis.client.gui.journal;

import net.minecraft.client.gui.FontRenderer;
import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.GuiJournal;

public class JournalPageText extends JournalPage {

	public String pageText;
	
	public JournalPageText(String text) {
		super();
		
		this.pageText = text;
	}
	
	@Override
	public void drawPage(GuiJournal journal, int x, int y, int mousex, int mousey) {
		FontRenderer renderer = this.canView() ? journal.mc.fontRenderer : Physis.runeFontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		renderer.setUnicodeFlag(true);
		
		String text = Physis.text.translate(Physis.text.articlePrefix + pageText);

		renderer.drawSplitString(text, x, y, GuiJournal.pageWidth, 0x000000);

		renderer.setUnicodeFlag(unicode);
	}
}
