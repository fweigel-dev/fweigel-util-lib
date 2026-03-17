package dev.fweigel.mobutils.core.network;

import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Tracks the set of players that have the mod installed, based on handshake packets.
 * Uses weak references so players are automatically removed when garbage collected.
 *
 * <p>Usage:
 * <pre>{@code
 * private static final ServerModPlayerRegistry modPlayers = new ServerModPlayerRegistry();
 *
 * // on HelloC2S received:
 * modPlayers.add(player);
 *
 * // when sending mod-specific packets:
 * if (modPlayers.contains(player)) { ServerPlayNetworking.send(player, payload); }
 *
 * // to iterate all registered players:
 * for (ServerPlayer p : modPlayers.getAll()) { ... }
 * }</pre>
 */
public class ServerModPlayerRegistry {

    private final Set<ServerPlayer> players = Collections.newSetFromMap(new WeakHashMap<>());

    public void add(ServerPlayer player) {
        players.add(player);
    }

    public boolean contains(ServerPlayer player) {
        return players.contains(player);
    }

    public Set<ServerPlayer> getAll() {
        return Collections.unmodifiableSet(players);
    }
}
