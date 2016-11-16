package com.chiva;

/**
 * Created by Coder on 2016/11/10.
 */
public interface ST<K, V> {
    void put(K k, V v);

    V get(K k);

    void delete(K k);

    boolean contain(K k);

    boolean isEmpty();

    int size();

    Iterable<K> keys();

}
