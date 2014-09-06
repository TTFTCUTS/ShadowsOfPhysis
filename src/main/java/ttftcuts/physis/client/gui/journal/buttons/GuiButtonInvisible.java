package ttftcuts.physis.client.gui.journal.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonInvisible extends GuiButton {
	public GuiButtonInvisible(int id, int x, int y, int w, int h) {
		super(id, x, y, w, h, "");
	}
	
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {}
}
