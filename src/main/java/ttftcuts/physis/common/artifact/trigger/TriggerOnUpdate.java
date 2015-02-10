package ttftcuts.physis.common.artifact.trigger;

import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

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
			List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
			targets.add(holder);
			PhysisAPI.artifactHandler.triggerArtifactEffect(stack, targets, holder, id, getCooldownCategory());
		}
	}
	
	@Override
	public void onTileUpdate(ItemStack stack, List<EntityLivingBase> targets, TileEntity tile, int id) {
		List<EntityLivingBase> shortlist = new ArrayList<EntityLivingBase>();
		for (EntityLivingBase entity : targets) {
			if (this.condition.condition(stack, entity)) {
				shortlist.add(entity);
			}
		}
		PhysisAPI.artifactHandler.triggerArtifactEffect(stack, targets, null, id, getCooldownCategory());
	}
	
	@Override
	public String getUnlocalizedTargetString() {
		return TARGET_HOLDER;
	}
}
