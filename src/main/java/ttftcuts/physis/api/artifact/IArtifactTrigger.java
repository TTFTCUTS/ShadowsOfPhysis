package ttftcuts.physis.api.artifact;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IArtifactTrigger {
	
	public void onUpdate(ItemStack stack, EntityLivingBase holder, int id);
	
	public void onEquippedUpdate(ItemStack stack, EntityLivingBase holder, int id);
	
	public void onTileUpdate(int x, int y, int z, int id);
	
	public void onDealDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id);
	
	public void onTakeDamage(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id);
	
	public String getName();
	public String getLocalizationName();
	
	public CooldownCategory getCooldownCategory();
	
	public String getUnlocalizedTriggerString();
	public String getUnlocalizedTargetString();
	public String getTooltipInfo();
	
	@SideOnly(Side.CLIENT)
	public IIcon registerIcon(IIconRegister register);
	public double getHue();
	public double getSaturation();
}
