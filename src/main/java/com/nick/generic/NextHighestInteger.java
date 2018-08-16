package com.nick.generic;

import java.util.Arrays;

public class NextHighestInteger {

	public static void main(String[] args) {
		int number = 32876;
		int nextHighestNumber = getNextHighestNumber(number);
		System.out.println(nextHighestNumber);
	}
	/*
	 * Second(Implemented) solution is: Try checking from right, if you find the left digit
	 * is smaller than the right digit, stop there- swap those numbers and then
	 * sort the number on the right of there.
	 */
	private static int getNextHighestNumber(int number) {
		int temp=-1;
		int[] tempArray;

		int[] numberArray = new int[Integer.toString(number).length()];
		// Convert a number to number Array
		for (int i = numberArray.length - 1; i >= 0; i--) {
			numberArray[i] = Integer.toString(number).charAt(i) - '0';
		}
		int size = numberArray.length - 1;
		boolean found = false;
		for (int i = size; i >= 0; i--) {
			if (numberArray[i] > numberArray[i - 1]) {
				temp = numberArray[i - 1];
				numberArray[i - 1] = numberArray[size];
				found = true;
			}
			if (found) {
				tempArray = new int[size - i + 1];
				tempArray[0]=temp;
				for (int j = i, k = 1; k < tempArray.length; k++, j++) {
					tempArray[k] = numberArray[j];
				}
				Arrays.sort(tempArray);
				for(int k=0;i<size+1;i++,k++){
					numberArray[i]=tempArray[k];
				}
				break;
			}
		}

		//convert a number array to number
		int nextHighestNumber = 0;
		int multiplier=1;
		for(int i=numberArray.length-1;i>=0;i--){
			nextHighestNumber+=(numberArray[i]*multiplier);
			multiplier*=10;
		}
		return nextHighestNumber;
	}

	/*
	 * First solution is: start from the right and get the remainder of the
	 * input number. Check all possible combinations from the remainder and if
	 * you find the number which is greater than the remainder, put that number
	 * back on the position of the remainder and your next highest integer is
	 * ready.
	 */

	
	
}
