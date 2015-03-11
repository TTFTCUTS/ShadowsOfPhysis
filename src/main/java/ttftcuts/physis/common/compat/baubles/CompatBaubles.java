package ttftcuts.physis.common.compat.baubles;

import java.util.List;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import ttftcuts.physis.common.artifact.LootSystem;
import ttftcuts.physis.common.compat.CompatModule;
import ttftcuts.physis.common.handler.ArtifactEventHandler;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;

public class CompatBaubles extends CompatModule {

	public static Item ring;
	
	@Override
	public void preInitEnd(FMLPreInitializationEvent event, boolean client) {
		if (!client) {
			MinecraftForge.EVENT_BUS.register(this);
			
			ring = new ItemMaterialRing();
			GameRegistry.registerItem(ring, "baubleRing");
		}
	}
	
	@Override
	public void initEnd(FMLInitializationEvent event, boolean client) {
		LootSystem.digSiteLootList.addItemStackChestGen(new ItemStack(ring), 1, 1, 600, 0.3);
	}
	
	@Override
	public void loadFinished(FMLLoadCompleteEvent event, boolean client) {
		if (!client) {
			for (PhysisToolMaterial mat : PhysisToolMaterial.materials.values()) {
				if (mat.orename.startsWith("ingot")) {
					List<ItemStack> ingots = OreDictionary.getOres(mat.orename);
					if (ingots.size() > 0) {
						ItemStack ingot = ingots.get(0);
						
						ItemStack ringitem = new ItemStack(ring, 1, mat.id);
						PhysisToolMaterial.writeMaterialToStack(mat, ringitem);
						
						GameRegistry.addSmelting(ringitem, ingot.copy(), 1.0F);
					}
				}
			}
		}
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
