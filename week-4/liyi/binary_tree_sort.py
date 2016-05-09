#!/usr/bin/env python
# -*- coding:utf-8 -*-
# 类的方法调用：先实例化这个类，然后通过实例才能使用类的方法

class Node:
    def __init__(self, value):
        self.data = value
        self.left = None
        self.right = None


class Binary_tree:
    def __init__(self, value):  # 构建二叉树根节点
        self.root = Node(value)

    def insert(self, node, value):
        n = Node(value)
        if value < node.data:
            if node.left == None:
                node.left = n
            else:
                self.insert(node.left, value)
        if value > node.data:
            if node.right == None:
                node.right = n
            else:
                self.insert(node.right, value)
        if value == node.data:
            print "The value had been inserted into the binary_sort_tree."

    def midTraverse(self, node):  # 中序遍历
        if node == None:
            return
        else:
            self.midTraverse(node.left)
            print node.data,
            self.midTraverse(node.right)
            

if __name__ == '__main__':
    array = [34, 1, 2, 56, 100, 1009, 78, 21, 433, 0, -1]
    onetree = Binary_tree(array[0])  # 创建二叉树根节点
    # root = Node(array[0])
    r = onetree.root
    for i in array[1:]:  # 将数组插入二叉树onetree
        onetree.insert(r, i)
    onetree.midTraverse(r)  # 中序遍历onetree，相当于从小到大排序
