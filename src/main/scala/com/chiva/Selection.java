package com.chiva;

import java.util.Random;

public class Selection {
    private static <T> void exg(Comparable<T>[] arr, int a, int b) {
        Comparable<T> tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    private static <T> void show(Comparable<T>[] arr) {
        for (Comparable<T> ele : arr) {
            System.out.print(ele + " ");
        }
        System.out.println();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    public static <T> void selectSort(Comparable<T>[] arr) {
        for (int j = 0; j < arr.length; j++) {
            int min = j;
            for (int i = j + 1; i < arr.length; i++) {
                if (less(arr[i], arr[min]))
                    min = i;
            }
            exg(arr, min, j);
        }
    }

    public static <T> void insertSort(Comparable<T>[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (less(arr[i], arr[j])) {
                    exg(arr, i, j);
                }
            }
        }
    }

    public static <T> void shell(Comparable<T>[] arr) {
        int N = arr.length;
        int h = 1;
        while (h < N / 3)
            h = 3 * h + 1;
        while (h > 0) {
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && less(arr[j], arr[j - h]); j -= h) {
                    exg(arr, j, j - h);
                }
            }
            h /= 3;
        }
    }

    private static <T> void merge(Comparable<T>[] arr, int lo, int mid, int hi) {
        int i = lo, j = mid + 1, N = arr.length;
        Comparable[] copy = new Comparable[N];
        System.arraycopy(arr, 0, copy, 0, N);
        for (int k = lo; k <= hi; k++)
            if (i > mid)
                arr[k] = copy[j++];
            else if (j > hi)
                arr[k] = copy[i++];
            else if (less(copy[i], copy[j]))
                arr[k] = copy[i++];
            else
                arr[k] = copy[j++];
    }

    public static <T> void msort(Comparable<T>[] arr, int lo, int hi) {
        if (lo >= hi)
            return;
        int mid = (lo + hi) / 2;
        msort(arr, lo, mid);
        msort(arr, mid + 1, hi);
        merge(arr, lo, mid, hi);
    }

    public static <T> void msortBU(Comparable<T>[] arr, int lo, int hi) {
        if (lo >= hi)
            return;
        int N = arr.length;
        for (int sz = 1; sz < N; sz += sz)
            for (int j = lo; j + sz < N; j = j + 2 * sz)
                merge(arr, j, j + sz - 1, Math.min(j + 2 * sz - 1, N - 1));
    }

    public static <T> void qsort(Comparable<T>[] arr, int lo, int hi) {
        if (lo >= hi)
            return;
        int p = partation(arr, lo, hi);
        qsort(arr, lo, p - 1);
        qsort(arr, p + 1, hi);
    }

    private static <T> int partation(Comparable<T>[] arr, int lo, int hi) {
        Comparable<T> pivoit = arr[lo];
        int i = lo, j = hi + 1;
        while (true) {
            while (i < hi && less(arr[++i], pivoit))
                ;
            while (j > lo && !less(arr[--j], pivoit))
                ;
            if (i >= j)
                break;
            exg(arr, i, j);
        }
        exg(arr, lo, j);
        //		System.out.println(lo + "-" + hi + "-" + j);
        //		show(arr);
        return j;
    }

    public static void main(String[] args) {
        Integer arr[] = /*{ 54, 41, 42, 21, 31 };*/randomGen(10, 100);
        show(arr);
        //		selectSort(arr);
        //		insertSort(arr);
        //		shell(arr);
        //		show(arr);
        //		msort(arr, 0, arr.length - 1);
        msortBU(arr, 0, arr.length - 1);
        //		qsort(arr, 0, arr.length - 1);
        show(arr);
    }

    private static Integer[] randomGen(int num, int max) {
        Integer[] rst = new Integer[num];
        for (int i = 0; i < num; i++) {
            Random random = new Random();
            rst[i] = random.nextInt(max);
        }
        return rst;
    }
}
