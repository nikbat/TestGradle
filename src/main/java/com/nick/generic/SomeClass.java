package com.nick.generic;

public class SomeClass implements SomeInterface{
	
	public static final int data = 2;
	
	@Override
	public void somemethod(){
		
	}
	
	
	public static void main(String[] args){
		SomeInterface a = new SomeClass();
		SomeClass b = new SomeClass();
		System.out.println(a.data);
		System.out.println(b.data);
	}

}
