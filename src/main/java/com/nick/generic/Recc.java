package com.nick.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.lang.ArrayUtils;

public class Recc {

	final String s = "abcd";
	StringBuilder o = new StringBuilder();
	boolean[] u = new boolean[4];
	final String in = "1234";

	public static void main(String[] args) throws Exception{
		Recc temp = new Recc();
		/*
		 * List<String> permutation = temp.permuteString("abc"); String[] a =
		 * new String[permutation.size()]; permutation.toArray(a);
		 * System.out.println(ArrayUtils.toString(a)); temp.permute();
		 * temp.combine(); temp.printDiagonal();
		 * System.out.println(temp.findMaxPathOfRobot(aa, 0, 0));
		 * temp.printbracket(3, 3, new char[6], 0);
		 * System.out.println(temp.makechange(100, 25));
		 * 
		 * int i = 11; System.out.println(Math.log10(i));
		 * System.out.println(Math.ceil(Math.log10(i)));
		 * System.out.println(Math.pow(10,Math.max(1,
		 * Math.ceil(Math.log10(i)))));
		 * 
		 * temp.addArrays(a1.length-1, a2.length-1, 0);
		 * System.out.println(ArrayUtils.toString(a3));
		 

		temp.bishopMoves(0, 0, 2, 3);*/
		//temp.printNumbers("1?00?101", 0, new StringBuilder());
		//temp.printNumbers("1?00?101", 0, new StringBuilder());
		
		//MessageDigest d = MessageDigest.getInstance("MD5");
		//System.out.println("test".getBytes());
		//System.out.println("test".getBytes());
		
		//temp.maxSeq();
		
		 Pattern pattern = Pattern.compile("[aeiou]");
		 Matcher matcher = pattern.matcher("This is a test");
		 while(matcher.find()){
			 System.out.println("Found");
		 }
		 // bad regular expression
		 
		  
		
		
	}

	private List<String> permuteString(String s) {
		List<String> permutation = new ArrayList<String>();

		if (s == null) {
			return null;
		} else if (s.length() == 0) {
			permutation.add("");
			return permutation;
		}

		char first = s.charAt(0);
		String remainder = s.substring(1);
		List<String> words = permuteString(remainder);
		for (String word : words) {
			for (int i = 0; i < word.length(); i++) {
				permutation.add(addFirst(i, word, first));
			}
		}
		return permutation;
	}

	private String addFirst(int i, String word, char first) {
		String s = word.substring(0, i);
		String u = word.substring(i);
		return s + first + u;

	}

	private void permute() {
		if (o.length() == s.length()) {
			System.out.println(o);
			return;
		}

		for (int i = 0; i < s.length(); i++) {
			if (u[i]) {
				continue;
			}
			o.append(s.charAt(i));
			u[i] = true;
			permute();
			u[i] = false;
			o.setLength(o.length() - 1);
		}
	}

	public void combine() {
		combine(0);
		o = new StringBuilder();
	}

	private void combine(int start) {
		for (int i = start; i < in.length(); ++i) {
			o.append(in.charAt(i));
			System.out.println(o);
			if (i < in.length())
				combine(i + 1);
			o.setLength(o.length() - 1);
		}
	}

	// move when you find 1 and dont move if you find 0
	static int[][] aa = { { 1, 1, 1, 1 }, { 1, 0, 0, 1 }, { 1, 0, 0, 1 },
			{ 1, 1, 1, 1 } };

	static int count = 0;

	private int findMaxPathOfRobot(int[][] a, int i, int j) {
		if (i == 3 && j == 3) {
			count++;
		}

		if (i + 1 <= 3 && a[i + 1][j] != 0) {
			findMaxPathOfRobot(a, i + 1, j);
		}

		if (j + 1 <= 3 && a[i][j + 1] != 0) {
			findMaxPathOfRobot(a, i, j + 1);
		}

		return count;
	}

	private void printbracket(int l, int r, char[] s, int count) {
		if (l < 0 || r < 0) {
			return;
		}
		if (l == 0 && r == 0) {
			System.out.println(s);
		}

		if (l > 0) {
			s[count] = '(';
			printbracket(l - 1, r, s, count + 1);
		}

		if (r > l) {
			s[count] = ')';
			printbracket(l, r - 1, s, count + 1);
		}
	}

	// coin example
	private int makechange(int n, int d) {
		int next_d = 0;
		switch (d) {
		case 25:
			next_d = 10;
			break;
		case 10:
			next_d = 5;
			break;
		case 5:
			next_d = 1;
			break;
		case 1:
			return 1;
		}

		int ways = 0;
		for (int i = 0; (i * d) <= n; i++) {
			ways += makechange(n, next_d);
		}
		return ways;
	}

	static int[] a1 = { 1, 2, 3, 4, 5, 7 };
	static int[] a2 = { 1, 2, 3, 4, 5, 6 };
	static int[] a3 = new int[a2.length];

	private void addArrays(int i, int j, int carry) {
		if (i < 0 && j < 0) {
			return;
		}

		int sum = carry;
		if (i >= 0) {
			sum += a1[i];
		}

		if (i >= 0) {
			sum += a2[i];
		}

		if (sum >= 10) {
			carry = sum / 10;
			sum = sum % 10;
		}

		a3[i] = sum;
		addArrays(i - 1, j - 1, carry);

	}

	// number of ways bishop can move
	int[][] board = new int[8][8];

	private void bishopMoves(int i, int j, int l, int m) {
		int ways = 0;
		while (i != l || j != m) {
			if (i != l && j != m) {
				if (i < l) {
					i++;
				} else {
					i--;
				}

				if (j < m) {
					j++;
				} else {
					j--;
				}
			} else if (i != l) {

				if (i < l) {
					i++;
				} else {
					i--;
				}
			} else {
				if (j < m) {
					j++;
				} else {
					j--;
				}
			}
			ways++;
		}

		System.out.println(ways);

	}

	static int getNumberOfPrimes(int N) { 

		if (N <= 0) return 0;
		if (N < 3) return N-1; 

		List<Integer> primes = new ArrayList<Integer>();
		primes.add(1);
		primes.add(2);

		for (int i = 3; i < N; i++) 
		{ 
			boolean flag = true; 

			for(int num : primes) 
			{ 
				if ((i % num) == 0) 
				{ 
					flag = false; 
					break; 
				}	 
			} 

			if (flag){ 
				primes.add(i); 
			} 
		} 
		return primes.size(); 
	}
	
	boolean isPrime(int n) {
	    //check if n is a multiple of 2
	    if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}
	
	public void printNumbers(String s, int i, StringBuilder b){
		if(s.length() == b.length()){
			System.out.println(b.toString());	
			return;
		}
		char a = s.charAt(i);
		if(a == '?'){
			StringBuilder temp = new StringBuilder(b.toString());
			temp.append(1);
			printNumbers(s, i+1,temp );
			StringBuilder temp2 = new StringBuilder(b.toString());
			temp2.append("0");
			printNumbers(s, i+1, temp2);
		}else{
			b.append(a);
			printNumbers(s, i+1, b);
		}
		
	}
	
	//Sequence
	//http://www.youtube.com/watch?v=U-xOVWlcgmM
	
	int[] sa = {10,22,9,33,21,50,41,60,80};
	
	private void maxSeq(){		
		List<List<Integer>> sequences = new CopyOnWriteArrayList<List<Integer>>();
		List<Integer> firstSequence = new CopyOnWriteArrayList<Integer>();
		firstSequence.add(sa[0]);
		sequences.add(firstSequence);
		for(int i=1; i < sa.length; i++){
			boolean foundSequence = false;
			int temp  = sa[i];
			for(List<Integer> presentSequences : sequences){
				if(temp >  presentSequences.get(presentSequences.size()-1) ){
					// before adding anything make a copy of existing sequence and add it to sequences
					List<Integer> tempSequence = new CopyOnWriteArrayList<Integer>(presentSequences);
					tempSequence.add(temp);
					sequences.add(tempSequence);
					foundSequence = true;
				}
				if(!foundSequence){
					List<Integer> tempSequence = new CopyOnWriteArrayList<Integer>();
					tempSequence.add(temp);
					sequences.add(tempSequence);
				}
			}
		}
		
		List<Integer> maxSequence = null;
		int size = 0;
		for(List<Integer> presentSequences : sequences){
			if(presentSequences.size() >= size){
				size = presentSequences.size();				
				maxSequence = presentSequences;
				/*if(size == 6){
					System.out.println(maxSequence);
				}*/
			}
		}
		
		System.out.println(maxSequence);
		
	}
}