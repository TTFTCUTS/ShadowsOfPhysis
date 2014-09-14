package ttftcuts.physis.common.block;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.texture.DigStripTexture;
import ttftcuts.physis.common.block.tile.DigSiteLocale;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.common.helper.TextureHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDigSite extends BlockContainerPhysis {

	public static final String LEVELTAG = "physisdiglevel";
	public static ResourceLocation blankTexture = new ResourceLocation(Physis.MOD_ID, "textures/items/journal.png");
	public static DigSiteLocale[] locales = new DigSiteLocale[]
	{
		new DigSiteLocale("dirt", "dirt", "dig"),
		new DigSiteLocale("sand", "sand", "dig"),
		new DigSiteLocale("clay", "clay", "dig"),
	};
	public static Map<String,ResourceLocation> artifacts;
	static {
		artifacts = new HashMap<String,ResourceLocation>();
		artifacts.put("testobject", new ResourceLocation(Physis.MOD_ID, "textures/blocks/digsite/testobject.png"));
	}
	public static Map<String,ResourceLocation> digtextures;
	static {
		digtextures = new HashMap<String,ResourceLocation>();
		digtextures.put("dig", new ResourceLocation(Physis.MOD_ID, "textures/blocks/digsite/testdig.png"));
	}
	
	public IIcon[] testicon;
	
	public BlockDigSite() {
		super(Material.rock);
		this.setHardness(4.0F);
		this.setResistance(10.0F);
		this.setBlockName("digsite");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List subtypes)
    {
		for (int meta = 0; meta < locales.length; meta++) {
	        for (int i = 0; i < 10; ++i)
	        {
	        	ItemStack stack = new ItemStack(this, 1, meta);
	        	
	        	NBTTagCompound tag = new NBTTagCompound();
	        	NBTTagCompound display = new NBTTagCompound();
	       		tag.setTag("display", display);
	       		NBTTagList lore = new NBTTagList();
	       		lore.appendTag(new NBTTagString("Level "+(i+1)));
	       		display.setTag("Lore", lore);
	        	tag.setInteger(LEVELTAG, i);
	        	
	        	stack.setTagCompound(tag);
	        	
	            subtypes.add(stack);
	        }
		}
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDigSite();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		meta = Math.min(meta, locales.length-1);
		return locales[meta].icons.get("testobject")[0];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		TileEntity tile = world.getTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		
		if (tile != null && tile instanceof TileEntityDigSite) {
			int i = ((TileEntityDigSite)tile).level;
			i = Math.min(i, DigStripTexture.numFrames - 1);
			
			return locales[meta].icons.get("testobject")[i];
		}
        return this.getIcon(side, 0);
    }
	
	public static String getDigTextureName(String dig, String dug) {
		return "physis_digsite_dig_" + dig + "_" + dug;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		if (register instanceof TextureMap) {
			TextureMap map = (TextureMap) register;
			
			int n = DigStripTexture.numFrames;
			
			Iterator<Entry<String, ResourceLocation>> artit = artifacts.entrySet().iterator();
			while(artit.hasNext()) {
			
				Map.Entry<String, ResourceLocation> art = (Map.Entry<String, ResourceLocation>)artit.next();
				
				Iterator<Entry<String, ResourceLocation>> digit = digtextures.entrySet().iterator();
				while(digit.hasNext()) {
				
					Map.Entry<String, ResourceLocation> dig = (Map.Entry<String, ResourceLocation>)digit.next();
	
					ResourceLocation[] digtex = new ResourceLocation[n];
					for (int i=0; i<n; i++) {
						String name = getDigTextureName(dig.getKey(), art.getKey()) + "_" + i;
						
						digtex[i] = TextureHelper.loadTexture(name, new DigStripTexture(Physis.MOD_ID, name, dig.getValue(), art.getValue(), i));
					}
				}
				
				for(int l = 0; l<locales.length; l++) {
					DigSiteLocale locale = locales[l];
					
					locale.addIconSet(map, art.getKey());
				}
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) 
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if (tile != null && tile instanceof TileEntityDigSite) {
			if(stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				if (tag.hasKey(LEVELTAG)) {
					((TileEntityDigSite)tile).level = tag.getInteger(LEVELTAG);
				}
			}
		}
	}
}
