package stack;

import java.util.Stack;

public class MyStack2 {

	public static void main(String[] args) {
		MyStack2 s = new MyStack2();
		sortStack();
		MyStack2.ss();

		//Tower of Hanoi
		Tower[] towers = new Tower[3];
		for(int i = 0; i < 3; i++){
			towers[0] = new Tower(i);
		}

		int disks = 5;
		for(int i = 0; i < disks; i++){
			towers[0].add(i);
		}

		towers[0].moveDisk(disks, towers[2], towers[1]);


	}

	private static void sortStack(){
		Stack<Integer> s1 = new Stack<>();
		s1.push(4);
		s1.push(5);
		//s1.push(5);
		s1.push(6);
		s1.push(7);
		s1.push(0);
		s1.push(1);
		s1.push(2);

		Stack<Integer> s2 = new Stack<>();

		while(!s1.empty()){
			int tmp = s1.pop();

			while(!s2.empty() && s2.peek() > tmp){
				s1.push(s2.pop());
			}
			s2.push(tmp);
		}
		System.out.println(s2);

	}

	static void ss(){
		Stack<Integer> s1 = new Stack<>();
		s1.push(4);
		s1.push(5);
		//s1.push(5);
		s1.push(6);
		s1.push(7);
		s1.push(0);
		s1.push(1);
		s1.push(2);

		Stack<Integer> s2 = new Stack<>();

		while(!s1.isEmpty()){
			int t = s1.pop();
			while(!s2.isEmpty() && s2.peek() > t){
				s1.push(s2.pop());
			}
			s2.push(t);
		}

		System.out.println(s2);
	}
}

class Tower{
	public Stack<Integer> disks;
	public int index;

	public Tower(int index){
		this.index = index;
		disks = new Stack<>();
	}

	public void add(int d){
		if(!disks.isEmpty() && disks.peek() <= d){
			//dont add
		}else{
			disks.push(d);
		}
	}

	public void moveToTop(Tower t){
		int d = disks.pop();
		t.add(d);
	}

	public void moveDisk(int n, Tower destination, Tower buffer){
		if(n > 0){
			moveDisk(n-1, buffer, destination);
			moveToTop(destination);
			buffer.moveDisk(n-1, destination, this);
		}
	}
}
