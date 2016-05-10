#!/usr/bin/env python
# -*- coding:utf-8 -*-

# 二叉堆的定义
# 二叉堆是完全二叉树或者是近似完全二叉树。
# 二叉堆满足二个特性：
# 1．父结点的键值总是大于或等于（小于或等于）任何一个子节点的键值。
# 2．每个结点的左子树和右子树都是一个二叉堆（都是大顶堆或小顶堆）。
# 当父结点的键值总是大于或等于任何一个子节点的键值时为最大堆。当父结点的键值总是小于或等于任何一个子节点的键值时为最小堆。

# 堆存储： 数组
# 建立堆：从数组末尾a[n]开始遍历，如果不符合顺序，父节点a[n-1//2]与子节点a(n)交换，n-- 依次类推
# 堆插入： 在数组末尾插入一个数，然后逐层向上调整
# 堆删除： 清除总是发生在根处a[0],然后将末尾元素移到a[0],再对堆逐层向下调整

# 堆排序： 首先可以看到堆建好之后堆中第0个数据是堆中最小的数据。取出这个数据再执行下堆的删除操作。
# 这样堆中第0个数据又是堆中最小的数据，重复上述步骤直至堆中只有一个数据时就直接取出这个数据。


# 本例中使用小顶堆，排序顺序是从小到大

# 堆的建立
def MinHeapCreate(a=[]):
    n = len(a)
    while n > 1:
        last_node = a[n - 1]
        last_node_parent = a[(n - 2) // 2]
        if last_node < last_node_parent:
            a[n - 1], a[(n - 2) // 2] = last_node_parent, last_node
            # print n, last_node, last_node_parent, a
            n -= 1
        else:
            # print n, last_node, last_node_parent, a
            n -= 1
    return a


# 堆的插入
def MinHeapAddNumber(i, a=[]):  # i是欲插入数据，a是待插入数组    #注意：函数定义时，非默认参数要放在默认参数前
    a.append(i)
    n = len(a)  # n是数组长度
    if n <= 1:
        return a
    if n > 1:
        i = n - 1  # i是索引，初始值是数组末尾
        while i > 0:  # i--，当 i=0 时循环结束
            current_node = a[i]
            parent_node = a[(i - 1) // 2]
            if current_node < parent_node:
                a[(i - 1) // 2], a[i] = current_node, parent_node  # 交换父子节点
                i = (i - 1) // 2  # 更新索引位置
            else:
                break
        return a


# 堆的删除
def MinHeapDelNumber(a=[]):  # 该函数返回值有两项，分别是“移出的数据“和”移出数据后调整完毕的堆“
    n = len(a)
    if n == 0:
        return None, []
    if n == 1:
        return a[0], []
    if n > 1:
        a[0], a[n - 1] = a[n - 1], a[0]  # 交换堆顶和堆尾
        outNumber = a.pop(n - 1)  # 将最小元素弹出
        n = len(a)
        i = 0  # 索引
        while i <= (n - 2) // 2:
            if 2 * i + 2 < n:  # 相当于，当前节点a[i]有两个子节点
                # 当前节点 与 两个子节点中 小的节点 比较 决定是否交换和继续循环
                current = a[i]
                left = a[2 * i + 1]
                right = a[2 * i + 2]
                if left < right:  # 左子节点小于右子节点
                    if current > left:
                        a[i], a[2 * i + 1] = left, current  # 交换
                        i = 2 * i + 1  # 更新当前节点索引i
                        # print "001", i, n, a
                    else:
                        # print "002", i, n, a
                        break
                else:  # 右子节点小于左子节点
                    if current > right:
                        a[i], a[2 * i + 2] = right, current  # 交换
                        i = 2 * i + 2  # 更新当前节点索引i
                        # print "003", i, n, a
                    else:
                        # print "004", i, n, a
                        break
            if 2 * i + 2 == n:  # 相当于，当前节点a[i]只有一个左子节点
                # 当前节点 与 其 唯一的子节点 比较 决定是否交换并结束循环
                current = a[i]
                left = a[2 * i + 1]
                if current > left:
                    a[i], a[2 * i + 1] = left, current  # 交换
                    # print "005", i, n, a
                    break
                else:
                    # print "006", i, n, a
                    break
        return outNumber, a


def MinHeapSort(a=[]):
    s = []  # s为排序数组
    while len(a) > 0:
        outNumber, a = MinHeapDelNumber(a)  # 堆的删除
        s.append(outNumber)
    return s


if __name__ == '__main__':
    a = [1, 9, 2, 8, 5, 101, 222, 4, 0, -123]
    a = MinHeapCreate(a)  # 堆的建立
    MinHeapAddNumber(-5, a) # 堆的插入
    print MinHeapSort(a)  # 堆的排序
    
