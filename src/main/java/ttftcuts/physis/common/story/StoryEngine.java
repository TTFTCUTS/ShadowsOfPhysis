package ttftcuts.physis.common.story;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ttftcuts.physis.Physis;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import ttftcuts.physis.common.network.packet.PacketStorySeed;

public class StoryEngine {

	public static class StorySeedHandler {
        @SubscribeEvent
        public void onPlayerLogin(PlayerLoggedInEvent event) {
        	Physis.logger.info("Logged in: "+event.player.getDisplayName());
            instance(false).sendDataToPlayer(event.player);
        }

        /*@SubscribeEvent
        public void onPlayerChangedDimension(PlayerLoggedOutEvent event) {
            instance(false).sendDataToPlayer(event.player);
        }*/
	}
	
	private static Map<String, Integer> registry = new HashMap<String, Integer>();
	
	private static StoryEngine serverInstance;
	private static StoryEngine clientInstance;
	
	public final boolean client;
	public long seed;
	private Random rand;
	private Map<String, Integer> storyVars = new HashMap<String, Integer>();
	
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
		}
	}
	
	//----------------
	
	public StoryEngine(long seed, boolean client) {
		this.seed = seed;
		this.client = client;
		
		this.rand = new Random(this.seed);
		
		this.storyVars.clear();
		
		if (seed != -1) {
			for(Entry<String, Integer> entry : registry.entrySet()) {
				this.storyVars.put(entry.getKey(), rand.nextInt(entry.getValue()));
			}
		}
		
		//Physis.logger.info("Starting Story Engine: "+seed);
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
			return storyVars.get(variable);
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
	
	public void sendDataToPlayer(EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			Physis.logger.info("Sending story packet to "+player.getDisplayName());
			PhysisPacketHandler.bus.sendTo(PacketStorySeed.createPacket(this.seed), (EntityPlayerMP)player);
		}
	}
}
