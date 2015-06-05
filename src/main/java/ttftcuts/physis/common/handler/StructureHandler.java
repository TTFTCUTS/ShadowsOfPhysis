package ttftcuts.physis.common.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ttftcuts.physis.common.worldgen.structure.StructureGenerator;
import ttftcuts.physis.common.worldgen.structure.StructureGenerator.StructureData;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;

public class StructureHandler {
	
	public Set<Block> protectionAllowBreaking = new HashSet<Block>();
	public Set<Block> protectionAllowPlacement = new HashSet<Block>();
	
	public StructureHandler() {
		
		this.addProtectionException(Blocks.torch, true);
		this.addProtectionException(Blocks.fire, true);
		this.addProtectionException(Blocks.water, true);
		this.addProtectionException(Blocks.flowing_water, true);
		this.addProtectionException(Blocks.lava, true);
		this.addProtectionException(Blocks.flowing_lava, true);
		this.addProtectionException(Blocks.snow_layer, true);
		
		this.addProtectionException(Blocks.wooden_door);
		this.addProtectionException(Blocks.iron_door);
		this.addProtectionException(Blocks.mob_spawner);
		this.addProtectionException(Blocks.tallgrass);
		this.addProtectionException(Blocks.double_plant);
		this.addProtectionException(Blocks.web);
	}
	
	public void addProtectionException(Block block) {
		this.addProtectionException(block, false);
	}
	
	public void addProtectionException(Block block, boolean place) {
		if (!this.protectionAllowBreaking.contains(block)) {
			this.protectionAllowBreaking.add(block);
		}
		if (place && !this.protectionAllowPlacement.contains(block)) {
			this.protectionAllowPlacement.add(block);
		}
	}
	
	// world loading for creating the structure systems
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {

	}
	
	// world unloading for destroying the structure systems
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		//StructureGenerator.cleanUp();
	}
	
	// biome block replacement for planning structures
	@SubscribeEvent
	public void onStructurePlan(ChunkProviderEvent.ReplaceBiomeBlocks event) {
		for(Entry<String, StructureGenerator> e : StructureGenerator.generators.entrySet()) {
			e.getValue().plan(event.chunkProvider, event.world, event.chunkX, event.chunkZ, event.blockArray);
		}
	}
	
	// populate event for building the structures
	@SubscribeEvent
	public void onPrePopulate(PopulateChunkEvent.Pre event) {
		for(Entry<String, StructureGenerator> e : StructureGenerator.generators.entrySet()) {
			e.getValue().generateStructuresInChunk(event.world, event.rand, event.chunkX, event.chunkZ);
		}
	}
	
	// cancel lakes and lava on structures
	@SubscribeEvent
	public void onPopulate(PopulateChunkEvent.Populate event) {
		if (event.type == EventType.LAKE || event.type == EventType.LAVA) {
			for(Entry<String, StructureGenerator> e : StructureGenerator.generators.entrySet()) {
				if (e.getValue().areStructuresInChunk(event.world, event.chunkX, event.chunkZ)) {
					event.setResult(Result.DENY);
					return;
				}
			}
		}
	}
	
	
	// #################################################################
	// Structure protection
	
	private boolean shouldProtectStructure(World world, int x, int y, int z) {
		if (world == null) { return false; }
		for(StructureGenerator generator : StructureGenerator.generators.values()) {
			if (!generator.allowedInWorld(world)) { continue; }
			
			List<StructureData> structures = generator.getStructuresAtPoint(world, x, y, z);
			if (structures == null || structures.size() == 0) { continue; }
			
			for (StructureData structure : structures) {
				if (structure.warded) {
					return true;
				}
			}
		}
		return false;
	}
	
	// block break event for preventing structure meddling
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if (this.shouldProtectStructure(event.world, event.x, event.y, event.z)) {
			if (!protectionAllowBreaking.contains(event.block)) {
				event.setCanceled(true);
			}
		}
	}
	
	/*@SubscribeEvent
	public void onAttemptBreak(PlayerEvent.BreakSpeed event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { return; }
		if (event.entity == null) { return; }
		Physis.logger.info("Breakspeed event world: "+event.entity.worldObj);
		if (this.shouldProtectStructure(event.entity.worldObj, event.x, event.y, event.z)) {
			event.setCanceled(true);
		}
	}*/
	
	@SubscribeEvent
	public void onBlockPlace(BlockEvent.PlaceEvent event) {
		if (this.shouldProtectStructure(event.world, event.x, event.y, event.z)) {
			if (!protectionAllowPlacement.contains(event.block)) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onExplosion(ExplosionEvent.Detonate event) {
		List<ChunkPosition> poslist = event.getAffectedBlocks();
		for (int i=poslist.size()-1; i > 0; i--) {
			ChunkPosition pos = poslist.get(i);
			if (this.shouldProtectStructure(event.world, pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ)) {
				poslist.remove(pos);
			}
		}
	}
}
