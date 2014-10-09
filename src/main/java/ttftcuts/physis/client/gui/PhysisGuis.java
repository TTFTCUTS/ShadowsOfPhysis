package ttftcuts.physis.client.gui;

public enum PhysisGuis {

	JOURNAL,
	SOCKET_TABLE,
	SOCKET_TABLE_DRAWER,
	;
	
	private static final PhysisGuis[] cache = values();
	
	public int getID() {
		return ordinal();
	}
	
	public static PhysisGuis fromId(int id) {
		return cache[id];
	}
}
