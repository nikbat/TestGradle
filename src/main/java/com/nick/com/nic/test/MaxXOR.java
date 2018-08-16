package com.nick.com.nic.test;

public class MaxXOR {

	public static void main(String[] args) {
		
		int max = Integer.MIN_VALUE;
		for (int i =10 ;i<= 15; i++){
			for(int j = 10; j<=15; j++){
				max = Math.max(max, i^j);
			}
		}
		
		System.out.println(max);

	}

}
