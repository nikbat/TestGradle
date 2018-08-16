package com.nick.generic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Singleton {
	
	private static Singleton INSTANCE = null;
	
	private Singleton(){
		
	}
	
	public static Singleton getInstastance(){
		if(INSTANCE == null){
			synchronized(Singleton.class){
				if(INSTANCE != null){
					INSTANCE = new Singleton();
				}
			}
		}
		return INSTANCE;
	}
	
	
	
	
	public static void main(String[] args) throws Exception{
		CountDownLatch latch = new CountDownLatch(2);		
		
		/*MyThread t1 = new MyThread("T1", latch);
		MyThread t2 = new MyThread("T2", latch);
		
		t1.start();
		t2.start();
		
		latch.await();*/
		
		ExecutorService service = Executors.newFixedThreadPool(2);
		Runnable t1 = new MyThread("T1", latch);
		Runnable t2 = new MyThread("T2", latch);
		Runnable t3 = new MyThread("T3", latch);
		Runnable t4 = new MyThread("T4", latch);
		service.execute(t1);
		service.execute(t2);
		service.execute(t3);
		service.execute(t4);
		
		latch.await();
		System.out.println("Now I am ready to work");
		
		
		
	}
}
