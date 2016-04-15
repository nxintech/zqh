package com.nxin.algorithm.sort;

import java.util.Arrays;

public class InsertionSorter {

	public static void main(String[] args) {
		
		int[] array1 = { 3, -1, 0, -1,-8,10,9};
		System.out.println(Arrays.toString(getSortedArrayByInsertion(array1,0,4)));
		int[] array2 = { 6, 0, 9, -10,-18,3,4};
		System.out.println(Arrays.toString(getSortedArrayByShell(array2)));
	}
	private static int [] getSortedArrayByInsertion(int [] array,int i,int length){
		int j;
		int first=i;
		int end=i+length;
		i++;
		for(;i<end;i++){
			int currentValue=array[i];
			j=i-1;
			while(j>=first && array[j]>currentValue){
				array[j+1]=array[j];
				j--;
			}
			array[j+1]=currentValue;
		}
		return array;
	}
	
	
	private static int [] getSortedArrayByShell(int [] array){
		int length=array.length;
        
		int step=length>>1;
		int i;
		while(step>0){
			int onelen=length/step;
			for (i = 0; i < step;i++)
            {
                if(i==step-1){
                	getSortedArrayByInsertion(array,onelen*i,length-onelen*i);
                }else{
                	getSortedArrayByInsertion(array,onelen*i,onelen);
                }
                
                
            }
			step=step>>1;
		}
		
		return array;
		
	}

}