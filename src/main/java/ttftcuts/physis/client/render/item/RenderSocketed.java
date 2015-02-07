package ttftcuts.physis.client.render.item;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.helper.PhysisRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderSocketed implements IItemRenderer {

	public IItemRenderer wrapped;
	
	public static ResourceLocation overlay = new ResourceLocation(Physis.MOD_ID +":textures/gui/socketoverlay.png");
	
	public RenderSocketed(IItemRenderer wrappedRenderer) {
		this.wrapped = wrappedRenderer;
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if (wrapped == null) {
			return type == ItemRenderType.INVENTORY;
		}
		return type == ItemRenderType.INVENTORY || wrapped.handleRenderType(item, type);
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		if (wrapped == null) {
			return false;
		}
		return wrapped.shouldUseRenderHelper(type, item, helper);
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Minecraft mc = Minecraft.getMinecraft();
		int iconsize = 6;
		double usize = iconsize/32.0;
		double vsize = iconsize/16.0;
		
		if (wrapped != null && wrapped.handleRenderType(item, type)) {
			this.wrapped.renderItem(type, item, data);
		} else {
			PhysisRenderHelper.renderItemStack(item, 0, 0, false);
			
		}

		if (type == ItemRenderType.INVENTORY) {
			RenderHelper.disableStandardItemLighting();
			boolean drawicon = false;
			int icon = 0;
			
			int numsockets = PhysisArtifacts.getSocketCount(item);
			if (numsockets > 0) {
				NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(item);

				int filled = 0;
				
				long longestcooldown = 0;
				int cooldownid = -1;
				
				for (int i=0; i<sockets.length; i++) {
					if (sockets[i] != null) {
						filled++;
						
						long cd = PhysisArtifacts.getEffectCooldown(sockets[i], true);
						if (cd >= longestcooldown) {
							longestcooldown = cd;
							cooldownid = i;
						}
					}
				}
				
				if (cooldownid != -1) {
					NBTTagCompound cdsocket = sockets[cooldownid];
					IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(cdsocket);
					IArtifactEffect effect = PhysisArtifacts.getEffectFromSocketable(cdsocket);
					
					long maxcd = effect.getCooldown(trigger.getCooldownCategory());
					
					double fraction = maxcd <= 0 ? 0 : longestcooldown / (double)maxcd; 
					
					this.renderCooldown(fraction);
				}
				
				drawicon = true;
				if (filled == 0) {
					icon = 1;
				} else if (filled == numsockets) {
					icon = 3;
				} else {
					icon = 2;
				}
			} else if (mc.thePlayer != null) {
				ItemStack held = mc.thePlayer.getHeldItem();
				if (held != null && held.getItem() == PhysisItems.addsocket) {
					drawicon = true;
					icon = 0;
				}
			}
			
			if (drawicon) {
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glColor4f(1f, 1f, 1f, 1f);
				Tessellator t = Tessellator.instance;
				mc.renderEngine.bindTexture(overlay);
				
				int x = 0;
				int y = 0;
				int w = iconsize;
				int h = iconsize;
				
				t.startDrawingQuads();
				t.addVertexWithUV(x, y+h, 0, (icon)*usize, vsize);
				t.addVertexWithUV(x+w, y+h, 0, (icon+1)*usize, vsize);
				t.addVertexWithUV(x+w, y, 0, (icon+1)*usize, 0);
				t.addVertexWithUV(x, y, 0, (icon)*usize, 0);
				t.draw();
			}
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
	
	
	
	public void renderCooldown(double f) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		int w = 16;
		int h = 16;
		//double u = 9/32.0;
		//double v = 3/16.0;
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(overlay);
		
		if (f>0.75) { this.renderCooldownSector((f-0.75)*4,w/2,0,w/2,h/2,0); }
		if (f>0.5) 	{ this.renderCooldownSector((f-0.5)*4,w/2,h/2,w/2,h/2,1); }
		if (f>0.25) { this.renderCooldownSector((f-0.25)*4,0,h/2,w/2,h/2,2); }
		if (f>0) 	{ this.renderCooldownSector((f)*4,0,0,w/2,h/2,3); };
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	private void renderCooldownSector(double f, double x, double y, double w, double h, int rotation) {
		double x1 = 0;
		double y1 = 0;
		double x2 = w;
		double y2 = 0;
		
		f = Math.max(0, Math.min(1.0, (1-f)));
		
		if (f <= 0.5) {
			double angle = f*0.5*Math.PI;
			x1 = Math.tan(angle) * w;
			y1 = 0;
		} else {
			double angle = (1.0-f)*0.5*Math.PI;
			x1 = w;
			y1 = (1.0-Math.tan(angle)) * h;
			x2 = x1;
			y2 = y1;
		}
		
		double u = 9/32.0;
		double v = 3/16.0;
		
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setColorRGBA(0, 0, 0, 127);
		if (rotation == 0) {
			t.addVertexWithUV(x, 		y+h, 		0, u, v);
			t.addVertexWithUV(x+w, 		y+h, 		0, u, v);
			t.addVertexWithUV(x+x2, 	y+y2, 		0, u, v);
			t.addVertexWithUV(x+x1, 	y+y1, 		0, u, v);
		} else if (rotation == 1) {
			t.addVertexWithUV(x, 		y+h, 		0, u, v);
			t.addVertexWithUV(x+w-y1, 	y+x2, 		0, u, v);
			t.addVertexWithUV(x+w-y2, 	y+x1,		0, u, v);
			t.addVertexWithUV(x, 		y, 			0, u, v);
		} else if (rotation == 2) {
			t.addVertexWithUV(x+h-x2, 	y+h-y2,		0, u, v);
			t.addVertexWithUV(x+w-x1,	y+h-y1,		0, u, v);
			t.addVertexWithUV(x+w, 		y, 			0, u, v);
			t.addVertexWithUV(x, 		y, 			0, u, v);
		} else {
			t.addVertexWithUV(x+y1,		y+h-x1, 	0, u, v);
			t.addVertexWithUV(x+w, 		y+h, 		0, u, v);
			t.addVertexWithUV(x+w, 		y, 			0, u, v);
			t.addVertexWithUV(x+y2, 	y+x2-w, 	0, u, v);
		}
		t.draw();
	}

	// registering magics
	@SuppressWarnings("rawtypes")
	public static void injectRenderer() {
		Iterator iter = Item.itemRegistry.iterator();

		Item item;
		ItemStack stack;
		IItemRenderer render;
		while (iter.hasNext()) {
			item = (Item)(iter.next());
			stack = new ItemStack(item);
			
			if (!PhysisArtifacts.canItemAcceptSockets(stack)) { 
				continue; 
			}
			
			render = MinecraftForgeClient.getItemRenderer(stack, ItemRenderType.INVENTORY);
			
			MinecraftForgeClient.registerItemRenderer(item, new RenderSocketed(render));
		}
	}
}
