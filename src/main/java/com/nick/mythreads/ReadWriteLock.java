package com.nick.mythreads;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {
	
	HashMap<Integer, Integer> t = new HashMap<>();
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	
	public void write() {
		try{
			lock.writeLock().lock();
			System.out.println("Start Writing " + LocalDateTime.now().getSecond());
			t.put(1, 1);
			Thread.sleep(10*1000);
			System.out.println("Done Writing " + LocalDateTime.now().getSecond());
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public void read() {
		try{
			System.out.println("Thread " + Thread.currentThread().getName() + " trying to read");
			lock.readLock().lock();
			System.out.println("Start Reading " + LocalDateTime.now().getSecond());
			t.get(1);
			Thread.sleep(1*1000);
			System.out.println("Done Reading " + LocalDateTime.now().getSecond());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			lock.readLock().unlock();
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		ReadWriteLock rw = new ReadWriteLock();
		Thread t1 = new Thread( () -> {
			int i = 0;
			while(i < 10){
				rw.read();
				i++;
			}
		}, "T1" );
		
		Thread t2 = new Thread( () -> {
			int i = 0;
			while(i < 5){
				rw.write();
				i++;
			}
		} , "T2");
		
		Thread t3 = new Thread( () -> {
			int i = 0;
			while(i < 10){
				rw.read();
				i++;
			}
		} , "T3");
		
		Thread t4 = new Thread( () -> {
			int i = 0;
			while(i < 10){
				rw.read();
				i++;
			}
		} , "T4");
		
		
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
	}

}
