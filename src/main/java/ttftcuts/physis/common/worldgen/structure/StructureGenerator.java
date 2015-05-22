package ttftcuts.physis.common.worldgen.structure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.file.IDataCallback;
import ttftcuts.physis.common.file.PhysisWorldSavedData;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;

public class StructureGenerator {

	public static final String GENERATORTAG = "StrucutreGen";
	public static Map<String, StructureGenerator> generators = new HashMap<String, StructureGenerator>();
	
	public String savename;
	protected Map<World, Map<Long, StructureData>> structureMap;
	protected int radius;
	
	public Set<Integer> allowedWorlds;
	protected Random rand;
	
	public static IDataCallback dataCallback = new IDataCallback() {
		@Override
		public void dataPacketSending() {}
		@Override
		public void dataPacketReceived() {}

		@Override
		public void dataSaving() {
			saveGeneratorsToWorldData();			
		}

		@Override
		public void dataLoaded() {
			loadFromWorldData();
		}
	};
	
	public StructureGenerator(String id, int radius, int... dimensionids) {
		this.savename = id;
		this.radius = radius;
		this.allowedWorlds = new HashSet<Integer>();
		this.structureMap = new HashMap<World, Map<Long, StructureData>>();
		
		this.rand = new Random();
		
		for(int i : dimensionids) {
			allowedWorlds.add(i);
		}
		
		generators.put(savename, this);
	}
	
	public void plan(IChunkProvider chunkProvider, World world, int chunkX, int chunkZ, Block[] blocks) {
		if (!this.allowedWorlds.contains(world.provider.dimensionId)) { 
			Physis.logger.info("NOT VALID DIM");
			return; 
		}

		if (!(world.getChunkProvider() instanceof ChunkProviderServer)) {
			Physis.logger.info("NOT CPS");
			return; 
		}
		
		ChunkProviderServer cps = (ChunkProviderServer)world.getChunkProvider();
		
		if (!(cps.currentChunkLoader instanceof AnvilChunkLoader)) { 
			Physis.logger.info("NOT ACL");
			return; 
		}
		
		AnvilChunkLoader acl = (AnvilChunkLoader)cps.currentChunkLoader;
		
		// check around this chunk
		for (int rx=-this.radius; rx<=this.radius; rx++) {
			scan:
			for (int rz=-this.radius; rz<=this.radius; rz++) {
				long coordhash = StructureData.idFromCoords(chunkX+rx, chunkZ+rz);
				if (!this.structureMap.containsKey(world)) {
					this.structureMap.put(world, new HashMap<Long, StructureData>());
				}
				if (this.structureMap.get(world).containsKey(coordhash)) {
					//Physis.logger.info("Duplicate");
					continue;
				}
				
				if (this.canGenerateInChunk(world, chunkX + rx, chunkZ + rz)) {
					// ok, we can generate in the found chunk - find out if only the source chunk was generated
					for (int sx=-this.radius; sx<=this.radius; sx++) {
						for (int sz=-this.radius; sz<=this.radius; sz++) {
							int checkX = chunkX + rx + sx;
							int checkZ = chunkZ + rz + sz;
							
							if (checkX == chunkX || checkZ == chunkZ) { continue; }
							
							if (acl.chunkExists(world, checkX, checkZ)) {
								Physis.logger.info("Rejected structure at "+(chunkX + rx)+","+(chunkZ + rz)+" due to generated chunk at "+checkX+","+checkZ);
								continue scan;
							}
						}
					}
					
					StructureData sdata = this.createStructureInChunk(world, chunkX + rx, chunkZ + rz);
					
					if (!this.structureMap.containsKey(world)) {
						this.structureMap.put(world, new HashMap<Long, StructureData>());
					}
					this.structureMap.get(world).put(sdata.id, sdata);
					
					Physis.logger.info("Generating a new structure at chunk "+sdata.x+","+sdata.z);
				}
			}
		}
	}
	
	public void generateStructuresInChunk(World world, Random rand, int chunkX, int chunkZ) {
		if (!this.allowedWorlds.contains(world.provider.dimensionId)) { return; }

	}
	
	public boolean canGenerateInChunk(World world, int chunkX, int chunkZ) {
		int k = chunkX >> 4;
        int l = chunkZ >> 4;
        this.rand.setSeed((long)(k ^ l << 4) ^ world.getSeed());
        this.rand.nextInt();
        return (Math.abs(chunkX) % 20 == 0) && (Math.abs(chunkZ) % 20 == 0);
	}
	
	public StructureData createStructureInChunk(World world, int chunkX, int chunkZ) {
		return new StructureData(world, chunkX, chunkZ);
	}

	protected NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		
		NBTTagList list = new NBTTagList();
		
		for(Entry<World,Map<Long, StructureData>> e : this.structureMap.entrySet()) {
			World w = e.getKey();
			Map<Long, StructureData> map = e.getValue();
			NBTTagCompound worldtag = new NBTTagCompound();
			
			NBTTagList worldlist = new NBTTagList();
			
			for(StructureData sdata : map.values()) {
				NBTTagCompound structuretag = sdata.writeToNBT();
				NBTTagCompound savetag = new NBTTagCompound();
				savetag.setLong("i", sdata.id);
				savetag.setTag("d", structuretag);
				worldlist.appendTag(savetag);
			}
			
			worldtag.setTag("l", worldlist);
			worldtag.setInteger("w", w.provider.dimensionId);
			
			list.appendTag(worldtag);
		}
		
		tag.setTag("l", list);

		return tag;
	}
	
	protected void loadFromNBT(NBTTagCompound tag) {
		NBTTagList list = tag.getTagList("l", 10);
		
		for (int i=0; i<list.tagCount(); i++) {
			NBTTagCompound worldtag = list.getCompoundTagAt(i);
			int worldid = worldtag.getInteger("w");
			NBTTagList worldlist = worldtag.getTagList("l", 10);
			
			World world = DimensionManager.getWorld(worldid);
			if (world == null) { continue; }
			
			if (!this.structureMap.containsKey(world)) {
				this.structureMap.put(world, new HashMap<Long, StructureData>());
			}
			
			for (int j=0; j<worldlist.tagCount(); j++) {
				NBTTagCompound savetag = worldlist.getCompoundTagAt(j);
				NBTTagCompound structuretag = savetag.getCompoundTag("d");
				//long id = savetag.getLong("i");
				
				StructureData data = StructureData.createFromNBT(structuretag);
				
				if (data != null) {
					this.structureMap.get(world).put(data.id, data);
				}
			}
		}
		
		Physis.logger.info("Structuremap: "+this.structureMap.toString());
	}

	public static void loadFromWorldData() {
		Physis.logger.info("Loading Structure data from world data");
		NBTTagCompound gens = PhysisWorldSavedData.getServerTag(GENERATORTAG);
		
		for(Entry<String, StructureGenerator> e : generators.entrySet()) {
			StructureGenerator gen = e.getValue();
			gen.structureMap.clear();

			if (gens.hasKey(gen.savename)) {
				gen.loadFromNBT(gens.getCompoundTag(gen.savename));
			}	
		}
	}

	public static void saveGeneratorsToWorldData() {
		NBTTagCompound tag = new NBTTagCompound();
		
		for(Entry<String, StructureGenerator> e : generators.entrySet()) {
			StructureGenerator gen = e.getValue();
			
			NBTTagCompound savetag = gen.writeToNBT();
			
			if (savetag != null) {
				tag.setTag(gen.savename, savetag);
			}
		}
		
		PhysisWorldSavedData.setServerTag(GENERATORTAG, tag);
		Physis.logger.info("Saving Structure data");
	}

	public static class StructureData {
		Long id;
		int x;
		int z;
		World world;
		
		public StructureData(World world, int x, int z) {
			this.x = x;
			this.z = z;
			this.world = world;
			this.id = idFromCoords(x, z);
		}
		
		public static long idFromCoords(int chunkX, int chunkZ) {
			return Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
		}
		
		public static StructureData createFromNBT(NBTTagCompound tag) {
			Physis.logger.info("StructureData: "+tag.toString());
			int x = tag.getInteger("x");
			int z = tag.getInteger("z");
			int worldid = tag.getInteger("w");
			World w = DimensionManager.getWorld(worldid);
			
			if (w == null) { return null; }
			
			StructureData sdata = new StructureData(w, x,z);
			
			return sdata;
		}
		
		public NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			
			tag.setInteger("x", this.x);
			tag.setInteger("z", this.z);
			tag.setInteger("w", this.world.provider.dimensionId);
			
			return tag;
		}
	}
}
