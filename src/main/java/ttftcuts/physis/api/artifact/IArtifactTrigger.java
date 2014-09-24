package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLiving;

public interface IArtifactTrigger {
	
	public EntityLiving onUpdate(EntityLiving holder, int id);
	
	public EntityLiving onTileUpdate(int x, int y, int z, int id);
}
