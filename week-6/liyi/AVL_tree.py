#!/usr/bin/env python
# -*- coding:utf-8 -*-
# AVL tree 平衡二叉树

class AVL_Node:
    def __init__(self, value):
        self.data = value
        self.left = None
        self.right = None
        self.parent = None  # 二叉树的删除时，需要用到
        # self.highth = 0


class AVL_tree:
    def __init__(self, value):  # 构建二叉树根节点
        self.root = AVL_Node(value)

    def height(self, node):
        h = self._height(node)
        return max(h[0], h[1])

    def _height(self, node, h=(0, 0)):  # 计算节点高度；node是指定节点，h是高度累计值；返回两个值，分别是左高和右高
        if node is None:
            return h[0], h[1]   # 如果return返回两个值，默认是元组格式
        else:
            left = node.left
            right = node.right
            if left:
                if right:
                    hl = self._height(left, (h[0] + 1, h[1] + 1))
                    hr = self._height(right, (h[0] + 1, h[1] + 1))
                    return max(hl[0], hl[1]), max(hr[0], hr[1])
                else:
                    hl = self._height(left, (h[0] + 1, h[1] + 1))
                    return max(hl[0], hl[1]), h[1]
            else:
                if right:
                    hr = self._height(right, (h[0] + 1, h[1] + 1))
                    return h[0], max(hr[0], hr[1])
                else:
                    return h[0], h[1]

    def _RightRotate(self, node=AVL_Node):
        #定义节点的内存地址：开始
        # cl = current.left
        # cr = current.right
        # cll = cl.left
        # clr = cl.right
        # #定义节点的内存地址：结束
        # #开始旋转
        # tmp = current.data         #记录旧data的value
        # current.data = cl.data  #当前节点：data更新
        # current.left = cll      #当前节点：左指针更新
        # cll.parent = current        #新左节点：父指针更新
        # current.right = cl      #当前节点：右指针更新（新右节点无需更新它的父指针，因为其指向未变）
        # cl.data = tmp    #新右节点：data更新
        # cl.left = clr     #新右节点：更新左指针（新右节点的左节点无需更新它的父指针，因为其指向未变）
        # cl.right = cr     #新右节点：更新右指针
        # if cr:
        #     cr.parent = cl        #新右节点的右节点：更新父指针
        return self.root

    def _LeftRotate(self, node=AVL_Node):
        # #定义节点的内存地址：开始
        # cl = current.left
        # cr = current.right
        # crl = cr.left
        # crr = cr.right
        # #定义节点的内存地址：结束
        # #开始旋转
        # tmp = current.data
        # current.data = cr.data
        # current.right = crr
        # crr.parent = current
        # current.left = cr
        # cr.data = tmp
        # cr.right = crl
        # cr.left = cl
        # if cl:
        #     cl.parent = cr
        return self.root

    def _rotation(self, node):
        current = node.parent
        while current:
            h = self._height(current)
            diff = h[0] - h[1]
            if diff > 1:  # 左比右重
                left_child_height = self._height(current.left)
                if left_child_height[0] > left_child_height[1]:
                    #self._RighttRotate(current)
                    #定义节点的内存地址：开始
                    cl = current.left
                    cr = current.right
                    cll = cl.left
                    clr = cl.right
                    #定义节点的内存地址：结束
                    #开始旋转
                    tmp = current.data         #记录旧data的value
                    current.data = cl.data  #当前节点：data更新
                    current.left = cll      #当前节点：左指针更新
                    cll.parent = current        #新左节点：父指针更新
                    current.right = cl      #当前节点：右指针更新（新右节点无需更新它的父指针，因为其指向未变）
                    cl.data = tmp    #新右节点：data更新
                    cl.left = clr     #新右节点：更新左指针（新右节点的左节点无需更新它的父指针，因为其指向未变）
                    cl.right = cr     #新右节点：更新右指针
                    if cr:
                        cr.parent = cl        #新右节点的右节点：更新父指针
                else:
                    #self._LeftRotate(current.left)
                    cl = current.left.left
                    cr = current.left.right
                    crl = cr.left
                    crr = cr.right
                    #定义节点的内存地址：结束
                    #开始旋转
                    tmp = current.left.data
                    current.left.data = cr.data
                    current.left.right = crr
                    if crr:
                        crr.parent = current.left
                    current.left.left = cr
                    cr.data = tmp
                    cr.right = crl
                    cr.left = cl
                    if cl:
                        cl.parent = cr

                    #self._RightRotate(current)
                    #定义节点的内存地址：开始
                    cl = current.left
                    cr = current.right
                    cll = cl.left
                    clr = cl.right
                    #定义节点的内存地址：结束
                    #开始旋转
                    tmp = current.data         #记录旧data的value
                    current.data = cl.data  #当前节点：data更新
                    current.left = cll      #当前节点：左指针更新
                    cll.parent = current        #新左节点：父指针更新
                    current.right = cl      #当前节点：右指针更新（新右节点无需更新它的父指针，因为其指向未变）
                    cl.data = tmp    #新右节点：data更新
                    cl.left = clr     #新右节点：更新左指针（新右节点的左节点无需更新它的父指针，因为其指向未变）
                    cl.right = cr     #新右节点：更新右指针
                    if cr:
                        cr.parent = cl        #新右节点的右节点：更新父指针

            if diff < -1:  # 右比左重
                right_child_height = self._height(current.right)
                if right_child_height[0] > right_child_height[1]:
                    #self._RightRotate(current.right)
                    #定义节点的内存地址：开始
                    cl = current.right.left
                    cr = current.right.right
                    cll = cl.left
                    clr = cl.right
                    #定义节点的内存地址：结束
                    #开始旋转
                    tmp = current.right.data         #记录旧data的value
                    current.right.data = cl.data  #当前节点：data更新
                    current.right.left = cll      #当前节点：左指针更新
                    if cll:
                        cll.parent = current.right        #新左节点：父指针更新
                    current.right.right = cl      #当前节点：右指针更新（新右节点无需更新它的父指针，因为其指向未变）
                    cl.data = tmp    #新右节点：data更新
                    cl.left = clr     #新右节点：更新左指针（新右节点的左节点无需更新它的父指针，因为其指向未变）
                    cl.right = cr     #新右节点：更新右指针
                    if cr:
                        cr.parent = cl        #新右节点的右节点：更新父指针

                    #self._LeftRotate(current)
                    #定义节点的内存地址：开始
                    cl = current.left
                    cr = current.right
                    crl = cr.left
                    crr = cr.right
                    #定义节点的内存地址：结束
                    #开始旋转
                    tmp = current.data
                    current.data = cr.data
                    current.right = crr
                    crr.parent = current
                    current.left = cr
                    cr.data = tmp
                    cr.right = crl
                    cr.left = cl
                    if cl:
                        cl.parent = cr

                else:
                    #self._LeftRotate(current)
                    #定义节点的内存地址：开始
                    cl = current.left
                    cr = current.right
                    crl = cr.left
                    crr = cr.right
                    #定义节点的内存地址：结束
                    #开始旋转
                    tmp = current.data
                    current.data = cr.data
                    current.right = crr
                    crr.parent = current
                    current.left = cr
                    cr.data = tmp
                    cr.right = crl
                    cr.left = cl
                    if cl:
                        cl.parent = cr

            current = current.parent
        return self.root

    def insert(self, value):  # 该函数，先插入，后旋转
        insert_node = AVL_Node(value)
        node = self._insert(self.root, insert_node)
        self._rotation(node)
        return self.root

    def _insert(self, node, insert_node):  # node是节点，value是待插入节点的data
        value = insert_node.data
        data = node.data
        left = node.left
        right = node.right
        if value < data:
            if left is None:
                node.left = insert_node
                insert_node.parent = node
                return insert_node
            else:
                return self._insert(left, insert_node)
        if value > data:
            if right is None:
                node.right = insert_node
                insert_node.parent = node
                return insert_node
            else:
                return self._insert(right, insert_node)
        if value == data:
            print "The value had been inserted into the binary_sort_tree."
            return False

    def midTraverse(self, node=AVL_Node):  # 中序遍历
        if node is None:
            return
        else:
            self.midTraverse(node.left)
            print node.data,
            self.midTraverse(node.right)


if __name__ == '__main__':
    array = [34, 1, 2, 56, 100, 1009, 78, 21, 433, 0, -1]
    onetree = AVL_tree(array[0])  # 创建二叉树根节点
    r = onetree.root
    for i in array[1:]:  # 将数组插入二叉树onetree
        onetree.insert(i)
    onetree.midTraverse(r)  # 中序遍历onetree，相当于从小到大排序
    print "\n"
    h = onetree.height(r)
    print "height =", h
