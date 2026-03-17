package dev.fweigel.mobutils.core.client.network;

/**
 * Manages client-side mod handshake state: tracks whether the server has the mod
 * and handles the hello timeout countdown.
 *
 * <p>Usage:
 * <pre>{@code
 * private static final ClientHandshakeTracker handshake = new ClientHandshakeTracker();
 *
 * // on HelloAck received:
 * handshake.onAck();
 *
 * // on join (if server supports hello packet):
 * handshake.startHandshake();
 *
 * // on disconnect:
 * handshake.reset();
 *
 * // every tick:
 * if (handshake.tick()) { /* timed out — server doesn't have mod *\/ }
 * }</pre>
 */
public class ClientHandshakeTracker {

    private static final int DEFAULT_TIMEOUT = 60;

    private final int timeout;
    private int handshakeTimer = -1;
    private boolean serverHasMod = false;

    public ClientHandshakeTracker() {
        this(DEFAULT_TIMEOUT);
    }

    public ClientHandshakeTracker(int timeout) {
        this.timeout = timeout;
    }

    /** Starts the handshake timeout after sending a Hello packet. */
    public void startHandshake() {
        handshakeTimer = timeout;
    }

    /** Call when the HelloAck packet is received. Marks server as having the mod. */
    public void onAck() {
        handshakeTimer = -1;
        serverHasMod = true;
    }

    /** Resets all state. Call on disconnect. */
    public void reset() {
        handshakeTimer = -1;
        serverHasMod = false;
    }

    /**
     * Advances the timeout counter by one tick.
     *
     * @return true if the handshake just timed out this tick, false otherwise
     */
    public boolean tick() {
        if (handshakeTimer > 0) {
            handshakeTimer--;
            if (handshakeTimer == 0) {
                handshakeTimer = -1;
                return true;
            }
        }
        return false;
    }

    public boolean isServerHasMod() {
        return serverHasMod;
    }
}
