package ttftcuts.physis.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import scala.actors.threadpool.Arrays;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.artifact.PhysisArtifacts.WeightedEffect;
import ttftcuts.physis.common.artifact.PhysisArtifacts.WeightedTrigger;
import ttftcuts.physis.common.helper.TextureHelper;

public class ItemSocketable extends ItemPhysis implements ISocketable {

	public IIcon[][] icons;
	public static final int ICONTYPES = 1;
	
	public ItemSocketable() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("socketable");
		this.setTextureName(Physis.MOD_ID+":socketable/gem0_0");
	}
	
	@Override
	public int getSocketColour(ItemStack stack) {
		return 5;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List types) {
		
		for(WeightedTrigger wtrig : PhysisArtifacts.triggers.values()) {
			IArtifactTrigger trigger = wtrig.theTrigger;
			
			for(WeightedEffect weff : PhysisArtifacts.effects.values()) {
				IArtifactEffect effect = weff.theEffect;
				
				ItemStack stack = new ItemStack(this);
				
				PhysisArtifacts.addTriggerAndEffectToItem(stack, trigger, effect);
				
				types.add(stack);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
	@Override
	public int getRenderPasses(int metadata) {
		return 9;
	}
	
	@Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
		int type = 0;
		
		return icons[type][pass];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
		double fraction = (7-pass)/7.0;
		double gfract = Math.min(1.0, Math.max(0.0, fraction * 1.25 - 0.65));
		double l = fraction * 0.95 + 0.025;
		double h = 0;
		double s = 0;
		
		IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(stack);
		IArtifactEffect effect = PhysisArtifacts.getEffectFromSocketable(stack);
		
		if (trigger != null && effect != null) {
			double thue = trigger.getHue();
			double tsat = trigger.getSaturation();
			double ehue = effect.getHue();
			double esat = effect.getSaturation();
			
			double h1 = ehue;
			double s1 = esat;
			double h2 = thue;
			double s2 = tsat;
			
			if (Math.abs(h2-h1) > 0.5) {
				if (h2 > h1) {
					h2 -= 1;
				} else {
					h1 -= 1;
				}
			}
			
			h = gfract * h1 + (1-gfract) * h2;
			if (h < 0) h += 1;
			if (h > 1) h -= 1;
			s = gfract * s1 + (1-gfract) * s2;
			
			if (pass == 8) {
				h = h1;
			}
		}
		
		if (pass == 8) {
			l = 0.65;
			s = 1.0;
		}
		
		int[] rgb = TextureHelper.hsl2rgb(h, s, l);
		
		return TextureHelper.compose(rgb);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        super.registerIcons(register);
        
    	icons = new IIcon[ICONTYPES][];
    	
        for (int i=0; i<ICONTYPES; i++) {
        	IIcon[] types = new IIcon[9];
        	for (int j=0; j<9; j++) {
        		types[j] = register.registerIcon(Physis.MOD_ID+":socketable/gem"+i+"_"+j);
        	}
        	icons[i] = types;
        }
        Physis.logger.info("Socketable Icons: "+Arrays.deepToString(icons));
	}
}
