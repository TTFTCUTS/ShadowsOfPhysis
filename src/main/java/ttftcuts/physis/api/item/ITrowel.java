package ttftcuts.physis.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ITrowel {

	public int getTrowelLevel(ItemStack stack);
	
	public void onUseTrowel(ItemStack stack, EntityLivingBase user, boolean success);
	
}