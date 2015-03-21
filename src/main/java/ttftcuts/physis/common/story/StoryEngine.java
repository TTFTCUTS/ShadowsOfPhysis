package ttftcuts.physis.common.story;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ttftcuts.physis.Physis;
import ttftcuts.physis.common.file.IDataCallback;
import ttftcuts.physis.common.file.PhysisWorldSavedData;

public class StoryEngine {
	private static final String STORYTAG = "story";
	private static final String SEEDTAG = "seed";
	private static final String VARIABLETAG = "vars";
	private static final String VARNAMETAG = "name";
	private static final String VARVALTAG = "val";
	
	private static IDataCallback dataCallback = new IDataCallback() {

		@Override
		public void dataPacketSending() {
			PhysisWorldSavedData.getWorldTag(STORYTAG).setLong("seed", instance(false).seed);
			Physis.logger.info("Adding story seed to packet");
		}

		@Override
		public void dataPacketReceived() {
			long s = PhysisWorldSavedData.getWorldTag(STORYTAG).getLong("seed");
			StoryEngine.reload(s, true);
			Physis.logger.info("Loaded story seed from packet");
		}
		
	};
	
	private static Map<String, Integer> registry = new HashMap<String, Integer>();
	
	private static StoryEngine serverInstance;
	private static StoryEngine clientInstance;
	
	public final boolean client;
	public long seed;
	private Map<String, StoryVariable> storyVars = new HashMap<String, StoryVariable>();
	
	public static StoryEngine instance(boolean client) {
		return client ? clientInstance : serverInstance;
	}
	
	public static void registerVariable(String name, int range) {
		registry.put(name, range);
	}
	
	public static void reload(long seed, boolean client) {
		StoryEngine newengine = new StoryEngine(seed, client);
		if (client) {
			clientInstance = newengine;
		} else {
			serverInstance = newengine;
			PhysisWorldSavedData.registerCallback(dataCallback);
		}
	}
	
	//----------------
	
	public StoryEngine(long seed, boolean client) {
		this.seed = seed;
		this.client = client;
		
		this.storyVars.clear();

		if (seed != -1) {
			NBTTagCompound data = PhysisWorldSavedData.getWorldTag(STORYTAG);
			this.loadFromNBT(data);
			if (!this.client) {
				this.writeToNBT(data);
				PhysisWorldSavedData.safeMarkDirty();
			}
		}
		
		//Physis.logger.info("Starting Story Engine: "+seed);
	}
	
	public void loadFromNBT(NBTTagCompound tag) {
		this.seed = tag.getLong(SEEDTAG);
		
		NBTTagList list = tag.getTagList(VARIABLETAG, 10);
		for (int i=0; i<list.tagCount(); i++) {
			NBTTagCompound vartag = list.getCompoundTagAt(i);
			String name = vartag.getString(VARNAMETAG);
			if (name != null && registry.containsKey(name)) {
				int val = vartag.getInteger(VARVALTAG);
				this.storyVars.put(name, new StoryVariable(name, registry.get(name), val));
			}
		}
		
		boolean dirty = false;
		for(Entry<String, Integer> entry : registry.entrySet()) {
			String name = entry.getKey();
			int max = entry.getValue();
			
			if (!storyVars.containsKey(name)) {
				this.storyVars.put(name, new StoryVariable(name, max));
				dirty = true;
			}
		}
		
		if (dirty && !this.client) {
			this.writeToNBT(tag);
			PhysisWorldSavedData.safeMarkDirty();
		}
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		tag.setLong(SEEDTAG, this.seed);
		
		NBTTagList list = new NBTTagList();
		for(Entry<String, StoryVariable> entry : storyVars.entrySet()) {
			NBTTagCompound vartag = new NBTTagCompound();
			vartag.setString(VARNAMETAG, entry.getKey());
			vartag.setInteger(VARVALTAG, entry.getValue().value);
			list.appendTag(vartag);
		}
		tag.setTag(VARIABLETAG, list);
	}
	
	public static int get(String variable, boolean client) {
		StoryEngine e = instance(client);
		if (e != null) {
			return e.get(variable);
		}
		return -1;
	}
	
	public int get(String variable) {
		if (storyVars.containsKey(variable)) {
			return storyVars.get(variable).value;
		}
		return -1;
	}
	
	public static int getRange(String variable) {
		if (registry.containsKey(variable)) {
			return registry.get(variable);
		}
		return 0;
	}
	
	public String getVarString(String input, String varname) {
		int var = get(varname);
		if (var == -1) {
			return "[MISSING]";
		} else {
			return input.replace("#", String.valueOf(var));
		}
	}
	
	//----------------
	
	private class StoryVariable {
		public String name;
		public int value;
		public int max;
		
		public StoryVariable(String name, int max) {
			this.name = name;
			this.max = max;
			
			Random rand = new Random(StoryEngine.this.seed ^ this.name.hashCode());
			
			this.value = rand.nextInt(this.max);
		}
		
		public StoryVariable(String name, int max, int value) {
			this.name = name;
			this.value = value;
			this.max = max;
		}
	}
}
