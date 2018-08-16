package com.nick.ll2;

public class MyLinkedList2<T> {
	
	private L2Node<T> root;
	private L2Node<T> last;
	int size;
	
	public MyLinkedList2(){
		
	}
	
	public void add(T t) {
		
		L2Node<T> temp = new L2Node<T>(t);
		
		if(root == null){
			root = last = temp;
		}else{			
			last.setNext(temp);
			last = temp;
		}		
		size++;
	}
	
	public void addFront(T t){
		L2Node<T> temp = new L2Node<T>(t);
		temp.setNext(root);
		root = temp;
		
	}
	
	public String print(){
		L2Node<T> temp = root;
		StringBuilder sb = new StringBuilder();		
		while(temp != null ){
			sb.append(temp.getData().toString() + "->");
			temp = temp.getNext();
		}
		return sb.toString();
	}

	public String print(L2Node<T> n){
		StringBuilder sb = new StringBuilder();
		while(n != null ){
			sb.append(n.getData().toString() + "->");
			n = n.getNext();
		}
		System.out.println(sb);
		return sb.toString();
	}

	
	
	public boolean isCyclic(){
		
		L2Node<T> temp1 = root;
		L2Node<T> temp2 = root;
		
		while(temp2.getNext() != null){
			temp1 = temp1.getNext(); 
			temp2 = temp2.getNext().getNext();
			
			if(temp2 == null){
				return false; //there is no cyclic if we reach to end of 
			}
			
			if(temp1.getData().toString().equals(temp2.getData().toString())){
				break; //we have a cyclic Linked List
			}
		}
		
		//Now make temp 1 as root
		temp1 = root;
		
		while (! temp1.getData().toString().equals(temp2.getData().toString())){
			temp1 = temp1.getNext();
			temp2 = temp2.getNext();
		}
		
		System.out.println("cyclic node is :"+temp1.getData().toString() );
		
		return false;
	}

	
	public void partitionList(MyLinkedList2<Integer> ll,  int i){
		L2Node<Integer> left = null;
		L2Node<Integer> right = null;
		L2Node<Integer> leftStart = null;
		L2Node<Integer> rightStart = null;
		
		L2Node<Integer> current = ll.root;
		
		while(current != null ){
			if(current.getData() < i){
				if(left == null){
					left = new L2Node<>(current.getData());
					leftStart = left;					
				}else{
					L2Node<Integer> temp = new L2Node(current.getData());
					left.setNext(temp);
					left = temp;					
				}
			}else{
				if(right == null){
					right = new L2Node<>(current.getData());
					rightStart = right;
				}else{
					L2Node<Integer> temp = new L2Node(current.getData());
					right.setNext(temp);
					right = temp;
				}
			}
			
			current = current.getNext();
		}
		
		if(leftStart != null){
			left.setNext(rightStart);
		}
		
		MyLinkedList2<Integer> l = new MyLinkedList2<>();
		l.root = leftStart;
		System.out.println(l.print());
	}

	// sum two linked list
	L2Node<Integer> sum(L2Node<Integer> r1, L2Node<Integer> r2, int carry){

		if(r1 == null && r2 == null){
			return null;
		}

		int data = carry;
		if(r1 != null){
			data = data + r1.data;
		}

		if(r2 != null){
			data = data + r2.data;
		}

		L2Node<Integer> node = new L2Node<>((data) % 10);
		node.next = sum(r1 != null ? r1.next : null, r2 != null ? r2.next : null,  (data +carry) / 10);
		return node;
	}
	
	public String printList(L2Node<String> n){
		if(n == null){
			return "";
		}else{
			return printList(n.getNext()) + n.getData();
		}
	}


	//https://www.youtube.com/watch?v=4mm39dVLlZ0&index=6&list=PLeIMaH7i8JDio7glJoO1rQIAo4g1msRRG
	//https://practice.geeksforgeeks.org/problems/reverse-a-linked-list/1
	static L2Node<Integer> reverse(L2Node<Integer> root){
		//10-20-30-40-50-60 ==========> 60-50-40-30-20-10
		// Here we will change the pointers only, initialize three Nodes

		L2Node<Integer> current = root;
		L2Node<Integer> prev = null;
		L2Node<Integer> next = null;

		while(current != null){
			next = current.next; //next = 20 or next = 30
			current.next = prev;
			prev = current; // prev = null so 10 -> null, next iteration 20 -> 10, next interation 30 -> 20, next iteration 40->30
			current = next;
		}

		return prev;
	}


	//https://practice.geeksforgeeks.org/problems/rotate-a-linked-list/1
	L2Node<T> rotate(L2Node<T> root, int k){

		//10-20-30-40-50-60 ======> k=4 ======> 50-60-10-20-30-40

		L2Node<T> current = root;
		L2Node<T> prev = null;
		//Traverse the list k time
		int i = 0;
		while(i < k && current != null){
			prev = current;
			current = current.next;
			i++;
		}
		if(current == null){
			return root; // List size is less than K, threfore cannot be rotated
		}

		//This will be out new root i.e. 50
		L2Node<T> newRoot = current;
		//set prev.next to null because k will be the end of the LL i.e. 40-null
		prev.next = null;


		//run a second loop to reach the end
		while(current != null){
			prev = current;
			current = current.next;
		}

		// set prev next i.e. 60 to root i.e. 10
		prev.next = root;

		return newRoot;

	}

	//https://practice.geeksforgeeks.org/problems/merge-two-sorted-linked-lists/1
	// This is like a merge sort
	public static L2Node<Integer> mergeTwoSortedLinkedList(L2Node<Integer> r1, L2Node<Integer> r2){

		L2Node<Integer> root = null;
		L2Node<Integer> current = null;

		if(r1 == null && r2 == null){
			return null;
		}

		if(r1 == null ){
			return r2;
		}

		if(r2 == null ){
			return r1;
		}

		if(r1.data < r2.data){
			root = r1;
			current = root;
			r1 = r1.next;
		}else{
			root = r2;
			current = root;
			r2 = r2.next;
		}

		while(r1 != null && r2 != null){
			if(r1.data < r2.data){
				current.next = r1;
				current = current.next;
				r1 = r1.next;
			}else{
				current.next = r2;
				current = current.next;
				r2 = r2.next;
			}
		}

		while(r1 != null){
			current.next = r1;
			current = current.next;;
			r1 = r1.next;
		}

		while(r2 != null){
			current.next = r2;
			current = current.next;
			r2 = r2.next;
		}

		return root;
	}

	//https://practice.geeksforgeeks.org/problems/intersection-point-in-y-shapped-linked-lists/1
	//https://www.youtube.com/watch?v=_7byKXAhxyM&index=16&list=PLeIMaH7i8JDio7glJoO1rQIAo4g1msRRG
	static L2Node<Integer> findIntersection(L2Node<Integer> r1, L2Node<Integer> r2){
		if(r1 == null || r2 == null){
			return null;
		}

		L2Node<Integer> n1 = r1;
		L2Node<Integer> n2 = r2;

		//find the length of node1 and node 2 i.e. n1 and n2 and differnce
		int l1 = 0;
		int l2 = 0;
		int d = 0;


		while(n1 != null){
			n1 = n1.next;
			l1++;
		}

		while(n2 != null){
			n2 = n2.next;
			l2++;
		}

		//find the different in lenght
		if(l1 > l2){
			d = l1 - l2;
		}else{
			d = l2 - l1;
		}

		//reset n1 and n2
		n1 = r1;
		n2 = r2;

		//move d nodes in the longer linked list
		if(l1 > l2){
			for(int i = 0; i < d; i++){
				n1 = n1.next;
			}
		}else{
			for(int i = 0; i < d; i++){
				n2 = n2.next;
			}
		}
		//now iterate through both list and compare n1 and n2
		while(n1 != null && n2 != null){
			if(n1.data == n2.data){
				return n1;
			}
			n1 = n1.next;
			n2 = n2.next;
		}

		return null;
	}

	//https://practice.geeksforgeeks.org/problems/pairwise-swap-elements-of-a-linked-list-by-swapping-data/1
	static void swapList(L2Node<Integer> root){
		L2Node<Integer> current = root;
		while(current != null && current.next != null){
			Integer i1 = current.data;
			Integer i2 = current.next.data;
			current.data = i2;
			current.next.data = i1;

			current = current.next;
			if(current != null){
				current = current.next;
			}else{
				break;
			}
		}
	}

	//https://www.youtube.com/watch?v=9F55R1VJRZ4&index=19&list=PLeIMaH7i8JDio7glJoO1rQIAo4g1msRRG
	//https://practice.geeksforgeeks.org/problems/reverse-a-linked-list-in-groups-of-given-size/1

	public static void reverseListInGroups(L2Node<Integer> root, int k){

	}



	public static void main(String[] args){
		MyLinkedList2<String> ll = new MyLinkedList2<String>();
		ll.add("A");
		ll.add("B");
		ll.add("C");
		ll.add("D");
		ll.add("E");
		//ll.add("B");
		ll.add("F");
		ll.add("G");
		//ll.add("H");
		ll.add("A");
		ll.add("I");
		ll.add("J");
		ll.add("K");
		ll.add("D");
		
		System.out.println(ll.printList(ll.root));
		
		System.out.println(ll.print());
		ll.isCyclic();
		
		MyLinkedList2<Integer> l1 = new MyLinkedList2<Integer>();
		l1.add(3);
		l1.add(5);
		l1.add(8);
		l1.add(5);
		l1.add(10);
		l1.add(2);
		l1.add(1);

		l1.partitionList(l1, 5);

		//Sum
		MyLinkedList2<Integer> la = new MyLinkedList2<>();
		la.add(7);
		la.add(1);
		la.add(6);
		MyLinkedList2<Integer> lb = new MyLinkedList2<>();
		lb.add(5);
		lb.add(9);
		lb.add(2);

		L2Node<Integer> n1 = l1.sum(la.root, lb.root, 0);
		System.out.println(la.print(n1));

		//merge two sorted linked list
		MyLinkedList2<Integer> lm1 = new MyLinkedList2<Integer>();
		lm1.add(1);
		lm1.add(3);
		lm1.add(5);
		lm1.add(7);
		lm1.add(9);

		MyLinkedList2<Integer> lm2 = new MyLinkedList2<Integer>();
		lm2.add(2);
		lm2.add(4);
		lm2.add(6);
		lm2.add(8);
		lm2.add(12);

		L2Node<Integer> nm = MyLinkedList2.mergeTwoSortedLinkedList(lm1.root, lm2.root);
		System.out.println(nm);

		MyLinkedList2<Integer> lmn1 = new MyLinkedList2<Integer>();
		lmn1.add(1);
		lmn1.add(3);
		lmn1.add(5);
		lmn1.add(7);
		lmn1.add(9);

		MyLinkedList2<Integer> lmn2 = new MyLinkedList2<Integer>();
		lmn2.add(2);
		lmn2.add(4);
		lmn2.add(6);
		lmn2.add(8);
		lmn2.add(12);
		lmn2.add(14);
		lmn2.add(15);

		//nm = MyLinkedList2.merge(lmn1.root, lmn2.root);
		//lm1.print(nm);

		nm = MyLinkedList2.mergeTwoSortedLinkedList(lmn1.root, lmn2.root);
		lm1.print(nm);

		//reverse
		MyLinkedList2<Integer> rl = new MyLinkedList2<Integer>();
		rl.add(1);
		rl.add(2);
		rl.add(3);
		rl.add(4);
		rl.add(5);
		rl.add(6);
		rl.add(7);
		rl.add(8);
		rl.add(9);
		rl.add(10);
		L2Node<Integer> nn = MyLinkedList2.reverse(rl.root);
		rl.print(nn);

		MyLinkedList2<Integer> ro = new MyLinkedList2<Integer>();
		ro.add(1);
		ro.add(2);
		ro.add(3);
		ro.add(4);
		ro.add(5);
		ro.add(6);

		//L2Node<Integer> rn = ro.rotate(ro.root, 4);
		L2Node<Integer> rn = ro.rotate(ro.root, 4);
		ro.print(rn);

		//find intersection
		L2Node<Integer> in1 = new L2Node<>(3);
		L2Node<Integer> in2 = new L2Node<>(6);
		L2Node<Integer> in3 = new L2Node<>(9);
		L2Node<Integer> in4 = new L2Node<>(15);
		L2Node<Integer> in5 = new L2Node<>(30);
		L2Node<Integer> in6 = new L2Node<>(10);

		in1.setNext(in2);
		in2.setNext(in3);
		in3.setNext(in4);
		in4.setNext(in5);

		in6.setNext(in4);

		L2Node<Integer> in7 = MyLinkedList2.findIntersection(in1, in6);
		System.out.println(in7);

		//swap list
		MyLinkedList2<Integer> sl = new MyLinkedList2<Integer>();
		sl.add(1);
		sl.add(2);
		sl.add(3);
		sl.add(4);
		sl.add(5);
		sl.add(6);
		sl.add(7);
		sl.add(8);
		sl.add(9);
		sl.add(10);

		MyLinkedList2.swapList(sl.root);
		sl.print(sl.root);

		//reverse list in groups
		MyLinkedList2<Integer> rgll = new MyLinkedList2<Integer>();
		rgll.add(1);
		rgll.add(2);
		rgll.add(3);
		rgll.add(4);
		rgll.add(5);
		rgll.add(6);
		rgll.add(7);
		rgll.add(8);
		rgll.add(9);
		rgll.add(10);
		//L2Node<Integer> n = MyLinkedList2.reverseListInGroups(rgll.root, 4);
		//rgll.print(n);
		MyLinkedList2.reverseListInGroups(rgll.root, 4);

	}
}

class L2Node<T> {	
	
	T data;
	L2Node<T> next;
	
	public L2Node(T data) {
		super();
		this.data = data;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public L2Node<T> getNext() {
		return next;
	}
	public void setNext(L2Node<T> next) {
		this.next = next;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return data.toString();
	}
	
	

}

