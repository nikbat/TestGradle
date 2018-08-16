package com.nick.stack;

import java.util.Stack;

public class MyStack2 {

	public static void main(String[] args) {
		MyStack2 s = new MyStack2();
		sortStack();
	}

	private static void sortStack(){
		Stack<Integer> s1 = new Stack<>();
		s1.push(4);
		s1.push(5);
		//s1.push(5);
		s1.push(6);
		s1.push(7);
		s1.push(0);
		s1.push(1);
		s1.push(2);

		Stack<Integer> s2 = new Stack<>();

		while(!s1.empty()){
			int tmp = s1.pop();

			while(!s2.empty() && s2.peek() > tmp){
				s1.push(s2.pop());
			}
			s2.push(tmp);
		}
		System.out.println(s2);

	}

}
