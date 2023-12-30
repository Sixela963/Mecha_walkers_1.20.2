package fr.sixela.mechawalkers.client.renderer;

import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.client.models.MechaModel;
import fr.sixela.mechawalkers.entity.CarrierGolem;
import fr.sixela.mechawalkers.entity.Mecha;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class MechaRenderer extends LivingEntityRenderer<Mecha, MechaModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MechaWalkersMod.MODID, "textures/entity/mecha.png");
    public MechaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MechaModel(pContext.bakeLayer(MechaModel.LAYER_LOCATION)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Mecha pEntity) {
        return TEXTURE;
    }

    //Copied over from ArmorStandRenderer
    @Override
    protected boolean shouldShowName(Mecha pEntity) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(pEntity);
        float f = pEntity.isCrouching() ? 32.0F : 64.0F;
        return d0 >= (double)(f * f) ? false : pEntity.isCustomNameVisible();
    }
}
