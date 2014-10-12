package ttftcuts.physis.client.gui;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.container.ContainerSocketTable;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSocketTable extends GuiContainerPhysis {

	public GuiSocketTable(InventoryPlayer inventory, TileEntitySocketTable table) {
		super(new ContainerSocketTable(inventory, table));
		this.ySize = 222;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float mousex, int mousey, int partialTicks) {
		ContainerSocketTable cont = (ContainerSocketTable)this.inventorySlots;
		
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
