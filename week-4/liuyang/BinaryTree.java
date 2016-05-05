/**
 * Created on 2016/5/5.
 * author web
 */
public class BinaryTree {
    private TreeNode root = null;

    public BinaryTree(int data) {
        root = new TreeNode(data);
    }

    public void insert(int data) {
        TreeNode currentroot = root;
        subinsert(data, root);
    }

    public void subinsert(int data, TreeNode current) {
        if (data <= current.data) {
            if(null == current.left) {
                current.left = new TreeNode(data);
            } else {
                current = current.left;
                subinsert(data, current);
            }
        } else {
            if(null == current.right) {
                current.right = new TreeNode(data);
            } else {
                current = current.right;
                subinsert(data, current);
            }
        }
    }

    // 中序遍历
    public void inorderTraversal(TreeNode parent) {
        if(parent != null) {
            inorderTraversal(parent.left);
            System.out.println(parent.data);
            inorderTraversal(parent.right);
        }
    }

    private class TreeNode {
        private TreeNode left;
        private TreeNode right;
        private int data;

        public TreeNode(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }


    public static void main(String[] args) {
        BinaryTree bt = new BinaryTree(26);
        bt.insert(3);
        bt.insert(13);
        bt.insert(29);
        bt.insert(33);
        bt.insert(28);
        bt.insert(1);
        bt.inorderTraversal(bt.root);
    }
}