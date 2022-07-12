package io.lagpixel.comet.common;

import java.util.Collection;
import java.util.List;

/**
* Basic interface to tag other classes as listable, but read-only (excepting removing method {@link Listable#remove(int)}).
* */
public interface Listable<T> extends Iterable<T> {
    /**
     * Get the size of the given list.
     * @return size of list
     */
    int size();

    /**
     * Get item by index
     * @param index index to get
     * @return item from index
     */
    T get(int index);

    /**
     *
     * Get all items from the list.
     * @return item list
     */
    Collection<T> get();

    /**
     * Remove item from list by index.
     * <p>
     * In some implementation this method may be unavailable due
     * to the principle of the interface policy which is read only access.
     * This depends on implementation and internals.
     * @param index index to remove
     */
    void remove(int index) throws UnsupportedOperationException;
}
