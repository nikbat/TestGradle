package com.nick.com.nic.test;

import java.util.Scanner;

public class FizBuzz {

	public static void main(String[] args) {
		try{
			Scanner s = new Scanner(System.in);
			int input = Integer.parseInt(s.next());
			
			for(int i=1;i<=input;i++){
				if( i % 15 == 0){
					System.out.println("FizzBuzz");
				}else if (i % 5 == 0){
					System.out.println("Buzz");
				}else if(i % 3 == 0){
					System.out.println("Fizz");
				}else{
					System.out.println(i);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
