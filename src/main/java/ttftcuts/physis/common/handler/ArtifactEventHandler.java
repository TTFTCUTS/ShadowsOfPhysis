package ttftcuts.physis.common.handler;

import java.util.Map;
import java.util.WeakHashMap;

import ttftcuts.physis.common.helper.HorseReflectionHelper;
import ttftcuts.physis.utils.Socket;
import ttftcuts.physis.utils.SocketIterator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.world.BlockEvent;

public class ArtifactEventHandler {

	private Map<EntityPlayer,ItemStack> lastBow;
	
	public ArtifactEventHandler() {
		this.lastBow = new WeakHashMap<EntityPlayer, ItemStack>();
	}
	
	// onUpdate and onEquippedUpdate
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entityLiving.worldObj.isRemote) {return;}

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)(event.entityLiving);
			
			for(ItemStack stack : player.inventory.mainInventory) {
				if (stack != null) {
					if (stack == player.getHeldItem()) {
						
						boolean delay = false;
						
						if(player instanceof EntityPlayerMP) {
							EntityPlayerMP playerMP = (EntityPlayerMP)player;
							ItemInWorldManager iiwm = playerMP.theItemInWorldManager;
							
							if (iiwm != null) {
								int x = iiwm.partiallyDestroyedBlockX;
								int y = iiwm.partiallyDestroyedBlockY;
								int z = iiwm.partiallyDestroyedBlockZ;
								
								Block b = player.worldObj.getBlock(x,y,z);
								int m = player.worldObj.getBlockMetadata(x,y,z);
								
								if (b != null && b.canHarvestBlock(playerMP, m)) {
									delay = true;
								}
							}
						}
						if (!(event.entityLiving.isSwingInProgress && delay)) {
							doTriggerUpdate(stack, event.entityLiving);
							doTriggerEquippedUpdate(stack, event.entityLiving);
						}
					} else {
						doTriggerUpdate(stack, event.entityLiving);
					}
				}
			}
			
			// player armour
			for(ItemStack stack : player.inventory.armorInventory) {
				if (stack != null) {
					doTriggerUpdate(stack, player);
					doTriggerEquippedUpdate(stack, event.entityLiving);
				}
			}			
		} else {
			// general entity equipment
			for(int i=0; i<5; i++) {
				ItemStack item = event.entityLiving.getEquipmentInSlot(i);
				if (item != null) {
					doTriggerUpdate(item, event.entityLiving);
					doTriggerEquippedUpdate(item, event.entityLiving);
				}
			}
			
			// horse inventory
			if (event.entityLiving instanceof EntityHorse) {
				EntityHorse horse = (EntityHorse) (event.entityLiving);
				if (horse != null) {
					AnimalChest inv = HorseReflectionHelper.getHorseChest(horse);
					if (inv != null) {
						// equipped updates for saddle/armour
						ItemStack item = inv.getStackInSlot(0);
						if (item != null) {
							doTriggerEquippedUpdate(item, event.entityLiving);
						}
						item = inv.getStackInSlot(1);
						if (item != null) {
							doTriggerEquippedUpdate(item, event.entityLiving);
						}
						
						// general update on all slots
						for (int i=0; i<inv.getSizeInventory(); i++) {
							item = inv.getStackInSlot(i);
							if (item != null) {
								doTriggerUpdate(item, event.entityLiving);
							}
						}
					}
				}
			}
		}
	}
	
	// prevents updates being delayed forever by digging
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if (event.getPlayer().worldObj.isRemote) { return; }
		
		EntityPlayer player = event.getPlayer();
		if (player != null && player.getHeldItem() != null) {

			doTriggerUpdate(player.getHeldItem(), player);
			doTriggerEquippedUpdate(player.getHeldItem(), player);
		}
	}
	
	// onDealDamage
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if (event.entity.worldObj.isRemote) { return; }
		
		Entity damagesource = event.source.getSourceOfDamage();
		EntityLivingBase damager = null;
		if (damagesource instanceof EntityLivingBase) {
			damager = (EntityLivingBase)damagesource;
		}
		
		if (damager != null) {
			if (damager instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)damager;

				if (player.getHeldItem() != null) {
					doTriggerOnDealDamage(player.getHeldItem(), event.entityLiving, player);
				}
			} else {
				ItemStack stack = damager.getEquipmentInSlot(0);
				if (stack != null) {
					doTriggerOnDealDamage(stack, event.entityLiving, damager);
				}
			}
		}
	}
	
	// onTakeDamage, and onDealDamage from ranged weapons
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event){
		if (event.entity.worldObj.isRemote) { return; }
		
		Entity damagesource = event.source.getSourceOfDamage();
		EntityLivingBase damager = null;
		if (damagesource instanceof EntityLivingBase) {
			damager = (EntityLivingBase)damagesource;
		}
		
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			
			if (player.getHeldItem() != null) {
				doTriggerOnTakeDamage(player.getHeldItem(), player, damager);
			}
			
			for(ItemStack armour: player.inventory.armorInventory) {
				if (armour != null) {
					doTriggerOnTakeDamage(armour, player, damager);
				}
			}
		} else {
			for(int i=0; i<5; i++) {
				ItemStack item = event.entityLiving.getEquipmentInSlot(i);
				if (item != null) {
					doTriggerOnTakeDamage(item, event.entityLiving, damager);
				}
			}
		}
		
		if (event.source.isProjectile() 
				&& event.source.getEntity() != null 
				&& event.source.getEntity() instanceof EntityPlayer) {
			
			EntityPlayer damagePlayer = (EntityPlayer)event.source.getEntity();
			
			ItemStack bow = this.lastBow.get(damagePlayer);
			if (bow != null) {
				doTriggerOnDealDamage(bow, event.entityLiving, damagePlayer);
			}
		}
	}
	
	// ############### utility events ###############
	
	@SubscribeEvent
	public void onArrowLoose(ArrowLooseEvent event) {
		if (event.entity.worldObj.isRemote) { return; }
		
		if (event.entityPlayer != null && event.entityPlayer.getHeldItem() != null) {
			this.lastBow.put(event.entityPlayer, event.entityPlayer.getHeldItem());
		}
	}
	
	// ############### trigger calling ###############
	
	private void doTriggerUpdate(ItemStack stack, EntityLivingBase target) {
		for(Socket socket : SocketIterator.triggers(stack)) {
			if (socket.trigger != null) {
				socket.trigger.onUpdate(stack, target, socket.slot);
			}
		}
	}
	
	private void doTriggerEquippedUpdate(ItemStack stack, EntityLivingBase target) {
		for(Socket socket : SocketIterator.triggers(stack)) {
			if (socket.trigger != null) {
				socket.trigger.onEquippedUpdate(stack, target, socket.slot);
			}
		}
	}
	
	private void doTriggerOnDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
		for(Socket socket : SocketIterator.triggers(stack)) {
			if (socket.trigger != null) {
				socket.trigger.onDealDamage(stack, target, source, socket.slot);
			}
		}
	}
	
	private void doTriggerOnTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
		for(Socket socket : SocketIterator.triggers(stack)) {
			if (socket.trigger != null) {
				socket.trigger.onTakeDamage(stack, target, source, socket.slot);
			}
		}
	}
}
