package ttftcuts.physis.common.worldgen.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import ttftcuts.physis.common.file.IDataCallback;
import ttftcuts.physis.common.file.PhysisWorldSavedData;
import ttftcuts.physis.common.helper.WorldGenHelper;
import ttftcuts.physis.common.worldgen.structure.layout.LayoutNode;
import ttftcuts.physis.common.worldgen.structure.layout.StructureLayout;
import ttftcuts.physis.common.worldgen.structure.prop.Prop;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
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
	
	public boolean allowedInWorld(World world) {
		return this.allowedWorlds.contains(world.provider.dimensionId);
	}
	
	public void plan(IChunkProvider chunkProvider, World world, int chunkX, int chunkZ, Block[] blocks) {
		if (!this.allowedInWorld(world)) { 
			//Physis.logger.info("NOT VALID DIM");
			return; 
		}

		if (!(world.getChunkProvider() instanceof ChunkProviderServer)) {
			//Physis.logger.info("NOT CPS");
			return; 
		}
		
		ChunkProviderServer cps = (ChunkProviderServer)world.getChunkProvider();
		
		if (!(cps.currentChunkLoader instanceof AnvilChunkLoader)) { 
			//Physis.logger.info("NOT ACL");
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
								//Physis.logger.info("Rejected structure at "+(chunkX + rx)+","+(chunkZ + rz)+" due to generated chunk at "+checkX+","+checkZ);
								continue scan;
							}
						}
					}
					
					StructureData sdata = this.createStructureInChunk(world, chunkX + rx, chunkZ + rz);
					
					if (!this.structureMap.containsKey(world)) {
						this.structureMap.put(world, new HashMap<Long, StructureData>());
					}
					this.structureMap.get(world).put(sdata.id, sdata);
					
					//Physis.logger.info("Generating a new structure at chunk "+sdata.x+","+sdata.z);
					
					//saveGeneratorsToWorldData();
				}
			}
		}
	}
	
	public void generateStructuresInChunk(World world, Random rand, int chunkX, int chunkZ) {
		if (!this.allowedWorlds.contains(world.provider.dimensionId)) { return; }

		if (!this.structureMap.containsKey(world)) { return; }
		Map<Long, StructureData> map = this.structureMap.get(world);
		
		//Physis.logger.info("gen coords: "+chunkX+","+chunkZ);
		
		int minx = (chunkX << 4) + 8;
		int maxx = minx + 15;
		int minz = (chunkZ << 4) + 8;
		int maxz = minz + 15;
		
		StructureBoundingBox chunkBounds = new StructureBoundingBox(minx,minz,maxx,maxz);
		
		for (int rx=-this.radius; rx<=this.radius; rx++) {
			for (int rz=-this.radius; rz<=this.radius; rz++) {
				long coordhash = StructureData.idFromCoords(chunkX+rx, chunkZ+rz);
				
				if (map.containsKey(coordhash)) {
					StructureData structure = map.get(coordhash);

					if(structure.bounds.intersectsWith(minx, minz, maxx, maxz)) {
						//Physis.logger.info("Structure bounds: "+structure.bounds);
						for (StructurePiece piece : structure.pieces) {
							//Physis.logger.info("Piece bounds: "+piece.bounds);
							if (piece.bounds.intersectsWith(minx, minz, maxx, maxz)) {
								piece.addComponentParts(world, rand, chunkBounds);
							}
						}
					}
				}
			}
		}
	}
	
	public List<StructureData> getStructuresAtPoint(World world, int x, int y, int z) {
		if (!this.allowedWorlds.contains(world.provider.dimensionId)) { return null; }
		if (!this.structureMap.containsKey(world)) { return null; }
		Map<Long, StructureData> map = this.structureMap.get(world);
		
		List<StructureData> out = new ArrayList<StructureData>();
		
		int chunkX = x >> 4;
		int chunkZ = z >> 4;
		
		for (int rx=-this.radius; rx<=this.radius; rx++) {
			for (int rz=-this.radius; rz<=this.radius; rz++) {
				long coordhash = StructureData.idFromCoords(chunkX+rx, chunkZ+rz);
				
				if (map.containsKey(coordhash)) {
					StructureData structure = map.get(coordhash);

					if(structure.bounds.isVecInside(x, y, z)) {
						for (StructurePiece piece : structure.pieces) {
							if (piece.bounds.isVecInside(x, y, z)) {
								out.add(structure);
								break;
							}
						}
					}
				}
			}
		}
		
		return out;
	}
	
	public boolean canGenerateInChunk(World world, int chunkX, int chunkZ) {
		int k = chunkX >> 4;
        int l = chunkZ >> 4;
        this.rand.setSeed((long)(k ^ l << 4) ^ world.getSeed());
        this.rand.nextInt();
        return (Math.abs(chunkX) % 20 == 0) && (Math.abs(chunkZ) % 20 == 0);
	}
	
	public StructureData createStructureInChunk(World world, int chunkX, int chunkZ) {
		StructureData structure = new StructureData(world, chunkX, chunkZ);
		
		StructureLayout layout = new StructureLayout((chunkX << 4) + 2, 192, (chunkZ << 4) + 2, this.rand);
        List<StructurePiece> parts = layout.exportToStructurePieces(rand);
        
        structure.pieces = parts;
        
        structure.updateBounds();
		
		return structure;
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
		
		//Physis.logger.info("Structuremap: "+this.structureMap.toString());
	}

	public static void loadFromWorldData() {
		//Physis.logger.info("Loading Structure data from world data");
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
		//Physis.logger.info("Saving Structure data");
	}
	
	public static void cleanUp() {
		saveGeneratorsToWorldData();
		
		for (StructureGenerator generator : generators.values()) {
			generator.structureMap.clear();
		}
	}

	public static class StructureData {
		public Long id;
		public int x;
		public int z;
		public World world;
		public StructureBoundingBox bounds;
		public List<StructurePiece> pieces;
		
		public boolean warded = true;
		
		public StructureData(World world, int x, int z) {
			this.x = x;
			this.z = z;
			this.world = world;
			this.id = idFromCoords(x, z);
			this.bounds = new StructureBoundingBox(0,0,0,0,0,0);
			this.pieces = new ArrayList<StructurePiece>();
		}
		
		public static long idFromCoords(int chunkX, int chunkZ) {
			return Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
		}
		
		public static StructureData createFromNBT(NBTTagCompound tag) {
			//Physis.logger.info("StructureData: "+tag.toString());
			int x = tag.getInteger("x");
			int z = tag.getInteger("z");
			int worldid = tag.getInteger("w");
			World w = DimensionManager.getWorld(worldid);
			
			boolean warded = tag.getBoolean("wa");
			
			if (w == null) { return null; }
			
			StructureData sdata = new StructureData(w, x,z);
			sdata.warded = warded;
			
			NBTTagList list = tag.getTagList("p", 10);
			
			for (int i=0; i<list.tagCount(); i++) {
				StructurePiece piece = StructurePiece.createFromNBT(list.getCompoundTagAt(i));
				if (piece != null) {
					sdata.pieces.add(piece);
				}
			}
			
			if (sdata.pieces.size() > 0) {
				sdata.bounds = WorldGenHelper.cloneBounds(sdata.pieces.get(0).bounds);
			}
			sdata.updateBounds();
			
			return sdata;
		}
		
		public NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			
			tag.setInteger("x", this.x);
			tag.setInteger("z", this.z);
			tag.setInteger("w", this.world.provider.dimensionId);
			tag.setBoolean("wa", this.warded);
			
			NBTTagList list = new NBTTagList();
			
			for (StructurePiece piece : this.pieces) {
				list.appendTag(piece.writeToNBT());
			}
			
			tag.setTag("p", list);
			
			return tag;
		}
		
		public void updateBounds() {
			for (StructurePiece piece : this.pieces) {
				this.bounds.expandTo(piece.bounds);
			}
		}
	}
	
	public static class StructurePiece {
		public LayoutNode blueprintNode;
		public StructureBoundingBox bounds;
		public int layoutOffsetX;
		public int layoutOffsetY;
		public int layoutOffsetZ;
		
		public StructurePiece(LayoutNode blueprint) {
			this.blueprintNode = blueprint;
			this.bounds = WorldGenHelper.cloneBounds(blueprint.bounds);
			this.updateBoundingForProps();
		}
		
		public NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			
			tag.setInteger("xmin", this.bounds.minX);
			tag.setInteger("xmax", this.bounds.maxX);
			tag.setInteger("ymin", this.bounds.minY);
			tag.setInteger("ymax", this.bounds.maxY);
			tag.setInteger("zmin", this.bounds.minZ);
			tag.setInteger("zmax", this.bounds.maxZ);
			
			if (this.blueprintNode != null) {
				tag.setTag("node", LayoutNode.writeToNBT(this.blueprintNode));
			}
			
			return tag;
		}
		
		public static StructurePiece createFromNBT(NBTTagCompound tag) {
			int xmin = tag.getInteger("xmin");
			int xmax = tag.getInteger("xmax");
			int ymin = tag.getInteger("ymin");
			int ymax = tag.getInteger("ymax");
			int zmin = tag.getInteger("zmin");
			int zmax = tag.getInteger("zmax");
			
			LayoutNode node = LayoutNode.createFromNBT(tag.getCompoundTag("node"));
			
			StructurePiece piece = new StructurePiece(node);
			
			piece.bounds = new StructureBoundingBox(xmin,ymin,zmin,xmax,ymax,zmax);
			
			return piece;
		}
		
		protected void updateBoundingForProps() {
			if (this.blueprintNode.props.size() == 0) {return;}
			
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int minZ = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;
			int maxZ = Integer.MIN_VALUE;
			
			for (Prop prop : this.blueprintNode.props) {
				minX = Math.min(minX, prop.bounds.minX);
				minY = Math.min(minY, prop.bounds.minY);
				minZ = Math.min(minZ, prop.bounds.minZ);
				
				maxX = Math.max(maxX, prop.bounds.maxX);
				maxY = Math.max(maxY, prop.bounds.maxY);
				maxZ = Math.max(maxZ, prop.bounds.maxZ);
			}
			
			int bminX = this.bounds.minX;
			int bminY = this.bounds.minY;
			int bminZ = this.bounds.minZ;
			
			this.bounds.minX = Math.min(this.bounds.minX, this.bounds.minX + minX);
			this.bounds.minY = Math.min(this.bounds.minY, this.bounds.minY + minY);
			this.bounds.minZ = Math.min(this.bounds.minZ, this.bounds.minZ + minZ);
			
			this.bounds.maxX = Math.max(this.bounds.maxX, this.bounds.minX + maxX);
			this.bounds.maxY = Math.max(this.bounds.maxY, this.bounds.minY + maxY);
			this.bounds.maxZ = Math.max(this.bounds.maxZ, this.bounds.minZ + maxZ);
			
			this.layoutOffsetX = bminX - this.bounds.minX;
			this.layoutOffsetY = bminY - this.bounds.minY;
			this.layoutOffsetZ = bminZ - this.bounds.minZ;
		}
		
		public void addComponentParts(World world, Random rand, StructureBoundingBox chunkBounds) {
			if (this.blueprintNode != null) {
				
				for(Prop prop : this.blueprintNode.props) {
					prop.type.buildProp(this, prop, world, chunkBounds, rand);
				}
			}

			/*int cx = this.bounds.getXSize()-1;
			int cy = this.bounds.getYSize()-1;
			int cz = this.bounds.getZSize()-1;
			
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, 0, 0, chunkBounds);
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, 0, cz, chunkBounds);
			
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, cy, 0, chunkBounds);
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, cy, cz, chunkBounds);
			
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, 0, 0, chunkBounds);
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, 0, cz, chunkBounds);
			
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, cy, 0, chunkBounds);
			this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, cy, cz, chunkBounds);*/
		}
	}
}
