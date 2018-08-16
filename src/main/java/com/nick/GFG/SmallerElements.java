package com.nick.GFG;


import java.util.Arrays;
import java.util.Scanner;

public class SmallerElements {

  public static void main (String[] args) {
    Scanner sc = new Scanner(System.in);
    int t = Integer.parseInt(sc.nextLine());
    while(t-- > 0){
      int n = Integer.parseInt(sc.nextLine());
      int[] a = new int[n];
      String[] str = sc.nextLine().split(" ");

      for(int i =0; i < n; i++){
        a[i] = Integer.parseInt(str[i]);
      }

      int val = Integer.parseInt(sc.nextLine());
      int r = 0;
      for(int i = 0; i < n; i++){
        if (a[i] <= val){
          r = r+1;
        }
      }
      System.out.println(r);
    }
  }

  public static void main2 (String[] args) {
    Scanner scan = new Scanner(System.in);
    int testCases = scan.nextInt();
    System.out.println("testCases:"+ testCases);
    for(int i = 0; i < testCases; i++){
      int N = scan.nextInt();
      System.out.println("size:"+N);

      int[] a = new int[N];

      for (int j = 0; j < N; j++){
        int t = scan.nextInt();
        System.out.print(t+",");
        a[j] = t;
      }

      Arrays.sort(a);
      int k = scan.nextInt();
      System.out.println("k:"+k);
      System.out.println(a[k-1]);

    }
  }

   /*
  public static void main (String[] args)
	 {
	 //code
	 Scanner sc=new Scanner(System.in);
	 int t=sc.nextInt();
	 while(t>0)
	 {
	     int n=sc.nextInt();
	     int a[]=new int[n];
	     for(int i=0;i<n;i++)
	      a[i]=sc.nextInt();
	     Arrays.sort(a);
	     int k=sc.nextInt();
	     System.out.println(a[k-1]);
	     t--;
	 }

	 }
   */

}
