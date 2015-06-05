package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.StructureGenerator.StructurePiece;

public abstract class BastionProps {

	public static class InnerWall extends PropType {
		public InnerWall() {
			super("BstInWall");
		}
		
		@Override
		public StructureBoundingBox getBoundingBoxForProp(Prop prop) {
			int length = prop.extraData.get("l");
			int height = prop.extraData.get("h");
			return new StructureBoundingBox(0, 0, 0, 1, height-1, length -1);
		}
		
		@Override
		public void buildProp(StructurePiece component, Prop prop, World world, StructureBoundingBox limit, Random rand) {
			BlockPalette p = component.blueprintNode.palette;
			int length = prop.extraData.get("l");
			int height = prop.extraData.get("h");
			
			prop.fillBlocks(world, limit, component, 0, 0, 0, 0, height-1, length-1, p.wall1, 0);
			prop.fillBlocks(world, limit, component, 1, height-1, 0, 1, height-1, length-1, p.roof_block1, 0);
			prop.fillBlocks(world, limit, component, 1, 0, 0, 1, 0, length-1, p.roof_stair1, 1);
		}
	}
	
	public static class Doorway extends PropType {
		public Doorway() {
			super("BstDrwy");
		}
		
		@Override
		public StructureBoundingBox getBoundingBoxForProp(Prop prop) {
			return new StructureBoundingBox(-2, 0, 0, 2, 3, 1);
		}
		
		@Override
		public void buildProp(StructurePiece component, Prop prop, World world, StructureBoundingBox limit, Random rand) {
			BlockPalette p = component.blueprintNode.palette;
			
			prop.fillBlocks(world, limit, component, -2, 0, 0, 2, 3, 0, p.roof_block1, 0);
			prop.clearFill(world, limit, component, -1, 0, 0, 1, 2, 1, false);
			prop.placeBlock(world, limit, component, -2, 0, 1, p.roof_stair1, 2);
			prop.placeBlock(world, limit, component, 2, 0, 1, p.roof_stair1, 2);
		}
	}
}
