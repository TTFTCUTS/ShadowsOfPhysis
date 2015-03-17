package ttftcuts.physis.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ttftcuts.physis.Physis;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class DigStripTexture extends PhysisAbstractTexture {

	public static final int numFrames = 8;
	
	public String name;
	private ResourceLocation digstrip;
	private ResourceLocation uncovered;
	private int frame;
	
	public DigStripTexture(String name, ResourceLocation digstrip, ResourceLocation uncovered, int frame) {
		super();
		
		this.name = name;
		this.digstrip = digstrip;
		this.uncovered = uncovered;
		this.frame = frame;
	}
	
	@Override
	public void loadTexture(IResourceManager manager) throws IOException {
		this.deleteGlTexture();
		BufferedImage texture = null;

		try
		{
			if (digstrip != null && uncovered != null) {
				BufferedImage striptexture = ImageIO.read(manager.getResource(digstrip).getInputStream());
				BufferedImage objecttexture = ImageIO.read(manager.getResource(uncovered).getInputStream());
				
				int w = objecttexture.getWidth();
				texture = striptexture.getSubimage(w * frame, 0, w, w);
				int length = w*w;
				
				int[] stripdata = new int[length];
				texture.getRGB(0, 0, w, w, stripdata, 0, w);
				
				int[] objectdata = new int[length];
				objecttexture.getRGB(0, 0, w, w, objectdata, 0, w);
				
				int c1,r1,a1;
				int c2,r2,g2,b2,a2;
				
				for (int i=0; i<length; i++) {
					c1 = stripdata[i];
					r1 = red(c1);
					a1 = alpha(c1);
					
					c2 = objectdata[i];
					r2 = red(c2);
					g2 = green(c2);
					b2 = blue(c2);
					a2 = alpha(c2);
					
					stripdata[i] = compose(mult(r2, r1), mult(g2, r1), mult(b2, r1), mult(a1, a2));
				}
				
				texture.setRGB(0, 0, w, w, stripdata, 0, w);
			}
		}
		catch (IOException ioexception)
        {
        	Physis.logger.error("Couldn\'t load dig strip texture "+name, ioexception);
            return;
        }
		
		TextureUtil.uploadTextureImage(this.getGlTextureId(), texture);
		this.generatedImage = texture;
	}

}
