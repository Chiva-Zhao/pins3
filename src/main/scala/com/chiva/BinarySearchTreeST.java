package com.chiva;

/**
 * Created by Coder on 2016/11/24.
 * 二分查找树实现的的哈希表
 */
public class BinarySearchTreeST<K extends Comparable<K>, V> {
    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;
        private int N;

        public Node(K k, V v, int N) {
            this.key = k;
            this.value = v;
            this.N = N;
        }
    }

    public int size() {
        return size(root);
    }

    private int size(Node root) {
        if (root == null) return 0;
        return root.N;
    }

    public V get(K key) {
        return get(root, key);
    }

    private V get(Node node, K key) {
        if (node == null) return null;
        int comp = key.compareTo(node.key);
        if (comp == 0) return node.value;
        else if (comp > 0) return get(node.right, key);
        else return get(node.left, key);
    }

    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value, 1);
        }
        int comp = key.compareTo(node.key);
        if (comp == 0) node.value = value;
        else if (comp > 0) node.right = put(node.right, key, value);
        else node.left = put(node.left, key, value);
        node.N = 1 + size(node.left) + size(node.right);
        return node;
    }

    public V min() {
        return null;
    }

    public V max() {
        return null;
    }

    public V floor() {
        return null;
    }

    public V ceiling() {
        return null;
    }

}
