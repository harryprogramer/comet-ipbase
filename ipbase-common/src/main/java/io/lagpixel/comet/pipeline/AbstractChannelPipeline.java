package io.lagpixel.comet.pipeline;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public abstract class AbstractChannelPipeline implements ChannelPipeline {
    @NotNull
    @Override
    public Iterator<PacketCodec<?>> iterator() {
        return get().iterator();
    }

    @Override
    public int size() {
        return get().size();
    }

    @Override
    public PacketCodec<?> get(int index) {
        return null;
    }

    @Override
    public void addNext(PacketCodec<?> codec) {
        addNext(null, codec);
    }

    @Override
    public void removeFirst() {
        int size = get().size();
        if(!(size > 0)){
            throw new IndexOutOfBoundsException("no handles have been set, current size of handlers is " + size);
        }
        remove(0);
    }

    @Override
    public void removeLast() {
        int size = get().size();
        if(!(size > 0)){
            throw new IndexOutOfBoundsException("no handles have been set, current size of handlers is " + size);
        }
        remove(size - 1);
    }


}
