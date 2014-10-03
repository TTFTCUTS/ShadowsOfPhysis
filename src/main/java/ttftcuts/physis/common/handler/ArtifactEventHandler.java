package ttftcuts.physis.common.handler;

import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class ArtifactEventHandler {

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)(event.entityLiving);
			
			for(ItemStack stack : player.inventory.mainInventory) {
				if (stack != null) {
					doTriggerUpdate(stack, event.entityLiving);
				}
			}
			
			for(ItemStack stack : player.inventory.armorInventory) {
				if (stack != null) {
					doTriggerUpdate(stack, player);
					doTriggerEquippedUpdate(stack, event.entityLiving);
				}
			}
			
			if (player.getHeldItem() != null) {
				doTriggerEquippedUpdate(player.getHeldItem(), event.entityLiving);
			}
			
		} else {
			for(int i=0; i<5; i++) {
				ItemStack item = event.entityLiving.getEquipmentInSlot(i);
				if (item != null) {
					doTriggerUpdate(item, event.entityLiving);
					doTriggerEquippedUpdate(item, event.entityLiving);
				}
			}
		}
	}
	
	private void doTriggerUpdate(ItemStack stack, EntityLivingBase target) {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		if (sockets != null) {
			for (int i=0; i<sockets.length; i++) {
				IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(sockets[i]);
				
				trigger.onUpdate(target, i);
				
				PhysisArtifacts.doEffectCooldownTick(sockets[i]);
			}
		}
	}
	
	private void doTriggerEquippedUpdate(ItemStack stack, EntityLivingBase target) {
		
	}
}
