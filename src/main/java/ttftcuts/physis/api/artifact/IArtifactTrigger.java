package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IArtifactTrigger {
	
	public void onUpdate(ItemStack stack, EntityLivingBase holder, int id);
	
	public void onEquippedUpdate(ItemStack stack, EntityLivingBase holder, int id);
	
	public void onTileUpdate(int x, int y, int z, int id);
	
	public void onDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id);
	
	public void onTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id);
	
	public String getName();
}
