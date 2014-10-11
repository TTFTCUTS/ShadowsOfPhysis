package ttftcuts.physis.client.gui;

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
