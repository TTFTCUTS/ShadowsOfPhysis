package ttftcuts.physis.common.artifact.trigger;

import java.util.ArrayList;
import java.util.List;

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
			List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
			targets.add(target);
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, targets, target, id, getCooldownCategory());
		} else {
			// effect on attacker when hit
			if (source != null) {
				List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
				targets.add(source);
				PhysisAPI.artifactHandler.triggerArtifactEffect(stack, targets, target, id, getCooldownCategory());
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
