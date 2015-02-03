package ttftcuts.physis.client.render.item;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.helper.PhysisRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderSocketable implements IItemRenderer {
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Minecraft mc = Minecraft.getMinecraft();
		
		PhysisRenderHelper.renderItemStack(item, 0, 0, false);
		
		RenderHelper.disableStandardItemLighting();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		
		Tessellator t = Tessellator.instance;
		
		IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(item);
		IArtifactEffect effect = PhysisArtifacts.getEffectFromSocketable(item);
		
		IIcon icon = PhysisArtifacts.getTriggerIcon(trigger);
		t.startDrawingQuads();
		t.addVertexWithUV(8, 8, 0, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(16, 8, 0, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(16, 0, 0, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(8, 0, 0, icon.getMinU(), icon.getMinV());
		t.draw();
		
		icon = PhysisArtifacts.getEffectIcon(effect);
		t.startDrawingQuads();
		t.addVertexWithUV(8, 16, 0, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(16, 16, 0, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(16, 8, 0, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(8, 8, 0, icon.getMinU(), icon.getMinV());
		t.draw();
		
		RenderHelper.enableGUIStandardItemLighting();
	}
}
