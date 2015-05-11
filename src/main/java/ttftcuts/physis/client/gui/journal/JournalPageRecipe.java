package ttftcuts.physis.client.gui.journal;

import java.util.List;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.common.helper.PhysisRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class JournalPageRecipe extends JournalPage {
	public static final ResourceLocation craftingtextures = new ResourceLocation(Physis.MOD_ID, "textures/gui/crafting_overlays.png"); 
	
	public List<ItemStack>[] inputstacks;
	public ItemStack outputstack;
	
	public JournalPageRecipe(ItemStack outputstack) {
		this.outputstack = outputstack;
	}
	
	public String getDescription() { return "test recipe description blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah"; }
	public void drawRecipe(GuiJournal journal, int x, int y, int mousex, int mousey) {};
	
	public void getRecipeData() {}
	
	public void drawItemStackPerm(GuiJournal journal, List<ItemStack> stacks, int x, int y, int mouseX, int mouseY, boolean encrypt) {
		if (!stacks.isEmpty()) {
			int perm = (int) (System.nanoTime()/1000000000 % (stacks.size()));
			this.drawItemStack(journal, stacks.get(perm), x, y, mouseX, mouseY, encrypt);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void drawItemStack(GuiJournal journal, ItemStack stack, int x, int y, int mouseX, int mouseY, boolean encrypt) {
		if (encrypt) {
			GL11.glColor4f(1.0f, 0.85f, 0.5f, 1f);
		}
		PhysisRenderHelper.renderItemStack(stack, x, y, true, true, encrypt);
		if (mouseX >= x && mouseX < x+16 && mouseY >= y && mouseY < y+16) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player != null) {
				journal.setTooltip(stack.getTooltip(player, false));
				if (encrypt) {
					journal.setTooltipRenderer(Physis.runeFontRenderer);
				}
			}
		}
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	@Override
	public void drawPage(GuiJournal journal, int x, int y, int mousex, int mousey) {
		float jz = journal.getZLevel();
		journal.setZLevel(jz+10);
		
		if (this.inputstacks == null) {
			this.getRecipeData();
		}
		
		FontRenderer renderer = this.canView() ? journal.mc.fontRenderer : Physis.runeFontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		
		renderer.setUnicodeFlag(false);
		
		String title = this.outputstack.getDisplayName();
		renderer.drawString(title, x + (GuiJournal.pageWidth / 2) - (renderer.getStringWidth(title) / 2), y + 6, 0x000000);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		journal.mc.renderEngine.bindTexture(GuiJournal.bookTextureRight);
		
		int width = renderer.getStringWidth(title);
		if (width + 38 <= GuiJournal.pageWidth) {
			journal.drawTexturedModalRect(x + (GuiJournal.pageWidth / 2) - (width / 2) - 19, y + 3, 350, 26, 16, 13);
			journal.drawTexturedModalRect(x + (GuiJournal.pageWidth / 2) + (width / 2) + 3, y + 3, 366, 26, 16, 13);
		}
		
		this.drawRecipe(journal, x, y, mousex, mousey);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		
		renderer.setUnicodeFlag(true);
		String desc = this.getDescription();
		if (desc != null && desc.length() > 0) {
			renderer.drawSplitString(desc, x, y+158, GuiJournal.pageWidth, 0x000000);
		}
		
		renderer.setUnicodeFlag(unicode);
		
		journal.setZLevel(jz);
	}
}
