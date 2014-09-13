package ttftcuts.physis.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.block.BlockDigSite;
import ttftcuts.physis.common.helper.TextureHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class DigSiteAtlasSprite extends TextureAtlasSprite {
	
	public String derivedname;
	public String name;
	public String base;
	public String[] layers;
	public ResourceLocation[] resources;
	
	public DigSiteAtlasSprite(String name, String base, String[] names, ResourceLocation[] resources) {
		super(getDerivedName(name, base, names));
		this.derivedname = getDerivedName(name, base, names);
		this.name = name;
		this.base = base;
		this.layers = names;
		this.resources = resources;
	}
	
	public static String getDerivedName(String name, String base, String[] names) {
		StringBuilder b = new StringBuilder(Physis.MOD_ID);
		b.append(":").append(name).append("|").append(base != null ? base : "none").append("|");
		
		for(int i=0; i<names.length; i++) {
			if (i!=0) {
				b.append("_");
			}
			b.append(names[i].toLowerCase());
		}
		
		return b.toString();
	}
	
	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
		
		int mip = Minecraft.getMinecraft().gameSettings.mipmapLevels;
		
		BufferedImage[] outputtextures = new BufferedImage[1 + mip];
		BufferedImage basetexture;
		BufferedImage[] layertextures = new BufferedImage[this.layers.length];
		
		AnimationMetadataSection animation;
		
		int w = 0;
		
		try {
			ResourceLocation baseresource = base != null ? getBlockResource(base) : BlockDigSite.blankTexture;
			IResource baseiresource = manager.getResource(baseresource);
			basetexture = ImageIO.read(baseiresource.getInputStream());

			animation = (AnimationMetadataSection) baseiresource.getMetadata("animation");
			
			for (int i=0; i<this.layers.length; i++) {
				layertextures[i] = TextureHelper.getImageForResource(resources[i]);
			}
			
			w = layertextures[0].getWidth();
			
			if (basetexture.getWidth() != width) {
				List resourcePacks = manager.getAllResources(baseresource);
				for (int i= resourcePacks.size() - 1; i >= 0; --i) {
					IResource resource = (IResource) resourcePacks.get(i);
					basetexture = ImageIO.read(resource.getInputStream());
					
					if (basetexture.getWidth() == width) {
						break;
					}
				}
			}
			
		} catch(IOException e) {
			e.printStackTrace();
			return true;
		}
		
		if (basetexture.getWidth() != w) {
			Physis.logger.error("Failed to load base image of the right size for texture: "+name);
			return true;
		}
		
		int h = basetexture.getHeight();
		
		BufferedImage output = new BufferedImage(w, h, 2);
		
		int[] basedata = new int[w*w];
		int[][] layerdata = new int[layertextures.length][w*w];
		
		for (int y=0; y<h; y+=w) {
			basetexture.getRGB(0, y, w, w, basedata, 0, w);
			
			for(int i=0; i<layertextures.length; i++) {
				layertextures[i].getRGB(0, y, w, w, layerdata[i], 0, w);
			}
			
			int[] composited = buildTexture(w, basedata, layerdata);
			
			output.setRGB(0, y, w, w, composited, 0, w);
		}
		
		outputtextures[0] = output;
		
		this.loadSprite(outputtextures, animation, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
		
		//Physis.logger.info("Successfully built texture: "+this.derivedname);
		
		return false;
	}
	
	private int[] buildTexture(int size, int[] data, int[][] layerdata) {
		
		for (int i=0; i<data.length; i++) {
			for (int n=0; n<layerdata.length; n++) {
				int rd = red(data[i]);
				int gd = green(data[i]);
				int bd = blue(data[i]);
				int ad = alpha(data[i]);
				
				int rl = red(layerdata[n][i]);
				int gl = green(layerdata[n][i]);
				int bl = blue(layerdata[n][i]);
				int al = alpha(layerdata[n][i]);
				
				double alpha = al/255;
				double oneminus = 1.0 - alpha;
				
				int r = (int)(rd * oneminus + rl * alpha);
				int g = (int)(gd * oneminus + gl * alpha);
				int b = (int)(bd * oneminus + bl * alpha);
				int a = (int)(255 * (alpha + (ad/255)*oneminus));
				
				data[i] = compose(r,g,b,a);
			}
		}
		
		return data;
	}
	
	public static ResourceLocation getBlockResource(String s2) {
        String s1 = "minecraft";

        int ind = s2.indexOf(58);

        if (ind >= 0) {
            if (ind > 1) {
                s1 = s2.substring(0, ind);
            }

            s2 = s2.substring(ind + 1, s2.length());
        }

        s1 = s1.toLowerCase();
        s2 = "textures/blocks/" + s2 + ".png";

        return new ResourceLocation(s1, s2);
    }
	
	private int alpha(int c) {
		return (c >> 24) & 0xFF;
	}

	private int red(int c) {
		return (c >> 16) & 0xFF;
	}

	private int green(int c) {
		return (c >> 8) & 0xFF;
	}

	private int blue(int c) {
		return (c) & 0xFF;
	}
	
	private int compose(int r, int g, int b, int a) {
		int rgb = a;
		rgb = (rgb << 8) + r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
		return rgb;
	}
}
