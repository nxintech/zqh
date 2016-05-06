package com.nxin.structure.tree.binarytree;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
	private Node root;
	
	public void createBinaryTree(Node node,int data){
		if(null==node){
			root=new Node();
			root.setData(data);
		}else{
			if(data<node.data){//data<parent.data , 置为左节点
				if(null==node.leftNode){
					Node leftNode=new Node();
					leftNode.setData(data);
					node.leftNode=leftNode;
				}else{
					createBinaryTree(node.leftNode, data);
				}
			}else{//>父节点，放置右节点
				if(null==node.rightNode){
					Node rightNode=new Node();
					rightNode.setData(data);
					node.rightNode=rightNode;
				}else{
					createBinaryTree(node.rightNode, data);
				}
			}
		}
			
			
	}
	private List<Node> preOrderList=new ArrayList<Node>();
	private List<Node> inOrderList=new ArrayList<Node>();
	private List<Node> postOrderList=new ArrayList<Node>();
	//Preorder Traversal 前序遍历：根节点->左子树->右子树
	private void preOrder(Node node){
		preOrderList.add(node);
		if(null!=node.getLeftNode()){
			preOrder(node.getLeftNode());
		}
		if(null!=node.getRightNode()){
			preOrder(node.getRightNode());
		}
	}
	//Inorder Traversal 中序遍历：左子树->根节点->右子树（仅支持二叉树）
	private void inOrder(Node node){
		if(null!=node.getLeftNode()){
			inOrder(node.getLeftNode());
		}
		inOrderList.add(node);
		if(null!=node.getRightNode()){
			inOrder(node.getRightNode());
		}
	}
	//Postorder Traversal 后序遍历：左子树->右子树->根节点
	private void postOrder(Node node){
		if(null!=node.getLeftNode()){
			postOrder(node.getLeftNode());
		}
		
		if(null!=node.getRightNode()){
			postOrder(node.getRightNode());
		}
		postOrderList.add(node);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] intArray={3,2,4,7,1,5,6,8};
		BinaryTree tree=new BinaryTree();
		for(int i : intArray){
			tree.createBinaryTree(tree.root, i);
		}
		System.out.println(tree.root);
		
		tree.preOrder(tree.root);
		tree.inOrder(tree.root);
		tree.postOrder(tree.root);
		
		for(Node node : tree.preOrderList){
			System.out.print(node.getData()+",");
		}
		System.out.println("--------------------");
		for(Node node : tree.inOrderList){
			System.out.print(node.getData()+",");
		}
		System.out.println("--------------------");
		for(Node node : tree.postOrderList){
			System.out.print(node.getData()+",");
		}
		
	}
	
}
