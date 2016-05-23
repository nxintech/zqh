#!/usr/bin/env python
# -*- coding:utf-8 -*-
# 类的方法调用：先实例化这个类，然后通过实例才能使用类的方法
# 函数要注意return,尤其在递归时

class Node:
    def __init__(self, value):
        self.data = value
        self.left = None
        self.right = None
        self.parent = None  # 二叉树的删除时，需要用到


class Binary_tree:
    def __init__(self, value):  # 构建二叉树根节点
        self.root = Node(value)

    def insert(self, value):
        insert_node = Node(value)
        return self._insert(self.root, insert_node)

    def _insert(self, node, insert_node):  # node是节点，value是待插入节点的data
        value = insert_node.data
        data = node.data
        left = node.left
        right = node.right
        if value < data:
            if left is None:
                node.left = insert_node
                insert_node.parent = node
            else:
                return self._insert(left, insert_node)
        if value > data:
            if right is None:
                node.right = insert_node
                insert_node.parent = node
            else:
                return self._insert(right, insert_node)
        if value == data:
            print "The value had been inserted into the binary_sort_tree."

    def midTraverse(self, node=Node):  # 中序遍历
        if node is None:
            return
        else:
            self.midTraverse(node.left)
            print node.data,
            self.midTraverse(node.right)

    def find(self, value):
        return self._find(self.root, value)

    def _find(self, node, value):
        data = node.data
        left = node.left
        right = node.right
        if value == data:
            return True
        if value < data:
            if left:
                return self._find(left, value)
            else:
                return False
        else:
            if right:
                return self._find(right, value)
            else:
                return False

                # if value < node.data and node.left:
                #     self._find(node.left, value)
                # if value > node.data and node.right:
                #     self._find(node.right, value)
                #
                #
                # if value < node.data and node.left is None:
                #     return False
                # if value > node.data and node.right is None:
                #     return False

    def min_node(self, node=Node):  # 查找指定节点下挂着的最小节点
        left = node.left
        if left is None:
            return node
        else:
            return self.min_node(left)

    def delete(self, value):
        return self._delete(self.root, value)

    def _delete(self, node, value):  # node是节点；value是待删除节点的值
        data = node.data
        left = node.left
        right = node.right
        parent = node.parent
        if value == data:
            if left is None:
                if right is None:
                    if parent is None:  # self.root == node
                        self.root = None  # 删除根节点，树空了
                        print "这个树空了"
                    else:
                        if value < parent.data:
                            parent.left = None
                        else:
                            parent.right = None
                else:
                    if parent is None:  # self.root == node
                        self.root = Node.right  # 试试看吧
                        self.root.parent = None
                    else:
                        if value < parent.data:
                            parent.left = right
                            right.parent = parent
                        else:
                            parent.right = right
                            right.parent = parent
            else:
                if right is None:
                    if parent is None:  # self.root == node
                        self.root = Node.left  # 试试看吧
                        self.root.parent = None
                    else:
                        if value < parent.data:
                            parent.left = left
                            left.parent = parent
                        else:
                            parent.right = left
                            left.parent = parent
                else:
                    next_node = self.min_node(right)
                    node.data, next_node.data = next_node.data, data
                    return self._delete(next_node, value)

        if value < data:
            if left:
                return self._delete(left, value)
            else:
                print "no item!"
                return
        if value > data:
            if right:
                return self._delete(right, value)
            else:
                print "no item!"
                return


if __name__ == '__main__':
    array = [34, 1, 2, 56, 100, 1009, 78, 21, 433, 0, -1]
    onetree = Binary_tree(array[0])  # 创建二叉树根节点
    # root = Node(array[0])
    r = onetree.root
    for i in array[1:]:  # 将数组插入二叉树onetree
        onetree.insert(i)
    onetree.midTraverse(r)  # 中序遍历onetree，相当于从小到大排序

    print "\n"

    onetree.delete(433)

    onetree.midTraverse(r)

    print "\n"
    print onetree.find(433)
