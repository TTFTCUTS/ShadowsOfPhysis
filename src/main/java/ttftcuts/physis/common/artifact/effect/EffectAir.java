package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public class EffectAir extends AbstractEffect {

	public int amount;
	
	public EffectAir(String name, int amount) {
		super(name);
		this.amount = amount;
	}
	
	@Override
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		int air = target.getAir();
		target.setAir(air + amount);
	}
}
