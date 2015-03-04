package ttftcuts.physis.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ModFinder {
	private static Map<String, String> modSource_Name = new HashMap<String, String>();
	private static Map<String, String> modSource_ID = new HashMap<String, String>();
	
	public static void init() {
		for (ModContainer mod : Loader.instance().getModList()){
	    	modSource_Name.put(mod.getSource().getName(), mod.getName());
	    	modSource_ID.put(mod.getSource().getName(), mod.getModId());
	    }
		
        modSource_Name.put("1.6.2.jar", "Minecraft");
        modSource_Name.put("1.6.3.jar", "Minecraft");          
        modSource_Name.put("1.6.4.jar", "Minecraft");
        modSource_Name.put("1.7.2.jar", "Minecraft");
        modSource_Name.put("1.7.10.jar", "Minecraft");
        modSource_Name.put("Forge", "Minecraft");  
        
        modSource_ID.put("1.6.2.jar", "Minecraft");
        modSource_ID.put("1.6.3.jar", "Minecraft");          
        modSource_ID.put("1.6.4.jar", "Minecraft");
        modSource_ID.put("1.7.2.jar", "Minecraft");
        modSource_ID.put("1.7.10.jar", "Minecraft");
        modSource_ID.put("Forge", "Minecraft"); 
	}
	
	public static String nameFromObject(Object obj){
		String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		
		try {
			objPath = URLDecoder.decode(objPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		
		String modName = "<Unknown>";
		for (String s: modSource_Name.keySet())
			if (objPath.contains(s)){
				modName = modSource_Name.get(s);
				break;
			}
		
		if (modName.equals("Minecraft Coder Pack"))
			modName = "Minecraft";
		
		return modName;
	}
	
	public static String idFromObject(Object obj){
		String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		
		try {
			objPath = URLDecoder.decode(objPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		
		String modID = "<Unknown>";
		for (String s: modSource_ID.keySet())
			if (objPath.contains(s)){
				modID = modSource_ID.get(s);
				break;
			}
		
		if (modID.equals("Forge"))
			modID = "Minecraft";
		
		return modID;
	}
	
	public static boolean isVanilla(Object obj) {
		return idFromObject(obj).equals("Minecraft");
	}
}
