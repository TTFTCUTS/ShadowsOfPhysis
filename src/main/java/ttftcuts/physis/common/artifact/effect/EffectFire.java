package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public class EffectFire extends AbstractEffect {

	public EffectFire(String name) {
		super(name);
	}
	
	@Override
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		int seconds = (int)Math.ceil(durations[cooldowntype.ordinal()] / 20);
		target.setFire(seconds);
	}
}
