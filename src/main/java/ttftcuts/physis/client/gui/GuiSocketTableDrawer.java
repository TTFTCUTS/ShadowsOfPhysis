package ttftcuts.physis.client.gui;

import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.container.ContainerSocketTableDrawer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSocketTableDrawer extends GuiContainerPhysis {

	public GuiSocketTableDrawer(InventoryPlayer inventory, TileEntitySocketTable table) {
		super(new ContainerSocketTableDrawer(inventory, table));
		this.ySize = 168;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float mousex, int mousey, int partialTicks) {
		this.drawPlayerInventory(8,18);
		this.drawPlayerInventory();
	}

}
