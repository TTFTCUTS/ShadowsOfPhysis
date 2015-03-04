package ttftcuts.physis.common.helper;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.texture.DigSiteAtlasSprite;
import ttftcuts.physis.client.texture.PhysisAbstractTexture;
import ttftcuts.physis.client.texture.PhysisAtlasSprite;
import ttftcuts.physis.utils.ColourBrightnessComparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
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
	
	public static List<Integer> getImageColourRange(BufferedImage image) {
		
		List<Integer> collist = new ArrayList<Integer>();
		
		int w = image.getWidth();
		int h = image.getHeight();
		int len = w*h;
		int[] data = new int[len];
		image.getRGB(0, 0, w, h, data, 0, w);
		
		int col, a;
		
		for(int i=0; i<len; i++) {
			col = data[i];
			a = alpha(col);
			
			if (a > 0) {
				collist.add(col);
			}
		}
		
		Collections.sort(collist, new ColourBrightnessComparator());
		
		return collist;
	}
	
	public static int getAverageColour(List<Integer> colours) {
		int r = 0;
		int g = 0;
		int b = 0;
		int col;
		
		int count = colours.size();
		
		for (int i=0; i<count; i++) {
			col = colours.get(i);
			r += red(col);
			g += green(col);
			b += blue(col);
		}
		
		return compose(r / count, g / count, b / count, 255);
	}
	
	public static int getPerceptualBrightness(int col) {
		double r = red(col) / 255.0;
		double g = green(col) / 255.0;
		double b = blue(col) / 255.0;
		
		double brightness = Math.sqrt(0.241 * r*r + 0.691 * g*g + 0.068 * b*b);
		
		return (int)(brightness*255);
	}
	
	// Bitwise colour ops!
	
	public static int compose(int r, int g, int b, int a) {
		int rgb = a;
		rgb = (rgb << 8) + r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
		return rgb;
	}
	
	public static int compose(int[] col) {
		int r = col[0];
		int g = col[1];
		int b = col[2];
		int a = col.length > 3 ? col[3] : 255;
		return compose(r,g,b,a);
	}

	public static int alpha(int c) {
		return (c >> 24) & 0xFF;
	}

	public static int red(int c) {
		return (c >> 16) & 0xFF;
	}

	public static int green(int c) {
		return (c >> 8) & 0xFF;
	}

	public static int blue(int c) {
		return (c) & 0xFF;
	}
	
	protected static int mult(int c1, int c2) {
		return (int)(c1 * (c2/255.0));
	}
	
	protected static double hue2rgb(double p, double q, double t) {
		if (t < 0) t += 1;
		if (t > 1) t -= 1;
		if (t < 1/6.0) return p + (q - p) * 6 * t;
		if (t < 1/2.0) return q;
		if (t < 2/3.0) return p + (q - p) * (2/3.0 - t) * 6;
		return p;
	}
	
	public static int[] hsl2rgb(double h, double s, double l) {
		//h *= 360.0;
		
		double r,g,b;
		
		if (s<=0) {
			r = g = b = l;
		} else {
			double q = l < 0.5 ? l * (1+s) : l + s - l*s;
			double p = 2 * l - q;
			r = hue2rgb(p, q, h + 1/3.0);
			g = hue2rgb(p, q, h);
			b = hue2rgb(p, q, h - 1/3.0);
		}
		
		return new int[]{(int)Math.round(r * 255), (int)Math.round(g * 255), (int)Math.round(b * 255)};
	}
	
	public static double[] rgb2hsl(int r, int g, int b) {
		double dr = r/255.0;
		double dg = g/255.0;
		double db = b/255.0;
		double max = Math.max(dr, Math.max(dg, db));
		double min = Math.min(dr, Math.min(dg, db));
		
		double h, s;
		double l = (max+min)*0.5;
		
		if (max == min) {
			h = 0;
			s = 0;
		} else {
			double d = max - min;
			s = l > 0.5 ? d / (2.0 - max - min) : d / (max + min);
			
			if (max == dr) {
				h = (dg - db) / d + (dg < db ? 6 : 0);
			} else if (max == dg) {
				h = (db - dr) / d + 2;
			} else {
				h = (dr - dg) / d + 4;
			}
			h /= 6.0;
		}
		
		return new double[]{h,s,l};
	}
	
	public static double[] rgb2hsl(int colour) {
		return rgb2hsl(red(colour), green(colour), blue(colour));
	}
}
