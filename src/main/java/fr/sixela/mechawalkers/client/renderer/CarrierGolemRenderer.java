package fr.sixela.mechawalkers.client.renderer;

import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.client.models.CarrierGolemModel;
import fr.sixela.mechawalkers.entity.CarrierGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class CarrierGolemRenderer extends LivingEntityRenderer<CarrierGolem, CarrierGolemModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MechaWalkersMod.MODID, "textures/entity/carrier_golem.png");

    public CarrierGolemRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CarrierGolemModel(pContext.bakeLayer(CarrierGolemModel.LAYER_LOCATION)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(CarrierGolem pEntity) {
        return TEXTURE;
    }

    //Copied over from ArmorStandRenderer
    @Override
    protected boolean shouldShowName(CarrierGolem pEntity) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(pEntity);
        float f = pEntity.isCrouching() ? 32.0F : 64.0F;
        return d0 >= (double)(f * f) ? false : pEntity.isCustomNameVisible();
    }
}
