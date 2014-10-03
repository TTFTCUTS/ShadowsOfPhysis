package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLivingBase;
import ttftcuts.physis.api.internal.IArtifactHandler;

public interface IArtifactEffect {

	public void doEffect(EntityLivingBase target, EntityLivingBase source, IArtifactHandler.CooldownCategory cooldowntype);
}
