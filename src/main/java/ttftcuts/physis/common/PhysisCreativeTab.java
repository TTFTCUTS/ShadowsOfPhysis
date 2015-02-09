package ttftcuts.physis.common;

import ttftcuts.physis.Physis;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PhysisCreativeTab extends CreativeTabs {
	
	private ItemStack display;
	
	public PhysisCreativeTab(String name) {
		super(Physis.MOD_ID+"_"+name);
	}
	
	public void setDisplayStack(ItemStack stack) {
		this.display = stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return display.getItem();
	}

	@Override
	public ItemStack getIconItemStack() {
		return display;
	}
}
