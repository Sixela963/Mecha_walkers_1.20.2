package fr.sixela.mechawalkers.event;

import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.entity.CarrierGolem;
import fr.sixela.mechawalkers.entity.Mecha;
import fr.sixela.mechawalkers.entity.MechaWalkersEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MechaWalkersMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(MechaWalkersEntities.CARRIER_GOLEM.get(), CarrierGolem.setAttributes());
        event.put(MechaWalkersEntities.MECHA.get(), Mecha.setAttributes());
    }
}
