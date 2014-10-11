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
}
