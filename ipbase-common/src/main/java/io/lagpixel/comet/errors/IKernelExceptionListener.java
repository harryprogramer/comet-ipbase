package io.lagpixel.comet.errors;

import io.lagpixel.comet.host.AbstractServerCore;

@FunctionalInterface
public interface IKernelExceptionListener {
    void handle(Throwable t, AbstractServerCore source);
}
