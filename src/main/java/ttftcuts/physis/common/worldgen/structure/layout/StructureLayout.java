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
		
		/*int o = 7;
		int s = 10;
		this.nodes.add(new LayoutNode(this.x-o, this.y, this.z-o, this.x-o+s-1, this.y+s-1, this.z-o+s-1));
		this.nodes.add(new LayoutNode(this.x+o, this.y, this.z-o, this.x+o+s-1, this.y+s-1, this.z-o+s-1));
		this.nodes.add(new LayoutNode(this.x-o, this.y, this.z+o, this.x-o+s-1, this.y+s-1, this.z+o+s-1));
		this.nodes.add(new LayoutNode(this.x+o, this.y, this.z+o, this.x+o+s-1, this.y+s-1, this.z+o+s-1));*/
		
		this.nodes.add(new LayoutNode(this.x-10, this.y, this.z-10, this.x+10, this.y+20, this.z+10));
	}
	
	public List<StructureComponent> exportToStructureParts(Random rand) {
		List<StructureComponent> parts = new ArrayList<StructureComponent>();
		
		for (int i=0; i<nodes.size(); i++) {
			parts.add(nodes.get(i).getComponent(i, rand));
		}
		
		return parts;
	}
}
