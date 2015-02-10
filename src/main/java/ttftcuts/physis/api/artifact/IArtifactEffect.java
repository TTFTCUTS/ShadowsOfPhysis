package ttftcuts.physis.api.artifact;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import ttftcuts.physis.api.internal.IArtifactHandler;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public interface IArtifactEffect {

	public void doEffect(ItemStack stack, List<EntityLivingBase> target, EntityLivingBase source, int id, IArtifactHandler.CooldownCategory cooldowntype);
	
	public int getCooldown(CooldownCategory cd);
	public int getDuration(CooldownCategory cd);
	
	public String getName();
	public String getLocalizationName();
	
	public String getUnlocalizedEffectString();
	public String getTooltipInfo();
	
	@SideOnly(Side.CLIENT)
	public IIcon registerIcon(IIconRegister register);
	public double getHue();
	public double getSaturation();
}
