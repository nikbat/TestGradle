package com.nick.mythreads;

import java.util.concurrent.Semaphore;

public class Foo {
	
	Semaphore s1;
	Semaphore s2;
	
	public Foo(){
		try{
			s1 = new Semaphore(1);
			s2 = new Semaphore(1);
			
			s1.acquire();
			s2.acquire();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void first() {
		try{
			System.out.println("First");
			s1.release();			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void second(){
		try{
			s1.acquire();
			s1.release();
			System.out.println("Second");
			s2.release();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void third(){
		try{
			s2.acquire();
			s2.release();
			System.out.println("Third");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
