package io.lagpixel.comet.errors;

import java.io.IOException;

public final class BindException extends IOException {
    public BindException(String message){
        super(message);
    }

    public BindException(){
        super();
    }
}
