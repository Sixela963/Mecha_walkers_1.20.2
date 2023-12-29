package fr.sixela.mechawalkers;

import com.mojang.logging.LogUtils;
import fr.sixela.mechawalkers.block.MechControlSeatBlock;
import fr.sixela.mechawalkers.block.MechaWalkersBlocks;
import fr.sixela.mechawalkers.client.models.CarrierGolemModel;
import fr.sixela.mechawalkers.client.models.MechaModel;
import fr.sixela.mechawalkers.client.renderer.CarrierGolemRenderer;
import fr.sixela.mechawalkers.client.renderer.MechaRenderer;
import fr.sixela.mechawalkers.entity.MechaWalkersEntities;
import fr.sixela.mechawalkers.item.MechaWalkersItems;
import fr.sixela.mechawalkers.item.WeldingTorchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.awt.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MechaWalkersMod.MODID)
public class MechaWalkersMod
{
    public static final String MODID = "mecha_walkers";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MechaWalkersMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        //other event listeners setup
        modEventBus.addListener(this::entityRenderers);
        modEventBus.addListener(this::registerLayerDefinitions);


        // Register the Deferred Register to the mod event bus so blocks get registered
        MechaWalkersBlocks.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        MechaWalkersItems.register(modEventBus);
        // Register entities
        MechaWalkersEntities.register(modEventBus);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

        // Some common setup code
        LOGGER.info("Mecha Walkers commonSetup running");
        /*

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

         */
    }

    public void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MechaWalkersEntities.CARRIER_GOLEM.get(), CarrierGolemRenderer::new);
        event.registerEntityRenderer(MechaWalkersEntities.MECHA.get(), MechaRenderer::new);
    }

    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CarrierGolemModel.LAYER_LOCATION,CarrierGolemModel::createBodyLayer);
        event.registerLayerDefinition(MechaModel.LAYER_LOCATION,MechaModel::createBodyLayer);
    }

    /*

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }


     */
}
