package ttftcuts.physis.common.worldgen;

import java.util.Random;

import ttftcuts.physis.common.PhysisBlocks;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenDigSiteBasic implements IWorldGenerator {

	public static final int chunksPerSiteSealevel = 200;
	public static final int chunksPerSiteTop = 50;
	private static final int siteRadius = 4;
	
	private static final int[][] pillarcoords = {{-1, -3},{1, -3},{3, -1},{3, 1},{1, 3},{-1, 3},{-3, 1},{-3, -1}};
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		//if (world != DimensionManager.getWorld(0)) { return; }

		int x = chunkX*16 + random.nextInt(16);
		int z = chunkZ*16 + random.nextInt(16);
		int y = world.getTopSolidOrLiquidBlock(x, z);
		
		if (y < 63) { return; }
		
		int clamped = Math.min(255, Math.max(63, y));
		
		double fraction = (clamped - 63) / 192.0;
		
		int outof = (int)(chunksPerSiteSealevel * (1.0-fraction) + chunksPerSiteTop * fraction);
		
		if (random.nextInt(outof) != 0) { return; }
		
		Block digtype = PhysisBlocks.digSiteDirt;
		Block topblock = world.getBlock(x, y-1, z);
		
		if (!(topblock == Blocks.grass 
			|| topblock == Blocks.dirt 
			|| topblock == Blocks.sand 
			|| topblock == Blocks.clay
			|| topblock == Blocks.hardened_clay
		)) 
		{ return; }
		
		Block pillar1 = Blocks.cobblestone;
		int pillarmeta1 = 0;
		Block pillar2 = Blocks.mossy_cobblestone;
		int pillarmeta2 = 0;
		
		if (topblock == Blocks.sand) {
			digtype = PhysisBlocks.digSiteSand;
			pillar1 = Blocks.sandstone;
			pillarmeta1 = 1;
			pillar2 = Blocks.sandstone;
			pillarmeta2 = 2;
		} else if (topblock == Blocks.clay) {
			digtype = PhysisBlocks.digSiteClay;
		}
		
		int dx,dy,dz;
		double d, xdiff, zdiff;
		int r = siteRadius * 2 + 1;
		for(int ix=0; ix<r; ix++) {
			for(int iz=0; iz<r; iz++) {
				dx = x + ix - siteRadius;
				dz = z + iz - siteRadius;
				dy = world.getTopSolidOrLiquidBlock(dx, dz) - 1;
				
				xdiff = dx-x;
				zdiff = dz-z;
				
				d = Math.sqrt(xdiff*xdiff + zdiff*zdiff) / (siteRadius + 0.5);
				
				if (random.nextDouble() >= (d - 0.5)*2) {
					this.modifySurface(random, world, dx, dy, dz);
				}
			}
		}
		
		//int level = (int)Math.round(9 * (random.nextDouble()*random.nextDouble()));
		int level = random.nextInt(10);
		
		world.setBlock(x, y-2, z, digtype);
		TileEntity t = world.getTileEntity(x, y-2, z);
		if (t != null && t instanceof TileEntityDigSite) {
			((TileEntityDigSite)t).onPlaced(level);
		}
		
		if (level >= 5) {
			world.setBlock(x, y, z, Blocks.mossy_cobblestone);
			this.setSnow(world, x, y+1, z);
			
			world.setBlock(x, y-1, z, Blocks.mob_spawner, 0, 2);
            TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getTileEntity(x, y-1, z);

            if (spawner != null)
            {
            	String mob = random.nextBoolean() ? "Zombie" : "Skeleton";
                spawner.func_145881_a().setEntityName(mob);
            }
		}
		
		int always = random.nextInt(pillarcoords.length);
		int onein = Math.max(1, (int)(6 - ((level / 9.0) * 5.0)));
		for (int i=0; i<pillarcoords.length; i++) {
			int ox = x + pillarcoords[i][0];
			int oz = z + pillarcoords[i][1];
			
			if (i == always || random.nextInt(onein) == 0) {
				int height = (i == always) ? random.nextInt(2)+2 : random.nextInt(3)+1;
				
				int oy = world.getTopSolidOrLiquidBlock(ox, oz);
				
				for (int h=0; h<height; h++) {
					Block b = pillar1;
					int m = pillarmeta1;
					if (random.nextBoolean()) {
						b = pillar2;
						m = pillarmeta2;
					}
					
					world.setBlock(ox, oy+h, oz, b, m, 3);
				}
				
				this.setSnow(world, ox, oy+1+height, oz);
			}
		}
		
		/*for (int i=0; i<20; i++) {
			world.setBlock(x, y+i+20, z, Blocks.glowstone);
		}*/
	}
	
	private void modifySurface(Random rand, World world, int x, int y, int z) {
		Block b = world.getBlock(x, y, z);
		
		if (b == Blocks.grass || b == Blocks.dirt || b == Blocks.stone) {
			world.setBlock(x, y, z, Blocks.dirt, 1, 3);
		} else if (b == Blocks.sand) {
			if (rand.nextInt(3) == 0) {
				world.setBlock(x, y, z, Blocks.gravel, 0, 3);
			}
		} else if (b == Blocks.hardened_clay) {
			if (rand.nextInt(2) == 0) {
				world.setBlock(x, y, z, Blocks.sand, 1, 3);
			}
		}
	}
	
	private void setSnow(World world, int x, int y, int z) {
		if (world.canSnowAtBody(x, y, z, true)) {
			world.setBlock(x, y, z, Blocks.snow_layer);
		}
	}

}
