package com.nick.com.nic.test;

public class OneDArrayTest {
	
	public static void main(String[] args){
		
		//I am assmung first element is jump factor in my second dimension array is m, therefore my position will start from 1 
		int[][] inputs = { 
					{3,0,0,0,0,0},
					{5,0,0,0,1,1,1},
					{3,0,0,1,1,1,0},
					{1,0,1,0}
				  };
		
		
		
		for(int i = 0; i <inputs.length; i++){
			
			//System.out.println(inputs[i].length);
			int k = 1;
			int jump = inputs[i][0];

			while(true){
				if(k >= inputs[i].length-1){ 
					System.out.println("YES");
					break;
				}
				
				if(inputs[i][k+1] == 0){
					k++;
					continue;
				}else
					// Jump
					if(k+jump >= inputs[i].length-1){
						System.out.println("YES");
						break;
					}else{
						if (k-1 >= 1 && inputs[i][k-1] == 0 && ((k-1)+(jump)) >= inputs[i].length-1){
							System.out.println("YES");
							break;
						}else{
							System.out.println("NO");
							break;
					}
						
				}
			}
		}
		
	}

}
