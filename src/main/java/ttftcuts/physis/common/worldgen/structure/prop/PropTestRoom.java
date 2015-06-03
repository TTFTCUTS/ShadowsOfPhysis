package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import ttftcuts.physis.Physis;
import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.StructureGenerator.StructurePiece;

public class PropTestRoom extends PropType {
	public PropTestRoom() {
		super("testroom");
	}

	@Override
	public StructureBoundingBox getBoundingBoxForProp(Prop prop) {
		int dx = prop.extraData.get("dx");
		int dy = prop.extraData.get("dy");
		int dz = prop.extraData.get("dz");
		return new StructureBoundingBox(0, 0, 0, dx, dy, dz);
	}
	
	@Override
	public void buildProp(StructurePiece component, Prop prop, World world, StructureBoundingBox limit, Random rand) {
		BlockPalette p = component.blueprintNode.palette;
		int dx = prop.extraData.get("dx");
		int dy = prop.extraData.get("dy");
		int dz = prop.extraData.get("dz");
		
		prop.fillBlocks(world, limit, component, 0, 0, 0, dx, 0, dz, p.foundation, 0);
		
		//prop.fillBlocks(world, limit, component, 0, dy, 0, dx, dy, dz, p.foundation, 0);
	}
}
