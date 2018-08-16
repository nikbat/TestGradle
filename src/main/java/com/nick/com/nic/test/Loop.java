package com.nick.com.nic.test;

import java.util.Scanner;

public class Loop {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);	
		String totalNumberOflinesStr = s.nextLine();
		
		int totalNumberOflines = Integer.parseInt(totalNumberOflinesStr);
		
		String[] lineInputs = new String[totalNumberOflines];
		
		for(int i =0; i < totalNumberOflines; i++){
			lineInputs[i] = s.nextLine();
		}
		
		s.close();
		
		for(String lineInput : lineInputs){
			String[] input = lineInput.split(" ");
			int a = Integer.parseInt(input[0]);
			int b = Integer.parseInt(input[1]);
			int n = Integer.parseInt(input[2]);
		
			//System.out.print(a);
			//System.out.print(b);
			//System.out.print(n);
			//System.out.println("");
			
			/*int a = 5;
			int b = 3;
			int n = 5;*/
			
			int output = 0;
			for(int i = 0; i< n; i++){
				if(i == 0){
					output = (int)(a+Math.pow(2, i)*b);
					System.out.print(output);
					continue;
				}
				System.out.print(" ");
				int temp = (int)( (Math.pow(2, i)) *b );
				output = output + temp;
				System.out.print(output);
				
			}
			System.out.println();
		}

	}

}
