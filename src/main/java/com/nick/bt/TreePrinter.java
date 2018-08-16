package com.nick.bt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreePrinter {
	
	public void printTree() {
		
		List<Association> input = new ArrayList<Association>(); 
		input.add(new Association("animal", "mammal")); 
		input.add(new Association("animal", "bird")); 
		input.add(new Association("lifeform", "animal")); 
		//input.add(new Association("cat", "lion")); 
		input.add(new Association("mammal", "cat")); 
		input.add(new Association("animal", "fish")); 
		
		MyTree mt = new MyTree();
		
		for(Association a : input){
			mt.addElement(a);
		}
		
		Queue<Node> q = new LinkedList<Node>();
		q.add(mt.root);
		while(!q.isEmpty()){
			Node n = q.poll();
			printTree(n);
			for(Node n1 : n.getChild()){
				q.add(n1);
			}
		}
	} 
	
	public void printTree(Node n){
		System.out.println(n.getName());
	}
	
	public static void main(String[] args){
		new TreePrinter().printTree();
	}
}

class MyTree{
	
	Node root;	
	public void addElement(Association a){
		if(root == null){
			Node n1 = new Node();
			n1.setName(a.parent);
			
			Node n2 = new Node();
			n2.setName(a.child);
			
			root = n1;
			root.setParent(null);
			root.addChild(n2);
			
		}else{
			Node n = new Node();
			n.setName(a.child); 
			
			//find if we have a parent starting with root
			Node p = findParent(root, a.parent);
			if(p != null){
				//check if this is equal to root				
				n.setParent(p);
				p.addChild(n);
			}else{
				//check if n is a root new root
				n.name.equals(root.name);
				n.setName(a.parent);
				n.addChild(root);
				root.setParent(n);
				root = n;
				
				
			}
			
		}
	}
	
	private Node findParent(Node root, String name){		
		if(root != null){			
			if(root.getName().equals(name)){
				return root;
			}else{
				for(Node n : root.getChild())
					return findParent(n, name);
			}
			
		}
		return null;
	}
}


class Association { 
	String parent; 
	String child; 
	
	public Association(String parent, String child) { 
		this.parent = parent;
		this.child = child;
	}	 
}

class Node {
	Node parent;
	List<Node> child = new ArrayList<Node>();
	String name;
	
	public Node(){
		
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getChild() {
		return child;
	}

	public void setChild(List<Node> child) {
		this.child = child;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addChild(Node e){
		child.add(e);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
		
	
}

