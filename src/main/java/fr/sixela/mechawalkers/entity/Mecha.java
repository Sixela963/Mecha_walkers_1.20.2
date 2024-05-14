package fr.sixela.mechawalkers.entity;


import com.mojang.logging.LogUtils;
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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
    private int leftToolProgress = 0;
    private float leftToolDigSpeed = 10f;
    private boolean usingRightTool;

    protected ItemStack coreItemStack;
    protected MechCoreAbstractModule coreModule;
    protected ItemStack frameItemStack;
    protected MechFrameAbstractModule frameModule;
    protected ItemStack leftToolItemStack;
    protected MechArmAbstractModule leftArmModule;
    protected ItemStack rightToolItemStack;
    protected MechArmAbstractModule rightArmModule;
    protected ItemStack legsItemStack;
    protected LegAbstractModule legModule;


    protected Mecha(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(0.5F);
        this.coreItemStack = ItemStack.EMPTY;
        this.frameItemStack = ItemStack.EMPTY;
        this.legsItemStack = ItemStack.EMPTY;
        this.leftToolItemStack = ItemStack.EMPTY;
        this.rightToolItemStack = ItemStack.EMPTY;
    }

    public void setModules (ItemStack coreStack,ItemStack frameStack,ItemStack legStack,ItemStack leftToolStack,ItemStack rightToolStack) {
        this.coreItemStack = coreStack;
        this.frameItemStack = frameStack;
        this.legsItemStack = legStack;
        this.leftToolItemStack = leftToolStack;
        this.rightToolItemStack = rightToolStack;

        Block coreBlock = Block.byItem(this.coreItemStack.getItem());
        Block frameBlock = Block.byItem(this.frameItemStack.getItem());
        Block legsBlock = Block.byItem(this.legsItemStack.getItem());
        Block leftToolBlock = Block.byItem(this.leftToolItemStack.getItem());
        Block rightToolBlock = Block.byItem(this.rightToolItemStack.getItem());

        if (coreBlock instanceof MechCoreAbstractBlock) {
            this.coreModule = ((MechCoreAbstractBlock)coreBlock).getModule();
        } else {
            LogUtils.getLogger().info("Invalid Core!");
        }
        if (frameBlock instanceof MechFrameAbstractBlock) {
            this.frameModule = ((MechFrameAbstractBlock)frameBlock).getModule();
        } else {
            LogUtils.getLogger().info("Invalid frame!");
        }
        if (legsBlock instanceof MechLegAbstractBlock) {
            this.legModule = ((MechLegAbstractBlock)legsBlock).getModule();
        } else {
            LogUtils.getLogger().info("Invalid legs!");
        }
        if (leftToolBlock instanceof MechArmAbstractBlock) {
            this.leftArmModule = ((MechArmAbstractBlock)leftToolBlock).getModule();
        } else {
            LogUtils.getLogger().info("Invalid left arm!");
        }
        if (rightToolBlock instanceof MechArmAbstractBlock) {
            this.rightArmModule = ((MechArmAbstractBlock)rightToolBlock).getModule();
        } else {
            LogUtils.getLogger().info("Invalid right arm!");
        }



        this.updateStats();
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
        this.setMaxUpStep(this.legModule.getStep());
    }

    //To be replaced by module-specific functions
    @Override
    protected float getJumpPower() {
        //wtf is wrong with this function
        //for some reason I can't correctly access the fields and methods from the instanciated entity when I am in one of those overridden protected functions. this is very annoying
        //return (float)this.getAttributeValue(Attributes.JUMP_STRENGTH) * super.getJumpPower();
        return this.legModule.getJumpPower() * super.getJumpPower();
    }

    @Override
    public float getEyeHeight(Pose pPose) {
        return 3f;
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
        super.tick();

        //LOGIC(?)
        if (!this.level().isClientSide) {
            if (this.usingLeftTool) {
                this.useLeftTool();
            }else{
                this.leftToolProgress=0;
            }
            if (this.usingRightTool) {
                this.useRightTool();
            }
        }
    }

    protected void useLeftTool() {
        //LogUtils.getLogger().info("Using Left mecha tool!!");
        if (!this.isVehicle()){
            return;
        }
        if (this.getControllingPassenger() == null) {
            return;
        }

        BlockHitResult blockHitResult = getEntityPOVHitResult(this.level(), this,10d, ClipContext.Fluid.NONE);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }
        LogUtils.getLogger().info("Starting:");
        LogUtils.getLogger().info(this.getControllingPassenger().getEyePosition().toString());
//        LogUtils.getLogger().info("Result:");
//        LogUtils.getLogger().info(blockHitResult.getBlockPos().toString());
        this.leftToolProgress++;
        float breakSpeed = this.level().getBlockState(blockHitResult.getBlockPos()).getDestroySpeed(this.level(),blockHitResult.getBlockPos());
        int breakTicksRequired = (int)(30f*breakSpeed/this.leftToolDigSpeed);
        int progress = (int)(10f*this.leftToolProgress/breakTicksRequired);
        level().destroyBlockProgress(this.getId(),blockHitResult.getBlockPos(),progress);
        if (this.leftToolProgress>=breakTicksRequired) {
            this.leftToolProgress=0;
            this.level().destroyBlock(blockHitResult.getBlockPos(),true);
        }
    }

    protected void useRightTool() {
        LogUtils.getLogger().info("Using Right mecha tool!!");
        LogUtils.getLogger().info(this.coreItemStack.getItem().toString());
        LogUtils.getLogger().info(String.valueOf(this.coreModule==null));
        LogUtils.getLogger().info(this.frameItemStack.getItem().toString());
        LogUtils.getLogger().info(String.valueOf(this.frameModule==null));
        LogUtils.getLogger().info(this.legsItemStack.getItem().toString());
        LogUtils.getLogger().info(String.valueOf(this.legModule==null));
        LogUtils.getLogger().info(String.valueOf(this.legModule.getJumpPower()));
        LogUtils.getLogger().info(this.leftToolItemStack.getItem().toString());
        LogUtils.getLogger().info(String.valueOf(this.leftArmModule==null));
        LogUtils.getLogger().info(this.rightToolItemStack.getItem().toString());
        LogUtils.getLogger().info(String.valueOf(this.rightArmModule==null));
    }

    //Copied helper function from the Item class, with minor modifications
    protected static BlockHitResult getEntityPOVHitResult(Level pLevel, Entity pEntity, double reach, ClipContext.Fluid pFluidMode) {
        float f = pEntity.getXRot();
        float f1 = pEntity.getYRot();
        Vec3 vec3 = pEntity.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = reach;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, pEntity));
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
    @Override
    public boolean isPushable() {
        return false;
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

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity pPassenger) {
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
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("MechCoreBlock", this.coreItemStack.save(new CompoundTag()));
        pCompound.put("MechFrameBlock", this.frameItemStack.save(new CompoundTag()));
        pCompound.put("MechLegBlock", this.legsItemStack.save(new CompoundTag()));
        pCompound.put("LeftToolBlock", this.leftToolItemStack.save(new CompoundTag()));
        pCompound.put("RightToolBlock", this.rightToolItemStack.save(new CompoundTag()));
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setModules(
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
