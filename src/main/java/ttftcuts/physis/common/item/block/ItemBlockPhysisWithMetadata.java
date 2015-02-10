package ttftcuts.physis.common.item.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ttftcuts.physis.Physis;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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
	
	@SuppressWarnings("rawtypes")
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean bool) {
		super.addInformation(stack, player, tooltip, bool);
		
		if(stack.getItem() instanceof ItemBlock) {
			ItemBlock ib = (ItemBlock)stack.getItem();
			Block block = ib.field_150939_a;
			
			if (block instanceof IBlockTooltip) {
				((IBlockTooltip)block).AddTooltipInformation(stack, player, tooltip, bool);
			}
		}
	}
}
