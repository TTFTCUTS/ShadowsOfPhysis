package ttftcuts.physis.client.render.tile;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.model.ModelSocketTable;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.helper.ShaderCallback;
import ttftcuts.physis.common.helper.ShaderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileSocketTable extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(Physis.MOD_ID+":textures/blocks/socketTable.png");
	// TEMA: MC end portal texture
	private static final ResourceLocation starsTexture = new ResourceLocation("textures/entity/end_portal.png");
	
	private static final ModelSocketTable model = new ModelSocketTable();
	
	// forward face to number of 90 degree rotations conversion
	private static final int[] rot = {-1,-1,2,0,1,3};
	
	private final EntityItem dummyEntityItem = new EntityItem(null);
	private final RenderItem dummyRenderItem;
	
	private TileEntitySocketTable table;
	private final ShaderCallback shaderCallback;
	
	public RenderTileSocketTable() {
		dummyRenderItem = new RenderItem() {
			@Override
			public boolean shouldBob() {
				return false;
			}
			
			@Override
			public boolean shouldSpreadItems() {
				return false;
			}
		};
		dummyRenderItem.setRenderManager(RenderManager.instance);
		
		// TEMA: this is the shader callback where the uniforms are set for this particular shader.
		// it's called each frame when the shader is bound. Probably the most expensive part of the whole thing.
		// you might be able to even call this once per frame instead of once per draw, pointing call at the program instead of passing this in useShader.
		shaderCallback = new ShaderCallback() {
			@Override
			public void call(int shader) {
				Minecraft mc = Minecraft.getMinecraft();
				float fov = mc.gameSettings.fovSetting * 2f;
				
				int x = ARBShaderObjects.glGetUniformLocationARB(shader, "xpos");
				ARBShaderObjects.glUniform1fARB(x, mc.thePlayer.rotationYaw / fov);
				
				int z = ARBShaderObjects.glGetUniformLocationARB(shader, "zpos");
				ARBShaderObjects.glUniform1fARB(z, - mc.thePlayer.rotationPitch / fov);
			}
		};
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		table = (TileEntitySocketTable)tile;
		
		GL11.glPushMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(x, y, z);
		
		Minecraft mc = Minecraft.getMinecraft();
		
		GL11.glTranslatef(0.5f, 1.5f, 0.5f);
		GL11.glScalef(1F, -1F, -1F);
		
		GL11.glRotatef(90F * rot[table.facing], 0F, 1F, 0F);
		
		ItemStack stack = table.getStackInSlot(0);
		if (stack != null) {
			GL11.glPushMatrix();
			GL11.glScalef(1F, -1F, -1F);
			
			dummyEntityItem.setEntityItemStack(stack);
			
			GL11.glRotatef(90f, 1f, 0f, 0f);
			
			GL11.glTranslatef(0.13f, 0.085f, 0.53f);
			
			GL11.glRotatef(110f, 0f, 0f, 1f);
			
			// corrective rotation for the item render spinning...
			float rot = -(((float)dummyEntityItem.age) / 20.0F + dummyEntityItem.hoverStart) * (180F / (float)Math.PI);
			GL11.glRotatef(rot, 0F, 1F, 0F);
			
			float scale = 1.2f;
			GL11.glScalef(scale, scale, scale);
			
			dummyRenderItem.doRender(dummyEntityItem, 0, 0, 0, 0, 0);
			
			GL11.glPopMatrix();
		}
		
		// TEMA: binding the star texture here instead of the normal table one - you'd want this conditional on ShaderHelper.useShaders, and act accordingly otherwise.
		mc.renderEngine.bindTexture(starsTexture);
		
		ShaderHelper.useShader(ShaderHelper.testShader, shaderCallback);
		model.render();
		ShaderHelper.releaseShader();
		
		GL11.glPopMatrix();
	}

}
