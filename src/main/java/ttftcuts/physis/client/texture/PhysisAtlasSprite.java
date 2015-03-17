package ttftcuts.physis.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class PhysisAtlasSprite extends TextureAtlasSprite {

	public ResourceLocation override;
	
	public PhysisAtlasSprite(String name) {
		super(name);
	}
	
	public PhysisAtlasSprite(String name, ResourceLocation resource) {
		this(name);
		this.override = resource;
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		return true;
	}
	
	@Override
	public boolean load(IResourceManager manager, ResourceLocation defaultlocation) {
		ResourceLocation location = override == null ? defaultlocation : override;
		//Physis.logger.info("default: "+defaultlocation+", override: "+override);
		BufferedImage image;
		
		try {
			image = TextureHelper.getImageForResource(location);
		} 
		catch (IOException e) {
			Physis.logger.error("Failed to load atlas sprite", e);
			return true;
		}
		
		int miplevels = Minecraft.getMinecraft().gameSettings.mipmapLevels;
		//Physis.logger.info("Miplevels: "+miplevels);
		
		BufferedImage[] output = new BufferedImage[miplevels+1];
		
		output[0] = image;
		
		if (miplevels > 1) {
			for (int i = 1; i<=miplevels; i++) {
				double scale = 1.0 / Math.pow(2,i);
				output[i] = TextureHelper.scaleBufferedImage(image, scale);
			}
		}

		
		this.loadSprite(output, null, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
		
		return false;
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public void generateMipmaps(int n) {
		int len = this.framesTextureData.size();
		BufferedImage first = (BufferedImage) this.framesTextureData.get(0);
		
		if (len < n) {
			for (int i=len; i<n; i++) {
				double scale = 1.0 / Math.pow(2,i);
				
				this.framesTextureData.add(TextureHelper.scaleBufferedImage(first, scale));
			}
		}
		
		super.generateMipmaps(n);
	}*/
}
