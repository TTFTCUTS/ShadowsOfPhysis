package ttftcuts.physis.common.artifact.trigger;

import java.util.ArrayList;
import java.util.List;

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
	public CooldownCategory getCooldownCategory() {
		if (self) {
			return CooldownCategory.MEDIUM;
		} else {
			return CooldownCategory.SHORT;
		}
	}
	
	@Override
	public void onDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {
		if (self) {
			// effect on self when damage enemy
			List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
			targets.add(source);
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, targets, source, id, getCooldownCategory());
		} else {
			// effect on target when attacking
			List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
			targets.add(target);
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, targets, source, id, getCooldownCategory());
		}
	}
	
	@Override
	public String getUnlocalizedTargetString() {
		if (self) {
			return TARGET_HOLDER;
		} else {
			return TARGET_TARGET;
		}
	}
}
