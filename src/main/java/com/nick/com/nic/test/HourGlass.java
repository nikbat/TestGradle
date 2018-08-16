package com.nick.com.nic.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HourGlass {

	public static void main(String[] args) {
		List<Integer> hourGlassesSum = new ArrayList<Integer>();
		int[][] inputs = {
							{1, 1, 1, 0, 0, 0},
							{0, 1, 0, 0, 0, 0},
							{1, 1, 1, 0, 0, 0},
							{0, 0, 2, 4, 4, 0},
							{0, 0, 0, 2, 0, 0},
							{0, 0, 1, 2, 4, 0}				
						 };
		
		for(int i = 0; i <= inputs.length/2; i++){ //no hourglass after the 4
			
			for(int j = 0; j <= inputs[i].length/2; j++){
				int hourglass = 0;
				// get the sum of 3 row forward and 3 down words
				hourglass = hourglass + inputs[i][j]+inputs[i][j+1]+inputs[i][j+2];
				hourglass = hourglass + inputs[i+1][j]+inputs[i+1][j+1]+inputs[i+1][j+2];
				hourglass = hourglass + inputs[i+2][j]+inputs[i+2][j+1]+inputs[i+2][j+2];
				hourGlassesSum.add(hourglass);
			}
		}
		Collections.sort(hourGlassesSum);
		System.out.println(hourGlassesSum.get(hourGlassesSum.size()-1));
		
		
		
	}

}
