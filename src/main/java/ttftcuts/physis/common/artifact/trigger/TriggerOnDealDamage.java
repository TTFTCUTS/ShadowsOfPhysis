package ttftcuts.physis.common.artifact.trigger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TriggerOnDealDamage extends AbstractTrigger {

	private boolean self;
	
	public TriggerOnDealDamage(String name, boolean self) {
		super(name);
		this.self = self;
	}

	@Override
	public void onDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {
		if (self) {
			// effect on self when damage enemy
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, source, source, id, CooldownCategory.MEDIUM);
		} else {
			// effect on target when attacking
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, target, source, id, CooldownCategory.SHORT);
		}
	}
}
