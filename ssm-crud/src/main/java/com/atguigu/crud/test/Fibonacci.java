package com.atguigu.crud.test;

public class Fibonacci {

	public static void main(String[] args) {
		int num1=1 ,num2=1;
		int  result=0;
		System.out.print(num1+" ");
		System.out.print(num2+" ");
		for(int i=2 ; i< 13 ; i++){
			result=num1+num2;
			num1=num2;
			num2=result;
			System.out.print(result+" ");
		}
	}

}
