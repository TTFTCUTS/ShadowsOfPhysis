package ttftcuts.physis.common.item.block;

import ttftcuts.physis.Physis;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockPhysisWithMetadata extends ItemBlockWithMetadata {
	public ItemBlockPhysisWithMetadata(Block block) {
		super(block, block);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("tile.", "tile." + Physis.MOD_ID +":");
	}
}
