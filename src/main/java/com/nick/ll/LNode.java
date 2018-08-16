package com.nick.ll;

public class LNode<E> {
	E data;
	LNode<E> next;
	
	LNode(){
		
	}
	
	public LNode(E data) {
		this.data = data;
	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public LNode<E> getNext() {
		return next;
	}

	public void setNext(LNode<E> next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "LNode [data=" + data + "]";
	}
	
	
	

}
