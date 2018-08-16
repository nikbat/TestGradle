package com.nick.mythreads;

import java.util.concurrent.*;

public class MyCountDownLatch {

	public static void main(String[] args) {
		try{
			new MyCountDownLatch().doTask();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void doTask() throws Exception{
		CountDownLatch latch = new CountDownLatch(4);
		ExecutorService s = Executors.newFixedThreadPool(4);
		Future<String> f1 = s.submit(new Service("Service1", latch));
		Future<String> f2 = s.submit(new Service("Service2", latch));
		Future<String> f3 = s.submit(new Service("Service3", latch));
		Future<String> f4 = s.submit(new Service("Service4", latch));
		//latch.await(1, TimeUnit.SECONDS);
		System.out.println("waiting");
		latch.await();
		System.out.println(f1.get());
		System.out.println(f2.get());
		System.out.println(f3.get());
		System.out.println(f4.get());
		System.out.println("All services is up lets start ");
		s.shutdown();
		
		
		
		
	}
	
	class Service implements Callable<String> {
		String name;
		CountDownLatch latch;
		public Service(String name,CountDownLatch latch ){
			this.latch = latch;
			this.name = name;			
		}
		@Override
		public String call(){
			try{
				System.out.println("First thread done");
				Thread.sleep(2000);
				latch.countDown();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return name+" service is up now";
		}
	}

}
