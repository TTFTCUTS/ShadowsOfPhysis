package ttftcuts.physis.common.container;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.container.slot.SlotSocketable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSocketTableDrawer extends ContainerPhysis {

private TileEntitySocketTable table;

	private final int numRows = 3;
	private final int numColumns = 9;
	
	public ContainerSocketTableDrawer(InventoryPlayer inventory, TileEntitySocketTable table) {
		this.table = table;
		
		for (int y=0; y<numRows; y++) {
			for (int x=0; x<numColumns; x++) {
				this.addSlotToContainer(new SlotSocketable(table.drawer, y*numColumns + x, 8 + x * 18, 20 + y * 18));
			}
		}
		
		this.addPlayerInventory(inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.table.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotid)
    {
		ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotid);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            Physis.logger.info("Slot id: "+slotid);
            
            if (slotid < this.numRows * 9)
            {
            	Physis.logger.info("< rows*9");
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!(itemstack1.getItem() instanceof ISocketable) || !this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
