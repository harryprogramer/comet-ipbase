package io.lagpixel.comet.codec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringPacketCodec implements PacketCodec<String> {
    private final Charset charset;

    public StringPacketCodec(){
        this(StandardCharsets.US_ASCII);
    }

    public StringPacketCodec(Charset charset){
        this.charset = charset;
    }

    @Override
    public String input(ByteBuffer inputPacket) throws IOException {
        return charset.decode(inputPacket).toString();
    }

    @Override
    public ByteBuffer output(String outputPacket) throws IOException {
        return charset.encode(outputPacket);
    }
}
