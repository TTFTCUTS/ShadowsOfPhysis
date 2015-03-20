package ttftcuts.physis.common.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.item.ITrowel;
import ttftcuts.physis.client.texture.ToneSplitTexture;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.handler.TooltipHandler;
import ttftcuts.physis.common.helper.TextureHelper;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class ItemTrowel extends ItemPhysisNBTDamage implements ITrowel {

	public static final String HANDLETAG = "physisTrowelHandle" ;
	public static final String CREATIVETAG = "physisTrowelCreative";
	
	public IIcon handle;
	public IIcon shaft;
	public IIcon fallbackicon;
	
	public List<IIcon> headIcons;;
	
	public ItemTrowel() {
		super();
		this.maxStackSize = 1;
		this.setUnlocalizedName("trowel");
		this.setTextureName("trowel");
		this.setMaxDamage(-1);
		this.setHasSubtypes(true);
		this.setNoRepair();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List types) {
		List<ItemStack> trowels = new ArrayList<ItemStack>();
		
		for(Entry<String,PhysisToolMaterial> entry : PhysisToolMaterial.materials.entrySet()) {
			PhysisToolMaterial mat = entry.getValue();
			ItemStack stack = new ItemStack(this, 1, mat.id);
			PhysisToolMaterial.writeMaterialToStack(mat, stack);

			trowels.add(stack);
		}
		
		Collections.sort(trowels, new Comparator<ItemStack>() {
			@Override
			public int compare(ItemStack s1, ItemStack s2) {
				int t1 = getTrowelLevel(s1);
				int d1 = s1.getMaxDamage();
				int e1 = PhysisToolMaterial.getMaterialFromItemStack(s1).toolmaterial.getEnchantability();
				
				int t2 = getTrowelLevel(s2);
				int d2 = s2.getMaxDamage();
				int e2 = PhysisToolMaterial.getMaterialFromItemStack(s2).toolmaterial.getEnchantability();
				
				if (t1 == t2) {
					double v1 = d1 + 10 * e1;
					double v2 = d2 + 10 * e2;
					return v1 < v2 ? -1 : 1;
				}
				return t1 < t2 ? -1 : 1;
			}
		});
		
		types.addAll(trowels);
		
		ItemStack creativeTrowel = new ItemStack(this);
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialById(0);
		
		PhysisToolMaterial.writeMaterialToStack(mat, creativeTrowel);
		
		setCreative(creativeTrowel);
		
		types.add(creativeTrowel);
	}

	public static boolean isCreative(ItemStack trowel) {
		return trowel.stackTagCompound.hasKey(CREATIVETAG) && trowel.stackTagCompound.getBoolean(CREATIVETAG);
	}
	
	public static void setCreative(ItemStack trowel) {
		trowel.stackTagCompound.setBoolean(CREATIVETAG, true);
	}
	
	/*@Override
	public int getMaxDamage(ItemStack stack) {
		if (stack.getItem() == this) {
			PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
			if (mat != null) {
				return mat.maxdamage <= -1 ? -1 : Math.max(1, Math.round(mat.maxdamage / 5));
			}
		}
		return this.getMaxDamage();
	}*/
	
	@Override
	public int getNBTMaxDamage(ItemStack stack) {
		if (isCreative(stack)) { return -1; }
		if (stack.getItem() == this) {
			PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
			if (mat != null) {
				return mat.maxdamage <= -1 ? -1 : Math.max(1, Math.round(mat.maxdamage / 5));
			}
		}
		return 1;
	}
	
	@Override
	public int getDamage(ItemStack stack) {
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		if (mat != null) {
			int id = mat.id;
			this.setDamage(stack, id);
			return id;
		}
		return 32767;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
    {
        String trowel = ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name")).trim();
        
        if (isCreative(stack)) {
        	return StatCollector.translateToLocal("item.physis:trowel.creative") + " " + trowel;
        }
        
        String regex = StatCollector.translateToLocal("item.physis:material.regex");
        
        if (regex.equals("item.physis:material.regex")) {
        	Physis.logger.warn("Trowel regex localisation failure, setting to default");
        	regex = "(^\\w*)(?=\\sPick(axe)?)";
        }
        
        Pattern p;
        try {
        	p = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
        	Physis.logger.warn("Trowel regex compilation failure, falling back", e);
        	p = Pattern.compile("(^\\w*)(?=\\sPick(axe)?)");
        }
        
        PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
        
        if (mat != null)
        {
        	String fullpick = mat.pick.getDisplayName();
        	Matcher m = p.matcher(fullpick);
        	
        	if (m.find()) {
        		String pickmat = m.group(1);
        		
        		return pickmat +" "+ trowel;
        	} else if (mat.orematerial != null) {
        		return mat.orematerial +" "+ trowel;
        	}
        }

        return trowel;
    }
	
	/*@SideOnly(Side.CLIENT)
	public static String getTrowelLevelString(ItemStack stack) {
		String t = null;
		if (stack.getItem() instanceof ITrowel) {
			int trowellevel = ((ITrowel)(stack.getItem())).getTrowelLevel(stack);
			t = new StringBuilder().append("\u00A78").append(StatCollector.translateToLocal("item.physis:trowel.leveltooltip")).append(": ").append(trowellevel).toString();
		}
		return t;
	}*/
	
	@SuppressWarnings({ "rawtypes" , "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (isCreative(stack)) {
			List<String> info = Physis.text.translateAndWrap("item.physis:trowel.tooltip.creative", TooltipHandler.tipWidth);
			list.addAll(info);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass)
	{
		 return super.hasEffect(stack, pass) || isCreative(stack);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
    {
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		if (mat != null) {
			return mat.pickitem.getRarity(mat.pick);
		}
		
        return super.getRarity(stack);
    }
	
	/*@Override
	public int getItemEnchantability(ItemStack stack)
    {
    	PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
    	if (mat != null) {
    		return mat.toolmaterial.getEnchantability();
    	}
    	
        return super.getItemEnchantability(stack);
    }*/

	@Override
    public boolean getIsRepairable(ItemStack stack, ItemStack repairstack)
    {
    	PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
    	
   		if (mat.ingot != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat.ingot, repairstack, false)) return true;
        
        return false;
    }
	
	public int getTrowelLevel(ItemStack stack) {
		int level = 0;
		
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		if (mat != null) {
			level =	Math.min(10, mat.toolmaterial.getHarvestLevel() * 3 + 1);
		}
		
		return level;
	}
	
	public void onUseTrowel(ItemStack stack, EntityLivingBase user, boolean success) {
		if (success) {
			//stack.damageItem(1, user);
			this.applyNBTDamage(stack, user, 1);
		} else {
			//stack.damageItem(3, user);
			this.applyNBTDamage(stack, user, 3);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
    {
		float damage = 1.0f;
		
		PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
		
		if (mat != null) {
			damage += mat.toolmaterial.getDamageVsEntity();
		}

		Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", (double)damage, 0));
        return multimap;
    }
	
	public static void buildRecipes() {
		boolean gt = Loader.isModLoaded("gregtech");
		
		for(Entry<String,PhysisToolMaterial> entry : PhysisToolMaterial.materials.entrySet()) {
			PhysisToolMaterial mat = entry.getValue();

			String headmat = mat.orename;
			boolean gtvariant = false;
			if (gt) { // gregtech head check
				if (mat.orename.startsWith("ingot")) {
					String plate = "plate" + mat.orename.substring(5);
					if (OreDictionary.getOres(plate).size() > 0) {
						headmat = plate;
					}
					gtvariant = true;
				}
			}
			
			// one trowel per wool colour
			for (int wool = 0; wool<16; wool++) {
				
				ItemStack trowel = new ItemStack(PhysisItems.trowel, 1, mat.id);
				
				PhysisToolMaterial.writeMaterialToStack(mat, trowel);
				
				trowel.stackTagCompound.setInteger(HANDLETAG, wool);
				
				Object stick = mat.stickorename == null ? mat.stick : mat.stickorename;
				
				if (!gt || !gtvariant) { // normal recipe
					mat.registerRecipe(trowel,
						"HH ", "HS ", "  W",
						'H', mat.orename,
						'S', stick,
						'W', new ItemStack(Blocks.wool, 1, wool)
					);
				} else { // GT recipe for metals, if loaded
					mat.registerRecipe(trowel,
						"MMH", "MS ", "F W",
						'M', headmat,
						'S', stick,
						'W', new ItemStack(Blocks.wool, 1, wool),
						'H', "craftingToolHardHammer",
						'F', "craftingToolFile"
					);
				}
			}
		}
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
		if (mat != null) {// && mat.hastint) {
			int[] tints = mat.getHeadTints();
			int shafttint = mat.getShaftTint();
			if (pass == 0) {
				if (stack.stackTagCompound.hasKey(HANDLETAG)) {
					int woolcol = stack.stackTagCompound.getInteger(HANDLETAG);
					
					if (woolcol < EntitySheep.fleeceColorTable.length && woolcol >= 0) {
						float[] col = EntitySheep.fleeceColorTable[woolcol];
						
						return new Color(col[0], col[1], col[2]).getRGB();
					}
				}
				return 0x6B6B6B;
			}
			else if (pass == 1) {
	        	return shafttint;
	        }
	        else if (pass == 2) {
	        	return tints[0];
	        }
	        else if (pass == 3) {
	        	return tints[1];
	        }
	        else if (pass == 4) {
	        	return tints[3];
	        }
	        else if (pass == 5) {
	        	return tints[4];
	        }
	        else if (pass == 6) {
	        	return tints[6];
	        }
	        else if (pass == 7) {
	        	return tints[7];
	        } 
	        else if (pass == 8) {
	        	return tints[9];
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
        
        ResourceLocation head = new ResourceLocation(Physis.MOD_ID, "textures/items/trowel_head.png");
        this.headIcons = new ArrayList<IIcon>();
        
        if (!(register instanceof TextureMap)) {return;}
        TextureMap map = (TextureMap)register;
        for(int i=0; i<7; i++) {
        	ResourceLocation t = TextureHelper.loadTexture(Physis.MOD_ID+":trowel_head_"+i, new ToneSplitTexture(head, i));
        	this.headIcons.add( TextureHelper.getIconForDynamicResource(map, Physis.MOD_ID+":trowel_head_"+i, t, true) );
        }
	}
	
	/*@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		stack.damageItem(1, player);
		return stack;
	}*/
}
