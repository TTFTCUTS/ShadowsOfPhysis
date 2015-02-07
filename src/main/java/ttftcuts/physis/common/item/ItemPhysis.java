package ttftcuts.physis.common.item;

import ttftcuts.physis.Physis;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPhysis extends Item {

	public ItemPhysis() {
		super();
		this.setCreativeTab(Physis.creativeTab);
	}
	
	@Override
	public Item setTextureName(String name)
    {
        this.iconString = Physis.MOD_ID+":"+name;
        return this;
    }
	
	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + Physis.MOD_ID +":");
	}
}
