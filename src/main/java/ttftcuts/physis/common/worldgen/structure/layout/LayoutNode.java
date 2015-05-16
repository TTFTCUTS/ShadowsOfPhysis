package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;
import net.minecraft.world.gen.structure.StructureComponent;

public class LayoutNode {
	int x;
	int y;
	int z;
	
	public LayoutNode(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public StructureComponent getComponent(int id, Random rand) {
		return new ComponentSiteRoom(id, rand, this.x, this.y, this.z, 5,5,5);
	}
}
