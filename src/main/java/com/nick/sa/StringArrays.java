package com.nick.sa;

public class StringArrays {
	
	public static void main(String[] args){
		String a = "Battle of the Vowels: Hawaii vs. Grozny";
		String b = "aeiou";
		System.out.println(new StringArrays().removeChars(a, b));
		new StringArrays().reverse();
		new StringArrays().reverseArray();
		new StringArrays().intToStr();
		new StringArrays().stringToInteger();
		new StringArrays().intToStr();
		new StringArrays().duplicate();
		new StringArrays().space();
		
	}
	
	/*
	 	remove string from a given
	 	string removeChars( string str, string remove );
		where any character existing in remove must be deleted from str. For example,
		given a str of "Battle of the Vowels: Hawaii vs. Grozny" and a remove of
		"aeiou", the function should transform str to �Bttl f th Vwls: Hw vs. Grzny�.
		Justify any design decisions you make, and discuss the efficiency of your solution.
	 */
	
	/*
	 *  Iterate string with 2 pointers,
	 */
	
	public String removeChars(String str, String remove){
		int source = 0;
		int dest = 0;
		char[] sa = str.toCharArray();
		char[] ra = remove.toCharArray();
		
		boolean[] rb = new boolean[132];
		
		for(char c : ra){
			rb[c] = true;
		}
		
		
		
		for(int i=0; i<sa.length;i++ ){
			//if(!contains(ra, sa[i])){
			if(!containsImproved(rb, sa[i])){
				sa[dest++] = sa[i];	
				if(i > dest){
					sa[i] = ' ';
				}
			}else{
				sa[i] = ' ';
			}
		}
		return new String(sa,0,dest);
	}
	
	public boolean contains(char[] ra, char c){
		
		for(char a : ra){
			if(a == c){
				return true;
			}
		}
		return false;
	}
	
	public boolean containsImproved(boolean[] ra, char c){
		
		return ra[c];
	}
	
	/*
	 * PROB LEM Write a function that reverses the order of the words in a string. For
		example, your function should transform the string "Do or do not, there is no
		try." to "try. no is there not, do or Do". Assume that all words are space delimited
		and treat punctuation the same as letters.
	 */
	
	public void reverse(){
		String data = "Do or do not, there is no try";
		String[] split = data.split(" ");
		for(int i=split.length-1; i>=0; i-- ){
			System.out.print(split[i] + " ");
		}
		System.out.println();
	}
	
	public void reverseArray(){
		String data = "Do or do not, there is no try";
		//String data = "Piglet quantum";
		char d[] = data.toCharArray();
		char n[] = new char[d.length];
		int startPoint = 0;
		int endPoint = 0;
		boolean wordStart = true;
		int np = 0;
		
		for(int i = d.length-1; i>=0; i--){
			//scan the data in reverse order till you get space,initiate the start point and end point
			if(wordStart){
				startPoint = i;
				wordStart = false;
			}
			
			if(d[i] == ' ' || i == 0){
				//found a word set a end point and reset wordStart
				if(i == 0){
					endPoint = i;
				}else{
					endPoint = i+1;
					
				}
				wordStart = true;
				for (int j=endPoint; j<=startPoint; j++){
					n[np++] = d[j];
				}
				
				if(i != 0){
					n[np++] = ' ';
				}
			}	
		}
		
		System.out.println(new String(n));
		char test = '1';
		System.out.println("char: "+((int)test));
		int zero = (int)'0';
		System.out.println("char: "+zero);
		System.out.println("char: "+(zero + 4));
		char temp = (char)(zero + 4);
		
		System.out.println("char: "+temp);
	}
	
	/*
	 *  PROBLEM Write two conversion routines. The first routine converts a string
		to a signed integer. You may assume that the string contains only digits and the
		minus character ('-'), that it is a properly formatted integer number, and that the
		number is within the range of an int type. The second routine converts a signed
		integer stored as an int back to a string.
	 */
	
	public void intToStr(){
		int data = 1234;
		int zero = (int)'0';
		int temp = 0;
		boolean isNegative = false;
		
		char[] a = new char[10];		
		int length = 0;
		
		if(data < 0){
			isNegative = true;
			data = data*-1;
		}
		
		while( data != 0 ) {
			temp = data % 10;
			a[length++] = (char)(zero +temp);					
			data = data/10;			
		}
		
		StringBuilder sb =new StringBuilder();
		if(isNegative){
			sb.append("-");
		}
		
		while(length > 0 ){
			sb.append(a[--length]);
		}
		
		System.out.println(sb);
	}
	
	public void stringToInteger(){
		String data = "-8687648";
		char d[] = data.toCharArray();
		int zero = (int)'0';
		int number = 0;
		int fn = 0;
		boolean isNegative = false;
		
		for(char t: d){
			if(t == '-'){
				isNegative = true;
				continue;
			}
			int temp = (t -zero);
			fn = number + temp;
			number = fn * 10;
			
		}
		
		if(isNegative){
			fn = fn*-1;
		}
		System.out.println(fn);
	}
	
	/*
	 * PROB LEM Write an efficient function to find the first nonrepeated character in a
		string. For instance, the first nonrepeated character in �total� is 'o' and the first
		nonrepeated character in �teeter� is 'r'. Discuss the efficiency of your algorithm.
	 */
	
	public void firstNonRepeatedChars(){
		String data = "total";
		int[] cp = new int[256];
		char[] ca = data.toCharArray();
		char find = ' ';
		for(char c : ca){
			cp[c] = cp[c]+1;
		}
		
		System.out.println("find "+find);
	}
	
	public void removeChars2(){
		String data = "Battle of the Vowels: Hawaii vs. Grozny";
		String remove = "aeiou";
		char[] da = data.toCharArray();
		boolean[] cp = new boolean[256];
		
		for(char c : remove.toCharArray()){
			cp[c] = true;
		}
		
		int p = 0;
		for(int i =0; i < da.length; i++){
			if(!cp[da[i]]){
				da[p++] = da[i];
			}
		}
		
		System.out.println(new String(da,0,p));
	}
	
	public void reverseString2(){
		String data = "Do or do not, there is no try";
	}
	
	/*
	 *	find if a string has all unique chars
	 *	This is a simple task, just maintain boolean char array 
	 */
	
	/*
	 * Design an algorithim to remove a duplicate chars in a string using no additional buffer 
	 * O(N^2) ifno 
	 */
	
	public void duplicate(){
		String data = "abcdabcdabcdabcd";
		char[] da = data.toCharArray();
		int p = 1; //tail
		boolean increment = true;
		
		for(int i=1; i<da.length;i++){
			int j;
			for(j = 0; j < i; j++) {
				if(da[j] == da[i]){
					da[i] = ' ';
					increment = false;
					break;
				}
			}
			if(increment){
				da[p++] = da[i];
			}
				
			
		}
		
		
		System.out.println("dups "+new String(da,0,p));
	}
	
	/*
	 * Replace spaces with %20
	 */
	
	public void space(){
		String data = "Keep trying someday you will be successful";
		char[] da = data.toCharArray();
		int l = da.length;
		int ns = 0;
		for(char d : da){
			if(d == ' '){
				ns++;
			}
		}
		
		int n = l + (ns*2);
		
		char[] dn = new char[n];
		int p = 0;
		for(int i=0;i<da.length;i++){
			if(da[i] != ' '){
				dn[p++] = da[i]; 
			}else{
				dn[p++] = '%';
				dn[p++] = '2';
				dn[p++] = '0';
			}
		}
		
		System.out.println(new String(dn));
		
	}
	
	public void matrix(){
		int[][] data = {
							{1,2,3,4,5,6,7,8},
							{1,2,3,4,5,6,7,8}
							
						};
		
		
	}
	
	/*
	 * Find if a given 2 strings are anagrams or not, "abcd"  "bdac"
	 * 
	 */
  
	/*
	 * "waterbottle" "erbottlewat"
	 */
	
	private void isLetter(){
		String s1 = "waterbottle";
		String s2 = "erbottlewat";
		s2.contains("waterbottlewaterbottle"); //s1+s1
		
	}
	
	

}
