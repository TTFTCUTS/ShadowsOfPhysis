package ttftcuts.physis.common.artifact.trigger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public enum UpdateCondition {
	ANY(CooldownCategory.LONGEST, CooldownCategory.LONG, 1.0),
	FIRE(CooldownCategory.MEDIUM, CooldownCategory.SHORT, 0.25) {
		@Override
		public boolean condition(ItemStack stack, EntityLivingBase entity) {
			return entity.isBurning();
		}
	},
	SUN(CooldownCategory.LONG, CooldownCategory.MEDIUM, 0.25) {
		@Override
		public boolean condition(ItemStack stack, EntityLivingBase entity) {
			// same logic as zombie burning
			if (entity.worldObj.isDaytime() && !entity.worldObj.isRemote)
	        {
	            float light = entity.getBrightness(1.0F);

	            if (light > 0.5F && entity.worldObj.canBlockSeeTheSky(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ)))
	            {
	            	return true;
	            }
	        }
			return false;
		}
	},
	DARK(CooldownCategory.LONG, CooldownCategory.MEDIUM, 0.25) {
		@Override
		public boolean condition(ItemStack stack, EntityLivingBase entity) {
            float light = entity.getBrightness(1.0F);

            if (light < 0.25F)
            {
            	return true;
            }
			return false;
		}
	},
	RAIN(CooldownCategory.LONG, CooldownCategory.MEDIUM, 0.1) {
		@Override
		public boolean condition(ItemStack stack, EntityLivingBase entity) {
			if (entity.worldObj.isRaining() && !entity.worldObj.isRemote)
	        {
	            if (entity.worldObj.canBlockSeeTheSky(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ)))
	            {
	            	return true;
	            }
	        }
			return false;
		}
	},
	SNEAK(CooldownCategory.LONGEST, CooldownCategory.LONG, 0.2) {
		@Override
		public boolean condition(ItemStack stack, EntityLivingBase entity) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)entity;
				return player.isSneaking();
			}
			return false;
		}
	},
	SPRINT(CooldownCategory.LONG, CooldownCategory.MEDIUM, 0.2) {
		@Override
		public boolean condition(ItemStack stack, EntityLivingBase entity) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)entity;
				return player.isSprinting();
			}
			return false;
		}
	},
	WATER(CooldownCategory.LONG, CooldownCategory.MEDIUM, 0.1) {
		@Override
		public boolean condition(ItemStack stack, EntityLivingBase entity) {
            return entity.isInWater();
		}
	},
	;
	
	//------------------------------------------------
	
	public String suffix;
	public CooldownCategory updateCooldown;
	public CooldownCategory equippedCooldown;
	public double relativeRarity;
	
	private UpdateCondition(CooldownCategory updateCooldown, CooldownCategory equippedCooldown, double rarity) {
		this.suffix = this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
		this.updateCooldown = updateCooldown;
		this.equippedCooldown = equippedCooldown;
		this.relativeRarity = rarity;
	}
	
	private UpdateCondition(CooldownCategory updateCooldown, CooldownCategory equippedCooldown) {
		this(updateCooldown, equippedCooldown, 1.0);
	}
	
	private UpdateCondition(CooldownCategory cooldown) {
		this(cooldown, cooldown, 1.0);
	}
	
	public boolean condition(ItemStack stack, EntityLivingBase entity) {
		return true;
	}
}
