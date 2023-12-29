package fr.sixela.mechawalkers.entity;

import fr.sixela.mechawalkers.MechaWalkersMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MechaWalkersEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MechaWalkersMod.MODID);

    public static final RegistryObject<EntityType<CarrierGolem>> CARRIER_GOLEM = ENTITIES.register("carrier_golem",
            () -> EntityType.Builder.of(CarrierGolem::new, MobCategory.MISC).sized(0.9f,0.9f).fireImmune().build(MechaWalkersMod.MODID+":carrier_golem"));
    public static final RegistryObject<EntityType<Mecha>> MECHA = ENTITIES.register("mecha",
            () -> EntityType.Builder.of(Mecha::new, MobCategory.MISC).sized(2.9f,3.9f).build(MechaWalkersMod.MODID+":mecha"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
