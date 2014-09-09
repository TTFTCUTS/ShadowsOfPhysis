package ttftcuts.physis.common.block;

import ttftcuts.physis.Physis;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockContainerPhysis extends BlockContainer {

	public BlockContainerPhysis(Material material) {
		super(material);
		this.setCreativeTab(Physis.creativeTab);
	}

	@Override
	public abstract TileEntity createNewTileEntity(World world, int meta);

}
