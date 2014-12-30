package ttftcuts.physis.common.artifact.trigger;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TriggerOnEquippedUpdate extends AbstractTrigger {

	public TriggerOnEquippedUpdate(String name) {
		super(name);
	}

	@Override
	public CooldownCategory getCooldownCategory() {
		return CooldownCategory.LONG;
	}
	
	@Override
	public void onEquippedUpdate(ItemStack stack, EntityLivingBase holder, int id) {
		PhysisAPI.artifactHandler.triggerArtifactEffect(stack, holder, holder, id, getCooldownCategory());
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
