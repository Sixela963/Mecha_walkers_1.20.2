package fr.sixela.mechawalkers.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

public class WeldingTorchItem extends Item {

    private static final float golemHealAmount = 25f;
    private static final int maxUses = 100;
    public WeldingTorchItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("uses",maxUses);
        pStack.setTag(tag);
        super.onCraftedBy(pStack, pLevel, pPlayer);
    }

    @Override
    public ItemStack getDefaultInstance() {
//        ItemStack itemStack = new ItemStack(this);
//        CompoundTag tag = new CompoundTag();
//        tag.putInt("uses",maxUses);
//        itemStack.setTag(tag);
//        return itemStack;
        return super.getDefaultInstance();
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return pStack.hasTag();
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        int uses = pStack.getTag().getInt("uses");
        return Math.round((float)uses * 13.0F / (float)maxUses);

        //return super.getBarWidth(pStack);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        float f = Math.max(0.0F, ((float)pStack.getTag().getInt("uses")) / (float)maxUses);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        //Can you use the tool?
        if (!pContext.getItemInHand().hasTag()){
            CompoundTag tag = new CompoundTag();
            tag.putInt("uses",maxUses);
            pContext.getItemInHand().setTag(tag);
        }
        if (pContext.getItemInHand().getTag().getInt("uses") <=0) {
            return InteractionResult.FAIL;
        }



        if (false) {
            return super.useOn(pContext);
        }else {
            //Flint and steel copy pasted behaviour

            Player player = pContext.getPlayer();
            Level level = pContext.getLevel();
            BlockPos blockpos = pContext.getClickedPos();
            BlockState blockstate = level.getBlockState(blockpos);
            if (!CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate) && !CandleCakeBlock.canLight(blockstate)) {
                BlockPos blockpos1 = blockpos.relative(pContext.getClickedFace());
                if (BaseFireBlock.canBePlacedAt(level, blockpos1, pContext.getHorizontalDirection())) {
                    level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    BlockState blockstate1 = BaseFireBlock.getState(level, blockpos1);
                    level.setBlock(blockpos1, blockstate1, 11);
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                    ItemStack itemstack = pContext.getItemInHand();
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos1, itemstack);
                        CompoundTag tag = itemstack.getTag();
                        tag.putInt("uses",tag.getInt("uses")-1);
                        itemstack.setTag(tag);
                        //player.setItemInHand(pContext.getHand(),itemstack);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockpos);
                if (player != null) {
                    CompoundTag tag = pContext.getItemInHand().getTag();
                    tag.putInt("uses",tag.getInt("uses")-1);
                    pContext.getItemInHand().setTag(tag);
                    //player.setItemInHand(pContext.getHand(),pContext.getItemInHand());
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        //return super.useOn(pContext);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        //Can you use the tool?
        if (!pStack.hasTag()){
            CompoundTag tag = new CompoundTag();
            tag.putInt("uses",maxUses);
            pStack.setTag(tag);
            pPlayer.setItemInHand(pUsedHand,pStack); //For SOME reason this line is necessary here. I don't know why and it annoys me a lot
        }
        if (pStack.getTag().getInt("uses") <=0) {
            return InteractionResult.FAIL;
        }



        //Repair golems
        if (pInteractionTarget instanceof AbstractGolem)
        {
            float healthBefore = pInteractionTarget.getHealth();
            pInteractionTarget.heal(golemHealAmount);
            if (healthBefore == pInteractionTarget.getHealth()) {
                return InteractionResult.PASS;
            }else {

                CompoundTag tag = pStack.getTag();
                tag.putInt("uses",tag.getInt("uses")-1);
                pStack.setTag(tag);
                pPlayer.setItemInHand(pUsedHand,pStack);

                return InteractionResult.sidedSuccess(pPlayer.level().isClientSide);
            }
        }else {
            return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
        }
    }
}
