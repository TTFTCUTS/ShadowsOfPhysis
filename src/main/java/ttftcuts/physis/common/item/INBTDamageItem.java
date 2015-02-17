package ttftcuts.physis.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface INBTDamageItem {
	public static final String NBTDAMAGETAG = "FakeDamage";
	
	public int getNBTDamage(ItemStack stack);
	public int getNBTMaxDamage(ItemStack stack);
	public boolean setNBTDamage(ItemStack stack, int damage);
	public void applyNBTDamage(ItemStack stack, EntityLivingBase entity, int damage);
}
