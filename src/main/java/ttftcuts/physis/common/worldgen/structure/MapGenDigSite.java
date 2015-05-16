package ttftcuts.physis.common.worldgen.structure;

import java.util.List;
import java.util.Random;

import ttftcuts.physis.common.worldgen.structure.layout.StructureLayout;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenDigSite extends MapGenStructure
{
    public MapGenDigSite()
    {

    }

    public String func_143025_a()
    {
        return "PhysisSite";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int k = chunkX >> 4;
        int l = chunkZ >> 4;
        this.rand.setSeed((long)(k ^ l << 4) ^ this.worldObj.getSeed());
        this.rand.nextInt();
        //return this.rand.nextInt(3) != 0 ? false : (p_75047_1_ != (k << 4) + 4 + this.rand.nextInt(8) ? false : p_75047_2_ == (l << 4) + 4 + this.rand.nextInt(8));
        return (Math.abs(chunkX) % 20 == 0) && (Math.abs(chunkZ) % 20 == 0);
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
    {
        return new MapGenDigSite.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }

    public static class Start extends StructureStart
    {

        public Start() {}

		@SuppressWarnings("unchecked")
		public Start(World world, Random rand, int chunkX, int chunkZ)
        {
            super(chunkX, chunkZ);

            StructureLayout layout = new StructureLayout((chunkX << 4) + 2, 192, (chunkZ << 4) + 2);
            List<StructureComponent> parts = layout.exportToStructureParts(rand);

            if (parts.size() == 0) { return; }
            
            StructureComponent first = parts.get(0);
            
            for (int i=0; i<parts.size(); i++) {
            	this.components.add(parts.get(i));
            	parts.get(i).buildComponent(first, this.components, rand);
            }
            
            this.updateBoundingBox();
        }
    }
}
