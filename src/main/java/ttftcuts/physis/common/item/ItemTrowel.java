package ttftcuts.physis.common.item;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.Physis;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemTrowel extends ItemPhysis {
	
	public static Map<PhysisToolMaterial, ResourceLocation> trowelHeads;
	
	public ItemTrowel() {
		super();
		this.maxStackSize = 1;
		this.setUnlocalizedName("trowel");
		this.setTextureName(Physis.MOD_ID+":trowel");
		this.setMaxDamage(5);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List types) {
		Iterator<Entry<String,PhysisToolMaterial>> iter = PhysisToolMaterial.materials.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, PhysisToolMaterial> entry = iter.next();
			PhysisToolMaterial mat = entry.getValue();
			ItemStack stack = new ItemStack(this, 1, 0);
			PhysisToolMaterial.writeMaterialToStack(mat, stack);

			types.add(stack);
		}
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		if (stack.getItem() == this) {
			PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
			if (mat != null) {
				return mat.maxdamage;
			}
		}
		return this.getMaxDamage();
	}
	
	/*@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getItem() == this) {
			PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
			if (mat != null) {
				return this.getUnlocalizedName() + mat.getMaterialName();
			}
		}
		return this.getUnlocalizedName();
	}*/
	
	public String getItemStackDisplayName(ItemStack stack)
    {
        String trowel = ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name")).trim();
        
        String regex = StatCollector.translateToLocal("item.physis:trowel.regex");
        
        if (regex.equals("item.physis:trowel.regex")) {
        	Physis.logger.warn("Trowel regex localisation failure, setting to default");
        	regex = "(^\\w*)(?=\\sPickaxe)";
        }
        
        Pattern p;
        try {
        	p = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
        	Physis.logger.warn("Trowel regex compilation failure, falling back", e);
        	p = Pattern.compile("(^\\w*)(?=\\sPickaxe)");
        }
        
        PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
        
        if (mat != null)
        {
        	String fullpick = mat.pick.getDisplayName();
        	Matcher m = p.matcher(fullpick);
        	
        	if (m.find()) {
        		String pickmat = m.group(1);
        		
        		return pickmat +" "+ trowel;
        	}
        }

        return trowel;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        return super.getIconFromDamageForRenderPass(damage, pass);
        
        
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        super.registerIcons(register);
        
        //register.
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		stack.damageItem(1, player);
		return stack;
	}
}
