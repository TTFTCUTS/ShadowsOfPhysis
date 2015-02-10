package ttftcuts.physis.common.artifact.trigger;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.artifact.PhysisArtifacts;

public abstract class AbstractTrigger implements IArtifactTrigger {

	public static final String TARGET_HOLDER = PhysisArtifacts.PREFIX + "target.holder";
	public static final String TARGET_TARGET = PhysisArtifacts.PREFIX + "target.target";
	public static final String TARGET_ATTACKER = PhysisArtifacts.PREFIX + "target.attacker";
	
	protected String name;
	protected String image;
	
	public double hue = 0.0;
	public double saturation = 0.0;
	
	public AbstractTrigger(String name) {
		this.name = PhysisArtifacts.PREFIX + name;
		this.image = "trigger"+name;
		this.hue = PhysisArtifacts.triggerColourRand.nextDouble();
		this.saturation = 1.0 - (PhysisArtifacts.triggerColourRand.nextDouble() * PhysisArtifacts.triggerColourRand.nextDouble());
	}
	
	@Override
	public void onUpdate(ItemStack stack, EntityLivingBase holder, int id) {}
	
	@Override
	public void onEquippedUpdate(ItemStack stack, EntityLivingBase holder, int id) {}

	@Override
	public void onTileUpdate(ItemStack stack, List<EntityLivingBase> targets, TileEntity tile, int id) {}
	
	@Override
	public void onDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {}
	
	@Override
	public void onTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id) {}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getLocalizationName() {
		return this.name;
	}
	
	@Override
	public String getUnlocalizedTriggerString() {
		return this.getLocalizationName() + ".trigger";
		//return "When something happens, %1$s.\n%4$s second cooldown.";
	}
	
	@Override
	public String getUnlocalizedTargetString() {
		return TARGET_TARGET;
		//return "something";
	}
	
	@Override
	public String getTooltipInfo() {
		return "<No extra info>";
	}
	
	@Override
	public IIcon registerIcon(IIconRegister register) {
		return register.registerIcon(Physis.MOD_ID+":trigger_effect/"+this.image);
	}
	
	public AbstractTrigger setColour(double hue, double saturation) {
		this.hue = hue;
		this.saturation = saturation;
		return this;
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
