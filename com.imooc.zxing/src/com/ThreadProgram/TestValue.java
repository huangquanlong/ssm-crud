package com.ThreadProgram;

import java.math.BigDecimal;

public class TestValue {

	public static void main(String[] args) {
		BigDecimal bg1=new BigDecimal("165516416341561");
		BigDecimal bg2=new BigDecimal("165516416341561");
		BigDecimal sum=bg1.multiply(bg2);
		System.out.println(sum);
		
/*		String a=new String("A");
		String b=new String("B");
		oprator(a, b);
		System.out.println(a+","+b);
	}
	
	static void oprator(String x ,String y){
		x="AB";
		y=x;
		System.out.println(x+"******,"+y);*/
		Value V=new Value();
		V.i=5;
		V.j=15;
//		System.out.println("调用方法前："+V.i+" "+ V.j);
//		oprator(V);
//		System.out.println("调用方法后："+V.i+" "+ V.j);
//	}	
//		static void oprator(Value V){
//			V.i=10;
//			V.j=20;
//			System.out.println(V.i+"******,"+V.j);
		}
}

class Value{
	int i=10;
	int j=20;
}


