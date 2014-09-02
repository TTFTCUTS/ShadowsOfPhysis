package ttftcuts.physis.client.gui;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiJournal extends GuiScreen {

	private int bookWidth = 350;
	private int bookHeight = 240;
	private int left;
	private int top;
	
	private static final ResourceLocation bookTextureLeft = new ResourceLocation(Physis.MOD_ID+":textures/gui/journal_left.png");
	private static final ResourceLocation bookTextureRight = new ResourceLocation(Physis.MOD_ID+":textures/gui/journal_right.png");
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.left = this.width / 2 - bookWidth / 2;
		this.top = this.height / 2 - bookHeight / 2;
		
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if(mc.gameSettings.keyBindInventory.getKeyCode() == par2) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}

		super.keyTyped(par1, par2);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(bookTextureLeft);
		drawTexturedModalRect(left, top, 0, 0, 256, this.bookHeight);
		mc.renderEngine.bindTexture(bookTextureRight);
		drawTexturedModalRect(left + 256, top, 0, 0, this.bookWidth - 256, this.bookHeight);
		
		super.drawScreen(par1, par2, par3);
	}
}
