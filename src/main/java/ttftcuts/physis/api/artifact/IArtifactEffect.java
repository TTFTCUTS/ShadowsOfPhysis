package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ttftcuts.physis.api.internal.IArtifactHandler;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public interface IArtifactEffect {

	public void doEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, IArtifactHandler.CooldownCategory cooldowntype);
	
	public int getCooldown(CooldownCategory cd);
	public int getDuration(CooldownCategory cd);
	
	public String getName();
	public String getLocalizationName();
	
	public String getUnlocalizedEffectString();
	public String getTooltipInfo();
	
	public double getHue();
	public double getSaturation();
}
