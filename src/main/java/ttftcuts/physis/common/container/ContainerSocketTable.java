package ttftcuts.physis.common.container;

import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.container.slot.SlotItemWithSockets;
import ttftcuts.physis.common.container.slot.SlotSocketable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerSocketTable extends ContainerPhysis {

	private TileEntitySocketTable table;
	
	private SlotItemWithSockets mainSlot;
	private SlotSocketable[] socketSlots;
	public int activeSlots = 0;
	
	public ContainerSocketTable(InventoryPlayer inventory, TileEntitySocketTable table) {
		this.table = table;
		
		mainSlot = new SlotItemWithSockets(table, 0, 26, 65);
		
		addSlotToContainer(mainSlot);
		
		socketSlots = new SlotSocketable[5];
		activeSlots = 0;
		
		for (int i = 0; i<5; i++) {
			socketSlots[i] = new SlotSocketable(table, i+1, 40 + 20 * i, 60);
			addSlotToContainer(socketSlots[i]);
		}
		
		this.addPlayerInventory(inventory, 8, 139);
		this.updateLayout();
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
	
	public void updateLayout() {
		
		ItemStack stack = mainSlot.getStack();
		
		if (stack != null) {
			NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
			
			if (sockets != null) {
				this.activeSlots = sockets.length;
			} else {
				this.activeSlots = 0;
			}
		} else {
			this.activeSlots = 0;
		}
		
		for (int i=0; i<5; i++) {
			Slot s = this.socketSlots[i];
			if (i < this.activeSlots) {
				// put in place
				s.xDisplayPosition = 98;
				s.yDisplayPosition = 65 + 18 * i - 9 * (this.activeSlots-1);
			} else {
				// put at the side
				s.xDisplayPosition = 151;
				s.yDisplayPosition = 29 + 18 * (i - this.activeSlots);
			}
		}
	}
}
