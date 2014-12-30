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
		double x = (target.boundingBox.minX + target.boundingBox.maxX)*0.5;
		double y = (target.boundingBox.minY + target.boundingBox.maxY)*0.5;
		double z = (target.boundingBox.minZ + target.boundingBox.maxZ)*0.5;
		target.worldObj.createExplosion(source, x, y, z, power, false);
	}

	/*@Override
	public String getUnlocalizedEffectString() {
		return "causes a small explosion at %2$s";
	}*/
}
