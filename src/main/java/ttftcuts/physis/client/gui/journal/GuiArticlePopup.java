package ttftcuts.physis.client.gui.journal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ttftcuts.physis.Physis;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiArticlePopup extends Gui
{
    private static final ResourceLocation texture = new ResourceLocation(Physis.MOD_ID, "textures/gui/inventory.png");
    private Minecraft mc;
    private int width;
    private int height;
    private String text;
    private JournalArticle article;
    private long time;
    private RenderItem renderItem;
    private boolean shouldRender;

    public GuiArticlePopup(Minecraft mc)
    {
        this.mc = mc;
        this.renderItem = new RenderItem();
        this.shouldRender = false;
    }

    public void setArticleInfo(JournalArticle article) {
    	this.text = article.getTranslatedName();
    	this.time = Minecraft.getSystemTime();
    	this.article = article;
    	this.shouldRender = true;
    }

    private void updateScale()
    {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.width, (double)this.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void update()
    {
        if (this.shouldRender && this.article != null && this.time != 0L && Minecraft.getMinecraft().thePlayer != null)
        {
            double d0 = (double)(Minecraft.getSystemTime() - this.time) / 3000.0D;
            
            if (d0 < -5.0 || d0 > 1.0)
            {
            	this.shouldRender = false;
            	this.time = 0L;
            	
            	return;
            }

            this.updateScale();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            double d1 = d0 * 2.0D;

            if (d1 > 1.0D)
            {
                d1 = 2.0D - d1;
            }

            d1 *= 4.0D;
            d1 = 1.0D - d1;

            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 *= d1;
            d1 *= d1;
            int i = this.width - 188;
            int j = 0 - (int)(d1 * 36.0D);

            if (j < -32) { return; }
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.mc.getTextureManager().bindTexture(texture);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.drawTexturedModalRect(i, j, 68, 224, 188, 32);

            this.mc.fontRenderer.drawString(Physis.text.translate("physis.journal.newinfo"), i + 58, j + 7, -256);
            this.mc.fontRenderer.drawString(this.text, i + 58, j + 18, -1);

            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            
            if (this.article.iconstack != null) {
            	this.renderItem.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), this.article.iconstack, i + 35, j + 8);
            }
            
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void clear()
    {
        this.article = null;
        this.time = 0L;
    }
}
