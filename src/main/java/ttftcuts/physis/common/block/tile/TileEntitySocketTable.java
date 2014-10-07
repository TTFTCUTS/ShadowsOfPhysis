package ttftcuts.physis.common.block.tile;

import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class TileEntitySocketTable extends TileEntityInventory implements ISidedInventory {

	@Override
	public int getSizeInventory() {
		return 6;
	}

	@Override
	public String getInventoryName() {
		return "test";
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

}
