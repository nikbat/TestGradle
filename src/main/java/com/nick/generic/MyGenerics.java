package com.nick.generic;

import java.util.ArrayList;
import java.util.List;


public class MyGenerics {
	
	public static void main(String[] args){
		
		//List<?> l1 = new ArrayList<?>();
		List<Object> l1 = new ArrayList<Object>();
		l1.add("String");
		l1.add(new Integer(1));
		
		List<String> l2 = new ArrayList<String>();
		//unsafeAddition(l2, "");
		unsafeRowAddition(l2, "");
		unsafeUnBoundedAddition(l2, "");
	}
	
	public static void unsafeAddition(List<Object> l , Object o){
		
	}
	
	public static void unsafeRowAddition(List l , Object o){
		
	}
	
	public static void unsafeUnBoundedAddition(List<?> l , Object o){
		//l.add();
	}

}
