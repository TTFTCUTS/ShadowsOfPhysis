package ttftcuts.physis.common.item;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
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
import ttftcuts.physis.client.texture.ItemMappedTexture;
import ttftcuts.physis.client.texture.PhysisAtlasSprite;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemTrowel extends ItemPhysis {
	
	public static Map<PhysisToolMaterial, IIcon> headIcons;
	
	private static IIconRegister savedregister;
	
	public IIcon handle;
	public IIcon shaft;
	public IIcon fallbackicon;
	
	public IIcon tempheadicon;
	
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
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
	@Override
	public int getRenderPasses(int metadata) {
		return 3;
	}
	
	@Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		
		if (mat != null) {
			if (pass == 0) {
				//return headIcons.get(mat);
				return tempheadicon;
			}
			else if (pass == 1) {
				return shaft;
			}
			else {
				return handle;
			}
		}
		return fallbackicon;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        if (pass == 1) {
        	PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
        	
        	if (mat != null) {
        		return 0x896727;
        	}
        }
		return 0xFFFFFF;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
		savedregister = register;
        super.registerIcons(register);
     
        fallbackicon = register.registerIcon("physis:trowel");
        handle = register.registerIcon("physis:trowel_handle");
        shaft = register.registerIcon("physis:trowel_shaft");
        
        tempheadicon = register.registerIcon("physis:trowel_head");

		if (register instanceof TextureMap) {
        	TextureMap map = (TextureMap)register;
        	
        	
        	headIcons = new HashMap<PhysisToolMaterial, IIcon>();
        	
        	/*if (PhysisToolMaterial.generated) {
	        	ItemStack teststack = new ItemStack(Items.apple);
				BufferedImage test = TextureHelper.getItemStackImage(teststack);
				Physis.logger.info("image");
				Physis.logger.info(Integer.toHexString(test.getRGB(0, 0)));
				Physis.logger.info(Integer.toHexString(test.getRGB(5, 5)));
				Physis.logger.info(Integer.toHexString(test.getRGB(8, 8)));
				
				for(Entry<String, PhysisToolMaterial> entry : PhysisToolMaterial.materials.entrySet()) {
					PhysisToolMaterial mat = entry.getValue();
					
					String name = "testrender"+mat.orematerial;
					
					ItemMappedTexture t = new ItemMappedTexture(test, null);
					
					Physis.logger.info(t);
					
					ResourceLocation loc = TextureHelper.loadTexture(name, t);
					
					Physis.logger.info(loc);
					
					IIcon icon = TextureHelper.getIconForDynamicResource(map, name, loc);
					
					Physis.logger.info(icon);
					
					headIcons.put(mat, icon);
				}
        	}*/
        }
	}
	
	@SideOnly(Side.CLIENT)
	public static void buildDynamicTextures() {
		if (savedregister instanceof TextureMap) {
			TextureMap map = (TextureMap)(savedregister);
			
			ItemStack teststack = new ItemStack(Items.apple);
			BufferedImage test = TextureHelper.getItemStackImage(teststack);

			for(Entry<String, PhysisToolMaterial> entry : PhysisToolMaterial.materials.entrySet()) {
				PhysisToolMaterial mat = entry.getValue();
				
				String name = "testrender"+mat.orematerial;
				
				ItemMappedTexture t = new ItemMappedTexture(test, null);
				
				Physis.logger.info(t);
				
				ResourceLocation loc = TextureHelper.loadTexture(name, t);
				
				Physis.logger.info(loc);
				
				IIcon icon = TextureHelper.getIconForDynamicResource(map, name, loc);
				
				Physis.logger.info(icon);
				
				headIcons.put(mat, icon);
			}
			
			Minecraft.getMinecraft().refreshResources();
			/*try {
				map.loadTexture(Minecraft.getMinecraft().getResourceManager());
			} catch (IOException e) {
				Physis.logger.info("################### SOME SHIT HAPPENED");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//Physis.logger.info(lasticon);
			
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		stack.damageItem(1, player);
		return stack;
	}
}
