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
	
	public Material material;
	public SoundType sounds;
	
	public HashMap<String,IIcon[]> icons;
	
	public DigSiteLocale(String name, String base, String maskname, Material mat, SoundType sound) {
		this.name = name;
		this.base = base;
		this.maskname = maskname;
		
		this.material = mat;
		this.sounds = sound;
		
		this.icons = new HashMap<String,IIcon[]>();
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
