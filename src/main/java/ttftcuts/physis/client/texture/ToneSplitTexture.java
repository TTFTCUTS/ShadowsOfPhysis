package ttftcuts.physis.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import ttftcuts.physis.Physis;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ToneSplitTexture extends PhysisAbstractTexture {

	private ResourceLocation resource;
	private int tone;
	
	public ToneSplitTexture(ResourceLocation resource, int tone) {
		this.resource = resource;
		this.tone = tone;
	}
	
	@Override
	public void loadTexture(IResourceManager manager) throws IOException {
		this.deleteGlTexture();
        BufferedImage texture = null;

        try {
        	if (this.resource != null) {
        		InputStream stream = manager.getResource(this.resource).getInputStream();
        		texture = ImageIO.read(stream);
        		
        		int w = texture.getWidth();
        		int h = texture.getHeight();
        		int length = w*h;
        		int[] pixels = new int[length];
        		
        		texture.getRGB(0, 0, w, h, pixels, 0, w);
        		
        		int c,r,a;
        		List<Integer> tones = new ArrayList<Integer>();
        		
        		for (int i=0; i<length; i++) {
        			c = pixels[i];
        			a = alpha(c);
        			r = red(c);
        			
        			if (a != 0) {
        				if(!tones.contains(r)) {
        					tones.add(r);
        				}
        			}
        		}
        		
        		Collections.sort(tones);
        		//Physis.logger.info(this.resource +": "+ tones);
        		int tonenum = tones.get(Math.min(this.tone, tones.size()-1));
        		
        		for (int i=0; i<length; i++) {
        			c = pixels[i];
        			a = alpha(c);
        			r = red(c);
        			
        			if (a != 0 && r == tonenum) {
        				pixels[i] = 0xFFFFFFFF;
        			} else {
        				pixels[i] = 0x00FFFFFF;
        			}
        		}
        		
        		texture.setRGB(0, 0, w, h, pixels, 0, w);
        	}
        } 
        catch(IOException e) {
        	Physis.logger.error("Couldn\'t load tone split texture "+this.resource+", tone "+this.tone, e);
            return;
        }
        
        TextureUtil.uploadTextureImage(this.getGlTextureId(), texture);
		this.generatedImage = texture;
	}

}
