package com.nick.generic;

public class L2Node<T> {	
	
	private T data;
	private L2Node<T> next;
	
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
