package io.lagpixel.comet.codec;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface PacketCodec<T> {
    T input(ByteBuffer inputPacket) throws IOException;

    ByteBuffer output(T outputPacket) throws IOException;
}
