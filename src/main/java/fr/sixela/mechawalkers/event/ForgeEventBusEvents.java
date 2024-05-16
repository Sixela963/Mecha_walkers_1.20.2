package fr.sixela.mechawalkers.event;

import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.block.MechaWalkersBlocks;
import fr.sixela.mechawalkers.entity.CarrierGolem;
import fr.sixela.mechawalkers.entity.Mecha;
import fr.sixela.mechawalkers.entity.MechaWalkersEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MechaWalkersMod.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {


    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        LevelAccessor level = event.getLevel();
        BlockPos blockPos = event.getPos();
        if (level.getBlockState(blockPos).is(MechaWalkersBlocks.MECH_CONTROL_SEAT_BLOCK.get()) && level.getBlockState(blockPos.below()).is(Blocks.FURNACE)) {
            CarrierGolem carrierGolem = MechaWalkersEntities.CARRIER_GOLEM.get().create(event.getEntity().level());

            float spawnAngle = level.getBlockState(blockPos.below()).getValue(FurnaceBlock.FACING).toYRot();

            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(blockPos.below(), Blocks.AIR.defaultBlockState(), 2);

            carrierGolem.moveTo(blockPos.below(),spawnAngle,0f);
            carrierGolem.setYBodyRot(spawnAngle);
            carrierGolem.setYHeadRot(spawnAngle);
            level.addFreshEntity(carrierGolem);

            level.blockUpdated(blockPos, Blocks.AIR);
            level.blockUpdated(blockPos.below(), Blocks.AIR);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if ((event.getEntity() instanceof Enemy) && (event.getEntity() instanceof Mob)) {
            Mob mob = (Mob)event.getEntity();
            mob.goalSelector.addGoal(2,new NearestAttackableTargetGoal<>(mob,Mecha.class,true));
        }
    }
}
