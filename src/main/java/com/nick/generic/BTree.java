package com.nick.generic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/*
 *                                           100
 *                            50							150
 *               		25			75				125					175
 *               								110
 */

public class BTree<T extends Comparable<T>> {
	
	BNode<T> root;
	
	public BTree(){
		root = null;
	}
	
	public void treeInsert(T t){
		BNode<T> temp = new BNode<T>(t);
		if(root ==null){
			root = temp;
			return;
		}
		
		BNode<T> runner;
		runner = root;
		while(true){
			if(t.compareTo(runner.data) < 0 ){
				if(runner.left == null){
					runner.left = temp;
					break;
				}else{
					runner = runner.left;
					continue;
				}
			}else{
				if(runner.right == null){
					runner.right = temp;
					break;
				}else{
					runner = runner.right;
				}
			}
		}		
	}
	
	public int findHeight(BNode<T> node){
		if(node == null)
			return 0;
		return 1 + Math.max(findHeight(node.left), findHeight(node.right));
	}
	
	public void inOrderPrint(BNode<T> node){
		if(node != null){
			inOrderPrint(node.left);
			node.show();
			inOrderPrint(node.right);
		}
	}
	
	public void preOrderPrint(BNode<T> node){
		if(node != null){
			node.show();
			inOrderPrint(node.left);			
			inOrderPrint(node.right);
		}
	}
	
	public void postOrderPrint(BNode<T> node){
		if(node != null){
			inOrderPrint(node.left);			
			inOrderPrint(node.right);
			node.show();
		}
	}
	
	public boolean treeContains(BNode<T> node, T t){
		
		if(node == null){
			return false;
		}else if(node.getData().equals(t)){
			return true;
		}else if(t.compareTo(node.getData()) < 0 ) {
			return treeContains(node.left, t);
		}else{
			return treeContains(node.right, t);
		}		
	}
	
	//For most common ancestor start with root and keep going either left or right 
	public BNode<T> findCommonAncestor(BNode<T> root, T first, T second){
		
		while(root != null){
			
			T t = root.getData();
			if(first.compareTo(t) < 0 && second.compareTo(t) < 0 ){
				root = root.getLeft();
			}else if(first.compareTo(t) > 0 && second.compareTo(t) > 0 ){
				root = root.getRight();
			}else{
				return root;
			}
		}
		return null;
		
	}
	
	//build balanced binary tree from a sorted array 
	
	public void build(BTree<Integer> t,int[] data,int start, int end){
		
		if(end < start){
			return;
		}
		int mid = (start+end)/2;		
		t.treeInsert(data[mid]);
		build(t, data, start, mid-1);
		build(t, data, mid+1, end);
	}
	
	public void getLLAtLevel(BNode<T> root){
		
		List<LinkedList<BNode<T>>> list = new ArrayList<LinkedList<BNode<T>>>();
		LinkedList<BNode<T>> ll = new LinkedList<BNode<T>>();
		ll.add(root);
		list.add(0,ll);
		int level = 0;
		
		while(true){
			ll = new LinkedList<BNode<T>>();
			for(int i=0;i<list.get(level).size(); i++){
				BNode<T> n = list.get(level).get(i);
				if(n.left != null){
					ll.add(n);
				}
				if(n.right != null){
					ll.add(n);
				}
			}
			
			if(ll.size() > 0){
				list.add(level++,ll);
			}else{
				break;
			}
		}
		
		
	}
	
	// Do this again when possible
	public void heapifyBinary(BNode<T> node){
		int size = traverse( root, 0, null );
		BNode<T>[] nodeArray = (BNode<T>[])new BNode[size];
		traverse( root, 0, nodeArray );
		//Arrays.sort(nodeArray);
		
		for( int i = 0; i < size; i++ ){
			int left = 2*i + 1;
			int right = left + 1;
			nodeArray[i].setLeft( left >= size ? null : nodeArray[left] );
			nodeArray[i].setRight( right >= size ? null : nodeArray[right] );
		}
		System.out.println(nodeArray[0]);
			
	}
	
	public int traverse( BNode<T> node, int count, BNode<T>[] arr ){
		if( node == null )
			return count;
		
		if( arr != null )
			arr[count] = node;
		
		count++;
		count = traverse( node.getLeft(), count, arr );
		count = traverse( node.getRight(), count, arr );
		return count;
	}
	
	
	
	public boolean mirrorImage(BNode<T> n1, BNode<T> n2){		
		if(n1 == null && n2 == null){
			return true;
		}else if(n1 == null || n2 == null){
			return false;
		}else if(n1.equals(n2)){
			return (mirrorImage(n1.left, n2.right) && mirrorImage(n1.right, n2.left));
		}else{
			return false;
		}
	}
	
	//2 large binary trees check if T1 is a subtree of T2
	public boolean subTree(BNode<T> t1, BNode<T> t2){
		if(t2 == null){
			//sub tree is null which is always a subtree of t1
			return true;
		}else if(t1.equals(t2)){
			matchTree(t1, t2);
		}else{
			return (subTree(t1.left, t2) || subTree(t1.right, t2));
		}
		
		return false;
	}
	
	public boolean matchTree(BNode<T> n1, BNode<T> n2){
		if(n1 == null && n2 == null){
			return true;
		}else if(n1 == null || n2 == null){
			return false;
		}else if(n1.equals(n2)){
			return (matchTree(n1.left, n2.left) && matchTree(n1.right, n2.right));
		}
		return false;
	}
	
	private BNode<T> mostCommonAncestorOfBinaryTreeNotBST(BNode<T> root, BNode<T> n1, BNode<T> n2){
		if(covers(root.left, n1) && covers(root.left, n2)){
			mostCommonAncestorOfBinaryTreeNotBST(root.left, n1, n2);			
		}else if (covers(root.left,n1) && covers(root.right, n2)){
			mostCommonAncestorOfBinaryTreeNotBST(root.right, n1, n2);
		}
		return root;
	}
	
	private boolean covers(BNode<T> root, BNode<T> n){
		if(root == null){
			return false;
		}
		if(root == n){
			return true;
		}
		
		return covers(root.left, n) || covers(root.right, n);
		
	}
	
	
	public static void main(String[] args){
		System.out.println(90007517%25);		
		BTree<Integer> tree = new BTree<Integer>();
		tree.treeInsert(100);
		tree.treeInsert(50);
		tree.treeInsert(150);
		tree.treeInsert(25);
		tree.treeInsert(75);
		tree.treeInsert(125);
		tree.treeInsert(175);
		tree.treeInsert(110);
		
		System.out.println(tree.findHeight(tree.root));
		System.out.println(tree.treeContains(tree.root, 111));
		System.out.println(tree.findCommonAncestor(tree.root, 110, 175).getData());
		System.out.println("InOrder");
		tree.inOrderPrint(tree.root);
		System.out.println("PreOrder");
		tree.preOrderPrint(tree.root);
		
		int[] data = {1,2,3,4,5,6,7,8,9,10};
		BTree<Integer> t2 = new BTree<Integer>();
		new BTree<Integer>().build(t2, data, 0, data.length-1);
		System.out.println("PreOrder");
		tree.preOrderPrint(t2.root);
		
		
		
		
		BTree<Integer> t1 = new BTree<Integer>();
		t1.treeInsert(1);
		t1.treeInsert(2);
		t1.treeInsert(3);
		t1.treeInsert(4);
		t1.treeInsert(5);
		t1.treeInsert(6);
		t1.treeInsert(7);
		t1.treeInsert(8);
		t1.treeInsert(9);
		t1.heapifyBinary(t1.root);
		
		
	}

}

class BNode2<T extends Comparable<T> > {
	List<? extends Number> test = new ArrayList<Number>();
	
	public void get(){
		Number n = test.get(1);
	}
	
}

class BNode<T extends Comparable<T> > {
	T data;
	BNode<T> left;
	BNode<T> right;
	
	public BNode(T data) {
		super();
		this.data = data;
	}
	
	public BNode(T data, BNode<T> left, BNode<T> right) {
		super();
		this.data = data;
		this.left = left;
		this.right = right;
	}
	
	public void show(){
		System.out.println(data);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public BNode<T> getLeft() {
		return left;
	}

	public void setLeft(BNode<T> left) {
		this.left = left;
	}

	public BNode<T> getRight() {
		return right;
	}

	public void setRight(BNode<T> right) {
		this.right = right;
	}

	@Override
	public String toString() {
		return "" + data;
	}
}
