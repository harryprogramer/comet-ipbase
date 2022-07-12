package server;

import io.lagpixel.comet.ServerRuntime;
import io.lagpixel.comet.codec.StringPacketCodec;
import io.lagpixel.comet.pipeline.ChannelPipeline;
import io.lagpixel.comet.pipeline.DedicatedPipelineOrder;
import io.netty.channel.ChannelInboundHandler;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Test {
    public static void main(String[] args) {

        ServerRuntime runtime = new ServerRuntime();
        ChannelPipeline pipeline = runtime.datapipe();
        pipeline.add(DedicatedPipelineOrder.ENCRYPTION, new PacketCodec<ByteBuffer>() {
            @Override
            public ByteBuffer input(ByteBuffer inputPacket) throws IOException {
                return null;
            }

            @Override
            public ByteBuffer output(ByteBuffer outputPacket) throws IOException {
                return null;
            }
        });
        pipeline.add(DedicatedPipelineOrder.APPLICATION, new StringPacketCodec());


        int test = 0;
        for(var v : pipeline){
            System.out.println(++test + ": " + v);
        }
    }
}
