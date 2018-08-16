package com.nick.mythreads;


import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class MySemaphore0{
	Semaphore s = new Semaphore(1); 
	LinkedList<Integer> data = new LinkedList<Integer>();
	
	public static void main(String[] args) throws Exception{
		new MySemaphore0().doTask();
	}
	
	private void doTask()  throws Exception{
		
		MyThreadP p = new MyThreadP(s, data);
		MyThreadC c = new MyThreadC(s, data);
		Thread pt = new Thread(p);
		Thread pc = new Thread(c);
		
		pt.start();
		pc.start();
		
		pt.join();
		pc.join();
		
	}

	


	class MyThreadP implements Runnable {
		
		Semaphore s;
		LinkedList<Integer> data = new LinkedList<Integer>();
		
		
		public MyThreadP(Semaphore s, LinkedList<Integer> data ){
			this.s = s;
			this.data = data;
		}
		
		@Override
		public void run() {
			try{
				for(int i = 0; i<10; i++){
					System.out.println(i);
					s.acquire();
					data.push(i);
					s.release();
					Thread.sleep(1000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	class MyThreadC implements Runnable {
		
		Semaphore s;
		LinkedList<Integer> data = new LinkedList<Integer>();
		
		
		public MyThreadC(Semaphore s, LinkedList<Integer> data ){
			this.s = s;
			this.data = data;
		}
		
		@Override
		public void run() {
			try{
				while(true){
					s.acquire();
					if(!data.isEmpty()){
						int i = data.poll();
						System.out.println(i);
						if(i == 9){
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