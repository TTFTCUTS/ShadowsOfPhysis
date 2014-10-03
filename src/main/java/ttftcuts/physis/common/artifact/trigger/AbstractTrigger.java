package ttftcuts.physis.common.artifact.trigger;

import net.minecraft.entity.EntityLivingBase;
import ttftcuts.physis.api.artifact.IArtifactTrigger;

public abstract class AbstractTrigger implements IArtifactTrigger {

	@Override
	public void onUpdate(EntityLivingBase holder, int id) {}

	@Override
	public void onTileUpdate(int x, int y, int z, int id) {}

}
