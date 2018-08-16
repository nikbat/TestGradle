package com.nick.mythreads;

public class CallFooInOrder {
	
	public static void main(String[] args){
		Foo f = new Foo();
		Thread t1 = new Thread(){
			public void run(){
				f.first();
			}
		};
		
		Thread t2 = new Thread(){
			public void run(){
				f.second();
			}
		};
		
		Thread t3 = new Thread(){
			public void run(){
				f.third();
			}
		};
		
		t1.start();
		t2.start();
		t3.start();
	}

}
