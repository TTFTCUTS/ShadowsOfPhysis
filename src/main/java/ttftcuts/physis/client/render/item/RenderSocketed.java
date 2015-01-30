package ttftcuts.physis.client.render.item;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
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
				
				for (int i=0; i<sockets.length; i++) {
					if (sockets[i] != null) {
						filled++;
					}
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
