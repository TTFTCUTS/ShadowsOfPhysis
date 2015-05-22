package ttftcuts.physis.common.worldgen.structure.prop;

import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.ComponentSiteRoom;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;


public abstract class PropType {
	public final String id;
	
	public static final StructureBoundingBox defaultBounds = new StructureBoundingBox(0,0,0,0,0,0);
	
	public StructureBoundingBox bounds = defaultBounds;
	
	public PropType(String name) {
		this.id = name;
		PropTypes.propTypes.put(name,this);
	}
	
	public StructureBoundingBox getBoundingBoxForProp(Prop prop) {
		return new StructureBoundingBox(-1, 0, -1, 2, 9, 2);
	}
	
	public void buildProp(ComponentSiteRoom component, Prop prop, World world, StructureBoundingBox limit, Random rand) {

	}
}
