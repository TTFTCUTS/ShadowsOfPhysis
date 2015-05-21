package ttftcuts.physis.client.gui.journal;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import ttftcuts.physis.client.ClientProxy;
import ttftcuts.physis.client.gui.GuiJournal;

public abstract class JournalPage {

	public JournalPage() {}
	
	public void initGui(GuiJournal journal) {}
	
	public List<GuiButton> getNavButtonsForPage(int id, int x, int y) { return null; }
	
	public void actionPerformed(GuiJournal journal, int id, GuiButton button) {}
	
	@SideOnly(Side.CLIENT)
	public void drawPage(GuiJournal journal, int x, int y, int mousex, int mousey) {}
	
	public boolean canView() { return true; }
	
	@SideOnly(Side.CLIENT)
	public FontRenderer getFont() {
		return this.canView() ? ClientProxy.bookFontRenderer : ClientProxy.bookRuneFontRenderer;
		//return this.canView() ? Minecraft.getMinecraft().fontRenderer : ClientProxy.runeFontRenderer;
	}
}
