package com.nick.com.nic.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lexicographically {

	public static void main(String[] args) {
		
		
		String input = "welcometojava";
		int count = 3;
		List<String> data = new ArrayList<String>(); 
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< ((input.length())-(count-1)) ;i++){
			sb.setLength(0);
			for(int k =i ; k < count+i; k++){
				sb.append(input.charAt(k));
			}
			data.add(sb.toString());
		}
		Collections.sort(data);
		System.out.println(data.get(0));		
		System.out.println(data.get(data.size()-1));
		
		
	}

}
