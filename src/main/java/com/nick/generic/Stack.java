package com.nick.generic;

import java.util.*;


public class Stack<E> {
	
	private final int DEFAULT_SIZE = 16;
	private E[] elements;
	private int size = 0;
	
	
	public Stack(){
		elements = (E[])new Object[DEFAULT_SIZE]; 
	}
	
	public E pop(){
		return elements[--size];
	}
	
	public void push(E element){
		elements[size++] = element;
	}
	
	public static <E> Set<E> join(Set<? extends E> s1, Set<? extends E> s2){
		Set<E> result = new HashSet<E>(s1);
		result.addAll(s2);
		return result;
	}
	
	public static <E> E max(List<? extends E> list) {
		E result = null;
		if(list != null){
			for(E e : list){
				if(result == null){
					result = e;
				}else{
					//if(e.compareTo(result) > 0){
						result = e;
					//}
				}
			}
		}
		return result;
	}
	
	public static <T extends Comparable<? super T>> T max2(List<? extends T> list ) {
		Iterator<? extends T> i = list.iterator();
		T t = i.next();
		while(i.hasNext()){
			T t2 = i.next();
			if(t.compareTo(t2) > 0){
				
			}
		}
		return t;
	}
	
	public void pushAll(Iterable<E> src){
		for( E e : src){
			push(e);
		}
	}
	
	public static void main(String[] args){
		Set<Integer> s1 = new HashSet<Integer>();
		Set<Double> s2 = new HashSet<Double>();
		Set<Double> s3 = new HashSet<Double>();
		Set<Number> result1 = Stack.<Number>join(s1, s2);
		Set<Double> result = Stack.join(s2, s3);
		
		List<Long> l1 = new ArrayList<Long>();
		Number n1 = Stack.<Number> max(l1);
		
		
	}
	

}
