package ttftcuts.physis.client.gui.button;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonJournal extends GuiButton {

	private int textureX;
	private int textureY;
	
	private int colour = -1;
	private int hovercolour = -1;
	
	public GuiButtonJournal(int id, int x, int y, int w, int h, int tx, int ty) {
		super(id, x, y, w, h, "");
		
		textureX = tx;
		textureY = ty;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int mousex, int mousey) {
		if (!this.visible) { return; }
		
		boolean hover = mousex >= this.xPosition && mousey >= this.yPosition && mousex < this.xPosition + this.width && mousey < this.yPosition + this.height;
		
		par1Minecraft.renderEngine.bindTexture(GuiJournal.bookTextureRight);
		this.setColourFromHover(hover);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		drawTexturedModalRect(xPosition, yPosition, textureX, textureY, width, height);
	}
	
	protected void setColourFromHover(boolean hover) {
		if (hover) {
			if (hovercolour != -1) {
				GL11.glColor4f(
					TextureHelper.red(hovercolour)/255f, 
					TextureHelper.green(hovercolour)/255f, 
					TextureHelper.blue(hovercolour)/255f, 
					TextureHelper.alpha(hovercolour)/255f
				);
				return;
			}
		} else {
			if (colour != -1) {
				GL11.glColor4f(
					TextureHelper.red(colour)/255f, 
					TextureHelper.green(colour)/255f, 
					TextureHelper.blue(colour)/255f, 
					TextureHelper.alpha(colour)/255f
				);
				return;
			}
		}
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
	
	public void setColours(int colour, int hovercolour) {
		this.colour = colour;
		this.hovercolour = hovercolour;
	}
}
