package io.lagpixel.comet.errors;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message){
        super(message);
    }

    public ItemNotFoundException(){
        super();
    }
}
