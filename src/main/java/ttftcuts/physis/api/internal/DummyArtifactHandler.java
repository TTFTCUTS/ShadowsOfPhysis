package ttftcuts.physis.api.internal;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;

public class DummyArtifactHandler implements IArtifactHandler {
	
	@Override
	public boolean registerArtifactTrigger(String name, IArtifactTrigger trigger, int weight) {
		return false;
	}

	@Override
	public boolean registerArtifactEffect(String name, IArtifactEffect effect, int weight) {
		return false;
	}
	
	@Override
	public boolean triggerArtifactEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldown) {
		return false;
	}

	@Override
	public boolean triggerInWorldArtifactEffect(int x, int y, int z, EntityLivingBase target, int id, CooldownCategory cooldown) {
		return false;
	}



}
