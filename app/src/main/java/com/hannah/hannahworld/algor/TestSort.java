package com.hannah.hannahworld.algor;

/**
 * Created by yongxu on 11/12/14.
 */
class TestSort {


    public static void main(String[] argvs) {
       int[]a = {15, 6, 18, 9, 27, 6};
        int size = a.length;
        int n = size / 2;
        for (int i = 0; i < n; i++) {
            if (2 * i + 1 < n) {
                if (a[i] >= a[2 * i] && a[i] > a[2 * i + 1]) {

                }
            }
        }
    }

    void swap(int[] a, int p, int q) {
        int x = a[p];
        a[p] = a[q];
        a[q] = x;
    }
}

