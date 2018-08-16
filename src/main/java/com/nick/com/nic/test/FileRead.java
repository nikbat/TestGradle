package com.nick.com.nic.test;

import java.util.Scanner;

public class FileRead {

	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		int i = 1;
		String line = "";
		while(s.hasNext()){
			line = s.nextLine();
			if(line == null || line.trim().length() <= 0){
				s.close();
				break;
			}
			System.out.println(i+++" "+line);
		}

	}

}
