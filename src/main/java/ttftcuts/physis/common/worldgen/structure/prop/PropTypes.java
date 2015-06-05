package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.HashMap;
import java.util.Map;

public class PropTypes {

	public static Map<String, PropType> propTypes;
	
	public static PropType paletteTest;
	public static PropType testRoom;
	
	public static PropType cornerFiller;
	
	public static PropType bastionInnerWall;
	public static PropType bastionDoorway;
	
	public static void init() {
		propTypes = new HashMap<String,PropType>();
		
		paletteTest = new PropTestHouse();
		testRoom = new PropTestRoom();
		
		cornerFiller = new BasicProps.RoomCornerFiller();
		
		bastionInnerWall = new BastionProps.InnerWall();
		bastionDoorway = new BastionProps.Doorway();
	}
}
