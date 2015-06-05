package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LayoutGrid {
	public final int gridsize;
	public final int gridrange;
	public final int celllength;
	
	Cell[][] cells;
	List<Room> rooms;	
	
	public LayoutGrid(int chunkrange, int gridsize) {
		this.gridsize = gridsize;
		this.gridrange = (int) Math.floor((chunkrange*16) / gridsize);
		this.celllength = this.gridrange*2+1;
		
		cells = new Cell[celllength][celllength];
		this.rooms = new ArrayList<Room>();
	}
	
	public Cell get(int x, int y) {
		return this.cells[c(x)][c(y)];
	}
	
	public Cell put(int x, int y) {
		if (x < -this.gridrange || x > this.gridrange || y < -gridrange || y > gridrange) { return null; }
		
		Cell cell = this.get(x,y);
		if (cell != null) {
			return cell;
		}
		cell = new Cell(x,y);
		cell.parent = this;
		this.cells[c(x)][c(y)] = cell;
		return cell;
	}
	
	public void clear(int x, int y) {
		Cell cell = this.get(x, y);
		if (cell == null) { return; }
		this.clearCell(cell);
	}
	
	public void clearCell(Cell cell) {
		if (cell.node != null) {
			cell.node.parent.removeNode(cell.node);
		}
		this.cells[c(cell.x)][c(cell.y)] = null;
	}
	
	public int c(int o) {
		return o + gridrange;
	}
	
	public LayoutGraph buildGraph() {
		LayoutGraph graph = new LayoutGraph();
		
		for (int x=0; x<this.celllength; x++) {
			for (int y=0; y<this.celllength; y++) {
				Cell c = this.cells[x][y];
				if (c != null && c.node == null) {
					c.node = graph.addNode((x-gridrange)*gridsize, (y-gridrange)*gridsize);
				}
			}
		}
		
		for (int x=0; x<this.celllength; x++) {
			for (int y=0; y<this.celllength; y++) {
				Cell c = this.cells[x][y];
				if (c == null) { continue; }
				
				if (x > 0) {
					Cell other = this.cells[x-1][y];
					if (other != null) {
						graph.addEdge(c.node, other.node);
					}
				}
				
				if (y > 0) {
					Cell other = this.cells[x][y-1];
					if (other != null) {
						graph.addEdge(c.node, other.node);
					}
				}
			}
		}
		
		return graph;
	}
	
	public Cell findEntry() {
		List<Cell> candidates = new ArrayList<Cell>();
		
		for (int x=0; x<this.celllength; x++) {
			for (int y=0; y<this.celllength; y++) {
				Cell c = this.cells[x][y];
				if (c == null) { continue; }
				
				if (x <= 0 || x >= gridrange*2 || y <= 0 || y >= gridrange*2) {
					candidates.add(c);
					continue;
				}
				
				if (this.cells[x-1][y] == null || this.cells[x+1][y] == null || this.cells[x][y-1] == null || this.cells[x][y+1] == null) {
					candidates.add(c);
				}
			}
		}
		
		Collections.shuffle(candidates);
		
		for (int i=0; i<candidates.size(); i++) {
			Cell cell = candidates.get(i);
			if (cell.x <= 0 || cell.x >= gridrange*2 || cell.y <= 0 || cell.y >= gridrange*2) {
				return cell;
			}
			
			boolean found = false;
			for(int x = cell.x-1; x>=0; x--) {
				if (this.cells[x][cell.y] != null) {
					found = true;
					break;
				}
			}
			if (!found) { return cell; }
			for(int x = cell.x+1; x<=gridrange*2; x++) {
				if (this.cells[x][cell.y] != null) {
					found = true;
					break;
				}
			}
			if (!found) { return cell; }
			for(int y = cell.y-1; y>=0; y--) {
				if (this.cells[cell.x][y] != null) {
					found = true;
					break;
				}
			}
			if (!found) { return cell; }
			for(int y = cell.y+1; y<=gridrange*2; y++) {
				if (this.cells[cell.x][y] != null) {
					found = true;
					break;
				}
			}
			if (!found) { return cell; }
		}
		
		return null;
	}
	
	public void roomify(Random rand) {
		List<Cell> unroomed = new ArrayList<Cell>();
		List<Cell> retry = new ArrayList<Cell>();
		
		for (int x=0; x<this.celllength; x++) {
			for (int y=0; y<this.celllength; y++) {
				Cell c = this.cells[x][y];
				if (c != null) {
					unroomed.add(c);
				}
			}
		}
		
		while (!unroomed.isEmpty()) {
			Cell cell = unroomed.remove(rand.nextInt(unroomed.size()));
			
			boolean done = this.growRoom(cell, unroomed, retry, rand);
			if (!done) {
				retry.add(cell);
			}
		}
		
		unroomed = retry;
		retry = new ArrayList<Cell>();
		
		while (!unroomed.isEmpty()) {
			Cell cell = unroomed.remove(rand.nextInt(unroomed.size()));
			
			boolean done = this.growCorridor(cell, unroomed, retry, rand);
			if (!done) {
				retry.add(cell);
			}
		}
		
		unroomed = retry;
		
		while (!unroomed.isEmpty()) {
			Cell cell = unroomed.remove(rand.nextInt(unroomed.size()));
			
			this.singleRoom(cell, rand);
		}
	}
	
	public boolean growRoom(Cell cell, List<Cell> unroomed, List<Cell> retry, Random rand) {
		int xmin = 0;
		int xmax = 0;
		int ymin = 0;
		int ymax = 0;
		
		int dx = c(cell.x);
		int dy = c(cell.y);
		
		List<String> dirs = new ArrayList<String>();
		
		for (int i=0; i<4; i++) {
			if (cell.connects(i)) {
				dirs.add(Integer.toString(i));
			}
		}
		
		while (!dirs.isEmpty()) {
			String dir = dirs.remove(rand.nextInt(dirs.size()));
			
			//Physis.logger.info("dir: "+dir+", x: "+(dx+xmin)+","+(dx+xmax)+", y: "+(dy+ymin)+","+(dy+ymax));
			
			if (dir.equals("0")) {
				// up
				if(dy+ymin <= 0) {
					dirs.remove("0");
				} else {
					boolean expand = true;
					for (int i=xmin; i<=xmax; i++) {
						Cell c = this.cells[dx+i][dy+ymin-1];
						if (c == null || c.room != null || !c.connectsSouth() || (i<xmax && !c.connectsEast())) {
							expand = false;
							dirs.remove("0");
							break;
						}
					}
					if (expand) {
						ymin--;
					}
				}
			}
			else if (dir.equals("1")) {
				// right
				if(dx+xmax >= gridrange*2) {
					dirs.remove("1");
				} else {
					boolean expand = true;
					for (int i=ymin; i<=ymax; i++) {
						Cell c = this.cells[dx+xmax+1][dy+i];
						if (c == null || c.room != null || !c.connectsWest() || (i<ymax && !c.connectsSouth())) {
							expand = false;
							dirs.remove("1");
							break;
						}
					}
					if (expand) {
						xmax++;
					}
				}
			}
			else if (dir.equals("2")) {
				// down
				if(dy+ymax >= gridrange*2) {
					dirs.remove("2");
				} else {
					boolean expand = true;
					for (int i=xmin; i<=xmax; i++) {
						Cell c = this.cells[dx+i][dy+ymax+1];
						if (c == null || c.room != null || !c.connectsNorth() || (i<xmax && !c.connectsEast())) {
							expand = false;
							dirs.remove("2");
							break;
						}
					}
					if (expand) {
						ymax++;
					}
				}
			}
			else {
				// left
				if(dx+xmin <= 0) {
					dirs.remove("3");
				} else {
					boolean expand = true;
					for (int i=ymin; i<=ymax; i++) {
						Cell c = this.cells[dx+xmin-1][dy+i];
						if (c == null || c.room != null || !c.connectsEast() || (i<ymax && !c.connectsSouth())) {
							expand = false;
							dirs.remove("3");
							break;
						}
					}
					if (expand) {
						xmin--;
					}
				}
			}
		}
		
		int xdiff = xmax-xmin;
		int ydiff = ymax-ymin;
		
		if (xdiff > 0 && ydiff > 0) {
			Room r = new Room("large");
    		
    		for (int x=xmin; x<=xmax; x++) {
    			for (int y=ymin; y<=ymax; y++) {
        			Cell rc = cells[dx+x][dy+y];
        			r.addCell(rc);
        			rc.room = r;
        			unroomed.remove(rc);
        			retry.remove(rc);
        		}
    		}
    		
    		this.rooms.add(r);
			
			return true;
		}
		
		return false;
	}
	
	public boolean growCorridor(Cell cell, List<Cell> unroomed, List<Cell> retry, Random rand) {
		if((cell.connectsWest() && cell.connectsEast()) || (cell.connectsNorth() && cell.connectsSouth())) {
			int xmin = 0;
    		int xmax = 0;
    		int ymin = 0;
    		int ymax = 0;
    		
    		int dx = c(cell.x);
    		int dy = c(cell.y);
			
    		// up
    		while(true) {
    			if (dy+ymin <= 0) { break; }
    			Cell c = this.cells[dx][dy+ymin-1];
    			if (c == null || c.room != null || !c.connectsSouth()) {
    				break;
    			} else {
    				ymin--;
    			}
    		}
    		
    		// right
    		while(true) {
    			if (dx+xmax >= gridrange*2) { break; }
    			Cell c = this.cells[dx+xmax+1][dy];
    			if (c == null || c.room != null || !c.connectsWest()) {
    				break;
    			} else {
    				xmax++;
    			}
    		}
    		// down
    		while(true) {
    			if (dy+ymax >= gridrange*2) { break; }
    			Cell c = this.cells[dx][dy+ymax+1];
    			if (c == null || c.room != null || !c.connectsNorth()) {
    				break;
    			} else {
    				ymax++;
    			}
    		}
    		// left
    		while(true) {
    			if (dx+xmin <= 0) { break; }
    			Cell c = this.cells[dx+xmin-1][dy];
    			if (c == null || c.room != null || !c.connectsEast()) {
    				break;
    			} else {
    				xmin--;
    			}
    		}
    		
    		int xdiff = xmax-xmin;
    		int ydiff = ymax-ymin;
    		int maxdiff = Math.max(xdiff,ydiff);
    		
    		if (maxdiff >= 2) {
	    		if (xdiff > ydiff) {
	    			ymin = 0;
	    			ymax = 0;
	    		} else {
	    			xmin = 0;
	    			xmax = 0;
	    		}
	    		
	    		Room r = new Room("corridor");
	    		
	    		for (int x=xmin; x<=xmax; x++) {
	    			for (int y=ymin; y<=ymax; y++) {
	        			Cell rc = cells[dx+x][dy+y];
	        			r.addCell(rc);
	        			rc.room = r;
	        			unroomed.remove(rc);
	        			retry.remove(rc);
	        		}
	    		}
	    		
	    		this.rooms.add(r);
				
				return true;
    		}
		}
		return false;
	}
	
	public void singleRoom(Cell cell, Random rand) {
		Room r = new Room("single");
		cell.room = r;
		r.addCell(cell);
		this.rooms.add(r);
	}
	
	public static class Cell {
		LayoutGrid parent;
		LayoutGraph.Node node;
		int x;
		int y;
		Room room;
		
		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean connects(int dir) {
			int dx = parent.c(this.x);
			int dy = parent.c(this.y);
			
			Cell other = null;
			if (dir == 0) {
				if (dy <= 0) { return false; }
				other = parent.cells[dx][dy-1];
			}
			else if (dir == 1) {
				if (dx >= parent.gridrange*2) { return false; }
				other = parent.cells[dx+1][dy];
			}
			else if (dir == 2) {
				if (dy >= parent.gridrange*2) { return false; }
				other = parent.cells[dx][dy+1];
			}
			else if (dir == 3) {
				if (dx <= 0) { return false; }
				other = parent.cells[dx-1][dy];
			}
			
			if (other != null) {
				if (this.node.edges.containsKey(other.node) && this.node.edges.get(other.node).active) {
					return true;
				}
			}
			return false;
		}
		
		boolean connectsNorth() { return this.connects(0); }
		boolean connectsEast()  { return this.connects(1); }
		boolean connectsSouth() { return this.connects(2); }
		boolean connectsWest()  { return this.connects(3); }
	}
	
	public static class Room {
		List<Cell> cells;
		List<Door> doors;
		String type;
		int depth = 0;
		
		int xmin = 0;
		int xmax = 0;
		int ymin = 0;
		int ymax = 0;
		
		public Room(String type) {
			this.type = type;
			this.cells = new ArrayList<Cell>();
			this.doors = new ArrayList<Door>();
		}
		
		void addCell(Cell cell) {
			if (this.cells.contains(cell)) { return; }
			if (this.cells.size() == 0) {
				this.xmin = cell.x;
				this.xmax = cell.x;
				this.ymin = cell.y;
				this.ymax = cell.y;
			}
			
			this.cells.add(cell);
			
			this.xmin = Math.min(xmin, cell.x);
			this.xmax = Math.max(xmax, cell.x);
			this.ymin = Math.min(ymin, cell.y);
			this.ymax = Math.max(ymax, cell.y);
		}
		
		void calcDepth() {
			int total = 0;
			for (Cell c : this.cells){
				total += c.node.depth;
			}
			total = (int) Math.floor(total / this.cells.size());
			this.depth = total;
		}
		
		void calcDoors() {
			this.doors.clear();
			
			for (Cell c : this.cells) {
				if (c.x == this.xmin && c.connectsWest()) {
					this.doors.add(new Door(c.x-this.xmin, c.y-this.ymin, 3));
				}
				if (c.x == this.xmax && c.connectsEast()) {
					this.doors.add(new Door(c.x-this.xmin, c.y-this.ymin, 1));
				}
				if (c.y == this.ymin && c.connectsNorth()) {
					this.doors.add(new Door(c.x-this.xmin, c.y-this.ymin, 0));
				}
				if (c.y == this.ymax && c.connectsSouth()) {
					this.doors.add(new Door(c.x-this.xmin, c.y-this.ymin, 2));
				}
			}
		}
		
		public static class Door {
			public final int x;
			public final int y;
			public final int dir;
			
			public Door(int x, int y, int dir) {
				this.x = x;
				this.y = y;
				this.dir = dir;
			}
		}
	}
}
