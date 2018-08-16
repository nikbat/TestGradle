package com.nick.sa;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Sort1 {

  public static void main(String[] args) {
    int[] a = {1, 3, 2, 1, 2, 2, 3, 1, 3, 3, 1};
    System.out.println(Sort1.leastFrequent(a));
    Sort1.isSortable();
    Sort1.listPartition();
    Sort1.printQuery();
    int[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    rotateArray(b, 1);
    System.out.println(ArrayUtils.toString(b));
    String[] s1 = {"at", "", "", "", "ball", "", "", "car", "", "", "dad", "", "test"};
    String[] s2 = {"at", "", "", "", "", "ball", "car", "", "", "dad", "", ""};
    System.out.println(findString(s1, "at "));
    System.out.println(findString(s1, "ballcar"));

    String[] anagram = {"Tar", "Arc", "Elbow","State", "Rat", "Car", "Below","Taste", "Dusty"};
    sortAnagram(anagram);
    System.out.println(ArrayUtils.toString(anagram));

    //int[] ra = {15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14};

    List<HtWt> htWtList = new ArrayList<>();
    htWtList.add( new HtWt(60, 100) );
    htWtList.add( new HtWt(70, 150) );
    htWtList.add( new HtWt(56, 90) );
    htWtList.add( new HtWt(75, 190) );
    htWtList.add( new HtWt(60, 95) );
    htWtList.add( new HtWt(68,110) );

    sortHtWt(htWtList);
    System.out.println(htWtList);

  }

  //https://www.geeksforgeeks.org/insertion-sort/
  static void insertionSort(int arr[]) {
    //int arr[] = {12, 11, 13, 5, 6};

    int n = arr.length;
    for (int i = 1; i < n; ++i) {
      int key = arr[i];
      int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j = j - 1;
      }
      arr[j + 1] = key;
    }
  }

  public static void test() {
    int[][] arr = {
        {1, 3},
        {1, 7},
        {4, 8},
        {2, 5}
    };

    Arrays.sort(arr);
    ArrayUtils.toString(arr);
  }

  //https://www.geeksforgeeks.org/least-frequent-element-array/
  static int leastFrequent(int[] a) {
    Arrays.sort(a);
    //{1, 3, 2, 1, 2, 2, 3, 1}
    int count = 1;
    int runner = a[0];
    int leastFrequent = a[0];
    int leastCount = Integer.MAX_VALUE;
    for (int i = 1; i < a.length; i++) {
      if (a[i] == runner) {
        count++;
      } else {
        if (count < leastCount) {
          leastFrequent = runner;
        }
        runner = a[i];
        count = 1;
      }
    }

    return leastFrequent;
  }

  //https://www.geeksforgeeks.org/sort-1-n-swapping-adjacent-elements/
  static boolean isSortable() {
    int[] a = {1, 2, 5, 3, 4, 6};
    int[] b = {0, 1, 1, 1, 0};

    for (int i = 1; i < a.length; i++) {
      if (a[i] < a[i - 1] && i < b.length && b[i] == 1) {
        int t = a[i];
        a[i] = a[i - 1];
        a[i - 1] = t;
      }
    }

    System.out.println(ArrayUtils.toString(a));
    return true;
  }

  static void listPartition() {
    int[] a = {2, 1, 0, 3, 4, 5, 1};
    boolean fresh_start = true;
    boolean isIncrement = false;
    int s = 0;
    List<StringBuilder> l = new ArrayList<>();
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < a.length; i++) {
      if (fresh_start && a[i] < a.length) {
        s = i;
        fresh_start = false;
        sb = new StringBuilder();
        sb.append(a[i]);
        if (i + 1 == a.length) {
          continue;
        }
        if (a[i] > a[i + 1]) {
          isIncrement = true;
        } else {
          isIncrement = false;
        }
        continue;
      }
      if (isIncrement && a[i] < a[i - 1]) {
        sb.append(a[i]);
      } else if (!isIncrement && a[i] > a[i - 1]) {
        sb.append(a[i]);
      } else {
        /*for(int j = s; j < i; j++){
          sb.append(a[j]+",");
        }*/
        l.add(sb);
        fresh_start = true;
        i--;
      }
    }
    l.add(sb);
    for (StringBuilder sb1 : l) {
      System.out.println(sb1);
    }
  }

  //https://www.geeksforgeeks.org/find-k-th-smallest-element-in-given-n-ranges/
  static void printQuery() {
    int a[][] = {{1, 4}, {6, 8}};
    int q[] = {2, 6, 10};

    List<Integer> l = new ArrayList<>();

    for (int i = 0; i < a.length; i++) {
      int j = a[i][0];
      int k = a[i][a[i].length - 1];
      while (j <= k) {
        l.add(j);
        j++;
      }
    }

    for (int i : q) {
      if (i > l.size()) {
        System.out.println(-1);
      } else {
        System.out.println(l.get(i - 1));
      }
    }
  }

  //https://www.geeksforgeeks.org/insertion-sorteven-odd-positioned-elements/
  static void isoe() {
    //Do instertion sort first
  }

  static void rotateArray(int[] a, int n) {
    if (a == null) {
      return;
    }
    for (int i = 0; i < a.length; i++) {
      int t = a[(i + n) % n];
      a[(i + n) % n] = a[i];
      a[i] = t;
    }
  }

  static int findString(String[] a, String st) {
    if (a == null) {
      return -1;
    }

    int s = 0;
    int l = a.length - 1;

    while (l >= s) {

      while (l >= s && a[l].equals("")) {
        l--;
      }

      if (l < s) {
        return -1;
      }

      int m = (s + l) / 2;

      while (a[m].equals("")) {
        m++;
      }

      int r = a[m].compareTo(st);
      if (r == 0) {
        return m;
      } else if (r < 0) {
        s = m + 1;
      } else {
        l = m - 1;
      }
    }
    return -1;
  }

  static void sortAnagram(String[] s){
    Arrays.sort(s, (s1, s2) -> {

      char[] c1 = s1.toLowerCase().toCharArray();
      char[] c2 = s2.toLowerCase().toCharArray();

      Arrays.sort(c1);
      Arrays.sort(c2);

      String s3 = new String(c1);
      String s4 = new String(c2);

      return s3.compareTo(s4);
    });
  }

  static void sortHtWt(List<HtWt> htWtList){
    htWtList.sort(Comparator.comparingInt(HtWt::getH).thenComparing(HtWt::getW));
  }

  private static class HtWt{
    int h;
    int w;
    HtWt(int h, int w){
      this.h = h;
      this.w = w;
    }

    public int getH() {
      return h;
    }

    public int getW() {
      return w;
    }

    @Override
    public String toString() {
      return "(" +
          h +
          w +
          ')';
    }
  }

}

