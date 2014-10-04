package ttftcuts.physis.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.google.common.io.Files;

import ttftcuts.physis.Physis;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ServerData {

	public class ServerDataHandler {
		@SubscribeEvent
        public void onWorldLoad(Load event) {
            if (event.world.isRemote) {
            	instance.load();
            }
        }

        @SubscribeEvent
        public void onWorldSave(Save event) {
            if (!event.world.isRemote && instance != null) {
            	instance.save();
            }
        }
	}
	
	public static ServerData instance;
	private File saveDir;
	private File saveFile;
	private File backupFile;
	
	public long serverTick = 0;
	
	public static void init() {
		instance = new ServerData();
		
		MinecraftForge.EVENT_BUS.register(instance.new ServerDataHandler());
	}
	
	private void load() {
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
				Physis.logger.info("Loaded server tick: "+this.serverTick);
				
				if (saveBackup) {
					this.save();
				}
			}
			
		} catch (Exception e) {
			Physis.logger.fatal("Failed to load world data file.");
			e.printStackTrace();
        }
	}
	
	private void save() {
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
}
