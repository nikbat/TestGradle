package com.nick.generic;

import java.util.Scanner;


public class ScannerExample {
	
	public static void main(String[] args){
		System.out.println("InputNumber");
		Scanner scan = new Scanner(System.in);

		/*int number = scan.nextInt();
		System.out.println(number);
		
		for(int i = 1; i<=number; i++){
			if(i%3 == 0 && i%5 == 0){
				System.out.println("FizzBuzz");				
			}else if(i%3 == 0){
				System.out.println("Fizz");
			}else if(i%5 == 0){
				System.out.println("Buzz");
			}else{
				System.out.println(i);
			}
		}*/
		
		//System.out.println(Math.pow(10, 5));
		
		// difference is k
		
		/*Scanner scan = new Scanner(System.in);
		String line = "5 2";//scan.nextLine();
		String[] line1 = line.split(" ");
		int n = Integer.parseInt(line1[0]);
		int k = Integer.parseInt(line1[1]);
		
		line = "1 5 3 4 2";//scan.nextLine();
		List<Integer> list = new ArrayList<Integer>();  
		String[] line2 = line.split(" ");
		for(String a : line2){
			list.add(Integer.parseInt(a));
		}
		
		Collections.sort(list);
		int count = 0;
		
		System.out.println(list.get(n-1));
		
		if(list.get(n-1).intValue() - list.get(0).intValue() < k){
			System.out.println(count);
			return;
		}
		
		
		for(int i=n-1; i >=0; i--){
			
			for(int j = i-1; j >=0; j --){
				if( list.get(i).intValue() - list.get(j).intValue() > k)
			           break;
			           else if((list.get(i).intValue() - list.get(j).intValue())==k)
			           count++;
			}
		}
		
		
		System.out.println(count);*/
		
		/*System.out.println((int)'a' + "," + (int)'z');
		
		int[] arr1 = new int[256];
		//int[] arr2 = new int[256];
		
		String A = "turtle";
		String B = "tletua";
		
		char[] Ac = A.toCharArray();
		char[] Bc = B.toCharArray();
		
		if(Ac.length != Bc.length){
			System.out.println(false);			
		}
		
		for(int i=0; i<Ac.length; i++){
			char c1 = Ac[i];
			char c2 = Bc[i];
			if(c1 == c2){
				continue;
			}else{
				//check the mapping 
				System.out.println((int)c1);
				System.out.println((int)c2);
				System.out.println(arr1[c1]);
				System.out.println(arr1[c2]);
				if(arr1[c1] == 0 && arr1[c2] == 0){
					// for now put arr1
					arr1[c1] = c2;
					arr1[c2] = c1;
				}else if(arr1[c1] != c2 && arr1[c2] != c1){
					System.out.println("Not equal");
					break;					
				}
			}
		}
			
		System.out.println("Equal");*/
		
		/*ScannerExample e = new ScannerExample();
		for(int i=1;i<20;i++){
			System.out.println(e.fibn(i));
		}*/
		
		char a = 'a';
		System.out.println(Character.isLetter(a));
		
		
	}
	
	public int fibn(int n){
		if(n == 1){
			return 1;
		}else if(n ==2){
			return 1;
		}else{
			return (fibn(n-1) + fibn(n-2));
		}
		
	}

}
