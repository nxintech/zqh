# -*- coding:utf-8 -*-

def mergesort(array):
    length = len(array)
    middle = length // 2
    if length > 1:
        left = array[0:middle]
        right = array[middle:]
        left = mergesort(left)
        right = mergesort(right)
        array = merge(left, right)

    return array


def merge(a, b):
    c = []
    i, j = 0, 0
    la = len(a)
    lb = len(b)
    while True:
        if i < la and j < lb:
            if a[i] < b[j]:
                c.append(a[i])
                i += 1
            else:
                c.append(b[j])
                j += 1
        else:
            if i == la and j != lb:
                c = c + b[j:]
                break
            if j == lb and i != la:
                c = c + a[i:]
                break
            if i == la and j == lb:
                break
    return c


if __name__ == '__main__':
    print mergesort([2,8,1,7,112,8000,43])
