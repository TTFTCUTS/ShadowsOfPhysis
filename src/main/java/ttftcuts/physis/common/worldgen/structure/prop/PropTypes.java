package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.HashMap;
import java.util.Map;

public class PropTypes {

	public static Map<String, PropType> propTypes;
	
	public static PropType paletteTest;
	public static PropType testRoom;

	public static void init() {
		propTypes = new HashMap<String,PropType>();
		
		paletteTest = new PropTestHouse();
		testRoom = new PropTestRoom();
	}
}
