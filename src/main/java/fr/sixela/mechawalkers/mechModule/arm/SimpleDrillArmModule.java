package fr.sixela.mechawalkers.mechModule.arm;

import com.mojang.logging.LogUtils;
import fr.sixela.mechawalkers.entity.Mecha;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SimpleDrillArmModule extends MechArmAbstractModule{

    private int toolProgress = 0;
    private int cooldown = 0;
    private double toolReach = 10d;
    private final static float digSpeed = 8f;

    @Override
    public void tick() {
        if (cooldown>0) {
            cooldown--;
        }
        super.tick();
    }

    @Override
    public float getPowerCost() {
        return 10;
    }

    @Override
    public void startUsing() {
        return;
    }

    @Override
    public void stopUsing() {
        toolProgress = 0;
        return;
    }

    @Override
    public void use() {

        if (cooldown!=0) {
            return;
        }
        //LogUtils.getLogger().info("Using Left mecha tool!!");
        if (!mechaEntity.isVehicle()){
            return;
        }
        if (mechaEntity.getControllingPassenger() == null) {
            return;
        }

        BlockHitResult blockHitResult = getEntityPOVHitResult(mechaEntity.level(), mechaEntity,toolReach, ClipContext.Fluid.NONE);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            toolProgress = 0;
            cooldown = 6;
            return;
        }
//        LogUtils.getLogger().info("Starting:");
//        LogUtils.getLogger().info(mechaEntity.getControllingPassenger().getEyePosition().toString());
//        LogUtils.getLogger().info("Result:");
//        LogUtils.getLogger().info(blockHitResult.getBlockPos().toString());
        toolProgress++;
        float blockHardness = mechaEntity.level().getBlockState(blockHitResult.getBlockPos()).getDestroySpeed(mechaEntity.level(),blockHitResult.getBlockPos());
        boolean canHarvest = (mechaEntity.level().getBlockState(blockHitResult.getBlockPos()).is(BlockTags.MINEABLE_WITH_PICKAXE)||(!mechaEntity.level().getBlockState(blockHitResult.getBlockPos()).requiresCorrectToolForDrops()));
        int correctToolMultiplier =  mechaEntity.level().getBlockState(blockHitResult.getBlockPos()).is(BlockTags.MINEABLE_WITH_PICKAXE) ? 30 : 100;
        int breakTicksRequired = (int)(blockHardness*correctToolMultiplier/digSpeed);
        if (!canHarvest) {
            breakTicksRequired = (int)(blockHardness*correctToolMultiplier);
        }
        int progress = (int)(10f*toolProgress/breakTicksRequired);
        mechaEntity.level().destroyBlockProgress(mechaEntity.getId(),blockHitResult.getBlockPos(),progress);
        if (toolProgress>=breakTicksRequired) {
            toolProgress=0;
            cooldown = 6;
            mechaEntity.level().destroyBlock(blockHitResult.getBlockPos(),canHarvest);
        }
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
        Vec3 vec31 = vec3.add((double)f6 * reach, (double)f5 * reach, (double)f7 * reach);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, pEntity));
    }
}
