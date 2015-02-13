package ttftcuts.physis.common.compat;

import travellersgear.api.TravellersGearAPI;
import ttftcuts.physis.common.handler.ArtifactEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CompatTravellersGear extends CompatModule {

	@Override
	public void preInit(FMLPreInitializationEvent event, boolean client) {
		if (!client) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entityLiving.worldObj.isRemote) {return;}

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)(event.entityLiving);
			ItemStack[] gear = TravellersGearAPI.getExtendedInventory(player);
			
			for (int i=0; i<gear.length; i++) {
				if (gear[i] != null) {
					ArtifactEventHandler.doTriggerUpdate(gear[i], player);
					ArtifactEventHandler.doTriggerEquippedUpdate(gear[i], player);
				}
			}
		}
	}
}
