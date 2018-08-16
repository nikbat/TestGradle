package com.nick.generic;

import java.util.ArrayList;
import java.util.List;


public class WaitThread{
	
	
	volatile boolean go = false;
	
	public static void main(String[] args) throws Exception{
		new WaitThread().performTask();
	}
	
	public void performTask() throws Exception{
		Thread t1 = new Thread(new WaitThread2("T1"));
		Thread t2 = new Thread(new WaitThread2("T2"));
		Thread t3 = new Thread(new WaitThread2("T3"));
		Thread t4 = new Thread(new WaitThread2("T4"));
		Thread n1 = new Thread(new NotifyThread("N1"));
		t1.start();
		//t2.start();
		//t3.start();
		//t4.start();
		Thread.sleep(200);		
		n1.start();
		
		List<?> ol;
		List<String> sl = new ArrayList<String>();
		ol = sl;
	}
	
	class WaitThread2 implements Runnable {
		
		String name;
		
		public WaitThread2(String name){
			this.name = name;			
		}
		
		@Override
		public void run(){
			try{
				System.out.println("Initializing Thread with name "+ name);
				doTask();			
				System.out.println("Ready Thread with name "+ name);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		private synchronized void doTask() throws Exception{
			while(!go){
				System.out.println("Waiting "+name);
				wait();
				System.out.println("Woken up "+name);
				go = true;
			}
			
		}
	}
	
	class NotifyThread implements Runnable {
		
		String name;
		
		public NotifyThread(String name){
			this.name = name;			
		}
		
		@Override
		public void run(){
			try{
				go = true;
				doTask();			
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		private synchronized void doTask() throws Exception{
			while(go){
				System.out.println("Notify "+ name);
				notifyAll();
				go = false;
				System.out.println("Done Notify "+go);
			}
		}


	}	
	
	class Wrapper<T>{
		private T item;
		
		public T getItem(){
			return item;
		}
		
		public void setItem(T item){
			this.item = item;
		}
	}
	
	class MapEntry<K,V>{
		
		private K key;
		private V value;
		private MapEntry<K, V> next;
		
		public void setKey(K key) {
			this.key = key;
		}
		
		public K getKey() {
			return key;
		}
		
		public void setValue(V value) {
			this.value = value;
		}
		
		public V getValue() {
			return value;
		}
		
		public MapEntry<K, V> getNext() {
			return next;
		}
		
		public void setNext(MapEntry<K, V> next) {
			this.next = next;
		}
	}
}


	