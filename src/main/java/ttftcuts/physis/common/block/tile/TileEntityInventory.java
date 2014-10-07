package ttftcuts.physis.common.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityInventory extends TileEntityPhysis implements IInventory {

	ItemStack[] inventory = new ItemStack[this.getSizeInventory()];
	
	@Override
	public void readCustomNBT(NBTTagCompound tag) {
		super.readCustomNBT(tag);
		
		NBTTagList list = tag.getTagList("Items", 10);
		this.inventory = new ItemStack[this.getSizeInventory()];
		
		for (int i=0; i<list.tagCount(); i++) {
			NBTTagCompound itag = list.getCompoundTagAt(i);
			byte slot = itag.getByte("Slot");
			if (slot >= 0 && slot < this.inventory.length) {
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(itag);
			}
		}
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound tag) {
		super.writeCustomNBT(tag);
		
		NBTTagList list = new NBTTagList();
		for(int i=0; i<this.inventory.length; i++) {
			if (this.inventory[i] != null) {
				NBTTagCompound itag = new NBTTagCompound();
				itag.setByte("Slot", (byte)i);
				
				this.inventory[i].writeToNBT(itag);
				list.appendTag(itag);
			}
		}
		tag.setTag("Items", list);
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.inventory[slot] != null) {
			ItemStack stack; 
			
			if (this.inventory[slot].stackSize <= amount) {
				stack = this.inventory[slot];
				this.inventory[slot] = null;
				return stack;
			} else {
				stack = this.inventory[slot].splitStack(amount);
				
				if (this.inventory[slot].stackSize == 0) {
					this.inventory[slot] = null;
				}
				return stack;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		TileEntity tile = player.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
		
		if (tile != this) { return false; }
		
		return player.getDistanceSq(this.xCoord, this.yCoord, this.zCoord) < 64;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

}
