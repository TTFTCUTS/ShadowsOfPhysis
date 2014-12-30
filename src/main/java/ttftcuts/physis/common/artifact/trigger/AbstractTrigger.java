package ttftcuts.physis.common.artifact.trigger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.artifact.PhysisArtifacts;

public abstract class AbstractTrigger implements IArtifactTrigger {

	protected String name;
	
	public double hue = 0.0;
	public double saturation = 0.0;
	
	public AbstractTrigger(String name) {
		this.name = Physis.MOD_ID +"_"+ name;
		this.hue = PhysisArtifacts.colourRand.nextDouble();
		this.saturation = 1.0 - (PhysisArtifacts.colourRand.nextDouble() * PhysisArtifacts.colourRand.nextDouble());
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
	
	@Override
	public String getUnlocalizedTriggerString() {
		return "When something happens, %e.\n%c second cooldown.";
	}
	
	@Override
	public String getUnlocalizedTargetString() {
		return "something";
	}
	
	@Override
	public double getHue() {
		return this.hue;
	}
	
	@Override
	public double getSaturation() {
		return this.saturation;
	}
	
	@Override
	public CooldownCategory getCooldownCategory() {
		return CooldownCategory.NONE;
	}
}
