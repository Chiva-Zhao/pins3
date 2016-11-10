package com.chiva;

import java.util.Random;

/**
 * Created by Coder on 2016/11/10.
 * heap priority queue
 */
public class MaxPQ<K extends Comparable<K>> {
    private K[] pq;
    private int N = 0;

    public MaxPQ(int max) {
        pq = (K[]) new Comparable[max + 1];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private void exg(int a, int b) {
        K tmp = pq[a];
        pq[a] = pq[b];
        pq[b] = tmp;
    }

    private boolean less(int a, int b) {
        return pq[a].compareTo(pq[b]) < 0;
    }

    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) {
            exg(k / 2, k);
            k /= 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && less(j, j + 1)) j++;
            if (!less(k, j)) break;
            exg(k, j);
            k = j;
        }
    }

    public void insert(K key) {
        pq[++N] = key;
        swim(N);
    }

    public K delMax() {
        K max = pq[1];
        exg(1, N--);
        pq[N + 1] = null;
        sink(1);
        return max;
    }

    private void show() {
        for (K ele : pq) {
            System.out.print(ele + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Random random = new Random();
        int max = 10;
        MaxPQ<Integer> maxPQ = new MaxPQ<>(max);
        for (int i = 0; i < max; i++)
            maxPQ.insert(random.nextInt(100));
        maxPQ.show();
        minN(100, max);
    }

    private static void minN(int maxNum, int N) {
        Random random = new Random();
        MaxPQ<Integer> maxPQ1 = new MaxPQ<>(N);
        for (int i = 1; i <= maxNum; i++) {
            int rNum = random.nextInt(maxNum);
            if (i > N)
                maxPQ1.delMax();
            maxPQ1.insert(rNum);
            maxPQ1.show();
        }
        maxPQ1.show();
    }
}
