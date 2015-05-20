package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.ArrayList;
import java.util.List;

public class PropTypes {

	public static List<PropType> propTypes;
	
	public static PropType paletteTest;

	public static void init() {
		propTypes = new ArrayList<PropType>();
		
		paletteTest = new PropTestHouse();
	}
}
