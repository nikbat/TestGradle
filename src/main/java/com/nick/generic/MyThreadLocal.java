package com.nick.generic;

import java.util.concurrent.CountDownLatch;


public class MyThreadLocal {

	public static volatile String temp ;
	
	public static void main(String[] args) throws Exception{
		CountDownLatch latch = new CountDownLatch(2);
		Thread t1 = new Thread(new Task("A",latch));
		Thread t2 = new Thread(new Task("B",latch));
		t1.start();
		t2.start();
	}
	
	public static String someMethod(String data){
		temp = data;
		ThreadLocal<String> threadLocal = new ThreadLocal<String>();
		threadLocal.set(data);
		return threadLocal.get();
	}
}

class Task implements Runnable{
	String name;
	CountDownLatch latch;
	public Task(){
		
	}
	public Task(String name, CountDownLatch latch){
		this.name = name;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try{
			String localName = MyThreadLocal.someMethod("Local "+name);
			System.out.println("Thread "+name + " Temp"+MyThreadLocal.temp +" localName"+localName);
			latch.countDown();
			System.out.println("Done waiting Thread "+name + " Temp"+MyThreadLocal.temp +" localName"+localName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


