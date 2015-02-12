package ttftcuts.physis.common.compat.baubles;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ttftcuts.physis.common.compat.CompatModule;
import ttftcuts.physis.common.handler.ArtifactEventHandler;

public class CompatBaubles extends CompatModule {

	@Override
	public void preInit(FMLPreInitializationEvent event, boolean client) {
		if (!client) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}
	
	@Override
	public void init(FMLInitializationEvent event, boolean client) {
		
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event, boolean client) {
		
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entityLiving.worldObj.isRemote) {return;}

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)(event.entityLiving);
			IInventory baubles = BaublesApi.getBaubles(player);
			
			for (int i=0; i<baubles.getSizeInventory(); i++) {
				ItemStack stack = baubles.getStackInSlot(i);
				if (stack != null) {
					ArtifactEventHandler.doTriggerUpdate(stack, player);
					ArtifactEventHandler.doTriggerEquippedUpdate(stack, player);
				}
			}
		}
	}
}
