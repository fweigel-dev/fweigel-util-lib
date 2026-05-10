package dev.fweigel.mobutils.core.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class ModHandshake {

    private final CustomPacketPayload.Type<UnitPayload> helloType;
    private final CustomPacketPayload.Type<UnitPayload> helloAckType;
    private final UnitPayload helloPayload;
    private final UnitPayload helloAckPayload;
    private final ServerModPlayerRegistry modPlayers = new ServerModPlayerRegistry();

    public ModHandshake(String modId) {
        this.helloType = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(modId, "hello"));
        this.helloAckType = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(modId, "hello_ack"));
        this.helloPayload = new UnitPayload(helloType);
        this.helloAckPayload = new UnitPayload(helloAckType);
    }

    public void registerPayloads() {
        PayloadTypeRegistry.serverboundPlay().register(helloType, StreamCodec.unit(helloPayload));
        PayloadTypeRegistry.clientboundPlay().register(helloAckType, StreamCodec.unit(helloAckPayload));
    }

    public void registerServerReceiver(Logger logger) {
        ServerPlayNetworking.registerGlobalReceiver(helloType, (payload, context) -> {
            ServerPlayer player = context.player();
            modPlayers.add(player);
            ServerPlayNetworking.send(player, helloAckPayload);
            logger.debug("Handshake with {}", player.getName().getString());
        });
    }

    public CustomPacketPayload.Type<UnitPayload> getHelloType() {
        return helloType;
    }

    public UnitPayload getHelloPayload() {
        return helloPayload;
    }

    public CustomPacketPayload.Type<UnitPayload> getHelloAckType() {
        return helloAckType;
    }

    public ServerModPlayerRegistry getModPlayers() {
        return modPlayers;
    }
}
