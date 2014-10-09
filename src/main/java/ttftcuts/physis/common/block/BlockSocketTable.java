package ttftcuts.physis.common.block;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.PhysisGuis;
import ttftcuts.physis.client.render.RenderSocketTable;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockSocketTable extends BlockContainerPhysis {
	
	public BlockSocketTable() {
		super(Material.wood);
		this.setBlockName("socketTable");
		this.setBlockTextureName("minecraft:planks_oak");
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntitySocketTable();
	}

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
    {
    	if (side == world.getBlockMetadata(x, y, z)) {
    		// drawer
    		player.openGui(Physis.instance, PhysisGuis.SOCKET_TABLE_DRAWER.getID(), world, x, y, z);
    	} else {
    		// work table
    		Block above = world.getBlock(x, y+1, z);
    		if (!above.isAir(world, x, y+1, z)) {
    			// blocked!
    			return false;
    		}
    		player.openGui(Physis.instance, PhysisGuis.SOCKET_TABLE.getID(), world, x, y, z);
    	}
    	
    	return true;
    }
    
    @Override
    public int getRenderType() {
    	return RenderSocketTable.renderid;
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
    	return false;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        int facing = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int meta = 0;

        if (facing == 0)
        {
            meta = 2;
        } 
        else if (facing == 1)
        {
            meta = 5;
        }
        else if (facing == 2)
        {
            meta = 3;
        }
        else if (facing == 3)
        {
            meta = 4;
        }
        
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
        
        TileEntity t = world.getTileEntity(x, y, z);
        if (t != null && t instanceof TileEntitySocketTable) {
        	((TileEntitySocketTable)t).facing = meta;
        }
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
    	
    	TileEntity tile = world.getTileEntity(x, y, z);
    	
    	if (tile != null && tile instanceof TileEntitySocketTable) {
    		((TileEntitySocketTable)tile).dropInventory();
    	}
    	
    	super.breakBlock(world, x,y,z, block, metadata);
    }
}
