package ttftcuts.physis.common.artifact.trigger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TriggerOnUpdate extends AbstractTrigger {

	public TriggerOnUpdate(String name) {
		super(name);
	}

	@Override
	public CooldownCategory getCooldownCategory() {
		return CooldownCategory.LONGEST;
	}
	
	@Override
	public void onUpdate(ItemStack stack, EntityLivingBase holder, int id) {
		PhysisAPI.artifactHandler.triggerArtifactEffect(stack, holder, holder, id, getCooldownCategory());
	}
	
	/*@Override
	public String getUnlocalizedTriggerString() {
		return "Every %3$s seconds, %1$s.";
	}*/
	
	@Override
	public String getUnlocalizedTargetString() {
		return TARGET_HOLDER;
	}
}
