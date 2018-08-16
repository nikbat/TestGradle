package com.nick.com.nic.test;

import java.util.Scanner;

public class Range {
	
	public static void main(String[] args){
				
		boolean dataType[] = new boolean[4]; 
		
		Scanner s = new Scanner(System.in);	
		String lines = s.nextLine();
		int totalInputs = Integer.parseInt(lines);
		
		String[] inputs = new String[totalInputs];
		
		for(int i =0; i < totalInputs; i++){
			inputs[i] = s.nextLine();
		}
				
		
		
		for(String input : inputs){
			
			boolean fitted = false;			
						
			
			for(int i = 0; i < dataType.length; i++){
				dataType[i] = false;
			}
			
			
			try{
				Byte.parseByte(input);
				fitted = true;
				dataType[0] = true;
				
			}catch(Exception e){
				//ignore 
			}
			
			try{
				Short.parseShort(input);
				fitted = true;
				dataType[1] = true;
				
			}catch(Exception e){
				//ignore 
			}
			
			try{
				Integer.parseInt(input);
				fitted = true;
				dataType[2] = true;
				
			}catch(Exception e){
				//ignore 
			}
			
			try{
				Long.parseLong(input);
				fitted = true;
				dataType[3] = true;
				
			}catch(Exception e){
				//ignore 
			}
			
			if(!fitted){
				System.out.println(input + " can't be fitted anywhere.");
			}else{
				System.out.println(input +" can be fitted in:");
				if(dataType[0]){
					System.out.println("* byte");
				}
				if(dataType[1]){
					System.out.println("* short");
				}
				if(dataType[2]){
					System.out.println("* int");
				}
				if(dataType[3]){
					System.out.println("* long");
				}
				
			}
		}
	}
	

}
