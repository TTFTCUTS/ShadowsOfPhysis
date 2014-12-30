package ttftcuts.physis.common.block.tile;

import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.inventory.Inventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySocketTable extends TileEntityInventory implements ISidedInventory {

	public static final int INSERTCOST = 3;
	public static final int REMOVECOST = 1;
	
	public int facing = 0;
	
	public Inventory drawer;
	
	public TileEntitySocketTable() {
		super();
		
		this.drawer = new Inventory(27);
	}
	
	@Override
	public int getSizeInventory() {
		return 7;
	}

	@Override
	public String getInventoryName() {
		return "Socketing Table";
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 0) {
			return PhysisArtifacts.getSocketablesFromStack(stack) != null;
		} else {
			return stack.getItem() instanceof ISocketable;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack,	int side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound tag) {
		super.readCustomNBT(tag);
		
		this.facing = tag.getByte("facing");
		
		this.drawer = new Inventory(27);
		NBTTagCompound itag = tag.getCompoundTag("drawer");
		this.drawer.readFromNBT(itag);
		
		
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound tag) {
		super.writeCustomNBT(tag);
		
		tag.setByte("facing", (byte)this.facing);
		
		NBTTagCompound itag = new NBTTagCompound();
		this.drawer.writeToNBT(itag);
		tag.setTag("drawer", itag);
	}
	
	@Override
	public void dropInventory() {
		super.dropInventory();
		this.drawer.dropItems(worldObj, xCoord, yCoord, zCoord);
	}
	
	public static boolean checkReagentValidity(ItemStack stack) {
		// is it lapis?
		if (stack.getItem() == Items.dye && stack.getItemDamage() == 4) {
			return true;
		}
		return false;
	}
}
