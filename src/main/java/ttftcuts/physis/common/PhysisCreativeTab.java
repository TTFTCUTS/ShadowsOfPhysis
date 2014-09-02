package ttftcuts.physis.common;

import ttftcuts.physis.Physis;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PhysisCreativeTab extends CreativeTabs {
	
	public PhysisCreativeTab() {
		super(Physis.MOD_ID);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return PhysisItems.journal;
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(PhysisItems.journal);
	}
}
