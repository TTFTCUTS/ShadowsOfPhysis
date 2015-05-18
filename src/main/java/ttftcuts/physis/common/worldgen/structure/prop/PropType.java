package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;


public class PropType {
	public final int id;
	
	public static final StructureBoundingBox defaultBounds = new StructureBoundingBox(0,0,0,0,0,0);
	
	public StructureBoundingBox bounds = defaultBounds;
	
	public PropType() {
		this.id = PropTypes.propTypes.size();
		PropTypes.propTypes.add(this);
	}
	
	public StructureBoundingBox getBoundingBoxForProp(Prop prop) {
		return new StructureBoundingBox(-1, 0, -1, 2, 9, 2);
	}
	
	public void buildProp(ComponentSiteRoom component, Prop prop, World world, StructureBoundingBox limit, Random rand) {
		//BlockPalette p = BlockPalette.defaultPalette;
		/*int top = 9;
		for (int i=0; i<=top; i++) {
			prop.placeBlock(world, limit, component, 0, i, 0, BlockPalette.BlockTypes.log_oak, 0);
		}
		prop.placeBlock(world, limit, component, 1, 6, 0, BlockPalette.BlockTypes.log_oak, 2);
		prop.placeBlock(world, limit, component, 2, 6, 0, BlockPalette.BlockTypes.log_oak, 2);
		
		prop.placeBlock(world, limit, component, 0, 6, 1, BlockPalette.BlockTypes.log_oak, 1);
		prop.placeBlock(world, limit, component, 0, 6, 2, BlockPalette.BlockTypes.log_oak, 1);
		
		prop.placeBlock(world, limit, component, -1, 3, 0, BlockPalette.BlockTypes.sign, 1);
		prop.placeBlock(world, limit, component, 0, 2, -1, BlockPalette.BlockTypes.ladder, 2);
		prop.placeBlock(world, limit, component, 0, 3, -1, BlockPalette.BlockTypes.ladder, 2);
		prop.placeBlock(world, limit, component, 0, 4, -1, BlockPalette.BlockTypes.ladder, 2);
		
		prop.placeBlock(world, limit, component, -1, 0, -1, BlockPalette.BlockTypes.stairs_cobble, 3);
		prop.placeBlock(world, limit, component, -1, 0, 0, BlockPalette.BlockTypes.stairs_cobble, 3);
		prop.placeBlock(world, limit, component, -1, 0, 1, BlockPalette.BlockTypes.stairs_cobble, 3);
		prop.placeBlock(world, limit, component, 1, 0, -1, BlockPalette.BlockTypes.stairs_cobble, 1);
		prop.placeBlock(world, limit, component, 1, 0, 0, BlockPalette.BlockTypes.stairs_cobble, 1);
		prop.placeBlock(world, limit, component, 1, 0, 1, BlockPalette.BlockTypes.stairs_cobble, 1);
		prop.placeBlock(world, limit, component, 0, 0, -1, BlockPalette.BlockTypes.stairs_cobble, 0);
		prop.placeBlock(world, limit, component, 0, 0, 1, BlockPalette.BlockTypes.stairs_cobble, 2);
		
		prop.placeBlock(world, limit, component, -1, top, -1, BlockPalette.BlockTypes.stairs_cobble, 7);
		prop.placeBlock(world, limit, component, -1, top, 0, BlockPalette.BlockTypes.stairs_cobble, 7);
		prop.placeBlock(world, limit, component, -1, top, 1, BlockPalette.BlockTypes.stairs_cobble, 7);
		prop.placeBlock(world, limit, component, 1, top, -1, BlockPalette.BlockTypes.stairs_cobble, 5);
		prop.placeBlock(world, limit, component, 1, top, 0, BlockPalette.BlockTypes.stairs_cobble, 5);
		prop.placeBlock(world, limit, component, 1, top, 1, BlockPalette.BlockTypes.stairs_cobble, 5);
		prop.placeBlock(world, limit, component, 0, top, -1, BlockPalette.BlockTypes.stairs_cobble, 4);
		prop.placeBlock(world, limit, component, 0, top, 1, BlockPalette.BlockTypes.stairs_cobble, 6);*/
	}
}
