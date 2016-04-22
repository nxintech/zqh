# -*- coding:utf-8 -*-

def merge(a, b):
    c = []
    i, j = 0, 0
#    if isinstance(a, int):
#        a = [a]
#    if isinstance(b, int):
#        b = [b]
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


def mergesort(array):
    a = array
    cache = []
    b = []  # 合并后的队列

    while len(a) > 1:
        lenth = len(a)
        if lenth % 2:  # 判断队列长度是奇数还是偶数
            for i in range(lenth / 2):
                cache.append([a.pop()])
                cache.append([a.pop()])
                tmp = merge(cache[0], cache[1])
                cache = []  # 清除缓存
                b.append(tmp)
            b = b + a
        else:
            for i in range(lenth / 2):
                cache.append([a.pop()])
                cache.append([a.pop()])
                tmp = merge(cache[0], cache[1])
                cache = []  # 清除缓存
                b.append(tmp)
        a = b
    return a


if __name__ == '__main__':
    print mergesort([2, 8, 1, 7, 112, -1, 43])
