package fr.sixela.mechawalkers.network;

import com.mojang.logging.LogUtils;
import fr.sixela.mechawalkers.entity.Mecha;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;

public class MechaWalkersPacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = ChannelBuilder.named("mechawalkerschannel").simpleChannel();

    public static void registerMessages () {
        INSTANCE.messageBuilder(ServerboundMechaToolPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ServerboundMechaToolPacket::write)
                .decoder(ServerboundMechaToolPacket::new)
                .consumerMainThread(MechaWalkersPacketHandler::handleMechaToolPacket)
                .add();
    }

    private static void handleMechaToolPacket(ServerboundMechaToolPacket pPacket, CustomPayloadEvent.Context context) {
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                LogUtils.getLogger().warn("NO PLAYER FOR MechaToolPacket!");
                return;
            }
            if (!player.isPassenger()) {
                LogUtils.getLogger().warn("PLAYER NOT PASSENGER FOR MechaToolPacket!");
                return;
            }
            Entity vehicle = player.getVehicle();
            if (vehicle == null) {
                LogUtils.getLogger().warn("NO VEHICLE FOR MechaToolPacket!");
                return;
            }
            if (vehicle instanceof Mecha) {
                ((Mecha)vehicle).setUsingTools(pPacket.getLeft(),pPacket.getRight());
            }else {
                LogUtils.getLogger().warn("VEHICLE NOT MECHA FOR MechaToolPacket!");
            }
        });
        context.setPacketHandled(true);
    }



    public static class ServerboundMechaToolPacket {

        private final boolean left;
        private final boolean right;

        public ServerboundMechaToolPacket(boolean pLeft, boolean pRight) {
            this.left = pLeft;
            this.right = pRight;
        }

        public ServerboundMechaToolPacket(FriendlyByteBuf pBuffer) {
            this.left = pBuffer.readBoolean();
            this.right = pBuffer.readBoolean();
        }

        public void write(FriendlyByteBuf pBuffer) {
            pBuffer.writeBoolean(this.left);
            pBuffer.writeBoolean(this.right);
        }

        public boolean getLeft() {
            return this.left;
        }

        public boolean getRight() {
            return this.right;
        }
    }
}
