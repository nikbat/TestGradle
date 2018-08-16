package com.nick.sa;

public class Sort {

	public static void main(String[] args) {

		/*int[] array = { 6, 4, 9, 5, 1, 3, 2, 7 };
		array = new Sort().merge(array);
		array = new Sort().quick(array);
		array = new Sort().quicksortSimple(array);
		System.out.println(Arrays.toString(array));*/
		System.out.println(rotated_binary_search(A, A.length, 4));
		System.out.println(rotatedaarray(A, 4));

	}

	public int[] merge(int[] data) {

		if (data.length < 2) {
			return data;
		}

		// Divide the data into 2 sub arrays
		int mid = data.length / 2;
		int[] left = new int[mid];
		int[] right = new int[data.length - mid];

		System.arraycopy(data, 0, left, 0, left.length);
		System.arraycopy(data, mid, right, 0, right.length);

		left = merge(left);
		right = merge(right);

		int i = 0, l = 0, r = 0;

		while (left.length != l && right.length != r) {
			if (left[l] < right[r]) {
				data[i] = left[l];
				i++;
				l++;
			} else {
				data[i] = right[r];
				i++;
				r++;
			}
		}

		while (left.length != l) {
			data[i] = left[l];
			i++;
			l++;
		}

		while (right.length != r) {
			data[i] = right[r];
			i++;
			r++;
		}
		return data;
	}
	
	/*
	 * Quicksort is a divide-and-conquer algorithm that involves choosing a
	 * pivot value from a data set and splitting the set into two subsets: a set
	 * that contains all values less than the pivot and a set that contains all
	 * values greater than or equal to the pivot. The pivot/split process is
	 * recursively applied to each subset until there are no more subsets to
	 * split. The results are combined to form the final sorted set.
	 */

	public int[] quick(int[] data) {
		if (data.length < 2) {
			return data;
		}

		int pivotIndex = data.length / 2;
		int pivotValue = data[pivotIndex];

		// count how many are less than pivot
		int leftCount = 0;

		for (int i = 0; i < data.length; ++i) {
			if (data[i] < pivotValue) {
				++leftCount;
			}
		}

		int[] left = new int[leftCount];
		int[] right = new int[data.length - (leftCount + 1)];

		int l = 0;
		int r = 0;

		for (int i = 0; i < data.length; ++i) {
			if (i == pivotIndex) {
				continue;
			}

			int val = data[i];
			if (val < pivotValue) {
				left[l++] = val;

			} else {
				right[r++] = val;

			}
		}

		left = quick(left);
		right = quick(right);

		System.arraycopy(left, 0, data, 0, left.length);
		data[left.length] = pivotValue;
		System.arraycopy(right, 0, data, left.length + 1, right.length);

		return data;
	}	

	public int[] quicksortSimple(int[] data) {
		if (data.length < 2) {
			return data;
		}
		int pivotIndex = data.length / 2;
		int pivotValue = data[pivotIndex];
		int leftCount = 0;
		// Count how many are less than the pivot
		for (int i = 0; i < data.length; i++) {
			if (data[i] < pivotValue)
				leftCount++;
		}
		// Allocate the arrays and create the subsets
		int[] left = new int[leftCount];
		int[] right = new int[data.length - (leftCount + 1)];
		int l = 0;
		int r = 0;
		for (int i = 0; i < data.length; i++) {
			if (i == pivotIndex)
				continue;
			int val = data[i];
			if (val < pivotValue) {
				left[l++] = val;
			} else {
				right[r++] = val;
			}
		}
		// Sort the subsets
		left = quicksortSimple(left);
		right = quicksortSimple(right);
		// Combine the sorted arrays and the pivot back into the original array
		System.arraycopy(left, 0, data, 0, left.length);
		data[left.length] = pivotValue;
		System.arraycopy(right, 0, data, left.length + 1, right.length);
		return data;
	}
	
	/*
	 * Insertion sort is another simple sorting algorithm. It builds a sorted
	 * array (or list) one element at a time by comparing each new element to
	 * the already-sorted elements and inserting the new element into the
	 * correct location, similar to the way you sort a hand of playing cards. A
	 * simple implementation of insertion sort is as follows:
	 */

	public int[] is(int[] data){
		
		for(int i=1; i < data.length; i++){
			int j = i;
			while(j > 0){
				if(data[j] < data[j-1]){
					//swap;
					j--;
				}
			}			
		}
		return data;
	}
	
	private int bs(int[] data, int e) {
		int p = -1;
		int s = 0;
		int l = data.length - 1;
		int m = data.length / 2;

		while (s <= l) {
			if (e == data[m]) {
				// found element break;
				p = m;
				break;
			} else if (e > data[m]) {
				// element is in a right block, so move s and find new middle
				// element
				s = m + 1;
			} else {
				l = m - 1;
			}

			m = (s + l) / 2;
		}

		return p;
	}
	

	static int rotated_binary_search(int A[], int N, int key) {
		  int L = 0;
		  int R = N - 1;
		 
		  while (L <= R) {
		    // Avoid overflow, same as M=(L+R)/2
		    int M = L + ((R - L) / 2);
		    if (A[M] == key) return M;
		 
		    // the bottom half is sorted
		    if (A[L] <= A[M]) {
		      if (A[L] <= key && key < A[M])
		        R = M - 1;
		      else
		        L = M + 1;
		    }
		    // the upper half is sorted
		    else {
		      if (A[M] < key && key <= A[R])
		        L = M + 1;
		      else 
		        R = M - 1;
		    }
		  }
		  return -1;
	}
	
	static int A[] = {4, 5, 6, 7, 0, 1, 2};
	
	private static int rotatedaarray(int[] a, int e){
		
		int s = 0;
		int l = a.length - 1;
		
		
		while(s <= l){
			int m = (s+l)/2;
			if(a[m] == e){
				return m;
			}else if(a[s] <= a[m]){
				if(a[s] <= e && e < a[m]){
					l = m-1;
				}else{
					s = m+1;
				}
			}else{
				if(a[m] < e && e <= a[l]){
					s = m + 1;
				}else{
					l = m - 1;
				}
			}
			
		}
		return -1;
		
	}	
}
