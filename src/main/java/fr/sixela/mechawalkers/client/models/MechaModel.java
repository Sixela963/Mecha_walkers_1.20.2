package fr.sixela.mechawalkers.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.entity.Mecha;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MechaModel extends EntityModel<Mecha> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MechaWalkersMod.MODID, "mecha_model"), "main");
    private final ModelPart HipBone;

    public MechaModel(ModelPart root) {
        this.HipBone = root.getChild("HipBone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition HipBone = partdefinition.addOrReplaceChild("HipBone", CubeListBuilder.create().texOffs(0, 72).addBox(-9.0F, -4.0F, -7.0F, 18.0F, 10.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(64, 0).addBox(-10.0F, -6.0F, -9.0F, 20.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition TorsoBone = HipBone.addOrReplaceChild("TorsoBone", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -28.0F, -10.0F, 22.0F, 28.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-12.0F, -29.0F, -10.0F, 24.0F, 1.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition LeftArmBone = HipBone.addOrReplaceChild("LeftArmBone", CubeListBuilder.create().texOffs(84, 20).addBox(-10.0F, -5.0F, -6.0F, 10.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(68, 46).addBox(-8.0F, 20.0F, -10.0F, 6.0F, 6.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-7.5F, 20.5F, 16.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -28.0F, 0.0F));

        PartDefinition cube_r1 = LeftArmBone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 13).addBox(-0.5F, -0.5F, 5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 23.0F, 19.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition cube_r2 = LeftArmBone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 11).addBox(-1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 23.0F, 19.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r3 = LeftArmBone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 23.0F, 19.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r4 = LeftArmBone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(8, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 23.0F, 19.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition cube_r5 = LeftArmBone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 96).addBox(-4.0F, -11.0F, -12.0F, 8.0F, 22.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 16.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition RightArmBone = HipBone.addOrReplaceChild("RightArmBone", CubeListBuilder.create().texOffs(84, 20).mirror().addBox(0.0F, -5.0F, -6.0F, 10.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(68, 46).mirror().addBox(2.0F, 20.0F, -10.0F, 6.0F, 6.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 48).mirror().addBox(2.5F, 20.5F, 16.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(11.0F, -28.0F, 0.0F));

        PartDefinition cube_r6 = RightArmBone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(14, 13).mirror().addBox(-0.5F, -0.5F, 5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 23.0F, 19.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition cube_r7 = RightArmBone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(8, 11).mirror().addBox(-1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 23.0F, 19.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r8 = RightArmBone.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(8, 6).mirror().addBox(-1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 23.0F, 19.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r9 = RightArmBone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(8, 0).mirror().addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 23.0F, 19.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition cube_r10 = RightArmBone.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 96).mirror().addBox(-4.0F, -11.0F, -12.0F, 8.0F, 22.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 16.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition LeftLeg = HipBone.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(64, 78).addBox(-8.0F, -3.0F, -7.0F, 8.0F, 10.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(94, 88).addBox(-8.0F, 22.0F, -7.0F, 8.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(32, 96).addBox(-6.0F, 20.0F, -12.0F, 4.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 2.0F, 0.0F));

        PartDefinition cube_r11 = LeftLeg.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 11.5F, -8.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r12 = LeftLeg.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(72, 102).addBox(-3.0F, -7.0F, -7.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 14.0F, 2.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightLeg = HipBone.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(64, 78).mirror().addBox(0.0F, -3.0F, -7.0F, 8.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(94, 88).mirror().addBox(0.0F, 22.0F, -7.0F, 8.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 96).mirror().addBox(2.0F, 20.0F, -12.0F, 4.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(9.0F, 2.0F, 0.0F));

        PartDefinition cube_r13 = RightLeg.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -7.5F, -1.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 11.5F, -8.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r14 = RightLeg.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(72, 102).mirror().addBox(-3.0F, -7.0F, -7.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 14.0F, 2.0F, -0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Mecha entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.HipBone.getChild("LeftArmBone").xRot = headPitch * (-(float)Math.PI / 180F);
        this.HipBone.getChild("RightArmBone").xRot = headPitch * (-(float)Math.PI / 180F);

        this.HipBone.getChild("LeftLeg").xRot = Mth.cos(limbSwing * 1F) * 1F * limbSwingAmount;
        this.HipBone.getChild("RightLeg").xRot = Mth.cos(limbSwing * 1F + (float)Math.PI) * 1F * limbSwingAmount;

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        HipBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
