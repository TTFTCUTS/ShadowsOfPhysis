package ttftcuts.physis.common.worldgen.structure;

import java.util.Random;

import ttftcuts.physis.Physis;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentSiteRoom extends StructureComponent {

	public ComponentSiteRoom() {}
	
	public ComponentSiteRoom(int id, Random rand, int x, int y, int z, int dx, int dy, int dz) {
		super(id);
		this.coordBaseMode = 0;
		
		this.boundingBox = new StructureBoundingBox(x,y,z,x+dx-1,y+dy-1,z+dz-1);
	}
	
	// SAVE
	@Override
	protected void func_143012_a(NBTTagCompound tag) {
		tag.setInteger("xmin", this.boundingBox.minX);
		tag.setInteger("xmax", this.boundingBox.maxX);
		tag.setInteger("ymin", this.boundingBox.minY);
		tag.setInteger("ymax", this.boundingBox.maxY);
		tag.setInteger("zmin", this.boundingBox.minZ);
		tag.setInteger("zmax", this.boundingBox.maxZ);
	}

	// LOAD
	@Override
	protected void func_143011_b(NBTTagCompound tag) {
		int xmin = tag.getInteger("xmin");
		int xmax = tag.getInteger("xmax");
		int ymin = tag.getInteger("ymin");
		int ymax = tag.getInteger("ymax");
		int zmin = tag.getInteger("zmin");
		int zmax = tag.getInteger("zmax");
		
		this.boundingBox = new StructureBoundingBox(xmin,ymin,zmin,xmax,ymax,zmax);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox bounds) {
		//Physis.logger.info("gen: sbounds: "+bounds+", cbounds: "+this.boundingBox);
		
		this.fillWithBlocks(world, bounds, 0,0,0, 
			this.boundingBox.getXSize()-1, this.boundingBox.getYSize()-1, this.boundingBox.getZSize()-1, 
			Blocks.cobblestone, Blocks.cobblestone, false);
		
		return true;
	}

}
