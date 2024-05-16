package fr.sixela.mechawalkers.item;

import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.block.MechaWalkersBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MechaWalkersItems {
    public static final DeferredRegister<Item>ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MechaWalkersMod.MODID);


    //----------------------------REGULAR ITEMS----------------------------//

    public static final RegistryObject<Item> WELDING_TORCH = ITEMS.register("welding_torch",
            () -> new WeldingTorchItem(new Item.Properties()
                    .stacksTo(1)));



    //----------------------------BLOCK ITEMS----------------------------//
    public static final RegistryObject<Item> MECH_CONTROL_SEAT_ITEM = ITEMS.register("mech_control_seat",
            () -> new BlockItem(MechaWalkersBlocks.MECH_CONTROL_SEAT_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> DEBUG_CORE_ITEM = ITEMS.register("debug_mech_core",
            () -> new BlockItem(MechaWalkersBlocks.DEBUG_CORE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> WOOD_FRAME_ITEM = ITEMS.register("wood_mech_frame",
            () -> new BlockItem(MechaWalkersBlocks.WOOD_FRAME_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> COPPER_FRAME_ITEM = ITEMS.register("copper_mech_frame",
            () -> new BlockItem(MechaWalkersBlocks.COPPER_FRAME_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> IRON_FRAME_ITEM = ITEMS.register("iron_mech_frame",
            () -> new BlockItem(MechaWalkersBlocks.IRON_FRAME_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BASIC_LEG_ITEM = ITEMS.register("basic_mech_leg",
            () -> new BlockItem(MechaWalkersBlocks.BASIC_LEG_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> SIMPLEDRILL_ARM_ITEM = ITEMS.register("simpledrill_mech_arm",
            () -> new BlockItem(MechaWalkersBlocks.SIMPLEDRILL_ARM_BLOCK.get(), new Item.Properties()));


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MechaWalkersMod.MODID);

    public static final RegistryObject<CreativeModeTab> MECHAWALKERS_TAB = CREATIVE_MODE_TABS.register("mechawalkers_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> WELDING_TORCH.get().getDefaultInstance())
                    .title(Component.translatable("creativetab.mecha_walkers_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(WELDING_TORCH.get()); //ADD ITEMS HERE
                        pOutput.accept(MECH_CONTROL_SEAT_ITEM.get());
                        pOutput.accept(DEBUG_CORE_ITEM.get());
                        pOutput.accept(WOOD_FRAME_ITEM.get());
                        pOutput.accept(COPPER_FRAME_ITEM.get());
                        pOutput.accept(IRON_FRAME_ITEM.get());
                        pOutput.accept(BASIC_LEG_ITEM.get());
                        pOutput.accept(SIMPLEDRILL_ARM_ITEM.get());
                    }).build());


    public static void register (IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
