package ttftcuts.physis.common.container.slot;

import ttftcuts.physis.api.artifact.ISocketable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSocketable extends Slot {

	public SlotSocketable(IInventory inventory, int slotid, int x, int y) {
		super(inventory, slotid, x,y);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
    {
		return stack.getItem() instanceof ISocketable;
    }
}
