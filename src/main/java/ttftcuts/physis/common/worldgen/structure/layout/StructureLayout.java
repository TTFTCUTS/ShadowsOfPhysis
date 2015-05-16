package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.gen.structure.StructureComponent;

public class StructureLayout {
	int x;
	int y;
	int z;
	
	List<LayoutNode> nodes;
	
	public StructureLayout(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.generate();
	}
	
	protected void generate() {
		this.nodes = new ArrayList<LayoutNode>();
		
		int o = 5;
		this.nodes.add(new LayoutNode(this.x-o, this.y, this.z-o));
		this.nodes.add(new LayoutNode(this.x+o, this.y, this.z-o));
		this.nodes.add(new LayoutNode(this.x-o, this.y, this.z+o));
		this.nodes.add(new LayoutNode(this.x+o, this.y, this.z+o));
	}
	
	public List<StructureComponent> exportToStructureParts(Random rand) {
		List<StructureComponent> parts = new ArrayList<StructureComponent>();
		
		for (int i=0; i<nodes.size(); i++) {
			parts.add(nodes.get(i).getComponent(i, rand));
		}
		
		return parts;
	}
}
