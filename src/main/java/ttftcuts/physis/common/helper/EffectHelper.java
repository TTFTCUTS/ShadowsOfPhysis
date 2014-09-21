package ttftcuts.physis.common.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EffectHelper {

	public static void doBlockBreakEffect(World world, EntityPlayer player, int x, int y, int z) {
		world.playAuxSFXAtEntity(player, 2001, x, y, z, Block.getIdFromBlock(world.getBlock(x, y, z)) + (world.getBlockMetadata(x, y, z) << 12));
	}
}
