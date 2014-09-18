package ttftcuts.physis.common.helper;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.texture.DigSiteAtlasSprite;
import ttftcuts.physis.client.texture.PhysisAbstractTexture;
import ttftcuts.physis.client.texture.PhysisAtlasSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TextureHelper {

	public static ResourceLocation loadTexture(String location, ITextureObject texture) {
		ResourceLocation resource = new ResourceLocation(Physis.MOD_ID, location);
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
	
	public static Map<String, TextureAtlasSprite> addedIcons; 
	
	public static IIcon getIconForDynamicResource(TextureMap map, String resourcename, ResourceLocation resource) {
		if (addedIcons == null) {
			addedIcons = new HashMap<String, TextureAtlasSprite>();
		}

		TextureAtlasSprite texture = new PhysisAtlasSprite(resourcename, resource);
		if (addedIcons.containsKey(resourcename)) {
			addedIcons.remove(resourcename);
		}
		addedIcons.put(resourcename, texture);
		
		return texture;
	}
	
	public static Framebuffer stackBuffer; 
	public static BufferedImage getItemStackImage(ItemStack stack) {
		if (stackBuffer == null) {
			stackBuffer = new Framebuffer(16,16,true);
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		
		// bind the buffer and such
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		stackBuffer.bindFramebuffer(true);
		
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, 16, 16, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        
        //GL11.glTranslatef(0, 0, -5);

        GL11.glColor3f(1, 0, 0);
		
		// draw image
		RenderItem render = new RenderItem();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		render.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, 0, 0);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		
		// set back to normal
		stackBuffer.unbindFramebuffer();
		GL11.glPopMatrix();
		
		// dump to image
		int width = 16;
		int height = 16;
		int length = width * height;
		IntBuffer data = BufferUtils.createIntBuffer(length);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, stackBuffer.framebufferTexture);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, data);
		
        int[] dataarray = new int[length];
        data.get(dataarray);
        TextureUtil.func_147953_a(dataarray, 16, 16);
        
        int l = stackBuffer.framebufferTextureHeight - stackBuffer.framebufferHeight;
        
        image.setRGB(0, 0, 16, 16, dataarray, 0, 16);
        
        for (int i1 = l; i1 < stackBuffer.framebufferTextureHeight; ++i1)
        {
            for (int j1 = 0; j1 < stackBuffer.framebufferWidth; ++j1)
            {
                image.setRGB(j1, i1 - l, dataarray[i1 * stackBuffer.framebufferTextureWidth + j1]);
            }
        }
        
		// send out the image
		return image;
	}
	
	public static BufferedImage scaleBufferedImage(BufferedImage original, double ratio) {
		int newWidth = new Double(original.getWidth() * ratio).intValue();
		int newHeight = new Double(original.getHeight() * ratio).intValue();
		
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
	    Graphics2D g = resized.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
	    g.dispose();
	    return resized;
	}
}
