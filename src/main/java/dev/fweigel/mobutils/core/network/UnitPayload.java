package dev.fweigel.mobutils.core.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class UnitPayload implements CustomPacketPayload {

    private final Type<UnitPayload> type;

    public UnitPayload(Type<UnitPayload> type) {
        this.type = type;
    }

    @Override
    public Type<UnitPayload> type() {
        return type;
    }
}
