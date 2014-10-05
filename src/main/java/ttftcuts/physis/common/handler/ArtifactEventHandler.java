package ttftcuts.physis.common.handler;

import java.util.Map;
import java.util.WeakHashMap;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
			
			for(ItemStack stack : player.inventory.armorInventory) {
				if (stack != null) {
					doTriggerUpdate(stack, player);
					doTriggerEquippedUpdate(stack, event.entityLiving);
				}
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
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if (event.getPlayer().worldObj.isRemote) { return; }
		
		EntityPlayer player = event.getPlayer();
		if (player != null && player.getHeldItem() != null) {
			Physis.logger.info("player");
			// prevents delaying effects forever by digging
			doTriggerUpdate(player.getHeldItem(), player);
			doTriggerEquippedUpdate(player.getHeldItem(), player);
		}
	}
	
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
	
	// ############### util events ###############
	
	@SubscribeEvent
	public void onArrowLoose(ArrowLooseEvent event) {
		if (event.entity.worldObj.isRemote) { return; }
		
		if (event.entityPlayer != null && event.entityPlayer.getHeldItem() != null) {
			this.lastBow.put(event.entityPlayer, event.entityPlayer.getHeldItem());
		}
	}
	
	// ############### trigger calling ###############
	
	private void doTriggerUpdate(ItemStack stack, EntityLivingBase target) {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		if (sockets != null) {
			for (int i=0; i<sockets.length; i++) {
				if (sockets[i] != null) {
					IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(sockets[i]);
					if (trigger != null) {
						trigger.onUpdate(stack, target, i);
					}
				}
			}
		}
	}
	
	private void doTriggerEquippedUpdate(ItemStack stack, EntityLivingBase target) {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		if (sockets != null) {
			for (int i=0; i<sockets.length; i++) {
				if (sockets[i] != null) {
					IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(sockets[i]);
					if (trigger != null) {
						trigger.onEquippedUpdate(stack, target, i);
					}
				}
			}
		}
	}
	
	private void doTriggerOnDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		if (sockets != null) {
			for (int i=0; i<sockets.length; i++) {
				if (sockets[i] != null) {
					IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(sockets[i]);
					if (trigger != null) {
						trigger.onDealDamage(stack, target, source, i);
					}
				}
			}
		}
	}
	
	private void doTriggerOnTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		if (sockets != null) {
			for (int i=0; i<sockets.length; i++) {
				if (sockets[i] != null) {
					IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(sockets[i]);
					if (trigger != null) {
						trigger.onTakeDamage(stack, target, source, i);
					}
				}
			}
		}
	}
}
