package com.chiva;

import java.util.Random;

/**
 * Created by Coder on 2016/11/16.
 * 双数组实现的二分查找有序哈希表
 */
public class BinarySearchST<K extends Comparable<K>, V> {
    private K[] keys;
    private V[] values;
    int N = 0;

    public BinarySearchST(int cap) {
        keys = (K[]) new Comparable[cap];
        values = (V[]) new Object[cap];
    }

    public void put(K key, V value) {
        int index = rank(key);
        if (index < N && keys[index].compareTo(key) == 0) {
            values[index] = value;
            return;
        }
        for (int i = N; i > index; i--) {
            keys[i] = keys[i - 1];
            values[i] = values[i - 1];
        }
        keys[index] = key;
        values[index] = value;
        N++;
    }

    public V get(K key) {
        if (isEmpty()) return null;
        int index = rank(key);
        if (index < N && keys[index].compareTo(key) == 0) return values[index];
        else return null;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * 返回查找的位置或者元素应该插入的位置
     *
     * @param key
     * @return
     */
    private int rank(K key) {
        int lo = 0, hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int com = key.compareTo(keys[mid]);
            if (com == 0) return mid;
            else if (com > 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return lo;
    }


    public void show() {
        for (int i = 0; i < N; i++)
            System.out.println(keys[i] + "->" + values[i]);
    }

    public static void main(String[] args) {
        int i = 0, N = 10;
        Random random = new Random();
        BinarySearchST bst = new BinarySearchST(N);
        while (i++ < 10) {
            int k = random.nextInt(N);
            int v = random.nextInt(N * 2);
            System.out.println(k + "->" + v);
            bst.put(k, v);
        }
        System.out.println("======================");
        bst.show();
    }
}
