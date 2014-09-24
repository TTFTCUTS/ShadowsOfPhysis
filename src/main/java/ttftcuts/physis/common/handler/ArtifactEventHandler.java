package ttftcuts.physis.common.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class ArtifactEventHandler {

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)(event.entityLiving);
			
			for(ItemStack stack : player.inventory.mainInventory) {
				if (stack != null) {
					doTriggerUpdate(stack);
				}
			}
			
			for(ItemStack stack : player.inventory.armorInventory) {
				if (stack != null) {
					doTriggerUpdate(stack);
					doTriggerEquippedUpdate(stack);
				}
			}
			
			if (player.getHeldItem() != null) {
				doTriggerEquippedUpdate(player.getHeldItem());
			}
			
		} else {
			for(int i=0; i<5; i++) {
				ItemStack item = event.entityLiving.getEquipmentInSlot(i);
				if (item != null) {
					doTriggerUpdate(item);
					doTriggerEquippedUpdate(item);
				}
			}
		}
	}
	
	private void doTriggerUpdate(ItemStack stack) {
		
	}
	
	private void doTriggerEquippedUpdate(ItemStack stack) {
		
	}
}
