package ttftcuts.physis.common.block.tile;

import java.util.HashMap;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.texture.DigStripTexture;
import ttftcuts.physis.common.block.BlockDigSite;
import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class DigSiteLocale {
	public String name;
	public String base;
	public String maskname;
	
	public String shapename1;
	public String shapename2;
	public String shapename3;
	
	public Material material;
	public SoundType sounds;
	
	public HashMap<String,IIcon[]> icons;
	public IIcon[][] shapes;
	public int[] colours;
	
	public DigSiteLocale(String name, String base, String maskname, Material mat, SoundType sound, String shapename1, String shapename2, String shapename3, int colour1, int colour2, int colour3) {
		this.name = name;
		this.base = base;
		this.maskname = maskname;
		
		this.material = mat;
		this.sounds = sound;
		
		this.shapename1 = shapename1;
		this.shapename2 = shapename2;
		this.shapename3 = shapename3;
		
		this.icons = new HashMap<String,IIcon[]>();
		int[] c = {colour1, colour2, colour3};
		this.colours = c;
	}
	
	public void addIconSet(TextureMap map, String artifactname) {
		int n = DigStripTexture.numFrames;
		IIcon[] set = new IIcon[n];
		String texname = BlockDigSite.getDigTextureName(maskname, artifactname);
		
		for (int i=0; i<n; i++) {
			String framename = texname + "_" + i;
			ResourceLocation frame = new ResourceLocation(Physis.MOD_ID, framename);
			set[i] = TextureHelper.buildDigSprite(map, "digsite_"+name, base, new String[]{maskname+i}, new ResourceLocation[]{frame});
		}
		
		icons.put(artifactname, set);
	}
}
