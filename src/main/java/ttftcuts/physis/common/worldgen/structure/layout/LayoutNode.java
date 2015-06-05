package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.BlockPalette.BlockPalettes;
import ttftcuts.physis.common.worldgen.structure.StructureGenerator.StructurePiece;
import ttftcuts.physis.common.worldgen.structure.prop.Prop;
import ttftcuts.physis.common.worldgen.structure.prop.PropTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class LayoutNode {
	public StructureBoundingBox bounds;
	
	public BlockPalette palette;
	public List<Prop> props;
	
	public LayoutNode(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPalette palette) {
		this.bounds = new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		this.palette = palette;
		this.props = new ArrayList<Prop>();
	}
	
	public LayoutNode placeProps(LayoutGrid.Room room) {
		int wallheight = 6;
		int width = this.bounds.getXSize();
		int length = this.bounds.getZSize();
		
		this.props.add(new Prop(PropTypes.testRoom, 0,0,0).setData("dx", width, "dy", this.bounds.maxY - this.bounds.minY, "dz", length).updateBounds());
		
		this.props.add(new Prop(PropTypes.bastionInnerWall, 0,1,1).setData("l", length-2, "h", wallheight).updateBounds());
		this.props.add(new Prop(PropTypes.bastionInnerWall, width-2,1,0).setData("l", width-2, "h", wallheight).setRotation(1).updateBounds());
		this.props.add(new Prop(PropTypes.bastionInnerWall, width-1,1,length-2).setData("l", length-2, "h", wallheight).setRotation(2).updateBounds());
		this.props.add(new Prop(PropTypes.bastionInnerWall, 1,1,length-1).setData("l", width-2, "h", wallheight).setRotation(3).updateBounds());
		
		this.props.add(new Prop(PropTypes.cornerFiller, 0,1,0).setData("h", wallheight).updateBounds());
		this.props.add(new Prop(PropTypes.cornerFiller, width-1,1,0).setData("h", wallheight).updateBounds());
		this.props.add(new Prop(PropTypes.cornerFiller, width-1,1,length-1).setData("h", wallheight).updateBounds());
		this.props.add(new Prop(PropTypes.cornerFiller, 0,1,length-1).setData("h", wallheight).updateBounds());
		
		for(LayoutGrid.Room.Door door : room.doors) {
			int offset = 4;
			int ox = door.dir == 1 ? offset : door.dir == 3 ? -offset : 0;
			int oz = door.dir == 0 ? -offset : door.dir == 2 ? offset : 0;
			
			this.props.add(new Prop(PropTypes.bastionDoorway, door.x * 9 + 4 + ox, 1, door.y * 9 + 4 + oz).setRotation(door.dir).updateBounds());
		}
		
		return this;
	}
	
	public StructurePiece getPiece(Random rand) {
		return new StructurePiece(this);
	}
	
	//##### static save/load stuff ##############################################
	
	public static NBTTagCompound writeToNBT(LayoutNode node) {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setInteger("minx", node.bounds.minX);
		tag.setInteger("miny", node.bounds.minY);
		tag.setInteger("minz", node.bounds.minZ);
		tag.setInteger("maxx", node.bounds.maxX);
		tag.setInteger("maxy", node.bounds.maxY);
		tag.setInteger("maxz", node.bounds.maxZ);
		
		tag.setString("pal", node.palette.id);
		
		NBTTagList list = new NBTTagList();
		
		for(Prop p : node.props) {
			list.appendTag(Prop.writeToNBT(p));
		}
		
		tag.setTag("props", list);
		
		return tag;
	}
	
	public static LayoutNode createFromNBT(NBTTagCompound tag) {
		
		int minx = tag.getInteger("minx");
		int miny = tag.getInteger("miny");
		int minz = tag.getInteger("minz");
		int maxx = tag.getInteger("maxx");
		int maxy = tag.getInteger("maxy");
		int maxz = tag.getInteger("maxz");
		
		BlockPalette palette = BlockPalettes.paletteRegistry.get(tag.getString("pal"));
		
		LayoutNode node = new LayoutNode(minx, miny, minz, maxx, maxy, maxz, palette);
		
		NBTTagList list = tag.getTagList("props", 10);
		
		for (int i=0; i<list.tagCount(); i++) {
			Prop prop = Prop.createFromNBT(list.getCompoundTagAt(i));
			
			if (prop != null) {
				node.props.add(prop);
			}
		}
		
		return node;
	}
}
