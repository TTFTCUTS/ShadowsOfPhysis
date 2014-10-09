package ttftcuts.physis.common.block.tile;

import ttftcuts.physis.common.inventory.Inventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityInventory extends TileEntityPhysis implements IInventory {

	private Inventory inventory = new Inventory(this.getSizeInventory());
	
	@Override
	public void readCustomNBT(NBTTagCompound tag) {
		super.readCustomNBT(tag);
		
		NBTTagCompound itag = tag.getCompoundTag("Items");
		this.inventory = new Inventory(this.getSizeInventory());
		
		this.inventory.readFromNBT(itag);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound tag) {
		super.writeCustomNBT(tag);
		
		NBTTagCompound invtag = new NBTTagCompound();
		this.inventory.writeToNBT(invtag);
		tag.setTag("Items", invtag);
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return this.inventory.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory.setInventorySlotContents(slot, stack);
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return this.inventory.getInventoryStackLimit();
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

	public void dropInventory() {
		this.inventory.dropItems(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}
}
