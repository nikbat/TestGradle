package com.nick.sa;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class SA {

  private static boolean isAllCharsUnique1(String str){
    if(str == null){
      return true;
    }

    char[] strc = str.toCharArray();
    boolean unique = true;

    for(int i=0; i <strc.length; i++){
      for(int j = i+1; j<strc.length; j++){
        if(strc[j] ==  strc[i]){
          return false;
        }
      }
    }
    return unique;
  }

  private static boolean isAllCharsUnique2(String str){
    if(str == null){
      return true;
    }

    char[] strc = str.toCharArray();
    boolean unique = true;
    boolean[] b = new boolean[256];

    for(int i=0; i <strc.length; i++){
      if(b[strc[i]]){
        return false;
      }else{
        b[strc[i]] = true;
      }
    }
    return unique;
  }

  private static String  removeDuplicate(String str){
    char[] strc = str.toCharArray();
    boolean[] b = new boolean[256];
    int j = 0;

    for(int i = 0; i < strc.length; i++){
      if(!b[strc[i]] || strc[i] == ' '){
        strc[j++] = strc[i];
        b[strc[i]] = true;
      }
    }

    for(int i = j; j<strc.length; i++){
      strc[j++] = ' ';
    }

    String s = new String(strc);
    return s;

  }

  private static String  removeDuplicate1(String str){
    char[] strc = str.toCharArray();
    boolean[] b = new boolean[256];
    int j = 0;

    for(int i = 0; i < strc.length; i++){
      if(!b[strc[i]] || strc[i] == ' '){
        strc[j++] = strc[i];
        b[strc[i]] = true;
      }
    }

    for(int i = j; j<strc.length; i++){
      strc[j++] = ' ';
    }

    String s = new String(strc);
    return s;
  }

  public String removeDuplicate2(String str){
    char[] strc = str.toCharArray();
    int k = 1;
    for(int i = 1; i < strc.length; i++ ){
      boolean dup = false;
      for(int j = 0; j < i ; j++){
        if(strc[j] == strc[i]){
          dup = true;
          break;
        }
      }
      if(!dup){
        strc[k++] = strc[i];
      }
    }

    strc[k] = 0;
    /*for(int i = k; k<strc.length; i++){
      strc[k++] = ' ';
    }*/

    String s = new String(strc);
    return s;
  }

  private static boolean isAnnagram(String s1, String s2){

    if(s1 == null && s2 == null){
      return true;
    }else if(s1 == null || s2 == null){
      return false;
    }else if(s1.length() != s2.length()){
      return false;
    }

    int[] cc = new int[256];
    char[] cas1 = s1.toCharArray();
    char[] cas2 = s2.toCharArray();

    for(int i = 0; i < cas1.length; i++){
      cc[cas1[i]]++;
    }

    for(int i = 0; i < cas2.length; i++){
      cc[cas2[i]]--;
      if(cc[cas2[i]] < 0){
        return false;
      }
    }

    return true;
  }

  private static void fillSpace(String str){

    char[] strc = str.toCharArray();

    int spaceCount = 0;
    System.out.println(strc.length);

    for(int i = 0; i < strc.length; i++){
      if(strc[i] == ' '){
        spaceCount++;
      }
    }
    int newlength = strc.length + spaceCount*2;

    //strc[newlength] = '\0';
    //System.out.println(strc.length);
  }

  private static void updateMatrix(int[][] matrix){

    System.out.println(ArrayUtils.toString(matrix));

    //DO null check
    int[] rows = new int[matrix.length];
    int[] columns = new int[matrix[0].length];

    for(int i = 0; i<matrix.length; i++){
      for(int j = 0; j < matrix[i].length; j++){
         if(matrix[i][j] == 0){
           rows[i] = 1;
           columns[j] = 1;
         }
      }
    }
    for(int i = 0; i<matrix.length; i++){
      for(int j = 0; j < matrix[i].length; j++){
        if(rows[i] == 1 || columns[j] ==1){
          matrix[i][j] = 0;
        }
      }
    }

    System.out.println(ArrayUtils.toString(matrix));
  }

  public static void reverse(){

    String s = "Do or do not, there is no try";
    char[] cs = s.toCharArray();
    char[] fa = new char[cs.length];
    int l = cs.length;
    int c = 0;
    for(int i = cs.length - 1; i >= 0; i--){
      if(cs[i] == ' ' || i == 0 ){
        int k;
        if( i == 0){
          k = i;
        }else{
          k = i+1;
        }
        for(int j = k ; j<l; j++){
          fa[c++] = cs[j];
        }
        if(i != 0){
          fa[c++] = ' ';
        }
        l = i;
      }
    }

    System.out.println(String.valueOf(fa));
  }

  //int[] a = {1,2,3,4,5};
  //This is wrong solution, see below - https://www.geeksforgeeks.org/array-rotation/
  static void rotateArray(int[] a,  int N){
    for(int i=0; i<a.length; i++){
      int temp = a[(i+N) % N];
      a[ (i+N) % N ] = a[i];
      a[i] = temp;
    }
  }

  static void rotateArray1(int[] a,  int N){
    for(int i=0; i<a.length; i++){
      int t = Math.abs(i-N);
      int temp = a[t % N];
      a[ t % N ] = a[i];
      a[i] = temp;
    }
  }

  static void leftRotate(int arr[],  int n) {
    for (int i = 0; i < n; i++) {
      leftRotatebyOne(arr, arr.length);
    }
  }
  static void leftRotatebyOne(int arr[], int n) {
    int i, temp;
    temp = arr[0];
    for (i = 0; i < n - 1; i++) {
      arr[i] = arr[i + 1];
    }
    arr[i] = temp;
  }

  //This is wrong implemantation see above
  static void leftRotate2(int arr[], int n, int k){

    /* To get the starting point of
        rotated array */
    int mod = k % n;

    // Prints the rotated array from
    // start position
    for(int i = 0; i < n; ++i)
      System.out.print(arr[(i + mod) % n] + " ");

    System.out.println();

  }

  //https://www.geeksforgeeks.org/find-smallest-number-whose-digits-multiply-given-number-n/
  // Find the smallest number whose digits multiply to a given number n
  static int minimumIntegerToMultiplyToGetSum(int N) {
    PriorityQueue<Integer> digits = new PriorityQueue<Integer>();

    for (int factor = 9; factor > 1; factor--) {
      while (N % factor == 0) {
        N /= factor;
        digits.add(factor);
      }
    }
    if (N != 1)
      return -1;

    int number = 0;
    for (int i : digits) {
      number = number * 10;
      number = number + i;
    }
    return number;
  }

  static String concatStaring(String s) {
    //String s = "45eeeedfghfffeff62";
    char[] sa = s.toCharArray();

    StringBuilder sb = new StringBuilder();

    for(int i=0; i < sa.length; i++){
      sb.append(sa[i]);
      int count = 1;
      while(i < sa.length-1 && sa[i] == sa[i+1]){
        i++;
        count++;
      }
      if(count > 1){
        sb.append(count);
      }
    }

    return sb.toString();
  }

  static int[][] sa = {
      { 10, 20, 30, 40 },
      { 15, 25, 35, 45 },
      { 27, 29, 37, 48 },
      { 32, 33, 39, 50 },
  };

  static void printDigonal(){
    /*i=0; j=0;
    i=0; j=1; i=1;j=0
    i=0; j=2; i=1;j=1; i=2;j=0
    i=0; j=3; i=1;j=2; i=2;j=1; i=3; j=0;
    i=1; j=3; i=2;j=2; i=3;j=1

    */


    for(int i = 0; i <sa.length; i++){
      int r = 0;
      int c = i;
      while(c>=0 && r<sa.length){
        System.out.print(sa[r][c]);
        c--;
        r++;
      }
      System.out.println();
    }
    System.out.println("-");
    for(int i = 1; i <sa.length; i++){
      int r = i;
      int c = sa[i].length-1;
      while(c>=0 && r<sa.length){
        System.out.print(sa[r][c]);
        c--;
        r++;
      }
      System.out.println();
    }

  }

  //https://www.geeksforgeeks.org/search-in-row-wise-and-column-wise-sorted-matrix/
  static boolean find(int[][] matrix, int n){

    /*int matrix1 [][] = {
        {10, 20, 30, 40},
        {15, 25, 35, 45},
        {27, 29, 37, 48},
        {32, 33, 39, 50}};*/

    int r = 0;
    int c = matrix[0].length -1;

    while(r < matrix.length && c>=0){
      if(matrix[r][c] == n){
        return true;
      }else if(matrix[r][c] > n){
        c--;
      }else if(matrix[r][c] < n){
        r++;
      }
    }

    return false;
  }

  //https://www.geeksforgeeks.org/calculate-angle-hour-hand-minute-hand/
  static void findAngle(int h, int m){

    // validate the input
    if (h <0 || m < 0 || h >12 || m > 60)
      System.out.println("Wrong input");

    double mpm = 360/60; // 6
    double hpm = 360.0/(12*60); //.5

    if (h == 12)
      h = 0;
    if (m == 60)
      m = 0;

    int hour_angle = (int)( (h*60 + m) *.5 );
    int minute_angle = (int) (6 * m);

    int angle = Math.abs(hour_angle - minute_angle);

    System.out.println(angle);

  }

  //https://www.geeksforgeeks.org/k-th-prime-factor-given-number/
  static void printKthprimeFactor(int n, int k){

  }

  static void printPrimeFactor(int n){
    List<Integer> l = new ArrayList<>();
    for(int i=2; i<=n; i++ ){
      if(n % i == 0 && i%2 == 0){
        l.add(i);
      }
    }
    System.out.println(l);
  }

  /*static final int[][] paths = {
      {0,0,1,0,1},
      {0,0,0,0,0},
      {0,1,1,1,0},
      {0,1,1,0,0},
      {0,1,1,0,0}

  };*/

  static int counter = 0;
  public static boolean solveMaze(int[][] paths, int x, int y, int i, int j){
    if(stepMaze(paths,x,y,i,j)){
      paths[x][y] = 5;
      return true;
    }
    return false;
  }



  public static boolean stepMaze(int[][] paths, int x, int y, int i, int j){
    counter++;
    if(x == i && y == j){
      return true;
    }

    if(x < 0 || x >= paths.length || y < 0 || y >= paths.length){
      return false;
    }

    if(paths[x][y] == 1 || paths[x][y] == 2){
      return false;
    }

    paths[x][y] = 2;
    if(stepMaze(paths, x, y+1, i, j)){
      return true;
    }

    if(stepMaze(paths, x-1, y, i, j)){
      return true;
    }

    if(stepMaze(paths, x+1, y, i, j)){
      return true;
    }

    if(stepMaze(paths, x, y-1, i, j)){
      return true;
    }

    paths[x][y] = 0;

    return false;
  }

  static boolean[] used;
  static StringBuilder sb = new StringBuilder();;

  public static void permute(String s){
    used = new boolean[s.length()];
    sb = new StringBuilder();
    permute1(s, sb);
  }

  public static void permute1( String s, StringBuilder sb){

    if (s.length() == sb.length()) {
      System.out.println(sb);
      return;
    }

    for(int i = 0; i < s.length(); i++) {
      if (used[i]) {
        continue;
      }
      used[i] = true;
      sb.append(s.charAt(i));
      permute1(s, sb);
      used[i] = false;
      sb.setLength(sb.length() - 1);
    }
  }

  static void compute(String s, int start){
    for(int i=start; i< s.length(); i++){
      sb.append(s.charAt(i));
      System.out.println(sb);
      if(i < s.length())
        compute(s, i+1);
      sb.setLength(sb.length() -1);
    }
  }

  //https://www.geeksforgeeks.org/print-all-combinations-of-balanced-parentheses/
  static int tp = 0;
  static void totalParenthesis(char[] a, int n, int i, int open, int close){
    if(close == n){
      for(int j = 0 ; j < a.length; j++){
        System.out.print(a[j]);
      }
      tp++;
      System.out.println(tp);
    }else {
      if (open < n) {
        a[i] = '{';
        totalParenthesis(a, n, i+1, open + 1, close);
      }
      if (open > close) {
        a[i] = '}';
        totalParenthesis(a, n, i+1, open, close + 1);
      }
    }
  }

  //https://www.geeksforgeeks.org/sort-matrix-row-wise-column-wise/
  static void sort2DArray(int[][] a){
    sort2DArrayByRow(a);
    transpose2DArray(a);
    sort2DArrayByRow(a);
    transpose2DArray(a);
  }

  static void sort2DArrayByRow(int[][] a){
    for(int i = 0; i< a.length; i++){
      Arrays.sort(a[i]);
    }
  }

  static void transpose2DArray(int[][] a){
    for(int i = 0; i< a.length; i++){
      for(int j = i + 1; j < a[i].length; j++){
        int t = a[i][j];
        a[i][j] = a[j][i];
        a[j][i] = t;
      }
    }
  }


  //https://www.geeksforgeeks.org/rearrange-array-arri/
  //int a[] = {-1, -1, 6, 1, 9, 3, 2, -1, 4, -1}
  static void arrangeArray(int[] a){
    int t = -1;
    for(int i = 0; i < a.length; i++){
      if(a[i] == -1){
        continue;
      }
      if(a[i] != i){
        t = a[i];
        a[i] = a[t];
        a[t] = t;
        i = i-1;
      }
    }
    System.out.println(Arrays.toString(a));
  }

  static void arrangeArray1(int[] a){

    for(int i = 0; i < a.length; i++){

      if(a[i] == -1){
        continue;
      }

      int t = a[i];

      if(t == i){
        continue;
      }else if(t < a.length){
        a[i] = a[t];
        a[t] = t;
        i = i-1;
      }

    }
  }



  static String timeConversion(String s) {
    if(s == null){
      return "";
    }
    char[] t = s.toCharArray();
    int h1 = Character.getNumericValue(t[0]);
    int h2 = Character.getNumericValue(t[1]);
    if(h1 == 1 && h2 == 2){
      t[0] = (char)(0+'0');
      t[1] = (char)(0+'0');
    }else{
      h1 = h1+1;
      h2 = h2+2;
      t[0] = (char)(h1+'0');
      t[1] = (char)(h2+'0');
    }

    String s1 =  new String(t);
    return s1.substring(0,8);

  }

  //https://www.geeksforgeeks.org/reverse-an-array-in-groups-of-given-size/
  static int[] reverseArrayInGroup(int n, int k){
    int[] a = new int[n];

    for(int i = 0; i < n; i++){
      a[i] = i;
    }
    reverseArrayInGroup1(a, n, k);

    return a;
  }

  static void reverseArrayInGroup1(int[] a, int n, int k){
    for(int i = 0; i<a.length; i=i+k){
      int left = i;
      int right = Math.min(i+k, n-1);

      while(right > left){
        int t = a[left];
        a[left] = a[right];
        a[right] = t;
        left++;
        right--;
      }
    }
  }


  static int[] reverseArrayInGroup1(int n, int k){
    int[] a = new int[n];

    for(int i = 0; i < n; i++){
      a[i] = i;

    }

    /*if(a == null || a.length < 2 || k >= a.length-1 || k <= 1 ){
      return null;
    }*/

    for (int i = 0; i < n; i += k)
    {
      int left = i;

      // to handle case when k is not multiple
      // of n
      int right = Math.min(i + k - 1, n - 1);
      int temp;

      // reverse the sub-array [left, right]
      while (left < right)
      {
        temp=a[left];
        a[left]=a[right];
        a[right]=temp;
        left+=1;
        right-=1;
      }
    }

    return a;
  }

  //https://practice.geeksforgeeks.org/problems/subarray-with-given-sum/0
  static void findSum(int[] a, int k){

    for(int i = 0 ; i < a.length; i++){

      boolean found = false;
      int start = i;
      int sum = 0;

      while(start < a.length){
        sum = sum + a[start];
        if(sum == k){
          found = true;
          break;
        }else if(sum > k){
          break;
        }
        start++;
      }

      if(found){
        System.out.println(i+1 +" "+ ++start);
        break;
      }
    }
  }

  //https://practice.geeksforgeeks.org/problems/equilibrium-point/0
  static void findEq(int[] a){

    if(a.length == 1){
      System.out.println(1);
    }

    for(int i = 0; i < a.length; i++){
      int leftSum = 0;
      int rightSum = 0;

      for(int j = 0; j < i; j++){
        leftSum = leftSum+a[i];
      }

      for(int j = i+1; j < a.length; j++){
        rightSum = rightSum+a[i];
      }

      if(leftSum == rightSum){
        System.out.println(i);
        return;
      }
    }
  }

  //https://www.geeksforgeeks.org/given-an-array-of-numbers-arrange-the-numbers-to-form-the-biggest-number/
  static void printLargestNumber(String[] a, int n){
    Arrays.sort(a, (x,y) -> {
      String xy = x+y;
      String yx = y+x;
      return xy.compareTo(yx);
    });
    for(int i = a.length-1; i >= 0; i--){
      System.out.print(a[i]);
    }
    System.out.println();

  }

  static void printMatrixDigonally(int[][] a){
    int r = a.length;
    int c = a[0].length;

    for(int i = 0; i < r; i++){
      int j = i;
      int k = 0;
      while(j >= 0){
        System.out.print(a[j][k]);
        j--;
        k++;
      }
      System.out.println();
    }
  }

  //https://practice.geeksforgeeks.org/problems/form-a-palindrome/0
  static int numberOfCharsToFormPalindrome(String s){
    //NOTE This is a wrong solution

    int count = 0;
    if(s == null || s.length() == 1){
      return count;
    }
    int[] charCount = new int[256];
    char[] a = s.toCharArray();

    for(char c : a){
      if(charCount[c] == 1){
        charCount[c]--;
        count--;
      }else{
        charCount[c]++;
        count++;
      }
    }

    if(count > 0) {
      count = count - 1;
    }
    return count;
  }


//http://javaconceptoftheday.com/diamond-pattern-program-in-java/
  public void printDiomand(int rows){
    int mid = rows/2;
    int starcount = 1;
    for(int i = mid; i > 0; i--){

      //print space
      for(int j = 1; j <= i; j++){
        System.out.print(" ");
      }

      //print star
      for(int j = 1; j <= starcount; j++){
        System.out.print("* ");
      }
      starcount++;
      System.out.println();
    }

    for(int i = 0; i <= mid; i++){
      //print space
      for(int j = 1; j <= i; j++){
        System.out.print(" ");
      }
      //print star
      for(int j = 1; j <= starcount; j++){
        System.out.print("* ");
      }
      starcount--;
      System.out.println();
    }
  }



  //https://practice.geeksforgeeks.org/problems/maximum-sum-increasing-subsequence/0
  static void printMaximumSumSubSequence(int[] a){

    int sum = a[0];
    List<Integer> sequence = new ArrayList<>();
    List<List<Integer>> l = new ArrayList<>();

    for(int i=0; i < a.length; i++){

      int currentSum = 0;

      List<Integer> ll = new ArrayList<>();
      ll.add(a[i]);

      for(int j = i+1; j < a.length; j++){
        //{1,101, 2, 3, 100, 4, 5,}
        if(a[j] < a[j-1]){
          //sequence is broken, calculate sum and add current sequence and start a new one
          //calculate sum
          currentSum = 0;
          for(int k : ll){
            currentSum = currentSum + k;
          }
          if(currentSum > sum){
            sum = currentSum;
            sequence = ll;
          }

          // start new sequencd
          l.add(ll);
          ll = new ArrayList<>();
          ll.add(a[i]);
        }

        if(a[j] > a[i]) {
          ll.add(a[j]);
        }
      }

      //add last sequence and calculate sum
      l.add(ll);

      //calculate sum
      currentSum = 0;
      for(int k : ll){
        currentSum = currentSum + i;
      }
      if(currentSum > sum){
        sum = currentSum;
        sequence = ll;
      }
    }

    System.out.println(sum);
    System.out.println(sequence);
  }

  //1 101 2 3 100 4 5
  static void printMaximumSumSubSequence1(int[] a){

    List<List<Integer>> subsequence = new ArrayList<>(); // there is no use of this array, I added it for see sequences
    int sum = 0;

    for(int i = 0; i < a.length; i++){
      List<Integer> l = new ArrayList<>();
      l.add(a[i]);

      for(int j = i+1; j< a.length; j++){

        if(a[j] > a[i] && a[j] > a[j-1]){
          l.add(a[j]);
        }else{

          // sequence is broken, calculate sum
          int currentSum = 0;
          for(int d : l){
            currentSum = currentSum + d;
          }

          // recaluclate max
          if(currentSum > sum){
            sum = currentSum;
          }

          //reset list
          subsequence.add(l);
          l = new ArrayList<>();
          l.add(a[i]);

          if(a[j] > a[i]){
            l.add(a[j]);
          }
        }
      }
      //When j ends current sum should be calculated again, thats why duplicate code here

      // sequence is broken, calculate sum
      int currentSum = 0;
      for(int d : l){
        currentSum = currentSum + d;
      }

      // recaluclate max
      if(currentSum > sum){
        sum = currentSum;
      }
    }
    System.out.println(sum);
  }

  //Kaden's algo
  //https://practice.geeksforgeeks.org/problems/kadanes-algorithm/0
  static void printMaxSubSequence(int[] a) {
    int sum = a[0];
    int currentSum = 0;

    for(int i=0; i < a.length; i++){
      currentSum = currentSum + a[i];

      if(currentSum > sum){
        sum = currentSum;
      }

      if(currentSum < 0){
        currentSum = 0;
      }
    }
    System.out.println(sum);
  }

  //Kaden's algo
  //https://practice.geeksforgeeks.org/problems/kadanes-algorithm/0
  static void printMaxSubSequenceWithIndexes(int[] a){
    int sum = a[0];
    int currentSum = 0;
    int s = 0;
    int l = 0;

    for(int i = 0; i < a.length; i++){
      currentSum = currentSum + a[i];

      if(currentSum > sum){
        sum = currentSum;
         l = i;
      }

      if(currentSum < 0){
        currentSum = 0;
        s = i+1;
      }
    }

  }



  //{1 2 3 7 5}
  //https://practice.geeksforgeeks.org/problems/subarray-with-given-sum/0
  static void subArrayWithGivenSum(int[] a, int k){
    for(int i = 0; i < a.length; i++){
      int sum = 0;
      int s = i;
      int e = -1;
      boolean found = false;
      for (int j = i; j < a.length; j++){
        sum = sum + a[j];
        if(sum == k){
          e = j;
          found = true;
          break;
        }
        if(sum > k){
          //no point in going forward
          break;
        }
      }
      if(found){
        s = s+1;
        e = e+1;
        System.out.println(s +","+ e);
        break;
      }
    }
  }

  //https://practice.geeksforgeeks.org/problems/maximum-of-all-subarrays-of-size-k/0
  //1 2 3 1 4 5 2 3 6
  static void printMaxOfAllSubArray(int[] a, int k){

    boolean noSubArrayPossible = false;
    for(int i = 0; i<a.length; i++){
      int l = -1;
      for(int j = i; j < i+3; j++){
        if(j >= a.length){
          noSubArrayPossible = true;
          break;
        }
        if(a[j] > l){
          l = a[j];
        }
      }
      if(noSubArrayPossible){
        break;
      }
      System.out.print(l+" ");
    }

    System.out.println();
  }

  //https://practice.geeksforgeeks.org/problems/reverse-array-in-groups/0
  static void reverseArrayInGroups(int[] a, int k){
    for(int i = 0; i < a.length; i = i+k){
      int s = i;
      int l = Math.min((i+k-1), a.length-1);

      while(s < l){
        int t = a[s];
        a[s] = a[l];
        a[l] = t;
        s++;
        l--;
      }
    }
    System.out.println(Arrays.toString(a));
  }


  //https://practice.geeksforgeeks.org/problems/kth-smallest-element/0
  //7 10 4 3 20 15
  static void printKthSmallestElement(int[] a, int k){
    Arrays.sort(a);
    if(k-1 < a.length-1){
      System.out.println(a[k-1]);
    }
  }

  //https://practice.geeksforgeeks.org/problems/chocolate-distribution-problem/0
  static void distributeChocklate(int[] a, int k){
    Arrays.sort(a);
    int minDiff = Integer.MAX_VALUE;
    int s = 0;
    int e = -1;
    for(int i = 0; i < a.length; i++){
      if(i+(k-1) >= a.length){
        break;
      }
      int diff = (a[(k-1)+i] - a[i]);
      if(  diff < minDiff){
        minDiff = diff;
        s = i;
        e = i+k-1;
      }
    }
    System.out.println(s+","+e);
    System.out.println(minDiff);
  }

  static void distributeChocklate1(int[] a, int k){
    Arrays.sort(a);
    int minDiff = Integer.MAX_VALUE;
    int s = 0;
    int e = a.length-1;
    for(int i = 0; i < a.length; i++){
      if( (i+k-1) >= a.length){
        break;
      }
      int diff = a[i+k-1] - a[i];
      if(diff < minDiff){
        s = i;
        e = i+k-1;
        minDiff = diff;
      }
    }
    System.out.println(s+","+e);
    System.out.println(minDiff);


  }

  //https://practice.geeksforgeeks.org/problems/convert-array-into-zig-zag-fashion/0
  //4 3 7 8 6 2 1
  static void zigzag(int[] a){
    boolean isEven = false;
    for(int i =0; i < a.length-1; i++){
      if(i%2 == 0){
        if(a[i] > a[i+1]){
          int t  = a[i];
          a[i] = a[i+1];
          a[i+1] = t;
        }
      }else{
        if(a[i] < a[i+1]){
          int t  = a[i];
          a[i] = a[i+1];
          a[i+1] = t;
        }
      }
    }
    System.out.println(Arrays.toString(a));
  }

  class ElementFrequency{
    int element;
    int frequency;
  }

  //{2, 3, 2, 4, 5, 12, 2, 3, 3, 3, 12}
  //https://practice.geeksforgeeks.org/problems/sorting-elements-of-an-array-by-frequency/0
  void sortElementsInFrequency(int[] a){

    Arrays.sort(a);
    List<ElementFrequency> l = new ArrayList<>();

    int e = a[0];
    int f = 1;

    ElementFrequency elementFrequency = new ElementFrequency();
    elementFrequency.element = e;

    for(int i = 1; i < a.length; i++){

      if(a[i] != a[i-1]){
        //different element
        elementFrequency.frequency = f;
        l.add(elementFrequency);
        elementFrequency = new ElementFrequency();
        elementFrequency.element = a[i];

        e = a[i];
        f = 1;
      }else{
        f++;
      }
    }
    //add last element
    elementFrequency.frequency = f;
    l.add(elementFrequency);

    l.sort( (e1, e2) -> {
      int d = e2.frequency - e1.frequency;
      if(d == 0){
        d = e1.element - e2.element;
      }
      return d;
    });

    int ai = 0;
    for(ElementFrequency ef : l){
      for(int k = 0; k < ef.frequency; k++)
      a[ai++] = ef.element;
    }

    System.out.println(Arrays.toString(a));

  }

  //https://www.youtube.com/watch?v=jH_5ypQggWg
  static void minimumNumberOfJumpsToReach(int[] a){

  }

  static void towerOfHanoi(int n, char from_rod, char to_rod, char aux_rod) {
    if (n == 1) {
      System.out.println("Move disk 1 from rod " +  from_rod + " to rod " + to_rod);
      return;
    }
    towerOfHanoi(n-1, from_rod, aux_rod, to_rod);
    System.out.println("Move disk " + n + " from rod " +  from_rod + " to rod " + to_rod);
    towerOfHanoi(n-1, aux_rod, to_rod, from_rod);
  }

  static void mergeTwoSortedArrays(int[] a, int[] b){
    int[] d = new int[a.length + b.length];

    int i = 0;
    int j = 0;
    int k = 0;

    while(i < a.length && j < b.length){
      if(a[i] < b[j]){
        d[k++] = a[i++];
      }else{
        d[k++] = b[j++];
      }
    }

    while(i < a.length){
      d[k++] = a[i++];
    }

    while(j < b.length){
      d[k++] = b[j++];
    }

    System.out.println(Arrays.toString(d));
  }

  //https://www.youtube.com/watch?v=5BI0Rdm9Yhk
  //int[] a = {9,12,15,17,25,28, 32, 37, 3, 5, 7, 8 };
  static int findElementInRotatedArray(int[] a, int k){
    //find pivot;
    int s = 0;
    int l = a.length-1;
    int p = findPivot(a);

    if(k > a[p] && k <= a[l]){
      s = p;
    }else{
      l = p-1;
    }

    //now do binary search
    while (s <= l){

      int m = (s+l)/2;

      if(k == a[m]){
        return m;
      }else if(k > a[m]){
        s = m+1;
      }else{
        l = m-1;
      }
    }

    return -1;
  }

  ////https://www.youtube.com/watch?v=5BI0Rdm9Yhk
  //int[] a = {9,12,15,17,25,28, 32, 37, 3, 5, 7, 8 };
  static int findPivot(int[] a){

    int s = 0;
    int l = a.length-1;

    while(s <= l ){

      int m = (s+l)/2;

      if(a[m] > a[m+1]){
        //this is the pivot
        return m+1;
      }else{
        if(a[s] > a[m]){
          l = m-1;
        }else{
          s = m+1;
        }
      }
    }
    return -1;
  }

  //{15,16,3,2,6,1,4}
  //https://www.youtube.com/watch?v=M0ffPDQ3cgY
  static void printLeader(int[] a){
    if(a.length < 1){
      return;
    }
    int l = a[a.length-1];
    System.out.print(l+",");
    for(int i = a.length-2; i >=0 ; i--){
      if(a[i] > l){
        l = a[i];
        System.out.print(a[i]+", ");
      }
    }
    System.out.println();
  }

  //https://www.youtube.com/watch?v=gf7vdIin0dk&index=6&list=PLeIMaH7i8JDjd21ZF6jlRKtChLttls7BG
  static void removeDulicates(int[] a){
    int prev = a[0];
    int s = 1;
    int i = 1;
    while(s < a.length){
      if(a[s] != prev){
        a[i++] = a[s];
        prev = a[s];
      }
      s++;
    }
    while(i < a.length){
      a[i++] = -1;
    }
    System.out.println(Arrays.toString(a));
  }

  //https://practice.geeksforgeeks.org/problems/equilibrium-point/0
  //https://www.youtube.com/watch?v=AjY2fFl58r8
  static int equilibriumPoint(int[] a){
    if(a.length == 1){
      return 1;
    }
    //find the sum
    int sum = 0;
    for(int i = 0; i < a.length; i++){
      sum = sum + a[i];
    }

    int leftSum = 0;
    for(int i = 0; i < a.length; i++){
      sum = sum - a[i];
      if(leftSum == sum){
        return i;
      }
      leftSum = leftSum + a[i];
    }
    return -1;
  }

  //https://www.geeksforgeeks.org/find-a-partition-point-in-array/
  static int findPartitionPoint(int[] a){
    int partitionPoint = -1;

    for(int i = 1; i < a.length-1; i++){

      boolean left = true;
      boolean right = true;

      //evaluate left
      for(int j = 0; j<i; j++){
        if(a[j] > a[i]){
          left = false;
          break;
        }
      }

      if(!left){
        //no point in evaluating right if left is not less than value
        continue;
      }
      //evaluate left
      for(int j = i+1; j < a.length; j++){
        if(a[j] <= a[i]){
          right = false;
          break;
        }
      }

      if(left && right){
        partitionPoint = i;
        break;
      }


    }
    return partitionPoint;
  }

  //https://www.geeksforgeeks.org/arrange-given-numbers-form-biggest-number-set-2/
  //https://www.programcreek.com/2014/02/leetcode-largest-number-java/
  //int[] largestNumber = {3,30,34,5,9};
  static void makeLargestNumberFromString(int[] a){
    //The idea here is to sort the strings not integers.
    //This problem can be solved by sorting strings, not sorting integer. Define a comparator to compare strings by concat() right-to-left or left-to-right

    String[] sa = new String[a.length];
    for(int i = 0; i < a.length; i++){
      sa[i] = String.valueOf(a[i]);
    }

    Arrays.sort(sa, (s1, s2) -> {
      return (s2+s1).compareTo(s1+s2);
    });

    System.out.println(ArrayUtils.toString(sa));

  }

  static void isStringRotatedByTwoPlaces(String s1, String s2){
    //s1 = "amazon";
    //s2 = "onamaz";
    String s = s2+s2;

    if(s.contains(s1)){
      int start = s.indexOf(s1);
      if(start == 2){
        System.out.println(true);
      }else if(start+s1.length()+2 == s.length()){ // OR it could be s1.lenght - start == 2
        System.out.println(true);
      }else{
        System.out.println(true);
      }
    }
  }


  //https://www.youtube.com/watch?v=BOt1DAvR0zI&index=10&list=PLeIMaH7i8JDjd21ZF6jlRKtChLttls7BG
  //int[] s012 = {0,1,1,0,1,2,1,0,0,0,1};
  static void segregate012(int[] a){
    int s = 0;
    int l = a.length -1;
    int pointer = 0;
    while (pointer <= l) { //IMPORTANT: complere with l NOT a.length
      if(a[pointer] == 0){

        int t = a[s];
        a[s] = a[pointer];
        a[pointer] = t;
        s++;
        pointer++;

      }else if(a[pointer] == 1){

        pointer++;

      }else if(a[pointer] == 2){

        int t = a[l];
        a[l] = a[pointer];
        a[pointer] = t;
        l--;
        //IMPORTANT - dont increase the pointer
        //pointer++;
      }
    }
    System.out.println(ArrayUtils.toString(a));

  }

  //https://practice.geeksforgeeks.org/problems/recursively-remove-all-adjacent-duplicates/0
  static void removeAdjacentDuplicates(String s){
    char[] sa = s.toCharArray();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < sa.length-1; i++){
      if(sa[i] == sa[i+1]){
        int k = i+1;
        while(k < sa.length && sa[i] == sa[k] ){
          k++;
        }
        i = k-1;
      }else{
        sb.append(sa[i]);
      }
    }
    System.out.println(sb);
  }

  //int[] a = {9,12,15,17,25,28,32,37,3,5,7,8};

  static int findElementInARotatedArray(int[] a, int k){
    int s = 0;
    int l = a.length-1;
    int p = findPivotInRotatedArray(a);
    if(p != -1){
      if(k > a[p] && k <= a[l]){
        s = p;
      }else{
        l = p-1;
      }
    }

    while(s <= l){
      int m = (s+l)/2;
      if(a[m] == k){
        return m;
      }else if(k > a[m]){
        s = m+1;
      }else{
        l = m-1;
      }
    }

    return -1;
  }

  static int findPivotInRotatedArray(int[] a){
    int s = 0;
    int l = a.length-1;

    while(s <= l){
      int m = (s+l)/2;
      if(a[m] > a[m+1]){
        //this is the place ordered reversed
        return m+1;
      }else if(a[s] > a[m]){
        l = m-1;
      }else{
        s = m+1;
      }
    }
    return -1;
  }



  public static void main(String[] args){
    System.out.println(SA.isAllCharsUnique1("This"));
    /*System.out.println(SA.isAllCharsUnique1("Thismaynotberu"));
    System.out.println(SA.isAllCharsUnique2("This"));
    System.out.println(SA.isAllCharsUnique2("Thismaynotberu"));*/

    System.out.println(SA.removeDuplicate("this is a test, lets see"));
    //System.out.println(SA.removeDuplicate1("this is a test, lets see"));

    System.out.println(SA.isAnnagram("motherinlaw1", "hitlerwoman"));

    System.out.println(Arrays.toString(SA.reverseArrayInGroup(335, 36)));

    SA.fillSpace("Do it today instead of stalling it");

    int[] rotatedArray = {9,12,15,17,25,28,32,37,3,5,7,8};
    System.out.println(SA.findElementInARotatedArray(rotatedArray, 8));
    System.out.println(SA.findPivotInRotatedArray(rotatedArray));

    SA.removeAdjacentDuplicates("geeksforgeek");
    SA.removeAdjacentDuplicates("acaaabbbacdddd");

    int[] s012 = {0,1,1,0,1,2,1,2,0,0,0,1};
    SA.segregate012(s012);

    int[] largestNumber = {3,30,34,5,9};
    SA.makeLargestNumberFromString(largestNumber);


    SA.isStringRotatedByTwoPlaces("amazon", "azonam");
    SA.isStringRotatedByTwoPlaces("amazon", "onamaz");

    int[] ra = {9,12,15,17,25,28, 32, 37, 3, 5, 7, 8 };
    System.out.println(SA.findPivot(ra));
    System.out.println(SA.findElementInRotatedArray(ra, 5));
    System.out.println(SA.findElementInRotatedArray(ra, 8));
    System.out.println(SA.findElementInRotatedArray(ra, 9));
    System.out.println(SA.findElementInRotatedArray(ra, 25));
    System.out.println(SA.findElementInRotatedArray(ra, 90));

    int[] leader = {15,16,3,2,6,1,4};
    SA.printLeader(leader);

    int[] dulicates = {1,2,2,3,3,3,4,4};
    SA.removeDulicates(dulicates);

    int[] array1 = {1,3,5,7,9};
    int[] array2 = {2,4,8,10,11,12,16};
    SA.mergeTwoSortedArrays(array1,array2);

    SA.towerOfHanoi(4, 'A', 'C', 'B');

    System.out.println(SA.numberOfCharsToFormPalindrome("abcd"));
    System.out.println(SA.numberOfCharsToFormPalindrome("anasdad"));

    int[] sumSubsequence = {1,101, 2, 3, 100, 4, 5,};

    SA.printMaximumSumSubSequence(sumSubsequence);
    SA.printMaximumSumSubSequence1(sumSubsequence);

    int sumInAnArray[] = {1, 2, 3, 7, 5};
    SA.subArrayWithGivenSum(sumInAnArray, 12);
    int[] sumInAnArray1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    SA.subArrayWithGivenSum(sumInAnArray1, 15);

    int[] findMaxSubArrayElement = {1, 2, 3, 1, 4, 5, 2, 3, 6};
    int[] findMaxSubArrayElement1 = {8, 5, 10, 7, 9, 4, 15, 12, 90, 13};
    SA.printMaxOfAllSubArray(findMaxSubArrayElement, 3);
    SA.printMaxOfAllSubArray(findMaxSubArrayElement1, 4);

    int[] maxSubsequence = {1, 2, 3}; //kadens algorithim
    SA.printMaxSubSequence(maxSubsequence);
    int[] maxSubsequence1 = {-1, -2, -3, -4};
    SA.printMaxSubSequence(maxSubsequence1);

    int[] reverseArraInGroups = {1, 2, 3, 4, 5};
    SA.reverseArrayInGroups(reverseArraInGroups, 3);

    int[] kSmallestElement = {7, 10, 4, 3, 20, 15};
    SA.printKthSmallestElement(kSmallestElement, 3);
    int[] kSmallestElement1 = {7, 10, 4, 20, 15};
    SA.printKthSmallestElement(kSmallestElement1, 4);

    int[] distributeChoclate = {3, 4, 1, 9, 56, 7, 9, 12};
    SA.distributeChocklate(distributeChoclate, 5);
    int[] distributeChoclate1 = {3, 4, 1, 9, 56, 7, 9, 12};
    distributeChocklate1(distributeChoclate1, 5);
    int[] distributeChoclate2 = {7, 3, 2, 4, 9, 12, 56};
    distributeChocklate1(distributeChoclate2, 3);

    int[] zigzag = {4, 3, 7, 8, 6, 2, 1};
    SA.zigzag(zigzag);

    int[] equilibriumPoint = {1, 3, 5, 2, 2};
    System.out.println(SA.equilibriumPoint(equilibriumPoint));

    int pp[] = {4, 3, 2, 5, 8, 6, 7};
    System.out.println(SA.findPartitionPoint(pp));
    int pp1[] = {5, 6, 2, 8, 10, 9, 8};
    System.out.println(SA.findPartitionPoint(pp1));

    int[] sortElementsInFrequency = {2, 3, 2, 4, 5, 12, 2, 3, 3, 3, 12};
    SA sa = new SA();
    sa.sortElementsInFrequency(sortElementsInFrequency);

    int[] fs = {1,2,3,7,5};
    int[] fs1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    SA.findSum(fs1, 15);

    int[] ea = {1,3,5,2,2};
    SA.findEq(ea);

    //3, 4, 1, 9, 56, 7, 9, 12
    //1,3,4,7,9,9,12,56
    //7, 3, 2, 4, 9, 12, 56
    //2, 3, 4, 7, 9, 12, 56

    int[][] dm = {
        {1,2,3,4,5,6},
        {7,8,9,10,11,13},
        {13,12,13,14,15,16},
        {21,22,23,20,25,26},
        {31,32,78,34,35,36},
        {40,42,43,44,45,46},
    };

    SA.printMatrixDigonally(dm);


    int[][] matrix = {
                      {1,2,3,4,5,6},
        {1,2,3,4,5,6},
        {1,2,3,4,5,6},
        {1,2,3,0,5,6},
        {1,2,3,4,5,6},
        {0,2,3,4,5,6},
    };


    int arrangeArray[] = {-1, -1, 6, 1, 9, 3, 2, -1, 4, -1};
    SA.arrangeArray1(arrangeArray);
    int arrangeArray1[] = {19, 7, 0, 3, 18, 15, 12, 6, 1, 8, 11, 10, 9, 5, 13, 16, 2, 14, 17, 4};
    SA.arrangeArray1(arrangeArray1);

    int arr[][] = {
        {11, 2, 4},
        {4, 5, 6},
        {10, 8, -12}
    };

    System.out.println(SA.timeConversion("06:40:03AM"));

    SA.updateMatrix(matrix);
    SA.reverse();
    int[] a = {1,2,3,4, 5, 6, 7};
    SA.rotateArray(a , 5);
    System.out.println(Arrays.toString(a));
    int[] b = {1,2,3,4, 5, 6, 7};
    SA.rotateArray1(b , 5);
    System.out.println(Arrays.toString(b));

    System.out.println("Left Rotate");

    int[] c = {1,2,3,4, 5, 6, 7};
    SA.leftRotate(c , 5);
    System.out.println(Arrays.toString(c));
    int[] c2 = {1,2,3,4, 5, 6, 7};
    SA.leftRotate2(c2 , c2.length, 5);
    System.out.println(Arrays.toString(c2));

    System.out.println(SA.minimumIntegerToMultiplyToGetSum(36));
    System.out.println(SA.concatStaring("45eeeedfghfffeff62"));
    SA.printDigonal();

    int matrix1 [][] = {
        {10, 20, 30, 40},
        {15, 25, 35, 45},
        {27, 29, 37, 48},
        {32, 33, 39, 50}};

    System.out.println(SA.find(matrix1, 50));

    SA.findAngle(12,30);
    SA.findAngle(3,30);

    //SA.printKthprimeFactor(225, 2);
    //SA.printKthprimeFactor(31, 1);

    SA.printPrimeFactor(30);

    final int[][] paths = {
        {0,0,1,0,1},
        {0,0,1,0,0},
        {0,0,0,0,1},
        {0,1,0,0,1},
        {0,1,1,1,0}

    };

    System.out.println(SA.solveMaze(paths,0,0,4,4));

    //SA.permute("ABCD");
    //SA.compute("ABCD", 0);

    SA.totalParenthesis(new char[6], 3, 0, 0, 0);

  }



}
