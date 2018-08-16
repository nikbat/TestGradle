package com.nick.com.nic.test;

import java.util.Stack;

public class JavaStack {

	public static void main(String[] args) {
		
		String[] inputs = {"{}()","({()})","{}(","[]"};
		Stack<Character> stack = new Stack<Character>();		
		for(String input : inputs){
			stack.clear();
			for(int i = 0; i < input.length(); i++){
				if(stack.isEmpty()){
					stack.push(input.charAt(i));
					continue;
				}
				//check the peek element
				char peek = stack.peek();
				char element = input.charAt(i);
				switch (element) {

				case '}':
						if(peek == '{'){
							stack.pop();
						}else{
							stack.push(element);
						}
						break;
				case ')':
					if(peek == '('){
						stack.pop();
					}else{
						stack.push(element);
					}
					break;		
				
				case ']':
					if(peek == '['){
						stack.pop();
					}else{
						stack.push(element);
					}
					break;
	
				default:
					stack.push(element);
					break;
				}
				
				
			}
			System.out.println(stack.isEmpty());				
			
		}
	

	}

}
