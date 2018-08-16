package com.nick.stack;

import java.util.Stack;

public class MyStack {
	
	public static void main(String[] args){
			sortStatck();
	}
	
	
	static Stack<Integer> s1 = new Stack<Integer>();
	
	
	private static void sortStatck(){
		s1.push(4);
		s1.push(5);
		s1.push(5);
		s1.push(6);
		s1.push(7);
		s1.push(0);
		s1.push(1);
		s1.push(2);
		
		Stack<Integer> s2 = new Stack<Integer>();
		System.out.println(s1);
		
		while(!s1.isEmpty()){
			int temp = s1.pop();
			while(!s2.isEmpty() && s2.peek() > temp){
				s1.push(s2.pop());
			}
			s2.push(temp);			
		}
		System.out.println(s2); 
	}

}
