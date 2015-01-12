package ttftcuts.physis.common.artifact.trigger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TriggerOnUpdate extends AbstractTrigger {

	public UpdateCondition condition;
	
	public TriggerOnUpdate(String name, UpdateCondition condition) {
		super(name + condition.suffix);
		this.condition = condition;
	}
	
	public TriggerOnUpdate(String name) {
		this(name, UpdateCondition.ANY);
	}

	@Override
	public CooldownCategory getCooldownCategory() {
		//return CooldownCategory.LONGEST;
		return condition.updateCooldown;
	}
	
	@Override
	public void onUpdate(ItemStack stack, EntityLivingBase holder, int id) {
		if (this.condition.condition(stack, holder)) {
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, holder, holder, id, getCooldownCategory());
		}
	}
	
	@Override
	public String getUnlocalizedTargetString() {
		return TARGET_HOLDER;
	}
}
