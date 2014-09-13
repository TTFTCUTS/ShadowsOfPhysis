package ttftcuts.physis.common.helper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import ttftcuts.physis.client.texture.DigSiteAtlasSprite;
import ttftcuts.physis.client.texture.PhysisAbstractTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TextureHelper {

	public static ResourceLocation loadTexture(String location, ITextureObject texture) {
		ResourceLocation resource = new ResourceLocation(location);
		Minecraft.getMinecraft().getTextureManager().loadTexture(resource, texture);
		return resource;
	}
	
	public static IIcon buildDigSprite(TextureMap map, String iconname, String base, String[] layernames, ResourceLocation[] layers) {
		String name = DigSiteAtlasSprite.getDerivedName(iconname, base, layernames);
		
		TextureAtlasSprite texture = map.getTextureExtry(name);
		if (texture == null) {
			texture = new DigSiteAtlasSprite(iconname, base, layernames, layers);
			map.setTextureEntry(name, texture);
		}
		
		return map.getTextureExtry(name);
	}
	
	public static BufferedImage getImageForResource(ResourceLocation resource) throws IOException{
		ITextureObject texob = Minecraft.getMinecraft().getTextureManager().getTexture(resource);
		
		if( texob instanceof PhysisAbstractTexture) {
			return ((PhysisAbstractTexture)texob).generatedImage;
		} else {
			InputStream layer = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
			return ImageIO.read(layer);
		}
	}
}
