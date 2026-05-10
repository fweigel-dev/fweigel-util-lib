package dev.fweigel.mobutils.core.client.network;

import dev.fweigel.mobutils.core.network.ModHandshake;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ModHandshakeClient {

    private final ModHandshake handshake;
    private final ClientHandshakeTracker tracker = new ClientHandshakeTracker();

    public ModHandshakeClient(ModHandshake handshake) {
        this.handshake = handshake;
    }

    /**
     * Registers the client-side hello_ack receiver. Call once during client init.
     * {@code onAck} runs on the main thread when the server acknowledges.
     */
    public void registerReceiver(Runnable onAck) {
        ClientPlayNetworking.registerGlobalReceiver(handshake.getHelloAckType(), (payload, context) -> {
            context.client().execute(() -> {
                tracker.onAck();
                onAck.run();
            });
        });
    }

    /**
     * Sends the hello packet if the server supports it, otherwise runs {@code onNoServer}.
     * Call on world join.
     */
    public void onJoin(Runnable onNoServer) {
        if (ClientPlayNetworking.canSend(handshake.getHelloType())) {
            ClientPlayNetworking.send(handshake.getHelloPayload());
            tracker.startHandshake();
        } else if (onNoServer != null) {
            onNoServer.run();
        }
    }

    /** Resets handshake state. Call on disconnect. */
    public void onDisconnect() {
        tracker.reset();
    }

    /** @return true if the handshake just timed out this tick */
    public boolean tick() {
        return tracker.tick();
    }

    public boolean isServerHasMod() {
        return tracker.isServerHasMod();
    }
}
