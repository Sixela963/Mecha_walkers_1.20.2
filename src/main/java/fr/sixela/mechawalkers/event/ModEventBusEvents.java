package fr.sixela.mechawalkers.event;

import com.mojang.blaze3d.platform.InputConstants;
import fr.sixela.mechawalkers.MechaWalkersMod;
import fr.sixela.mechawalkers.entity.CarrierGolem;
import fr.sixela.mechawalkers.entity.Mecha;
import fr.sixela.mechawalkers.entity.MechaWalkersEntities;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = MechaWalkersMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void onEntityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(MechaWalkersEntities.CARRIER_GOLEM.get(), CarrierGolem.setAttributes());
        event.put(MechaWalkersEntities.MECHA.get(), Mecha.setAttributes());
    }

    @OnlyIn(Dist.CLIENT)
    public static final Lazy<KeyMapping> KEYMAP_TOOL_LEFT = Lazy.of(()-> {
        return new KeyMapping("key.mechawalkers.tool.left",
                InputConstants.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_1,
                "key.categories.misc"
        );
    });
    @OnlyIn(Dist.CLIENT)
    public static final Lazy<KeyMapping> KEYMAP_TOOL_RIGHT = Lazy.of(()-> {
        return new KeyMapping("key.mechawalkers.tool.left",
                InputConstants.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_2,
                "key.categories.misc"
        );
    });

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterKeyMappingsEvent (RegisterKeyMappingsEvent event) {
        event.register(KEYMAP_TOOL_LEFT.get());
        event.register(KEYMAP_TOOL_RIGHT.get());
    }
}
