package com.nick.com.nic.test;

public class MaxDifferince {

	public static void main(String[] args) {

		int[] array1 = {2,3,10,2,4,8,1};
		int[] array2 = {7,9,5,6,3,2};
		
		System.out.println(maxDifference(array1));
		//System.out.println(maxDifference(array2));
	}
	
	public static int maxDifference(int[] a){
		
		if(a.length <= 0 ){
			return -1;
		}
		
		//int min = Integer.MAX_VALUE;
		//int max = Integer.MIN_VALUE;
		int min = 1000000;
		int max = 0;
		int minIndex = -1;
		int maxIndex = -1;
		
		
		//find the max number and min number in an array
		
		for(int i = 0; i < a.length; i++){
			if(a[i] < min ){
				min = a[i];
				minIndex = i;
			}
			
			if(a[i] > max  ){
				max = a[i];
				maxIndex = i;
			}
		}	
	
		//check if maxIndex is greater than minIndex
		if(maxIndex > minIndex){
			return a[maxIndex] - a[minIndex];
		}else if (maxIndex < minIndex){
			//start for 0 position till maxIndex and check if any number is less than max
			minIndex = 0;
			for(int i = 0; i < maxIndex; i++){
				if(a[i] < a[maxIndex] && a[minIndex] > a[i]){
					minIndex = i;
				}
			}
			return a[maxIndex] - a[minIndex];
		}else{
			return -1;
		}
	
		
		
	}

}
