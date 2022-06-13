package io.lagpixel.comet.errors;

public final class TransportNotFound extends RuntimeException {
    public TransportNotFound(String message){
        super(message);
    }

    public TransportNotFound(){
        super();
    }
}
