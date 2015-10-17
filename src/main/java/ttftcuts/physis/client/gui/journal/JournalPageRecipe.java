package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.ClientProxy;
import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.client.gui.button.GuiButtonJournal;
import ttftcuts.physis.common.helper.PhysisRenderHelper;
import ttftcuts.physis.common.helper.recipe.RecipeDisplayData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class JournalPageRecipe extends JournalPage {
	public static final ResourceLocation craftingtextures = new ResourceLocation(Physis.MOD_ID, "textures/gui/crafting_overlays.png"); 
	
	public List<RecipeDisplayData> displayrecipes;
	public int currentRecipe = 0;
	public ItemStack[] outputstacks;
	protected boolean initialised = false;
	
	protected int buttonPrevTop = 143;
	protected int buttonPrevLeft = 36;
	protected int buttonNextTop = 143;
	protected int buttonNextLeft = 91;
	
	protected GuiButtonJournal prevbutton;
	protected GuiButtonJournal nextbutton;
	
	protected int recipeNumberTop = 143;
	protected int recipeNumberMiddle = GuiJournal.pageWidth/2;
	
	public JournalPageRecipe(ItemStack... outputstacks) {
		this.outputstacks = outputstacks;
	}
	
	public String getDescription() { return "test recipe description blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah"; }
	public void drawRecipe(GuiJournal journal, RecipeDisplayData recipe, int x, int y, int mousex, int mousey) {};
	
	public void getRecipeData() {}
	
	@SideOnly(Side.CLIENT)
	public void drawItemStackPerm(GuiJournal journal, ItemStack[] stacks, int x, int y, int mouseX, int mouseY, boolean encrypt) {
		if (stacks.length>0) {
			int perm = (int) (System.nanoTime()/1000000000 % (stacks.length));
			this.drawItemStack(journal, stacks[perm], x, y, mouseX, mouseY, encrypt);
		}
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void drawItemStack(GuiJournal journal, ItemStack stack, int x, int y, int mouseX, int mouseY, boolean encrypt) {
		if (stack == null) { return; }
		if (encrypt) {
			GL11.glColor4f(1.0f, 0.85f, 0.5f, 1f);
		}
		PhysisRenderHelper.renderItemStack(stack, x, y, true, true, encrypt);
		if (mouseX >= x && mouseX < x+16 && mouseY >= y && mouseY < y+16) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player != null) {
				journal.setTooltip(stack.getTooltip(player, false));
				if (encrypt) {
					journal.setTooltipRenderer(ClientProxy.runeFontRenderer);
				}
			}
		}
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawPage(GuiJournal journal, int x, int y, int mousex, int mousey) {
		float jz = journal.getZLevel();
		journal.setZLevel(jz+10);
		
		if (!this.initialised) {
			this.initialised = true;
			this.getRecipeData();
		}
		
		FontRenderer renderer = this.canView() ? journal.mc.fontRenderer : ClientProxy.runeFontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		
		renderer.setUnicodeFlag(false);
		
		RecipeDisplayData recipe = this.displayrecipes.get(this.currentRecipe);
		
		String title = recipe.output.getDisplayName();
		//renderer.drawString(title, x + (GuiJournal.pageWidth / 2) - (renderer.getStringWidth(title) / 2), y + 6, 0x000000);
		journal.drawJournalString(renderer, title, x + (GuiJournal.pageWidth / 2) - (renderer.getStringWidth(title) / 2), y + 6, 0x000000, false);
		
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
		
		this.drawRecipe(journal, recipe, x, y, mousex, mousey);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		
		renderer.setUnicodeFlag(true);
		String desc = this.getDescription();
		if (desc != null && desc.length() > 0) {
			//renderer.drawSplitString(desc, x, y+158, GuiJournal.pageWidth, 0x000000);
			journal.drawJournalSplitString(renderer, desc, x, y+158, GuiJournal.pageWidth, 0x000000);
		}
		
		// draw out-of recipe text
		if (this.displayrecipes != null && this.displayrecipes.size() > 1) {
			int currentrecipelength = String.valueOf(this.currentRecipe+1).length();
			int totalrecipelength = String.valueOf(this.displayrecipes.size()).length();
			int rlen = Math.max(currentrecipelength, totalrecipelength);
			
			String current = String.valueOf(this.currentRecipe+1);
			while (current.length() < rlen) {
				current = " "+current;
			}
			String total = String.valueOf(this.displayrecipes.size());
			while (total.length() < rlen) {
				total = total+" ";
			}
			String recipestring = current+" of "+total;
			
			int swidth = renderer.getStringWidth(recipestring);
			
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, 50.0);
			//renderer.drawString(recipestring, x+ recipeNumberMiddle - swidth/2, y+ recipeNumberTop, 0x342C0E);
			journal.drawJournalString(renderer, recipestring, x+ recipeNumberMiddle - swidth/2, y+ recipeNumberTop, 0x342C0E);
			GL11.glPopMatrix();
		}
		
		renderer.setUnicodeFlag(unicode);
		
		this.updateButtons();
		
		journal.setZLevel(jz);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiButton> getNavButtonsForPage(int id, int x, int y) { 
		List<GuiButton> buttons = new ArrayList<GuiButton>();
		
		GuiButtonJournal prev = new GuiButtonJournal(id, x + buttonPrevLeft, y + buttonPrevTop, 14,9, 127, 0);
		prev.setColours(0xFF342C0E, 0xFF64551b, 0xFF937f30);
		this.prevbutton = prev;
		buttons.add(prev);
		
		GuiButtonJournal next = new GuiButtonJournal(id+1, x + buttonNextLeft, y + buttonNextTop, 14,9, 127, 9);
		next.setColours(0xFF342C0E, 0xFF64551b, 0xFF937f30);
		this.nextbutton = next;
		buttons.add(next);

		this.updateButtons();
		return buttons;
	}
	
	@SideOnly(Side.CLIENT)
	public void updateButtons() {
		boolean show = this.displayrecipes != null && this.displayrecipes.size() > 1;
		
		this.prevbutton.enabled = show && this.currentRecipe > 0;
		this.prevbutton.visible = show;
		this.nextbutton.enabled = show && this.currentRecipe < this.displayrecipes.size()-1;
		this.nextbutton.visible = show;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void actionPerformed(GuiJournal journal, int id, GuiButton button) {
		int bid = button.id - id;

		if (bid == 0 && this.currentRecipe > 0) {
			//Physis.logger.info("PREV: "+this.currentRecipe+"->"+(this.currentRecipe-1));
			this.currentRecipe--;
		}
		else if (bid == 1 && this.currentRecipe < this.displayrecipes.size() - 1) {
			//Physis.logger.info("NEXT: "+this.currentRecipe+"->"+(this.currentRecipe+1));
			this.currentRecipe++;
		}
	}
}
