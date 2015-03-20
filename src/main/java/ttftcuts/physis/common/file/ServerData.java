package ttftcuts.physis.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.google.common.io.Files;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import ttftcuts.physis.common.network.packet.PacketWorldTime;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;

public class ServerData {
	private static final String TIMETAG = "ServerTick";
	
	public static class ServerDataHandler {
		@SubscribeEvent
        public void onWorldLoad(Load event) {
            if (event.world.isRemote) {
                reload(true);
            }
        }

        /*@SubscribeEvent
        public void onWorldSave(Save event) {
            if (!event.world.isRemote && instance(false) != null) {
            	instance(false).save();
            }
        }*/

        @SubscribeEvent
        public void onPlayerLogin(PlayerLoggedInEvent event) {
            instance(false).sendDataToPlayer(event.player);
        }

        @SubscribeEvent
        public void onPlayerChangedDimension(PlayerLoggedOutEvent event) {
            instance(false).sendDataToPlayer(event.player);
        }
	}
	
	public final boolean client;
	private static ServerData serverInstance;
	private static ServerData clientInstance;
	private File saveDir;
	private File saveFile;
	private File backupFile;
	
	public long serverTick = 0;

	public static void reload(boolean client) {
		ServerData data = new ServerData(client);
		if (client) {
			clientInstance = data;
		} else {
			serverInstance = data;
		}
	}
	
	public static ServerData instance(boolean client) {
		return client ? clientInstance : serverInstance;
	}
	
	public static void tick(boolean client) {
		ServerData instance = ServerData.instance(client);
		if (instance != null) {
			if (client) {
				if (!Minecraft.getMinecraft().isGamePaused()) {
					instance.serverTick++;
				}
			} else {
				instance.serverTick++;
				if (PhysisWorldSavedData.instance != null) {
					PhysisWorldSavedData.instance.setWorldLong(TIMETAG, instance.serverTick);
				}
				
				if (instance.serverTick % 600 == 0) {
					instance.sendDataToAll();
				}
			}
		}
	}
	
	public static void setTime(long time, boolean client) {
		ServerData instance = ServerData.instance(client);
		if (instance != null) {
			instance.serverTick = time;
			if (!client  && PhysisWorldSavedData.instance != null) {
				PhysisWorldSavedData.instance.setWorldLong(TIMETAG, instance.serverTick);
			}
		}
	}
	
	public ServerData(boolean client) {
		this.client = client;
		
		this.serverTick = 0;
		
		if (!client) {
			//this.load();
			if (PhysisWorldSavedData.instance != null) {
				this.serverTick = PhysisWorldSavedData.instance.getWorldLong(TIMETAG);
				Physis.logger.info("Loaded server tick: "+this.serverTick);
			}
		}
	}
	
	public void load() {
		saveDir = new File(DimensionManager.getCurrentSaveRootDirectory(), Physis.MOD_ID);
		try {
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}
			saveFile = new File(saveDir, "serverData.dat");
			backupFile = new File(saveDir, "serverDataBackup.dat");
			
			NBTTagCompound data = null;
			boolean saveBackup = false;
			
			if (saveFile != null && saveFile.exists()) {
				try {
					FileInputStream input = new FileInputStream(saveFile);
					data = CompressedStreamTools.readCompressed(input);
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (saveFile == null || !saveFile.exists() || data == null || data.hasNoTags() ) {
				if (backupFile != null && backupFile.exists()) {
					try {
						FileInputStream input = new FileInputStream(backupFile);
						data = CompressedStreamTools.readCompressed(input);
						input.close();
						saveBackup = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if (data != null) {
				this.serverTick = data.getLong("serverTick");
				Physis.logger.info("Loaded server tick: "+this.serverTick+ " for "+saveDir);
				
				if (saveBackup) {
					this.save();
				}
			}
			
		} catch (Exception e) {
			Physis.logger.fatal("Failed to load world data file.");
			e.printStackTrace();
        }
	}
	
	public void save() {
		try {
			if (saveFile != null && saveFile.exists()) {
				try {
					Files.copy(saveFile, backupFile);
				} catch (Exception e) {
					Physis.logger.error("Could not save backup world data file");
					e.printStackTrace();
				}
			}
			
			try {
				if (saveFile != null) {
					NBTTagCompound data = new NBTTagCompound();
					
					data.setLong("serverTick", serverTick);
					
					FileOutputStream output = new FileOutputStream(saveFile);
					CompressedStreamTools.writeCompressed(data, output);
					output.close();
					
					//Physis.logger.info("Saved server tick: "+this.serverTick+" for "+saveDir);
				}
			} catch (Exception e) {
				Physis.logger.error("Failed to save world data file.");
				e.printStackTrace();
				
				if (saveFile.exists()) {
					try {
						saveFile.delete();
					} catch (Exception e2) {}
				}
			}
		} catch (Exception e) {
			Physis.logger.fatal("Failed to save world data file.");
			e.printStackTrace();
		}
	}
	
	public void sendDataToPlayer(EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			PhysisPacketHandler.bus.sendTo(PacketWorldTime.createPacket(this.serverTick-1), (EntityPlayerMP)player);
		}
	}
	
	public void sendDataToAll() {
		PhysisPacketHandler.bus.sendToAll(PacketWorldTime.createPacket(this.serverTick-1));
	}
}
