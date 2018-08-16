package com.nick.generic;

import java.util.concurrent.CountDownLatch;


public class MyThread implements Runnable {
	
	CountDownLatch latch;
	String name;		
	
	public MyThread(String name,CountDownLatch latch){
		this.name = name;
		this.latch = latch;
	}
	
	@Override
	public void run(){
		try{
			System.out.println("Initializing Thread with name "+ name);
			Thread.sleep(10000);
			latch.countDown();
			System.out.println("Ready Thread with name "+ name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
