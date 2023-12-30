package fr.sixela.mechawalkers.entity;


import com.mojang.logging.LogUtils;
import fr.sixela.mechawalkers.event.ModEventBusEvents;
import fr.sixela.mechawalkers.network.MechaWalkersPacketHandler;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.util.Mth;
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

/*
The main Mecha class
 */
public class Mecha extends LivingEntity {

    private static final float yRotSpeed = 4f; //degree/tick
    private static final float xRotSpeed = 6f; //degree/tick

    private boolean usingLeftTool;
    private boolean usingRightTool;


    protected Mecha(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1.6F);
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return super.canCollideWith(pEntity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public static AttributeSupplier setAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.ARMOR, 5d)
                .add(Attributes.ARMOR_TOUGHNESS, 5d)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.ATTACK_SPEED,1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0f)
                .build();
    }

    //To be replaced by module-specific functions
    @Override
    protected float getJumpPower() {
        return 1.5f*super.getJumpPower();
    }


    //Main movement code function (possibly move to specific function?)
    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {

        //MOVEMENT
        super.tickRidden(pPlayer, pTravelVector);
        Vec2 riddenRotation = this.getRiddenRotation(pPlayer);
        float newYRot = 0f;
        if (Math.abs(riddenRotation.y-this.getYRot()) > this.yRotSpeed) {
            //This part is annoying
            float deltaYRot = this.yRotSpeed*Math.signum(Mth.degreesDifference(this.getYRot(),riddenRotation.y));

            newYRot = this.getYRot() + deltaYRot;

        }else {
            newYRot = riddenRotation.y;
        }
        newYRot = Mth.wrapDegrees(newYRot);


        float newXRot = 0f;
        if (Math.abs(riddenRotation.x-this.getXRot()) > this.xRotSpeed) {
            newXRot = this.getXRot() + Math.signum(riddenRotation.x-this.getXRot())*this.xRotSpeed;
        }else {
            newXRot = riddenRotation.x;
        }

        this.setRot(newYRot,newXRot);
        this.yBodyRot = this.yHeadRot = this.getYRot();
//        this.yBodyRot = this.yHeadRot = this.getYRot();

        //PLAYER INPUT
        if (this.isControlledByLocalInstance()) {
            if (pPlayer instanceof LocalPlayer){
                this.setJumping(((LocalPlayer) pPlayer).input.jumping);
                MechaWalkersPacketHandler.INSTANCE.send(new MechaWalkersPacketHandler.ServerboundMechaToolPacket(ModEventBusEvents.KEYMAP_TOOL_LEFT.get().isDown(),
                                ModEventBusEvents.KEYMAP_TOOL_RIGHT.get().isDown()),
                        Minecraft.getInstance().getConnection().getConnection());
//                this.usingLeftTool = ModEventBusEvents.KEYMAP_TOOL_LEFT.get().isDown();
//                this.usingRightTool =ModEventBusEvents.KEYMAP_TOOL_RIGHT.get().isDown();
            }
        }
    }

    public void setUsingTools(boolean left, boolean right) {
        this.usingLeftTool = left;
        this.usingRightTool = right;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.usingLeftTool) {
                this.useLeftTool();
            }
            if (this.usingRightTool) {
                this.useRightTool();
            }
        }
        super.tick();
    }

    protected void useLeftTool() {
        LogUtils.getLogger().info("Using Left mecha tool!!");
    }

    protected void useRightTool() {
        LogUtils.getLogger().info("Using Right mecha tool!!");
    }

    //Untested in multiplayer. Copy-pasted from horse
    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (!this.isVehicle()) {
            if (!this.level().isClientSide) {
                pPlayer.setYRot(this.getYRot());
                pPlayer.setXRot(this.getXRot());
                return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    protected Vec2 getRiddenRotation(LivingEntity pEntity) {
//        return this.getRotationVector();
        return new Vec2(pEntity.getXRot(), Mth.wrapDegrees(pEntity.getYRot()));
//        return new Vec2(pEntity.getXRot(), Mth.wrapDegrees(pEntity.getYRot()));
    }


    protected Vector3f getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pScale) {
        return new Vector3f(0f, 2f, -0.3f);
    }

    @Override
    protected float getRiddenSpeed(Player pPlayer) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }



    @Override
    protected Vec3 getRiddenInput(Player pPlayer, Vec3 pTravelVector) {
        return new Vec3(pPlayer.xxa*0.5f, 0d, pPlayer.zza);
        //return super.getRiddenInput(pPlayer, pTravelVector);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getDirectEntity() == this.getControllingPassenger()) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        super.positionRider(pPassenger, pCallback);
        if (pPassenger instanceof LivingEntity) {
            ((LivingEntity)pPassenger).yBodyRot = this.yBodyRot;
        }
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

    //Function overriden to remove the behaviour where the mech would turn toward its direction
    @Override
    protected float tickHeadTurn(float pYRot, float pAnimStep) {
        return pAnimStep;
    }

    //Will later be replaced by a function dependent on modules
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
        return HumanoidArm.RIGHT;
    }
}
