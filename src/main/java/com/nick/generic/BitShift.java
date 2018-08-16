package com.nick.generic;

public class BitShift {
	
	public static void main(String[] args){
		/*int i = 8;
		System.out.println(i << 1);
		i = -2;
		System.out.println(i >> 1);
		System.out.println(i >>> 1);
		
		*/
		int a = 18;
		int b = 12;
		
		/*
		//^ is a sum of 2 operators		
		System.out.println((a ^ b));
		
		//~Bitwise unary operator
		a = 10; 
		System.out.println("~"+ (~a));
		*/
		
		//& carry of 2 bits
		a = 6;
		b = 1;
		System.out.println(a & b);
		System.out.println(a ^ b);
		
		/*System.out.println(Integer.toBinaryString(1));
		System.out.println(Integer.toBinaryString(2));
		System.out.println(Integer.toBinaryString(3));
		System.out.println(Integer.toBinaryString(4));
		System.out.println(Integer.toBinaryString(10));
		System.out.println(Integer.toBinaryString(11));
		System.out.println(Integer.toBinaryString(12));
		System.out.println(Integer.toBinaryString(13));
		System.out.println(Integer.toBinaryString(14));
		System.out.println(Integer.toBinaryString(15));
		System.out.println(Integer.toBinaryString(16));*/
		
		//System.out.println(mult(2,6));
		int m = ~0;
		System.out.println(m);
		int left = m - (1 << 6 -1);
		int right = m - (1 << 2 -1);
	}
	
	public static int mult(int a, int b)
    {
        int r = 0;
        while (b != 0)
        {
            int temp = b & 1;

            if (temp!= 0)
            {
                r = r + a;
            }
            a= a << 1;
            b= b >> 1;
        }

        return r;
    }

}
