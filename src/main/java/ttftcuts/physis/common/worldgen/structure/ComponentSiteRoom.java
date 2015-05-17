package ttftcuts.physis.common.worldgen.structure;

import java.util.Random;

import ttftcuts.physis.common.helper.WorldGenHelper;
import ttftcuts.physis.common.worldgen.structure.layout.LayoutNode;
import ttftcuts.physis.common.worldgen.structure.prop.Prop;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentSiteRoom extends StructureComponent {

	public LayoutNode blueprintNode;
	
	public ComponentSiteRoom() {}
	
	public ComponentSiteRoom(int id, Random rand, LayoutNode blueprint) {
		super(id);
		this.coordBaseMode = 0;
		
		this.blueprintNode = blueprint;
		
		this.boundingBox = WorldGenHelper.cloneBounds(blueprint.bounds);
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
		
		if (this.blueprintNode != null) {
			tag.setTag("node", LayoutNode.writeToNBT(this.blueprintNode));
		}
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
		
		this.blueprintNode = LayoutNode.createFromNBT(tag.getCompoundTag("node"));
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox bounds) {
		//Physis.logger.info("gen: sbounds: "+bounds+", cbounds: "+this.boundingBox);
		
		this.fillWithBlocks(world, bounds, 0,0,0, 
			this.boundingBox.getXSize()-1, 0, this.boundingBox.getZSize()-1, 
			Blocks.cobblestone, Blocks.cobblestone, false);
		
		if (this.blueprintNode != null) {
			
			for(Prop prop : this.blueprintNode.props) {
				prop.type.buildProp(this, prop, world, bounds, rand);
			}
		}
		
		return true;
	}

}
