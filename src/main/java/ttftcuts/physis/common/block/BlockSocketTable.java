package ttftcuts.physis.common.block;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.PhysisGuis;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSocketTable extends BlockContainerPhysis {
	
	private IIcon topIcon;
	private IIcon frontIcon;
	
	public BlockSocketTable() {
		super(Material.wood);
		this.setBlockName("socketTable");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntitySocketTable();
	}

	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int pass)
    {
        return side == 1 ? this.topIcon : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ? this.blockIcon : this.frontIcon));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(this.getTextureName() + "_side");
        this.topIcon = register.registerIcon(this.getTextureName() + "_top");
        this.frontIcon = register.registerIcon(this.getTextureName() + "_front");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
    {
    	player.openGui(Physis.instance, PhysisGuis.SOCKET_TABLE.getID(), world, x, y, z);
    	
    	return true;
    }
}
