package ttftcuts.physis.client.gui;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.button.GuiButtonDynamic;
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
	private InventoryPlayer playerinv;
	
	private static final ResourceLocation uiTexture = new ResourceLocation(Physis.MOD_ID, "textures/gui/sockettable.png");
	
	public GuiSocketTable(InventoryPlayer inventory, TileEntitySocketTable table) {
		super(new ContainerSocketTable(inventory, table));
		this.playerinv = inventory;
		this.ySize = 222;
		this.cont = (ContainerSocketTable)this.inventorySlots;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.add(new GuiButtonDynamic(0, 0,0,176,32,16,16, "", inventoryTexture));
		this.buttonList.add(new GuiButtonDynamic(1, 0,0,176,32,16,16, "", inventoryTexture));
		this.buttonList.add(new GuiButtonDynamic(2, 0,0,176,32,16,16, "", inventoryTexture));
		this.buttonList.add(new GuiButtonDynamic(3, 0,0,176,32,16,16, "", inventoryTexture));
		this.buttonList.add(new GuiButtonDynamic(4, 0,0,176,32,16,16, "", inventoryTexture));
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
		
		this.drawWoodenSlot(8, 101);
		
		int y = 65 - 9 * (cont.activeSlots-1);
		
		Slot slot = cont.getSlot(0);
		NBTTagCompound[] sockets = null;
		
		if (slot.getHasStack()) {
			sockets = PhysisArtifacts.getSocketablesFromStack(slot.getStack());
		}
		
		// socket slots
		if (cont.activeSlots > 0) {
			this.drawWoodenColumn(53, y, cont.activeSlots);
			
			this.drawWoodenColumn(107, y, cont.activeSlots);
			
			if (slot.getHasStack()) {
				for (int i=0; i<cont.activeSlots; i++) {
					if (sockets[i] != null) {
						y = 65 - 9 * (cont.activeSlots-1) + i*18;
						
						ItemStack stack = ItemStack.loadItemStackFromNBT(sockets[i]);
						
						PhysisRenderHelper.renderItemStack(stack, this.guiLeft + 53, this.guiTop + y);
					}
				}
			}
		}
		
		mc.renderEngine.bindTexture(inventoryTexture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		for (int i=0; i<5; i++) {
			GuiButton b = (GuiButton) this.buttonList.get(i);
			if (i<cont.activeSlots) {
				b.visible = true;
				y = 65 - 9 * (cont.activeSlots-1) + i*18;
				
				b.xPosition = this.guiLeft + 80;
				b.yPosition = this.guiTop + y;
				
				boolean left = sockets != null && sockets[i] != null;
				boolean right = cont.getSlot(i+2).getHasStack();
				
				if (left ^ right) {
					int mats = cont.getReagentCount();
					if (left) {
						if (mats < TileEntitySocketTable.REMOVECOST) {
							b.enabled = false;
						} else {
							b.enabled = true;
						}
					} else {
						if (mats < TileEntitySocketTable.INSERTCOST) {
							b.enabled = false;
						} else {
							b.enabled = true;
						}
					}
				} else {
					b.enabled = false;
				}
				
				if (!(left ^ right)) {
					// nothing
					b.displayString = "?";
				} else if (left) {
					// right arrow
					this.drawTexturedModalRect(this.guiLeft + 72, this.guiTop + y + 2, 176, 80, 33, 12);
					b.displayString = "" + TileEntitySocketTable.REMOVECOST;
				} else if (right) {
					// left arrow
					this.drawTexturedModalRect(this.guiLeft + 71, this.guiTop + y + 2, 176, 92, 33, 12);
					b.displayString = "" + TileEntitySocketTable.INSERTCOST;
				}
			} else {
				b.visible = false;
				b.enabled = false;
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
		if (slot.getHasStack() && playerinv.getItemStack() == null) {
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
			
				boolean left = sockets != null && sockets[i] != null;
				boolean right = cont.getSlot(i+2).getHasStack();
				
				GuiButton b = (GuiButton) this.buttonList.get(i);
				if (b.visible && playerinv.getItemStack() == null) {
					if (mousex >= b.xPosition && mousex < b.xPosition+16 && mousey >= b.yPosition && mousey < b.yPosition+16 ) {
						if (left && right) {
							// both
							this.drawCustomTooltip(mousex, mousey, "blocked", 150);
						} else if (left) {
							// left
							if (b.enabled) {
								this.drawCustomTooltip(mousex, mousey, "unsocket", 150);
							} else {
								this.drawCustomTooltip(mousex, mousey, "can't unsocket", 150);
							}
						} else if (right) {
							// right
							if (b.enabled) {
								this.drawCustomTooltip(mousex, mousey, "socket", 150);
							} else {
								this.drawCustomTooltip(mousex, mousey, "can't socket", 150);
							}
						} else {
							// neither
							this.drawCustomTooltip(mousex, mousey, "no item", 150);
						}
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
		//Physis.logger.info("button "+button.id);
	}
}
