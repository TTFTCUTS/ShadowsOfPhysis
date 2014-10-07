package ttftcuts.physis.common.container;

import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSocketTable extends ContainerPhysis {

	private TileEntitySocketTable table;
	
	private Slot[] socketSlots;
	
	public ContainerSocketTable(InventoryPlayer inventory, TileEntitySocketTable table) {
		this.table = table;
		
		addSlotToContainer(new Slot(table, 0, 40, 20));
		
		socketSlots = new Slot[5];
		
		for (int i = 0; i<5; i++) {
			socketSlots[i] = new Slot(table, i+1, 40 + 20 * i, 40);
			addSlotToContainer(socketSlots[i]);
		}
		
		this.addPlayerInventory(inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.table.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
		return null;
    }
}
