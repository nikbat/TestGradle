package com.nick.mythreads;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class MySemaphore1 {

	public static void main(String[] args) throws Exception {
		new MySemaphore1().performTask();
	}
	
	private void performTask() throws Exception{
		LinkedList<Integer> ll = new LinkedList<>();
		Semaphore s = new Semaphore(1);
		Producer p = new Producer(ll,s);
		Consumer c = new Consumer(ll,s);
		p.start();
		c.start();
		p.join();
		c.join();
		
	}
	
	class Producer extends Thread{
		LinkedList<Integer> ll;
		Semaphore s;;
		public Producer(LinkedList ll, Semaphore s){
			this.ll = ll;
			this.s = s;
		}
		public void run(){
			try{
				for(int i =0; i < 10; i++){
					s.acquire();
					System.out.println("Adding "+ i);
					ll.push(i);
					s.release();
					Thread.sleep(1000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	class Consumer extends Thread{
		LinkedList<Integer> ll;
		Semaphore s;;
		public Consumer(LinkedList ll, Semaphore s){
			this.ll = ll;
			this.s = s;
		}
		public void run(){
			try{
				while(true){
					s.acquire();					
					Integer i = ll.poll();
					if(i != null){
						System.out.println("Getting "+ i);
						if( i.intValue() > 9){
							break;
						}
					}
					s.release();
					Thread.sleep(1000);
					
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
