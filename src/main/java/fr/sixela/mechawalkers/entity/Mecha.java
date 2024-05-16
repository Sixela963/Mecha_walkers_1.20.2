package fr.sixela.mechawalkers.entity;


import fr.sixela.mechawalkers.block.MechModule.arm.MechArmAbstractBlock;
import fr.sixela.mechawalkers.block.MechModule.core.MechCoreAbstractBlock;
import fr.sixela.mechawalkers.block.MechModule.frame.MechFrameAbstractBlock;
import fr.sixela.mechawalkers.block.MechModule.leg.MechLegAbstractBlock;
import fr.sixela.mechawalkers.event.ModEventBusEvents;
import fr.sixela.mechawalkers.mechModule.arm.MechArmAbstractModule;
import fr.sixela.mechawalkers.mechModule.core.MechCoreAbstractModule;
import fr.sixela.mechawalkers.mechModule.frame.MechFrameAbstractModule;
import fr.sixela.mechawalkers.mechModule.leg.LegAbstractModule;
import fr.sixela.mechawalkers.network.MechaWalkersPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.level.block.Block;
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

    private boolean isPowered = false;
    private float yRotSpeed = 4f; //degree/tick
    private float xRotSpeed = 6f; //degree/tick

    private float powerSum = 0f;

    private boolean usingLeftTool;
    private boolean wasUsingLeftTool;
    private boolean usingRightTool;
    private boolean wasUsingRightTool;

    private static final EntityDataAccessor<ItemStack> DATA_CORE_ITEM = SynchedEntityData.defineId(Mecha.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> DATA_FRAME_ITEM = SynchedEntityData.defineId(Mecha.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> DATA_LEGS_ITEM = SynchedEntityData.defineId(Mecha.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> DATA_LEFT_TOOL_ITEM = SynchedEntityData.defineId(Mecha.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> DATA_RIGHT_TOOL_ITEM = SynchedEntityData.defineId(Mecha.class, EntityDataSerializers.ITEM_STACK);
    protected MechCoreAbstractModule coreModule;
    protected MechFrameAbstractModule frameModule;
    protected LegAbstractModule legModule;
    protected MechArmAbstractModule leftArmModule;
    protected MechArmAbstractModule rightArmModule;

    protected Mecha(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(0.5F);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_CORE_ITEM,ItemStack.EMPTY);
        this.getEntityData().define(DATA_FRAME_ITEM,ItemStack.EMPTY);
        this.getEntityData().define(DATA_LEGS_ITEM,ItemStack.EMPTY);
        this.getEntityData().define(DATA_LEFT_TOOL_ITEM,ItemStack.EMPTY);
        this.getEntityData().define(DATA_RIGHT_TOOL_ITEM,ItemStack.EMPTY);
        super.defineSynchedData();
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (DATA_CORE_ITEM.equals(pKey)||DATA_FRAME_ITEM.equals(pKey)||DATA_LEGS_ITEM.equals(pKey)||DATA_LEFT_TOOL_ITEM.equals(pKey)||DATA_RIGHT_TOOL_ITEM.equals(pKey)){
            //LogUtils.getLogger().info("Mecha specific data synced");
            this.updateModulesFromItems();
        }
    }

    public void setModuleItems(ItemStack coreStack, ItemStack frameStack, ItemStack legStack, ItemStack leftToolStack, ItemStack rightToolStack) {
        //LogUtils.getLogger().info("setting module items");
        this.getEntityData().set(DATA_CORE_ITEM,coreStack);
        this.getEntityData().set(DATA_FRAME_ITEM,frameStack);
        this.getEntityData().set(DATA_LEGS_ITEM,legStack);
        this.getEntityData().set(DATA_LEFT_TOOL_ITEM,leftToolStack);
        this.getEntityData().set(DATA_RIGHT_TOOL_ITEM,rightToolStack);
        this.updateModulesFromItems();
    }

    public void updateModulesFromItems() {
        //LogUtils.getLogger().info("updating module objects");
        Block coreBlock = Block.byItem(this.getEntityData().get(DATA_CORE_ITEM).getItem());
        Block frameBlock = Block.byItem(this.getEntityData().get(DATA_FRAME_ITEM).getItem());
        Block legsBlock = Block.byItem(this.getEntityData().get(DATA_LEGS_ITEM).getItem());
        Block leftToolBlock = Block.byItem(this.getEntityData().get(DATA_LEFT_TOOL_ITEM).getItem());
        Block rightToolBlock = Block.byItem(this.getEntityData().get(DATA_RIGHT_TOOL_ITEM).getItem());

        if (coreBlock instanceof MechCoreAbstractBlock) {
            this.coreModule = ((MechCoreAbstractBlock)coreBlock).getModule();
            this.coreModule.setMechaEntity(this);
        } else {
            //LogUtils.getLogger().info("Invalid Core!");
            return;
        }
        if (frameBlock instanceof MechFrameAbstractBlock) {
            this.frameModule = ((MechFrameAbstractBlock)frameBlock).getModule();
            this.frameModule.setMechaEntity(this);
        } else {
            //LogUtils.getLogger().info("Invalid frame!");
            return;
        }
        if (legsBlock instanceof MechLegAbstractBlock) {
            this.legModule = ((MechLegAbstractBlock)legsBlock).getModule();
            this.legModule.setMechaEntity(this);
        } else {
            //LogUtils.getLogger().info("Invalid legs!");
            return;
        }
        if (leftToolBlock instanceof MechArmAbstractBlock) {
            this.leftArmModule = ((MechArmAbstractBlock)leftToolBlock).getModule();
            this.leftArmModule.setMechaEntity(this);
        } else {
            //LogUtils.getLogger().info("Invalid left arm!");
            return;
        }
        if (rightToolBlock instanceof MechArmAbstractBlock) {
            this.rightArmModule = ((MechArmAbstractBlock)rightToolBlock).getModule();
            this.rightArmModule.setMechaEntity(this);
        } else {
            //LogUtils.getLogger().info("Invalid right arm!");
            return;
        }

        this.updateStats();
    }

    public static AttributeSupplier setAttributes() {
        // Base default values
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.ARMOR, 0d)
                .add(Attributes.ARMOR_TOUGHNESS, 0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0f)
                .add(Attributes.JUMP_STRENGTH,0.0f)
                .build();
    }

    public void updateStats() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.frameModule.getMaxHealth());
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.frameModule.getArmor());
        this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(this.frameModule.getArmorToughness());

        //Speed formula: 0.05f * LegSpeedMult * (CoreMovementPower/FrameMass) (0.05f is to convert from blocks/s to blocks/tick)
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.05f*this.legModule.getSpeedMultiplier()*(this.coreModule.getMovementPower()/this.frameModule.getMass()));
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.legModule.getJumpPower());

        this.yRotSpeed = this.legModule.getLegTurnSpeed();
        this.setMaxUpStep(this.legModule.getStep());

        this.powerSum = this.coreModule.getPowerCost()+this.frameModule.getPowerCost()+this.legModule.getPowerCost()+this.leftArmModule.getPowerCost()+this.rightArmModule.getPowerCost();
        this.setPowered(!(this.powerSum < 0));
    }

    //To be replaced by module-specific functions
    @Override
    protected float getJumpPower() {
        //something is wrong with this function
        //for some reason I can't correctly access the fields and methods from the instanciated entity when I am in one of those overridden protected functions. this is very annoying
        //return (float)this.getAttributeValue(Attributes.JUMP_STRENGTH) * super.getJumpPower();
        return this.legModule.getJumpPower() * super.getJumpPower();
    }

    @Override
    public float getEyeHeight(@NotNull Pose pPose) {
        return 3f;
    }

    @Override
    public boolean canCollideWith(@NotNull Entity pEntity) {
        return super.canCollideWith(pEntity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public void setPowered(boolean pIsPowered) {
        this.isPowered = pIsPowered;
    }
    public boolean getPowered() {
        return this.isPowered;
    }

    //Main movement code function (possibly move to specific function?)
    @Override
    protected void tickRidden(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        //MOVEMENT
        super.tickRidden(pPlayer, pTravelVector);

        if (!(this.isPowered && this.coreModule.getCorePowered())) {
            return;
        }

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

    @Override
    public void tick() {
        super.tick();

        //ticking modules
        this.coreModule.tick();
        this.frameModule.tick();
        this.legModule.tick();
        this.leftArmModule.tick();
        this.rightArmModule.tick();

        if (!(this.isPowered && this.coreModule.getCorePowered())) {
            return;
        }

        //LOGIC(?)
        if (!this.level().isClientSide) {
            if (!this.wasUsingLeftTool && this.usingLeftTool) {
                this.leftArmModule.startUsing();
            } else if (this.wasUsingLeftTool && this.usingLeftTool) {
                this.leftArmModule.use();
            } else if (this.wasUsingLeftTool) {
                this.leftArmModule.stopUsing();
            }
            this.wasUsingLeftTool = this.usingLeftTool;

            if (!this.wasUsingRightTool && this.usingRightTool) {
                this.rightArmModule.startUsing();
            } else if (this.wasUsingRightTool && this.usingRightTool) {
                this.rightArmModule.use();
            } else if (this.wasUsingRightTool) {
                this.rightArmModule.stopUsing();
            }
            this.wasUsingRightTool = this.usingRightTool;
        }
    }

    public void setUsingTools(boolean left, boolean right) {
        this.usingLeftTool = left;
        this.usingRightTool = right;
    }

    //Untested in multiplayer. Copy-pasted from horse
    @Override
    public @NotNull InteractionResult interact(Player pPlayer, @NotNull InteractionHand pHand) {
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
    @Override
    public boolean isPushable() {
        return false;
    }
    protected Vec2 getRiddenRotation(LivingEntity pEntity) {
//        return this.getRotationVector();
        return new Vec2(pEntity.getXRot(), Mth.wrapDegrees(pEntity.getYRot()));
//        return new Vec2(pEntity.getXRot(), Mth.wrapDegrees(pEntity.getYRot()));
    }


    protected @NotNull Vector3f getPassengerAttachmentPoint(@NotNull Entity pEntity, @NotNull EntityDimensions pDimensions, float pScale) {
        return new Vector3f(0f, 2f, -0.3f);
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }



    @Override
    protected @NotNull Vec3 getRiddenInput(Player pPlayer, @NotNull Vec3 pTravelVector) {
        float zza = pPlayer.zza;
        if (zza<0) {
            zza = zza * this.legModule.getBackwardsFactor();
        }
        return new Vec3(pPlayer.xxa*this.legModule.getSideStepFactor(), 0d, zza);
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
    protected void positionRider(@NotNull Entity pPassenger, Entity.@NotNull MoveFunction pCallback) {
        super.positionRider(pPassenger, pCallback);
        if (pPassenger instanceof LivingEntity) {
            ((LivingEntity)pPassenger).yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity pPassenger) {
        return this.position();
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

    //NBT save data
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("MechCoreBlock", this.getEntityData().get(DATA_CORE_ITEM).save(new CompoundTag()));
        pCompound.put("MechFrameBlock", this.getEntityData().get(DATA_FRAME_ITEM).save(new CompoundTag()));
        pCompound.put("MechLegBlock", this.getEntityData().get(DATA_LEGS_ITEM).save(new CompoundTag()));
        pCompound.put("LeftToolBlock", this.getEntityData().get(DATA_LEFT_TOOL_ITEM).save(new CompoundTag()));
        pCompound.put("RightToolBlock", this.getEntityData().get(DATA_RIGHT_TOOL_ITEM).save(new CompoundTag()));
    }
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setModuleItems(
                ItemStack.of(pCompound.getCompound("MechCoreBlock")),
                ItemStack.of(pCompound.getCompound("MechFrameBlock")),
                ItemStack.of(pCompound.getCompound("MechLegBlock")),
                ItemStack.of(pCompound.getCompound("LeftToolBlock")),
                ItemStack.of(pCompound.getCompound("RightToolBlock")));
    }

    //Function overriden to remove the behaviour where the mech would turn toward its direction
    @Override
    protected float tickHeadTurn(float pYRot, float pAnimStep) {
        return pAnimStep;
    }

    //Will later be replaced by a function dependent on modules
    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        if (this.legModule.getTakeFallDamage()) {
            return super.causeFallDamage(pFallDistance, pMultiplier, pSource);
        }
        return false;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return new Iterable<ItemStack>() {
            @NotNull
            @Override
            public Iterator<ItemStack> iterator() {
                return Collections.emptyIterator();
            }
        };
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack) {
        return;
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
}
