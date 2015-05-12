package ttftcuts.physis.common.helper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldGenHelper {
	public static int getTopGroundBlock(World world, int x, int z) {
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
        int y = chunk.getTopFilledSegment() + 15;
        int cx = x & 15;

        for (int cz = z & 15; y > 0; --y)
        {
            Block block = chunk.getBlock(cx, y, cz);

            if (block.getMaterial().blocksMovement() && 
            	block.getMaterial() != Material.leaves && 
            	block.getMaterial() != Material.wood &&
            	block.getMaterial() != Material.gourd &&
            	block.getMaterial() != Material.ice &&
            	!block.isFoliage(world, x, y, z) &&
            	block.isBlockNormalCube())
            {
                return y + 1;
            }
        }

        return -1;
	}
}
