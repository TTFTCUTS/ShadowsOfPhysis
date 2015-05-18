package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.HashMap;
import java.util.Map;

import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class Prop {

	public PropType type;
	public Map<String, Integer> extraData;
	public int x,y,z;
	public int rotation;
	public boolean flipped;
	public StructureBoundingBox bounds = PropType.defaultBounds;
	
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
	
	public Prop setData(Object... args) {
		for (int i=0; i<args.length; i+=2) {
			if (i+1 < args.length) {
				if (args[i] instanceof String && args[i+1] instanceof Integer) {
					String key = (String)args[i];
					Integer val = (int)args[i+1];
					
					this.extraData.put(key, val);
				}
			}
		}
		
		return this;
	}
	
	public Prop updateBounds() {
		StructureBoundingBox newbounds = this.type.getBoundingBoxForProp(this);
		
		this.rotateBoundingBox(newbounds);
		
		this.bounds = newbounds;
		
		return this;
	}

	// ##### instance-level utils ###############################################
	protected void placeBlock(World world, StructureBoundingBox limit, ComponentSiteRoom component, int x, int y, int z, BlockPalette.Entry block, int metaoffset) {
		this.placeBlock(world, limit, component, x, y, z, block, metaoffset, 0);
	}
	
	protected void placeBlock(World world, StructureBoundingBox limit, ComponentSiteRoom component, int x, int y, int z, BlockPalette.Entry block, int metaoffset, int colour) {
		this.placeBlock(world, limit, component, x, y, z, block.getBlock(), block.getMeta(this.rotation, this.flipped, metaoffset, colour));
	}

	protected void placeBlock(World world, StructureBoundingBox limit, ComponentSiteRoom component, int x, int y, int z, Block block, int meta) {
		int tx = transformX(x,y,z, component);
		int ty = transformY(x,y,z, component);
		int tz = transformZ(x,y,z, component);
		
		if (limit.isVecInside(tx, ty, tz)) {
			world.setBlock(tx, ty, tz, block, meta, 2);
		}
	}
	
	protected int localTransformX(int x, int y, int z) {
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
		
		return this.x + rx;
	}
	protected int transformX(int x, int y, int z, ComponentSiteRoom component) {
		return this.localTransformX(x, y, z) + component.getBoundingBox().minX + component.layoutOffsetX;
	}
	
	protected int localTransformY(int x, int y, int z) {
		return this.y + y;
	}
	protected int transformY(int x, int y, int z, ComponentSiteRoom component) {
		return this.localTransformY(x, y, z) + component.getBoundingBox().minY + component.layoutOffsetY;
	}
	
	protected int localTransformZ(int x, int y, int z) {
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
		
		return this.z + rz;
	}
	protected int transformZ(int x, int y, int z, ComponentSiteRoom component) {
		return this.localTransformZ(x, y, z) + component.getBoundingBox().minZ + component.layoutOffsetZ;
	}
	
	protected void rotateBoundingBox(StructureBoundingBox b) {
		int rminx = this.localTransformX(b.minX, b.minY, b.minZ);
		int rminy = this.localTransformY(b.minX, b.minY, b.minZ);
		int rminz = this.localTransformZ(b.minX, b.minY, b.minZ);
		int rmaxx = this.localTransformX(b.maxX, b.maxY, b.maxZ);
		int rmaxy = this.localTransformY(b.maxX, b.maxY, b.maxZ);
		int rmaxz = this.localTransformZ(b.maxX, b.maxY, b.maxZ);
		
		b.minX = Math.min(rminx, rmaxx);
		b.minY = Math.min(rminy, rmaxy);
		b.minZ = Math.min(rminz, rmaxz);
		b.maxX = Math.max(rminx, rmaxx);
		b.maxY = Math.max(rminy, rmaxy);
		b.maxZ = Math.max(rminz, rmaxz);
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
