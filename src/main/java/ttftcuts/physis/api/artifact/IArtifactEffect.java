package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ttftcuts.physis.api.internal.IArtifactHandler;

public interface IArtifactEffect {

	public void doEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, IArtifactHandler.CooldownCategory cooldowntype);
	
	public String getName();
}
