package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.BlockPalette.BlockPalettes;
import ttftcuts.physis.common.worldgen.structure.StructureGenerator.StructurePiece;

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

		int len = BlockPalettes.paletteRegistry.size();
		int spread = 10;
		int size = 20;
		int width = len * size + (len-1) * spread;
		
		Iterator<BlockPalette> iter = BlockPalettes.paletteRegistry.values().iterator();
		
		for (int i=0; i<len; i++) {
			int placex = this.x - (width/2) + i * (spread+size);
			
			BlockPalette pal = iter.next();
			
			this.nodes.add(new LayoutNode(placex, this.y, this.z-10, placex+size, this.y+20, this.z+10, pal).placeProps());
		}
		
		//this.nodes.add(new LayoutNode(this.x-10, this.y, this.z-10, this.x+10, this.y+20, this.z+10, p));
		
		//this.nodes.add(new LayoutNode(this.x-10 - 30, this.y, this.z-10, this.x+10 - 30, this.y+20, this.z+10, p1));
		//this.nodes.add(new LayoutNode(this.x-10 + 30, this.y, this.z-10, this.x+10 + 30, this.y+20, this.z+10, p));
	}
	
	public List<StructurePiece> exportToStructurePieces(Random rand) {
		List<StructurePiece> parts = new ArrayList<StructurePiece>();
		
		for (int i=0; i<nodes.size(); i++) {
			parts.add(nodes.get(i).getPiece(rand));
		}
		
		return parts;
	}
}
