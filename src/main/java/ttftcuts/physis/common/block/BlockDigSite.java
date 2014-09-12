package ttftcuts.physis.common.block;

import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.texture.DigSiteAtlasSprite;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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
	
	public IIcon testicon;
	
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
        for (int i = 0; i < 10; ++i)
        {
        	ItemStack stack = new ItemStack(this, 1, 0);
        	
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
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDigSite();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		return Blocks.soul_sand.getBlockTextureFromSide(side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		/*TileEntity tile = world.getTileEntity(x, y, z);
		
		if (tile != null && tile instanceof TileEntityDigSite) {
			int i = ((TileEntityDigSite)tile).level;
			
			if (i==0) {
				return Blocks.dirt.getBlockTextureFromSide(side);
			} else if (i==1) {
				return Blocks.stone.getBlockTextureFromSide(side);
			} else {
				return Blocks.diamond_block.getBlockTextureFromSide(side);
			}
		}
        return this.getIcon(side, 0);*/
		return testicon;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		if (register instanceof TextureMap) {
			TextureMap map = (TextureMap) register;
			
			String name = DigSiteAtlasSprite.getDerivedName("digsite", "dirt", new String[]{"journal"});
			
			Physis.logger.info(name);
			
			TextureAtlasSprite texture = map.getTextureExtry(name);
			if (texture == null) {
				texture = new DigSiteAtlasSprite("digsite", "dirt", new String[]{"journal"}, new ResourceLocation[]{new ResourceLocation("physis", "textures/items/journal.png")});
				map.setTextureEntry(name, texture);
			}
			
			testicon = map.getTextureExtry(name);
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
