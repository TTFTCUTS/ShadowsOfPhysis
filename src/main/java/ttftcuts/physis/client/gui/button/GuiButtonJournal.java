package ttftcuts.physis.client.gui.button;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.client.gui.GuiJournal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonJournal extends GuiButton {

	private int textureX;
	private int textureY;
	
	public GuiButtonJournal(int id, int x, int y, int w, int h, int tx, int ty) {
		super(id, x, y, w, h, "");
		
		textureX = tx;
		textureY = ty;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (!this.visible) { return; }
		par1Minecraft.renderEngine.bindTexture(GuiJournal.bookTextureRight);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		drawTexturedModalRect(xPosition, yPosition, textureX, textureY, width, height);
	}
}
