package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IArtifactTrigger {
	
	public void onUpdate(ItemStack stack, EntityLivingBase holder, int id);
	
	public void onTileUpdate(int x, int y, int z, int id);
	
	public String getName();
}
