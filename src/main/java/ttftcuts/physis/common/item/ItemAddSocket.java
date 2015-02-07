package ttftcuts.physis.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ttftcuts.physis.Physis;

public class ItemAddSocket extends ItemPhysis {

	public ItemAddSocket() {
		super();
		this.setUnlocalizedName("addsocket");
		this.setTextureName("journal");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean par4) {
		List<String> info = Physis.text.translateAndWrap("item.physis:addsocket.tooltip", 150);
		if (info != null) {
			for(String s : info) {
				list.add("\u00A78" + s);
			}
		}
	}
}
