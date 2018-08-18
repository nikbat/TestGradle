package com.nick.bt;


import java.util.*;

/*
 *                                   100
 *                            50							150
 *               		25			75				125					175
 *               								110
 */


public class BTree2<T extends Comparable<T>> {

	BNode<T> root;
	BNode<T> result;
	int level;

	public BTree2() {
		super();
	}

	public void treeInsert(T t){
		BNode<T> node = new BNode<T>(t);

		if(root == null){
			root = node;
		}else{
			BNode<T> runner = root;
			while(true){
				if(runner.getData().compareTo(node.getData()) >= 0){
					if(runner.left == null){
						runner.left = node;
						node.parent = runner;
						break;
					}else{
						runner = runner.left;
						continue;
					}
				}else{
					if(runner.right == null){
						runner.right = node;
						node.parent = runner;
						break;
					}else{
						runner = runner.right;
						continue;
					}
				}

			}
		}
	}

	/*
		IMPORTANT READ
		1. Inorder Traversal always output sorted output
		2. Find inoreder predecorror https://www.youtube.com/watch?v=rukYFD8cYBY&list=PLeIMaH7i8JDj7DnmO7lll97P1yZjMCpgY
			if the node has left child - go to left and from left keep iterating right child
			if node has no left child, search the node from root and each time you take the right turn, keep that in memory, the last right is our predecossor

		3. Find inoreder successor https://www.youtube.com/watch?v=JdmAYw5h3G8&list=PLeIMaH7i8JDj7DnmO7lll97P1yZjMCpgY&index=2
			if the node has right child -> go to right child and keep traverseing to the left of that right child
			if the node has no right child -> serach that node and keep in memory from where we took the last left

		4. Level Order Traversal of binary Tree
	 */

	public boolean bfs(BNode<T> node, T t){
		if(node == null){
			return false;
		}
		Queue<BNode<T>> q = new ArrayDeque<BNode<T>>();
		q.add(node);

		while(!q.isEmpty()){
			BNode<T> n = q.poll();
			if(n.getData().equals(t)){
				return true;
			}else{
				if(n.left != null) q.add(n.getLeft());
				if(n.right != null) q.add(n.getRight());
			}
		}
		return false;
	}

	public boolean dfs(BNode<T> n, T t){
		if(n == null){
			return false;
		}
		Stack<BNode<T>> s = new Stack<BNode<T>>();
		s.push(n);

		while(!s.isEmpty()){
			BNode<T> temp = s.pop();
			if(temp.data == t){
				return true;
			}else{
				s.push(temp.right);
				s.push(temp.left);
			}
		}
		return false;
	}

	boolean dfsRecursive(BNode<Integer> n, int t){

		if(n == null){
			return false;
		}
		if(n.data == t){
			return true;
		}else if(t > n.data){
			return dfsRecursive(n.right, t);
		}else if(t < n.data){
			return dfsRecursive(n.left, t);
		}
		return false;
	}



	// build tree from postorder post order inorder in order
	private BNode<T> buildTree(List<T> ioList, List<T> poList, int inOrderLower, int inOrderUpper,int postOrderLower, int postOrderUpper){

		if(inOrderLower > inOrderUpper || postOrderLower > postOrderUpper){
			return null;
		}

		if(inOrderLower == inOrderUpper ) return new BNode<T>(ioList.get(inOrderLower));

		if(postOrderLower == postOrderUpper ) return new BNode<T>(poList.get(postOrderLower));

		T t = poList.get(postOrderLower);
		int index = ioList.indexOf(t);

		if(index == -1){
			return null;
		}

		BNode<T> root = new BNode<T>(t);

		root.left = buildTree(ioList, poList, inOrderLower, index-1, postOrderLower+1, index - (inOrderLower+postOrderLower));
		root.right = buildTree(ioList, poList, index+1, inOrderUpper, index - (inOrderLower+postOrderLower), postOrderUpper);

		return null;

	}

	List<T> io = new ArrayList<T>();

	public void inOrderTraversal(BNode<T> node){
		if(node != null){
			inOrderTraversal(node.left);
			io.add(node.getData());
			node.show();
			inOrderTraversal(node.right);
		}
	}

	public void inOrderTraversalTraversal(){ //*

		if (root == null) {
			return;
		}

		//keep the nodes in the path that are waiting to be visited
		Stack<BNode<T>> stack = new Stack<BNode<T>>();
		BNode<T> node = root;

		//first node to be visited will be the left one
		while (node != null) {
			stack.push(node);
			node = node.left;
		}

		// traverse the tree
		while (stack.size() > 0) {

			// visit the top node
			node = stack.pop();
			System.out.print(node.data + " ");
			if (node.right != null) {
				node = node.right;

				// the next node to be visited is the leftmost
				while (node != null) {
					stack.push(node);
					node = node.left;
				}
			}
		}
	}



	List<T> po = new ArrayList<T>();

	public void preOrderTraversal(BNode<T> node){
		if(node == null){
			return;
		}
		po.add(node.getData());
		node.show();
		preOrderTraversal(node.left);
		preOrderTraversal(node.right);

	}

	public void preOrderTraversalReccursive(BNode<T> node){
		//Create a stack whis is LIFO
		Stack<BNode<T>> stack = new Stack<BNode<T>>();
		if(node != null){
			// if node is not null put node in a stack
			stack.push(node);
		}

		// Now look at the preOrder in recursion, first root node, left node and after that right node, that means right node is going in a memory stack, that what we will do with out stack
		while(!stack.isEmpty()){
			BNode<T> temp = stack.pop();
			temp.show();
			if(temp.getRight() != null){
				stack.push(temp.right);
			}

			if(temp.left != null){
				stack.push(temp.left);
			}
		}

	}

	public int findHeight(BNode<T> node){
		if(node == null){
			return 0;
		}
		return 1 + Math.max(findHeight(node.left), findHeight(node.right));
	}


	public boolean treeContains(T t){
		boolean contains = false;
		if(root != null){
			BNode<T> runnerNode = root;

			while(runnerNode != null){
				if(runnerNode.getData().equals(t)){
					contains = true;
					break;
				}else{
					if(runnerNode.getData().compareTo(t) >= 0){
						runnerNode = runnerNode.right;
					}else{
						runnerNode = runnerNode.left;
					}

				}
			}
		}
		return contains;
	}

	private int traverse(BNode<T> node, int count, List<? super BNode<T>> list){
		if(node == null){
			return count;
		}
		if(list != null){
			list.add(node);
		}
		count++;
		count = traverse(node.left, count, list);
		count = traverse(node.right, count, list);
		return count;
	}

	private List<LinkedList<BNode<T>>> getLAtlevel(BNode<T> root){
		List<LinkedList<BNode<T>>> list = new ArrayList<LinkedList<BNode<T>>>();
		if(root == null){
			return list;
		}
		LinkedList<BNode<T>> ll = new LinkedList<BNode<T>>();
		ll.add(root);
		int level = 0;

		while (true){
			ll = list.get(level);
			LinkedList<BNode<T>> t1 = new LinkedList<BNode<T>>();
			for(int i=0; i<ll.size();i++){
				if(ll.get(i).getLeft() != null){
					t1.add(ll.get(i).left);
				}
				if(ll.get(i).right != null){
					t1.add(ll.get(i).right);
				}
			}
			if(!t1.isEmpty()){
				list.add(level++, t1);
			}else{
				break;
			}
		}

		return list;
	}

	private LinkedList<List<BNode<T>>> getLL(BNode<T> node){
		LinkedList<List<BNode<T>>> ll = new LinkedList<List<BNode<T>>>();
		List<BNode<T>> l = new ArrayList<BNode<T>>();

		if(node != null){
			l.add(node);

		}
		while(!l.isEmpty()){
			ll.add(l);
			List<BNode<T>> lt = new ArrayList<BNode<T>>();
			for(BNode<T> n : l){
				if(n.left != null)
					lt.add(n.left);
				if(n.right != null)
					lt.add(n.right);
			}
			l = lt;
		}
		return ll;
	}


	//https://gist.github.com/bittib/5620951
	public void serialize(BNode<T> node){
		StringBuilder sb = new StringBuilder();
		serialize(node,sb);
		System.out.println(sb);

		String[] tokens = sb.toString().split(",");
		BNode<Integer> root2 = deserialize(tokens);
		System.out.println(root2);

	}
	public void serialize(BNode<T> node, StringBuilder sb){
		if(node == null){
			sb.append("#,");
		}else{
			sb.append(node.getData()+",");
			serialize(node.left, sb);
			serialize(node.right, sb);
		}
	}
	int index = 0;
	public BNode<Integer> deserialize(String[] tokens){
		if(index >= tokens.length){
			return null;
		}
		if(tokens[index].equals("#")){
			index++;
			return null;
		}

		BNode<Integer> root2 = new BNode<Integer>();
		root2.setData(Integer.parseInt(tokens[index++]));
		root2.left = deserialize(tokens);
		root2.right = deserialize(tokens);
		return root2;
	}

	private void build(BTree2<T> tree, BNode<T>[] na, int start, int length){

		if(tree == null){
			return;
		}

		if(length < start){
			return;
		}

		int mid = (start+length)/2;
		tree.treeInsert(na[mid].getData());
		build(tree, na, start, mid-1);
		build(tree, na, mid+1, length);
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
		}else if (covers(root.right,n1) && covers(root.right, n2)){
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



	//http://www.youtube.com/watch?v=jSZ4e3cmh2A
	private BNode<T> inOrderSuccessor(BNode<T> node){
		if(node.right != null){
			BNode<T> n = node.right;
			while(n != null){
				n = n.left;
			}
			return n;
		}else{
			BNode<T> p = node.parent;
			while(p != null && node.getData() == p.right.getData()){
				p = p.parent;
				node = p;
			}
			return p;
		}
	}



	private BNode<T> preOrderSucc(BNode<T> node) {
		if (node.left != null)
			return node.left;
		if (node.right != null)
			return node.right;

		// if node is right child of its parent
		// go up till parent becomes left child.
		// If that does not happen, return -1
		if (node == node.parent.right) {
			while ((node.parent != null) && (node == node.parent.right)) {
				node = node.parent;
			}
		}

		if (node == null)
			return null;

		// if we are here, that means node is now left child of its parent
		// if node is left child of its parent
		BNode<T> temp = node.parent;
		while (temp != null) {
			if (temp.right != null)
				return temp.right;
			temp = temp.parent;
		}
		return null;
	}

	static List<Integer> nl = new ArrayList<Integer>();
	public void traverse(BNode<Integer> node){
		if(node != null){
			nl.add(node.getData());
			siftUp();
			traverse(node.getLeft());
			traverse(node.getRight());
		}

	}
	//https://www.youtube.com/watch?v=LhhRbRXhB40
	//p childs are at 2k+1 = 2k+2;
	// p = (k-1)/2
	public void siftUp(){
		if(nl.size() > 1){
			int k = nl.size() - 1;
			int p = (k-1)/2;

			while(p >= 0 ){
				if(nl.get(k) > nl.get(p)){
					int temp = nl.get(k);
					nl.set(k, nl.get(p));
					//nl.remove(k+1);
					nl.set(p, temp);
					//nl.remove(p+1);
					k = p;
					p = (k-1)/2;
				}else{
					break;
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	public void generateLL(HashMap<Integer, LinkedList<T>> lmap,BNode<T> node, int level){
		LinkedList<T> ll =  lmap.get(level);
		if(ll == null){
			ll = new LinkedList<T>();
			ll.add(node.getData());
			lmap.put(level, ll);
		}else{
			ll.add(node.getData());
		}

		if(node.left != null){
			generateLL(lmap, node.getLeft(), level+1);
		}
		if(node.getRight() != null){
			generateLL(lmap, node.getRight(), level+1);
		}
	}

	T nodeData;
	public boolean isBalancedBinaryTree(BNode<T> n){
		//Approach 1 : in order traversal in array should be always sorter.
		//Approach 2 : No need of extra array maintain a number
		//Approach 3 : find the max a min height difference should not be greater than 1
		if(n == null){
			return true;
		}
		if(! isBalancedBinaryTree(n.getLeft()) ){
			return false;
		}
		nodeData = n.getData();

		if(nodeData != null && nodeData.compareTo(n.getData()) < 0){
			return false;
		}

		if(! isBalancedBinaryTree(n.getRight()) ){
			return false;
		}

		return true;
	}

	public BNode<T> inOrderSuccessor1(BNode<T> node){
		if(node.right != null){
			//return leftMost(node.right);
			return null;
		}else{
			BNode<T> n = node;
			BNode<T> p = node.parent;
			while(p != null && p.left != n){
				n = p;
				p = p.parent;
			}
			return p;
		}
	}

	public BNode<T> commonAncestor(BNode<T> node, BNode<T> p, BNode<T> q){
		if(cover(node.left,p) && cover(node.left, q)) {
			return commonAncestor(node.left, p, q);
		}else if(cover(node.right,p) && cover(node.right, q)){
			return commonAncestor(node.right, p, q);
		}else{
			return node;
		}
	}

	public boolean cover(BNode<T> node, BNode<T> n){
		if(node == null){
			return false;
		}
		if(node == n){
			return true;
		}
		return cover(node.left, n) || cover(node.right, n);
	}

	public boolean T1ContainsT2(BNode<T> t1, BNode<T> t2){
		if(t2 == null){
			return false;
		}
		if(t1.data.equals(t2.data)){
			return matchTree(t1, t2);
		}else {
			return T1ContainsT2(t1.left, t2) || T1ContainsT2(t1.right, t2);
		}
	}

	public boolean matchTree1(BNode<T> t1, BNode<T> t2){
		if(t1 == null && t2 == null){
			return true;
		}else if(t1 == null || t2 == null){
			return false;
		}else if(t1.getData().equals(t2.getData())){
			return true;
		}else{
			return matchTree(t1.left, t2.left) && matchTree(t1.right, t2.right);
		}
	}

	List<BNode<Integer>> l = new  ArrayList<>();

	private void binaryTreeToHeap(BNode<Integer> node){
		traverse1(node);
		System.out.println(l);
		l.sort((n1, n2) -> n2.compareTo(n1));
		System.out.println(l);

		for(int i=0; i < l.size(); i++){
			if((2*i+1) < l.size()){
				l.get(i).left = l.get((2*i)+1);
			}else{
				l.get(i).left = null;
			}
			if((2*i+2) < l.size()){
				l.get(i).right = l.get((2*i)+2);
			}else{
				l.get(i).right = null;
			}
		}
		BTree2<Integer> t = new BTree2<Integer>();
		t.root = l.get(0);
	}

	public void traverse1(BNode<Integer> node){
		if(node == null){
			return;
		}
		l.add(node);
		traverse1(node.left);
		traverse1(node.right);

	}



	public BNode<Integer> build(int[] a, int s, int l){
		BNode<Integer> n = null;

		if(s < l){
			int m = (s+l)/2;
			n = new BNode(a[m]);
			n.left = build(a,s, m-1);
			n.right = build(a,m+1, l);
			return n;
		}else{
			return null;
		}
	}

	public void printLeafNode(BNode<T> n){
		if(n == null){
			return;
		}
		if(n.left == null && n.right == null){
			System.out.println(n.data);
		}else{
			printLeafNode(n.left);
			printLeafNode(n.right);
		}

	}

	void printLL(BNode<T> n){
		if(n == null){
			return;
		}

		List<List<BNode<T>>> ll = new ArrayList<>();
		List<BNode<T>> l = new ArrayList<>();
		int level = 0;
		l.add(n);
		while(!l.isEmpty()){
			ll.add(l);
			List<BNode<T>> nodes = ll.get(level);
			l = new ArrayList<>();
			level = level + 1;

			for(int i = 0; i < nodes.size(); i++) {
				BNode<T> c = nodes.get(i);

				if (c.getLeft() != null) {
					l.add(c.getLeft());
				}
				if (c.getRight() != null) {
					l.add(c.getRight());
				}
			}

		}
		for(int i = 0; i < ll.size(); i++){
			l = ll.get(i);
			for(int j = 0; j < l.size(); j++){
				System.out.print(l.get(j));
				System.out.print(",");
			}
			System.out.println();
		}
	}

	//https://www.youtube.com/watch?v=YsLko6sSKh8&index=20&list=PLeIMaH7i8JDj7DnmO7lll97P1yZjMCpgY
	void spiralOrderTraversal(BNode<T> n){
		if(n == null){
			return;
		}

		Stack<BNode<T>> s1 = new Stack<>();
		Stack<BNode<T>> s2 = new Stack<>();
		s1.push(n);

		while(!s1.isEmpty() || !s2.isEmpty()){
			while(!s1.isEmpty()){
				BNode<T> tn = s1.pop();
				System.out.print(tn+ ",");
				if(tn.left != null){
					s2.push(tn.left);
				}
				if(tn.right != null){
					s2.push(tn.right);
				}
			}
			System.out.println();

			while(!s2.isEmpty()){
				BNode<T> tn = s2.pop();

				System.out.print(tn+ ",");

				if(tn.right != null){
					s1.push(tn.right);
				}

				if(tn.left != null){
					s1.push(tn.left);
				}

			}
			System.out.println();
		}

	}

	//https://www.geeksforgeeks.org/diagonal-traversal-of-binary-tree/
	void printDiagonal(BNode<Integer> n, int i,  Map<Integer, List<Integer>> data){
		if(n == null){
			return;
		}
		List<Integer> l = data.get(i);
		if(l == null){
			l = new ArrayList<>();
		}
		l.add(n.data);
		data.put(i,l);


		printDiagonal(n.left, i+1, data);
		printDiagonal(n.right, i, data);
	}

	void printLeaf(BNode<Integer> n){
		if(n == null){
			return;
		}

		if(n.left == null && n.right == null){
			System.out.println(n.data+",");
		}
	}

	//https://www.youtube.com/watch?v=0QOtVxTVj4w
	//build tree from pre order traversal VERY EASY
	//int[] pota = {12,9,5,4,7,10,15,13,19,16};
	BNode<Integer> buildTreeFromPreOrderTraversal(int[] a, int s, int l){
		if(s >= l){
			return null;
		}

		int rootValue = a[s]; // first node is always the root

		int ls = s+1; //start of left, the
		int ll = -1; // left end

		int rs = l;
		int rl = l; // right end

		for(int i=s; i < l;  i++){
			if(a[i] > rootValue){
				rs = i; // start of right node
				ll = i; // left end
				break;
			}
		}
		BNode<Integer> root = new BNode<>(rootValue);
		root.left = buildTreeFromPreOrderTraversal(a, ls, ll);
		root.right = buildTreeFromPreOrderTraversal(a, rs, rl);
		return root;

	}

	//TODO: https://www.geeksforgeeks.org/construct-binary-tree-from-inorder-traversal/




	void findDeepestLeftNode(BNode<T> n, int currentlevel, boolean isLeft){
		if(n == null){
			return;
		}

		if(isLeft && currentlevel > this.level){
			this.result = n;
			this.level = currentlevel;
		}

		findDeepestLeftNode(n.left,  currentlevel+1, true);
		findDeepestLeftNode(n.right, currentlevel+1, false);
	}

	//https://www.geeksforgeeks.org/check-two-nodes-cousins-binary-tree/
	int findLevelOfNode(BNode<Integer> root, int data, int level){
		if(root == null){
			return 0;
		}
		else if(root.data == data){
			return level;
		}else{
			int l = findLevelOfNode(root.left, data, level+1);
			if(l != 0){
				return l;
			}
			return findLevelOfNode(root.right, data, level+1);
		}
	}

	boolean checkBST(BNode<Integer> n){
		if(n == null){
			return true;
		}
		return checkBST(n, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	boolean checkBST(BNode<Integer> n, int min, int max){

		if(n == null){
			return true;
		}

		if(n.data < min || n.data > max ){
			return false;
		}
		return checkBST(n.left, min, n.data) && checkBST(n.right, n.data, max);

	}

	void leftView(BNode<T> root) {
		leftViewUtil(root,1);
	}

	int max_level = 0;
	void leftViewUtil(BNode<T> root, int level) {
		if (root == null) {
			return;
		}

		if(max_level < level) {
			System.out.print(root + " ");
			max_level = level;
		}

		leftViewUtil(root.left, level+1);
		leftViewUtil(root.right, level+1);
	}

	//https://www.youtube.com/watch?v=NjdOhYKjFrU
	//This can be done recursively also
	// Make a queue and insert a root node and null (-1), while dequeuq if null node appears and queue is NOT-empty put null again
	static void levelOrderTraversal(BNode<Integer> n){
		if(n == null){
			return;
		}
		Queue<BNode<Integer>> q = new ArrayDeque<>();
		q.add(n);
		q.add(new BNode<Integer>(-1));
		while(!q.isEmpty()){
			BNode<Integer> t = q.poll();
			if(t != null && t.data != -1){
				System.out.print(t.data+",");
				if(t.left != null){
					q.add(t.left);
				}

				if(t.right != null){
					q.add(t.right);
				}
			}else if(t.data == -1){
				System.out.println("");
				if(!q.isEmpty()) {
					q.add(new BNode<Integer>(-1));
				}
			}
		}
	}

	public void levelOrderTraversalGeneric(BNode<T> root){
		if(root == null) return;

		Queue<BNode<T>> q = new LinkedList<>();
		q.add(root);
		//insert null, null value tells us if the level changed
		q.add(null);

		while(!q.isEmpty()){
			BNode<T> n = q.poll();
			if(n != null){
				System.out.print(n+",");

				if(n.left != null) {
					q.add(n.left);
				}
				if(n.right != null) {
					q.add(n.right);
				}
			}else{
				if(!q.isEmpty()){
					System.out.println();
					q.add(null);
				}
			}
		}
	}


	//vertical order Traversal
	//https://www.youtube.com/watch?v=PQKkr036wRc
	// Create a HashMap<Integer, LinkedList> for root put i=0 for left i-1 and for right i+1
	HashMap<Integer,LinkedList<BNode<T>>> horizontalDistanceMap =  new HashMap<>();
	void verticalOrderTraversal(BNode<T> node){
		if(node == null){
			return;
		}

		Queue<BNode<T>> q = new LinkedList<>();
		node.hd = 0; // set the root to zero
		q.add(node);
		while(!q.isEmpty()) {

			BNode<T> n = q.poll();
			LinkedList<BNode<T>> nodesAtHorizontalDistance = horizontalDistanceMap.get(n.hd);
			if (nodesAtHorizontalDistance == null) {
				nodesAtHorizontalDistance = new LinkedList<>(); //Note: ints important to have Linked List for the boot view
			}
			nodesAtHorizontalDistance.add(n);
			horizontalDistanceMap.put(n.hd, nodesAtHorizontalDistance);

			if(n.left != null) {
				n.left.hd = n.hd - 1;
				q.add(n.left);
			}
			if(n.right != null) {
				n.right.hd = n.hd + 1;
				q.add(n.right);
			}

			//verticalOrderTraversal(n.left, horizontalDistance - 1);
			//verticalOrderTraversal(n.right, horizontalDistance + 1);
		}
	}

	//see iterative approach above
	public void verticalOrderTraversalRecrusive(BNode<T> n, int i,  HashMap<Integer,LinkedList<BNode<T>>> hdMap){
		if(n == null) return;

		LinkedList<BNode<T>> list = hdMap.get(i);

		if(list == null){
			list = new LinkedList<>();
			list.add(n);
			hdMap.put(i, list);
		}else {
			list.add(n);
		}

		verticalOrderTraversalRecrusive(n.left, i-1, hdMap );
		verticalOrderTraversalRecrusive(n.right, i+1, hdMap);

	}



	//Bootom View of a Binary Tree
	//https://www.youtube.com/watch?v=V7alrvgS5AI
	void bottomViewOfBinaryTree(BNode<T> n){
		horizontalDistanceMap =  new HashMap<>();
		verticalOrderTraversal(n);
		System.out.println();
		for(int k : horizontalDistanceMap.keySet()){
			LinkedList<BNode<T>> nodesAtHorizontalDistance  = horizontalDistanceMap.get(k);
			System.out.print(nodesAtHorizontalDistance.getLast()+",");
		}
	}

	//left view of a Binary Tree using level Order Traversal
	//https://www.youtube.com/watch?v=eBdKNoW3VJg
	void leftView1(BNode<T> n){
		//do a level order traversal here
	}

	//Diameter for the binary tree
	//https://www.youtube.com/watch?v=ey7DYc9OANo
	int findDiameter(BNode<T> n){
		if(n == null){
			return 0;
		}

		int leftHeight = findHeight(n.left);
		int rightHeight = findHeight(n.right);

		int leftDiameter = findDiameter(n.left);
		int rightDiameter = findDiameter(n.right);

		//return Math.max(leftHeight+rightHeight+1, Math.max(findDiameter(n.left), findDiameter(n.right)) );
		return Math.max(leftHeight+rightHeight+1, Math.max(leftDiameter, rightDiameter));

	}

	int findMaxHeight(BNode<T> n){
		if(n == null){
			return 0;
		}
		return 1+Math.max(findMaxHeight(n.left), findMaxHeight(n.right));
	}

	//https://www.youtube.com/watch?v=aYwiLCCdb-k&index=19&list=PLeIMaH7i8JDj7DnmO7lll97P1yZjMCpgY
	//do inorder traversal
	int sum = 0;
	Stack<BNode<Integer>> s = new Stack<>();
	public void findSumPath(BNode<Integer> n, int k){

		if(n == null){
			return;
		}

		sum = sum + n.data;
		s.push(n);
		if(sum == k){
			s.stream().forEach(tn -> System.out.print(tn.data +","));
		}
		findSumPath(n.left, k);
		findSumPath(n.right, k);

		sum = sum - s.pop().data;
	}

	//https://www.youtube.com/watch?v=zIkDfgFAg60&index=30&list=PLeIMaH7i8JDj7DnmO7lll97P1yZjMCpgY
	// do inorder traversal, put in stack, instead of printing node - print full stack when left and right are null, after right pop the last element from stack
	Stack<BNode<Integer>> rlpath = new Stack<>();
	public void printRootToLeaf(BNode<Integer> n){

		if(n == null) return;

		rlpath.add(n);
		printRootToLeaf(n.left);

		//print stack if both left and right are null
		if(n.left == null && n.right == null) {
			rlpath.stream().forEach(tn -> {
				System.out.print(tn.data + ",");
			});
			System.out.println();
		}

		printRootToLeaf(n.right);

		//pop element
		rlpath.pop();

	}

	//https://www.youtube.com/watch?v=zEIWqb8nWDk&index=32&list=PLeIMaH7i8JDj7DnmO7lll97P1yZjMCpgY
	public boolean isSumTree(BNode<Integer> n){
		if(n == null) return false;
		if(n.data == sum(n.left) + sum(n.right)){
			return true;
		}
		return false;
	}

	public int sum(BNode<Integer> n){
		if(n == null) return 0;
		return n.data + sum(n.left) + sum(n.right);
	}


	//TODO:
	//https://www.youtube.com/watch?v=I3BC8nEKYm8
	static void diogonalTraversalOfBinaryTree(){

	}

	//https://www.youtube.com/watch?v=u2O11mengx8&index=21&list=PLeIMaH7i8JDj7DnmO7lll97P1yZjMCpgY
	//find nodes with k having k leaves
	//do a post order traversal left, right, node,
	public int allNodedsHavingKLeaves(BNode<T> n, int k){
		if(n == null) {
			return 0;
		}
		if(n.left == null && n.right == null){
			return  1;
		}
		int leftCount = allNodedsHavingKLeaves(n.left, k);
		int rightCount = allNodedsHavingKLeaves(n.right, k);

		if(k == (leftCount + rightCount)){
			System.out.print(n + ",");
		}

		return leftCount+rightCount;

	}

	int findTotalNumberOfNodes(BNode<T> n){
		if(n == null){
			return 0;
		}
		else if(n.left == null && n.right == null){
			return 1;
		}else{
			return 1 + findTotalNumberOfNodes(n.left) + findTotalNumberOfNodes(n.right);
		}

	}

	public static void main(String[] args){

		System.out.println(21/10);
		System.out.println(5/10);
		System.out.println(21%10);
		System.out.println(5%10);

		BTree2<Integer> tree = new BTree2<Integer>();
		tree.treeInsert(100);
		tree.treeInsert(50);
		tree.treeInsert(150);
		tree.treeInsert(25);
		tree.treeInsert(75);
		tree.treeInsert(125);
		tree.treeInsert(175);
		tree.treeInsert(110);

		tree.serialize(tree.root);


		/*tree.treeInsert(500);
		tree.treeInsert(700);
		tree.treeInsert(45);
		tree.treeInsert(30);
		tree.treeInsert(40);*/

		BTree2<Integer> tree1 = new BTree2<Integer>();
		tree1.treeInsert(100);
		tree1.treeInsert(50);
		tree1.treeInsert(150);
		tree1.treeInsert(25);
		tree1.treeInsert(75);
		tree1.treeInsert(125);
		tree1.treeInsert(175);
		tree1.treeInsert(110);
		tree1.treeInsert(500);
		tree1.treeInsert(700);
		tree1.treeInsert(45);
		tree1.treeInsert(30);
		tree1.treeInsert(40);

		tree1.allNodedsHavingKLeaves(tree1.root, 2);

		tree1.findSumPath(tree1.root, 425);
		tree1.findSumPath(tree1.root, 485);

		tree1.printRootToLeaf(tree1.root);


		tree1.verticalOrderTraversal(tree1.root);
		HashMap<Integer,LinkedList<BNode<Integer>>> horizontalDistanceMap1 =  new HashMap<>();
		tree1.verticalOrderTraversalRecrusive(tree1.root, 0, horizontalDistanceMap1);

		tree1.spiralOrderTraversal(tree1.root);

		tree1.levelOrderTraversal(tree1.root);
		tree1.levelOrderTraversalGeneric(tree1.root);

		System.out.println(tree1.dfsRecursive(tree1.root, 700));

		tree1.leftView(tree1.root);
		tree1.leftView1(tree1.root);
		tree1.bottomViewOfBinaryTree(tree1.root);


		System.out.println(tree1.findLevelOfNode(tree1.root, 500, 1));
		//tree1.findDeepestLeftNode(tree1.root, 0,  false);
		//System.out.println(tree1.result);

		/*Map<Integer, List<Integer>> data = new HashMap<>();
		tree.printDiagonal(tree.root, 0, data);
		for(int i : data.keySet()){
			List<Integer> l = data.get(i);
			for(int j : l){
				System.out.print(j+",");
			}
			System.out.println();
		}
		System.out.println();*/


		int[] pota = {12,9,5,4,7,10,15,13,19,16};
		BNode<Integer> node = tree.buildTreeFromPreOrderTraversal(pota, 0, pota.length);
		BTree2<Integer> tree5 = new BTree2<>();
		tree5.root = node;

		tree.inOrderTraversal(tree1.root);
		System.out.println();

		tree.preOrderTraversal(tree5.root);
		System.out.println();

		tree.inOrderTraversal(tree.root);
		System.out.println();
		tree.preOrderTraversal(tree.root);

		//tree.serialize(tree.root);

		//This method spoils the tree be carefull
		//tree.binaryTreeToHeap(tree.root);
		//System.out.println("#"+  tree.bfs1(tree.root, 110));
		//tree.printLL(tree.root);


		/*
		 * Generate LinkedList
		HashMap<Integer, LinkedList<Integer>> lmap = new HashMap<Integer, LinkedList<Integer>>();
		tree.generateLL(lmap, tree.root, 0);
		for(int i : lmap.keySet()){
			System.out.print(i + "-> ");
			for(int k : lmap.get(i)){
				System.out.print(k + " ");
			}
			System.out.println();
		}
		*/

		//check BST
		//System.out.println(tree.isBalancedBinaryTree(tree.root));
		//System.out.println(1/26);
		//System.out.println(tree.bfs1(tree.root, 75));


		//tree.traverse(tree.root);
		//System.out.println(BTree2.nl);

		//tree.inOrderTraversal(tree.root);
		//System.out.println(tree.io);
		//tree.io.clear();

		//tree.inOrderTraversalTraversal();
		//System.out.println(tree.io);


		//tree.preOrderTraversal(tree.root);
		//System.out.println(tree.po);


		/*tree.preOrderTraversal(tree.root);
		System.out.println();
		tree.inOrderTraversal(tree.root);
		System.out.println("-----");
		tree.preOrderTraversalReccursive(tree.root);

		tree.treeContains(75);
		//BNode<Integer> n = tree.inOrderSuccessor(75);
		//n.show();
		*/


		//tree.getLL(tree.root);
		//tree.serialize(tree.root);

		//System.out.println("tree Height is :"+tree.findHeight(tree.root));
		/*System.out.println("tree contains data 100 :"+tree.treeContains(100));
		System.out.println("tree contains data 110 :"+tree.treeContains(110));
		System.out.println("tree contains data 175 :"+tree.treeContains(175));
		System.out.println("tree contains data 199 :"+tree.treeContains(199));

		System.out.println("mca :"+tree.mostCommonAncestor(125, 110));

		//BNode[] aBNode = new BNode[tree.traverse(tree.root, 0, null)];

		*/

		/*List<BNode<Integer>> nodeList = new ArrayList<BNode<Integer>>();
		tree.traverse(tree.root, 0, nodeList);
		Collections.sort(nodeList,new MyBTreeNodeComparator());
		BNode[] na = new BNode[nodeList.size()];
		nodeList.toArray(na);
		System.out.println(ArrayUtils.toString(na));

		BTree2<Integer> t2 = new BTree2<Integer>();
		tree.build(t2, na, 0, na.length-1);

		t2.preOrderTraversal(t2.root);*/


		/*
		//System.out.println("Nodes :"+);

		/*System.out.println(ArrayUtils.toString(aBNode));
		Arrays.sort(aBNode, new MyBTreeNodeComparator());

		System.out.println(ArrayUtils.toString(aBNode));*/

		int[] a = {100,50,150,25,75,125,175,110};
		Arrays.sort(a);

		//BNode<Integer> n1 = tree.build(a,0,a.length);

		//StringBuilder s = new StringBuilder();
		//tree.serialize(n1, s);
		//System.out.println(s);


	}

	class Test{
		int someData = 1;
		private void someMethod(){
			System.out.println(root);
		}
	}

	static class Test2{
		private void someMethod(){
			// root cannot be accedd from here
		}
	}


}


class BNode<T extends Comparable<? super T> > implements Comparable<BNode<? extends T>>{
	T data;
	BNode<T> left;
	BNode<T> right;
	BNode<T> parent;
	int hd;

	public BNode() {

	}

	public BNode(T data) {
		super();
		this.data = data;
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
		return data.toString();
	}

	public void show(){
		System.out.print(data);
		System.out.print(",");

	}

	@Override
	public int compareTo(BNode<? extends T> o) {
		// TODO Auto-generated method stub
		return data.compareTo(o.getData());
	}
}

class MyBTreeNodeComparator<T extends Comparable<T>> implements Comparator<T>{

//class MyBTreeNodeComparator<T extends Comparable<T>> implements Comparator<T>{

	@Override
	public int compare(T t1, T t2) {
		return t1.compareTo(t2);
	}
}
