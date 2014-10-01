package ttftcuts.physis.puzzle.oddoneout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ttftcuts.physis.utils.TPair;

public class OddOneOutOption {
	public List<TPair<Integer>> symbols; 
	public Map<OddOneOutProperty, Integer> properties;
	
	public OddOneOutOption(Object... args) {
		this.properties = new HashMap<OddOneOutProperty, Integer>();
		this.symbols = new ArrayList<TPair<Integer>>();
	}
	
	public void addSymbol(TPair<Integer> symbol) {
		this.symbols.add(symbol);
	}
	
	public void calculateProperties() {
		this.setOrAddProperty(OddOneOutProperty.numSymbols, symbols.size());
		
		Set<Integer> col = new HashSet<Integer>();
		Set<Integer> shapes = new HashSet<Integer>();
		
		for(TPair<Integer> s : symbols) {
			if (!col.contains(s.val1)) { col.add(s.val1); }
			if (!shapes.contains(s.val2)) { shapes.add(s.val2); }
			
			if (s.val1 == 0) {
				this.setOrAddProperty(OddOneOutProperty.red, 1);
				if (s.val2 == 0) {
					this.setOrAddProperty(OddOneOutProperty.squares, 1);
					this.setOrAddProperty(OddOneOutProperty.redSquares, 1);
				} else if (s.val2 == 1) {
					this.setOrAddProperty(OddOneOutProperty.triangles, 1);
					this.setOrAddProperty(OddOneOutProperty.redTriangles, 1);
				} else if (s.val2 == 2) {
					this.setOrAddProperty(OddOneOutProperty.circles, 1);
					this.setOrAddProperty(OddOneOutProperty.redCircles, 1);
				}
			} else if (s.val1 == 1) {
				this.setOrAddProperty(OddOneOutProperty.green, 1);
				if (s.val2 == 0) {
					this.setOrAddProperty(OddOneOutProperty.squares, 1);
					this.setOrAddProperty(OddOneOutProperty.greenSquares, 1);
				} else if (s.val2 == 1) {
					this.setOrAddProperty(OddOneOutProperty.triangles, 1);
					this.setOrAddProperty(OddOneOutProperty.greenTriangles, 1);
				} else if (s.val2 == 2) {
					this.setOrAddProperty(OddOneOutProperty.circles, 1);
					this.setOrAddProperty(OddOneOutProperty.greenCircles, 1);
				}
			} else if (s.val1 == 2) {
				this.setOrAddProperty(OddOneOutProperty.blue, 1);
				if (s.val2 == 0) {
					this.setOrAddProperty(OddOneOutProperty.squares, 1);
					this.setOrAddProperty(OddOneOutProperty.blueSquares, 1);
				} else if (s.val2 == 1) {
					this.setOrAddProperty(OddOneOutProperty.triangles, 1);
					this.setOrAddProperty(OddOneOutProperty.blueTriangles, 1);
				} else if (s.val2 == 2) {
					this.setOrAddProperty(OddOneOutProperty.circles, 1);
					this.setOrAddProperty(OddOneOutProperty.blueCircles, 1);
				}
			}
		}
		
		this.setOrAddProperty(OddOneOutProperty.numColours, col.size());
		this.setOrAddProperty(OddOneOutProperty.numShapes, shapes.size());
		
		for (OddOneOutProperty p : OddOneOutProperty.propertyList) {
			this.setOrAddProperty(p, 0);
		}
	}
	
	private void setOrAddProperty(OddOneOutProperty property, int increment) {
		if (this.properties.containsKey(property)) {
			this.properties.put(property, this.properties.get(property) + increment);
		} else {
			this.properties.put(property, increment);
		}
	}
	
	@Override
	public String toString() {
		String[] col = {"Red", "Green", "Blue"};
		String[] shape = {"Square", "Triangle", "Circle"};
		
		String s = "Option( ";
		
		for(TPair<Integer> symbol : symbols) {
			s += "[" + col[symbol.val1] + " " + shape[symbol.val2] + "] ";
		}
		
		s += ")";
		
		return s;
	}
}
