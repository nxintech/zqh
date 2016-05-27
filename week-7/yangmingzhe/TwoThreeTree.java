package  test.study;

import java.util.List;
import java.util.zip.Inflater;

/**
 * 二三树
 * Created by yangmingzhe on 2016/5/22.
 */
public class TwoThreeTree {
    private  TwoThreeTree parentTree;
    private Integer leftData;
    private Integer rightData;
    private Integer flagCenterData;
    private TwoThreeTree leftNode;
    private TwoThreeTree centerNode;
    private TwoThreeTree centerNodeFlag;
    private TwoThreeTree rightNode;

    public TwoThreeTree() {

    }

    public TwoThreeTree(Integer data) {
        this.leftData = data;
    }

    public static TwoThreeTree create(int data) {
        return new TwoThreeTree(data);
    }

    public static TwoThreeTree addData(TwoThreeTree rootTree, int data) {
        // 搜索节点
        TwoThreeTree flagNode = searchNode(rootTree, data);
        if (flagNode.rightData == null) {
            if (flagNode.leftData > data) {
                flagNode.rightData = flagNode.leftData;
                flagNode.leftData = data;
            } else {
                flagNode.rightData = data;
            }
        } else {
            if (data < flagNode.leftData) {
                flagNode.flagCenterData = flagNode.leftData;
                flagNode.leftData = data;
            } else if (flagNode.rightData < data) {
                flagNode.flagCenterData = flagNode.rightData;
                flagNode.rightData = data;
            } else {
                flagNode.flagCenterData = data;
            }
        }
        // 如果当前节点三个值需要分裂
        TwoThreeTree editNode = editNode(flagNode);
        if (editNode != null) {
            return editNode;
        } else {
            return rootTree;
        }



    }

    /**
     * 变更节点
     * @param treeNode
     */
    public static TwoThreeTree editNode(TwoThreeTree treeNode) {
        if (treeNode.leftData != null && treeNode.rightData != null && treeNode.flagCenterData != null) {
            if (treeNode.parentTree == null) {
                TwoThreeTree parent = new TwoThreeTree(treeNode.flagCenterData);
                parent.leftNode = new TwoThreeTree(treeNode.leftData);
                parent.leftNode.parentTree = parent;
                parent.rightNode = new TwoThreeTree(treeNode.rightData);
                parent.rightNode.parentTree = parent;
                if (treeNode.leftNode != null) {
                    parent.leftNode.leftNode = treeNode.leftNode;
                    parent.leftNode.leftNode.parentTree = parent.leftNode;
                }
                if (treeNode.centerNode != null) {
                    parent.leftNode.rightNode = treeNode.centerNode;
                    parent.leftNode.rightNode.parentTree = parent.leftNode;
                }
                if (treeNode.rightNode != null) {
                    parent.rightNode.rightNode = treeNode.rightNode;
                    parent.rightNode.rightNode.parentTree = parent.rightNode;
                }
                if (treeNode.centerNodeFlag != null) {
                    parent.rightNode.leftNode = treeNode.centerNodeFlag;
                    parent.rightNode.leftNode.parentTree = parent.rightNode;
                }
                return parent;

            } else {
                TwoThreeTree parent = treeNode.parentTree;
                // 父亲节点，只有一个值
                if (parent.leftData != null && parent.flagCenterData == null && parent.rightData == null) {
                    // 左边节点
                    if (treeNode.flagCenterData < parent.leftData) {
                        parent.rightData = parent.leftData;
                        parent.flagCenterData = null;
                        parent.leftData = treeNode.flagCenterData;
                        parent.leftNode = new TwoThreeTree(treeNode.leftData);
                        parent.leftNode.parentTree = parent;
                        parent.leftNode.leftNode = treeNode.leftNode;
                        parent.leftNode.rightNode = treeNode.centerNode;;
                        parent.centerNode = new TwoThreeTree(treeNode.rightData);
                        parent.centerNode.parentTree = parent;
                        parent.centerNode.leftNode = treeNode.centerNodeFlag;
                        parent.centerNode.rightNode = treeNode.rightNode;
                    } else {
                        parent.rightData = treeNode.flagCenterData;
                        parent.flagCenterData = null;
                        parent.centerNode = new TwoThreeTree(treeNode.leftData);
                        parent.centerNode.parentTree = parent;
                        parent.centerNode.leftNode = treeNode.leftNode;
                        parent.centerNode.rightNode = treeNode.centerNode;
                        parent.rightNode = new TwoThreeTree(treeNode.rightData);
                        parent.rightNode.parentTree = parent;
                        parent.rightNode.leftNode = treeNode.centerNodeFlag;
                        parent.rightNode.rightNode = treeNode.rightNode;
                    }

                    // 递归父节点
                    return editNode(parent);
                // 父节点有两个值
                } else if (parent.leftData != null && parent.rightData != null) {
                    // 中间节点值
                    if (treeNode.flagCenterData > parent.rightData) {
                        parent.flagCenterData = parent.rightData;
                        parent.rightData = treeNode.flagCenterData;
                        // 最右边节点
                        parent.rightNode = new TwoThreeTree(treeNode.rightData);
                        parent.rightNode.parentTree = parent;
                        //中间第二个
                        parent.centerNodeFlag = new TwoThreeTree(treeNode.leftData);
                        parent.centerNodeFlag.parentTree = parent;
                    } else if (treeNode.flagCenterData < parent.leftData) {
                        parent.flagCenterData = parent.leftData;
                        parent.leftData = treeNode.flagCenterData;
                        // 左边节点
                        parent.leftNode = new TwoThreeTree(treeNode.leftData);
                        parent.leftNode.parentTree = parent;
                        // 中间第二个节点
                        parent.centerNodeFlag = parent.centerNode;
                        //中间第一个节点
                        parent.centerNode = new TwoThreeTree(treeNode.rightData);
                        parent.centerNode.parentTree = parent;
                    } else {
                        parent.flagCenterData = treeNode.flagCenterData;
                        // 中间第一个
                        parent.centerNode = new TwoThreeTree(treeNode.leftData);
                        parent.centerNode.parentTree = parent;
                        // 中间第二个
                        parent.centerNodeFlag = new TwoThreeTree(treeNode.rightData);
                        parent.centerNodeFlag.parentTree = parent;
                    }

                    // 节点递归
                    return editNode(parent);
                }
            }
        }
        return null;
    }

    public static TwoThreeTree searchNode(TwoThreeTree treeNode, int data) {
        if (treeNode.leftData > data) {
            if (treeNode.leftNode != null) {
                return searchNode(treeNode.leftNode, data);
            } else {
                return treeNode;
            }
        } else {
            if (treeNode.rightNode == null) {
                return treeNode;
            } else if (treeNode.rightData == null && treeNode.rightNode != null) {
                return searchNode(treeNode.rightNode, data);
            } else if (treeNode.rightData < data) {
                return searchNode(treeNode.rightNode, data);
            } else {
                return searchNode(treeNode.centerNode, data);
            }
        }
    }

    public static void main(String[] args) {
        //Integer[] array = new Integer[]{45,30,70,10,20,40,35};
        //Integer[] array = new Integer[]{45,30,70,10,20,40,35,49,72};
        Integer[] array = new Integer[]{45,30,70,10,20,40,35,49,72,44,36};
        TwoThreeTree rootTree = new TwoThreeTree(array[0]);
        for (int i = 1; i < array.length; i++) {
            rootTree = addData(rootTree, array[i]);
        }
    }
}
