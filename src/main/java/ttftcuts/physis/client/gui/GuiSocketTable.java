package ttftcuts.physis.client.gui;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.container.ContainerSocketTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSocketTable extends GuiContainerPhysis {

	private static final ResourceLocation uiTexture = new ResourceLocation(Physis.MOD_ID, "textures/gui/sockettable.png");
	
	public GuiSocketTable(InventoryPlayer inventory, TileEntitySocketTable table) {
		super(new ContainerSocketTable(inventory, table));
		this.ySize = 222;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float mousex, int mousey, int partialTicks) {
		ContainerSocketTable cont = (ContainerSocketTable)this.inventorySlots;
		
		mc.renderEngine.bindTexture(uiTexture);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		this.drawTexturedModalRect(this.guiLeft - 40, this.guiTop - 65, 0, 0, 256, 256);
		
		this.drawWoodenSlot(26, 65);
		
		int sideslots = 5 - cont.activeSlots;
		
		if (sideslots > 0) {
			mc.renderEngine.bindTexture(inventoryTexture);
			
			this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 21, 176, 0, 32, 7);
			for (int i=0; i<sideslots; i++) {
				this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 28 + 18*i, 176, 7, 32, 18);
			}
			this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 28 + 18 * sideslots, 176, 25, 32, 7);
		}
		
		
		this.drawPlayerInventory();
	}

	@Override
	public void updateScreen() {
		
		ContainerSocketTable cont = (ContainerSocketTable)this.inventorySlots;
		
		if (cont != null) {
			cont.updateLayout();
		}
		
		super.updateScreen();
	}
}
