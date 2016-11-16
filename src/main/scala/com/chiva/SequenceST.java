package com.chiva;

/**
 * Created by Coder on 2016/11/10.
 */
public class SequenceST<K, V> {
    private Node first;

    class Node {
        private K key;
        private V value;
        private Node next;

        public Node(K k, V v, Node next) {
            this.key = k;
            this.value = v;
            this.next = next;
        }
    }

    public void put(K k, V v) {
        Node tmp = first;
        for (Node n = first; n != null; n = n.next) {
            if (n.key.equals(k)) {
                n.value = v;
                return;
            }
        }
       /* while (tmp != null) {
            if (tmp.key == k) {
                tmp.value = v;
                return;
            } else
                tmp = tmp.next;
        }*/
        first = new Node(k, v, first);
    }

    public V get(K k) {
        V v = null;
        for (Node n = first; n != null; n = n.next) {
            if (n.key.equals(k)) {
                v = n.value;
                break;
            }
        }
        return v;
    }

}
