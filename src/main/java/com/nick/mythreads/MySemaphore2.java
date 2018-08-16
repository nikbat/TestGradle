package com.nick.mythreads;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;


public class MySemaphore2 {
	
	//BlockingQueue<Integer> mylist = new LinkedBlockingQueue<Integer>(2);
	LinkedList<Integer> mylist = new LinkedList<Integer>();
	Semaphore s = new Semaphore(1,true);
			
	public static void main(String[] args) throws Exception{
		
		new MySemaphore2().doTask();

	}
	
	public void doTask() throws Exception{
		//System.out.println(mylist.poll()); // take waits for data to be available in queue
		MyProducer a = new MyProducer(mylist,s);
		MyConsumer b = new MyConsumer(mylist,s);
		a.start();
		b.start();
		
		a.join();
		b.join();
		
		
		
		
	}
}

class MyProducer extends Thread{
	
	//BlockingQueue<Integer> mylist = null;
	LinkedList<Integer> mylist;
	Semaphore s = null;
	
	MyProducer(LinkedList<Integer> mylist, Semaphore s){
		this.mylist = mylist;
		this.s = s;
	}
	
	@Override
	public void run(){
		try{
			for(int i=0; i <10; i++){
				s.acquire();
				System.out.println("Adding "+(i+1));
				mylist.add(i+1);				
				s.release();	
				Thread.sleep(1000);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


class MyConsumer extends Thread{
	
	//BlockingQueue<Integer> mylist = null;
	LinkedList<Integer> mylist;
	Semaphore s = null;
	
	MyConsumer(LinkedList<Integer> mylist, Semaphore s){
		this.mylist = mylist;
		this.s = s;
	}
	
	@Override
	public void run(){
		try{
			while(true){
				s.acquire();
				if(!mylist.isEmpty()){
					System.out.println("Getting "+mylist.poll());				
					
				}
				s.release();
				Thread.sleep(1000);
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
