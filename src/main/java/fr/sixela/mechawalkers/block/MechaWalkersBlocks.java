package fr.sixela.mechawalkers.block;

import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.item.MechaWalkersItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MechaWalkersBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MechaWalkersMod.MODID);

    public static final RegistryObject<Block> MECH_CONTROL_SEAT_BLOCK = BLOCKS.register("mech_control_seat",
            () -> new MechControlSeatBlock(BlockBehaviour.Properties.copy(Blocks.PISTON).noOcclusion()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
