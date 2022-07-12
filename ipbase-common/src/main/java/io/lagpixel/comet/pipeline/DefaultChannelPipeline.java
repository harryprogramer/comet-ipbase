package io.lagpixel.comet.pipeline;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.lagpixel.comet.errors.ItemNotFoundException;
import io.lagpixel.comet.errors.NotImplementedYet;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultChannelPipeline extends AbstractChannelPipeline {
    private final TreeMap<String, PacketCodec<?>> codecs = new TreeMap<>();
    private static final AtomicInteger codecIds = new AtomicInteger(0);

    private void moveItem(int order, String name, PacketCodec<?> codec){
        BiMap<String, PacketCodec<?>> codecs = HashBiMap.create();
        for(int i = 0; i < this.codecs.size(); i++){
            if((i + 1) == order){
                codecs.put(name, codec);
                continue;
            }

            String k =          (String)            this.codecs.keySet().toArray()[i];
            PacketCodec<?> v =  (PacketCodec<?>)    this.codecs.values().toArray()[i];
            codecs.put(k, v);
        }
    }

    @Override
    public Collection<PacketCodec<?>> get() {
        return codecs.descendingMap().values();
    }

    @Override
    public void remove(int index) throws UnsupportedOperationException {
        throw new NotImplementedYet();
    }

    @Override
    public void add(DedicatedPipelineOrder order, PacketCodec<?> codec) {
        codecs.put(order.levelName(), codec);
    }

    @Override
    public void addNext(@Nullable String name, PacketCodec<?> codec) {
        this.codecs.put(name == null ? "codec-" + codecIds.getAndIncrement() : name, codec);
    }

    @Override
    public void remove(String name) {
        this.codecs.remove(name);
    }

    @Override
    public void remove(Class<? extends PacketCodec<?>> codec) {
        for(Map.Entry<String, PacketCodec<?>> entry: codecs.entrySet()) {
            if(codec.isInstance(entry.getValue())){
                this.codecs.remove(entry.getKey());
                return;
            }
        }

        throw new ItemNotFoundException("codec " + codec + " it's not registered in pipeline");
    }

    @Nullable
    @Override
    public PacketCodec<?> get(String name) {
        return this.codecs.get(name);
    }
}
