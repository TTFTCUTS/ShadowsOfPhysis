package ttftcuts.physis.common.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.item.ITrowel;
import ttftcuts.physis.client.render.RenderDigSite;
import ttftcuts.physis.client.texture.DigStripTexture;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.block.tile.DigSiteLocale;
import ttftcuts.physis.common.block.tile.DigSiteRenderLayer;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.common.helper.TextureHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

	public static ResourceLocation blankTexture = new ResourceLocation(Physis.MOD_ID, "textures/items/journal.png");
	public static Map<String, DigSiteLocale> locales;
	static {
		locales = new HashMap<String, DigSiteLocale>();
		locales.put("dirt", new DigSiteLocale("dirt", "dirt", "dig", Material.ground, Block.soundTypeGravel, 
				"square", "triangle", "circle", 0xFF0000, 0x80FF80, 0x000080));
		locales.put("sand", new DigSiteLocale("sand", "sand", "dig", Material.sand, Block.soundTypeSand, 
				"square", "triangle", "circle", 0xFF0000, 0x80FF80, 0x000080));
		locales.put("clay", new DigSiteLocale("clay", "clay", "dig", Material.ground, Block.soundTypeGravel, 
				"square", "triangle", "circle", 0xFF0000, 0x80FF80, 0x000080));
	}
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
	
	public IIcon testicon;
	public IIcon transparent;
	public final DigSiteLocale locale;
	
	public BlockDigSite(String localename) {		
		super(locales.get(localename).material);
		this.setCreativeTab(Physis.digsiteTab);
		this.locale = locales.get(localename);
		this.setStepSound(locale.sounds);
		this.setHardness(4.0F);
		this.setResistance(10.0F);
		this.setBlockName("digsite."+locale.name);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return RenderDigSite.renderid;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List subtypes)
    {
        for (int i = 0; i < 10; ++i)
        {
        	ItemStack stack = new ItemStack(this, 1, 0);
        	
        	NBTTagCompound tag = new NBTTagCompound();
        	NBTTagCompound display = new NBTTagCompound();
       		tag.setTag("display", display);
       		NBTTagList lore = new NBTTagList();
       		lore.appendTag(new NBTTagString("Level "+(i+1)));
       		display.setTag("Lore", lore);
        	tag.setInteger(TileEntityDigSite.LEVELTAG, i);
        	
        	stack.setTagCompound(tag);
        	
            subtypes.add(stack);
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDigSite();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		//meta = Math.min(meta, locales.length-1);
		return this.locale.icons.get("testobject")[0];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		TileEntity btile = world.getTileEntity(x, y, z);
		//int meta = world.getBlockMetadata(x, y, z);
		
		if (btile != null && btile instanceof TileEntityDigSite) 
		{
			TileEntityDigSite tile = (TileEntityDigSite)btile;
			int renderlayer = tile.renderlayer;
			
			if (renderlayer == 0) {
				int i = tile.getDigFrame();
				
				return this.locale.icons.get("testobject")[i];
			} else {
				DigSiteRenderLayer r = tile.renderdata.get(renderlayer-1);
				if ( r != null) {
					if (r.shapes[side] == -1) { return transparent; }
					return this.locale.shapes[r.shapes[side]][r.positions[side]];
				}
				
				return testicon;
			}
		}
        return this.getIcon(side, 0);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		TileEntity btile = world.getTileEntity(x, y, z);
		
		if (btile != null && btile instanceof TileEntityDigSite) {
			TileEntityDigSite tile = (TileEntityDigSite)btile;
			int renderlayer = tile.renderlayer;
			
			if (renderlayer != 0) {
				DigSiteRenderLayer r = tile.renderdata.get(renderlayer-1);
				if ( r != null) {
					return this.locale.colours[r.colour];
				}
			}
		}
        return 0xFFFFFF;
	}
	
	public static String getDigTextureName(String dig, String dug) {
		return "physis_digsite_dig_" + dig + "_" + dug;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) 
	{
		if (register instanceof TextureMap) {
			TextureMap map = (TextureMap) register;
			
			int n = DigStripTexture.numFrames;
			
			for (Entry<String, ResourceLocation> art : artifacts.entrySet()) {
				for (Entry<String, ResourceLocation> dig : digtextures.entrySet()) {
					ResourceLocation[] digtex = new ResourceLocation[n];
					for (int i=0; i<n; i++) {
						String name = getDigTextureName(dig.getKey(), art.getKey()) + "_" + i;
						
						digtex[i] = TextureHelper.loadTexture(name, new DigStripTexture(name, dig.getValue(), art.getValue(), i));
					}
				}
				
				
				for(Entry<String,DigSiteLocale> entry : locales.entrySet()) {
					DigSiteLocale locale = entry.getValue();
					
					locale.addIconSet(map, art.getKey());
				}
			}
			
			String shapepath = Physis.MOD_ID+":digsite/shapes/";
			for(Entry<String,DigSiteLocale> entry : locales.entrySet()) {
				DigSiteLocale locale = entry.getValue();
				
				IIcon[] s1 = new IIcon[8];
				IIcon[] s2 = new IIcon[8]; 
				IIcon[] s3 = new IIcon[8]; 
				for(int i=0; i<8; i++) {
					s1[i] = register.registerIcon(shapepath+locale.shapename1+i);
					s2[i] = register.registerIcon(shapepath+locale.shapename2+i);
					s3[i] = register.registerIcon(shapepath+locale.shapename3+i);
				}
				IIcon[][] s = {s1,s2,s3};
				locale.shapes = s;
			}
		}
		
		testicon = register.registerIcon("physis:digsite/testmarking");
		transparent = register.registerIcon("physis:transparent");
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) 
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if (tile != null && tile instanceof TileEntityDigSite) {
			if(stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				if (tag.hasKey(TileEntityDigSite.LEVELTAG)) {
					((TileEntityDigSite)tile).onPlaced(tag.getInteger(TileEntityDigSite.LEVELTAG));
				}
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
	{
		ItemStack held = player.getHeldItem();
		TileEntity btile = world.getTileEntity(x, y, z);
		
		if (held != null && held.getItem() instanceof ITrowel) {
			if (btile != null && btile instanceof TileEntityDigSite) {
				TileEntityDigSite tile = (TileEntityDigSite)btile;
				
				return tile.onActivation(world, player, side);
			}
		}
		
		return false;
	}
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return PhysisItems.component;
    }

	@Override
    public int quantityDroppedWithBonus(int bonus, Random rand)
    {
        return this.quantityDropped(rand) + (bonus > 0 ? Math.max(0, rand.nextInt(bonus)-2) : 0);
    }
    @Override
    public int quantityDropped(Random rand)
    {
        return 1;
    }
    
    @Override
    public int damageDropped(int meta)
    {
        return 0;
    }
}
