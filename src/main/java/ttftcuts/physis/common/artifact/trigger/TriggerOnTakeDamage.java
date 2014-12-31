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
	public CooldownCategory getCooldownCategory() {
		if (self) {
			return CooldownCategory.MEDIUM;
		} else {
			return CooldownCategory.SHORTER;
		}
	}
	
	@Override
	public void onTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {
		if (self) {
			// effect on self when hit
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, target, target, id, getCooldownCategory());
		} else {
			// effect on attacker when hit
			if (source != null) {
				PhysisAPI.artifactHandler.triggerArtifactEffect(stack, source, target, id, getCooldownCategory());
			}
		}
	}
	
	@Override
	public String getUnlocalizedTargetString() {
		if (self) {
			return TARGET_HOLDER;
		} else {
			return TARGET_ATTACKER;
		}
	}
}
