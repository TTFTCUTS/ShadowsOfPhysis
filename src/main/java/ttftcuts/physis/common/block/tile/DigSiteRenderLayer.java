package ttftcuts.physis.common.block.tile;

public class DigSiteRenderLayer {

	public int colour;
	
	public int[] shapes = {-1,-1,-1,-1,-1,-1};
	public int[] positions = {0,0,0,0,0,0};
	
	public DigSiteRenderLayer(int colour) {
		this.colour = colour;
	}
	
	public void setSymbol(int face, int shape, int position) {
		this.shapes[face] = shape;
		this.positions[face] = position;
	}
	
	@Override
	public String toString() {
		String[] cstring = {"Red", "Green", "Blue"};
		String[] sstring = {"Square", "Triangle", "Circle"};
		
		String s = "RLayer: "+ cstring[this.colour] +" (";
		for(int i=0; i<6; i++) {
			if (shapes[i] != -1) {
				s+=" ["+sstring[shapes[i]]+"@"+ positions[i] +"]";
			} else {
				s+=" [EMPTY]";
			}
		}
		s += " )";
		
		return s;
	}
}
