package ttftcuts.physis.puzzle.oddoneout;

import java.util.ArrayList;
import java.util.List;

public class OddOneOutProperty {

	public static List<OddOneOutProperty> propertyList = new ArrayList<OddOneOutProperty>();
	
	public static OddOneOutProperty numSymbols;
	public static OddOneOutProperty numColours;
	public static OddOneOutProperty numShapes;
	
	public static OddOneOutProperty squares;
	public static OddOneOutProperty triangles;
	public static OddOneOutProperty circles;
	
	public static OddOneOutProperty red;
	public static OddOneOutProperty green;
	public static OddOneOutProperty blue;
	
	public static OddOneOutProperty redSquares;
	public static OddOneOutProperty redTriangles;
	public static OddOneOutProperty redCircles;
	
	public static OddOneOutProperty greenSquares;
	public static OddOneOutProperty greenTriangles;
	public static OddOneOutProperty greenCircles;
	
	public static OddOneOutProperty blueSquares;
	public static OddOneOutProperty blueTriangles;
	public static OddOneOutProperty blueCircles;
	
	public static void registerProperties() {
		numSymbols = new OddOneOutProperty("Number of symbols", 1, 2);
		numColours = new OddOneOutProperty("Number of colours", 1, 3);
		numShapes = new OddOneOutProperty("Number of shape types", 1, 3);
		
		squares = new OddOneOutProperty("Squares", 0, 2);
		triangles = new OddOneOutProperty("Triangles", 0, 2);
		circles = new OddOneOutProperty("Circles", 0, 2);
		
		red = new OddOneOutProperty("Red", 0, 2);
		green = new OddOneOutProperty("Green", 0, 2);
		blue = new OddOneOutProperty("Blue", 0, 2);
		
		redSquares = new OddOneOutProperty("Red Squares", 0, 2);
		redTriangles = new OddOneOutProperty("Red Triangles", 0, 2);
		redCircles = new OddOneOutProperty("Red Squares", 0, 2);
		
		greenSquares = new OddOneOutProperty("Green Squares", 0, 2);
		greenTriangles = new OddOneOutProperty("Green Triangles", 0, 2);
		greenCircles = new OddOneOutProperty("Green Circles", 0, 2);
		
		blueSquares = new OddOneOutProperty("Blue Squares", 0, 2);
		blueTriangles = new OddOneOutProperty("Blue Triangles", 0, 2);
		blueCircles = new OddOneOutProperty("Blue Circles", 0, 2);
	}
	
	public String name;
	public int min;
	public int max;
	
	public OddOneOutProperty(String name, int min, int max) {
		this.name = name;
		this.min = min;
		this.max = max;
		
		propertyList.add(this);
	}
}
