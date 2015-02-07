package ttftcuts.physis.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ttftcuts.physis.Physis;

public class ItemJournal extends ItemPhysis {

	public ItemJournal() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("journal");
		this.setTextureName("journal");
		
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.openGui(Physis.instance, 0, world, 0, 0, 0);

		return stack;
	}
}
