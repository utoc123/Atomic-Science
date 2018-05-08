package com.builtbroken.atomic.content.tiles.reactor.fission;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TESRReactorCell extends TileEntitySpecialRenderer
{
    IModelCustom top_model;
    IModelCustom middle_model;
    IModelCustom bottom_model;

    ResourceLocation top_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/cell.top.png");
    ResourceLocation middle_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/cell.middle.png");
    ResourceLocation bottom_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/cell.bottom.png");

    public TESRReactorCell()
    {
        top_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/cell.top.obj"));
        middle_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/cell.middle.obj"));
        bottom_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/cell.bottom.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);

        if (tile instanceof TileEntityReactorCell)
        {
            TileEntityReactorCell reactorCell = (TileEntityReactorCell) tile;
            if (reactorCell.isBottom())
            {
                bindTexture(bottom_texture);
                bottom_model.renderAll();
            }
            else if (reactorCell.isTop())
            {
                bindTexture(top_texture);
                GL11.glTranslatef(0, -0.154f, 0);
                GL11.glScalef(1f, 1.32f, 1f);
                top_model.renderAllExcept("BottomPad", "BaseDepth", "BaseWidth", "Base");
            }
            else if (reactorCell.isMiddle())
            {
                bindTexture(middle_texture);
                //GL11.glTranslatef(0, 0.075f, 0);
                //GL11.glScalef(1f, 1.15f, 1f);
                middle_model.renderAll();
            }
            else
            {
                GL11.glTranslatef(0, -0.1f, 0);
                GL11.glScalef(1f, 1.2f, 1f);
                bindTexture(top_texture);
                top_model.renderAll();
            }

            if(reactorCell.shouldRenderFuel())
            {
                //TODO render fuel rods
                //TODO decrease in size as fuel is used
                //TODO decrease in color (green -> grey) as its used
                //TODO glow when running (blue or green? hmm)
                //TODO glow molten like if overheating
            }
        }

        GL11.glPopMatrix();
    }
}