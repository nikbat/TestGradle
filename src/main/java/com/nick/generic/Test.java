package com.nick.generic;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//multiplecation by 2;
		
		
		int number = 8;
		System.out.println(number << 1);
		
		//division by 2 
		number = 8;
		System.out.println(number >> 1);
		
		number = 8;
		System.out.println(number >>> 1);
		
		/*int a = 9;
		int b = 5;
		
		a = a^b;
		System.out.println(a);
		b = a^b;
		System.out.println(b);
		a = a^b;
		System.out.println(a);
		
		a = 9;
		b = 5;
		
		a = a+b;
		b = a-b;
		a = a-b;
		
		System.out.println(a+" , "+b);*/
		
		
		//printSeries(50);
		//printSeries2(10);
		
		/*int i = 125/10;
		System.out.println(i);*/
		
		//Pattern p = Pattern.compile(".*[1-9].*");
		//System.out.println(p.matcher("1abcd").find());
		fib(10);
		System.out.println("fibr "+fibr(10));
		System.out.println(great(21,51,61));
		System.out.println(reverseString("Test"));
		printFloyd();
		firstNonRepeatedChar("total");
		removeSpecificChars("Battle of the Vowels: Hawaii vs. Grozny", "aeiou"	);
		stringToInteger("1234");
		integerToString(-1);
		System.out.println(stringPermutationOfEachOther("abcd", "dacb"));
		addSpaceToString();
		stringCompresion("aabcccccaaa");
		
		int a[][] = {
					{1,2,3,0},
					{1,2,3,4},
					{1,2,3,4},
					{0,2,3,4},
		};
		setZerosInMatrix(a);
	}

	private static void printSeries(int data) {
		int l = 1;
		int m = 1;
		System.out.print(l);
		System.out.print(",");
		System.out.print(m);
		System.out.print(",");
		for (int i = 3; i < data; i++) {
			int fib = l + m;
			System.out.print(fib);
			System.out.print(",");
			l = m;
			m = fib;
		}
	}

	private static int printSeries2(int n) {
		if (n == 0 || n == 1) {
			return n;
		}

		int fib = printSeries2(n - 1) + printSeries2(n - 2);
		System.out.print(fib + ",");
		return fib;
	}

	public int greater(int one, int two, int three) {
		return (one > two) ? ((one > three) ? one : three) : ((two > three) ? two : three);

	}

	public static void fib(int n) {
		int a = 1;
		int b = 1;
		int fib;
		for (int i = 2; i < n; i++) {
			fib = a + b;
			System.out.print(fib);
			System.out.print(",");
			a = b;
			b = fib;
		}
	}

	public static int fibr(int n) {
		if (n == 1 || n == 2) {
			return 1;
		}
		return fibr(n - 1) + fibr(n - 2);
	}

	public static int great(int one, int two, int three) {
		return (one > two) ? ((one > three) ? one : three) : ((two > three) ? two : three);
	}

	public static String reverseString(String s) {
		if (s.length() <= 1) {
			return s;
		}

		return reverseString(s.substring(1)) + s.charAt(0);
	}

	public static void printFloyd() {
		int number = 1;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < i; j++) {
				System.out.print(number++);
			}
			System.out.println();
		}
	}
	
	public static void firstNonRepeatedChar(String a){
		int[] charCounts = new int[256];
		a.chars().forEach(c -> charCounts[c]++);
		for(char c : a.toCharArray()){
			if(charCounts[c] == 1){
				System.out.println(c);
				break;
			}
		}
	}
	
	public static void removeSpecificChars(String a, String b){
		boolean[] rc = new boolean[256];
		b.chars().forEach(c -> rc[c] = true);
		StringBuilder sb = new StringBuilder();
		for(char c : a.toCharArray()){
			if(!rc[c]){
				sb.append(c);
			}else{
				sb.append(' ');
			}
		}
		
		System.out.println(sb.toString());
	}
	
	public static void stringToInteger(String a){
		boolean negative = false;
		if(a.startsWith("-")){
			negative = true;
		}
		
		int number = 0;
		for(char c : a.toCharArray()){
			if(c == '-'){
				continue;
			}
			int t = c - '0';
			number = (number * 10) + t;
		}
		if(negative){
			number = number * -1;
		}
		
		System.out.println(number);
	}
	
	public static void integerToString(int a){	
		
		StringBuilder sb = new StringBuilder();
		boolean negative = false;
		if(a < 0){
			negative = true;
			a = a *-1;
		}
		while (a != 0){
			int t = a % 10;
			sb.append(t);
			a = a/10;
		}
		System.out.println(sb.reverse());
		
	}
	
	public static boolean stringPermutationOfEachOther(String a, String b){
		if(a.length() != b.length()){
			return false;
		}
		char[] aa = a.toCharArray();
		Arrays.sort(aa);
		String sa = new String(aa);
		
		char[] bb = b.toCharArray();
		Arrays.sort(bb);
		String sb = new String(bb);
		
		return sa.equals(sb);
	}
	
	public static boolean stringPermutationOfEachOther1(String a, String b){
		if(a.length() != b.length()){
			return false;
		}
		int[] ds = new int[256];
		a.chars().forEach(c -> ds[c]++);
		for (char c : b.toCharArray()){
			ds[c]--;
			if(ds[c] < 0){
				return false;
			}
				
		}
		return true;
	}
	
	public static void addSpaceToString(){
		String a = "This is a test for you          ";
		char[] ca = a.toCharArray();
		int trueLength = 22;
		int totalLenght = 32;
		if((trueLength + 5*2) != totalLenght){
			System.out.println("Error");
		}
		System.out.println(trueLength);
		System.out.println(totalLenght);
		for(int i = trueLength-1; i >=0; i--){
			if(a.charAt(i) == ' '){
				ca[--totalLenght] = 'a';
				ca[--totalLenght] = '2';
				ca[--totalLenght] = '%';
			}else{
				ca[--totalLenght] = a.charAt(i);
			}
		}
		
		System.out.println(new String(ca));		
	}
	
	public static void stringCompresion(String a){
		int l = 0;
		char tc = ' ';
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < a.length(); i++){
			if(i == 0){
				tc = a.charAt(i);
				l = 1;
			}else{
				if(a.charAt(i) == tc){
					l++;
				}else{
					sb.append(tc);
					sb.append(l);
					l = 1;
					tc = a.charAt(i);
				}
			}
		}
		sb.append(tc);
		sb.append(l);
		System.out.println(sb.toString());
		
	}
	
	public static void setZerosInMatrix(int[][] a){
		boolean[] row = new boolean[a.length];
		boolean[] column = new boolean[a[0].length];
		
		for(int i = 0; i < a.length; i++){
			for(int j =0; j< a[0].length; j++){
				if(a[i][j] == 0){
					row[i] = true;
					column[j] = true;
				}
			}
		}
		
		for(int i = 0; i < a.length; i++ ){
			for(int j = 0; j < a[0].length; j++){
				if(row[i] || column[j]){
					a[i][j] = 0;
				}
			}
		}
		
		System.out.println(ArrayUtils.toString(a));
	}

}
