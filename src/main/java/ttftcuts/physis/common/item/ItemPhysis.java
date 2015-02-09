package ttftcuts.physis.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ttftcuts.physis.Physis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPhysis extends Item {

	private boolean showTooltip = false;
	
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (showTooltip) {
			List<String> info = Physis.text.translateAndWrap("item.physis:" + this.getUnlocalizedName(stack).replace("item.", "") + ".tooltip", 150);
			if (info != null) {
				for(String s : info) {
					list.add(s);
				}
			}
		}
	}
	
	public ItemPhysis setShowTooltip(boolean show) {
		this.showTooltip = show;
		return this;
	}
}
