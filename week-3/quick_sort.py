# -*- coding:utf-8 -*-

def qsort(array):
    if len(array) <= 1:
        return array

    pivot = array[0]
    left = []
    right = []
    for i in array:
        if i < pivot:
            left.append(i)
        if i > pivot:
            right.append(i)
    return qsort(left) + [pivot] + qsort(right)

print qsort([16,7,3,20,17,8])
