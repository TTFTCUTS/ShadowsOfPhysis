package ttftcuts.physis.common.block;

import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.texture.DigStripTexture;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.common.helper.TextureHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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
	public static ResourceLocation blankTexture = new ResourceLocation(Physis.MOD_ID, "textures/items/journal.png");
	public static ResourceLocation[][] digTextures = new ResourceLocation[1][10];
	
	public IIcon[] testicon;
	
	public BlockDigSite() {
		super(Material.rock);
		this.setHardness(4.0F);
		this.setResistance(10.0F);
		this.setBlockName("digsite");
	}

	private static void initResources() {
		ResourceLocation dig = new ResourceLocation(Physis.MOD_ID, "textures/blocks/digsite/testdig.png");
		ResourceLocation dug = new ResourceLocation(Physis.MOD_ID, "textures/blocks/digsite/testobject.png");
		for (int i=0; i<DigStripTexture.numFrames; i++) {
			String name = "physis_digsite_dig_" + 0 + "_" + i;
			
			digTextures[0][i] = TextureHelper.loadTexture(name, 
					new DigStripTexture(Physis.MOD_ID, name, dig, dug, i));
		}
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
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if (tile != null && tile instanceof TileEntityDigSite) {
			int i = ((TileEntityDigSite)tile).level;
			
			if (i<DigStripTexture.numFrames) {
				return testicon[i];
			} else {
				return testicon[DigStripTexture.numFrames-1];
			}
		}
        return this.getIcon(side, 0);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		initResources();
		
		testicon = new IIcon[DigStripTexture.numFrames];
		if (register instanceof TextureMap) {
			TextureMap map = (TextureMap) register;
			
			//testicon = TextureHelper.buildDigSprite(map, "digsite", "sand", new String[]{"journal"}, new ResourceLocation[]{new ResourceLocation("physis", "textures/items/journal.png")});
			for (int i=0; i<DigStripTexture.numFrames; i++) {
				testicon[i] = TextureHelper.buildDigSprite(map, "digsite", "dirt", new String[]{"testdig"+i}, new ResourceLocation[]{digTextures[0][i]});
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
