package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public class EffectExplosion extends AbstractEffect {

	private int power;
	
	public EffectExplosion(String name, int power) {
		super(name);
	}
	
	@Override
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		target.worldObj.createExplosion(source, target.posX, target.posY, target.posZ, power, false);
	}

}
