package ttftcuts.physis.common.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class SlotShowSocketable extends SlotSocketable {

	public SlotShowSocketable(IInventory inventory, int slotid, int x, int y) {
		super(inventory, slotid, x, y);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player)
    {
        return false;
    }
}
