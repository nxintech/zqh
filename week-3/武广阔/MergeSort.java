package com.nxin.znt.train.alg;

public class MergeSort {

	public static void main(String[] args) {
		int[] array = new int[]{10, 3, 9, 2, 6, 8};
		
		sort(array);
		
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + ", ");
		}
	}

	static void sort(int[] arr) {
		
		if (arr != null && arr.length > 0) {
			int[] tmpArr = new int[arr.length];
			
			sort(arr, tmpArr, 0, arr.length - 1);
		}
		
	}
	
	static void sort(int[] arr, int[] tmpArr, int left, int right) {
		if (left < right) {
			int center = (left + right) / 2;
			sort(arr, tmpArr, left, center);
			sort(arr, tmpArr, center + 1, right);
			merge(arr, tmpArr, left, center + 1, right);
		}
	}
	
	static void merge(int[] arr, int[] tempArr, int leftPos, int rightPos, int rightEnd) {
		int leftEnd = rightPos - 1;
		int tmpPos = leftPos;
		int numElements = rightEnd - leftPos + 1;
		
		while (leftPos <= leftEnd && rightPos <= rightEnd) {
			if (arr[leftPos] <= arr[rightPos]) {
				tempArr[tmpPos++] = arr[leftPos++];
			} else {
				tempArr[tmpPos++] = arr[rightPos++];
			}
		}
		
		while (leftPos <= leftEnd) {
			tempArr[tmpPos++] = arr[leftPos++];
		}
		
		while (rightPos <= rightEnd) {
			tempArr[tmpPos++] = arr[rightPos++];
		}
		
		for (int i = 0; i < numElements; i++) {
			arr[i] = tempArr[i];
		}
	}
}
