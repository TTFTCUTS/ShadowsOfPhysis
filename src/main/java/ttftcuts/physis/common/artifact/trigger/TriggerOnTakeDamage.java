package ttftcuts.physis.common.artifact.trigger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TriggerOnTakeDamage extends AbstractTrigger {

	private boolean self;
	
	public TriggerOnTakeDamage(String name, boolean self) {
		super(name);
		this.self = self;
	}

	@Override
	public void onTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {
		if (self) {
			// effect on self when hit
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, target, target, id, CooldownCategory.MEDIUM);
		} else {
			// effect on attacker when hit
			if (source != null) {
				PhysisAPI.artifactHandler.triggerArtifactEffect(stack, source, target, id, CooldownCategory.SHORTER);
			}
		}
	}
}
