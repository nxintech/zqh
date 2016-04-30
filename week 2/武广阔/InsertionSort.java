package com.nxin.znt.train.alg;

public class InsertionSort {

	public static void main(String[] args) {
		
		int[] array = new int[]{10, 3, 9, 2, 6, 8};
		array = sort(array);
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + ", ");
		}
	}

	static int[] sort(int[] array) {
		if (array != null && array.length > 0) {
			for (int i = 1; i < array.length; i++) {
				for (int j = 0; j < i; j++) {
					if (array[j] > array[i]) {
						int t = array[i];
						array[i] = array[j];
						array[j] = t;
					}
				}
			}
			return array;
		}
		return null;
	}
}
