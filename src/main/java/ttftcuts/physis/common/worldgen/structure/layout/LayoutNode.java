package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;
import ttftcuts.physis.common.worldgen.structure.prop.Prop;
import ttftcuts.physis.common.worldgen.structure.prop.PropTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class LayoutNode {
	public StructureBoundingBox bounds;
	
	public List<Prop> props;
	
	public LayoutNode(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.bounds = new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		this.props = new ArrayList<Prop>();
		this.placeProps();
	}
	
	public void placeProps() {
		this.props.add(new Prop(PropTypes.testProp, 1,1,1).setFlip(true));
		
		this.props.add(new Prop(PropTypes.testProp, 8,1,1).setRotation(1));
		
		this.props.add(new Prop(PropTypes.testProp, 8,1,8).setRotation(2));
		
		this.props.add(new Prop(PropTypes.testProp, 1,1,8).setRotation(3));
		
		this.props.add(new Prop(PropTypes.testProp, 6,1,4).setRotation(1).setFlip(true));
	}
	
	public StructureComponent getComponent(int id, Random rand) {
		return new ComponentSiteRoom(id, rand, this);
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
		
		LayoutNode node = new LayoutNode(minx, miny, minz, maxx, maxy, maxz);
		
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
