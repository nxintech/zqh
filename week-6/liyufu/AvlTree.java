package com.nxin.zqh.weeksix;
public class AvlTree {
	public Node root;
	public Node createAvlTree(Node node,int data){
		if(null==node){
			node=new Node();
			node.setData(data);
		}else{
			if(data<node.getData()){//data<parent.data , 置为左节点
				node.setLeftNode(createAvlTree(node.getLeftNode(),data));
				if(node.getLeftNodeHeight()-node.getRigthNodeHeight()>=2){//失衡，左右节点>1
					if(data<node.getLeftNode().getData()){//如果比左子树小，则是LL型
						node=rightRotate(node);
					}else{//LR型，一次左旋、一次右旋
						node.setLeftNode(leftRotate(node.getLeftNode()));
						node=rightRotate(node);
					}
				}
			}else{//>父节点，放置右节点
				node.setRightNode(createAvlTree(node.getRightNode(), data));
				if(node.getRigthNodeHeight()-node.getLeftNodeHeight()>=2){//失衡,操作分别为RR型和RL型，刚好与LL型和LR型相反
					if(data>node.getRightNode().getData()){
						node=leftRotate(node);
					}else{
						node.setRightNode(rightRotate(node.getRightNode()));
						node=leftRotate(node);

					}
				}
			}
		}
		node.setHeight(getHeight(node));
		return node;
	}
	//左旋
	private Node leftRotate(Node node) {
		//右子节点提出来
		Node returnNode=node.getRightNode();
		//右节点左子树挂到根节点右树
		node.setRightNode(returnNode.getLeftNode());
		//根节点挂到右树
		returnNode.setLeftNode(node);
		node.setHeight(getHeight(node));
		returnNode.setHeight(getHeight(returnNode));
		return returnNode;
	}
	//右旋
	private Node rightRotate(Node node) {
		//左子节点提出来
		Node returnNode=node.getLeftNode();
		//左节点右子树挂到根节点左树
		node.setLeftNode(returnNode.getRightNode());
		//根节点挂到右树
		returnNode.setRightNode(node);
		node.setHeight(getHeight(node));
		returnNode.setHeight(getHeight(returnNode));
		return returnNode;
	}
	//根据生成的树更新高度
	private int getHeight(Node node){
		int leftHeight=null==node.getLeftNode()?-1:node.getLeftNode().getHeight();
		int rightHeight=null==node.getRightNode()?-1:node.getRightNode().getHeight();
		return leftHeight>rightHeight?leftHeight+1:rightHeight+1;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*int [] intArray={5,4,3,2,1};*/

		int [] intArray={8,4,9,2,3};
		AvlTree tree=new AvlTree();
		for(int i : intArray){
			tree.root=tree.createAvlTree(tree.root,i);
			System.out.println(tree.root);
		}
	}
}
