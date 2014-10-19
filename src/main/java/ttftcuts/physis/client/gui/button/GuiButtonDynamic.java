package ttftcuts.physis.client.gui.button;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class GuiButtonDynamic extends GuiButton {

	private ResourceLocation texture;
	private int u,v;
	
	public GuiButtonDynamic(int id, int x, int y, int u, int v, int w, int h, String caption, ResourceLocation texture) {
		super(id, x, y, w, h, caption);
		this.texture = texture;
		this.u = u;
		this.v = v;
	}

	@Override
	public void drawButton(Minecraft mc, int mousex, int mousey)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(this.texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = mousex >= this.xPosition && mousey >= this.yPosition && mousex < this.xPosition + this.width && mousey < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v + k * this.height, this.width, this.height);
            this.mouseDragged(mc, mousex, mousey);
            int l = 14737632;

            if (packedFGColour != 0)
            {
                l = packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.field_146123_n)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
        }
    }
}
