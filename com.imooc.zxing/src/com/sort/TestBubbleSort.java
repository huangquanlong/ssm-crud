package com.sort;

import java.util.LinkedList;
import java.util.List;

class BubbleSort {
	public static void bubbleSort(DataWrap[] data){
		for(int i=0;i<data.length-1;i++){
			boolean flag=false;
			for(int j=0;j<data.length-1-i;j++){
				if(data[j].compareTo(data[j+1]) > 0 ){
					DataWrap temp=data[j+1];
					data[j+1]=data[j];
					data[j]=temp;
					flag=true;
				}
			}
			System.out.println(java.util.Arrays.toString(data));
			if (!flag) {
				break;
			}
		}
	}
}

 
 public class TestBubbleSort{
	 public static void main(String[] args) {
//		 DataWrap[] data = { new DataWrap(25, ""), new DataWrap(01, ""),
//					new DataWrap(8, "*"), new DataWrap(-8, ""),
//					new DataWrap(23, ""), new DataWrap(100, ""),
//					new DataWrap(56, ""), new DataWrap(-100, "*"),
//					new DataWrap(20, "")};
//			System.out.println("排序之前：\n" + java.util.Arrays.toString(data));
//			BubbleSort.bubbleSort(data);
//			System.out.println("排序之后：\n" + java.util.Arrays.toString(data));
//			List list=new LinkedList<>();
//			list.add("01");
//			list.add("02");
//			list.add("03");
//			list.add("04");
//			list.add("05");
//			System.out.println(list);
//			System.out.println(list.size());
//			for(int i=0;i<list.size();i++){
//				if("02".equals(list.get(i))){
//					list.remove("02");
//				}
//			}
//			System.out.println(list);
//			System.out.println(list.size());
//		 System.out.println(TestBubbleSort.total(100));
//		 System.out.println(TestBubbleSort.accumulate(10));
		 System.out.println(TestBubbleSort.plus(100));
	}
//	 public static long total(int n) {  
//	        if(1 == n) {  
//	            return n;  
//	        }else {  
//	            return total(n-1) + n;  
//	        }  
//	    } 
	 public static long plus(int n) {  
	        if(1 == n) {  
	            return n;  
	        }else {  
	            return  plus(n-1) - n ;  
	        }  
	    } 
//	 public static long accumulate(int n) {   
//	        if(1 == n) {  
//	            return n;  
//	        }else {  
//	            return accumulate(n-1) * n;  
//	        }  
//	    } 
 }