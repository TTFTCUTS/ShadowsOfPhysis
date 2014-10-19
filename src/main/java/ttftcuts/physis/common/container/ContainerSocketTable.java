package ttftcuts.physis.common.container;

import ttftcuts.physis.Physis;
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
	private Slot materialSlot;
	private SlotSocketable[] socketSlots;
	public int activeSlots = 0;
	
	public ContainerSocketTable(InventoryPlayer inventory, TileEntitySocketTable table) {
		this.table = table;
		
		mainSlot = new SlotItemWithSockets(table, 0, 8, 65);
		addSlotToContainer(mainSlot);
		
		materialSlot = new Slot(table, 1, 8, 101);
		addSlotToContainer(materialSlot);
		
		socketSlots = new SlotSocketable[5];
		activeSlots = 0;
		
		for (int i = 0; i<5; i++) {
			socketSlots[i] = new SlotSocketable(table, i+2, 40 + 20 * i, 60);
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
				s.xDisplayPosition = 107;
				s.yDisplayPosition = 65 + 18 * i - 9 * (this.activeSlots-1);
			} else {
				// put at the side
				s.xDisplayPosition = 151;
				s.yDisplayPosition = 29 + 18 * (i - this.activeSlots);
			}
		}
	}

	@Override
	public void processMessage(EntityPlayer player, NBTTagCompound tag) {
		int id = tag.getInteger("id");
		Physis.logger.info("Button "+id+" pressed");
		
		ItemStack mainitem = this.mainSlot.getStack();
		if (mainitem != null) {
			NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(mainitem);
			Physis.logger.info(mainitem);
			if (id < this.activeSlots && id < sockets.length) {
				//Physis.logger.info("test");
				boolean left = sockets[id] != null;
				boolean right = this.socketSlots[id].getHasStack();
				
				int mats = this.getReagentCount();
				Physis.logger.info(mats);
				
				if (left && !right && mats >= TileEntitySocketTable.REMOVECOST) {
					// unsocket
					ItemStack stack = ItemStack.loadItemStackFromNBT(sockets[id]);
					PhysisArtifacts.removeItemFromSocket(mainitem, id);
					this.socketSlots[id].putStack(stack);
					this.consumeReagent(TileEntitySocketTable.REMOVECOST);
				} else if (right && !left && mats >= TileEntitySocketTable.INSERTCOST) {
					// socket
					ItemStack toSocket = this.socketSlots[id].getStack();
					this.socketSlots[id].putStack(null);
					
					PhysisArtifacts.addItemToSocket(mainitem, toSocket, id);
					this.consumeReagent(TileEntitySocketTable.INSERTCOST);
				}
			}
		}
	}
	
	public int getReagentCount() {
		if (this.materialSlot.getHasStack()) {
			return this.materialSlot.getStack().stackSize;
		}
		return 0;
	}
	
	public void consumeReagent(int number) {
		if (this.materialSlot.getHasStack()) {
			ItemStack stack = this.materialSlot.getStack();
			stack.splitStack(number);
			if (stack.stackSize <= 0) {
				this.materialSlot.putStack(null);
			}
		}
	}
}
