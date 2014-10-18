package ttftcuts.physis.client.gui;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.container.ContainerPhysis;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiContainerPhysis extends GuiContainer {

	public static ResourceLocation inventoryTexture = new ResourceLocation(Physis.MOD_ID, "textures/gui/inventory.png");
	
	public GuiContainerPhysis(Container container) {
		super(container);
	}

	protected void drawPlayerInventory(int x, int y) {
		GL11.glColor3f(1f, 1f, 1f);
		mc.renderEngine.bindTexture(inventoryTexture);
		this.drawTexturedModalRect(this.guiLeft + x-8, this.guiTop + y-8, 0, 0, 176, 90);
	}
	
	protected void drawPlayerInventory() {
		if (this.inventorySlots != null && this.inventorySlots instanceof ContainerPhysis) {
			ContainerPhysis container = (ContainerPhysis)this.inventorySlots;
			
			this.drawPlayerInventory(container.inventoryX, container.inventoryY);
		} else {
			this.drawPlayerInventory(0, 0);
		}
	}
	
	protected void drawWoodenSlot(int x, int y) {
		GL11.glColor3f(1f, 1f, 1f);
		mc.renderEngine.bindTexture(inventoryTexture);
		this.drawTexturedModalRect(this.guiLeft + x-16, this.guiTop + y-16, 208, 0, 48, 48);
	}
	
	protected void drawWoodenColumn(int x, int y, int number) {
		GL11.glColor3f(1f, 1f, 1f);
		mc.renderEngine.bindTexture(inventoryTexture);
		this.drawTexturedModalRect(this.guiLeft + x-16, this.guiTop + y-16, 208, 0, 48, 15);
		
		for (int i=0; i<number; i++) {
			this.drawTexturedModalRect(this.guiLeft + x-16, this.guiTop + y - 1 + i*18, 208, 15, 48, 18);
		}
		
		this.drawTexturedModalRect(this.guiLeft + x-16, this.guiTop + y-1 + number*18, 208, 33, 48, 15);
	}
	
	protected void drawSlotOverlay(int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glColorMask(true, true, true, false);
        this.drawGradientRect(x, y, x + 16, y + 16, -2130706433, -2130706433);
        GL11.glColorMask(true, true, true, true);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
