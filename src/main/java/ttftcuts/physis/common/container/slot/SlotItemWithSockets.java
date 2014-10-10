package ttftcuts.physis.common.container.slot;

import ttftcuts.physis.common.artifact.PhysisArtifacts;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SlotItemWithSockets extends Slot {

	public SlotItemWithSockets(IInventory inventory, int slotid, int x, int y) {
		super(inventory, slotid, x,y);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
    {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		
		if (sockets != null && sockets.length > 0) {
			return true;
		}
		
        return false;
    }
}
