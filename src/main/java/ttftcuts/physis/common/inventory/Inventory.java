package ttftcuts.physis.common.inventory;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class Inventory implements IInventory {

	private Random rng = new Random();
	private ItemStack[] items;
	private int size;
	
	public Inventory(int slotcount) {
		this.items = new ItemStack[slotcount];
		this.size = slotcount;
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList list = tag.getTagList("Items", 10);
		this.items = new ItemStack[this.size];
		
		for (int i=0; i<list.tagCount(); i++) {
			NBTTagCompound itag = list.getCompoundTagAt(i);
			byte slot = itag.getByte("Slot");
			if (slot >= 0 && slot < this.items.length) {
				this.items[slot] = ItemStack.loadItemStackFromNBT(itag);
			}
		}
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList list = new NBTTagList();
		for(int i=0; i<this.items.length; i++) {
			if (this.items[i] != null) {
				NBTTagCompound itag = new NBTTagCompound();
				itag.setByte("Slot", (byte)i);
				
				this.items[i].writeToNBT(itag);
				list.appendTag(itag);
			}
		}
		tag.setTag("Items", list);
	}
	
	public ItemStack getStackInSlot(int slot) {
		return this.items[slot];
	}

	public ItemStack decrStackSize(int slot, int amount) {
		if (this.items[slot] != null) {
			ItemStack stack; 
			
			if (this.items[slot].stackSize <= amount) {
				stack = this.items[slot];
				this.items[slot] = null;
				return stack;
			} else {
				stack = this.items[slot].splitStack(amount);
				
				if (this.items[slot].stackSize == 0) {
					this.items[slot] = null;
				}
				return stack;
			}
		}
		return null;
	}

	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (slot < 0 || slot >= this.size) {return;}
		items[slot] = stack;
	}
	
	public int getInventoryStackLimit() {
		return 64;
	}
	
	public void dropItemFromSlot(World world, int x, int y, int z, int slot) {
		ItemStack stack = getStackInSlot(slot);
		
		if (stack != null) {
			float ox = rng.nextFloat() * 0.8f + 0.1f;
			float oy = rng.nextFloat() * 0.8f + 0.1f;
			float oz = rng.nextFloat() * 0.8f + 0.1f;
			
			while(stack.stackSize > 0) {
				int dropsize = rng.nextInt(21) + 10;
				
				if (dropsize > stack.stackSize) {
					dropsize = stack.stackSize;
				}
				
				stack.stackSize -= dropsize;
				
				EntityItem drop = new EntityItem(world, x+ox, y+oy, z+oz, new ItemStack(stack.getItem(), dropsize, stack.getItemDamage()));
				
				float maxvel = 0.05f;
				
				drop.motionX = (float)rng.nextGaussian() * maxvel;
				drop.motionY = (float)rng.nextGaussian() * maxvel + 0.2f;
				drop.motionZ = (float)rng.nextGaussian() * maxvel;
				
				if (stack.hasTagCompound()) {
					drop.getEntityItem().setTagCompound((NBTTagCompound) stack.stackTagCompound.copy());
				}
				
				world.spawnEntityInWorld(drop);
			}
		}
	}
	
	public void dropItems(World world, int x, int y, int z) {
		for(int i=0; i<this.items.length; i++) {
			this.dropItemFromSlot(world, x, y, z, i);
		}
	}

	//#################### after here is all about IInventory so it can be directly used in slots ####################
	@Override
	public int getSizeInventory() {	return this.size; }
	@Override
	public String getInventoryName() { return null;	}
	@Override
	public boolean hasCustomInventoryName() { return false; }
	@Override
	public void markDirty() {}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {	return true; }
	@Override
	public void openInventory() {}
	@Override
	public void closeInventory() {}
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) { return false; }
}
