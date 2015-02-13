package ttftcuts.physis.common.compat.baubles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ttftcuts.physis.Physis;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.item.ItemPhysis;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import baubles.api.BaubleType;
import baubles.api.IBauble;

public class ItemMaterialRing extends ItemPhysis implements IBauble {

	private IIcon fallbackicon;
	private List<IIcon> layerIcons;
	
	public ItemMaterialRing() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("baublering");
		this.setTextureName("baubles/ring");
		this.hasSubtypes = true;
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public int getDamage(ItemStack stack)
    {
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		if (mat != null) {
			stack.setItemDamage(mat.id);
			return mat.id;
		}
        return 32767;
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List types) {
		List<ItemStack> rings = new ArrayList<ItemStack>();
		
		for(Entry<String,PhysisToolMaterial> entry : PhysisToolMaterial.materials.entrySet()) {
			PhysisToolMaterial mat = entry.getValue();
			ItemStack stack = new ItemStack(this, 1, mat.id);
			PhysisToolMaterial.writeMaterialToStack(mat, stack);

			PhysisArtifacts.addSocketToItem(stack);
			
			rings.add(stack);
		}
		
		types.addAll(rings);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
    {
        String ring = ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name")).trim();
        
        String regex = StatCollector.translateToLocal("item.physis:material.regex");
        
        if (regex.equals("item.physis:material.regex")) {
        	Physis.logger.warn("Material regex localisation failure, setting to default");
        	regex = "(^\\w*)(?=\\sPick(axe)?)";
        }
        
        Pattern p;
        try {
        	p = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
        	Physis.logger.warn("Material regex compilation failure, falling back", e);
        	p = Pattern.compile("(^\\w*)(?=\\sPick(axe)?)");
        }
        
        PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
        
        if (mat != null)
        {
        	String fullpick = mat.pick.getDisplayName();
        	Matcher m = p.matcher(fullpick);
        	
        	if (m.find()) {
        		String pickmat = m.group(1);
        		
        		return pickmat +" "+ ring;
        	} else if (mat.orematerial != null) {
        		return mat.orematerial +" "+ ring;
        	}
        }

        return ring;
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
			return this.layerIcons.get(pass);
		}
		return fallbackicon;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		if (mat != null && mat.hastint) {
			if (pass == 0) {
				return mat.tints[0];
			}
			else if (pass == 1) {
				return mat.tints[1];
	        }
	        else if (pass == 2) {
	        	return mat.tints[2];
	        }
	        else if (pass == 3) {
	        	return mat.tints[3];
	        }
	        else if (pass == 4) {
	        	return mat.tints[2];
	        }
	        else if (pass == 5) {
	        	return mat.tints[4];
	        }
	        else if (pass == 6) {
	        	return mat.tints[5];
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
     
        fallbackicon = register.registerIcon(Physis.MOD_ID+":baubles/ring");
        
        layerIcons = new ArrayList<IIcon>();
        
        for(int i=0; i<9; i++) {
        	this.layerIcons.add( register.registerIcon(Physis.MOD_ID+":baubles/ring_"+i) );
        }
	}
	
	@Override
	public WeightedRandomChestContent getChestGenBase(ChestGenHooks chest, Random rnd, WeightedRandomChestContent original)
    {				
        ItemStack stack = original.theItemId;
        
        PhysisToolMaterial.writeMaterialToStack(PhysisToolMaterial.getRandomMaterial(rnd), stack);
        
        PhysisArtifacts.addSocketToItem(stack);
        
        if (rnd.nextInt(5) == 0) {
        	PhysisArtifacts.addSocketToItem(stack);
        }
        
    	if (rnd.nextInt(50) == 0) {
        	PhysisArtifacts.addSocketToItem(stack);
        }
        
        original.theItemId = stack;
        
        return original;
    }

}
