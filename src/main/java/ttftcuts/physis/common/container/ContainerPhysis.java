package ttftcuts.physis.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerPhysis extends Container {

	protected void addPlayerInventory(InventoryPlayer inventory, int x, int y) {
		for (int sloty = 0; sloty < 3; sloty++) {
			for (int slotx = 0; slotx < 9; slotx++) {
				addSlotToContainer( new Slot(inventory, slotx + sloty * 9 + 9, x + slotx * 18, y + sloty * 18));
			}
		}
		
		for (int slotx = 0; slotx < 9; slotx++) {
			addSlotToContainer( new Slot(inventory, slotx, x + slotx * 18, y + 58));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
		return null;
    }
}
