package com.nick.com.tech;

import java.util.Arrays;
import java.util.Scanner;

public class ShiftingForSomeReason {
	
	char[][] keys = {
						{'1','2','3','4','5','6','7','8','9','0'},
						{'q','w','e','r','t','y','u','i','o','p'},
						{'a','s','d','f','g','h','j','k','l',';'},
						{'z','x','c','v','b','n','m',',','.','/'},
	};
	
	int[] xPos = new int[256];
	int[] yPos = new int[256];
	
	int rows = keys[0].length;
	int columns = keys.length;
	
	//TODO Define Enum instead so that in future it is easy to extend class, I guess the focus of this execrise in not ENUM
	String HORIZONTAL = "H";
	String VERTICAL = "V";
	
	
	/*
	 * This method defines the x and y position of each char in a array
	 *  so that we can get the position in O(1) time. 
	 */
	private void intitialize() throws Exception{
		
		//first fill the array with -1
		Arrays.fill(xPos, -1);
		Arrays.fill(yPos, -1);
		
		for(int i=0; i < keys.length; i++){
			for(int j =0; j < keys[i].length;j++){
				 xPos[(int)keys[i][j]] = j;
				 yPos[(int)keys[i][j]] = i;
			}
		}
	}
	
	
	/*
	 * Following code does a shift
	 * TODO Revisit this logic again, I dont think I need to check if shift is less than 0  
	 */
	private char doShift(char c, int shift){
		
		if( !((int)c > 255 || xPos[(int)c] == -1 || yPos[(int)c] == -1) ){
			int x = xPos[(int)c];
			int y = yPos[(int)c];
			
			int newX = 0;
			int newY = 0;
			
			if(shift < 0){				
				int shiftPossitive = shift*-1;
				newX = ( x +  (rows - (shiftPossitive % rows) )) % rows;
				
				if(shiftPossitive > 10){				
					newY = (columns + ( y - ( (shiftPossitive / rows) + ( ( ( (shiftPossitive % rows) ) + newX ) / rows) ) ) ) % columns;
					
				}else{
					newY = (columns + (y - ((newX+shiftPossitive) / rows) ) ) % columns;  
				}
				
				if(newY < 0){
					newY = newY *-1;
				}
				
			}else if(shift > 0){
				newX = (x + shift) % rows;
				
				if(shift > 10){				
					newY = ( (shift / rows) + ( ( ( (shift % rows) ) + x ) / 10) + y  ) % columns;
				}else{
					newY = (((x+shift) / rows) + y) % columns;  
				}
				
			}
			return keys[newY][newX];
			
		}
		
		return c;
	}
	
	
	/*
	 * Following code does a horizontal shift
	 */
	private char doHorizontalShift(char c) {
		if( !((int)c > 255 || xPos[(int)c] == -1 || yPos[(int)c] == -1) ){			
			
			//find y position, since this is a horizontal shift Y position will be constant 
			int y = yPos[(int)c];
			
			//find xPos of the letter and substract that by total rows
			int position = rows - xPos[(int)c];;
			
			return keys[y][position-1];
		}
		return c;
	}
	
	/*
	 * Following code does a vertical shift
	 */
	private char doVerticalShift(char c) {
		if( !((int)c > 255 || xPos[(int)c] == -1 || yPos[(int)c] == -1) ){			
			
			//find x position, since this is a horizontal shift X position will be constant 
			int x = xPos[(int)c];
			
			//find yPos of the letter and substract that by total columns
			int position = columns - yPos[(int)c];;
			
			return keys[position-1][x];
		}
		return c;
	}
	
	
	private void doTask() throws Exception{

		//Initialize the position of each char, so that we dont have to keep looking again		
		intitialize();		
		
		Scanner scan =new Scanner(System.in);
		
		System.out.println("Please Enter the Transformation Text sperated by comma e.g 5,H");
		System.out.println("\n");
		String transform = scan.nextLine();		
		String[] tInput = validateTransformationText(transform);
		
		
		System.out.println("Please enter your Input Text");
		System.out.println("\n");
		String tText = scan.nextLine();
		
		//If I am reading this input from a file, change the logic using buffered reader and read one line at a time
		//Second option is read the content of the file using random access file using seek, read each char and replace the char in same file.
		
		StringBuilder outputText = new StringBuilder();
		for(int i=0; i < tText.length(); i++){			
			outputText.append(transform(tText.charAt(i), tInput));
		}
		
		System.out.println("Output Text:"+outputText.toString());
		
	}
	
	/*
	 * This code needs a improvement, I dont have to check equals again and again
	 */
	private char transform(char c, String[] tInput) throws Exception{
		
		for(String tranString : tInput){
			if(tranString.equals(HORIZONTAL)){
				c = doHorizontalShift(c);
			}else if(tranString.equals(VERTICAL)){
				c = doVerticalShift(c);
			}else{
				c = doShift(c, Integer.parseInt(tranString));
			}
		}
		return c;
		
	}
	
	/*
	 * This code is validing the transformation text, Caould have improved it but I guess focus is on logic
	 */
	private String[] validateTransformationText(String transform) throws Exception{
		if(transform == null || transform.length() <= 0)
			throw new Exception("Please enter a valid input as defined in the problem");
		String[] inputs = null;
		if(transform.indexOf(',') > 0 ){
			inputs = transform.split(",");
			for(String s : inputs){
				if(s.equals(HORIZONTAL) || s.equals(VERTICAL)){
					continue;
				}else{
					try{
						Integer.parseInt(s);
					}catch(Exception e){
						throw new Exception("Please enter a valid input as defined in the problem");
						
					}
				}
			}			
		}else{
			inputs = new String[1];
			inputs[0] = transform;
		}
		return inputs;
		
	}
	
	public static void main(String[] args){
		
		try{
			new ShiftingForSomeReason().doTask();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	
	
	 
}
