package ttftcuts.physis.common.worldgen.structure.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.BlockPalette;
import ttftcuts.physis.common.worldgen.structure.BlockPalette.BlockPalettes;
import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;
import ttftcuts.physis.common.worldgen.structure.prop.Prop;
import ttftcuts.physis.common.worldgen.structure.prop.PropTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class LayoutNode {
	public StructureBoundingBox bounds;
	
	public BlockPalette palette;
	public List<Prop> props;
	
	public LayoutNode(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPalette palette) {
		this.bounds = new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		this.palette = palette;
		this.props = new ArrayList<Prop>();
	}
	
	public LayoutNode placeProps() {
		this.props.add(new Prop(PropTypes.paletteTest, 10,0,10).updateBounds());
		
		return this;
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
		
		tag.setInteger("pal", node.palette.id);
		
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
		
		BlockPalette palette = BlockPalettes.paletteRegistry.get(tag.getInteger("pal"));
		
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
