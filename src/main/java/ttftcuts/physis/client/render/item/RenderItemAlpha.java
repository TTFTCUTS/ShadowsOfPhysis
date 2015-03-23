package ttftcuts.physis.client.render.item;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ttftcuts.physis.common.helper.PhysisRenderHelper;
import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemAlpha implements IItemRenderer {

	@SuppressWarnings("unused")
	private RenderItem renderItem = new RenderItem();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type)
        {
            case ENTITY:
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
            case INVENTORY:
                return true;
            default:
                return false;
        }
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		//return type == ItemRenderType.ENTITY || type == ItemRenderType.INVENTORY;
		if (type == ItemRenderType.ENTITY) {
			return true;
		}
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Tessellator t = Tessellator.instance;
		GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
		switch (type)
        {
            case ENTITY:
            	GL11.glPushMatrix();
            	GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glTranslatef(-0.5F, 0F, 0F);
                renderItem(ItemRenderType.EQUIPPED, item, data);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glPopMatrix();
            	break;
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
            	int passes = item.getItem().getRenderPasses(item.getItemDamage());
                
            	for (int i = 0; i < passes; i++) {
                    IIcon icon = item.getItem().getIcon(item, i);
                    int col = item.getItem().getColorFromItemStack(item, i);

                    int r = TextureHelper.red(col);
                    int g = TextureHelper.green(col);
                    int b = TextureHelper.blue(col);
                    
                    float a = TextureHelper.alpha(col) / 255.0f;
                    boolean trans = false;
                    if (!(a == 0 || a == 1)) { 
                    	trans = true; 
                    } else {
                    	a = 1.0f;
                    }
                    
                    GL11.glColor4f(r/255.0f, g/255.0f, b/255.0f, a);
                    
                    if (trans) {
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                    }
                    
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                    ItemRenderer.renderItemIn2D(t, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
                    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                    
                    if (trans) {
                    	GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glDisable(GL11.GL_BLEND);
                    }
                }
                break;
            case INVENTORY:
            	GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                
            	PhysisRenderHelper.renderItemStack(item, 0, 0, false, false);
            	
            	GL11.glDisable(GL11.GL_BLEND);
                break;
            default:
        }
	}


}
