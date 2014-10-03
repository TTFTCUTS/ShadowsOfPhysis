package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLivingBase;

public interface IArtifactTrigger {
	
	public void onUpdate(EntityLivingBase holder, int id);
	
	public void onTileUpdate(int x, int y, int z, int id);
}
