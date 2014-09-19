package ttftcuts.physis.common.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemTrowel extends ItemPhysis {

	public IIcon handle;
	public IIcon shaft;
	public IIcon fallbackicon;
	
	public List<IIcon> headIcons;;
	
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
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
	@Override
	public int getRenderPasses(int metadata) {
		return 9;
	}
	
	@Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		
		if (mat != null && mat.hastint) {
			if (pass == 0) {
				//return headIcons.get(mat);
				return handle;
			}
			else if (pass == 1) {
				return shaft;
			}
			else {
				int i = pass-2;
				i = Math.min(this.headIcons.size()-1, i);
				return this.headIcons.get(i);
			}
		}
		return fallbackicon;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		if (mat != null && mat.hastint) {
	        if (pass == 1) {
	        	return mat.shafttint;
	        }
	        else if (pass == 2) {
	        	return mat.tints[0];
	        }
	        else if (pass == 3) {
	        	return mat.tints[1];
	        }
	        else if (pass == 4) {
	        	return mat.tints[3];
	        }
	        else if (pass == 5) {
	        	return mat.tints[4];
	        }
	        else if (pass == 6) {
	        	return mat.tints[6];
	        }
	        else if (pass == 7) {
	        	return mat.tints[7];
	        } 
	        else if (pass == 8) {
	        	return mat.tints[9];
	        }
		}
		return 0xFFFFFF;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        super.registerIcons(register);
     
        fallbackicon = register.registerIcon(Physis.MOD_ID+":trowel");
        handle = register.registerIcon(Physis.MOD_ID+":trowel_handle");
        shaft = register.registerIcon(Physis.MOD_ID+":trowel_shaft");
        
        headIcons = new ArrayList<IIcon>();
        
        for(int i=0; i<7; i++) {
        	this.headIcons.add( register.registerIcon(Physis.MOD_ID+":trowel_head_"+i) );
        }
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		stack.damageItem(1, player);
		return stack;
	}
}
