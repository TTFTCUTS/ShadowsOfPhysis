package ttftcuts.physis.client.gui.journal;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.GuiJournal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class JournalPageImage extends JournalPage {
	
	public String[] imagepaths;
	public Map<String, ResourceLocation> images;
	public String caption;
	
	public JournalPageImage(String caption, String... imagepaths) {
		super();
		
		this.caption = caption;
		this.imagepaths = imagepaths;
		this.images = new HashMap<String, ResourceLocation>();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawPage(GuiJournal journal, int x, int y, int mousex, int mousey) {
		
		GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		for (int i=0; i<this.imagepaths.length; i++) {
			String translated = Physis.text.translate(this.imagepaths[i]);
			
			if (!this.images.containsKey(translated)) {
				this.images.put(translated, new ResourceLocation(Physis.MOD_ID, translated));
			}
			
			journal.mc.renderEngine.bindTexture(images.get(translated));
			
			journal.drawTexturedModalRect(x, y, 0, 0, GuiJournal.pageWidth, GuiJournal.pageHeight-10);
		}
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		
		FontRenderer renderer = this.getFont();
		boolean unicode = renderer.getUnicodeFlag();
		renderer.setUnicodeFlag(true);
		
		String text = Physis.text.translate(Physis.text.imagePrefix + this.caption);

		//renderer.drawSplitString(text, x + (GuiJournal.pageWidth / 2) - (renderer.getStringWidth(text) / 2), y + GuiJournal.pageHeight - 10, GuiJournal.pageWidth, 0x000000);
		journal.drawJournalSplitString(renderer, text, x + (GuiJournal.pageWidth / 2) - (renderer.getStringWidth(text) / 2), y + GuiJournal.pageHeight - 10, GuiJournal.pageWidth, 0x000000, false);
		
		renderer.setUnicodeFlag(unicode);
	}
}
