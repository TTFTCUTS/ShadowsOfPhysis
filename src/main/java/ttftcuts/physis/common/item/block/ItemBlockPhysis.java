package ttftcuts.physis.common.item.block;

import ttftcuts.physis.Physis;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPhysis extends ItemBlock {
	public ItemBlockPhysis(Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("tile.", "tile." + Physis.MOD_ID +":");
	}
}
