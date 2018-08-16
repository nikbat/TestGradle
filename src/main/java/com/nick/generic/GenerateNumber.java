package com.nick.generic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GenerateNumber {
	
	public static Map<Integer, String> numberToAlphabetMap = new HashMap<>();
	int count = 26;
	static{
		numberToAlphabetMap.put(1, "A");
		numberToAlphabetMap.put(2, "B");
		numberToAlphabetMap.put(3, "C");
		numberToAlphabetMap.put(4, "D");
		numberToAlphabetMap.put(5, "E");
		numberToAlphabetMap.put(6, "F");
		numberToAlphabetMap.put(7, "G");
		numberToAlphabetMap.put(8, "H");
		numberToAlphabetMap.put(9, "I");
		numberToAlphabetMap.put(10, "J");
		numberToAlphabetMap.put(11, "K");
		numberToAlphabetMap.put(12, "L");
		numberToAlphabetMap.put(13, "M");
		numberToAlphabetMap.put(14, "N");
		numberToAlphabetMap.put(15, "O");
		numberToAlphabetMap.put(16, "P");
		numberToAlphabetMap.put(17, "Q");
		numberToAlphabetMap.put(18, "R");
		numberToAlphabetMap.put(19, "S");
		numberToAlphabetMap.put(20, "T");
		numberToAlphabetMap.put(21, "U");
		numberToAlphabetMap.put(22, "V");
		numberToAlphabetMap.put(23, "W");
		numberToAlphabetMap.put(24, "X");
		numberToAlphabetMap.put(25, "Y");
		numberToAlphabetMap.put(26, "Z");		
	}
	
	public static void main(String[] args){
		System.out.println(new GenerateNumber().getAlbhabet(56));
		LinkedList<String> l = new LinkedList<>();
		l.addFirst("A");
		l.addFirst("B");
	}
	
	public String getAlbhabet(int number){
		StringBuilder sb = new StringBuilder();
		while(number != 0){
			int temp = number%count;
			sb.insert(0,numberToAlphabetMap.get(temp));
			number = number / count;
		}
		return sb.toString();
	}
}
