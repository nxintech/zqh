package com.nxin.znt.train.alg;

public class ShellSort {

	public static void main(String[] args) {
		int[] array = new int[]{10, 3, 9, 2, 6, 8};
		array = sort(array);
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + ", ");
		}
	}

	static int[] sort(int[] arr) {
		if (arr != null && arr.length > 0) {
			for (int step = arr.length / 2; step > 0; step = step / 2) {
				
				for (int i = step; i < arr.length; i++) {
					for (int j = i; j >= step; j -= step) {
						if (arr[j] < arr[j - step]) {
							int t = arr[j];
							arr[j] = arr[j - step];
							arr[j  - step] = t;
						}
					}
				}
			}
			
			return arr;
		}
		return null;
	}
}
