package ttftcuts.physis.common.artifact.trigger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TriggerOnEquippedUpdate extends AbstractTrigger {

	public UpdateCondition condition;
	
	public TriggerOnEquippedUpdate(String name, UpdateCondition condition) {
		super(name + condition.suffix);
		this.condition = condition;
	}
	
	public TriggerOnEquippedUpdate(String name) {
		this(name, UpdateCondition.ANY);
	}

	@Override
	public CooldownCategory getCooldownCategory() {
		//return CooldownCategory.LONG;
		return this.condition.equippedCooldown;
	}
	
	@Override
	public void onEquippedUpdate(ItemStack stack, EntityLivingBase holder, int id) {
		if (this.condition.condition(stack, holder)) {
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, holder, holder, id, getCooldownCategory());
		}
	}
	
	/*@Override
	public String getUnlocalizedTriggerString() {
		return "Every %3$s seconds while equipped or held, %1$s.";
	}*/
	
	@Override
	public String getUnlocalizedTargetString() {
		return TARGET_HOLDER;
	}
}
