package ttftcuts.physis.common.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class ItemPhysisNBTDamage extends ItemPhysis implements INBTDamageItem {

	@Override
	public int getNBTDamage(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			return 0;
		} else {
			if (stack.getTagCompound().hasKey(NBTDAMAGETAG)) {
				return stack.getTagCompound().getInteger(NBTDAMAGETAG);
			}
		}
		return 0;
	}

	@Override
	public int getNBTMaxDamage(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean setNBTDamage(ItemStack stack, int damage) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger(NBTDAMAGETAG, damage);

		return damage > getNBTMaxDamage(stack);
	}
	
	private boolean attemptNBTDamage(ItemStack stack, int damage, Random rand) {
		if (!this.isItemStackNBTDamageable(stack))
        {
            return false;
        }
        else
        {
            if (damage > 0)
            {
                int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                int k = 0;

                for (int l = 0; j > 0 && l < damage; ++l)
                {
                    if (EnchantmentDurability.negateDamage(stack, j, rand))
                    {
                        ++k;
                    }
                }

                damage -= k;

                if (damage <= 0)
                {
                    return false;
                }
            }

            setNBTDamage(stack, getNBTDamage(stack) + damage);

            return getNBTDamage(stack) > getNBTMaxDamage(stack);
        }
	}
	
	private boolean isItemStackNBTDamageable(ItemStack stack) {
		return this.getNBTMaxDamage(stack) < 0 ? false : !stack.hasTagCompound() || !stack.getTagCompound().getBoolean("Unbreakable");
	}
	
	@Override
	public void applyNBTDamage(ItemStack stack, EntityLivingBase entity, int damage) {
		if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.isCreativeMode)
        {
            if (this.isItemStackNBTDamageable(stack))
            {
                if (this.attemptNBTDamage(stack, damage, entity.getRNG()))
                {
                    entity.renderBrokenItemStack(stack);
                    --stack.stackSize;

                    if (entity instanceof EntityPlayer)
                    {
                        EntityPlayer entityplayer = (EntityPlayer)entity;
                        //entityplayer.addStat(StatList.objectBreakStats[Item.getIdFromItem(stack.field_151002_e)], 1);

                        if (stack.stackSize == 0 && stack.getItem() instanceof ItemBow)
                        {
                            entityplayer.destroyCurrentEquippedItem();
                        }
                    }

                    if (stack.stackSize < 0)
                    {
                    	stack.stackSize = 0;
                    }

                    //stack.itemDamage = 0;
                }
            }
        }
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return this.isItemStackNBTDamageable(stack) && this.isDamaged(stack);
    }
	
	@Override
	public boolean isDamaged(ItemStack stack)
    {
        return getNBTDamage(stack) > 0;
    }

	@Override
	public int getDisplayDamage(ItemStack stack)
    {
        return getNBTDamage(stack);
    }
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
        return (double)stack.getItemDamageForDisplay() / (double)getNBTMaxDamage(stack);
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase user)
    {
        this.applyNBTDamage(stack, user, 2);
        return true;
    }

	@Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase user)
    {
        if ((double)block.getBlockHardness(world, x, y, z) != 0.0D)
        {
            this.applyNBTDamage(stack, user, 1);
        }
        
        return true;
    }
	
	@Override
	public boolean isDamageable()
    {
        return false;
    }
	
	@Override
	public boolean isRepairable()
    {
        return canRepair;
    }
}
