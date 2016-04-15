package com.nxin.algorithm.sort;

import java.util.Arrays;

public class InsertionSorter {
    public static void main(String[] args) {

        int[] array1 = { 3, -1, 0, -1,-8,10,9};
        System.out.println(Arrays.toString(getSortedArrayByInsertion(array1, 0, array1.length)));
        int[] array2 = { 6, 0, 9, -10,-18,3,4};
        System.out.println(Arrays.toString(getSortedArrayByShell(array2)));
    }
    private static int [] getSortedArrayByInsertion(int [] array,int i,int length){
        if(length>array.length)
            length=array.length;
        int first=i;
        int end=i+length;
        for(i=i+1;i<end;i++){
            int currentValue=array[i];
            int j=i-1;
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
        while(step>0){
            int oneSize=length/step;
            for (int i = 0; i < step;i++)
            {
                if(i==step-1){
                    getSortedArrayByInsertion(array,oneSize*i,length-oneSize*i);
                }else{
                    getSortedArrayByInsertion(array,oneSize*i,oneSize);
                }
            }
            step=step>>1;
        }
        return array;

    }
}
