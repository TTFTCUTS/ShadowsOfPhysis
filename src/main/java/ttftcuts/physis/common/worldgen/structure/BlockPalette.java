package ttftcuts.physis.common.worldgen.structure;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.world.World;

public class BlockPalette {
	public Entry foundation = BlockTypes.cobble;
	
	// floors
	public Entry floor1 = BlockTypes.planks_birch;
	public Entry floor2 = BlockTypes.planks_oak;
	public Entry floor_coloured1 = BlockTypes.wool;
	
	// stairs
	public Entry stairs1 = BlockTypes.stairs_birch;
	public Entry stairs2 = BlockTypes.stairs_oak;
	
	// pillars
	public Entry pillar1 = BlockTypes.log_oak;
	public Entry fence = BlockTypes.fence_wood;
	
	// walls
	public Entry wall1 = BlockTypes.planks_oak;
	public Entry wall_trim1 = BlockTypes.stonebrick;
	public Entry wall_trim2 = BlockTypes.planks_darkoak;
	
	public Entry window_pane = BlockTypes.glass_pane;
	public Entry window_block = BlockTypes.glass;
	
	// roof
	public Entry roof_block1 = BlockTypes.brick;
	public Entry roof_stair1 = BlockTypes.stairs_brick;
	public Entry roof_slab1 = BlockTypes.slab_brick;
	
	public Entry roof_block2 = BlockTypes.planks_spruce;
	public Entry roof_stair2 = BlockTypes.stairs_spruce;
	public Entry roof_slab2 = BlockTypes.slab_spruce;
	
	// misc
	public Entry ladder = BlockTypes.ladder;
	public Entry light = BlockTypes.torch;
	public Entry door = BlockTypes.door_wood;
	
	// furniture
	public Entry goods = BlockTypes.haybale;
	public Entry chair_body = BlockTypes.stairs_oak;
	public Entry chair_arm = BlockTypes.sign;
	public Entry bed = BlockTypes.bed;
			
	//###############################################################
	public final int id;
	
	public BlockPalette() {
		this.id = BlockPalettes.paletteRegistry.size();
		BlockPalettes.paletteRegistry.add(this);
	}
	
	public static class BlockPalettes {
		public static List<BlockPalette> paletteRegistry;
		
		public static BlockPalette defaultPalette;
		
		public static BlockPalette desert;
		public static BlockPalette nether;
		
		public static void init() {
			paletteRegistry = new ArrayList<BlockPalette>();
			
			defaultPalette = new BlockPalette();
			
			desert = new BlockPalette();
			desert.foundation = BlockTypes.sandstone;
			desert.wall1 = BlockTypes.clay_stained.getColour(8);
			desert.wall_trim1 = BlockTypes.sandstone_smooth;
			desert.wall_trim2 = BlockTypes.sandstone_glyph;
			desert.roof_block1 = BlockTypes.sandstone;
			desert.roof_slab1 = BlockTypes.slab_sandstone;
			desert.roof_stair1 = BlockTypes.stairs_sandstone;
			desert.roof_block2 = BlockTypes.planks_acacia;
			desert.roof_slab2 = BlockTypes.slab_acacia;
			desert.roof_stair2 = BlockTypes.stairs_acacia;
			desert.floor1 = BlockTypes.clay_stained.getColour(9);
			desert.stairs1 = BlockTypes.stairs_sandstone;
			desert.floor2 = BlockTypes.sandstone;
			desert.stairs2 = BlockTypes.stairs_sandstone;
			desert.goods = BlockTypes.log_acacia;
			desert.pillar1 = BlockTypes.log_acacia;
			
			nether = new BlockPalette();
			nether.foundation = BlockTypes.netherbrick;
			nether.wall1 = BlockTypes.stonebrick_cracked;
			nether.wall_trim1 = BlockTypes.netherbrick;
			nether.wall_trim2 = BlockTypes.netherbrick;
			nether.roof_block1 = BlockTypes.netherbrick;
			nether.roof_slab1 = BlockTypes.slab_netherbrick;
			nether.roof_stair1 = BlockTypes.stairs_netherbrick;
			nether.roof_block2 = BlockTypes.netherbrick;
			nether.roof_slab2 = BlockTypes.slab_netherbrick;
			nether.roof_stair2 = BlockTypes.stairs_netherbrick;
			nether.floor1 = BlockTypes.quartz;
			nether.stairs1 = BlockTypes.stairs_quartz;
			nether.floor2 = BlockTypes.netherbrick;
			nether.stairs2 = BlockTypes.stairs_netherbrick;
			nether.goods = BlockTypes.glowstone;
			nether.pillar1 = BlockTypes.quartz_pillar;
			nether.fence = BlockTypes.fence_netherbrick;
			nether.window_pane = BlockTypes.ironbars;
			nether.floor_coloured1 = BlockTypes.clay_stained;
			nether.door = BlockTypes.door_iron;
		}
	}
	
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
		public static final Entry clay_hardened 	 = new Entry(Blocks.hardened_clay);
		
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
		public static final EntryColour glass_stained= new EntryColour(Blocks.stained_glass);
		public static final EntryColour wool		 = new EntryColour(Blocks.wool);
		public static final EntryColour clay_stained = new EntryColour(Blocks.stained_hardened_clay);
		
		public static final Entry glass_pane		 = new Entry(Blocks.glass_pane);
		public static final EntryColour glass_pane_stained = new EntryColour(Blocks.stained_glass_pane);
		public static final Entry ironbars			 = new Entry(Blocks.iron_bars);
		
		public static final Entry carpet			 = new Entry(Blocks.carpet, MetaType.COLOURED);
		
		public static final Entry cobweb			 = new Entry(Blocks.web);
		
		public static final Entry quartz			 = new Entry(Blocks.quartz_block, 0);
		public static final Entry quartz_chiselled	 = new Entry(Blocks.quartz_block, 1);
		public static final Entry quartz_pillar		 = new Entry(Blocks.quartz_block, MetaType.PILLAR);
		
		public static final Entry bookshelf			 = new Entry(Blocks.bookshelf);
		public static final Entry glowstone			 = new Entry(Blocks.glowstone);
		public static final Entry glowstone_lamp	 = new Entry(Blocks.redstone_lamp);
		
		public static final Entry torch				 = new Entry(Blocks.torch);
		
		public static final Entry ladder			 = new Entry(Blocks.ladder, MetaType.WALLOBJECT);
		public static final Entry sign				 = new Entry(Blocks.wall_sign, MetaType.WALLOBJECT);
		
		public static final Entry haybale 			 = new Entry(Blocks.hay_block, MetaType.LOG);
		
		public static final Entry bed				 = new Entry(Blocks.bed, MetaType.BED);
		
		// doors
		public static final Entry door_wood			 = new Entry(Blocks.wooden_door, MetaType.DOOR);
		public static final Entry door_iron			 = new Entry(Blocks.iron_door, MetaType.DOOR);
	}
	
	public static class Entry {
		protected final Block block;
		protected final int meta;
		protected final MetaType metatype;
		//private final int variants;
		
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
			
			/*List<ItemStack> list = new ArrayList<ItemStack>();
			this.block.getSubBlocks(null, null, list);
			
			this.variants = list.size();*/
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
						return this.meta + (offset == 1 ? 4 : 8);
					} else {
						return this.meta + (offset == 1 ? 8 : 4);
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
			// doors
			else if (this.metatype == MetaType.DOOR) {
				return (rotation + offset + 5) % 4;
			}
			// beds
			else if (this.metatype == MetaType.BED) {
				return (rotation + offset + 2) % 4;
			}
			
			// default
			return this.meta;
		}
		
	}
	
	public static class EntryColour extends Entry {

		protected Entry[] colours;
		
		public EntryColour(Block block) {
			this(block, MetaType.STANDARD);
		}
		
		public EntryColour(Block block, MetaType type) {
			super(block, MetaType.COLOURED);
			
			colours = new Entry[16];
			
			for(int i=0; i<16; i++) {
				colours[i] = new Entry(this.block, i, type);
			}
		}
		
		public Entry getColour(int colour) {
			return this.colours[colour];
		}
		
		@Override
		public int getMeta(int rotation, boolean flipped, int offset, int colour) { 
			return this.colours[colour].getMeta(rotation, flipped, offset, colour);
		}
	}
	
	public static enum MetaType {
		STANDARD,
		COLOURED,
		STAIRS,
		LOG,
		PILLAR,
		SLAB,
		WALLOBJECT,
		DOOR,
		BED
	}
	
	public static void placeBlock(World world, int x, int y, int z, Entry block, int meta) {
		// doors
		if (block.metatype == MetaType.DOOR) {
			ItemDoor.placeDoorBlock(world, x, y, z, meta, block.getBlock());
			return;
		}
		// beds
		else if (block.metatype == MetaType.BED) {
			byte ox = 0;
            byte oz = 0;
            if (meta == 0) { oz =  1; }
            if (meta == 1) { ox = -1; }
            if (meta == 2) { oz = -1; }
            if (meta == 3) { ox =  1; }
			
			world.setBlock(x, y, z, block.getBlock(), meta, 2);
			world.setBlock(x+ox, y, z+oz, block.getBlock(), meta+8, 2);
			return;
		}
		
		// default
		world.setBlock(x, y, z, block.getBlock(), meta, 2);
	}
}
