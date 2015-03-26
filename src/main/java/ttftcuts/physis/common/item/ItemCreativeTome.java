package ttftcuts.physis.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ttftcuts.physis.common.story.Knowledge;

public class ItemCreativeTome extends ItemPhysis {

	public ItemCreativeTome() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("knowledgetome");
		this.setTextureName("journal");
		this.setShowTooltip(true);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			if (!player.isSneaking()) {
				Knowledge.giveAll(player);
			} else {
				Knowledge.removeAll(player);
			}
		}

		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass)
	{
		 return true;
	}
}
