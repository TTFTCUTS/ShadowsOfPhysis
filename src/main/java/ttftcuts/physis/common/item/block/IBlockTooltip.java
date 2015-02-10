package ttftcuts.physis.common.item.block;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IBlockTooltip {
	@SuppressWarnings("rawtypes")
	public void AddTooltipInformation(ItemStack stack, EntityPlayer player, List list, boolean par4);
}
