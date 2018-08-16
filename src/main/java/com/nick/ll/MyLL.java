package com.nick.ll;

import java.util.HashMap;

public class MyLL<E> {
	private LNode<E> root;
	private LNode<E> last;
	int length = 0;
	
	public LNode add(E e){
		if (root == null){
			root = new LNode<>(e);
			last = root;
			length++;
			return root;
		}else{
			LNode<E> temp = new LNode<E>(e);
			last.next = temp;
			last = temp;
			length++;
			return temp;
		}
	}
	
	public LNode add(LNode<E> n){
		if (root == null){
			root = n;
			last = root;
			length++;
			return root;
		}else{
			LNode<E> temp = n;
			last.next = temp;
			last = temp;
			length++;
			return temp;
		}
	}
	
	public void addFront(E e){
		if(root == null){
			root = new LNode<E>(e);
			last = root;
			length++;
		}else{
			LNode<E> temp = new LNode<E>(e);
			temp.next = root;
			root = temp;
			length++;
		}
	}
	
	public void findN(int n){
		LNode<E> temp = root;
		LNode<E> temp2 = root;
		int count = 1;
		while (temp != null){
			if(count > n){
				temp2 = temp2.next;
			}
			temp = temp.next;
			count++;
		}
		System.out.println(temp2.getData());
		
	}
	
	public void print(){
		LNode<E> temp = root;
		while (temp != null){
			System.out.print(temp.getData().toString()+ "->");
			temp = temp.next;
		}
		System.out.println();
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		LNode<E> temp = root;
		while (temp != null){
			sb.append(temp.getData().toString()+ "->");
			temp = temp.next;
		}
		return sb.toString();
	}
	
	public void printCyclic(){
		
		if(root == null){
			System.out.println("not cyclic");
			return;
		}
		LNode<E> n1 = root;
		LNode<E> n2 = root;
		
		while(n2.next != null){
			n1 = n1.next;
			n2 = n2.next.next;
			if(n1.getData() == n2.getData()){
				break;
			}
		}
		
		if(n1 == null || n2 == null){
			System.out.println("not cyclic");
			return;
		}
		n1 = root;
		
		while(! (n1.getData().equals(n2.getData())) ){
			n1 = n1.next;
			n2 = n2.next;
		}
		
		System.out.println(n1.getData());		
	}
	
	public void removeDuplicate(){
		HashMap<String, LNode<E>> duplicate = new HashMap();
		LNode<E> temp = root;
		while (temp != null){
			if(duplicate.containsKey(temp.getData().toString())){
				if(temp.next != null){
					temp.data = temp.next.data;
					temp.next = temp.next.next;
				}else{
					//this is a last emlement make it null
					temp = null;
				}
			}else{
				duplicate.put(temp.getData().toString(), temp);
				//move next only in this loop
				temp = temp.next;
			}
			
		}
	}
	
	public void partitionList(int p){
		if(root == null){
			return;
		}
		MyLL<E> p1 = new MyLL<E>();
		MyLL<E> p2 = new MyLL<E>();
		
		LNode<E> temp = root; 
		while(temp != null){
			if ( Integer.parseInt(temp.data.toString()) < p){
				p1.add(temp);
			}else{
				p2.add(temp);
			}
			temp = temp.next;
		}
		//now merge
		p2.last.next = null;
		p1.last.next = p2.root;
		p1.print();
		p2.print();
	}
	
	public void isPalindrome(){
		//Approach 1 reverse a list and compare the 2 LinkLists
		//Approach 2 Fast Ruuner and Slow runner approach, store the slow runner in stack, once you have reached the end pop each element from a stack and get compre elements with slow runner
	}
	
	public void hasIntersection(MyLL<E> l1, MyLL<E> l2){
		//Approach 1 Put each element in a hashtable and compare if there is any match
		//Approach 2
		//a. Run through each list and find length
		//b. If the last node of both ll are not same return false immediately
		//c. Find the diffrence and run the longer ll for diffrence 
		//d. After that run both nodes simulataniouly and stop where you find same reference
	}
	
	
	
	public void reverse1(LNode<E> node, MyLL<E> ll){		
		while(node != null){			
			ll.addFront(node.getData());
			node = node.next;
		}
	}
	
	
	
	public static void main(String[] args){
		MyLL<String> ml = new MyLL<String>();
		LNode<String> A1 = ml.add("A");
		LNode<String> B1 = ml.add("B");
		LNode<String> C1 = ml.add("C");
		LNode<String> D1 = ml.add("D");
		LNode<String> E1 = ml.add("E");
		LNode<String> F1 = ml.add("F");
		LNode<String> A2 = ml.add("A"); // Duplicate
		LNode<String> G1 = ml.add("G");
		LNode<String> H1 = ml.add("H");
		LNode<String> I1 = ml.add("I");		
		LNode<String> J1 = ml.add("J");
		LNode<String> B2 = ml.add("B"); // Duplicate
		
		ml.print();
		MyLL<String> ll = new MyLL<String>();
		ml.reverse1(ml.root,ll);
		ll.print();
		ml.removeDuplicate();
		ml.print();
		ml.findN(2);
		
		
		
		
		MyLL<String> ml1 = new MyLL<String>();
		LNode<String> A = ml1.add("A");
		LNode<String> B = ml1.add("B");
		LNode<String> C = ml1.add("C");
		LNode<String> D = ml1.add("D");
		LNode<String> E = ml1.add("E");
		LNode<String> F = ml1.add("F");
		LNode<String> G = ml1.add("G");
		LNode<String> H = ml1.add("H");
		LNode<String> I = ml1.add("I");		
		ml1.add(D); // uncomment this to enable cyclic
		ml1.printCyclic();
		
		MyLL<Integer> ml2 = new MyLL<Integer>();
		LNode<Integer> I3 = ml2.add(3);
		LNode<Integer> I5 = ml2.add(5);
		LNode<Integer> I8 = ml2.add(8);
		LNode<Integer> I15 = ml2.add(5);
		LNode<Integer> I10 = ml2.add(10);
		LNode<Integer> I2 = ml2.add(2);
		LNode<Integer> I11 = ml2.add(1); 
		ml2.partitionList(5);
		
		
	}

	
}
