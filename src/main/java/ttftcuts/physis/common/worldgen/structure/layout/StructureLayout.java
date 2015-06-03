package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.StructureGenerator.StructurePiece;
import ttftcuts.physis.common.worldgen.structure.layout.LayoutGrid.Cell;
import ttftcuts.physis.common.worldgen.structure.layout.LayoutGrid.Room;

public class StructureLayout {
	int x;
	int y;
	int z;
	
	List<LayoutNode> nodes;
	
	public StructureLayout(int x, int y, int z, Random rand) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.generate(rand);
	}
	
	protected void generate(Random rand) {
		this.nodes = new ArrayList<LayoutNode>();
		
		LayoutGrid grid = new LayoutGrid(8, 8);
		
		Cell origin = grid.put(0,0);
		
		double range = grid.gridrange * 0.8;
		
		int blobs = 10;
		
		for (int i=0; i<blobs; i++) {
			double r = rand.nextDouble()*(rand.nextDouble()*0.5 + 0.5)*range;
			double a = rand.nextDouble()*2*Math.PI;
			
			int x = (int) Math.round(Math.sin(a)*r);
			int y = (int) Math.round(Math.cos(a)*r);
			
			int xsize = rand.nextInt(4)+3;
			int ysize = rand.nextInt(4)+3;
			
			int xmin = (int) (x-Math.floor(xsize/2));
			int xmax = (int) (x+Math.ceil(xsize/2));
			int ymin = (int) (y-Math.floor(ysize/2));
            int ymax = (int) (y+Math.ceil(ysize/2));
			for(int dx = xmin;dx<=xmax; dx++) {
				for(int dy = ymin;dy<=ymax; dy++) {
    				grid.put(dx, dy);
    				grid.put(-dx, -dy);
    				grid.put(dx, -dy);
    				grid.put(-dx, dy);
    			}
			}
		}
		
		LayoutGraph graph = grid.buildGraph();
		
		graph.tree(origin.node, rand);
		
		for(int x=0; x<grid.celllength; x++) {
			for(int y=0; y<grid.celllength; y++) {
				Cell c = grid.cells[x][y];
				if (c != null && c.node != null && !c.node.visited) {
					grid.clearCell(c);
				}
			}
		}
		
		Cell ecell = grid.findEntry();
		if (ecell != null) {
			graph.tree(ecell.node, rand);
			graph.repopulateEdges(0.6, 2, rand);
			grid.roomify(rand);
		}
		
		BlockPalette palette = BlockPalette.BlockPalettes.defaultPalette;
		
		for (Room r : grid.rooms) {
			int rxmin = this.x + (r.xmin * grid.gridsize) +1;
			int rymin = this.y;
			int rzmin = this.z + (r.ymin * grid.gridsize) +1;
			int rxmax = this.x + ((r.xmax+1) * grid.gridsize) -1;
			int rymax = this.y + 10;
			int rzmax = this.z + ((r.ymax+1) * grid.gridsize) -1;
			
			//Physis.logger.info("room node: "+r.xmin+"-"+r.xmax+","+r.ymin+"-"+r.ymax+" ---> "+rxmin+"-"+rxmax+","+rzmin+"-"+rzmax);
			
			this.nodes.add(new LayoutNode(rxmin, rymin, rzmin, rxmax, rymax, rzmax, palette).placeProps());
		}
	}
	
	public List<StructurePiece> exportToStructurePieces(Random rand) {
		List<StructurePiece> parts = new ArrayList<StructurePiece>();
		
		for (int i=0; i<nodes.size(); i++) {
			parts.add(nodes.get(i).getPiece(rand));
		}
		
		return parts;
	}
}
