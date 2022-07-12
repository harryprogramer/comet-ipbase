package io.lagpixel.comet.codec.compression;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ZStdCompressionCodec implements PacketCodec<ByteBuffer> {
    @Override
    public ByteBuffer input(ByteBuffer inputPacket) throws IOException {
        return null;
    }

    @Override
    public ByteBuffer output(ByteBuffer outputPacket) throws IOException {
        return null;
    }
}
