package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;

public class PropTestHouse extends PropType {
	
	@Override
	public StructureBoundingBox getBoundingBoxForProp(Prop prop) {
		return new StructureBoundingBox(-10, 0, -10, 10, 20, 10);
	}
	
	@Override
	public void buildProp(ComponentSiteRoom component, Prop prop, World world, StructureBoundingBox limit, Random rand) {
		BlockPalette p = component.blueprintNode.palette;
		
		// initial clear
		prop.clearFill(world, limit, component, -3, 1,-4, 3, 11, 3, false);
		prop.clearFill(world, limit, component, -3, 1, 4, 3,  6, 7, false);
		prop.clearFill(world, limit, component,  4, 1,-4, 8,  5, 3, false);
		prop.clearFill(world, limit, component,-10, 1,-2,-4,  4, 2, false);
		prop.clearFill(world, limit, component,-10, 5,-2,-6, 17, 2, false);
		
		// base floor
		prop.fillBlocks(world, limit, component,-10, 0,-10, 10, 0, 10, Blocks.grass, 0);

		prop.fillBlocks(world, limit, component, -3, 1,-4, 3, 1, 6, p.foundation, 0);
		prop.fillBlocks(world, limit, component,  4, 1,-4, 8, 1, 3, p.foundation, 0);
		prop.fillBlocks(world, limit, component,-10, 1,-2,-4, 1, 2, p.foundation, 0);
		
		// indoor floors
		prop.fillBlocks(world, limit, component,-2, 1,-3, 2, 1, 2, p.floor1, 0);
		prop.fillBlocks(world, limit, component, 4, 1,-3, 7, 1, 2, p.floor1, 0);
		
		prop.fillBlocks(world, limit, component,-1, 1,-1, 1, 1, 1, p.floor_coloured1, 0, 14);
		
		// outside wood floors
		prop.fillBlocks(world, limit, component,-2, 1, 4, 2, 1, 5, p.floor2, 0);
		prop.fillBlocks(world, limit, component,-5, 1,-1,-4, 1, 1, p.floor2, 0);
		
		prop.fillBlocks(world, limit, component,-1, 1, 6, 1, 1, 6, p.stairs2, 2);
		
		// wall edges
		prop.fillBlocks(world, limit, component,-3, 2, 3, 8, 2, 3, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-3, 2,-4, 8, 2,-4, p.wall_trim1, 0);
		
		prop.fillBlocks(world, limit, component,-3, 2,-3,-3, 2, 2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, 3, 2,-3, 3, 2, 2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, 8, 2,-3, 8, 2, 2, p.wall_trim1, 0);
		
		// ground floor dark trim
		prop.fillBlocks(world, limit, component,-2, 3, 3, 7, 3, 3, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component,-2, 3,-4, 7, 3,-4, p.wall_trim2, 0);
		
		prop.fillBlocks(world, limit, component,-3, 3,-3,-3, 3, 2, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component, 8, 3,-3, 8, 3, 2, p.wall_trim2, 0);
		
		// ground floor wood
		prop.fillBlocks(world, limit, component,-2, 4, 3, 7, 5, 3, p.wall1, 0);
		prop.fillBlocks(world, limit, component,-2, 4,-4, 7, 5,-4, p.wall1, 0);
		
		prop.fillBlocks(world, limit, component,-3, 4,-3,-3, 5, 2, p.wall1, 0);
		prop.fillBlocks(world, limit, component, 3, 3,-3, 3, 5, 3, p.wall1, 0);
		
		// first floor... floor
		prop.fillBlocks(world, limit, component,-2, 6, 3, 2, 6, 3, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-2, 6,-4, 2, 6,-4, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-3, 6,-3,-3, 6, 2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, 3, 6,-3, 3, 6, 2, p.wall_trim1, 0);
		
		prop.fillBlocks(world, limit, component,-2, 6,-3, 2, 6, 2, p.floor1, 0);
		prop.fillBlocks(world, limit, component, 1, 6,-3, 2, 6,-1, p.wall_trim1, 0);
		
		// first floor walls
		prop.fillBlocks(world, limit, component,-2, 7, 3, 2, 10, 3, p.wall1, 0);
		prop.fillBlocks(world, limit, component,-2, 7,-4, 2, 10,-4, p.wall1, 0);
		prop.fillBlocks(world, limit, component,-3, 7,-3,-3,  9, 2, p.wall1, 0);
		prop.fillBlocks(world, limit, component, 3, 7,-3, 3,  9, 2, p.wall1, 0);
		
		prop.fillBlocks(world, limit, component,-1, 8, 3, 1, 11, 3, p.wall1, 0);
		prop.fillBlocks(world, limit, component,-1, 8,-4, 1, 11,-4, p.wall1, 0);
		prop.placeBlock(world, limit, component, 0, 12, 3, p.wall1, 0);
		prop.placeBlock(world, limit, component, 0, 12,-4, p.wall1, 0);
		
		prop.fillBlocks(world, limit, component,-2, 8, 3, 2, 8, 3, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component,-2, 8,-4, 2, 8,-4, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component,-3, 8,-3,-3, 8, 2, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component, 3, 8,-3, 3, 8, 2, p.wall_trim2, 0);
		
		//corner verticals
		prop.fillBlocks(world, limit, component,-3, 3, 3,-3, 9, 3, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-3, 3,-4,-3, 9,-4, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, 3, 3, 3, 3, 9, 3, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, 3, 3,-4, 3, 9,-4, p.wall_trim1, 0);
		
		prop.placeBlock(world, limit, component, 8, 3, 3, p.wall_trim1, 0);
		prop.placeBlock(world, limit, component, 8, 3,-4, p.wall_trim1, 0);
		
		// door frames
		prop.fillBlocks(world, limit, component,-1, 3, 3, 1, 4, 3, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-3, 3,-1,-3, 4, 1, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, 3, 3,-1, 3, 4, 1, p.wall_trim1, 0);
		
		// doors and ladder
		prop.fillBlocks(world, limit, component, 2, 2,-2, 2, 8,-2, p.ladder, 1);
		
		prop.clearFill(world, limit, component, 3, 2, 0, 3, 3, 0, false);
		
		prop.placeBlock(world, limit, component, 0, 2, 3, p.door, 2);
		prop.placeBlock(world, limit, component,-3, 2, 0, p.door, 3);
		
		// windows
		prop.placeBlock(world, limit, component,-2, 3, 3, p.window_pane, 0);
		prop.placeBlock(world, limit, component, 2, 3, 3, p.window_pane, 0);
		
		prop.fillBlocks(world, limit, component,-1, 3,-4, 1, 4,-4, p.window_pane, 0);
		prop.fillBlocks(world, limit, component, 5, 3, 3, 6, 4, 3, p.window_pane, 0);
		
		prop.fillBlocks(world, limit, component,-1, 8,-4, 1, 9,-4, p.window_pane, 0);
		prop.fillBlocks(world, limit, component,-1, 8, 3, 1, 9, 3, p.window_pane, 0);
		
		// fence
		prop.fillBlocks(world, limit, component, -9, 2,-2, -4, 2,-2, p.fence, 0);
		prop.fillBlocks(world, limit, component, -9, 2, 2, -4, 2, 2, p.fence, 0);
		prop.fillBlocks(world, limit, component,-10, 2,-1,-10, 2, 1, p.fence, 0);
		
		prop.fillBlocks(world, limit, component,-3, 2, 4,-3, 2, 5, p.fence, 0);
		prop.fillBlocks(world, limit, component, 3, 2, 4, 3, 2, 5, p.fence, 0);
		prop.placeBlock(world, limit, component,-2, 2, 6, p.fence, 0);
		prop.placeBlock(world, limit, component, 2, 2, 6, p.fence, 0);
		
		// pillars
		prop.fillBlocks(world, limit, component,-3, 2, 6,-3, 4, 6, p.pillar1, 0);
		prop.fillBlocks(world, limit, component, 3, 2, 6, 3, 4, 6, p.pillar1, 0);
		
		prop.fillBlocks(world, limit, component,-10, 2, 2,-10, 4, 2, p.pillar1, 0);
		prop.fillBlocks(world, limit, component,-10, 2,-2,-10, 4,-2, p.pillar1, 0);
		prop.fillBlocks(world, limit, component, -6, 2, 2, -6, 4, 2, p.pillar1, 0);
		prop.fillBlocks(world, limit, component, -6, 2,-2, -6, 4,-2, p.pillar1, 0);
		
		// main roof
		prop.fillBlocks(world, limit, component,-4,  9,-5,-4,  9, 4, p.roof_stair1, 3);
		prop.fillBlocks(world, limit, component,-3, 10,-5,-3, 10, 4, p.roof_stair1, 3);
		prop.fillBlocks(world, limit, component,-2, 11,-5,-2, 11, 4, p.roof_stair1, 3);
		prop.fillBlocks(world, limit, component,-1, 12,-5,-1, 12, 4, p.roof_stair1, 3);
		
		prop.fillBlocks(world, limit, component, 4,  9,-5, 4,  9, 4, p.roof_stair1, 1);
		prop.fillBlocks(world, limit, component, 3, 10,-5, 3, 10, 4, p.roof_stair1, 1);
		prop.fillBlocks(world, limit, component, 2, 11,-5, 2, 11, 4, p.roof_stair1, 1);
		prop.fillBlocks(world, limit, component, 1, 12,-5, 1, 12, 4, p.roof_stair1, 1);
		
		prop.fillBlocks(world, limit, component, 0, 13,-5, 0, 13, 4, p.roof_slab1, 0);
		
		// main roof edging
		prop.placeBlock(world, limit, component,-3,  9,-5, p.roof_stair1, 5);
		prop.placeBlock(world, limit, component,-2, 10,-5, p.roof_stair1, 5);
		prop.placeBlock(world, limit, component,-1, 11,-5, p.roof_stair1, 5);
		prop.placeBlock(world, limit, component,-3,  9, 4, p.roof_stair1, 5);
		prop.placeBlock(world, limit, component,-2, 10, 4, p.roof_stair1, 5);
		prop.placeBlock(world, limit, component,-1, 11, 4, p.roof_stair1, 5);
		
		prop.placeBlock(world, limit, component, 3,  9,-5, p.roof_stair1, 7);
		prop.placeBlock(world, limit, component, 2, 10,-5, p.roof_stair1, 7);
		prop.placeBlock(world, limit, component, 1, 11,-5, p.roof_stair1, 7);
		prop.placeBlock(world, limit, component, 3,  9, 4, p.roof_stair1, 7);
		prop.placeBlock(world, limit, component, 2, 10, 4, p.roof_stair1, 7);
		prop.placeBlock(world, limit, component, 1, 11, 4, p.roof_stair1, 7);
		
		prop.placeBlock(world, limit, component, 0, 12,-5, p.roof_block1, 0);
		prop.placeBlock(world, limit, component, 0, 12, 4, p.roof_block1, 0);
		
		// main roof interior
		prop.fillBlocks(world, limit, component, 0, 12,-3, 0, 12, 2, p.roof_block2, 0);
		
		prop.fillBlocks(world, limit, component,-1, 11,-3,-1, 11, 2, p.roof_stair2, 5);
		prop.fillBlocks(world, limit, component,-2, 10,-3,-2, 10, 2, p.roof_stair2, 5);
		
		prop.fillBlocks(world, limit, component, 1, 11,-3, 1, 11, 2, p.roof_stair2, 7);
		prop.fillBlocks(world, limit, component, 2, 10,-3, 2, 10, 2, p.roof_stair2, 7);
		
		// side roof
		prop.fillBlocks(world, limit, component, 4, 6,-5, 5, 6, 4, p.roof_block2, 0);
		prop.fillBlocks(world, limit, component, 6, 6,-5, 6, 6, 4, p.roof_slab2, 0);
		prop.fillBlocks(world, limit, component, 7, 5,-5, 7, 5, 4, p.roof_stair2, 1);
		prop.fillBlocks(world, limit, component, 8, 4,-5, 8, 4, 4, p.roof_stair2, 1);
		prop.fillBlocks(world, limit, component, 9, 3,-5, 9, 3, 4, p.roof_slab2, 1);
		
		prop.fillBlocks(world, limit, component, 6, 5,-3, 6, 5, 2, p.roof_stair2, 7);
		prop.fillBlocks(world, limit, component, 7, 4,-3, 7, 4, 2, p.roof_stair2, 7);
		
		prop.placeBlock(world, limit, component, 6, 5, 4, p.roof_stair2, 7);
		prop.placeBlock(world, limit, component, 6, 5,-5, p.roof_stair2, 7);
		prop.placeBlock(world, limit, component, 7, 4, 4, p.roof_stair2, 7);
		prop.placeBlock(world, limit, component, 7, 4,-5, p.roof_stair2, 7);
		prop.placeBlock(world, limit, component, 8, 3, 4, p.roof_slab2, 1);
		prop.placeBlock(world, limit, component, 8, 3,-5, p.roof_slab2, 1);
		
		prop.placeBlock(world, limit, component, 3, 6,-5, p.roof_slab2, 0);
		
		// porch roof
		prop.fillBlocks(world, limit, component,-3, 6, 4, 3, 6, 4, p.roof_block2, 0);
		prop.fillBlocks(world, limit, component,-3, 6, 5, 3, 6, 5, p.roof_stair2, 2);
		
		prop.fillBlocks(world, limit, component,-3, 5, 6,-2, 5, 6, p.roof_stair2, 2);
		prop.fillBlocks(world, limit, component, 2, 5, 6, 3, 5, 6, p.roof_stair2, 2);
		
		prop.fillBlocks(world, limit, component,-1, 5, 6,-1, 5, 7, p.roof_block2, 0);
		prop.fillBlocks(world, limit, component, 1, 5, 6, 1, 5, 7, p.roof_block2, 0);
		
		prop.fillBlocks(world, limit, component, 0, 6, 6, 0, 6, 7, p.roof_slab2, 0);
		prop.fillBlocks(world, limit, component, 0, 5, 6, 0, 5, 7, p.roof_slab2, 1);
		
		prop.fillBlocks(world, limit, component,-3, 4, 7,-2, 4, 7, p.roof_slab2, 1);
		prop.fillBlocks(world, limit, component, 2, 4, 7, 3, 4, 7, p.roof_slab2, 1);
		prop.placeBlock(world, limit, component,-2, 5, 7, p.roof_slab2, 0);
		prop.placeBlock(world, limit, component, 2, 5, 7, p.roof_slab2, 0);
		
		prop.fillBlocks(world, limit, component,-2, 5, 5, 2, 5, 5, p.roof_slab2, 1);
		prop.placeBlock(world, limit, component,-3, 5, 5, p.roof_stair2, 4);
		prop.placeBlock(world, limit, component, 3, 5, 5, p.roof_stair2, 4);
		prop.placeBlock(world, limit, component,-3, 5, 4, p.roof_stair2, 6);
		prop.placeBlock(world, limit, component, 3, 5, 4, p.roof_stair2, 6);
		
		// silo
		prop.fillBlocks(world, limit, component,-10, 5,-2, -6, 5,-2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-10, 5, 2, -6, 5, 2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-10, 5,-1,-10, 5, 1, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, -6, 5,-1, -6, 5, 1, p.wall_trim1, 0);
		
		prop.fillBlocks(world, limit, component,-10, 16,-2, -6, 16,-2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-10, 16, 2, -6, 16, 2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-10, 16,-1,-10, 16, 1, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, -6, 16,-1, -6, 16, 1, p.wall_trim1, 0);
		
		prop.fillBlocks(world, limit, component,-10, 6,-2,-10, 15,-2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component,-10, 6, 2,-10, 15, 2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, -6, 6,-2, -6, 15,-2, p.wall_trim1, 0);
		prop.fillBlocks(world, limit, component, -6, 6, 2, -6, 15, 2, p.wall_trim1, 0);
		
		prop.fillBlocks(world, limit, component, -9, 6,-2, -7, 15,-2, p.wall1, 0);
		prop.fillBlocks(world, limit, component, -9, 6, 2, -7, 15, 2, p.wall1, 0);
		prop.fillBlocks(world, limit, component,-10, 6,-1,-10, 15, 1, p.wall1, 0);
		prop.fillBlocks(world, limit, component, -6, 6,-1, -6, 15, 1, p.wall1, 0);
		
		prop.fillBlocks(world, limit, component, -9,  8,-2, -7,  8,-2, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component, -9,  8, 2, -7,  8, 2, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component,-10,  8,-1,-10,  8, 1, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component, -6,  8,-1, -6,  8, 1, p.wall_trim2, 0);
		
		prop.fillBlocks(world, limit, component, -9, 13,-2, -7, 13,-2, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component, -9, 13, 2, -7, 13, 2, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component,-10, 13,-1,-10, 13, 1, p.wall_trim2, 0);
		prop.fillBlocks(world, limit, component, -6, 13,-1, -6, 13, 1, p.wall_trim2, 0);
		
		prop.fillBlocks(world, limit, component,-10, 10, 0,-10, 11, 0, p.window_pane, 0);
		prop.fillBlocks(world, limit, component, -8, 10,-2, -8, 11,-2, p.window_pane, 0);
		prop.fillBlocks(world, limit, component, -8, 10, 2, -8, 11, 2, p.window_pane, 0);
		
		prop.fillBlocks(world, limit, component, -9, 16,-1, -7, 16, 1, p.roof_slab2, 0);
		
		prop.placeBlock(world, limit, component, -9, 5, 0, p.foundation, 0);
		prop.placeBlock(world, limit, component, -7, 5, 0, p.foundation, 0);
		prop.placeBlock(world, limit, component, -8, 5, 1, p.foundation, 0);
		prop.placeBlock(world, limit, component, -8, 5,-1, p.foundation, 0);
		
		// torches
		prop.placeBlock(world, limit, component,-10, 17,-2, p.light, 0);
		prop.placeBlock(world, limit, component,-10, 17, 2, p.light, 0);
		prop.placeBlock(world, limit, component, -6, 17,-2, p.light, 0);
		prop.placeBlock(world, limit, component, -6, 17, 2, p.light, 0);
		
		prop.placeBlock(world, limit, component,-4, 3,-1, p.light, 0);
		prop.placeBlock(world, limit, component,-4, 3, 1, p.light, 0);
		
		prop.placeBlock(world, limit, component,-2, 3,-1, p.light, 0);
		prop.placeBlock(world, limit, component, 2, 3, 1, p.light, 0);
		prop.placeBlock(world, limit, component, 4, 3,-1, p.light, 0);
		
		prop.placeBlock(world, limit, component,-1, 3, 4, p.light, 0);
		prop.placeBlock(world, limit, component, 1, 3, 4, p.light, 0);
		
		prop.placeBlock(world, limit, component,-3, 3, 7, p.light, 0);
		prop.placeBlock(world, limit, component, 3, 3, 7, p.light, 0);
		prop.placeBlock(world, limit, component,-1, 6, 7, p.light, 0);
		prop.placeBlock(world, limit, component, 1, 6, 7, p.light, 0);
		
		prop.placeBlock(world, limit, component,-2, 8, 0, p.light, 0);
		prop.placeBlock(world, limit, component, 2, 8, 0, p.light, 0);
		
		prop.placeBlock(world, limit, component,-9, 8, 0, p.light, 0);
		
		prop.placeBlock(world, limit, component, 5, 7, 4, p.light, 0);
		prop.placeBlock(world, limit, component, 5, 7,-5, p.light, 0);
		
		// furniture
		prop.placeBlock(world, limit, component, 0, 7, 1, p.chair_body, 2);
		prop.placeBlock(world, limit, component,-1, 7, 1, p.chair_arm, 1);
		prop.placeBlock(world, limit, component, 1, 7, 1, p.chair_arm, 3);
		
		prop.placeBlock(world, limit, component,-2, 2,-2, p.chair_body, 1);
		prop.fillBlocks(world, limit, component,-2, 2,-3, 1, 2,-3, p.chair_body, 2);
		prop.placeBlock(world, limit, component,-2, 2,-1, p.chair_arm, 0);
		prop.placeBlock(world, limit, component, 2, 2,-3, p.chair_arm, 3);
		
		prop.placeBlock(world, limit, component,-1, 7,-2, p.bed, 3);
		
		prop.placeBlock(world, limit, component, 5, 2, 2, p.goods, 1);
		prop.placeBlock(world, limit, component, 6, 2, 2, p.goods, 0);
		prop.placeBlock(world, limit, component, 7, 2, 2, p.goods, 0);
		prop.placeBlock(world, limit, component, 7, 2, 1, p.goods, 2);
		
		prop.placeBlock(world, limit, component, 5, 2,-3, p.goods, 0);
		prop.placeBlock(world, limit, component, 6, 2,-3, p.goods, 0);
		prop.placeBlock(world, limit, component, 7, 2,-3, p.goods, 0);
		prop.placeBlock(world, limit, component, 6, 2,-2, p.goods, 2);
		prop.placeBlock(world, limit, component, 7, 2,-2, p.goods, 1);
		prop.placeBlock(world, limit, component, 7, 3,-3, p.goods, 2);
	}
}
