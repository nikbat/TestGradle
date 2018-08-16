package com.nick.sa;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StringArray1 {
	
	public String reverseString(String s){
		if(s.length() <= 1){
			return s;
		}
		return reverseString(s.substring(1) ) + s.charAt(0);
	}
	
	public void fib(int n){
		int fib1 = 1;
		int fib2 = 1;
		int fib = 1;		
		for(int i=3; i<=n; i++){
			fib = fib1+fib2;
			System.out.print(fib + ",");
			fib2 = fib1;
			fib1 = fib;					
		}				
	}
	public int fibR(int n){
		if(n == 1){
			return 1;
		}
		if(n == 2){
			return 1;
		}
		return fibR(n -1) + fibR(n-2);
	}
	
	public boolean isAnangram(String s1, String s2){
		if(s1 == null && s2 == null){
			return true;
		}else if(s1.length() != s2.length()){
			return false;
		}else if(s1==null || s2==null){
			return false;
		}else{
			boolean[] ba = new boolean[256];
			s1.chars().forEach(c -> ba[c] = true);
			s2.chars().forEach(c -> ba[c] = false);			
			for(boolean b : ba){
				if(b){
					return false;
				}
			}
		}
		return true;
	}
	
	int sum = 0;
	public int sumNumberRecursive(int number){
		if(number == 0){
			return 0;
		}else{
			sum = sum + number % 10;
			number = number / 10;
			sumNumberRecursive(number);			
		}
		return sum;
	}
	
	public int sumNumberIterative(int number){
		int data = 0;
		while (number !=0){
			data =+ number % 10;
			number = number / 10;
		}
		return data;
	}
	
	public void swapNumbers(int a, int b){
		a = a+b;
		b = a-b;
		a = a-b;		
	}
	
	public void firstNonrepeatedChar(String s){
		char[] ca = s.toCharArray();
		LinkedHashMap<Character, Integer> lh = new LinkedHashMap<>();		
		for(char c : ca){
			if(lh.get(c) != null){
				lh.put(c, lh.get(c)+1);
			}else{
				lh.put(c, 1);
			}
		}
		for(Character k : lh.keySet()){
			if(lh.get(k) == 1){
				System.out.println("First nonrepreated Charater is "+k);
				break;
			}
		}
	}
	
	public void firstNonrepeatedCharIterative(String s){
		char[] ca = s.toCharArray();
		boolean repeated = false;
		for(int i=0; i<ca.length; i++){
			repeated = false;
			if(ca[i] == '#'){
				continue;
			}
			for(int j=i+1; j<ca.length; j++){
				//System.out.println(ca[j] +"=="+ ca[i]);
				if(ca[j] == ca[i]){
					repeated = true;
					ca[j] = '#';
					break;
				}
			}
			if(!repeated){
				System.out.println("First nonrepreated Charater is "+ca[i]);
				return;
			}
		}
	}
	public void firstNonrepeatedWordStream(List<String> s){		
		Map<String, Integer> d = s.stream().collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));
		//s.stream().collect(groupingBy(Function.identity(), counting()));		
	}
	
	public int stringInteger(String s){
		int number = 0 ;
		boolean isNegative = false;
		int n = '0';
		for(char c : s.toCharArray()){
			if(c == '-'){
				isNegative = true;
				continue;
			}
			int temp = c - n;
			number = number*10 + temp;
		}
		if(isNegative){
			number = number * -1;
		}
		return number;
	}
	
	public String integerString(int n){
		StringBuilder sb = new StringBuilder();
		boolean isnegative = false;
		if(n < 0){
			isnegative = true;	
			n = n*-1;
		}
		while(n != 0){
			int temp = n%10;
			n = n/10;
			sb.append(temp);
		}		
		return isnegative ? "-" + reverseString(sb.toString()) : reverseString(sb.toString());
	}
	
	public boolean oneStringIsPermutationOfOther(String s1, String s2){
		//Approach 1 is to sort both Array.sort(ch[]), both string should be equal after sort.
		//Approach 2 count the counts in int[] array
		return true;
	}
	
	public void insertPercentTwoZeroInString(String s, int trueLength){
		s = "Mr Jhon Smith    ";
		trueLength = 13;
		char[] c = s.toCharArray();
		System.out.println(c.length);
		
		final AtomicInteger ai = new AtomicInteger(0);
		s.trim().chars().forEach(t -> {
			if(t == ' '){
				ai.incrementAndGet();
			}
		});
		System.out.println(ai.get());
		
		int index = trueLength + ai.get()*2;
		if(index != c.length){
			return;
		}
		
		for(int i=trueLength-1; i>=0; i--){
			if(c[i] == ' '){
				c[index-1] = '0';
				c[index-2] = '2';
				c[index-3] = '%';
				index = index - 3;
			}else{
				c[index-1] = c[i];
				index--;
			}
		}
		System.out.println(new String(c));
	}
	
	public boolean oneEditReplace(String s1, String s2){
		int i = 0;
		int totalEdits = 0;
		
		for(char c : s1.toCharArray()){
			if(s2.charAt(i) != c){
				totalEdits++;
			}
			if(totalEdits > 1){
				return false;
			}
		}
		i++;
		return true;
	}
	
	public String stringCompress(String s1){
		StringBuilder sb = new StringBuilder();
		int count = 0;
		char temp = ' ';
		
		for(int i=0; i < s1.length();  i++){
			if(count == 0){
				sb.append(temp = s1.charAt(i));
				count++;
			}else{
				if(s1.charAt(i) == temp){
					count++;
				}else{
					sb.append(count);
					count = 0;
					sb.append(temp = s1.charAt(i));
					count++;
				}
			}
		}
		if(count > 0){
			sb.append(count);
		}
		return sb.toString();		
	}
	
	public void markRowOrColumnToZero(int[][] data){
		boolean[] r = new boolean[data.length];
		boolean[] c = new boolean[data[0].length];
		
		for (int i = 0; i < data.length; i++){
			for(int j =0; j< data[i].length ; j++){
				if(data[i][j] == 0){
					r[i] = true;
					c[j] = true;
				}
			}
		}
		for (int i = 0; i < data.length; i++){
			for(int j =0; j< data[i].length ; j++){
				if(r[i] || c[j]){
					data[i][j] = 0;
				}
			}
		}
	}
	
	private void rotateArray(int[] a, int n){
		for(int i=0; i<a.length; i++){
			int temp = a[(i+n) % n];
			a[(i+n) % n] = a[i]; 
			a[i] = temp;
		}
	}
	
	
	static final int[][] paths = {
			{0,0,1,0,1},
			{0,0,1,0,0},
			{0,0,0,0,1},
			{0,1,0,0,1},
			{0,1,1,1,0}
			
	};
	
	public boolean sm(int[][] paths, int x, int y, int i, int j){
		if(sm1(paths, x, y, i, j)){
			paths[x][y] = 5;
			return true;
		}
		return false;
	}
	
	public boolean sm1(int[][] paths, int x, int y, int i, int j){
		if(x == i && y == j){
			return true;
		}
		
		if(x >= paths.length || y >= paths[0].length){
			return false;
		}
		if(x < 0 || y < 0){
			return false;
		}
		if(paths[x][y] == 1 || paths[x][y] == 2){
			return false;
		}
		
		paths[x][y] = 2;
		if(sm1(paths, x+1, y, i, j)){
			return true;
		}
		if(sm1(paths, x-1, y, i, j)){
			return true;
		}
		if(sm1(paths, x, y+1, i, j)){
			return true;
		}
		if(sm1(paths, x, y-1, i, j)){
			return true;
		}
		paths[x][y] = 0;
		return false;
	}
	
	int counter = 0;
	public boolean solveMaze(int[][] paths, int x, int y, int i, int j){
		if(stepMaze(paths,x,y,i,j)){
			paths[x][y] = 5;
			return true;
		}
		return false;
	}	
	
	public boolean stepMaze(int[][] paths, int x, int y, int i, int j){
		counter++;
		if(x == i && y == j){
			return true;
		}
		
		if(x < 0 || x >= paths.length || y < 0 || y >= paths.length){
			return false;
		}
		
		if(paths[x][y] == 1 || paths[x][y] == 2){
			return false;
		}
		
		paths[x][y] = 2;
		if(stepMaze(paths, x, y+1, i, j)){
			return true;
		}
		
		if(stepMaze(paths, x-1, y, i, j)){
			return true;
		}
		
		if(stepMaze(paths, x+1, y, i, j)){
			return true;
		}
		
		if(stepMaze(paths, x, y-1, i, j)){
			return true;
		}
		
		paths[x][y] = 0;
		
		return false;
	}
	
	private void printFloyd(){
		int number = 1;
		for(int i = 1; i < 5; i++){			
			for(int j=1; j<=i ; j++){
				System.out.print(number++);				
			}
			System.out.println("");
			
		}
	}
	
	
	//FACTORIAL
	public HashSet<Integer> primeFactors(int number){
		HashSet<Integer> s = new HashSet<>();
		int copyOfNumber = number;
		for(int i=2 ; i<= copyOfNumber; i++){
			if(copyOfNumber % i == 0){
				s.add(i);
				copyOfNumber = copyOfNumber/i;
				i--;
			}
		}
		return s;
	}
	
	//permutation
	
	StringBuilder sb;
	boolean[] used;
	
	public void permutation(String s){
		sb = new StringBuilder(s.length());
		used = new boolean[s.length()];
	
		permute(s);
	}
	
	public void permute(String s){
		if(sb.length() == s.length()){
			System.out.println(sb.toString());
		}
		
		for(int i=0; i<s.length(); i++){
			if(used[i]){
				continue;
			}
			sb.append(s.charAt(i));
			used[i] = true;
			permute(s);
			used[i] = false;
			sb.setLength(sb.length() - 1);
		}
	}
	
	StringBuilder out = new StringBuilder();
	public void combine(String s, int start){
		for(int i=start ; i < s.length() ; i ++){
			out.append(s.charAt(i));
			System.out.println(out);
			if(i < s.length()){
				combine(s, i+1);
			}
			out.setLength(out.length() -1);
		}
	}
	
	//Greatest Common Multiplier Factor 
	public int GCF(int a, int b){
		if(b == 0){
			return a;
		}else{
			return GCF(b, a%b);
		}
	}

	//Lowest Common Multiplier Factor
	public int LCF(int a, int b){
		return (a * b) / GCF(a,b);
	}
	
	
	public void queenPlacement(int N){
		int[] columnsForRow = new int[N];
		boolean[] rows = new boolean[N];
		boolean[] columns = new boolean[N];
		int[][] board = new int[N][N];
		
		for(int i=0; i<board.length; i++){
			if(rows[i]){
				continue; // row already has queen
			}
			for(int j=0; j<board[0].length; j++){
				if((i == 0 && j == 0) || columns[j] || rows[i]){ // column already has queen, if both i & are same dont place
					continue;
				}
				if(checkDiagonal(i,j,board, columnsForRow)){
					rows[i] = true;
					columns[j] = true;
					columnsForRow[i] = j;
					board[i][j] = 1;
				}
			}
		}
		
		System.out.println(Arrays.deepToString(board));
	}
	private boolean checkDiagonal(int i, int j, int[][] board, int[] columnsForRows){ // j is the column we are at presently
		int k = i;
		int l = j;
		
		while(--k >= 0 && ++l < board.length){
			if(board[k][l] == 1){
				return false;
			}
		}
		
		k = i;
		l = j;
		
		while(--k >= 0 && --l >= 0){
			if(board[k][l] == 1){
				return false;
			}
		}
		
		return true;
	}
	
	//http://javaconceptoftheday.com/diamond-pattern-program-in-java/
	public void printDiomand(int rows){
		int mid = rows/2;
		int starcount = 1;
		for(int i = mid; i > 0; i--){
			
			//print space 
			for(int j = 1; j <= i; j++){
				System.out.print(" ");
			}
			
			//print star
			for(int j = 1; j <= starcount; j++){
				System.out.print("* ");
			}
			starcount++;
			System.out.println();
		}
		
		for(int i = 0; i <= mid; i++){
			//print space
			for(int j = 1; j <= i; j++){
				System.out.print(" ");
			}
			//print star
			for(int j = 1; j <= starcount; j++){
				System.out.print("* ");
			}
			starcount--;
			System.out.println();
		}
	}
	
	public boolean checkParenthesis(String a){
		char[] ca = a.toCharArray();
		
		HashMap<Character, Character> op = new HashMap<>();
		op.put('(', ')');
		op.put('{', '}');
		op.put('[', ']');
		
		HashMap<Character, Character> cp = new HashMap<>();
		cp.put(')', '(');
		cp.put('}', '{');
		cp.put(']', '[');
		
		Stack<Character> s = new Stack<>();
		for(int i = 0; i < ca.length; i++){
			if(op.containsKey(ca[i])){
				s.push(ca[i]);
			}else if(cp.containsKey(ca[i])){
				if(s.peek() != cp.get(ca[i])){
					return false;
				}else{
					s.pop();
				}
			}				
		}
		if(!s.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	//http://stackoverflow.com/questions/9368205/given-a-number-find-the-next-higher-number-which-has-the-exact-same-set-of-digi
	public void findNextHeighestInteger(int number){
		char[] sa = Integer.toString(number).toCharArray();
		int[] a = new int[sa.length];
		int[] ra = null;
		for(int i = 0; i < sa.length; i++){
			a[i] = sa[i] - '0';
		}
		boolean found = false;
		int pivot = -1;
		int pivotvalue = '0';
		for(int i = a.length -1; i>= 0; i--){
			if(a[i] - a[i-1] > 0){
				found = true;
				pivot = i-1;
				pivotvalue = a[i-1];
				break;
			}
		}
		if(found){
			int j = 0;
			ra = new int[a.length - pivot -1 ];
			int smallest = 10;
			int smallestLoc = -1;
			for(int i = pivot+1; i < a.length; i++){
				ra[j] = a[i];
				if(a[i] > pivotvalue){					
					if(a[i] < smallest){
						smallest = a[i];
						smallestLoc = j;
					}
					j++;
				}
			}
			
			int t = a[pivot];
			a[pivot] = ra[smallestLoc];
			ra[smallestLoc] =  t;
			Arrays.sort(ra);
			j = 0;
			for(int i = pivot+1; i < a.length; i++){
				a[i] = ra[j++];
			}
			System.out.println(ArrayUtils.toString(a));
		}
		
		
	}
	
	

	public static void main(String[] args) {
		StringArray1  sa = new StringArray1();
		sa.printDiomand(7);
		/*String s = "This string needs to be reversed";
		System.out.println(sa.reverseString(s));*/		
		/*sa.fib(17);		
		System.out.println("->"+ sa.fibR(5));*/
		/*System.out.println(sa.isAnangram("word", "wordd"));
		System.out.println(sa.isAnangram("word", "word"));*/
		
		/*System.out.println(sa.sumNumberRecursive(1234));
		System.out.println(sa.sumNumberIterative(1234));*/
		
		//sa.firstNonrepeatedChar("aabbccddeeffghhijjk");
		//sa.firstNonrepeatedCharIterative("aabbccddeeffghhijjk");
		//System.out.println(sa.stringInteger("6789"));
		//System.out.println(sa.integerString(-6789));
		
		//sa.insertPercentTwoZeroInString("s",0);
		//System.out.println(sa.stringCompress("aabcccccaaa"));
		
		int[][] data = 	{{15,20,40,85},
				 {0,35,80,95},
				 {30,55,95,105},
				 {40,80,100,120}
				};
		
		//sa.markRowOrColumnToZero(data);
		//System.out.println(ArrayUtils.toString(data));
		
		System.out.println(sa.solveMaze(paths, 0, 0, 1, 2));
		//System.out.println(sa.solveMaze(paths, 4, 4, 3, 4));
		
		//System.out.println(sa.solveMaze1(paths, 0, 0, 1, 3,0,0,true,true,true,true));
		//System.out.println(sa.solveMaze1(paths, 4, 4, 3, 4,true,true,true,true));
		
		int[] a = {1,2,3,4,5,6,7,8,9};
		sa.rotateArray(a, 8);
		System.out.println(Arrays.toString(a));
		
		
		
		//System.out.println( ( (char)0) ) ;
		
		//System.out.println(sa.solveMaze(paths, 0, 0, 4, 4));
		//System.out.println(Arrays.deepToString(paths));
		
		System.out.println(sa.sm(paths, 0, 0, 2, 4));
		System.out.println(Arrays.deepToString(paths));
		
		//sa.printFloyd();
		
		//System.out.println(Arrays.toString(sa.primeFactors(232321).toArray()));
		
		//sa.permutation("abcd");
		//sa.queenPlacement(8);
		//sa.combine("abcd", 0);
		
		System.out.println(sa.checkParenthesis("((({))))"));
		
		sa.findNextHeighestInteger(34722641);
		
	}
}
