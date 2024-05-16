package fr.sixela.mechawalkers.block;

import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.block.MechModule.arm.SimpleDrillArmBlock;
import fr.sixela.mechawalkers.block.MechModule.core.DebugCoreBlock;
import fr.sixela.mechawalkers.block.MechModule.frame.CopperFrameBlock;
import fr.sixela.mechawalkers.block.MechModule.frame.IronFrameBlock;
import fr.sixela.mechawalkers.block.MechModule.frame.WoodFrameBlock;
import fr.sixela.mechawalkers.block.MechModule.leg.BasicLegBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MechaWalkersBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MechaWalkersMod.MODID);

    public static final RegistryObject<Block> MECH_CONTROL_SEAT_BLOCK = BLOCKS.register("mech_control_seat",
            () -> new MechControlSeatBlock(BlockBehaviour.Properties.copy(Blocks.PISTON).noOcclusion()));

    //MECH CORE BLOCKS
    public static final RegistryObject<Block> DEBUG_CORE_BLOCK = BLOCKS.register("debug_mech_core",
            () -> new DebugCoreBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    //MECH FRAME BLOCKS
    public static final RegistryObject<Block> WOOD_FRAME_BLOCK = BLOCKS.register("wood_mech_frame",
            () -> new WoodFrameBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> COPPER_FRAME_BLOCK = BLOCKS.register("copper_mech_frame",
            () -> new CopperFrameBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryObject<Block> IRON_FRAME_BLOCK = BLOCKS.register("iron_mech_frame",
            () -> new IronFrameBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    //MECH LEG BLOCKS
    public static final RegistryObject<Block> BASIC_LEG_BLOCK = BLOCKS.register("basic_mech_leg",
            () -> new BasicLegBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    //MECH ARM BLOCKS
    public static final RegistryObject<Block> SIMPLEDRILL_ARM_BLOCK = BLOCKS.register("simpledrill_mech_arm",
            () -> new SimpleDrillArmBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
