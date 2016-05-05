//
//  QuickSort.m
//  TestProject
//
//  Created by zengxiangrong on 16/5/5.
//  Copyright © 2016年 zengxiangrong. All rights reserved.
//

#import "QuickSort.h"

@implementation QuickSort
-(void)quickSort:(NSMutableArray *)array leftIndex:(int)leftIndex  rightIndex:(int)rightIndex{
    
    if(leftIndex<rightIndex){
        
        int i=leftIndex;
        int j=rightIndex;
        
        //随意选取数组中的一个数当作分段数组闸值
        int breakValue=[array[leftIndex] intValue];
        
        while (i<j) {
            
            //从数组末尾开始遍历，如果大于闸值则遍历下个
            while (i<j&&breakValue<=[array[j] intValue]) {
                j--;
            }
            if (i<j) {
                array[i]=array[j];
            }
            
            while (i<j&&[array[i] intValue]<= breakValue) {
                
                i++;
            }
            if (i<j) {
                array[j]=array[i];
            }
            
        }
        
        array[i]=[NSNumber numberWithInt:breakValue];
        
        
        [self quickSort:array leftIndex:leftIndex rightIndex:i-1];
        [self quickSort:array leftIndex:i+1 rightIndex:rightIndex];
      
        
    }
    
}
@end
