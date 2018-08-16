package com.nick.sa;


import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

//import org.apache.commons.lang.ArrayUtils; 

public class Sort2 {

	public static void main(String[] args) {
		Sort2 so = new Sort2();
		
		//int[] data = {1,2,3,5,6,7,9,10};
		//System.out.println(so.bsbs(data, 4, 0, data.length));
		
		//so.findNextHighestInteger();
		
		/*int[] array = { 6, 4, 9, 5, 1, 3, 2, 7, 8, 10 };
		int[] data = so.mergeSort(array);
		//System.out.println(ArrayUtils.toString(data));
		int[] array2 = { 6, 4, 9, 5, 1, 3, 2, 7, 8, 10 };
		int[] data2 = so.quickSort(array2);
		//System.out.println("q " + ArrayUtils.toString(data2));

		int[] a = { 0, 11, 12, 13, 14, 15 };
		int[] fa = new int[array.length + a.length];
		int[] oa = so.merge(fa, array, a);
		//System.out.println(ArrayUtils.toString(oa));
		System.out.println(so.bs(oa, 14));
		System.out.println(so.bs(oa, 11));
		System.out.println(so.bs(oa, 15));
		System.out.println(so.bs(oa, 2));
		System.out.println(so.bs(oa, 1));
		System.out.println(so.bs(oa, 0));
		System.out.println(so.bs(oa, 16));

		int[] aa = { 15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 15 };
		System.out.println(so.msbs(aa, 20));

		String[] sa = { "at", "", "", "", "", "ball", "car", "", "", "dad", "",	"" };
		System.out.println("sa    " + so.bsss(sa, "ball", 0, sa.length - 1));
		// int i = 7516192768;
		int i = 939524102;

		// int[] a1 = {23,27,29,31,37,1,4,11,14,15,17,19};
		// so.pivotedSearch(A, s, h, elem)
		// int[] a1 = {23,27,29,31,37,9,11,14,15,17};
		System.out.println(so.pivotedSearch(a1, 0, a1.length - 1, 27));*/
		
		String[] sa = { "at", "", "", "", "", "ball", "car", "", "", "dad", "",	"" };
		System.out.println(so.sparseSearch(sa, "ball", 0, sa.length-1));
		
		
		//int[] data = {1,2,3,5,6,7,9,10};
		//System.out.println(so.bs(data, 6, 0, data.length));
		
		//String[] sa = { "at", "", "", "", "", "ball", "car", "", "", "dad", "",	"" };
		//System.out.println("sa    " + so.ss(sa, "no", 0, sa.length - 1));
		
		//int[] data = {1,2,3,4,5,6,7,8};
		//so.zigzag(data);
		//System.out.println(ArrayUtils.toString(data));
		int[] data1 = {1,2,3,4,5,6,7,8};
		int[] data2 = {4, 3, 7, 8, 6, 2, 1};
		int[] data5 = {4, 3, 7, 8, 6, 2, 1};
		so.zigzag1(data5);
		System.out.println("zigzag"+ArrayUtils.toString(data5));
		
		int[] A={1,2,7,12,16,-1,-1,-1,-1};  
		int[] B={4,5,8,14};
		so.msa(A, B, 5);
		System.out.println("ms"+ArrayUtils.toString(A));
		
		//int[] A={15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14};
		//System.out.println(so.searchRotatedArray(A, 16, 0, A.length-1));
		
		int[][] data = 	{
				 {15,20,40,85},
				 {20,35,80,95},
				 {30,55,95,105},
				 {40,80,100,120}
				};
		
		//System.out.println(so.searchElementInTwoDimensionalSortedArray(data, 120));
		System.out.println(Math.abs((1-2)));
		so.ss();
		String[] A1 = {"at", "", "", "", "ball", "", "", "car", "", "", "dad", "", ""};
		System.out.println(so.s1(A1, "car", 0, A1.length-1));
				
	}
	
	//int[] a = {15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14}; 

	//{15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14}
	//{"at", "", "", "", "ball", "", "", "car", "", "", "dad", "", ""}
	public int s1(String[] A, String e, int s, int l){
		while(l > s){
			while(A[l].equals("")){
				l--;
			}
			if(l < s){
				return -1;
			}
			int mid = (s+l)/2;
			
			while(A[mid].equals("")){
				mid++;
			}
			
			if(e.equals(A[mid])){
				return mid;
			}else if(A[mid].compareTo(e) < 0){
				s = mid + 1;
			}else{
				l = mid -1;
			}
		}
		return -1;
		
		
	}
	
	//int[] A={1,2,7,12,16,-1,-1,-1,-1};  
	//int[] B={4,5,8,14};
	
	public void msa(int[] a, int[] b, int trueLength){
		int i = trueLength -1;
		int j = b.length-1;
		int k = a.length-1;
		
		while(i >= 0 && j >= 0){
			if(a[i] > a[j]){
				a[k--] = a[i--];
			}else{
				a[k--] = b[j--];
			}
		}
		
		while(j >= 0){
			a[k--] = b[j--];
		}
	}
	
	private int[] mergeSort(int[] data) {
		if (data.length < 2) {
			return data;
		}

		int mid = data.length / 2;
		int[] left = new int[mid];
		int[] right = new int[data.length - mid];

		System.arraycopy(data, 0, left, 0, left.length);
		System.arraycopy(data, mid, right, 0, right.length);

		left = mergeSort(left);
		right = mergeSort(right);

		return merge(data, left, right);
	}
	
	private int[] merge(int[] data, int[] left, int[] right) {

		int di = 0;
		int li = 0;
		int ri = 0;

		while (li < left.length && ri < right.length) {
			if (left[li] <= right[ri]) {
				data[di++] = left[li++];
			} else {
				data[di++] = right[ri++];
			}
		}

		while (li < left.length) {
			data[di++] = left[li++];
		}

		while (ri < right.length) {
			data[di++] = right[ri++];
		}

		return data;
	}

	private int[] quickSort(int data[]) {

		if (data.length < 2) {
			return data;
		}
		int pIndex = (data.length) / 2;
		int p = data[pIndex];
		// count how many are less than pivot
		int leftCount = 0;
		for (int i = 0; i < data.length; ++i) {
			if (data[i] < p) {
				++leftCount;
			}
		}
		
		System.out.println(data.length);
		System.out.println(data.length - (leftCount + 1));
		
		int[] left = new int[leftCount];
		int[] right = new int[data.length - (leftCount + 1)];

		int l = 0;
		int r = 0;

		for (int i = 0; i < data.length; i++) {
			if (i == pIndex) {
				continue;
			}
			if (data[i] < p) {
				left[l++] = data[i];
			} else {
				right[r++] = data[i];
			}
		}

		left = quickSort(left);
		right = quickSort(right);

		System.arraycopy(left, 0, data, 0, left.length);
		data[left.length] = p;
		System.arraycopy(right, 0, data, left.length + 1, right.length);

		return data;
	}
	
	//binary search
	private int bs(int[] data, int e, int start, int end){
		
		if(start > end){
			return -1;
		}
		int mid = (start+end)/2;
		if(e == data[mid]){
			return mid;
		}else if (data[mid] < e){
			return bs(data, e, mid+1, end );			
		}else{
			return bs(data, e, start, mid-1);
		}
		
	}
	
	private int msbs(int[] data, int e) {
		int p = -1;

		int l = 0;
		int u = data.length - 1;
		int m = (l + u) / 2;

		while (l <= u) {
			if (e == data[m]) {
				// found element break;
				p = m;
				break;
			} else if (data[u] <= data[m]) {
				if (e > data[m]) {
					l = m + 1;
				} else if (e >= data[l]) {
					u = m - 1;
				} else {
					l = m + 1;
				}
			} else if (e > data[m]) {
				// element is in a right block, so move s and find new middle
				// element
				l = m + 1;
			} else {
				u = m - 1;
			}

			m = (l + u) / 2;

		}

		return p;
	}
		
	//rotate-array search
	//A={15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14}
	public int searchRotatedArray(int[] data, int e, int start, int end){
		return -1;
	}
	
	public String searchElementInTwoDimensionalSortedArray(int[][] data, int d){
		int i = 0;
		int j = data[0].length -1;
		
		while(i < data.length && j >= 0){
			if(data[i][j] == d){
				return i+","+j;
			}else if(data[i][j] > d){
				j--;
			}else{
				i++;
			}
		}
		return "-1";
	}
	//sparce space search
	private int bsss(String[] data, String x, int start, int last) {
		int p = -1;

		while (start <= last) {

			while (data[last].equals("")) {
				last--;
				if (last < start) {
					return -1;
				}
			}

			// find mid
			int m = (start + last) / 2;
			while (data[m].equals("")) {
				m++;
			}

			if (data[m].equals(x)) {
				p = m;
				break;
			} else if (data[m].compareTo(x) > 0) {
				last = m - 1;
			} else {
				start = m + 1;
			}
		}

		return p;
	}
	
	//sparse search recursive
	private int ss(String[] data, String s, int start, int end){
		if(start > end){
			return -1;
		}
		
		while(end >= start && data[end].equals("")){
			end--;			
		}
		int mid =  (start+end)/2;
		
			
		boolean foundMid = true;
		if(data[mid].equals("")){
			foundMid = false;
			
			while(mid < end && !foundMid){
				mid++;
				if(!data[mid].equals("")){
					foundMid = true;
					break;
				}
			}
			while(mid > 0 && !foundMid){
					mid--;
					if(!data[mid].equals("")){
						foundMid = true;
						break;
					}
			}
			
		}
		if(!foundMid){
			return -1;
		}
		
		if(data[mid].equals(s)){
			return mid;
		}else if(s.compareTo(data[mid]) < 0){
			return ss(data, s, start, mid-1);			
		}else{
			return ss(data, s, mid+1, end);
		}
	}
	
	//["at", "", "", "", "ball", "", "", "car", "", "", "dad", "", ""]
	
	private int sparseSearch(String[] a, String str, int s, int l){
		while(s <= l){
			while (a[l].equals("")){
				l--;
			}
			if(l < s){
				return -1;
			}
			 
			int m = (s+l)/2;
			while(a[m].equals("")){
				m++;
			}
			if(a[m].equals(str)){
				return m;
			}else if(a[m].compareTo(str) < 0){
				s = m+1;
			}else{
				l = m-1;
			}
		}
		return -1;
	}
	
	//2 sorted array merge, one array is big engough to hold other	//A={1,2,7,12,14,-1,-1,-1,-1}  B={4,5,8,16}
	//A={1,2,7,12,14,7,8,12,16}
	
	public void mergeSortedArray(int[] a, int[] b, int trueLength){
		int ai = trueLength - 1; //ai = i = j 
		int bi = b.length -1; //bi = j = i
		int i = a.length -1; //i =k = k
		
		while(ai >=0 && bi >=0 ){
			if(a[ai] > b[bi]){
				a[i--] = a[ai--];
			}else{
				a[i--] = b[bi--];
			}
		}
		while(bi >= 0){
			a[i--] = b[bi--];
		}
	}
	
	
	// {23,27,29,31,37,1,4,11,14,15,17,19}	
	//zigzag assign a>b<c>d {1,2,3,4,5,6,7,8} = {2 > 1 < 4 > 3 < 5 > 4 < 7 > 6 < 8}
	
	private void zigzag(int data[]){
		if (data.length < 2){
			return;
		}
		boolean s = true; //if true means item should be greater than next
		for(int i=0; i< data.length - 1; i++){
			if(i % 2 == 0){
				s = true;
			}else{
				s = false;
			}
			if(s){
				if(! (data[i] > data[i+1]) ){
					int temp = data[i+1];
					data[i+1] = data[i];
					data[i] = temp;					
				}
			}else{
				if(! (data[i] < data[i+1]) ){
					int temp = data[i+1];
					data[i+1] = data[i];
					data[i] = temp;					
				}
			}
			
		}
	}
	
	private void zigzag1(int[] a){
		if(a.length < 2){
			return;
		}
		//Arrays.sort(a);
		boolean flag = true;

		for(int i = 0; i < a.length -1; i++){
			if(i%2 == 0){
				flag = true;
			}else{
				flag = false;
			}
			if(flag){
				if(a[i] > a[i+1]){
					int t = a[i];
					a[i] = a[i+1];
					a[i+1] = t;
				}
			}else{
				if(a[i] < a[i+1]){
					int t = a[i];
					a[i] = a[i+1];
					a[i+1] = t;
				}
			}
		}

	}
	
	static int[] a1 = { 11, 14, 15, 17, 23, 27, 29, 31, 37, 9 };

	private int pivotedSearch(int[] A, int s, int h, int elem) {
		int p = findPivot(A, s, h);
		if (p == -1) {
			binarySearch(A, s, h, elem);
		}

		if (A[p] == elem) {
			return p;
		} else if (A[0] <= elem) {
			return binarySearch(A, 0, p - 1, elem);
		} else {
			return binarySearch(A, p + 1, h, elem);
		}
	}

	private int findPivot(int[] A, int s, int h) {
		if (h < s)
			return -1;
		if (h == s)
			return s;

		int mid = (s + h) / 2;

		// if(mid < h && A[mid] > A[mid+1] ) return mid;
		// if(mid > s && A[mid] < A[mid -1]) return mid-1;
		if (A[mid] > A[mid + 1])
			return mid;
		if (A[mid] < A[mid - 1])
			return mid - 1;
		if (A[s] >= A[mid]) {
			return findPivot(A, s, mid - 1);
		} else {
			return findPivot(A, mid + 1, h);
		}
	}

	private int binarySearch(int[] A, int s, int h, int elem) {

		if (h < s) {
			return -1;
		}

		int m = (s + h) / 2;
		if (A[m] == elem) {
			return m;
		} else if (A[m] > elem) {
			return binarySearch(A, s, m - 1, elem);
		} else {
			return binarySearch(A, m + 1, h, elem);
		}
	}
	
	
	// odd even sort
	Integer[] oe = { 1, 2, 3, 4, 5, 6, 7, 8 };

	private void sortOddEvenNumber() {
		List<Integer> oelist = Arrays.asList(oe);
		Collections.sort(oelist, new MyOddEvenComparator());
		System.out.println(oelist);
	}

	int[][] sa = { { 10, 20, 30, 40 }, { 15, 25, 35, 45 }, { 27, 29, 37, 48 },
			{ 32, 33, 39, 50 }, };

	private void sortedTwoDimensionalArray(int element) {
		int i = 0;
		int j = sa[0].length - 1;

		while (i < sa.length && j >= 0) {
			if (sa[i][j] == element) {
				System.out.println("foundElement " + i + j);
				break;
			}

			if (sa[i][j] > element) {
				j--;
			} else {
				i++;
			}
		}
	}
	
	// sort stack
	public static Stack<Integer> sort(Stack<Integer> s) {
		Stack<Integer> r = new Stack<Integer>();
		while (!s.isEmpty()) {
			int tmp = s.pop();
			while (!r.isEmpty() && r.peek() > tmp) {
				s.push(r.pop());
			}
			r.push(tmp);
		}
		return r;
	}	
	
	private void ss(){
		Stack<Integer> s1 = new Stack<>(); 
		Stack<Integer> s2 = new Stack<>();
		s1.push(9);
		s1.push(8);
		s1.push(2);
		s1.push(3);
		s1.push(4);
		
		while(!s1.isEmpty()){
			int i = s1.pop();			
			while(!s2.isEmpty() && s2.peek() > i){
					s1.push(s2.pop());
				}
				s2.push(i);
				
		}
		
		System.out.println(s2);
	}
	
	
	
	
	
	static int A[] = {4, 5, 6, 7, 0, 1, 2};	
	
	//{15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14}
	
	public int rotatedArray(int[] A, int e){
		int s  = 0;
		int u = A.length;
		while (u > s){
			int m = (s+u)/2;
			if(A[m] == e){
				return m;
			}else if(A[m] >= A[s]){
				if(e > A[m]){
					s = m+1;					
				}else if(e > A[s]){
					u = m-1;
				}else{
					s = m+1;
				}
			}else if(e < A[m]){
				u = m-1;
			}else if(e < A[s]){
				s = m+1;
			}else{
				u = m-1;
			}
		}
		
		return -1;
	}
}

class MyOddEvenComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer n1, Integer n2) {
		if (n1 % 2 == 0 && n2 % 2 == 0) {
			return n1.compareTo(n2);
		} else if (n1 % 2 == 0) {
			return 1;
		} else if (n2 % 2 == 0) {
			return -1;
		} else {
			return 0;
		}
	}

}