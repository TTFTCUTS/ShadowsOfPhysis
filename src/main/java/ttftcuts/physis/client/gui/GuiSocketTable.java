package ttftcuts.physis.client.gui;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.container.ContainerSocketTable;
import ttftcuts.physis.common.helper.PhysisRenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiSocketTable extends GuiContainerPhysis {

	private ContainerSocketTable cont;
	
	private static final ResourceLocation uiTexture = new ResourceLocation(Physis.MOD_ID, "textures/gui/sockettable.png");
	
	public GuiSocketTable(InventoryPlayer inventory, TileEntitySocketTable table) {
		super(new ContainerSocketTable(inventory, table));
		this.ySize = 222;
		cont = (ContainerSocketTable)this.inventorySlots;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.add(new GuiButton(0, 0,0,60,30, "test"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mousex, int mousey) {
		mc.renderEngine.bindTexture(uiTexture);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		this.drawTexturedModalRect(this.guiLeft - 40, this.guiTop - 65, 0, 0, 256, 256);
		
		this.drawWoodenSlot(8, 65);
		
		int y = 65 - 9 * (cont.activeSlots-1);
		
		// socket slots
		if (cont.activeSlots > 0) {
			this.drawWoodenColumn(53, y, cont.activeSlots);
			
			this.drawWoodenColumn(107, y, cont.activeSlots);
			
			Slot slot = cont.getSlot(0);
			if (slot.getHasStack()) {
				NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(slot.getStack());
				for (int i=0; i<cont.activeSlots; i++) {
					if (sockets[i] != null) {
						y = 65 - 9 * (cont.activeSlots-1) + i*18;
						
						ItemStack stack = ItemStack.loadItemStackFromNBT(sockets[i]);
						
						PhysisRenderHelper.renderItemStack(stack, this.guiLeft + 53, this.guiTop + y);
					}
				}
			}
		}
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// side slots
		int sideslots = 5 - cont.activeSlots;
		
		if (sideslots > 0) {
			mc.renderEngine.bindTexture(inventoryTexture);
			
			this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 21, 176, 0, 32, 7);
			for (int i=0; i<sideslots; i++) {
				this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 28 + 18*i, 176, 7, 32, 18);
			}
			this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 28 + 18 * sideslots, 176, 25, 32, 7);
		}
		
		this.drawPlayerInventory();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mousex, int mousey) {
		
		for (int i=0; i<cont.activeSlots; i++) {
			int x = this.guiLeft + 53;
			int y = 65 - 9 * (cont.activeSlots-1) + i*18;
			int gy = this.guiTop + y;
			
			if (mousex >= x-1 && mousex < x+17 && mousey >= gy-1 && mousey < gy+17 ) {
				this.drawSlotOverlay(53, y);
			}
		}
	}

	@Override
	public void drawScreen(int mousex, int mousey, float partialTicks)
    {
		super.drawScreen(mousex, mousey, partialTicks);

		Slot slot = cont.getSlot(0);
		if (slot.getHasStack()) {
			RenderHelper.disableStandardItemLighting();
			
			NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(slot.getStack());
			for (int i=0; i<cont.activeSlots; i++) {
				if (sockets[i] != null) {
					int x = this.guiLeft + 53;
					int y = 65 - 9 * (cont.activeSlots-1) + i*18;
					int gy = this.guiTop + y;
					
					if (mousex >= x-1 && mousex < x+17 && mousey >= gy-1 && mousey < gy+17 ) {
						ItemStack stack = ItemStack.loadItemStackFromNBT(sockets[i]);
						
						this.renderToolTip(stack, mousex, mousey);
					}
				}
			}
			
			RenderHelper.enableStandardItemLighting();
		}
    }
	
	@Override
	public void updateScreen() {
		
		ContainerSocketTable cont = (ContainerSocketTable)this.inventorySlots;
		
		if (cont != null) {
			cont.updateLayout();
		}
		
		super.updateScreen();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("id", button.id);
		this.cont.sendUiPacket(tag);
	}
}
