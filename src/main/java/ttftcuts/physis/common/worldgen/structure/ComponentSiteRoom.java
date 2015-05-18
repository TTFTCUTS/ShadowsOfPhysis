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
	public int layoutOffsetX = 0;
	public int layoutOffsetY = 0;
	public int layoutOffsetZ = 0;
	
	public ComponentSiteRoom() {}
	
	public ComponentSiteRoom(int id, Random rand, LayoutNode blueprint) {
		super(id);
		this.coordBaseMode = 0;
		
		this.blueprintNode = blueprint;
		
		this.boundingBox = WorldGenHelper.cloneBounds(blueprint.bounds);
		this.updateBoundingForProps();
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
		
		/*this.fillWithBlocks(world, bounds, 0,0,0, 
			this.boundingBox.getXSize()-1, 0, this.boundingBox.getZSize()-1, 
			Blocks.cobblestone, Blocks.cobblestone, false);*/
		
		if (this.blueprintNode != null) {
			
			for(Prop prop : this.blueprintNode.props) {
				prop.type.buildProp(this, prop, world, bounds, rand);
			}
		}

		int cx = this.boundingBox.getXSize()-1;
		int cy = this.boundingBox.getYSize()-1;
		int cz = this.boundingBox.getZSize()-1;
		
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, 0, 0, bounds);
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, 0, cz, bounds);
		
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, cy, 0, bounds);
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 0, cy, cz, bounds);
		
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, 0, 0, bounds);
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, 0, cz, bounds);
		
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, cy, 0, bounds);
		this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, cx, cy, cz, bounds);
		
		return true;
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
		
		int bminX = this.boundingBox.minX;
		int bminY = this.boundingBox.minY;
		int bminZ = this.boundingBox.minZ;
		
		this.boundingBox.minX = Math.min(this.boundingBox.minX, this.boundingBox.minX + minX);
		this.boundingBox.minY = Math.min(this.boundingBox.minY, this.boundingBox.minY + minY);
		this.boundingBox.minZ = Math.min(this.boundingBox.minZ, this.boundingBox.minZ + minZ);
		
		this.boundingBox.maxX = Math.max(this.boundingBox.maxX, this.boundingBox.minX + maxX);
		this.boundingBox.maxY = Math.max(this.boundingBox.maxY, this.boundingBox.minY + maxY);
		this.boundingBox.maxZ = Math.max(this.boundingBox.maxZ, this.boundingBox.minZ + maxZ);
		
		this.layoutOffsetX = bminX - this.boundingBox.minX;
		this.layoutOffsetY = bminY - this.boundingBox.minY;
		this.layoutOffsetZ = bminZ - this.boundingBox.minZ;
	}
}
