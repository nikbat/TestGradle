package com.nick.ll;

public class LLFinal<T> {
	public LNode<T> root;
	public LNode<T> last;
	public int size = 0;
	
	public LNode<T> add(T t){
		LNode<T> n = new LNode<>(t);
		
		if(root == null){
			root = n;
			last = n;
		}else{
			last.next = n;
			last = n;
		}
		
		return n;
	}
	
	public LNode<T> add(LNode<T> n){
		if(root == null){
			root = n;
			last = n;
		}else{
			last.next = n;
			last = n;
		}
		
		return n;
	}
	
	void print(LNode<T> n, StringBuilder sb){
		while(n != null){
			sb.append(n.getData());
			sb.append(">");
			n = n.next;
		}
	}
	
	void addFront(T t){
		LNode<T> n = new LNode<T>(t);
		n.next = root;
		root = n;
	}
	
	void printCyclic(LNode<T> n){
		LNode<T> n1 = n;
		LNode<T> n2 = n;
		
		if(n1 == null){
			System.out.println("NA");
			return;
		}
		
		while(n1.next != null){
			n2 = n2.next.next;
			n1 = n1.next;
			
			if(n1.data.equals(n2.data)){
				break;
			}
		}
		
		if(n1 == null || n2 == null){
			System.out.println("NA");
			return;
		}
		
		n1 = root;
		
		while(n1 != null){
			n1 = n1.next;
			n2 = n2.next;
			
			if(n1.data == n2.data){
				System.out.println(n1.data);
				break;
			}
		}
		
	}
	
	public static void main(String[] args){
		LLFinal<String> ml = new LLFinal<String>();
		ml.add("A");
		ml.add("B");
		ml.add("C");
		LNode<String> n = ml.add("D");
		ml.add("E");
		ml.add("F");
		ml.add("G");
		ml.add("H");
		ml.add("I");
		ml.add("J");
		
		
		StringBuilder b = new StringBuilder();
		ml.print(ml.root,b);
		System.out.println(b);
		
		ml.addFront("Z");
		
		b = new StringBuilder();
		ml.print(ml.root,b);
		System.out.println(b);
		
		ml.add(n);
		ml.printCyclic(ml.root);
	}


	
}
