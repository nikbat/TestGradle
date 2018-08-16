package com.nick.generic;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ReverseString {

	public static void main(String[] args) {		
		int number = 255;  
		new ReverseString().reverseNumber(number);
		System.out.println(new ReverseString().reverseString("abcde"));
		System.out.printf("Amount : %08d %n" , 221);
		System.out.printf("positive number : +%d %n", 1534632142);
		System.out.printf("negative number : -%d %n", 989899);
		System.out.printf("Today is %tD", new Date());
		Date today = new Date();
        System.out.printf("Date in dd/mm/yy format %td/%tm/%ty %n", today,today,today );
        System.out.printf("Today is %tB %te, %tY %n", today,today,today,today);
        
        CountDownLatch latch = new CountDownLatch(4);
        CyclicBarrier barrier = new CyclicBarrier(4);
        
          


	}
	
	public void reverseNumber(int number){
		
		int replace = 0;
		
		while(number > 0){
			int temp = number%10;
			replace = replace*10 + temp;
			number = number/10;
		}	
	}
	
	public String reverseString(String data){
		if(data.length() < 2){
			return data;
		}
		
		return reverseString(data.substring(1)) +data.charAt(0); 
	}
	

}
