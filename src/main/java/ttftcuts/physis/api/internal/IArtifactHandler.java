package ttftcuts.physis.api.internal;

import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IArtifactHandler {

	public boolean registerArtifactTrigger(IArtifactTrigger trigger, int weight);
	
	public boolean registerArtifactEffect(IArtifactEffect effect, int weight);
	
	public boolean triggerArtifactEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldown);
	
	public boolean triggerInWorldArtifactEffect(int x, int y, int z, EntityLivingBase target, int id, CooldownCategory cooldown);
	
	public enum CooldownCategory {
		LONG,
		SHORT,
		NONE
	}
}
