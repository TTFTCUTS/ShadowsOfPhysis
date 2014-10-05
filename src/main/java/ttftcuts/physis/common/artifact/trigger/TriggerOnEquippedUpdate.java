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
	public void onEquippedUpdate(ItemStack stack, EntityLivingBase holder, int id) {
		PhysisAPI.artifactHandler.triggerArtifactEffect(stack, holder, holder, id, CooldownCategory.LONG);
	}
}
