package com.nick.ll;

public class MyLinkedList<E> {
	LNode<E> root;
	LNode<E> last;
	int size;
	
	
	public void add(E e){
		LNode<E> temp  = new LNode<E>(e);
		if(root == null){
			root = last = temp;
			size++;
		}else{
			last.next = temp;
			last = temp;
			size++;
		}
	}
	
	
	public static void main(String[] args){
		MyLinkedList<String> ml = new MyLinkedList<String>();
		ml.add("A");
		ml.add("B");
		ml.add("C");
		ml.add("D");
		ml.add("E");
		ml.add("F");
		ml.add("G");
		ml.add("H");
		ml.add("I");
		ml.add("J");
		ml.add("D");
		
		StringBuilder b = new StringBuilder();
		ml.print(ml.root,b);
		System.out.println(b);
		
		//ml.addFront("Z");
		
		b = new StringBuilder();
		ml.print(ml.root,b);
		System.out.println(b);
		
		ml.printCyclic(ml.root);
		
	
		
	}
	
	public void printCyclic(LNode<String> root){
		 
		LNode<String> n1 = root;
		LNode<String> n2 = root;
		
		while(n2.next != null){
			n1 = n1.next;
			n2 = n2.next.next;
			
			if(n1.data.equals(n2.data)){
				break;
			}
		}
		
		if(n2 == null || n2.next == null){
			System.out.println("Not cyclic");
			return;
		}
		
		n1 = root;
		
		while( !(n1.getData().equals(n2.getData())) ){
			n1 = n1.next;
			n2 = n2.next;
		}
		
		System.out.println("cyclic "+n2.getData());
	}
	
	public void print(LNode<String> node, StringBuilder builder){
		if(node != null){
			print(node.next,builder);
			builder.append(node.getData()+">");
		}
		
	}
	
	public void addFront(E e){
		LNode<E> temp = new LNode<E>(e);
		temp.next = root;
		root = temp;
	}
	

}
