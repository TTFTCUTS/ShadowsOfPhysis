package ttftcuts.physis.common.worldgen.structure;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class BlockPalette {
	public static BlockPalette defaultPalette = new BlockPalette();

	public Entry test = BlockTypes.cobble;
	
	public static class BlockTypes {
		// stone and brick
		public static final Entry cobble 			 = new Entry(Blocks.cobblestone);
		public static final Entry cobble_mossy 		 = new Entry(Blocks.mossy_cobblestone);
		public static final Entry stone 			 = new Entry(Blocks.stone);
		public static final Entry stonebrick 		 = new Entry(Blocks.stonebrick, 0);
		public static final Entry stonebrick_mossy	 = new Entry(Blocks.stonebrick, 1);
		public static final Entry stonebrick_cracked = new Entry(Blocks.stonebrick, 2);
		public static final Entry stone_chiselled	 = new Entry(Blocks.stonebrick, 3);
		public static final Entry netherbrick		 = new Entry(Blocks.nether_brick);
		public static final Entry brick				 = new Entry(Blocks.brick_block);
		public static final Entry obsidian 			 = new Entry(Blocks.obsidian);
		public static final Entry netherrack		 = new Entry(Blocks.netherrack);
		
		public static final Entry sandstone			 = new Entry(Blocks.sandstone, 0);
		public static final Entry sandstone_glyph	 = new Entry(Blocks.sandstone, 1);
		public static final Entry sandstone_smooth	 = new Entry(Blocks.sandstone, 2);
		
		// earth
		public static final Entry dirt				 = new Entry(Blocks.dirt, 0);
		public static final Entry dirt_coarse		 = new Entry(Blocks.dirt, 1);
		public static final Entry sand				 = new Entry(Blocks.sand, 0);
		public static final Entry sand_red			 = new Entry(Blocks.sand, 1);
		public static final Entry gravel			 = new Entry(Blocks.gravel);
		public static final Entry soulsand			 = new Entry(Blocks.soul_sand);
		public static final Entry clay				 = new Entry(Blocks.clay);
		
		public static final Entry grass				 = new Entry(Blocks.grass);
		public static final Entry podzol			 = new Entry(Blocks.dirt, 2);
		public static final Entry mycelium			 = new Entry(Blocks.mycelium);
		
		// wood
		public static final Entry planks_oak 		 = new Entry(Blocks.planks, 0);
		public static final Entry planks_spruce 	 = new Entry(Blocks.planks, 1);
		public static final Entry planks_birch 		 = new Entry(Blocks.planks, 2);
		public static final Entry planks_jungle 	 = new Entry(Blocks.planks, 3);
		public static final Entry planks_acacia 	 = new Entry(Blocks.planks, 4);
		public static final Entry planks_darkoak 	 = new Entry(Blocks.planks, 5);
		
		public static final Entry log_oak 			 = new Entry(Blocks.log,  0, MetaType.LOG);
		public static final Entry log_spruce 		 = new Entry(Blocks.log,  1, MetaType.LOG);
		public static final Entry log_birch 		 = new Entry(Blocks.log,  2, MetaType.LOG);
		public static final Entry log_jungle 		 = new Entry(Blocks.log,  3, MetaType.LOG);
		public static final Entry log_acacia 		 = new Entry(Blocks.log2, 0, MetaType.LOG);
		public static final Entry log_darkoak 		 = new Entry(Blocks.log2, 1, MetaType.LOG);
		
		// stairs
		public static final Entry stairs_cobble 	 = new Entry(Blocks.stone_stairs, MetaType.STAIRS);
		public static final Entry stairs_stonebrick	 = new Entry(Blocks.stone_brick_stairs, MetaType.STAIRS);
		public static final Entry stairs_oak 		 = new Entry(Blocks.oak_stairs, MetaType.STAIRS);
		public static final Entry stairs_spruce 	 = new Entry(Blocks.spruce_stairs, MetaType.STAIRS);
		public static final Entry stairs_jungle 	 = new Entry(Blocks.jungle_stairs, MetaType.STAIRS);
		public static final Entry stairs_birch 		 = new Entry(Blocks.birch_stairs, MetaType.STAIRS);
		public static final Entry stairs_acacia 	 = new Entry(Blocks.acacia_stairs, MetaType.STAIRS);
		public static final Entry stairs_darkoak 	 = new Entry(Blocks.dark_oak_stairs, MetaType.STAIRS);
		public static final Entry stairs_netherbrick = new Entry(Blocks.nether_brick_stairs, MetaType.STAIRS);
		public static final Entry stairs_sandstone 	 = new Entry(Blocks.sandstone_stairs, MetaType.STAIRS);
		public static final Entry stairs_quartz 	 = new Entry(Blocks.quartz_stairs, MetaType.STAIRS);
		public static final Entry stairs_brick	 	 = new Entry(Blocks.brick_stairs, MetaType.STAIRS);
		
		// slabs
		public static final Entry slab_stone		 = new Entry(Blocks.stone_slab, 0, MetaType.SLAB);
		public static final Entry slab_sandstone	 = new Entry(Blocks.stone_slab, 1, MetaType.SLAB);
		public static final Entry slab_cobble		 = new Entry(Blocks.stone_slab, 3, MetaType.SLAB);
		public static final Entry slab_brick		 = new Entry(Blocks.stone_slab, 4, MetaType.SLAB);
		public static final Entry slab_stonebrick	 = new Entry(Blocks.stone_slab, 5, MetaType.SLAB);
		public static final Entry slab_netherbrick	 = new Entry(Blocks.stone_slab, 6, MetaType.SLAB);
		public static final Entry slab_quartz		 = new Entry(Blocks.stone_slab, 7, MetaType.SLAB);
		
		public static final Entry slab_oak			 = new Entry(Blocks.wooden_slab, 0, MetaType.SLAB);
		public static final Entry slab_spruce		 = new Entry(Blocks.wooden_slab, 1, MetaType.SLAB);
		public static final Entry slab_birch		 = new Entry(Blocks.wooden_slab, 2, MetaType.SLAB);
		public static final Entry slab_jungle		 = new Entry(Blocks.wooden_slab, 3, MetaType.SLAB);
		public static final Entry slab_acacia		 = new Entry(Blocks.wooden_slab, 4, MetaType.SLAB);
		public static final Entry slab_darkoak		 = new Entry(Blocks.wooden_slab, 5, MetaType.SLAB);
		
		// fences
		public static final Entry fence_wood		 = new Entry(Blocks.fence);
		public static final Entry fence_netherbrick	 = new Entry(Blocks.nether_brick_fence);
		public static final Entry wall_cobble		 = new Entry(Blocks.cobblestone_wall, 0);
		public static final Entry wall_cobble_mossy  = new Entry(Blocks.cobblestone_wall, 1);
		
		// decoratives
		public static final Entry glass 			 = new Entry(Blocks.glass);
		public static final Entry glass_stained		 = new Entry(Blocks.stained_glass, MetaType.COLOURED);
		public static final Entry wool				 = new Entry(Blocks.wool, MetaType.COLOURED);
		public static final Entry stained_clay		 = new Entry(Blocks.stained_hardened_clay, MetaType.COLOURED);
		
		public static final Entry glass_pane		 = new Entry(Blocks.glass_pane);
		public static final Entry glass_pane_stained = new Entry(Blocks.stained_glass_pane, MetaType.COLOURED);
		public static final Entry ironbars			 = new Entry(Blocks.iron_bars);
		
		public static final Entry carpet			 = new Entry(Blocks.carpet, MetaType.COLOURED);
		
		public static final Entry cobweb			 = new Entry(Blocks.web);
		
		public static final Entry quartz			 = new Entry(Blocks.quartz_block, 0);
		public static final Entry quartz_chiselled	 = new Entry(Blocks.quartz_block, 0);
		public static final Entry quartz_pillar		 = new Entry(Blocks.quartz_block, MetaType.PILLAR);
		
		public static final Entry bookshelf			 = new Entry(Blocks.bookshelf);
		public static final Entry glowstone			 = new Entry(Blocks.glowstone);
		public static final Entry glowstone_lamp	 = new Entry(Blocks.redstone_lamp);
		
		public static final Entry torch				 = new Entry(Blocks.torch);
		
		public static final Entry ladder			 = new Entry(Blocks.ladder, MetaType.WALLOBJECT);
		public static final Entry sign				 = new Entry(Blocks.wall_sign, MetaType.WALLOBJECT);
	}
	
	public static class Entry {
		private final Block block;
		private final int meta;
		private final MetaType metatype;
		private final int variants;
		
		public Entry(Block block) {
			this(block, 0, MetaType.STANDARD);
		}
		
		public Entry(Block block, int meta) {
			this(block, meta, MetaType.STANDARD);
		}
		
		public Entry(Block block, MetaType type) {
			this(block, 0, type);
		}
		
		public Entry(Block block, int meta, MetaType type) {
			this.block = block;
			this.meta = meta;
			this.metatype = type;
			
			List<ItemStack> list = new ArrayList<ItemStack>();
			this.block.getSubBlocks(null, null, list);
			
			this.variants = list.size();
		}
		
		public Block getBlock() {
			return this.block;
		}
		
		public int getMeta() {
			return getMeta(0, false, 0, 0);
		}
		public int getMeta(int rotation, boolean flipped, int offset, int colour) {
			// stairs
			if (this.metatype == MetaType.STAIRS) {
				boolean upsidedown = offset > 3;
				if (upsidedown) {
					offset -= 4;
				}
				if (flipped) {
					if (rotation == 1 || rotation == 3) {
						rotation = 4-rotation;
					}
				}
				int rot = (rotation + offset) % 4;
				int out = rot == 0 ? 2 : rot == (flipped ? 3 : 1) ? 1 : rot == 2 ? 3 : 0;
				return upsidedown ? out + 4 : out;
			}
			// logs
			else if (this.metatype == MetaType.LOG) {
				if (offset > 0) {
					if (rotation == 1 || rotation == 3) {
						return this.meta + (offset == 1 ? this.variants : this.variants*2);
					} else {
						return this.meta + (offset == 1 ? this.variants*2 : this.variants);
					}
				} else {
					return this.meta;
				}
			}
			// subtypes
			else if (this.metatype == MetaType.COLOURED) {
				return colour;
			}
			// pillar
			else if (this.metatype == MetaType.PILLAR) {
				if (offset > 0) {
					if (rotation == 1 || rotation == 3) {
						return this.meta + (offset == 1 ? 4 : 3);
					} else {
						return this.meta + (offset == 1 ? 3 : 4);
					}
				} else {
					return 2;
				}
			}
			// slab
			else if (this.metatype == MetaType.SLAB) {
				return this.meta + (offset > 0 ? 8 : 0);
			}
			// wall object
			else if (this.metatype == MetaType.WALLOBJECT) {
				if (flipped) {
					if (rotation == 1 || rotation == 3) {
						rotation = 4-rotation;
					}
				}
				int rot = (rotation + offset) % 4;
				return rot == 0 ? 3 : rot == (flipped ? 3 : 1) ? 4 : rot == 2 ? 2 : 5;
				//return offset == 0 ? 3 : offset == 1 ? 4 : offset == 2 ? 2 : 5;
			}
			
			// default
			return this.meta;
		}
	}
	
	public static enum MetaType {
		STANDARD,
		COLOURED,
		STAIRS,
		LOG,
		PILLAR,
		SLAB,
		WALLOBJECT
	}
}
