package com.nick.mythreads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyBQ {
 
	BlockingQueue<Integer> data = new LinkedBlockingQueue<Integer>(1);
	
	public static void main(String[] args) throws Exception{
		new MyBQ().doTask();
	}
	
	private void doTask()  throws Exception{

		Producer p = new Producer(data);
		Consumer c = new Consumer(data);

		Thread pt = new Thread(p);
		Thread pc = new Thread(c);
		
		pt.start();
		pc.start();
		
		pt.join();
		pc.join();
		
	}

	class Producer implements Runnable {

		BlockingQueue<Integer> data;

		public Producer(BlockingQueue<Integer> data){
			this.data = data;
		}
		
		@Override
		public void run() {
			try{
				for(int i = 0; i<10; i++){
					System.out.println("P " +i);
					data.add(i);
					Thread.sleep(1000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	class Consumer implements Runnable {
		
		
		BlockingQueue<Integer> data;
		
		
		public Consumer(BlockingQueue<Integer> data){
			this.data = data;
		}
		
		@Override
		public void run() {
			try{
				while(true){					
					if(!data.isEmpty()){
						int i = data.poll();
						System.out.println("C "+i);
						if(i == 9){
							break;
						}
					}
					
					Thread.sleep(1000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
}