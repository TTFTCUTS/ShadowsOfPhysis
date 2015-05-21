package ttftcuts.physis.common.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;
import ttftcuts.physis.common.worldgen.structure.MapGenDigSite;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

public class StructureHandler {
	
	public Map<World, List<MapGenStructure>> structureMap = new HashMap<World, List<MapGenStructure>>();
	
	public StructureHandler() {
		MapGenStructureIO.registerStructure(MapGenDigSite.Start.class, "PhysisDigSite");
		
		MapGenStructureIO.func_143031_a(ComponentSiteRoom.class, "PhysisSiteRoom");
	}
	
	// world loading for creating the structure systems
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		//Physis.logger.info("Loaded world: "+event.world.getWorldInfo().getWorldName());
		
		if (event.world.provider.dimensionId == 0) {
			List<MapGenStructure> s = new ArrayList<MapGenStructure>();
			
			s.add(new MapGenDigSite());
			
			structureMap.put(event.world, s);
		}
	}
	
	// world unloading for destroying the structure systems
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		//Physis.logger.info("Unloaded world: "+event.world.getWorldInfo().getWorldName());
		
		if (structureMap.containsKey(event.world)) {
			structureMap.remove(event.world);
		}
	}
	
	// biome block replacement for planning structures
	@SubscribeEvent
	public void onStructurePlan(ChunkProviderEvent.ReplaceBiomeBlocks event) {
		//Physis.logger.info("Plan structures in chunk: "+event.chunkX+","+event.chunkZ);
		
		if (structureMap.containsKey(event.world)) {
			List<MapGenStructure> generators = structureMap.get(event.world);
			
			for (MapGenStructure gen : generators) {
				gen.func_151539_a(event.chunkProvider, event.world, event.chunkX, event.chunkZ, event.blockArray);
			}
		}
	}
	
	// populate event for building the structures
	@SubscribeEvent
	public void onPopulate(PopulateChunkEvent.Pre event) {
		//Physis.logger.info("Build structures in chunk: "+event.chunkX+","+event.chunkZ);
		
		if (structureMap.containsKey(event.world)) {
			List<MapGenStructure> generators = structureMap.get(event.world);
			
			for (MapGenStructure gen : generators) {
				gen.generateStructuresInChunk(event.world, event.rand, event.chunkX, event.chunkZ);
			}
		}
	}
	
	
	// #################################################################
	// Structure protection
	
	private boolean shouldProtectStructure(World world, int x, int y, int z) {
		if (world == null) { return false; }
		List<MapGenStructure> worldlist = structureMap.get(world);
		for (MapGenStructure mgs : worldlist) {
			if (mgs instanceof MapGenDigSite) {
				MapGenDigSite dig = (MapGenDigSite)mgs;
				StructureStart start = dig.getStructureAt(x, y, z);
				
				if (start != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	// block break event for preventing structure meddling
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		Physis.logger.info("Break event world: "+event.world);
		if (this.shouldProtectStructure(event.world, event.x, event.y, event.z)) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onAttemptBreak(PlayerEvent.BreakSpeed event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { return; }
		if (event.entity == null) { return; }
		Physis.logger.info("Breakspeed event world: "+event.entity.worldObj);
		if (this.shouldProtectStructure(event.entity.worldObj, event.x, event.y, event.z)) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onBlockPlace(BlockEvent.PlaceEvent event) {
		Physis.logger.info("Place event world: "+event.world);
		if (this.shouldProtectStructure(event.world, event.x, event.y, event.z)) {
			event.setCanceled(true);
		}
	}
}
