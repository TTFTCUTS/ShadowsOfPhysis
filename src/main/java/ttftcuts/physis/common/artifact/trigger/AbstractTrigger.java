package ttftcuts.physis.common.artifact.trigger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactTrigger;

public abstract class AbstractTrigger implements IArtifactTrigger {

	protected String name;
	
	public AbstractTrigger(String name) {
		this.name = Physis.MOD_ID +"_"+ name;
	}
	
	@Override
	public void onUpdate(ItemStack stack, EntityLivingBase holder, int id) {}
	
	@Override
	public void onEquippedUpdate(ItemStack stack, EntityLivingBase holder, int id) {}

	@Override
	public void onTileUpdate(int x, int y, int z, int id) {}
	
	@Override
	public void onDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {}
	
	@Override
	public void onTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {}

	@Override
	public String getName() {
		return this.name;
	}
}
