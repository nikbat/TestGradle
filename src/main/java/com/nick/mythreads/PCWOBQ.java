package com.nick.mythreads;

import java.util.concurrent.Semaphore;


public class PCWOBQ {
	
	
	Semaphore s = new Semaphore(1); 
			
	public static void main(String[] args) throws Exception{
		
		new PCWOBQ().doTask();

	}
	
	public void doTask() throws Exception{
		//System.out.println(mylist.poll()); // take waits for data to be available in queue
		IntBuffer buffer = new IntBuffer();
		MyProducer1 a = new MyProducer1(buffer);
		MyConsumer1 b = new MyConsumer1(buffer);
		a.start();
		b.start();		
		a.join();
		b.join();
	}
	
	public class IntBuffer{
		int index = 0; 
		int[] buffer = new int[8];
		
		private synchronized void add(int number){
			
				while(index == buffer.length-1){
					try{
						wait();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				buffer[index++] = number;
				notifyAll();
			
		
		}
		
		private synchronized int get(){
			while(index == 0){
				try{
					wait();
				}catch(Exception e){
					e.printStackTrace();
				}
			}	
			int data = buffer[--index];
			notifyAll();
			return data;
		}
		
	}
	
	class MyProducer1 extends Thread{
		
		//BlockingQueue<Integer> mylist = null;
		Semaphore s = null;
		IntBuffer buffer;
		int number = 10;
		
		MyProducer1(IntBuffer buffer){
			this.buffer =buffer;
		}
		
		@Override
		public void run(){
			try{
				while(true){
					System.out.println("Adding "+number );
					buffer.add(number++);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	class MyConsumer1 extends Thread{
		
		//BlockingQueue<Integer> mylist = null;
		IntBuffer buffer;
		Semaphore s = null;
		
		MyConsumer1(IntBuffer buffer){
			this.buffer = buffer;
		}
		
		@Override
		public void run(){
			try{
				while(true){
					System.out.println("Getting "+buffer.get());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}


}



