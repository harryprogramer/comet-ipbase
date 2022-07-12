package io.lagpixel.comet.pipeline;

import org.jetbrains.annotations.Contract;

/**
 * This enumeration describes the default and recommended message decoding order.<p>
 * Example if we have {@link DedicatedPipelineOrder#ENCRYPTION} on the level 0, and {@link DedicatedPipelineOrder#COMPRESSION} on level 1,
 * set in {@link DedicatedPipelineOrder#level}
 * the packets decode flow will look like this:
 *
 * <pre>
 *
 *   Example data Input:
 *
 * |--------------------|
 * |----Recv Message----|
 * |--------------------|
 *           |
 *           |
 *          \|/
 * |-------------------|
 * |-Encryption decode-| (level 0)
 * |-------------------|
 *           |
 *           |
 *          \|/
 * |-------------------|
 * |-Compression decode| (level 1)
 * |-------------------|
 *           |
 *           |
 *          \|/
 * |-------------------|
 * |-User defined codec|
 * |-------------------|
 *
 * </pre>
 *
 * The output works the same as the input described here.
 * <p>
 * The order can be changed by method {@link DedicatedPipelineOrder#level(int)}
 * <p>
 * The purpose of the class is to simplify adding default decoders to the pipeline. Adding more decoder levels to {@link ChannelPipeline}
 * relies on the order in which the {@link ChannelPipeline#addNext(PacketCodec)} methods are called.
 * If the code looked like this:
 * <pre>
 *      DataPipeline pipeline = runtime.datapipe();
 *      pipeline.addNext(new SomeEncryptionCodec());
 *      pipeline.addNext(new SomeCompressionCodec());
 *      pipeline.addNext(new SomeUserDefinedFinalCodec());
 * </pre>
 *
 * The data pipeline will look like this:
 * <pre>
 *     Message from socket
 *             |
 *             |
 *            \|/
 *     SomeEncryptionCodec
 *             |
 *             |
 *            \|/
 *     SomeCompressionCodec
 *              |
 *              |
 *             \|/
 *    SomeUserDefinedFinalCodec
 *
 * </pre>
 *
 * This is also a good method, but the condition for operation is to maintain the correct sequence.
 * <p>
 * In case you don't want to implement this, you can use this enumeration.
 * <p>
 * No matter what order you use the {@link ChannelPipeline#add(DedicatedPipelineOrder, PacketCodec)} method, it will keep in the correct order.
 * Example code:
 * <pre>
 *  DataPipeline pipe = runtime.datapipe();
 *  pipe.add(DedicatedPipelineOrder.APPLICATION, new SomeAppCodec());
 *  pipe.add(DedicatedPipelineOrder.ENCRYPTION, new SomeEncryptionCodec());
 *  pipe.add(DedicatedPipelineOrder.COMPRESSION, new SomeCompressionnCodec());
 * </pre>
 * Pipeline result:
 * <pre>
 *     Message from socket
 *             |
 *             |
 *            \|/
 *     SomeEncryptionCodec
 *             |
 *             |
 *            \|/
 *     SomeCompressionCodec
 *              |
 *              |
 *             \|/
 *    SomeUserDefinedFinalCodec
 *
 * </pre>
 */
public enum DedicatedPipelineOrder {
    ENCRYPTION(0, "encryption"),
    COMPRESSION(1, "compression"),
    APPLICATION(2, "application")
    ;

    private final String name;
    private int level;
    DedicatedPipelineOrder(int level, String name){
        this.level = level;
        this.name = name;
    }

    public String levelName() {
        return name;
    }

    @Contract(mutates = "this")
    public void level(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
}
