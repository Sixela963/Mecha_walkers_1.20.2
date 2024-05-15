package fr.sixela.mechawalkers.block.MechModule.core;

import fr.sixela.mechawalkers.block.MechModule.MechModuleAbstractBlock;
import fr.sixela.mechawalkers.block.MechModule.arm.MechArmAbstractBlock;
import fr.sixela.mechawalkers.block.MechModule.frame.MechFrameAbstractBlock;
import fr.sixela.mechawalkers.block.MechModule.leg.MechLegAbstractBlock;
import fr.sixela.mechawalkers.block.MechaWalkersBlocks;
import fr.sixela.mechawalkers.entity.Mecha;
import fr.sixela.mechawalkers.entity.MechaWalkersEntities;
import fr.sixela.mechawalkers.item.MechaWalkersItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class MechCoreAbstractBlock extends MechModuleAbstractBlock {

    public MechCoreAbstractBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    private BlockPattern size1MechaPattern;

    private BlockPattern getOrCreateSize1MechaPattern() {
        if (this.size1MechaPattern==null)
        {
            this.size1MechaPattern = BlockPatternBuilder.start()
                    .aisle("~S~",
                            "ACA",
                            "~F~",
                            "L~L")
                    .where('~', blockInWorld -> {return blockInWorld.getState().isAir();})
                    .where('S',blockInWorld -> {return blockInWorld.getState().is(MechaWalkersBlocks.MECH_CONTROL_SEAT_BLOCK.get());})
                    .where('C',blockInWorld -> {return blockInWorld.getState().getBlock() instanceof MechCoreAbstractBlock;})
                    .where('A',blockInWorld -> {return blockInWorld.getState().getBlock() instanceof MechArmAbstractBlock;})
                    .where('F',blockInWorld -> {return blockInWorld.getState().getBlock() instanceof MechFrameAbstractBlock;})
                    .where('L',blockInWorld -> {return blockInWorld.getState().getBlock() instanceof MechLegAbstractBlock;})
                    .build();
        }
        return this.size1MechaPattern;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack item = pPlayer.getItemInHand(pHand);

        if (item.is(MechaWalkersItems.WELDING_TORCH.get())) {
            tryAssembleMechaFromStructure(pLevel,pPos);
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    private void tryAssembleMechaFromStructure(Level pLevel, BlockPos pPos) {
        //Using BlockPatterns, try and find a size 1 mecha structure. if found, remove it and make a mecha from the appropriate modules
        BlockPattern.BlockPatternMatch patternMatch = this.getOrCreateSize1MechaPattern().find(pLevel,pPos);
        if (patternMatch != null) {
            /*LogUtils.getLogger().info("Recognized mecha pattern");
            LogUtils.getLogger().info(patternMatch.getBlock(1, 2, 0).getState().getBlock().getDescriptionId()); //frame
            LogUtils.getLogger().info(patternMatch.getBlock(1, 1, 0).getState().getBlock().getDescriptionId()); //core
            LogUtils.getLogger().info(patternMatch.getBlock(1, 0, 0).getState().getBlock().getDescriptionId()); //seat
            LogUtils.getLogger().info(patternMatch.getBlock(0, 1, 0).getState().getBlock().getDescriptionId()); //tool 1
            LogUtils.getLogger().info(patternMatch.getBlock(2, 1, 0).getState().getBlock().getDescriptionId()); //tool 2
            LogUtils.getLogger().info(patternMatch.getBlock(0, 3, 0).getState().getBlock().getDescriptionId()); //foot 1
            LogUtils.getLogger().info(patternMatch.getBlock(2, 3, 0).getState().getBlock().getDescriptionId()); //foot 2*/
            Mecha mecha = MechaWalkersEntities.MECHA.get().create(pLevel);
            if (mecha != null) {
                mecha.setModuleItems(
                        patternMatch.getBlock(1, 1, 0).getState().getBlock().asItem().getDefaultInstance(),
                        patternMatch.getBlock(1, 2, 0).getState().getBlock().asItem().getDefaultInstance(),
                        patternMatch.getBlock(0, 3, 0).getState().getBlock().asItem().getDefaultInstance(),
                        patternMatch.getBlock(0, 1, 0).getState().getBlock().asItem().getDefaultInstance(),
                        patternMatch.getBlock(2, 1, 0).getState().getBlock().asItem().getDefaultInstance());
                BlockPos mechSpawnPos = patternMatch.getBlock(1, 3, 0).getPos();
                clearPatternBlocks(pLevel, patternMatch);
                mecha.moveTo(mechSpawnPos,0,0);
                pLevel.addFreshEntity(mecha);

                updatePatternBlocks(pLevel, patternMatch);
            }
        }
    }


    //Helper functions copied from CarvedPumpkinBlock
    public static void clearPatternBlocks(Level pLevel, BlockPattern.BlockPatternMatch pPatternMatch) {
        for(int i = 0; i < pPatternMatch.getWidth(); ++i) {
            for(int j = 0; j < pPatternMatch.getHeight(); ++j) {
                BlockInWorld blockinworld = pPatternMatch.getBlock(i, j, 0);
                pLevel.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                pLevel.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
            }
        }

    }

    public static void updatePatternBlocks(Level pLevel, BlockPattern.BlockPatternMatch pPatternMatch) {
        for(int i = 0; i < pPatternMatch.getWidth(); ++i) {
            for(int j = 0; j < pPatternMatch.getHeight(); ++j) {
                BlockInWorld blockinworld = pPatternMatch.getBlock(i, j, 0);
                pLevel.blockUpdated(blockinworld.getPos(), Blocks.AIR);
            }
        }

    }
}