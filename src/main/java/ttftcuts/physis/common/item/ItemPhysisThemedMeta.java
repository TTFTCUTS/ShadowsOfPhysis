package ttftcuts.physis.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemPhysisThemedMeta extends ItemPhysisThemed {
	
	public final String name;
	public final int subtypes;
	
	public ItemPhysisThemedMeta(String name, int subtypes) {
		super();
		this.name = name;
		this.subtypes = subtypes;
		this.setHasSubtypes(true);
		this.setUnlocalizedName(name);
		this.setTextureName(name+"0");
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
		for (int i=0; i<subtypes; i++) {
			this.registerIcon(name+i, register);
		}
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return this.getIcon(name + (damage < subtypes ? damage : 0));
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List types) {
		for(int i = 0; i < subtypes; i++)
			types.add(new ItemStack(item, 1, i));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}
}
