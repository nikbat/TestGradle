package com.nick.com.nic.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Difference {
	
	public static void main(String[] args){
		try{
			Map<Integer, String> nipunTestNumberMap = new HashMap<Integer, String>();
			Scanner s = new Scanner(System.in);
			int N = s.nextInt();
			int K = s.nextInt();
			s.nextLine();
			for(int i = 0; i < N; i++){
				nipunTestNumberMap.put(s.nextInt(), "");				
			}
			
			int count = 0;
			for(int number : nipunTestNumberMap.keySet()){
				int find = number-K;
				if(nipunTestNumberMap.containsKey(find)){
					count++;
				}
			}
			System.out.println(count);
			s.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
