package fr.sixela.mechawalkers.entity;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;

public class CarrierGolem extends LivingEntity {

    public CarrierGolem(EntityType<? extends CarrierGolem> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1.1F);
    }

    public static AttributeSupplier setAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ATTACK_DAMAGE, 0.0f)
                .add(Attributes.ATTACK_SPEED,1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0f)
                .build();
    }

    @Override
    protected float getJumpPower() {
        return 1.5f*super.getJumpPower();
    }


    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        //Code copied from Boat + horse
        if (!this.level().isClientSide) {
            boolean startedRiding = pPlayer.startRiding(this);
            if (startedRiding) {
                pPlayer.setXRot(this.getXRot());
                pPlayer.setYRot(this.getYRot());
            }
            return startedRiding ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }


    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        Vec2 riddenRotation = this.getRiddenRotation(pPlayer);
        this.setRot(riddenRotation.y,riddenRotation.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {
            if (pPlayer instanceof LocalPlayer){
                this.setJumping(((LocalPlayer) pPlayer).input.jumping);
            }
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player pPlayer, Vec3 pTravelVector) {
        return new Vec3(pPlayer.xxa *0.5, 0d, pPlayer.zza);
        //return super.getRiddenInput(pPlayer, pTravelVector);
    }

    protected Vector3f getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pScale) {
        return new Vector3f(0f, 0.7f, -0.1f);
    }

    protected Vec2 getRiddenRotation(LivingEntity pEntity) {
        return new Vec2(pEntity.getXRot() * 0.5F, pEntity.getYRot());
    }



    @Override
    protected float getRiddenSpeed(Player pPlayer) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        super.positionRider(pPassenger, pCallback);
        if (pPassenger instanceof LivingEntity) {
            ((LivingEntity)pPassenger).yBodyRot = this.yBodyRot;
        }

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity firstPassenger = this.getFirstPassenger();
        if (firstPassenger instanceof LivingEntity) {
            return (LivingEntity) firstPassenger;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new Iterable<ItemStack>() {
            @NotNull
            @Override
            public Iterator<ItemStack> iterator() {
                return Collections.emptyIterator();
            }
        };
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        return;
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }
}
