package ttftcuts.physis.client.gui.journal;

import java.util.List;

import net.minecraft.client.gui.GuiButton;

import ttftcuts.physis.client.gui.GuiJournal;

public abstract class JournalPage {

	public JournalPage() {}
	
	public void initGui(GuiJournal journal) {}
	
	public List<GuiButton> getNavButtonsForPage(int id, int x, int y) { return null; }
	
	public void actionPerformed(GuiJournal journal, int id, GuiButton button) {}
	
	public void drawPage(GuiJournal journal, int x, int y) {}
	
}
