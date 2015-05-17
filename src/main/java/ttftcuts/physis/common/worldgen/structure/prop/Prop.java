package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.HashMap;
import java.util.Map;

import ttftcuts.physis.common.worldgen.structure.BlockPalette;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class Prop {

	public PropType type;
	public Map<String, Integer> extraData;
	public int x,y,z;
	public int rotation;
	public boolean flipped;
	
	public Prop(PropType type, int x, int y, int z) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.extraData = new HashMap<String, Integer>();
		this.rotation = 0;
		this.flipped = false;
	}
	
	public Prop setRotation(int rotation) {
		this.rotation = rotation;
		return this;
	}
	
	public Prop setFlip(boolean flip) {
		this.flipped = flip;
		return this;
	}
	
	// ##### instance-level utils ###############################################
	protected void placeBlock(World world, StructureBoundingBox limit, StructureComponent component, int x, int y, int z, BlockPalette.Entry block, int metaoffset) {
		this.placeBlock(world, limit, component, x, y, z, block, metaoffset, 0);
	}
	
	protected void placeBlock(World world, StructureBoundingBox limit, StructureComponent component, int x, int y, int z, BlockPalette.Entry block, int metaoffset, int colour) {
		this.placeBlock(world, limit, component, x, y, z, block.getBlock(), block.getMeta(this.rotation, this.flipped, metaoffset, colour));
	}

	protected void placeBlock(World world, StructureBoundingBox limit, StructureComponent component, int x, int y, int z, Block block, int meta) {
		int tx = transformX(x,y,z, component);
		int ty = transformY(x,y,z, component);
		int tz = transformZ(x,y,z, component);
		
		if (limit.isVecInside(tx, ty, tz)) {
			world.setBlock(tx, ty, tz, block, meta, 2);
		}
	}
	
	protected int transformX(int x, int y, int z, StructureComponent component) {
		int fx = this.flipped ? -x : x;
		int rx = fx;
		
		switch (this.rotation) {
		case 1:
			rx = -z;
			break;
		case 2:
			rx = -fx;
			break;
		case 3:
			rx = z;
			break;
		default:
			rx = fx;
		}
		
		return component.getBoundingBox().minX + this.x + rx;
	}
	
	protected int transformY(int x, int y, int z, StructureComponent component) {
		return component.getBoundingBox().minY + this.y + y;
	}
	
	protected int transformZ(int x, int y, int z, StructureComponent component) {
		int fx = this.flipped ? -x : x;
		int rz = z;
		
		switch (this.rotation) {
		case 1:
			rz = fx;
			break;
		case 2:
			rz = -z;
			break;
		case 3:
			rz = -fx;
			break;
		default:
			rz = z;
		}
		
		return component.getBoundingBox().minZ + this.z + rz;
	}
	
	// ##### static utils #######################################################
	
	public static Prop createFromNBT(NBTTagCompound tag) {
		int x = tag.getInteger("x");
		int y = tag.getInteger("y");
		int z = tag.getInteger("z");
		int id = tag.getInteger("id");
		
		PropType type = PropTypes.propTypes.get(id);
		
		if (type == null) { return null; }
		
		Prop prop = new Prop(type, x,y,z);
		
		prop.rotation = tag.getInteger("r");
		prop.flipped = tag.getBoolean("f");
		
		NBTTagList data = tag.getTagList("data", 10);
		
		for (int i=0; i<data.tagCount(); i++) {
			NBTTagCompound d = data.getCompoundTagAt(i);
			String key = d.getString("k");
			int val = d.getInteger("v");
			
			prop.extraData.put(key, val);
		}
		
		return prop;
	}
	
	public static NBTTagCompound writeToNBT(Prop prop) {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setInteger("x", prop.x);
		tag.setInteger("y", prop.y);
		tag.setInteger("z", prop.z);
		tag.setInteger("id", prop.type.id);
		tag.setInteger("r", prop.rotation);
		tag.setBoolean("f", prop.flipped);
		
		NBTTagList list = new NBTTagList();
		
		for (Map.Entry<String, Integer> entry : prop.extraData.entrySet()) {
			NBTTagCompound datatag = new NBTTagCompound();
			datatag.setString("k", entry.getKey());
			datatag.setInteger("v", entry.getValue());
			
			list.appendTag(datatag);
		}
		
		tag.setTag("data", list);
		
		return tag;
	}
}
