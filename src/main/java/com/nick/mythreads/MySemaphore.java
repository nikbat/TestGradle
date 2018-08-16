package com.nick.mythreads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;


public class MySemaphore {
	
	BlockingQueue<Integer> mylist = new ArrayBlockingQueue<Integer>(2);
	Semaphore s = new Semaphore(1);
			
	public static void main(String[] args) throws Exception{
		
		new MySemaphore().doTask();

	}
	
	public void doTask() throws Exception{
		System.out.println(mylist.poll()); // take waits for data to be available in queue
		MyAdd a = new MyAdd(mylist,s);
		MyAdd b = new MyAdd(mylist,s);
		a.start();
		b.start();
		Thread.sleep(5000);
		a.join();
		b.join();
		System.out.println(mylist.poll());
		System.out.println(mylist.poll());
		
	}
}

class MyAdd extends Thread{
	
	BlockingQueue<Integer> mylist = null;
	Semaphore s = null;
	
	MyAdd(BlockingQueue<Integer> mylist, Semaphore s){
		this.mylist = mylist;
		this.s = s;
	}
	
	@Override
	public void run(){
		try{
			System.out.println(mylist.poll());
			System.out.println("Acquire");
			s.acquire();
			System.out.println("Adding");
			mylist.add(1);			
			Thread.sleep(10000);
			System.out.println("releasing");
			s.release();
			System.out.println("released");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
