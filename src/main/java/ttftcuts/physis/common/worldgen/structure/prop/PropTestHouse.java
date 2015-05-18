package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.Random;

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
		BlockPalette p = BlockPalette.defaultPalette;
		
		for (int x=-10; x<=10; x++) {
			for (int z=-10; z<=10; z++) {
				prop.placeBlock(world, limit, component, x, 0, z, p.test, 0);
			}
		}
	}
}
