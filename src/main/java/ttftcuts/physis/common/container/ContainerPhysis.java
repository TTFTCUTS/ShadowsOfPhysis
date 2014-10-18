package ttftcuts.physis.common.container;

import ttftcuts.physis.common.network.IGuiMessageHandler;
import ttftcuts.physis.common.network.PacketGuiMessage;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ContainerPhysis extends Container implements IGuiMessageHandler {

	public int inventoryX = 0;
	public int inventoryY = 0;
	
	protected void addPlayerInventory(InventoryPlayer inventory, int x, int y) {
		for (int sloty = 0; sloty < 3; sloty++) {
			for (int slotx = 0; slotx < 9; slotx++) {
				addSlotToContainer( new Slot(inventory, slotx + sloty * 9 + 9, x + slotx * 18, y + sloty * 18));
			}
		}
		
		for (int slotx = 0; slotx < 9; slotx++) {
			addSlotToContainer( new Slot(inventory, slotx, x + slotx * 18, y + 58));
		}
		
		this.inventoryX = x;
		this.inventoryY = y;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
		return null;
    }
	
	public void sendUiPacket(NBTTagCompound tag) {
		PhysisPacketHandler.bus.sendToServer(PacketGuiMessage.createPacket(this.windowId, tag));
		this.processMessage(Minecraft.getMinecraft().thePlayer, tag);
	}
	
	@Override
	public void processMessage(EntityPlayer player, NBTTagCompound tag) {}
}
