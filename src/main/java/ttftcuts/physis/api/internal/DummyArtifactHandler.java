package ttftcuts.physis.api.internal;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;

public class DummyArtifactHandler implements IArtifactHandler {
	
	@Override
	public boolean registerArtifactTrigger(IArtifactTrigger trigger, int weight) {
		return false;
	}

	@Override
	public boolean registerArtifactEffect(IArtifactEffect effect, int weight) {
		return false;
	}
	
	@Override
	public boolean triggerArtifactEffect(ItemStack stack, List<EntityLivingBase> target, EntityLivingBase source, int id, CooldownCategory cooldown) {
		return false;
	}

	@Override
	public void triggerEffectCooldown(ItemStack stack, int ticks, int id) {}

}
