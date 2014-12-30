package ttftcuts.physis.common.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends Slot {

	public SlotFilter filter;
	public boolean canTake = true;
	
	public SlotFiltered(IInventory inventory, int slotid, int x, int y, SlotFilter filter, boolean canTake) {
		super(inventory, slotid, x,y);
		this.filter = filter;
		this.canTake = canTake;
	}
	
	public SlotFiltered(IInventory inventory, int slotid, int x, int y, SlotFilter filter) {
		this(inventory, slotid, x,y, filter, true);
	}
	
	public SlotFiltered(IInventory inventory, int slotid, int x, int y, boolean canTake) {
		this(inventory, slotid, x,y, null, canTake);
	}
	
	public SlotFiltered(IInventory inventory, int slotid, int x, int y) {
		this(inventory, slotid, x,y, null, true);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
    {
		return this.filter == null || this.filter.isValidStack(stack);
    }
	
	@Override
	public boolean canTakeStack(EntityPlayer player)
    {
        return this.canTake;
    }
}
