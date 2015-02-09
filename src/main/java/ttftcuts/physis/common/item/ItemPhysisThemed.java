package ttftcuts.physis.common.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import ttftcuts.physis.Physis;
import ttftcuts.physis.common.story.StoryEngine;

public class ItemPhysisThemed extends ItemPhysis {

	protected String textureRoot;
	protected Map<String, IIcon[]> variantIcons = new HashMap<String, IIcon[]>();;
	protected String baseIcon;
	
	@Override
	public Item setTextureName(String name)
    {
        super.setTextureName(name);
        this.textureRoot = name;
        return this;
    }

	@SideOnly(Side.CLIENT)
	protected IIcon[] registerIcon(String name, IIconRegister register)
	{
		int variants = StoryEngine.getRange("theme");
		IIcon[] icons = new IIcon[variants];
		
		for (int i=0; i<variants; i++) {
			icons[i] = register.registerIcon(Physis.MOD_ID+":theme"+i+"/"+name);
		}
		
		variantIcons.put(name, icons);
		
		return icons;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
		//variantIcons = new HashMap<String, IIcon[]>();
		
		baseIcon = textureRoot; 
		
		registerIcon(this.textureRoot, register);
    }
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(String name) {
		int theme = StoryEngine.get("theme", true);
		if (theme == -1) {
			return null;
		}
		if (this.variantIcons.containsKey(name)) {
			return this.variantIcons.get(name)[theme];
		}
		return null;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return this.getIcon(baseIcon);
    }
	
	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		int theme = StoryEngine.get("theme", true);
		if (theme == -1) {
			theme = 0;
		}
		
		return super.getUnlocalizedNameInefficiently(par1ItemStack)+".theme."+theme;
	}
}
