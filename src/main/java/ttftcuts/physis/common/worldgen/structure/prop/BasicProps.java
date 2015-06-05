package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.StructureGenerator.StructurePiece;

public abstract class BasicProps {

	public static class RoomCornerFiller extends PropType {
		public RoomCornerFiller() {
			super("cflr");
		}
		
		@Override
		public StructureBoundingBox getBoundingBoxForProp(Prop prop) {
			int height = prop.extraData.get("h");
			return new StructureBoundingBox(0, 0, 0, 0, height-1, 0);
		}
		
		@Override
		public void buildProp(StructurePiece component, Prop prop, World world, StructureBoundingBox limit, Random rand) {
			BlockPalette p = component.blueprintNode.palette;
			int height = prop.extraData.get("h");
			
			prop.fillBlocks(world, limit, component, 0, 0, 0, 0, height-1, 0, p.foundation, 0);
		}
	}
}
