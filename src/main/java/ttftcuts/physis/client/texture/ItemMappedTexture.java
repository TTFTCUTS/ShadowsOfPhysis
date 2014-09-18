package ttftcuts.physis.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ItemMappedTexture extends PhysisAbstractTexture {

	BufferedImage sourceImage;
	ResourceLocation mapImage;

	public ItemMappedTexture(BufferedImage sourceImage, ResourceLocation mapImage) {
		super();
		
		this.sourceImage = sourceImage;
		this.mapImage = mapImage;
	}
	
	@Override
	public void loadTexture(IResourceManager manager) throws IOException {
		
		TextureUtil.uploadTextureImage(this.getGlTextureId(), this.sourceImage);
		this.generatedImage = this.sourceImage;
	}

}
